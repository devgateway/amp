package org.digijava.module.contentrepository.helper;

public class CrConstants {
	public static final String MAKE_PUBLIC	= "make_public";
	public static final String UNPUBLISH	= "unpublish";
	
	/* Name for jcr sessions stored in http session */
	public static final String JCR_READ_SESSION						= "jcrReadSession";
	public static final String JCR_WRITE_SESSION						= "jcrWriteSession";
	
	/*  */
	public static final String GET_PUBLIC_DOCUMENTS					= "getPublicDocuments";
	
	public static final String PROPERTY_NAME						= "ampdoc:name";
	public static final String PROPERTY_TITLE						= "ampdoc:title";
	public static final String PROPERTY_DESCRIPTION					= "ampdoc:description";
	public static final String PROPERTY_NOTES						= "ampdoc:notes";
	public static final String PROPERTY_CONTENT_TYPE				= "ampdoc:contentType";
	public static final String PROPERTY_ADDING_DATE					= "ampdoc:addingDate";
	public static final String PROPERTY_CREATOR						= "ampdoc:creator";
	public static final String PROPERTY_VERSION_NUMBER				= "ampdoc:versionNumber";
	public static final String PROPERTY_DATA						= "ampdoc:data";
	public static final String PROPERTY_LINK						= "ampdoc:link";
	public static final String PROPERTY_WEB_LINK					= "ampdoc:webLink";
	public static final String PROPERTY_FILE_SIZE					= "ampdoc:fileSize";
	public static final String PROPERTY_CM_DOCUMENT_TYPE			= "ampdoc:cmDocType";
	
	public static final String TEMPORARY_UUID						= "__TEMPORARY__";
	
	public static final String URL_CONTENT_TYPE						= "URL";
	
	public static final String REQUEST_GET_SHOW_LINKS				= "showOnlyLinks";
	public static final String REQUEST_GET_SHOW_DOCS				= "showOnlyDocs";
	
	public static final String REQUEST_UPDATED_DOCUMENTS_IN_SESSION	= "updatedDocumentList";
}
