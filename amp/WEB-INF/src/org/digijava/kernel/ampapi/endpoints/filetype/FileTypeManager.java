package org.digijava.kernel.ampapi.endpoints.filetype;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFileType;
import org.hibernate.Session;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * FileType Manager used for saving and detecting types of files 
 * 
 * @author Viorel Chihai
 */
public class FileTypeManager {
    
    private static Logger logger = Logger.getLogger(FileTypeManager.class);
    
    private static FileTypeManager fileTypeManager;
    private List<AmpFileType> allFileTypes;

    private FileTypeManager() {
        allFileTypes = new ArrayList<>();
        allFileTypes.add(createFileType(FileTypesMimeMapper.FILE_TYPE_WORD, "MS Word ("
                + Util.toCSString(this.getExtensionsFromMimeType(FileTypesMimeMapper.FILE_TYPE_WORD), false) + ")"));
        allFileTypes.add(createFileType(FileTypesMimeMapper.FILE_TYPE_EXCEL, "MS Excel ("
                + Util.toCSString(this.getExtensionsFromMimeType(FileTypesMimeMapper.FILE_TYPE_EXCEL), false) + ")"));
        allFileTypes
                .add(createFileType(FileTypesMimeMapper.FILE_TYPE_POWERPOINT,
                        "MS PowerPoint ("
                                + Util.toCSString(
                                        this.getExtensionsFromMimeType(FileTypesMimeMapper.FILE_TYPE_POWERPOINT), false)
                                + ")"));
        allFileTypes
                .add(createFileType(FileTypesMimeMapper.FILE_TYPE_PROJECT,
                        "MS Project ("
                                + Util.toCSString(
                                        this.getExtensionsFromMimeType(FileTypesMimeMapper.FILE_TYPE_POWERPOINT), false)
                                + ")"));
        allFileTypes.add(createFileType(FileTypesMimeMapper.FILE_TYPE_VISIO, "MS Visio ("
                + Util.toCSString(this.getExtensionsFromMimeType(FileTypesMimeMapper.FILE_TYPE_VISIO), false) + ")"));
        allFileTypes.add(createFileType(FileTypesMimeMapper.FILE_TYPE_PDF, "PDF ("
                + Util.toCSString(this.getExtensionsFromMimeType(FileTypesMimeMapper.FILE_TYPE_PDF), false) + ")"));
        allFileTypes.add(createFileType(FileTypesMimeMapper.FILE_TYPE_ARCHIVE, "Zip Files ("
                + Util.toCSString(this.getExtensionsFromMimeType(FileTypesMimeMapper.FILE_TYPE_ARCHIVE), false) + ")"));
        allFileTypes.add(createFileType(FileTypesMimeMapper.FILE_TYPE_LIBREOFFICE_SHEETS,
                "Libre Office spreadsheets ("
                        + Util.toCSString(
                                this.getExtensionsFromMimeType(FileTypesMimeMapper.FILE_TYPE_LIBREOFFICE_SHEETS), false)
                        + ")"));
        allFileTypes.add(createFileType(FileTypesMimeMapper.FILE_TYPE_LIBREOFFICE_DOCUMENTS,
                "Libre Office documents (" + Util.toCSString(
                        this.getExtensionsFromMimeType(FileTypesMimeMapper.FILE_TYPE_LIBREOFFICE_DOCUMENTS), false)
                        + ")"));
        allFileTypes.add(createFileType(FileTypesMimeMapper.FILE_TYPE_CSV, "CSV ("
                + Util.toCSString(this.getExtensionsFromMimeType(FileTypesMimeMapper.FILE_TYPE_CSV), false) + ")"));
        allFileTypes.add(createFileType(FileTypesMimeMapper.FILE_TYPE_PNG, "PNG ("
                + Util.toCSString(this.getExtensionsFromMimeType(FileTypesMimeMapper.FILE_TYPE_PNG), false) + ")"));
        allFileTypes.add(createFileType(FileTypesMimeMapper.FILE_TYPE_GIF, "GIF ("
                + Util.toCSString(this.getExtensionsFromMimeType(FileTypesMimeMapper.FILE_TYPE_GIF), false) + ")"));
        allFileTypes.add(createFileType(FileTypesMimeMapper.FILE_TYPE_JPEG, "JPEG ("
                + Util.toCSString(this.getExtensionsFromMimeType(FileTypesMimeMapper.FILE_TYPE_JPEG), false) + ")"));
        allFileTypes.add(createFileType(FileTypesMimeMapper.FILE_TYPE_BMP, "BMP ("
                + Util.toCSString(this.getExtensionsFromMimeType(FileTypesMimeMapper.FILE_TYPE_BMP), false) + ")"));
        allFileTypes.add(createFileType(FileTypesMimeMapper.FILE_TIFF, "TIFF ("
                + Util.toCSString(this.getExtensionsFromMimeType(FileTypesMimeMapper.FILE_TIFF), false) + ")"));
        allFileTypes.add(createFileType(FileTypesMimeMapper.FILE_PHOTOSHOP, "PSD ("
                + Util.toCSString(this.getExtensionsFromMimeType(FileTypesMimeMapper.FILE_PHOTOSHOP), false) + ")"));
        allFileTypes.add(createFileType(FileTypesMimeMapper.FILE_TYPE_RTF, "Rich Text Format ("
                + Util.toCSString(this.getExtensionsFromMimeType(FileTypesMimeMapper.FILE_TYPE_RTF), false) + ")"));
        allFileTypes.add(createFileType(FileTypesMimeMapper.FILE_TYPE_TXT, "Text Files ("
                + Util.toCSString(this.getExtensionsFromMimeType(FileTypesMimeMapper.FILE_TYPE_TXT), false) + ")"));
        
        allFileTypes.sort((p1, p2) -> p1.getDescription().compareTo(p2.getDescription()));
    }
    
