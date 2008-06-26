package org.digijava.module.message.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.message.form.QuartzJobManagerForm;
import org.digijava.module.message.helper.QuartzJobForm;
import org.digijava.module.message.util.QuartzJobUtils;

import java.util.Date;
import java.text.SimpleDateFormat;

public class QuartzJobManager extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws java.lang.Exception {

        QuartzJobManagerForm qmform = (QuartzJobManagerForm) form;

        if ("saveJob".equals(qmform.getAction())) {
            QuartzJobForm job=new QuartzJobForm();
            job.setClassFullname(qmform.getClassFullname());
            job.setDayOfWeek(qmform.getSelectedDay());

            Date date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(qmform.getStartDateTime());
            job.setStartDateTime(date);

            job.setExeTime(qmform.getExeTime());
            job.setName(qmform.getName());

            if(!"".equals(qmform.getEndDateTime())){
                date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(qmform.getEndDateTime());
                job.setEndDateTime(date);
            }
            job.setTriggerType(qmform.getTriggerType());

            QuartzJobUtils.addJob(job);
        } else if ("addJob".equals(qmform.getAction())) {
            qmform.reset(mapping, request);
            return mapping.findForward("addJob");

        } else if ("editJob".equals(qmform.getAction())) {
            for (QuartzJobForm exJob : qmform.getJobs()) {
                if (exJob.getName().equals(qmform.getName())) {
                    if (exJob.getEndDateTime() != null) {
                        qmform.setEndDateTime(exJob.getEndDateTime().toString());
                    }
                    qmform.setStartDateTime(exJob.getStartDateTime().toString());
                    qmform.setName(exJob.getName());
                    qmform.setClassFullname(exJob.getClassFullname());
                    qmform.setJob(exJob);
                    break;
                }
            }
            return mapping.findForward("forward");

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
        }

        qmform.setJobs(QuartzJobUtils.getAllJobs());
        qmform.setAction(null);

        return mapping.findForward("forward");
    }

    public QuartzJobManager() {
    }
}
