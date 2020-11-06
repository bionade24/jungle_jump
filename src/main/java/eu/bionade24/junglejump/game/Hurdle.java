package eu.bionade24.junglejump.game;

import java.awt.image.BufferedImage;

import eu.bionade24.junglejump.components.Coordinate;
import eu.bionade24.junglejump.components.GameObject;

public class Hurdle extends GameObject {

    public Hurdle(Coordinate position, double witdth, double height, BufferedImage img) {

        super(position, witdth, height, img);
    }
}