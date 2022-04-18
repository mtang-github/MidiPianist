import mainloop.ThreadedFixedTimeLoop;
import resource.ResourceController;
import midi.MidiController;
import util.file.FileUtil;
import util.observer.IObserver;
import window.WindowController;

import java.io.File;

/**
 * The MidiPianist program creates a GUI representing the 16 MIDI channels and
 * plays MIDI files, updating the graphical representation of the channels in real time
 * depending on what notes are played.
 */
final class Main {

    private static final String RESOURCE_FOLDER = "res";

    private static final int WIDTH = 750;
    private static final int HEIGHT = 480;
    private static final String TITLE = "MidiPianist";

    private static final int FRAMES_PER_SECOND = 60;

    private static Thread setupThread;

    private static ResourceController resourceController;
    private static WindowController windowController;
    private static MidiController midiController;
    private static DisplayController displayController;

    private static IObserver<Void> cleanupReceiver;

    private static ThreadedFixedTimeLoop mainLoop;

    /**
     * The entry point of the program.
     * @param args unused
     */
    public static void main(String[] args) {
        setupThread = new Thread("Setup") {
            @Override
            public void run() {
                setupThread = Thread.currentThread();
                setup();
                setupThread = null;
            }
        };
        setupThread.start();
    }

    /**
     * Sets up and coordinates the various high-level parts of the program and starts the main loop.
     */
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

    /**
     * Creates the cleanup receiver which calls {@link #cleanUp} upon receiving a broadcast.
     */
    private static void makeCleanupReceiver() {
        cleanupReceiver = (Void) -> cleanUp();
    }

    /**
     * Creates the {@link ResourceController} and loads all the files in the resource folder.
     */
    private static void makeResourceController() {
        resourceController = ResourceController.makeResourceController(ResourceTypes.values());
        resourceController.loadFile(RESOURCE_FOLDER);
    }

    /**
     * Creates the {@link WindowController} and attaches the cleanup receiver to it.
     */
    private static void makeWindowController() {
        windowController = new WindowController(WIDTH, HEIGHT, TITLE);
        windowController.getWindowCloseBroadcaster().attach(cleanupReceiver);
    }

    /**
     * Creates the {@link MidiController}.
     */
    private static void makeMusicController() {
        midiController = new MidiController();
    }

    /**
     * Creates the {@link DisplayController} and attaches it to the window and music controllers
     */
    private static void makeDisplayController(){
        displayController = new DisplayController(WIDTH, HEIGHT, resourceController.getResourceManager(ResourceTypes.IMAGE));
        displayController.getImageBroadcaster().attach(windowController.getImageReceiver());
        midiController.getMidiMessageBroadcaster().attach(displayController.getMidiMessageReceiver());
    }

    /**
     * Creates the {@link ThreadedFixedTimeLoop}, attaches it to the display controller, and begins the main loop.
     */
    private static void makeAndRunGameLoop() {
        mainLoop = new ThreadedFixedTimeLoop(FRAMES_PER_SECOND);
        mainLoop.getFixedTimeBroadcaster().attach(displayController.getUpdateReceiver());
        mainLoop.begin();
        //todo test midi
        midiController.getTrackStartReceiver().update(FileUtil.makeInputStream(new File("res/test.mid")));
    }

    /**
     * Cleans up program resources and ends the program.
     */
    private static void cleanUp() {
        if (setupThread != null) {
            setupThread.interrupt();
        }

        windowController = null;

        if (mainLoop != null) {
            mainLoop.end();
            mainLoop = null;
        }
        if (midiController != null) {
            midiController.cleanUp();
            midiController = null;
        }
        if (resourceController != null) {
            resourceController.cleanUp();
            resourceController = null;
        }
        System.exit(0);
    }
}