cd C:\Users\WillN\Documents\College\CompetitiveProgramming\Halite3\Halite3Round2
call run_clean.bat
javac MyBot.java
javac StarterBot2.java
halite.exe --replay-directory replays/ -vvv --width 32 --height 32 "java MyBot" "java StarterBot2" "java StarterBot2" "java StarterBot2"
rm *.class
cp replays/*.hlt ./
ren *.hlt replay.hlt
C:\Users\WillN\Documents\College\CompetitiveProgramming\Halite3\fluorine\Fluorine.exe replay.hlt