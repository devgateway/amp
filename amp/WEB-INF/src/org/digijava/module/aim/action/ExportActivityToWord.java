package org.digijava.module.aim.action;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.FeaturesUtil;

public class ExportActivityToWord extends Action {
    private static Logger logger = Logger.getLogger(ExportActivityToWord.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        EditActivityForm myForm=(EditActivityForm)form;

        //to know whether print happens from Public View or not
        HttpSession session = request.getSession();
        
        TeamMember teamMember = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
        if(teamMember == null && !FeaturesUtil.isVisibleModule("Show Editable Export Formats")) {
            return mapping.findForward("index");
        }
        
        Long actId=null;
        AmpActivityVersion activity=null;
        if (request.getParameter("activityid") != null) {
            actId = new Long(request.getParameter("activityid"));
        }

        response.setContentType("application/msword");
        response.setHeader("content-disposition", "inline;filename=activity.doc");
        XWPFDocument doc = null;

        try {
            activity = ActivityUtil.loadActivity(actId);

            if (activity != null) {
                doc = new ExportActivityToWordBuilder(activity, myForm, request).render();
            }

            ServletOutputStream out = response.getOutputStream();
            doc.write(out);
            out.flush();

        } catch (IOException e) {
            handleExportException("File data write exception", e);
        } catch (Exception e) {
            handleExportException("Exception", e);
        }

        return null;
    }

    private void handleExportException(String message, Exception e) {
        logger.error(message, e);
    }

    private List<Table> getActivityRiskTables(HttpServletRequest request,
                                              AmpActivityVersion activity)
            throws Exception {
        ServletContext ampContext = getServlet().getServletContext();
        List<Table> retVal = new ArrayList<Table>();
        HttpSession session=request.getSession();
        Long actId = new Long(request.getParameter("activityid"));

        /**
         * Activity - Risk
         */
        if (FeaturesUtil.isVisibleField("Project Risk")) {
            Table table = new Table(1);
            table.setWidth(100);
            table.setAlignment("center");

            RtfCell titleCell = new RtfCell(new Paragraph("Activity Risk",
                    HEADERFONT));
            titleCell.setBackgroundColor(CELLCOLORGRAY);
            table.addCell(titleCell);

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
                java.awt.Font font = new java.awt.Font(null, 0, 24);
                plot.setNoDataMessageFont(font);
                // write image in response
                ChartUtilities.writeChartAsPNG(outByteStream1, riskChart, 350,
                        420, riskInfo);
                Image img = Image.getInstance(outByteStream1.toByteArray());
                img.setWidthPercentage(60);
                img.setAlignment(Image.ALIGN_MIDDLE);

                RtfCell imgCell = new RtfCell(img);
                table.addCell(imgCell);

            }
            retVal.add(table);
        }
        return retVal;
    }

    private List<Table> getActivityPerformanceTables(
            HttpServletRequest request, AmpActivityVersion activity) throws BadElementException, IOException {
        ServletContext ampContext = getServlet().getServletContext();
        List<Table> retVal = new ArrayList<Table>();
        HttpSession session=request.getSession();
        /**
         * Activity - Performance
         */
        if (FeaturesUtil.isVisibleField("Activity Performance")) {
            Table table = new Table(1);
            table.setWidth(100);
            table.setAlignment("center");

            RtfCell titleCell = new RtfCell(new Paragraph(
                    "Activity Performance", HEADERFONT));
            titleCell.setBackgroundColor(CELLCOLORGRAY);
            table.addCell(titleCell);

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
                java.awt.Font font = new java.awt.Font(null, 0, 24);
                plot.setNoDataMessageFont(font);

                // write image in response
                ChartUtilities.writeChartAsPNG(outByteStream, chart, 350, 420,
                        info);
                Image img = Image.getInstance(outByteStream.toByteArray());
                img.setAlignment(Image.ALIGN_MIDDLE);
                img.setWidthPercentage(60);

                RtfCell imgCell = new RtfCell(img);
                table.addCell(imgCell);

                retVal.add(table);
            }

        }
        return retVal;
    }

    private List<Table> getActivityCreationFieldsTables(
            HttpServletRequest request, EditActivityForm myForm)
            throws WorkerException, BadElementException {
        List<Table> retVal = new ArrayList<Table>();
        ServletContext ampContext = getServlet().getServletContext();
        HttpSession session=request.getSession();
        ExportSectionHelper sectionHelper = new ExportSectionHelper(null)
                .setWidth(100).setAlign("left");
        /**
         * Activity created by
         */

        if (FeaturesUtil.isVisibleField("Activity Created By")) {
            String actCreatedByString = identification.getActAthEmail() == null ? "(unknown)" :
                    identification.getActAthFirstName() + " "
                            + identification.getActAthLastName() + "-"
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
        if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Activity Last Updated by")) {
        RtfCell currencyInfoCell=new RtfCell();             
        
        currencyInfoCell.add(currencyInfoSubTable);             
