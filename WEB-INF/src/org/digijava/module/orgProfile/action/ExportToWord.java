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
import org.digijava.module.orgProfile.helper.ExportSettingHelper;

public class ExportToWord extends Action {

    private static Logger logger = Logger.getLogger(ExportToWord.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
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
            AmpOrganisation organization = filter.getOrganization();
            String multipleSelected = TranslatorWorker.translateText("Multiple Organizations Selected", langCode, siteId);
            String all = TranslatorWorker.translateText("All", langCode, siteId);

            String orgName = "";
            String orgGroupTpName = "";
            String grpName = "";
            String orgAcronym = "";
            String orgUrl = "";
            String orgBackground = "N/A";
            String orgDesc = "N/A";
            String contactName = "N/A";
            String email = "N/A";
            String contactPhone = "N/A";
            String contactFax = "N/A";

            AmpOrgGroup group = null;
            AmpOrgType orgGroupType = null;
            if (organization != null) {
                group = organization.getOrgGrpId();
                orgGroupType = group.getOrgType();
                orgGroupTpName = orgGroupType.getOrgType();
                grpName = group.getOrgGrpName();
                orgName = organization.getName();
                orgAcronym = organization.getAcronym();
                orgUrl = organization.getOrgUrl();
                contactName = organization.getContactPersonName();
                email = organization.getEmail();
                contactPhone = organization.getPhone();
                contactFax = organization.getFax();
                orgBackground = organization.getOrgBackground();
                orgDesc = organization.getOrgDescription();

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
                    orgName = "N/A";
                    orgAcronym = "N/A";
                    orgUrl = "N/A";

                }
            }
            while (placeIter.hasNext()) {
                AmpDaWidgetPlace place = placeIter.next();
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
                        JFreeChart chart = null;
                        JFreeChart chartDisb = null;
                        Table orgSummaryTbl = null;

                        Table largetsProjectsTbl = null;
                        Table parisDecTbl = null;
                        Table typeOfAidTbl = null;
                        Table odaProfileTbl = null;
                        Table pledgesCommDisbTbl = null;
                        Table sectorTbl = null;
                        Table regionTbl = null;
                        ChartRenderingInfo info = new ChartRenderingInfo();
                        String transTypeName = "";
                        String currName = filter.getCurrName();
                        String amountInThousands = "";
                        String typeOfAid = TranslatorWorker.translateText("TYPE OF AID", langCode, siteId);
                        String odaProfile = TranslatorWorker.translateText("ODA Profile", langCode, siteId);
                        String pledgesCommDisb = TranslatorWorker.translateText("Pledges|Commitments|Disbursements", langCode, siteId);
                        String regionBreakdown = TranslatorWorker.translateText("Regional Breakdown", langCode, siteId);
                        String sectorBreakdown = TranslatorWorker.translateText("Primary Sector(s) Breakdown ", langCode, siteId);

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
                        int colspan = 6;
                        int oneYearColspan = 2;
                        if (filter.getTransactionType() == 2) {
                            colspan = 11;
                            oneYearColspan = 3;
                        }

                        switch (type.intValue()) {
                            case WidgetUtil.ORG_PROFILE_TYPE_OF_AID:
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_DATA_SOURCE_ONLY == typeOfExport) {
                                    typeOfAidTbl = new Table(colspan);
                                    RtfCell typeofAidTitleCell = new RtfCell(new Paragraph(typeOfAid + "(" + transTypeName + "|" + currName + amountInThousands + ")", OrgProfileUtil.HEADERFONT));
                                    typeofAidTitleCell.setColspan(colspan);
                                    typeOfAidTbl.addCell(typeofAidTitleCell);
                                    typeofAidTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    RtfCell tpAidTitleCell = new RtfCell(new Paragraph(typeOfAid, OrgProfileUtil.HEADERFONT));
                                    tpAidTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    typeOfAidTbl.addCell(tpAidTitleCell);
                                    OrgProfileUtil.getDataTable(typeOfAidTbl, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_TYPE_OF_AID);
                                }
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_CHART_ONLY == typeOfExport) {
                                    if (filter.getTransactionType() == 2) {
                                        FilterHelper newFilter = new FilterHelper(filter);
                                        newFilter.setTransactionType(Constants.COMMITMENT);
                                        chart = ChartWidgetUtil.getTypeOfAidOdaProfileChart(opt, newFilter, true);
                                        newFilter.setTransactionType(Constants.DISBURSEMENT);
                                        chartDisb = ChartWidgetUtil.getTypeOfAidOdaProfileChart(opt, newFilter, true);
                                    } else {
                                        chart = ChartWidgetUtil.getTypeOfAidOdaProfileChart(opt, filter, true);
                                    }
                                }
                                break;
                            case WidgetUtil.ORG_PROFILE_PLEDGES_COMM_DISB:
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_DATA_SOURCE_ONLY == typeOfExport) {
                                    pledgesCommDisbTbl = new Table(4);
                                    RtfCell pledgesCommDisbTitleCell = new RtfCell(new Paragraph(pledgesCommDisb + "(" + currName + amountInThousands + ")", OrgProfileUtil.HEADERFONT));
                                    pledgesCommDisbTitleCell.setColspan(4);
                                    pledgesCommDisbTbl.addCell(pledgesCommDisbTitleCell);
                                    pledgesCommDisbTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    RtfCell pldsCommDisbdTitleCell = new RtfCell(new Paragraph(pledgesCommDisb, OrgProfileUtil.HEADERFONT));
                                    pldsCommDisbdTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    pledgesCommDisbTbl.addCell(pldsCommDisbdTitleCell);
                                    OrgProfileUtil.getDataTable(pledgesCommDisbTbl, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_PLEDGES_COMM_DISB);
                                }
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_CHART_ONLY == typeOfExport) {
                                    chart = ChartWidgetUtil.getPledgesCommDisbChart(opt, filter);
                                }
                                break;
                            case WidgetUtil.ORG_PROFILE_ODA_PROFILE:
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_DATA_SOURCE_ONLY == typeOfExport) {
                                    odaProfileTbl = new Table(colspan);
                                    RtfCell odaProfileTitleCell = new RtfCell(new Paragraph(odaProfile + "(" + currName + amountInThousands + ")", OrgProfileUtil.HEADERFONT));
                                    odaProfileTitleCell.setColspan(colspan);
                                    odaProfileTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    odaProfileTbl.addCell(odaProfileTitleCell);
                                    RtfCell odaProfTitleCell = new RtfCell(new Paragraph(odaProfile, OrgProfileUtil.HEADERFONT));
                                    odaProfTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    odaProfileTbl.addCell(odaProfTitleCell);
                                    OrgProfileUtil.getDataTable(odaProfileTbl, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_ODA_PROFILE);
                                }
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_CHART_ONLY == typeOfExport) {
                                    if (filter.getTransactionType() == 2) {
                                        FilterHelper newFilter = new FilterHelper(filter);
                                        newFilter.setTransactionType(Constants.COMMITMENT);
                                        chart = ChartWidgetUtil.getTypeOfAidOdaProfileChart(opt, newFilter, false);
                                        newFilter.setTransactionType(Constants.DISBURSEMENT);
                                        chartDisb = ChartWidgetUtil.getTypeOfAidOdaProfileChart(opt, newFilter, false);
                                    } else {
                                        chart = ChartWidgetUtil.getTypeOfAidOdaProfileChart(opt, filter, false);
                                    }
                                }
                                break;
                            case WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN:
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_DATA_SOURCE_ONLY == typeOfExport) {
                                    sectorTbl = new Table(oneYearColspan);
                                    RtfCell sectorTitleCell = new RtfCell(new Paragraph(sectorBreakdown + "(" + currName + amountInThousands + ")", OrgProfileUtil.HEADERFONT));
                                    sectorTitleCell.setColspan(oneYearColspan);
                                    sectorTbl.addCell(sectorTitleCell);
                                    sectorTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    RtfCell sectorNameTitleCell = new RtfCell(new Paragraph(sectorBreakdown, OrgProfileUtil.HEADERFONT));
                                    sectorNameTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    sectorTbl.addCell(sectorNameTitleCell);
                                    OrgProfileUtil.getDataTable(sectorTbl, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN);
                                }
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_CHART_ONLY == typeOfExport) {
                                    if (filter.getTransactionType() == 2) {
                                        FilterHelper newFilter = new FilterHelper(filter);
                                        newFilter.setTransactionType(Constants.COMMITMENT);
                                        chart = ChartWidgetUtil.getSectorByDonorChart(opt, newFilter);
                                        newFilter.setTransactionType(Constants.DISBURSEMENT);
                                        chartDisb = ChartWidgetUtil.getSectorByDonorChart(opt, newFilter);

                                    } else {
                                        chart = ChartWidgetUtil.getSectorByDonorChart(opt, filter);
                                    }
                                }
                                break;
                            case WidgetUtil.ORG_PROFILE_REGIONAL_BREAKDOWN:
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_DATA_SOURCE_ONLY == typeOfExport) {
                                    regionTbl = new Table(oneYearColspan);
                                    RtfCell regionTitleCell = new RtfCell(new Paragraph(regionBreakdown + "(" + currName + amountInThousands + ")", OrgProfileUtil.HEADERFONT));
                                    regionTitleCell.setColspan(oneYearColspan);
                                    regionTbl.addCell(regionTitleCell);
                                    regionTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    RtfCell regionNameTitleCell = new RtfCell(new Paragraph(regionBreakdown, OrgProfileUtil.HEADERFONT));
                                    regionNameTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    regionTbl.addCell(regionNameTitleCell);
                                    OrgProfileUtil.getDataTable(regionTbl, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_REGIONAL_BREAKDOWN);
                                }
                                if (typeOfExport == Constants.EXPORT_OPTION_CHART_DATA_SOURCE || Constants.EXPORT_OPTION_CHART_ONLY == typeOfExport) {
                                    if (filter.getTransactionType() == 2) {
                                        FilterHelper newFilter = new FilterHelper(filter);
                                        newFilter.setTransactionType(Constants.COMMITMENT);
                                        chart = ChartWidgetUtil.getRegionByDonorChart(opt, newFilter);
                                        newFilter.setTransactionType(Constants.DISBURSEMENT);
                                        chartDisb = ChartWidgetUtil.getRegionByDonorChart(opt, newFilter);
                                    } else {
                                        chart = ChartWidgetUtil.getRegionByDonorChart(opt, filter);
                                    }
                                }

                                break;
                            case WidgetUtil.ORG_PROFILE_SUMMARY:

                                if (Constants.EXPORT_OPTION_CHART_DATA_SOURCE == typeOfExport) {
                                    //create summary table

                                    orgSummaryTbl = new Table(2);
                                    RtfCell summaryTitleCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Organization Profile", langCode, siteId), OrgProfileUtil.HEADERFONT));
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
                                    orgCell.addElement(new Paragraph(orgName, OrgProfileUtil.PLAINFONT));
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

                                    RtfCell orgWbLinkTitleCell = new RtfCell();
                                    orgWbLinkTitleCell.addElement(new Paragraph(TranslatorWorker.translateText("Web Link", langCode, siteId) + ":", OrgProfileUtil.PLAINFONT));
                                    orgSummaryTbl.addCell(orgWbLinkTitleCell);

                                    RtfCell orgWbLinkCell = new RtfCell();
                                    orgWbLinkCell.addElement(new Paragraph(orgUrl, OrgProfileUtil.PLAINFONT));
                                    orgSummaryTbl.addCell(orgWbLinkCell);

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






                                    //create largest projects table
                                    int largestColspan = 3;
                                    String titlePart = TranslatorWorker.translateText("Largest projects", langCode, siteId);
                                    int projectNumber = filter.getLargestProjectNumb();
                                    if (projectNumber == -1) {
                                        titlePart = TranslatorWorker.translateText("All", langCode, siteId) + " " + titlePart;
                                    } else {
                                        titlePart = projectNumber + " " + titlePart;
                                    }
                                    RtfCell largestProjectsTitle = new RtfCell(new Paragraph(titlePart + " (" + (filter.getYear() - 1) + ")", OrgProfileUtil.HEADERFONT));
                                    largestProjectsTitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    if (filter.getTransactionType() == 2) { // // we are showing disb only when  comm&disb is selected
                                        largestColspan = 4;
                                    }
                                    largetsProjectsTbl = new Table(largestColspan);
                                    largestProjectsTitle.setColspan(largestColspan);
                                    largestProjectsTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    largetsProjectsTbl.addCell(largestProjectsTitle);

                                    RtfCell largestProjectsProjecttitle = new RtfCell(new Paragraph(TranslatorWorker.translateText("Project title", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                    largestProjectsProjecttitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    largestProjectsProjecttitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    largetsProjectsTbl.addCell(largestProjectsProjecttitle);

                                    RtfCell largestProjectsCommitmentTitle = new RtfCell(new Paragraph(TranslatorWorker.translateText("Commitment", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                    largestProjectsCommitmentTitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    largestProjectsCommitmentTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    largetsProjectsTbl.addCell(largestProjectsCommitmentTitle);

                                    if (filter.getTransactionType() == 2) { // // we are showing disb only when  comm&disb is selected
                                        RtfCell largestProjectsDisbursemenTitle = new RtfCell(new Paragraph(TranslatorWorker.translateText("Disbursemen", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                        largestProjectsDisbursemenTitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                        largestProjectsDisbursemenTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        largetsProjectsTbl.addCell(largestProjectsDisbursemenTitle);
                                    }

                                    RtfCell largestProjectsSectorTitle = new RtfCell(new Paragraph(TranslatorWorker.translateText("Sector", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                    largestProjectsSectorTitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    largestProjectsSectorTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    largetsProjectsTbl.addCell(largestProjectsSectorTitle);
                                    List<Project> projects = OrgProfileUtil.getOrganisationLargestProjects(filter);
                                    Iterator<Project> projectIter = projects.iterator();
                                    int count = 0;
                                    while (projectIter.hasNext()) {
                                        Project project = projectIter.next();
                                        String fullTitle = (project.getFullTitle() == null) ? project.getTitle() : project.getFullTitle();
                                        RtfCell title = new RtfCell(new Paragraph(fullTitle, OrgProfileUtil.PLAINFONT));
                                        RtfCell amount = new RtfCell(new Paragraph(project.getAmount(), OrgProfileUtil.PLAINFONT));
                                        RtfCell disbAmount = null;
                                        if (filter.getTransactionType() == 2) { // // we are showing disb only when  comm&disb is selected
                                            disbAmount = new RtfCell(new Paragraph(project.getDisbAmount(), OrgProfileUtil.PLAINFONT));
                                        }
                                        RtfCell sectorsCell = new RtfCell(new Paragraph(project.getSectorNames(), OrgProfileUtil.PLAINFONT));
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


                                    RtfCell parisDecTitle = new RtfCell(new Paragraph(TranslatorWorker.translateText("PARIS DECLARATION INDICATORS - DONORS", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                    parisDecTitle.setColspan(2);
                                    parisDecTitle.setRowspan(2);
                                    parisDecTitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    RtfCell allDonorsCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("All Donors ", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                    allDonorsCell.setColspan(3);
                                    allDonorsCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    RtfCell selectedOrgCell = new RtfCell(new Paragraph(orgName, OrgProfileUtil.HEADERFONT));
                                    selectedOrgCell.setColspan(2);
                                    selectedOrgCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    RtfCell baseline = new RtfCell(new Paragraph(2005 + TranslatorWorker.translateText(" Baseline ", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                    RtfCell value = new RtfCell(new Paragraph(filter.getYear() - 1 + TranslatorWorker.translateText(" Value ", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                    RtfCell target = new RtfCell(new Paragraph(2010 + TranslatorWorker.translateText(" Target ", langCode, siteId), OrgProfileUtil.HEADERFONT));
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
                                        ParisIndicatorHelper piHelper = new ParisIndicatorHelper(piIndicator, filter, true);
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

                                            ParisIndicatorHelper piInd5aHelper = new ParisIndicatorHelper(ind5aii, filter, true);
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

                                            ParisIndicatorHelper piInd5bHelper = new ParisIndicatorHelper(ind5bii, filter, true);
                                            RtfCell indicator5bAllBaseline = new RtfCell(new Paragraph(piInd5bHelper.getAllDonorBaseLineValue() + sufix, OrgProfileUtil.PLAINFONT));
                                            RtfCell indicator5bAllCurrentValue = new RtfCell(new Paragraph(piInd5bHelper.getAllCurrentValue() + sufix, OrgProfileUtil.PLAINFONT));
                                            RtfCell indicator5bAllTargetValue = new RtfCell(new Paragraph(piInd5bHelper.getAllTargetValue() + sufix, OrgProfileUtil.PLAINFONT));
                                            RtfCell indicator5bOrgBaseline = new RtfCell(new Paragraph(piInd5bHelper.getOrgBaseLineValue() + sufix, OrgProfileUtil.PLAINFONT));
                                            RtfCell indicator5bOrgCurrentValue = new RtfCell(new Paragraph(piInd5bHelper.getOrgValue() + sufix, OrgProfileUtil.PLAINFONT));

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
                        if (chartDisb != null) {
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
                            doc.add(imgDisb);
                        }
                        if (chart != null) {
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
                            doc.add(img);
                        }
                        if (orgSummaryTbl != null) {
                            doc.add(orgSummaryTbl);
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
                        if (regionTbl != null) {
                            doc.add(regionTbl);
                        }
                    }
                }
            }
            String footerText = OrgProfileUtil.getFooterText(langCode, siteId, filter);
            HeaderFooter footer = new HeaderFooter(new Phrase(footerText), false);
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
