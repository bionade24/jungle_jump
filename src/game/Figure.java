package game;

import java.awt.image.BufferedImage;
import window.GameWindow;

public class Figure extends GameObject {

    public Figure(Coordinate position, double witdth, double height, double movingAngle, double movingDistance,
            BufferedImage img) {

        super(position, witdth, height, img);
    }

    @Override
    protected void moveGameObject() {
        this.prevPosition = objectPosition;
        this.objectPosition.x += xSpeed;
        if (this.objectPosition.x > GameWindow.getInstance().getWidth()) {
            this.objectPosition.x = -this.getWidth();
        } else {
            if (this.objectPosition.x < -this.getWidth()) {
                this.objectPosition.x = GameWindow.getInstance().getWidth();
            }
        }
        if (this.ySpeed == 0) {
            return;
        } else {
            if (this.objectPosition.y > Game.GROUND_HEIGHT) {
                this.setYSpeed(0);
                this.objectPosition.y = Game.GROUND_HEIGHT;
            } else {
                if (Math.abs(this.ySpeed) >= Game.INIT_SPEED / -1.5) {
                    // Has to be working during fall, too
                    this.objectPosition.y += this.ySpeed;
                    this.ySpeed /= Game.SPEED_DECREASE;
                } else {
                    ySpeed = -Game.INIT_SPEED; // Begin of falling down
                }
            }
        }
    }
}