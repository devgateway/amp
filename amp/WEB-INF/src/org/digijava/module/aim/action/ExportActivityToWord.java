package org.digijava.module.aim.action;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.EditActivityForm.Identification;
import org.digijava.module.aim.form.EditActivityForm.Planning;
import org.digijava.module.aim.form.EditActivityForm.Programs;
import org.digijava.module.aim.helper.*;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.ExportActivityToPdfUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.budget.dbentity.AmpBudgetSector;
import org.digijava.module.budget.dbentity.AmpDepartments;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.aim.helper.GlobalSettings;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.table.RtfCell;



public class ExportActivityToWord extends Action {
	
	private static Logger logger = Logger.getLogger(ExportActivityToWord.class);
	public static final Color TITLECOLOR = new Color(34, 46, 93);
    public static final Color BORDERCOLOR = new Color(0, 255, 0);
    public static final Color CELLCOLOR = new Color(219, 229, 241);
    public static final Color CELLCOLORGRAY = new Color(242,242,242);
    public static final Font PLAINFONT = new Font(Font.TIMES_ROMAN, 10);
    public static final Font BOLDFONT = new Font(Font.TIMES_ROMAN, 10,Font.BOLD);
    public static final Font HEADERFONT = new Font(Font.TIMES_ROMAN, 18, Font.BOLD);
    public static final Font TITLEFONT = new Font(Font.TIMES_ROMAN, 24, Font.BOLD);
    public static final Font SUBTITLEFONT = new Font(Font.TIMES_ROMAN, 18, Font.BOLD);
    public static final Font HEADERFONTWHITE = new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.WHITE);
    public static final int ROWS_IN_IDENTIFICATION_TABLE = 10;
    
    private static final Chunk BULLET_SYMBOL = new Chunk("\u2022");    
    private Identification identification = null;
    private Planning planning = null;
    private org.digijava.module.aim.form.EditActivityForm.Location location = null;
    private Programs programs = null;
    org.digijava.module.aim.form.EditActivityForm.Sector sectors = null;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();

	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		EditActivityForm myForm=(EditActivityForm)form;
		identification = myForm.getIdentification();
		planning = myForm.getPlanning();
		location = myForm.getLocation();
		programs = myForm.getPrograms();
		sectors = myForm.getSectors();
		
		
		ServletContext ampContext = getServlet().getServletContext();
		//to know whether print happens from Public View or not
		HttpSession session = request.getSession();
        TeamMember teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
		Long actId=null;
		AmpActivityVersion activity=null;
		if(request.getParameter("activityid")!=null){
			actId=new Long(request.getParameter("activityid"));
		}
				
        response.setContentType("application/msword");
        response.setHeader("content-disposition", "inline;filename=activity.doc");
        com.lowagie.text.Document doc = new com.lowagie.text.Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        
        RtfWriter2.getInstance(doc, baos);
        doc.open();
        try {
        	activity=ActivityUtil.loadActivity(actId);
			if(activity != null){
				AmpCategoryValue catVal=null;
				String translatedValue="";
				String output="";
				String columnName="";
				String columnVal="";
				String currency = myForm.getCurrCode();
				
				Paragraph pageTitle = new Paragraph(activity.getName(), TITLEFONT);
	            pageTitle.setAlignment(Element.ALIGN_CENTER);
	            doc.add(pageTitle);
	            doc.add(new Paragraph(" "));
	            
	            RtfCell cell = null;
	            Paragraph p1 = null;
	            
	            
	            Table overAllTable = createOverallInformationTable(request,myForm, ampContext, currency);	            
	            doc.add(overAllTable);
	            doc.add(new Paragraph(" "));
	            
	            
	            /**
	             * Identification step
	             */
	            Table identificationTbl = null;
	            identificationTbl = new Table(1);
	            identificationTbl.setWidth(100);
	            RtfCell identificationTitleCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Identification").toUpperCase(), HEADERFONT));
	            //identificationTitleCell.setColspan(2);
	            identificationTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            identificationTitleCell.setBackgroundColor(CELLCOLORGRAY);
	            identificationTbl.addCell(identificationTitleCell);	            
	            //1st cell of the overAll Table	            
	            RtfCell identificationTblCell1 = new RtfCell();	            
	            Table identificationSubTable1 = new Table(2);
	            identificationSubTable1.setWidths(new float[]{1f,2f});
	            identificationSubTable1.setWidth(100);
	            identificationSubTable1.setBorder(0);
	            generateIdentificationPart(request, ampContext, p1,identificationSubTable1);
	            identificationTblCell1.add(identificationSubTable1);
	            //identificationTbl.addCell(identificationTblCell1);
	            doc.add(identificationTbl);
                doc.add(identificationSubTable1);
	            
	            /**
	             * Planning step
	             */
	            Table planningTbl = null;
	            planningTbl = new Table(1);
	            planningTbl.setWidth(100);
	            RtfCell planningTitleCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Planning").toUpperCase(), HEADERFONT));
	            //identificationTitleCell.setColspan(2);
	            planningTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            planningTitleCell.setBackgroundColor(CELLCOLORGRAY);
	            planningTbl.addCell(planningTitleCell);
	            
	            RtfCell planningTblCell1 = new RtfCell();	            
	            Table planningSubTable1 = new Table(2);
	            planningSubTable1.setWidths(new float[]{1f,2f});
	            planningSubTable1.setWidth(100);
	            
	            processPlanningPart(request, ampContext, planningSubTable1);
	            planningTblCell1.add(planningSubTable1);
	            //planningTbl.addCell(planningTblCell1);
	            doc.add(planningTbl);
                doc.add(planningSubTable1);
	            doc.add(new Paragraph(" "));

	            //References
	    		if (FeaturesUtil.isVisibleModule("References", ampContext)) {
		            Table referencesTbl = null;
					referencesTbl = new Table(1);
					referencesTbl.setWidth(100);
					RtfCell referencesTitleCell = new RtfCell(new Paragraph(
							TranslatorWorker.translateText("References")
									.toUpperCase(), HEADERFONT));
					referencesTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
					referencesTitleCell.setBackgroundColor(CELLCOLORGRAY);
					referencesTbl.addCell(referencesTitleCell);
					//RtfCell referencesTblCell1 = new RtfCell();
					
					Table referencesSubTable1 = processReferencesPart(myForm, ampContext, request);
					//referencesTblCell1.add(referencesSubTable1);
					doc.add(referencesTbl);
					if (referencesSubTable1 != null){
						doc.add(referencesSubTable1);	
					}
					doc.add(new Paragraph(" "));
	    		}
	            
	            /**
	             * Location Step
	             */	            
	            Table locationTbl = null;
	            locationTbl = new Table(1);
	            locationTbl.setWidth(100);
	            RtfCell locationTitleCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Location").toUpperCase(), HEADERFONT));
	            locationTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            locationTitleCell.setBackgroundColor(CELLCOLORGRAY);
	            locationTbl.addCell(locationTitleCell);

	            RtfCell locationTblCell1 = new RtfCell();	            
	            Table locationSubTable1 = new Table(2);
	            locationSubTable1.setWidths(new float[]{1f,2f});
	            locationSubTable1.setWidth(100);
	            
	            if(FeaturesUtil.isVisibleModule("/Activity Form/Location/Implementation Location", ampContext)){
	            	cell = new RtfCell();
	    			cell.setColspan(2);
	    			cell.setBorder(0);	            	
	            	columnVal="";
	            	if(location.getSelectedLocs()!=null){    	    			
    	    			for (Location loc  : location.getSelectedLocs()) {
    	    				for (String val : loc.getAncestorLocationNames()) {
    	    					columnVal+="["+val+"]";
							}
    	    				if(FeaturesUtil.isVisibleField("Regional Percentage", ampContext)){
    	    					columnVal+="\t\t"+loc.getPercent()+" %\n";
    	    				}    						
    					}    					
    	        	}
	            	p1 = new Paragraph(columnVal,BOLDFONT);
	            	cell.add(p1);
	            	locationSubTable1.addCell(cell);	            	
	            }
	            
	            if(FeaturesUtil.isVisibleModule("/Activity Form/Location/Implementation Level", ampContext)){	            	
	            	columnName=TranslatorWorker.translateText("Implementation Level");
	    			columnVal="";
	    			catVal = null;
	    			if(location.getLevelId()!=null && location.getLevelId() !=0){
	    				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(location.getLevelId());
	    			}					
	    			if(catVal!=null){
	    				columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal);
	    			}
	    			generateOverAllTableRows(locationSubTable1,columnName,columnVal,null);
	            }
	            
	            if(FeaturesUtil.isVisibleModule("/Activity Form/Location/Implementation Location", ampContext)){
	            	columnName=TranslatorWorker.translateText("Implementation Location");
	    			columnVal="";
	    			catVal = null;
	    			if(location.getImplemLocationLevel()!=null && location.getImplemLocationLevel() !=0){
	    				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(location.getImplemLocationLevel());
	    			}					
	    			if(catVal!=null){
	    				columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal);
	    			}
	    			generateOverAllTableRows(locationSubTable1,columnName,columnVal,null);
	            }
	            
	            locationTblCell1.add(locationSubTable1);
            	//locationTbl.addCell(locationTblCell1);
	            doc.add(locationTbl);
                doc.add(locationSubTable1);
	            doc.add(new Paragraph(" "));
	            
	            /**
	             * Sectors Part
	             */	            
	            if(FeaturesUtil.isVisibleModule("/Activity Form/Sectors", ampContext)){
	            	 Table sectorsTbl = null;
	 	            sectorsTbl = new Table(1);
	 	            sectorsTbl.setWidth(100);
	 	            RtfCell sectTitleCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Sector").toUpperCase(), HEADERFONT));
	 	            sectTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	 	            sectTitleCell.setBackgroundColor(CELLCOLORGRAY);
	 	            sectorsTbl.addCell(sectTitleCell);
	 	            
	 	            if(sectors.getClassificationConfigs() != null){
	 	            	for (AmpClassificationConfiguration config : (List<AmpClassificationConfiguration>)sectors.getClassificationConfigs()) {
	 	            		if(FeaturesUtil.isVisibleModule("/Activity Form/Sectors/"+config.getName()+" Sectors", ampContext)){
	 	            			boolean hasSectors=false;
	 	            			if (sectors.getActivitySectors() != null) {
		 	            			for (ActivitySector actSect : sectors.getActivitySectors()) {
										if(actSect.getConfigId().equals(config.getId())){
											hasSectors=true;
										}									
									}
	 	            			}
	 	            			if(hasSectors){
									cell = new RtfCell();
									cell.setBorder(0);
									cell.add(new Paragraph(config.getName()+TranslatorWorker.translateText(" Sector").toUpperCase(), PLAINFONT));						
									sectorsTbl.addCell(cell);
									sectorsTbl.addCell(new RtfCell());
								}
								if(sectors.getActivitySectors() != null){
									for (ActivitySector actSect : sectors.getActivitySectors()) {
										if(actSect.getConfigId().equals(config.getId())){
											cell = new RtfCell();
											cell.setBorder(0);
											columnVal = actSect.getSectorScheme();
											if(actSect.getSectorName() !=null){
												columnVal += " - " +actSect.getSectorName(); 
											}
											if(actSect.getSubsectorLevel1Name() !=null){
												columnVal += " - "+actSect.getSubsectorLevel1Name();
											}
											if(actSect.getSubsectorLevel2Name() !=null){
												columnVal += " - "+actSect.getSubsectorLevel2Name();
											}
											columnVal += " "+actSect.getSectorPercentage() +" %";
											cell.add(new Paragraph(columnVal,PLAINFONT));
											sectorsTbl.addCell(cell);
										}
									}
								}
								applyEmptyCell(sectorsTbl, 1);
	 	            		}
						}
	 	            }
	 	           doc.add(sectorsTbl);
	 	           doc.add(new Paragraph(" "));
	            }	
	            
	            /**
	             * PROGRAMS PART
	             */
				if (FeaturesUtil.isVisibleFeature("NPD Programs", ampContext)) {
					
		            Table programsTbl = null;
		            programsTbl = new Table(1);
		            programsTbl.setWidth(100);
		            RtfCell natPlanTitleCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("programs").toUpperCase(), HEADERFONT));
		            natPlanTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		            natPlanTitleCell.setBackgroundColor(CELLCOLORGRAY);
		            programsTbl.addCell(natPlanTitleCell);
		            programsTbl.addCell(new RtfCell());
		            
		            if(FeaturesUtil.isVisibleModule("/Activity Form/Program/National Plan Objective", ampContext)){		           
			            if(programs.getNationalPlanObjectivePrograms()!=null){
							cell = new RtfCell();
							cell.setBorder(0);
							cell.add(new Paragraph(TranslatorWorker.translateText("National Plan Objective").toUpperCase(), BOLDFONT));						
							programsTbl.addCell(cell);
							 programsTbl.addCell(new RtfCell());
							columnVal= buildProgramsOutput(programs.getNationalPlanObjectivePrograms());
							cell = new RtfCell();
							cell.setBorder(0);
							cell.add(new Paragraph(columnVal,PLAINFONT));
							programsTbl.addCell(cell);
							applyEmptyCell(programsTbl,1);
						}
		            }
		            if(FeaturesUtil.isVisibleModule("/Activity Form/Program", ampContext)){
		            	
		            	if(FeaturesUtil.isVisibleModule("/Activity Form/Program/Primary Programs", ampContext)){		           
				            if(programs.getNationalPlanObjectivePrograms()!=null){
								cell = new RtfCell();
								cell.setBorder(0);
								cell.add(new Paragraph(TranslatorWorker.translateText("Primary Programs").toUpperCase(), BOLDFONT));						
								programsTbl.addCell(cell);
								 programsTbl.addCell(new RtfCell());
								columnVal= buildProgramsOutput(programs.getPrimaryPrograms());
								cell = new RtfCell();
								cell.setBorder(0);
								cell.add(new Paragraph(columnVal,PLAINFONT));
								programsTbl.addCell(cell);
								
								applyEmptyCell(programsTbl,1);
							}			           
			            }
		            	
		            	
			            if(FeaturesUtil.isVisibleModule("/Activity Form/Program/Secondary Programs", ampContext)){		           
				            if(programs.getNationalPlanObjectivePrograms()!=null){
								cell = new RtfCell();
								cell.setBorder(0);
								cell.add(new Paragraph(TranslatorWorker.translateText("Secondary Programs").toUpperCase(), BOLDFONT));						
								programsTbl.addCell(cell);
								programsTbl.addCell(new RtfCell());
								columnVal= buildProgramsOutput(programs.getSecondaryPrograms());
								cell = new RtfCell();
								cell.setBorder(0);
								cell.add(new Paragraph(columnVal,PLAINFONT));
								programsTbl.addCell(cell);
							}
			            }
			            if(FeaturesUtil.isVisibleModule("/Activity Form/Program/Tertiary Programs", ampContext)){		           
				            if(programs.getNationalPlanObjectivePrograms()!=null){
								cell = new RtfCell();
								cell.setBorder(0);
								cell.add(new Paragraph(TranslatorWorker.translateText("Tertiary Programs").toUpperCase(), BOLDFONT));						
								programsTbl.addCell(cell);
								programsTbl.addCell(new RtfCell());
								columnVal= buildProgramsOutput(programs.getTertiaryPrograms());
								cell = new RtfCell();
								cell.setBorder(0);
								cell.add(new Paragraph(columnVal,PLAINFONT));
								programsTbl.addCell(cell);
							}
			            }
		            } 
		            
		            doc.add(programsTbl);
		            doc.add(new Paragraph(" "));
				}
            
				if (teamMember != null && FeaturesUtil.isVisibleModule("/Activity Form/Funding", ampContext)) { 
	                List<Table> donorFundingTables = getDonorFundingTables(request, ampContext, activity, myForm);
					for (Table tbl : donorFundingTables) {
	                    doc.add(tbl);
	                }
				}
				
                List<Table> regioanlFundingTables = getRegioanlFundingTables(request, ampContext, activity);
				for (Table tbl : regioanlFundingTables) {
                    doc.add(tbl);
                }

                List<Table> componentTables = getComponentTables(request, ampContext, myForm);
				for (Table tbl : componentTables) {
                    doc.add(tbl);
                }

                List<Table> issuesTables = getIssuesTables(request, ampContext, activity);
				for (Table tbl : issuesTables) {
                    doc.add(tbl);
                }

				List<Table> relatedDocsTables = getRelatedDocsTables(request,
						ampContext, myForm);
				for (Table tbl : relatedDocsTables) {
					doc.add(tbl);
				}
				

                List<Table> relatedOrgsTables = getRelatedOrgsTables(request, ampContext, activity, myForm);
				for (Table tbl : relatedOrgsTables) {
                    doc.add(tbl);
                }

                List<Table> contactInfoTables = getContactInfoTables(request, ampContext, myForm);
				for (Table tbl : contactInfoTables) {
                    doc.add(tbl);
                }

                List<Table> proposedProjectCostTables = getProposedProjectCostTables(myForm, request, ampContext, activity);
				for (Table tbl : proposedProjectCostTables) {
                    doc.add(tbl);
                }
				List<Table> contractsTables = getContractsTables(request,myForm);
				for (Table tbl : contractsTables) {
					doc.add(tbl);
				}

				List<Table> activityCreationFieldsTables = getActivityCreationFieldsTables(	request, myForm);
				for (Table tbl : activityCreationFieldsTables) {
					doc.add(tbl);
				}

				List<Table> activityPerformanceTables = getActivityPerformanceTables(request, activity);
				for (Table tbl : activityPerformanceTables) {
					doc.add(tbl);
				}

				List<Table> activityRiskTables = getActivityRiskTables(request,	activity);
				for (Table tbl : activityRiskTables) {
					doc.add(tbl);
				}
				
				List<Table> structures1 = getStructures(myForm, request, ampContext, activity);
				for (Table tbl : structures1) {
                    doc.add(tbl);
                }
			}
        	
            
            //close document
            doc.close();
            response.setContentLength(baos.size());
            ServletOutputStream out = response.getOutputStream();
            baos.writeTo(out);
            out.flush();
		} catch (IOException e) {
			handleExportException("File data write exception", e);
		} catch (com.lowagie.text.DocumentException e) {
			handleExportException("Exception while building the document tree", e);
		} catch (WorkerException e) {
			handleExportException("Translation worker exception", e);
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

		Long actId = new Long(request.getParameter("activityid"));

		/**
		 * Activity - Risk
		 */
		if (FeaturesUtil.isVisibleField("Project Risk", ampContext)) {
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
		/**
		 * Activity - Performance
		 */
		if (FeaturesUtil.isVisibleField("Activity Performance", ampContext)) {
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

		ExportSectionHelper sectionHelper = new ExportSectionHelper(null)
				.setWidth(100).setAlign("left");
		/**
		 * Activity created by
		 */

		if (FeaturesUtil.isVisibleField("Activity Created By", ampContext)) {
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
		if (FeaturesUtil.isVisibleField("Activity Updated On", ampContext)) {
			ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
					"Updated On", null, null, true).addRowData(identification
					.getUpdatedDate());
			sectionHelper.addRowData(rowData);
		}

		/**
		 * Activity updated by
		 */
		if (FeaturesUtil.isVisibleField("Activity Updated By", ampContext)) {
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
		if (FeaturesUtil.isVisibleField("Activity Created On", ampContext)) {
			ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
					"Created On", null, null, true).addRowData(identification
					.getCreatedDate());
		}
		retVal.add(createSectionTable(sectionHelper, request, ampContext));
		return retVal;
	}

	private List<Table> getContractsTables(HttpServletRequest request,
			EditActivityForm myForm) throws WorkerException,
			BadElementException {
		ServletContext ampContext = getServlet().getServletContext();
		String output = "";

		List<Table> retVal = new ArrayList<Table>();
		if (FeaturesUtil
				.isVisibleModule("/Activity Form/Contracts", ampContext)) {
			ExportSectionHelper sectionHelper = new ExportSectionHelper(
					"IPA Contracting", true).setWidth(100f).setAlign("left");
			retVal.add(createSectionTable(sectionHelper, request, ampContext));

			if (myForm.getContracts().getContracts() != null) {
				sectionHelper = new ExportSectionHelper(null, false).setWidth(
						100f).setAlign("left");

				for (IPAContract contract : (List<IPAContract>) myForm
						.getContracts().getContracts()) {
					// name
					if (FeaturesUtil
							.isVisibleModule(
									"/Activity Form/Contracts/Contract Item/Contract Info/Contract Name",
									ampContext)) {
						ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
								"Contract Name", null, null, true)
								.addRowData(contract.getContractName());
						sectionHelper.addRowData(rowData);
					}

					// description
					if (FeaturesUtil
							.isVisibleModule(
									"/Activity Form/Contracts/Contract Item/Contract Info/Contract Description",
									ampContext)) {
						ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
								"Contract Description", null, null, true)
								.addRowData(contract.getDescription());
						sectionHelper.addRowData(rowData);
					}

					// activity category
					if (FeaturesUtil
							.isVisibleModule(
									"/Activity Form/Contracts/Contract Item/Contract Info/Activity Type",
									ampContext)) {
						ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
								"Activity Category", null, null, true)
								.addRowData(contract.getActivityCategory() != null ? contract
										.getActivityCategory().getValue() : "");
						sectionHelper.addRowData(rowData);
					}

					// type
					if (FeaturesUtil
							.isVisibleModule(
									"/Activity Form/Contracts/Contract Item/Contract Info/Contract Type",
									ampContext)) {
						ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
								"Type", null, null, true).addRowData(contract
								.getType() != null ? contract.getType()
								.getValue() : "");
						sectionHelper.addRowData(rowData);
					}

					// start of tendering
					if (FeaturesUtil
							.isVisibleModule(
									"/Activity Form/Contracts/Contract Item/Contract Details/Start of Tendering",
									ampContext)) {
						ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
								"Start of Tendering", null, null, true)
								.addRowData(contract
										.getFormattedStartOfTendering());
						sectionHelper.addRowData(rowData);
					}

					// Signature of Contract
					if (FeaturesUtil
							.isVisibleModule(
									"/Activity Form/Contracts/Contract Item/Contract Details/Signature",
									ampContext)) {
						ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
								"Signature of Contract", null, null, true)
								.addRowData(contract
										.getFormattedSignatureOfContract());
						sectionHelper.addRowData(rowData);
					}

					// Contract Organization
					if (FeaturesUtil
							.isVisibleModule(
									"/Activity Form/Contracts/Contract Item/Contract Organizations",
									ampContext)) {
						ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
								"Contract Organization", null, null, true)
								.addRowData(contract.getOrganization() != null ? contract
										.getOrganization().getName() : "");
						sectionHelper.addRowData(rowData);
					}

					// Contract Organization
					if (FeaturesUtil
							.isVisibleModule(
									"/Activity Form/Contracts/Contract Item/Contract Details/Contractor Name",
									ampContext)) {
						ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
								"Contractor Name", null, null, true)
								.addRowData(contract
										.getContractingOrganizationText());
						sectionHelper.addRowData(rowData);
					}

					// Contract Completion
					if (FeaturesUtil
							.isVisibleModule(
									"/Activity Form/Contracts/Contract Item/Contract Details/Completion",
									ampContext)) {
						ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
								"Contract Completion", null, null, true)
								.addRowData(contract
										.getFormattedContractCompletion());
						sectionHelper.addRowData(rowData);
					}

					// Status
					if (FeaturesUtil
							.isVisibleModule(
									"/Activity Form/Contracts/Contract Item/Contract Details/Status",
									ampContext)) {
						ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
								"Status", null, null, true).addRowData(contract
								.getStatus() != null ? contract.getStatus()
								.getValue() : "");
						sectionHelper.addRowData(rowData);
					}

					// Total Amount
					if (FeaturesUtil
							.isVisibleModule(
									"/Activity Form/Contracts/Contract Item/Funding Allocation/Contract Total Value",
									ampContext)) {
						output = contract.getTotalAmount() != null ? contract
								.getTotalAmount().floatValue()
								+ " "
								+ contract.getTotalAmountCurrency()
										.getCurrencyCode() : " ";
						ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
								"Total Amount", null, null, true)
								.addRowData(output);
						sectionHelper.addRowData(rowData);
					}
					sectionHelper.addRowData(new ExportSectionHelperRowData(
							null, null, null, false).setSeparator(true));
					// Total EC Contribution
					if (FeaturesUtil
							.isVisibleModule(
									"/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/Contract Total Amount",
									ampContext)) {
						ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
								"Total EC Contribution", null, null, true);
						sectionHelper.addRowData(rowData);
					}

					// IB
					if (FeaturesUtil
							.isVisibleModule(
									"/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/IB Amount",
									ampContext)) {
						if (contract.getTotalECContribIBAmount() != null) {
							output = contract.getTotalECContribIBAmount()
									.floatValue()
									+ " "
									+ contract.getTotalAmountCurrency()
											.getCurrencyCode();
						} else if (contract.getTotalAmountCurrency() != null) {
							output = " "
									+ contract.getTotalAmountCurrency()
											.getCurrencyCode();
						} else {
							output = "";
						}

						ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
								"IB", null, null, true).addRowData(output);
						sectionHelper.addRowData(rowData);
					}

					// INV
					if (FeaturesUtil
							.isVisibleModule(
									"/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/INV Amount",
									ampContext)) {
						if (contract.getTotalECContribINVAmount() != null) {
							output = contract.getTotalECContribINVAmount()
									.floatValue()
									+ " "
									+ contract.getTotalAmountCurrency()
											.getCurrencyCode();
						} else if (contract.getTotalAmountCurrency() != null) {
							output = " "
									+ contract.getTotalAmountCurrency()
											.getCurrencyCode();
						} else {
							output = "";
						}
						ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
								"Contracting INV", null, null, true)
								.addRowData(output);
						sectionHelper.addRowData(rowData);
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
									"/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/Central Amount",
									ampContext)) {
						if (contract.getTotalNationalContribCentralAmount() != null) {
							output = contract
									.getTotalNationalContribCentralAmount()
									.floatValue()
									+ " "
									+ contract.getTotalAmountCurrency()
											.getCurrencyCode();
						} else if (contract.getTotalAmountCurrency() != null) {
							output = " "
									+ contract.getTotalAmountCurrency()
											.getCurrencyCode();
						} else {
							output = "";
						}
						rowData = new ExportSectionHelperRowData(
								"Contracting Central Amount", null, null, true)
								.addRowData(output);
						sectionHelper.addRowData(rowData);
					}

					// Regional
					if (FeaturesUtil
							.isVisibleField(
									"/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/Regional Amount",
									ampContext)) {
						if (contract.getTotalNationalContribRegionalAmount() != null) {
							output = contract
									.getTotalNationalContribRegionalAmount()
									.floatValue()
									+ " "
									+ contract.getTotalAmountCurrency()
											.getCurrencyCode();
						} else if (contract.getTotalAmountCurrency() != null) {
							output = " "
									+ contract.getTotalAmountCurrency()
											.getCurrencyCode();
						} else {
							output = "";
						}
						rowData = new ExportSectionHelperRowData("Regional",
								null, null, true).addRowData(output);
						sectionHelper.addRowData(rowData);
					}

					// IFIs
					if (FeaturesUtil
							.isVisibleField(
									"/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/IFI Amount",
									ampContext)) {
						if (contract.getTotalNationalContribIFIAmount() != null) {
							output = contract
									.getTotalNationalContribIFIAmount()
									.floatValue()
									+ " "
									+ contract.getTotalAmountCurrency()
											.getCurrencyCode();
						} else if (contract.getTotalAmountCurrency() != null) {
							output = " "
									+ contract.getTotalAmountCurrency()
											.getCurrencyCode();
						} else {
							output = "";
						}
						rowData = new ExportSectionHelperRowData("IFIs", null,
								null, true).addRowData(output);
						sectionHelper.addRowData(rowData);
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
									"/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/IB Amount",
									ampContext)) {
						if (contract.getTotalPrivateContribAmount() != null) {
							output = contract.getTotalPrivateContribAmount()
									.floatValue()
									+ " "
									+ contract.getTotalAmountCurrency()
											.getCurrencyCode();
						} else if (contract.getTotalAmountCurrency() != null) {
							output = " "
									+ contract.getTotalAmountCurrency()
											.getCurrencyCode();
						} else {
							output = "";
						}
						rowData = new ExportSectionHelperRowData("IB", null,
								null, true).addRowData(output);
						sectionHelper.addRowData(rowData);
					}

					sectionHelper.addRowData(new ExportSectionHelperRowData(
							null, null, null, false).setSeparator(true));

					if (FeaturesUtil
							.isVisibleModule(
									"/Activity Form/Contracts/Contract Item/Contract Disbursements",
									ampContext)) {

						Integer disbFieldsCount = 0;
						disbFieldsCount += FeaturesUtil
								.isVisibleModule(
										"/Activity Form/Contracts/Contract Item/Contract Disbursements/Adjustment Type",
										ampContext) ? 1 : 0;
						disbFieldsCount += FeaturesUtil
								.isVisibleModule(
										"/Activity Form/Contracts/Contract Item/Contract Disbursements/Amount",
										ampContext) ? 1 : 0;
						disbFieldsCount += FeaturesUtil
								.isVisibleModule(
										"/Activity Form/Contracts/Contract Item/Contract Disbursements/Currency",
										ampContext) ? 1 : 0;
						disbFieldsCount += FeaturesUtil
								.isVisibleModule(
										"/Activity Form/Contracts/Contract Item/Contract Disbursements/Transaction Date",
										ampContext) ? 1 : 0;

						// Total disbursements
						output = (contract.getTotalDisbursements() != null ? contract
								.getTotalDisbursements().floatValue() : " ")
								+ " ";
						output += contract.getDibusrsementsGlobalCurrency() != null ? contract
								.getDibusrsementsGlobalCurrency()
								.getCurrencyCode() : myForm.getCurrCode();
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
								.getCurrencyCode() : myForm.getCurrCode();
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
													"/Activity Form/Contracts/Contract Item/Contract Disbursements/Adjustment Type",
													ampContext)) {
										rowData.addRowData(ipaDisb
												.getAdjustmentType().getValue());
									}

									if (FeaturesUtil
											.isVisibleModule(
													"/Activity Form/Contracts/Contract Item/Contract Disbursements/Amount",
													ampContext)) {
										String currency = "";
										if (FeaturesUtil
												.isVisibleModule(
														"/Activity Form/Contracts/Contract Item/Contract Disbursements/Currency",
														ampContext)) {
											currency = ipaDisb.getCurrency()
													.getCurrencyName();
										}
										rowData.addRowData(ipaDisb.getAmount()
												.floatValue() + " " + currency);
									}

									if (FeaturesUtil
											.isVisibleModule(
													"/Activity Form/Contracts/Contract Item/Contract Disbursements/Transaction Date",
													ampContext)) {
										rowData.addRowData(ipaDisb
												.getDisbDate());
									}
									sectionHelper.addRowData(rowData);
								}
							}

							// Funding Disbursements

							rowData = new ExportSectionHelperRowData(
									"Funding Disbursements", null, null, true);
							sectionHelper.addRowData(rowData);

							if (myForm.getFunding() != null) {
								rowData = new ExportSectionHelperRowData(null);

								if (FeaturesUtil
										.isVisibleModule(
												"/Activity Form/Contracts/Contract Item/Contract Disbursements/Adjustment Type",
												ampContext)) {
									rowData.addRowData("Adj. Type Disb.", true);
								}

								if (FeaturesUtil
										.isVisibleModule(
												"/Activity Form/Contracts/Contract Item/Contract Disbursements/Amount",
												ampContext)) {
									rowData.addRowData("Amount Disb.", true);
								}

								if (FeaturesUtil
										.isVisibleModule(
												"/Activity Form/Contracts/Contract Item/Contract Disbursements/Currency",
												ampContext)) {
									rowData.addRowData("Currency Disb.", true);
								}

								if (FeaturesUtil
										.isVisibleModule(
												"/Activity Form/Contracts/Contract Item/Contract Disbursements/Transaction Date",
												ampContext)) {
									rowData.addRowData("Date Disb.", true);
								}
								sectionHelper.addRowData(rowData);

								for (FundingDetail fundingDetail : myForm
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
														"/Activity Form/Contracts/Contract Item/Contract Disbursements/Adjustment Type",
														ampContext)) {
											rowData.addRowData(fundingDetail
													.getAdjustmentTypeName()
													.getValue());
										}
										if (FeaturesUtil
												.isVisibleModule(
														"/Activity Form/Contracts/Contract Item/Contract Disbursements/Amount",
														ampContext)) {
											rowData.addRowData(fundingDetail
													.getTransactionAmount());
										}
										if (FeaturesUtil
												.isVisibleModule(
														"/Activity Form/Contracts/Contract Item/Contract Disbursements/Currency",
														ampContext)) {
											rowData.addRowData(fundingDetail
													.getCurrencyName());
										}
										if (FeaturesUtil
												.isVisibleModule(
														"/Activity Form/Contracts/Contract Item/Contract Disbursements/Transaction Date",
														ampContext)) {
											rowData.addRowData(fundingDetail
													.getTransactionDate());
										}
										sectionHelper.addRowData(rowData);
									}
								}
							}
						}
					}
				}
				retVal.add(createSectionTable(sectionHelper, request,
						ampContext));
			}

		}
		return retVal;
	}

	private List<Table> getRelatedDocsTables(HttpServletRequest request,
			ServletContext ampContext, EditActivityForm myForm)
			throws WorkerException, BadElementException {
		List<Table> retVal = new ArrayList<Table>();
		if (FeaturesUtil.isVisibleModule("/Activity Form/Related Documents",
				ampContext)) {
			boolean createTable = false;
			ExportSectionHelper sectionHelper = new ExportSectionHelper(
					"Related Documents", true);
			retVal.add(createSectionTable(sectionHelper, request, ampContext));

			sectionHelper = new ExportSectionHelper(null, false);
			// documents
			if (myForm.getDocuments().getDocuments() != null
					&& myForm.getDocuments().getDocuments().size() > 0) {
				createTable = true;
				for (Documents doc : myForm.getDocuments().getDocuments()) {

					if (doc.getIsFile()) {
						ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
								doc.getTitle()).addRowData(doc.getFileName());
						sectionHelper.addRowData(rowData);

						rowData = new ExportSectionHelperRowData("Description",
								null, null, true).addRowData(doc
								.getDocDescription());
						sectionHelper.addRowData(rowData);

						rowData = new ExportSectionHelperRowData("Date", null,
								null, true).addRowData(doc.getDate());
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
			if (myForm.getDocuments().getCrDocuments() != null
					&& myForm.getDocuments().getCrDocuments().size() > 0) {
				createTable = true;
				for (DocumentData crDoc : myForm.getDocuments()
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

			// links
			if (myForm.getDocuments().getLinksList() != null
					&& myForm.getDocuments().getLinksList().size() > 0) {
				createTable = true;
				for (RelatedLinks doc : (Collection<RelatedLinks>) myForm
						.getDocuments().getLinksList()) {

					ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
							doc.getRelLink().getTitle()).addRowData(doc
							.getRelLink().getUrl());
					sectionHelper.addRowData(rowData);

					rowData = new ExportSectionHelperRowData("Description",
							null, null, true).addRowData(doc.getRelLink()
							.getDescription());
					sectionHelper.addRowData(rowData);

					rowData = new ExportSectionHelperRowData("Date", null,
							null, true).addRowData(doc.getRelLink().getDate());
					sectionHelper.addRowData(rowData);

					sectionHelper.addRowData(new ExportSectionHelperRowData(
							null, null, null, false).setSeparator(true));
				}
			}
			if (createTable){
				retVal.add(createSectionTable(sectionHelper, request, ampContext));
			}
		}
		return retVal;
	}

	private Table processReferencesPart(EditActivityForm myForm,
			ServletContext ampContext, HttpServletRequest request
			/*Table referencesSubTable1*/) throws WorkerException, Exception {
		
		String output = "";
		// References
		Table referencesSubTable1 = null;
			Collection<AmpCategoryValue> catValues = CategoryManagerUtil
					.getAmpCategoryValueCollectionByKey(
							CategoryConstants.REFERENCE_DOCS_KEY, false);

			if (catValues != null) {
				referencesSubTable1 = new Table(2);
				referencesSubTable1.setWidths(new float[] { 1f, 2f });
				referencesSubTable1.setWidth(100);

				ReferenceDoc[] refDocs = myForm.getDocuments()
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
		
		return referencesSubTable1;
	}


	private void processPlanningPart(HttpServletRequest request, ServletContext ampContext, Table planningSubTable1)
			throws Exception {

		String columnName;
		String columnVal;
		if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Line Ministry Rank", ampContext)){
			columnName=TranslatorWorker.translateText("Line Ministry Rank")+": ";
			columnVal = planning.getLineMinRank().equals("-1")?"":planning.getLineMinRank();
			generateOverAllTableRows(planningSubTable1,columnName,columnVal,null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Ministry of Planning Rank", ampContext)){
			columnName=TranslatorWorker.translateText("Ministry of Planning Rank")+": ";
			columnVal = planning.getPlanMinRank().equals("-1")?"":planning.getPlanMinRank();
			generateOverAllTableRows(planningSubTable1,columnName,columnVal,null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Proposed Approval Date", ampContext)){
			columnName=TranslatorWorker.translateText("Proposed Approval Date") +": ";
			generateOverAllTableRows(planningSubTable1,columnName,planning.getOriginalAppDate(),null);
		}

		if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Actual Approval Date", ampContext)){
			columnName=TranslatorWorker.translateText("Actual Approval Date")+": "; 
			generateOverAllTableRows(planningSubTable1,columnName,planning.getRevisedAppDate(),null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Proposed Start Date", ampContext)){
			columnName=TranslatorWorker.translateText("Proposed Start Date") + ":";
			generateOverAllTableRows(planningSubTable1,columnName,planning.getOriginalStartDate(),null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Actual Start Date", ampContext)){
			columnName=TranslatorWorker.translateText("Actual Start Date") +": ";
			generateOverAllTableRows(planningSubTable1,columnName,planning.getRevisedStartDate(),null);
		}
			
        if (FeaturesUtil.isVisibleModule("/Activity Form/Planning/Original Completion Date", ampContext)) {
            columnName = TranslatorWorker.translateText("Original Completion Date")+": ";
            generateOverAllTableRows(planningSubTable1, columnName, planning.getOriginalCompDate(), null);
        }
        
		if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Proposed Completion Date", ampContext)){
			columnName=TranslatorWorker.translateText("Proposed Completion Date")+": ";
			generateOverAllTableRows(planningSubTable1,columnName,planning.getProposedCompDate(),null);
		}

		if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Actual Completion Date", ampContext)){
			columnName=TranslatorWorker.translateText("Actual Completion Date")+": ";
			generateOverAllTableRows(planningSubTable1,columnName,planning.getCurrentCompDate(),null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Final Date for Contracting", ampContext)){
			columnName=TranslatorWorker.translateText("Final Date for Contracting")+": ";
			generateOverAllTableRows(planningSubTable1,columnName,planning.getContractingDate(),null);
		}
		if(FeaturesUtil.isVisibleModule("/Activity Form/Planning/Final Date for Disbursements", ampContext)){
			columnName=TranslatorWorker.translateText("Final Date for Disbursements")+": ";
			generateOverAllTableRows(planningSubTable1,columnName,planning.getDisbursementsDate(),null);
		}
		
		if(FeaturesUtil.isVisibleField("Duration of Project", ampContext)){
			columnName=TranslatorWorker.translateText("Duration of Project")+": ";
            columnVal = "";
            if (planning.getProjectPeriod() != null) {
                columnVal = planning.getProjectPeriod().toString() + " " + TranslatorWorker.translateText("Months");
            }
			generateOverAllTableRows(planningSubTable1,columnName,columnVal,null);
		}
	}
	
	/*
     * Structures
     */
    private List<Table> getStructures (EditActivityForm myForm, HttpServletRequest request,	ServletContext ampContext, AmpActivityVersion act) throws CloneNotSupportedException,BadElementException, WorkerException {
        List<Table> retVal = new ArrayList<Table>();
		if (FeaturesUtil.isVisibleModule("/Activity Form/Structures",ampContext)) {

	        ExportSectionHelper eshTitle = new ExportSectionHelper("Structures", true).setWidth(100f).setAlign("left");
	
				retVal.add(createSectionTable(eshTitle, request, ampContext));
				
				Set<AmpStructure> structures = act.getStructures();
   
				ArrayList<AmpStructure> res = new ArrayList<AmpStructure>();
				for (AmpStructure struc : structures) {
					ExportSectionHelper eshProjectCostTable = new ExportSectionHelper(
							null, false).setWidth(100f).setAlign("left");
					eshProjectCostTable.addRowData(new ExportSectionHelperRowData(
							"Name", null, null, true).addRowData(struc.getTitle()));
					eshProjectCostTable.addRowData(new ExportSectionHelperRowData(
							"Type", null, null, true).addRowData(struc.getType().getName()));
					eshProjectCostTable.addRowData(new ExportSectionHelperRowData(
							"Description", null, null, true).addRowData(struc
							.getDescription()));
					eshProjectCostTable.addRowData(new ExportSectionHelperRowData(
							"Latitude", null, null, true).addRowData(struc
							.getLatitude()));
					eshProjectCostTable.addRowData(new ExportSectionHelperRowData(
							"Longitude", null, null, true).addRowData(struc
							.getLongitude()));
					retVal.add(createSectionTable(eshProjectCostTable, request, ampContext));
				}
			
		}
        return retVal;
    }
    /*
     * Proposed Project Cost
     */
    private List<Table> getProposedProjectCostTables (EditActivityForm myForm, HttpServletRequest request,	ServletContext ampContext, AmpActivityVersion act) throws BadElementException, WorkerException {
        List<Table> retVal = new ArrayList<Table>();
		if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Proposed Project Cost",ampContext)) {

	        ExportSectionHelper eshTitle = new ExportSectionHelper("Proposed Project Cost", true).setWidth(100f).setAlign("left");
	
	        retVal.add(createSectionTable(eshTitle, request, ampContext));
	
	//        Set<AmpFundingDetail> allComponents = new HashSet<AmpFundingDetail>();
	//        
	//        for (AmpFunding f : (Set<AmpFunding>) act.getFunding()) {
	//            if (f.getFundingDetails() != null && !f.getFundingDetails().isEmpty()) {
	//                allComponents.addAll(f.getFundingDetails());
	//            }
	//        }
	        String currencyCode = null;
	        if(myForm.getFunding().getProProjCost()!=null){
	        	currencyCode = myForm.getFunding().getProProjCost().getCurrencyCode();
	        }
	        if(currencyCode == null) {
	            currencyCode = Constants.DEFAULT_CURRENCY;
	        }
	        
	        String translatedCurrency = TranslatorWorker.translateText(currencyCode);
	        translatedCurrency=("".equalsIgnoreCase(currencyCode))?currencyCode:translatedCurrency;
	
	
	//        FundingCalculationsHelper fch = new FundingCalculationsHelper();
	//        fch.doCalculations(allComponents, currencyCode);
	
	
	
	
	        ExportSectionHelper eshProjectCostTable = new ExportSectionHelper(null, false).setWidth(100f).setAlign("left");
	        eshProjectCostTable.addRowData(new ExportSectionHelperRowData("Cost", null, null,  true).
	                                                addRowData(FormatHelper.formatNumber(act.getFunAmount())).
	                                                addRowData(translatedCurrency));
	
	        eshProjectCostTable.addRowData(new ExportSectionHelperRowData("Proposed Completion Date ", null, null,  true).
	                                                        addRowData(DateConversion.ConvertDateToString(act.getFunDate())));
	
	
	        retVal.add(createSectionTable(eshProjectCostTable, request, ampContext));
		}
        return retVal;
    }



    /*
     * Contact info. section
     */
    private List<Table> getContactInfoTables (HttpServletRequest request,	ServletContext ampContext, EditActivityForm myForm) throws BadElementException, WorkerException {
        List<Table> retVal = new ArrayList<Table>();

        ExportSectionHelper eshTitle = new ExportSectionHelper("Contact Information", true).setWidth(100f).setAlign("left");

        if(FeaturesUtil.isVisibleModule("/Activity Form/Contacts", ampContext)){

        	retVal.add(createSectionTable(eshTitle, request, ampContext));
			
        	ExportSectionHelper eshContactInfoTable = new ExportSectionHelper(null, false).setWidth(100f).setAlign("left");

			// Donor funding contact information
			if (FeaturesUtil.isVisibleModule("/Activity Form/Contacts/Donor Contact Information",ampContext)) {
				buildContactInfoOutput(eshContactInfoTable,	"Donor funding contact information", myForm
								.getContactInformation().getDonorContacts(),ampContext, request);
			}
			// MOFED contact information
			if (FeaturesUtil.isVisibleModule("/Activity Form/Contacts/Mofed Contact Information",
					ampContext)) {
				buildContactInfoOutput(eshContactInfoTable,"MOFED contact information", myForm
								.getContactInformation().getMofedContacts(),ampContext, request);
			}
			// Sec Min funding contact information
			if (FeaturesUtil.isVisibleModule("/Activity Form/Contacts/Sector Ministry Contact Information",	ampContext)) {
				buildContactInfoOutput(eshContactInfoTable,	"Sector Ministry contact information", myForm
								.getContactInformation().getSectorMinistryContacts(), ampContext, request);
			}
			// Project Coordinator contact information
			if (FeaturesUtil.isVisibleModule("/Activity Form/Contacts/Project Coordinator Contact Information",	ampContext)) {
				buildContactInfoOutput(eshContactInfoTable,	"Proj. Coordinator contact information", myForm					.getContactInformation()
								.getProjCoordinatorContacts(), ampContext,request);
			}
			// Implementing/executing agency contact information
			if (FeaturesUtil.isVisibleModule("/Activity Form/Contacts/Implementing Executing Agency Contact Information",ampContext)) {
				buildContactInfoOutput(eshContactInfoTable,"Implementing/Executing Agency contact information",	myForm.getContactInformation()
								.getImplExecutingAgencyContacts(), ampContext,request);
			}

			retVal.add(createSectionTable(eshContactInfoTable, request,
					ampContext));
        }

        return retVal;
    }

	private void buildContactInfoOutput(
			ExportSectionHelper eshContactInfoTable, String contactType,
			Collection<AmpActivityContact> contacts, ServletContext ampContext,
			HttpServletRequest request) throws WorkerException,
			BadElementException {

		eshContactInfoTable.addRowData(new ExportSectionHelperRowData(
				contactType, null, null, true));

		if (contacts != null && contacts.size() > 0) {
			String output = "";
			for (AmpActivityContact cont : contacts) {
				String contactName = cont.getContact().getName() + " "
						+ cont.getContact().getLastname();

				ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
						contactName, null, null, true);

				Set<AmpContactProperty> contactProperties = cont.getContact()
						.getProperties();
				String emails = "";
				if (contactProperties != null) {
					for (AmpContactProperty email : contactProperties) {
						if (email.getName().equals(
								Constants.CONTACT_PROPERTY_NAME_EMAIL)) {
							emails += email.getValue() + "; ";
						}
					}
				}

				rowData.addRowData(emails);
				eshContactInfoTable.addRowData(rowData);
			}
		}
		eshContactInfoTable.addRowData(new ExportSectionHelperRowData(null,
				null, null, false).setSeparator(true));
	}


    /*
     * Related org.s section
     */

	private List<Table> getRelatedOrgsTables (HttpServletRequest request, 
			ServletContext ampContext, 
			AmpActivityVersion act,
			EditActivityForm form) throws BadElementException, WorkerException {

		List<Table> retVal = new ArrayList<Table>();

		ExportSectionHelper eshTitle = new ExportSectionHelper("Related Organizations", true).setWidth(100f).setAlign("left");
		ExportSectionHelper eshRelatedOrgsTable = new ExportSectionHelper(null, false).setWidth(100f).setAlign("left");
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Related Organizations", ampContext)){
			retVal.add(createSectionTable(eshTitle, request, ampContext));

			if (act.getOrgrole() != null && !act.getOrgrole().isEmpty()) {
				Set<AmpOrgRole> orgRoles = act.getOrgrole();

				Map <String, Set<AmpOrgRole>> roleGrouper = new HashMap<String, Set<AmpOrgRole>>();				
				for (AmpOrgRole orgRole : orgRoles) {
					if (!roleGrouper.containsKey(orgRole.getRole().getRoleCode())) {
						roleGrouper.put(orgRole.getRole().getRoleCode(), new HashSet<AmpOrgRole>());
					}

					roleGrouper.get(orgRole.getRole().getRoleCode()).add(orgRole);

					//eshIssuesTable.addRowData(new ExportSectionHelperRowData(orgRile.getRole().getName(), false));
					//retVal.add(createSectionTable(eshIssuesTable, request));
				}

				for (String roleCode : roleGrouper.keySet()) {
					Set<AmpOrgRole> groupedRoleSet = roleGrouper.get(roleCode);

					if (roleCode.equals(Constants.FUNDING_AGENCY) &&
							FeaturesUtil.isVisibleModule("/Activity Form/Funding", ampContext)){
						buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, "Donor Agency",false);
					}

					if (roleCode.equals(Constants.RESPONSIBLE_ORGANISATION) &&
							FeaturesUtil.isVisibleModule("/Activity Form/Related Organizations/Responsible Organization", ampContext)){
						buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, "Responsible Organization");
					}

					if (roleCode.equals(Constants.EXECUTING_AGENCY) &&
							FeaturesUtil.isVisibleModule("/Activity Form/Related Organizations/Executing Agency", ampContext)){
						buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, "Executing Agency");
					}

					if (roleCode.equals(Constants.IMPLEMENTING_AGENCY) &&
							FeaturesUtil.isVisibleModule("/Activity Form/Related Organizations/Implementing Agency", ampContext)){
						buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, "Implementing Agency");
					}

					if (roleCode.equals(Constants.BENEFICIARY_AGENCY) &&
							FeaturesUtil.isVisibleModule("/Activity Form/Related Organizations/Beneficiary Agency", ampContext)){
						buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, "Beneficiary Agency");
					}

					if (roleCode.equals(Constants.CONTRACTING_AGENCY) &&
							FeaturesUtil.isVisibleModule("/Activity Form/Related Organizations/Contracting Agency", ampContext)){
						buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, "Contracting Agency");
					}

					if (roleCode.equals(Constants.SECTOR_GROUP) &&
							FeaturesUtil.isVisibleModule("/Activity Form/Related Organizations/Sector Group", ampContext)){
						buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, "Sector Group");
					}

					if (roleCode.equals(Constants.REGIONAL_GROUP) &&
							FeaturesUtil.isVisibleModule("/Activity Form/Related Organizations/Regional Group", ampContext)){
						buildRoleOrgInfo(eshRelatedOrgsTable, groupedRoleSet, "Regional Group");
					}

				}

				retVal.add(createSectionTable(eshRelatedOrgsTable, request, ampContext));
			} else {
				// This section has been adapted from ExportActivityToPDF, someday all exports should be reviewed and merged.
				if (form.getFunding().getFundingOrganizations() != null && form.getFunding().getFundingOrganizations().size() > 0) {					
					if (FeaturesUtil.isVisibleModule("/Activity Form/Funding", ampContext)) {
						buildRoleOrgInfo(form.getFunding().getFundingOrganizations(), "Donor Agency", eshRelatedOrgsTable);
					}
					retVal.add(createSectionTable(eshRelatedOrgsTable, request, ampContext));
				}
			}
		}

		return retVal;
	}
    
    private void buildRoleOrgInfo(ExportSectionHelper eshRelatedOrgsTable, Set<AmpOrgRole> groupedRoleSet, String roleName){
    	 buildRoleOrgInfo( eshRelatedOrgsTable, groupedRoleSet, roleName,true);
    }
    private void buildRoleOrgInfo(ExportSectionHelper eshRelatedOrgsTable, Set<AmpOrgRole> groupedRoleSet, String roleName,boolean addPercentage){
    	if(!groupedRoleSet.isEmpty()){
	        eshRelatedOrgsTable.addRowData(new ExportSectionHelperRowData(roleName, null, null,  true));
	        for (AmpOrgRole role : groupedRoleSet) {
	            Double orgPercentage = role.getPercentage() == null ? new Double(0) : role.getPercentage();
	            if(addPercentage){
		            eshRelatedOrgsTable.addRowData(new ExportSectionHelperRowData(" ", null, null,  false).
		                    addRowData(role.getOrganisation().getName())
		                    .addRowData(orgPercentage.toString() + "%"));
	            }
	            else{
	            	eshRelatedOrgsTable.addRowData(new ExportSectionHelperRowData(" ", null, null,  false).
		                    addRowData(role.getOrganisation().getName()));
	            }
	        }
	        eshRelatedOrgsTable.addRowData(new ExportSectionHelperRowData(null,null,null, false).setSeparator(true));
    	}
    }
    
    private void buildRoleOrgInfo(List<FundingOrganization> groupedRoleSet, String roleName, ExportSectionHelper eshRelatedOrgsTable){
    	if(!groupedRoleSet.isEmpty()){
	        eshRelatedOrgsTable.addRowData(new ExportSectionHelperRowData(roleName, null, null,  true));
	        for (FundingOrganization role : groupedRoleSet) {	            
	            	eshRelatedOrgsTable.addRowData(new ExportSectionHelperRowData(" ", null, null,  false).addRowData(role.getOrgName()));

	        }
	        eshRelatedOrgsTable.addRowData(new ExportSectionHelperRowData(null,null,null, false).setSeparator(true));
    	}
    }

    /*
     * Issue section
     */
    private List<Table> getIssuesTables (HttpServletRequest request,	ServletContext ampContext, AmpActivityVersion act) throws BadElementException, WorkerException {
        List<Table> retVal = new ArrayList<Table>();
        if(FeaturesUtil.isVisibleModule("/Activity Form/Issues Section", ampContext)){
        	ExportSectionHelper eshTitle = new ExportSectionHelper("Issues", true).setWidth(100f).setAlign("left");
            retVal.add(createSectionTable(eshTitle, request, ampContext));

            if (act.getIssues() != null && !act.getIssues().isEmpty()) {
                        Set<AmpIssues> issues = act.getIssues();

                        ExportSectionHelper eshIssuesTable = new ExportSectionHelper(null, false).setWidth(100f).setAlign("left");
                        for (AmpIssues issue : issues) {
                            eshIssuesTable.addRowData(new ExportSectionHelperRowData(issue.getName() + "  " + DateConversion.ConvertDateToString(issue.getIssueDate()), null, null, false));
                            if (issue.getMeasures() != null && !issue.getMeasures().isEmpty()) {
                                for (AmpMeasure measure : (Set<AmpMeasure>) issue.getMeasures()) {
                                    eshIssuesTable.addRowData((new ExportSectionHelperRowData(" �" + measure.getName(), null, null, false)));
                                    if(measure.getActors() != null && !measure.getActors().isEmpty()) {
                                        for (AmpActor actor : (Set<AmpActor>) measure.getActors()) {
                                            eshIssuesTable.addRowData((new ExportSectionHelperRowData("  �" + actor.getName(), null, null, false)));
                                        }
                                    }
                                }
                            }
                            retVal.add(createSectionTable(eshIssuesTable, request, ampContext));
                        }
                    }
           }

        return retVal;
    }

    /*
     * Component funding section
     */
    private List<Table> getComponentTables (HttpServletRequest request,	ServletContext ampContext, EditActivityForm myForm)	throws BadElementException, WorkerException {
		final String[] componentCommitmentsFMfields = {
				"/Activity Form/Components/Component/Components Commitments",
				"/Activity Form/Components/Component/Components Commitments/Commitment Table/Amount",
				"/Activity Form/Components/Component/Components Commitments/Commitment Table/Currency",
				"/Activity Form/Components/Component/Components Commitments/Commitment Table/Transaction Date" };
		final String[] componentDisbursementsFMfields = {
				"/Activity Form/Components/Component/Components Disbursements",
				"/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Amount",
				"/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Currency",
				"/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Transaction Date" };
		final String[] componentExpendituresFMfields = {
				"/Activity Form/Components/Component/Components Expenditures",
				"/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Amount",
				"/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Currency",
				"/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Transaction Date" };


        List<Table> retVal = new ArrayList<Table>();

        ExportSectionHelper eshTitle = new ExportSectionHelper("Components", true).setWidth(100f).setAlign("left");
        
        if (FeaturesUtil.isVisibleModule("/Activity Form/Components", ampContext)) {
        	retVal.add(createSectionTable(eshTitle, request, ampContext));
        	
			boolean visibleModuleCompCommitments = FeaturesUtil.isVisibleModule(
							"/Activity Form/Components/Component/Components Commitments",
							ampContext);
			boolean visibleModuleCompDisbursements = FeaturesUtil.isVisibleModule(
							"/Activity Form/Components/Component/Components Disbursements",
							ampContext);
			boolean visibleModuleCompExpenditures = FeaturesUtil
					.isVisibleModule(
							"/Activity Form/Components/Component/Components Expenditures",
							ampContext);
			if (myForm.getComponents().getSelectedComponents() != null) {
				for (Components<FundingDetail> comp : myForm.getComponents().getSelectedComponents()) {
				if (!GlobalSettings.getInstance()
						.getShowComponentFundingByYear()) {
					ExportSectionHelper eshCompFundingDetails = new ExportSectionHelper(
							null, false).setWidth(100f).setAlign("left");
					eshCompFundingDetails
							.addRowData((new ExportSectionHelperRowData(comp
									.getTitle(), null, null, false)));
					String compDesc = comp.getDescription() != null ? comp
							.getDescription() : "";
					eshCompFundingDetails
							.addRowData((new ExportSectionHelperRowData(
									"Description", null, null, true))
									.addRowData(compDesc));

					eshCompFundingDetails
							.addRowData((new ExportSectionHelperRowData(
									"Component Funding", null, null, true)));

					if (visibleModuleCompCommitments
							&& comp.getCommitments() != null
							&& comp.getCommitments().size() > 0) {
						createComponentDetails(eshCompFundingDetails,
								comp.getCommitments(),
								componentCommitmentsFMfields, ampContext);
					}
					if (visibleModuleCompDisbursements
							&& comp.getDisbursements() != null
							&& comp.getDisbursements().size() > 0) {
						createComponentDetails(eshCompFundingDetails,
								comp.getDisbursements(),
								componentDisbursementsFMfields, ampContext);
					}

					if (visibleModuleCompExpenditures
							&& comp.getExpenditures() != null
							&& comp.getExpenditures().size() > 0) {
						createComponentDetails(eshCompFundingDetails,
								comp.getExpenditures(),
								componentExpendituresFMfields, ampContext);
					}
					
					int amountsUnitCode = Integer.valueOf(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS));

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
					
					retVal.add(createSectionTable(eshCompFundingDetails,
							request, ampContext));

					if (FeaturesUtil.isVisibleField(
							"Components Physical Progress", ampContext)) {
						ExportSectionHelper sectionHelper = new ExportSectionHelper(
								"Physical progress of the component", true);

						if (comp.getPhyProgress() != null
								&& comp.getPhyProgress().size() > 0) {
							for (PhysicalProgress phy : comp.getPhyProgress()) {
								ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
										phy.getTitle());
								rowData.addRowData(phy.getReportingDate())
										.addRowData(phy.getDescription());

								sectionHelper.addRowData(rowData);
							}
							retVal.add(createSectionTable(sectionHelper,
									request, ampContext));
						}
					}

				} else if (GlobalSettings.getInstance()
						.getShowComponentFundingByYear()
						&& FeaturesUtil.isVisibleModule("Components Resume",
								ampContext)) {
					ExportSectionHelper fundingByYearSection = new ExportSectionHelper(
							null, false).setWidth(100f).setAlign("left");
					ExportSectionHelperRowData rowData = new ExportSectionHelperRowData(
							"Component Code", null, null, true).addRowData(comp
							.getCode());
					fundingByYearSection.addRowData(rowData);
					rowData = new ExportSectionHelperRowData(
							"Finance of the component", null, null, true);

					createFinanceOfComponentSection(fundingByYearSection, comp);

					retVal.add(createSectionTable(fundingByYearSection,
							request, ampContext));
				}
			}
		}
		}



        return retVal;
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

			String actExpSum =FormatHelper.formatNumber(myMap.get("MontoEjecutado"));

			rowData = new ExportSectionHelperRowData("Actual Expenditures Sum",
					null, null, true).addRowData(actExpSum);
			fundingByYearSection.addRowData(rowData);
		}
	}

	private void createComponentDetails(
			ExportSectionHelper eshCompFundingDetails,
			Collection<FundingDetail> listToIterate,
			final String[] componentFMfields, ServletContext ampContext) {

		for (FundingDetail compFnd : listToIterate) {
			ExportSectionHelperRowData sectionHelper = new ExportSectionHelperRowData(
					getTransactionTypeLable(compFnd.getTransactionType()),
					null, null, true);
			if (FeaturesUtil.isVisibleModule(componentFMfields[0], ampContext)) {
				sectionHelper.addRowData(
						compFnd.getAdjustmentTypeNameTrimmed(), true);
			}
			if (FeaturesUtil.isVisibleModule(componentFMfields[1], ampContext)) {
				String output = compFnd.getTransactionAmount().toString();
				if (FeaturesUtil.isVisibleModule(componentFMfields[2],
						ampContext)) {
					output += compFnd.getCurrencyCode();
				}
				sectionHelper.addRowData(output);
			}
			if (FeaturesUtil.isVisibleModule(componentFMfields[3], ampContext)) {
				sectionHelper.addRowData(compFnd.getTransactionDate());
			}
			
			sectionHelper
					.addRowData(compFnd.getFormattedRate() != null ? compFnd
							.getFormattedRate() : "");
			
			if (componentFMfields[0].equals("/Activity Form/Components/Component/Components Commitments")) // hacky way of detecting "we are rendering a component funding item"
			{
				String descriptionFm = "/Activity Form/Components/Component/Components Commitments/Commitment Table/Description";
				String orgNameFm = "/Activity Form/Components/Component/Components Commitments/Commitment Table/Component Organization";
				
				if (FeaturesUtil.isVisibleModule(orgNameFm, ampContext))
				{
					String orgString = compFnd.getComponentOrganisation() != null ? compFnd.getComponentOrganisation().getName() : "";
					sectionHelper.addRowData(TranslatorWorker.translateText("Organization") + ":" + orgString);
				}
				
				if (FeaturesUtil.isVisibleModule(descriptionFm, ampContext))
				{
					String compTransStr = compFnd.getComponentTransactionDescription() == null ? "" : compFnd.getComponentTransactionDescription();
					sectionHelper.addRowData(TranslatorWorker.translateText("Transaction Description") + ": " + compTransStr);
				}
			}

			eshCompFundingDetails.addRowData(sectionHelper);
		}

	}

    
    /*
     * Regional funding section
     */
    private List<Table> getRegioanlFundingTables (HttpServletRequest request,	ServletContext ampContext, AmpActivityVersion act) throws BadElementException, WorkerException {
        List<Table> retVal = new ArrayList<Table>();

        ExportSectionHelper eshTitle = new ExportSectionHelper("Regional Fundings", true).setWidth(100f).setAlign("left");
        	if(FeaturesUtil.isVisibleModule("/Activity Form/Regional Funding", ampContext)){
                retVal.add(createSectionTable(eshTitle, request, ampContext));

                if (act.getRegionalFundings() != null && !act.getRegionalFundings().isEmpty()) {
                    Set<AmpRegionalFunding> regFnds = act.getRegionalFundings();
                	boolean visibleModuleRegCommitments = FeaturesUtil.isVisibleModule(
    								"/Activity Form/Regional Funding/Region Item/Commitments",ampContext);
    				boolean visibleModuleRegDisbursements = FeaturesUtil.isVisibleModule(
    								"/Activity Form/Regional Funding/Region Item/Disbursements", ampContext);
    				boolean visibleModuleRegExpenditures = FeaturesUtil.isVisibleModule(
    						"/Activity Form/Regional Funding/Region Item/Expenditures", ampContext);
    				
                    
    				for (AmpRegionalFunding regFnd : regFnds) {
        				// validating module visibility
    					// Commitments
    					if (regFnd.getTransactionType() == Constants.COMMITMENT && visibleModuleRegCommitments) {
    						ExportSectionHelper eshRegFundingDetails = new ExportSectionHelper(null, false).setWidth(100f).setAlign("left");
    						eshRegFundingDetails.addRowData((new ExportSectionHelperRowData(getTransactionTypeLable(regFnd
    												.getTransactionType()), null,null, true))
    										.addRowData(regFnd.getRegionLocation().getName())
    										.addRowData(regFnd.getAdjustmentType().getLabel(), true)
    										.addRowData(DateConversion.ConvertDateToString(regFnd.getTransactionDate()))
    										.addRowData(regFnd.getTransactionAmount().toString())
    										.addRowData(regFnd.getCurrency().getCurrencyCode()));
    						retVal.add(createSectionTable(eshRegFundingDetails,	request, ampContext));
    					}
    				}
    				for (AmpRegionalFunding regFnd : regFnds) {
    					// validating module visibility
    					// Disbursements
    					if (regFnd.getTransactionType() == Constants.DISBURSEMENT && visibleModuleRegDisbursements) {
    						ExportSectionHelper eshRegFundingDetails = new ExportSectionHelper(null, false).setWidth(100f).setAlign("left");
    						eshRegFundingDetails.addRowData((new ExportSectionHelperRowData(getTransactionTypeLable(regFnd
    												.getTransactionType()), null,null, true))
    										.addRowData(regFnd.getRegionLocation().getName())
    										.addRowData(regFnd.getAdjustmentType().getLabel(), true)
    										.addRowData(DateConversion.ConvertDateToString(regFnd.getTransactionDate()))
    										.addRowData(regFnd.getTransactionAmount().toString())
    										.addRowData(regFnd.getCurrency().getCurrencyCode()));
    						retVal.add(createSectionTable(eshRegFundingDetails,	request, ampContext));
    					}
    				}
    				for (AmpRegionalFunding regFnd : regFnds) {
    					// validating module visibility
    					// Expenditures
    					if (regFnd.getTransactionType() == Constants.EXPENDITURE && visibleModuleRegExpenditures) {
    						ExportSectionHelper eshRegFundingDetails = new ExportSectionHelper(null, false).setWidth(100f).setAlign("left");
    						eshRegFundingDetails.addRowData((new ExportSectionHelperRowData(getTransactionTypeLable(regFnd
    												.getTransactionType()), null,null, true))
    										.addRowData(regFnd.getRegionLocation().getName())
    										.addRowData(regFnd.getAdjustmentType().getLabel(), true)
    										.addRowData(DateConversion.ConvertDateToString(regFnd.getTransactionDate()))
    										.addRowData(regFnd.getTransactionAmount().toString())
    										.addRowData(regFnd.getCurrency().getCurrencyCode()));
    						retVal.add(createSectionTable(eshRegFundingDetails,	request, ampContext));
    					}
    				}
    			}
    		}
        
        return retVal;
    }

    /*
     * Donor funding section
     */
    private List<Table> getDonorFundingTables (HttpServletRequest request,	ServletContext ampContext, AmpActivityVersion act, EditActivityForm myForm) throws BadElementException, WorkerException {
        List<Table> retVal = new ArrayList<Table>();

        ExportSectionHelper eshTitle = new ExportSectionHelper("Donor Funding", true).setWidth(100f).setAlign("left");

        
            retVal.add(createSectionTable(eshTitle, request, ampContext));
            
        	boolean visibleModuleCommitments = FeaturesUtil.isVisibleModule(
					"/Activity Form/Funding/Funding Group/Funding Item/Commitments", ampContext);
			boolean visibleModuleDisbursement = FeaturesUtil.isVisibleModule(
					"/Activity Form/Funding/Funding Group/Funding Item/Disbursements", ampContext);
			boolean visibleModuleExpenditures = FeaturesUtil.isVisibleModule(
					"/Activity Form/Funding/Funding Group/Funding Item/Expenditures", ampContext);
			boolean visibleModuleRoF = FeaturesUtil.isVisibleModule(
					"/Activity Form/Funding/Funding Group/Funding Item/Release of Funds", ampContext);
			boolean visibleModuleEDD = FeaturesUtil.isVisibleModule(
					"/Activity Form/Funding/Funding Group/Funding Item/Estimated Disbursements", ampContext);
			boolean visibleModuleDisbOrders = FeaturesUtil.isVisibleModule(
							"/Activity Form/Funding/Funding Group/Funding Item/Disbursement Orders", ampContext);
            boolean visibleModuleMTEFProjections = FeaturesUtil.isVisibleModule(
                    "/Activity Form/Funding/Funding Group/Funding Item/MTEF Projections", ampContext);
        	
        	
            if (act.getFunding() != null && !act.getFunding().isEmpty()) {
                for (AmpFunding fnd : (Set<AmpFunding>)act.getFunding()) {
                    ExportSectionHelper eshDonorInfo = new ExportSectionHelper(null, false).setWidth(100f).setAlign("left");
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification", ampContext)){
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Funding Organization Id", ampContext)) {
                        eshDonorInfo.addRowData((new ExportSectionHelperRowData("Funding Organization Id", null, null, true)).addRowData(fnd.getFinancingId()));
                    }

                    if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Donor Organisation", ampContext)) {
                        eshDonorInfo.addRowData((new ExportSectionHelperRowData("Funding Organization Name", null, null, true)).addRowData(fnd.getAmpDonorOrgId().getName()));
                    }
                    
                    if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Donor Organisation", ampContext)) {
                        AmpRole orgRole = fnd.getSourceRole();
						eshDonorInfo.addRowData((new ExportSectionHelperRowData("Organization Role", null, null, true)).addRowData(orgRole != null ? orgRole.getName():""));
                    }

                    if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Type of Assistence", ampContext)) {
                        AmpCategoryValue typeOfAssistance = fnd.getTypeOfAssistance();
						eshDonorInfo.addRowData((new ExportSectionHelperRowData("Type of Assistance", null, null, true)).addRowData(typeOfAssistance != null ? typeOfAssistance.getLabel():""));
                    }

                    if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Financing Instrument", ampContext)) {
                        AmpCategoryValue financingInstrument = fnd.getFinancingInstrument();
						eshDonorInfo.addRowData((new ExportSectionHelperRowData("Financial Instrument", null, null, true)).addRowData(financingInstrument != null ? financingInstrument.getLabel():""));
                    }
                    
					if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Funding Status",	ampContext)) {
						String fndStatus = fnd.getFundingStatus() != null ? fnd.getFundingStatus().getValue() : " ";
						eshDonorInfo.addRowData((new ExportSectionHelperRowData("Funding Status", null, null, true))
										.addRowData(fndStatus));
					}

					if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Mode of Payment", ampContext)) {
						String modeOfPayment = fnd.getModeOfPayment() != null ? fnd.getModeOfPayment().getValue() : " ";
						eshDonorInfo.addRowData((new ExportSectionHelperRowData("Mode of Payment", null, null, true))
										.addRowData(modeOfPayment));
					}
                    }
					if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Donor Objective", ampContext)) {
						eshDonorInfo.addRowData((new ExportSectionHelperRowData("Donor Objective", null, null, true))
										.addRowData(fnd.getDonorObjective()));
					}
					if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Conditions", ampContext)) {
						eshDonorInfo.addRowData((new ExportSectionHelperRowData("Conditions", null, null, true))
										.addRowData(fnd.getConditions()));
					}
					if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Agreement", ampContext)) {
						String agreementTitle = fnd.getAgreement() != null ? fnd.getAgreement().getTitle() : " ";
						eshDonorInfo.addRowData((new ExportSectionHelperRowData("Agreement Title", null, null, true))
										.addRowData(agreementTitle));
						String agreementCode = fnd.getAgreement() != null ? fnd.getAgreement().getCode() : " ";
						eshDonorInfo.addRowData((new ExportSectionHelperRowData("Agreement Code", null, null, true))
										.addRowData(agreementCode));
					}
                    
                    eshDonorInfo.addRowData(new ExportSectionHelperRowData(null).setSeparator(true));
                    retVal.add(createSectionTable(eshDonorInfo, request, ampContext));

                    Set <AmpFundingDetail> fndDets = fnd.getFundingDetails();

                    Map<String, Map<String, Set<AmpFundingDetail>>> structuredFundings = getStructuredFundings(fndDets);
                    String toCurrCode=null;
                    HttpSession session = request.getSession();
                    TeamMember tm = (TeamMember) session.getAttribute("currentMember");
                    if (tm != null)
                        toCurrCode = CurrencyUtil.getAmpcurrency(tm.getAppSettings().getCurrencyId()).getCurrencyCode();
                    
                    if (structuredFundings.size() > 0) {
	                    ExportSectionHelper eshDonorFundingDetails = new ExportSectionHelper(null, false).setWidth(100f).setAlign("left");
	                    for (String transTypeKey : structuredFundings.keySet()) {
	                        Map<String, Set<AmpFundingDetail>> transTypeGroup = structuredFundings.get(transTypeKey);
	                        for (String adjTypeKey : transTypeGroup.keySet()) {
	                            eshDonorFundingDetails.addRowData(new ExportSectionHelperRowData(new StringBuilder(adjTypeKey).
	                                    append(" ").append(transTypeKey).toString(),null, null, true));
	                            Set<AmpFundingDetail> structuredFndDets = transTypeGroup.get(adjTypeKey);
	                            for (AmpFundingDetail fndDet : structuredFndDets) {
	                    			// validating module visibility
									// Commitments
									if ((fndDet.getTransactionType() == Constants.COMMITMENT && visibleModuleCommitments)
										// Disbursements
										|| (fndDet.getTransactionType() == Constants.DISBURSEMENT && visibleModuleDisbursement)
										// Expenditures
										|| (fndDet.getTransactionType() == Constants.EXPENDITURE && visibleModuleExpenditures)
										// Release of Funds
										|| (fndDet.getTransactionType() == Constants.RELEASE_OF_FUNDS && visibleModuleRoF)
										// Estimated Disbursements
										|| (fndDet.getTransactionType() == Constants.ESTIMATED_DONOR_DISBURSEMENT && visibleModuleEDD)
										// DisbOrders
										|| (fndDet.getTransactionType() == Constants.DISBURSEMENT_ORDER && visibleModuleDisbOrders)) {

        	                            //convert TransactionAmount to toCurrCode										
										Double total=0D;
										java.sql.Date dt = new java.sql.Date(fndDet.getTransactionDate().getTime());

        	                			double frmExRt;
        	                			if ( fndDet.getFixedExchangeRate() == null){
        	                				frmExRt = Util.getExchange(fndDet.getAmpCurrencyId().getCurrencyCode(), dt);
        	                			}else{
        	                				frmExRt = fndDet.getFixedExchangeRate();
        	                			}
        	                			
        	                			double toExRt;
        	                			if (fndDet.getAmpCurrencyId().getCurrencyCode().equalsIgnoreCase(toCurrCode)){
        	                				toExRt=frmExRt;
        	                			}else{
        	                				toExRt = Util.getExchange(toCurrCode, dt);
        	                			}
        	                			
        	                            DecimalWraper amt = CurrencyWorker.convertWrapper(fndDet.getTransactionAmount().doubleValue(), frmExRt, toExRt, dt);
        	                            
        	                            

                                        ExportSectionHelperRowData sectionHelperRowData = new ExportSectionHelperRowData(getTransactionTypeLable(fndDet.getTransactionType()), null, null, true);

                                        ExportSectionHelperRowData currentRowData = sectionHelperRowData.
		                                        addRowData(fndDet.getAdjustmentType().getLabel(), true).
		                                        addRowData(DateConversion.ConvertDateToString(fndDet.getTransactionDate())).
		                                        addRowData(FormatHelper.formatNumber(amt.doubleValue())).
		                                        addRowData(toCurrCode);


                                        

                                        if (fndDet.getFixedExchangeRate() != null) {
                                            String exchangeRateStr = TranslatorWorker.translateText("Exchange Rate: ");
                                            exchangeRateStr += DECIMAL_FORMAT.format(fndDet.getFixedExchangeRate());
                                            currentRowData.addRowData(exchangeRateStr);
                                        }
                                        if (fndDet.getRecipientOrg() != null && fndDet.getRecipientRole() != null) {
                                            String recStr = TranslatorWorker.translateText("Recipient:") + " ";
                                            recStr += fndDet.getRecipientOrg().getName() + "\n" + TranslatorWorker.translateText("as the") + " " + fndDet.getRecipientRole().getName();
                                            currentRowData.addRowData(recStr);
                                        }

                                        eshDonorFundingDetails.addRowData(sectionHelperRowData);
									}
	                            }
	                            FundingCalculationsHelper calculationsSubtotal=new FundingCalculationsHelper();
	                            calculationsSubtotal.doCalculations(structuredFndDets, toCurrCode, true);
	                            String subTotal = "";
	                            String subTotalValue = "";
	                            if (transTypeKey.equals("Commitment")&&adjTypeKey.equals("Actual")){
	                            	subTotal = TranslatorWorker.translateText("Sub-Total")+" "+TranslatorWorker.translateText("Commitment")+" "+TranslatorWorker.translateText("Actual")+ ":";
	                            	subTotalValue = FormatHelper.formatNumber(calculationsSubtotal.getTotActualComm().doubleValue());
	                            } else if (transTypeKey.equals("Commitment")&&adjTypeKey.equals("Planned")){
	                            	subTotal = TranslatorWorker.translateText("Sub-Total")+" "+TranslatorWorker.translateText("Commitment")+" "+TranslatorWorker.translateText("Planned")+ ":";
	                            	subTotalValue = FormatHelper.formatNumber(calculationsSubtotal.getTotPlannedComm().doubleValue());
	                            } else if (transTypeKey.equals("Disbursement")&&adjTypeKey.equals("Actual")){
	                            	subTotal = TranslatorWorker.translateText("Sub-Total")+" "+TranslatorWorker.translateText("Disbursement")+" "+TranslatorWorker.translateText("Actual")+ ":";
	                            	subTotalValue = FormatHelper.formatNumber(calculationsSubtotal.getTotActualDisb().doubleValue());
	                            } else if (transTypeKey.equals("Disbursement")&&adjTypeKey.equals("Planned")){
	                            	subTotal = TranslatorWorker.translateText("Sub-Total")+" "+TranslatorWorker.translateText("Disbursement")+" "+TranslatorWorker.translateText("Planned")+ ":";
	                            	subTotalValue = FormatHelper.formatNumber(calculationsSubtotal.getTotPlanDisb().doubleValue());
	                            } else if (transTypeKey.equals("Expenditure")&&adjTypeKey.equals("Actual")){
	                            	subTotal = TranslatorWorker.translateText("Sub-Total")+" "+TranslatorWorker.translateText("Expenditure")+" "+TranslatorWorker.translateText("Actual")+ ":";
	                            	subTotalValue = FormatHelper.formatNumber(calculationsSubtotal.getTotActualExp().doubleValue());
	                            } else if (transTypeKey.equals("Expenditure")&&adjTypeKey.equals("Planned")){
	                            	subTotal = TranslatorWorker.translateText("Sub-Total")+" "+TranslatorWorker.translateText("Expenditure")+" "+TranslatorWorker.translateText("Planned")+ ":";
	                            	subTotalValue = FormatHelper.formatNumber(calculationsSubtotal.getTotPlannedExp().doubleValue());
	                            }
	                            
	                            eshDonorFundingDetails.addRowData(new ExportSectionHelperRowData(subTotal).addRowData(subTotalValue + toCurrCode));
                              	
	                            eshDonorFundingDetails.addRowData(new ExportSectionHelperRowData(null).setSeparator(true));
	                        }
	                    }
	                    retVal.add(createSectionTable(eshDonorFundingDetails, request, ampContext));
                    }

                    // MTEF Projections
                    if(visibleModuleMTEFProjections && fnd.getMtefProjections() != null && fnd.getMtefProjections().size() > 0) {
                        ExportSectionHelper mtefProjections = new ExportSectionHelper(null, false).setWidth(100f).setAlign("left");
                        ExportSectionHelperRowData sectionHelperRowData = new ExportSectionHelperRowData("Mtef Projections", null, null, true);
                        mtefProjections.addRowData(sectionHelperRowData);

                        for (AmpFundingMTEFProjection projection : fnd.getMtefProjections()) {

                            String projectedType = "";
                            if (MTEFProjection.PROJECTION_ID == projection.getProjected().getId()) {
                                projectedType = "Projection";
                            }

                            if (MTEFProjection.PIPELINE_ID == projection.getProjected().getId()) {
                                projectedType = "Pipeline";
                            }

                            sectionHelperRowData = new ExportSectionHelperRowData(TranslatorWorker.translateText(projectedType), null, null, true);
                            sectionHelperRowData.addRowData(DateConversion.ConvertDateToString(projection.getProjectionDate()));
                            
                            sectionHelperRowData.addRowData(FormatHelper.formatNumber(projection.getAmount()) + " " + projection.getAmpCurrency().getCurrencyCode());
                            mtefProjections.addRowData(sectionHelperRowData);
                        }

                        mtefProjections.addRowData(new ExportSectionHelperRowData(null).setSeparator(true));
                        retVal.add(createSectionTable(mtefProjections, request, ampContext));
                    }

                }

            }



			// TOTALS
			String currencyCode = myForm.getCurrCode() == null ? Constants.DEFAULT_CURRENCY : myForm.getCurrCode();

			String totalsOutput = "";
			String totalAmountType = null;
			ExportSectionHelper fundingTotalsDetails = new ExportSectionHelper(
					null, false).setWidth(100f).setAlign("left");

			if (visibleModuleCommitments) {
				// TOTAL PLANNED COMMITMENTS
				totalAmountType = TranslatorWorker.translateText(
						"TOTAL PLANNED COMMITMENTS") + ":";
				if (myForm.getFunding().getTotalPlannedCommitments() != null
						&& myForm.getFunding().getTotalPlannedCommitments()
								.length() > 0) {
					totalsOutput = myForm.getFunding()
							.getTotalPlannedCommitments() + currencyCode;
				}
				fundingTotalsDetails.addRowData(new ExportSectionHelperRowData(
						totalAmountType).addRowData(totalsOutput));

				// TOTAL ACTUAL COMMITMENTS
				totalAmountType = TranslatorWorker.translateText(
						"TOTAL ACTUAL COMMITMENTS") + ":";
				totalsOutput = "";
				if (myForm.getFunding().getTotalCommitments() != null
						&& myForm.getFunding().getTotalCommitments().length() > 0) {
					totalsOutput = myForm.getFunding().getTotalCommitments()
							+ currencyCode;
				}
				fundingTotalsDetails.addRowData(new ExportSectionHelperRowData(
						totalAmountType).addRowData(totalsOutput));
			}

			if (visibleModuleDisbursement) {
				// TOTAL PLANNED DISBURSEMENT
				totalAmountType = TranslatorWorker.translateText(
						"TOTAL PLANNED DISBURSEMENT").toUpperCase() + ":";
				totalsOutput = "";
				if (myForm.getFunding().getTotalPlannedDisbursements() != null
						&& myForm.getFunding().getTotalPlannedDisbursements()
								.length() > 0) {
					totalsOutput = myForm.getFunding()
							.getTotalPlannedDisbursements() + currencyCode;
				}
				fundingTotalsDetails.addRowData(new ExportSectionHelperRowData(
						totalAmountType).addRowData(totalsOutput));

				// TOTAL ACTUAL DISBURSEMENT
				totalAmountType = TranslatorWorker.translateText(
						"TOTAL ACTUAL DISBURSEMENT").toUpperCase() + ":";
				totalsOutput = "";
				if (myForm.getFunding().getTotalDisbursements() != null
						&& myForm.getFunding().getTotalDisbursements().length() > 0) {
					totalsOutput = myForm.getFunding().getTotalDisbursements()
							+ currencyCode;
				}
				fundingTotalsDetails.addRowData(new ExportSectionHelperRowData(
						totalAmountType).addRowData(totalsOutput));
			}

			if (visibleModuleExpenditures) {
				// TOTAL PLANNED EXPENDITURES
				totalAmountType = TranslatorWorker.translateText(
						"TOTAL PLANNED EXPENDITURES").toUpperCase() + ":";
				totalsOutput = "";
				if (myForm.getFunding().getTotalPlannedExpenditures() != null
						&& myForm.getFunding().getTotalPlannedExpenditures()
								.length() > 0) {
					totalsOutput = myForm.getFunding()
							.getTotalPlannedExpenditures() + currencyCode;
				}
				fundingTotalsDetails.addRowData(new ExportSectionHelperRowData(
						totalAmountType).addRowData(totalsOutput));

				// TOTAL ACTUAL EXPENDITURES
				totalAmountType = TranslatorWorker.translateText(
						"TOTAL ACTUAL EXPENDITURES").toUpperCase() + ":";
				totalsOutput = "";
				if (myForm.getFunding().getTotalExpenditures() != null
						&& myForm.getFunding().getTotalExpenditures().length() > 0) {
					totalsOutput = myForm.getFunding().getTotalExpenditures()
							+ currencyCode;
				}
				fundingTotalsDetails.addRowData(new ExportSectionHelperRowData(
						totalAmountType).addRowData(totalsOutput));
			}

        if (visibleModuleRoF) {
            // Total Planned Release of Funds
            totalAmountType = TranslatorWorker.translateText(
                    "Total Planned Release of Funds").toUpperCase() + ":";
            totalsOutput = "";
            if (myForm.getFunding().getTotalPlannedReleaseOfFunds() != null
                    && myForm.getFunding().getTotalPlannedReleaseOfFunds()
                    .length() > 0) {
                totalsOutput = myForm.getFunding()
                        .getTotalPlannedReleaseOfFunds() + currencyCode;
            }
            fundingTotalsDetails.addRowData(new ExportSectionHelperRowData(
                    totalAmountType).addRowData(totalsOutput));

            // Total Actual Release of Funds
            totalAmountType = TranslatorWorker.translateText(
                    "Total Actual Release of Funds").toUpperCase() + ":";
            totalsOutput = "";
            if (myForm.getFunding().getTotalActualRoF() != null
                    && myForm.getFunding().getTotalActualRoF().length() > 0) {
                totalsOutput = myForm.getFunding().getTotalActualRoF()
                        + currencyCode;
            }
            fundingTotalsDetails.addRowData(new ExportSectionHelperRowData(
                    totalAmountType).addRowData(totalsOutput));
        }


			if (visibleModuleDisbOrders) {
				// TOTAL ACTUAL DISBURSeMENT ORDERS:
				totalAmountType = TranslatorWorker.translateText(
						"TOTAL ACTUAL DISBURSEMENT ORDERS") + ":";
				totalsOutput = "";
				if (myForm.getFunding().getTotalActualDisbursementsOrders() != null
						&& myForm.getFunding()
								.getTotalActualDisbursementsOrders().length() > 0) {
					totalsOutput = myForm.getFunding()
							.getTotalActualDisbursementsOrders() + currencyCode;
				}
				fundingTotalsDetails.addRowData(new ExportSectionHelperRowData(
						totalAmountType).addRowData(totalsOutput));
			}
			// UNDISBURSED BALANCE
			if (FeaturesUtil
					.isVisibleFeature("Undisbursed Balance", ampContext)) {
				totalAmountType = TranslatorWorker.translateText(
						"UNDISBURSED BALANCE") + ":";
				totalsOutput = "";
				if (myForm.getFunding().getUnDisbursementsBalance() != null
						&& myForm.getFunding().getUnDisbursementsBalance()
								.length() > 0) {
					totalsOutput = myForm.getFunding()
							.getUnDisbursementsBalance() + currencyCode;
				}
				fundingTotalsDetails.addRowData(new ExportSectionHelperRowData(
						totalAmountType).addRowData(totalsOutput));
			}
			
			// Consumption Rate
            if (myForm.getFunding().getConsumptionRate()!=null && myForm.getFunding().getConsumptionRate().length()>0) {
                totalsOutput = "";
                totalAmountType = TranslatorWorker.translateText("Consumption Rate") + ":";
                totalsOutput=myForm.getFunding().getConsumptionRate();

                fundingTotalsDetails.addRowData(new ExportSectionHelperRowData(totalAmountType).addRowData(totalsOutput));
            }

			// Delivery Rate
            if (myForm.getFunding().getDeliveryRate() != null && myForm.getFunding().getDeliveryRate().length() > 0) {
                totalsOutput = "";
                totalAmountType = TranslatorWorker.translateText("Delivery Rate") + ":";
                totalsOutput = myForm.getFunding().getDeliveryRate();

                fundingTotalsDetails.addRowData(new ExportSectionHelperRowData(totalAmountType).addRowData(totalsOutput));
            }

			retVal.add(createSectionTable(fundingTotalsDetails, request,
					ampContext));
        
        return retVal;
    }
    
	private void generateIdentificationPart(HttpServletRequest request,	ServletContext ampContext, Paragraph p1, Table identificationSubTable1) 
		throws WorkerException, Exception {
		AmpCategoryValue catVal;
		String columnName;
		String columnVal;
		RtfCell cell;

		if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Title", ampContext)) {
			columnName = TranslatorWorker.translateText("Activity Name");
			generateOverAllTableRows(identificationSubTable1, columnName, identification.getTitle(), null);
		}

		//AMPID cells
		if(FeaturesUtil.isVisibleField("AMP ID", ampContext)){
			columnName=TranslatorWorker.translateText("AMP ID");
			generateOverAllTableRows(identificationSubTable1,columnName,identification.getAmpId(),null);					
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Status Reason", ampContext)){
			columnName=TranslatorWorker.translateText("Status");
			columnVal="";
			catVal = null;
			if(identification.getStatusId()!=null && identification.getStatusId()!=0){
				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getStatusId());
			}					
			if(catVal!=null){
				columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal);
			}
			if(identification.getStatusReason() != null){
				columnVal += processHtml(request, identification.getStatusReason());
			}
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}
		
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Type of Cooperation", ampContext)){
			
			columnName=TranslatorWorker.translateText("Type of Cooperation");
			columnVal = identification.getSsc_typeOfCooperation();
							
			if (columnVal != null) {
				columnVal = TranslatorWorker.translateText(columnVal);
			}
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}

		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Type of Implementation", ampContext)){
			
			columnName=TranslatorWorker.translateText("Type of Implementation");
			columnVal = identification.getSsc_typeOfImplementation();
							
			if (columnVal != null) {
				columnVal = TranslatorWorker.translateText(columnVal);
			}
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}

		if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Modalities", ampContext)){
			
			columnName=TranslatorWorker.translateText("Modalities");
			columnVal = identification.getSsc_modalities();
							
			if (columnVal != null) {
				columnVal = TranslatorWorker.translateText(columnVal);
			}
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}
		
		
		if (FeaturesUtil.isVisibleField("Contract Number", ampContext)) {
			columnName = TranslatorWorker.translateText("Contract Number");
			generateOverAllTableRows(identificationSubTable1, columnName,
					identification.getConvenioNumcont(), null);
		}

		if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Comments", ampContext)) {
			columnName = TranslatorWorker.translateText("Project Comments");
			generateOverAllTableRows(identificationSubTable1, columnName,
					processEditTagValue(request, identification.getProjectComments()), null);
		}

		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Objective", ampContext)){
			columnName=TranslatorWorker.translateText("Objectives");
			generateOverAllTableRows(identificationSubTable1,columnName,processEditTagValue(request, identification.getObjectives()),null);
		}
		
		ArrayList<AmpComments> colAux	= null;
        Collection ampFields = DbUtil.getAmpFields();
        

        
		HashMap allComments = new HashMap();
		Long actId=new Long(request.getParameter("activityid"));
        if (ampFields!=null) {
        	for (Iterator itAux = ampFields.iterator(); itAux.hasNext(); ) {
                AmpField field = (AmpField) itAux.next();
                colAux = DbUtil.getAllCommentsByField(field.getAmpFieldId(),actId);
                allComments.put(field.getFieldName(), colAux);
              }
        }

		if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Objective Comments", ampContext)) {
			TeamMember teamMember = (TeamMember) request.getSession().getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
			//objective comments
			if(teamMember!=null ){ //Objective Comments shouldn't show up on Publc View
	            
	            Table objTable=new Table(2);
	            objTable.getDefaultCell().setBorder(0);
	            for (Object commentKey : allComments.keySet()) {            	
					String key=(String)commentKey;
					List<AmpComments> values=(List<AmpComments>)allComments.get(key);
					if(key.equalsIgnoreCase("Objective Assumption") && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Objective Comments/Objective Assumption", ampContext)){
						for (AmpComments value : values) {
							objTable.addCell(new Paragraph(TranslatorWorker.translateText("Objective Assumption")+" :",PLAINFONT));
							objTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment()),BOLDFONT));
						}					
					}else if(key.equalsIgnoreCase("Objective Verification") && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Objective Comments/Objective Verification", ampContext)){
						for (AmpComments value : values) {
							objTable.addCell(new Paragraph(TranslatorWorker.translateText("Objective Verification")+" :",PLAINFONT));
							objTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment()),BOLDFONT));
						}					
					}else if (key.equalsIgnoreCase("Objective Objectively Verifiable Indicators") && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Objective Comments/Objective Objectively Verifiable Indicators", ampContext)) {
						for (AmpComments value : values) {
							objTable.addCell(new Paragraph(TranslatorWorker.translateText("Objective Objectively Verifiable Indicators")+" :",PLAINFONT));
							objTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment()),BOLDFONT));						
						}
					}
				}			            
	            generateOverAllTableRows(identificationSubTable1, TranslatorWorker.translateText("Objective Comments"), objTable,null);
			}
		}

		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Description", ampContext)){
			columnName=TranslatorWorker.translateText("Description");
			generateOverAllTableRows(identificationSubTable1,columnName,processEditTagValue(request, identification.getDescription()),null);
		}
		
		if(FeaturesUtil.isVisibleField("NPD Clasification", ampContext)){
			columnName=TranslatorWorker.translateText("NPD Clasification");
			generateOverAllTableRows(identificationSubTable1,columnName,identification.getClasiNPD(),null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Lessons Learned", ampContext)){
			columnName=TranslatorWorker.translateText("Lessons Learned");
			generateOverAllTableRows(identificationSubTable1,columnName,identification.getLessonsLearned(),null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Impact", ampContext)){
			columnName=TranslatorWorker.translateText("Project Impact");
			generateOverAllTableRows(identificationSubTable1,columnName,processEditTagValue(request, identification.getProjectImpact()),null);
		}
		
		if(identification.getActivitySummary() != null && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Activity Summary", ampContext)){
			columnName=TranslatorWorker.translateText("Activity Summary");
			generateOverAllTableRows(identificationSubTable1,columnName,processEditTagValue(request, identification.getActivitySummary()),null);
		}
		
		if(identification.getContractingArrangements() != null && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Contracting Arrangements", ampContext)){
			columnName=TranslatorWorker.translateText("Contracting Arrangements");
			generateOverAllTableRows(identificationSubTable1,columnName,processEditTagValue(request, identification.getContractingArrangements()),null);
		}
		
		if(identification.getCondSeq() != null && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Conditionality and Sequencing", ampContext)){
			columnName=TranslatorWorker.translateText("Conditionality and Sequencing");
			generateOverAllTableRows(identificationSubTable1,columnName,processEditTagValue(request, identification.getCondSeq()),null);
		}
		
		if(identification.getLinkedActivities() != null && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Linked Activities", ampContext)){
			columnName=TranslatorWorker.translateText("Linked Activities");
			generateOverAllTableRows(identificationSubTable1,columnName,processEditTagValue(request, identification.getLinkedActivities()),null);
		}
		
		if(identification.getConditionality() != null && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Conditionalities", ampContext)){
			columnName=TranslatorWorker.translateText("Conditionalities");
			generateOverAllTableRows(identificationSubTable1,columnName,processEditTagValue(request, identification.getConditionality()),null);
		}
		
		if(identification.getProjectManagement() != null && FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Management", ampContext)){
			columnName=TranslatorWorker.translateText("Project Management");
			generateOverAllTableRows(identificationSubTable1,columnName,processEditTagValue(request, identification.getProjectManagement()),null);
		}

		if (FeaturesUtil.isVisibleModule(
				"/Activity Form/Identification/Purpose", ampContext)) {
			columnName = TranslatorWorker.translateText("Purpose");
			columnVal = processEditTagValue(request,
					identification.getPurpose());
			generateOverAllTableRows(identificationSubTable1, columnName,
					columnVal, null);
		}

		if (FeaturesUtil.isVisibleModule(
				"/Activity Form/Identification/Purpose Comments", ampContext)) {
			boolean visiblePurposeAssumtion = FeaturesUtil
					.isVisibleModule(
							"/Activity Form/Identification/Purpose Comments/Purpose Assumption",
							ampContext);
			boolean visiblePurposeVerification = FeaturesUtil
					.isVisibleModule(
							"/Activity Form/Identification/Purpose Comments/Purpose Verification",
							ampContext);
			boolean visiblePurposeIndicators = FeaturesUtil
					.isVisibleModule(
							"/Activity Form/Identification/Purpose Comments/Purpose Objectively Verifiable Indicators",
							ampContext);

			Table objTable = new Table(2);
			objTable.getDefaultCell().setBorder(0);
			for (Object commentKey : allComments.keySet()) {
				String key = (String) commentKey;
				List<AmpComments> values = (List<AmpComments>) allComments
						.get(key);
				if (key.equalsIgnoreCase("Purpose Assumption")
						&& visiblePurposeAssumtion) {
					for (AmpComments value : values) {
						objTable.addCell(new Paragraph(TranslatorWorker
								.translateText("Purpose Assumption")
								+ " :", PLAINFONT));
						objTable.addCell(new Paragraph(TranslatorWorker
								.translateText(value.getComment()),
								BOLDFONT));
					}
				} else if (key.equalsIgnoreCase("Purpose Verification")
						&& visiblePurposeVerification) {
					for (AmpComments value : values) {
						objTable.addCell(new Paragraph(TranslatorWorker
								.translateText("Purpose Verification")
								+ " :", PLAINFONT));
						objTable.addCell(new Paragraph(TranslatorWorker
								.translateText(value.getComment()),
								BOLDFONT));
					}
				} else if (key
						.equalsIgnoreCase("Purpose Objectively Verifiable Indicators")
						&& visiblePurposeIndicators) {
					for (AmpComments value : values) {
						objTable.addCell(new Paragraph(
								TranslatorWorker
										.translateText("Purpose Objectively Verifiable Indicators")
										+ " :", PLAINFONT));
						objTable.addCell(new Paragraph(TranslatorWorker
								.translateText(value.getComment()),
								BOLDFONT));
					}
				}
			}
			generateOverAllTableRows(
					identificationSubTable1,
					TranslatorWorker.translateText("Purpose Comments"),
					objTable, null);
		}
		
		//Results

		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Results", ampContext)){
			
			columnName=TranslatorWorker.translateText("Results");
			columnVal=processEditTagValue(request, identification.getResults());
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}
		/**
		 *  Results Comments
		 */
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Results Comments", ampContext)){

			boolean visibleResultsAssumption = FeaturesUtil.isVisibleModule(
							"/Activity Form/Identification/Results Comments/Results Assumption", ampContext);
			boolean visibleResultsVerification = FeaturesUtil.isVisibleModule(
							"/Activity Form/Identification/Results Comments/Results Verification", ampContext);
			boolean visibleResultsIndicators = FeaturesUtil.isVisibleModule(
							"/Activity Form/Identification/Results Comments/Results Objectively Verifiable Indicators", ampContext);

			Table objTable=new Table(2);
			objTable.getDefaultCell().setBorder(0);
	        for (Object commentKey : allComments.keySet()) {            	
        	   String key=(String)commentKey;
        	   List<AmpComments> values=(List<AmpComments>)allComments.get(key);
        	   if(key.equalsIgnoreCase("Results Assumption") && visibleResultsAssumption){
        		   for (AmpComments value : values) {
        			   objTable.addCell(new Paragraph(TranslatorWorker.translateText("Results Assumption")+" :",PLAINFONT));
        			   objTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment()),BOLDFONT));
        		   }					
				}else if(key.equalsIgnoreCase("Results Verification") && visibleResultsVerification){
					for (AmpComments value : values) {
						objTable.addCell(new Paragraph(TranslatorWorker.translateText("Results Verification")+" :",PLAINFONT));
						objTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment()),BOLDFONT));
					}					
				}else if (key.equalsIgnoreCase("Results Objectively Verifiable Indicators")&& visibleResultsIndicators) {
					for (AmpComments value : values) {
						objTable.addCell(new Paragraph(TranslatorWorker.translateText("Results Objectively Verifiable Indicators")+" :",PLAINFONT));
						objTable.addCell(new Paragraph(TranslatorWorker.translateText(value.getComment()),BOLDFONT));						
					}
				}
			}
            generateOverAllTableRows(identificationSubTable1, TranslatorWorker.translateText("Results Comments"), objTable,null);
		}
				
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Accession Instrument", ampContext)){
			columnName=TranslatorWorker.translateText("Accession Instrument");
			columnVal="";
			catVal = null;
			if(identification.getAccessionInstrument()!=null && identification.getAccessionInstrument() !=0){
				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getAccessionInstrument());
			}					
			if(catVal!=null){
				columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal);
			}
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}	
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Implementing Unit", ampContext)){
			columnName=TranslatorWorker.translateText("Project Implementing Unit");
			columnVal="";
			catVal = null;
			if(identification.getProjectImplUnitId()!=null && identification.getProjectImplUnitId() !=0){
				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getProjectImplUnitId());
			}					
			if(catVal!=null){
				columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal);
			}					
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}
			
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/A.C. Chapter", ampContext)){
			columnName=TranslatorWorker.translateText("A.C. Chapter");
			columnVal="";
			catVal = null;
			if(identification.getAcChapter()!=null && identification.getAcChapter() !=0){
				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getAcChapter());
			}					
			if(catVal!=null){
				columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal);
			}
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}	
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Cris Number", ampContext)){
			columnName=TranslatorWorker.translateText("Cris Number");
			columnVal="";
			catVal = null;
			if(identification.getCrisNumber()!=null){
				columnVal = identification.getCrisNumber();
			}
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Procurement System", ampContext)){
			columnName=TranslatorWorker.translateText("Procurement System");
			columnVal="";
			catVal = null;
			if(identification.getProcurementSystem()!=null && identification.getProcurementSystem() !=0){
				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getProcurementSystem());
			}					
			if(catVal!=null){
				columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal);
			}
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Reporting System", ampContext)){
			columnName=TranslatorWorker.translateText("Reporting System");
			columnVal="";
			catVal = null;
			if(identification.getReportingSystem()!=null && identification.getReportingSystem() !=0){
				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getReportingSystem());
			}					
			if(catVal!=null){
				columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal);
			}
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Audit System", ampContext)){
			columnName=TranslatorWorker.translateText("Audit System");
			columnVal="";
			catVal = null;
			if(identification.getAuditSystem()!=null && identification.getAuditSystem() !=0){
				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getAuditSystem());
			}					
			if(catVal!=null){
				columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal);
			}
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Institutions", ampContext)){
			columnName=TranslatorWorker.translateText("Institutions");
			columnVal="";
			catVal = null;
			if(identification.getInstitutions()!=null && identification.getInstitutions() !=0){
				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getInstitutions());
			}					
			if(catVal!=null){
				columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal);
			}
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Project Category", ampContext)){
			columnName=TranslatorWorker.translateText("Project Category");
			columnVal="";
			catVal = null;
			if(identification.getProjectCategory()!=null && identification.getProjectCategory() !=0){
				catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(identification.getProjectCategory());
			}					
			if(catVal!=null){
				columnVal	= CategoryManagerUtil.translateAmpCategoryValue(catVal);
			}
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Government Agreement Number", ampContext)){
			columnName=TranslatorWorker.translateText("Government Agreement Number");
			columnVal="";
			catVal = null;
			if(identification.getGovAgreementNumber()!=null){
				columnVal	= identification.getGovAgreementNumber();
			}
			generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Activity Budget", ampContext)){
			columnName=TranslatorWorker.translateText("Budget");

			if (identification.getBudgetCV()!=null) {
				if(identification.getBudgetCV().equals(identification.getBudgetCVOn())){
					//p1=new Paragraph(TranslatorWorker.translateText("Activity is on budget"),PLAINFONT) ;
					columnVal=TranslatorWorker.translateText("Activity is on budget");
				}else if (identification.getBudgetCV().equals(identification.getBudgetCVOff())){
					//p1=new Paragraph(TranslatorWorker.translateText("Activity is off budget"),PLAINFONT) ;
					columnVal=TranslatorWorker.translateText("Activity is off budget");
				}else if (identification.getBudgetCV().equals(new Long(0))) {
					//p1=new Paragraph(TranslatorWorker.translateText("Budget Unallocated"),PLAINFONT) ;
					columnVal=TranslatorWorker.translateText("Budget Unallocated");
				}else{
					p1=new Paragraph(TranslatorWorker.translateText("Activity is on ")+identification.getBudgetCV(),PLAINFONT) ;
					columnVal=TranslatorWorker.translateText("Activity is on ")+identification.getBudgetCV();
				}
				generateOverAllTableRows(identificationSubTable1,columnName,columnVal,null);
			}
			

			cell = new RtfCell();
			cell.setBorder(0);			
			if (identification.getChapterForPreview() !=null){
				cell.add(new Paragraph(TranslatorWorker.translateText("Code Chapitre")+": ",PLAINFONT));
				cell.add(new Paragraph(identification.getChapterForPreview().getCode() + " - " 
						+ identification.getChapterForPreview().getDescription(),BOLDFONT));
				cell.add(new Paragraph(TranslatorWorker.translateText("Imputations")+": ",PLAINFONT));
				for (AmpImputation imputation : identification.getChapterForPreview().getImputations()) {
					cell.add(new Paragraph(identification.getChapterForPreview().getYear()+" - " +
							imputation.getCode() +" - "+ imputation.getDescription(),BOLDFONT));
				}
			}
			
			identificationSubTable1.addCell(cell);			
			cell=new RtfCell();
			cell.setBorder(0);
			identificationSubTable1.addCell(cell);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras", ampContext)){
			//AMP-16421
			if(identification.getBudgetCV()==identification.getBudgetCVOn()){
				if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras/FY", ampContext)){
				//	
					columnName=TranslatorWorker.translateText("FY");
					generateOverAllTableRows(identificationSubTable1,columnName,identification.getFY(),null);
				}
				if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras/Ministry Code", ampContext)) {
					columnName = TranslatorWorker.translateText("Ministry Code");
					generateOverAllTableRows(identificationSubTable1, columnName, identification.getMinistryCode(), null);
				}
				if (FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras/Project Code", ampContext)) {
					columnName = TranslatorWorker.translateText("Project Code");
					generateOverAllTableRows(identificationSubTable1, columnName, identification.getProjectCode(), null);
				}
				if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras/Vote", ampContext)){
					columnName=TranslatorWorker.translateText("Vote");
					generateOverAllTableRows(identificationSubTable1,columnName,identification.getVote(),null);
				}
				if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras/Sub-Vote", ampContext)){
					columnName=TranslatorWorker.translateText("Sub-Vote");
					generateOverAllTableRows(identificationSubTable1,columnName,identification.getSubVote(),null);
				}
				if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Extras/Sub-Program", ampContext)){
					columnName=TranslatorWorker.translateText("Sub-Program");
					generateOverAllTableRows(identificationSubTable1,columnName,identification.getSubProgram(),null);
				}
			}	
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Budget Classification", ampContext)) {
			cell = new RtfCell();
			cell.setColspan(2);
			cell.setBorder(0);

			if(identification.getSelectedbudgedsector() != null) {
				for (AmpBudgetSector budgSect : identification.getBudgetsectors()) {
                    if (identification.getSelectedbudgedsector().equals(budgSect.getIdsector())) {

                        String classificationColumnName = TranslatorWorker.translateText("Budget Classification")+": ";
                        String classificationColumnValue = BULLET_SYMBOL + budgSect.getCode() + " - " + budgSect.getSectorname();

                        generateOverAllTableRows(identificationSubTable1, classificationColumnName, classificationColumnValue, null);
                    }
				}
			}
			
			if(identification.getSelectedorg() !=null ){
				for (AmpOrganisation budgOrg : identification.getBudgetorgs()) {
					cell.add(new Paragraph(BULLET_SYMBOL + budgOrg.getBudgetOrgCode()+" - "+ budgOrg.getName(),BOLDFONT));
				}
			}
			
			if(identification.getSelecteddepartment() !=null ){
				for (AmpDepartments budgDept : identification.getBudgetdepartments()) {
					cell.add(new Paragraph(BULLET_SYMBOL + budgDept.getCode()+" - "+ budgDept.getName(),BOLDFONT));
				}
			}
			
			if(identification.getSelectedprogram() !=null ){
				for (AmpTheme budgprog : identification.getBudgetprograms()) {
					cell.add(new Paragraph(BULLET_SYMBOL + budgprog.getThemeCode()+" - "+ budgprog.getName(),BOLDFONT));
				}
			}
			identificationSubTable1.addCell(cell);
		}
		
		if(FeaturesUtil.isVisibleField("Organizations and Project ID", ampContext)){
			cell = new RtfCell();
			cell.setBorder(0);
			cell.add(new Paragraph(TranslatorWorker.translateText("Organizations and Project ID")+": ",PLAINFONT));
			if(identification.getSelectedOrganizations()!=null){
				for (OrgProjectId selectedOrgForPopup : identification.getSelectedOrganizations()) {
					if(selectedOrgForPopup!=null && selectedOrgForPopup.getOrganisation()!=null){
						cell.add(new Paragraph(BULLET_SYMBOL + "["+selectedOrgForPopup.getOrganisation().getName()+"]",PLAINFONT));
					}
				}
			}			
			
			identificationSubTable1.addCell(cell);
			cell=new RtfCell();
			cell.setBorder(0);
			identificationSubTable1.addCell(cell);
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Identification/Humanitarian Aid", ampContext)){
			cell = new RtfCell();
			cell.setColspan(2);
			cell.setBorder(0);
			cell.add(new Paragraph(TranslatorWorker.translateText("Humanitarian Aid")+": ",PLAINFONT));
			
			columnVal="";
			if(identification.getHumanitarianAid()!=null && identification.getHumanitarianAid()){
				columnVal="Yes";
			}else if(identification.getHumanitarianAid()!=null && ! identification.getHumanitarianAid()){
				columnVal="No";
			}
			cell.add(new Paragraph(columnVal,BOLDFONT));
			identificationSubTable1.addCell(cell);
		}
	}

	private void applyEmptyCell(Table identificationSubTable1) {
		applyEmptyCell(identificationSubTable1, 2);		
	}
	
	private void applyEmptyCell(Table identificationSubTable1,int colspan) {
		RtfCell lineCell = new RtfCell();
		lineCell.setColspan(colspan);		
		lineCell.add(new Paragraph("_____________________________________________________________________________________",new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.GRAY)));
		identificationSubTable1.addCell(lineCell);
	}

	private Table createOverallInformationTable(HttpServletRequest request,	EditActivityForm myForm, ServletContext ampContext, String currency)throws BadElementException, Exception, WorkerException {
		String columnVal;
		Table overAllTable = new Table(3); //overall table contains 3 subtables: funding informaiton, activity creation information , all amounts are in {curr] information
		overAllTable.setWidth(100);
		overAllTable.setBorder(0);	            
		overAllTable.setWidths(new float[]{2f,2f,1f});
		overAllTable.setBackgroundColor(CELLCOLORGRAY);
		overAllTable.setPadding(0);
		
		//1st cell of the overAll Table
		int rowAmountForCell1 = 0;
		RtfCell overallFundingCell=new RtfCell(); 
		overallFundingCell.setBackgroundColor(CELLCOLORGRAY);
		overallFundingCell.setBorder(0);	            
		overallFundingCell.setVerticalAlignment(Element.ALIGN_TOP);
		Table overAllFundingSubTable = new Table(2);
		overAllFundingSubTable.setWidth(100);
		overAllFundingSubTable.setBorder(0);
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Item/Commitments", ampContext)){
			if(myForm.getFunding().getTotalCommitments()!=null && myForm.getFunding().getTotalCommitments().length()>0){
				columnVal =  myForm.getFunding().getTotalCommitments() +" ";
			}else{
				columnVal = "0 ";
			}
			columnVal += currency;
			generateOverAllTableRows(overAllFundingSubTable,TranslatorWorker.translateText("Total Actual Commitment")+": ",columnVal,CELLCOLORGRAY );
			rowAmountForCell1++;
			
			if(myForm.getFunding().getTotalPlannedCommitments()!=null && myForm.getFunding().getTotalPlannedCommitments().length()>0){
				columnVal =  myForm.getFunding().getTotalPlannedCommitments() +" ";
			}else{
				columnVal = "0 ";
			}
			columnVal += currency;
			generateOverAllTableRows(overAllFundingSubTable,TranslatorWorker.translateText("Total Planned Commitment")+": ",columnVal,CELLCOLORGRAY );
			rowAmountForCell1++;	    		
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Item/Disbursements", ampContext)){
			if(myForm.getFunding().getTotalDisbursements()!=null && myForm.getFunding().getTotalDisbursements().length()>0){
				columnVal =  myForm.getFunding().getTotalDisbursements() +" ";
			}else{
				columnVal = "0 ";
			}
			columnVal += currency;
			generateOverAllTableRows(overAllFundingSubTable,TranslatorWorker.translateText("Total Actual Distributements")+": ",columnVal ,CELLCOLORGRAY);
			rowAmountForCell1++;
			
			if(myForm.getFunding().getTotalPlannedDisbursements()!=null && myForm.getFunding().getTotalPlannedDisbursements().length()>0){
				columnVal =  myForm.getFunding().getTotalPlannedDisbursements()  +" ";
			}else{
				columnVal = "0 ";
			}
			columnVal += currency;
			generateOverAllTableRows(overAllFundingSubTable,TranslatorWorker.translateText("Total Planned Distributements")+": ",columnVal ,CELLCOLORGRAY);
			rowAmountForCell1++;
		}
		
		if(FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Item/Expenditures", ampContext)){
			if(myForm.getFunding().getTotalExpenditures()!=null && myForm.getFunding().getTotalExpenditures().length()>0){
				columnVal =  myForm.getFunding().getTotalExpenditures() +" ";
			}else{
				columnVal = "0 ";
			}
			columnVal += currency;
			generateOverAllTableRows(overAllFundingSubTable,TranslatorWorker.translateText("Total Actual Expenditures")+": ",columnVal ,CELLCOLORGRAY);
			rowAmountForCell1++;
			
			if(myForm.getFunding().getTotalPlannedExpenditures()!=null && myForm.getFunding().getTotalPlannedExpenditures().length()>0){
				columnVal =  myForm.getFunding().getTotalPlannedExpenditures() +" ";
			}else{
				columnVal = "0 ";
			}
			columnVal += currency;
			generateOverAllTableRows(overAllFundingSubTable,TranslatorWorker.translateText("Total Planned Expenditures")+": ",columnVal ,CELLCOLORGRAY);	
			rowAmountForCell1++;
		}
		
		if(FeaturesUtil.isVisibleField("Duration of Project", ampContext)){
			if(myForm.getPlanning().getProjectPeriod()!=null){
				columnVal =  myForm.getPlanning().getProjectPeriod().toString() + " " + TranslatorWorker.translateText("Months");
			}else{
				columnVal = "";
			}
			generateOverAllTableRows(overAllFundingSubTable,TranslatorWorker.translateText("Duration of project")+": ",columnVal,CELLCOLORGRAY);
			rowAmountForCell1++;
		}
		
