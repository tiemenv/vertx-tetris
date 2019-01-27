package game;

public class Timer {
    private int minutes;
    private int seconds;
    private int ten = 10;

    public Timer() {
        this.minutes = 2;
        this.seconds = 0;
    }

    public Timer(int seconds) {
        this.minutes = 0;
        this.seconds = seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void removeSecond() {
        if (minutes >= 0) {
            if (seconds > 0) {
                seconds--;
            } else if (minutes > 0) {
                minutes--;
                seconds = 59;
            }
        }
    }

    @Override
    public String toString() {
        if (this.seconds < ten) {
            return this.minutes + ":0" + this.seconds;
        }
        return this.minutes + ":" + this.seconds;
    }
}
