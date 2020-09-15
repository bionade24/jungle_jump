package eu.bionade24.junglejump.game;

import java.awt.image.BufferedImage;

import eu.bionade24.junglejump.window.GameWindow;

import java.awt.Graphics2D;

public abstract class GameObject {
    protected Coordinate objectPosition;
    protected Coordinate prevPosition;
    private double width;
    private double height;
    protected double xSpeed;
    protected double ySpeed;
    private BufferedImage image;

    public GameObject(Coordinate objectPosition, double width, double height, BufferedImage image) {
        this.objectPosition = objectPosition;
        this.prevPosition = this.objectPosition;
        this.width = width;
        this.height = height;
        this.image = image;
        xSpeed = 0.d;
        ySpeed = 0.d;
    }

    public Coordinate getObjectPosition() {
        return objectPosition;
    }

    public void setObjectPosition(Coordinate position) {
        this.objectPosition = position;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getXSpeed() {
        return xSpeed;
    }

    public void setXSpeed(double speed) {
        this.xSpeed = speed;
    }

    public double getYSpeed() {
        return ySpeed;
    }

    public void setYSpeed(double speed) {
        this.ySpeed = speed;
    }

    protected void moveGameObject() {
        prevPosition = objectPosition;
        this.objectPosition.x += xSpeed;
        this.objectPosition.y += ySpeed;
    }

    // Overloaded method makeMove()
    public void makeMove() {
        moveGameObject();
    }

    public void makeMove(double xSpeed, double ySpeed) {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        moveGameObject();
    }

    public void makeMove(double xSpeed, double ySpeed, Coordinate position) {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.objectPosition = position;
    }

    public void spawn() {
        double position = 0.d;
        if (xSpeed >= 0) {
            position = this.width;
        } else {
            position = GameWindow.getInstance().getWidth() + this.width;
        }
        this.getObjectPosition().x = position;
    }

    public boolean isLeftOf(GameObject that) {
        return this.getObjectPosition().x + this.getWidth() < that.getObjectPosition().x;
    }

    public boolean isAbove(GameObject that) {
        return this.getObjectPosition().y + this.getHeight() < that.getObjectPosition().y;
    }

    public boolean touches(GameObject that) {
        if (this.isLeftOf(that))
            return false;
        if (that.isLeftOf(this))
            return false;
        if (this.isAbove(that))
            return false;
        if (that.isAbove(this))
            return false;

        return true;
    }

    public boolean isVisible() {
        if (objectPosition.x  + getWidth() < 0) {
            return false;
        }
        if (objectPosition.x > GameWindow.getInstance().getWidth()) {
            return false;
        }
        if (objectPosition.y + getHeight() < 0) {
            return false;
        }

        return true;
    }

    public void paintMe(Graphics2D g2d, double interpolation) {
        if (isVisible()) {
        // Interpolate points between prevPosition and objectPosition. Add step to
        // prevPosition.n each time.
            g2d.drawImage(this.image, (int) (prevPosition.x += (objectPosition.x - prevPosition.x) * interpolation),
                    (int) (prevPosition.y += (objectPosition.y - prevPosition.y) * interpolation),
                    (int) this.getWidth(), (int) this.getHeight(), null);
        }
    }
}