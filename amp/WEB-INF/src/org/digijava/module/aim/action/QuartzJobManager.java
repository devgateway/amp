package org.digijava.module.aim.action;

import org.apache.struts.action.ActionForward;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.form.QuartzJobManagerForm;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import java.util.Date;
import org.quartz.Scheduler;
import org.quartz.TriggerUtils;
import org.quartz.SchedulerFactory;
import org.digijava.module.message.jobs.ActivityDisbursementsDatesJob;

public class QuartzJobManager extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws java.lang.Exception {

            QuartzJobManagerForm qmform=(QuartzJobManagerForm)form;

            SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
            Scheduler sched = schedFact.getScheduler();
            JobDetail ampJobDetail = new JobDetail("ampActivityDisbursementsDatesJob", null, ActivityDisbursementsDatesJob.class);
            Trigger ampJobTrigger = TriggerUtils.makeDailyTrigger(1,0);
            ampJobTrigger.setName("ampActivityDisbursementsDatesJobTrigger");
            ampJobTrigger.setStartTime(TriggerUtils.getEvenHourDate(new Date()));
            sched.scheduleJob(ampJobDetail, ampJobTrigger);
            sched.start();

            qmform.setMsText("Job Created");

            return mapping.findForward("forward");
        }
            public QuartzJobManager() {
    }
}
