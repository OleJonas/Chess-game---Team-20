package Game;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer{

    private Timer timer;
    private final int increment;
    //private int gameTime;
    private int interval;
    private boolean end = false;

    public GameTimer(int gameTime, int increment){
        this.increment = increment;
        this.timer = new Timer();
        this.interval = interval;
        //System.out.println(interval);
        //this.startTime = System.currentTimeMillis();
    }

    public GameTimer(int interval){
        this.interval = interval;
        this.increment = 0;
        this.timer = new Timer();
        //System.out.println(interval);
        //this.startTime = System.currentTimeMillis();
    }

    public int getGameTime(){
        return interval;
    }

    public void clock(){
        this.timer = new Timer();
        int delay = 1000;
        int period = 1000;
        System.out.println(interval);
        timer.scheduleAtFixedRate(new TimerTask(){
            public void run(){
                setInterval();
                if(interval > 0) {
                    System.out.println(interval);
                }
            }
        }, delay, period);
    }

    private void setInterval(){
        // Can swap this interval variable with a boolean so that the timer keeps going as long as one wants it to.
        // One could also add another attribute to this class so that one gets a separate gameTime attribute.
        if(end){
            timer.cancel();
            //System.out.println("Tiden er ute!");
        }
        if(interval > 0){
            --interval;
        }
    }

    public static void main(String[] args){
        GameTimer timer = new GameTimer(20);
        timer.clock();
    }
}