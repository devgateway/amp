package org.digijava.module.contentrepository.helper.template;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class PdfFileHelper {
	
	private String docTitle;
	private InputStream content;
	private String contentType;
	private int fileSize;
	
	public PdfFileHelper(){
		
	}
	
	public PdfFileHelper(String docTitle,String contentType,byte[] pdfbody){
		this.docTitle=docTitle;
		this.contentType=contentType;
		this.content=new ByteArrayInputStream(pdfbody);
		this.fileSize=pdfbody.length;
	}

		
	public String getDocTitle() {
		return docTitle;
	}

	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}

	public InputStream getContent() {
		return content;
	}

	public void setContent(InputStream content) {
		this.content = content;
	}

	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	
}
