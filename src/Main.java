import mainloop.FixedTimeLoop;
import resource.ResourceController;
import midi.MusicController;
import util.file.FileUtil;
import util.observer.AbstractObserver;
import window.WindowController;

import java.io.File;

final class Main {

    private static final int WIDTH = 750;
    private static final int HEIGHT = 480;
    private static final String TITLE = "MidiPianist";

    private static final int FRAMES_PER_SECOND = 60;

    private static Thread setupThread;

    private static ResourceController resourceController;
    private static WindowController windowController;
    private static MusicController musicController;
    private static DisplayController displayController;

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

        makeWindowController();
        if (Thread.interrupted()) {
            cleanUp();
            return;
        }

        makeMusicController();
        if (Thread.interrupted()) {
            cleanUp();
            return;
        }

        makeDisplayController();
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
        resourceController.loadFile("res");
    }

    private static void makeWindowController() {
        windowController = new WindowController(WIDTH, HEIGHT, TITLE);
        windowController.getWindowCloseBroadcaster().attach(cleanupReceiver);
    }

    private static void makeMusicController() {
        musicController = new MusicController();
    }

    private static void makeDisplayController(){
        displayController = new DisplayController(WIDTH, HEIGHT, resourceController.getResourceManager(ResourceTypes.IMAGE));
        displayController.getImageBroadcaster().attach(windowController.getImageReceiver());
        musicController.getMidiMessageBroadcaster().attach(displayController.getMidiMessageReceiver());
    }

    private static void makeAndRunGameLoop() {
        mainLoop = new FixedTimeLoop(FRAMES_PER_SECOND);
        mainLoop.getFixedTimeBroadcaster().attach(displayController.getUpdateReceiver());
        mainLoop.begin();
        musicController.getTrackStartReceiver().update(FileUtil.makeInputStream(new File("res/test.mid")));
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