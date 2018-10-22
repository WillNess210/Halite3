import subprocess
import json


def compile_v6():
    p = subprocess.Popen(
        'cd .. &'
        'call otherbots/V6/compile.bat &',
        shell=True,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE)


def compile_my_bot():
    p = subprocess.Popen(
        'cd .. &'
        'call compile.bat &',
        shell=True,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE)

def run_match():
    p = subprocess.Popen(
        'cd .. &'
        'halite.exe --no-logs --replay-directory gym/replays/ --results-as-json "java MyBot" "java -classpath otherbots/V6 MyBot',
        shell=True,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE)
    return json.loads(p.stdout.read())


def get_match_winner(results):
    if results['stats']['0']['rank'] == 1:
        return '0'
    elif results['stats']['1']['rank'] == 1:
        return '1'
    return '-1'


def get_player_score_str(results, player):
    return str(get_player_score(results, player))


def get_player_score(results, player):
    return results['stats'][player]['score']


def get_map_dim_str(results):
    return str(get_map_dim(results)) + "x" + str(get_map_dim(results))


def get_map_dim(results):
    return results['map_height']


def print_match_stats(results):
    result = "Winner: "
    result = result + get_match_winner(results) + ' | '
    result = result + get_player_score_str(results, '0') + " - " + get_player_score_str(results, '1') + ' | '
    result = result + get_map_dim_str(results)
    print(result)


compile_v6()
compile_my_bot()
for i in range(0, 10):
    print_match_stats(run_match())



