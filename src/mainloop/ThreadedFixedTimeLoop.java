package mainloop;

import util.observer.ISubject;
import util.observer.Subject;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A {@code ThreadedFixedTimeLoop} encapsulates the creation of a loop on a new thread and
 * broadcasts updates through a void subject.
 */
public class ThreadedFixedTimeLoop {

    private static final String THREAD_NAME = "ThreadedFixedTimeLoop";

    private final Subject<Void> fixedTimeBroadcaster;

    private final int millisBetweenUpdates;
    private final AtomicBoolean running;

    private Thread thread;

    /**
     * Constructs a new {@code ThreadedFixedTimeLoop} with the given updates per second.
     *
     * @param updatesPerSecond - how many times per second this loop will run.
     */
    public ThreadedFixedTimeLoop(int updatesPerSecond) {
        fixedTimeBroadcaster = new Subject<>();
        millisBetweenUpdates = calcMillisBetweenUpdates(updatesPerSecond);
        running = new AtomicBoolean(false);
    }

    /**
     * Calculates the number of milliseconds between each update for the given updates per second.
     *
     * @param updatesPerSecond how many times per second a loop will run.
     *
     * @return the number of milliseconds between each update for the given updates per second.
     */
    private static int calcMillisBetweenUpdates(int updatesPerSecond){
        double millisPerSecond = 1000d;
        return (int)(millisPerSecond/updatesPerSecond);
    }

    /**
     * Begins execution of this loop on a new thread; Ends the previous thread if one exists.
     */
    public final void begin() {
        if(thread != null) {
            running.set(false);
            boolean hasThreadEnded = false;
            while(!hasThreadEnded){
                try {
                    thread.join();
                    hasThreadEnded = true;
                }
                catch (InterruptedException e) {
                    //do nothing
                }
            }
        }
        running.set(true);
        thread = new Thread(THREAD_NAME){
            @Override
            public void run() {
                runLoop();
            }
        };
        thread.start();
    }

    /**
     * Continuously runs this loop until {@code end()} is called.
     */
    private void runLoop() {
        long nextUpdateMillis = System.currentTimeMillis();

        while(running.get()) {
            if (System.currentTimeMillis() >= nextUpdateMillis) {
                fixedTimeBroadcaster.broadcast(null);
                nextUpdateMillis += millisBetweenUpdates;
            }
        }
    }

    /**
     * Ends this loop.
     */
    public void end(){
        running.set(false);
    }

    /**
     * Returns the {@code ISubject<Void>} through which updates will be broadcast.
     *
     * @return the {@code ISubject<Void>} through which updates will be broadcast.
     */
    public ISubject<Void> getFixedTimeBroadcaster() {
        return fixedTimeBroadcaster;
    }
}