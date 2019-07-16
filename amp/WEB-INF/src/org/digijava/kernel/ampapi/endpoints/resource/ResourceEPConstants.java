package org.digijava.kernel.ampapi.endpoints.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public static final String FILE_NAME = "file_name";
    public static final String ADDING_DATE = "adding_date";
    public static final String TEAM = "team";
    public static final String CREATOR_EMAIL = "creator_email";
    public static final String PRIVATE = "private";
    public static final String RESOURCE_TYPE = "resource_type";
    
    public static final String LINK = "link";
    public static final String FILE = "file";
    
    public static final List<String> RESOURCE_TYPES = Collections.unmodifiableList(new ArrayList<String>() {{
        this.add(LINK);
        this.add(FILE);
    }});
    
}
