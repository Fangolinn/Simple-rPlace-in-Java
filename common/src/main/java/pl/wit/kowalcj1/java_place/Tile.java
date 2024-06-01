package pl.wit.kowalcj1.java_place;

import java.awt.Color;

/**
 * Utility class for passing around information about grid tiles
 * 
 * @author Jakub Kowalczyk
 */
public class Tile {
    // Matrix coordinates
    private int iCoordinate;
    private int jCoordinate;

    private Color color;

    public Tile(int i, int j, Color color){
        this.iCoordinate = i;
        this.jCoordinate = j;
        this.color = color;
    }

    public Tile(int i, int j){
        this(i, j, Color.WHITE);
    }
}
