package org.digijava.module.um.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.user.User;
import org.digijava.module.um.dbentity.SuspendLogin;
import org.digijava.module.um.form.SuspendLoginManagerForm;
import org.digijava.module.um.util.DbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: flyer
 * Date: 10/1/13
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class SuspendLoginManager extends DispatchAction {
    private static Logger logger = Logger.getLogger(SuspendLoginManager.class);
    public ActionForward unspecified(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response) throws DgException {
        return view(mapping, form, request, response);
    }

    public ActionForward view(ActionMapping mapping, ActionForm form,
                              HttpServletRequest request, HttpServletResponse response) throws DgException {
        SuspendLoginManagerForm slmf = (SuspendLoginManagerForm) form;
        slmf.setSuspendLoginObjects(DbUtil.getSuspendedLoginObjs());
        return mapping.findForward("forward");
    }

    public ActionForward add(ActionMapping mapping, ActionForm form,
                              HttpServletRequest request, HttpServletResponse response) throws DgException {
        SuspendLoginManagerForm slmf = (SuspendLoginManagerForm) form;
        SuspendLogin sl = new SuspendLogin();
        sl.setActive(true);
        sl.setSuspendTil(Calendar.getInstance());
        ((SuspendLoginManagerForm) form).setCurrentObj(sl);
        return mapping.findForward("add");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form,
                             HttpServletRequest request, HttpServletResponse response) throws DgException {
        SuspendLoginManagerForm slmf = (SuspendLoginManagerForm) form;
        SuspendLogin sl = DbUtil.getSuspendedLoginObjById(slmf.getObjId());
        ((SuspendLoginManagerForm) form).setCurrentObj(sl);
        return mapping.findForward("add");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form,
                             HttpServletRequest request, HttpServletResponse response) throws DgException {
        SuspendLoginManagerForm slmf = (SuspendLoginManagerForm) form;
        SuspendLogin sl = DbUtil.getSuspendedLoginObjById(slmf.getObjId());
        DbUtil.deleteSuspendedLoginObj(sl);
        return view(mapping, form, request, response);
    }

    public ActionForward save(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) throws DgException {
        SuspendLoginManagerForm slmf = (SuspendLoginManagerForm) form;
        SuspendLogin sl = slmf.getCurrentObj();
        ActionErrors err = new ActionErrors();
        //New Object
        if (sl.getId() == 0) {
            sl.setId(null);
        }

        //Check unique name
        SuspendLogin testSl = DbUtil.getSuspendedLoginObjByName(sl.getName());
        if (testSl != null && !testSl.getId().equals(sl.getId())) {
            err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.um.suspendLoginManager.nonUniqueName"));
            saveErrors(request, err);
            return mapping.findForward("add");
        }

        //Edit
        if (sl.getId() != null) {
            SuspendLogin slFromDb = DbUtil.getSuspendedLoginObjById(sl.getId());
            slFromDb.setName(sl.getName());
            slFromDb.setReasonText(sl.getReasonText());
            slFromDb.setExpires(sl.getExpires());
            slFromDb.setActive(sl.getActive());
            slFromDb.setSuspendTil(sl.getSuspendTil());
            sl = slFromDb;
        }


        DbUtil.saveSuspendedLoginObj(sl);

        return view(mapping, form, request, response);
    }

    public ActionForward users(ActionMapping mapping, ActionForm form,
                             HttpServletRequest request, HttpServletResponse response) throws DgException {
        SuspendLoginManagerForm slmf = (SuspendLoginManagerForm) form;
        SuspendLogin sl = DbUtil.getSuspendedLoginObjById(slmf.getObjId());
        slmf.setCurrentObj(sl);

        slmf.setSuspendedUserIDs(new Long[sl.getUsers().size()]);

        int userIdx = 0;
        for (User user : sl.getUsers()) {
            slmf.getSuspendedUserIDs()[userIdx] = user.getId();
            userIdx ++;
        }

        List<User> users = DbUtil.getAllUsers();
        slmf.setAllUsers(users);

        return mapping.findForward("users");
    }

    public ActionForward saveUsers(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request, HttpServletResponse response) throws DgException {
        SuspendLoginManagerForm slmf = (SuspendLoginManagerForm) form;
        if (slmf.getObjId()==null){
            return view(mapping, form, request, response);
        }
        SuspendLogin sl = DbUtil.getSuspendedLoginObjById(slmf.getObjId());
        List<User> users = DbUtil.getAllUsers();

        Set<Long> selUserIdSet = null;
        if (slmf.getSuspendedUserIDs() != null) {
            selUserIdSet = new HashSet<Long>();
            for (int userIdIdx = 0;
                 userIdIdx < slmf.getSuspendedUserIDs().length;
                 userIdIdx ++) {
                selUserIdSet.add(slmf.getSuspendedUserIDs()[userIdIdx]);
            }
        }



        //Remove unchecked
        if (selUserIdSet != null) {
            Set <User> removeUsers = new HashSet<User>();
            for (User user : sl.getUsers()) {
                if (!selUserIdSet.contains(user.getId())) {
                    removeUsers.add(user);
                }
            }
            for (User user : removeUsers) {
                sl.getUsers().remove(user);
            }

            //Add new ones
            Set <User> addUsers = new HashSet<User>();
            for (User user : users) {
                if (selUserIdSet.contains(user.getId())) {
                    addUsers.add(user);
                }
            }
            for (User user : addUsers) {
                if (!sl.getUsers().contains(user)) {
                    sl.getUsers().add(user);
                }
            }
        } else {
            //Remove all
            sl.getUsers().clear();
        }
        DbUtil.saveSuspendedLoginObj(sl);

        return view(mapping, form, request, response);
    }



}
