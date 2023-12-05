package org.digijava.module.aim.auth;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;


public class AmpAuthenticationStatus extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception {
        String error= request.getParameter("loginError") == null? "noError" : request.getParameter("loginError");
        OutputStreamWriter outputStream = new OutputStreamWriter( response.getOutputStream(),"UTF-8");
        PrintWriter out = new PrintWriter(outputStream, true);
        try{
            out.println(error);
            out.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        return null;
    }

}
