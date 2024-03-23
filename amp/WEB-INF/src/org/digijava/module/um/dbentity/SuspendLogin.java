package org.digijava.module.um.dbentity;

import org.digijava.kernel.user.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: flyer
 * Date: 10/1/13
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class SuspendLogin {
    private static SimpleDateFormat sdf = null;
    static {
        sdf = new SimpleDateFormat("dd/MM/yyyy");
    }


    private Long id;
    private String name;
    private String reasonText;
    private Calendar suspendTil;
    private Boolean expires;
    private Boolean active;

    private Set<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReasonText() {
        return reasonText;
    }

    public void setReasonText(String reasonText) {
        this.reasonText = reasonText;
    }

    public Calendar getSuspendTil() {
        return suspendTil;
    }

    public void setSuspendTil(Calendar suspendTil) {
        this.suspendTil = suspendTil;
    }

    public Boolean getExpires() {
        return expires;
    }

    public void setExpires(Boolean expires) {
        this.expires = expires;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    //NonPersistent
    public String getFormatedDate() {
        String retVal = null;
        if (suspendTil != null) {
            retVal = sdf.format(suspendTil.getTime());
        } else {
            retVal = "";
        }
        return retVal;
    }

    public void setFormatedDate(String val) {
        if (val != null && !val.isEmpty()) {
            StringTokenizer st = new StringTokenizer(val, "/");
            Calendar newCal = Calendar.getInstance();
            newCal.set(Calendar.HOUR, 0);
            newCal.set(Calendar.MINUTE, 0);
            newCal.set(Calendar.SECOND, 0);
            int date = Integer.parseInt(st.nextToken());
            int month = Integer.parseInt(st.nextToken());
            int year = Integer.parseInt(st.nextToken());

            newCal.set(Calendar.DAY_OF_MONTH, date);
            newCal.set(Calendar.MONTH, month - 1);
            newCal.set(Calendar.YEAR, year);

            this.suspendTil = newCal;
        }
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
