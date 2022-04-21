import display.DisplayController;
import mainloop.ThreadedFixedTimeLoop;
import resource.ResourceSystem;
import midi.MidiController;
import util.file.FileUtil;
import window.WindowController;

import java.io.File;
import java.util.Objects;

/**
 * The MidiPianist program creates a GUI representing the 16 MIDI channels and
 * plays MIDI files, updating the graphical representation of the channels in real time
 * depending on what notes are played.
 */
final class Main {

    private static final String DEFAULT_SYNTH_NAME = "Microsoft MIDI Mapper";
    private static final String RESOURCE_FOLDER = "res";

    private static final int WIDTH = 750;
    private static final int HEIGHT = 480;
    private static final String TITLE = "MidiPianist";

    private static final int FRAMES_PER_SECOND = 60;

    private static String synthName;
    private static Thread setupThread;

    private static ResourceSystem resourceSystem;
    private static WindowController windowController;
    private static MidiController midiController;
    private static DisplayController displayController;

    private static ThreadedFixedTimeLoop mainLoop;

    /**
     * The entry point of the program.
     * @param args the first string is the name of the synth to use if provided, otherwise this program will default
     *             to the Microsoft MIDI Mapper.
     */
    public static void main(String[] args) {
        if(args.length > 0){
            synthName = args[0];
        }
        else{
            synthName = DEFAULT_SYNTH_NAME;
        }
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
        try {
            System.setProperty("sun.java2d.d3d", "true");

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

            makeMidiController();
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
        catch(Exception e){
            System.err.println(e.getMessage());
            cleanUp();
        }
    }

    /**
     * Creates the {@link ResourceSystem} and loads all the files in the resource folder.
     */
    private static void makeResourceController() {
        resourceSystem = new ResourceSystem(ResourceTypes.values());
        resourceSystem.loadFile(RESOURCE_FOLDER);
    }

    /**
     * Creates the {@link WindowController}. Attaches the window clean up broadcaster to {@link #cleanUp()}.
     */
    private static void makeWindowController() {
        windowController = new WindowController(WIDTH, HEIGHT, TITLE);
        windowController.getWindowCloseBroadcaster().attach(data -> cleanUp());
    }

    /**
     * Creates the {@link MidiController}. Attaches the window file drop broadcaster to the track start receiver.
     */
    private static void makeMidiController() {
        midiController = new MidiController(synthName);
        windowController.getFileDropBroadcaster().attach(fileList -> {
            for(File file : fileList){
                if(Objects.equals(FileUtil.getFileExtension(file), "mid")){
                    midiController.getTrackStartReceiver().update(FileUtil.makeInputStream(file));
                    break;
                }
            }
        });
    }

    /**
     * Creates the {@link DisplayController} and attaches it to the window and music controllers.
     */
    private static void makeDisplayController(){
        displayController = new DisplayController(WIDTH, HEIGHT, resourceSystem.getResourceManager(ResourceTypes.IMAGE));
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
        if (resourceSystem != null) {
            resourceSystem.cleanUp();
            resourceSystem = null;
        }
        System.exit(0);
    }
}