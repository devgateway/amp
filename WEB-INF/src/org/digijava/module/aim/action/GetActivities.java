package org.digijava.module.aim.action;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.NpdSettings;
import org.digijava.module.aim.form.ActivitiesForm;
import org.digijava.module.aim.helper.ActivityItem;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.NpdUtil;

/**
 * Returns XML of Activities list depending on request parameters. This action
 * is called asynchronously from NPD, but can be used in any other place where
 * Activity list is needed.
 *
 * @author Irakli Kobiashvili
 *
 */
public class GetActivities extends Action {
	public static final String PARAM_PROGRAM_ID = "programId";

	public static final String PARAM_STATUS = "status";

	public static final String PARAM_DONOR = "donorId";

	public static final String PARAM_YEAR_START = "startYear";

	public static final String PARAM_YEAR_END = "endYear";

	public static final String ROOT_TAG = "activityList";

	private static Logger logger = Logger.getLogger(GetActivities.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.debug("got asynchronous request for Activities list");
		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
		NpdSettings settings = NpdUtil.getCurrentSettings(tm.getTeamId());

		int maxPages=1;

		response.setContentType("text/xml");
		ActivitiesForm actForm = (ActivitiesForm) form;
		logger.debug("programId=" + actForm.getProgramId() + " statusCode=" + actForm.getStatusId());

		logger.debug("Setting activties XML in the response");
		OutputStreamWriter outputStream = new OutputStreamWriter( response.getOutputStream(),"UTF-8");
		PrintWriter out = new PrintWriter(outputStream, true);

		try {
			Date fromYear = NpdUtil.yearToDate(actForm.getStartYear(), false);
			Date toYear = NpdUtil.yearToDate(actForm.getEndYear(), true);

			//count activities with same filter but without pagination. this should be fast.
			logger.debug("counting activities");
			Integer count=NpdUtil.getActivitiesCount(actForm.getProgramId(),actForm.getStatusId(),actForm.getDonorIds(), fromYear,toYear, null, tm, false);
			//calculate pagination
			Integer pageStart=null;
			Integer rowCount=null;
			if (count!=null && count.intValue()>0 && settings!=null && settings.getActListPageSize()!=null && actForm.getCurrentPage()!=null && actForm.getCurrentPage()>0){
				rowCount=settings.getActListPageSize();
				pageStart=(actForm.getCurrentPage()-1)*rowCount;
				maxPages=count/settings.getActListPageSize();
				if (count % settings.getActListPageSize()!=0){
					maxPages++;
				}
			}

			logger.debug("retriving activities");
			Collection<ActivityItem> activities = NpdUtil.getActivities(actForm.getProgramId(),actForm.getStatusId(), actForm.getDonorIds(), fromYear,toYear, null, tm, pageStart,rowCount);

            AmpApplicationSettings ampAppSettings = DbUtil.getTeamAppSettings(tm.getTeamId());
            AmpCurrency curr=ampAppSettings.getCurrency();
            String currCode="USD";
            if(curr!=null){
              currCode=ampAppSettings.getCurrency().toString();
            }
			//convert activities to xml
			logger.debug("Converting activities to XML");
			String xml = activities2XML(activities,maxPages,currCode,request);

			out.println(xml);
//			outputStream.write(xml.getBytes());
			out.close();
			// return xml
			logger.debug("closing and returning response XML of NPD Activities");
			outputStream.close();
		} catch (Exception e) {
			logger.info(e);
			e.printStackTrace();
			if (outputStream != null) {
				try {
					outputStream.write(stackTrace2XML(e));
				} catch (IOException e1) {
					logger.info(e1);
				}
			}
		}
		return null;
	}
	
	

	/**
	 * Converts Exception stack trace to XML.
	 * used to display trace instead of results in ajax calls.
	 * @param e
	 * @return String representing XML of error
	 */
	private String stackTrace2XML(Exception e) {
		String result = "<error>";
		result += "<frame>" + e + "</frame>";
		StackTraceElement[] traceArray = e.getStackTrace();
		if (traceArray != null) {
			for (int i = 0; i < traceArray.length; i++) {
				String frame = "<frame>" + traceArray[i].toString()
						+ "</frame>";
				result += frame;
			}
		}
		result += "</error>";
		return result;
	}

	/**
	 * Constructs XML from Activities This method converts every AmpActivity db
	 * bean to ActivtyItem helper beans. Then getXml() method of the helper bean
	 * is used to get portions of the activity xml.
	 * TODO we should have XML file and velocity here. and probably Schema too...
	 *
	 * @param acts
	 *            Collection of AmpActivity db beans
	 * @return XML representing list of the activities.
	 * @see AmpActivity
	 * @see ActivityItem
	 */
	private String activities2XML(Collection<ActivityItem> acts,int maxPages, String currencyCode, HttpServletRequest request) throws Exception {
        BigDecimal proposedSum = new BigDecimal(0);
        BigDecimal actualSum = new BigDecimal(0);
        BigDecimal actualDisbSum = new BigDecimal(0);
        BigDecimal plannedCommitments=new BigDecimal(0);
		String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		result += "<" + ROOT_TAG;
		String temp = "";
		if (acts != null && acts.size() > 0) {
			for(ActivityItem activity : acts) {
//                activity.
                //create helper bean from activity and program percent
				ActivityItem item = new ActivityItem(activity.getAct(),currencyCode,activity.getPercent(), request);
				//get already calculated amounts from helper
				ActivityUtil.ActivityAmounts amounts = item.getAmounts();
				//calculate totals
				proposedSum =proposedSum.add( amounts.getProposedAmout());
				actualSum =actualSum.add( amounts.getActualAmount());
				actualDisbSum=actualDisbSum.add( amounts.getActualDisbAmoount());
				plannedCommitments=plannedCommitments.add(amounts.getPlannedAmount());
				//generate one activity portion of XML from helper
				temp += item.getXml();
			}
		}
		//result += " proposedSum=\"" +((proposedSum.doubleValue()!=0)? FormatHelper.formatNumber(proposedSum):0) + "\" ";
		result += " actualSum=\"" + ((actualSum.doubleValue()!=0)? FormatHelper.formatNumber(actualSum):0)+ "\" ";
		result += " actualDisbSum=\"" + ((actualDisbSum.doubleValue()!=0)? FormatHelper.formatNumber(actualDisbSum):0) + "\" ";
		result += " plannedCommSum=\"" + ((plannedCommitments.doubleValue()!=0)? FormatHelper.formatNumber(plannedCommitments):0) + "\" ";
		result += " totalPages=\""+maxPages+"\" ";
		result += ">" + temp + "</" + ROOT_TAG + ">";
		return result;
	}

			}
