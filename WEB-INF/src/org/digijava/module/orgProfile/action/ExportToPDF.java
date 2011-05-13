package org.digijava.module.orgProfile.action;

import com.lowagie.text.Element;
import com.lowagie.text.HeaderFooter;
import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletOutputStream;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpOrganisationContact;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
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
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.orgProfile.helper.ExportSettingHelper;


public class ExportToPDF extends Action {

    private static Logger logger = Logger.getLogger(ExportToPDF.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ServletContext ampContext = getServlet().getServletContext();
        response.setContentType("application/download");
        response.setHeader("content-disposition", "attachment;filename=orgProfile.pdf");
        com.lowagie.text.Document doc = new com.lowagie.text.Document(PageSize.A4);
        String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
        String langCode = RequestUtils.getNavigationLanguage(request).getCode();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter pdfWriter =PdfWriter.getInstance(doc, baos);
            List<AmpDaWidgetPlace> orgPlaces = WidgetUtil.getAllOrgProfilePlaces();
            Iterator<AmpDaWidgetPlace> placeIter = orgPlaces.iterator();
            List<ExportSettingHelper> helpers = (List<ExportSettingHelper>) request.getAttribute("orgProfileExportSettings");
            Boolean monochrome = (Boolean) request.getAttribute("orgProfileMonochrome");
            HttpSession session = request.getSession();
            FilterHelper filter = (FilterHelper) session.getAttribute("orgProfileFilter");
            String footerText = OrgProfileUtil.getFooterText(langCode, siteId, filter);
            HeaderFooter footer = new HeaderFooter(new Phrase(footerText), false);
            footer.setBorder(0);
            doc.setFooter(footer);
            doc.open();
            com.lowagie.text.Font pageTitleFont = com.lowagie.text.FontFactory.getFont("Arial", 24, com.lowagie.text.Font.BOLD);
            Paragraph pageTitle = new Paragraph(TranslatorWorker.translateText("Org. Profile", langCode, siteId), pageTitleFont);
            pageTitle.setAlignment(Element.ALIGN_CENTER);
            doc.add(pageTitle);
            doc.add(new Paragraph(" "));
     
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
                    orgGroupTpName =grpName =orgName =orgAcronym = orgUrl =contactName
                    =email = contactPhone =contactFax = orgBackground = orgDesc =multipleSelected;
                     
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
                if (!FeaturesUtil.isVisibleFeature(place.getName(), ampContext)) {
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
                        PdfPTable orgSummaryTbl = null;
                        PdfPTable orgContactsTbl = null;
                        PdfPTable largetsProjectsTbl = null;
                        PdfPTable parisDecTbl = null;
                        PdfPTable typeOfAidTbl = null;
                        PdfPTable odaProfileTbl = null;
                        PdfPTable aidPredTable = null;
                        PdfPTable pledgesCommDisbTbl = null;
                        PdfPTable sectorTbl = null;
                        PdfPTable secondarySectorTbl = null;
                        PdfPTable tertiarySectorTbl = null;
                        PdfPTable regionTbl = null;
                        ChartRenderingInfo info = new ChartRenderingInfo();
                        String currName = filter.getCurrName();
                        String amountInThousands = "";
                        String typeOfAid = TranslatorWorker.translateText("TYPE OF AID", langCode, siteId);
                        String odaProfile = TranslatorWorker.translateText("ODA Profile", langCode, siteId);
                        String aidPred = TranslatorWorker.translateText("Aid Predictability", langCode, siteId);
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
                        String sectorBreakdown = SectorUtil.getPrimaryConfigClassification().getClassification().getSecSchemeName();
                        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
                            amountInThousands = "," + TranslatorWorker.translateText("Amounts in thousands", langCode, siteId) + " ";
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
                        int colspan = filter.getYearsInRange() + 1;
                        int oneYearColspan = 2;
                        if (filter.getTransactionType() == 2) {
                            colspan = 2 * filter.getYearsInRange() + 1;
                            oneYearColspan = 3;
                        }
                        switch (type.intValue()) {
                            case WidgetUtil.ORG_PROFILE_TYPE_OF_AID:
                                // type of aid table
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_DATA_SOURCE_ONLY == typeOfExport) {
                                    typeOfAidTbl = new PdfPTable(colspan);
                                    typeOfAidTbl.setWidthPercentage(100);
                                    PdfPCell typeofAidTitleCell = new PdfPCell(new Paragraph(typeOfAid + "(" + currName + amountInThousands + ")", OrgProfileUtil.HEADERFONT));
                                    typeofAidTitleCell.setColspan(colspan);
                                    typeOfAidTbl.addCell(typeofAidTitleCell);
                                    PdfPCell tpAidTitleCell = new PdfPCell(new Paragraph(typeOfAid, OrgProfileUtil.HEADERFONT));
                                    tpAidTitleCell.setBorderWidthRight(0);
                                    tpAidTitleCell.setBorderWidthBottom(0);
                                    tpAidTitleCell.setBorderWidthTop(0);
                                    typeOfAidTbl.addCell(tpAidTitleCell);
                                    OrgProfileUtil.getDataTable(typeOfAidTbl, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_TYPE_OF_AID, null);
                                }
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_CHART_ONLY == typeOfExport) {
                                    chartTitle = typeOfAid;
                                    opt.setTitle(chartTitle);
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
                                    pledgesCommDisbTbl = new PdfPTable(colspan);
                                    pledgesCommDisbTbl.setWidthPercentage(100);
                                    PdfPCell pledgesCommDisbTitleCell = new PdfPCell(new Paragraph(pledgesCommDisbExp + "(" + currName + amountInThousands + ")", OrgProfileUtil.HEADERFONT));
                                    pledgesCommDisbTitleCell.setColspan(colspan);
                                    pledgesCommDisbTbl.addCell(pledgesCommDisbTitleCell);
                                    PdfPCell pldsCommDisbdTitleCell = new PdfPCell(new Paragraph(" ", OrgProfileUtil.HEADERFONT));
                                    pledgesCommDisbTbl.addCell(pldsCommDisbdTitleCell);
                                    OrgProfileUtil.getDataTable(pledgesCommDisbTbl, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_PLEDGES_COMM_DISB, null);
                                }
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_CHART_ONLY == typeOfExport) {
                                    chartTitle = pledgesCommDisbExp;
                                    opt.setTitle(chartTitle);
                                    chart = ChartWidgetUtil.getBarChart(opt, filter, WidgetUtil.ORG_PROFILE_PLEDGES_COMM_DISB);
                                }
                                break;
                            case WidgetUtil.ORG_PROFILE_ODA_PROFILE:
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_DATA_SOURCE_ONLY == typeOfExport) {
                                    odaProfileTbl = new PdfPTable(colspan);
                                    odaProfileTbl.setWidthPercentage(100);
                                    PdfPCell odaProfileTitleCell = new PdfPCell(new Paragraph(odaProfile + "(" + currName + amountInThousands + ")", OrgProfileUtil.HEADERFONT));
                                    odaProfileTitleCell.setColspan(colspan);
                                    odaProfileTbl.addCell(odaProfileTitleCell);
                                    PdfPCell odaProfTitleCell = new PdfPCell(new Paragraph(odaProfile, OrgProfileUtil.HEADERFONT));
                                    odaProfTitleCell.setBorderWidthRight(0);
                                    odaProfTitleCell.setBorderWidthBottom(0);
                                    odaProfTitleCell.setBorderWidthTop(0);
                                    odaProfileTbl.addCell(odaProfTitleCell);
                                    OrgProfileUtil.getDataTable(odaProfileTbl, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_ODA_PROFILE, null);
                                }
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_CHART_ONLY == typeOfExport) {
                                    chartTitle = odaProfile;
                                    opt.setTitle(chartTitle);
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
                            case WidgetUtil.ORG_PROFILE_AID_PREDICTIBLITY:
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_DATA_SOURCE_ONLY == typeOfExport) {
                                    aidPredTable = new PdfPTable(colspan);
                                    aidPredTable.setWidthPercentage(100);
                                    PdfPCell aidPredTitleCell = new PdfPCell(new Paragraph(aidPred + "(" + currName + amountInThousands + ")", OrgProfileUtil.HEADERFONT));
                                    aidPredTitleCell.setColspan(colspan);
                                    aidPredTable.addCell(aidPredTitleCell);
                                    PdfPCell aidPredTitleCellValue = new PdfPCell(new Paragraph(aidPred, OrgProfileUtil.HEADERFONT));
                                    aidPredTitleCellValue.setBorderWidthRight(0);
                                    aidPredTitleCellValue.setBorderWidthBottom(0);
                                    aidPredTitleCellValue.setBorderWidthTop(0);
                                    aidPredTable.addCell(aidPredTitleCellValue);
                                    OrgProfileUtil.getDataTable(aidPredTable, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_AID_PREDICTIBLITY, null);

                                }
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_CHART_ONLY == typeOfExport) {
                                    chartTitle = aidPred;
                                    opt.setTitle(chartTitle);
                                    if (filter.getTransactionType() == 2) {
                                        FilterHelper newFilter = new FilterHelper(filter);
                                        newFilter.setTransactionType(Constants.COMMITMENT);
                                        chart = ChartWidgetUtil.getBarChart(opt, newFilter,WidgetUtil.ORG_PROFILE_AID_PREDICTIBLITY);
                                        newFilter.setTransactionType(Constants.DISBURSEMENT);
                                        chartDisb = ChartWidgetUtil.getBarChart(opt, newFilter, WidgetUtil.ORG_PROFILE_AID_PREDICTIBLITY);
                                    } else {
                                        chart = ChartWidgetUtil.getBarChart(opt, filter, WidgetUtil.ORG_PROFILE_AID_PREDICTIBLITY);
                                    }
                                }
                                break;
                            case WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN:
                                Long primaryConfigId = SectorUtil.getPrimaryConfigClassification().getId();
                                Long secondaryConfigId = null;
                                Long tertiaryConfigId=null;
                                List<AmpClassificationConfiguration> congifs = SectorUtil.getAllClassificationConfigs();
                                String secondarySectorBreakdown = null;
                                String tertiarySectorBreakdown = null;
                                if (congifs != null) {
                                    for (AmpClassificationConfiguration config : congifs) {
                                        if (config.getName().equals("Secondary") && FeaturesUtil.isVisibleSectors("Secondary", ampContext)) {
                                            secondaryConfigId = config.getId();
                                            secondarySectorBreakdown = config.getClassification().getSecSchemeName() ;

                                        } else {
                                            if (config.getName().equals("Tertiary") && FeaturesUtil.isVisibleSectors("Tertiary", ampContext)) {
                                                tertiaryConfigId = config.getId();
                                                tertiarySectorBreakdown = config.getClassification().getSecSchemeName();

                                            }

                                        }
                                    }
                                }
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_DATA_SOURCE_ONLY == typeOfExport) {
                                    sectorTbl = new PdfPTable(oneYearColspan);
                                    sectorTbl.setWidthPercentage(100);
                                    PdfPCell sectorTitleCell = new PdfPCell(new Paragraph(sectorBreakdown + "(" + currName + amountInThousands + ")", OrgProfileUtil.HEADERFONT));
                                    sectorTitleCell.setColspan(oneYearColspan);
                                    sectorTbl.addCell(sectorTitleCell);
                                    PdfPCell sectorNameTitleCell = new PdfPCell(new Paragraph(sectorBreakdown, OrgProfileUtil.HEADERFONT));
                                    sectorNameTitleCell.setBorderWidthRight(0);
                                    sectorNameTitleCell.setBorderWidthBottom(0);
                                    sectorNameTitleCell.setBorderWidthTop(0);
                                    sectorTbl.addCell(sectorNameTitleCell);
                                    OrgProfileUtil.getDataTable(sectorTbl, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN, primaryConfigId);
                                    if (secondaryConfigId != null) {
                                        secondarySectorTbl = new PdfPTable(oneYearColspan);
                                        secondarySectorTbl.setWidthPercentage(100);
                                        PdfPCell secondarySectorTitleCell = new PdfPCell(new Paragraph(secondarySectorBreakdown + "(" + currName + amountInThousands + ")", OrgProfileUtil.HEADERFONT));
                                        secondarySectorTitleCell.setColspan(oneYearColspan);
                                        secondarySectorTbl.addCell(secondarySectorTitleCell);
                                        PdfPCell secondarySectorNameTitleCell = new PdfPCell(new Paragraph(secondarySectorBreakdown, OrgProfileUtil.HEADERFONT));
                                        secondarySectorNameTitleCell.setBorderWidthRight(0);
                                        secondarySectorNameTitleCell.setBorderWidthBottom(0);
                                        secondarySectorNameTitleCell.setBorderWidthTop(0);
                                        secondarySectorTbl.addCell(secondarySectorNameTitleCell);
                                        OrgProfileUtil.getDataTable(secondarySectorTbl, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN, secondaryConfigId);

                                    }
                                     if (tertiaryConfigId != null) {
                                        tertiarySectorTbl = new PdfPTable(oneYearColspan);
                                        tertiarySectorTbl.setWidthPercentage(100);
                                        PdfPCell tertiarySectorTitleCell = new PdfPCell(new Paragraph(tertiarySectorBreakdown + "(" + currName + amountInThousands + ")", OrgProfileUtil.HEADERFONT));
                                        tertiarySectorTitleCell.setColspan(oneYearColspan);
                                        tertiarySectorTbl.addCell(tertiarySectorTitleCell);
                                        PdfPCell tertiarySectorNameTitleCell = new PdfPCell(new Paragraph(tertiarySectorBreakdown, OrgProfileUtil.HEADERFONT));
                                        tertiarySectorNameTitleCell.setBorderWidthRight(0);
                                        tertiarySectorNameTitleCell.setBorderWidthBottom(0);
                                        tertiarySectorNameTitleCell.setBorderWidthTop(0);
                                        tertiarySectorTbl.addCell(tertiarySectorNameTitleCell);
                                        OrgProfileUtil.getDataTable(tertiarySectorTbl, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN, tertiaryConfigId);

                                    }
                                }
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_CHART_ONLY == typeOfExport) {
                                    if (filter.getTransactionType() == 2) {
                                        FilterHelper newFilter = new FilterHelper(filter);
                                        newFilter.setTransactionType(Constants.COMMITMENT);
                                        opt.setTitle( sectorBreakdown);
                                        chart = ChartWidgetUtil.getDonutChart(opt, newFilter, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN, primaryConfigId);
                                        if (secondaryConfigId != null) {
                                            opt.setTitle(secondarySectorBreakdown);
                                            chartSecondaryScheme = ChartWidgetUtil.getDonutChart(opt, newFilter, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN, secondaryConfigId);

                                        }
                                        if(tertiaryConfigId!=null){
                                            opt.setTitle(tertiarySectorBreakdown);
                                            chartTertiaryScheme = ChartWidgetUtil.getDonutChart(opt, newFilter, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN,tertiaryConfigId);

                                        }
                                        newFilter.setTransactionType(Constants.DISBURSEMENT);
                                        opt.setTitle( sectorBreakdown);
                                        chartDisb = ChartWidgetUtil.getDonutChart(opt, newFilter, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN, primaryConfigId);
                                        if (secondaryConfigId != null) {
                                            opt.setTitle(secondarySectorBreakdown);
                                            chartDisbSecondaryScheme = ChartWidgetUtil.getDonutChart(opt, newFilter, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN, secondaryConfigId);
                                        }
                                         if(tertiaryConfigId!=null){
                                            opt.setTitle(tertiarySectorBreakdown);
                                            chartDisbTertiaryScheme = ChartWidgetUtil.getDonutChart(opt, newFilter, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN, tertiaryConfigId);

                                        }

                                    } else {
                                        opt.setTitle( sectorBreakdown);
                                        chart = ChartWidgetUtil.getDonutChart(opt, filter, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN, primaryConfigId);
                                        if (secondaryConfigId != null) {
                                            opt.setTitle(secondarySectorBreakdown);
                                            chartSecondaryScheme = ChartWidgetUtil.getDonutChart(opt, filter, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN, secondaryConfigId);
                                        }
                                        if(tertiaryConfigId!=null){
                                            opt.setTitle(tertiarySectorBreakdown);
                                            chartDisbTertiaryScheme = ChartWidgetUtil.getDonutChart(opt, filter, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN, tertiaryConfigId);

                                        }
                                    }
                                }
                                break;
                            case WidgetUtil.ORG_PROFILE_REGIONAL_BREAKDOWN:
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_DATA_SOURCE_ONLY == typeOfExport) {
                                    regionTbl = new PdfPTable(oneYearColspan);
                                    regionTbl.setWidthPercentage(100);
                                    PdfPCell regionTitleCell = new PdfPCell(new Paragraph(regionBreakdown + "(" + currName + amountInThousands + ")", OrgProfileUtil.HEADERFONT));
                                    regionTitleCell.setColspan(oneYearColspan);
                                    regionTbl.addCell(regionTitleCell);
                                    PdfPCell regionNameTitleCell = new PdfPCell(new Paragraph(regionBreakdown, OrgProfileUtil.HEADERFONT));
                                    regionNameTitleCell.setBorderWidthRight(0);
                                    regionNameTitleCell.setBorderWidthBottom(0);
                                    regionNameTitleCell.setBorderWidthTop(0);
                                    regionTbl.addCell(regionNameTitleCell);
                                    OrgProfileUtil.getDataTable(regionTbl, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_REGIONAL_BREAKDOWN, null);
                                }
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_CHART_ONLY == typeOfExport) {
                                    String title = regionBreakdown;
                                    opt.setTitle(title);
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


                                //create summary table
                                if (Constants.EXPORT_OPTION_CHART_DATA_SOURCE == typeOfExport) {
                                    orgSummaryTbl = new PdfPTable(2);
                                    orgSummaryTbl.setWidthPercentage(100);
                                    PdfPCell summaryTitleCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Organization Profile", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                    summaryTitleCell.setColspan(2);
                                    summaryTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    orgSummaryTbl.addCell(summaryTitleCell);



                                    PdfPCell grTypeTitleCell = new PdfPCell();
                                    grTypeTitleCell.addElement(new Paragraph(TranslatorWorker.translateText("Type", langCode, siteId) + ":", OrgProfileUtil.PLAINFONT));
                                    orgSummaryTbl.addCell(grTypeTitleCell);

                                    PdfPCell grTypeCell = new PdfPCell();
                                    grTypeCell.addElement(new Paragraph(orgGroupTpName, OrgProfileUtil.PLAINFONT));
                                    orgSummaryTbl.addCell(grTypeCell);

                                    PdfPCell orgTitleCell = new PdfPCell();
                                    orgTitleCell.addElement(new Paragraph(TranslatorWorker.translateText("Organization Name", langCode, siteId) + ":", OrgProfileUtil.PLAINFONT));
                                    orgSummaryTbl.addCell(orgTitleCell);

                                    PdfPCell orgCell = new PdfPCell();
                                    orgCell.addElement(new Paragraph(orgName, OrgProfileUtil.PLAINFONT));
                                    orgSummaryTbl.addCell(orgCell);

                                    PdfPCell orgAcrTitleCell = new PdfPCell();
                                    orgAcrTitleCell.addElement(new Paragraph(TranslatorWorker.translateText("Organization Acronym", langCode, siteId), OrgProfileUtil.PLAINFONT));
                                    orgSummaryTbl.addCell(orgAcrTitleCell);

                                    PdfPCell orgAcrCell = new PdfPCell();
                                    orgAcrCell.addElement(new Paragraph(orgAcronym, OrgProfileUtil.PLAINFONT));
                                    orgSummaryTbl.addCell(orgAcrCell);


                                    PdfPCell orgDnGrpTitleCell = new PdfPCell();
                                    orgDnGrpTitleCell.addElement(new Paragraph(TranslatorWorker.translateText("Donor Group", langCode, siteId) + ":", OrgProfileUtil.PLAINFONT));
                                    orgSummaryTbl.addCell(orgDnGrpTitleCell);

                                    PdfPCell orgDnGrpCell = new PdfPCell();
                                    orgDnGrpCell.addElement(new Paragraph(grpName, OrgProfileUtil.PLAINFONT));
                                    orgSummaryTbl.addCell(orgDnGrpCell);
                                    if (!filter.getFromPublicView()) {

                                        PdfPCell orgBackgroundCell = new PdfPCell();
                                        orgBackgroundCell.addElement(new Paragraph(TranslatorWorker.translateText("Background of donor", langCode, siteId) + ":", OrgProfileUtil.PLAINFONT));
                                        orgSummaryTbl.addCell(orgBackgroundCell);

                                        PdfPCell orgBackgroundValue = new PdfPCell();
                                        orgBackgroundValue.addElement(new Paragraph(orgBackground, OrgProfileUtil.PLAINFONT));
                                        orgSummaryTbl.addCell(orgBackgroundValue);

                                        PdfPCell orgDescCell = new PdfPCell();
                                        orgDescCell.addElement(new Paragraph(TranslatorWorker.translateText("Description", langCode, siteId) + ":", OrgProfileUtil.PLAINFONT));
                                        orgSummaryTbl.addCell(orgDescCell);

                                        PdfPCell orgDescCellValue = new PdfPCell();
                                        orgDescCellValue.addElement(new Paragraph(orgDesc, OrgProfileUtil.PLAINFONT));
                                        orgSummaryTbl.addCell(orgDescCellValue);
                                    }


                                    PdfPCell orgWbLinkTitleCell = new PdfPCell();
                                    orgWbLinkTitleCell.addElement(new Paragraph(TranslatorWorker.translateText("Web Link", langCode, siteId) + ":", OrgProfileUtil.PLAINFONT));
                                    orgSummaryTbl.addCell(orgWbLinkTitleCell);

                                    PdfPCell orgWbLinkCell = new PdfPCell();
                                    orgWbLinkCell.addElement(new Paragraph(orgUrl, OrgProfileUtil.PLAINFONT));
                                    orgSummaryTbl.addCell(orgWbLinkCell);



                                    //create contacts table

                                    int count = 0;
                                    // contacts for NGO organizations,  we have mixed code in 1.14 :(
                                    if (orgGroupType!=null&&orgGroupType.getClassification() != null && orgGroupType.getClassification().equals(Constants.ORG_TYPE_NGO)) {
                                        boolean noContactsToShow = true;
                                        if (organization != null) {
                                            orgContactsTbl = new PdfPTable(6);
                                            orgContactsTbl.setWidthPercentage(100);

                                            PdfPCell contactHeaderCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Contact Information", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                            contactHeaderCell.setColspan(6);
                                            contactHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            orgContactsTbl.addCell(contactHeaderCell);

                                            PdfPCell contactLastNameCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Last Name", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                            contactLastNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            orgContactsTbl.addCell(contactLastNameCell);

                                            PdfPCell contactNameCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("First name", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                            contactNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            orgContactsTbl.addCell(contactNameCell);

                                            PdfPCell contactEmailCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Email", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                            contactEmailCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            orgContactsTbl.addCell(contactEmailCell);

                                            PdfPCell contactPhoneCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Telephone", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                            contactPhoneCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            orgContactsTbl.addCell(contactPhoneCell);

                                            PdfPCell contactFaxCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Fax", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                            contactFaxCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            orgContactsTbl.addCell(contactFaxCell);

                                            PdfPCell contactTitleCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Title", langCode, siteId), OrgProfileUtil.HEADERFONT));
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
                                                        PdfPCell name = new PdfPCell(new Paragraph(contact.getName(), OrgProfileUtil.PLAINFONT));
                                                        PdfPCell lastName = new PdfPCell(new Paragraph(contact.getLastname(), OrgProfileUtil.PLAINFONT));
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
                                                        PdfPCell emailCell = new PdfPCell(new Paragraph(emails, OrgProfileUtil.PLAINFONT));
                                                        PdfPCell phone = new PdfPCell(new Paragraph(phones, OrgProfileUtil.PLAINFONT));
                                                        PdfPCell fax = new PdfPCell(new Paragraph(faxes, OrgProfileUtil.PLAINFONT));
                                                        String contacTitle = "";
                                                        if (contact.getTitle() != null) {
                                                            contacTitle = contact.getTitle().getValue();
                                                        }
                                                        PdfPCell title = new PdfPCell(new Paragraph(contacTitle, OrgProfileUtil.PLAINFONT));
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
                                                PdfPCell contactTitleCl = new PdfPCell();
                                                contactTitleCl.addElement(new Paragraph(TranslatorWorker.translateText("Contact", langCode, siteId) + ":", OrgProfileUtil.PLAINFONT));
                                                orgSummaryTbl.addCell(contactTitleCl);
                                                PdfPCell contactCell = new PdfPCell();
                                                contactCell.addElement(new Paragraph(notAvailable, OrgProfileUtil.PLAINFONT));
                                                orgSummaryTbl.addCell(contactCell);
                                            }
                                        }
                                    } // contacts for non NGO organizations, we have mixed code in 1.14 :(
                                    else {
                                        PdfPCell contactNameCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Name", langCode, siteId), OrgProfileUtil.PLAINFONT));
                                        orgSummaryTbl.addCell(contactNameCell);

                                        PdfPCell contactNameCellValue = new PdfPCell(new Paragraph(contactName, OrgProfileUtil.PLAINFONT));
                                        orgSummaryTbl.addCell(contactNameCellValue);


                                        PdfPCell contactEmailCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Email", langCode, siteId), OrgProfileUtil.PLAINFONT));
                                        orgSummaryTbl.addCell(contactEmailCell);

                                        PdfPCell contactEmailCellValue = new PdfPCell(new Paragraph(email, OrgProfileUtil.PLAINFONT));
                                        orgSummaryTbl.addCell(contactEmailCellValue);

                                        PdfPCell contactPhoneCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Telephone", langCode, siteId), OrgProfileUtil.PLAINFONT));
                                        orgSummaryTbl.addCell(contactPhoneCell);

                                        PdfPCell contactPhoneCellValue = new PdfPCell(new Paragraph(contactPhone, OrgProfileUtil.PLAINFONT));
                                        orgSummaryTbl.addCell(contactPhoneCellValue);

                                        PdfPCell contactFaxCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Fax", langCode, siteId), OrgProfileUtil.PLAINFONT));
                                        orgSummaryTbl.addCell(contactFaxCell);

                                        PdfPCell contactFaxCellValue = new PdfPCell(new Paragraph(contactFax, OrgProfileUtil.PLAINFONT));
                                        orgSummaryTbl.addCell(contactFaxCellValue);

                                    }
                                   
                                    //create largest projects table
                                    largetsProjectsTbl = new PdfPTable(new float[]{25, 20, 55});
                                    int projectNumber = filter.getLargestProjectNumb();
                                    String titlePart = TranslatorWorker.translateText("Largest projects", langCode, siteId);                  
                                    titlePart = projectNumber + " " + titlePart;
                                    PdfPCell largestProjectsTitle = new PdfPCell(new Paragraph(titlePart + " (" + (filter.getYear()) + ")", OrgProfileUtil.HEADERFONT));             
                                    int largestColspan = 3;

                                    if (filter.getTransactionType() == 2) { // // we are showing disb only when  comm&disb is selected
                                        largetsProjectsTbl = new PdfPTable(new float[]{25, 20, 20, 35});
                                        largestColspan = 4;
                                    }
                                    largetsProjectsTbl.setWidthPercentage(100);
                                    largestProjectsTitle.setColspan(largestColspan);
                                    largestProjectsTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    largetsProjectsTbl.addCell(largestProjectsTitle);

                                    PdfPCell largestProjectsProjecttitle = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Project title", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                    largestProjectsProjecttitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    largetsProjectsTbl.addCell(largestProjectsProjecttitle);

                                    PdfPCell largestProjectsCommitmentTitle = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Commitment", langCode, siteId) + "\n(" + filter.getCurrName() + ")", OrgProfileUtil.HEADERFONT));
                                    largestProjectsCommitmentTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    largestProjectsCommitmentTitle.setNoWrap(true);
                                    largetsProjectsTbl.addCell(largestProjectsCommitmentTitle);

                                    if (filter.getTransactionType() == 2) { // // we are showing disb only when  comm&disb is selected
                                        PdfPCell largestProjectsDisbTitle = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Disbursement", langCode, siteId) + "\n(" + filter.getCurrName() + ")", OrgProfileUtil.HEADERFONT));
                                        largestProjectsDisbTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        largestProjectsDisbTitle.setNoWrap(true);
                                        largetsProjectsTbl.addCell(largestProjectsDisbTitle);
                                    }
                                    PdfPCell largestProjectsSectorTitle = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Sector", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                    largestProjectsSectorTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    largetsProjectsTbl.addCell(largestProjectsSectorTitle);


                                    List<Project> projects = OrgProfileUtil.getOrganisationLargestProjects(filter);
                                    Iterator<Project> projectIter = projects.iterator();
                                    count = 0;
                                    while (projectIter.hasNext()) {
                                        Project project = projectIter.next();
                                        String fullTitle = (project.getFullTitle() == null) ? project.getTitle() : project.getFullTitle();
                                        PdfPCell title = new PdfPCell(new Paragraph(fullTitle, OrgProfileUtil.PLAINFONT));
                                        PdfPCell amount = new PdfPCell(new Paragraph(project.getAmount(), OrgProfileUtil.PLAINFONT));
                                        PdfPCell disbAmount = new PdfPCell(new Paragraph(project.getDisbAmount(), OrgProfileUtil.PLAINFONT));
                                        PdfPCell sectorsCell = new PdfPCell();
                                        if (project.getSectorNames() != null) {
                                            for (String sectorName : project.getSectorNames()) {
                                                sectorsCell.addElement(new Paragraph(sectorName, OrgProfileUtil.PLAINFONT));
                                            }
                                        }
                                      

                                        largetsProjectsTbl.addCell(title);
                                        largetsProjectsTbl.addCell(amount);
                                        if (filter.getTransactionType() == 2) {
                                            largetsProjectsTbl.addCell(disbAmount);
                                        }
                                        largetsProjectsTbl.addCell(sectorsCell);
                                        count++;

                                    }
                                }
                                break;

                            case WidgetUtil.ORG_PROFILE_PARIS_DECLARATION:
                                if (Constants.EXPORT_OPTION_CHART_DATA_SOURCE == typeOfExport) {
                                    // creating Paris declaration table

                                    /*  creating heading */
                                    float widths[] = new float[]{10f, 40f, 10f, 10f, 10f, 10f, 10f};

                                    parisDecTbl = new PdfPTable(widths.length);
                                    parisDecTbl.setWidths(widths);

                                    parisDecTbl.setWidthPercentage(100);
                                    PdfPCell parisDecTitle = new PdfPCell(new Paragraph(TranslatorWorker.translateText("PARIS DECLARATION INDICATORS - DONORS", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                    parisDecTitle.setColspan(2);
                                    
                                    parisDecTbl.addCell(parisDecTitle);
                                    PdfPCell allDonorsCell = new PdfPCell();
                                    allDonorsCell.setColspan(3);
                                    allDonorsCell.setPadding(0);
                                    PdfPCell selectedOrgCell = new PdfPCell();
                                    selectedOrgCell.setColspan(2);
                                    selectedOrgCell.setPadding(0);
                                    PdfPTable allDonorsTbl = new PdfPTable(3);
                                    PdfPTable selectedOrgTbl = new PdfPTable(2);

                                    PdfPCell allDonorsTblTitle = new PdfPCell(new Paragraph(TranslatorWorker.translateText("All Donors ", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                    
                                    allDonorsTblTitle.setColspan(3);
                                    allDonorsTblTitle.setFixedHeight(30);


                                    allDonorsTbl.addCell(allDonorsTblTitle);
                                    PdfPCell baseline = new PdfPCell(new Paragraph(2005 +" "+ TranslatorWorker.translateText(" Baseline ", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                    PdfPCell value = new PdfPCell(new Paragraph(filter.getYear() +" "+ TranslatorWorker.translateText(" Value ", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                    PdfPCell target = new PdfPCell(new Paragraph(2010 +" "+ TranslatorWorker.translateText(" Target ", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                                       
                                    allDonorsTbl.addCell(baseline);
                                    allDonorsTbl.addCell(value);
                                    allDonorsTbl.addCell(target);
                                    allDonorsTbl.setWidthPercentage(100);
                                    allDonorsCell.addElement(allDonorsTbl);
                                    parisDecTbl.addCell(allDonorsCell);


                                    PdfPCell selectedOrgTblTitle = new PdfPCell(new Paragraph(orgName, OrgProfileUtil.HEADERFONT));
                                    selectedOrgTblTitle.setColspan(2);
                                    selectedOrgTblTitle.setFixedHeight(30);
                                    selectedOrgTbl.addCell(selectedOrgTblTitle);

                                    selectedOrgTbl.addCell(baseline);
                                    selectedOrgTbl.addCell(value);
                                    selectedOrgTbl.setWidthPercentage(100);

                                    selectedOrgCell.addElement(selectedOrgTbl);
                                    parisDecTbl.addCell(selectedOrgCell);
                                    //-- end of creating heading--//

                                    //-- creating content--//

                                    Collection<AmpAhsurveyIndicator> indicators = DbUtil.getAllAhSurveyIndicators();

                                    Iterator<AmpAhsurveyIndicator> iter = indicators.iterator();
                                    int count = 0;
                                    while (iter.hasNext()) {
                                        AmpAhsurveyIndicator piIndicator = iter.next();
                                        if (piIndicator.getIndicatorCode().equals("10b") || piIndicator.getIndicatorCode().equals("8")) {
                                            continue;
                                        }
                                        ParisIndicatorHelper piHelper = new ParisIndicatorHelper(piIndicator, filter);
                                        PdfPCell indicatorCode = new PdfPCell(new Paragraph(piIndicator.getIndicatorCode(), OrgProfileUtil.PLAINFONT));
                                        PdfPCell indicatorName = new PdfPCell(new Paragraph(TranslatorWorker.translateText(piIndicator.getName(), langCode, siteId), OrgProfileUtil.PLAINFONT));
                                        String sufix = "";
                                        if (!piIndicator.getIndicatorCode().equals("6")) {
                                            sufix = "%";
                                        }

                                        PdfPCell indicatorAllBaseline = new PdfPCell(new Paragraph(piHelper.getAllDonorBaseLineValue() + sufix, OrgProfileUtil.PLAINFONT));
                                        PdfPCell indicatorAllCurrentValue = new PdfPCell(new Paragraph(piHelper.getAllCurrentValue() + sufix, OrgProfileUtil.PLAINFONT));
                                        PdfPCell indicatorAllTargetValue = new PdfPCell(new Paragraph(piHelper.getAllTargetValue() + sufix, OrgProfileUtil.PLAINFONT));
                                        PdfPCell indicatorOrgBaseline = new PdfPCell(new Paragraph(piHelper.getOrgBaseLineValue() + sufix, OrgProfileUtil.PLAINFONT));
                                        PdfPCell indicatorOrgCurrentValue = new PdfPCell(new Paragraph(piHelper.getOrgValue() + sufix, OrgProfileUtil.PLAINFONT));
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
                                            PdfPCell indicator5aCode = new PdfPCell(new Paragraph("5aii", OrgProfileUtil.PLAINFONT));
                                            PdfPCell indicator5aName = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Number of donors using country PFM", langCode, siteId), OrgProfileUtil.PLAINFONT));

                                            ParisIndicatorHelper piInd5aHelper = new ParisIndicatorHelper(ind5aii, filter);
                                            PdfPCell indicator5aAllBaseline = new PdfPCell(new Paragraph(piInd5aHelper.getAllDonorBaseLineValue() + " ", OrgProfileUtil.PLAINFONT));
                                            PdfPCell indicatorAll5aCurrentValue = new PdfPCell(new Paragraph(piInd5aHelper.getAllCurrentValue() + " ", OrgProfileUtil.PLAINFONT));
                                            PdfPCell indicatorAll5aTargetValue = new PdfPCell(new Paragraph(piInd5aHelper.getAllTargetValue() + " ", OrgProfileUtil.PLAINFONT));
                                            PdfPCell indicatorOrg5aBaseline = new PdfPCell(new Paragraph(piInd5aHelper.getOrgBaseLineValue() + " ", OrgProfileUtil.PLAINFONT));
                                            PdfPCell indicatorOrg5aCurrentValue = new PdfPCell(new Paragraph(piInd5aHelper.getOrgValue() + " ", OrgProfileUtil.PLAINFONT));

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

                                            PdfPCell indicator5bCode = new PdfPCell(new Paragraph("5bii", OrgProfileUtil.PLAINFONT));
                                            PdfPCell indicator5bName = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Number of donors using country procurement system", langCode, siteId), OrgProfileUtil.PLAINFONT));
                                            ParisIndicatorHelper piInd5bHelper = new ParisIndicatorHelper(ind5bii, filter);
                                            PdfPCell indicator5bAllBaseline = new PdfPCell(new Paragraph(piInd5bHelper.getAllDonorBaseLineValue()+ " ",OrgProfileUtil.PLAINFONT));
                                            PdfPCell indicator5bAllCurrentValue = new PdfPCell(new Paragraph(piInd5bHelper.getAllCurrentValue()+ " ",OrgProfileUtil.PLAINFONT));
                                            PdfPCell indicator5bAllTargetValue = new PdfPCell(new Paragraph(piInd5bHelper.getAllTargetValue()+ " ",OrgProfileUtil.PLAINFONT));
                                            PdfPCell indicator5bOrgBaseline = new PdfPCell(new Paragraph(piInd5bHelper.getOrgBaseLineValue() + " ",OrgProfileUtil.PLAINFONT));
                                            PdfPCell indicator5bOrgCurrentValue = new PdfPCell(new Paragraph(piInd5bHelper.getOrgValue() +  " ",OrgProfileUtil.PLAINFONT));

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
                                    //--end of creating content--//
                                }
                                break;

                        }
                        Font font = new Font(null, 0, 24);
                        float width=opt.getWidth();
                        float height=opt.getHeight();
                         if (chart != null) {
                            PdfPTable chartTable=new  PdfPTable(1);
                            Plot plot = chart.getPlot();
                            plot.setNoDataMessage("No Data Available");
                            plot.setNoDataMessageFont(font);
                            PdfContentByte cb = pdfWriter.getDirectContent();
                            PdfTemplate tp = cb.createTemplate(width, height);
                            Graphics2D g2 = tp.createGraphics(width, height, new DefaultFontMapper());
                            Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height);
                            chart.draw(g2, r2D, null);
                            g2.dispose();
                            Image img=Image.getInstance(tp);
                            chartTable.addCell(img);
                            doc.add(chartTable);
                            doc.add(new Paragraph(" "));
                        }
                        if (chartDisb != null) {
                            PdfPTable chartTable=new  PdfPTable(1);
                            Plot plotDisb = chartDisb.getPlot();
                            plotDisb.setNoDataMessage("No Data Available");
                            plotDisb.setNoDataMessageFont(font);
                            PdfContentByte cb = pdfWriter.getDirectContent();
                            PdfTemplate tp = cb.createTemplate(width, height);
                            Graphics2D g2 = tp.createGraphics(width, height, new DefaultFontMapper());
                            Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height);
                            chartDisb.draw(g2, r2D, null);
                            g2.dispose();
                            Image img=Image.getInstance(tp);
                            chartTable.addCell(img);
                            doc.add(chartTable);
                            doc.add(new Paragraph(" "));

                        }
                        
                        if (chartSecondaryScheme != null) {
                            PdfPTable chartTable=new  PdfPTable(1);
                            Plot plot = chartSecondaryScheme.getPlot();
                            plot.setNoDataMessage("No Data Available");
                            plot.setNoDataMessageFont(font);
                            PdfContentByte cb = pdfWriter.getDirectContent();
                            PdfTemplate tp = cb.createTemplate(width, height);
                            Graphics2D g2 = tp.createGraphics(width, height, new DefaultFontMapper());
                            Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height);
                            chartSecondaryScheme.draw(g2, r2D, null);
                            g2.dispose();
                            Image img=Image.getInstance(tp);
                            chartTable.addCell(img);
                            doc.add(chartTable);
                            doc.add(new Paragraph(" "));
                        }

                        if (chartDisbSecondaryScheme != null) {
                            PdfPTable chartTable=new  PdfPTable(1);
                            Plot plot = chartDisbSecondaryScheme.getPlot();
                            plot.setNoDataMessage("No Data Available");
                            plot.setNoDataMessageFont(font);
                            PdfContentByte cb = pdfWriter.getDirectContent();
                            PdfTemplate tp = cb.createTemplate(width, height);
                            Graphics2D g2 = tp.createGraphics(width, height, new DefaultFontMapper());
                            Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height);
                            chartDisbSecondaryScheme.draw(g2, r2D, null);
                            g2.dispose();
                            Image img=Image.getInstance(tp);
                            chartTable.addCell(img);
                            doc.add(chartTable);
                            doc.add(new Paragraph(" "));
                        }

                         if (chartTertiaryScheme != null) {
                            PdfPTable chartTable=new  PdfPTable(1);
                            Plot plot = chartTertiaryScheme.getPlot();
                            plot.setNoDataMessage("No Data Available");
                            plot.setNoDataMessageFont(font);
                            PdfContentByte cb = pdfWriter.getDirectContent();
                            PdfTemplate tp = cb.createTemplate(width, height);
                            Graphics2D g2 = tp.createGraphics(width, height, new DefaultFontMapper());
                            Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height);
                            chartTertiaryScheme.draw(g2, r2D, null);
                            g2.dispose();
                            Image img=Image.getInstance(tp);
                            chartTable.addCell(img);
                            doc.add(chartTable);
                            doc.add(new Paragraph(" "));
                        }

                        if (chartDisbTertiaryScheme != null) {
                            PdfPTable chartTable=new  PdfPTable(1);
                            Plot plot = chartDisbTertiaryScheme.getPlot();
                            plot.setNoDataMessage("No Data Available");
                            plot.setNoDataMessageFont(font);
                            PdfContentByte cb = pdfWriter.getDirectContent();
                            PdfTemplate tp = cb.createTemplate(width, height);
                            Graphics2D g2 = tp.createGraphics(width, height, new DefaultFontMapper());
                            Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height);
                            chartDisbTertiaryScheme.draw(g2, r2D, null);
                            g2.dispose();
                            Image img=Image.getInstance(tp);
                            chartTable.addCell(img);
                            doc.add(chartTable);
                            doc.add(new Paragraph(" "));
                        }
                       
                      
                        if (orgSummaryTbl != null) {
                            doc.add(orgSummaryTbl);
                            doc.add(new Paragraph(" "));
                        }
                        if (orgContactsTbl != null) {
                            doc.add(orgContactsTbl);
                            doc.add(new Paragraph(" "));

                        }
                        if (largetsProjectsTbl != null) {
                            doc.add(largetsProjectsTbl);
                            doc.add(new Paragraph(" "));
                        }
                        if (parisDecTbl != null) {
                            doc.add(parisDecTbl);
                            doc.add(new Paragraph(" "));
                        }
                        if (typeOfAidTbl != null) {
                            doc.add(typeOfAidTbl);
                            doc.add(new Paragraph(" "));
                        }
                        if (odaProfileTbl != null) {
                            doc.add(odaProfileTbl);
                            doc.add(new Paragraph(" "));
                        }

                        if (pledgesCommDisbTbl != null) {
                            doc.add(pledgesCommDisbTbl);
                            doc.add(new Paragraph(" "));
                        }
                        if (sectorTbl != null) {
                            doc.add(sectorTbl);
                            doc.add(new Paragraph(" "));

                        }
                        if (secondarySectorTbl != null) {
                            doc.add(secondarySectorTbl);
                            doc.add(new Paragraph(" "));

                        }
                        if (tertiarySectorTbl != null) {
                            doc.add(tertiarySectorTbl);
                            doc.add(new Paragraph(" "));

                        }
                        if (regionTbl != null) {
                            doc.add(regionTbl);
                            doc.add(new Paragraph(" "));
                        }
                        if (aidPredTable != null) {
                            doc.add(aidPredTable);
                            doc.add(new Paragraph(" "));
                    }
                        
                }
            }
            }
            //   mainLayout.writeSelectedRows(0, -1, 50, 50, writer.getDirectContent());
            doc.close();
            response.setContentLength(baos.size());
            ServletOutputStream out = response.getOutputStream();
            baos.writeTo(out);
            out.flush();
        } catch (Exception ex) {
            logger.error("error", ex);
        }

        return null;
    }
}
