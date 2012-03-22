package org.digijava.module.orgProfile.action;

import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.orgProfile.helper.FilterHelper;
import org.digijava.module.orgProfile.helper.ParisIndicatorHelper;
import org.digijava.module.orgProfile.helper.Project;
import org.digijava.module.orgProfile.util.OrgProfileUtil;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpWidget;
import org.digijava.module.widget.dbentity.AmpWidgetOrgProfile;
import org.digijava.module.widget.helper.ChartOption;
import org.digijava.module.widget.helper.WidgetVisitor;
import org.digijava.module.widget.helper.WidgetVisitorAdapter;
import org.digijava.module.widget.util.ChartWidgetUtil;
import org.digijava.module.widget.util.WidgetUtil;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;

import com.lowagie.text.Element;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.table.RtfCell;
import javax.servlet.ServletContext;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpOrganisationContact;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.orgProfile.helper.ExportSettingHelper;

public class ExportToWord extends Action {

    private static Logger logger = Logger.getLogger(ExportToWord.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ServletContext ampContext = getServlet().getServletContext();
        response.setContentType("application/msword");
        response.setHeader("content-disposition", "inline;filename=orgProfile.doc");
        String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
        String langCode = RequestUtils.getNavigationLanguage(request).getCode();
        com.lowagie.text.Document doc = new com.lowagie.text.Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            RtfWriter2.getInstance(doc, baos);
            List<AmpDaWidgetPlace> orgPlaces = WidgetUtil.getAllOrgProfilePlaces();
            Iterator<AmpDaWidgetPlace> placeIter = orgPlaces.iterator();
            doc.open();
            com.lowagie.text.Font pageTitleFont = com.lowagie.text.FontFactory.getFont("Arial", 24, com.lowagie.text.Font.BOLD);
            Paragraph pageTitle = new Paragraph(TranslatorWorker.translateText("Org. Profile", langCode, siteId), pageTitleFont);
            pageTitle.setAlignment(Element.ALIGN_CENTER);
            doc.add(pageTitle);
            HttpSession session = request.getSession();
            FilterHelper filter = (FilterHelper) session.getAttribute("orgProfileFilter");
            List<ExportSettingHelper> helpers = (List<ExportSettingHelper>) request.getAttribute("orgProfileExportSettings");
            Boolean monochrome = (Boolean) request.getAttribute("orgProfileMonochrome");
            AmpOrganisation organization = filter.getOrganization();
            String multipleSelected = TranslatorWorker.translateText("Multiple Organizations Selected", langCode, siteId);
            String all = TranslatorWorker.translateText("All", langCode, siteId);
            String notApplicable = TranslatorWorker.translateText("Not Applicable", langCode, siteId);
            String notAvailable = TranslatorWorker.translateText("Not Available", langCode, siteId);


            String orgName = notAvailable;
            String orgGroupTpName = notAvailable;
            String grpName = notAvailable;
            String orgAcronym = notAvailable;
            String orgUrl = notAvailable;
            String orgBackground = notAvailable;
            String orgDesc = notAvailable;
            String contactName =notAvailable;
            String email = notAvailable;
            String contactPhone = notAvailable;
            String contactFax = notAvailable;

            AmpOrgGroup group = null;
            AmpOrgType orgGroupType = null;
            if (organization != null) {
                group = organization.getOrgGrpId();
                orgGroupType = group.getOrgType();
                orgGroupTpName = orgGroupType.getOrgType();
                grpName = group.getOrgGrpName();
                orgName = organization.getName();
                if (organization.getOrgUrl() != null&&!organization.getOrgUrl().equals("")) {
                    orgUrl = organization.getOrgUrl();
                }
                if (organization.getContactPersonName() != null&&!organization.getContactPersonName().trim().equals("")) {
                    contactName = organization.getContactPersonName();
                }
                if (organization.getEmail() != null&&!organization.getEmail().trim().equals("")) {
                    email = organization.getEmail();
                }
                if (organization.getPhone() != null&&!organization.getPhone().trim().equals("")) {
                    contactPhone = organization.getPhone();
                }

                if (organization.getFax() != null&&!organization.getFax().trim().equals("")) {
                    contactFax = organization.getFax();
                }
                if (organization.getOrgBackground() != null&&!organization.getOrgBackground().trim().equals("")) {
                    orgBackground = organization.getOrgBackground();
                }
                if (organization.getOrgDescription() != null&&!organization.getOrgDescription().trim().equals("")) {
                    orgDesc = organization.getOrgDescription();
                }

            } else {
                if (filter.getOrgIds() != null) {
                    orgGroupTpName = multipleSelected;
                    grpName = multipleSelected;
                    orgName = multipleSelected;
                    orgAcronym = multipleSelected;
                    orgUrl = multipleSelected;
                    contactName = multipleSelected;
                    email = multipleSelected;
                    contactPhone = multipleSelected;
                    contactFax = multipleSelected;
                    orgBackground = multipleSelected;
                    orgDesc = multipleSelected;
                } else {
                    group = filter.getOrgGroup();
                    if (group != null) {
                        orgGroupTpName = group.getOrgType().getOrgType();
                        grpName = group.getOrgGrpName();
                    } else {
                        orgGroupTpName = all;
                        grpName = all;
                    }
                      orgName = orgAcronym = orgUrl = orgBackground = orgDesc =
                      contactName = email = contactPhone = contactFax = notApplicable;
                   
                }
            }
            while (placeIter.hasNext()) {
                AmpDaWidgetPlace place = placeIter.next();
               if(!FeaturesUtil.isVisibleFeature(place.getName(), ampContext)){
                   continue;
               }

                AmpWidget wd = place.getAssignedWidget();
                if (wd != null) {
                    final ArrayList rendertype = new ArrayList();
                    WidgetVisitor adapter = new WidgetVisitorAdapter() {

                        @Override
                        public void visit(AmpWidgetOrgProfile orgProfile) {
                            rendertype.add(orgProfile.getType());

                        }
                    };
                    wd.accept(adapter);
                    if (rendertype.size() > 0) {
                        Long type = (Long) rendertype.get(0);
                        ChartOption opt = new ChartOption();
                        opt.setWidth(500);
                        opt.setHeight(350);
                        opt.setSiteId(siteId);
                        opt.setLangCode(langCode);
                        opt.setMonochrome(monochrome);
                        JFreeChart chart = null;
                        JFreeChart chartDisb = null;
                        JFreeChart chartSecondaryScheme = null;
                        JFreeChart chartDisbSecondaryScheme = null;
                        JFreeChart chartTertiaryScheme = null;
                        JFreeChart chartDisbTertiaryScheme = null;
                        Table orgSummaryTbl = null;
                        Table orgContactsTbl=null;
                        Table largetsProjectsTbl = null;
                        Table parisDecTbl = null;
                        Table typeOfAidTbl = null;
                        Table odaProfileTbl = null;
                        Table pledgesCommDisbTbl = null;
                        Table sectorTbl = null;
                        Table secondarySectorTbl = null;
                        Table tertiarySectorTbl = null;
                        Table regionTbl = null;
                        Table aidPredTable = null;
                        ChartRenderingInfo info = new ChartRenderingInfo();
                        String transTypeName = "";
                        String currName = filter.getCurrName();
                        String amountInThousands = "";
                        String sectorBreakdown= SectorUtil.getPrimaryConfigClassification().getClassification().getSecSchemeName();
                        String typeOfAid = TranslatorWorker.translateText("TYPE OF AID", langCode, siteId);
                        String odaProfile = TranslatorWorker.translateText("ODA Profile", langCode, siteId);
                        String charttitle="";
                        if(filter.isPledgeVisible()){
                             charttitle="Pledges|";
                        }
                        charttitle+="Commitments|Disbursements";
                        if(filter.isExpendituresVisible()){
                             charttitle+="|Expenditures";
                        }
                        String pledgesCommDisbExp = TranslatorWorker.translateText(charttitle, langCode, siteId);
                        String regionBreakdown = TranslatorWorker.translateText("Regional", langCode, siteId);
                        String aidPred = TranslatorWorker.translateText("Aid Predictability", langCode, siteId);

                        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
                            amountInThousands = "," + TranslatorWorker.translateText("Amounts in thousands", langCode, siteId) + " ";
                        }
                        switch (filter.getTransactionType()) {
                            case org.digijava.module.aim.helper.Constants.COMMITMENT:
                                transTypeName = TranslatorWorker.translateText("Commitment", langCode, siteId);
                                break;
                            case org.digijava.module.aim.helper.Constants.DISBURSEMENT:
                                transTypeName = TranslatorWorker.translateText("Disbursement", langCode, siteId);
                                break;
                        }

                        int typeOfExport = Constants.EXPORT_OPTION_CHART_DATA_SOURCE;
                        if (helpers != null) {
                            for (ExportSettingHelper helper : helpers) {
                                if (type == helper.getWidget().getType()) {
                                    typeOfExport = helper.getSelectedTypeOfExport();
                                    helpers.remove(helper);
                                    break;
                                }
                            }
                        }
                        String chartTitle = null;
                        String secondarySectorchartTitle = null;
                        String tertiarySectorchartTitle = null;
                        int colspan = filter.getYearsInRange() + 1;
                        int oneYearColspan = 2;
                        if (filter.getTransactionType() == 2) {
                            colspan = 2 * filter.getYearsInRange() + 1;
                            oneYearColspan = 3;
                        }

                        switch (type.intValue()) {
                            case WidgetUtil.ORG_PROFILE_TYPE_OF_AID:
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_DATA_SOURCE_ONLY == typeOfExport) {
                                    typeOfAidTbl = new Table(colspan);
                                    typeOfAidTbl.setWidth(100);
                                    RtfCell typeofAidTitleCell = new RtfCell(new Paragraph(typeOfAid + "(" + transTypeName + "|" + currName + amountInThousands + ")", OrgProfileUtil.HEADERFONTWHITE));
                                    typeofAidTitleCell.setColspan(colspan);
                                    typeOfAidTbl.addCell(typeofAidTitleCell);
                                    typeofAidTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    RtfCell tpAidTitleCell = new RtfCell(new Paragraph(typeOfAid, OrgProfileUtil.HEADERFONTWHITE));
                                    tpAidTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    typeOfAidTbl.addCell(tpAidTitleCell);
                                    OrgProfileUtil.getDataTable(typeOfAidTbl, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_TYPE_OF_AID, null);
                                }
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_CHART_ONLY == typeOfExport) {
                                    chartTitle = typeOfAid;

                                    if (filter.getTransactionType() == 2) {
                                        FilterHelper newFilter = new FilterHelper(filter);
                                        newFilter.setTransactionType(Constants.COMMITMENT);
                                        chart = ChartWidgetUtil.getBarChart(opt, newFilter, WidgetUtil.ORG_PROFILE_TYPE_OF_AID);
                                        newFilter.setTransactionType(Constants.DISBURSEMENT);
                                        chartDisb = ChartWidgetUtil.getBarChart(opt, newFilter, WidgetUtil.ORG_PROFILE_TYPE_OF_AID);
                                    } else {
                                        chart = ChartWidgetUtil.getBarChart(opt, filter, WidgetUtil.ORG_PROFILE_TYPE_OF_AID);
                                    }
                                }
                                break;
                            case WidgetUtil.ORG_PROFILE_PLEDGES_COMM_DISB:
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_DATA_SOURCE_ONLY == typeOfExport) {
                                    colspan = filter.getYearsInRange() + 1;
                                    pledgesCommDisbTbl = new Table(colspan);
                                    pledgesCommDisbTbl.setWidth(100);
                                    RtfCell pledgesCommDisbTitleCell = new RtfCell(new Paragraph(pledgesCommDisbExp + "(" + currName + amountInThousands + ")", OrgProfileUtil.HEADERFONTWHITE));
                                    pledgesCommDisbTitleCell.setColspan(colspan);
                                    pledgesCommDisbTbl.addCell(pledgesCommDisbTitleCell);
                                    pledgesCommDisbTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    RtfCell pldsCommDisbdTitleCell = new RtfCell(new Paragraph(" ", OrgProfileUtil.HEADERFONTWHITE));
                                    pldsCommDisbdTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    pledgesCommDisbTbl.addCell(pldsCommDisbdTitleCell);
                                    OrgProfileUtil.getDataTable(pledgesCommDisbTbl, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_PLEDGES_COMM_DISB, null);
                                }
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_CHART_ONLY == typeOfExport) {
                                    chartTitle = pledgesCommDisbExp;

                                    chart = ChartWidgetUtil.getBarChart(opt, filter, WidgetUtil.ORG_PROFILE_PLEDGES_COMM_DISB);
                                }
                                break;
                            case WidgetUtil.ORG_PROFILE_AID_PREDICTIBLITY:
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_DATA_SOURCE_ONLY == typeOfExport) {
                                    aidPredTable = new Table(colspan);
                                    aidPredTable.setWidth(100);
                                    RtfCell aidPredTitleCell = new RtfCell(new Paragraph(aidPred + "(" + currName + amountInThousands + ")", OrgProfileUtil.HEADERFONTWHITE));
                                    aidPredTitleCell.setColspan(colspan);
                                    aidPredTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    aidPredTable.addCell(aidPredTitleCell);
                                    RtfCell aidPredTitleCellValue = new RtfCell(new Paragraph(aidPred, OrgProfileUtil.HEADERFONTWHITE));
                                    aidPredTitleCellValue.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    aidPredTable.addCell(aidPredTitleCellValue);
                                    OrgProfileUtil.getDataTable(aidPredTable, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_AID_PREDICTIBLITY, null);
                                }
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_CHART_ONLY == typeOfExport) {
                                    chartTitle = aidPred;
                                    if (filter.getTransactionType() == 2) {
                                        FilterHelper newFilter = new FilterHelper(filter);
                                        newFilter.setTransactionType(Constants.COMMITMENT);
                                        chart = ChartWidgetUtil.getBarChart(opt, newFilter, WidgetUtil.ORG_PROFILE_AID_PREDICTIBLITY);
                                        newFilter.setTransactionType(Constants.DISBURSEMENT);
                                        chartDisb = ChartWidgetUtil.getBarChart(opt, newFilter, WidgetUtil.ORG_PROFILE_AID_PREDICTIBLITY);
                                    } else {
                                        chart = ChartWidgetUtil.getBarChart(opt, filter, WidgetUtil.ORG_PROFILE_AID_PREDICTIBLITY);
                                    }
                                }
                                break;
                            case WidgetUtil.ORG_PROFILE_ODA_PROFILE:
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_DATA_SOURCE_ONLY == typeOfExport) {
                                    odaProfileTbl = new Table(colspan);
                                    odaProfileTbl.setWidth(100);
                                    RtfCell odaProfileTitleCell = new RtfCell(new Paragraph(odaProfile + "(" + currName + amountInThousands + ")", OrgProfileUtil.HEADERFONTWHITE));
                                    odaProfileTitleCell.setColspan(colspan);
                                    odaProfileTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    odaProfileTbl.addCell(odaProfileTitleCell);
                                    RtfCell odaProfTitleCell = new RtfCell(new Paragraph(odaProfile, OrgProfileUtil.HEADERFONTWHITE));
                                    odaProfTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    odaProfileTbl.addCell(odaProfTitleCell);
                                    OrgProfileUtil.getDataTable(odaProfileTbl, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_ODA_PROFILE, null);
                                }
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_CHART_ONLY == typeOfExport) {
                                    chartTitle = odaProfile;
                                    if (filter.getTransactionType() == 2) {
                                        FilterHelper newFilter = new FilterHelper(filter);
                                        newFilter.setTransactionType(Constants.COMMITMENT);
                                        chart = ChartWidgetUtil.getBarChart(opt, newFilter, WidgetUtil.ORG_PROFILE_ODA_PROFILE);
                                        newFilter.setTransactionType(Constants.DISBURSEMENT);
                                        chartDisb = ChartWidgetUtil.getBarChart(opt, newFilter, WidgetUtil.ORG_PROFILE_ODA_PROFILE);
                                    } else {
                                        chart = ChartWidgetUtil.getBarChart(opt, filter, WidgetUtil.ORG_PROFILE_ODA_PROFILE);
                                    }
                                }
                                break;
                            case WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN:
                                Long primaryConfigId = SectorUtil.getPrimaryConfigClassification().getId();
                                Long secondaryConfigId = null;
                                Long tertiaryConfigId = null;
                                List<AmpClassificationConfiguration> congifs = SectorUtil.getAllClassificationConfigs();
                                String secondarySectorBreakdown = null;
                                String tertiarySectorBreakdown = null;
                                if (congifs != null) {
                                    for (AmpClassificationConfiguration config : congifs) {
                                        if (config.getName().equals("Secondary") && FeaturesUtil.isVisibleSectors("Secondary", ampContext)) {
                                            secondaryConfigId = config.getId();
                                            secondarySectorBreakdown = config.getClassification().getSecSchemeName();

                                        } else {
                                            if (config.getName().equals("Tertiary") && FeaturesUtil.isVisibleSectors("Tertiary", ampContext)) {
                                                tertiaryConfigId = config.getId();
                                                tertiarySectorBreakdown = config.getClassification().getSecSchemeName();

                                            }
                                        }
                                    }
                                }
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_DATA_SOURCE_ONLY == typeOfExport) {
                                    sectorTbl = new Table(oneYearColspan);
                                    sectorTbl.setWidth(100);
                                    RtfCell sectorTitleCell = new RtfCell(new Paragraph(sectorBreakdown + "(" + currName + amountInThousands + ")", OrgProfileUtil.HEADERFONTWHITE));
                                    sectorTitleCell.setColspan(oneYearColspan);
                                    sectorTbl.addCell(sectorTitleCell);
                                    sectorTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    RtfCell sectorNameTitleCell = new RtfCell(new Paragraph(sectorBreakdown, OrgProfileUtil.HEADERFONTWHITE));
                                    sectorNameTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    sectorTbl.addCell(sectorNameTitleCell);
                                    OrgProfileUtil.getDataTable(sectorTbl, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN, primaryConfigId);
                                    if (secondaryConfigId != null) {
                                        secondarySectorTbl = new Table(oneYearColspan);
                                        secondarySectorTbl.setWidth(100);
                                        RtfCell secondarySectorTitleCell = new RtfCell(new Paragraph(secondarySectorBreakdown + "(" + currName + amountInThousands + ")", OrgProfileUtil.HEADERFONTWHITE));
                                        secondarySectorTitleCell.setColspan(oneYearColspan);
                                        secondarySectorTbl.addCell(secondarySectorTitleCell);
                                        secondarySectorTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                        RtfCell secondarySectorNameTitleCell = new RtfCell(new Paragraph(secondarySectorBreakdown, OrgProfileUtil.HEADERFONTWHITE));
                                        secondarySectorNameTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                        secondarySectorTbl.addCell(secondarySectorNameTitleCell);
                                        OrgProfileUtil.getDataTable(secondarySectorTbl, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN, secondaryConfigId);

                                    }
                                     if (tertiaryConfigId != null) {
                                        tertiarySectorTbl = new Table(oneYearColspan);
                                        tertiarySectorTbl.setWidth(100);
                                        RtfCell tertiarySectorTitleCell = new RtfCell(new Paragraph(tertiarySectorBreakdown + "(" + currName + amountInThousands + ")", OrgProfileUtil.HEADERFONTWHITE));
                                        tertiarySectorTitleCell.setColspan(oneYearColspan);
                                        tertiarySectorTbl.addCell(tertiarySectorTitleCell);
                                        tertiarySectorTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                        RtfCell tertiarySectorNameTitleCell = new RtfCell(new Paragraph(tertiarySectorBreakdown, OrgProfileUtil.HEADERFONTWHITE));
                                        tertiarySectorNameTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                        tertiarySectorTbl.addCell(tertiarySectorNameTitleCell);
                                        OrgProfileUtil.getDataTable(tertiarySectorTbl, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN, tertiaryConfigId);

                                    }
                                }
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_CHART_ONLY == typeOfExport) {
                                    chartTitle = sectorBreakdown;
                                    if (filter.getTransactionType() == 2) {
                                        FilterHelper newFilter = new FilterHelper(filter);
                                        newFilter.setTransactionType(Constants.COMMITMENT);
                                        chart = ChartWidgetUtil.getDonutChart(opt, newFilter, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN, primaryConfigId);
                                        if (secondaryConfigId != null) {
                                            chartSecondaryScheme = ChartWidgetUtil.getDonutChart(opt, newFilter, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN, secondaryConfigId);
                                            secondarySectorchartTitle = secondarySectorBreakdown;
                                        }
                                        if (tertiaryConfigId != null) {
                                            tertiarySectorchartTitle = tertiarySectorBreakdown;
                                            chartTertiaryScheme = ChartWidgetUtil.getDonutChart(opt, newFilter, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN, tertiaryConfigId);

                                        }
                                        newFilter.setTransactionType(Constants.DISBURSEMENT);
                                        chartDisb = ChartWidgetUtil.getDonutChart(opt, newFilter, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN, primaryConfigId);
                                        if (secondaryConfigId != null) {
                                            chartDisbSecondaryScheme = ChartWidgetUtil.getDonutChart(opt, newFilter, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN, secondaryConfigId);
                                        }
                                        if (tertiaryConfigId != null) {
                                            chartDisbTertiaryScheme = ChartWidgetUtil.getDonutChart(opt, newFilter, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN, tertiaryConfigId);

                                        }

                                    } else {
                                        chart = ChartWidgetUtil.getDonutChart(opt, filter, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN, primaryConfigId);
                                        if (secondaryConfigId != null) {
                                            chartSecondaryScheme = ChartWidgetUtil.getDonutChart(opt, filter, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN, secondaryConfigId);
                                            secondarySectorchartTitle = secondarySectorBreakdown;
                                        }
                                        if (tertiaryConfigId != null) {
                                            tertiarySectorchartTitle = tertiarySectorBreakdown;
                                            chartTertiaryScheme = ChartWidgetUtil.getDonutChart(opt, filter, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN, tertiaryConfigId);

                                        }
                                    }
                                }
                                break;
                            case WidgetUtil.ORG_PROFILE_REGIONAL_BREAKDOWN:
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_DATA_SOURCE_ONLY == typeOfExport) {
                                    regionTbl = new Table(oneYearColspan);
                                    regionTbl.setWidth(100);
                                    RtfCell regionTitleCell = new RtfCell(new Paragraph(regionBreakdown + "(" + currName + amountInThousands + ")", OrgProfileUtil.HEADERFONTWHITE));
                                    regionTitleCell.setColspan(oneYearColspan);
                                    regionTbl.addCell(regionTitleCell);
                                    regionTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    RtfCell regionNameTitleCell = new RtfCell(new Paragraph(regionBreakdown, OrgProfileUtil.HEADERFONTWHITE));
                                    regionNameTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    regionTbl.addCell(regionNameTitleCell);
                                    OrgProfileUtil.getDataTable(regionTbl, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_REGIONAL_BREAKDOWN, null);
                                }
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_CHART_ONLY == typeOfExport) {
                                    chartTitle = regionBreakdown;
                                    if (filter.getTransactionType() == 2) {
                                        FilterHelper newFilter = new FilterHelper(filter);
                                        newFilter.setTransactionType(Constants.COMMITMENT);
                                        chart = ChartWidgetUtil.getDonutChart(opt, newFilter, WidgetUtil.ORG_PROFILE_REGIONAL_BREAKDOWN, null);
                                        newFilter.setTransactionType(Constants.DISBURSEMENT);
                                        chartDisb = ChartWidgetUtil.getDonutChart(opt, newFilter, WidgetUtil.ORG_PROFILE_REGIONAL_BREAKDOWN, null);
                                    } else {
                                        chart = ChartWidgetUtil.getDonutChart(opt, filter, WidgetUtil.ORG_PROFILE_REGIONAL_BREAKDOWN, null);
                                    }
                                }

                                break;
                            case WidgetUtil.ORG_PROFILE_SUMMARY:

                                if (Constants.EXPORT_OPTION_CHART_DATA_SOURCE == typeOfExport) {
                                    //create summary table

                                    orgSummaryTbl = new Table(2);
                                    orgSummaryTbl.setWidth(100);
                                    RtfCell summaryTitleCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Organization Profile", langCode, siteId), OrgProfileUtil.HEADERFONTWHITE));
                                    summaryTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    summaryTitleCell.setColspan(2);
                                    summaryTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    orgSummaryTbl.addCell(summaryTitleCell);


                                    RtfCell grTypeTitleCell = new RtfCell();
                                    grTypeTitleCell.addElement(new Paragraph(TranslatorWorker.translateText("Type", langCode, siteId) + ":", OrgProfileUtil.PLAINFONT));
                                    orgSummaryTbl.addCell(grTypeTitleCell);


                                    RtfCell grTypeCell = new RtfCell();
                                    grTypeCell.addElement(new Paragraph(orgGroupTpName, OrgProfileUtil.PLAINFONT));
                                    orgSummaryTbl.addCell(grTypeCell);

                                    RtfCell orgTitleCell = new RtfCell();
                                    orgTitleCell.addElement(new Paragraph(TranslatorWorker.translateText("Organization Name", langCode, siteId) + ":", OrgProfileUtil.PLAINFONT));
                                    orgTitleCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                    orgSummaryTbl.addCell(orgTitleCell);

                                    RtfCell orgCell = new RtfCell();
                                    orgCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                    orgCell.addElement(new Paragraph((filter.getOrgIds()!=null&&filter.getOrgIds().length>1)?orgName+" ( "+OrgProfileUtil.getOrgNamesText(filter.getOrgIds())+")":orgName, OrgProfileUtil.PLAINFONT));
                                    orgSummaryTbl.addCell(orgCell);

                                    RtfCell orgAcrTitleCell = new RtfCell();
                                    orgAcrTitleCell.addElement(new Paragraph(TranslatorWorker.translateText("Organization Acronym", langCode, siteId) + ":", OrgProfileUtil.PLAINFONT));
                                    orgSummaryTbl.addCell(orgAcrTitleCell);

                                    RtfCell orgAcrCell = new RtfCell();
                                    orgAcrCell.addElement(new Paragraph(orgAcronym, OrgProfileUtil.PLAINFONT));
                                    orgSummaryTbl.addCell(orgAcrCell);


                                    RtfCell orgDnGrpTitleCell = new RtfCell();
                                    orgDnGrpTitleCell.addElement(new Paragraph(TranslatorWorker.translateText("Donor Group", langCode, siteId) + ":", OrgProfileUtil.PLAINFONT));
                                    orgDnGrpTitleCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                    orgSummaryTbl.addCell(orgDnGrpTitleCell);

                                    RtfCell orgDnGrpCell = new RtfCell();
                                    orgDnGrpCell.addElement(new Paragraph(grpName, OrgProfileUtil.PLAINFONT));
                                    orgDnGrpCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                    orgSummaryTbl.addCell(orgDnGrpCell);

                                    if (!filter.getFromPublicView()) {

                                    RtfCell orgBackgroundCell = new RtfCell();
                                    orgBackgroundCell.addElement(new Paragraph(TranslatorWorker.translateText("Background of donor", langCode, siteId) + ":", OrgProfileUtil.PLAINFONT));
                                    orgSummaryTbl.addCell(orgBackgroundCell);

                                    RtfCell orgBackgroundValue = new RtfCell();
                                    orgBackgroundValue.addElement(new Paragraph(orgBackground, OrgProfileUtil.PLAINFONT));
                                    orgSummaryTbl.addCell(orgBackgroundValue);

                                    RtfCell orgDescCell = new RtfCell();
                                    orgDescCell.addElement(new Paragraph(TranslatorWorker.translateText("Description", langCode, siteId) + ":", OrgProfileUtil.PLAINFONT));
                                    orgDescCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                    orgSummaryTbl.addCell(orgDescCell);

                                    RtfCell orgDescCellValue = new RtfCell();
                                    orgDescCellValue.addElement(new Paragraph(orgDesc, OrgProfileUtil.PLAINFONT));
                                    orgDescCellValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                    orgSummaryTbl.addCell(orgDescCellValue);
                                    }

                                    RtfCell orgWbLinkTitleCell = new RtfCell();
                                    orgWbLinkTitleCell.addElement(new Paragraph(TranslatorWorker.translateText("Web Link", langCode, siteId) + ":", OrgProfileUtil.PLAINFONT));
                                    orgSummaryTbl.addCell(orgWbLinkTitleCell);

                                    RtfCell orgWbLinkCell = new RtfCell();
                                    orgWbLinkCell.addElement(new Paragraph(orgUrl, OrgProfileUtil.PLAINFONT));
                                    orgSummaryTbl.addCell(orgWbLinkCell);

                                    int count = 0;
                                    // contacts for NGO organizations,  we have mixed code in 1.14 :(
                                    if (orgGroupType!=null&&orgGroupType.getClassification() != null && orgGroupType.getClassification().equals(Constants.ORG_TYPE_NGO)) {
                                        boolean noContactsToShow = true;
                                        if (organization != null) {
                                            orgContactsTbl = new Table(6);
                                            orgContactsTbl.setWidth(100);
                                            RtfCell contactHeaderCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Contact Information", langCode, siteId), OrgProfileUtil.HEADERFONTWHITE));
                                            contactHeaderCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                            contactHeaderCell.setColspan(6);
                                            contactHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            orgContactsTbl.addCell(contactHeaderCell);

                                            RtfCell contactLastNameCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Last Name", langCode, siteId), OrgProfileUtil.HEADERFONTWHITE));
                                            contactLastNameCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                            contactLastNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            orgContactsTbl.addCell(contactLastNameCell);

                                            RtfCell contactNameCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("First name", langCode, siteId), OrgProfileUtil.HEADERFONTWHITE));
                                            contactNameCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                            contactNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            orgContactsTbl.addCell(contactNameCell);

                                            RtfCell contactEmailCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Email", langCode, siteId), OrgProfileUtil.HEADERFONTWHITE));
                                            contactEmailCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                            contactEmailCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            orgContactsTbl.addCell(contactEmailCell);

                                            RtfCell contactPhoneCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Telephone", langCode, siteId), OrgProfileUtil.HEADERFONTWHITE));
                                            contactPhoneCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                            contactPhoneCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            orgContactsTbl.addCell(contactPhoneCell);

                                            RtfCell contactFaxCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Fax", langCode, siteId), OrgProfileUtil.HEADERFONTWHITE));
                                            contactFaxCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                            contactFaxCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            orgContactsTbl.addCell(contactFaxCell);

                                            RtfCell contactTitleCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Title", langCode, siteId), OrgProfileUtil.HEADERFONTWHITE));
                                            contactTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                            contactTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            orgContactsTbl.addCell(contactTitleCell);
                                            //contacts
                                            if (organization.getOrganizationContacts() != null) {
                                                List<AmpOrganisationContact> orgContacts = new ArrayList(organization.getOrganizationContacts());
                                                Iterator<AmpOrganisationContact> contactsIter = orgContacts.iterator();
                                                while (contactsIter.hasNext()) {
                                                    AmpOrganisationContact orgCont = contactsIter.next();
                                                    if (orgCont.getPrimaryContact() != null && orgCont.getPrimaryContact()) {
                                                        noContactsToShow = false;
                                                        AmpContact contact = orgCont.getContact();
                                                        RtfCell name = new RtfCell(new Paragraph(contact.getName(), OrgProfileUtil.PLAINFONT));
                                                        RtfCell lastName = new RtfCell(new Paragraph(contact.getLastname(), OrgProfileUtil.PLAINFONT));
                                                        String emails = "";
                                                        String phones = "";
                                                        String faxes = "";
                                                        for (AmpContactProperty property : contact.getProperties()) {
                                                            if (property.getName().equals(Constants.CONTACT_PROPERTY_NAME_EMAIL)) {
                                                                emails += property.getValue() + ";\n";
                                                            } else if (property.getName().equals(Constants.CONTACT_PROPERTY_NAME_PHONE)) {
                                                                phones += property.getValue() + ";\n";
                                                            } else {
                                                                faxes += property.getValue() + ";\n";
                                                            }
                                                        }
                                                        RtfCell emailCell = new RtfCell(new Paragraph(emails, OrgProfileUtil.PLAINFONT));
                                                        RtfCell phone = new RtfCell(new Paragraph(phones, OrgProfileUtil.PLAINFONT));
                                                        RtfCell fax = new RtfCell(new Paragraph(faxes, OrgProfileUtil.PLAINFONT));
                                                        String contacTitle = "";
                                                        if (contact.getTitle() != null) {
                                                            contacTitle = contact.getTitle().getValue();
                                                        }
                                                        RtfCell title = new RtfCell(new Paragraph(contacTitle, OrgProfileUtil.PLAINFONT));
                                                        if (count % 2 == 0) {
                                                            title.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                                            fax.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                                            phone.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                                            emailCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                                            lastName.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                                            name.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                                        }
                                                        orgContactsTbl.addCell(lastName);
                                                        orgContactsTbl.addCell(name);
                                                        orgContactsTbl.addCell(emailCell);
                                                        orgContactsTbl.addCell(phone);
                                                        orgContactsTbl.addCell(fax);
                                                        orgContactsTbl.addCell(title);
                                                        count++;
                                                    }
                                                }

                                            }


                                            if (noContactsToShow) {
                                                RtfCell contactTitleCl = new RtfCell();
                                                contactTitleCl.addElement(new Paragraph(TranslatorWorker.translateText("Contact", langCode, siteId) + ":", OrgProfileUtil.PLAINFONT));
                                                contactTitleCl.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                                orgSummaryTbl.addCell(contactTitleCl);
                                                RtfCell contactCell = new RtfCell();
                                                contactCell.addElement(new Paragraph(notAvailable, OrgProfileUtil.PLAINFONT));
                                                contactCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                                orgSummaryTbl.addCell(contactCell);
                                            }
                                        }
                                    } // contacts for non NGO organizations, we have mixed code in 1.14 :(
                                    else {
                                        RtfCell contactNameCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Name", langCode, siteId), OrgProfileUtil.PLAINFONT));
                                        contactNameCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        orgSummaryTbl.addCell(contactNameCell);

                                        RtfCell contactNameCellValue = new RtfCell(new Paragraph(contactName, OrgProfileUtil.PLAINFONT));
                                        contactNameCellValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        orgSummaryTbl.addCell(contactNameCellValue);


                                        RtfCell contactEmailCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Email", langCode, siteId), OrgProfileUtil.PLAINFONT));
                                        orgSummaryTbl.addCell(contactEmailCell);

                                        RtfCell contactEmailCellValue = new RtfCell(new Paragraph(email, OrgProfileUtil.PLAINFONT));
                                        orgSummaryTbl.addCell(contactEmailCellValue);

                                        RtfCell contactPhoneCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Telephone", langCode, siteId), OrgProfileUtil.PLAINFONT));
                                        contactPhoneCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        orgSummaryTbl.addCell(contactPhoneCell);

                                        RtfCell contactPhoneCellValue = new RtfCell(new Paragraph(contactPhone, OrgProfileUtil.PLAINFONT));
                                        contactPhoneCellValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        orgSummaryTbl.addCell(contactPhoneCellValue);

                                        RtfCell contactFaxCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Fax", langCode, siteId), OrgProfileUtil.PLAINFONT));
                                        orgSummaryTbl.addCell(contactFaxCell);

                                        RtfCell contactFaxCellValue = new RtfCell(new Paragraph(contactFax, OrgProfileUtil.PLAINFONT));
                                        orgSummaryTbl.addCell(contactFaxCellValue);

                                    }






                                    //create largest projects table
                                    int largestColspan = 3;
                                     // creating heading
                                    float widths[] = new float[]{50f, 15f, 35f};
                                    String titlePart = TranslatorWorker.translateText("Largest projects", langCode, siteId);
                                    int projectNumber = filter.getLargestProjectNumb();
                                    titlePart = projectNumber + " " + titlePart;
                                    
                                    RtfCell largestProjectsTitle = new RtfCell(new Paragraph(titlePart + " (" + (filter.getYear()) + ")", OrgProfileUtil.HEADERFONTWHITE));
                                    largestProjectsTitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    if (filter.getTransactionType() == 2) { // // we are showing disb only when  comm&disb is selected
                                        largestColspan = 4;
                                        widths= new float[]{40f, 15f,15f, 30f};
                                    }
                                    largetsProjectsTbl = new Table(largestColspan);
                                    largetsProjectsTbl.setWidths(widths);
                                    largetsProjectsTbl.setWidth(100);
                                    largestProjectsTitle.setColspan(largestColspan);
                                    largestProjectsTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    largetsProjectsTbl.addCell(largestProjectsTitle);

                                    RtfCell largestProjectsProjecttitle = new RtfCell(new Paragraph(TranslatorWorker.translateText("Project title", langCode, siteId), OrgProfileUtil.HEADERFONTWHITE));
                                    largestProjectsProjecttitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    largestProjectsProjecttitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    largetsProjectsTbl.addCell(largestProjectsProjecttitle);

                                    RtfCell largestProjectsCommitmentTitle = new RtfCell(new Paragraph(TranslatorWorker.translateText("Commitment", langCode, siteId)+"\n(" + filter.getCurrName()+")", OrgProfileUtil.HEADERFONTWHITE));
                                    largestProjectsCommitmentTitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    largestProjectsCommitmentTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    largetsProjectsTbl.addCell(largestProjectsCommitmentTitle);

                                    if (filter.getTransactionType() == 2) { // // we are showing disb only when  comm&disb is selected
                                        RtfCell largestProjectsDisbursemenTitle = new RtfCell(new Paragraph(TranslatorWorker.translateText("Disbursement", langCode, siteId)+"\n(" + filter.getCurrName()+")", OrgProfileUtil.HEADERFONTWHITE));
                                        largestProjectsDisbursemenTitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                        largestProjectsDisbursemenTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        largetsProjectsTbl.addCell(largestProjectsDisbursemenTitle);
                                    }

                                    RtfCell largestProjectsSectorTitle = new RtfCell(new Paragraph(TranslatorWorker.translateText("Sector", langCode, siteId), OrgProfileUtil.HEADERFONTWHITE));
                                    largestProjectsSectorTitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    largestProjectsSectorTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    largetsProjectsTbl.addCell(largestProjectsSectorTitle);
                                    List<Project> projects = OrgProfileUtil.getOrganisationLargestProjects(filter);
                                    Iterator<Project> projectIter = projects.iterator();
                                    count = 0;
                                    while (projectIter.hasNext()) {
                                        Project project = projectIter.next();
                                        String fullTitle = (project.getFullTitle() == null) ? project.getTitle() : project.getFullTitle();
                                        RtfCell title = new RtfCell(new Paragraph(fullTitle, OrgProfileUtil.PLAINFONT));
                                        RtfCell amount = new RtfCell(new Paragraph(project.getAmount(), OrgProfileUtil.PLAINFONT));
                                        RtfCell disbAmount = null;
                                        if (filter.getTransactionType() == 2) { // // we are showing disb only when  comm&disb is selected
                                            disbAmount = new RtfCell(new Paragraph(project.getDisbAmount(), OrgProfileUtil.PLAINFONT));
                                        }
                                        RtfCell sectorsCell = new RtfCell();
                                        if (project.getSectorNames() != null) {
                                            for (String sectorName : project.getSectorNames()) {
                                                sectorsCell.add(new Paragraph(sectorName, OrgProfileUtil.PLAINFONT));
                                            }
                                        }
                                       
                                        if (count % 2 == 0) {
                                            title.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            amount.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            if (filter.getTransactionType() == 2) { // // we are showing disb only when  comm&disb is selected
                                                disbAmount.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            }
                                            sectorsCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        }
                                        largetsProjectsTbl.addCell(title);
                                        largetsProjectsTbl.addCell(amount);
                                        if (filter.getTransactionType() == 2) { // // we are showing disb only when  comm&disb is selected
                                            largetsProjectsTbl.addCell(disbAmount);
                                        }
                                        largetsProjectsTbl.addCell(sectorsCell);
                                        count++;

                                    }
                                }
                                break;

                            case WidgetUtil.ORG_PROFILE_PARIS_DECLARATION:
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_DATA_SOURCE_ONLY == typeOfExport) {
                                    // creating Paris declaration table

                                    // creating heading
                                    float widths[] = new float[]{10f, 40f, 10f, 10f, 10f, 10f, 10f};

                                    parisDecTbl = new Table(widths.length);
                                    parisDecTbl.setWidths(widths);
                                    parisDecTbl.setWidth(100);


                                    RtfCell parisDecTitle = new RtfCell(new Paragraph(TranslatorWorker.translateText("PARIS DECLARATION INDICATORS - DONORS", langCode, siteId), OrgProfileUtil.HEADERFONTWHITE));
                                    parisDecTitle.setColspan(2);
                                    parisDecTitle.setRowspan(2);
                                    parisDecTitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    RtfCell allDonorsCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("All Donors ", langCode, siteId), OrgProfileUtil.HEADERFONTWHITE));
                                    allDonorsCell.setColspan(3);
                                    allDonorsCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    RtfCell selectedOrgCell = new RtfCell(new Paragraph(orgName, OrgProfileUtil.HEADERFONTWHITE));
                                    selectedOrgCell.setColspan(2);
                                    selectedOrgCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    RtfCell baseline = new RtfCell(new Paragraph(2005 +" "+ TranslatorWorker.translateText(" Baseline ", langCode, siteId), OrgProfileUtil.HEADERFONTWHITE));
                                    RtfCell value = new RtfCell(new Paragraph(filter.getYear() +" "+ TranslatorWorker.translateText(" Value ", langCode, siteId), OrgProfileUtil.HEADERFONTWHITE));
                                    RtfCell target = new RtfCell(new Paragraph(2010 + " "+TranslatorWorker.translateText(" Target ", langCode, siteId), OrgProfileUtil.HEADERFONTWHITE));
                                    baseline.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    value.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    target.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    baseline.setBorderColor(OrgProfileUtil.BORDERCOLOR);
                                    value.setBorderColor(OrgProfileUtil.BORDERCOLOR);
                                    target.setBorderColor(OrgProfileUtil.BORDERCOLOR);

                                    //adding headers
                                    parisDecTbl.addCell(parisDecTitle);
                                    parisDecTbl.addCell(allDonorsCell);
                                    parisDecTbl.addCell(selectedOrgCell);
                                    parisDecTbl.addCell(baseline);
                                    parisDecTbl.addCell(value);
                                    parisDecTbl.addCell(target);
                                    parisDecTbl.addCell(baseline);
                                    parisDecTbl.addCell(value);

                                    //end of creating heading

                                    // creating content


                                    Collection<AmpAhsurveyIndicator> indicators = DbUtil.getAllAhSurveyIndicators();

                                    Iterator<AmpAhsurveyIndicator> iter = indicators.iterator();
                                    int count = 0;
                                    while (iter.hasNext()) {
                                        AmpAhsurveyIndicator piIndicator = iter.next();
                                        if (piIndicator.getIndicatorCode().equals("10b") || piIndicator.getIndicatorCode().equals("8")) {
                                            continue;
                                        }
                                        ParisIndicatorHelper piHelper = new ParisIndicatorHelper(piIndicator, filter);
                                        RtfCell indicatorCode = new RtfCell(new Paragraph(piIndicator.getIndicatorCode(), OrgProfileUtil.PLAINFONT));
                                        RtfCell indicatorName = new RtfCell(new Paragraph(TranslatorWorker.translateText(piIndicator.getName(), langCode, siteId), OrgProfileUtil.PLAINFONT));
                                        String sufix = "";
                                        if (!piIndicator.getIndicatorCode().equals("6")) {
                                            sufix = "%";
                                        }

                                        RtfCell indicatorAllBaseline = new RtfCell(new Paragraph(piHelper.getAllDonorBaseLineValue() + sufix, OrgProfileUtil.PLAINFONT));
                                        RtfCell indicatorAllCurrentValue = new RtfCell(new Paragraph(piHelper.getAllCurrentValue() + sufix, OrgProfileUtil.PLAINFONT));
                                        RtfCell indicatorAllTargetValue = new RtfCell(new Paragraph(piHelper.getAllTargetValue() + sufix, OrgProfileUtil.PLAINFONT));
                                        RtfCell indicatorOrgBaseline = new RtfCell(new Paragraph(piHelper.getOrgBaseLineValue() + sufix, OrgProfileUtil.PLAINFONT));
                                        RtfCell indicatorOrgCurrentValue = new RtfCell(new Paragraph(piHelper.getOrgValue() + sufix, OrgProfileUtil.PLAINFONT));
                                        if (count % 2 == 1) {
                                            indicatorAllBaseline.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicatorAllCurrentValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicatorAllTargetValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicatorOrgBaseline.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicatorOrgCurrentValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicatorCode.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicatorName.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        }
                                        count++;
                                        parisDecTbl.addCell(indicatorCode);
                                        parisDecTbl.addCell(indicatorName);
                                        indicatorAllBaseline.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        parisDecTbl.addCell(indicatorAllBaseline);
                                        indicatorAllCurrentValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        parisDecTbl.addCell(indicatorAllCurrentValue);
                                        indicatorAllTargetValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        parisDecTbl.addCell(indicatorAllTargetValue);
                                        indicatorOrgBaseline.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        parisDecTbl.addCell(indicatorOrgBaseline);
                                        indicatorOrgCurrentValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        parisDecTbl.addCell(indicatorOrgCurrentValue);


                                        /* we should add indicator 5aii and indicator 5bii,
                                        these indicators don't exist in db so we add them manually*/

                                        if (piIndicator.getIndicatorCode().equals("5a")) {
                                            AmpAhsurveyIndicator ind5aii = new AmpAhsurveyIndicator();
                                            ind5aii.setAmpIndicatorId(piIndicator.getAmpIndicatorId());
                                            ind5aii.setIndicatorCode("5aii");
                                            RtfCell indicator5aCode = new RtfCell(new Paragraph("5aii", OrgProfileUtil.PLAINFONT));
                                            RtfCell indicator5aName = new RtfCell(new Paragraph(TranslatorWorker.translateText("Number of donors using country PFM", langCode, siteId), OrgProfileUtil.PLAINFONT));

                                            ParisIndicatorHelper piInd5aHelper = new ParisIndicatorHelper(ind5aii, filter);
                                            RtfCell indicator5aAllBaseline = new RtfCell(new Paragraph(piInd5aHelper.getAllDonorBaseLineValue() + " ", OrgProfileUtil.PLAINFONT));
                                            RtfCell indicatorAll5aCurrentValue = new RtfCell(new Paragraph(piInd5aHelper.getAllCurrentValue() + " ", OrgProfileUtil.PLAINFONT));
                                            RtfCell indicatorAll5aTargetValue = new RtfCell(new Paragraph(piInd5aHelper.getAllTargetValue() + " ", OrgProfileUtil.PLAINFONT));
                                            RtfCell indicatorOrg5aBaseline = new RtfCell(new Paragraph(piInd5aHelper.getOrgBaseLineValue() + " ", OrgProfileUtil.PLAINFONT));
                                            RtfCell indicatorOrg5aCurrentValue = new RtfCell(new Paragraph(piInd5aHelper.getOrgValue() + " ", OrgProfileUtil.PLAINFONT));

                                            if (count % 2 == 1) {
                                                indicator5aAllBaseline.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                                indicatorAll5aCurrentValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                                indicatorAll5aTargetValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                                indicatorOrg5aBaseline.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                                indicatorOrg5aCurrentValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                                indicator5aCode.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                                indicator5aName.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            }
                                            count++;
                                            parisDecTbl.addCell(indicator5aCode);
                                            parisDecTbl.addCell(indicator5aName);
                                            indicator5aAllBaseline.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            parisDecTbl.addCell(indicator5aAllBaseline);
                                            indicatorAll5aCurrentValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            parisDecTbl.addCell(indicatorAll5aCurrentValue);
                                            indicatorAll5aTargetValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            parisDecTbl.addCell(indicatorAll5aTargetValue);
                                            indicatorOrg5aBaseline.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            parisDecTbl.addCell(indicatorOrg5aBaseline);
                                            indicatorOrg5aCurrentValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            parisDecTbl.addCell(indicatorOrg5aCurrentValue);

                                        }
                                        if (piIndicator.getIndicatorCode().equals("5b")) {
                                            AmpAhsurveyIndicator ind5bii = new AmpAhsurveyIndicator();
                                            ind5bii.setIndicatorCode("5bii");
                                            ind5bii.setAmpIndicatorId(piIndicator.getAmpIndicatorId());

                                            RtfCell indicator5bCode = new RtfCell(new Paragraph("5bii", OrgProfileUtil.PLAINFONT));
                                            RtfCell indicator5bName = new RtfCell(new Paragraph(TranslatorWorker.translateText("Number of donors using country procurement system", langCode, siteId), OrgProfileUtil.PLAINFONT));

                                            ParisIndicatorHelper piInd5bHelper = new ParisIndicatorHelper(ind5bii, filter);
                                            RtfCell indicator5bAllBaseline = new RtfCell(new Paragraph(piInd5bHelper.getAllDonorBaseLineValue() + "", OrgProfileUtil.PLAINFONT));
                                            RtfCell indicator5bAllCurrentValue = new RtfCell(new Paragraph(piInd5bHelper.getAllCurrentValue() + "", OrgProfileUtil.PLAINFONT));
                                            RtfCell indicator5bAllTargetValue = new RtfCell(new Paragraph(piInd5bHelper.getAllTargetValue() + "", OrgProfileUtil.PLAINFONT));
                                            RtfCell indicator5bOrgBaseline = new RtfCell(new Paragraph(piInd5bHelper.getOrgBaseLineValue() +"", OrgProfileUtil.PLAINFONT));
                                            RtfCell indicator5bOrgCurrentValue = new RtfCell(new Paragraph(piInd5bHelper.getOrgValue() + "", OrgProfileUtil.PLAINFONT));

                                            if (count % 2 == 1) {
                                                indicator5bAllBaseline.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                                indicator5bAllCurrentValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                                indicator5bAllTargetValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                                indicator5bOrgBaseline.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                                indicator5bOrgCurrentValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                                indicator5bCode.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                                indicator5bName.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            }
                                            count++;
                                            parisDecTbl.addCell(indicator5bCode);
                                            parisDecTbl.addCell(indicator5bName);
                                            indicator5bAllBaseline.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            parisDecTbl.addCell(indicator5bAllBaseline);
                                            indicator5bAllCurrentValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            parisDecTbl.addCell(indicator5bAllCurrentValue);
                                            indicator5bAllTargetValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            parisDecTbl.addCell(indicator5bAllTargetValue);
                                            indicator5bOrgBaseline.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            parisDecTbl.addCell(indicator5bOrgBaseline);
                                            indicator5bOrgCurrentValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            parisDecTbl.addCell(indicator5bOrgCurrentValue);

                                        }
                                    }
                                }
                                //--end of creating content
                                break;


                        }
                        Font font = new Font(null, 0, 24);
                        Table chartTable = null;
                        Table chartTableDisb = null;
                        Table chartSecondarySecTable=null;
                        Table chartDisbSecondarySecTable=null;
                        Table chartTertiarySecTable=null;
                        Table chartDisbTertiarySecTable=null;

                        if (chart != null) {
                            chartTable = new Table(1);
                            chartTable.setWidth(100);
                            Plot plot = chart.getPlot();
                            plot.setNoDataMessage("No Data Available");
                            plot.setNoDataMessageFont(font);

                            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                            // write image in response
                            ChartUtilities.writeChartAsPNG(
                                    outByteStream,
                                    chart,
                                    opt.getWidth().intValue(),
                                    opt.getHeight().intValue(),
                                    info);
                            Image img = Image.getInstance(outByteStream.toByteArray());
                            img.setAlignment(Image.ALIGN_CENTER);

                            Paragraph imgTitle = new Paragraph(chartTitle, new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 12, Font.BOLD));
                            imgTitle.setAlignment(Element.ALIGN_CENTER);
                            RtfCell imgTitleCell = new RtfCell(imgTitle);
                            imgTitleCell.setHeader(true);
                            chartTable.addCell(imgTitleCell);
                            RtfCell cell = new RtfCell(img);
                            chartTable.addCell(cell);
                        }
                        if (chartSecondaryScheme != null) {
                            chartSecondarySecTable = new Table(1);
                            chartSecondarySecTable.setWidth(100);
                            Plot plot = chart.getPlot();
                            plot.setNoDataMessage("No Data Available");
                            plot.setNoDataMessageFont(font);

                            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                            // write image in response
                            ChartUtilities.writeChartAsPNG(
                                    outByteStream,
                                    chartSecondaryScheme,
                                    opt.getWidth().intValue(),
                                    opt.getHeight().intValue(),
                                    info);
                            Image img = Image.getInstance(outByteStream.toByteArray());
                            img.setAlignment(Image.ALIGN_CENTER);

                            Paragraph imgTitle = new Paragraph(secondarySectorchartTitle, new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 12, Font.BOLD));
                            imgTitle.setAlignment(Element.ALIGN_CENTER);
                            RtfCell imgTitleCell = new RtfCell(imgTitle);
                            imgTitleCell.setHeader(true);
                            chartSecondarySecTable.addCell(imgTitleCell);
                            RtfCell cell = new RtfCell(img);
                            chartSecondarySecTable.addCell(cell);
                        }

                          if (chartDisbSecondaryScheme != null) {
                            chartDisbSecondarySecTable = new Table(1);
                            chartDisbSecondarySecTable.setWidth(100);
                            Plot plot = chart.getPlot();
                            plot.setNoDataMessage("No Data Available");
                            plot.setNoDataMessageFont(font);

                            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                            // write image in response
                            ChartUtilities.writeChartAsPNG(
                                    outByteStream,
                                    chartDisbSecondaryScheme,
                                    opt.getWidth().intValue(),
                                    opt.getHeight().intValue(),
                                    info);
                            Image img = Image.getInstance(outByteStream.toByteArray());
                            img.setAlignment(Image.ALIGN_CENTER);

                            Paragraph imgTitle = new Paragraph(secondarySectorchartTitle, new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 12, Font.BOLD));
                            imgTitle.setAlignment(Element.ALIGN_CENTER);
                            RtfCell imgTitleCell = new RtfCell(imgTitle);
                            imgTitleCell.setHeader(true);
                            chartDisbSecondarySecTable.addCell(imgTitleCell);
                            RtfCell cell = new RtfCell(img);
                            chartDisbSecondarySecTable.addCell(cell);
                        }
                        if (chartDisb != null) {
                            chartTableDisb = new Table(1);
                            chartTableDisb.setWidth(100);
                            Plot plotDisb = chartDisb.getPlot();
                            plotDisb.setNoDataMessage("No Data Available");
                            plotDisb.setNoDataMessageFont(font);

                            ByteArrayOutputStream outByteStreamDisb = new ByteArrayOutputStream();
                            // write image in response
                            ChartUtilities.writeChartAsPNG(
                                    outByteStreamDisb,
                                    chartDisb,
                                    opt.getWidth().intValue(),
                                    opt.getHeight().intValue(),
                                    info);
                            Image imgDisb = Image.getInstance(outByteStreamDisb.toByteArray());
                            imgDisb.setAlignment(Image.ALIGN_MIDDLE);
                            Paragraph imgTitle = new Paragraph(chartTitle, new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 12, Font.BOLD));
                            imgTitle.setAlignment(Element.ALIGN_CENTER);
                            RtfCell imgTitleCell = new RtfCell(imgTitle);
                            imgTitleCell.setHeader(true);
                            chartTableDisb.addCell(imgTitleCell);
                            RtfCell cell = new RtfCell(imgDisb);
                            chartTableDisb.addCell(cell);

                        }
                         if (chartTertiaryScheme != null) {
                            chartTertiarySecTable = new Table(1);
                            chartTertiarySecTable.setWidth(100);
                            Plot plot = chartTertiaryScheme.getPlot();
                            plot.setNoDataMessage("No Data Available");
                            plot.setNoDataMessageFont(font);

                            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                            // write image in response
                            ChartUtilities.writeChartAsPNG(
                                    outByteStream,
                                    chartTertiaryScheme,
                                    opt.getWidth().intValue(),
                                    opt.getHeight().intValue(),
                                    info);
                            Image img = Image.getInstance(outByteStream.toByteArray());
                            img.setAlignment(Image.ALIGN_CENTER);

                            Paragraph imgTitle = new Paragraph(chartTitle, new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 12, Font.BOLD));
                            imgTitle.setAlignment(Element.ALIGN_CENTER);
                            RtfCell imgTitleCell = new RtfCell(imgTitle);
                            imgTitleCell.setHeader(true);
                            chartTertiarySecTable.addCell(imgTitleCell);
                            RtfCell cell = new RtfCell(img);
                            chartTertiarySecTable.addCell(cell);
                        }
                        if (chartDisbTertiaryScheme != null) {
                            chartDisbTertiarySecTable= new Table(1);
                            chartDisbTertiarySecTable.setWidth(100);
                            Plot plot = chartDisbTertiaryScheme.getPlot();
                            plot.setNoDataMessage("No Data Available");
                            plot.setNoDataMessageFont(font);

                            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                            // write image in response
                            ChartUtilities.writeChartAsPNG(
                                    outByteStream,
                                    chartDisbTertiaryScheme,
                                    opt.getWidth().intValue(),
                                    opt.getHeight().intValue(),
                                    info);
                            Image img = Image.getInstance(outByteStream.toByteArray());
                            img.setAlignment(Image.ALIGN_CENTER);

                            Paragraph imgTitle = new Paragraph(secondarySectorchartTitle, new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 12, Font.BOLD));
                            imgTitle.setAlignment(Element.ALIGN_CENTER);
                            RtfCell imgTitleCell = new RtfCell(imgTitle);
                            imgTitleCell.setHeader(true);
                            chartDisbTertiarySecTable.addCell(imgTitleCell);
                            RtfCell cell = new RtfCell(img);
                            chartDisbTertiarySecTable.addCell(cell);
                        }
                        if (chartTable != null) {
                            chartTable.setBorder(Table.NO_BORDER);
                            chartTable.setTableFitsPage(true);
                            doc.add(chartTable);
                        }
                        if (chartTableDisb != null) {
                            chartTableDisb.setBorder(Table.NO_BORDER);
                            chartTableDisb.setTableFitsPage(true);
                            doc.add(chartTableDisb);
                        }
                        if (chartSecondarySecTable != null) {
                            chartSecondarySecTable.setBorder(Table.NO_BORDER);
                            chartSecondarySecTable.setTableFitsPage(true);
                            doc.add(chartSecondarySecTable);
                        }
                        if (chartDisbSecondarySecTable != null) {
                            chartDisbSecondarySecTable.setBorder(Table.NO_BORDER);
                            chartDisbSecondarySecTable.setTableFitsPage(true);
                            doc.add(chartDisbSecondarySecTable);
                        }
                        if (chartTertiarySecTable != null) {
                            chartTertiarySecTable.setBorder(Table.NO_BORDER);
                            chartTertiarySecTable.setTableFitsPage(true);
                            doc.add(chartTertiarySecTable);
                        }
                        if (chartDisbTertiarySecTable != null) {
                            chartDisbTertiarySecTable.setBorder(Table.NO_BORDER);
                            chartDisbTertiarySecTable.setTableFitsPage(true);
                            doc.add(chartDisbTertiarySecTable);
                        }
                        if (orgSummaryTbl != null) {
                            doc.add(orgSummaryTbl);
                            if(orgContactsTbl!=null){
                                doc.add(orgContactsTbl);
                            }
                            doc.add(largetsProjectsTbl);
                        }
                        if (parisDecTbl != null) {
                            doc.add(parisDecTbl);
                        }
                        if (typeOfAidTbl != null) {
                            doc.add(typeOfAidTbl);
                        }
                        if (odaProfileTbl != null) {
                            doc.add(odaProfileTbl);
                        }
                        if (pledgesCommDisbTbl != null) {
                            doc.add(pledgesCommDisbTbl);
                        }
                        if (sectorTbl != null) {
                            doc.add(sectorTbl);
                        }
                        if (secondarySectorTbl != null) {
                            doc.add(secondarySectorTbl);
                        }
                        if(tertiarySectorTbl!=null){
                             doc.add(tertiarySectorTbl);
                        }
                        if (regionTbl != null) {
                            doc.add(regionTbl);
                        }
                        if (aidPredTable != null) {
                            doc.add(aidPredTable);
                        }
                    }
                }
            }
            HeaderFooter footer = new HeaderFooter(new Phrase(TranslatorWorker.translateText("Page", langCode, siteId)+": "), true);
            doc.setFooter(footer);
            doc.close();
            response.setContentLength(baos.size());
            ServletOutputStream out = response.getOutputStream();
            baos.writeTo(out);
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
