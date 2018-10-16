call run_clean.bat
javac MyBot.java
javac StarterBot.java
halite.exe --replay-directory replays/ -vvv --width 32 --height 32 "java MyBot" "java StarterBot"
rm *.class
cp replays/*.hlt ./