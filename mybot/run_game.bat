cd C:\Users\WillN\Documents\College\CompetitiveProgramming\Halite3\Halite3Round2
call run_clean.bat
cd C:\Users\WillN\Documents\College\CompetitiveProgramming\Halite3\Halite3Round2\mybot
call compile.bat
cd C:\Users\WillN\Documents\College\CompetitiveProgramming\Halite3\Halite3Round2\otherbots\V6
call compile.bat
cd C:\Users\WillN\Documents\College\CompetitiveProgramming\Halite3\Halite3Round2
halite.exe --replay-directory replays/ -vvv "java -classpath mybot MyBot 0.5" "java -classpath otherbots/V6 MyBot"
rm *.class
cp replays/*.hlt ./
ren *.hlt replay.hlt
C:\Users\WillN\Documents\College\CompetitiveProgramming\Halite3\fluorine\Fluorine.exe replay.hlt