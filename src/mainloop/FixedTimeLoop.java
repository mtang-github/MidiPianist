package mainloop;

import util.observer.AbstractSubject;
import util.observer.Subject;

public class FixedTimeLoop extends AbstractThreadedMainLoop {

    private boolean running = true;
    private final int updatesPerSecond;
    private int millisBetweenUpdates;

    private final AbstractSubject fixedTimeBroadcaster;

    public FixedTimeLoop(int updatesPerSecond) {
        fixedTimeBroadcaster = new Subject();
        this.updatesPerSecond = updatesPerSecond;
        calcMillisBetweenUpdates();
    }

    private void calcMillisBetweenUpdates(){
        double millisPerSecond = 1000d;
        millisBetweenUpdates = (int)(millisPerSecond/updatesPerSecond);
    }

    @Override
    protected void threadAction() {
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

    @Override
    public void end(){
        running = false;
    }

    public AbstractSubject getFixedTimeBroadcaster() {
        return fixedTimeBroadcaster;
    }
}