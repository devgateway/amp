package org.digijava.module.gis.form;

import org.apache.struts.action.ActionForm;

/**
 * Form for GIS Dashboard actions.
 * @author George Kvizhinadze
 *
 */
public class GisMapLoaderForm extends ActionForm {

	private static final long serialVersionUID = 1L;

        private int canvasWidth;
        private int canvasHeight;
        private float left;
        private float right;
        private float top;
        private float bottom;
        private int action;
        private String mapCode;
        private boolean autoRect;

    public int getAction() {
        return action;
    }

    public boolean isAutoRect() {
        return autoRect;
    }

    public float getBottom() {
        return bottom;
    }

    public int getCanvasHeight() {
        return canvasHeight;
    }

    public int getCanvasWidth() {
        return canvasWidth;
    }

    public float getLeft() {
        return left;
    }

    public String getMapCode() {
        return mapCode;
    }

    public float getRight() {
        return right;
    }

    public float getTop() {
        return top;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public void setAutoRect(boolean autoRect) {
        this.autoRect = autoRect;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public void setCanvasHeight(int canvasHeight) {
        this.canvasHeight = canvasHeight;
    }

    public void setCanvasWidth(int canvasWidth) {
        this.canvasWidth = canvasWidth;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public void setMapCode(String mapCode) {
        this.mapCode = mapCode;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public void setTop(float top) {
        this.top = top;
    }

}
