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
import java.awt.Color;
import java.awt.Font;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
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

public class ExportToPDF extends Action {

    private static Logger logger = Logger.getLogger(ExportToPDF.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/pdf");
        com.lowagie.text.Document doc = new com.lowagie.text.Document(PageSize.A4.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(doc, baos);
            List<AmpDaWidgetPlace> orgPlaces = WidgetUtil.getAllOrgProfilePlaces();
            Iterator<AmpDaWidgetPlace> placeIter = orgPlaces.iterator();
            doc.open();
            PdfPTable mainLayout = new PdfPTable(2);
            com.lowagie.text.Font pageTitleFont = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 24,com.lowagie.text.Font.BOLD);
            PdfPCell pageTitleCell=new PdfPCell(new Paragraph("Org. Profile \n\n", pageTitleFont));
            pageTitleCell.setColspan(2);
            pageTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pageTitleCell.setBorder(PdfPCell.NO_BORDER);
            mainLayout.addCell(pageTitleCell);
            mainLayout.setWidthPercentage(100);
           
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
                        HttpSession session = request.getSession();
                        FilterHelper filter = (FilterHelper) session.getAttribute("orgProfileFilter");
                        ChartOption opt = new ChartOption();
                        opt.setWidth(350);
                        opt.setHeight(420);
                        Long siteId = RequestUtils.getSiteDomain(request).getSite().getId();
                        opt.setSiteId(siteId);
                        String langCode = RequestUtils.getNavigationLanguage(request).getCode();
                        opt.setLangCode(langCode);
                        JFreeChart chart = null;
                        PdfPTable orgSummaryTbl =null;
                        PdfPTable largetsProjectsTbl =null;
                        PdfPTable parisDecTbl =null;
                        ChartRenderingInfo info = new ChartRenderingInfo();
                        com.lowagie.text.Font plainFont = new com.lowagie.text.Font(com.lowagie.text.Font.COURIER, 11);
                        com.lowagie.text.Font headerFont = new com.lowagie.text.Font(com.lowagie.text.Font.COURIER, 11, Font.BOLD, new Color(255, 255, 255));
                        Color titleColor = new Color(34, 46, 93);
                        Color borderColor = new Color(255, 255, 255);
                        Color cellColor=new Color(219, 229, 241);
                        switch (type.intValue()) {
                            case WidgetUtil.ORG_PROFILE_TYPE_OF_AID:
                                chart = ChartWidgetUtil.getTypeOfAidChart(opt, filter);
                                break;
                            case WidgetUtil.ORG_PROFILE_PLEDGES_COMM_DISB:
                                chart = ChartWidgetUtil.getPledgesCommDisbChart(opt, filter);
                                break;
                            case WidgetUtil.ORG_PROFILE_ODA_PROFILE:
                                chart = ChartWidgetUtil.getODAProfileChart(opt, filter);
                                break;
                            case WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN:
                                chart = ChartWidgetUtil.getSectorByDonorChart(opt, filter);
                                break;
                            case WidgetUtil.ORG_PROFILE_REGIONAL_BREAKDOWN:
                                chart = ChartWidgetUtil.getRegionByDonorChart(opt, filter);
                                break;
                            case WidgetUtil.ORG_PROFILE_SUMMARY:
                              

                                 //create summary table
                                
                                orgSummaryTbl=new PdfPTable(2);
                                orgSummaryTbl.setWidthPercentage(100);
                                PdfPCell summaryTitleCell = new PdfPCell(new Paragraph("Summary",headerFont));
                                summaryTitleCell.setBackgroundColor(titleColor);
                                summaryTitleCell.setColspan(2);
                                summaryTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                orgSummaryTbl.addCell(summaryTitleCell);

                                PdfPCell grTitleCell = new PdfPCell();
                                grTitleCell.addElement(new Paragraph("Group:",plainFont));
                                grTitleCell.setBackgroundColor(cellColor);
                                orgSummaryTbl.addCell(grTitleCell);

                                PdfPCell grCell = new PdfPCell();
                                grCell.setBackgroundColor(cellColor);
                                grCell.addElement(new Paragraph(filter.getOrgGroup().getOrgGrpName(),plainFont));
                                orgSummaryTbl.addCell(grCell);

                                PdfPCell grTypeTitleCell = new PdfPCell();
                                grTypeTitleCell.addElement(new Paragraph("Type:",plainFont));
                                orgSummaryTbl.addCell(grTypeTitleCell);

                                PdfPCell grTypeCell = new PdfPCell();
                                AmpOrgType orgGroupType=filter.getOrgGroup().getOrgType();
                                String orgGroupTpName="";
                                if(orgGroupType!=null){
                                    orgGroupTpName=orgGroupType.getOrgType();
                                }
                                grTypeCell.addElement(new Paragraph(orgGroupTpName,plainFont));
                                orgSummaryTbl.addCell(grTypeCell);

                                PdfPCell orgTitleCell = new PdfPCell();
                                orgTitleCell.addElement(new Paragraph("Organization Name:",plainFont));
                                orgTitleCell.setBackgroundColor(cellColor);
                                orgSummaryTbl.addCell(orgTitleCell);

                                PdfPCell orgCell = new PdfPCell();
                                orgCell.setBackgroundColor(cellColor);
                                orgCell.addElement(new Paragraph(filter.getOrganization().getName(),plainFont));
                                orgSummaryTbl.addCell(orgCell);

                                PdfPCell orgAcrTitleCell = new PdfPCell();
                                orgAcrTitleCell.addElement(new Paragraph("Organization Acronym:",plainFont));
                                orgSummaryTbl.addCell(orgAcrTitleCell);

                                PdfPCell orgAcrCell = new PdfPCell();
                                orgAcrCell.addElement(new Paragraph(filter.getOrganization().getAcronym(),plainFont));
                                orgSummaryTbl.addCell(orgAcrCell);


                                PdfPCell orgDnGrpTitleCell = new PdfPCell();
                                orgDnGrpTitleCell.addElement(new Paragraph("Donor Group:",plainFont));
                                orgDnGrpTitleCell.setBackgroundColor(cellColor);
                                orgSummaryTbl.addCell(orgDnGrpTitleCell);

                                PdfPCell orgDnGrpCell = new PdfPCell();
                                AmpOrgGroup grp=filter.getOrganization().getOrgGrpId();
                                String grpName="";
                                if(grp!=null){
                                    grpName=grp.getOrgGrpName();
                                }
                                orgDnGrpCell.addElement(new Paragraph(grpName,plainFont));
                                orgDnGrpCell.setBackgroundColor(cellColor);
                                orgSummaryTbl.addCell(orgDnGrpCell);

                                PdfPCell orgWbLinkTitleCell = new PdfPCell();
                                orgWbLinkTitleCell.addElement(new Paragraph("Web Link:",plainFont));
                                orgSummaryTbl.addCell(orgWbLinkTitleCell);

                                PdfPCell orgWbLinkCell = new PdfPCell();
                                orgWbLinkCell.addElement(new Paragraph(filter.getOrganization().getOrgUrl(),plainFont));
                                orgSummaryTbl.addCell(orgWbLinkCell);

                                PdfPCell orgContactNameTitleCell = new PdfPCell();
                                orgContactNameTitleCell.addElement(new Paragraph("Contact Name:",plainFont));
                                orgContactNameTitleCell.setBackgroundColor(cellColor);
                                orgSummaryTbl.addCell(orgContactNameTitleCell);

                                PdfPCell orgContactNameCell = new PdfPCell();
                                orgContactNameCell.addElement(new Paragraph(filter.getOrganization().getContactPersonName(),plainFont));
                                orgContactNameCell.setBackgroundColor(cellColor);
                                orgSummaryTbl.addCell(orgContactNameCell);

                                PdfPCell orgPhoneTitleCell = new PdfPCell();
                                orgPhoneTitleCell.addElement(new Paragraph("Contact Phone:",plainFont));
                                orgSummaryTbl.addCell(orgPhoneTitleCell);
                                PdfPCell orgPhoneCell = new PdfPCell();
                                orgPhoneCell.addElement(new Paragraph(filter.getOrganization().getPhone(),plainFont));
                                orgSummaryTbl.addCell(orgPhoneCell);

                                PdfPCell orgEmailTitleCell = new PdfPCell();
                                orgEmailTitleCell.addElement(new Paragraph("Contact Email:",plainFont));
                                orgEmailTitleCell.setBackgroundColor(cellColor);
                                orgSummaryTbl.addCell(orgEmailTitleCell);

                                PdfPCell orgEmailCell = new PdfPCell();
                                orgEmailCell.addElement(new Paragraph(filter.getOrganization().getEmail(),plainFont));
                                orgEmailCell.setBackgroundColor(cellColor);
                                orgSummaryTbl.addCell(orgEmailCell);

                                //create largest projects table
                                largetsProjectsTbl=new PdfPTable(new float[]{25,20,60});
                                largetsProjectsTbl.setWidthPercentage(100);
                                PdfPCell largestProjectsTitle = new PdfPCell(new Paragraph("5 Largest projects ("+(filter.getYear()-1)+")",headerFont));
                                largestProjectsTitle.setBackgroundColor(titleColor);
                                largestProjectsTitle.setColspan(3);
                                largestProjectsTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                largetsProjectsTbl.addCell(largestProjectsTitle);

                                PdfPCell largestProjectsProjecttitle = new PdfPCell(new Paragraph("Project title",headerFont));
                                largestProjectsProjecttitle.setBackgroundColor(titleColor);
                                largestProjectsProjecttitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                largetsProjectsTbl.addCell(largestProjectsProjecttitle);

                                PdfPCell largestProjectsCommitmentTitle = new PdfPCell(new Paragraph("Commitment", headerFont));
                                largestProjectsCommitmentTitle.setBackgroundColor(titleColor);
                                largestProjectsCommitmentTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                largetsProjectsTbl.addCell(largestProjectsCommitmentTitle);

                                PdfPCell largestProjectsSectorTitle = new PdfPCell(new Paragraph("Sector",headerFont));
                                largestProjectsSectorTitle.setBackgroundColor(titleColor);
                                largestProjectsSectorTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                largetsProjectsTbl.addCell(largestProjectsSectorTitle);
                                List<Project> projects=OrgProfileUtil.getOrganisationLargestProjects(filter);
                                Iterator<Project> projectIter=projects.iterator();
                                int count=0;
                                while(projectIter.hasNext()){
                                    Project project=projectIter.next();
                                    PdfPCell title = new PdfPCell(new Paragraph(project.getTitle(),plainFont));
                                    PdfPCell amount = new PdfPCell(new Paragraph(project.getAmount(),plainFont));
                                    PdfPCell sectorsCell = new PdfPCell(new Paragraph(project.getSectorNames(),plainFont));
                                    if(count%2==0){
                                        title.setBackgroundColor(cellColor);
                                        amount.setBackgroundColor(cellColor);
                                        sectorsCell.setBackgroundColor(cellColor);
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
                                   float widths[]=new float[]{10f,40f,10f,10f,10f,10f,10f};

                                   parisDecTbl=new PdfPTable(widths.length);
                                   parisDecTbl.setWidths(widths);
                                  
                                   parisDecTbl.setWidthPercentage(100);
                                   PdfPCell parisDecTitle = new PdfPCell(new Paragraph("PARIS DECLARATION INDICATORS - DONORS",headerFont));
                                   parisDecTitle.setColspan(2);
                                   parisDecTitle.setBackgroundColor(titleColor);
                                   parisDecTbl.addCell(parisDecTitle);
                                   PdfPCell allDonorsCell = new PdfPCell();
                                   allDonorsCell.setColspan(3);
                                   PdfPCell selectedOrgCell = new PdfPCell();
                                   selectedOrgCell.setColspan(2);
                                   PdfPTable allDonorsTbl=new PdfPTable(3);
                                   PdfPTable selectedOrgTbl=new PdfPTable(2);

                                   PdfPCell allDonorsTblTitle = new PdfPCell(new Paragraph("All Donors ",headerFont));
                                   allDonorsTblTitle.setBackgroundColor(titleColor);
                                   allDonorsTblTitle.setColspan(3);
                                

                                   allDonorsTbl.addCell(allDonorsTblTitle);
                                   PdfPCell baseline= new PdfPCell(new Paragraph(2005+" Baseline ",headerFont));
                                   PdfPCell value= new PdfPCell(new Paragraph(filter.getYear()-1+" Value ",headerFont));
                                   PdfPCell target= new PdfPCell(new Paragraph(2010+" Target ",headerFont));
                                   baseline.setBackgroundColor(titleColor);
                                   value.setBackgroundColor(titleColor);
                                   target.setBackgroundColor(titleColor);
                                   baseline.setBorderColor(borderColor);
                                   value.setBorderColor(borderColor);;
                                   target.setBorderColor(borderColor);
                                   allDonorsTbl.addCell(baseline);
                                   allDonorsTbl.addCell(value);
                                   allDonorsTbl.addCell(target);
                                   allDonorsTbl.setWidthPercentage(100);
                                   allDonorsCell.addElement(allDonorsTbl);
                                   parisDecTbl.addCell(allDonorsCell);


                                   PdfPCell selectedOrgTblTitle = new PdfPCell(new Paragraph(filter.getOrganization().getName(),headerFont));
                                   selectedOrgTblTitle.setColspan(2);
                                   selectedOrgTblTitle.setBackgroundColor(titleColor);
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
                                   count=0;
                                   while (iter.hasNext()) {
                                       AmpAhsurveyIndicator piIndicator = iter.next();
                                       ParisIndicatorHelper piHelper = new ParisIndicatorHelper(piIndicator, filter);
                                       PdfPCell indicatorCode = new PdfPCell(new Paragraph(piIndicator.getIndicatorCode()));
                                       PdfPCell indicatorName = new PdfPCell(new Paragraph(piIndicator.getName()));
                                       String sufix="";
                                       if(!piIndicator.getIndicatorCode().equals("6")){
                                           sufix="%";
                                       }

                                       PdfPCell indicatorAllBaseline = new PdfPCell(new Paragraph(piHelper.getAllDonorBaseLineValue()+ sufix));
                                       PdfPCell indicatorAllCurrentValue = new PdfPCell(new Paragraph(piHelper.getAllCurrentValue()+ sufix));
                                       PdfPCell indicatorAllTargetValue = new PdfPCell(new Paragraph(piHelper.getAllTargetValue()+ sufix));
                                       PdfPCell indicatorOrgBaseline = new PdfPCell(new Paragraph(piHelper.getOrgBaseLineValue()+ sufix));
                                       PdfPCell indicatorOrgCurrentValue = new PdfPCell(new Paragraph(piHelper.getOrgPreviousYearValue()+ sufix));
                                       if (count % 2 == 1) {
                                           indicatorAllBaseline.setBackgroundColor(cellColor);
                                           indicatorAllCurrentValue.setBackgroundColor(cellColor);
                                           indicatorAllTargetValue.setBackgroundColor(cellColor);
                                           indicatorOrgBaseline.setBackgroundColor(cellColor);
                                           indicatorOrgCurrentValue.setBackgroundColor(cellColor);
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
                                           PdfPCell indicator5aCode = new PdfPCell(new Paragraph("5aii"));
                                           PdfPCell indicator5aName = new PdfPCell(new Paragraph("Number of donors using country PFM"));
                                           
                                           ParisIndicatorHelper piInd5aHelper = new ParisIndicatorHelper(ind5aii, filter);
                                           PdfPCell indicator5aAllBaseline = new PdfPCell(new Paragraph(piInd5aHelper.getAllDonorBaseLineValue()+" "));
                                           PdfPCell indicatorAll5aCurrentValue = new PdfPCell(new Paragraph(piInd5aHelper.getAllCurrentValue()+" "));
                                           PdfPCell indicatorAll5aTargetValue = new PdfPCell(new Paragraph(piInd5aHelper.getAllTargetValue()+" "));
                                           PdfPCell indicatorOrg5aBaseline = new PdfPCell(new Paragraph(piInd5aHelper.getOrgBaseLineValue()+" "));
                                           PdfPCell indicatorOrg5aCurrentValue = new PdfPCell(new Paragraph(piInd5aHelper.getOrgPreviousYearValue()+" "));

                                            if (count % 2 == 1) {
                                               indicator5aAllBaseline.setBackgroundColor(cellColor);
                                               indicatorAll5aCurrentValue.setBackgroundColor(cellColor);
                                               indicatorAll5aTargetValue.setBackgroundColor(cellColor);
                                               indicatorOrg5aBaseline.setBackgroundColor(cellColor);
                                               indicatorOrg5aCurrentValue.setBackgroundColor(cellColor);
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

                                           PdfPCell indicator5bCode = new PdfPCell(new Paragraph("5aii"));
                                           PdfPCell indicator5bName = new PdfPCell(new Paragraph("Number of donors using country PFM"));
                                          
                                           ParisIndicatorHelper piInd5bHelper = new ParisIndicatorHelper(ind5bii, filter);
                                           PdfPCell indicator5bAllBaseline = new PdfPCell(new Paragraph(piInd5bHelper.getAllDonorBaseLineValue()+sufix));
                                           PdfPCell indicator5bAllCurrentValue = new PdfPCell(new Paragraph(piInd5bHelper.getAllCurrentValue()+sufix));
                                           PdfPCell indicator5bAllTargetValue = new PdfPCell(new Paragraph(piInd5bHelper.getAllTargetValue()+sufix));
                                           PdfPCell indicator5bOrgBaseline = new PdfPCell(new Paragraph(piInd5bHelper.getOrgBaseLineValue()+sufix));
                                           PdfPCell indicator5bOrgCurrentValue = new PdfPCell(new Paragraph(piInd5bHelper.getOrgPreviousYearValue()+sufix));

                                           if (count % 2 == 1) {
                                               indicator5bAllBaseline.setBackgroundColor(cellColor);
                                               indicator5bAllCurrentValue.setBackgroundColor(cellColor);
                                               indicator5bAllTargetValue.setBackgroundColor(cellColor);
                                               indicator5bOrgBaseline.setBackgroundColor(cellColor);
                                               indicator5bOrgCurrentValue.setBackgroundColor(cellColor);
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
                            if(orgSummaryTbl!=null){
                                tableCell.addElement(orgSummaryTbl);
                                tableCell.addElement(new Paragraph("   "));
                                tableCell.addElement(largetsProjectsTbl);
                            }
                            if(parisDecTbl!=null){
                               
                                tableCell.addElement(parisDecTbl);
                              

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
            ex.printStackTrace();
        }

        return null;
    }
        }
