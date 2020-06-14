package game;

import java.awt.event.*;
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
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import window.GameWindow;


public class Game extends JPanel{

    // Game class internal attributes
    private static final int DELAY = 1000/60;
    public static final double INIT_SPEED = GameWindow.getInstance().getHeight()/-100.d;
    public static final double SPEED_DECREASE = 1.006;
    public static final double GROUND_HEIGHT = GameWindow.getInstance().getHeight()*0.4;
    private enum playerDirection {
        LEFT, ZERO, RIGHT
    }
    private boolean isRunning;
    private Timer t; // Game loop timer
    private BufferedImage backgroundImage;
    private BufferedImage playerImage;

    // Game objects
    private Figure playerFigure;


    public Game() {

        // load pictures
        try {
            backgroundImage = ImageIO.read(new File("graphics/Dschungel.png"));
            playerImage = ImageIO.read(new File("graphics/Affe.png"));
        } catch (IOException e) {
            System.err.println(e);
        }

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

        playerFigure.paintMe(g2d);
    }

    private void openIngameMenu() {
        System.out.println("Ingame Menu opened");
    }

    private void doTick() {
        // run the game
        //System.out.println("Tick");
        this.repaint();
        playerFigure.makeMove();
    }

    private void jump() {
        playerFigure.setYSpeed(INIT_SPEED);
    }
}