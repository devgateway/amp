package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.nireports.meta.CategCell;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * the most widely instantiated cell in NiReports - the one holding "funding" (e.g. regular hierarchies-abiding numerical cells).
 * A CategAmountCell is an amount with a date ({@link #translatedDate}) and a currency which has attached both coordinates and metadata. The coordinates obey the same contract as {@link Cell#getCoordinates()},
 * while the metadata is an opaque map from String to Object. NiReports Core ignores the metadata - it is up to the schema to populate and interpret it (probably via Behaviour subclasses or a {@link BasicFiltersConverter} subclass). <br />
 * You can notice that this class (and its enclosed instances of other classes) offer lots of data which is ignored by Core with the expectation that they would be useful for real-life usage (for example: {@link MonetaryAmount#origAmount}, {@link MonetaryAmount#origCurrency})
 * 
 * Because measure cells cannot be used as bases for a hierarchy, they universally have {@link Cell#entityId} = -1 and {@link Cell#mainLevel} is empty(). <br />
 * <strong>This class is deeply immutable</strong>
 * 
 * @author Dolghier Constantin
 *
 */
public final class CategAmountCell extends Cell implements CategCell, DatedCell, NumberedCell {
        
    /**
     * the amount stored in this cell, plus some accessory information like the date of the transaction.
     */
    public final MonetaryAmount amount;
    
    /**
     * the opaque metadata 
     */
    public final MetaInfoSet metaInfo;
    
    /**
     * the effective date of the cell - to be used while V-splitting reports 
     */
    public final TranslatedDate translatedDate;
    
    /**
     * is amount informative
     */
    private final Boolean isInformativeAmount;
    
    /**
     * constructs an instance which has its fields trivially set to the supplied arguments. entityId will be set as -1, levelColumn will be set to empty() 
     * @param activityId the owning "activity" (fundamental entity)
     * @param amount  
     * @param metaInfo
     * @param coos
     * @param translatedDate
     */
    public CategAmountCell(long activityId, MonetaryAmount amount, MetaInfoSet metaInfo, Map<NiDimensionUsage, Coordinate> coos, TranslatedDate translatedDate) {
        this(activityId, amount, metaInfo, coos, translatedDate, true);
    }
    
    /**
     * constructs an instance which has its fields trivially set to the supplied arguments. 
     * entityId will be set as -1, levelColumn will be set to empty() 
     * 
     * @param activityId the owning "activity" (fundamental entity)
     * @param amount  
     * @param metaInfo
     * @param coos
     * @param translatedDate
     */
    public CategAmountCell(long activityId, MonetaryAmount amount, MetaInfoSet metaInfo, 
            Map<NiDimensionUsage, Coordinate> coos, TranslatedDate translatedDate, Boolean isInformative) {
        super(activityId, -1, coos, Optional.empty());
        this.amount = amount;
        this.metaInfo = metaInfo.freeze();
        this.translatedDate = translatedDate;
        this.isInformativeAmount = isInformative;
    }

    /**
     * creates a new cell which is a clone of this one, save for the metadata which gets added a new entry
     * @param cat the category of the metadata entry to add
     * @param value the value of the metadata entry to add
     * @return
     */
    public CategAmountCell withMeta(String cat, Object value) {
        return new CategAmountCell(this.activityId, this.amount, this.metaInfo.newInstance(cat, value), this.coordinates, this.translatedDate);
    }
    
    /**
     * creates a new cell with stripped coordinates (therefore yield a cell which ignores transaction-level hierarchies and filters)
     * @return
     */
    public CategAmountCell withStrippedCoords() {
        return new CategAmountCell(this.activityId, this.amount, this.metaInfo, Collections.emptyMap(), this.translatedDate);
    }
    
    /**
     * creates a new cell which is a clone of this one, save for the {@link #amount} which is multiplied by a given value
     * @param cat the category of the metadata entry to add
     * @param value the value of the metadata entry to add
     * @return
     */
    public CategAmountCell multiply(BigDecimal multipland) {
        if (multipland.equals(BigDecimal.ONE))
            return this;
        return new CategAmountCell(this.activityId, this.amount.multiplyBy(multipland), this.metaInfo, this.coordinates, this.translatedDate);
    }

    @Override
    public Cell changeOwnerId(long newActivityId) {
        return new CategAmountCell(newActivityId, this.amount, this.metaInfo, this.coordinates, this.translatedDate);
    }

    @Override
    public String toString() {
        return String.format("(actId: %d, amt: %s, coos: {%s}, meta: {%s}", this.activityId, amount, AmpCollections.sortedMap(coordinates, (a, b) -> a.toString().compareTo(b.toString())), metaInfo);
    }

    @Override
    public MetaInfoSet getMetaInfo() {
        return this.metaInfo;
    }

    @Override
    public TranslatedDate getTranslatedDate() {
        return translatedDate;
    }

    @Override
    public int compareTo(Object o) {
        CategAmountCell cac = (CategAmountCell) o;
        return amount.compareTo(cac.amount);
    }

    @Override
    public String getDisplayedValue() {
        return amount.getDisplayable();
    }

    @Override
    public BigDecimal getAmount() {
        return amount.amount;
    }
    
    @Override
    public NiPrecisionSetting getPrecision() {
        return amount.precisionSetting;
    }
    
    public Boolean isInformativeAmount() {
        return isInformativeAmount;
    }

    @Override
    public boolean isScalableByUnits() {
        return true;
    }

}
