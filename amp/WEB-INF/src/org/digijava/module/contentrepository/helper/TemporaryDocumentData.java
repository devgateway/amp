/**
 * 
 */
package org.digijava.module.contentrepository.helper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.contentrepository.form.DocumentManagerForm;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

/**
 * @author Alex Gartner
 *
 */
public class TemporaryDocumentData extends DocumentData {
    private boolean errorsFound;
    private int trueUploadedFileSize;
    private FormFile formFile;
    private static Logger logger    = Logger.getLogger(TemporaryDocumentData.class);
    
    public int getTrueUploadedFileSize() {
        return trueUploadedFileSize;
    }
    public void setTrueUploadedFileSize(int trueUploadedFileSize) {
        this.trueUploadedFileSize = trueUploadedFileSize;
    }
    public boolean isErrorsFound() {
        return errorsFound;
    }
    public void setErrorsFound(boolean errorsFound) {
        this.errorsFound = errorsFound;
    }   
    public FormFile getFormFile() {
        return formFile;
    }
    public void setFormFile(FormFile formFile) {
        this.formFile = formFile;
    }
    
    public TemporaryDocumentData (){
        
    }
    
    public TemporaryDocumentData (DocumentManagerForm dmForm, HttpServletRequest request, ActionMessages errors) {
        errorsFound     = false;
        //HashMap errors = new HashMap();
        if ( dmForm.getFileData() != null ) {
            FormFile formFile   = dmForm.getFileData();
            if ( DocumentManagerUtil.checkFileSize(formFile, errors) ) {
                try {
                    this.setName( new String(formFile.getFileName().getBytes("UTF8"), "UTF8"));
                } catch (UnsupportedEncodingException e) {
                    logger.error(e);
                }
                this.setTrueUploadedFileSize( formFile.getFileSize() );
                this.setFileSize( DocumentManagerUtil.bytesToMega(trueUploadedFileSize) );
                this.setContentType( formFile.getContentType() );
                this.setFormFile( formFile );
            }
            else {
                errorsFound = true;
            }
        }
        
        if ( dmForm.getWebLink() != null) {
            String webLink;
            if ( (webLink=DocumentManagerUtil.processUrl(dmForm.getWebLink(), dmForm)) != null ) {
                    this.setWebLink( webLink );
                    this.setContentType( CrConstants.URL_CONTENT_TYPE );
            }
            else
                errorsFound = true;
        }
        
        this.setTitle( dmForm.getDocTitle() );
        this.setDescription( dmForm.getDocDescription() );
        this.setNotes( dmForm.getDocNotes() );
        this.setCalendar( DocumentManagerUtil.calendarToString(Calendar.getInstance(),false));
        //year of publication
        Calendar yearOfPublicationDate = Calendar.getInstance();
        Long selYearOfPublication=dmForm.getYearOfPublication();
        if(selYearOfPublication!=null && selYearOfPublication.intValue()!=-1){
            //yearOfPublicationDate=Calendar.getInstance();
            yearOfPublicationDate.set(selYearOfPublication.intValue(), 1, 1);
        }
        this.setYearofPublication(DocumentManagerUtil.calendarToString(yearOfPublicationDate,true));
        
        this.setHasViewRights(true);
        this.setHasVersioningRights(false);
        this.setShowVersionHistory(false);
        this.setCmDocTypeId( dmForm.getDocType() );
        
        this.process();
        this.computeIconPath(true);
    }
    
    public void addToSession(HttpServletRequest request) {
        ArrayList<DocumentData> list = DocumentManagerUtil.retrieveTemporaryDocDataList(request);
        list.add(this);
        this.setUuid(CrConstants.TEMPORARY_UUID + (list.size() - 1));
    }
    
    public NodeWrapper saveToRepository(HttpServletRequest request) {
        TeamMember teamMember = (TeamMember) request.getSession().getAttribute(Constants.CURRENT_MEMBER);

        return saveToRepository(request, teamMember);
    }
    
    public NodeWrapper saveToRepository(HttpServletRequest request, TeamMember teamMember) {
        Session jcrWriteSession = DocumentManagerUtil.getWriteSession(request);
        Node homeNode = DocumentManagerUtil.getOrCreateUserPrivateNode(jcrWriteSession, teamMember);
        NodeWrapper nodeWrapper = new NodeWrapper(this, request, teamMember, homeNode, false);

        if (!nodeWrapper.isErrorAppeared()) {
            if (nodeWrapper.saveNode(jcrWriteSession)) {
                return nodeWrapper;
            }
        }

        return null;
    }
    
    @Override
    public String getNodeVersionUUID () {
        return this.uuid;
    }
}
