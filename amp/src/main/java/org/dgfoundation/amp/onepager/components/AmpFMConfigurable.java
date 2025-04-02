/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components;

import org.apache.wicket.markup.html.panel.Panel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.FMUtil;

/**
 * Interface for accessing the AMP Feature Manager settings. 
 * All AMP interface controls implement this interface. This gives access to the FM name of each control
 * and its type. By using these two methods, it is possible to identify any FM state for any component.
 * For example for the project title, you will have {@link AmpFMConfigurable#getFMName()} equal to 
 * the 'Project Title' and the {@link AmpFMConfigurable#getFMType()} to {@link AmpFMTypes#FIELD}
 * If the feature manager database is queried using these two values it will say if the project title is
 * visible - {@link Panel#isVisible()} and enabled {@link Panel#isEnabled()}
 * @author mpostelnicu@dgateway.org since Sep 22, 2010
 * @see AmpFMTypes
 * @see FMUtil
 * @see AmpComponentPanel
 */
public interface AmpFMConfigurable {
    public AmpFMTypes getFMType();
    public String getFMName();
}
