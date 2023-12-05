package org.dgfoundation.amp.onepager.components.upload;

import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.upload.FileItem;

import java.io.*;

public class FileItemEx implements FileItem {
    private static final long serialVersionUID = 1L;
    
    private String contentType;
    private String fileName;
    private Bytes fileSize;
    private InputStream fileData;

    public FileItemEx(String fileNameIn, String contentTypeIn, InputStream fileDataIn, Bytes fileSizeIn) {
        fileName = fileNameIn;
        contentType = contentTypeIn;
        fileData = fileDataIn;
        fileSize = fileSizeIn;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return fileData;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getName() {
        return fileName;
    }

    @Override
    public boolean isInMemory() {
        return false;
    }

    @Override
    public long getSize() {
        if (fileSize == null)
            return 0;
        return fileSize.bytes();
    }

    @Override
    public byte[] get() {
        return new byte[0];
    }

    @Override
    public String getString(String s) throws UnsupportedEncodingException {
        return null;
    }

    @Override
    public String getString() {
        return null;
    }

    @Override
    public void write(File file) {
    }

    @Override
    public void delete() {
    }

    @Override
    public String getFieldName() {
        return null;
    }

    @Override
    public void setFieldName(String s) {
    }

    @Override
    public boolean isFormField() {
        return false;
    }

    @Override
    public void setFormField(boolean b) {
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return null;
    }
}
