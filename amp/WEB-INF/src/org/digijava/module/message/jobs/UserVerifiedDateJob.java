package org.digijava.module.message.jobs;

import org.digijava.kernel.user.User;
import org.digijava.module.aim.util.AmpDateUtils;
import org.digijava.module.um.util.AmpUserUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;


/**
 * Quartz job
 * TODO lets review this job!
 * @author Vazha Ezugbaia
 *
 */
public class UserVerifiedDateJob extends ConnectionCleaningJob implements StatefulJob { 
    
    @Override 
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {

        Date curDate = new Date();
        Date dateBeforeDays = AmpDateUtils.getDateBeforeDays(curDate,30);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String exDt=sdf.format(dateBeforeDays);

        Collection<User> userList=AmpUserUtil.getAllNotVerifiedUsers();
        if(userList!=null){
            for (User user : userList) {
                if (user.getCreationDate()!= null) {
                     String dt = sdf.format(user.getCreationDate());
                    if (dt.equals(exDt)) {
                        AmpUserUtil.deleteUser(user.getId());//TODO Why delete user? is this safe
                    }
                }
            }
        }
    }
}
