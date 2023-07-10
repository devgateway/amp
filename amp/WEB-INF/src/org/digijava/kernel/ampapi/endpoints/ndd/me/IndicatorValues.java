package org.digijava.kernel.ampapi.endpoints.ndd.me;

public class IndicatorValues {
    private MeValue actual;
    private MeValue base;
    private MeValue target;

    public MeValue getActual () {
        return actual;
    }

    public void setActual (MeValue actual) {
        this.actual = actual;
    }

    public MeValue getBase () {
        return base;
    }

    public void setBase (MeValue base) {
        this.base = base;
    }

    public MeValue getTarget () {
        return target;
    }

    public void setTarget (MeValue target) {
        this.target = target;
    }
}
