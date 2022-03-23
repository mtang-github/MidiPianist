package mainloop;

import util.observer.AbstractSubject;
import util.observer.Subject;

public class FixedTimeLoop {

    private static final String THREAD_NAME = "main loop";

    private final AbstractSubject fixedTimeBroadcaster;

    private final int millisBetweenUpdates;

    private boolean running = true;

    public FixedTimeLoop(int updatesPerSecond) {
        fixedTimeBroadcaster = new Subject();
        millisBetweenUpdates = calcMillisBetweenUpdates(updatesPerSecond);
    }

    private static int calcMillisBetweenUpdates(int updatesPerSecond){
        double millisPerSecond = 1000d;
        return (int)(millisPerSecond/updatesPerSecond);
    }

    public final void begin() {
        Thread thread = new Thread(THREAD_NAME){
            @Override
            public void run() {
                threadAction();
            }
        };
        thread.start();
    }

    private void threadAction() {
        long nextUpdateMillis = System.currentTimeMillis();

        while(running) {
            if (System.currentTimeMillis() >= nextUpdateMillis) {
                fixedTimeUpdate();
                nextUpdateMillis += millisBetweenUpdates;
            }
        }
    }

    private void fixedTimeUpdate(){
        fixedTimeBroadcaster.broadcast();
    }

    public void end(){
        running = false;
    }

    public AbstractSubject getFixedTimeBroadcaster() {
        return fixedTimeBroadcaster;
    }
}