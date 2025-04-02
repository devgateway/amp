/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.digijava.module.aim.action;

import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.TeamMemberForm;
import org.digijava.module.um.util.AmpUserUtil;

/**
 *
 * @author medea
 */
public class SearchUser extends Action {

    private static Logger logger = Logger.getLogger(SearchUser.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //HttpSession session = request.getSession();
        String srchStr = request.getParameter("srchStr");
        TeamMemberForm upMemForm = (TeamMemberForm) form;
        Long teamId = upMemForm.getTeamId();

        List<String> srcResArray = AmpUserUtil.searchUsesers(srchStr, teamId);
        StringBuilder retVal = new StringBuilder();
        if (srcResArray != null && srcResArray.size() > 0) {
            for (String name : srcResArray) {
                 retVal.append(name);
                 retVal.append("\n");
                 
            }
        }
        int length = retVal.length();
        if (length > 0) {
           retVal.deleteCharAt(length - 1);
        }

        response.setCharacterEncoding("UTF-8");
        ServletOutputStream sos = response.getOutputStream();
        sos.write(retVal.toString().getBytes("UTF-8"));
        sos.close();

        return null;
    }
}
