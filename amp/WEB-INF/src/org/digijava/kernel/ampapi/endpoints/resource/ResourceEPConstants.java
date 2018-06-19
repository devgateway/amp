package org.digijava.kernel.ampapi.endpoints.resource;

import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;

/**
 * @author Viorel Chihai
 */
public final class ResourceEPConstants {

    private ResourceEPConstants() {
    }

    public static final String RESOURCE = "resource";

    public static final String UUID = "uuid";
    public static final String TITLE = "title";
    public static final String NOTE = "note";
    public static final String DESCRIPTION = "description";
    public static final String TYPE = "type";
    public static final String WEB_LINK = "web_link";
    public static final String ADDING_DATE = "adding_date";
    
    public static final String TEAM_MEMBER = InterchangeUtils.underscorify(ResourceFieldsConstants.TEAM_MEMBER);

}
