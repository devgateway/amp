package org.digijava.module.calendar.entity;

public class AmpCalendarGraphItem {
    private String color;
    private int left;
    private int center;
    private int right;

    public AmpCalendarGraphItem() {

    }

    public AmpCalendarGraphItem(String color, int left, int center, int right) {
        this.color = color;
        this.left = left;
        this.center = center;
        this.right = right;
    }

    public int getCenter() {
        return center;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public String getColor() {
        return color;
    }

    public void setCenter(int center) {
        this.center = center;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
