package Game;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.TimerTask;
import java.util.Timer;
import java.util.concurrent.CountDownLatch;

public class GameTimer{

    private static Timer timer;
    private final int increment;
    private static int interval;
    private static boolean end = false;

    public GameTimer(int interval, int increment){
        this.increment = increment;
        this.timer = new Timer();
        this.interval = interval;
    }

    public GameTimer(int interval){
        this.interval = interval;
        this.increment = 0;
        this.timer = new Timer(true);
    }


    public int getTime(){
        return interval;
    }

    public void clock(){
        int delay = 1000;
        int period = 1000;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                service();
            }
        }, delay, period);
    }

    // Using java.util.Timer to schedule events at a fixed rate.
    static void service() {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //Background work
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    setInterval();
                                } finally {
                                    latch.countDown();
                                }
                            }
                        });
                        latch.await();
                        //Keep with the background work
                        return null;
                    }
                };
            }
        };
        service.start();
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
    private static void setInterval(){
        if(end){
            timer.cancel();
        }
        if(interval > 0){
            --interval;
        }
    }

    // Could be used when someone forfeits the game etc...
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

    public static void main(String[] args){
        GameTimer timer = new GameTimer(45, 3);
        timer.clock();
    }
}