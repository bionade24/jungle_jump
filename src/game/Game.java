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
import javax.swing.Timer;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import window.GameWindow;
import components.Helper;


public class Game extends JPanel{

    // Game class internal attributes
    private boolean isRunning;
    private Timer t; // Game loop timer
    private int lifes;
    private static BufferedImage backgroundImage = Helper.getImage("graphics/Dschungel.png", 2560);
    private BufferedImage playerImage;
    private List<BufferedImage> gObjectImages = new LinkedList<BufferedImage>();

    private static final int DELAY = 1000/60;
    public static final double INIT_SPEED = GameWindow.getInstance().getHeight()/-100.d;
    public static final double SPEED_DECREASE = 1.006;
    public static final double GROUND_HEIGHT = Math.pow(GameWindow.getInstance().getHeight(), 2)/backgroundImage.getHeight()*0.7;

    // Game objects
    private Figure playerFigure;
    private List<GameObject> gameObjects = new LinkedList<GameObject>();


    public Game() {

        lifes = 3;

        // load pictures
        gObjectImages.add(Helper.getImage("graphics/wooden_hurdle.png", 1920));
        playerImage = Helper.getImage("graphics/Affe.png", 1920);

        t = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doTick();
            }
        });

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

        playerFigure = new Figure(new Coordinate(0, GROUND_HEIGHT), 3200/8, 4267/8, 0, 2, playerImage);
        gameObjects.add(new Hurdle(new Coordinate(600, GROUND_HEIGHT + 80), 1138/2, 692/2, gObjectImages.get(0)));
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
            t.start();
            isRunning = true;
        } else {
            throw new RuntimeException("Game is already running");
        }
    }

    private void stopGame() {
        if (isRunning) {
            t.stop();
            isRunning = false;
        } else {
            throw new RuntimeException("Game does not run");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
        g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
        g2d.drawString(String.format("Lifes: %d", lifes), (int)(this.getWidth()*0.7), this.getHeight()/10);

        playerFigure.paintMe(g2d);

        for (GameObject i : gameObjects) {
            i.paintMe(g2d);
        }
    }

    private void openIngameMenu() {
        System.out.println("Ingame Menu opened");
    }

    private void doTick() {
        // run the game
        //System.out.println("Tick");
        this.repaint();
        playerFigure.makeMove();
        for (GameObject i : gameObjects) {
            i.makeMove();;
        }
        if (playerFigure.touches(gameObjects.get(0))) {
            //this.stopGame();
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
}