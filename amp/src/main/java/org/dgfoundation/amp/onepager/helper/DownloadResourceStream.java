package org.dgfoundation.amp.onepager.helper;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.time.Time;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.util.SessionUtil;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.ObjectReferringDocument;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class DownloadResourceStream<T extends ObjectReferringDocument> implements IResourceStream {
    private static final Logger logger = Logger.getLogger(DownloadResourceStream.class);

    private String contentType;
    private transient Locale locale;
    private FileUpload file;
    private boolean newResource;
    private IModel<T> doc;
    private String fileName;
    private transient InputStream fileData;
    private Bytes fileSize;

    public DownloadResourceStream(FileUpload f, String fileName) {
        this(f);
        this.fileName = fileName;
    }

    public DownloadResourceStream(FileUpload f) {
        this.file = f;
        this.newResource = true;
    }

    public DownloadResourceStream(IModel<T> doc) {
        this.doc = doc;
        this.newResource = false;
    }

    public DownloadResourceStream(IModel<T> doc, String fileName) {
        this(doc);
        this.fileName = fileName;

    }


    private synchronized void initHelperFields() {
        //Singleton
        if (true || contentType == null) {
            if (newResource) {
                contentType = file.getContentType();
                fileSize = Bytes.bytes(file.getSize());
                fileName = file.getClientFileName();
                try {
                    fileData = file.getInputStream();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            } else {
                AmpAuthWebSession s = (AmpAuthWebSession) org.apache.wicket.Session.get();
                Node node = DocumentManagerUtil.getWriteNode(doc.getObject().getUuid(), SessionUtil.getCurrentServletRequest());
                NodeWrapper nw = new NodeWrapper(node);

                contentType = nw.getContentType();
                fileName = nw.getName();
                try {
                    Property data = nw.getNode().getProperty(CrConstants.PROPERTY_DATA);
                    fileData = data.getStream();

                    Property size = nw.getNode().getProperty(CrConstants.PROPERTY_FILE_SIZE);
                    fileSize = Bytes.bytes(size.getLong());
                    DocumentManagerUtil.logoutJcrSessions(SessionUtil.getCurrentServletRequest());
                } catch (RepositoryException e) {
                    logger.error("Error while getting data stream from JCR:", e);
                }
            }
        }
    }

    @Override
    public Time lastModifiedTime() {
        return null;
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public String getContentType() {
        initHelperFields();
        return contentType;
    }

    @Override
    public InputStream getInputStream() throws ResourceStreamNotFoundException {
        initHelperFields();
        return fileData;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public Bytes length() {
        initHelperFields();
        return fileSize;
    }

    @Override
    public void setLocale(Locale arg0) {
        this.locale = arg0;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getStyle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setStyle(String style) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getVariation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setVariation(String variation) {
        // TODO Auto-generated method stub

    }


}
