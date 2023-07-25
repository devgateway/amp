/**
 * @author dan
 */
package org.dgfoundation.amp.visibility;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.HashMap;
import java.util.Map;


/**
 * @author dan
 *
 */
public class FieldVisibilityTag extends BodyTagSupport {


    /**
     *
     */
    private static final long serialVersionUID = -7009665621191882475L;
    private static Logger logger = Logger.getLogger(FieldVisibilityTag.class);
    private String name;
    private String feature;
    private String enabled;
    private String hasLevel;

    public String getHasLevel() {
        return hasLevel;
    }

    public void setHasLevel(String hasLevel) {
        this.hasLevel = hasLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     */
    public FieldVisibilityTag() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Transactional
    public int doStartTag() throws JspException {

        ServletContext ampContext = pageContext.getServletContext();
        HttpSession session = pageContext.getSession();
        try {
            AmpTreeVisibility ampTreeVisibility = FeaturesUtil.getAmpTreeVisibility(ampContext, session);
            if (ampTreeVisibility != null)
                if (!existFieldinDB(ampTreeVisibility)) {
                    synchronized (this) {
                        if (FeaturesUtil.getFieldVisibility(name) == null) {
                            AmpFeaturesVisibility featureByNameFromRoot = ampTreeVisibility.getFeatureByNameFromRoot(this.getFeature());
                            Long id = null;
                            if (featureByNameFromRoot != null) {
                                id = featureByNameFromRoot.getId();
                                try {//extra verification...
                                    if (FeaturesUtil.getFieldVisibility(this.getName()) != null) {
                                        FeaturesUtil.updateFieldWithFeatureVisibility(ampTreeVisibility.getFeatureByNameFromRoot(this.getFeature()).getId(), this.getName());
                                    } else {
                                        FeaturesUtil.insertFieldWithFeatureVisibility(ampTreeVisibility.getRoot().getId(), id, this.getName(), this.getHasLevel(), null);
                                    }

                                    AmpTemplatesVisibility currentTemplate = (AmpTemplatesVisibility) FeaturesUtil.getTemplateById(ampTreeVisibility.getRoot().getId());
                                    ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
                                    FeaturesUtil.setAmpTreeVisibility(ampContext, session, ampTreeVisibility);
                                } catch (DgException ex) {
                                    throw new JspException(ex);
                                }
                            } else {
                                logger.debug("Field: " + this.getName() + " has the parent: " + this.getFeature() + " which doesn't exist in DB");
                                return SKIP_BODY;
                            }
                        }
                    }
                }
            ampTreeVisibility = FeaturesUtil.getAmpTreeVisibility(ampContext, session);
            if (ampTreeVisibility != null) {
                if (!isFeatureTheParent(ampTreeVisibility)) {
                    AmpFeaturesVisibility ampFeaturesVisibility= ampTreeVisibility.getFeatureByNameFromRoot(this.getFeature());
                        System.out.println("Feature: "+this.getFeature() +" Name: "+ this.getName() +"FeatureVisibility: "+ampFeaturesVisibility);
                        FeaturesUtil.updateFieldWithFeatureVisibility(ampFeaturesVisibility.getId(), this.getName());
                        AmpTemplatesVisibility currentTemplate = FeaturesUtil.getTemplateById(ampTreeVisibility.getRoot().getId());
                        ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
                        FeaturesUtil.setAmpTreeVisibility(ampContext, session, ampTreeVisibility);
                    }

            }
//     }

        } catch (Exception e) {
            logger.error("error in field visibility. pls check the field: " + this.getName() + " or its parent: " + this.getFeature());
            logger.error(e.getMessage(), e);
        }

        return EVAL_BODY_BUFFERED;//super.doStartTag();

    }

    public int doEndTag() throws JspException {
        if (bodyContent == null) return EVAL_PAGE;//SKIP_BODY;
        if (bodyContent.getString() == null) return EVAL_PAGE;
        String bodyText = bodyContent.getString();

        try {
            ServletContext ampContext = pageContext.getServletContext();
            AmpTreeVisibility ampTreeVisibility = FeaturesUtil.getAmpTreeVisibility(ampContext, pageContext.getSession());

            /* name, feature, enable
             *
             * if feature is not in the db, error! it has to be already added this feature
             *
             *if field is not in db insert it with feature as parent
             *
             * is this feature the correct parent? if not -> error!
             *
             * if field is active then display the content
             */

            if (ampTreeVisibility != null) {
                if (!existFeature(ampTreeVisibility)) {
                    //////System.out.println("  FM ::: field:"+this.getName()+" is disabled");
                    return SKIP_BODY;
                }

                AmpFieldsVisibility ampFieldFromTree = ampTreeVisibility.getFieldByNameFromRoot(getName());

                HashMap<String, HttpSession> sessionMap = new HashMap<String, HttpSession>();
                sessionMap.put("session", pageContext.getSession());

                Map scope = PermissionUtil.getScope(pageContext.getSession());
                HttpSession requestForFields = pageContext.getSession();
                String dbgFM = (String) requestForFields.getAttribute("debugFM");

                if (isFieldActive(ampTreeVisibility)) {
                    HttpSession session = pageContext.getSession();
                    TeamMember teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
                    String isAdmin = (String) session.getAttribute("ampAdmin");
                    if (isAdmin == null) {
                        //AMP-9768
                        AmpActivityVersion editedActivity = (AmpActivityVersion) PermissionUtil.getFromScope(session, GatePermConst.ScopeKeys.ACTIVITY);
                        boolean sameTeamAsEditedActivity = false;
                        if (editedActivity != null && editedActivity.getTeam() != null && teamMember != null && editedActivity.getTeam().getAmpTeamId().equals(teamMember.getTeamId()))
                            sameTeamAsEditedActivity = true;

                        //TODO AMP-2579 this IF was added to fix null pointer temporary.
                        if (teamMember != null && !(teamMember.getTeamHead() && sameTeamAsEditedActivity)) {
                            PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, teamMember);
                            ServletRequest request = pageContext.getRequest();
                            String actionMode = (String) request.getAttribute(GatePermConst.ACTION_MODE);
                            if (ampFieldFromTree != null && ampFieldFromTree.getPermission(false) != null)
                                if (
                                        !ampFieldFromTree.canDo(GatePermConst.Actions.EDIT.equals(actionMode) ?
                                                actionMode : GatePermConst.Actions.VIEW, scope)) {
                                    ////System.out.println("        FM ::: field:"+this.getName()+" is disabled from permissions");
                                    return SKIP_BODY;
                                }
                        }
                    }
                    String output = "";
                    if (dbgFM != null && "true".compareTo(dbgFM) == 0)
                        output += this.createDebugText2(bodyText);
                    else output = bodyText;
                    pageContext.getOut().print(output);
                    //////System.out.println("FM ::: field:"+this.getName()+" is ACTIVE");
                } else {
                    //////System.out.println("  FM ::: field:"+this.getName()+" is disabled");
                    return SKIP_BODY;//the field is not active!!!
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new JspTagException(e.getMessage());
        }
        return EVAL_PAGE;//SKIP_BODY
    }

    public boolean isFieldActive(AmpTreeVisibility atv) {
        AmpTemplatesVisibility currentTemplate = (AmpTemplatesVisibility) atv.getRoot();
        if (currentTemplate != null)
            return currentTemplate.fieldExists(this.getName()); // the "if" above was a copy-paste error, checking on features instead of fields
        return false;
    }

    public boolean existFeature(AmpTreeVisibility atv) {

        AmpFeaturesVisibility featureByNameFromRoot = atv.getFeatureByNameFromRoot(this.getFeature());
        if (featureByNameFromRoot == null) return false;
        return true;
    }

    public boolean existFieldinDB(AmpTreeVisibility atv) {

        AmpFieldsVisibility fieldByNameFromRoot = atv.getFieldByNameFromRoot(this.getName());
        if (fieldByNameFromRoot == null) return false;
        return true;
    }

    public boolean isFeatureTheParent(AmpTreeVisibility atv) {
        AmpTreeVisibility featureByNameFromRoot = atv.getFeatureTreeByNameFromRoot(this.getFeature());
        //AmpFeaturesVisibility f=(AmpFeaturesVisibility) featureByNameFromRoot.getRoot();
        if (featureByNameFromRoot != null)
            if (featureByNameFromRoot.getItems() != null) {
                return featureByNameFromRoot.getItems().containsKey(this.getName());
            }
        //else ////System.out.println("errror in FM - field: "+this.getName() + " -- feature:"+this.getFeature());
        //else ////System.out.println("errror in FM - field: "+this.getName() + " -- feature:"+this.getFeature());
        return false;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }


    public String createDebugText(String input) {
        String s = new String("<script type=\"text/javascript\" language=\"JavaScript\">" +
                " function HideContent(d) { " +
                "if(d.length < 1) { return; } " +
                "alert(document.getElementById(d).id);" +
                "document.getElementById(d).style.display = \"none\";}" +
                " function ShowContent(d) {" +
                "if(d.length < 1) { return; }" +
                "alert(document.getElementById(d).id);" +
                "document.getElementById(d).style.display = \"block\";" +
                "}" +
                "</script>" +
                " <div id=\"" + this.getName() + "\">" +
                input +
                "<a " +
                "href=\"javascript: ShowContent(\'" + this.getName() + "\')\">" +
                "[show] " +
                "</a> </div>");
        return s;
    }

    public String createDebugText2(String input) {
        String s = new String("<script type=\"text/javascript\" language=\"JavaScript\">" +
                " function showInfo(d,f) { " +
                "alert(\"field: \" + d +\" ; feature: \" + f);" +
                "}" +
                "</script>" +
                " <span id=\"" + this.getName() + "\">" +
                input +
                "<a style=\"color: red;\"" +
                " href=\"javascript: showInfo(\'" + this.getName() + "\', \'" + this.getFeature() + "\')\">" +
                "[" + this.getName() + "] " +
                "</a> </span>&nbsp;&nbsp;");
        return s;
    }

}
