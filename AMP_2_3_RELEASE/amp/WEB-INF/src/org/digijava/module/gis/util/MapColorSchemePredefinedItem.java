package org.digijava.module.gis.util;

/**
 * User: flyer
 * Date: 4/11/12
 * Time: 2:47 PM
 */
public class MapColorSchemePredefinedItem {

    public MapColorSchemePredefinedItem() {

    }

    public MapColorSchemePredefinedItem(float start, float lessThen, ColorRGB color) {
        this.start = start;
        this.lessThen = lessThen;
        this.color = color;
    }

    private float start;
    private float lessThen;
    private ColorRGB color;

    public float getStart() {
        return start;
    }

    public void setStart(float start) {
        this.start = start;
    }

    public float getLessThen() {
        return lessThen;
    }

    public void setLessThen(float lessThen) {
        this.lessThen = lessThen;
    }

    public ColorRGB getColor() {
        return color;
    }

    public void setColor(ColorRGB color) {
        this.color = color;
    }
}
