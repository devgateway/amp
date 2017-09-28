package org.digijava.module.aim.action;
/*
* @ author Clipa Cornel Dan
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionServlet;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.form.ListAppliedPatchesForm;
import org.digijava.module.autopatcher.core.PatchFile;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;

public class ListAppliedPatches extends Action {
    private static Logger logger                = Logger.getLogger(ListAppliedPatches.class);
    private ActionMessages errors                   = new ActionMessages();
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
    {
        String param = request.getParameter("view");
        
        if (param != null) {
            
            File dir1 = new File(".");
            File dir2 = dir1.getCanonicalFile().getParentFile();
            
            ActionServlet serv = getServlet();
            //logger.info("alt path :"+serv.getServletContext().getRealPath("/patches/"));
            File dir = new File(serv.getServletContext().getRealPath("/patches/")+"/"+param);
            logger.info(dir.getPath());
            
            BufferedReader r = new BufferedReader(new FileReader(dir));
            
            char buffer[] = new char[(int)dir.length()];
            
            r.read(buffer,0, (int)dir.length());
            String s = new String(buffer);

            ListAppliedPatchesForm apForm = (ListAppliedPatchesForm) form;
            apForm.setContent(s);
                
            return mapping.findForward("viewPatch");
        }
        else {
            ListAppliedPatchesForm apForm = (ListAppliedPatchesForm) form;
            List patches = null;
            patches = this.getPatchNames();
            apForm.setPatch(patches);
            return mapping.findForward("forward");
        }
    }

    /**
     * function seems to return nothing
     * @return
     */
    private List getPatchNames()
    {
        Session session = null;
        List col = null;
        
        try{
            session             = PersistenceManager.getSession();

            Query query = session.createQuery("select plm from " + PatchFile.class.getName() + " plm order by plm.invoked desc");
            col = query.list();
                
            Iterator it = col.iterator();
            while (it.hasNext()) {
                PatchFile p = (PatchFile) it.next();
                //logger.info("Getting patches:" + p.getAbsolutePatchName() + "," + p.getInvoked());
            }
        }
        catch (Exception ex) {
            logger.error("Exception : " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return col;
    }
}
