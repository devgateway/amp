/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager;

import org.apache.wicket.MetaDataKey;
import org.dgfoundation.amp.onepager.helper.*;
import org.digijava.kernel.startup.AmpSessionListener;
import org.digijava.module.aim.dbentity.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.HashSet;

/**
 * One Pager Constants
 * @author mpostelnicu@dgateway.org
 * since Nov 11, 2010
 */
public final class OnePagerConst {
    public final static String ONEPAGER_URL_PREFIX = "onepager";
    public final static String ONEPAGER_URL_PARAMETER_ACTIVITY = "activity";
    public final static String ONEPAGER_URL_PARAMETER_SSC = "ssc";
    public static final int STRING_VALIDATOR_MAX_LENGTH = 255;

//  public final static MetaInfo<Integer>[] adjustmentTypes=new MetaInfo[] { new MetaInfo<Integer>("Actual",Constants.ACTUAL), 
//      new MetaInfo<Integer>("Planned" ,Constants.PLANNED),  new MetaInfo<Integer>("Pipeline",Constants.ADJUSTMENT_TYPE_PIPELINE )};
//  public final static MetaInfo<Integer>[] adjustmentTypesShort=new MetaInfo[] { new MetaInfo<Integer>("Actual",Constants.ACTUAL), 
//      new MetaInfo<Integer>("Planned" ,Constants.PLANNED)};
    
    
    public static final MetaDataKey<HashSet<TemporaryActivityDocument>> RESOURCES_NEW_ITEMS = new MetaDataKey<HashSet<TemporaryActivityDocument>>(){};
    public static final MetaDataKey<HashMap<String,HashSet<TemporaryComponentFundingDocument>>> COMPONENT_FUNDING_NEW_ITEMS = new MetaDataKey<HashMap<String,HashSet<TemporaryComponentFundingDocument>>>(){};
    public static final MetaDataKey<HashSet<AmpActivityDocument>> RESOURCES_DELETED_ITEMS = new MetaDataKey<HashSet<AmpActivityDocument>>(){};
    public static final MetaDataKey<HashMap<String,HashSet<AmpComponentFundingDocument>>> COMPONENT_FUNDING_DELETED_ITEMS = new MetaDataKey<HashMap<String,HashSet<AmpComponentFundingDocument>>>(){};
    public static final MetaDataKey<HashSet<TemporaryActivityDocument>> RESOURCES_EXISTING_ITEM_TITLES = new MetaDataKey<HashSet<TemporaryActivityDocument>>(){};
    public static final MetaDataKey<HashMap<String,HashSet<TemporaryComponentFundingDocument>>> COMPONENT_FUNDING_EXISTING_ITEM_TITLES = new MetaDataKey<HashMap<String,HashSet<TemporaryComponentFundingDocument>>>(){};
    public static final MetaDataKey<HashMap<String,ResourceTranslationStore>> RESOURCES_TRANSLATIONS = new MetaDataKey<HashMap<String,ResourceTranslationStore>>(){};
    
    public static final MetaDataKey<HashSet<TemporaryGPINiDocument>> GPI_RESOURCES_NEW_ITEMS = new MetaDataKey<HashSet<TemporaryGPINiDocument>>(){};
    public static final MetaDataKey<HashSet<AmpGPINiSurveyResponseDocument>> GPI_RESOURCES_DELETED_ITEMS = new MetaDataKey<HashSet<AmpGPINiSurveyResponseDocument>>(){};

    
    public static final MetaDataKey<EditorStore> EDITOR_ITEMS = new MetaDataKey<EditorStore>(){};
    public static final MetaDataKey<HashSet<AmpAgreement>> AGREEMENT_ITEMS = new MetaDataKey<HashSet<AmpAgreement>>(){};

    public static final MetaDataKey<HashSet<AmpComments>> COMMENTS_ITEMS = new MetaDataKey<HashSet<AmpComments>>(){};
    public static final MetaDataKey<HashSet<AmpComments>> COMMENTS_DELETED_ITEMS = new MetaDataKey<HashSet<AmpComments>>(){};
    
    public static final MetaDataKey<AmpActivityFrozen> FUNDING_FREEZING_CONFIGURATION = new MetaDataKey<AmpActivityFrozen>(){};
    public static final MetaDataKey<Boolean> ACTIVITY_FREEZING_CONFIGURATION = new MetaDataKey<Boolean>(){};
    public static final MetaDataKey<Boolean> ACTIVITY_IS_AFFECTED_BY_FREEZING = new MetaDataKey<Boolean>() {
    };
    /**
     * {@linkplain http://community.jboss.org/wiki/OpenSessionInView}
     * manual session-per-conversation model
     * We do not use Wicket MetaDatKeyS because we want to store this in the big {@link HttpSession} 
     * Thus we can use a session listener to catch any {@link HttpSession#invalidate()} and close the Hibernate session too
     * @see AmpSessionListener
     */
    public static final String ONE_PAGER_HIBERNATE_SESSION_KEY =  "onePagerHibernateSessionKey";

    /**
     * NOTICE: please load this as a JS resource, DO NOT PUT JS scripts in java unless they only invoke a function
     */
    /**
     * @deprecated
     * Don't use this unless necessary, it updates all
     * sliders on page and some might get the click set 
     * more than once!
     */
    @Deprecated
    private final static String slideToggle = "$('a.slider').click(function(){$(this).siblings('div:first').slideToggle();return false;});";

    final static String toggleJS= "$('#%s').click(function(){$(this).siblings('div:first').slideToggle();return false;})";
    final static String toggleChildrenJS = "$('#%s').find('a.slider').click(function(){$(this).siblings('div:first').slideToggle();return false;})";
    final static String toggleChildrenJSComponent = "$('#%s').find('a.slider').click(function(){$(this).closest('tr.wicketFundingRowItem').find('#%s').slideToggle();return false;})";

    final static String clickToggleJS= "$('#%s').siblings('div:first').slideToggle();";
    final static String clickToggle2JS= "$('#%s').find('div:first').find('div:first').slideToggle();";
    final static String toggleJSPM ="$(document).ready(function(){$('#%s').click(function(){$(this).siblings('div:first').slideToggle();return false;});})";
}
