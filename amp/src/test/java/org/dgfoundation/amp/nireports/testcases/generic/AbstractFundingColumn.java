package org.dgfoundation.amp.nireports.testcases.generic;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.codegenerators.FundingCellEntry;
import org.dgfoundation.amp.codegenerators.FundingColumnGenerator;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.MonetaryAmount;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.TranslatedDate;
import org.dgfoundation.amp.nireports.meta.MetaInfo;
import org.dgfoundation.amp.nireports.meta.MetaInfoGenerator;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.nireports.testcases.IHardcodedNames;
import org.dgfoundation.amp.nireports.testcases.TestModelConstants;
import org.dgfoundation.amp.nireports.testcases.TestcasesPrecisionSetting;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * a Funding Column which gets its data from a source which encodes the cells independent of the schema. 
 * Supplies various utilities to be used by the concrete subclasses (which construct cells either from hardcoded-in-src data OR hardcoded-in-files data); also supplies a method for reading cells off a binary dump created through {@link FundingColumnGenerator#binaryDump()}
 * (please see {@link #decodeCells(InputStream)})
 * @author Dolghier Constantin
 *
 */
public abstract class AbstractFundingColumn extends HardcodedCells<CategAmountCell> {

    protected final NiDimension cats;
    protected final NiDimensionUsage donorDimUsg;
    protected final MetaInfoGenerator mig = new MetaInfoGenerator();
    protected final NiPrecisionSetting precisionSetting = new TestcasesPrecisionSetting();
    protected final Map<String, Map<String, Long>> fundingIds;
    
    protected AbstractFundingColumn(Map<String, Long> activityNames, NiDimension cats, NiDimensionUsage donorDimUsg, IHardcodedNames fundingIdsSrc) {
        super(activityNames, activityNames, null);
        this.cats = cats;
        this.donorDimUsg = donorDimUsg;
        this.fundingIds = fundingIdsSrc.getParams();
    }

    protected CategAmountCell cell(String amount, String activityTitle, Integer year, String month, 
            String pledge, String transaction_type, String agreement, String recipient_org, 
            String recipient_role, String source_role, String adjustment_type,
            String donor_org, String funding_status, String mode_of_payment, 
            String terms_assist, String financing_instrument, String transaction_date) {
                
        LocalDate parsedDate = LocalDate.parse(transaction_date, DateTimeFormatter.ISO_LOCAL_DATE);
        if (year == null) {
            year = parsedDate.getYear();
            month = months[parsedDate.getMonth().getValue()];
        }
        Map<NiDimensionUsage, Coordinate> coos = new HashMap<NiDimensionUsage, Coordinate>();
        MetaInfoSet mis = new MetaInfoSet(mig);
        TranslatedDate td = new GregorianTestDateGenerator(year, month).toTranslatedDate();
        addToMetaIfExists(TestModelConstants.PLEDGE_ID, pledge, mis);
        addToMetaIfExists(TestModelConstants.TRANSACTION_TYPE, transaction_type, mis);
        addToMetaIfExists(TestModelConstants.AGREEMENT_ID, agreement, mis);
        addToMetaIfExists(TestModelConstants.RECIPIENT_ORG, recipient_org, mis);
        addToMetaIfExistsDirectly(TestModelConstants.RECIPIENT_ROLE, recipient_role, mis);
        addToMetaIfExistsDirectly(TestModelConstants.SOURCE_ROLE, source_role, mis);
        addToMetaIfExistsDirectly(TestModelConstants.ADJUSTMENT_TYPE, adjustment_type, mis);
        addToCoordsIfExists(donorDimUsg.getLevelColumn(2), TestModelConstants.DONOR_ORG_ID, donor_org, coos);
        addToCoordsIfExists(degenerate(cats, ColumnConstants.FUNDING_STATUS), TestModelConstants.FUNDING_STATUS_ID, funding_status, coos);
        addToCoordsIfExists(degenerate(cats, ColumnConstants.MODE_OF_PAYMENT), TestModelConstants.MODE_OF_PAYMENT_ID, mode_of_payment, coos);
        addToCoordsIfExists(degenerate(cats, ColumnConstants.TYPE_OF_ASSISTANCE), TestModelConstants.TERMS_ASSIST_ID, terms_assist, coos);
        addToCoordsIfExists(degenerate(cats, ColumnConstants.FINANCING_INSTRUMENT), TestModelConstants.FINANCING_INSTRUMENT_ID, financing_instrument, coos);
        
        return new CategAmountCell(activityIds.get(activityTitle), 
                new MonetaryAmount(new BigDecimal(amount), null, null, parsedDate, precisionSetting), 
                mis, coos, td);
    }
    
    /**
     * Adds funding parameter id if value != null.
     * This is done for: pledge, transaction type, agreement, recipient org.
     * Mirrors code in AmpFundingColumn.fetch().
     * @param categoryName
     * @param value
     * @param mis
     */
    private void addToMetaIfExists(String categoryName, String value, MetaInfoSet mis) {
        if (value != null)
            mis.add(new MetaInfo(categoryName, fundingIds.get(categoryName).get(value)));
    }
    
    /**
     * Adds funding parameter as string (not as id), if != null.
     * This is done for: recipient role, source role, adjustment type
     * Mirrors code in AmpFundingColumn.fetch().
     * @param categoryName
     * @param value
     * @param mis
     */
    private void addToMetaIfExistsDirectly(String categoryName, String value, MetaInfoSet mis) {
        if (value != null)
            mis.add(new MetaInfo(categoryName, value));
    }
    /*          this.donor_org = donor_org;
            this.funding_status = funding_status;
            this.mode_of_payment = mode_of_payment;
            this.terms_assist = terms_assist;
            this.financing_instrument = financing_instrument;*/
    
    /**
     * Adds funding parameter (as id) to the coordinates map, if != null.
     * this is done for: donor org, funding status, mode of payment, terms of assistance (type of assistance in other places), financing instrument.
     * @param categoryName
     * @param value
     * @param coos
     */
    private void addToCoordsIfExists(LevelColumn levelColumn, String categoryName, String value, Map<NiDimensionUsage, Coordinate> coos) {
        //Map<String, LevelColumn> optionalIdsMap = new FundingIdsMapper().getOptionalIdsMap();
        Long val = value == null ? ColumnReportData.UNALLOCATED_ID : fundingIds.get(categoryName).get(value);
        //LevelColumn levelColumn = optionalIdsMap.get(categoryName);
        Coordinate newVal = levelColumn.getCoordinate(val);
        coos.put(levelColumn.dimensionUsage, newVal);
    }
    
    protected CategAmountCell entryToCell(FundingCellEntry fce) {
        return cell(fce.amount.toString(), fce.activityTitle, fce.year, fce.month, fce.pledge, fce.transaction_type, fce.agreement, fce.recipient_org, fce.recipient_role, fce.source_role, fce.adjustment_type, fce.donor_org, fce.funding_status, fce.mode_of_payment, fce.terms_assist, fce.financing_instrument, fce.transaction_date);
    }

    protected List<CategAmountCell> decodeCells(InputStream uis) {
        try(InputStream is = new GZIPInputStream(new BufferedInputStream(uis))) {
            try(ObjectInputStream ois = new ObjectInputStream(is)) {
                List<FundingCellEntry> entries = (List<FundingCellEntry>) ois.readObject();
                return AmpCollections.relist(entries, this::entryToCell);
            }
        }
        catch(IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        /*          amount                         , activity title                          , year, month        , pledge                        , transaction_type              , agreement                     , recipient_org                 , recipient_role                , source_role                   , adjustment_type               , donor_org                     , funding_status                , mode_of_payment               , terms_assist                  , financing_instrument          */
/*return Arrays.asList(
            cell("121000.000000"     , "f5d0a35b"                                                  , 2015, "April"      , null                          , null                          , null                          , null                          , null                          , "DN"                          , "Actual"                      , "ACTIONAID  LBG"              , null                          , null                          , "Grant"                       , "Sector Budget Support (SBS)" , "2016-04-12"),
            cell("1580000.000000"    , "f5d0a35b"                                                  , 2015, "April"      , null                          , "commitment"                  , null                          , null                          , null                          , "DN"                          , "Actual"                      , "ACTIONAID  LBG"              , null                          , null                          , "Grant"                       , "Sector Budget Support (SBS)" , "2016-04-12")
);*/
    }
    
    static String[] months = new String[] {"dummy", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

}
