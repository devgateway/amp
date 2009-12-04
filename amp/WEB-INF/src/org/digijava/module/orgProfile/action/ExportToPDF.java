package org.digijava.module.orgProfile.action;

import com.lowagie.text.Element;
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
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Font;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
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

public class ExportToPDF extends Action {

    private static Logger logger = Logger.getLogger(ExportToPDF.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/pdf");
        com.lowagie.text.Document doc = new com.lowagie.text.Document(PageSize.A4.rotate());
        Long siteId = RequestUtils.getSiteDomain(request).getSite().getId();
        String langCode = RequestUtils.getNavigationLanguage(request).getCode();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(doc, baos);
            List<AmpDaWidgetPlace> orgPlaces = WidgetUtil.getAllOrgProfilePlaces();
            Iterator<AmpDaWidgetPlace> placeIter = orgPlaces.iterator();
            doc.open();
            PdfPTable mainLayout = new PdfPTable(2);
            mainLayout.setWidthPercentage(100);
            HttpSession session = request.getSession();
            FilterHelper filter = (FilterHelper) session.getAttribute("orgProfileFilter");
            AmpOrganisation organization = filter.getOrganization();
            String multipleSelected = TranslatorWorker.translateText("Multiple Organizations Selected", langCode, siteId);
            String all = TranslatorWorker.translateText("All", langCode, siteId);

            String orgName = "";
            String orgGroupTpName = "";
            String grpName = "";
            String orgAcronym = "";
            String orgUrl = "";

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
            } else {
                if (filter.getOrgIds() != null) {
                    orgGroupTpName = multipleSelected;
                    grpName = multipleSelected;
                    orgName = multipleSelected;
                    orgAcronym = multipleSelected;
                    orgUrl = multipleSelected;
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
                        opt.setWidth(380);
                        opt.setHeight(350);
                        opt.setSiteId(siteId);
                        opt.setLangCode(langCode);
                        JFreeChart chart = null;
                        PdfPTable orgSummaryTbl = null;
                        PdfPTable orgContactsTbl = null;
                        PdfPTable largetsProjectsTbl = null;
                        PdfPTable parisDecTbl = null;
                        PdfPTable typeOfAidTbl = null;
                        PdfPTable odaProfileTbl = null;
                        ChartRenderingInfo info = new ChartRenderingInfo();


                        String transTypeName = "";
                        String currName = filter.getCurrName();
                        String amountInThousands = "";
                        String typeOfAid = TranslatorWorker.translateText("TYPE OF AID", langCode, siteId);
                        String odaProfile = TranslatorWorker.translateText("ODA Profile", langCode, siteId);
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
                        switch (type.intValue()) {
                            case WidgetUtil.ORG_PROFILE_TYPE_OF_AID:

                                // type of aid table
                                typeOfAidTbl = new PdfPTable(6);
                                typeOfAidTbl.setWidthPercentage(100);
                                PdfPCell typeofAidTitleCell = new PdfPCell(new Paragraph(typeOfAid + "(" + transTypeName + "|" + currName + amountInThousands + ")", OrgProfileUtil.HEADERFONT));
                                typeofAidTitleCell.setColspan(6);
                                typeofAidTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                typeOfAidTbl.addCell(typeofAidTitleCell);
                                PdfPCell tpAidTitleCell = new PdfPCell(new Paragraph(typeOfAid, OrgProfileUtil.HEADERFONT));
                                tpAidTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                typeOfAidTbl.addCell(tpAidTitleCell);
                                OrgProfileUtil.getDataTable(typeOfAidTbl, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_TYPE_OF_AID);
                                break;
                            case WidgetUtil.ORG_PROFILE_PLEDGES_COMM_DISB:
                                chart = ChartWidgetUtil.getPledgesCommDisbChart(opt, filter);
                                break;
                            case WidgetUtil.ORG_PROFILE_ODA_PROFILE:
                                odaProfileTbl = new PdfPTable(6);
                                odaProfileTbl.setWidthPercentage(100);
                                PdfPCell odaProfileTitleCell = new PdfPCell(new Paragraph(odaProfile + "(" + transTypeName + "|" + currName + amountInThousands + ")", OrgProfileUtil.HEADERFONT));
                                odaProfileTitleCell.setColspan(6);
                                odaProfileTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                odaProfileTbl.addCell(odaProfileTitleCell);
                                PdfPCell odaProfTitleCell = new PdfPCell(new Paragraph(odaProfile, OrgProfileUtil.HEADERFONT));
                                odaProfTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                odaProfileTbl.addCell(odaProfTitleCell);
                                OrgProfileUtil.getDataTable(odaProfileTbl, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_ODA_PROFILE);
                                break;
                            case WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN:
                                chart = ChartWidgetUtil.getSectorByDonorChart(opt, filter);
                                break;
                            case WidgetUtil.ORG_PROFILE_REGIONAL_BREAKDOWN:
                                chart = ChartWidgetUtil.getRegionByDonorChart(opt, filter);
                                break;
                            case WidgetUtil.ORG_PROFILE_SUMMARY:


                                //create summary table

                                orgSummaryTbl = new PdfPTable(2);
                                orgSummaryTbl.setWidthPercentage(100);
                                PdfPCell summaryTitleCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Organization Profile", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                summaryTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
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
                                orgTitleCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                orgSummaryTbl.addCell(orgTitleCell);

                                PdfPCell orgCell = new PdfPCell();
                                orgCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
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
                                orgDnGrpTitleCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                orgSummaryTbl.addCell(orgDnGrpTitleCell);

                                PdfPCell orgDnGrpCell = new PdfPCell();
                                orgDnGrpCell.addElement(new Paragraph(grpName, OrgProfileUtil.PLAINFONT));
                                orgDnGrpCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                orgSummaryTbl.addCell(orgDnGrpCell);

                                PdfPCell orgWbLinkTitleCell = new PdfPCell();
                                orgWbLinkTitleCell.addElement(new Paragraph(TranslatorWorker.translateText("Web Link", langCode, siteId) + ":", OrgProfileUtil.PLAINFONT));
                                orgSummaryTbl.addCell(orgWbLinkTitleCell);

                                PdfPCell orgWbLinkCell = new PdfPCell();
                                orgWbLinkCell.addElement(new Paragraph(orgUrl, OrgProfileUtil.PLAINFONT));
                                orgSummaryTbl.addCell(orgWbLinkCell);



                                //create contacts table
                                int count = 0;
                                if (organization != null) {
                                    orgContactsTbl = new PdfPTable(6);
                                    orgContactsTbl.setWidthPercentage(100);

                                    PdfPCell contactHeaderCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Contact Information", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                    contactHeaderCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    contactHeaderCell.setColspan(6);
                                    contactHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    orgContactsTbl.addCell(contactHeaderCell);

                                    PdfPCell contactLastNameCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Last Name", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                    contactLastNameCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    contactLastNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    orgContactsTbl.addCell(contactLastNameCell);

                                    PdfPCell contactNameCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("First name", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                    contactNameCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    contactNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    orgContactsTbl.addCell(contactNameCell);

                                    PdfPCell contactEmailCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Email", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                    contactEmailCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    contactEmailCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    orgContactsTbl.addCell(contactEmailCell);

                                    PdfPCell contactPhoneCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Telephone", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                    contactPhoneCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    contactPhoneCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    orgContactsTbl.addCell(contactPhoneCell);

                                    PdfPCell contactFaxCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Fax", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                    contactFaxCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    contactFaxCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    orgContactsTbl.addCell(contactFaxCell);

                                    PdfPCell contactTitleCell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Title", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                    contactTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                    contactTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    orgContactsTbl.addCell(contactTitleCell);
                                    if (organization.getContacts() != null) {
                                        List<AmpContact> contacts = new ArrayList(organization.getContacts());
                                        Iterator<AmpContact> contactsIter = contacts.iterator();
                                        while (contactsIter.hasNext()) {
                                            AmpContact contact = contactsIter.next();
                                            PdfPCell name = new PdfPCell(new Paragraph(contact.getLastname(), OrgProfileUtil.PLAINFONT));
                                            PdfPCell lastName = new PdfPCell(new Paragraph(contact.getName(), OrgProfileUtil.PLAINFONT));
//                                    PdfPCell email = new PdfPCell(new Paragraph(contact.getEmail(),OrgProfileUtil.PLAINFONT));
//                                    PdfPCell phone = new PdfPCell(new Paragraph(contact.getPhone(),OrgProfileUtil.PLAINFONT));
//                                    PdfPCell fax = new PdfPCell(new Paragraph(contact.getFax(),OrgProfileUtil.PLAINFONT));
                                            String contacTitle = "";
                                            if (contact.getTitle() != null) {
                                                contacTitle = contact.getTitle().getValue();
                                            }
                                            PdfPCell title = new PdfPCell(new Paragraph(contacTitle, OrgProfileUtil.PLAINFONT));
                                            if (count % 2 == 0) {
                                                title.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
//                                        fax.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
//                                        phone.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
//                                        email.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                                lastName.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                                name.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            }
                                            orgContactsTbl.addCell(lastName);
                                            orgContactsTbl.addCell(name);
//                                    orgContactsTbl.addCell(email);
//                                    orgContactsTbl.addCell(phone);
//                                    orgContactsTbl.addCell(fax);
                                            orgContactsTbl.addCell(title);
                                            count++;

                                        }


                                    }
                                } else {
                                    PdfPCell contactTitleCell = new PdfPCell();
                                    contactTitleCell.addElement(new Paragraph(TranslatorWorker.translateText("Contact", langCode, siteId) + ":", OrgProfileUtil.PLAINFONT));
                                    contactTitleCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                    orgSummaryTbl.addCell(contactTitleCell);
                                    PdfPCell contactCell = new PdfPCell();
                                    if (filter.getOrgIds() != null && filter.getOrgIds().length > 1) {
                                        contactCell.addElement(new Paragraph(multipleSelected, OrgProfileUtil.PLAINFONT));
                                    } else {
                                        contactCell.addElement(new Paragraph("N/A", OrgProfileUtil.PLAINFONT));
                                    }
                                    contactCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                    orgSummaryTbl.addCell(contactCell);
                                }


                                //create largest projects table
                                largetsProjectsTbl = new PdfPTable(new float[]{25, 20, 60});
                                largetsProjectsTbl.setWidthPercentage(100);
                                PdfPCell largestProjectsTitle = new PdfPCell(new Paragraph(TranslatorWorker.translateText("5 Largest projects", langCode, siteId) + " (" + (filter.getYear() - 1) + ")", OrgProfileUtil.HEADERFONT));
                                largestProjectsTitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                largestProjectsTitle.setColspan(3);
                                largestProjectsTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                largetsProjectsTbl.addCell(largestProjectsTitle);

                                PdfPCell largestProjectsProjecttitle = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Project title", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                largestProjectsProjecttitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                largestProjectsProjecttitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                largetsProjectsTbl.addCell(largestProjectsProjecttitle);

                                PdfPCell largestProjectsCommitmentTitle = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Commitment", langCode, siteId) + "(" + filter.getCurrName() + ")", OrgProfileUtil.HEADERFONT));
                                largestProjectsCommitmentTitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                largestProjectsCommitmentTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                largetsProjectsTbl.addCell(largestProjectsCommitmentTitle);

                                PdfPCell largestProjectsSectorTitle = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Sector", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                largestProjectsSectorTitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
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
                                    PdfPCell sectorsCell = new PdfPCell(new Paragraph(project.getSectorNames(), OrgProfileUtil.PLAINFONT));
                                    if (count % 2 == 0) {
                                        title.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        amount.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        sectorsCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                    }

                                    largetsProjectsTbl.addCell(title);
                                    largetsProjectsTbl.addCell(amount);
                                    largetsProjectsTbl.addCell(sectorsCell);
                                    count++;

                                }
                                break;

                            case WidgetUtil.ORG_PROFILE_PARIS_DECLARATION:
                                // creating Paris declaration table

                                /*  creating heading */
                                float widths[] = new float[]{10f, 40f, 10f, 10f, 10f, 10f, 10f};

                                parisDecTbl = new PdfPTable(widths.length);
                                parisDecTbl.setWidths(widths);

                                parisDecTbl.setWidthPercentage(100);
                                PdfPCell parisDecTitle = new PdfPCell(new Paragraph(TranslatorWorker.translateText("PARIS DECLARATION INDICATORS - DONORS", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                parisDecTitle.setColspan(2);
                                parisDecTitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                parisDecTbl.addCell(parisDecTitle);
                                PdfPCell allDonorsCell = new PdfPCell();
                                allDonorsCell.setColspan(3);
                                PdfPCell selectedOrgCell = new PdfPCell();
                                selectedOrgCell.setColspan(2);
                                PdfPTable allDonorsTbl = new PdfPTable(3);
                                PdfPTable selectedOrgTbl = new PdfPTable(2);

                                PdfPCell allDonorsTblTitle = new PdfPCell(new Paragraph(TranslatorWorker.translateText("All Donors ", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                allDonorsTblTitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                allDonorsTblTitle.setColspan(3);
                                allDonorsTblTitle.setFixedHeight(30);


                                allDonorsTbl.addCell(allDonorsTblTitle);
                                PdfPCell baseline = new PdfPCell(new Paragraph(2005 + TranslatorWorker.translateText(" Baseline ", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                PdfPCell value = new PdfPCell(new Paragraph(filter.getYear() - 1 + TranslatorWorker.translateText(" Value ", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                PdfPCell target = new PdfPCell(new Paragraph(2010 + TranslatorWorker.translateText(" Target ", langCode, siteId), OrgProfileUtil.HEADERFONT));
                                baseline.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                value.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                target.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                baseline.setBorderColor(OrgProfileUtil.BORDERCOLOR);
                                value.setBorderColor(OrgProfileUtil.BORDERCOLOR);
                                target.setBorderColor(OrgProfileUtil.BORDERCOLOR);

                                allDonorsTbl.addCell(baseline);
                                allDonorsTbl.addCell(value);
                                allDonorsTbl.addCell(target);
                                allDonorsTbl.setWidthPercentage(100);
                                allDonorsCell.addElement(allDonorsTbl);
                                parisDecTbl.addCell(allDonorsCell);


                                PdfPCell selectedOrgTblTitle = new PdfPCell(new Paragraph(orgName, OrgProfileUtil.HEADERFONT));
                                selectedOrgTblTitle.setColspan(2);
                                selectedOrgTblTitle.setFixedHeight(30);
                                selectedOrgTblTitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
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
                                count = 0;
                                while (iter.hasNext()) {
                                    AmpAhsurveyIndicator piIndicator = iter.next();
                                    if (piIndicator.getIndicatorCode().equals("10b") || piIndicator.getIndicatorCode().equals("8")) {
                                        continue;
                                    }
                                    ParisIndicatorHelper piHelper = new ParisIndicatorHelper(piIndicator, filter, true);
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
                                    if (count % 2 == 1) {
                                        indicatorCode.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        indicatorName.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        indicatorAllBaseline.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        indicatorAllCurrentValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        indicatorAllTargetValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        indicatorOrgBaseline.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        indicatorOrgCurrentValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
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
                                        PdfPCell indicator5aCode = new PdfPCell(new Paragraph("5aii", OrgProfileUtil.PLAINFONT));
                                        PdfPCell indicator5aName = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Number of donors using country PFM", langCode, siteId), OrgProfileUtil.PLAINFONT));

                                        ParisIndicatorHelper piInd5aHelper = new ParisIndicatorHelper(ind5aii, filter, true);
                                        PdfPCell indicator5aAllBaseline = new PdfPCell(new Paragraph(piInd5aHelper.getAllDonorBaseLineValue() + " ", OrgProfileUtil.PLAINFONT));
                                        PdfPCell indicatorAll5aCurrentValue = new PdfPCell(new Paragraph(piInd5aHelper.getAllCurrentValue() + " ", OrgProfileUtil.PLAINFONT));
                                        PdfPCell indicatorAll5aTargetValue = new PdfPCell(new Paragraph(piInd5aHelper.getAllTargetValue() + " ", OrgProfileUtil.PLAINFONT));
                                        PdfPCell indicatorOrg5aBaseline = new PdfPCell(new Paragraph(piInd5aHelper.getOrgBaseLineValue() + " ", OrgProfileUtil.PLAINFONT));
                                        PdfPCell indicatorOrg5aCurrentValue = new PdfPCell(new Paragraph(piInd5aHelper.getOrgValue() + " ", OrgProfileUtil.PLAINFONT));

                                        if (count % 2 == 1) {
                                            indicator5aCode.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicator5aName.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicator5aAllBaseline.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicatorAll5aCurrentValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicatorAll5aTargetValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicatorOrg5aBaseline.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicatorOrg5aCurrentValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
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

                                        PdfPCell indicator5bCode = new PdfPCell(new Paragraph("5aii", OrgProfileUtil.PLAINFONT));
                                        PdfPCell indicator5bName = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Number of donors using country procurement system", langCode, siteId), OrgProfileUtil.PLAINFONT));
                                        ParisIndicatorHelper piInd5bHelper = new ParisIndicatorHelper(ind5bii, filter, true);
                                        PdfPCell indicator5bAllBaseline = new PdfPCell(new Paragraph(piInd5bHelper.getAllDonorBaseLineValue() + sufix));
                                        PdfPCell indicator5bAllCurrentValue = new PdfPCell(new Paragraph(piInd5bHelper.getAllCurrentValue() + sufix));
                                        PdfPCell indicator5bAllTargetValue = new PdfPCell(new Paragraph(piInd5bHelper.getAllTargetValue() + sufix));
                                        PdfPCell indicator5bOrgBaseline = new PdfPCell(new Paragraph(piInd5bHelper.getOrgBaseLineValue() + sufix));
                                        PdfPCell indicator5bOrgCurrentValue = new PdfPCell(new Paragraph(piInd5bHelper.getOrgValue() + sufix));

                                        if (count % 2 == 1) {
                                            indicator5bCode.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicator5bName.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicator5bAllBaseline.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicator5bAllCurrentValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicator5bAllTargetValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicator5bOrgBaseline.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicator5bOrgCurrentValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
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


                                //--end of creating content--//






                                break;



                        }
                        if (chart != null) {
                            Plot plot = chart.getPlot();
                            plot.setNoDataMessage("No Data Available");
                            Font font = new Font(null, 0, 24);
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
                            img.setAlignment(Image.ALIGN_MIDDLE);
                            PdfPCell chartCell = new PdfPCell(img);
                            chartCell.setPadding(4);
                            chartCell.setBorder(PdfPCell.NO_BORDER);
                            mainLayout.addCell(chartCell);
                        } else {
                            PdfPCell tableCell = new PdfPCell();
                            tableCell.setBorder(PdfPCell.NO_BORDER);
                            if (orgSummaryTbl != null) {
                                tableCell.addElement(orgSummaryTbl);
                                tableCell.addElement(new Paragraph("   "));
                                tableCell.addElement(orgContactsTbl);
                                tableCell.addElement(new Paragraph("   "));
                                tableCell.addElement(largetsProjectsTbl);
                            }
                            if (parisDecTbl != null) {
                                tableCell.setColspan(2);
                                tableCell.addElement(parisDecTbl);


                            }
                            if (typeOfAidTbl != null) {
                                tableCell.addElement(typeOfAidTbl);
                            }
                            if (odaProfileTbl != null) {
                                tableCell.addElement(odaProfileTbl);
                            }
                            mainLayout.addCell(tableCell);
                        }

                    } else {
                        PdfPCell cell = new PdfPCell();
                        cell.setBorder(PdfPCell.NO_BORDER);
                        mainLayout.addCell(cell);
                    }
                } else {
                    PdfPCell cell = new PdfPCell();
                    cell.setBorder(PdfPCell.NO_BORDER);
                    mainLayout.addCell(cell);
                }
            }
            mainLayout.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            //empty cell
            mainLayout.addCell("");
            //   mainLayout.writeSelectedRows(0, -1, 50, 50, writer.getDirectContent());
            doc.add(mainLayout);

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
