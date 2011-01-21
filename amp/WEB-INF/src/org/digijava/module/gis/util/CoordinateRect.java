package org.digijava.module.gis.util;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: </p>
 *
 * @author George Kvizhinadze
 * @version 1.0
 */
public class CoordinateRect {
    private float left, right, top, bottom;

    public CoordinateRect() {

    }

    public CoordinateRect(float left, float right, float top, float bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }


    public float getBottom() {
        return bottom;
    }

    public float getLeft() {
        return left;
    }

    public float getRight() {
        return right;
    }

    public float getTop() {
        return top;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public void setTop(float top) {
        this.top = top;
    }
}
