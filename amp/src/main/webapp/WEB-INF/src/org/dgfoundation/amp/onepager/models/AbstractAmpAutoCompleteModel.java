/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.model.LoadableDetachableModel;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.util.AmpAutoCompleteDisplayable;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * @author mpostelnicu@dgateway.org since Oct 13, 2010
 */
public abstract class AbstractAmpAutoCompleteModel<T> extends
        LoadableDetachableModel<java.util.Collection<T>> {

    private static final Integer MAX_RESULTS_VALUE = 0;

    /**
     * {@link PARAM#EXACT_MATCH} - put true here if you want the autocomplete to
     * search for an exact text match (usually when selecting a value in the
     * dropdown {@link PARAM#MAX_RESULTS} - optionally limit the max results
     * shown
     * 
     * @author mpostelnicu@dgateway.org
     * @since Aug 12, 2011
     */
    public enum PARAM implements AmpAutoCompleteModelParam {
        MAX_RESULTS, EXACT_MATCH
    };

    private static final long serialVersionUID = 361801375994474348L;

    /**
     * Use this to add more parameters after the
     * {@link AbstractAmpAutoCompleteModel} has been initialized The internal
     * {@link #params} Map is cloned, so you can add stuff every time you
     * construct a new model
     * 
     * @return
     */
    public Map<AmpAutoCompleteModelParam, Object> getParams() {
        return params;
    }

    protected Map<AmpAutoCompleteModelParam, Object> params;
    protected String input;
    private String language;

    public Object getParam(AmpAutoCompleteModelParam p) {
        if (params == null)
            return null;
        return params.get(p);
    }

    /**
     * Quick access to the parameter {@link PARAM#EXACT_MATCH} returns its value
     * 
     * @return
     */
    protected boolean isExactMatch() {
        Boolean b = (Boolean) params.get(PARAM.EXACT_MATCH);
        if (b != null && b)
            return true;
        return false;
    }

    
    protected Criterion getTextCriterion(String propertyName, String value) {
        return getUnaccentILikeExpression(propertyName, value, MatchMode.ANYWHERE);
    }
    
    /**
     * Returns the text criterion for searching text in the autocompletemodel
     * This is used in {@link MatchMode} when {@link PARAM#EXACT_MATCH} is
     * false, or using {@link Restrictions#eq(String, Object)} when
     * {@link PARAM#EXACT_MATCH} is true This is done to simplify the search
     * scheme, which is almost identical for both searching and for selecting an
     * item
     * 
     * @param propertyName
     *            the hibernate property name where the text is stored
     * @param value
     *            the keyword to search for
     * @return a {@link Criterion} that can be added to the main
     *         {@link org.hibernate.Criteria}
     */
    protected Criterion getTextCriterion(String propertyName, String value, MatchMode matchMode) {
        return getUnaccentILikeExpression(propertyName, value, matchMode);
    }


    protected Criterion getUnaccentILikeExpression(final String propertyName, final String value, MatchMode matchMode) {
        return SQLUtils.getUnaccentILikeExpression(propertyName, value, this.language, matchMode);
    }
    
    /**
     * This recursive method adds to the sector root tree the given sector. It
     * does this only if this sector has no parent (it's a root sector).
     * Otherwise it will add itself to the parent sector tree (this becoming
     * visible in rendering). It also makes sure the parent, if not null, goes
     * through the same loop thus adding its way to the root parent. If our
     * current sector is a search hit (it was discovered by mysql search) it
     * also adds its children, if any, to the results.
     * 
     * It also ignores softdeleted entries, if said objects are softdeletable.
     * 
     * @param tree
     *            the output tree with sector results
     * @param obj
     *            the current sector to be added
     * @param searchHit
     *            if this current sector is a search hit (if it has been found
     *            externally by mysql or its through recursive call
     */
    protected void addToRootTree(Collection<AmpAutoCompleteDisplayable> tree, AmpAutoCompleteDisplayable obj,
            boolean searchHit) {
        if (obj.getParent() == null)
            tree.add(obj);
        else {
            if (searchHit)
                obj.getVisibleSiblings().addAll(obj.getNonDeletedChildren());
            obj.getParent().getVisibleSiblings().add(obj);
            addToRootTree(tree, obj.getParent(), false);
        }
    }

    /**
     * Helper method that translates the results tree into a results list,
     * displayable by the dropdown. This recursive method adds the current
     * sector to the list and then all its children
     * 
     * @param list
     *            the output list
     * @param obj
     *            the sector to be added to the output list
     */
    protected void addToRootList(Collection<AmpAutoCompleteDisplayable> list, AmpAutoCompleteDisplayable obj) {
        list.add(obj);
        Collection<? extends AmpAutoCompleteDisplayable> children = obj.getVisibleSiblings();
        for (AmpAutoCompleteDisplayable ampSector : children) {
            addToRootList(list, ampSector);
        }
    }

    /**
     * Creates the tree view for comboboxes
     * 
     * @param l
     *            the input list of objects to be displayed in tree-like
     *            structure
     * @return the list of full objects with parents that can be directly
     *         diplayed by the combobox if {@link PARAM.EXACT_MATCH} is true, do
     *         not process further
     */
    protected Collection<? extends AmpAutoCompleteDisplayable> createTreeView(Collection l) {
        Boolean b = (Boolean) params.get(PARAM.EXACT_MATCH);
        if (b != null && b)
            return l;
        Collection<AmpAutoCompleteDisplayable> ret = new ArrayList<AmpAutoCompleteDisplayable>();
        
        Set<AmpAutoCompleteDisplayable> root = new TreeSet<AmpAutoCompleteDisplayable>(new AmpAutoCompleteDisplayable.AmpAutoCompleteComparator());
        for (Object o : l)
            addToRootTree(root, (AmpAutoCompleteDisplayable) o, true);
        for (Object o : root) {
            addToRootList(ret, (AmpAutoCompleteDisplayable) o);
        }

        return ret;
    }

    /**
     * Creates a new search model for the {@link AmpAutocompleteFieldPanel}
     * 
     * @param input
     *            the search keywords or the exact match (if
     *            {@link PARAM#EXACT_MATCH} is provided)
     * @param language TODO
     * @param params
     *            extra parameters to customize the search
     */
    public AbstractAmpAutoCompleteModel(String input,
            String language, Map<AmpAutoCompleteModelParam, Object> params) {
        super();
        this.input = input;
        this.language=language;
        if (params != null) {
            this.params = new HashMap<AmpAutoCompleteModelParam, Object>(params);
        } else {
            this.params = new HashMap<AmpAutoCompleteModelParam, Object>();
        }
        if (getParam(PARAM.MAX_RESULTS) == null)
            this.params.put(PARAM.MAX_RESULTS, MAX_RESULTS_VALUE);
    }

}
