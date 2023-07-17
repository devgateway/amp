package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.util.Output;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The amp aid effectiveness indicator option domain class
 * An option could be anything, not just predefined
 */
import javax.persistence.*;

@Entity
@Table(name = "AMP_AID_EFFECTIVENESS_INDICATOR_OPTION")
public class AmpAidEffectivenessIndicatorOption implements Serializable, Versionable, Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_indicator_option_seq_generator")
    @SequenceGenerator(name = "amp_indicator_option_seq_generator", sequenceName = "AMP_AID_EFFECTIVENESS_INDICATOR_OPTION_seq", allocationSize = 1)
    @Column(name = "amp_indicator_option_id")
    private Long ampIndicatorOptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_indicator_id")
    private AmpAidEffectivenessIndicator indicator;

    @Column(name = "amp_indicator_option_name")
    private String ampIndicatorOptionName;


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
    /**
     * Having indicator here is essential because 2 options with null ids could be connected to different indicators
     * And these should be treated as 2 different options
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AmpAidEffectivenessIndicatorOption)) return false;

        AmpAidEffectivenessIndicatorOption that = (AmpAidEffectivenessIndicatorOption) o;

        if (ampIndicatorOptionId != null && that.ampIndicatorOptionId != null) {
            return ampIndicatorOptionId.equals(that.ampIndicatorOptionId);
        } else if (indicator != null && that.indicator != null) {
            return indicator.equals(that.indicator);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result = 0;
        if (ampIndicatorOptionId != null) {
            result = ampIndicatorOptionId.hashCode();
        } else if (indicator != null) {
            result = indicator.hashCode();
        }
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
