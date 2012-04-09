package org.digijava.module.orgProfile.helper;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jcr.Node;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicatorCalcFormula;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.AmpMath;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.orgProfile.util.OrgProfileUtil;

/**
 *
 * @author medea
 */
public class ParisIndicatorHelper {

    private AmpAhsurveyIndicator prIndicator;// indicator
    private Long year;
    private Long fiscalCalendarId;
    private Long orgGroupId;
    private String currency;
    private TeamMember member;
    private Long[] orgIds;
    private Collection<Long> locationIds;
    private boolean showOnlyApprovedActivities;
    private List<NodeWrapper> nodesWrappers;

    public Collection<Long> getLocationIds() {
        return locationIds;
    }

    public void setLocationIds(Collection<Long> locationIds) {
        this.locationIds = locationIds;
    }

    public Long getOrgGroupId() {
        return orgGroupId;
    }

    public void setOrgGroupId(Long orgGroupId) {
        this.orgGroupId = orgGroupId;
    }

    public Long[] getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(Long[] orgIds) {
        this.orgIds = orgIds;
    }

    public Long getFiscalCalendarId() {
        return fiscalCalendarId;
    }

    public void setFiscalCalendarId(Long fiscalCalendarId) {
        this.fiscalCalendarId = fiscalCalendarId;
    }

    public TeamMember getMember() {
        return member;
    }

    public void setMember(TeamMember member) {
        this.member = member;
    }

    public long getAllDonorBaseLineValue() throws DgException {

        //apply calendar filter
        Date startDate = OrgProfileUtil.getStartDate(fiscalCalendarId, 2005);
        Date endDate = OrgProfileUtil.getEndDate(fiscalCalendarId, 2005);
        String indicatorCode = prIndicator.getIndicatorCode();
        long allDonorBaseLineValue=0;
		if (indicatorCode.equalsIgnoreCase("10b")) {
			allDonorBaseLineValue = OrgProfileUtil.getParisIndicator10bValue(2005l, nodesWrappers, null, null);
		} else {
			allDonorBaseLineValue = OrgProfileUtil.getValue(indicatorCode,
					currency, null, null, startDate, endDate, member,
					locationIds, showOnlyApprovedActivities);
		}
        return allDonorBaseLineValue;
    }

    public ParisIndicatorHelper(AmpAhsurveyIndicator prIndicator, FilterHelper helper) {
        this.prIndicator = prIndicator;
        this.year = helper.getYear();
        AmpCurrency curr = CurrencyUtil.getAmpcurrency(helper.getCurrId());
        this.currency = curr.getCurrencyCode();
        this.member = helper.getTeamMember();
        this.orgGroupId = helper.getOrgGroupId();
        this.fiscalCalendarId=helper.getFiscalCalendarId();
        this.orgIds=helper.getOrgIds();
        this.locationIds=helper.getLocationIds();


    }

    public long getAllCurrentValue() throws DgException {
        //apply calendar filter
        Date startDate = OrgProfileUtil.getStartDate(fiscalCalendarId, year.intValue());
        Date endDate = OrgProfileUtil.getEndDate(fiscalCalendarId, year.intValue());
        String indicatorCode = prIndicator.getIndicatorCode();
        long previousYearValue=0;
        if (indicatorCode.equalsIgnoreCase("10b")) {
        	previousYearValue = OrgProfileUtil.getParisIndicator10bValue(year, nodesWrappers, null, null);
		} else {
         previousYearValue =OrgProfileUtil.getValue( indicatorCode,  currency, null, null, startDate, endDate, member,locationIds, showOnlyApprovedActivities);
		}
        return previousYearValue;

    }

