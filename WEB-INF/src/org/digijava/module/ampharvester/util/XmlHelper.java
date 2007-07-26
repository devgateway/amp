package org.digijava.module.ampharvester.util;

import java.util.ArrayList;
import java.util.List;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpPerspective;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.ampharvester.exception.AmpHarvesterException;
import org.digijava.module.ampharvester.jaxb10.FundingDetailType;
import org.digijava.module.ampharvester.jaxb10.LocationType;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.digijava.module.aim.helper.Constants;

public class XmlHelper {
  public static final String siteId = "amp";

  public static List<AmpRegionalFunding> getAmpRegionalFunding(LocationType location, AmpActivity activity, Session session) throws AmpHarvesterException,
      DgException, HibernateException {
    List<AmpRegionalFunding> retValue = new ArrayList();
    AmpRegion ampRegion = DbUtil.getAmpRegion(location.getRegion().trim(), session);
    if (ampRegion == null) {
      throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_REGION, location.getRegion());
    }

    if (location.getCommitment() != null) {
      for (Object elem : location.getCommitment()) {
        FundingDetailType fdt = (FundingDetailType)elem;
        AmpRegionalFunding arf = getAmpRegionalFunding(fdt, session);
        arf.setTransactionType(new Integer(Constants.COMMITMENT));
        arf.setRegion(ampRegion);
        arf.setActivity(activity);
        retValue.add(arf);
      }
    }
    if (location.getDisbursement() != null) {
      for (Object elem : location.getDisbursement()) {
        FundingDetailType fdt = (FundingDetailType)elem;
        AmpRegionalFunding arf = getAmpRegionalFunding(fdt, session);
        arf.setTransactionType(new Integer(Constants.DISBURSEMENT));
        arf.setRegion(ampRegion);
        arf.setActivity(activity);
        retValue.add(arf);
      }
    }
    if (location.getExpenditure() != null) {
      for (Object elem : location.getExpenditure()) {
        FundingDetailType fdt = (FundingDetailType)elem;
        AmpRegionalFunding arf = getAmpRegionalFunding(fdt, session);
        arf.setTransactionType(new Integer(Constants.EXPENDITURE));
        arf.setRegion(ampRegion);
        arf.setActivity(activity);
        retValue.add(arf);
      }
    }
    return retValue;
  }

  public static AmpRegionalFunding getAmpRegionalFunding(FundingDetailType fdt, Session session) throws AmpHarvesterException, DgException, HibernateException {
    AmpRegionalFunding retValue = new AmpRegionalFunding();
    retValue.setAdjustmentType(Integer.valueOf(fdt.getAdjustmentType()));
    retValue.setTransactionDate(fdt.getDate().getTime());
    retValue.setTransactionAmount(new Double(fdt.getAmount()));
    AmpCurrency ampCurrency = DbUtil.getAmpCurrency(fdt.getCurrencyCode(), session);
    if (ampCurrency == null) {
      throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_CURRENCY, fdt.getCurrencyCode());
    }
    retValue.setCurrency(ampCurrency);
    AmpPerspective ampPerspective = DbUtil.getAmpPerspective(fdt.getPerspectiveType(), session); /** @todo will be change from ID to CODE */
    if (ampPerspective == null) {
      throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_PERSPECTIVE, Integer.toString(fdt.getPerspectiveType()));
    }
    retValue.setPerspective(ampPerspective);
    return retValue;
  }

  public static AmpFundingDetail getAmpFundingDetail(FundingDetailType fdt, Integer type, AmpFunding ampFunding, Session session) throws AmpHarvesterException,
      DgException, HibernateException {
    AmpFundingDetail retValue = new AmpFundingDetail();
    retValue.setAdjustmentType(Integer.valueOf(fdt.getAdjustmentType()));
    retValue.setTransactionDate(fdt.getDate().getTime());
    retValue.setTransactionAmount(new Double(fdt.getAmount()));
    AmpCurrency ampCurrency = DbUtil.getAmpCurrency(fdt.getCurrencyCode(), session);
    if (ampCurrency == null) {
      throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_CURRENCY, fdt.getCurrencyCode());
    }
    retValue.setAmpCurrencyId(ampCurrency);
    AmpPerspective ampPerspective = DbUtil.getAmpPerspective(fdt.getPerspectiveType(), session); /** @todo will be change from ID to CODE */
    if (ampPerspective == null) {
      throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_PERSPECTIVE, Integer.toString(fdt.getPerspectiveType()));
    }
    retValue.setPerspectiveId(ampPerspective);
    retValue.setTransactionType(type);
    retValue.setAmpFundingId(ampFunding);
    return retValue;
  }

  public static AmpComponentFunding getAmpComponentFunding(FundingDetailType fdt, Integer type, Session session) throws AmpHarvesterException, DgException,
      HibernateException {
    AmpComponentFunding retValue = new AmpComponentFunding();
    retValue.setAdjustmentType(Integer.valueOf(fdt.getAdjustmentType()));
    retValue.setTransactionDate(fdt.getDate().getTime());
    retValue.setTransactionAmount(new Double(fdt.getAmount()));
    AmpCurrency ampCurrency = DbUtil.getAmpCurrency(fdt.getCurrencyCode(), session);
    if (ampCurrency == null) {
      throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_CURRENCY, fdt.getCurrencyCode());
    }
    retValue.setCurrency(ampCurrency);
    AmpPerspective ampPerspective = DbUtil.getAmpPerspective(fdt.getPerspectiveType(), session); /** @todo will be change from ID to CODE */
    if (ampPerspective == null) {
      throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_PERSPECTIVE, Integer.toString(fdt.getPerspectiveType()));
    }
    retValue.setPerspective(ampPerspective);
    retValue.setTransactionType(type);
    return retValue;
  }

}
