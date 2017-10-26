/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.SetModel;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.AmpFundingFlowsOrgRoleSelector;
import org.dgfoundation.amp.onepager.util.FMUtil;
import org.dgfoundation.amp.onepager.web.pages.OnePager;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;
import org.hibernate.Criteria;
import org.hibernate.Session;

/**
 * Various utility methods for the {@link OnePager}
 * @author mpostelnicu@dgateway.org since Nov 5, 2010
 */
public final class OnePagerUtil {

    
    /**
     * This is used by {@link #jsonMarshal(Object)}
     */
    private static final JsonFactory jf = new JsonFactory();

     /**
      * Marshall to JSON an object using the {@link ObjectMapper} (dynamically mapping)
      * @param o the object
      * @return the resulting JSON stream as a string
      */
     public static String jsonMarshal(Object o) {
            StringWriter sw = new StringWriter();
            try {
                JsonGenerator gen = jf.createJsonGenerator(sw);
                new ObjectMapper().writeValue(gen, o);
                return sw.toString();
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }
    
    protected static Logger logger = Logger.getLogger(OnePagerUtil.class);
    
    /**
     * Returns an {@link AbstractReadOnlyModel} wrapping a list from a source {@link SetModel}.
     * Useful to return a {@link ListView} compliant model out of a Hibernate {@link Set} 
     * @param <T> The type of object in the set
     * @param setModel the given set model
     * @return the returned model
     */
    public final static <T> AbstractReadOnlyModel<List<T>> getReadOnlyListModelFromSetModel(final IModel<Set<T>> setModel) {
        return new AbstractReadOnlyModel<List<T>>() {
            private static final long serialVersionUID = 3706184421459839210L;

            @Override
            public List<T> getObject() {
                if(setModel.getObject()==null) return new ArrayList<T>();
                return new ArrayList<T>(setModel.getObject());
            }
        };

    }

    /**
     * Returns an {@link AbstractReadOnlyModel} wrapping a list from a source {@link SetModel}.
     * Useful to return a {@link ListView} compliant model out of a Hibernate {@link Set} 
     * @param <T> The type of object in the set
     * @param setModel the given set model
     * @param c the comparator used to provide total ordering of elements using an internal {@link TreeSet}
     * @return the returned model
     */
    public final static <T> AbstractReadOnlyModel<List<T>> getReadOnlyListModelFromSetModel(final IModel<Set<T>> setModel, final Comparator<T> c) {
        return getReadOnlyListModelFromSetModel(setModel, c, o -> true);
    }
    
    public final static <T> AbstractReadOnlyModel<List<T>> getReadOnlyListModelFromSetModel(final IModel<Set<T>> setModel, final Comparator<T> c, SerializablePredicate<T> filterPredicate) {
        return new AbstractReadOnlyModel<List<T>>() {
            private static final long serialVersionUID = 3706184421459839210L;

            @Override
            public List<T> getObject() {
                TreeSet<T> ts = new TreeSet<T>(c);
                Set<T> filteredSet = setModel.getObject().stream().filter(filterPredicate).collect(Collectors.toSet());
                ts.addAll(filteredSet);
                
                return new ArrayList<T>(ts);
            }
        };

    }
    
    /**
     * Returns an {@link AbstractReadOnlyModel} wrapping a list from a source ArrayList.
     * Useful to return a {@link ListView} compliant model out of a Hibernate Set 
     * @param <T> The type of object in the set
     * @param list
     * @return the returned model
     */
    public final static <T> AbstractReadOnlyModel<List<T>> getReadOnlyListModelFromArray(final ArrayList<T> list) {
        return new AbstractReadOnlyModel<List<T>>() {
            private static final long serialVersionUID = 3706184421459839210L;

            @Override
            public List<T> getObject() {
                return list;
            }
        };

    }
    
    
    /**
     * Cascade the {@link #switchFmVisible(AjaxRequestTarget)} to all children of this {@link AmpComponentPanel}
     * @param target
     */
    public static void cascadeFmVisible(AjaxRequestTarget target, final boolean visible, Component c) {
        if (c instanceof MarkupContainer){
            //logger.info("Attempting cascadeFmVisible on "+c.getId());
            MarkupContainer m = (MarkupContainer) c;
            for (int i = 0; i < m.size(); i++) {
                Component component = m.get(i);
                if(component instanceof AmpComponentPanel) {
                    FMUtil.changeFmVisible(component, visible);
                }
                cascadeFmVisible(target,visible, component);
            }
        }
    }

    /**
     * Cascade the {@link #switchFmEnabled(AjaxRequestTarget)} to all children of this {@link AmpComponentPanel}
     * @param target
     */
    public static void cascadeFmEnabled(AjaxRequestTarget target, boolean enabled, Component c) {
        if (c instanceof MarkupContainer){
            MarkupContainer m = (MarkupContainer) c;
            for (int i = 0; i < m.size(); i++) {
                Component component = m.get(i);
                if(component instanceof AmpComponentPanel) {
                    FMUtil.changeFmEnabled(component, enabled);
                } else {
                    
                }
                cascadeFmEnabled(target, enabled, component);
            }
        }
    }
    

    /**
     * Use to set the slider for current component only
     * @param c - make sure that c has setMarkupId(true)
     * @return
     */
    public static String getToggleJS(Component c)
    {
        return String.format(OnePagerConst.toggleJS, c.getMarkupId());
    }

    /**
     * Use to set the slider for current component's children
     * @param c - make sure that c has setMarkupId(true)
     * @return
     */
    public static String getToggleChildrenJS(Component c)
    {
        return String.format(OnePagerConst.toggleChildrenJS, c.getMarkupId());
    }

    public static String getToggleJSPM(Component c)
    {
        return String.format(OnePagerConst.toggleJSPM, c.getMarkupId());
    }

    /**
     * Slide the section on refresh
     * Use this when c is a sibbling with the slider or the slider itself
     * @param c
     * @return
     */
    public static String getClickToggleJS(Component c){
        return String.format(OnePagerConst.clickToggleJS, c.getMarkupId());
    }

    /**
     * Slide the section on refresh
     * Use this when c is the parent to the slider
     * @param c
     * @return
     */
    public static String getClickToggle2JS(Component c){
        return String.format(OnePagerConst.clickToggle2JS, c.getMarkupId());
    }
    
    public static AmpFundingFlowsOrgRoleSelector getFundingFlowRoleSelector(final IModel<AmpFunding> model,
            IModel itemModel) {
        AmpFundingFlowsOrgRoleSelector orgRoleSelector=
        new AmpFundingFlowsOrgRoleSelector("orgRoleSelector",model, itemModel,"Funding Flows OrgRole Selector");
        orgRoleSelector.setAffectedByFreezing(false);
        return orgRoleSelector;
    }
    
    public interface SerializablePredicate<T> extends Predicate<T>, Serializable {
        
    }

}
