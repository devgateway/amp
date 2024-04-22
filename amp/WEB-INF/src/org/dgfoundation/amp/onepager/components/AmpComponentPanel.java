/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.sections.AmpFormSectionFeaturePanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.FMFormCache;
import org.dgfoundation.amp.onepager.util.FMUtil;
import org.dgfoundation.amp.onepager.web.pages.OnePager;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.OnepagerSection;
import org.hibernate.Session;

/**
 * Basic class for AMP components. This component wraps a feature manager
 * connectivity, receiving the feature name and the fmType
 * (field,feature,module). It then queries the FM utils and determines if this
 * component is visible or not and if this component is enabled or disabled.
 * This class can be used directly to create panels or extended to make amp
 * field /feature panels
 * 
 * @author mpostelnicu@dgateway.org since Sep 22, 2010
 */
public abstract class AmpComponentPanel<T> extends Panel implements AmpFMConfigurable {

    protected String fmName;
    protected AmpFMTypes fmType;
    protected WebMarkupContainer fmBorder;
    protected IndicatingAjaxLink visibleFmButton;
    protected IndicatingAjaxLink enabledFmButton;
    protected IndicatingAjaxLink upButton;
    protected IndicatingAjaxLink downButton;
    protected IndicatingAjaxLink foldButton;
    protected AjaxCheckBox cascadeFmToChildren;

    protected boolean ignoreFmVisibility = false;
    protected boolean ignorePermissions = false;
    protected boolean isAffectedByFreezing = true;

    public boolean isIgnorePermissions() {
        return ignorePermissions;
    }

    public void setIgnorePermissions(boolean ignorePermissions) {
        this.ignorePermissions = ignorePermissions;
    }

    protected boolean ignoreFmButtonsVisibility = false;

    public IModel<T> getModel() {
        return (IModel<T>) getDefaultModel();
    }

    public String getShorterFmName() {
        int maxLen = 26;
        if (fmType.equals(AmpFMTypes.MODULE))
            maxLen = 13;
        if (getFMName().length() > maxLen)
            return getFMName().substring(0, maxLen) + "..";
        else
            return getFMName();
    }

    public AjaxCheckBox getCascadeFmToChildren() {
        return cascadeFmToChildren;
    }

    public void setCascadeFmToChildren(AjaxCheckBox cascadeFmToChildren) {
        this.cascadeFmToChildren = cascadeFmToChildren;
    }

    protected static Logger logger = Logger.getLogger(AmpComponentPanel.class);

    /**
     * 
     */
    private static final long serialVersionUID = 5847159396251223479L;

    /**
     * Constructs a new object using the component id, fmName and fmType
     * 
     * @see AmpFMTypes
     * @param id
     *            the component id
     * @param fmName
     *            the feature manager name
     * @param fmType
     *            the feature type
     */
    public AmpComponentPanel(String id, String fmName, AmpFMTypes fmType) {
        this(id, null, fmName, fmType);
    }

    /**
     * Constructs a new object using only the component id and the FM Name.
     * Assumes the FM Type is {@link AmpFMTypes#FEATURE}
     * 
     * @param id
     * @param fmName
     */
    public AmpComponentPanel(String id, String fmName) {
        this(id, null, fmName, AmpFMTypes.MODULE);
    }

    public void setIgnoreFmVisibility(boolean ignoreFmVisibility) {
        this.ignoreFmVisibility = ignoreFmVisibility;
    }