    /**
     * 
     * @return FileTypeManager instance
     */
    public static FileTypeManager getInstance() {
        if (fileTypeManager == null) {
            fileTypeManager = new FileTypeManager();
        }
        
        return fileTypeManager;
    }
    
    /**
     * Get all supported file types by AMP
     * 
     * @return List<AmpFileType>
     */
    public List<AmpFileType> getAllFileTypes() {
        return allFileTypes;
    }
    
    /**
     * Get all allowed file types for file uploading
     * 
     * @return List<AmpFileType>
     */
    public List<AmpFileType> getAllowedFileTypes() {
        Set<String> dbFileTypes = getDbAmpFileTypes().stream()
                .map(m -> m.getName()).collect(Collectors.toSet());
        
        List<AmpFileType> allowedFileTypes = allFileTypes.stream()
                .filter(m -> dbFileTypes.contains(m.getName()))
                .collect(Collectors.toList());  
        
        return allowedFileTypes;
    }
    
    @SuppressWarnings("unchecked")
    public List<AmpFileType> getDbAmpFileTypes() {
        return PersistenceManager.getRequestDBSession().createQuery(
                "select o from " +  AmpFileType.class.getName() + " o").list();
    }

    /**
     * 
     * @param updatedFileTypes
     */
    public void updateFileTypesConfig(Set<String> updatedFileTypes) {
        Session session = PersistenceManager.getSession();
        
        List<AmpFileType> fileTypesToBeDeleted = getFileTypesToBeDeleted(updatedFileTypes);
        fileTypesToBeDeleted.forEach(ft -> session.delete(ft));
        
        List<AmpFileType> fileTypesToBeInserted = getFileTypesToBeInserted(updatedFileTypes);
        fileTypesToBeInserted.forEach(ft -> session.save(ft));
    }

    /**
     * @param updatedFileTypes
     * @return
     */
    private List<AmpFileType> getFileTypesToBeDeleted(Set<String> updatedFileTypes) {
        List<AmpFileType> fileTypesToBeDeleted = getDbAmpFileTypes().stream()
                .filter(ft -> !updatedFileTypes.contains(ft.getName()))
                .collect(Collectors.toList());
        
        return fileTypesToBeDeleted;
    }

    /**
     * @param updatedFileTypes
     * @param dbFileTypesNames
     * @return
     */
    private List<AmpFileType> getFileTypesToBeInserted(Set<String> updatedFileTypes) {
        List<AmpFileType> fileTypesToBeInserted = new ArrayList<>();
        
        Set<String> dbFileTypesNames = getDbAmpFileTypes().stream()
                .map(ft -> ft.getName()).collect(Collectors.toSet());
        
        allFileTypes.stream().filter(ft -> updatedFileTypes.contains(ft.getName()))
                .filter(ft -> !dbFileTypesNames.contains(ft.getName()))
                .forEach(ft -> {
                    AmpFileType fileType = new AmpFileType(ft.getName(), ft.getDescription(), ft.getMimeTypes(), ft.getExtensions());
                    fileTypesToBeInserted.add(fileType);
                });
        
        return fileTypesToBeInserted;
    }
    
