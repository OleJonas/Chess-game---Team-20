package Game;

import java.lang.reflect.Method;
import java.util.TimerTask;
import java.util.Timer;

import javafx.geometry.Pos;

import static javafx.application.Application.launch;

// Will probably need to run this in a separate thread since it will be running in parallell with the program.
public class GameTimer{

    private Timer timer;
    private final int increment;
    private int interval;
    private boolean end = false;
    //private int lastChat;

    public GameTimer(int interval, int increment){
        this.increment = increment;
        this.timer = new Timer(true);
        this.interval = interval;
    }

    public GameTimer(int interval){
        this.interval = interval;
        this.increment = 0;
        this.timer = new Timer(true);
    }


    // For use with database and chat functions


    public int getTime(){
        return interval;
    }

    // Using java.util.Timer to schedule events at a fixed rate.
    public void clock(){
        int delay = 1000;
        int period = 1000;
        System.out.println(interval);
        timer.scheduleAtFixedRate(new TimerTask(){
            public void run(){
                if(interval > 0){
                    setInterval();
                    System.out.println(interval);
                }
            }
        }, delay, period);
    }


    // Fetched from https://stackoverflow.com/questions/22378422/how-to-use-timertask-with-lambdas
    public void schedule(final Runnable r, int delay, int period){
        final TimerTask task = new TimerTask(){
            public void run(){
                r.run();
            }
        };
        timer.scheduleAtFixedRate(task, delay, period);
        //return task;
    }



    // Private method for use in clock() method.
    // Keeps track of game time, while also making scheduled database lookups possible. (hopefully...)
    private void setInterval(){
        if(end){
            timer.cancel();
        }
        if(interval > 0){
            --interval;
        }
    }

    public void endTimer(){
        this.end = true;
    }

    public void incrementTimer(){
        if(interval > 0) {
            this.interval += increment;
        } else{
            System.out.println("Time's up!");
        }
    }
}