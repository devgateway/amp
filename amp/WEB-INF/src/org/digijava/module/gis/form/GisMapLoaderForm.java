package org.digijava.module.gis.form;

import org.apache.struts.action.ActionForm;

/**
 * Form for GIS Dashboard actions.
 * @author George Kvizhinadze
 *
 */
public class GisMapLoaderForm extends ActionForm {

	private static final long serialVersionUID = 1L;

        private int canvasXRes;
        private int canvasYRes;
        private float mapLeftX;
        private float mapRightX;
        private float mapTopY;
        private float mapLowY;
    public int getCanvasXRes() {
        return canvasXRes;
    }

    public int getCanvasYRes() {
        return canvasYRes;
    }

    public float getMapLeftX() {
        return mapLeftX;
    }

    public float getMapLowY() {
        return mapLowY;
    }

    public float getMapRightX() {
        return mapRightX;
    }

    public float getMapTopY() {
        return mapTopY;
    }

    public void setCanvasXRes(int canvasXRes) {
        this.canvasXRes = canvasXRes;
    }

    public void setCanvasYRes(int canvasYRes) {
        this.canvasYRes = canvasYRes;
    }

    public void setMapLeftX(float mapLeftX) {
        this.mapLeftX = mapLeftX;
    }

    public void setMapLowY(float mapLowY) {
        this.mapLowY = mapLowY;
    }

    public void setMapRightX(float mapRightX) {
        this.mapRightX = mapRightX;
    }

    public void setMapTopY(float mapTopY) {
        this.mapTopY = mapTopY;
    }

}
