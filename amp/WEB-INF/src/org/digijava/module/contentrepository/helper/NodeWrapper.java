package org.digijava.module.contentrepository.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Workspace;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import javax.jcr.version.VersionManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.jackrabbit.core.PropertyImpl;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.helper.ActivityDocumentsUtil;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.form.DocumentManagerForm;
import org.digijava.module.contentrepository.helper.template.WordOrPdfFileHelper;
import org.digijava.module.contentrepository.jcrentity.Label;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;

/**
 * a class wrapping a javax.jcr.Node instance for convenience reasons mainly (nice getters / setters which would otherwise be a soup of hardcoded strings and exception handling) 
 */
public class NodeWrapper{
    
    private static Logger logger    = Logger.getLogger(NodeWrapper.class);
    
    private Node node;
    private boolean errorAppeared   = false;
    
    public NodeWrapper() {
        
    }
    
    public NodeWrapper(Node node) {
        this.node   = node;
    }
    
    public NodeWrapper(DocumentManagerForm myForm, HttpServletRequest myRequest, Node parentNode, boolean isANewVersion, ActionMessages errors) {
        FormFile formFile = myForm.getFileData();       
        boolean isAUrl = false;
        if (myForm.getWebLink() != null && myForm.getWebLink().length() > 0) {
            isAUrl = true;
        }
        
        try {
            TeamMember teamMember = (TeamMember)myRequest.getSession().getAttribute(Constants.CURRENT_MEMBER);
            Node newNode = null;
            long docType = 0;
            if (isANewVersion) {
                Property docTypeProp = parentNode.getProperty(CrConstants.PROPERTY_CM_DOCUMENT_TYPE);
                docType = docTypeProp.getLong();
                newNode = parentNode;
                newNode.checkout();
            } else {
                String encTitle = URLEncoder.encode(myForm.getDocTitle(), "UTF-8");
                docType = myForm.getDocType();
                newNode = parentNode.addNode(encTitle);
                newNode.addMixin("mix:versionable");
            }
            
            if (isANewVersion) {
                int vernum = DocumentManagerUtil.getNextVersionNumber(newNode.getIdentifier(), myRequest);
                newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double) vernum);
            } else {
                newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double) 1.0);
            }
            
            String contentType = null;
            if (isAUrl) {
                String link = DocumentManagerUtil.processUrl(myForm.getWebLink(), myForm);
                if (link != null) {
                    newNode.setProperty(CrConstants.PROPERTY_WEB_LINK, link);
                    contentType = CrConstants.URL_CONTENT_TYPE;
                } else {
                    errorAppeared = true;
                }
            } else {
                if (!DocumentManagerUtil.validateFile(formFile, errors)) {
                    errorAppeared = true;
                } else {
                    newNode.setProperty(CrConstants.PROPERTY_DATA, formFile.getInputStream());
                    
                    contentType             = formFile.getContentType();
                    int uploadedFileSize    = formFile.getFileSize(); // This is in bytes
                    
                    newNode.setProperty( CrConstants.PROPERTY_NAME, new String(formFile.getFileName().getBytes("UTF8"), "UTF8"));
                    newNode.setProperty( CrConstants.PROPERTY_FILE_SIZE, uploadedFileSize );
                }
            }
            
            if (!errorAppeared) {
                Calendar yearOfPublicationDate = null;
                Long selYearOfPublication = myForm.getYearOfPublication();
                if (selYearOfPublication != null && selYearOfPublication.intValue() != -1) {
                    yearOfPublicationDate = Calendar.getInstance();
                    yearOfPublicationDate.set(selYearOfPublication.intValue(), 1, 1);
                }
                
                String docIndex = myForm.getDocIndex();
                String docCategory = myForm.getDocCategory();
                populateNode(isANewVersion, newNode, myForm.getDocTitle(), myForm.getDocDescription(), myForm.getDocNotes(), 
                    contentType, docType, teamMember.getEmail(), teamMember.getTeamId(), yearOfPublicationDate, docIndex, docCategory);
            }
            
            this.node = newNode;

        } catch(RepositoryException e) {
            ActionMessage error = new ActionMessage("error.contentrepository.addFile:badPath");
            errors.add("title", error);
            logger.error(error.getValues(), e);
            errorAppeared = true;
        } 
        catch (Exception e) {
            logger.error(e);
            errorAppeared = true;
        }       
    }
    
    /**
     * create document from template
     */
    public NodeWrapper(WordOrPdfFileHelper pdfOrWordFile,  HttpServletRequest myRequest, Node parentNode, boolean isANewVersion, ActionMessages errors) {
            
        try {
            TeamMember teamMember       = (TeamMember)myRequest.getSession().getAttribute(Constants.CURRENT_MEMBER);
            Node newNode    = null;
            String encTitle = URLEncoder.encode(pdfOrWordFile.getDocTitle(), "UTF-8"); //URLEncoder.encode("Simple Test", "UTF-8");
            newNode = parentNode.addNode( encTitle );
            newNode.addMixin("mix:versionable");
            
            if (isANewVersion){
                int vernum  = DocumentManagerUtil.getNextVersionNumber(newNode.getIdentifier(), myRequest);
                newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double)vernum);
            }
            else{
                newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double)1.0);
            }
            
            newNode.setProperty(CrConstants.PROPERTY_DATA, pdfOrWordFile.getContent());
            int uploadedFileSize    = pdfOrWordFile.getFileSize();
            String fileName = pdfOrWordFile.getDocTitle()+"."+pdfOrWordFile.getFileType();
            newNode.setProperty( CrConstants.PROPERTY_NAME, new String(fileName.getBytes("UTF8"), "UTF8"));
            newNode.setProperty( CrConstants.PROPERTY_FILE_SIZE, uploadedFileSize );
            
            //TODO: ask Garty whether these 2 fields should be null or fetched off pdfOrWordFile
            String docIndex = null;
            String docCategory = null;
            
            if ( !errorAppeared ) {
                Calendar yearOfPublicationDate=null;                                
                populateNode(isANewVersion, newNode, pdfOrWordFile.getDocTitle(), null, null,
                        pdfOrWordFile.getContentType(), pdfOrWordFile.getDocumentType(),  
                        teamMember.getEmail(), teamMember.getTeamId(),yearOfPublicationDate, docIndex, docCategory);
            }
            
            this.node       = newNode;

        } catch(RepositoryException e) {
            ActionMessage error = new ActionMessage("error.contentrepository.addFile:badPath");
            errors.add("title",error);
            e.printStackTrace();
            errorAppeared   = true;
        } 
        catch (Exception e) {
            e.printStackTrace();
            errorAppeared   = true;
        }
    }
    
    /**
     * 
     * @param myRequest
     * @param parentNode
     * @param originalNode from which resource we are making new copy
     */
    public NodeWrapper(HttpServletRequest myRequest,Node parentNode,Node originalNode) {
        try {
//          TeamMember teamMember       = (TeamMember)myRequest.getSession().getAttribute(Constants.CURRENT_MEMBER);
//          long docType=originalNode.getProperty(CrConstants.PROPERTY_CM_DOCUMENT_TYPE).getLong();
//          String docTitle=originalNode.getProperty(CrConstants.PROPERTY_TITLE).getString();
//          
//          Node newNode=parentNode.addNode(docTitle);
//          newNode.addMixin("mix:versionable");
//          newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double)1.0);
//          //content type and content
//          String contentType          = originalNode.getProperty(CrConstants.PROPERTY_CONTENT_TYPE).getString();
//          if(originalNode.hasProperty(CrConstants.PROPERTY_WEB_LINK)){
//              newNode.setProperty ( CrConstants.PROPERTY_WEB_LINK, originalNode.getProperty(CrConstants.PROPERTY_WEB_LINK).getValue());
//          }else{
//              newNode.setProperty(CrConstants.PROPERTY_DATA, originalNode.getProperty(CrConstants.PROPERTY_DATA).getValue());
//              newNode.setProperty( CrConstants.PROPERTY_NAME, originalNode.getProperty(CrConstants.PROPERTY_NAME).getValue());
//              newNode.setProperty( CrConstants.PROPERTY_FILE_SIZE, originalNode.getProperty(CrConstants.PROPERTY_FILE_SIZE).getValue() );
//          }
//          String description=originalNode.getProperty(CrConstants.PROPERTY_DESCRIPTION).getString();
//          if(description!=null){
//              description=URLDecoder.decode(description, "UTF-8");
//          }            
//          String docNotes=originalNode.getProperty(CrConstants.PROPERTY_NOTES).getString();
//          if(docNotes!=null){
//              description=URLDecoder.decode(docNotes, "UTF-8");
//          }
//          
//          //year of publication
//          Calendar yearOfPublication=null;
//          if(originalNode.hasProperty(CrConstants.PROPERTY_YEAR_OF_PUBLICATION)){
//              yearOfPublication=originalNode.getProperty(CrConstants.PROPERTY_YEAR_OF_PUBLICATION).getDate();
//          }
//          
//          populateNode(false, newNode, URLDecoder.decode(docTitle, "UTF-8"), description, docNotes,contentType, docType , teamMember.getEmail(), teamMember.getTeamId(),yearOfPublication );
//          this.node       = newNode;
            Node newNode=buildNewNode(myRequest, parentNode, originalNode, false);
            this.node       = newNode;
        } catch (Exception e) {
            e.printStackTrace();
            errorAppeared   = true;
        }       
    }
    
    private Node buildNewNode(HttpServletRequest myRequest, Node parentNode,Node originalNode,boolean isANewVersion) {
        try {
            TeamMember teamMember       = (TeamMember)myRequest.getSession().getAttribute(Constants.CURRENT_MEMBER);
            Node newNode    = null;
            String docTitle=originalNode.getProperty(CrConstants.PROPERTY_TITLE).getString();
            long docType = 0;
            
            if (isANewVersion){
                Property docTypeProp = parentNode.getProperty(CrConstants.PROPERTY_CM_DOCUMENT_TYPE);
                docType = docTypeProp.getLong();
                newNode     = parentNode;
                newNode.checkout();
            }
            else{               
                docType = originalNode.getProperty(CrConstants.PROPERTY_CM_DOCUMENT_TYPE).getLong();
                newNode = parentNode.addNode( docTitle );
                newNode.addMixin("mix:versionable");
            }           
            
            if (isANewVersion){
                int vernum  = DocumentManagerUtil.getNextVersionNumber(newNode.getIdentifier(), myRequest);
                newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double)vernum);
            }
            else{
                newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double)1.0);
            }
            
            //content type and content
            String contentType          = originalNode.getProperty(CrConstants.PROPERTY_CONTENT_TYPE).getString();
            if(originalNode.hasProperty(CrConstants.PROPERTY_WEB_LINK)){
                newNode.setProperty ( CrConstants.PROPERTY_WEB_LINK, originalNode.getProperty(CrConstants.PROPERTY_WEB_LINK).getValue());
            }else{
                newNode.setProperty(CrConstants.PROPERTY_DATA, originalNode.getProperty(CrConstants.PROPERTY_DATA).getValue());
                newNode.setProperty( CrConstants.PROPERTY_NAME, originalNode.getProperty(CrConstants.PROPERTY_NAME).getValue());
                newNode.setProperty( CrConstants.PROPERTY_FILE_SIZE, originalNode.getProperty(CrConstants.PROPERTY_FILE_SIZE).getValue() );
            }
            String description = decodeUTF8(originalNode.getProperty(CrConstants.PROPERTY_DESCRIPTION).getString());         
            String docNotes = decodeUTF8(originalNode.getProperty(CrConstants.PROPERTY_NOTES).getString());
            
            //year of publication
            Calendar yearOfPublication=null;
            if(originalNode.hasProperty(CrConstants.PROPERTY_YEAR_OF_PUBLICATION)){
                yearOfPublication=originalNode.getProperty(CrConstants.PROPERTY_YEAR_OF_PUBLICATION).getDate();
            }
            String docIndex;
            String docCategory;
            try {
                docIndex = decodeUTF8(originalNode.getProperty(CrConstants.PROPERTY_INDEX).getString());
            } catch (Exception e) {
                docIndex="";
            }
            try {
                docCategory = decodeUTF8(originalNode.getProperty(CrConstants.PROPERTY_CATEGORY).getString());
            } catch (Exception e) {
                docCategory="";
            }
            
            populateNode(isANewVersion, newNode, decodeUTF8(docTitle), description, docNotes, contentType, 
                    docType, teamMember.getEmail(), teamMember.getTeamId(), yearOfPublication,
                    docIndex, docCategory);
            
            return newNode;
            // this.node        = newNode;
        } catch (Exception e) {
            e.printStackTrace();
            errorAppeared   = true;
        }
        return null;
    }
    
    public NodeWrapper(HttpServletRequest myRequest, Node parentNode,Node originalNode,boolean isANewVersion) {
        try {
            Node newNode=buildNewNode(myRequest, parentNode, originalNode, isANewVersion);
            this.node       = newNode;
        } catch (Exception e) {
            e.printStackTrace();
            errorAppeared   = true;
        }
    }   
    
    
    public NodeWrapper(TemporaryDocumentData tempDoc, HttpServletRequest httpRequest, TeamMember teamMember,
            Node parentNode, boolean isANewVersion, ActionMessages errors) {
        
        FormFile formFile       = tempDoc.getFormFile(); 
        
        boolean isAUrl          = false;
        if ( tempDoc.getWebLink()!=null && tempDoc.getWebLink().length()>0 ){
//          if (tempDoc.getWebLink().indexOf("http://") >= 0){
//              tempDoc.setWebLink(tempDoc.getWebLink().replaceFirst("http://", ""));
//          }
            //for the case when the file title is missing -- we have to set it to something
            //so we'll set it to the link
            if (tempDoc.getTitle() == null) 
                tempDoc.setTitle(tempDoc.getWebLink());
            if (tempDoc.getName() == null) 
                tempDoc.setName(tempDoc.getWebLink());
            
            
            if (tempDoc.getName().indexOf("http://") >= 0){
                tempDoc.setName(tempDoc.getName().replaceFirst("http://", ""));
            }
            if (tempDoc.getTitle().indexOf("http://") >= 0){
                tempDoc.setTitle(tempDoc.getTitle().replaceFirst("http://", ""));
            }
            isAUrl              = true;
        }
        
        try {
            Node newNode    = null;
            if (isANewVersion){
                newNode     = parentNode;
                newNode.checkout();
            }
            else{
                if (tempDoc.getTitle() == null) {
                    logger.error("title is null!");
                    //use one of the translated titles
                    //it's a deeply broken document, but we can't just ignore it
                    for (String localTitle: tempDoc.getTranslatedTitles().values()) {
                        if (localTitle != null) {
                            tempDoc.setTitle(localTitle);
                            break;
                        }
                    }
                    //if it's still null, set it to "<missing title>"
                    if (tempDoc.getTitle() == null) {
                        tempDoc.setTitle("<missing title>");
                    }
 
                    
                }
                    
                String encTitle = URLEncoder.encode(tempDoc.getTitle(), "UTF-8");
                newNode = parentNode.addNode(encTitle);
                newNode.addMixin("mix:versionable");
            }
            
            if (isANewVersion){
                int vernum  = DocumentManagerUtil.getNextVersionNumber(newNode.getIdentifier(), httpRequest);
                newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double)vernum);
            }
            else{
                newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double)1.0);
            }
            String contentType          = null;
            //HashMap errors = new HashMap();
            if ( isAUrl ){
                contentType             = CrConstants.URL_CONTENT_TYPE;
                newNode.setProperty ( CrConstants.PROPERTY_WEB_LINK, tempDoc.getWebLink() );
            }
            else{
                ////System.out.println("NodeWrapper.NodeWrapper() 2");
                if(formFile != null){
                    
                    if ( !DocumentManagerUtil.checkFileSize(formFile, errors) ) {
                        errorAppeared   = true;
                    }
                    else {
                        newNode.setProperty(CrConstants.PROPERTY_DATA, formFile.getInputStream());
                        contentType             = formFile.getContentType();
                        int uploadedFileSize    = formFile.getFileSize(); // This is in bytes
                        //AMP-3468
                        newNode.setProperty( CrConstants.PROPERTY_NAME, new String(formFile.getFileName().getBytes("UTF8"), "UTF8") );
                        newNode.setProperty( CrConstants.PROPERTY_FILE_SIZE, uploadedFileSize );
                    }
                }
                else logger.error("Form file is null. It is ok if it imported using IDML");
            }
            
            if ( !errorAppeared ) {
                Calendar yearofPublicationDate=null;
                if(tempDoc.getYearofPublication()!=null){
                    Integer yearofPublication = new Integer(tempDoc.yearofPublication);
                    yearofPublicationDate= Calendar.getInstance();
                    yearofPublicationDate.set(yearofPublication.intValue(), 1, 1);
                }
                populateNode(isANewVersion,newNode, tempDoc.getTitle(), tempDoc.getDescription(), tempDoc.getNotes(), 
                    contentType, tempDoc.getCmDocTypeId(), teamMember.getEmail(), teamMember.getTeamId(), yearofPublicationDate,
                    tempDoc.getIndex(), tempDoc.getCategory());
                populateMultilingualNode(newNode,tempDoc.getTranslatedTitles(),tempDoc.getTranslatedDescriptions(),tempDoc.getTranslatedNotes());
            } 
            
            this.node       = newNode;

        } catch(RepositoryException e) {
            ActionMessage error = 
                new ActionMessage("error.contentrepository.addFile.badPath", "Error adding new document. Please make sure you specify a valid path to the local file and the file is not empty."); 
            errors.add("title", error);
            logger.error("could not add document to JCR", e);
            errorAppeared   = true;
        } 
        catch (Exception e) {
            e.printStackTrace();
            errorAppeared   = true;
        }
        
    }
    
    private void populateMultilingualNode (Node newNode,Map<String,String> translatedTitles,Map <String,String>translatedDesc,Map <String,String>translatedNotes) {
        try {
        if (translatedTitles !=null) {
            Node titleNode = newNode.addNode(CrConstants.PROPERTY_TITLE);
            for (String locale:translatedTitles.keySet()) {
                titleNode.setProperty(locale, translatedTitles.get(locale));
            }
        }
        if (translatedDesc !=null) {
            Node titleNode = newNode.addNode(CrConstants.PROPERTY_DESCRIPTION);
            for (String locale:translatedDesc.keySet()) {
                titleNode.setProperty(locale, translatedDesc.get(locale));
            }
        }
    
        if (translatedNotes !=null) {
            Node titleNode = newNode.addNode(CrConstants.PROPERTY_NOTES);
            for (String locale:translatedNotes.keySet()) {
                titleNode.setProperty(locale, translatedNotes.get(locale));
            }
        }
        }catch (Exception e){
            e.printStackTrace();
            errorAppeared   = true;
            
        }
    }
    private void populateNode(boolean isANewVersion,Node newNode, String docTitle, String docDescr, String docNotes, String contentType, Long cmDocType, 
            String user, Long teamId,Calendar yearOfPublication, String index, String category) {
        try{
            if (!isANewVersion) {
                newNode.setProperty( CrConstants.PROPERTY_CREATOR, user );
                newNode.setProperty( CrConstants.PROPERTY_CREATOR_TEAM, teamId);
            }
            if ( docDescr == null )
                docDescr = "";
            if ( docNotes == null )
                docNotes = "";
            
            String encTitle     = URLEncoder.encode(docTitle, "UTF-8");
            String encDescr     = URLEncoder.encode(docDescr, "UTF-8");
            String encNotes     = URLEncoder.encode(docNotes, "UTF-8");
            
            newNode.setProperty(CrConstants.PROPERTY_TITLE, encTitle );
            newNode.setProperty(CrConstants.PROPERTY_DESCRIPTION, encDescr );
            newNode.setProperty(CrConstants.PROPERTY_NOTES, encNotes );
            newNode.setProperty(CrConstants.PROPERTY_CONTENT_TYPE, contentType );
            newNode.setProperty(CrConstants.PROPERTY_INDEX, index);
            newNode.setProperty(CrConstants.PROPERTY_CATEGORY, category);
            
            
            Node labelContainerNode = newNode.addNode( CrConstants.LABEL_CONTAINER_NODE_NAME );
            labelContainerNode.addMixin("mix:versionable");
            
            if(cmDocType != null) newNode.setProperty( CrConstants.PROPERTY_CM_DOCUMENT_TYPE, cmDocType );
            else logger.error("Doctype is null. It is ok if the file is importing using IDML");
            newNode.setProperty( CrConstants.PROPERTY_ADDING_DATE, Calendar.getInstance());
            //year of publication
            if(yearOfPublication!=null){                
                newNode.setProperty(CrConstants.PROPERTY_YEAR_OF_PUBLICATION, yearOfPublication);
            }           
            newNode.setProperty( CrConstants.PROPERTY_VERSION_CREATOR, user );
            newNode.setProperty( CrConstants.PROPERTY_VERSION_CREATOR_TEAM, teamId);
        }
        catch (Exception e) {
            e.printStackTrace();
            errorAppeared   = true;
        }
    }

    public boolean isErrorAppeared() {
        return errorAppeared;
    }

    public void setErrorAppeared(boolean errorAppeared) {
        this.errorAppeared = errorAppeared;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
    
    public boolean saveNode(Session jcrWriteSession) {
        try {
            jcrWriteSession.save();
            VersionManager vm = jcrWriteSession.getWorkspace().getVersionManager();
            vm.checkin(node.getPath());
            logger.info(String.format("CREATED JackRabbit node with uuid = %s, name = %s", getUuid(), tryGetName()));
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    public String tryGetName(){
        try{
            return this.getName();
        }catch(Exception e){
            return "<cannot get name>";
        }
    }
    
    public String getUuid() {
        try {
            return node.getIdentifier();
        } catch (RepositoryException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String decodeUTF8(String str)
    {
        try
        {
            if (str == null)
                return null;
            return URLDecoder.decode(str, "UTF-8");
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public String getStringProperty(String propertyName)
    {
        Property value = DocumentManagerUtil.getPropertyFromNode(node, propertyName);
        if (value == null)
            return null;
        try
        {
            return decodeUTF8(value.getString());
        }
        catch(RepositoryException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public String getTitle() {
        return getTranslatedTitleByLang(TLSUtils.getLangCode());
    }

    public String getDescription() {
        return getTranslatedDescriptionByLang(TLSUtils.getLangCode());
    }

    public String getNotes() {
        // Now is a multilingual property
        return getTranslatedNoteByLang(TLSUtils.getLangCode());
    }
    
    public String getDate() {
        Property calProperty    =  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_ADDING_DATE);
        if ( calProperty != null ) {
            try {
                Calendar cal    = calProperty.getDate();
                return DocumentManagerUtil.calendarToString(cal,false);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }
        return null;
    }
    
    public String getYearOfPublication() {
        Property calProperty    =  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_YEAR_OF_PUBLICATION);
        if ( calProperty != null ) {
            try {
                Calendar cal    = calProperty.getDate();
                return DocumentManagerUtil.calendarToString(cal,true);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }
        return null;
    }
    
    public Calendar getCalendarDate() {
        Property calProperty    =  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_ADDING_DATE);
        if ( calProperty != null ) {
            try {
                Calendar cal    = calProperty.getDate();
                return cal;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }
        return null;
    }
    
    
    public double getFileSizeInMegabytes() {
        Property fileSize   =  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_FILE_SIZE);
        if ( fileSize != null ) {
            try {
                double size     = DocumentManagerUtil.bytesToMega( fileSize.getLong() );
                return size;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }
        return 0;
    }
    
    public String getContentType() {
        return getStringProperty(CrConstants.PROPERTY_CONTENT_TYPE);
    }
    
    public float getVersionNumber() {
        Property versionNumber      =  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_VERSION_NUMBER);
        if ( versionNumber != null ) {
            try {
                return (float)versionNumber.getDouble();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }
        return 0;
    }
    
    public String getName() {
        return getStringProperty(CrConstants.PROPERTY_NAME);
    }
    
    public String getWebLink() {
        return getStringProperty(CrConstants.PROPERTY_WEB_LINK);
    }
    
    public String getIndex()
    {
        return getStringProperty(CrConstants.PROPERTY_INDEX);
    }
    
    public String getCategory()
    {
        return getStringProperty(CrConstants.PROPERTY_CATEGORY);
    }
    
    public Long getCmDocTypeId() {
        Property docType            = DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_CM_DOCUMENT_TYPE);
        if ( docType != null ) {
            try{
                return docType.getLong(); 
            }
            catch ( Exception E ) {
                E.printStackTrace();
            }
        }
        return null;
    }
    
    public Collection<KeyValue> getObjectsUsingThisDocument() throws Exception {
        Collection<KeyValue> ret = new ArrayList<KeyValue>();
        if (this.node == null) {
            throw new RuntimeException("Inner node not initialized");
        }

        Collection<String> names = ActivityDocumentsUtil.getNamesOfActForDoc(node.getIdentifier());

        ret = stringColToKeyValueCol("Activities", names);

        return ret;
    }
    
    public String getCreator() {
        Property creator        =  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_CREATOR);
        if ( creator != null ) {
            try {
                return creator.getString();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }
        return null;
    }
    public Long getCreatorTeam() {
        Property creatorTeam        =  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_CREATOR_TEAM);
        if ( creatorTeam != null ) {
            try {
                return creatorTeam.getLong();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }
        return null;
    }
    
    public List<Label> getLabels() {
        ArrayList<Label> labels = new ArrayList<Label>();
        try {
            Node labelContainerNode = node.getNode(CrConstants.LABEL_CONTAINER_NODE_NAME);
            Property pVH = null;
            try {
                if (labelContainerNode.hasProperty("jcr:childVersionHistory")) {
                    pVH = labelContainerNode.getProperty("jcr:childVersionHistory");
                    VersionHistory vh = (VersionHistory) pVH.getNode();
                    VersionIterator vIter = vh.getAllVersions();
                    Version v = null;
                    while (vIter.hasNext()) {
                        v = vIter.nextVersion();
                    }
                    if (v != null) {
                        NodeIterator nIter = v.getNodes();
                        if (nIter.hasNext()) {
                            labelContainerNode = nIter.nextNode();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (labelContainerNode instanceof VersionHistory) {
                VersionHistory vh = (VersionHistory) labelContainerNode;
                NodeIterator nIter = vh.getBaseVersion().getNodes();
                if (nIter.hasNext()) {
                    labelContainerNode = nIter.nextNode();
                }
            }
            PropertyIterator pIter = labelContainerNode.getProperties();
            while (pIter.hasNext()) {
                Property p = pIter.nextProperty();
                if (p.getName().contains("ampdoc:label")) {
                    Node labelNode = p.getNode();
                    labels.add(new Label(labelNode));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return labels;
    }
    
    public void addLabel(Node label) {
        try {
            Node labelContainerNode         = null;
            try {
                labelContainerNode      = node.getNode( CrConstants.LABEL_CONTAINER_NODE_NAME );
            }
            catch (PathNotFoundException e) {
                labelContainerNode      = this.addLabelContainerNode(node);
            }
            PropertyIterator pIter      = labelContainerNode.getProperties();
            Long maxNumber              = 0L;
            while ( pIter.hasNext() ) {
                Property p          = pIter.nextProperty();
                try {
                    if ( p.getName().contains("ampdoc:label") ) {
                        Long number         = Long.parseLong( p.getName().substring("ampdoc:label".length() ) );
                        maxNumber           = (number>maxNumber)?number:maxNumber;
                    }
                }
                catch (NumberFormatException e) {
                    logger.error("Was trying to parse " + p.getName() );
                }
            }
            maxNumber ++;
            labelContainerNode.checkout();
            labelContainerNode.setProperty("ampdoc:label" + maxNumber, label);
            labelContainerNode.save();
            labelContainerNode.checkin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void removeLabel(String labelUUID) {
        try {
            Node labelContainerNode     = node.getNode( CrConstants.LABEL_CONTAINER_NODE_NAME );
            PropertyIterator pIter      = labelContainerNode.getProperties();
            while ( pIter.hasNext() ) {
                Property p          = pIter.nextProperty();
                if (p.getName().contains("ampdoc:label")) {
                    Node labelNode = p.getNode();
                    if (labelNode.getIdentifier().equals(labelUUID)) {
                        labelContainerNode.checkout();
                        p.remove();
                        break;
                    }
                }
            }
            labelContainerNode.save();
            labelContainerNode.checkin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * This should be used only for old documents which don't have a container node 
     * built at creation time (check populateNode() ).
     * This function will create another version for the node Node
     * @param node the node which will get a label container node
     * @throws RepositoryException 
     */
    private Node addLabelContainerNode( Node node ) throws RepositoryException {
        Session jcrSession  = node.getSession();
        node.checkout();
        
        Node labelContainerNode = node.addNode( CrConstants.LABEL_CONTAINER_NODE_NAME );
        labelContainerNode.addMixin("mix:versionable");
        
        this.saveNode(jcrSession);
        
        return labelContainerNode;
    }
    
    public Boolean isTeamDocument() {
        try {
            Workspace workspace         = node.getSession().getWorkspace();
            String path                 = node.getCorrespondingNodePath( workspace.getName() );
            if ( path.contains(CrConstants.TEAM_PATH_ITEM) ) {
                return new Boolean(true);
            }
            else 
                return new Boolean(false);
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        
    }
    
    public Boolean deleteNode(HttpServletRequest request) throws Exception  {
        String uuid     = node.getIdentifier();
        Boolean ret     = DocumentManagerUtil.deleteDocumentWithRightsChecking( uuid, request);
        
        DocumentManagerUtil.deleteObjectsReferringDocument(uuid, CrDocumentNodeAttributes.class.getName() );
        int delActivityDocs = DocumentManagerUtil.deleteObjectsReferringDocument(uuid, AmpActivityDocument.class.getName() );
        if ( delActivityDocs > 0 ) {
            logger.error(delActivityDocs + " AmpActivityDocument object have been deleted on deletion of referring node. " +
                    "Deletion of this node should not have been allowed.");
        }
        //delete all approved/unapproved versions and sharing records
        DocumentManagerUtil.deleteNodeStates(uuid);
        DocumentManagerUtil.deleteAllShareRecordsrelatedToResource(uuid);
        return ret;
    }
    
    public String getLastVersionUUID(HttpServletRequest request) {
        try {
            Node lv =   DocumentManagerUtil.getNodeOfLastVersion(this.getUuid(), request);
            return lv.getIdentifier();
        } catch (Exception e) {
            return null;
        }
    }
    
    private static Collection<KeyValue> stringColToKeyValueCol(String key, Collection<String> names) {
        Collection<KeyValue> ret    = new ArrayList<KeyValue>(names.size());
        Iterator<String> iter   = names.iterator();
        while ( iter.hasNext() ) {
            KeyValue kv = new KeyValue( key, iter.next() );
            ret.add(kv);
        }
        
        return ret;
    }
    

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NodeWrapper other = (NodeWrapper) obj;
        if (node == null) {
            if (other.node != null)
                return false;
        } else {
            try {
                if (!node.getIdentifier().equals(other.node.getIdentifier())) {
                    return false;
                }
            } catch (RepositoryException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    
    /**
     * Used for Wicket ,when selecting item from dropdown.
     * Only title isn't unique, together with id it becomes one
     * @return
     */
    public String getFullName(){
        return this.getTitle()+" ("+this.getUuid().hashCode()+")";
    }
    
    public Map <String, String> getTranslatedTitle () {
        return getTranslatedNode(CrConstants.PROPERTY_TITLE);
    }
    
    public Map <String, String> getTranslatedNote () {
        return getTranslatedNode(CrConstants.PROPERTY_NOTES);
    }
    
    public Map <String, String> getTranslatedDescription () {
        return getTranslatedNode(CrConstants.PROPERTY_DESCRIPTION);
    }
    
    public String getTranslatedTitleByLang (String language) {
        return getTranslatedProperty(CrConstants.PROPERTY_TITLE, language);
    }
    
    public String getTranslatedNoteByLang (String language) {
        return getTranslatedProperty(CrConstants.PROPERTY_NOTES, language);
    }
    
    public String getTranslatedDescriptionByLang (String language) {
        return getTranslatedProperty(CrConstants.PROPERTY_DESCRIPTION, language);
    }
    
    
    private String getTranslatedProperty(String fieldName, String language) {
        String value = null;
        if (ContentTranslationUtil.multilingualIsEnabled()) {
            try {
                Node titleNode = node.getNode(fieldName);
                if (titleNode != null) {
                    PropertyIterator  iterator = titleNode.getProperties();
                    while (iterator.hasNext()) {
                        PropertyImpl property = (PropertyImpl) iterator.next();
                        if (property.getName().equals(language)) {
                            value = property.getString();
                            break;
                        }
                    }
                }
            } catch (PathNotFoundException ex) {
                value = getStringProperty(fieldName);
            } catch (RepositoryException e) {
                logger.error("Exception accesing traslated titles in NodeWrapper", e);
            }
        } else {
            value = getStringProperty(fieldName);
        }
        
        return value;
    }
    
    private Map <String,String> getTranslatedNode (String fieldName) {
        Map <String, String> translatedField = new HashMap<String,String> ();
        try {
            Node titleNode = node.getNode(fieldName);
            if (titleNode != null) {
                PropertyIterator  iterator = titleNode.getProperties();
                while (iterator.hasNext()) {
                    PropertyImpl property = (PropertyImpl)iterator.next();
                    translatedField.put(property.getName(),property.getString());
                        
                }
            }
        } catch (PathNotFoundException e) {
            logger.error(e.getMessage(), e);
            translatedField.put(TLSUtils.getEffectiveLangCode(), getStringProperty(fieldName));
        } catch (RepositoryException e) {
            logger.error("Exception accesing traslated titles in NodeWrapper", e);
        }
        
        return translatedField;
    }
    
}
