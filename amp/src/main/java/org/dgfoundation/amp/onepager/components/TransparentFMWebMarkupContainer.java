/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
*/
package org.dgfoundation.amp.onepager.components;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerApp;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.FMInfo;
import org.dgfoundation.amp.onepager.util.FMUtil;
import org.dgfoundation.amp.onepager.util.FMUtil.PathException;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.module.aim.util.FeaturesUtil;

import javax.servlet.ServletContext;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * Component used by the wicket:fm tag in order to
 * verify if all the listed AmpModules are visible
 * and modifies current markupContainter visibility
 * 
 * @author aartimon@dginternational.org
 * @since Nov 21, 2011
 */
public class TransparentFMWebMarkupContainer extends TransparentWebMarkupContainer {
    protected static Logger logger = Logger.getLogger(TransparentFMWebMarkupContainer.class); 
    private static final long serialVersionUID = 1L;
    private Model<String> model;

    public TransparentFMWebMarkupContainer(String id, Model<String> model) {
        super(id);
        this.model = model;
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        LinkedList<FMInfo> fmList;
        try {
            fmList = FMUtil.getFmPath(this);
        } catch (PathException e) {
            return;
        }
        String fmPath = FMUtil.getFmPathString(fmList);
        String names = (String) model.getObject();
        boolean allInvisible = true;
        if (names != null){
            ServletContext context   = ((WebApplication)Application.get()).getServletContext();
            AmpAuthWebSession session = (AmpAuthWebSession) org.apache.wicket.Session.get();
            AmpTreeVisibility ampTreeVisibility=FeaturesUtil.getAmpTreeVisibility(context, session.getHttpSession());

            StringTokenizer tok = new StringTokenizer(names, ";");
            while (tok.hasMoreElements()) {
                String name = (String) tok.nextElement();
                String fmPathString = fmPath + "/" + name.replaceAll("/", " ");;
                if(ampTreeVisibility!=null)
                    if (FMUtil.checkIsVisible(ampTreeVisibility, fmPathString, AmpFMTypes.MODULE)){
                        allInvisible = false;
                        break;
                    }
            }
        }
        boolean fmMode = ((AmpAuthWebSession)getSession()).isFmMode();
        if( OnePagerApp.IS_DEVELOPMENT_MODE ) {
            checkInvalidFMDeclarations(names+";");
        }
        if (allInvisible) {
            this.setVisible(fmMode?true:false);
        }
    }
    
    private void checkInvalidFMDeclarations(final String fmNames){
        final String markupStr = this.getMarkup().toString();
        final String markupFileName = FilenameUtils.getName(this.getMarkup().getMarkupResourceStream().locationAsString());
        Component parent = this.findParent(AmpComponentPanel.class);
        //final String parentMarkupFileName = FilenameUtils.getName(parent.getMarkup().getMarkupResourceStream().locationAsString());
        ((AmpComponentPanel)parent).visitChildren(AmpComponentPanel.class, new IVisitor<AmpComponentPanel, Object>() {
            @Override
            public void component(AmpComponentPanel child, IVisit<Object> visit ) {
                //validate children from current markup only
                String wickedId = "wicket:id=\""+child.getId()+"\"";
                boolean check = markupStr.contains(wickedId);
                if( check ){
                    if(!fmNames.contains(child.getFMName()+";")){
                        logger.error(wickedId+" has \""+child.getFMName()+"\" not declared in the FM names=\""+fmNames+"\", file:"+markupFileName);
                    }
                }
                visit.dontGoDeeper();
            }
        });
    }
}
