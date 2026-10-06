package org.digijava.module.fundingpledges.form;

import org.apache.log4j.Logger;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDocument;

/**
 * Shim representing a document for the purposes of Pledge Form
 * @author Dolghier Constantin
 *
 */
public class DocumentShim implements UniquelyIdentifiable{
    
    private static Logger logger = Logger.getLogger(DocumentShim.class);
    
    private long uniqueId = PledgeForm.uniqueIds.getAndIncrement();
    protected String title;
    private String fileName;
    protected String uuid;
    private String generalLink;
        
    /**
     * megabytes
     */
    private double fileSize = 0.0;
    
    DocumentShim(){};
    DocumentShim(String title, String fileName, String uuid, String generalLink){
        this.title = title;
        this.fileName = fileName;
        this.uuid = uuid;
        this.generalLink = generalLink;
    }
    
    public static DocumentShim buildFrom(FundingPledgesDocument doc){
        try{
            DocumentShim res = new DocumentShim();
            res.populateFrom(DocumentData.buildFromUuid(doc.getUuid()));
            return res;
        }
        catch(Exception e){
            logger.error("error loading document with uuid = " + doc.getUuid(), e);
            return new DocumentShim("na", "(error)", "(error): " + e.getMessage(), "#");
        }
    }
    
    public FundingPledgesDocument buildPledgeEntry(FundingPledges pledge){
        FundingPledgesDocument res = new FundingPledgesDocument();
        res.setTitle(this.getTitle());
        res.setUuid(this.getUuid());
        res.setPledgeid(pledge);
        return res;
    }
        
    public void populateFrom(DocumentData documentData){
        this.title = documentData.getTitle();
        this.fileName = documentData.getName();
        this.uuid = documentData.getUuid();
        this.fileSize = documentData.getFileSize();
        this.generalLink = documentData.getGeneralLink();
    }
    
    
    public String getFormattedSize(){
        return String.format("%.2f", fileSize);
    }
    
    @Override public long getUniqueId(){
        return this.uniqueId;
    }
    
    public String getTitle(){
        return title;
    }
    
    public void setTitle(String title){ // for Struts in PledgeForm
        this.title = title;
    }
    
    public String getFileName(){
        return fileName;
    }
    
    public String getUuid(){
        return uuid;
    }
    
    public String getGeneralLink(){
        return generalLink;
    }
    
    public Long getFileSizeInBytes(){
        return (long) this.fileSize * (1024 * 1024l);
    }
}
