package org.digijava.module.contentrepository.helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.util.HashMap;
import java.util.UUID;

/** Session-scoped storage for files awaiting an activity save. */
public final class StagedResourceUploadStore {

    private static final String SESSION_ATTRIBUTE = StagedResourceUploadStore.class.getName();

    private StagedResourceUploadStore() {
    }

    public static String store(HttpServletRequest request, TemporaryDocumentData upload) {
        String uploadId = UUID.randomUUID().toString();
        HttpSession session = request.getSession();
        synchronized (session) {
            getUploads(session, true).put(uploadId, upload);
        }
        return uploadId;
    }

    public static TemporaryDocumentData get(HttpServletRequest request, String uploadId) {
        if (request == null || request.getSession(false) == null || uploadId == null) {
            return null;
        }
        HttpSession session = request.getSession(false);
        synchronized (session) {
            StagedUploads uploads = getUploads(session, false);
            return uploads == null ? null : uploads.get(uploadId);
        }
    }

    public static void delete(HttpServletRequest request, String uploadId) {
        if (request == null || request.getSession(false) == null || uploadId == null) {
            return;
        }
        HttpSession session = request.getSession(false);
        TemporaryDocumentData upload;
        synchronized (session) {
            StagedUploads uploads = getUploads(session, false);
            upload = uploads == null ? null : uploads.remove(uploadId);
        }
        if (upload != null) {
            upload.deleteStagedFile();
        }
    }

    public static void clear(HttpServletRequest request) {
        if (request == null || request.getSession(false) == null) {
            return;
        }
        HttpSession session = request.getSession(false);
        synchronized (session) {
            session.removeAttribute(SESSION_ATTRIBUTE);
        }
    }

    private static StagedUploads getUploads(HttpSession session, boolean create) {
        synchronized (session) {
            StagedUploads uploads = (StagedUploads) session.getAttribute(SESSION_ATTRIBUTE);
            if (uploads == null && create) {
                uploads = new StagedUploads();
                session.setAttribute(SESSION_ATTRIBUTE, uploads);
            }
            return uploads;
        }
    }

    private static class StagedUploads extends HashMap<String, TemporaryDocumentData>
            implements HttpSessionBindingListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void valueBound(HttpSessionBindingEvent event) {
        }

        @Override
        public void valueUnbound(HttpSessionBindingEvent event) {
            for (TemporaryDocumentData upload : values()) {
                upload.deleteStagedFile();
            }
            clear();
        }
    }
}