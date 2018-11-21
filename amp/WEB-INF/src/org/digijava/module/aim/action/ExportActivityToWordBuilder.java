package org.digijava.module.aim.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpActivityBudgetStructure;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpGPISurvey;
import org.digijava.module.aim.dbentity.AmpGPISurveyResponse;
import org.digijava.module.aim.dbentity.AmpImputation;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLineMinistryObservation;
import org.digijava.module.aim.dbentity.AmpLineMinistryObservationActor;
import org.digijava.module.aim.dbentity.AmpLineMinistryObservationMeasure;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpRegionalObservation;
import org.digijava.module.aim.dbentity.AmpRegionalObservationActor;
import org.digijava.module.aim.dbentity.AmpRegionalObservationMeasure;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.dbentity.AmpStructureCoordinate;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.dbentity.IPAContractDisbursement;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.ProposedProjCost;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.ChartGenerator;
import org.digijava.module.aim.helper.ChartParams;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingDetailComparator;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.helper.ReferenceDoc;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.ExportActivityToPdfUtil;
import org.digijava.module.aim.util.ExportUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.budget.dbentity.AmpBudgetSector;
import org.digijava.module.budget.dbentity.AmpDepartments;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

import clover.com.google.common.base.Strings;

/**
 * Created by apicca.
 */
public class ExportActivityToWordBuilder {

    public static final String FONT_FAMILY = "Times-Roman";
    public static final int FONT_SIZE_NORMAL = 10;
    public static final int FONT_SIZE_TITLE = 24;
    public static final int FONT_SIZE_SECTION_TITLE = 18;
    public static final int WIDTH = 100;
    public static final int POINTS_WIDTH = 350;
    public static final int POINTS_HEIGHT = 420;
    public static final int COLUMNS_4 = 4;
    public static final int TABLE_WIDTH = 10500;
    public static final int ADDITIONAL_INFORMATION_ROWS = 5;
    public static final int TABLE_SEPARATOR_LENGTH = 100;
    public static final int COLUMNS_2 = 2;
    private static final int CELL_LEFT_INDENT_SPACE = 50;
    private static Logger logger = Logger.getLogger(ExportActivityToWordBuilder.class);

    public static final String CELLCOLORGRAY = "F2F2F2";
    private EditActivityForm.Identification identification = null;
    private EditActivityForm.Planning planning = null;
    private EditActivityForm.Location location = null;
    private EditActivityForm.Programs programs = null;
    private EditActivityForm.Sector sectors = null;
    private Map<String, List<AmpComments>> allComments = null;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();

    private XWPFDocument doc;
    private AmpActivityVersion activity;
    private EditActivityForm activityForm;
    private HttpServletRequest request;

    public ExportActivityToWordBuilder(AmpActivityVersion activity, EditActivityForm form, HttpServletRequest request) {
        this.activity = activity;
        this.doc = new XWPFDocument();
        this.activityForm = form;
        this.request = request;
    }

    public XWPFDocument render() throws Exception {

        if (activity != null) {
            identification = activityForm.getIdentification();
            planning = activityForm.getPlanning();
            location = activityForm.getLocation();
            programs = activityForm.getPrograms();
            sectors = activityForm.getSectors();

            addTitle(activity.getName());

            createOverallInformationTable();

            /**
             * Identification step
             */
            addSectionTitle(TranslatorWorker.translateText("Identification").toUpperCase());

            //1st cell of the overAll Table
            getIdentificationTables();

            //AGENCY INTERNAL IDS
            getAgencyInternalIdsTables();


            /**
             * Planning step
             */
            getPlanningTables();

            //References
            getReferencesTables();

            /**
             * Location Step
             */
            getLocationsTables();

            /**
             * Sectors Part
             */
            getSectorsTables();

            /**
             * PROGRAMS PART
             */
            getProgramsTables();

            getDonorFundingTables();

            //AiddEffectiveness
            getAidEffectivenessTables();

            getRegionalFundingTables();

            getComponentTables();

            getIssuesTables();
    
            getRegionObservationsTables();
    
            getLineMinistryObservationsTables();

            getRelatedDocsTables();

            getRelatedOrgsTables();

            getContactInfoTables();

            getGpiTables();

            addProjectCostTables(activityForm.getFunding().getProProjCost(), "Proposed Project Cost");
            addProjectCostTables(activityForm.getFunding().getRevProjCost(), "Revised Project Cost");

            addTotalNumberOfFundingSources();

            getBudgetStructureTables();

            getContractsTables();

            getActivityCreationFieldsTables();

            getMETables();

            getActivityPerformanceTables();

            getActivityRiskTables();

            getStructures();

        }

        return doc;
    }

    private void getAidEffectivenessTables() throws Exception {
        if (FeaturesUtil.isVisibleModule("/Activity Form/Aid Effectivenes")) {
            List<String[]> aidEffectivenesToAdd = ActivityUtil.getAidEffectivenesForExport(activity);
            if (aidEffectivenesToAdd != null && aidEffectivenesToAdd.size() > 0) {
                addEffectivenessTable(aidEffectivenesToAdd);
            }
        }
    }

    private void getMETables() throws Exception {
        String columnVal;
        if (FeaturesUtil.isVisibleModule("/Activity Form/M&E")) {
            addSectionTitle(TranslatorWorker.translateText("M & E").toUpperCase());
            XWPFTable meTbl = buildXwpfTable(1);

            if (activityForm.getIndicators() != null) {
                String valueLabel = TranslatorWorker.translateText("Value");
                String commentLabel = TranslatorWorker.translateText("Comment");
                String dateLabel = TranslatorWorker.translateText("Date");
                String nameLabel = TranslatorWorker.translateText("Name");
                String codeLabel = TranslatorWorker.translateText("Code");
                String logFrameLabel = TranslatorWorker.translateText("LogFrame");
                String sectorsLabel = TranslatorWorker.translateText("Sectors");

                for (IndicatorActivity indicator : activityForm.getIndicators()) {
                    XWPFTable headerTable = buildXwpfTable(COLUMNS_4);

                    XWPFTableRow headerRow = headerTable.getRow(0);
                    int cellIndex = 0;
                    headerRow.getCell(cellIndex++).setText(nameLabel);
                    headerRow.getCell(cellIndex++).setText(codeLabel);
                    headerRow.getCell(cellIndex++).setText(logFrameLabel);
                    headerRow.getCell(cellIndex++).setText(sectorsLabel);
                    cellIndex = 0;
                    headerRow = headerTable.createRow();

                    if (FeaturesUtil.isVisibleModule("/Activity Form/M&E/Name")) {
                        setRun(headerRow.getCell(cellIndex++).getParagraphs().get(0).createRun(), 
                                new RunStyle(FONT_FAMILY, FONT_SIZE_NORMAL, null, true),
                                indicator.getIndicator().getName(), false);
                    }
                    if (FeaturesUtil.isVisibleModule("/Activity Form/M&E/Code")) {
                        setRun(headerRow.getCell(cellIndex++).getParagraphs().get(0).createRun(), 
                                new RunStyle(FONT_FAMILY, FONT_SIZE_NORMAL, null, true),
                                indicator.getIndicator().getCode(), false);
                    }
                    if (FeaturesUtil.isVisibleModule("/Activity Form/M&E/ME Item/Logframe Category")) {
                        if (indicator.getValues() != null && indicator.getValues().size() > 0) {
                            setRun(headerRow.getCell(cellIndex++).getParagraphs().get(0).createRun(),
                                    new RunStyle(FONT_FAMILY, FONT_SIZE_NORMAL, null, false),
                                    indicator.getLogFrame() + "\n", false);
                        }
                    }

                    if (indicator.getIndicator().getSectors() != null) {
                        setRun(headerRow.getCell(cellIndex++).getParagraphs().get(0).createRun(), 
                                new RunStyle(FONT_FAMILY, FONT_SIZE_NORMAL, null, false),
                                ExportUtil.getIndicatorSectors(indicator) + "\n", false);
                    }

                    //XWPFTableRow newRow = meTbl.createRow();
                    //newRow.getCell(0).insertTable(0, headerTable);

                    for (AmpIndicatorValue value : indicator.getValuesSorted()) {
                        XWPFTable additionalInfoSubTable = buildXwpfTable(2);
                        XWPFTableRow valueRow = additionalInfoSubTable.getRow(0);
                        spanCellsAcrossRow(valueRow, 0, 2);
                        cellIndex = 0;
                        String fieldName = ExportUtil.getIndicatorValueType(value);
                        columnVal = TranslatorWorker.translateText(ExportUtil.INDICATOR_VALUE_NAME.get(value
                                .getValueType()));
                        setRun(valueRow.getCell(cellIndex++).getParagraphs().get(0).createRun(), 
                                new RunStyle(FONT_FAMILY, FONT_SIZE_NORMAL, null, true), columnVal, false);

                        if (FeaturesUtil.isVisibleModule("/Activity Form/M&E/ME Item/" + fieldName + " Value/"
                                + fieldName + " Value")) {
                            generateOverAllTableRows(additionalInfoSubTable, valueLabel, (value.getValue()
                                    != null ? FormatHelper.formatNumber(value.getValue()) : null), null);
                        }
                        if (FeaturesUtil.isVisibleModule("/Activity Form/M&E/ME Item/" + fieldName + " Value/"
                                + fieldName + " Comments")) {
                            generateOverAllTableRows(additionalInfoSubTable, commentLabel,
                                    DgUtil.trimChars(Strings.nullToEmpty(value.getComment())), null);
                        }
                        if (FeaturesUtil.isVisibleModule(
                                "/Activity Form/M&E/ME Item/" + fieldName + " Value/" + fieldName + " Date")) {
                            generateOverAllTableRows(additionalInfoSubTable, dateLabel, (value.getValueDate()
                                    != null ? DateConversion.convertDateToLocalizedString(value.getValueDate())
                                    : null), null);
                        }

                    }
                }

            }

        }
    }

    private void getProgramsTables() throws Exception {
        String columnVal;
        if (FeaturesUtil.isVisibleFeature("NPD Programs")) {
            addSectionTitle(TranslatorWorker.translateText("programs").toUpperCase());
            
            XWPFTable programsTbl = buildXwpfTable(2);

            if (FeaturesUtil.isVisibleModule("/Activity Form/Program/National Plan Objective")) {
                if (hasContent(programs.getNationalPlanObjectivePrograms())) {

                    columnVal = buildProgramsOutput(programs.getNationalPlanObjectivePrograms());
                    generateOverAllTableRows(programsTbl, TranslatorWorker.translateText("National Plan Objective")
                            .toUpperCase(), columnVal, null);
                }
            }
            if (FeaturesUtil.isVisibleModule("/Activity Form/Program")) {

                if (FeaturesUtil.isVisibleModule("/Activity Form/Program/Primary Programs")) {
                    if (hasContent(programs.getPrimaryPrograms())) {

                        columnVal = buildProgramsOutput(programs.getPrimaryPrograms());
                        generateOverAllTableRows(programsTbl, TranslatorWorker.translateText("Primary Programs")
                                .toUpperCase(), columnVal, null);
                    }
                }


                if (FeaturesUtil.isVisibleModule("/Activity Form/Program/Secondary Programs")) {
                    if (hasContent(programs.getSecondaryPrograms())) {
                        columnVal = buildProgramsOutput(programs.getSecondaryPrograms());
                        generateOverAllTableRows(programsTbl, TranslatorWorker.translateText("Secondary Programs")
                                .toUpperCase(), columnVal, null);
                    }
                }
                if (FeaturesUtil.isVisibleModule("/Activity Form/Program/Tertiary Programs")) {
                    if (hasContent(programs.getTertiaryPrograms())) {
                        columnVal = buildProgramsOutput(programs.getTertiaryPrograms());
                        generateOverAllTableRows(programsTbl, TranslatorWorker.translateText("Tertiary Programs")
                                .toUpperCase(), columnVal, null);
                    }
                }
                if (FeaturesUtil.isVisibleModule("/Activity Form/Program/Program Description")) {
                    if (programs.getProgramDescription() != null) {
                        columnVal = processEditTagValue(request, programs.getProgramDescription());
                        generateOverAllTableRows(programsTbl, TranslatorWorker.translateText("Program Description")
                                .toUpperCase(), columnVal, null);
                    }
                }
            }

        }
    }

    private void getSectorsTables() throws Exception  {
        String columnVal;
        if (FeaturesUtil.isVisibleModule("/Activity Form/Sectors")) {
            addSectionTitle(TranslatorWorker.translateText("Sectors").toUpperCase());

            XWPFTable sectorsTbl = buildXwpfTable(COLUMNS_2);
            sectorsTbl.setWidth(WIDTH);

            if (sectors.getClassificationConfigs() != null) {
                List<AmpClassificationConfiguration> classificationConfigs = SectorUtil
                        .getAllClassificationConfigsOrdered();
                for (AmpClassificationConfiguration config : classificationConfigs) {
                    boolean hasSectors = false;
                    if (sectors.getActivitySectors() != null) {
                        for (ActivitySector actSect : sectors.getActivitySectors()) {
                            if (actSect.getConfigId().equals(config.getId())) {
                                hasSectors = true;
                            }
                        }
                    }
                    if (hasSectors) {
                        String sector = TranslatorWorker.translateText(config.getName()) + " Sector";
                        XWPFTableRow sectorsTblTitleRow = sectorsTbl.createRow();
                        XWPFParagraph sectorTitleParagraphs = sectorsTblTitleRow.getCell(0).getParagraphs().get(0);
                        setOrientation(sectorTitleParagraphs);
                        setRun(sectorTitleParagraphs.createRun(), 
                                new RunStyle(FONT_FAMILY, FONT_SIZE_NORMAL, null, false), sector.toUpperCase(), false);
                    }
                    if (sectors.getActivitySectors() != null) {
                        for (ActivitySector actSect : sectors.getActivitySectors()) {
                            if (actSect.getConfigId().equals(config.getId())) {
                                columnVal = actSect.getSectorScheme();
                                if (actSect.getSectorName() != null) {
                                    columnVal += " - " + actSect.getSectorName();
                                }
                                if (actSect.getSubsectorLevel1Name() != null) {
                                    columnVal += " - " + actSect.getSubsectorLevel1Name();
                                }
                                if (actSect.getSubsectorLevel2Name() != null) {
                                    columnVal += " - " + actSect.getSubsectorLevel2Name();
                                }
                                generateOverAllTableRows(sectorsTbl, columnVal, actSect.getSectorPercentage() + " %",
                                        null);
                            }
                        }
                    }
                }
            }
        }
    }

    private void getLocationsTables() throws Exception {
        String columnVal;
        String columnName;
        AmpCategoryValue catVal;
        addSectionTitle(TranslatorWorker.translateText("Location").toUpperCase());

        XWPFTable locationSubTable1 = buildXwpfTable(2);

        if (FeaturesUtil.isVisibleModule("/Activity Form/Location/Implementation Location")) {

            if (location.getSelectedLocs() != null) {
                for (Location loc : location.getSelectedLocs()) {
                    columnVal = "";
                    String columnPercentage = "";
                    for (String val : loc.getAncestorLocationNames()) {
                        columnVal += "[" + val + "]";
                    }
                    if (FeaturesUtil.isVisibleField("Regional Percentage")) {
                        columnPercentage = loc.getPercent() + " %";
                    }
                    generateOverAllTableRows(locationSubTable1, columnVal, columnPercentage, null);
                }
            }
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Location/Implementation Level")) {
            columnName = TranslatorWorker.translateText("Implementation Level");
            columnVal = "";
            catVal = null;
            if (location.getLevelId() != null && location.getLevelId() != 0) {
                catVal = CategoryManagerUtil.getAmpCategoryValueFromDb(location.getLevelId());
            }
            if (catVal != null) {
                columnVal = CategoryManagerUtil.translateAmpCategoryValue(catVal);
            }
            generateOverAllTableRows(locationSubTable1, columnName, columnVal, null);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Location/Implementation Location")) {
            columnName = TranslatorWorker.translateText("Implementation Location");
            columnVal = "";
            catVal = null;
            if (location.getImplemLocationLevel() != null && location.getImplemLocationLevel() != 0) {
                catVal = CategoryManagerUtil.getAmpCategoryValueFromDb(location.getImplemLocationLevel());
            }
            if (catVal != null) {
                columnVal = CategoryManagerUtil.translateAmpCategoryValue(catVal);
            }
            generateOverAllTableRows(locationSubTable1, columnName, columnVal, null);
        }
    }

    private void getAgencyInternalIdsTables() {
        if (FeaturesUtil.isVisibleModule("/Activity Form/Activity Internal IDs")) {
            addSectionTitle(TranslatorWorker.translateText("Agency Internal IDs").toUpperCase());
            String columnVal;
            XWPFTable internalTbl = buildXwpfTable(1);
            internalTbl.setWidth(WIDTH);
            setTableAlignment(internalTbl, STJc.LEFT);

            columnVal = "";
            if (activity.getInternalIds() != null) {
                columnVal = ExportUtil.buildInternalId(activityForm.getInternalIds());
            }
            XWPFTableCell cell = internalTbl.getRow(0).getCell(0);
            setRun(cell.getParagraphs().get(0).createRun(), new RunStyle(FONT_FAMILY, FONT_SIZE_NORMAL, null, false),
                    columnVal, false);
        }
    }

    public boolean hasContent(Collection<?> col) {
        return col != null && !col.isEmpty();
    }

    private static void setOrientation(XWPFParagraph par) {
        setOrientation(par, false);
    }

    private static void setOrientation(XWPFParagraph par, boolean isNumber) {
        if (SiteUtils.isEffectiveLangRTL()) {
            if (par.getCTP().getPPr() == null) {
                par.getCTP().addNewPPr();
            }
            if (par.getCTP().getPPr().getBidi() == null) {
                par.getCTP().getPPr().addNewBidi();
            }
            
            if (isNumber) {
                par.getCTP().getPPr().getBidi().setVal(STOnOff.OFF);
                par.setIndentFromLeft(CELL_LEFT_INDENT_SPACE);
                par.setAlignment(ParagraphAlignment.LEFT);
            } else {
                par.getCTP().getPPr().getBidi().setVal(STOnOff.ON);
            }
        } else if (isNumber) {
            par.setIndentFromRight(CELL_LEFT_INDENT_SPACE);
            par.setAlignment(ParagraphAlignment.RIGHT);
        }
    }
    
    private static void setRunOrientation(XWPFRun run, boolean isLtr) {
        if (SiteUtils.isEffectiveLangRTL()) {
            if (run.getCTR().getRPr() == null) {
                run.getCTR().addNewRPr().addNewRtl();
            }
            if (run.getCTR().addNewRPr().getRtl() == null) {
                run.getCTR().addNewRPr().addNewRtl();
            }
            
            STOnOff.Enum st = isLtr ? STOnOff.FALSE : STOnOff.TRUE;
            run.getCTR().addNewRPr().addNewRtl().setVal(st);
        }
    }

    private void addTitle(String title) {
        XWPFParagraph titleParagraph = doc.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        setRun(titleParagraph.createRun(), new RunStyle(FONT_FAMILY, FONT_SIZE_TITLE, null, true), title, false);
    }

    private void addSectionTitle(String title) {
        XWPFTable table = buildXwpfTable(1);
        XWPFTableRow row = table.createRow();
        XWPFTableCell cell = row.getCell(0);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        cell.setColor(CELLCOLORGRAY);
        XWPFParagraph sectionTitle = cell.getParagraphs().get(0);
        setOrientation(sectionTitle);
        sectionTitle.setAlignment(ParagraphAlignment.LEFT);
        setRun(sectionTitle.createRun(), new RunStyle(FONT_FAMILY, FONT_SIZE_SECTION_TITLE, null, true), title, false);
    }

    private XWPFTable buildXwpfTable(int columns) {
        doc.createParagraph();
        XWPFTable table = doc.createTable(1, columns);
        setTableAlignment(table, STJc.CENTER);
        table.getCTTbl().getTblPr().unsetTblBorders();

        CTTblWidth oWidth = table.getCTTbl().getTblPr().addNewTblW();
        oWidth.setType(STTblWidth.DXA);
        oWidth.setW(BigInteger.valueOf(TABLE_WIDTH));

        if (SiteUtils.isEffectiveLangRTL()) {
            if (table.getCTTbl().getTblPr() == null) {
                table.getCTTbl().addNewTblPr().addNewBidiVisual().setVal(STOnOff.ON);
            } else {
                table.getCTTbl().getTblPr().addNewBidiVisual().setVal(STOnOff.ON);
            }
        }
        return table;
    }

