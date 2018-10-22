import subprocess
import json
import datetime

def compile_bot(bot):
    subprocess.Popen(
        'cd ../' + bot + ' &'
        'call compile.bat',
        shell=True,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE)


def run_match(bot1, bot2):
    bot1java = ""
    if len(bot1) == 0:
        bot1java = "java MyBot"
    else:
        bot1java = "java -classpath " + bot1 + " MyBot"
    bot2java = ""
    if len(bot2) == 0:
        bot2java = "java MyBot"
    else:
        bot2java = "java -classpath " + bot2 + " MyBot"
        # 'halite.exe --replay-directory gym/replays/ --results-as-json "' + bot1java + '" "' + bot2java + '"',
    p = subprocess.Popen(
        'cd .. &'
        'halite.exe --no-replay --no-logs --results-as-json "' + bot1java + '" "' + bot2java + '"',
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


# BOTS
myBot = ""
v6 = "otherbots/V6"
starter_bot = "otherbots/StarterBot"
# CHOOSE BOTS TO RUN WITH
BOT1 = myBot
BOT2 = v6
# COMPILING

compile_bot(BOT1)
compile_bot(BOT2)

# RUNNING & COLLECTING DATA
total_games = 0
games = [0, 0, 0, 0, 0]
wins = [[0, 0], [0, 0], [0, 0], [0, 0], [0, 0]]  # 32, 40, 48, 56, 64
scores = [[0, 0], [0, 0], [0, 0], [0, 0], [0, 0]] # 32, 40, 48, 56, 64
while datetime.datetime.now().hour < 8 or datetime.datetime.now().minute < 15:
    results = run_match(BOT1, BOT2)
    total_games = total_games + 1
    print("GAME " + str(total_games))
    print_match_stats(results)
    map_size = get_map_dim(results)
    winner = int(get_match_winner(results))
    access = int((map_size - 32) / 8)
    games[access] = games[access] + 1
    wins[access][winner] = wins[access][winner] + 1
    scores[access][0] = scores[access][0] + get_player_score(results, '0')
    scores[access][1] = scores[access][1] + get_player_score(results, '1')
    print("Wins")
    print("Total: " + str(wins[0][0] + wins[1][0] + wins[2][0] + wins[3][0] + wins[4][0]) + " v " + str(wins[0][1] + wins[1][1] + wins[2][1] + wins[3][1] + wins[4][1]))
    for i in range(0, len(wins)):
        print_map_size = 32 + (i * 8)
        print(str(print_map_size) + ": " + str(wins[i][0]) + " v " + str(wins[i][1]))
    print("Avg")
    for i in range(0, len(scores)):
        print_map_size = 32 + (i * 8)
        numgames = games[i]
        if numgames == 0:
            numgames = 1
        print(str(print_map_size) + ": " + str(int(scores[i][0]/numgames)) + " v " + str(int(scores[i][1]/numgames)))

print("=========")
print("FINISHED AT " + str(datetime.datetime.now()))
print("Games ran: " + str(total_games))
print("Wins")
print("Total: " + str(wins[0][0] + wins[1][0] + wins[2][0] + wins[3][0] + wins[4][0]) + " v " + str(wins[0][1] + wins[1][1] + wins[2][1] + wins[3][1] + wins[4][1]))

for i in range(0, len(wins)):
    print_map_size = 32 + (i * 8)
    print(str(print_map_size) + ": " + str(wins[i][0]) + " v " + str(wins[i][1]))
print("Avg")
for i in range(0, len(scores)):
    print_map_size = 32 + (i * 8)
    numgames = games[i]
    if numgames == 0:
        numgames = 1
    print(str(print_map_size) + ": " + str(int(scores[i][0] / numgames)) + " v " + str(int(scores[i][1] / numgames)))


