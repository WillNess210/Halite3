cd C:\Users\WillN\Documents\College\CompetitiveProgramming\Halite3\Halite3Round2
call run_clean.bat
javac MyBot.java
//halite.exe --replay-directory replays/ -vvv "java MyBot" "java -classpath otherbots/StarterBot StarterBot2"
halite.exe --replay-directory replays/ -vvv "java MyBot 55" "java -classpath otherbots/V6 MyBot"
rm *.class
cp replays/*.hlt ./
ren *.hlt replay.hlt
C:\Users\WillN\Documents\College\CompetitiveProgramming\Halite3\fluorine\Fluorine.exe replay.hlt