package org.digijava.module.admin.util;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.admin.dbentity.FileStatus;
import org.digijava.module.admin.dbentity.ImportedFilesRecord;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Stack;

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
        return sb.toString();
    }

    public static ImportedFilesRecord saveFile(File file, String filename) throws IOException, NoSuchAlgorithmException {
        Session session = PersistenceManager.getRequestDBSession();
        String generated = generateSHA256Hash(file);
        ImportedFilesRecord importedFilesRecord = new ImportedFilesRecord();
        importedFilesRecord.setFileHash(generated);
        importedFilesRecord.setFileStatus(FileStatus.UPLOADED);
        importedFilesRecord.setFileName(filename);
        session.saveOrUpdate(importedFilesRecord);
        session.flush();
        return importedFilesRecord;
    }

    public static void updateFileStatus(ImportedFilesRecord importDataModel, FileStatus status) {
        logger.info("Updating file status to " + status);
        Session session = PersistenceManager.getRequestDBSession();
        importDataModel.setFileStatus(status);
        session.save(importDataModel);
        session.flush();
    }
    public static List<ImportedFilesRecord> getImportedFiles(File file) throws IOException, NoSuchAlgorithmException {
        String hash = generateSHA256Hash(file);
        Session session = PersistenceManager.getRequestDBSession();
        Query query = session.createQuery("from ImportedFilesRecord where fileHash = :hash");
        query.setParameter("hash", hash);
        return query.list();
    }


}
