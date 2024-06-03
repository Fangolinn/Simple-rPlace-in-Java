package pl.wit.kowalcj1.java_place;

import java.awt.Color;
import java.io.Serializable;

public class CellInfo implements Serializable{
    private int iCoord;

    private int jCoord;

    private Color color;

    public CellInfo(int iCoord, int jCoord, Color color){
        this.iCoord = iCoord;
        this.jCoord = jCoord;
        this.color = color;
    }

    public int getiCoord() {
        return iCoord;
    }

    public int getjCoord() {
        return jCoord;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + iCoord;
        result = prime * result + jCoord;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CellInfo other = (CellInfo) obj;
        if (iCoord != other.iCoord)
            return false;
        if (jCoord != other.jCoord)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "CellInfo [i=" + iCoord + ", j=" + jCoord + ", color=" + color + "]";
    }
}
