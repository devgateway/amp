
package org.digijava.module.contentrepository.helper;

public class CrConstants {
    public static final String MAKE_PUBLIC  = "make_public";
    public static final String UNPUBLISH    = "unpublish";
        
    /*  */
    public static final String GET_PUBLIC_DOCUMENTS                 = "getPublicDocuments";
    
    public static final String PROPERTY_NAME                        = "ampdoc:name";
    public static final String PROPERTY_TITLE                       = "ampdoc:title";
    public static final String PROPERTY_DESCRIPTION                 = "ampdoc:description";
    public static final String PROPERTY_NOTES                       = "ampdoc:notes";
    public static final String PROPERTY_CONTENT_TYPE                = "ampdoc:contentType";
    public static final String PROPERTY_ADDING_DATE                 = "ampdoc:addingDate";
    public static final String PROPERTY_CREATOR                     = "ampdoc:creator";
    public static final String PROPERTY_CREATOR_TEAM                = "ampdoc:creatorTeam";
    public static final String PROPERTY_CREATOR_CLIENT              = "ampdoc:creatorClient";
    public static final String PROPERTY_VERSION_CREATOR             = "ampdoc:versionCreator";
    public static final String PROPERTY_VERSION_CREATOR_TEAM        = "ampdoc:versionCreatorTeam";
    public static final String PROPERTY_VERSION_NUMBER              = "ampdoc:versionNumber";
    public static final String PROPERTY_DATA                        = "ampdoc:data";
    public static final String PROPERTY_LINK                        = "ampdoc:link";
    public static final String PROPERTY_WEB_LINK                    = "ampdoc:webLink";
    public static final String PROPERTY_FILE_SIZE                   = "ampdoc:fileSize";
    public static final String PROPERTY_CM_DOCUMENT_TYPE            = "ampdoc:cmDocType";
    public static final String PROPERTY_YEAR_OF_PUBLICATION         = "ampdoc:yearOfPublication";
    public static final String PROPERTY_INDEX                       = "ampdoc:index";
    public static final String PROPERTY_CATEGORY                    = "ampdoc:category";
    
    public static final String LABEL_PROPERTY_NAME                  = "amplabel:name";
    public static final String LABEL_PROPERTY_COLOR                 = "amplabel:color";
    public static final String LABEL_PROPERTY_BACKGROUNDCOLOR       = "amplabel:backgroundColor";
    public static final String LABEL_PROPERTY_CHILDREN              = "amplabel:children";
    public static final String LABEL_PROPERTY_TYPE                  = "amplabel:type";
    
    public static final String LABEL_NODE_NAME                      = "amplabel:label";
    public static final String LABEL_ROOT_NODE_NAME                 = "amplabel:rootLabel5";
    
    public static final String LABEL_TYPE_FOLDER                    = "FOLDER_LABEL";
    public static final String LABEL_TYPE_LEAF                      = "LEAF_LABEL";
    
    
    //states
    public static final Integer PENDING_STATUS                  =   0;
    public static final Integer SHARED_IN_WORKSPACE             =   1;
    public static final Integer SHARED_AMONG_WORKSPACES         =   2;
    
    public static final String SHAREABLE_WITH_TEAM              ="team";     //private space resources and pending approval resources are sharable on team level 
    public static final String SHAREABLE_WITH_OTHER_TEAMS       ="global";  //team resources are shareable on global level
    
    //which tab documents are processed
    public static final String PRIVATE_DOCS_TAB="private resources";
    public static final String TEAM_DOCS_TAB="team resources";
    public static final String SHARED_DOCS_TAB="shared resources";
    public static final String PUBLIC_DOCS_TAB="public resources";
    
    public static final String TEMPORARY_UUID                       = "__TEMPORARY__";
    
    public static final String URL_CONTENT_TYPE                     = "URL";
    
    public static final String REQUEST_GET_SHOW_LINKS               = "showOnlyLinks";
    public static final String REQUEST_GET_SHOW_DOCS                = "showOnlyDocs";
    
    public static final String REQUEST_UPDATED_DOCUMENTS_IN_SESSION = "updatedDocumentList";
    
    
    public static final Integer TEAM_RESOURCES_ADD_ONLY_WORKSP_MANAGER          = 1;
    public static final Integer TEAM_RESOURCES_ADD_ALLOWED_WORKSP_MEMBER        = 2;
    public static final Integer TEAM_RESOURCES_VERSIONING_ALLOWED_WORKSP_MEMBER = 3;
    
    public static final Integer SHARE_AMONG_WRKSPACES_ALLOWED_WM                =1;
    public static final Integer SHARE_AMONG_WRKSPACES_ALLOWED_TM                =2;
    
    public static final Integer PUBLISHING_RESOURCES_ALLOWED_ONLY_TL                    =1;
    public static final Integer PUBLISHING_RESOURCES_ALLOWED_SPECIFIC_USERS             =2;
    public static final Integer PUBLISHING_RESOURCES_ALLOWED_TM                         =3;
    
    
    public static final String TEAM_PATH_ITEM                       = "team";
    
    public static final String LABEL_CONTAINER_NODE_NAME            = "LabelContainer";
    public static final String JACKRABBIT_REPOSITORY = "JackrabbitRepository";
    
}
