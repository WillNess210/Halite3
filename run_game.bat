cd C:\Users\WillN\Documents\College\CompetitiveProgramming\Halite3\Halite3_Java_Windows-x86
call run_clean.bat
javac MyBot.java
javac StarterBot2.java
halite.exe --replay-directory replays/ -vvv --width 32 --height 32 "java MyBot" "java StarterBot2"
rm *.class
cp replays/*.hlt ./