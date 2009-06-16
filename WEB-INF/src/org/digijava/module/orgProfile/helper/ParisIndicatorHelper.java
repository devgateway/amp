package org.digijava.module.orgProfile.helper;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpOrganisation;

import org.digijava.module.aim.dbentity.AmpAhsurveyIndicatorCalcFormula;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.AmpMath;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.orgProfile.util.OrgProfileUtil;

/**
 *
 * @author medea
 */
public class ParisIndicatorHelper {

    private AmpAhsurveyIndicator prIndicator;// indicator
    private AmpOrganisation organization;
    private Long year;
    private Long fiscalCalendarId;
    private AmpOrgGroup orgGroup;
    private String currency;
    private TeamMember member;

    public Long getFiscalCalendarId() {
        return fiscalCalendarId;
    }

    public void setFiscalCalendarId(Long fiscalCalendarId) {
        this.fiscalCalendarId = fiscalCalendarId;
    }

    public AmpOrgGroup getOrgGroup() {
        return orgGroup;
    }

    public void setOrgGroup(AmpOrgGroup orgGroup) {
        this.orgGroup = orgGroup;
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
        long allDonorBaseLineValue = OrgProfileUtil.getValue( indicatorCode, currency, null, null, startDate, endDate, member);
        return allDonorBaseLineValue;
    }

    public ParisIndicatorHelper(AmpAhsurveyIndicator prIndicator, FilterHelper helper,boolean previousYear) {
        this.prIndicator = prIndicator;
        this.organization = helper.getOrganization();
        if (previousYear) {
            // in the org profile we are interested in previous year value according to specs.
            this.year = helper.getYear() - 1;
        } else {
            this.year = helper.getYear();
        }
        AmpCurrency curr = CurrencyUtil.getAmpcurrency(helper.getCurrId());
        this.currency = curr.getCurrencyCode();
        this.member = helper.getTeamMember();
        this.orgGroup = helper.getOrgGroup();
        this.fiscalCalendarId=helper.getFiscalCalendarId();


    }

    public long getAllCurrentValue() throws DgException {
        //apply calendar filter
        Date startDate = OrgProfileUtil.getStartDate(fiscalCalendarId, year.intValue());
        Date endDate = OrgProfileUtil.getEndDate(fiscalCalendarId, year.intValue());
        String indicatorCode = prIndicator.getIndicatorCode();
        long previousYearValue =OrgProfileUtil.getValue( indicatorCode,  currency, null, null, startDate, endDate, member);;
        return previousYearValue;

    }

    public long getAllTargetValue() throws DgException {
        long targetValue = 0;
        AmpAhsurveyIndicatorCalcFormula formula = getFormula();
        if (formula != null) {
            if (formula.getEnabled() != null && formula.getEnabled()) {

                String form = getFormulaText(formula, getAllDonorBaseLineValue());

                targetValue = AmpMath.calcExp(form);

            } else {
                if (formula.getTargetValue() != null && !formula.getTargetValue().equals("")) {
                    targetValue = Long.parseLong(formula.getTargetValue());
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
        long orgBaseLineValue = OrgProfileUtil.getValue(indicatorCode, currency, organization.getAmpOrgId(), orgGroup.getAmpOrgGrpId(), startDate, endDate, member);
        return orgBaseLineValue;
    }

    public long getOrgValue() throws DgException {
        //apply calendar filter
        Date startDate = OrgProfileUtil.getStartDate(fiscalCalendarId, year.intValue());
        Date endDate = OrgProfileUtil.getEndDate(fiscalCalendarId, year.intValue());
        String indicatorCode = prIndicator.getIndicatorCode();
        long previousYearValue = OrgProfileUtil.getValue( indicatorCode,  currency, organization.getAmpOrgId(), orgGroup.getAmpOrgGrpId(), startDate, endDate, member);;

        return previousYearValue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public AmpOrganisation getOrganization() {
        return organization;
    }

    public void setOrganization(AmpOrganisation organization) {
        this.organization = organization;
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
  
}