    private void addTotalNumberOfFundingSources()
            throws WorkerException {
        if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Overview Section/Total Number of Funding Sources")) {
            ExportSectionHelper eshTitle = new ExportSectionHelper("Total Number of Funding Sources", true)
                    .setWidth(WIDTH).setAlign(STJc.LEFT);
            createSectionTable(eshTitle);
            ExportSectionHelper eshProjectCostTable = new ExportSectionHelper(null, false)
                    .setWidth(WIDTH).setAlign(STJc.LEFT);
            Integer total = activityForm.getIdentification().getFundingSourcesNumber();
            eshProjectCostTable.addRowData(new ExportSectionHelperRowData("Total", null, null, true).
                    addRowData(total == null ? "" : total.toString()));
            createSectionTable(eshProjectCostTable);
        }
    }


    private void addEffectivenessTable(List<String[]> aidEffectivenesToAdd) throws
            Exception {
        if (aidEffectivenesToAdd == null || !(aidEffectivenesToAdd.size() > 0)) {
            return;
        }
        XWPFTable addEffectiveness = buildXwpfTable(2);
        addEffectiveness.setWidth(WIDTH);
        addSectionTitle(TranslatorWorker.translateText("Aid Effectivenes").toUpperCase());

        for (String[] a : aidEffectivenesToAdd) {
            generateOverAllTableRows(addEffectiveness, a[0], a[1], CELLCOLORGRAY);
        }

    }

    private void getActivityRiskTables() throws Exception {

        Long actId = new Long(request.getParameter("activityid"));

        /**
         * Activity - Risk
         */
        if (FeaturesUtil.isVisibleField("Project Risk")) {

            addSectionTitle("Activity Risk");
            
            XWPFTable table = buildXwpfTable(1);
            table.setWidth(WIDTH);
            setTableAlignment(table, STJc.CENTER);

            // chart
            ByteArrayOutputStream outByteStream1 = new ByteArrayOutputStream();
            Collection<AmpIndicatorRiskRatings> risks = IndicatorUtil
                    .getRisks(actId);
            ChartParams rcp = new ChartParams();
            rcp.setData(risks);
            rcp.setTitle("");
            JFreeChart riskChart = ChartGenerator.generateRiskChart(rcp,
                    TLSUtils.getSiteId(), TLSUtils.getLangCode());
            ChartRenderingInfo riskInfo = new ChartRenderingInfo();
            if (riskChart != null) {
                Plot plot = riskChart.getPlot();
                plot.setNoDataMessage("No Data Available");
                java.awt.Font font = new java.awt.Font(null, 0, FONT_SIZE_TITLE);
                plot.setNoDataMessageFont(font);
                // write image in response
                ChartUtilities.writeChartAsPNG(outByteStream1, riskChart, POINTS_WIDTH,
                        POINTS_HEIGHT, riskInfo);

                XWPFTableCell cell = table.getRow(0).getCell(0);
                XWPFParagraph paragraph = cell.addParagraph();
                XWPFRun run = paragraph.createRun();
                run.addPicture(new ByteArrayInputStream(outByteStream1.toByteArray()), XWPFDocument
                        .PICTURE_TYPE_JPEG, "chart", Units.toEMU(POINTS_WIDTH), Units.toEMU(POINTS_HEIGHT));
            }

        }

    }

    private void getActivityPerformanceTables() throws Exception {

        /**
         * Activity - Performance
         */
        if (FeaturesUtil.isVisibleField("Activity Performance")) {
            addSectionTitle("Activity Performance");
            
            XWPFTable table = buildXwpfTable(1);
            table.setWidth(WIDTH);
            setTableAlignment(table, STJc.CENTER);

            // chart
            Set<IndicatorActivity> values = activity.getIndicators();
            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
            org.digijava.module.aim.helper.ChartParams cp = new ChartParams();
            cp.setData(values);
            cp.setTitle("");
            cp.setSession(request.getSession());
            JFreeChart chart = ChartGenerator.generatePerformanceChart(cp,
                    TLSUtils.getSiteId(), TLSUtils.getLangCode());
            CategoryPlot pl = (CategoryPlot) chart.getPlot();
            CategoryItemRenderer r1 = pl.getRenderer(); // new
            // StackedBarRenderer();
            r1.setSeriesPaint(0, Constants.ACTUAL_VAL_CLR);
            r1.setSeriesPaint(1, Constants.TARGET_VAL_CLR);
            pl.setRenderer(r1);
            ChartRenderingInfo info = new ChartRenderingInfo();
            if (chart != null) {
                Plot plot = chart.getPlot();
                plot.setNoDataMessage("No Data Available");
                java.awt.Font font = new java.awt.Font(null, 0, FONT_SIZE_TITLE);
                plot.setNoDataMessageFont(font);

                // write image in response
                ChartUtilities.writeChartAsPNG(outByteStream, chart, POINTS_WIDTH, POINTS_HEIGHT,
                        info);

                XWPFTableCell cell = table.getRow(0).getCell(0);
                XWPFParagraph paragraph = cell.addParagraph();
                XWPFRun run = paragraph.createRun();
                run.addPicture(new ByteArrayInputStream(outByteStream.toByteArray()), XWPFDocument
                        .PICTURE_TYPE_JPEG, "chart", Units.toEMU(POINTS_WIDTH), Units.toEMU(POINTS_HEIGHT));
            }

        }
    }

    private void getActivityCreationFieldsTables() throws WorkerException {

        ExportSectionHelper sectionHelper = new ExportSectionHelper(null)
                .setWidth(WIDTH).setAlign(STJc.LEFT);
        /**
         * Activity created by
         */

        if (FeaturesUtil.isVisibleField("Activity Created By")) {
            String actCreatedByString = identification.getActAthEmail() == null ? "(unknown)"
                    : identification.getActAthFirstName() + " " + identification.getActAthLastName() + "-"
                    + identification.getActAthEmail();
            ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
                    "Activity created by", null, null, true)
                    .addRowData(actCreatedByString);
            sectionHelper.addRowData(rowData);
        }


        /**
         * Activity updated on
         */
        if (FeaturesUtil.isVisibleField("Activity Updated On")) {
            ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
                    "Updated On", null, null, true).addRowLtrData(identification
                    .getUpdatedDate());
            sectionHelper.addRowData(rowData);
        }

        /**
         * Activity updated by
         */
        if (FeaturesUtil.isVisibleField("Activity Updated By")) {
            String output = "";
            if (identification.getModifiedBy() != null) {
                User user = identification.getModifiedBy().getUser();
                output = user.getFirstNames() + " " + user.getLastName() + "-"
                        + user.getEmail();
            }
            ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
                    "Activity Updated By", null, null, true).addRowData(output);
            sectionHelper.addRowData(rowData);
        }

        /**
         * Activity created on
         */
        if (FeaturesUtil.isVisibleField("Activity Created On")) {
            ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
                    "Created On", null, null, true).addRowLtrData(identification
                    .getCreatedDate());
        }
        createSectionTable(sectionHelper);

    }

    private void getContractsTables() throws WorkerException {

        if (FeaturesUtil
                .isVisibleModule("/Activity Form/Contracts")) {
            ExportSectionHelper sectionHelper = new ExportSectionHelper(
                    "IPA Contracting", true).setWidth(WIDTH).setAlign(STJc.LEFT);
            createSectionTable(sectionHelper);

            if (activityForm.getContracts().getContracts() != null) {
                sectionHelper = new ExportSectionHelper(null, false).setWidth(
                        WIDTH).setAlign(STJc.LEFT);

                for (IPAContract contract : (List<IPAContract>) activityForm
                        .getContracts().getContracts()) {
                    // name
                    if (FeaturesUtil
                            .isVisibleModule(
                                    "/Activity Form/Contracts/Contract Item/Contract Info/Contract Name")) {
                        addRowDataToHelper(sectionHelper, "Contract Name", contract.getContractName());
                    }

                    // description
                    if (FeaturesUtil
                            .isVisibleModule(
                                    "/Activity Form/Contracts/Contract Item/Contract Info/Contract Description")) {
                        addRowDataToHelper(sectionHelper, "Contract Description", contract.getDescription());
                    }

                    // activity category
                    if (FeaturesUtil
                            .isVisibleModule(
                                    "/Activity Form/Contracts/Contract Item/Contract Info/Activity Type")) {
                        addRowDataToHelper(sectionHelper, "Activity Category", contract.getActivityCategory() != null
                                ? contract.getActivityCategory().getValue() : "");
                    }

                    // type
                    if (FeaturesUtil
                            .isVisibleModule(
                                    "/Activity Form/Contracts/Contract Item/Contract Info/Contract Type")) {
                        addRowDataToHelper(sectionHelper, "Type", contract.getType() != null ? contract.getType()
                                .getValue() : "");
                    }

                    // start of tendering
                    if (FeaturesUtil
                            .isVisibleModule(
                                    "/Activity Form/Contracts/Contract Item/Contract Details/Start of Tendering")) {
                        addRowDataToHelper(sectionHelper, "Start of Tendering", contract
                                .getFormattedStartOfTendering());
                    }

                    // Signature of Contract
                    if (FeaturesUtil
                            .isVisibleModule(
                                    "/Activity Form/Contracts/Contract Item/Contract Details/Signature")) {
                        addRowDataToHelper(sectionHelper, "Signature of Contract", contract
                                .getFormattedSignatureOfContract());
                    }

                    // Contract Organization
                    if (FeaturesUtil
                            .isVisibleModule(
                                    "/Activity Form/Contracts/Contract Item/Contract Organizations")) {
                        addRowDataToHelper(sectionHelper, "Contract Organization", contract.getOrganization() != null
                                ? contract.getOrganization().getName() : "");
                    }

                    // Contract Organization
                    if (FeaturesUtil
                            .isVisibleModule(
                                    "/Activity Form/Contracts/Contract Item/Contract Details/Contractor Name")) {
                        addRowDataToHelper(sectionHelper, "Contractor Name", contract.getContractingOrganizationText());
                    }

                    // Contract Completion
                    if (FeaturesUtil
                            .isVisibleModule(
                                    "/Activity Form/Contracts/Contract Item/Contract Details/Completion")) {
                        addRowDataToHelper(sectionHelper, "Contract Completion", contract
                                .getFormattedContractCompletion());
                    }

                    // Status
                    if (FeaturesUtil
                            .isVisibleModule(
                                    "/Activity Form/Contracts/Contract Item/Contract Details/Status")) {
                        addRowDataToHelper(sectionHelper, "Status", contract.getStatus() != null
                                ? contract.getStatus().getValue() : "");
                    }

                    getContractsTablesAmounts(sectionHelper, contract);

                }
                createSectionTable(sectionHelper);
            }
        }
    }

    private void getContractsTablesAmounts(ExportSectionHelper sectionHelper, IPAContract
            contract) {
        String output;
        // Total Amount
        if (FeaturesUtil
                .isVisibleModule(
                        "/Activity Form/Contracts/Contract Item/Funding Allocation/Contract Total Value")) {
            output = contract.getTotalAmount() != null ? contract.getTotalAmount().floatValue() + " "
                    + contract.getTotalAmountCurrency().getCurrencyCode() : " ";
            addRowDataToHelper(sectionHelper, "Total Amount", output);
        }
        sectionHelper.addRowData(new ExportSectionHelperRowData(
                null, null, null, false).setSeparator(true));
        // Total EC Contribution
        if (FeaturesUtil
                .isVisibleModule(
                        "/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/Contract "
                                + "Total Amount")) {
            addRowDataToHelper(sectionHelper, "Total EC Contribution", null);
        }

        // IB
        if (FeaturesUtil
                .isVisibleModule(
                        "/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/IB Amount")) {
            output = getContractAmount(contract.getTotalECContribIBAmount(), contract
                    .getTotalAmountCurrency());
            addRowDataToHelper(sectionHelper, "IB", output);
        }

        // INV
        if (FeaturesUtil
                .isVisibleModule(
                        "/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/INV "
                                + "Amount")) {
            output = getContractAmount(contract.getTotalECContribINVAmount(), contract
                    .getTotalAmountCurrency());
            addRowDataToHelper(sectionHelper, "Contracting INV", output);
        }
        sectionHelper.addRowData(new ExportSectionHelperRowData(
                null, null, null, false).setSeparator(true));
        // Total National Contribution
        ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
                "Total National Contribution", null, null, true);
        sectionHelper.addRowData(rowData);

        // Central
        if (FeaturesUtil
                .isVisibleModule(
                        "/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/Central "
                                + "Amount")) {
            output = getContractAmount(contract.getTotalNationalContribCentralAmount(), contract
                    .getTotalAmountCurrency());
            addRowDataToHelper(sectionHelper, "Contracting Central Amount", output);
        }

        // Regional
        if (FeaturesUtil
                .isVisibleField(
                        "/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/Regional "
                                + "Amount")) {
            output = getContractAmount(contract.getTotalNationalContribRegionalAmount(), contract
                    .getTotalAmountCurrency());
            addRowDataToHelper(sectionHelper, "Regional", output);
        }

        // IFIs
        if (FeaturesUtil
                .isVisibleField(
                        "/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/IFI "
                                + "Amount")) {
            output = getContractAmount(contract.getTotalNationalContribIFIAmount(), contract
                    .getTotalAmountCurrency());
            addRowDataToHelper(sectionHelper, "IFIs", output);
        }

        // Total Private Contribution
        sectionHelper.addRowData(new ExportSectionHelperRowData(
                null, null, null, false).setSeparator(true));

        rowData = new ExportSectionHelperRowData(
                "Total Private Contribution", null, null, true);
        sectionHelper.addRowData(rowData);

        // IB
        if (FeaturesUtil
                .isVisibleModule(
                        "/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/IB Amount")) {
            output = getContractAmount(contract.getTotalPrivateContribAmount(), contract
                    .getTotalAmountCurrency());
            addRowDataToHelper(sectionHelper, "IB", output);
        }

        sectionHelper.addRowData(new ExportSectionHelperRowData(
                null, null, null, false).setSeparator(true));

        if (FeaturesUtil
                .isVisibleModule(
                        "/Activity Form/Contracts/Contract Item/Contract Disbursements")) {
            generateContractDisbursements(sectionHelper, contract);
        }
    }

    private String getContractAmount(Double amount, AmpCurrency currency) {
        String output;
        if (amount != null) {
            output = amount.floatValue() + " " + currency.getCurrencyCode();
        } else if (currency != null) {
            output = " " + currency.getCurrencyCode();
        } else {
            output = "";
        }
        return output;
    }

    private void addRowDataToHelper(ExportSectionHelper sectionHelper, String title, String value) {
        ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
                title, null, null, true)
                .addRowData(value);
        sectionHelper.addRowData(rowData);
    }

    private void generateContractDisbursements(ExportSectionHelper sectionHelper,
                                               IPAContract contract) {
        String output;
        ExportSectionHelperRowData rowData;
        Integer disbFieldsCount = 0;
        disbFieldsCount += FeaturesUtil
                .isVisibleModule(
                        "/Activity Form/Contracts/Contract Item/Contract Disbursements/Adjustment "
                                + "Type") ? 1 : 0;
        disbFieldsCount += FeaturesUtil
                .isVisibleModule(
                        "/Activity Form/Contracts/Contract Item/Contract Disbursements/Amount") ? 1 : 0;
        disbFieldsCount += FeaturesUtil
                .isVisibleModule(
                        "/Activity Form/Contracts/Contract Item/Contract Disbursements/Currency") ? 1
                : 0;
        disbFieldsCount += FeaturesUtil
                .isVisibleModule(
                        "/Activity Form/Contracts/Contract Item/Contract Disbursements/Transaction "
                                + "Date") ? 1 : 0;

        // Total disbursements
        output = (contract.getTotalDisbursements() != null ? contract
                .getTotalDisbursements().floatValue() : " ")
                + " ";
        output += contract.getDibusrsementsGlobalCurrency() != null ? contract
                .getDibusrsementsGlobalCurrency()
                .getCurrencyCode() : activityForm.getCurrCode();
        rowData = new ExportSectionHelperRowData(
                "Total disbursements", null, null, true)
                .addRowData(output);
        sectionHelper.addRowData(rowData);

        output = (contract.getFundingTotalDisbursements() != null ? contract
                .getFundingTotalDisbursements().floatValue()
                : " ")
                + " ";
        output += contract.getDibusrsementsGlobalCurrency() != null ? contract
                .getDibusrsementsGlobalCurrency()
                .getCurrencyCode() : activityForm.getCurrCode();
        rowData = new ExportSectionHelperRowData(
                "Total Funding Disbursements", null, null, true)
                .addRowData(output);
        sectionHelper.addRowData(rowData);

        // Contract Execution Rate
        rowData = new ExportSectionHelperRowData(
                "Contract Execution Rate", null, null, true)
                .addRowData(contract.getExecutionRate() != null ? String
                        .valueOf(contract.getExecutionRate()
                                .floatValue()) : " ");
        sectionHelper.addRowData(rowData);

        // Contract Execution Rate
        rowData = new ExportSectionHelperRowData(
                "Contract Funding Execution Rate", null, null,
                true).addRowData(contract
                .getFundingExecutionRate() != null ? String
                .valueOf(contract.getFundingExecutionRate()
                        .floatValue()) : " ");
        sectionHelper.addRowData(rowData);

        if (disbFieldsCount > 0) {
            generateContractDisbursementsFields(sectionHelper, contract);
        }
    }

    private void generateContractDisbursementsFields(ExportSectionHelper sectionHelper,
                                                     IPAContract contract) {
        ExportSectionHelperRowData rowData;
        // Disbursements
        rowData = new ExportSectionHelperRowData(
                "Disbursements", null, null, true);
        sectionHelper.addRowData(rowData);

        if (contract.getDisbursements() != null) {
            for (IPAContractDisbursement ipaDisb : (Set<IPAContractDisbursement>) contract
                    .getDisbursements()) {

                rowData = new ExportSectionHelperRowData(
                        null);

                if (FeaturesUtil
                        .isVisibleModule(
                                "/Activity Form/Contracts/Contract Item/Contract "
                                        + "Disbursements/Adjustment Type")) {
                    rowData.addRowData(ipaDisb
                            .getAdjustmentType().getValue());
                }

                if (FeaturesUtil
                        .isVisibleModule(
                                "/Activity Form/Contracts/Contract Item/Contract "
                                        + "Disbursements/Amount")) {
                    String currency = "";
                    if (FeaturesUtil
                            .isVisibleModule(
                                    "/Activity Form/Contracts/Contract Item/Contract "
                                            + "Disbursements/Currency")) {
                        currency = ipaDisb.getCurrency()
                                .getCurrencyCode();
                    }
                    rowData.addRowData(ipaDisb.getAmount()
                            .floatValue() + " " + currency);
                }

                if (FeaturesUtil
                        .isVisibleModule(
                                "/Activity Form/Contracts/Contract Item/Contract "
                                        + "Disbursements/Transaction Date")) {
                    rowData.addRowLtrData(ipaDisb
                            .getDisbDate());
                }
                sectionHelper.addRowData(rowData);
            }
        }

        // Funding Disbursements

        rowData = new ExportSectionHelperRowData(
                "Funding Disbursements", null, null, true);
        sectionHelper.addRowData(rowData);

        if (activityForm.getFunding() != null) {
            rowData = new ExportSectionHelperRowData(null);

            if (FeaturesUtil
                    .isVisibleModule(
                            "/Activity Form/Contracts/Contract Item/Contract "
                                    + "Disbursements/Adjustment Type")) {
                rowData.addRowSimpleData("Adj. Type Disb.", true);
            }

            if (FeaturesUtil
                    .isVisibleModule(
                            "/Activity Form/Contracts/Contract Item/Contract "
                                    + "Disbursements/Amount")) {
                rowData.addRowSimpleData("Amount Disb.", true);
            }

            if (FeaturesUtil
                    .isVisibleModule(
                            "/Activity Form/Contracts/Contract Item/Contract "
                                    + "Disbursements/Currency")) {
                rowData.addRowSimpleData("Currency Disb.", true);
            }

            if (FeaturesUtil
                    .isVisibleModule(
                            "/Activity Form/Contracts/Contract Item/Contract "
                                    + "Disbursements/Transaction Date")) {
                rowData.addRowSimpleData("Date Disb.", true);
            }
            sectionHelper.addRowData(rowData);

            for (FundingDetail fundingDetail : activityForm
                    .getFunding().getFundingDetails()) {
                if (fundingDetail.getContract() != null
                        && contract.getContractName()
                        .equals(fundingDetail
                                .getContract()
                                .getContractName())
                        && fundingDetail
                        .getTransactionType() == 1) {
                    rowData = new ExportSectionHelperRowData(
                            null);

                    if (FeaturesUtil
                            .isVisibleModule(
                                    "/Activity Form/Contracts/Contract Item/Contract "
                                            + "Disbursements/Adjustment Type")) {
                        rowData.addRowData(fundingDetail
                                .getAdjustmentTypeName()
                                .getValue());
                    }
                    if (FeaturesUtil
                            .isVisibleModule(
                                    "/Activity Form/Contracts/Contract Item/Contract "
                                            + "Disbursements/Amount")) {
                        rowData.addRowData(fundingDetail
                                .getTransactionAmount());
                    }
                    if (FeaturesUtil
                            .isVisibleModule(
                                    "/Activity Form/Contracts/Contract Item/Contract "
                                            + "Disbursements/Currency")) {
                        rowData.addRowData(fundingDetail
                                .getCurrencyCode());
                    }
                    if (FeaturesUtil
                            .isVisibleModule(
                                    "/Activity Form/Contracts/Contract Item/Contract "
                                            + "Disbursements/Transaction Date")) {
                        rowData.addRowLtrData(fundingDetail
                                .getTransactionDate());
                    }
                    sectionHelper.addRowData(rowData);
                }
            }
        }
    }

    private void getGpiTables()
            throws WorkerException {

        if (FeaturesUtil.isVisibleModule("/Activity Form/GPI")) {
            boolean createTable = false;
            ExportSectionHelper sectionHelper = new ExportSectionHelper(
                    "GPI", true);
            createSectionTable(sectionHelper);

            sectionHelper = new ExportSectionHelper(null, false);

            for (AmpGPISurvey survey : activityForm.getGpiSurvey()) {
                List<AmpGPISurveyResponse> list = new ArrayList<>(survey.getResponses());
                Collections.sort(list, new AmpGPISurveyResponse.AmpGPISurveyResponseComparator());
                String indicatorName = "";
                for (AmpGPISurveyResponse response : list) {
                    if (!indicatorName.equals(response.getAmpQuestionId().getAmpIndicatorId().getName())) {
                        indicatorName = response.getAmpQuestionId().getAmpIndicatorId().getName();
                        ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(indicatorName, null,
                                null, true);
                        sectionHelper.addRowData(rowData);
                    }
                    String responseText = (response.getResponse() != null ? response.getResponse() : "");
                    if (ExportUtil.GPI_TYPE_YES_NO.equalsIgnoreCase(response.getAmpQuestionId().getAmpTypeId()
                            .getName())) {
                        responseText = TranslatorWorker.translateText(responseText);
                    }
                    ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
                            response.getAmpQuestionId().getQuestionText(), null, null, true)
                            .addRowData(responseText);
                    sectionHelper.addRowData(rowData);
                }
            }

            createSectionTable(sectionHelper);

        }
    }

    private void getRelatedDocsTables()
            throws WorkerException {

        HttpSession session = request.getSession();
        if (FeaturesUtil.isVisibleModule("/Activity Form/Related Documents")) {
            boolean createTable = false;
            ExportSectionHelper sectionHelper = new ExportSectionHelper(
                    "Related Documents", true);
            createSectionTable(sectionHelper);

            sectionHelper = new ExportSectionHelper(null, false);
            // documents
            if (activityForm.getDocuments().getDocuments() != null
                    && activityForm.getDocuments().getDocuments().size() > 0) {
                createTable = true;
                for (Documents doc : activityForm.getDocuments().getDocuments()) {

                    if (doc.getIsFile()) {
                        ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
                                doc.getTitle()).addRowData(doc.getFileName());
                        sectionHelper.addRowData(rowData);

                        rowData = new ExportSectionHelperRowData("Description",
                                null, null, true).addRowData(doc
                                .getDocDescription());
                        sectionHelper.addRowData(rowData);

                        rowData = new ExportSectionHelperRowData("Date", null,
                                null, true).addRowLtrData(doc.getDate());
                        sectionHelper.addRowData(rowData);

                        if (doc.getDocType() != null) {
                            rowData = new ExportSectionHelperRowData(
                                    "Document Type", null, null, true)
                                    .addRowData(doc.getDocType());
                            sectionHelper.addRowData(rowData);
                        }
                        sectionHelper
                                .addRowData(new ExportSectionHelperRowData(
                                        null, null, null, false)
                                        .setSeparator(true));
                    }
                }
            }
            if (activityForm.getDocuments().getCrDocuments() != null
                    && activityForm.getDocuments().getCrDocuments().size() > 0) {
                createTable = true;
                for (DocumentData crDoc : activityForm.getDocuments()
                        .getCrDocuments()) {
                    ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
                            crDoc.getTitle()).addRowData(crDoc.getName());
                    sectionHelper.addRowData(rowData);

                    rowData = new ExportSectionHelperRowData("Description",
                            null, null, true)
                            .addRowData(crDoc.getDescription());
                    sectionHelper.addRowData(rowData);

                    if (crDoc.getCalendar() != null) {
                        rowData = new ExportSectionHelperRowData("Date", null,
                                null, true).addRowData(crDoc.getCalendar());
                        sectionHelper.addRowData(rowData);
                    }
                    sectionHelper.addRowData(new ExportSectionHelperRowData(
                            null, null, null, false).setSeparator(true));
                }
            }

            if (createTable) {
                createSectionTable(sectionHelper);
            }
        }
    }

    private void getReferencesTables() throws Exception {
        if (FeaturesUtil.isVisibleModule("References")) {

            addSectionTitle(TranslatorWorker.translateText("References").toUpperCase());
            String output = "";
            // References
            XWPFTable referencesSubTable1 = null;
            Collection<AmpCategoryValue> catValues = CategoryManagerUtil
                    .getAmpCategoryValueCollectionByKey(
                            CategoryConstants.REFERENCE_DOCS_KEY, false);

            if (catValues != null) {
                referencesSubTable1 = buildXwpfTable(2);
                //referencesSubTable1.setWidths(new float[] { 1f, 2f });
                referencesSubTable1.setWidth(WIDTH);

                ReferenceDoc[] refDocs = activityForm.getDocuments()
                        .getReferenceDocs();
                output = "";
                if (refDocs != null) {
                    for (ReferenceDoc referenceDoc : refDocs) {
                        if (referenceDoc.getComment() != null) {
                            output += referenceDoc.getCategoryValue() + "\n";
                        }
                    }
                }
                String columnName = TranslatorWorker.translateText(
                        "References");
                generateOverAllTableRows(referencesSubTable1, columnName,
                        output, null);
            }
        }
    }


    private void getPlanningTables()
            throws Exception {

        addSectionTitle(TranslatorWorker.translateText("Planning").toUpperCase());

        XWPFTable planningSubTable1 = buildXwpfTable(2);

        Map<String, List<AmpComments>> allComments = this.fetchAllComments(activity.getAmpActivityId());

        String columnName;
        String columnVal;
        if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Line Ministry Rank")) {
            columnName = TranslatorWorker.translateText("Line Ministry Rank") + ": ";
            columnVal = planning.getLineMinRank().equals("-1") ? "" : planning.getLineMinRank();
            generateOverAllTableRows(planningSubTable1, columnName, columnVal, null);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Proposed Approval Date")) {
            columnName = TranslatorWorker.translateText("Proposed Approval Date") + ": ";
            generateOverAllTableRows(planningSubTable1, columnName, planning.getOriginalAppDate(), null, true);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Actual Approval Date")) {
            columnName = TranslatorWorker.translateText("Actual Approval Date") + ": ";
            generateOverAllTableRows(planningSubTable1, columnName, planning.getRevisedAppDate(), null, true);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Proposed Start Date")) {
            columnName = TranslatorWorker.translateText("Proposed Start Date") + ":";
            generateOverAllTableRows(planningSubTable1, columnName, planning.getOriginalStartDate(), null, true);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Actual Start Date")) {
            columnName = TranslatorWorker.translateText("Actual Start Date") + ": ";
            generateOverAllTableRows(planningSubTable1, columnName, planning.getRevisedStartDate(), null, true);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Original Completion Date")) {
            columnName = TranslatorWorker.translateText("Original Completion Date") + ": ";
            generateOverAllTableRows(planningSubTable1, columnName, planning.getOriginalCompDate(), null, true);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Proposed Completion Date")) {
            columnName = TranslatorWorker.translateText("Proposed Completion Date") + ": ";
            generateOverAllTableRows(planningSubTable1, columnName, planning.getProposedCompDate(), null, true);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Actual Completion Date")) {
            columnName = TranslatorWorker.translateText("Actual Completion Date") + ": ";
            generateOverAllTableRows(planningSubTable1, columnName, planning.getCurrentCompDate(), null, true);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Project Implementation Delay")) {
            columnName = TranslatorWorker.translateText("Project Implementation Delay") + ": ";
            generateOverAllTableRows(planningSubTable1, columnName, planning.getProjectImplementationDelay(), 
                    null, true);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Final Date for Contracting")) {
            columnName = TranslatorWorker.translateText("Final Date for Contracting") + ": ";
            generateOverAllTableRows(planningSubTable1, columnName, planning.getContractingDate(), null, true);
        }
        if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Final Date for Disbursements")) {
            columnName = TranslatorWorker.translateText("Final Date for Disbursements") + ": ";
            generateOverAllTableRows(planningSubTable1, columnName, planning.getDisbursementsDate(), null, true);
        }

        String commColumnName = "Final Date for Disbursements Comments";
        if (FeaturesUtil.isVisibleField(commColumnName)) {
            this.buildCommentsPart("Final Date for Disbursements", commColumnName, allComments, planningSubTable1);
        }
        commColumnName = "Current Completion Date Comments";
        if (FeaturesUtil.isVisibleField(commColumnName)) {
            this.buildCommentsPart("current completion date", commColumnName, allComments, planningSubTable1);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Proposed Project Life")) {
            columnName = TranslatorWorker.translateText("Proposed Project Life") + ": ";
            generateOverAllTableRows(planningSubTable1, columnName, planning.getProposedProjectLife() == null ? ""
                    : String.valueOf(planning.getProposedProjectLife()), null, true);
        }

        if (FeaturesUtil.isVisibleField("Duration of Project")) {
            columnName = TranslatorWorker.translateText("Duration of Project") + ": ";
            columnVal = "";
            if (planning.getProjectPeriod() != null) {
                columnVal = planning.getProjectPeriod().toString() + " " + TranslatorWorker.translateText("Months");
            }
            generateOverAllTableRows(planningSubTable1, columnName, columnVal, null);
        }
    }


    private void buildCommentsPart(String fieldName, String columnName, Map<String, List<AmpComments>> allComments,
                                   XWPFTable table) throws Exception {
        List<String> outList = new ArrayList<String>();
        for (Object commentKey : allComments.keySet()) {
            String key = (String) commentKey;
            List<AmpComments> values = (List<AmpComments>) allComments.get(key);
            if (key.equalsIgnoreCase(fieldName)) {
                for (AmpComments value : values) {
                    outList.add(TranslatorWorker.translateText(value.getComment()));
                }
            }
        }
        for (int i = 0; i < outList.size(); i++) {

            XWPFTableRow row = table.createRow();

            XWPFTableCell cell = row.getCell(0);
            setRun(cell.getParagraphs().get(0).createRun(), new RunStyle(FONT_FAMILY, FONT_SIZE_NORMAL, null, false),
                    TranslatorWorker.translateText(columnName) + " " + (i + 1), false);

            XWPFTableCell cellValue = row.getCell(1);
            setRun(cellValue.getParagraphs().get(0).createRun(), 
                    new RunStyle(FONT_FAMILY, FONT_SIZE_NORMAL, null, true), outList.get(i), false);
        }
    }

    /*
     * Structures
     */
    private void getStructures() throws CloneNotSupportedException,
            WorkerException {

        if (FeaturesUtil.isVisibleModule("/Activity Form/Structures")) {

            ExportSectionHelper eshTitle = new ExportSectionHelper(TranslatorWorker.translateText("Structures"),
                    true).setWidth(WIDTH).setAlign(STJc.LEFT);

            createSectionTable(eshTitle);

            Set<AmpStructure> structures = activity.getStructures();

            ArrayList<AmpStructure> res = new ArrayList<AmpStructure>();
            for (AmpStructure struc : structures) {
                ExportSectionHelper eshProjectCostTable = new ExportSectionHelper(
                        null, false).setWidth(WIDTH).setAlign(STJc.LEFT);
                eshProjectCostTable.addRowData(new ExportSectionHelperRowData(
                        "Name", null, null, true).addRowData(struc.getTitle()));
                String typeName = "";
                if (struc.getType() != null) {
                    typeName = struc.getType().getName();
                }
                eshProjectCostTable.addRowData(new ExportSectionHelperRowData("Type", null, null,
                        true).addRowData(typeName));

                eshProjectCostTable.addRowData(new ExportSectionHelperRowData(
                        "Description", null, null, true).addRowData(struc
                        .getDescription()));
                if (struc.getLatitude() != null) {
                    eshProjectCostTable.addRowData(new ExportSectionHelperRowData(
                            "Latitude", null, null, true).addRowData(struc
                            .getLatitude()));
                }
                if (struc.getLongitude() != null) {
                    eshProjectCostTable.addRowData(new ExportSectionHelperRowData(
                            "Longitude", null, null, true).addRowData(struc
                            .getLongitude()));
                }

                if (struc.getCoordinates() != null && struc.getCoordinates().size() > 0) {
                    StringJoiner coordinatesOutput = new StringJoiner("\n");
                    for (AmpStructureCoordinate coordinate : struc.getCoordinates()) {
                        coordinatesOutput.add(coordinate.getLatitude() + " " + coordinate.getLongitude());
                    }

                    eshProjectCostTable.addRowData(new ExportSectionHelperRowData(
                            "Coordinates", null, null, true)
                            .addRowData(coordinatesOutput.toString()));

                }
                createSectionTable(eshProjectCostTable);
            }

        }
    }

    /**
     * Proposed Project Cost
     */
    private void addProjectCostTables(final ProposedProjCost projCost, final String costName)
            throws WorkerException {

        if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Overview Section/" + costName)) {
            ExportSectionHelper eshTitle = new ExportSectionHelper(costName, true).setWidth(WIDTH).setAlign(STJc.LEFT);
            createSectionTable(eshTitle);
            String currencyCode = null;
            if (projCost != null) {
                currencyCode = projCost.getCurrencyCode();
            }
            if (currencyCode == null) {
                currencyCode = CurrencyUtil.getCurrencyByCode(Constants.DEFAULT_CURRENCY).getCurrencyCode();
            }
            ExportSectionHelper eshProjectCostTable = new ExportSectionHelper(null, false).
                    setWidth(WIDTH).setAlign(STJc.LEFT);
            eshProjectCostTable.addRowData(new ExportSectionHelperRowData("Cost", null, null,
                    true).
                    addRowLtrData(projCost == null ? null : projCost.getFunAmount()).
                    addRowData(currencyCode));
            eshProjectCostTable.addRowData(new ExportSectionHelperRowData("Date", null, null, true).
                    addRowLtrData(projCost == null ? null : projCost.getFunDate())
                    .addEmptyData());

            if ("Proposed Project Cost".equals(costName)
                    && FeaturesUtil.isVisibleModule(
                    "/Activity Form/Funding/Overview Section/Proposed Project Cost/Annual Proposed Project Cost")) {
                List<ProposedProjCost> proposedProjectCostList = activityForm.getFunding().getProposedAnnualBudgets();
                for (ProposedProjCost ppc : proposedProjectCostList) {
                    eshProjectCostTable.addRowData(new ExportSectionHelperRowData(
                            ppc.getFunDate(), null, null, true).addRowLtrData(
                            FormatHelper.formatNumber(ppc.getFunAmountAsDouble()))
                            .addRowData(ppc.getCurrencyCode()));
                }
            }
            createSectionTable(eshProjectCostTable);
        }
    }

    /*
     * Budget Structure
     */
    private void getBudgetStructureTables() throws WorkerException {

        if (FeaturesUtil.isVisibleModule("/Activity Form/Budget Structure")) {

            ExportSectionHelper eshTitle = new ExportSectionHelper("Budget Structure", true)
                    .setWidth(WIDTH).setAlign(STJc.LEFT);
            createSectionTable(eshTitle);

            ExportSectionHelper eshBudgetStructureTable = new ExportSectionHelper(null, false)
                    .setWidth(WIDTH).setAlign(STJc.LEFT);

            if (activity.getActBudgetStructure().size() > 0) {
                Iterator<AmpActivityBudgetStructure> it = activity.getActBudgetStructure().iterator();
                while (it.hasNext()) {
                    AmpActivityBudgetStructure abs = it.next();
                    eshBudgetStructureTable.addRowData(new ExportSectionHelperRowData(abs.getBudgetStructureName(),
                    null, null, true).
                            addRowData(FormatHelper.formatPercentage(abs.getBudgetStructurePercentage()) + "%"));
                }
            }

            createSectionTable(eshBudgetStructureTable);
        }

    }


    /*
     * Contact info. section
     */
    private void getContactInfoTables()
            throws WorkerException {
        HttpSession session = request.getSession();
        ExportSectionHelper eshTitle = new ExportSectionHelper("Contact Information", true).
                setWidth(WIDTH).setAlign(STJc.LEFT);

        boolean isContactInformationVisible = FeaturesUtil.isVisibleModule("/Activity Form/Contacts")
                && ((TeamMember) session.getAttribute(Constants.CURRENT_MEMBER) != null || FeaturesUtil
                .isVisibleFeature("Contacts"));

        if (isContactInformationVisible) {
            createSectionTable(eshTitle);
            ExportSectionHelper eshContactInfoTable = new ExportSectionHelper(null, false)
                    .setWidth(WIDTH).setAlign(STJc.LEFT);

            // Donor funding contact information
            if (FeaturesUtil.isVisibleModule("/Activity Form/Contacts/Donor Contact Information")) {
                buildContactInfoOutput(eshContactInfoTable, "Donor funding contact information",
                        activityForm.getContactInformation().getDonorContacts(), request);
            }
            // MOFED contact information
            if (FeaturesUtil.isVisibleModule("/Activity Form/Contacts/Mofed Contact Information")) {
                buildContactInfoOutput(eshContactInfoTable, "MOFED contact information",
                        activityForm.getContactInformation().getMofedContacts(), request);
            }
            // Sec Min funding contact information
            if (FeaturesUtil.isVisibleModule("/Activity Form/Contacts/Sector Ministry Contact Information")) {
                buildContactInfoOutput(eshContactInfoTable, "Sector Ministry contact information",
                        activityForm.getContactInformation().getSectorMinistryContacts(), request);
            }
            // Project Coordinator contact information
            if (FeaturesUtil.isVisibleModule("/Activity Form/Contacts/Project Coordinator Contact Information")) {
                buildContactInfoOutput(eshContactInfoTable, "Proj. Coordinator contact information",
                        activityForm.getContactInformation().getProjCoordinatorContacts(), request);
            }
            // Implementing/executing agency contact information
            if (FeaturesUtil.isVisibleModule("/Activity Form/Contacts/Implementing Executing Agency Contact "
            + "Information")) {
                buildContactInfoOutput(eshContactInfoTable, "Implementing/Executing Agency contact information",
                        activityForm.getContactInformation().getImplExecutingAgencyContacts(), request);
            }

            createSectionTable(eshContactInfoTable);
        }

    }

    private void buildContactInfoOutput(
            ExportSectionHelper eshContactInfoTable, String contactType,
            Collection<AmpActivityContact> contacts,
            HttpServletRequest request) throws WorkerException {

        eshContactInfoTable.addRowData(new ExportSectionHelperRowData(
                contactType, null, null, true));

        if (contacts != null && contacts.size() > 0) {
            String output = "";
            for (AmpActivityContact cont : contacts) {
                output = ExportUtil.getContactInformation(cont.getContact());
                ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
                        output, null, null, true);

                eshContactInfoTable.addRowData(rowData);
            }
        }
        eshContactInfoTable.addRowData(new ExportSectionHelperRowData(null,
                null, null, false).setSeparator(true));
    }


    /*
     * Related org.s section
     */
    private void getRelatedOrgsTables() throws WorkerException {

        ExportSectionHelper eshTitle = new ExportSectionHelper("Organizations", true).setWidth(WIDTH).setAlign(STJc
        .LEFT);
        ExportSectionHelper eshRelatedOrgsTable = new ExportSectionHelper(null, false).setWidth(WIDTH).setAlign(STJc
        .LEFT);

        if (FeaturesUtil.isVisibleModule("/Activity Form/Organizations")) {
            createSectionTable(eshTitle);

            if (activity.getOrgrole() != null && !activity.getOrgrole().isEmpty()) {
                Set<AmpOrgRole> orgRoles = activity.getOrgrole();

                Map<String, Set<AmpOrgRole>> roleGrouper = new HashMap<String, Set<AmpOrgRole>>();
                for (AmpOrgRole orgRole : orgRoles) {
                    if (!roleGrouper.containsKey(orgRole.getRole().getRoleCode())) {
                        roleGrouper.put(orgRole.getRole().getRoleCode(), new HashSet<AmpOrgRole>());
                    }
                    roleGrouper.get(orgRole.getRole().getRoleCode()).add(orgRole);
                }

                for (String roleCode : roleGrouper.keySet()) {
                    Set<AmpOrgRole> groupedRoleSet = roleGrouper.get(roleCode);

                    if (roleCode.equals(Constants.FUNDING_AGENCY)
                            && FeaturesUtil.isVisibleModule("/Activity Form/Organizations/Donor Organization")) {
                        buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, "Donor Agency", false);
                    }

                    if (roleCode.equals(Constants.RESPONSIBLE_ORGANISATION)
                            && FeaturesUtil.isVisibleModule("/Activity Form/Organizations/Responsible Organization")) {
                        buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, "Responsible Organization");
                    }

                    if (roleCode.equals(Constants.EXECUTING_AGENCY)
                            && FeaturesUtil.isVisibleModule("/Activity Form/Organizations/Executing Agency")) {
                        buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, "Executing Agency");
                    }

                    if (roleCode.equals(Constants.IMPLEMENTING_AGENCY)
                            && FeaturesUtil.isVisibleModule("/Activity Form/Organizations/Implementing Agency")) {
                        buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, "Implementing Agency");
                    }

                    if (roleCode.equals(Constants.BENEFICIARY_AGENCY)
                            && FeaturesUtil.isVisibleModule("/Activity Form/Organizations/Beneficiary Agency")) {
                        buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, "Beneficiary Agency");
                    }

                    if (roleCode.equals(Constants.CONTRACTING_AGENCY)
                            && FeaturesUtil.isVisibleModule("/Activity Form/Organizations/Contracting Agency")) {
                        buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, "Contracting Agency");
                    }

                    if (roleCode.equals(Constants.SECTOR_GROUP)
                            && FeaturesUtil.isVisibleModule("/Activity Form/Organizations/Sector Group")) {
                        buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, "Sector Group");
                    }

                    if (roleCode.equals(Constants.REGIONAL_GROUP)
                            && FeaturesUtil.isVisibleModule("/Activity Form/Organizations/Regional Group")) {
                        buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, "Regional Group");
                    }

                }

                createSectionTable(eshRelatedOrgsTable);
            } else {
                // This section has been adapted from ExportActivityToPDF, someday all exports should be reviewed and
                // merged.
                if (activityForm.getFunding().getFundingOrganizations() != null
                        && activityForm.getFunding().getFundingOrganizations().size() > 0) {
                    if (FeaturesUtil.isVisibleModule("/Activity Form/Funding")) {
                        buildRoleOrgInfo(activityForm.getFunding().getFundingOrganizations(), "Donor Agency",
                        eshRelatedOrgsTable);
                    }
                    createSectionTable(eshRelatedOrgsTable);
                }
            }
        }

    }

    private void buildRoleOrgInfo(ExportSectionHelper eshRelatedOrgsTable, Set<AmpOrgRole> groupedRoleSet, String
    roleName) {
        buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, roleName, true);
    }

    private void buildRoleOrgInfo(ExportSectionHelper eshRelatedOrgsTable, Set<AmpOrgRole> groupedRoleSet, String
    roleName, boolean addPercentage) {
        if (!groupedRoleSet.isEmpty()) {
            eshRelatedOrgsTable.addRowData(new ExportSectionHelperRowData(roleName, null, null, true));
            for (AmpOrgRole role : groupedRoleSet) {
                Double orgPercentage = role.getPercentage() == null ? new Double(0) : role.getPercentage();
                if (addPercentage) {
                    eshRelatedOrgsTable.addRowData(new ExportSectionHelperRowData(" ", null, null, false).
                            addRowData(role.getOrganisation().getName())
                            .addRowData(orgPercentage.toString() + "%"));
                } else {
                    eshRelatedOrgsTable.addRowData(new ExportSectionHelperRowData(" ", null, null, false).
                            addRowData(role.getOrganisation().getName())
                            .addRowData(""));
                }
            }
            eshRelatedOrgsTable.addRowData(new ExportSectionHelperRowData(null, null, null, false).setSeparator(true));
        }
    }

    private void buildRoleOrgInfo(List<FundingOrganization> groupedRoleSet, String roleName, ExportSectionHelper
    eshRelatedOrgsTable) {
        if (!groupedRoleSet.isEmpty()) {
            eshRelatedOrgsTable.addRowData(new ExportSectionHelperRowData(roleName, null, null, true));
            for (FundingOrganization role : groupedRoleSet) {
                eshRelatedOrgsTable.addRowData(new ExportSectionHelperRowData(" ", null, null, false).addRowData(role
                .getOrgName()));

            }
            eshRelatedOrgsTable.addRowData(new ExportSectionHelperRowData(null, null, null, false).setSeparator(true));
        }
    }

    /*
     * Issue section
     */
    private void getIssuesTables() throws WorkerException {

        if (FeaturesUtil.isVisibleModule("/Activity Form/Issues Section")) {
            ExportSectionHelper eshTitle = new ExportSectionHelper("Issues", true).setWidth(WIDTH).setAlign(STJc.LEFT);
            createSectionTable(eshTitle);

            if (activity.getIssues() != null && !activity.getIssues().isEmpty()) {
                Set<AmpIssues> issues = activity.getIssues();

                ExportSectionHelper eshIssuesTable = new ExportSectionHelper(null, false)
                        .setWidth(WIDTH).setAlign(STJc.LEFT);
                for (AmpIssues issue : issues) {
                    String issueName = issue.getName();
                    if (FeaturesUtil.isVisibleModule("/Activity Form/Issues Section/Issue/Date")) {
                        issueName += "  " + DateConversion.convertDateToLocalizedString(issue.getIssueDate());
                    }
                    eshIssuesTable.addRowData(new ExportSectionHelperRowData(issueName, null, null, false));
                    if (FeaturesUtil.isVisibleModule("/Activity Form/Issues Section/Issue/Measure")
                            && issue.getMeasures() != null && !issue.getMeasures().isEmpty()) {
                        for (AmpMeasure measure : (Set<AmpMeasure>) issue.getMeasures()) {
                            String measureName = measure.getName();
                            if (FeaturesUtil.isVisibleModule("/Activity Form/Issues Section/Issue/Measure/Date")) {
                                measureName += "  " + DateConversion.convertDateToLocalizedString(measure
                                .getMeasureDate());
                            }
                            eshIssuesTable.addRowData((new ExportSectionHelperRowData(" \u2022" + measureName, null,
                            null, false)));
                            if (FeaturesUtil.isVisibleModule("/Activity Form/Issues Section/Issue/Measure/Actor")
                                    && measure.getActors() != null && !measure.getActors().isEmpty()) {
                                for (AmpActor actor : (Set<AmpActor>) measure.getActors()) {
                                    eshIssuesTable.addRowData((new ExportSectionHelperRowData(" \t \u2022" + actor
                                    .getName(), null, null, false)));
                                }
                            }
                        }
                    }
                }
                createSectionTable(eshIssuesTable);
            }
        }

    }
    
    /*
     * Regional Observations
     */
    private void getRegionObservationsTables() throws WorkerException {
        String regObsModulePath = "/Activity Form/Regional Observations/Observation";
        String regObsDatePath = regObsModulePath + "/Date";
        String regObsMeasurePath = regObsModulePath + "/Measure";
        String regObsActorPath = regObsMeasurePath + "/Actor";
        
        if (FeaturesUtil.isVisibleModule(regObsModulePath)) {
            ExportSectionHelper eshTitle = new ExportSectionHelper("Regional Observations", true)
                    .setWidth(WIDTH).setAlign(STJc.LEFT);
            createSectionTable(eshTitle);
            
            if (activity.getRegionalObservations() != null && !activity.getRegionalObservations().isEmpty()) {
                Set<AmpRegionalObservation> regObsValues = activity.getRegionalObservations();
                
                ExportSectionHelper eshRegObsSection = new ExportSectionHelper(null, false)
                        .setWidth(WIDTH).setAlign(STJc.LEFT);
                for (AmpRegionalObservation regObs : regObsValues) {
                    String issueName = regObs.getName();
                    if (FeaturesUtil.isVisibleModule(regObsDatePath)) {
                        issueName += "  " + DateConversion.convertDateToLocalizedString(regObs.getObservationDate());
                    }
                    eshRegObsSection.addRowData(new ExportSectionHelperRowData(issueName, null, null, false));
                    if (FeaturesUtil.isVisibleModule(regObsMeasurePath)
                            && regObs.getRegionalObservationMeasures() != null
                            && !regObs.getRegionalObservationMeasures().isEmpty()) {
                        for (AmpRegionalObservationMeasure measure : regObs.getRegionalObservationMeasures()) {
                            String measureName = measure.getName();
                            eshRegObsSection.addRowData((new ExportSectionHelperRowData(" \u2022" + measureName, null,
                                    null, false)));
                            if (measure.getActors() != null && !measure.getActors().isEmpty()
                                    && FeaturesUtil.isVisibleModule(regObsActorPath)) {
                                for (AmpRegionalObservationActor actor : measure.getActors()) {
                                    eshRegObsSection.addRowData((new ExportSectionHelperRowData(" \t \u2022" + actor
                                            .getName(), null, null, false)));
                                }
                            }
                        }
                    }
                }
                createSectionTable(eshRegObsSection);
            }
        }
    }
    
    /*
     * Line Ministry Observations
     */
    private void getLineMinistryObservationsTables() throws WorkerException {
        String lmoModulePath = "/Activity Form/Line Ministry Observations/Observation";
        String lmoDatePath = lmoModulePath + "/Date";
        String lmoMeasurePath = lmoModulePath + "/Measure";
        String lmoActorPath = lmoMeasurePath + "/Actor";
        
        if (FeaturesUtil.isVisibleModule(lmoModulePath)) {
            ExportSectionHelper eshTitle = new ExportSectionHelper("Line Ministry Observations", true)
                    .setWidth(WIDTH).setAlign(STJc.LEFT);
            createSectionTable(eshTitle);
            
            if (activity.getLineMinistryObservations() != null && !activity.getLineMinistryObservations().isEmpty()) {
                Set<AmpLineMinistryObservation> lmoValues = activity.getLineMinistryObservations();
                
                ExportSectionHelper eshLmoSection = new ExportSectionHelper(null, false)
                        .setWidth(WIDTH).setAlign(STJc.LEFT);
                for (AmpLineMinistryObservation lmo : lmoValues) {
                    String issueName = lmo.getName();
                    if (FeaturesUtil.isVisibleModule(lmoDatePath)) {
                        issueName += "  " + DateConversion.convertDateToLocalizedString(lmo.getObservationDate());
                    }
                    eshLmoSection.addRowData(new ExportSectionHelperRowData(issueName, null, null, false));
                    if (FeaturesUtil.isVisibleModule(lmoMeasurePath) && lmo.getLineMinistryObservationMeasures() != null
                            && !lmo.getLineMinistryObservationMeasures().isEmpty()) {
                        for (AmpLineMinistryObservationMeasure measure : lmo.getLineMinistryObservationMeasures()) {
                            String measureName = measure.getName();
                            eshLmoSection.addRowData((new ExportSectionHelperRowData(" \u2022" + measureName, null,
                                    null, false)));
                            if (measure.getActors() != null && !measure.getActors().isEmpty()
                                    && FeaturesUtil.isVisibleModule(lmoActorPath)) {
                                for (AmpLineMinistryObservationActor actor : measure.getActors()) {
                                    eshLmoSection.addRowData((new ExportSectionHelperRowData(" \t \u2022" + actor
                                            .getName(), null, null, false)));
                                }
                            }
                        }
                    }
                }
                createSectionTable(eshLmoSection);
            }
        }
    }
    
    /*
     * Component funding section
     */
    private void getComponentTables() throws WorkerException {
        final String[] componentCommitmentsFMfields = {
                "/Activity Form/Components/Component/Components Commitments",
                "/Activity Form/Components/Component/Components Commitments/Commitment Table/Amount",
                "/Activity Form/Components/Component/Components Commitments/Commitment Table/Currency",
                "/Activity Form/Components/Component/Components Commitments/Commitment Table/Transaction Date",
                "/Activity Form/Components/Component/Components Commitments/Commitment Table/Component Organization",
                "/Activity Form/Components/Component/Components Commitments/Commitment Table/Second Reporting "
                        + "Organisation",
                "/Activity Form/Components/Component/Components Commitments/Commitment Table/Description"
        };
        final String[] componentDisbursementsFMfields = {
                "/Activity Form/Components/Component/Components Disbursements",
                "/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Amount",
                "/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Currency",
                "/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Transaction Date",
                "/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Component "
                        + "Organization",
                "/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Second Reporting "
                        + "Organisation",
                "/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Description"
        };
        final String[] componentExpendituresFMfields = {
                "/Activity Form/Components/Component/Components Expenditures",
                "/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Amount",
                "/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Currency",
                "/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Transaction Date",
                "/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Component Organization",
                "/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Second Reporting "
                        + "Organisation",
                "/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Description"
        };


        HttpSession session = request.getSession();
        ExportSectionHelper eshTitle = new ExportSectionHelper("Components", true).setWidth(WIDTH).setAlign(STJc.LEFT);

        if (FeaturesUtil.isVisibleModule("/Activity Form/Components")) {
            createSectionTable(eshTitle);

            boolean visibleModuleCompCommitments = FeaturesUtil.isVisibleModule(
                    "/Activity Form/Components/Component/Components Commitments");
            boolean visibleModuleCompDisbursements = FeaturesUtil.isVisibleModule(
                    "/Activity Form/Components/Component/Components Disbursements");
            boolean visibleModuleCompExpenditures = FeaturesUtil
                    .isVisibleModule(
                            "/Activity Form/Components/Component/Components Expenditures");
            if (activityForm.getComponents().getSelectedComponents() != null) {
                for (Components<FundingDetail> comp : activityForm.getComponents().getSelectedComponents()) {
                    if (!org.digijava.module.aim.helper.GlobalSettings.getInstance()
                            .getShowComponentFundingByYear()) {
                        ExportSectionHelper eshCompFundingDetails = new ExportSectionHelper(
                                null, false).setWidth(WIDTH).setAlign(STJc.LEFT);
                        eshCompFundingDetails
                                .addRowData((new ExportSectionHelperRowData(comp
                                        .getTitle(), null, null, false)));

                        String compDesc = comp.getDescription() != null ? comp.getDescription() : "";
                        String compTypeName = comp.getTypeName() != null ? comp.getTypeName() : "";

                        eshCompFundingDetails
                                .addRowData((new ExportSectionHelperRowData(
                                        "Description", null, null, true))
                                        .addRowData(compDesc));

                        eshCompFundingDetails
                                .addRowData((new ExportSectionHelperRowData(
                                        "Component Type", null, null, true))
                                        .addRowData(compTypeName));

                        eshCompFundingDetails
                                .addRowData((new ExportSectionHelperRowData(
                                        "Component Funding", null, null, true)));

                        if (visibleModuleCompCommitments
                                && comp.getCommitments() != null
                                && comp.getCommitments().size() > 0) {
                            createComponentDetails(eshCompFundingDetails,
                                    comp.getCommitments(),
                                    componentCommitmentsFMfields, session);
                        }
                        if (visibleModuleCompDisbursements
                                && comp.getDisbursements() != null
                                && comp.getDisbursements().size() > 0) {
                            createComponentDetails(eshCompFundingDetails,
                                    comp.getDisbursements(),
                                    componentDisbursementsFMfields, session);
                        }

                        if (visibleModuleCompExpenditures
                                && comp.getExpenditures() != null
                                && comp.getExpenditures().size() > 0) {
                            createComponentDetails(eshCompFundingDetails,
                                    comp.getExpenditures(),
                                    componentExpendituresFMfields, session);
                        }

                        int amountsUnitCode = Integer.valueOf(FeaturesUtil
                                .getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS));

                        if (amountsUnitCode == AmpARFilter.AMOUNT_OPTION_IN_THOUSANDS) {
                            ExportSectionHelperRowData sectionHelper = new ExportSectionHelperRowData(
                                    "The amount entered are in thousands (000)",
                                    null, null, true);
                            eshCompFundingDetails.addRowData(sectionHelper);
                        }

                        if (amountsUnitCode == AmpARFilter.AMOUNT_OPTION_IN_MILLIONS) {
                            ExportSectionHelperRowData sectionHelper = new ExportSectionHelperRowData(
                                    "The amount entered are in millions (000 000)",
                                    null, null, true);
                            eshCompFundingDetails.addRowData(sectionHelper);
                        }

                        createSectionTable(eshCompFundingDetails);

                    } else if (org.digijava.module.aim.helper.GlobalSettings.getInstance()
                            .getShowComponentFundingByYear()
                            && FeaturesUtil.isVisibleModule("Components Resume")) {
                        ExportSectionHelper fundingByYearSection = new ExportSectionHelper(
                                null, false).setWidth(WIDTH).setAlign(STJc.LEFT);
                        ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
                                "Component Code", null, null, true).addRowData(comp
                                .getCode());
                        fundingByYearSection.addRowData(rowData);
                        rowData = new ExportSectionHelperRowData(
                                "Finance of the component", null, null, true);

                        createFinanceOfComponentSection(fundingByYearSection, comp);

                        createSectionTable(fundingByYearSection);
                    }
                }
            }
        }

    }

    private void createFinanceOfComponentSection(
            ExportSectionHelper fundingByYearSection,
            Components<FundingDetail> comp) {
        for (Integer key : comp.getFinanceByYearInfo().keySet()) {

            ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
                    key.toString());
            fundingByYearSection.addRowData(rowData);

            Map<String, Double> myMap = comp.getFinanceByYearInfo().get(key);
            String plannedCommSum = FormatHelper.formatNumber(myMap.get("MontoProgramado"));
            rowData = new ExportSectionHelperRowData("Planned Commitments Sum",
                    null, null, true).addRowData(plannedCommSum);
            fundingByYearSection.addRowData(rowData);

            String actCommSum = FormatHelper.formatNumber(myMap.get("MontoReprogramado"));
            rowData = new ExportSectionHelperRowData("Actual Commitments Sum",
                    null, null, true).addRowData(actCommSum);
            fundingByYearSection.addRowData(rowData);

            String actExpSum = FormatHelper.formatNumber(myMap.get("MontoEjecutado"));

            rowData = new ExportSectionHelperRowData("Actual Expenditures Sum",
                    null, null, true).addRowData(actExpSum);
            fundingByYearSection.addRowData(rowData);
        }
    }

    private void createComponentDetails(
            final ExportSectionHelper eshCompFundingDetails,
            Collection<FundingDetail> listToIterate,
            final String[] componentFMfields, final HttpSession session) {

        for (FundingDetail compFnd : listToIterate) {
            ExportSectionHelperRowData sectionHelper = new ExportSectionHelperRowData(
                    FundingCalculationsHelper.getTransactionTypeLabel(compFnd.getTransactionType()),
                    null, null, true);
            if (FeaturesUtil.isVisibleModule(componentFMfields[ExportUtil.COMPONENT_FM_FIELD_TYPE])) {
                sectionHelper.addRowSimpleData(
                        compFnd.getAdjustmentTypeNameTrimmed(), true);
            }
            if (FeaturesUtil.isVisibleModule(componentFMfields[ExportUtil.COMPONENT_FM_FIELD_AMOUNT])) {
                sectionHelper.addRowLtrData(compFnd.getTransactionAmount());
                String currency = "";
                if (FeaturesUtil.isVisibleModule(componentFMfields[ExportUtil.COMPONENT_FM_FIELD_CURRENCY])) {
                    currency = compFnd.getCurrencyCode();
                }
                sectionHelper.addRowData(currency);
            }
            if (FeaturesUtil.isVisibleModule(componentFMfields[ExportUtil.COMPONENT_FM_FIELD_TRANSCTION_DATE])) {
                sectionHelper.addRowLtrData(compFnd.getTransactionDate());
            }

            sectionHelper
                    .addRowData(compFnd.getFormattedRate() != null ? compFnd
                            .getFormattedRate() : "");

            eshCompFundingDetails.addRowData(sectionHelper);

            if (FeaturesUtil.isVisibleModule(componentFMfields[ExportUtil.COMPONENT_FM_FIELD_ORGANISATION])) {
                ExportSectionHelperRowData organisationHelper = new ExportSectionHelperRowData("",
                        null, null, false);
                String orgString = compFnd.getComponentOrganisation() != null ? compFnd.getComponentOrganisation()
                        .getName() : "";
                eshCompFundingDetails.addRowData(getExportSectionHelperRowData("Organization",
                        orgString, compFnd));
            }

            if (FeaturesUtil.isVisibleModule(componentFMfields[ExportUtil.COMPONENT_FM_FIELD_SECOND_REPORTING])) {
                String orgString = compFnd.getComponentSecondResponsibleOrganization() != null ? compFnd
                        .getComponentSecondResponsibleOrganization().getName() : "";
                eshCompFundingDetails.addRowData(
                        getExportSectionHelperRowData("Component Second Responsible Organization", orgString,
                                compFnd));
            }

            if (FeaturesUtil.isVisibleModule(componentFMfields[ExportUtil.COMPONENT_FM_FIELD_DESCRIPTION])) {
                ExportSectionHelperRowData organisationHelper = new ExportSectionHelperRowData(null,
                        null, null, true);
                String compTransStr = compFnd.getComponentTransactionDescription() == null ? "" : compFnd
                        .getComponentTransactionDescription();
                eshCompFundingDetails.addRowData(getExportSectionHelperRowData("Transaction Description",
                        compTransStr, compFnd));
            }


        }

    }

    private ExportSectionHelperRowData getExportSectionHelperRowData(final String title, final String orgString,
                                                                     final FundingDetail compFnd) {
        ExportSectionHelperRowData organisationHelper = new ExportSectionHelperRowData(null,
                null, null, false);
        organisationHelper.addRowData(TranslatorWorker.translateText(title) + ": " + orgString);
        return organisationHelper;
    }


    /*
     * Regional funding section
     */
    private void getRegionalFundingTables() throws WorkerException {

        ExportSectionHelper eshTitle = new ExportSectionHelper("Regional Fundings", true)
                .setWidth(WIDTH).setAlign(STJc.LEFT);
        if (FeaturesUtil.isVisibleModule("/Activity Form/Regional Funding")) {
            createSectionTable(eshTitle);
            if (activity.getRegionalFundings() != null && !activity.getRegionalFundings().isEmpty()) {
                Set<AmpRegionalFunding> regFnds = activity.getRegionalFundings();
                boolean visibleModuleRegCommitments = FeaturesUtil.isVisibleModule(
                        "/Activity Form/Regional Funding/Region Item/Commitments");
                boolean visibleModuleRegDisbursements = FeaturesUtil.isVisibleModule(
                        "/Activity Form/Regional Funding/Region Item/Disbursements");
                boolean visibleModuleRegExpenditures = FeaturesUtil.isVisibleModule(
                        "/Activity Form/Regional Funding/Region Item/Expenditures");
                for (AmpRegionalFunding regFnd : regFnds) {
                    // validating module visibility
                    // Commitments
                    if (regFnd.getTransactionType() == Constants.COMMITMENT && visibleModuleRegCommitments) {
                        ExportSectionHelper eshRegFundingDetails = new ExportSectionHelper(null, false)
                                .setWidth(WIDTH).setAlign(STJc.LEFT);
                        eshRegFundingDetails.addRowData((new ExportSectionHelperRowData(FundingCalculationsHelper
                        .getTransactionTypeLabel(regFnd
                                .getTransactionType()), null, null, true))
                                .addRowData(regFnd.getRegionLocation().getName())
                                .addRowSimpleData(regFnd.getAdjustmentType().getLabel(), true)
                                .addRowLtrData(DateConversion.convertDateToString(regFnd.getTransactionDate()))
                                .addRowLtrData(regFnd.getTransactionAmount().toString())
                                .addRowData(regFnd.getCurrency().getCurrencyCode()));
                        createSectionTable(eshRegFundingDetails);
                    }
                }
                for (AmpRegionalFunding regFnd : regFnds) {
                    // validating module visibility
                    // Disbursements
                    if (regFnd.getTransactionType() == Constants.DISBURSEMENT && visibleModuleRegDisbursements) {
                        ExportSectionHelper eshRegFundingDetails = new ExportSectionHelper(null, false)
                                .setWidth(WIDTH).setAlign(STJc.LEFT);
                        eshRegFundingDetails.addRowData((new ExportSectionHelperRowData(FundingCalculationsHelper
                        .getTransactionTypeLabel(regFnd
                                .getTransactionType()), null, null, true))
                                .addRowData(regFnd.getRegionLocation().getName())
                                .addRowSimpleData(regFnd.getAdjustmentType().getLabel(), true)
                                .addRowLtrData(DateConversion.convertDateToString(regFnd.getTransactionDate()))
                                .addRowLtrData(regFnd.getTransactionAmount().toString())
                                .addRowData(regFnd.getCurrency().getCurrencyCode()));
                        createSectionTable(eshRegFundingDetails);
                    }
                }
                for (AmpRegionalFunding regFnd : regFnds) {
                    // validating module visibility
                    // Expenditures
                    if (regFnd.getTransactionType() == Constants.EXPENDITURE && visibleModuleRegExpenditures) {
                        ExportSectionHelper eshRegFundingDetails = new ExportSectionHelper(null, false)
                                .setWidth(WIDTH).setAlign(STJc.LEFT);
                        eshRegFundingDetails.addRowData((new ExportSectionHelperRowData(FundingCalculationsHelper
                        .getTransactionTypeLabel(regFnd
                                .getTransactionType()), null, null, true))
                                .addRowData(regFnd.getRegionLocation().getName())
                                .addRowSimpleData(regFnd.getAdjustmentType().getLabel(), true)
                                .addRowLtrData(DateConversion.convertDateToString(regFnd.getTransactionDate()))
                                .addRowLtrData(regFnd.getTransactionAmount().toString())
                                .addRowData(regFnd.getCurrency().getCurrencyCode()));
                        createSectionTable(eshRegFundingDetails);
                    }
                }
            }
        }

    }

    protected String formatNumber(final Double val) {
        return FormatHelper.formatNumber(FeaturesUtil.applyThousandsForVisibility(val));
    }

    /*
     * Donor funding section
     */
    private void getDonorFundingTables() throws WorkerException {
        if (FeaturesUtil.isVisibleModule("/Activity Form/Funding")) {
            ExportSectionHelper eshTitle = new ExportSectionHelper("Donor Funding", true).setWidth(WIDTH)
                    .setAlign(STJc.LEFT);
            HttpSession session = request.getSession();

            createSectionTable(eshTitle);

            boolean visibleModuleCommitments = FeaturesUtil.isVisibleModule(
                    "/Activity Form/Funding/Funding Group/Funding Item/Commitments");
            boolean visibleModuleDisbursement = FeaturesUtil.isVisibleModule(
                    "/Activity Form/Funding/Funding Group/Funding Item/Disbursements");
            boolean visibleModuleExpenditures = FeaturesUtil.isVisibleModule(
                    "/Activity Form/Funding/Funding Group/Funding Item/Expenditures");
            boolean visibleModuleArrears = FeaturesUtil.isVisibleModule(
                    "/Activity Form/Funding/Funding Group/Funding Item/Arrears");
            boolean visibleModuleRoF = FeaturesUtil.isVisibleModule(
                    "/Activity Form/Funding/Funding Group/Funding Item/Release of Funds");
            boolean visibleModuleEDD = FeaturesUtil.isVisibleModule(
                    "/Activity Form/Funding/Funding Group/Funding Item/Estimated Disbursements");
            boolean visibleModuleDisbOrders = FeaturesUtil.isVisibleModule(
                    "/Activity Form/Funding/Funding Group/Funding Item/Disbursement Orders");
            boolean visibleModuleMTEFProjections = FeaturesUtil.isVisibleModule(
                    "/Activity Form/Funding/Funding Group/Funding Item/MTEF Projections");

            boolean mtefExisting = false; //more spaghetti!
            if (activity.getFunding() != null && !activity.getFunding().isEmpty()) {
                for (AmpFunding fnd : (Set<AmpFunding>) activity.getFunding()) {
                    ExportSectionHelper eshDonorInfo = new ExportSectionHelper(null, false)
                            .setWidth(WIDTH).setAlign(STJc.LEFT);

                    buildFunding(fnd, eshDonorInfo);

                    createSectionTable(eshDonorInfo);

                    Set<AmpFundingDetail> fndDets = fnd.getFundingDetails();

                    Map<String, Map<String, Set<AmpFundingDetail>>> structuredFundings = getStructuredFundings(fndDets);
                    String toCurrCode = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);

                    TeamMember tm = (TeamMember) session.getAttribute("currentMember");
                    if (tm != null && tm.getAppSettings() != null) {
                        toCurrCode = CurrencyUtil.getAmpcurrency(tm.getAppSettings().getCurrencyId()).getCurrencyCode();
                    }

                    if (structuredFundings.size() > 0) {
                        ExportSectionHelper eshDonorFundingDetails =
                                new ExportSectionHelper(null, false).setWidth(WIDTH)

                                .setAlign(STJc.LEFT);
                        for (String transTypeKey : structuredFundings.keySet()) {

                            Map<String, Set<AmpFundingDetail>> transTypeGroup = structuredFundings.get(transTypeKey);
                            for (String adjTypeKey : transTypeGroup.keySet()) {
                                eshDonorFundingDetails.addRowData(new ExportSectionHelperRowData(new StringBuilder(
                                        adjTypeKey).append(" ").append(transTypeKey).toString(), null, null, true));
                                Set<AmpFundingDetail> structuredFndDets = transTypeGroup.get(adjTypeKey);

                                ArrayList<AmpFundingDetail> fundingDetails = new ArrayList<AmpFundingDetail>();
                                fundingDetails.addAll(structuredFndDets);
                                Collections.sort(fundingDetails, FundingDetailComparator.getFundingDetailComparator());

                                FundingCalculationsHelper fundingCalculations = new FundingCalculationsHelper();
                                fundingCalculations.doCalculations(fundingDetails, toCurrCode, true);
                                for (FundingDetail fndDet : fundingCalculations.getFundDetailList()) {
                                    // validating module visibility
                                    // Commitments
                                    if ((fndDet.getTransactionType() == Constants.COMMITMENT
                                            && visibleModuleCommitments)
                                            // Disbursements
                                            || (fndDet.getTransactionType() == Constants.DISBURSEMENT
                                            && visibleModuleDisbursement)
                                            // Expenditures
                                            || (fndDet.getTransactionType() == Constants.EXPENDITURE
                                            && visibleModuleExpenditures)
                                            // Release of Funds
                                            || (fndDet.getTransactionType() == Constants.RELEASE_OF_FUNDS
                                            && visibleModuleRoF)
                                            // Estimated Disbursements
                                            || (fndDet.getTransactionType() == Constants.ESTIMATED_DONOR_DISBURSEMENT
                                            && visibleModuleEDD)
                                            // DisbOrders
                                            || (fndDet.getTransactionType() == Constants.DISBURSEMENT_ORDER
                                            && visibleModuleDisbOrders)
                                            // Arrears
                                            || (fndDet.getTransactionType() == Constants.ARREARS
                                            && visibleModuleArrears)) {
                                        buildFundingDetail(eshDonorFundingDetails, fndDet);
                                    }
                                }

                                processSubTotals(toCurrCode, eshDonorFundingDetails, transTypeKey, adjTypeKey,
                                        fundingCalculations);

                                eshDonorFundingDetails.addRowData(new ExportSectionHelperRowData(null)
                                        .setSeparator(true));
                            }
                        }
                        createSectionTable(eshDonorFundingDetails);
                    }

                    // MTEF Projections
                    if (visibleModuleMTEFProjections && fnd.getMtefProjections() != null
                            && fnd.getMtefProjections().size() > 0) {
                        ExportSectionHelper mtefSection = renderMtefSection(fnd, toCurrCode);
                        createSectionTable(mtefSection);
                    }
                }
            }


            // TOTALS
            ExportSectionHelper fundingTotalsDetails = getDonorFundingTablesTotals(visibleModuleCommitments,
                    visibleModuleDisbursement, visibleModuleExpenditures, visibleModuleArrears, visibleModuleRoF,
                    visibleModuleDisbOrders, mtefExisting);

            // Delivery Rate
            if (activityForm.getFunding().getDeliveryRate() != null
                    && activityForm.getFunding().getDeliveryRate().length() > 0) {
                addTotalsOutput(fundingTotalsDetails, "Delivery Rate",
                        activityForm.getFunding().getDeliveryRate().replace("%", ""), "%");
            }

            createSectionTable(fundingTotalsDetails);
        }
    }

    private void processSubTotals(String toCurrCode, ExportSectionHelper eshDonorFundingDetails, String transTypeKey,
                                  String adjTypeKey, FundingCalculationsHelper fundingCalculations) {

        String subTotal = "";
        String subTotalValue = "";

        if (transTypeKey.equals("Commitment") && adjTypeKey.equals("Actual")) {
            subTotal = TranslatorWorker.translateText("Sub-Total") + " " + TranslatorWorker
                    .translateText("Commitment") + " " + TranslatorWorker.translateText("Actual")
                    + ":";
            subTotalValue = formatNumber(fundingCalculations.getTotActualComm().doubleValue());
        } else if (transTypeKey.equals("Commitment") && adjTypeKey.equals("Planned")) {
            subTotal = TranslatorWorker.translateText("Sub-Total") + " " + TranslatorWorker
                    .translateText("Commitment") + " "
                    + TranslatorWorker.translateText("Planned") + ":";
            subTotalValue = formatNumber(fundingCalculations.getTotPlannedComm().doubleValue());
        } else if (transTypeKey.equals("Commitment") && adjTypeKey.equals("Pipeline")) {
            subTotal = TranslatorWorker.translateText("Sub-Total") + " " + TranslatorWorker
                    .translateText("Commitment") + " "
                    + TranslatorWorker.translateText("Pipeline") + ":";
            subTotalValue = formatNumber(fundingCalculations.getTotPipelineComm().doubleValue());
        } else if (transTypeKey.equals("Disbursement") && adjTypeKey.equals("Actual")) {
            subTotal = TranslatorWorker.translateText("Sub-Total") + " " + TranslatorWorker
                    .translateText("Disbursement") + " "
                    + TranslatorWorker.translateText("Actual") + ":";
            subTotalValue = formatNumber(fundingCalculations.getTotActualDisb().doubleValue());
        } else if (transTypeKey.equals("Disbursement") && adjTypeKey.equals("Planned")) {
            subTotal = TranslatorWorker.translateText("Sub-Total") + " " + TranslatorWorker
                    .translateText("Disbursement") + " "
                    + TranslatorWorker.translateText("Planned") + ":";
            subTotalValue = formatNumber(fundingCalculations.getTotPlanDisb().doubleValue());
        } else if (transTypeKey.equals("Disbursement") && adjTypeKey.equals("Pipeline")) {
            subTotal = TranslatorWorker.translateText("Sub-Total") + " " + TranslatorWorker
                    .translateText("Disbursement") + " "
                    + TranslatorWorker.translateText("Pipeline") + ":";
            subTotalValue = formatNumber(fundingCalculations.getTotPipelineDisb().doubleValue());
        } else if (transTypeKey.equals("Estimated Disbursement") && adjTypeKey.equals("Actual")) {
            subTotal = TranslatorWorker.translateText("Sub-Total") + " " + TranslatorWorker
                    .translateText("Estimated Disbursement") + " "
                    + TranslatorWorker.translateText("Actual") + ":";
            subTotalValue = formatNumber(fundingCalculations.getTotActualEDD().doubleValue());
        } else if (transTypeKey.equals("Estimated Disbursement") && adjTypeKey.equals("Planned")) {
            subTotal = TranslatorWorker.translateText("Sub-Total") + " " + TranslatorWorker
                    .translateText("Estimated Disbursement") + " "
                    + TranslatorWorker.translateText("Planned") + ":";
            subTotalValue = formatNumber(fundingCalculations.getTotPlannedEDD().doubleValue());
        } else if (transTypeKey.equals("Estimated Disbursement") && adjTypeKey.equals("Pipeline")) {
            subTotal = TranslatorWorker.translateText("Sub-Total") + " " + TranslatorWorker
                    .translateText("Estimated Disbursement") + " "
                    + TranslatorWorker.translateText("Pipeline") + ":";
            subTotalValue = formatNumber(fundingCalculations.getTotPipelineEDD().doubleValue());
        } else if (transTypeKey.equals("Expenditure") && adjTypeKey.equals("Actual")) {
            subTotal = TranslatorWorker.translateText("Sub-Total") + " " + TranslatorWorker
                    .translateText("Expenditure") + " "
                    + TranslatorWorker.translateText("Actual") + ":";
            subTotalValue = formatNumber(fundingCalculations.getTotActualExp().doubleValue());
        } else if (transTypeKey.equals("Expenditure") && adjTypeKey.equals("Planned")) {
            subTotal = TranslatorWorker.translateText("Sub-Total") + " " + TranslatorWorker
                    .translateText("Expenditure") + " "
                    + TranslatorWorker.translateText("Planned") + ":";
            subTotalValue = formatNumber(fundingCalculations.getTotPlannedExp().doubleValue());
        } else if (transTypeKey.equals("Expenditure") && adjTypeKey.equals("Pipeline")) {
            subTotal = TranslatorWorker.translateText("Sub-Total") + " " + TranslatorWorker
                    .translateText("Expenditure") + " "
                    + TranslatorWorker.translateText("Pipeline") + ":";
            subTotalValue = formatNumber(fundingCalculations.getTotPipelineExp().doubleValue());
        } else if (transTypeKey.equals("Release of Funds") && adjTypeKey.equals("Pipeline")) {
            subTotal = TranslatorWorker.translateText("Sub-Total") + " " + TranslatorWorker
                    .translateText("Release of Funds") + " "
                    + TranslatorWorker.translateText("Pipeline")
                    + ":";
            subTotalValue = formatNumber(fundingCalculations.getTotPipelineReleaseOfFunds()
                    .doubleValue());
        } else if (transTypeKey.equals("Release of Funds") && adjTypeKey.equals("Actual")) {
            subTotal = TranslatorWorker.translateText("Sub-Total") + " " + TranslatorWorker
                    .translateText("Release of Funds") + " "
                    + TranslatorWorker.translateText("Actual")
                    + ":";
            subTotalValue = formatNumber(fundingCalculations.getTotActualReleaseOfFunds()
                    .doubleValue());
        } else if (transTypeKey.equals("Release of Funds") && adjTypeKey.equals("Planned")) {
            subTotal = TranslatorWorker.translateText("Sub-Total") + " " + TranslatorWorker
                    .translateText("Release of Funds") + " "
                    + TranslatorWorker.translateText("Planned")
                    + ":";
            subTotalValue = formatNumber(fundingCalculations.getTotPlannedReleaseOfFunds()
                    .doubleValue());
        }

        eshDonorFundingDetails.addRowData(new ExportSectionHelperRowData(subTotal)
                .addEmptyData()
                .addEmptyData()
                .addEmptyData()
                .addRowLtrData(subTotalValue)
                .addRowData(toCurrCode));
    }

    private void buildFundingDetail(ExportSectionHelper eshDonorFundingDetails, FundingDetail fndDet) {
        ExportSectionHelperRowData sectionHelperRowData = new ExportSectionHelperRowData(
                FundingCalculationsHelper.getTransactionTypeLabel(fndDet
                        .getTransactionType()), null, null, true);
        String disasterResponse = "";
        if (Boolean.TRUE.equals(fndDet.getDisasterResponse())) {
            disasterResponse = TranslatorWorker.translateText("Disaster Response");
        }

        ExportSectionHelperRowData currentRowData = sectionHelperRowData.
                addRowSimpleData(fndDet.getAdjustmentTypeName().getLabel(), true).
                addRowData(disasterResponse).
                addRowLtrData(fndDet.getTransactionDate()).
                addRowLtrData(fndDet.getTransactionAmount()).
                addRowData(fndDet.getCurrencyCode());

        if (fndDet.getFixedExchangeRate() != null) {
            String exchangeRateStr = TranslatorWorker.translateText("Exchange Rate: ");
            exchangeRateStr += DECIMAL_FORMAT.format(FormatHelper.parseDouble(fndDet
                    .getFixedExchangeRate()));
            currentRowData.addRowData(exchangeRateStr);
        }
        String rolesOrgFundingFlows = getRoleAndOrgForFundingFlows(fndDet
                        .getRecipientOrganisation(), fndDet.getRecipientOrganisationRole(),
                ActivityUtil.getFmForFundingFlows(fndDet.getTransactionType()));

        if (rolesOrgFundingFlows != null) {
            currentRowData.addRowData(rolesOrgFundingFlows);
        }

        eshDonorFundingDetails.addRowData(sectionHelperRowData);

        if (fndDet.getPledge() != null && fndDet.getPledge() > 0) {
            ExportSectionHelperRowData pledgeSectorData = new ExportSectionHelperRowData(
                    null, null, null, true);
            pledgeSectorData.addRowData(TranslatorWorker.translateText("Source Pledge")
                    + ": " + fndDet.getPledgename());
            eshDonorFundingDetails.addRowData(pledgeSectorData);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding "
                + "Item/Expenditures/Expenditures Table/Expenditure Class")
                && fndDet.getExpenditureClass() != null) {
            ExportSectionHelperRowData data = new ExportSectionHelperRowData(null, null,
                    null, true);
            data.addRowData(TranslatorWorker.translateText("Expenditure Class") + ": "
                    + fndDet.getExpenditureClass());
            eshDonorFundingDetails.addRowData(data);
        }
    }

    private void buildFunding(AmpFunding fnd, ExportSectionHelper eshDonorInfo) {
        if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Funding "
                + "Classification/Funding Organization Id")) {
            eshDonorInfo.addRowData((new ExportSectionHelperRowData("Funding Organization Id", null,
                    null, true)).addRowData(fnd.getFinancingId()));
        }

        eshDonorInfo.addRowData((new ExportSectionHelperRowData("Funding Organization Name", null,
                null, true)).addRowData(fnd.getAmpDonorOrgId().getName()));

        AmpRole orgRole = fnd.getSourceRole();
        eshDonorInfo.addRowData((new ExportSectionHelperRowData("Organization Role", null, null,
                true)).addRowData(orgRole != null ? orgRole.getName() : ""));

        if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Funding "
                + "Classification/Type of Assistence")) {
            AmpCategoryValue typeOfAssistance = fnd.getTypeOfAssistance();
            eshDonorInfo.addRowData((new ExportSectionHelperRowData("Type of Assistance", null, null,
                    true)).addRowData(typeOfAssistance != null ? typeOfAssistance.getLabel() : ""));
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Funding "
                + "Classification/Financing Instrument")) {
            AmpCategoryValue financingInstrument = fnd.getFinancingInstrument();
            eshDonorInfo.addRowData((new ExportSectionHelperRowData("Financial Instrument", null, null,
                    true)).addRowData(financingInstrument != null ? financingInstrument.getLabel() : ""));
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Funding "
                + "Classification/Funding Status")) {
            String fndStatus = fnd.getFundingStatus() != null ? fnd.getFundingStatus().getValue() : " ";
            eshDonorInfo.addRowData((new ExportSectionHelperRowData("Funding Status", null, null, true))
                    .addRowData(fndStatus));
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Funding "
                + "Classification/Mode of Payment")) {
            String modeOfPayment = fnd.getModeOfPayment() != null ? fnd.getModeOfPayment().getValue() : " ";
            eshDonorInfo.addRowData((new ExportSectionHelperRowData("Mode of Payment", null, null, true))
                    .addRowData(modeOfPayment));
        }
        if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Funding "
        + "Classification/Concessionality Level")) {
            String concessionalityLevel = fnd.getConcessionalityLevel() != null
                    ? fnd.getConcessionalityLevel().getValue() : " ";
            eshDonorInfo.addRowData((new ExportSectionHelperRowData("Concessionality Level", null, null, true))
                    .addRowData(concessionalityLevel));
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Donor Objective")
                && (fnd.getDonorObjective() != null)) {
            eshDonorInfo.addRowData((new ExportSectionHelperRowData("Donor Objective", null, null, true))
                    .addRowData(fnd.getDonorObjective()));
        }
        if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Conditions")
                && (fnd.getConditions() != null)) {
            eshDonorInfo.addRowData((new ExportSectionHelperRowData("Conditions", null, null, true))
                    .addRowData(fnd.getConditions()));
        }
        if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Funding "
                + "Classification/Agreement")) {
            String agreementTitle = fnd.getAgreement() != null ? fnd.getAgreement().getTitle() : " ";
            eshDonorInfo.addRowData((new ExportSectionHelperRowData("Agreement Title", null, null, true))
                    .addRowData(agreementTitle));
            String agreementCode = fnd.getAgreement() != null ? fnd.getAgreement().getCode() : " ";
            eshDonorInfo.addRowData((new ExportSectionHelperRowData("Agreement Code", null, null, true))
                    .addRowData(agreementCode));
        }

        if (fnd.getFundingClassificationDate() != null) {
            eshDonorInfo.addRowData(new ExportSectionHelperRowData("Funding Classification Date", null, null,
                    true)
                    .addRowLtrData(DateConversion.convertDateToLocalizedString(fnd.getFundingClassificationDate()
                    )));
        }
        if (fnd.getEffectiveFundingDate() != null) {
            eshDonorInfo.addRowData(new ExportSectionHelperRowData("Effective Funding Date", null, null, true)
                    .addRowLtrData(DateConversion.convertDateToString(fnd.getEffectiveFundingDate())));
        }
        if (fnd.getFundingClosingDate() != null) {
            eshDonorInfo.addRowData(new ExportSectionHelperRowData("Funding Closing Date", null, null, true)
                    .addRowLtrData(DateConversion.convertDateToString(fnd.getFundingClosingDate())));
        }

        if (fnd.getRatificationDate() != null) {
            eshDonorInfo.addRowData(new ExportSectionHelperRowData("Ratification Date", null, null, true)
                    .addRowLtrData(DateConversion.convertDateToLocalizedString(fnd.getRatificationDate())));
        }

        if (fnd.getMaturity() != null) {
            eshDonorInfo.addRowData(new ExportSectionHelperRowData("Maturity", null, null, true)
                    .addRowLtrData(DateConversion.convertDateToLocalizedString(fnd.getMaturity())));
        }

        if (fnd.getInterestRate() != null) {
            eshDonorInfo.addRowData(new ExportSectionHelperRowData("Interest Rate", null, null, true)
                    .addRowLtrData(String.valueOf(fnd.getInterestRate())));
        }

        if (fnd.getGracePeriod() != null) {
            eshDonorInfo.addRowData(new ExportSectionHelperRowData("Grace Period", null, null, true)
                    .addRowLtrData(String.valueOf(fnd.getGracePeriod())));
        }

        eshDonorInfo.addRowData(new ExportSectionHelperRowData(null).setSeparator(true));
    }

    private ExportSectionHelper getDonorFundingTablesTotals(boolean visibleModuleCommitments, boolean
            visibleModuleDisbursement, boolean visibleModuleExpenditures, boolean visibleModuleArrears, boolean
            visibleModuleRoF, boolean visibleModuleDisbOrders, boolean mtefExisting) {

        String currencyCode = activityForm.getCurrCode() == null ? Constants.DEFAULT_CURRENCY
                : activityForm.getCurrCode();

        ExportSectionHelper fundingTotalsDetails = new ExportSectionHelper(
                null, false).setWidth(WIDTH).setAlign(STJc.LEFT);

        if (visibleModuleCommitments) {
            // TOTAL PLANNED COMMITMENTS
            addTotalsOutput(fundingTotalsDetails, "TOTAL PLANNED COMMITMENTS", activityForm.getFunding()
                    .getTotalPlannedCommitments(), currencyCode);

            // TOTAL ACTUAL COMMITMENTS
            addTotalsOutput(fundingTotalsDetails, "TOTAL ACTUAL COMMITMENTS", activityForm.getFunding()
                    .getTotalCommitments(), currencyCode);
            addTotalsOutput(fundingTotalsDetails, "TOTAL PIPELINE COMMITMENTS", activityForm.getFunding()
                    .getTotalPipelineCommitments(), currencyCode);
        }

        if (mtefExisting) {
            // TOTAL MTEF PROJECTIONS
            addTotalsOutput(fundingTotalsDetails, "TOTAL MTEF PROJECTIONS", activityForm.getFunding()
                    .getTotalMtefProjections(), currencyCode);
        }

        if (visibleModuleDisbursement) {
            // TOTAL PLANNED DISBURSEMENT
            addTotalsOutput(fundingTotalsDetails, "TOTAL PLANNED DISBURSEMENT", activityForm.getFunding()
                    .getTotalPlannedDisbursements(), currencyCode);

            // TOTAL ACTUAL DISBURSEMENT
            addTotalsOutput(fundingTotalsDetails, "TOTAL ACTUAL DISBURSEMENT", activityForm.getFunding()
                    .getTotalDisbursements(), currencyCode);
        }

        if (visibleModuleExpenditures) {
            // TOTAL PLANNED EXPENDITURES
            addTotalsOutput(fundingTotalsDetails, "TOTAL PLANNED EXPENDITURES", activityForm.getFunding()
                    .getTotalPlannedExpenditures(), currencyCode);

            // TOTAL ACTUAL EXPENDITURES
            addTotalsOutput(fundingTotalsDetails, "TOTAL ACTUAL EXPENDITURES", activityForm.getFunding()
                    .getTotalExpenditures(), currencyCode);
        }

        if (visibleModuleArrears) {
            // TOTAL PLANNED ARREARS
            addTotalsOutput(fundingTotalsDetails, "TOTAL PLANNED ARREARS", activityForm.getFunding()
                    .getTotalPlannedArrears(), currencyCode);

            // TOTAL ACTUAL ARREARS
            addTotalsOutput(fundingTotalsDetails, "TOTAL ACTUAL ARREARS", activityForm.getFunding().getTotalArrears(),
                    currencyCode);
        }

        if (visibleModuleRoF) {
            // Total Planned Release of Funds
            addTotalsOutput(fundingTotalsDetails, "Total Planned Release of Funds".toUpperCase(),
                    activityForm.getFunding().getTotalPlannedRoF(), currencyCode);

            // Total Actual Release of Funds
            addTotalsOutput(fundingTotalsDetails, "Total Actual Release of Funds".toUpperCase(),
                    activityForm.getFunding().getTotalActualRoF(), currencyCode);

        }

        if (visibleModuleDisbOrders) {
            // TOTAL ACTUAL DISBURSeMENT ORDERS:
            addTotalsOutput(fundingTotalsDetails, "TOTAL ACTUAL DISBURSEMENT ORDERS", activityForm.getFunding()
                    .getTotalActualDisbursementsOrders(), currencyCode);
        }

        // UNDISBURSED BALANCE
        if (FeaturesUtil.isVisibleFeature("Funding", "Undisbursed Balance")) {
            addTotalsOutput(fundingTotalsDetails, "UNDISBURSED BALANCE", activityForm.getFunding()
                    .getUnDisbursementsBalance(), currencyCode);
        }

        return fundingTotalsDetails;
    }

    private ExportSectionHelper renderMtefSection(AmpFunding fnd, String toCurrCode) {
        FundingCalculationsHelper calc = new FundingCalculationsHelper();
        calc.doCalculations(fnd, toCurrCode);

        ExportSectionHelper mtefProjections = new ExportSectionHelper(null, false).setWidth(WIDTH).setAlign(STJc.LEFT);
        ExportSectionHelperRowData sectionHelperRowData = new ExportSectionHelperRowData("Mtef Projections", null,
        null, true);
        mtefProjections.addRowData(sectionHelperRowData);

        ArrayList<AmpFundingMTEFProjection> mtefList = new ArrayList<AmpFundingMTEFProjection>();
        mtefList.addAll(fnd.getMtefProjections());
        Collections.sort(mtefList, new Comparator<AmpFundingMTEFProjection>() {
            @Override
            public int compare(AmpFundingMTEFProjection o1, AmpFundingMTEFProjection o2) {
                return o1.getProjectionDate().compareTo(o2.getProjectionDate());
            }

        });

        List<ExportSectionHelperRowData> mtefPipeline = new ArrayList<ExportSectionHelperRowData>();
        List<ExportSectionHelperRowData> mtefProjection = new ArrayList<ExportSectionHelperRowData>();

        for (AmpFundingMTEFProjection projection : mtefList) {
            String projectedType = projection.getProjected().getValue();
            FundingDetail fd = getCalculatedMtefFundingDetail(calc, projection);

            String transactionAmount = fd == null ? formatNumber(projection.getAmount()) : fd.getTransactionAmount();
            String transactionCurrencyCode = fd == null ? projection.getAmpCurrency().getCurrencyCode() : fd
            .getCurrencyCode();
            String transactionYear = DateConversion.convertDateToFiscalYearString(projection.getProjectionDate());

            sectionHelperRowData = new ExportSectionHelperRowData(TranslatorWorker.translateText(projectedType),
            null, null, true);
            
            sectionHelperRowData.addRowData(transactionYear)
                    .addRowLtrData(transactionAmount)
                    .addRowData(transactionCurrencyCode);
            
            String roleAndOrgForFundingFlows = getRoleAndOrgForFundingFlows(
                    projection.getRecipientOrg(), projection.getRecipientRole(),
                    "/Activity Form/Funding/Funding Group/Funding Item/MTEF Projections/MTEF Projections "
                    + "Table/Funding Flows OrgRole Selector");
            if (roleAndOrgForFundingFlows != null) {
                sectionHelperRowData.addRowData(roleAndOrgForFundingFlows);
            }

            if ("pipeline".equals(projectedType)) {
                mtefPipeline.add(sectionHelperRowData);
            } else if ("projection".equals(projectedType)) {
                mtefProjection.add(sectionHelperRowData);
            }
        }

        renderMtefSubsection(mtefProjections, "MTEF Projections Pipeline", mtefPipeline, calc.getTotalMtefPipeline()
        .doubleValue(), toCurrCode);
        renderMtefSubsection(mtefProjections, "MTEF Projections Projection", mtefProjection, calc
        .getTotalMtefProjection().doubleValue(), toCurrCode);

        renderMtefTotals(mtefProjections, "Total", "MTEF Projections", calc.getTotalMtef().doubleValue(), toCurrCode);

        return mtefProjections;
    }

    private void renderMtefSubsection(ExportSectionHelper mtefProjections, String mtefProjectType,
    List<ExportSectionHelperRowData> mtefProjectRows,
                                      double subTotal, String toCurrCode) {
        if (mtefProjectRows.size() > 0) {
            for (ExportSectionHelperRowData row : mtefProjectRows) {
                mtefProjections.addRowData(row);
            }

            renderMtefTotals(mtefProjections, "Sub-Total", mtefProjectType, subTotal, toCurrCode);
        }
    }

    private void renderMtefTotals(ExportSectionHelper mtefProjections, String totalType, String totalTitle, double
    totalAmount, String currencyCode) {
        String total = TranslatorWorker.translateText(totalType + " " + totalTitle);
        String totalValue = formatNumber(totalAmount);

        mtefProjections.addRowData(new ExportSectionHelperRowData(null).setSeparator(true));
        mtefProjections.addRowData(new ExportSectionHelperRowData(total)
                .addEmptyData()
                .addRowLtrData(totalValue)
                .addRowData(currencyCode));
        mtefProjections.addRowData(new ExportSectionHelperRowData(null).setSeparator(true));
    }

    private FundingDetail getCalculatedMtefFundingDetail(FundingCalculationsHelper calc, AmpFundingMTEFProjection
    projection) {
        for (FundingDetail fd : calc.getFundDetailList()) {
            if (fd.getFundDetId() == projection.getDbId()) {
                return fd;
            }
        }

        return null;
    }

    private String getRoleAndOrgForFundingFlows(AmpOrganisation recipientOrg, AmpRole recipientRole, String fm) {
        if (recipientOrg != null && recipientRole != null && FeaturesUtil.isVisibleModule(fm)) {
            String recStr = TranslatorWorker.translateText("Recipient:") + " ";
            recStr += recipientOrg.getName() + "\n" + TranslatorWorker.translateText("as the") + " " + recipientRole
            .getName();
            return recStr;
        }

        return null;
    }

    private void getIdentificationTables() throws Exception {

        XWPFTable identificationSubTable1 = buildXwpfTable(2);
        AmpCategoryValue catVal;
        String columnName;
        String columnVal;

        generateIdentificationPart1(request, identificationSubTable1);

        //if activityid is null, the method should have broken much earlier
        Map<String, List<AmpComments>> allComments = this.fetchAllComments(
                new Long(request.getParameter("activityid")));

        generateIdentificationPart2(request, identificationSubTable1, allComments);

        generateIdentificationPart3(request, identificationSubTable1, allComments);

        generateIdenticationPart4(identificationSubTable1);

        //AMP-16421
        if (identification.getBudgetCV().equals(identification.getBudgetCVOn())) {
            if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras/FY")) {
                //
                columnName = TranslatorWorker.translateText("FY");
                generateOverAllTableRows(identificationSubTable1, columnName, identification.getFY(), null);
            }
            if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras/Ministry Code")) {
                columnName = TranslatorWorker.translateText("Ministry Code");
                generateOverAllTableRows(identificationSubTable1, columnName, identification.getMinistryCode(), null);
            }
            if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras/Project Code")) {
                columnName = TranslatorWorker.translateText("Project Code");
                generateOverAllTableRows(identificationSubTable1, columnName, identification.getProjectCode(), null);
            }
            if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras/Vote")) {
                columnName = TranslatorWorker.translateText("Vote");
                generateOverAllTableRows(identificationSubTable1, columnName, identification.getVote(), null);
            }
            if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras/Sub-Vote")) {
                columnName = TranslatorWorker.translateText("Sub-Vote");
                generateOverAllTableRows(identificationSubTable1, columnName, identification.getSubVote(), null);
            }
            if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras/Sub-Program")) {
                columnName = TranslatorWorker.translateText("Sub-Program");
                generateOverAllTableRows(identificationSubTable1, columnName, identification.getSubProgram(), null);
            }
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Classification")) {

            if (identification.getSelectedbudgedsector() != null) {
                for (AmpBudgetSector budgSect : identification.getBudgetsectors()) {
                    if (identification.getSelectedbudgedsector().equals(budgSect.getIdsector())) {

                        String classificationColumnName = TranslatorWorker.translateText("Budget Classification")
                                + ": ";
                        String classificationColumnValue = budgSect.getCode() + " - " + budgSect.getSectorname();
                        generateOverAllTableRows(identificationSubTable1, classificationColumnName,
                        classificationColumnValue, null);
                    }
                }
            }

            if (identification.getSelectedorg() != null) {
                for (AmpOrganisation budgOrg : identification.getBudgetorgs()) {
                    XWPFTableRow newRow = identificationSubTable1.createRow();
                    XWPFTableCell cell = newRow.getCell(0);
                    XWPFParagraph p1 = cell.getParagraphs().get(0);
                    setRun(p1.createRun(), new RunStyle(FONT_FAMILY, FONT_SIZE_NORMAL, null, true), 
                            budgOrg.getBudgetOrgCode() + " - " + budgOrg.getName(), false);
                }
            }

            if (identification.getSelecteddepartment() != null) {
                for (AmpDepartments budgDept : identification.getBudgetdepartments()) {
                    XWPFTableRow newRow = identificationSubTable1.createRow();
                    XWPFTableCell cell = newRow.getCell(0);
                    XWPFParagraph p1 = cell.getParagraphs().get(0);
                    setRun(p1.createRun(), new RunStyle(FONT_FAMILY, FONT_SIZE_NORMAL, null, true), 
                            budgDept.getCode() + " - " + budgDept.getName(), false);
                }
            }

            if (identification.getSelectedprogram() != null) {
                for (AmpTheme budgprog : identification.getBudgetprograms()) {
                    XWPFTableRow newRow = identificationSubTable1.createRow();
                    XWPFTableCell cell = newRow.getCell(0);
                    XWPFParagraph p1 = cell.getParagraphs().get(0);
                    setRun(p1.createRun(), new RunStyle(FONT_FAMILY, FONT_SIZE_NORMAL, null, true), 
                            budgprog.getThemeCode() + " - " + budgprog.getName(), false);
                }
            }

        }

        if (FeaturesUtil.isVisibleField("Organizations and Project ID")) {

            XWPFTableRow row = identificationSubTable1.createRow();
            XWPFTableCell cell = row.getCell(0);
            setRun(cell.getParagraphs().get(0).createRun(), new RunStyle(FONT_FAMILY, FONT_SIZE_NORMAL, null, false),
                    TranslatorWorker.translateText("Organizations and Project ID") + ": ", false);

            if (identification.getSelectedOrganizations() != null) {
                for (OrgProjectId selectedOrgForPopup : identification.getSelectedOrganizations()) {
                    if (selectedOrgForPopup != null && selectedOrgForPopup.getOrganisation() != null) {
                        XWPFTableCell cell2 = row.getCell(0);
                        setRun(cell2.getParagraphs().get(0).createRun(), 
                                new RunStyle(FONT_FAMILY, FONT_SIZE_NORMAL, null, false),
                                "[" + selectedOrgForPopup.getOrganisation().getName() + "]", false);
                    }
                }
            }

        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Government Approval Procedures")) {

            String label = TranslatorWorker.translateText("Government Approval Procedures");

            String value = identification.getGovernmentApprovalProcedures() == null ? ""
                    : TranslatorWorker.translateText(identification.getGovernmentApprovalProcedures() ? "Yes" : "No");

            generateOverAllTableRows(identificationSubTable1, label, value, null);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Joint Criteria")) {

            String label = TranslatorWorker.translateText("Joint Criteria");

            String value = identification.getJointCriteria() == null ? ""
                    : TranslatorWorker.translateText(identification.getJointCriteria() ? "Yes" : "No");

            generateOverAllTableRows(identificationSubTable1, label, value, null);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Humanitarian Aid")) {

            String label = TranslatorWorker.translateText("Humanitarian Aid");

            String value = identification.getHumanitarianAid() == null ? ""
                    : TranslatorWorker.translateText(identification.getHumanitarianAid() ? "Yes" : "No");

            generateOverAllTableRows(identificationSubTable1, label, value, null);

        }
    }

    private void generateIdenticationPart4(XWPFTable identificationSubTable1) throws Exception {
        String columnName;
        String columnVal;
        AmpCategoryValue catVal;
        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Institutions")) {
            columnName = TranslatorWorker.translateText("Institutions");
            columnVal = "";
            catVal = null;
            if (identification.getInstitutions() != null && identification.getInstitutions() != 0) {
                catVal = CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getInstitutions());
            }
            if (catVal != null) {
                columnVal = CategoryManagerUtil.translateAmpCategoryValue(catVal);
            }
            generateOverAllTableRows(identificationSubTable1, columnName, columnVal, null);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Category")) {
            columnName = TranslatorWorker.translateText("Project Category");
            columnVal = "";
            catVal = null;
            if (identification.getProjectCategory() != null && identification.getProjectCategory() != 0) {
                catVal = CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getProjectCategory());
            }
            if (catVal != null) {
                columnVal = CategoryManagerUtil.translateAmpCategoryValue(catVal);
                generateOverAllTableRows(identificationSubTable1, columnName, columnVal, null);
            }
        }
        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Category Other Info")) {
            columnName = TranslatorWorker.translateText("Project Category Other Info");
            generateOverAllTableRows(identificationSubTable1, columnName,
                    identification.getProjectCategoryOtherInfo(), null);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Government Agreement Number")) {
            columnName = TranslatorWorker.translateText("Government Agreement Number");
            columnVal = "";
            catVal = null;
            if (identification.getGovAgreementNumber() != null) {
                columnVal = identification.getGovAgreementNumber();
            }
            generateOverAllTableRows(identificationSubTable1, columnName, columnVal, null);
        }
        //end identification
        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Activity Budget")) {
            columnName = TranslatorWorker.translateText("Budget");

            if (identification.getBudgetCV() != null) {
                if (identification.getBudgetCV().equals(identification.getBudgetCVOn())) {
                    columnVal = TranslatorWorker.translateText("Activity is on budget");
                } else if (identification.getBudgetCV().equals(identification.getBudgetCVOff())) {
                    columnVal = TranslatorWorker.translateText("Activity is off budget");
                } else if (identification.getBudgetCV().equals(new Long(0))) {
                    columnVal = TranslatorWorker.translateText("Budget Unallocated");
                } else {
                    AmpCategoryValue value = (AmpCategoryValue) DbUtil.getObject(AmpCategoryValue.class,
                    identification.getBudgetCV());
                    columnVal = TranslatorWorker.translateText("Activity is on") + " " + value.getLabel();
                }
                generateOverAllTableRows(identificationSubTable1, columnName, columnVal, null);
            }

            XWPFTableRow row = identificationSubTable1.createRow();
            if (identification.getChapterForPreview() != null) {
                setRun(row.getCell(0).addParagraph().createRun(), 
                        new RunStyle(FONT_FAMILY, FONT_SIZE_NORMAL, null, false),
                        TranslatorWorker.translateText("Code Chapitre") + ": ", false);

                setRun(row.getCell(0).addParagraph().createRun(), 
                        new RunStyle(FONT_FAMILY, FONT_SIZE_NORMAL, null, true),
                        identification.getChapterForPreview().getCode() + " - "
                                + identification.getChapterForPreview().getDescription(), false);

                setRun(row.getCell(0).addParagraph().createRun(), 
                        new RunStyle(FONT_FAMILY, FONT_SIZE_NORMAL, null, false),
                        TranslatorWorker.translateText("Imputations") + ": ", false);

                for (AmpImputation imputation : identification.getChapterForPreview().getImputations()) {
                    setRun(row.getCell(0).addParagraph().createRun(), 
                            new RunStyle(FONT_FAMILY, FONT_SIZE_NORMAL, null, true),
                            identification.getChapterForPreview().getYear() + " - "
                                    + imputation.getCode() + " - " + imputation.getDescription(), false);
                }
            }

        }
    }

    private void generateIdentificationPart1(HttpServletRequest request, XWPFTable identificationSubTable1) throws
            Exception {
        String columnName;
        String columnVal;
        AmpCategoryValue catVal;
        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Title")) {
            columnName = TranslatorWorker.translateText("Activity Name");
            generateOverAllTableRows(identificationSubTable1, columnName, identification.getTitle(), null);
        }

        //AMPID cells
        if (FeaturesUtil.isVisibleField("AMP ID")) {
            columnName = TranslatorWorker.translateText("AMP ID");
            generateOverAllTableRows(identificationSubTable1, columnName, identification.getAmpId(), null);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Activity Status")) {
            columnName = TranslatorWorker.translateText("Status");
            columnVal = "";
            catVal = null;
            if (identification.getStatusId() != null && identification.getStatusId() != 0) {
                catVal = CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getStatusId());
            }
            if (catVal != null) {
                columnVal = CategoryManagerUtil.translateAmpCategoryValue(catVal);
                generateOverAllTableRows(identificationSubTable1, columnName, columnVal, null);
            }
        }
        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Status Other Info")) {
            columnName = TranslatorWorker.translateText("Status Other Info");
            generateOverAllTableRows(identificationSubTable1, columnName, identification.getStatusOtherInfo(), null);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Status Reason")) {
            columnName = TranslatorWorker.translateText("Status Reason");
            if (identification.getStatusReason() != null) {
                generateOverAllTableRows(identificationSubTable1, columnName,
                        processEditTagValue(request, identification.getStatusReason()), null);
            }
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Overview Section/Type of Cooperation")) {
            columnName = TranslatorWorker.translateText("Type of Cooperation");
            columnVal = identification.getSsc_typeOfCooperation();

            if (columnVal != null) {
                columnVal = TranslatorWorker.translateText(columnVal);
            }
            generateOverAllTableRows(identificationSubTable1, columnName, columnVal, null);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Overview Section/Type of Implementation")) {
            columnName = TranslatorWorker.translateText("Type of Implementation");
            columnVal = identification.getSsc_typeOfImplementation();

            if (columnVal != null) {
                columnVal = TranslatorWorker.translateText(columnVal);
            }
            generateOverAllTableRows(identificationSubTable1, columnName, columnVal, null);
        }

        String sscPrefix = "";
        if (identification.getTeam() != null && identification.getTeam().isSSCWorkspace()) {
            sscPrefix = "SSC ";
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Overview Section/" + sscPrefix + "Modalities")) {
            columnName = TranslatorWorker.translateText("Modalities");
            //for AMP-17127 they are multiple modalities for activities of SSC
            columnVal = identification.getSscModalitiesAsString("\n");

            generateOverAllTableRows(identificationSubTable1, columnName, columnVal, null);
        }
        if (FeaturesUtil.isVisibleModule(
                "/Activity Form/Funding/Overview Section/" + sscPrefix + "Modalities Other Info")) {
            columnName = TranslatorWorker.translateText("Modalities Other Info");
            generateOverAllTableRows(identificationSubTable1, columnName, identification.getModalitiesOtherInfo(),
                    null);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Objective")) {
            columnName = TranslatorWorker.translateText("Objectives");
            generateOverAllTableRows(identificationSubTable1, columnName, processEditTagValue(request, identification
                    .getObjectives()), null);
        }
    }

    //TODO: unify it with the one from ExportActivityToPDF (move to a common place)
    private Map<String, List<AmpComments>> fetchAllComments(Long actId) {
        if (this.allComments != null) {
            return this.allComments;
        } else {
            //objective comments and comments
            Map<String, List<AmpComments>> allComments = new HashMap<String, List<AmpComments>>();
            List<AmpComments> colAux = null;
            Collection<AmpField> ampFields = DbUtil.getAmpFields();
            if (ampFields != null) {
                for (AmpField field : ampFields) {
                    colAux = DbUtil.getAllCommentsByField(field.getAmpFieldId(), actId);
                    allComments.put(field.getFieldName(), colAux);
                }
            }
            this.allComments = allComments;
            return allComments;
        }
    }

    private void generateIdentificationPart2(HttpServletRequest request, XWPFTable identificationSubTable1,
                                             Map<String, List<AmpComments>> allComments) throws Exception {


        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Objective Comments")) {
            //objective comments
            XWPFTable objTable = buildXwpfTable(2);

            for (Object commentKey : allComments.keySet()) {
                String key = (String) commentKey;
                List<AmpComments> values = (List<AmpComments>) allComments.get(key);
                if (key.equalsIgnoreCase("Objective Assumption") && FeaturesUtil.isVisibleModule("/Activity "
                        + "Form/Identification/Objective Comments/Objective Assumption")) {
                    String colName = TranslatorWorker.translateText("Objective Assumption") + " :";
                    for (AmpComments value : values) {
                        String colValue = TranslatorWorker.translateText(value.getComment());
                        generateOverAllTableRows(objTable, colName, colValue, null);
                    }
                } else if (key.equalsIgnoreCase("Objective Verification") && FeaturesUtil.isVisibleModule("/Activity "
                        + "Form/Identification/Objective Comments/Objective Verification")) {
                    String colName = TranslatorWorker.translateText("Objective Verification") + " :";
                    for (AmpComments value : values) {
                        String colValue = TranslatorWorker.translateText(value.getComment());
                        generateOverAllTableRows(objTable, colName, colValue, null);
                    }
                } else if (key.equalsIgnoreCase("Objective Objectively Verifiable Indicators") && FeaturesUtil
                        .isVisibleModule("/Activity Form/Identification/Objective Comments/Objective Objectively "
                                + "Verifiable "
                                + "Indicators")) {
                    String colName = TranslatorWorker.translateText("Objective Objectively Verifiable Indicators")
                            + " :";
                    for (AmpComments value : values) {
                        String colValue = TranslatorWorker.translateText(value.getComment());
                        generateOverAllTableRows(objTable, colName, colValue, null);
                    }
                }
            }
            generateOverAllTableRows(identificationSubTable1, TranslatorWorker.translateText("Objective Comments"),
                    objTable, null);

        }

        String columnName;
        String columnVal;
        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Description")) {
            columnName = TranslatorWorker.translateText("Description");
            generateOverAllTableRows(identificationSubTable1, columnName, processEditTagValue(request, identification
                    .getDescription()), null);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Comments")) {
            columnName = TranslatorWorker.translateText("Project Comments");
            generateOverAllTableRows(identificationSubTable1, columnName,
                    processEditTagValue(request, identification.getProjectComments()), null);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Lessons Learned")) {
            columnName = TranslatorWorker.translateText("Lessons Learned");
            generateOverAllTableRows(identificationSubTable1, columnName, processEditTagValue(request, identification
            .getLessonsLearned()), null);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Impact")) {
            columnName = TranslatorWorker.translateText("Project Impact");
            generateOverAllTableRows(identificationSubTable1, columnName, processEditTagValue(request, identification
            .getProjectImpact()), null);
        }

        if (identification.getActivitySummary() != null && FeaturesUtil.isVisibleModule("/Activity "
        + "Form/Identification/Activity Summary")) {
            columnName = TranslatorWorker.translateText("Activity Summary");
            generateOverAllTableRows(identificationSubTable1, columnName, processEditTagValue(request, identification
            .getActivitySummary()), null);
        }

        if (identification.getConditionality() != null && FeaturesUtil.isVisibleModule("/Activity "
        + "Form/Identification/Conditionalities")) {
            columnName = TranslatorWorker.translateText("Conditionalities");
            generateOverAllTableRows(identificationSubTable1, columnName, processEditTagValue(request, identification
            .getConditionality()), null);
        }

        if (identification.getProjectManagement() != null && FeaturesUtil.isVisibleModule("/Activity "
        + "Form/Identification/Project Management")) {
            columnName = TranslatorWorker.translateText("Project Management");
            generateOverAllTableRows(identificationSubTable1, columnName, processEditTagValue(request, identification
            .getProjectManagement()), null);
        }

        if (FeaturesUtil.isVisibleModule(
                "/Activity Form/Identification/Purpose")) {
            columnName = TranslatorWorker.translateText("Purpose");
            columnVal = processEditTagValue(request,
                    identification.getPurpose());
            generateOverAllTableRows(identificationSubTable1, columnName,
                    columnVal, null);
        }

        if (FeaturesUtil.isVisibleModule(
                "/Activity Form/Identification/Purpose Comments")) {
            boolean visiblePurposeAssumtion = FeaturesUtil
                    .isVisibleModule(
                            "/Activity Form/Identification/Purpose Comments/Purpose Assumption");
            boolean visiblePurposeVerification = FeaturesUtil
                    .isVisibleModule(
                            "/Activity Form/Identification/Purpose Comments/Purpose Verification");
            boolean visiblePurposeIndicators = FeaturesUtil
                    .isVisibleModule(
                            "/Activity Form/Identification/Purpose Comments/Purpose Objectively Verifiable Indicators");

            XWPFTable objTable = buildXwpfTable(2);

            for (Object commentKey : allComments.keySet()) {
                String key = (String) commentKey;
                List<AmpComments> values = (List<AmpComments>) allComments
                        .get(key);
                if (key.equalsIgnoreCase("Purpose Assumption")
                        && visiblePurposeAssumtion) {
                    String colName = TranslatorWorker.translateText("Purpose Assumption") + " :";
                    for (AmpComments value : values) {
                        String colValue = TranslatorWorker.translateText(value.getComment());
                        generateOverAllTableRows(objTable, colName, colValue, null);
                    }
                } else if (key.equalsIgnoreCase("Purpose Verification")
                        && visiblePurposeVerification) {
                    String colName = TranslatorWorker.translateText("Purpose Verification") + " :";
                    for (AmpComments value : values) {
                        String colValue = TranslatorWorker.translateText(value.getComment());
                        generateOverAllTableRows(objTable, colName, colValue, null);
                    }
                } else if (key
                        .equalsIgnoreCase("Purpose Objectively Verifiable Indicators")
                        && visiblePurposeIndicators) {
                    String colName = TranslatorWorker.translateText("Purpose Objectively Verifiable Indicators") + " :";
                    for (AmpComments value : values) {
                        String colValue = TranslatorWorker.translateText(value.getComment());
                        generateOverAllTableRows(objTable, colName, colValue, null);
                    }
                }
            }
            generateOverAllTableRows(
                    identificationSubTable1,
                    TranslatorWorker.translateText("Purpose Comments"),
                    objTable, null);
        }
    }

    private void generateIdentificationPart3(HttpServletRequest request, XWPFTable identificationSubTable1,
                                             Map<String, List<AmpComments>> allComments) throws Exception {
        String columnName;
        String columnVal;
        AmpCategoryValue catVal;

        //Results
        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Results")) {

            columnName = TranslatorWorker.translateText("Results");
            columnVal = processEditTagValue(request, identification.getResults());
            generateOverAllTableRows(identificationSubTable1, columnName, columnVal, null);
        }
        /**
         *  Results Comments
         */
        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Results Comments")) {

            boolean visibleResultsAssumption = FeaturesUtil.isVisibleModule(
                    "/Activity Form/Identification/Results Comments/Results Assumption");
            boolean visibleResultsVerification = FeaturesUtil.isVisibleModule(
                    "/Activity Form/Identification/Results Comments/Results Verification");
            boolean visibleResultsIndicators = FeaturesUtil.isVisibleModule(
                    "/Activity Form/Identification/Results Comments/Results Objectively Verifiable Indicators");

            XWPFTable objTable = buildXwpfTable(2);
            for (Object commentKey : allComments.keySet()) {
                String key = (String) commentKey;
                List<AmpComments> values = (List<AmpComments>) allComments.get(key);
                if (key.equalsIgnoreCase("Results Assumption") && visibleResultsAssumption) {
                    String colName = TranslatorWorker.translateText("Results Assumption") + " :";
                    for (AmpComments value : values) {
                        String colValue = TranslatorWorker.translateText(value.getComment());
                        generateOverAllTableRows(objTable, colName, colValue, null);
                    }
                } else if (key.equalsIgnoreCase("Results Verification") && visibleResultsVerification) {
                    String colName = TranslatorWorker.translateText("Results Verification") + " :";
                    for (AmpComments value : values) {
                        String colValue = TranslatorWorker.translateText(value.getComment());
                        generateOverAllTableRows(objTable, colName, colValue, null);
                    }
                } else if (key.equalsIgnoreCase("Results Objectively Verifiable Indicators")
                        && visibleResultsIndicators) {
                    String colName = TranslatorWorker.translateText("Results Objectively Verifiable Indicators") + " :";
                    for (AmpComments value : values) {
                        String colValue = TranslatorWorker.translateText(value.getComment());
                        generateOverAllTableRows(objTable, colName, colValue, null);
                    }
                }
            }
            generateOverAllTableRows(identificationSubTable1, TranslatorWorker.translateText("Results Comments"),
                    objTable, null);
        }


        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Accession Instrument")) {
            columnName = TranslatorWorker.translateText("Accession Instrument");
            columnVal = "";
            catVal = null;
            if (identification.getAccessionInstrument() != null && identification.getAccessionInstrument() != 0) {
                catVal = CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getAccessionInstrument());
            }
            if (catVal != null) {
                columnVal = CategoryManagerUtil.translateAmpCategoryValue(catVal);
            }
            generateOverAllTableRows(identificationSubTable1, columnName, columnVal, null);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Implementing Unit")) {
            columnName = TranslatorWorker.translateText("Project Implementing Unit");
            columnVal = "";
            catVal = null;
            if (identification.getProjectImplUnitId() != null && identification.getProjectImplUnitId() != 0) {
                catVal = CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getProjectImplUnitId());
            }
            if (catVal != null) {
                columnVal = CategoryManagerUtil.translateAmpCategoryValue(catVal);
            }
            generateOverAllTableRows(identificationSubTable1, columnName, columnVal, null);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/A.C. Chapter")) {
            columnName = TranslatorWorker.translateText("A.C. Chapter");
            columnVal = "";
            catVal = null;
            if (identification.getAcChapter() != null && identification.getAcChapter() != 0) {
                catVal = CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getAcChapter());
            }
            if (catVal != null) {
                columnVal = CategoryManagerUtil.translateAmpCategoryValue(catVal);
            }
            generateOverAllTableRows(identificationSubTable1, columnName, columnVal, null);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Cris Number")) {
            columnName = TranslatorWorker.translateText("Cris Number");
            columnVal = "";
            catVal = null;
            if (identification.getCrisNumber() != null) {
                columnVal = identification.getCrisNumber();
            }
            generateOverAllTableRows(identificationSubTable1, columnName, columnVal, null);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Procurement System")) {
            columnName = TranslatorWorker.translateText("Procurement System");
            columnVal = "";
            catVal = null;
            if (identification.getProcurementSystem() != null && identification.getProcurementSystem() != 0) {
                catVal = CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getProcurementSystem());
            }
            if (catVal != null) {
                columnVal = CategoryManagerUtil.translateAmpCategoryValue(catVal);
            }
            generateOverAllTableRows(identificationSubTable1, columnName, columnVal, null);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Reporting System")) {
            columnName = TranslatorWorker.translateText("Reporting System");
            columnVal = "";
            catVal = null;
            if (identification.getReportingSystem() != null && identification.getReportingSystem() != 0) {
                catVal = CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getReportingSystem());
            }
            if (catVal != null) {
                columnVal = CategoryManagerUtil.translateAmpCategoryValue(catVal);
            }
            generateOverAllTableRows(identificationSubTable1, columnName, columnVal, null);
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Audit System")) {
            columnName = TranslatorWorker.translateText("Audit System");
            columnVal = "";
            catVal = null;
            if (identification.getAuditSystem() != null && identification.getAuditSystem() != 0) {
                catVal = CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getAuditSystem());
            }
            if (catVal != null) {
                columnVal = CategoryManagerUtil.translateAmpCategoryValue(catVal);
            }
            generateOverAllTableRows(identificationSubTable1, columnName, columnVal, null);
        }
    }

    private void createOverallInformationTable() throws
            Exception {
        String columnVal;
        XWPFTable overAllTable = buildXwpfTable(2);
        //overall table contains 2 subtables: funding informaiton, activity creation information
        //1st cell of the overAll Table
        int rowAmountForCell1 = 0;
        String currency = activityForm.getCurrCode();

        if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Item/Commitments")) {
            if (activityForm.getFunding().getTotalCommitments() != null
                    && activityForm.getFunding().getTotalCommitments().length() > 0) {
                columnVal = activityForm.getFunding().getTotalCommitments() + " ";
            } else {
                columnVal = "0 ";
            }
            columnVal += currency;
            generateOverAllTableRows(overAllTable, TranslatorWorker.translateText("Total Actual Commitment") + ": ",
            columnVal, CELLCOLORGRAY);
            rowAmountForCell1++;

            if (activityForm.getFunding().getTotalPlannedCommitments() != null && activityForm.getFunding()
            .getTotalPlannedCommitments().length() > 0) {
                columnVal = activityForm.getFunding().getTotalPlannedCommitments() + " ";
            } else {
                columnVal = "0 ";
            }
            columnVal += currency;
            generateOverAllTableRows(overAllTable, TranslatorWorker.translateText("Total Planned Commitment") + ": ",
             columnVal, CELLCOLORGRAY);
            rowAmountForCell1++;
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Item/Disbursements")) {
            if (activityForm.getFunding().getTotalDisbursements() != null
                    && activityForm.getFunding().getTotalDisbursements().length() > 0) {
                columnVal = activityForm.getFunding().getTotalDisbursements() + " ";
            } else {
                columnVal = "0 ";
            }
            columnVal += currency;
            generateOverAllTableRows(overAllTable, TranslatorWorker.translateText("Total Actual Distributements")
                    + ": ", columnVal, CELLCOLORGRAY);
            rowAmountForCell1++;

            if (activityForm.getFunding().getTotalPlannedDisbursements() != null && activityForm.getFunding()
            .getTotalPlannedDisbursements().length() > 0) {
                columnVal = activityForm.getFunding().getTotalPlannedDisbursements() + " ";
            } else {
                columnVal = "0 ";
            }
            columnVal += currency;
            generateOverAllTableRows(overAllTable, TranslatorWorker.translateText("Total Planned Distributements")
                    + ": ", columnVal, CELLCOLORGRAY);
            rowAmountForCell1++;
        }

        if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Item/Expenditures")) {
            if (activityForm.getFunding().getTotalExpenditures() != null
                    && activityForm.getFunding().getTotalExpenditures().length() > 0) {
                columnVal = activityForm.getFunding().getTotalExpenditures() + " ";
            } else {
                columnVal = "0 ";
            }
            columnVal += currency;
            generateOverAllTableRows(overAllTable, TranslatorWorker.translateText("Total Actual Expenditures") + ": "
            + "", columnVal, CELLCOLORGRAY);
            rowAmountForCell1++;

            if (activityForm.getFunding().getTotalPlannedExpenditures() != null && activityForm.getFunding()
            .getTotalPlannedExpenditures().length() > 0) {
                columnVal = activityForm.getFunding().getTotalPlannedExpenditures() + " ";
            } else {
                columnVal = "0 ";
            }
            columnVal += currency;
            generateOverAllTableRows(overAllTable, TranslatorWorker.translateText("Total Planned Expenditures") + ": "
            + "", columnVal, CELLCOLORGRAY);
            rowAmountForCell1++;
        }

        if (FeaturesUtil.isVisibleField("Duration of Project")) {
            if (activityForm.getPlanning().getProjectPeriod() != null) {
                columnVal = activityForm.getPlanning().getProjectPeriod().toString() + " "
                        + TranslatorWorker.translateText("Months");
            } else {
                columnVal = "";
            }
            generateOverAllTableRows(overAllTable, TranslatorWorker.translateText("Duration of project") + ": ",
            columnVal, CELLCOLORGRAY);
            rowAmountForCell1++;
        }

        //second cell of overall table is additional info
        createOverallInformationTableSecondCell(rowAmountForCell1);

    }

    private void createOverallInformationTableSecondCell(int rowAmountForCell1) throws Exception {
        String columnVal;
        int rowAmountForCell2 = ADDITIONAL_INFORMATION_ROWS; //there are 5 rows in additional information
        XWPFTable additionalInfoSubTable = buildXwpfTable(2);
        setTableAlignment(additionalInfoSubTable, STJc.CENTER);

        columnVal = "";
        if (identification.getActAthFirstName() != null) {
            columnVal += identification.getActAthFirstName();
        }
        if (identification.getActAthLastName() != null) {
            columnVal += " " + identification.getActAthLastName();
        }
        generateOverAllTableRows(additionalInfoSubTable, TranslatorWorker.translateText("Activity created by") + ": "
        + "", columnVal, CELLCOLORGRAY);

        columnVal = "";
        if (identification.getCreatedBy() != null) {
            if (identification.getCreatedBy().getAmpTeam().getName() != null) {
                columnVal += identification.getCreatedBy().getAmpTeam().getName();
            }
            if (identification.getCreatedBy().getAmpTeam().getAccessType() != null) {
                columnVal += "-" + TranslatorWorker.translateText(identification.getCreatedBy().getAmpTeam()
                .getAccessType());
            }
        }
        generateOverAllTableRows(additionalInfoSubTable, TranslatorWorker.translateText("Workspace of creator") + ": "
        + "", columnVal, CELLCOLORGRAY);

        columnVal = "";
        if (identification.getCreatedBy() != null) {
            if (identification.getCreatedBy().getAmpTeam().getComputation()) {
                columnVal += TranslatorWorker.translateText("yes");
            } else {
                columnVal += TranslatorWorker.translateText("no");
            }
        }
        generateOverAllTableRows(additionalInfoSubTable, TranslatorWorker.translateText("Computation") + ": ",
        columnVal, CELLCOLORGRAY);

        columnVal = "";
        if (identification.getCreatedDate() != null) {
            columnVal += identification.getCreatedDate();
        }
        generateOverAllTableRows(additionalInfoSubTable, TranslatorWorker.translateText("Activity created on") + ": "
        + "", columnVal, CELLCOLORGRAY, true);

        columnVal = "";
        if (identification.getTeam() != null
                && identification.getTeam().getTeamLead() != null
                && identification.getTeam().getTeamLead().getUser() != null) {
            columnVal += identification.getTeam().getTeamLead().getUser().getFirstNames() + " ";
            columnVal += identification.getTeam().getTeamLead().getUser().getLastName() + " ";
            columnVal += identification.getTeam().getTeamLead().getUser().getEmail();
        }
        generateOverAllTableRows(additionalInfoSubTable, TranslatorWorker.translateText("Data Team Leader") + ": ",
        columnVal, CELLCOLORGRAY);

        int emptyRowsAmount = rowAmountForCell1 >= rowAmountForCell2 ? (rowAmountForCell1 - rowAmountForCell2)
                : (rowAmountForCell2 - rowAmountForCell1);
        for (int i = 0; i < emptyRowsAmount; i++) {
            generateOverAllTableRows(additionalInfoSubTable, "", "", CELLCOLORGRAY);
        }
    }
    

    private void generateOverAllTableRows(XWPFTable table, String fieldName, String fieldValue, String bgColor) 
            throws Exception {
        
        generateOverAllTableRows(table, fieldName, fieldValue, bgColor, false);
    }

    private void generateOverAllTableRows(XWPFTable table, String fieldName, String fieldValue, String bgColor, 
            boolean isLtr) throws Exception {
        
        XWPFTableRow newRow = table.createRow();
        XWPFTableCell cell = newRow.getCell(0);
        XWPFParagraph p1 = cell.getParagraphs().get(0);
        setOrientation(p1);
        setRun(p1.createRun(), new RunStyle(FONT_FAMILY, FONT_SIZE_NORMAL, null, false), fieldName, false);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.TOP);
        if (bgColor != null) {
            cell.setColor(bgColor);
        }

        cell = newRow.getCell(1);
        p1 = cell.getParagraphs().get(0);
        setOrientation(p1);
        setRun(p1.createRun(), new RunStyle(FONT_FAMILY, FONT_SIZE_NORMAL, null, true), fieldValue, false, isLtr);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.TOP);
        if (bgColor != null) {
            cell.setColor(bgColor);
        }
    }

    private void generateOverAllTableRows(XWPFTable table, String fieldName, XWPFTable fieldValue, String bgColor)
            throws Exception {
        XWPFTableRow newRow = table.createRow();
        XWPFTableCell cell = newRow.getCell(0);
        XWPFParagraph p1 = cell.getParagraphs().get(0);
        setRun(p1.createRun(), new RunStyle(FONT_FAMILY, FONT_SIZE_NORMAL, bgColor, false), fieldName, false);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.TOP);

        cell = newRow.getCell(1);
        cell.insertTable(0, fieldValue);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.TOP);
    }

    private String getEditTagValue(HttpServletRequest request, String editKey) throws Exception {
        Site site = RequestUtils.getSite(request);
        String editorBody = org.digijava.module.editor.util.DbUtil.getEditorBody(site, editKey, RequestUtils
        .getNavigationLanguage(request).getCode());
        if (editorBody == null) {
            editorBody = org.digijava.module.editor.util.DbUtil.getEditorBody(site, editKey, SiteUtils
            .getDefaultLanguages(site).getCode());
        }
        return editorBody;
    }

    //cuts <p> and </p> tags from editTag value
    private String processEditTagValue(HttpServletRequest request, String editTagKey) throws Exception {
        String result = getEditTagValue(request, editTagKey);
        return processHtml(request, result);
    }

    private String processHtml(HttpServletRequest request, String text) throws Exception {

        if (text != null && text.indexOf("<![endif]-->") != -1) {
            text = text.substring(text.lastIndexOf("<![endif]-->") + "<![endif]-->".length());
        }

        if (text != null) {
            String formatterPrefix = "<![endif]-->";  //some records contain wordpress tags in comments,which need to
            // be filtered
            if (text.indexOf(formatterPrefix) != -1) {
                text = text.substring(text.lastIndexOf(formatterPrefix) + formatterPrefix.length());
            }
            while (text.contains("<!--")) { //remove possible comments
                String text1 = text.substring(0, text.indexOf("<!--"));
                String text2 = text.substring(text.lastIndexOf("-->") + ("-->").length() + 1);
                text = text1 + text2;
            }

            String[] possibleTags = {"span", "table", "tr", "td", "tbody", "p", "style", "ul", "li", "div"};

            for (int i = 0; i < possibleTags.length; i++) { //remove all possible tags
                String tag = possibleTags[i];

                String startTagStr = "<" + tag;
                String endTagStr = "</" + tag + ">";

                int endTagLength = endTagStr.length();


                while (text.contains(startTagStr)) {

                    int firstIndexOfStartTag = text.indexOf(startTagStr);
                    int beginIndex = text.indexOf(">", firstIndexOfStartTag) + 1;
                    int firstIndexOfEndTag = text.indexOf(endTagStr, beginIndex);

                    String text1 = text.substring(0, firstIndexOfStartTag);
                    String text2 = text.substring(beginIndex, firstIndexOfEndTag);
                    String text3 = text.length() == firstIndexOfEndTag
                            + endTagLength ? "" : text.substring(firstIndexOfEndTag + endTagLength);
                    text = text1 + text2 + text3;
                }
            }

            text = text.replaceAll("\\<.*?>", "");
            text = text.replaceAll("&lt;", "<");
            text = text.replaceAll("&gt;", ">");
            text = text.replaceAll("&amp;", "&");
            text = text.replaceAll("&rsquo;", "'");
            text = text.replaceAll("\\r", "");
        }

        return ExportActivityToPdfUtil.unhtmlentities(text);
    }

    private String buildProgramsOutput(List<AmpActivityProgram> programs) {
        String result = "";
        for (AmpActivityProgram pr : programs) {
            result += pr.getHierarchyNames() + " \t\t" + pr.getProgramPercentage() + "% \n";
        }
        return result;
    }

    private class ExportSectionHelper {
        private String secTitle;
        private boolean translateTitle;
        private float width = WIDTH;
        private STJc.Enum align = STJc.LEFT;
        private List<ExportSectionHelperRowData> rowData;

        ExportSectionHelper(String secTitle, boolean translateTitle) {
            this.secTitle = secTitle;
            this.translateTitle = translateTitle;
            rowData = new ArrayList<ExportSectionHelperRowData>();
        }

        ExportSectionHelper(String secTitle) {
            this(secTitle, false);
        }

        public void setRowData(List<ExportSectionHelperRowData> rowData) {
            this.rowData = rowData;
        }

        public String getSecTitle() {
            return secTitle;
        }

        public boolean isTranslateTitle() {
            return translateTitle;
        }

        public List<ExportSectionHelperRowData> getRowData() {
            return rowData;
        }

        public void addRowData(ExportSectionHelperRowData data) {
            this.rowData.add(data);
        }

        public float getWidth() {
            return width;
        }

        public ExportSectionHelper setWidth(float width) {
            this.width = width;
            return this;
        }

        public STJc.Enum getAlign() {
            return align;
        }

        public ExportSectionHelper setAlign(STJc.Enum align) {
            this.align = align;
            return this;
        }
    }

    private class ExportSectionHelperRowData {
        private String title;
        private boolean translateTitle;
        private List<String> fieldOrderedList;
        private Map<String, String> fldVisibilityKeyMap;
        private List<SectionItem> items;
        private boolean separator;

        ExportSectionHelperRowData(String title, List<String> fields, List<String> visibilityKeys, boolean
        translateTitle, boolean separator) {
            this.title = title;
            this.translateTitle = translateTitle;
            this.separator = separator;
            items = new ArrayList<SectionItem>();
            fldVisibilityKeyMap = new HashMap<String, String>();
            fieldOrderedList = fields;
            if (fields != null && !fields.isEmpty() && visibilityKeys != null && !visibilityKeys.isEmpty()) {
                int count = 0;
                for (String fld : fields) {
                    fldVisibilityKeyMap.put(fld, visibilityKeys.get(count));
                    count++;
                }
            }
        }

        ExportSectionHelperRowData(String title, List<String> fields, List<String> visibilityKeys, boolean
        translateTitle) {
            this(title, fields, visibilityKeys, translateTitle, false);
        }

        ExportSectionHelperRowData(String title) {
            this(title, null, null, false);
        }

        public ExportSectionHelperRowData addRowSimpleData(String value, boolean translated) {
            SectionItem item = new SectionItem();
            item.setValue(value);
            item.setTranslated(translated);
            items.add(item);
            
            return this;
        }
        
        public ExportSectionHelperRowData addRowLtrData(String value) {
            SectionItem item = new SectionItem();
            item.setValue(value);
            item.setLtr(true);
            items.add(item);
            
            return this;
        }
        
        public ExportSectionHelperRowData addEmptyData() {
            return addRowData("");
        }

        public ExportSectionHelperRowData addRowData(String value) {
            return addRowSimpleData(value, false);
        }

        public String getTitle() {
            return title;
        }

        public boolean isTranslateTitle() {
            return translateTitle;
        }

        public List<SectionItem> getItems() {
            return items;
        }

        public boolean isSeparator() {
            return separator;
        }

        public ExportSectionHelperRowData setSeparator(boolean separator) {
            this.separator = separator;
            return this;
        }

        public Map<String, String> getFldVisibilityKeyMap() {
            return fldVisibilityKeyMap;
        }

        public List<String> getFieldOrderedList() {
            return fieldOrderedList;
        }

    }
    
    private class SectionItem {

        private String value;
        // ltr means left-to-right, is used in RTL languages. E.g.: numbers and dates should be displayed as ltr
        private boolean ltr = false;
        private boolean translated = false;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
        
        public String getTranslatedValue() {
            if (translated) {
                return TranslatorWorker.translateText(value);
            }
            
            return value;
        }

        public boolean isLtr() {
            return ltr;
        }

        public void setLtr(boolean ltr) {
            this.ltr = ltr;
        }

        public boolean isTranslated() {
            return translated;
        }

        public void setTranslated(boolean translated) {
            this.translated = translated;
        }
    }

    private void createSectionTable(ExportSectionHelper esh) throws WorkerException {
        XWPFTable retVal;
        int maxCols = 0;

        for (ExportSectionHelperRowData rd : esh.getRowData()) {
            //Calculate visible row count
            int visibleRowCount = 0;
            if (rd.getItems() != null && !rd.getItems().isEmpty()) {
                if (rd.getFieldOrderedList() != null) {
                    for (String fldTitle : rd.getFieldOrderedList()) {
                        String visKey = rd.getFldVisibilityKeyMap().get(fldTitle);
                        if (FeaturesUtil.isVisibleField(visKey)) {
                            visibleRowCount++;
                        }
                    }
                } else {  //If no visibility keys
                    visibleRowCount = rd.getItems().size();
                }
            }

            if (visibleRowCount > maxCols) {
                maxCols = rd.getItems().size();
            }
        }

        maxCols++; //first col will be the row title by default

        retVal = buildXwpfTable(maxCols);
        retVal.setWidth((int) esh.getWidth());

        if (esh.getSecTitle() != null && esh.getSecTitle().trim().length() > 0) {
            String secTitle = esh.translateTitle
                    ? TranslatorWorker.translateText(esh.getSecTitle()) : esh.getSecTitle();

            XWPFTableRow titleRow = retVal.createRow();
            XWPFTableCell titleCell = titleRow.getCell(0);
            XWPFParagraph titleParagraph = titleCell.getParagraphs().get(0);
            setOrientation(titleParagraph);
            setRun(titleParagraph.createRun(), new RunStyle(FONT_FAMILY, FONT_SIZE_SECTION_TITLE, null, true), 
                    secTitle, false);
            titleCell.setColor(CELLCOLORGRAY);
            spanCellsAcrossRow(titleRow, 0, maxCols);
        }

        for (ExportSectionHelperRowData rd : esh.getRowData()) {
            XWPFTableRow dataRow = retVal.createRow();
            if (!rd.isSeparator()) {
                String title = (rd.translateTitle && rd.title != null)
                        ? TranslatorWorker.translateText(rd.title) : rd.title;

                XWPFTableCell cell = dataRow.getCell(0);
                cell.setColor(CELLCOLORGRAY);
                XWPFParagraph dataParagraph = cell.getParagraphs().get(0);
                setOrientation(dataParagraph);
                setRun(dataParagraph.createRun(), new RunStyle(FONT_FAMILY, FONT_SIZE_NORMAL, null, true), 
                        title, false);

                if ((rd.getItems() == null || rd.getItems().isEmpty()) && maxCols > 1) {
                    spanCellsAcrossRow(dataRow, 0, maxCols);
                }
            } else {
                spanCellsAcrossRow(dataRow, 0, maxCols);
                dataRow
                        .getCell(0)
                        .setText(StringUtils.repeat("_", TABLE_SEPARATOR_LENGTH));
            }

            int rowCounter = 1;
            int idx = 0;
            int cellIndex = 1;
            for (SectionItem rowData : rd.getItems()) {
                String visKey = null;
                if (rd.getFieldOrderedList() != null) {
                    String colTitle = rd.getFieldOrderedList().get(idx);
                    visKey = rd.getFldVisibilityKeyMap().get(colTitle);
                }

                if (visKey == null || (visKey != null && FeaturesUtil.isVisibleField(visKey))) {
                    String trnVal = rowData.getTranslatedValue();
                    XWPFTableCell cell = dataRow.getCell(cellIndex);
                    XWPFParagraph dataParagraph = cell.getParagraphs().get(0);
                    setOrientation(dataParagraph, rowData.isLtr());
                    setRun(dataParagraph.createRun(), new RunStyle(FONT_FAMILY, FONT_SIZE_NORMAL, null, false),
                            trnVal != null ? trnVal : "-", false, rowData.isLtr());
                    if (rd.getItems().size() < (maxCols - 1) && rowCounter == rd.getItems().size()) {
                        spanCellsAcrossRow(dataRow, 0, maxCols - rowCounter);
                    }
                    rowCounter++;
                }
                idx++;
                cellIndex++;
            }
        }
    }
    
    private static void setRun(XWPFRun run, RunStyle runStyle, String text, boolean addBreak, boolean isLtr) {
        
        setRunOrientation(run, isLtr);
       
        run.setFontFamily(runStyle.getFontFamily());
        run.setFontSize(runStyle.getFontSize());
        run.setBold(runStyle.isBold());
        if (runStyle.getColorRGB() != null) {
            run.setColor(runStyle.getColorRGB());
        }
        
        run.setText(text);
        if (addBreak) {
            run.addBreak();
        }
    }

    private static void setRun(XWPFRun run, RunStyle runStyle, String text, boolean addBreak) {
        setRun(run, runStyle, text, addBreak, false);
    }

    public void setTableAlignment(XWPFTable table, STJc.Enum justification) {
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        CTJc jc = (tblPr.isSetJc() ? tblPr.getJc() : tblPr.addNewJc());
        jc.setVal(justification);
    }

    private void spanCellsAcrossRow(XWPFTableRow row, int colNum, int span) {
        XWPFTableCell cell = row.getCell(colNum);
        if (cell.getCTTc().getTcPr() == null) {
            cell.getCTTc().addNewTcPr();
        }

        CTHMerge hMerge = CTHMerge.Factory.newInstance();
        hMerge.setVal(STMerge.RESTART);
        cell.getCTTc().getTcPr().setHMerge(hMerge);

        for (int i = colNum + 1; i < span; i++) {
            XWPFTableCell cell2 = row.getCell(i);
            if (cell2.getCTTc().getTcPr() == null) {
                cell2.getCTTc().addNewTcPr();
            }
            CTHMerge hMerge1 = CTHMerge.Factory.newInstance();
            hMerge.setVal(STMerge.CONTINUE);
            cell2.getCTTc().getTcPr().setHMerge(hMerge1);
        }
    }

    private Map<String, Map<String, Set<AmpFundingDetail>>> getStructuredFundings(Set<AmpFundingDetail> fndDets) {
        Map<String, Map<String, Set<AmpFundingDetail>>> retVal = new HashMap<>();
        for (AmpFundingDetail fndDet : fndDets) {
            String transactionType = FundingCalculationsHelper.getTransactionTypeLabel(fndDet.getTransactionType());
            if (!retVal.containsKey(transactionType)) {
                retVal.put(transactionType, new HashMap<>());
            }

            if (!retVal.get(transactionType).containsKey(fndDet.getAdjustmentType().getLabel())) {
                retVal.get(transactionType).put(fndDet.getAdjustmentType().getLabel(), new TreeSet<>(new
                AmpFundingDetail.FundingDetailComparator()));
            }
            retVal.get(transactionType).get(fndDet.getAdjustmentType().getLabel()).add(fndDet);
        }
        return retVal;
    }

    private void addTotalsOutput(ExportSectionHelper fundingTotalsDetails, String title, String value, 
            String currencyCode) {
        
        if (value == null || value.isEmpty()) {
            return;
        }
        
        String totalAmountType = TranslatorWorker.translateText(title) + ":";
        fundingTotalsDetails.addRowData(new ExportSectionHelperRowData(totalAmountType)
                .addRowLtrData(value)
                .addRowData(currencyCode));
    }
    
    private class RunStyle {

        private String fontFamily;
        private int fontSize;
        private String colorRGB;
        private boolean bold;
        
        RunStyle(String fontFamily, int fontSize, String colorRGB, boolean bold) {
            super();
            this.fontFamily = fontFamily;
            this.fontSize = fontSize;
            this.colorRGB = colorRGB;
            this.bold = bold;
        }

        public String getFontFamily() {
            return fontFamily;
        }

        public void setFontFamily(String fontFamily) {
            this.fontFamily = fontFamily;
        }

        public int getFontSize() {
            return fontSize;
        }

        public void setFontSize(int fontSize) {
            this.fontSize = fontSize;
        }

        public String getColorRGB() {
            return colorRGB;
        }

        public void setColorRGB(String colorRGB) {
            this.colorRGB = colorRGB;
        }

        public boolean isBold() {
            return bold;
        }

        public void setBold(boolean bold) {
            this.bold = bold;
        }
    }
}
