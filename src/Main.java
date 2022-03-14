import mainloop.FixedTimeLoop;
import resource.ResourceController;
import midi.MusicController;
import util.observer.AbstractObserver;
import window.WindowController;

final class Main {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;
    private static final String TITLE = "MidiPianist";

    private static final int FRAMES_PER_SECOND = 60;

    private static Thread setupThread;

    private static ResourceController resourceController;
    private static WindowController windowController;
    private static MusicController musicController;

    private static AbstractObserver cleanupReceiver;

    private static FixedTimeLoop mainLoop;

    public static void main(String[] args) {
        setupThread = new Thread("setup") {
            @Override
            public void run() {
                setupThread = Thread.currentThread();
                setup();
                setupThread = null;
            }
        };
        setupThread.start();
    }

    private static void setup() {
        System.setProperty("sun.java2d.d3d", "true");

        makeCleanupReceiver();

        makeResourceController();
        if (Thread.interrupted()) {
            cleanUp();
            return;
        }

        makeMusicController();
        if (Thread.interrupted()) {
            cleanUp();
            return;
        }

        makeWindowController();
        if (Thread.interrupted()) {
            cleanUp();
            return;
        }

        makeAndRunGameLoop();
    }

    private static void makeCleanupReceiver() {
        cleanupReceiver = Main::cleanUp;
    }

    private static void makeResourceController() {
        resourceController = ResourceController.makeResourceController(ResourceTypes.values());
        //todo load files
    }

    private static void makeWindowController() {
        windowController = new WindowController(WIDTH, HEIGHT, TITLE);
        windowController.getWindowCloseBroadcaster().attach(cleanupReceiver);
    }

    private static void makeMusicController() {
        musicController = new MusicController();
    }

    private static void makeAndRunGameLoop() {
        mainLoop = new FixedTimeLoop(FRAMES_PER_SECOND);
        //todo attach mainLoop to other shit
        mainLoop.begin();
    }

    private static void cleanUp() {
        if (setupThread != null) {
            setupThread.interrupt();
        }

        windowController = null;

        if (mainLoop != null) {
            mainLoop.end();
            mainLoop = null;
        }
        if (musicController != null) {
            musicController.cleanUp();
            musicController = null;
        }
        if (resourceController != null) {
            resourceController.cleanUp();
            resourceController = null;
        }
        System.exit(0);
    }
}