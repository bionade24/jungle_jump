package game;

import java.awt.event.*;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.*;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import window.GameWindow;
import components.Helper;

public class Game extends JPanel {

    // Game class internal attributes
    private boolean isRunning;
    private GameLoop gl; // Game loop
    private double interpolation;
    private int lifes;
    private static BufferedImage backgroundImage = Helper.getImage("graphics/Dschungel.png",
            GameWindow.getInstance().getWidth());
    private BufferedImage playerImage;
    private List<BufferedImage> gObjectImages = new LinkedList<BufferedImage>();

    public static final double INIT_SPEED = GameWindow.getInstance().getHeight() / -100.d;
    public static final double SPEED_DECREASE = 1.006;
    public static final double GROUND_HEIGHT = GameWindow.getInstance().getHeight() * 0.6;

    // Game objects
    private Figure playerFigure;
    private List<GameObject> gameObjects = new LinkedList<GameObject>();

    public Game() {

        lifes = 3;

        // load pictures
        gObjectImages.add(Helper.getImage("graphics/wooden_hurdle.png", GameWindow.getInstance().getWidth()));
        playerImage = Helper.getImage("graphics/Affe.png", GameWindow.getInstance().getWidth() / 2);

        gl = new GameLoop();

        InputMap inputMap = this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getActionMap();

        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        KeyStroke space = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false);
        KeyStroke left_p = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false);
        KeyStroke left_r = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true);
        KeyStroke right_p = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false);
        KeyStroke right_r = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true);

        inputMap.put(escape, "Esc");
        inputMap.put(space, "Space");
        inputMap.put(left_p, "left pressed");
        inputMap.put(left_r, "left released");
        inputMap.put(right_p, "right pressed");
        inputMap.put(right_r, "right released");

        actionMap.put("Esc", action(a -> openIngameMenu()));
        actionMap.put("Space", action(a -> jump()));
        actionMap.put("left pressed", action(a -> playerFigure.setXSpeed(-10)));
        actionMap.put("left released", action(a -> playerFigure.setXSpeed(0)));
        actionMap.put("right pressed", action(a -> playerFigure.setXSpeed(10)));
        actionMap.put("right released", action(a -> playerFigure.setXSpeed(0)));

        playerFigure = new Figure(new Coordinate(0, GROUND_HEIGHT), playerImage.getWidth() / 4,
                playerImage.getHeight() / 4, 0, 2, playerImage);
        gameObjects.add(new Hurdle(new Coordinate(600, GROUND_HEIGHT), gObjectImages.get(0).getWidth() / 4,
                gObjectImages.get(0).getHeight() / 4, gObjectImages.get(0)));
        gameObjects.get(0).setXSpeed(5);

        this.setVisible(true);
        startGame();
    }

    static Action action(Consumer<ActionEvent> actionPerformed) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionPerformed.accept(e);
            }
        };
    };

    private void startGame() {
        if (!isRunning) {
            isRunning = true;
            gl.runGameLoop();
        } else {
            throw new RuntimeException("Game is already running");
        }
    }

    private void stopGame() {
        if (isRunning) {
            isRunning = false;
        } else {
            throw new RuntimeException("Game does not run");
        }
    }

    public void drawGame(double interpolation) {
        this.setInterpolation(interpolation);
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
        g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
        g2d.drawString(String.format("Lifes: %d", lifes), (int) (this.getWidth() * 0.7), this.getHeight() / 10);

        playerFigure.paintMe(g2d, this.interpolation);

        for (GameObject i : gameObjects) {
            i.paintMe(g2d, this.interpolation);
        }
    }

    private void openIngameMenu() {
        System.out.println("Ingame Menu opened");
    }

    private void doTick() {
        // run the game
        // System.out.println("Tick");
        playerFigure.makeMove();
        for (GameObject i : gameObjects) {
            i.makeMove();
            ;
        }
        if (playerFigure.touches(gameObjects.get(0))) {
            // this.stopGame();
            lifes -= 1;
        }

        for (GameObject i : gameObjects) {
            if (i.getObjectPosition().x + i.getWidth() < 0) {
                i.spawn();
            }
        }
    }

    private void jump() {
        if (playerFigure.getYSpeed() == 0) {
            playerFigure.setYSpeed(INIT_SPEED);
        }
    }

    private void setInterpolation(double i) {
        this.interpolation = i;
    }

    public double getInterpolation() {
        return this.interpolation;
    }

    class GameLoop {
        private int frameCount = 0;

        // Starts a new thread and runs the game loop in it.
        public void runGameLoop() {
            Thread loop = new Thread() {
                public void run() {
                    gameLoop();
                }
            };
            loop.start();
        }

        // Only run this in another Thread!
        private void gameLoop() {
            // This value would probably be stored elsewhere.
            final double GAME_HERTZ = 40.d;
            // Calculate how many ns each frame should take for our target game hertz.
            final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
            // At the very most we will update the game this many times before a new render.
            // If you're worried about visual hitches more than perfect timing, set this to
            // 1.
            final int MAX_UPDATES_BEFORE_RENDER = 5;
            // We will need the last update time.
            double lastUpdateTime = System.nanoTime();
            // Store the last time we rendered.
            double lastRenderTime = System.nanoTime();

            // If we are able to get as high as this FPS, don't render again.
            final double TARGET_FPS = 60;
            final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

            int lastSecondTime = (int) (lastUpdateTime / 1000000000);

            double now;
            int updateCount;

            while (isRunning) {
                now = System.nanoTime();
                updateCount = 0;
                // Do as many game updates as we need to, potentially playing catchup.
                while (now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER) {
                    doTick();
                    lastUpdateTime += TIME_BETWEEN_UPDATES;
                    updateCount++;
                }
                // If for some reason an update takes forever, we don't want to do an insane
                // number of catchups.
                // If you were doing some sort of game that needed to keep EXACT time, you would
                // get rid of this.
                if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
                    lastUpdateTime = now - TIME_BETWEEN_UPDATES;
                }
                // Render. To do so, we need to calculate interpolation for a smooth render.
                float interpolation = Math.min(1.0f, (float) ((now - lastUpdateTime) / TIME_BETWEEN_UPDATES));
                drawGame(interpolation);
                lastRenderTime = now;
                frameCount++;
                // Update the frames we got.
                int thisSecond = (int) (lastUpdateTime / 1000000000);
                if (thisSecond > lastSecondTime) {
                    System.out.println(
                            "NEW SECOND " + thisSecond + " " + frameCount + " " + updateCount + " " + interpolation);
                    frameCount = 0;
                    lastSecondTime = thisSecond;
                }
                // Yield until it has been at least the target time between renders. This saves
                // the CPU from hogging.
                while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS
                        && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
                    Thread.yield();
                    // This stops the app from consuming all your CPU. It makes this slightly less
                    // accurate, but is worth it.
                    // You can remove this line and it will still work (better), your CPU just
                    // climbs on certain OSes.
                    // FYI on some OS's this can cause pretty bad stuttering. Scroll down and have a
                    // look at different peoples' solutions to this.
                    try {
                        Thread.sleep(1);
                    } catch (Exception e) {
                    }
                    now = System.nanoTime();
                }
            }
        }
    }
}