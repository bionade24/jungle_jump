package game;

import java.awt.image.BufferedImage;
import java.awt.*;

public class Figure extends GameObject {

    private Image playerImage;

    public Figure(Coordinate position, double witdth, double height, double movingAngle,
     double movingDistance, BufferedImage img) {

        super(position, witdth, height);
        this.playerImage = img;
    }

    @Override
    public void paintMe(Graphics2D g2d) {
        g2d.drawImage(playerImage, (int)this.getObjectPosition().x,
         (int)this.getObjectPosition().y,
         (int)this.getWidth(), (int)this.getHeight(), null);
    }

    @Override
    protected void moveGameObject() {
        this.objectPosition.x += xSpeed;
        if(this.ySpeed == 0) {
            return;
        } else {
            if(this.objectPosition.y > Game.GROUND_HEIGHT) {
                this.setYSpeed(0);
                this.objectPosition.y = Game.GROUND_HEIGHT;
            } else {
                if (Math.abs(this.ySpeed) >= Game.INIT_SPEED/-1.5) {
                    //Has to be working during fall, too
                    this.objectPosition.y += this.ySpeed;
                    this.ySpeed /= Game.SPEED_DECREASE;
                } else {
                    ySpeed = -Game.INIT_SPEED; //Begin of falling down
                }
            }
        }
    }
}