    public long getAllTargetValue() throws DgException {
        long targetValue = 0;
        String indicatorCode = prIndicator.getIndicatorCode();

        /* for some indicators target values are fixed,
         * others are calculating using formula.
         * See  AMP-1680, for mo details*/

        if (indicatorCode.equals("4")) {
            targetValue = 50;
        } else {
            if (indicatorCode.equals("5aii") || indicatorCode.equals("5bii")) {
                targetValue = 90;
            } else {
                if (indicatorCode.equals("9")) {
                    targetValue = 66;
                } else {
                    if (indicatorCode.equals("10a")) {
                        targetValue = 40;
                    } else {
                        AmpAhsurveyIndicatorCalcFormula formula = getFormula();
                        if (formula != null) {
                            if (formula.getEnabled() != null && formula.getEnabled()) {
                                String form = getFormulaText(formula, getAllDonorBaseLineValue());
                                targetValue = AmpMath.calcExp(form);
                            }
                        }
                    }
                }
            }
        }
        return targetValue;
    }

    private String getFormulaText(AmpAhsurveyIndicatorCalcFormula formula, long constant) {
        String flText = null;
        if (formula != null) {
            flText = formula.getCalcFormula().replace(formula.getConstantName(), String.valueOf(constant));
        }
        return flText;
    }

    public long getOrgBaseLineValue() throws DgException {
        //apply calendar filter
        Date startDate = OrgProfileUtil.getStartDate(fiscalCalendarId, 2005);
        Date endDate = OrgProfileUtil.getEndDate(fiscalCalendarId, 2005);
        String indicatorCode = prIndicator.getIndicatorCode();
        long orgBaseLineValue=0;
        if (indicatorCode.equalsIgnoreCase("10b")) {
        	orgBaseLineValue = OrgProfileUtil.getParisIndicator10bValue(2005l, nodesWrappers, orgIds, orgGroupId);
        }
        else{
        orgBaseLineValue = OrgProfileUtil.getValue(indicatorCode, currency,orgIds, orgGroupId, startDate, endDate, member,locationIds, showOnlyApprovedActivities);
        }
        return orgBaseLineValue;
    }

    public long getOrgValue() throws DgException {
        //apply calendar filter
        Date startDate = OrgProfileUtil.getStartDate(fiscalCalendarId, year.intValue());
        Date endDate = OrgProfileUtil.getEndDate(fiscalCalendarId, year.intValue());
        String indicatorCode = prIndicator.getIndicatorCode();
        long yearValue =0;
        if (indicatorCode.equalsIgnoreCase("10b")) {
        	yearValue = OrgProfileUtil.getParisIndicator10bValue(year, nodesWrappers, orgIds, orgGroupId);
        }
        else{
        	yearValue = OrgProfileUtil.getValue( indicatorCode,  currency, orgIds, orgGroupId, startDate, endDate, member,locationIds, showOnlyApprovedActivities);
        }
        return yearValue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public AmpAhsurveyIndicator getPrIndicator() {
        return prIndicator;
    }

    public void setPrIndicator(AmpAhsurveyIndicator pi) {
        this.prIndicator = pi;
    }

    public AmpAhsurveyIndicatorCalcFormula getFormula() {
        AmpAhsurveyIndicatorCalcFormula retSurvey = null;
        Set<AmpAhsurveyIndicatorCalcFormula> calcFormulas = this.prIndicator.getCalcFormulas();
        if (calcFormulas != null && !calcFormulas.isEmpty()) {
            Iterator<AmpAhsurveyIndicatorCalcFormula> itr = calcFormulas.iterator();
            retSurvey = (AmpAhsurveyIndicatorCalcFormula) itr.next();
        }

        return retSurvey;
    }

	public boolean getShowOnlyApprovedActivities() {
		return showOnlyApprovedActivities;
	}

	public void setShowOnlyApprovedActivities(boolean showOnlyApprovedActivities) {
		this.showOnlyApprovedActivities = showOnlyApprovedActivities;
	}

	public void setNodesWrappers(List<NodeWrapper> nodesWrappers) {
		this.nodesWrappers = nodesWrappers;
	}

	public List<NodeWrapper> getNodesWrappers() {
		return nodesWrappers;
	}

	
}