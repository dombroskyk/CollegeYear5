package model;

import javafx.scene.paint.Color;

/**
 * A sample shape for a laboratory assignment on drawing and moving shapes
 *
 * @author James Heliotis
 */
public class Square {

    private double x,y;

    private final double length;

    /**
     * Note: for simplicity we use the JavaFX color class here. In a small way
     * this violates the separation of concerns between the model and view.
     */
    private final Color color;

    /**
     * Create a new conceptual, i.e., model-based, square shape.
     * @param x upper left corner x coordinate
     * @param y upper left corner y coordinate
     * @param len length of one side
     * @param color square's color
     */
    Square( double x, double y, double len, Color color ) {
        this.x = x;
        this.y = y;
        this.length = len;
        this.color = color;
    }

    /**
     * Accessor
     * @return upper left corner x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Accessor
     * @return upper left corner y coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Accessor
     * @return length of side
     */
    public double getLength() {
        return this.length;
    }

    /**
     * Accessor
     * @return color of this Square
     */
    public Color getColor() {
        return color;
    }

    /**
     * Mutator => move the square
     * @param x new x-axis location
     * @param y new y-axis location
     */
    void move( double x, double y ) {
        this.x = x;
        this.y = y;
    }

}
