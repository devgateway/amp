package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.util.QuartzJobUtils;
import org.digijava.module.aim.helper.QuartzJobForm;
import org.digijava.module.aim.form.QuartzJobManagerForm;
import org.digijava.module.aim.util.QuartzJobClassUtils;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QuartzJobManager extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws java.lang.Exception {

    	
		HttpSession session = request.getSession();
		String str = (String) session.getAttribute("ampAdmin");

		if (str == null || str.equals("no")) {
			  SiteDomain currentDomain = RequestUtils.getSiteDomain(request);

			  String url = SiteUtils.getSiteURL(currentDomain, request
									.getScheme(), request.getServerPort(), request
									.getContextPath());
			  url += "/aim/index.do";
			  response.sendRedirect(url);
			  return null;
		}
        QuartzJobManagerForm qmform = (QuartzJobManagerForm) form;

        if ("saveJob".equals(qmform.getAction())) {
            QuartzJobForm job=new QuartzJobForm();
            job.setClassFullname(qmform.getClassFullname());
            if (qmform.getTriggerType() == 4) {
                job.setDayOfWeek(qmform.getSelectedDay());
            }
            else{
                job.setDayOfMonth(qmform.getSelectedMonthDay());
            }
            job.setStartDateTime(qmform.getStartDateTime());
            job.setExeTime(qmform.getExeTime());
            job.setName(qmform.getName());

            if(!"".equals(qmform.getEndDateTime())){
                job.setEndDateTime(qmform.getEndDateTime());
            }
            job.setTriggerType(qmform.getTriggerType());
            if(qmform.getEditAction()!=null&&qmform.getEditAction()){
                QuartzJobUtils.reScheduleJob(job);
            }
            else{
                QuartzJobUtils.addJob(job);
            }
            qmform.reset();
        } else if ("addJob".equals(qmform.getAction())) {
            qmform.reset();
            return mapping.findForward("addJob");

        } else if ("editJob".equals(qmform.getAction())) {
            qmform.setEditAction(true);
            for (QuartzJobForm exJob : qmform.getJobs()) {
                if (exJob.getName().equals(qmform.getName())) {
                    if (exJob.getEndDateTime() != null) {
                        qmform.setEndDateTime(exJob.getEndDateTime().toString());
                    }
                    if(exJob.getStartDateTime()!=null){
                        qmform.setStartDateTime(exJob.getStartDateTime().toString());
                    }
                    qmform.setName(exJob.getName());
                    qmform.setClassFullname(exJob.getClassFullname());
                    qmform.setJob(exJob);
                    qmform.setSelectedMonthDay(exJob.getDayOfMonth());
                    qmform.setSelectedDay(exJob.getDayOfWeek());
                    if (exJob.getExeTime() != null) {
                        qmform.setExeTime(exJob.getExeTime());
                        qmform.setTriggerType(exJob.getTriggerType());
                    }
                    break;
                }
            }
            return mapping.findForward("addJob");

        } else if ("deleteJob".equals(qmform.getAction())) {
            QuartzJobUtils.deleteJob(qmform.getName());
        } else if ("pauseAll".equals(qmform.getAction())) {
            QuartzJobUtils.pauseAll();
        } else if ("resumeAll".equals(qmform.getAction())) {
            QuartzJobUtils.resumeAll();
        } else if ("pauseJob".equals(qmform.getAction())) {
            QuartzJobUtils.pauseJob(qmform.getName());
        } else if ("resumeJob".equals(qmform.getAction())) {
            QuartzJobUtils.resumeJob(qmform.getName());
        } else if ("deleteJob".equals(qmform.getAction())) {
            QuartzJobUtils.deleteJob(qmform.getName());
        }else if("serverTime".equals(qmform.getAction())){
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String dt=sdf.format(new Date());
            OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream());
            PrintWriter out = new PrintWriter(outputStream, true);
            out.print(dt);
            out.close();
            outputStream.close();
            qmform.setAction(null);
            return null;
        }
        else{
            if("runJobNow".equals(qmform.getAction())){
                 QuartzJobUtils.runJob(qmform.getName());
            }
        }
       
        qmform.setJcCol(QuartzJobClassUtils.getAllJobClasses());
        qmform.setJobs(QuartzJobUtils.getAllJobs());
        qmform.setAction(null);

        return mapping.findForward("forward");
    }

    public QuartzJobManager() {
    }
}
