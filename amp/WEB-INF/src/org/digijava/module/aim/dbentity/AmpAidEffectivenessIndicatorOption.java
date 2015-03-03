package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.util.Output;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The amp aid effectiveness indicator option domain class
 * An option could be anything, not just predefined
 */
public class AmpAidEffectivenessIndicatorOption implements Serializable, Versionable, Cloneable {

    private Long ampIndicatorOptionId;
    private String ampIndicatorOptionName;
    private AmpAidEffectivenessIndicator indicator;

    public AmpAidEffectivenessIndicator getIndicator() {
        return indicator;
    }

    public void setIndicator(AmpAidEffectivenessIndicator indicator) {
        this.indicator = indicator;
    }

    public Long getAmpIndicatorOptionId() {
        return ampIndicatorOptionId;
    }

    public void setAmpIndicatorOptionId(Long ampIndicatorOptionId) {
        this.ampIndicatorOptionId = ampIndicatorOptionId == 0 ? null : ampIndicatorOptionId;
    }

    public String getAmpIndicatorOptionName() {
        return ampIndicatorOptionName;
    }

    public void setAmpIndicatorOptionName(String ampIndicatorOptionName) {
        this.ampIndicatorOptionName = ampIndicatorOptionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AmpAidEffectivenessIndicatorOption)) return false;

        AmpAidEffectivenessIndicatorOption that = (AmpAidEffectivenessIndicatorOption) o;

        if (ampIndicatorOptionId != null ? !ampIndicatorOptionId.equals(that.ampIndicatorOptionId) : that.ampIndicatorOptionId != null)
            return false;
        if (indicator != null ? !indicator.equals(that.indicator) : that.indicator != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ampIndicatorOptionId != null ? ampIndicatorOptionId.hashCode() : 0;
        result = 31 * result + (indicator != null ? indicator.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equalsForVersioning(Object o) {
        if (this == o) return true;
        if (!(o instanceof AmpAidEffectivenessIndicatorOption)) return false;

        AmpAidEffectivenessIndicatorOption that = (AmpAidEffectivenessIndicatorOption) o;

        return !(ampIndicatorOptionId != null ? !ampIndicatorOptionId.equals(that.ampIndicatorOptionId) : that.ampIndicatorOptionId != null);
    }

    @Override
    public Object getValue() {
        return this.ampIndicatorOptionName;
    }

    @Override
    public Output getOutput() {
        Output out = new Output();
        out.setOutputs(new ArrayList<Output>());
        out.getOutputs().add(new Output(null, new String[] { getIndicator().getAmpIndicatorName() }, new Object[] { this.ampIndicatorOptionName}));
        return out;
    }

    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws Exception {
        return (AmpAidEffectivenessIndicatorOption) clone();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
