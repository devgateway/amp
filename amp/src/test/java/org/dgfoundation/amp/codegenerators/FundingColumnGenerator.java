package org.dgfoundation.amp.codegenerators;


import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import org.dgfoundation.amp.nireports.amp.MetaCategory;
import org.dgfoundation.amp.nireports.meta.MetaInfo;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.nireports.testcases.TestModelConstants;

import java.io.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.GZIPOutputStream;

import static org.dgfoundation.amp.algo.AmpCollections.sorted;

/**
 * Code generator for funding columns.
 * Standalone since funding columns are a very special kind of column.
 * @author acartaleanu
 *
 */
public class FundingColumnGenerator extends ColumnGenerator {


    public FundingColumnGenerator() {
        super("Funding", CategAmountCell.class);
        this.entries = populateList();
    }
    
    final List<FundingCellEntry> entries;

    private Map<String, LevelColumn> buildOptionalDimensionCols(AmpReportsSchema schema) {
        Map<String, NiReportColumn<?>> cols = schema.getColumns();
        Map<String, LevelColumn> res = new HashMap<>();
        FundingIdsMapper.getCoosFundingViewFilter().forEach((colName, viewColName) -> res.put(viewColName, cols.get(colName).levelColumn.get()));
        return res;
    }
    
    private Map<String, LevelColumn> optionalDimensionCols = buildOptionalDimensionCols(AmpReportsSchema.getInstance());
    
    
    // columns of type long which are optional
    protected static List<ImmutablePair<MetaCategory, String>> longColumnsToFetch = Arrays.asList(
            new ImmutablePair<>(MetaCategory.TRANSACTION_TYPE, "transaction_type"),         
            new ImmutablePair<>(MetaCategory.RECIPIENT_ORG, "recipient_org_id")
            );  
            
    private List<FundingCellEntry> populateList() {
        final List<FundingCellEntry> entries = new ArrayList<>();
        runInEngineContext( 
                new ArrayList<String>(getActivityNames().values()), 
                eng -> {
                    List<CategAmountCell> cells = sorted((List<CategAmountCell>) eng.schema.getFundingFetcher(eng).fetch(eng));
                    Map<Long, String> activityNames = AmpCollections.remap(getActivityNames(), CodeGenerator::anon, null);
                    for (CategAmountCell cell : cells) {
                        
                        BigDecimal amount = cell.getAmount();
                        String transaction_date = DateTimeFormatter.ISO_LOCAL_DATE.format(cell.amount.date);
                        
                        String activityTitle = activityNames.get(cell.activityId);
                        String year = String.format("%s", cell.getTranslatedDate().year.getValue());
                        String month = cell.getTranslatedDate().month.getValue();
                        String pledge = cat(TestModelConstants.PLEDGE_ID, cell.metaInfo); //meta
                        String transaction_type = cat(TestModelConstants.TRANSACTION_TYPE, cell.metaInfo); //meta
                        String agreement = cat(TestModelConstants.AGREEMENT_ID, cell.metaInfo); //meta
                        String recipient_org = cat(TestModelConstants.RECIPIENT_ORG, cell.metaInfo); //meta

                        //these three are saved directly as values
                        String recipient_role = catValueDirectly(TestModelConstants.RECIPIENT_ROLE, cell.metaInfo);
                        String source_role = catValueDirectly(TestModelConstants.SOURCE_ROLE, cell.metaInfo);
                        String adjustment_type = catValueDirectly(TestModelConstants.ADJUSTMENT_TYPE, cell.metaInfo);
                        
                        
                        //these are read from the coords map
                        String donor_org = cat(TestModelConstants.DONOR_ORG_ID, cell.coordinates);
                        if (donor_org == null)
                            continue;
                        String funding_status = cat(TestModelConstants.FUNDING_STATUS_ID, cell.coordinates);
                        String mode_of_payment = cat(TestModelConstants.MODE_OF_PAYMENT_ID, cell.coordinates);
                        String terms_assist = cat(TestModelConstants.TERMS_ASSIST_ID, cell.coordinates);
                        String financing_instrument = cat(TestModelConstants.FINANCING_INSTRUMENT_ID, cell.coordinates);
                        entries.add(new FundingCellEntry(amount, activityTitle, year, month, pledge, transaction_type, 
                                agreement, recipient_org, recipient_role, source_role, adjustment_type, 
                                donor_org, funding_status, mode_of_payment, terms_assist, financing_instrument,
                                transaction_date));
                    }
                });
        entries.sort((FundingCellEntry e1, FundingCellEntry e2) -> {
            if (!e1.activityTitle.equals(e2.activityTitle))
                return e1.activityTitle.compareTo(e2.activityTitle);
            if (!e1.amount.equals(e2.amount))
                return e1.amount.compareTo(e2.amount);
            if (!e1.donor_org.equals(e2.donor_org))
                return e1.donor_org.compareTo(e2.donor_org);
            if (e1.year != e2.year)
                return Integer.compare(e1.year, e2.year);
            if (!e1.month.equals(e2.month))
                return e1.month.compareTo(e2.month);
            return e1.transaction_type.compareTo(e2.transaction_type);
        });
        return entries;
    }