    /**
     * Switch visibility for this fm Control. Change Hide with Show for the FM
     * Button
     * 
     * @param target
     *            the ajax target
     */
    public void switchFmVisible(AjaxRequestTarget target) {
        FMUtil.switchFmVisible(AmpComponentPanel.this);
        visibleFmButton.add(new AttributeModifier("value", new Model<String>(
                (FMUtil.isFmVisible(AmpComponentPanel.this) ? "Hide" : "Show") + " " + getShorterFmName())));
        target.add(this);
        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(this));
    }

    /**
     * Switch enabling/disabling for this fm Control. Change Hide with Show for
     * the FM Button
     * 
     * @param target
     *            the ajax target
     */
    public void switchFmEnabled(AjaxRequestTarget target) {
        FMUtil.switchFmEnabled(AmpComponentPanel.this);
        enabledFmButton.add(new AttributeModifier("value", new Model<String>(
                (FMUtil.isFmEnabled(AmpComponentPanel.this) ? "Disable" : "Enable") + " " + getShorterFmName())));
        target.add(this);
        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(this));
    }

    /**
     * @param id
     * @param model
     * @param fmName
     * @param fmBehavior
     */
    public AmpComponentPanel(String id, IModel<T> model, String fmName, AmpFMTypes fmBehavior) {
        super(id, model);
        setOutputMarkupId(true);
        this.fmName = fmName;
        this.fmType = fmBehavior;
        fmBorder = new TransparentWebMarkupContainer("fmBorder");
        add(fmBorder);
        visibleFmButton = new IndicatingAjaxLink("visibleFmButton") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                FMFormCache.getInstance().disable(true);
                switchFmVisible(target);
                if (cascadeFmToChildren.getModelObject() != null && cascadeFmToChildren.getModelObject())
                    OnePagerUtil.cascadeFmVisible(target, FMUtil.isFmVisible(AmpComponentPanel.this),
                            AmpComponentPanel.this);
                FMFormCache.getInstance().enable(true);
            }
        };
        visibleFmButton.setOutputMarkupId(true);
        visibleFmButton.setVisible(false);
        add(visibleFmButton);

        enabledFmButton = new IndicatingAjaxLink("enabledFmButton") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                switchFmEnabled(target);
                if (cascadeFmToChildren.getModelObject() != null && cascadeFmToChildren.getModelObject())
                    OnePagerUtil.cascadeFmEnabled(target, FMUtil.isFmEnabled(AmpComponentPanel.this),
                            AmpComponentPanel.this);
            }
        };

        cascadeFmToChildren = new IndicatingAjaxCheckBox("cascadeFmToChildren", new Model<Boolean>()) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            }
        };

        cascadeFmToChildren.setOutputMarkupId(true);
        cascadeFmToChildren.setVisible(false);
        cascadeFmToChildren.add(new AttributeModifier("title", new Model<String>("Cascade to children")));
        add(cascadeFmToChildren);

        enabledFmButton.setOutputMarkupId(true);
        enabledFmButton.setVisible(false);
        add(enabledFmButton);

        upButton = new IndicatingAjaxLink("upButton") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                OnepagerSection os = OnePager.findByName(this.getParent().getClass().getName());
                if (os == null)
                    return;
                OnepagerSection tmpOs = OnePager.findByPosition(os.getPosition() - 1);
                if (tmpOs == null)
                    return;
                tmpOs.setPosition(tmpOs.getPosition() + 1);
                os.setPosition(os.getPosition() - 1);

                Session session = PersistenceManager.getSession();
                session.update(os);
                session.update(tmpOs);
                OnePager.sortSections(OnePager.sectionsList);
                target.appendJavaScript(
                        "var newLoc=window.location.href;newLoc=newLoc.substr(0,newLoc.lastIndexOf('?'));window.location.replace(newLoc);");
            }
        };
        add(upButton);

        downButton = new IndicatingAjaxLink("downButton") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                OnepagerSection os = OnePager.findByName(this.getParent().getClass().getName());
                if (os == null)
                    return;
                OnepagerSection tmpOs = OnePager.findByPosition(os.getPosition() + 1);
                if (tmpOs == null)
                    return;
                tmpOs.setPosition(tmpOs.getPosition() - 1);
                os.setPosition(os.getPosition() + 1);
                Session session = PersistenceManager.getRequestDBSession();
                session.update(os);
                session.update(tmpOs);
                OnePager.sortSections(OnePager.sectionsList);
                target.appendJavaScript(
                        "var newLoc=window.location.href;newLoc=newLoc.substr(0,newLoc.lastIndexOf('?'));window.location.replace(newLoc);");
            }
        };
        add(downButton);

        foldButton = new IndicatingAjaxLink("foldButton") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                OnepagerSection os = OnePager.findByName(this.getParent().getClass().getName());
                if (os == null)
                    return;
                os.setFolded(!os.getFolded());
                PersistenceManager.getSession().update(os);
                target.appendJavaScript(
                        "var newLoc=window.location.href;newLoc=newLoc.substr(0,newLoc.lastIndexOf('?'));window.location.replace(newLoc);");
            }
        };
        add(foldButton);

        boolean fmMode = ((AmpAuthWebSession) getSession()).isFmMode();
        if (this instanceof AmpFormSectionFeaturePanel && fmMode) {
            upButton.add(new AttributeModifier("title", new Model<>("Up")));
            upButton.add(
                    new AttributeModifier("src", new Model<>("/TEMPLATE/ampTemplate/img_2/onepager/up.png")));
            downButton.add(new AttributeModifier("title", new Model<>("Down")));
            downButton.add(
                    new AttributeModifier("src", new Model<>("/TEMPLATE/ampTemplate/img_2/onepager/down.png")));
        } else {
            upButton.setVisible(false);
            downButton.setVisible(false);
            foldButton.setVisible(false);
        }

        if (ignoreFmVisibility) {
            visibleFmButton.setEnabled(false);
        }

    }

    public AmpComponentPanel(String id, IModel<T> model, String fmName) {
        this(id, model, fmName, AmpFMTypes.MODULE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AmpFMTypes getFMType() {
        return fmType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFMName() {
        return fmName;
    }

    public boolean isIgnoreFmButtonsVisibility() {
        return ignoreFmButtonsVisibility;
    }

    public void setIgnoreFmButtonsVisibility(boolean ignoreFmButtonsVisibility) {
        this.ignoreFmButtonsVisibility = ignoreFmButtonsVisibility;
    }

    public boolean isAffectedByFreezing() {
        return isAffectedByFreezing;
    }

    public void setAffectedByFreezing(boolean isAffectedByFreezing) {
        this.isAffectedByFreezing = isAffectedByFreezing;
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender(); // To change body of overridden methods use File
                                // | Settings | File Templates.

        final Model<Boolean> foundEnabledChild = new Model<Boolean>(Boolean.FALSE);
        searchForEnabledChild(foundEnabledChild);
        if (Boolean.TRUE.equals(foundEnabledChild.getObject()))
            setEnabled(true);
    }

    @Override
    protected void onConfigure() {
        boolean fmMode = ((AmpAuthWebSession) getSession()).isFmMode();

        final Model<Boolean> foundEnabledChild = new Model<Boolean>(Boolean.FALSE);
        searchForEnabledChild(foundEnabledChild);

        /**
         * Do not reverse the order of fmEnabled and fmVisible
         */
        boolean isEnabledInFm = FMUtil.isFmEnabled(this); // be sure to run this
                                                            // assures that
                                                            // module inserted
                                                            // in FM
        boolean fmEnabled = (foundEnabledChild.getObject() || isEnabledInFm);
        // we should check here
        boolean fmVisible = FMUtil.isFmVisible(this);

        if (isAffectedByFreezing && this.findParent(FundingListEditor.class) == null && !fmMode) {
            // if visibility needs to be checked , this is
            // to avoid freezing "add funding item or save buttons" and if its a
            // parent of AmpFundingAmountComponent we avoid the check
            Boolean isEditableByFreezingConfiguration = org.apache.wicket.Session.get()
                    .getMetaData(OnePagerConst.ACTIVITY_FREEZING_CONFIGURATION);
            fmEnabled = fmEnabled && (isEditableByFreezingConfiguration == null || isEditableByFreezingConfiguration);
        }

        if (fmMode) {
            setEnabled(true);
            setVisible(true);
        } else {
            if (!ignorePermissions)
                setEnabled(fmEnabled);
            if (!ignoreFmVisibility)
                setVisible(fmVisible);
        }

        if (this instanceof AmpFormSectionFeaturePanel && fmMode) {
            OnepagerSection tmpos = OnePager.findByName(this.getClass().getName());
            if (tmpos != null) {
                foldButton.add(
                        new AttributeModifier("title", new Model<>((tmpos.getFolded() ? "Unfold" : "Fold"))));
                foldButton.add(new AttributeModifier("src", new Model<>(
                        "/TEMPLATE/ampTemplate/img_2/onepager/" + (tmpos.getFolded() ? "fold.png" : "unfold.png"))));
            }
        }

        if (fmMode && !ignoreFmButtonsVisibility) {
            enabledFmButton.add(new AttributeModifier("title",
                    new Model<>((fmEnabled ? "Disable" : "Enable") + " " + getFMName())));
            enabledFmButton.add(new AttributeModifier("src", new Model<>(
                    "/TEMPLATE/ampTemplate/img_2/onepager/" + (fmEnabled ? "enable.png" : "disable.png"))));
            visibleFmButton.add(new AttributeModifier("title",
                    new Model<>((fmVisible ? "Hide" : "Show") + " " + getFMName())));
            visibleFmButton.add(new AttributeModifier("src", new Model<>(
                    "/TEMPLATE/ampTemplate/img_2/onepager/" + (fmVisible ? "alt_enable.png" : "alt_disable.png"))));
            visibleFmButton.setVisible(true);
            // enabledFmButton.setVisible(true);
            cascadeFmToChildren.setVisible(true);
            String style = "border: 1px dashed #9E334D; padding: 4px;";
            fmBorder.add(new AttributeModifier("style", style));
        } else {
            enabledFmButton.setVisible(false);
            visibleFmButton.setVisible(false);
            cascadeFmToChildren.setVisible(false);
            fmBorder.add(new AttributeModifier("style", ""));
        }
        super.onConfigure();
    }

    private void searchForEnabledChild(final Model<Boolean> foundEnabledChild) {
        // Check if any child is enabled
        this.visitChildren(AmpComponentPanel.class, (child, visit) -> {
            if (Boolean.TRUE.equals(foundEnabledChild.getObject())) {
                visit.stop();
                return;
            }
            // run onConfigure for direct child
            child.configure();
            if (child.isEnabled()) {
                foundEnabledChild.setObject(Boolean.TRUE);
                visit.stop();
                return;
            }
            // visit only direct children
            visit.dontGoDeeper();
        });
    }
}
