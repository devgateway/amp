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
 * @author not attributable
 * @version 1.0
 */
public class MapCodeLevelPair {
    private String mapCode;
    private int mapLevel;

    public MapCodeLevelPair() {
    }

    public MapCodeLevelPair(String mapCode, int mapLevel) {
        this.mapCode = mapCode;
        this.mapLevel = mapLevel;
    }

    public String getMapCode() {
        return mapCode;
    }

    public int getMapLevel() {
        return mapLevel;
    }

    public void setMapCode(String mapCode) {
        this.mapCode = mapCode;
    }

    public void setMapLevel(int mapLevel) {
        this.mapLevel = mapLevel;
    }

    public boolean equals(Object compareWith){
        return (this.mapCode.equals(((MapCodeLevelPair)compareWith).mapCode) && this.mapLevel == ((MapCodeLevelPair)compareWith).mapLevel);
    }

    public int hashCode(){
        return this.mapCode.hashCode() + this.mapLevel;
    }

}