//		//if(FeaturesUtil.isVisibleField("Delivery rate", ampContext))
//		{
//			if(myForm.getFunding().getDeliveryRate()!=null){
//				columnVal =  myForm.getFunding().getDeliveryRate();
//			}else{
//				columnVal = "";
//			}
//			generateOverAllTableRows(overAllFundingSubTable,TranslatorWorker.translateText("Delivery rate")+": ",columnVal,CELLCOLORGRAY);
//			rowAmountForCell1++;
//		}
//		
//		//if(FeaturesUtil.isVisibleField("Consumption rate", ampContext))
//		{
//			if(myForm.getFunding().getConsumptionRate()!=null){
//				columnVal =  myForm.getFunding().getConsumptionRate();
//			}else{
//				columnVal = "";
//			}
//			generateOverAllTableRows(overAllFundingSubTable,TranslatorWorker.translateText("Consumption rate")+": ",columnVal,CELLCOLORGRAY);	
//			rowAmountForCell1++;
//		}	            
		overallFundingCell.add(overAllFundingSubTable);	            
		overAllTable.addCell(overallFundingCell);
		
		//second cell of overall table is additional info
		int rowAmountForCell2 = 5; //there are 5 rows in additional information
		RtfCell additionalInfoCell=new RtfCell();	            
		additionalInfoCell.setBackgroundColor(CELLCOLORGRAY);
		additionalInfoCell.setBorder(0);
		additionalInfoCell.setVerticalAlignment(Element.ALIGN_TOP);
		Table additionalInfoSubTable = new Table(2);
		additionalInfoSubTable.setWidth(100);
		
		columnVal = "";
		if(identification.getActAthFirstName()!=null){
			columnVal +=  identification.getActAthFirstName();
		}
		if(identification.getActAthLastName()!=null){
			columnVal += " "+ identification.getActAthLastName();
		}
		generateOverAllTableRows(additionalInfoSubTable,TranslatorWorker.translateText("Activity created by")+": ",columnVal,CELLCOLORGRAY);
		
		columnVal = "";
		if(identification.getCreatedBy() != null)
		{
		    if(identification.getCreatedBy().getAmpTeam().getName()!=null){
				columnVal +=  identification.getCreatedBy().getAmpTeam().getName();
			}
			if(identification.getCreatedBy().getAmpTeam().getAccessType()!=null){
				columnVal += "-"+identification.getCreatedBy().getAmpTeam().getAccessType();
			}
		}
		generateOverAllTableRows(additionalInfoSubTable,TranslatorWorker.translateText("Workspace of creator")+": ",columnVal,CELLCOLORGRAY);
		
		columnVal = "";
		if(identification.getCreatedBy() != null)
		{
			if(identification.getCreatedBy().getAmpTeam().getComputation()){
				columnVal += TranslatorWorker.translateText("yes");
			}else{
				columnVal += TranslatorWorker.translateText("no");
			}
		}
		generateOverAllTableRows(additionalInfoSubTable,TranslatorWorker.translateText("Computation")+": ",columnVal,CELLCOLORGRAY);
		
		columnVal = "";
		if(identification.getCreatedDate()!=null){
			columnVal += identification.getCreatedDate();
		}
		generateOverAllTableRows(additionalInfoSubTable,TranslatorWorker.translateText("Activity created on")+": ",columnVal,CELLCOLORGRAY);
		
		columnVal = "";
		if (identification.getTeam() != null 
				&& identification.getTeam().getTeamLead() != null 
				&& identification.getTeam().getTeamLead().getUser() != null) {
			columnVal += identification.getTeam().getTeamLead().getUser().getFirstNames();
			columnVal += identification.getTeam().getTeamLead().getUser().getLastName();
			columnVal += identification.getTeam().getTeamLead().getUser().getEmail();
		}
		generateOverAllTableRows(additionalInfoSubTable,TranslatorWorker.translateText("Data Team Leader")+": ",columnVal,CELLCOLORGRAY);
		
		int emptyRowsAmount = rowAmountForCell1>=rowAmountForCell2?(rowAmountForCell1 - rowAmountForCell2):(rowAmountForCell2 - rowAmountForCell1);
		for (int i=0;i<emptyRowsAmount;i++){
			generateOverAllTableRows(additionalInfoSubTable,"","",CELLCOLORGRAY);
		}

		additionalInfoCell.add(additionalInfoSubTable);
		overAllTable.addCell(additionalInfoCell);
		
		//3rd cell is for currency
		RtfCell currencyInfoCell=new RtfCell();	            
		currencyInfoCell.setBorder(0);
		Table currencyInfoSubTable = new Table(1);
		currencyInfoSubTable.setWidth(100);
		RtfCell mycell = new RtfCell(new Paragraph(TranslatorWorker.translateText("All amounts are in ") + currency, PLAINFONT));
		mycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		mycell.setVerticalAlignment(Element.ALIGN_TOP);
		mycell.setBackgroundColor(CELLCOLORGRAY);
		mycell.setRowspan(rowAmountForCell1>rowAmountForCell2?rowAmountForCell1:rowAmountForCell2);
		currencyInfoSubTable.addCell(mycell);
		
		currencyInfoCell.add(currencyInfoSubTable);	            
		overAllTable.addCell(currencyInfoCell);
		return overAllTable;
	}
	
	private void generateOverAllTableRows (Table table,String fieldName,String fieldValue,Color bgColor) throws Exception {		
		RtfCell cell=new RtfCell();
		Paragraph p1=new Paragraph(fieldName,PLAINFONT);
		cell.addElement(p1);
		if(bgColor !=null){
			cell.setBackgroundColor(bgColor);
		}
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setBorder(0);
		table.addCell(cell);		    		
		cell=new RtfCell();
		p1=new Paragraph(fieldValue,BOLDFONT);
		cell.addElement(p1);
		if(bgColor !=null){
			cell.setBackgroundColor(bgColor);
		}
		cell.setBorder(0);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		table.addCell(cell);
	}
	
	private void generateOverAllTableRows (Table table,String fieldName,Table fieldValue,Color bgColor) throws Exception {
		RtfCell cell=new RtfCell();
		Paragraph p1=new Paragraph(fieldName,PLAINFONT);
		cell.addElement(p1);	    		
		cell.setBackgroundColor(bgColor);
		cell.setBorder(0);
		table.addCell(cell);		    		
		cell=new RtfCell();		
		cell.addElement(fieldValue);
		cell.setBackgroundColor(bgColor);
		cell.setBorder(0);
		table.addCell(cell);
	}
	
	private String  getEditTagValue(HttpServletRequest request,String editKey) throws Exception{
		Site site = RequestUtils.getSite(request);
        String editorBody = org.digijava.module.editor.util.DbUtil.getEditorBody(site,editKey,RequestUtils.getNavigationLanguage(request).getCode());
        if (editorBody == null) {
        	editorBody = org.digijava.module.editor.util.DbUtil.getEditorBody(site,editKey,SiteUtils.getDefaultLanguages(site).getCode());
        }
        return editorBody;
	}
	
	//cuts <p> and </p> tags from editTag value
	private String processEditTagValue(HttpServletRequest request,String editTagKey) throws Exception {
		String result=getEditTagValue(request,editTagKey);
		return processHtml(request, result);
	}
	
	private String processHtml(HttpServletRequest request,String text) throws Exception {
		
		if(text!=null && text.indexOf("<![endif]-->") != -1){
			text = text.substring(text.lastIndexOf("<![endif]-->")+"<![endif]-->".length()); 
		}
		
		if(text!=null){
			String formatterPrefix = "<![endif]-->";  //some records contain wordpress tags in comments,which need to be filtered
			if(text.indexOf(formatterPrefix) != -1){
				text = text.substring(text.lastIndexOf(formatterPrefix)+formatterPrefix.length());				
			}
			while (text.contains("<!--")){ //remove possible comments 
				String text1 = text.substring(0,text.indexOf("<!--"));
				String text2 = text.substring(text.lastIndexOf("-->")+("-->").length()+1);
				text = text1 + text2;
			}
			
			String[] possibleTags = {"span","table","tr","td","tbody","p","style","ul","li","div"};
			
			for (int i = 0; i < possibleTags.length; i++) { //remove all possible tags
				String tag = possibleTags[i];
				
				String startTagStr = "<"+tag;
				String endTagStr = "</"+tag+">";
				
				int endTagLength = endTagStr.length();
				
				
				while(text.contains(startTagStr)){
					
					int firstIndexOfStartTag = text.indexOf(startTagStr);
					int beginIndex = text.indexOf(">", firstIndexOfStartTag)+1;
					int firstIndexOfEndTag = text.indexOf(endTagStr, beginIndex);
					
					String text1 = text.substring(0,firstIndexOfStartTag);
					String text2 = text.substring(beginIndex, firstIndexOfEndTag);
					String text3 = text.length()==firstIndexOfEndTag + endTagLength? "" : text.substring(firstIndexOfEndTag + endTagLength);
					text = text1 + text2 + text3;
				}
			}
						
			text = text.replaceAll("\\<.*?>","");
			text = text.replaceAll("&lt;", "<");
			text = text.replaceAll("&gt;",">");
			text = text.replaceAll("&amp;","&");
			text = text.replaceAll("&rsquo;","'");
			text = text.replaceAll("\\r","");
		}
		
		return ExportActivityToPdfUtil.unhtmlentities(text);
	}	

	private String buildProgramsOutput(List<AmpActivityProgram> programs) {
		String result="";
		for (AmpActivityProgram pr :programs) {			
			result+=pr.getHierarchyNames()+" \t\t"+pr.getProgramPercentage()+"% \n";
		}
		return result;
	}

    private class ExportSectionHelper {
        private String secTitle;
        private boolean translateTitle;
        private float width = 100f;
        private String align = "left";
        List <ExportSectionHelperRowData> rowData;

        public ExportSectionHelper(String secTitle, boolean translateTitle) {
            this.secTitle = secTitle;
            this.translateTitle = translateTitle;
            rowData = new ArrayList<ExportSectionHelperRowData>();
        }

        public ExportSectionHelper(String secTitle) {
            this(secTitle, false);
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

        public void addRowData (ExportSectionHelperRowData data) {
            this.rowData.add(data);
        }

        public float getWidth() {
            return width;
        }

        public ExportSectionHelper setWidth(float width) {
            this.width = width;
            return this;
        }

        public String getAlign() {
            return align;
        }

        public ExportSectionHelper setAlign(String align) {
            this.align = align;
            return this;
        }
    }
    
    private class ExportSectionHelperRowData {
        private String title;
        private boolean translateTitle;
        private List <String> fieldOrderedList;
        private Map<String, String> fldVisibilityKeyMap;
        private List<String> values;
        private boolean separator;
        private Map<String, Boolean> translateValues;




        public ExportSectionHelperRowData (String title, List<String> fields, List<String> visibilityKeys, boolean translateTitle, boolean separator) {
            this.title = title;
            this.translateTitle = translateTitle;
            this.separator = separator;
            values = new ArrayList<String>();
            translateValues = new HashMap<String, Boolean>();
            fldVisibilityKeyMap = new HashMap<String, String>();
            fieldOrderedList = fields;
            if (fields != null && !fields.isEmpty() && visibilityKeys != null && !visibilityKeys.isEmpty()) {
                int count = 0;
                for (String fld : fields) {
                    fldVisibilityKeyMap.put(fld, visibilityKeys.get(count));
                    count ++;
                }
            }
        }

        public ExportSectionHelperRowData (String title, List<String> fields, List<String> visibilityKeys, boolean translateTitle) {
            this (title, fields, visibilityKeys, translateTitle, false);
        }

        public ExportSectionHelperRowData (String title) {
            this(title, null, null, false);
        }

        public ExportSectionHelperRowData addRowData(String data, Boolean translate) {
            values.add(data);
            translateValues.put(data, translate);
            return this;
        }

        public ExportSectionHelperRowData addRowData(String data) {
            return addRowData(data, false);
        }

        public String getTitle() {
            return title;
        }

        public boolean isTranslateTitle() {
            return translateTitle;
        }

        public List<String> getValues() {
            return values;
        }

        public boolean isSeparator() {
            return separator;
        }

        public ExportSectionHelperRowData setSeparator(boolean separator) {
            this.separator = separator;
            return this;
        }

        public Map<String, Boolean> getTranslateValues() {
            return translateValues;
        }

        public Map<String, String> getFldVisibilityKeyMap() {
            return fldVisibilityKeyMap;
        }

        public List<String> getFieldOrderedList() {
            return fieldOrderedList;
        }

    }

    private Table createSectionTable(ExportSectionHelper esh, HttpServletRequest request, ServletContext ampContext) throws BadElementException, WorkerException {
        Table retVal = null;
        int maxCols = 0;
        for (ExportSectionHelperRowData rd : esh.getRowData()) {
            //Calculate visible row count
            int visibleRowCount = 0;
            if (rd.getValues() != null && !rd.getValues().isEmpty()) {
                if (rd.getFieldOrderedList() != null) {
                    for (String fldTitle : rd.getFieldOrderedList()) { 
                        String visKey = rd.getFldVisibilityKeyMap().get(fldTitle);
                        if(FeaturesUtil.isVisibleField(visKey, ampContext)) {
                            visibleRowCount ++;
                        }
                    }
                } else {  //If no visibility keys
                    visibleRowCount = rd.getValues().size();
                }
            }

            if (visibleRowCount > maxCols) {
                maxCols = rd.getValues().size();
            }
        }

        maxCols++; //first col will be the row title by default

        retVal = new Table(maxCols);
        retVal.setWidth(esh.getWidth());
        retVal.setAlignment(esh.getAlign());

        if (esh.getSecTitle() != null && esh.getSecTitle().trim().length() > 0) {
            String secTitle = esh.translateTitle ?
                                    TranslatorWorker.translateText(esh.getSecTitle()) : esh.getSecTitle();
            RtfCell titleCell = new RtfCell(new Paragraph(secTitle, HEADERFONT));
            titleCell.setColspan(maxCols);
            titleCell.setBackgroundColor(CELLCOLORGRAY);
            retVal.addCell(titleCell);
        }



        for (ExportSectionHelperRowData rd : esh.getRowData()) {
            RtfCell dataTitleCell = null;

            if (!rd.isSeparator()) {
                String title = (rd.translateTitle && rd.title != null) ?
                        TranslatorWorker.translateText(rd.title) : rd.title;


                
                dataTitleCell = new RtfCell(new Paragraph(title, BOLDFONT));
                dataTitleCell.setBackgroundColor(CELLCOLORGRAY);

                if ((rd.getValues() == null || rd.getValues().isEmpty()) && maxCols > 1) {
                    dataTitleCell.setColspan(maxCols);
                }
            } else {
                dataTitleCell = new RtfCell(new Paragraph("_____________________________________________________________________________________"));
                dataTitleCell.setColspan(maxCols);
            }


            retVal.addCell(dataTitleCell);

            int rowCounter = 1;
            int idx = 0;
            for(String rowData : rd.getValues()) {
                String visKey = null;
                if (rd.getFieldOrderedList() != null) {
                    String colTitle = rd.getFieldOrderedList().get(idx);
                    visKey = rd.getFldVisibilityKeyMap().get(colTitle);
                }

                idx ++;

                if(visKey == null || (visKey != null && FeaturesUtil.isVisibleField(visKey, ampContext))) {
                    String trnVal = (rowData !=null && rd.getTranslateValues().containsKey(rowData)) ?
                            TranslatorWorker.translateText(rowData) : rowData;
                    RtfCell dataValCell = new RtfCell(new Paragraph(trnVal != null ? trnVal : "-", PLAINFONT));
                    if (rd.getValues().size() < (maxCols - 1) && rowCounter == rd.getValues().size()) {
                        dataValCell.setColspan(maxCols - rowCounter);
                    }
                    retVal.addCell(dataValCell);
                    rowCounter ++;
                }
            }



        }


        return retVal;
    }
    
    private static String getTransactionTypeLable (int type) {
        String retVal = null;
        switch (type) {
            case Constants.COMMITMENT:
                retVal = "Commitment";
                break;

            case Constants.DISBURSEMENT:
                retVal = "Disbursement";
                break;

            case Constants.EXPENDITURE:
                retVal = "Expenditure";
                break;

            case Constants.RELEASE_OF_FUNDS:
                retVal = "Release of Funds";
                break;

            case Constants.ESTIMATED_DONOR_DISBURSEMENT:
                retVal = "Estimated Disbursement";
                break;

            case Constants.DISBURSEMENT_ORDER:
                retVal = "Disbursement Order";
                break;

            case Constants.MTEFPROJECTION:
                retVal = "MTEF Projection";
                break;

        }
        return retVal;
    }

    private static String getContactTypeLable (String type) {
        String retVal = null;
        if (type.equals(Constants.DONOR_CONTACT)) {
            retVal = "Donor funding contact";
        } else if (type.equals(Constants.MOFED_CONTACT)) {
            retVal = "MOFED contact";
        } else if (type.equals(Constants.SECTOR_MINISTRY_CONTACT)) {
            retVal = "Project Coordinator Contact";
        } else if (type.equals(Constants.PROJECT_COORDINATOR_CONTACT)) {
            retVal = "Sector Ministry Contact";
        } else if (type.equals(Constants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT)) {
            retVal = "Implementing/Executing Agency Contact";
        }

        return retVal;
    }
    
    private Map<String, Map<String, Set<AmpFundingDetail>>> getStructuredFundings (Set<AmpFundingDetail> fndDets) {
        Map<String, Map<String, Set<AmpFundingDetail>>> retVal = new HashMap<String, Map<String, Set<AmpFundingDetail>>>();
        for (AmpFundingDetail fndDet : fndDets) {
            String transactionType = getTransactionTypeLable(fndDet.getTransactionType());
            if (!retVal.containsKey(transactionType)) {
                retVal.put(transactionType, new HashMap<String, Set<AmpFundingDetail>>());
            }

            if (!retVal.get(transactionType).containsKey(fndDet.getAdjustmentType().getLabel())) {
                retVal.get(transactionType).put(fndDet.getAdjustmentType().getLabel(), new HashSet<AmpFundingDetail>());
            }
            retVal.get(transactionType).get(fndDet.getAdjustmentType().getLabel()).add(fndDet);
        }
        return retVal;
    }


}
