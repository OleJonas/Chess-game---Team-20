package Game;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer{

    private Timer timer;
    private final int increment;
    private int gameTime;
    private int interval;


    public GameTimer(int gameTime, int increment){
        this.gameTime = gameTime;
        this.increment = increment;
        this.timer = new Timer();
        this.interval = gameTime;
        System.out.println(interval);
        //this.startTime = System.currentTimeMillis();
    }

    public GameTimer(int gameTime){
        this.gameTime = gameTime;
        this.increment = 0;
        this.timer = new Timer();
        this.interval = gameTime;
        System.out.println(interval);
        //this.startTime = System.currentTimeMillis();
    }

    public int getGameTime(){
        return gameTime;
    }

    public void clock(){
        this.timer = new Timer();
        int delay = 1000;
        int period = 1000;
        System.out.println(interval);
        timer.scheduleAtFixedRate(new TimerTask(){
            public void run(){
                gameTime = setInterval();
                System.out.println(gameTime);
            }
        }, delay, period);
    }

    private int setInterval(){
        if(interval == 1){
            timer.cancel();
            //System.out.println("Tiden er ute!");
        }
        return --interval;
    }

    public static void main(String[] args){
        GameTimer timer = new GameTimer(20);
        timer.clock();
    }
}