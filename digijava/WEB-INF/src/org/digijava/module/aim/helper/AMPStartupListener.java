/*
 * @Author Priyajith C
 */
package org.digijava.module.aim.helper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.util.DbUtil;

public class AMPStartupListener 
	extends HttpServlet 
	implements ServletContextListener {
    
    private static Logger logger = Logger.getLogger(
            AMPStartupListener.class);
    
    public void contextDestroyed(ServletContextEvent sce) {

    }
    public void contextInitialized(ServletContextEvent sce) {
    	/*
        logger.debug("In contextInitialized");
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            Connection con = session.connection();
            Statement stmt = con.createStatement();
            

             * Check whether the desc and obj is already moved 
            logger.debug("Checking the description and objectives");
            boolean moved = false;
            String qryStr = "select count(*) from amp_activity where " +
            		"author = -1";
            ResultSet rs = stmt.executeQuery(qryStr);
            if (rs.next()) {
                int cnt = rs.getInt(1);
                if (cnt > 0) {
                    moved = true;
                    logger.debug("Objectives and description already moved");
                }
            }
                
            if (!moved) {
                logger.debug("Moving Objectives and description");

                 * Move the description and objectives from amp_activity to dg_editor
                qryStr = "select site_id from dg_site where id " +
                		"not in (1,2) and name like '%AMP%'";
                rs = stmt.executeQuery(qryStr);
                String siteId = "amp";
                if (rs.next()) {
                    siteId = rs.getString(1);
                }
                
                long userId = 0;
                qryStr = "select id from dg_user where email " +
                		"like 'system@digijava.org'";
                rs = stmt.executeQuery(qryStr);
                if (rs.next()) {
                    userId = rs.getLong(1);
                }

                String description = "";
                String objectives = "";
                long activityId = 0;
                long currTime = System.currentTimeMillis();
                String objKey = "aim-obj-";
                String descKey = "aim-desc-";
                StringBuffer qryBfr = new StringBuffer();
                String tempObjKey = null;
                String tempDescKey = null;
                
                qryStr = "select amp_activity_id,description,objectives " +
                		"from amp_activity";
                rs = stmt.executeQuery(qryStr);
                boolean flag = true;
                while (rs.next()) {
                    activityId = rs.getLong(1);
                    description = rs.getString(2);
                    if (description == null || description.trim().length() == 0) {
                        description = " ";
                    }
                    objectives = rs.getString(3);
                    if (objectives == null || objectives.trim().length() == 0) {
                        objectives = " ";
                    }
                    tempObjKey = objKey + userId + "-" + currTime;
                    currTime++;
                    tempDescKey = descKey + userId + "-" + currTime; 
                    currTime++;
                    
                    Editor editor = new Editor();
                    editor.setBody(description);
                    editor.setCreationIp("127.0.0.1");
                    editor.setEditorKey(tempDescKey);
                    editor.setGroupName("others");
                    editor.setLanguage("en");
                    editor.setSiteId(siteId);
                    editor.setOrderIndex(0);
                    editor.setTitle("");
                    DbUtil.saveEditor(editor);
                    
                    editor = new Editor();
                    editor.setBody(objectives);
                    editor.setCreationIp("127.0.0.1");
                    editor.setEditorKey(tempObjKey);
                    editor.setGroupName("others");
                    editor.setLanguage("en");
                    editor.setSiteId(siteId);
                    editor.setOrderIndex(0);
                    editor.setTitle("");
                    DbUtil.saveEditor(editor);                
                    
                    qryBfr.append("update amp_activity set description = '");
                    qryBfr.append(tempDescKey);
                    qryBfr.append("',objectives = '");
                    qryBfr.append(tempObjKey);
                    if (flag) {
                        qryBfr.append("', author = -1 ");
                        flag = false;
                    } else {
                        qryBfr.append("' ");
                    }
                    qryBfr.append("where amp_activity_id = ");
                    qryBfr.append(activityId);
                    stmt.executeUpdate(qryBfr.toString());
                    qryBfr.delete(0,qryBfr.length());
                                        
                }
                logger.debug("Moved.");
            }
            
            logger.debug("Changing the language name to code");
            qryStr = "select distinct language from " +
            		"amp_application_settings where length(language) > 2";
            rs = stmt.executeQuery(qryStr);
            while (rs.next()) {
            	String lang = rs.getString(1);
            	qryStr = "select code from dg_locale" +
            			" where name = '" + lang + "'";
            	ResultSet rs1 = con.createStatement().executeQuery(qryStr);
            	while (rs1.next()) {
            		String code = rs1.getString(1);
            		qryStr = "update amp_application_settings" +
            				" set language='" + code + "' where " +
            				"language='" + lang + "'";
            		con.createStatement().executeUpdate(qryStr);
            	}
            }
            
            stmt.close();
            session.close();
            
        } catch (Exception e) {
            logger.warn("Exception from contextInitialized");
            e.printStackTrace(System.out);
            if (session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                } catch (Exception ex) {
                    logger.warn("Release session failed :" + ex);
                }
            }
        }
        */
    }
}