    /**
     * Validate if is allowed to upload the file
     * 
     * @param file to validate
     * @return FileTypeValidationResponse containing the status of the validation
     */
    public FileTypeValidationResponse validateFileType(File file) {
        try {
            return validateFileType(new FileInputStream(file), file.getName());
        } catch (FileNotFoundException e) {
            logger.error("IOException during the file type validation ", e);
            return new FileTypeValidationResponse(FileTypeValidationStatus.INTERNAL_ERROR); 
        }
    }
    
    /**
     * Validate if the InputStream has a file type allowed for uploading. 
     * The file name is used for checking the matching the mime type of the stream.
     * 
     * @param is
     * @param fileName
     * @return FileTypeValidationResponse containing the status of the validation
     */
    public FileTypeValidationResponse validateFileType(InputStream is, String fileName) {
        String extension = FilenameUtils.EXTENSION_SEPARATOR + FilenameUtils.getExtension(fileName);
        try {
            Metadata md = new Metadata();
            md.set(Metadata.RESOURCE_NAME_KEY, fileName);
            Detector detector = new DefaultDetector();

            MediaType mediaType = detector.detect(is, md);
            String mimeTypeName = mediaType.toString();
            
            MimeType mimeType = MimeTypes.getDefaultMimeTypes().forName(mimeTypeName);
            if (!mimeType.getExtensions().contains(extension)) {
                return new FileTypeValidationResponse(FileTypeValidationStatus.CONTENT_EXTENSION_MISMATCH, mimeTypeName, getFileTypeDescription(mimeType), extension);
            }
            
            if (isMimeTypeAllowed(mimeTypeName)) {
                return new FileTypeValidationResponse(FileTypeValidationStatus.ALLOWED);
            }
            
            return new FileTypeValidationResponse(FileTypeValidationStatus.NOT_ALLOWED, mimeTypeName, getFileTypeDescription(mimeType));
        } catch (IOException e) {
            logger.error("Error in detecting content type of the stream ", e);
            return new FileTypeValidationResponse(FileTypeValidationStatus.INTERNAL_ERROR); 
        } catch (MimeTypeException e) {
            logger.error("Invalid media type name ", e);
            return new FileTypeValidationResponse(FileTypeValidationStatus.INTERNAL_ERROR); 
        }
    }
    
    /**
     * Checks if the content type is allowed (Exists in DB)
     * 
     * @param contentTypeName
     * @return 
     */
    private boolean isMimeTypeAllowed(String mimeTypeName) {
        List<AmpFileType> allowedFileTypes = getAllowedFileTypes();
        
        Set<String> allowedMimeTypeNames= allowedFileTypes.stream()
                                            .flatMap(ft -> ft.getMimeTypes().stream())
                                            .collect(Collectors.toSet());
        
        return allowedMimeTypeNames.contains(mimeTypeName);
    }
    
    private AmpFileType createFileType(String fileTypeName, String fileTypeDescription) {
        AmpFileType fileType = new AmpFileType();
        fileType.setName(fileTypeName);
        fileType.setDescription(fileTypeDescription);
        fileType.setMimeTypes(FileTypesMimeMapper.FILE_MIME_TYPES.get(fileTypeName));
        
        List<String> extensions = new ArrayList<>();
        
        for (String mimeType : FileTypesMimeMapper.FILE_MIME_TYPES.get(fileTypeName)) {
            try {
                extensions.addAll(MimeTypes.getDefaultMimeTypes().forName(mimeType).getExtensions());
            } catch (MimeTypeException e) {
                logger.error("Could not instantiate Apache Tika mimetype!", e);
            }
        }
        
        fileType.setExtensions(extensions);
        
        return fileType;
    }
    
    private String getFileTypeDescription(MimeType mimeType) {
        Optional<AmpFileType> fileType = allFileTypes.stream().filter(ft -> ft.getMimeTypes().contains(mimeType.getName())).findFirst();
        
        return fileType.isPresent() ? fileType.get().getDescription() : mimeType.getDescription();
    }

    private List<String> getExtensionsFromMimeType(String fileType) {
        List<String> extensions = new ArrayList<String>();
        try {

            for (String mimeType : FileTypesMimeMapper.FILE_MIME_TYPES.get(fileType)) {

                extensions.addAll(MimeTypes.getDefaultMimeTypes().forName(mimeType).getExtensions());
            }
        } catch (MimeTypeException e) {
            logger.error("Cannot get file type", e);
        }
        return extensions;
    }   
}
