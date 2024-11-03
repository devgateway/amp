package org.digijava.module.aim.action.dataimporter.util;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.action.dataimporter.dbentity.ImportStatus;
import org.digijava.module.aim.action.dataimporter.dbentity.ImportedFilesRecord;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class ImportedFileUtil {
    private static final Logger logger = LoggerFactory.getLogger(ImportedFileUtil.class);
    public static String generateSHA256Hash(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer))!= -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }
        byte[] hash = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb +"+"+file.getName();
    }

    public static ImportedFilesRecord saveFile(File file, String filename) throws IOException, NoSuchAlgorithmException {
        Session session = PersistenceManager.getRequestDBSession();
        Transaction transaction = null;
        ImportedFilesRecord importedFilesRecord = null;

        try {
            transaction = session.beginTransaction();
            String generatedHash = generateSHA256Hash(file);
            logger.info("Saving File hash is " + generatedHash);

            importedFilesRecord = new ImportedFilesRecord();
            importedFilesRecord.setFileHash(generatedHash);
            importedFilesRecord.setImportStatus(ImportStatus.UPLOADED);
            importedFilesRecord.setFileName(filename);

            session.saveOrUpdate(importedFilesRecord);
            session.flush();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();  // Roll back in case of an error
            logger.error("Error saving ImportedFilesRecord: " + e.getMessage(), e);
        }

        return importedFilesRecord;
    }
    public static void updateFileStatus(ImportedFilesRecord importDataModel, ImportStatus status) {
        logger.info("Updating file status to " + status);
        Session session = PersistenceManager.getRequestDBSession();
        importDataModel.setImportStatus(status);
        session.saveOrUpdate(importDataModel);
        session.flush();
    }
    public static List<ImportedFilesRecord> getSimilarFiles(File file) throws IOException, NoSuchAlgorithmException {
        String hash = generateSHA256Hash(file);
        logger.info("Checking File hash is " + hash);
        Session session = PersistenceManager.getRequestDBSession();
        Query query = session.createQuery("from ImportedFilesRecord where fileHash = :hash");
        query.setParameter("hash", hash);
        return query.list();
    }


}
