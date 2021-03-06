package eu.bionade24.junglejump.game;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import java.util.LinkedList;
import java.util.List;

import eu.bionade24.junglejump.window.GameWindow;
import eu.bionade24.junglejump.components.AdvancedButton;
import eu.bionade24.junglejump.components.Helper;

public class Game extends JLayeredPane {
    // Game class internal attributes
    private boolean isRunning;
    private GameLoop gl; // Game loop
    private double interpolation;
    private int lifes;
    //private static BufferedImage backgroundImage = Helper.getImage("graphics/jungle.png",
    //        GameWindow.getInstance().getWidth());
    private static BufferedImage backgroundImage = Helper.getVectorGraphic("graphics/jungle.svg",
            GameWindow.getInstance().getWidth(), 4267.f/3200.f * GameWindow.getInstance().getHeight());
    private BufferedImage playerImage;
    private List<BufferedImage> gObjectImages = new LinkedList<BufferedImage>();

    public static final double INIT_SPEED = GameWindow.getInstance().getHeight() / -80.d;
    public static final double SPEED_DECREASE = 1.01;
    public static final double GROUND_HEIGHT = GameWindow.getInstance().getHeight() * 0.6;

    JPanel deathmessage;

    // Ingame menu
    private JLayeredPane ingameMenu;
    public AdvancedButton menuButton;
    private AdvancedButton resumeButton;
    public AdvancedButton quitButton;

    // Game objects
    private Figure playerFigure;
    private List<GameObject> gameObjects = new LinkedList<GameObject>();

    public Game() {

        lifes = 3;

        // load pictures
        gObjectImages.add(Helper.getImage("graphics/wooden_hurdle.png", GameWindow.getInstance().getWidth()));
        playerImage = Helper.getVectorGraphic("graphics/ape.svg", GameWindow.getInstance().getWidth()/5.f, GameWindow.getInstance().getHeight()/2.f);

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

        actionMap.put("Esc", Helper.actionFactory(a -> openIngameMenu()));
        actionMap.put("Space", Helper.actionFactory(a -> jump()));
        actionMap.put("left pressed", Helper.actionFactory(a -> playerFigure.setXSpeed(-10)));
        actionMap.put("left released", Helper.actionFactory(a -> playerFigure.setXSpeed(0)));
        actionMap.put("right pressed", Helper.actionFactory(a -> playerFigure.setXSpeed(10)));
        actionMap.put("right released", Helper.actionFactory(a -> playerFigure.setXSpeed(0)));

        playerFigure = new Figure(new Coordinate(0, GROUND_HEIGHT), playerImage.getWidth() / 4,
                playerImage.getHeight() / 4, 0, 2, playerImage);
        gameObjects.add(new Hurdle(new Coordinate(600, GROUND_HEIGHT), gObjectImages.get(0).getWidth() / 4,
                gObjectImages.get(0).getHeight() / 4, gObjectImages.get(0)));
        gameObjects.get(0).setXSpeed(-5);

        this.setVisible(true);
        startGame();
    }

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
        /*JTextArea text = new JTextArea();
        text.setForeground(Color.BLUE);
        text.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
        text.setText(String.format("Lifes: %d", lifes));*/
        playerFigure.paintMe(g2d, this.interpolation);

        for (GameObject i : gameObjects) {
            i.paintMe(g2d, this.interpolation);
        }
    }

    private void showDeathMessage() {
        this.stopGame();
        deathmessage = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                // g2d.drawImage()
            }
        };
        deathmessage.setSize(this.getWidth(), this.getHeight());
        AdvancedButton gomenuButton = new AdvancedButton("Go to Menu");
        AdvancedButton restartButton = new AdvancedButton("Restart Game");

        gomenuButton.addActionListener(Helper.actionFactory(a -> GameWindow.getInstance().launchMenu()));
        restartButton.addActionListener(Helper.actionFactory(a -> {
            this.remove(deathmessage);
            this.invalidate();
            this.revalidate();
            this.repaint();
            deathmessage = null;
            for (GameObject g : gameObjects) {
                g.spawn();
            }
            playerFigure.spawn();
            lifes = 3;
            startGame();
        }));

        deathmessage.setLayout(new GridLayout(3, 3));
        deathmessage.add(gomenuButton);
        JButton t1 = new JButton();
        t1.setVisible(false);
        deathmessage.add(t1);
        JButton t2 = new JButton();
        t2.setVisible(false);
        deathmessage.add(t2);
        JButton t3 = new JButton();
        t3.setVisible(false);
        deathmessage.add(t3);
        JButton t4 = new JButton();
        t4.setVisible(false);
        deathmessage.add(t4);
        JButton t5 = new JButton();
        t5.setVisible(false);
        deathmessage.add(t5);
        deathmessage.add(restartButton);

        this.add(deathmessage);
        this.invalidate();
        this.revalidate();
        this.repaint();
    }

    private void openIngameMenu() {
        System.out.println("Ingame Menu opened");
        this.stopGame();
        ingameMenu = new JLayeredPane();
        ingameMenu.setSize(this.getWidth(), this.getHeight());
        menuButton = new AdvancedButton("Return to Menu");
        resumeButton = new AdvancedButton("Resume Game");
        quitButton = new AdvancedButton("Quit");

        menuButton.addActionListener(Helper.actionFactory(a -> GameWindow.getInstance().launchMenu()));
        resumeButton.addActionListener(Helper.actionFactory(a -> {
            this.remove(ingameMenu);
            this.invalidate();
            this.revalidate();
            startGame();
        }));
        quitButton.addActionListener(Helper.actionFactory(a -> GameWindow.getInstance().dispose()));

        ingameMenu.setLayout(new GridLayout(1, 3));
        JButton t1 = new JButton();
        t1.setVisible(false);
        ingameMenu.add(t1);

        JLayeredPane p2 = new JLayeredPane();
        p2.setLayout(new GridLayout(3, 1, 0, 5));
        p2.add(menuButton);
        p2.add(resumeButton);
        p2.add(quitButton);

        ingameMenu.add(p2);

        JButton t2 = new JButton();
        t2.setVisible(false);

        ingameMenu.add(t2);

        this.add(ingameMenu);
        this.invalidate();
        this.revalidate();
        this.repaint();
    }

    private void doTick() {
        // run the game
        // System.out.println("Tick");
        playerFigure.makeMove();
        for (GameObject i : gameObjects) {
            i.makeMove();
        }
        if (playerFigure.touches(gameObjects.get(0))) {
            lifes -= 1;
            if (lifes <= 0)
                showDeathMessage();
            gameObjects.get(0).spawn();
            playerFigure.spawn();
        }

        for (GameObject i : gameObjects) {
            if (playerFigure.touches(i)) {
                lifes -= 1;
                if (lifes <= 0)
                    showDeathMessage();
                gameObjects.get(0).spawn();
                playerFigure.spawn();
            }
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