    private String cat(String name, MetaInfoSet mis) {
        MetaInfo mi = mis.getMetaInfo(name);
        if (mi == null)
            return null;
        Long id = (Long)mi.v;
        if (id == null)
            return null;
        if (!categs.get(name).containsKey(id))
            return null;
        return categs.get(name).get(id); 
    }
    
    
    private String catValueDirectly(String name, MetaInfoSet mis){
        MetaInfo mi = mis.getMetaInfo(name);
        if (mi == null)
            return null;
        String val = (String)mi.v;
        return val;
    }
    
    private String cat(String name, Map<NiDimensionUsage, Coordinate> coos) {
        if (optionalDimensionCols.get(name) == null)
            throw new RuntimeException("no column with requested name!");
        LevelColumn lc = optionalDimensionCols.get(name);
        if (coos.get(lc.dimensionUsage) == null)
            return null;
        Map<Long, String> categ = categs.get(name);
        if (categ == null)
            return null;
        return categ.get(coos.get(lc.dimensionUsage).id);
    }
    
    Map<String, Map<Long, String>> categs = new FundingIdsMapper().getAllIds();

    public static DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-ddd");
    }

    static String generateNames() {
        return String.format("\t\t\t%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s", 
                pad("amount", 31), 
                pad("activity title", 40),
                pad("year", 4),
                pad("month", 13),
                pad("pledge", 30), 
                pad("transaction_type", 30), 
                pad("agreement", 30), 
                pad("recipient_org", 30), 
                pad("recipient_role", 30),
                pad("source_role", 30),
                pad("adjustment_type", 30),
                pad("donor_org", 30),
                pad("funding_status", 30),
                pad("mode_of_payment", 30),
                pad("terms_assist", 30),
                pad("financing_instrument", 30),
                pad("transaction_date", 30));
    }
    
    @Override
    public String generate() {
        StringBuilder strb = new StringBuilder();
        strb.append("/*");
        strb.append(generateNames());
        strb.append("*/\n");
        strb.append("return Arrays.asList(\n");
        for (int i = 0; i < entries.size(); i++) {
            FundingCellEntry ent = entries.get(i);
            strb.append("\t\t\tcell(");
            strb.append(ent.toString());
            strb.append(")");
            if (i < entries.size() - 1)
                strb.append(",");
            strb.append("\n");
        }
        strb.append(");");
        return strb.toString();
    }
    
    public void binaryDump() {
        try(OutputStream os = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(getPath() + File.pathSeparator + "funding.gz"), 1024))) {
            try(ObjectOutputStream oos = new ObjectOutputStream(os)) {
                oos.writeObject(entries);
            }
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
