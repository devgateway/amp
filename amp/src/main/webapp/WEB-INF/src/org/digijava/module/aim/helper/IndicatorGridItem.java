package org.digijava.module.aim.helper;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.digijava.module.aim.dbentity.AmpAuditLogger;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.DbUtil.AmpIndicatorValuesComparatorByTypeAndYear;

import java.util.Collections;

/**
 * Indicator value helper bean. Stores indicator name and both values, target,
 * and latest actual value for one particular year. Used in reports and grids on
 * NPD inside the IndicatorGridRow beans
 * 
 * @see org.digijava.module.aim.helper.IndicatorGridRow
 * @author Irakli Kobiashvili ikobiashvili@picktek.com
 * 
 */
public class IndicatorGridItem {
    private Long valueId;
    private Long indicatorId;
    private String year;
    private String actualValue = " - ";
    private String targetValue = " - ";
    private String baseValue = " - ";

    /**
     * Constructs new object from set of indicator values
     * 
     * @param year
     *            string representing year
     * @param values
     *            set of AmpThemeIndicatorValue beans
     * @see org.digijava.module.aim.dbentity.AmpThemeIndicatorValue
     */
    public IndicatorGridItem(String year, Set<AmpIndicatorValue> values) {
        this.year = year;
        int yearValue = Integer.valueOf(this.year).intValue();
        AmpIndicatorValue actualValue = null;
        AmpIndicatorValue baseValue = null;
        AmpIndicatorValue targetValue = null;

        if (values != null) {
            // Look for the Actual value for this particular year.
            // If there are more than 1 Actual values then select the latest.
            Iterator<AmpIndicatorValue> iValues = values.iterator();
            while (iValues.hasNext()) {
                AmpIndicatorValue auxValue = iValues.next();
                if (auxValue.getValueType() == 1) {
                    Date auxCreationDate = auxValue.getValueDate();
                    if (auxCreationDate != null) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(auxCreationDate);
                        int creationYear = cal.get(Calendar.YEAR);
                        if (creationYear == yearValue) {
                            if ((actualValue == null || auxCreationDate.compareTo(actualValue.getValueDate()) > 0)) {
                                actualValue = auxValue;
                            }
                        }
                    }
                }
            }

            // Look for the Base value and Target for this particular year.
            // NOTE: If we don't have an Actual value, then select Base and
            // Target anyway.
            iValues = values.iterator();
            while (iValues.hasNext()) {
                AmpIndicatorValue auxValue = iValues.next();
                Date auxCreationDate = auxValue.getValueDate();
                int auxCreationYear = 0;
                if (auxCreationDate != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(auxCreationDate);
                    auxCreationYear = cal.get(Calendar.YEAR);
                }
                if (auxCreationYear == yearValue) {
                    // Base value.
                    if (auxValue.getValueType() == 2) {
                        if (actualValue != null) {
                            // Check this date is before the Actual date.
                            if (auxCreationDate.compareTo(actualValue.getValueDate()) <= 0) {
                                if (baseValue == null) {
                                    baseValue = auxValue;
                                } else {
                                    // If we have more than 1 possible Base
                                    // values
                                    // then select the highest.
                                    if (auxValue.getValueDate().compareTo(baseValue.getValueDate()) > 0) {
                                        baseValue = auxValue;
                                    }
                                }
                            }
                        } else {
                            // Just select latest Base date.
                            if (baseValue == null) {
                                baseValue = auxValue;
                            } else {
                                // If we have more than 1 possible Base
                                // values
                                // then select the highest.
                                if (auxValue.getValueDate().compareTo(baseValue.getValueDate()) > 0) {
                                    baseValue = auxValue;
                                }
                            }
                        }
                    }
                    // Target value.
                    if (auxValue.getValueType() == 0) {
                        if (actualValue != null) {
                            // Check this date is before the Actual date.
                            if (auxCreationDate.compareTo(actualValue.getValueDate()) >= 0) {
                                if (targetValue == null) {
                                    targetValue = auxValue;
                                } else {
                                    // If we have more than 1 possible Target
                                    // values
                                    // then select the highest.
                                    if (auxValue.getValueDate().compareTo(targetValue.getValueDate()) > 0) {
                                        targetValue = auxValue;
                                    }
                                }
                            }
                        } else {
                            // Just select latest Target date.
                            if (targetValue == null) {
                                targetValue = auxValue;
                            } else {
                                // If we have more than 1 possible Target
                                // values
                                // then select the highest.
                                if (auxValue.getValueDate().compareTo(targetValue.getValueDate()) > 0) {
                                    targetValue = auxValue;
                                }
                            }
                        }
                    }
                }
            }

            // If we didn't find the Base value in this year, then look for the
            // Base value in the nearest year.
            iValues = values.iterator();
            while (iValues.hasNext()) {
                AmpIndicatorValue auxValue = iValues.next();
                Date auxCreationDate = auxValue.getValueDate();
                int auxCreationYear = 0;
                if (auxCreationDate != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(auxCreationDate);
                    auxCreationYear = cal.get(Calendar.YEAR);
                }
                if (auxCreationYear < yearValue) {
                    // Base value.
                    if (auxValue.getValueType() == 2) {
                        if (baseValue == null) {
                            baseValue = auxValue;
                        } else {
                            // If we have more than 1 possible Base values
                            // then select the highest (nearest to the
                            // Actual value)
                            if (auxValue.getValueDate().compareTo(baseValue.getValueDate()) > 0) {
                                baseValue = auxValue;
                            }
                        }
                    }
                }
            }

            // If we didn't find the Target value in this year, then look for
            // the
            // Target value in the nearest next year.
            iValues = values.iterator();
            while (iValues.hasNext()) {
                AmpIndicatorValue auxValue = iValues.next();
                Date auxCreationDate = auxValue.getValueDate();
                int auxCreationYear = 0;
                if (auxCreationDate != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(auxCreationDate);
                    auxCreationYear = cal.get(Calendar.YEAR);
                }
                if (auxCreationYear > yearValue) {
                    // Target value.
                    if (auxValue.getValueType() == 0) {
                        if (targetValue == null) {
                            targetValue = auxValue;
                        } else {
                            // If we have more than 1 possible Target values
                            // then select the lowest (nearest to the
                            // Actual value)
                            if (auxValue.getValueDate().compareTo(targetValue.getValueDate()) < 0) {
                                targetValue = auxValue;
                            }
                        }
                    }
                }
            }
        }
        if (actualValue != null) {
            this.actualValue = actualValue.getValue().toString();
        }
        if (baseValue != null) {
            this.baseValue = baseValue.getValue().toString();
        }
        if (targetValue != null) {
            this.targetValue = targetValue.getValue().toString();
        }
    }

    public String getActualValue() {
        return actualValue;
    }

    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }

    public Long getIndicatorId() {
        return indicatorId;
    }

    public void setIndicatorId(Long indicatorId) {
        this.indicatorId = indicatorId;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public Long getValueId() {
        return valueId;
    }

    public void setValueId(Long valueId) {
        this.valueId = valueId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(String baseValue) {
        this.baseValue = baseValue;
    }
}
