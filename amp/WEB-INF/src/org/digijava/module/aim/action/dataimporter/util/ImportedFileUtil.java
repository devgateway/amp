package org.digijava.module.aim.action.dataimporter.util;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.action.dataimporter.dbentity.ImportStatus;
import org.digijava.module.aim.action.dataimporter.dbentity.ImportedFilesRecord;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        String generatedHash = generateSHA256Hash(file);
        logger.info("Saving File hash is " + generatedHash);

        String sql = "INSERT INTO IMPORTED_FILES_RECORD (id, file_name, file_hash, import_status) VALUES (nextval('IMPORTED_FILES_RECORD_SEQ'), ?, ?, ?) RETURNING id";

        try (Connection connection = PersistenceManager.getJdbcConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, filename);
            preparedStatement.setString(2, generatedHash);
            preparedStatement.setObject(3, ImportStatus.UPLOADED.ordinal()); // Store Enum as String

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new SQLException("Saving file failed, no ID obtained.");
            }

            long generatedId = resultSet.getLong(1);

            ImportedFilesRecord importedFilesRecord = new ImportedFilesRecord();
            importedFilesRecord.setId(generatedId);
            importedFilesRecord.setFileHash(generatedHash);
            importedFilesRecord.setImportStatus(ImportStatus.UPLOADED);
            importedFilesRecord.setFileName(filename);

            logger.info("File saved {}", importedFilesRecord);
            return importedFilesRecord;

        } catch (SQLException e) {
            logger.error("Error saving file: {}", e.getMessage(), e);
            throw new RuntimeException("Database error while saving file.", e);
        }
    }

    public static void updateFileStatus(ImportedFilesRecord importedFilesRecord, ImportStatus status) {
        logger.info("Updating file status to {}", status);
        Session session = PersistenceManager.getRequestDBSession();
        importedFilesRecord.setImportStatus(status);
        session.saveOrUpdate(importedFilesRecord);
        session.flush();
    }
    public static List<ImportedFilesRecord> getSimilarFiles(File file) throws IOException, NoSuchAlgorithmException {
        String hash = generateSHA256Hash(file);
        logger.info("Checking File hash is {}", hash);
        Session session = PersistenceManager.getRequestDBSession();
        Query query = session.createQuery("from ImportedFilesRecord where fileHash = :hash");
        query.setParameter("hash", hash);
        return query.list();
    }


}
