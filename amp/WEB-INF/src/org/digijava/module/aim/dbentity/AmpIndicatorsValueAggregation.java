package org.digijava.module.aim.dbentity;

import org.apache.log4j.Logger;
import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.common.util.DateTimeUtil;

import java.io.Serializable;
import java.util.Date;

public class AmpIndicatorsValueAggregation implements Serializable, Cloneable {
    private static final Logger logger = Logger.getLogger(AmpIndicatorsValueAggregation.class);
    /*
     * NOTICE
     *
     * When adding new fields please update the clone() method {@link #clone()}
     * so that information is not lost.
     *
     * Thanks!
     */

    private static final long serialVersionUID = 1L;

    private Long indValId;

    @Interchangeable(fieldTitle = "Comment", importable = true)
    private String comment;

    @Interchangeable(fieldTitle = "Date", importable = true)
    private Date valueDate;

    private Date dataIntervalStart;
    private Date dataIntervalEnd;

    @Interchangeable(fieldTitle = "Value", importable = true)
    private Double value;

    private int valueType;

    private String valueName;
    private Boolean defaultInd;

    @InterchangeableBackReference
    private IndicatorConnection indicatorConnection;

    private AmpLocation location;

    private AmpCategoryValue indicatorSource;
    private AmpIndicatorSubgroup subgroup;
    /*
     * NOTICE
     *
     * When adding new fields please update the clone() method {@link #clone()}
     * so that information is not lost.
     *
     * Thanks!
     */

    public AmpIndicatorsValueAggregation() {
    }

    public AmpIndicatorsValueAggregation(int valueType) {
        this.valueType = valueType;
    }

    /*these  helper methods are used
     to set and get date object value to(from) the date input field
    see addEditValue.jsp*/
    public String getValueDateString(){
        if( valueDate!=null){
            return  DateTimeUtil.formatDateLocalized(valueDate);
        }
        return null;
    }

    public void setValueDateString(String valueDateString) throws Exception{
        if(!valueDateString.trim().equals("")){
            valueDate=DateTimeUtil.parseDate(valueDateString);
        }
    }

    public Long getIndValId() {
        return indValId;
    }
    public void setIndValId(Long indValId) {
        this.indValId = indValId;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public Date getValueDate() {
        return valueDate;
    }
    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }
    public Double getValue() {
        return value;
    }
    public void setValue(Double value) {
        this.value = value;
    }
    public int getValueType() {
        return valueType;
    }
    public void setValueType(int valueType) {
        this.valueType = valueType;
    }
    public IndicatorConnection getIndicatorConnection() {
        return indicatorConnection;
    }
    public void setIndicatorConnection(IndicatorConnection indicatorConnection) {
        this.indicatorConnection = indicatorConnection;
    }
    public Boolean getDefaultInd() {
        return defaultInd;
    }
    public void setDefaultInd(Boolean defaultInd) {
        this.defaultInd = defaultInd;
    }
    public AmpLocation getLocation() {
        return location;
    }

    public AmpIndicatorSubgroup getSubgroup() {
        return subgroup;
    }

    public Date getDataIntervalEnd() {
        return dataIntervalEnd;
    }

    public Date getDataIntervalStart() {
        return dataIntervalStart;
    }

    public void setLocation(AmpLocation location) {
        this.location = location;
    }

    public void setSubgroup(AmpIndicatorSubgroup subgroup) {
        this.subgroup = subgroup;
    }

    public void setDataIntervalEnd(Date dataIntervalEnd) {
        this.dataIntervalEnd = dataIntervalEnd;
    }

    public void setDataIntervalStart(Date dataIntervalStart) {
        this.dataIntervalStart = dataIntervalStart;
    }


    public AmpCategoryValue getIndicatorSource() {
        return indicatorSource;
    }

    public void setIndicatorSource(AmpCategoryValue indicatorSource) {
        this.indicatorSource = indicatorSource;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }
    public AmpIndicatorSource getSource() {
        return null;
    }

    public void setSource(AmpIndicatorSource source) {

    }


    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            logger.error("Clone not supported!", e);
            return null;
        }
    }
    /*
    @Override
    public AmpIndicatorValue clone() {
        AmpIndicatorValue r = new AmpIndicatorValue();

        r.setValue(value);
        r.setValueDate(valueDate);
        r.setComment(comment);
        r.setIndValId(indValId);
        r.setDataIntervalStart(dataIntervalStart);
        r.setDataIntervalEnd(dataIntervalEnd);
        r.setValueType(valueType);
        r.setLogFrame(logFrame);
        r.setRisk(risk);
        r.setDefaultInd(defaultInd);
        r.setIndicatorConnection(indicatorConnection);
        r.setLocation(location);
        r.setIndicatorSource(indicatorSource);
        r.setSubgroup(subgroup);

        return r;
    }
    */
    public void copyValuesTo(AmpIndicatorsValueAggregation r) {
        r.setValue(value);
        r.setValueName(valueName);
        r.setValueDate(valueDate);
        r.setComment(comment);
        r.setIndValId(indValId);
        r.setDataIntervalStart(dataIntervalStart);
        r.setDataIntervalEnd(dataIntervalEnd);
        r.setValueType(valueType);
        //r.setLogFrame(logFrame);
        //r.setRisk(risk);
        //r.setIndicatorConnection(indicatorConnection);
        r.setDefaultInd(defaultInd);
        //r.setLocation(location);
        //r.setIndicatorSource(indicatorSource);
        //r.setSubgroup(subgroup);
    }

}
