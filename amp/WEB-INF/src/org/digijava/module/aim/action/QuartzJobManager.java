package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.form.QuartzJobManagerForm;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.QuartzJobForm;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.QuartzJobClassUtils;
import org.digijava.module.aim.util.QuartzJobUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



public class QuartzJobManager extends Action {
    private static SimpleDateFormat dateFormatOnly = new SimpleDateFormat(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_DATE_FORMAT));
    private static SimpleDateFormat fullDateFormat = new SimpleDateFormat(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_DATE_FORMAT) + " HH:mm:ss");
    private static java.text.DecimalFormat formatHM = new DecimalFormat("00");
    
    public static final Logger logger = Logger.getLogger(QuartzJobManager.class);
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        QuartzJobManagerForm qmform = (QuartzJobManagerForm) form;
        qmform.setInvalidEndDate(false);
        qmform.setInvalidTrigger(false);
        
        if ("saveJob".equals(qmform.getAction())) {
            //check uniqueness
            boolean jobWithSameNameExists = false;
            if(qmform.getEditAction()!=null){
                if(!qmform.getEditAction()){
                    if(QuartzJobUtils.getJobByName(qmform.getName())!=null){
                        jobWithSameNameExists = true;
                    }
                }
            }
            
            if(jobWithSameNameExists){
                ActionMessages errors= new ActionMessages();
                errors.add("name not unique", new ActionMessage("admin.jobs.duplicateName","Job name should be unique"));
                saveErrors(request, errors);
                return mapping.findForward("addJob");
            }
            
            QuartzJobForm job = new QuartzJobForm();
            job.setClassFullname(qmform.getClassFullname());
            if (qmform.getTriggerType() == 4) {
                job.setDayOfWeek(qmform.getSelectedDay());
            } else {
                job.setDayOfMonth(qmform.getSelectedMonthDay());
            }
            job.setStartDateTime(qmform.getStartDateTime());
            job.setStartH(qmform.getStartH());
            job.setStartM(qmform.getStartM());

            job.setExeTimeH(qmform.getExeTimeH());
            job.setExeTimeM(qmform.getExeTimeM());

            job.setName(qmform.getName());

            if (!"".equals(qmform.getEndDateTime())) {
                job.setEndDateTime(qmform.getEndDateTime());
                job.setEndH(qmform.getEndH());
                job.setEndM(qmform.getEndM());

            }

            Date startDate = fullDateFormat.parse(job.getStartDateTime()+" "+qmform.getStartH()+":"+qmform.getStartM()+":00");
            Date endDate;
            if (job.getEndDateTime() != null && job.getEndDateTime().trim().compareTo("") != 0)
                endDate = fullDateFormat.parse(job.getEndDateTime()+" "+qmform.getEndH()+":"+qmform.getEndM()+":00");
            else
                endDate = null;

            job.setTriggerType(qmform.getTriggerType());

            if (endDate != null && startDate.after(endDate)) {
                qmform.setInvalidEndDate(true);
                return mapping.findForward("addJob");
            }

            try {

                if (qmform.getEditAction() != null && qmform.getEditAction()) {
                    QuartzJobUtils.reScheduleJob(job);
                } else {
                    // add job validation
                    QuartzJobUtils.addJob(job);
                }
                qmform.reset();
            } catch (ClassNotFoundException exc)  {
                qmform.setInvalidClass(true);
                logger.error("Class not found", exc);
                return mapping.findForward("addJob");
            } catch (Exception e) {
                qmform.setInvalidTrigger(true);
                logger.error("Some other error", e);
                return mapping.findForward("addJob");
            }

        } else if ("addJob".equals(qmform.getAction())) {
            qmform.resetJobForm();
            return mapping.findForward("addJob");

        } else if ("editJob".equals(qmform.getAction())) {
            qmform.setEditAction(true);

            for (QuartzJobForm exJob : qmform.getJobs()) {
                if (exJob.getName().equals(qmform.getName())) {
                    if (exJob.getEndDateTime() != null) {
                        Date endDate = fullDateFormat.parse(exJob.getEndDateTime());
                        qmform.setEndDateTime(dateFormatOnly.format(endDate));
                        qmform.setEndH(formatHM.format(endDate.getHours()));
                        qmform.setEndM(formatHM.format(endDate.getMinutes()));
                    } else {
                        qmform.setEndDateTime("");
                    }
                    
                    if (exJob.getStartDateTime() != null) {
                        Date startDate = fullDateFormat.parse(exJob.getStartDateTime());
                        qmform.setStartDateTime(dateFormatOnly.format(startDate));
                        qmform.setStartH(formatHM.format(startDate.getHours()));
                        qmform.setStartM(formatHM.format(startDate.getMinutes()));
                    } else {
                        qmform.setStartDateTime("");
                    }
                    
                    qmform.setName(exJob.getName());
                    qmform.setClassFullname(exJob.getClassFullname());
                    qmform.setJob(exJob);
                    qmform.setSelectedMonthDay(exJob.getDayOfMonth());
                    qmform.setSelectedDay(exJob.getDayOfWeek());
                    if (exJob.getExeTimeH() != null) {
                        qmform.setExeTimeH(formatHM.format(Long.parseLong(exJob.getExeTimeH())));
                    }
                    if (exJob.getExeTimeM() != null) {
                        qmform.setExeTimeM(formatHM.format(Long.parseLong(exJob.getExeTimeM())));
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
        } else if ("serverTime".equals(qmform.getAction())) {
            String dt = fullDateFormat.format(new Date());
            OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream());
            PrintWriter out = new PrintWriter(outputStream, true);
            out.print(dt);
            out.close();
            outputStream.close();
            qmform.setAction(null);
            return null;
        } else {
            if ("runJobNow".equals(qmform.getAction())) {
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
