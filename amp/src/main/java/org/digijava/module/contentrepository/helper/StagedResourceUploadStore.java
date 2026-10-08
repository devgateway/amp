package org.digijava.module.contentrepository.helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/** Session-scoped storage for files awaiting an activity save. */
public final class StagedResourceUploadStore {

    private static final String SESSION_ATTRIBUTE = StagedResourceUploadStore.class.getName();
    public static final int MAX_UPLOADS_PER_SESSION = 5;
    public static final long MAX_STAGED_BYTES_PER_SESSION = 100L * 1024 * 1024;
    private static final long UPLOAD_TTL_MILLIS = 30L * 60 * 1000;
    private static final ScheduledExecutorService EXPIRATION_EXECUTOR = Executors.newSingleThreadScheduledExecutor(task -> {
        Thread thread = new Thread(task, "amp-staged-upload-expiration");
        thread.setDaemon(true);
        thread.setContextClassLoader(ClassLoader.getSystemClassLoader());
        return thread;
    });

    private StagedResourceUploadStore() {
    }

    public static String store(HttpServletRequest request, TemporaryDocumentData upload) {
        return store(request, upload, null);
        }

        public static String store(HttpServletRequest request, TemporaryDocumentData upload, String replacedUploadId) {
        String uploadId = UUID.randomUUID().toString();
        HttpSession session = request.getSession();
        synchronized (session) {
            StagedUploads uploads = getUploads(session, true);
            uploads.removeExpired();
            TemporaryDocumentData replacedUpload = uploads.getUploadWithoutExpiry(replacedUploadId);
            int retainedUploadCount = uploads.size() - (replacedUpload == null ? 0 : 1);
            long retainedBytes = uploads.getStagedBytes()
                - (replacedUpload == null ? 0 : replacedUpload.getTrueUploadedFileSize());
                long byteLimit = Math.max(MAX_STAGED_BYTES_PER_SESSION, upload.getTrueUploadedFileSize());
            if (retainedUploadCount >= MAX_UPLOADS_PER_SESSION
                    || retainedBytes + upload.getTrueUploadedFileSize() > byteLimit) {
                throw new UploadLimitExceededException();
            }
            if (replacedUpload != null) {
            uploads.removeUpload(replacedUploadId);
            replacedUpload.deleteStagedFile();
            }
                StoredUpload storedUpload = new StoredUpload(upload, System.currentTimeMillis());
                storedUpload.expirationTask = EXPIRATION_EXECUTOR.schedule(upload::deleteStagedFile,
                    UPLOAD_TTL_MILLIS, TimeUnit.MILLISECONDS);
                uploads.put(uploadId, storedUpload);
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
            return uploads == null ? null : uploads.getUpload(uploadId);
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
            upload = uploads == null ? null : uploads.removeUpload(uploadId);
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

    public static class UploadLimitExceededException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    private static class StoredUpload {
        private final TemporaryDocumentData upload;
        private final long createdAt;
        private ScheduledFuture<?> expirationTask;

        private StoredUpload(TemporaryDocumentData upload, long createdAt) {
            this.upload = upload;
            this.createdAt = createdAt;
        }

        private void cancelExpiration() {
            if (expirationTask != null) {
                expirationTask.cancel(false);
            }
        }
    }

    private static class StagedUploads extends HashMap<String, StoredUpload>
            implements HttpSessionBindingListener {
        private static final long serialVersionUID = 1L;

        private TemporaryDocumentData getUpload(String uploadId) {
            removeExpired();
            return getUploadWithoutExpiry(uploadId);
        }

        private TemporaryDocumentData getUploadWithoutExpiry(String uploadId) {
            StoredUpload upload = uploadId == null ? null : get(uploadId);
            return upload == null ? null : upload.upload;
        }

        private TemporaryDocumentData removeUpload(String uploadId) {
            removeExpired();
            StoredUpload upload = remove(uploadId);
            if (upload != null) {
                upload.cancelExpiration();
            }
            return upload == null ? null : upload.upload;
        }

        private long getStagedBytes() {
            long total = 0;
            for (StoredUpload upload : values()) {
                total += upload.upload.getTrueUploadedFileSize();
            }
            return total;
        }

        private void removeExpired() {
            long expiredBefore = System.currentTimeMillis() - UPLOAD_TTL_MILLIS;
            Iterator<Map.Entry<String, StoredUpload>> iterator = entrySet().iterator();
            while (iterator.hasNext()) {
                StoredUpload upload = iterator.next().getValue();
                if (upload.createdAt <= expiredBefore) {
                    iterator.remove();
                    upload.cancelExpiration();
                    upload.upload.deleteStagedFile();
                }
            }
        }

        @Override
        public void valueBound(HttpSessionBindingEvent event) {
        }

        @Override
        public void valueUnbound(HttpSessionBindingEvent event) {
            for (StoredUpload upload : values()) {
                upload.cancelExpiration();
                upload.upload.deleteStagedFile();
            }
            clear();
        }
    }
}