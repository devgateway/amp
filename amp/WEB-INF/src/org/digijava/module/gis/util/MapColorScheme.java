package org.digijava.module.gis.util;

/**
 * User: George Kvizhinadze
 * Date: Nov 1, 2010
 * Time: 2:59:31 PM
 */
public class MapColorScheme {

    private ColorRGB backgroundColor;
    private ColorRGB terrainColor;
    private ColorRGB waterColor;
    private ColorRGB borderColor;
    private ColorRGB regionBorderColor;
    private ColorRGB dashColor;
    private ColorRGB textColor;

    private ColorRGB gradientMinColor;
    private ColorRGB gradientMaxColor;

    public MapColorScheme() {
        
    }

    public static MapColorScheme getDefaultScheme() {
        return new MapColorScheme(new ColorRGB(255,255,255),
                                  new ColorRGB(214, 194, 158),
                                  new ColorRGB(0,0,100),
                                  new ColorRGB(255,255,255,170),
                                  new ColorRGB(0,0,0,70),
                                  new ColorRGB(0,0,0,150),
                                  new ColorRGB(0,0,3,3),
                                  new ColorRGB(23,49,65),
                                  new ColorRGB(218,220,221));

    }

    public MapColorScheme(ColorRGB backgroundColor,
                          ColorRGB terrainColor,
                          ColorRGB waterColor,
                          ColorRGB borderColor,
                          ColorRGB regionBorderColor,
                          ColorRGB dashColor,
                          ColorRGB textColor,
                          ColorRGB gradientMinColor,
                          ColorRGB gradientMaxColor) {
        this.backgroundColor = backgroundColor;
        this.terrainColor = terrainColor;
        this.waterColor = waterColor;
        this.borderColor = borderColor;
        this.regionBorderColor = regionBorderColor;
        this.dashColor = dashColor;
        this.textColor = textColor;
        this.gradientMinColor = gradientMinColor;
        this.gradientMaxColor = gradientMaxColor;
        
    }

    public ColorRGB getGradientMinColor() {
        return gradientMinColor;
    }

    public void setGradientMinColor(ColorRGB gradientMinColor) {
        this.gradientMinColor = gradientMinColor;
    }

    public ColorRGB getGradientMaxColor() {
        return gradientMaxColor;
    }

    public void setGradientMaxColor(ColorRGB gradientMaxColor) {
        this.gradientMaxColor = gradientMaxColor;
    }

    public ColorRGB getTextColor() {
        return textColor;
    }

    public void setTextColor(ColorRGB textColor) {
        this.textColor = textColor;
    }

    public ColorRGB getDashColor() {
        return dashColor;
    }

    public void setDashColor(ColorRGB dashColor) {
        this.dashColor = dashColor;
    }

    public ColorRGB getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(ColorRGB backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public ColorRGB getTerrainColor() {
        return terrainColor;
    }

    public void setTerrainColor(ColorRGB terrainColor) {
        this.terrainColor = terrainColor;
    }

    public ColorRGB getWaterColor() {
        return waterColor;
    }

    public void setWaterColor(ColorRGB waterColor) {
        this.waterColor = waterColor;
    }

    public ColorRGB getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(ColorRGB borderColor) {
        this.borderColor = borderColor;
    }

    public ColorRGB getRegionBorderColor() {
        return regionBorderColor;
    }

    public void setRegionBorderColor(ColorRGB regionBorderColor) {
        this.regionBorderColor = regionBorderColor;
    }


}
