package org.dgfoundation.amp.gpi.reports;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import org.dgfoundation.amp.nireports.MonetaryAmount;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.TranslatedDate;

/**
 * <strong>This class is deeply immutable</strong>
 * 
 * @author Viorel Chihai
 *
 */
public final class GPIAmount {

    /**
     * the amount stored in this cell, plus some accessory information like the
     * date of the transaction.
     */
    public final MonetaryAmount amount;

    /**
     * the effective date
     */
    public final TranslatedDate translatedDate;

    public final LocalDate transactionMoment;
    
    public final Long donorId;

    public final String donorName;
    
    public final String donorGroup;

    public GPIAmount(Long donorId, String donorName, String donorGroup, MonetaryAmount amount, Date transactionMoment,
            TranslatedDate translatedDate) {

        this.donorId = donorId;
        this.donorName = donorName;
        this.donorGroup = donorGroup;
        this.amount = amount;
        this.transactionMoment = transactionMoment.toLocalDate();
        this.translatedDate = translatedDate;
    }

    public TranslatedDate getTranslatedDate() {
        return translatedDate;
    }

    public int compareTo(Object o) {
        GPIAmount gac = (GPIAmount) o;
        return amount.compareTo(gac.amount);
    }

    public String getDisplayedValue() {
        return amount.getDisplayable();
    }

    public BigDecimal getAmount() {
        return amount.amount;
    }

    public Long getDonorId() {
        return donorId;
    }

    public String getDonorName() {
        return donorName;
    }

    public String getDonorGroup() {
        return donorGroup;
    }

    public LocalDate getTransactionMoment() {
        return transactionMoment;
    }

    public NiPrecisionSetting getPrecision() {
        return amount.precisionSetting;
    }

    public boolean isScalableByUnits() {
        return true;
    }

    public String getTranslatedYear() {
        return translatedDate.year.getValue();
    }

    public int getYear() {
        return transactionMoment.getYear();
    }
}
