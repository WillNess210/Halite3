import subprocess
import json
import multiprocessing
import argparse


def bot(arg):
    return [x for x in arg.split(',')]


class Gym():
    def __init__(self, number_of_runs, number_of_players, bots, enable_replays,
                 mp, width, height, disable_timeouts, constant_seed):
        self.number_of_runs = number_of_runs
        self.bots = bots
        if number_of_players is not None:
            if number_of_players < len(bots):
                self.number_of_players = None
        else:
            self.number_of_players = number_of_players
        self.enable_replays = enable_replays
        self.mp = mp
        self.width = width
        self.height = height
        self.disable_timeouts = disable_timeouts
        self.constant_seed = constant_seed
        self.seed = None
        self.stats = {'Wins': {}, 'Halite': {}}

    def run_game(self):
        process_command = './halite --results-as-json'
        if not self.enable_replays:
            process_command += ' --no-replay'
        else:
            process_command += ' --replay_directory replays/'
        if self.disable_timeouts:
            process_command += ' --no-timeouts'
        if self.width and self.height:
            process_command += ' --width {} --height {}'.format(
                self.width, self.height)
        if self.number_of_players:
            process_command += ' -n {}'.format(self.number_of_players)
        if self.constant_seed:
            process_command += ' -s {}'.format(self.constant_seed)
        for b in self.bots:
            process_command += ' "{} {}"'.format(b[0], b[1])
        p = subprocess.Popen(
            process_command,
            shell=True,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE)
        p.daemon = 1
        output = p.stdout.read()
        output = json.loads(output)

        return output

    def run_gym(self):
        if self.mp:
            pool = multiprocessing.Pool(multiprocessing.cpu_count())
            if self.constant_seed:
                output = self.run_game()
                if self.constant_seed and self.seed is None:
                    self.seed = output["map_seed"]
                    self.width = output["map_width"]
                    self.height = output["map_height"]
            for i in range(self.number_of_runs):
                output = pool.apply_async(self.run_game).get()
                if (output["map_width"] *
                        output["map_height"]) not in self.stats['Halite']:
                    self.stats['Halite'][output["map_width"] *
                                         output["map_height"]] = [
                                             1, output['stats']['0']['score']
                                         ]
                else:
                    self.stats['Halite'][output["map_width"] *
                                         output["map_height"]][0] += 1
                    self.stats['Halite'][output["map_width"] * output[
                        "map_height"]][1] += output['stats']['0']['score']
                win = 0
                if output['stats']['0']['rank'] == 1:
                    win = 1
                if (output["map_width"] *
                        output["map_height"]) not in self.stats['Wins']:
                    self.stats['Wins'][output["map_width"] *
                                         output["map_height"]] = [
                                         1, win
                                     ]
                else:
                    self.stats['Wins'][output["map_width"] *
                                         output["map_height"]][0] += 1
                    self.stats['Wins'][output["map_width"] *
                                         output["map_height"]][0] += win
                print("finished game {}".format(i), end='\r')
            pool.close()
            pool.join()
        else:
            for i in range(self.number_of_runs):
                output = self.run_game()
                print("finished game {}".format(i), end='\r')
                if self.constant_seed and self.seed is None:
                    self.seed = output["map_seed"]
                    self.width = output["map_width"]
                    self.height = output["map_height"]
                if (output["map_width"] *
                        output["map_height"]) not in self.stats['Halite']:
                    self.stats['Halite'][output["map_width"] *
                                         output["map_height"]] = [
                                             1, output['stats']['0']['score']
                                         ]
                else:
                    self.stats['Halite'][output["map_width"] *
                                         output["map_height"]][0] += 1
                    self.stats['Halite'][output["map_width"] * output[
                        "map_height"]][1] += output['stats']['0']['score']
                win = 0
                if output['stats']['0']['rank'] == 1:
                    win = 1
                if (output["map_width"] *
                        output["map_height"]) not in self.stats['Wins']:
                    self.stats['Wins'][output["map_width"] *
                                         output["map_height"]] = [
                                         1, win
                                     ]
                else:
                    self.stats['Wins'][output["map_width"] *
                                         output["map_height"]][0] += 1
                    self.stats['Wins'][output["map_width"] *
                                         output["map_height"]][0] += win


        if not self.stats['Wins']:
            print("Your bot won 0 games :(")
        else:
            for key in self.stats['Wins']:
                print("Win rate on {} size maps".format(key),
                      self.stats['Wins'][key][1] / self.stats['Wins'][key][0])
            for key in self.stats['Halite']:
                print("Halite per game on {} size maps".format(key),
                      self.stats['Halite'][key][1] / self.stats['Halite'][key][0])


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "-n",
        "--number_of_runs",
        default=1,
        type=int,
        help="how many games you want to run")
    parser.add_argument(
        "-p",
        "--number_of_players",
        type=int,
        help="create a map supporting p players (even) if using just one bot")
    parser.add_argument(
        "-b",
        "--bots",
        nargs='+',
        type=bot,
        help="bots to use, in language,botname pairs")
    parser.add_argument(
        "-r",
        "--enable_replays",
        action="store_true",
        help="enables replays, off by default to make the game run faster")
    parser.add_argument(
        "-m",
        "--multiprocessing",
        action="store_true",
        help=
        "enables multiprocessing, off by default, WARNING: will use all your cores"
    )
    parser.add_argument(
        "--width", type=int, default=None, help="map width, random by default")
    parser.add_argument(
        "--height",
        type=int,
        default=None,
        help="map height, random by default")
    parser.add_argument(
        "-t",
        "--disable_timeouts",
        action="store_true",
        help=
        "disable timeouts, timeouts enabled by default, WARNING: will useful sometimes for testing this is dangerous, if your bot hangs indefinitely this script will hang indefinitely."
    )
    parser.add_argument(
        "-c",
        "--constant_seed",
        action="store_true",
        help="plays all maps on the same seed, off by default")
    args = parser.parse_args()

    gym = Gym(args.number_of_runs, args.number_of_players, args.bots,
              args.enable_replays, args.multiprocessing, args.width,
              args.height, args.disable_timeouts, args.constant_seed)

    gym.run_gym()
