package org.digijava.module.aim.dbentity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.module.aim.jackson.TranslatingConverter;

/**
 * Simple Heat Colors and Threshold storage
 * 
 * @author Nadejda Mandrescu
 */
import javax.persistence.*;

@Entity
@Table(name = "AMP_COLOR_THRESHOLD")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmpColorThreshold {
    @Id
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_COLOR_THRESHOLD_seq")
    @SequenceGenerator(name = "AMP_COLOR_THRESHOLD_seq", sequenceName = "AMP_COLOR_THRESHOLD_seq", allocationSize = 1)    @Column(name = "amp_color_threshold_id")
    private Long ampColorThresholdId;

    @Column(name = "color_name", unique = true)
    @JsonProperty("name")
    @JsonSerialize(converter = TranslatingConverter.class)
    @ApiModelProperty(example = "Dark Red")
    private String colorName;

    @Column(name = "color_hash", unique = true)
    @JsonProperty("color")
    @ApiModelProperty(example = "#d05151")
    private String colorHash;

    @Column(name = "threshold_start")
    @JsonProperty("amountFrom")

    private BigDecimal thresholdStart;

    @Column(name = "index")
    @JsonIgnore
    private Integer index;


    
    /**
     * @return the ampColorThresholdId
     */
    public Long getAmpColorThresholdId() {
        return ampColorThresholdId;
    }
    
    /**
     * @param ampColorThresholdId the ampColorThresholdId to set
     */
    public void setAmpColorThresholdId(Long ampColorThresholdId) {
        this.ampColorThresholdId = ampColorThresholdId;
    }
    

    /**
     * @return the colorName
     */
    public String getColorName() {
        return colorName;
    }

    /**
     * @param colorName the colorName to set
     */
    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    /**
     * @return the colorHash
     */
    public String getColorHash() {
        return colorHash;
    }

    /**
     * @param colorHash the colorHash to set
     */
    public void setColorHash(String colorHash) {
        this.colorHash = colorHash;
    }

    /**
     * @return the thresholdStart
     */
    public BigDecimal getThresholdStart() {
        return thresholdStart;
    }

    /**
     * @param thresholdStart the thresholdStart to set
     */
    public void setThresholdStart(BigDecimal thresholdStart) {
        this.thresholdStart = thresholdStart;
    }

    /**
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }
    
    public String toString() {
        return String.format("[%d] %s (%s) from %s (idx = %d)", ampColorThresholdId, colorName, colorHash, 
                thresholdStart, index);
    }

}
