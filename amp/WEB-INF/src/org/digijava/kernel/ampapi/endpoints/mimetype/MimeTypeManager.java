package org.digijava.kernel.ampapi.endpoints.mimetype;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpMimeType;
import org.hibernate.Session;

/**
 * MimeType Manager used for saving and detecting mime types of files 
 * 
 * @author Viorel Chihai
 */
public class MimeTypeManager {
	
	private static Logger logger = Logger.getLogger(MimeTypeManager.class);
	
	private static MimeTypeManager mimeTypeManager;
	private List<AmpMimeType> allMimeTypes;
	
	private MimeTypeManager() {
		allMimeTypes = new ArrayList<>();
		
		for (MediaType mediaType : MimeTypes.getDefaultMimeTypes().getMediaTypeRegistry().getTypes()) {
			try {
				MimeType mimeType = MimeTypes.getDefaultMimeTypes().forName(mediaType.getBaseType().toString());
				allMimeTypes.add(new AmpMimeType(mimeType.getName(), mimeType.getDescription(), mimeType.getExtensions()));
			} catch (MimeTypeException e) {
				logger.error("Could not instantiate Apache Tika mimetype!", e);
			}
		}
	}
	
	/**
	 * 
	 * @return MimeTypeManager instance
	 */
	public static MimeTypeManager getInstance() {
		if (mimeTypeManager == null) {
			mimeTypeManager = new MimeTypeManager();
		}
		
		return mimeTypeManager;
	}
	
	/**
	 * Get all supported mime types by Apache Tika
	 * 
	 * @return List<AmpMimeType>
	 */
	public List<AmpMimeType> getAllMimeTypes() {
		return allMimeTypes;
	}
	
	/**
	 * Get all allowed mime types for file uploading
	 * 
	 * @return List<AmpMimeType
	 */
	public List<AmpMimeType> getAllowedMimeTypes() {
		Set<String> dbMimeTypes = getDbAmpMimeTypes().stream()
				.map(m -> m.getName()).collect(Collectors.toSet());
		
		List<AmpMimeType> allowedMimeTypes = allMimeTypes.stream()
				.filter(m -> dbMimeTypes.contains(m.getName()))
				.collect(Collectors.toList());	
		
		return allowedMimeTypes;
	}
	
	@SuppressWarnings("unchecked")
	public List<AmpMimeType> getDbAmpMimeTypes() {
		return PersistenceManager.getRequestDBSession().createQuery(
				"select o from " +  AmpMimeType.class.getName() + " o").list();
	}

	/**
	 * 
	 * @param updatedMimeTypes
	 */
	public void updateMimeTypesConfig(Set<String> updatedMimeTypes) {
		Session session = PersistenceManager.getCurrentSession();
		
		List<AmpMimeType> mimeTypesToBeDeleted = getMimeTypesToBeDeleted(updatedMimeTypes);
		mimeTypesToBeDeleted.forEach(m -> session.delete(m));
		
		List<AmpMimeType> mimeTypesToBeInserted = getMimeTypesToBeInserted(updatedMimeTypes);
		mimeTypesToBeInserted.forEach(m -> session.save(m));
	}

	/**
	 * @param updatedMimeTypes
	 * @return
	 */
	private List<AmpMimeType> getMimeTypesToBeDeleted(Set<String> updatedMimeTypes) {
		List<AmpMimeType> mimeTypesToBeDeleted = getDbAmpMimeTypes().stream()
				.filter(m -> !updatedMimeTypes.contains(m.getName()))
				.collect(Collectors.toList());
		
		return mimeTypesToBeDeleted;
	}

	/**
	 * @param updatedMimeTypes
	 * @param dbMimeTypesNames
	 * @return
	 */
	private List<AmpMimeType> getMimeTypesToBeInserted(Set<String> updatedMimeTypes) {
		List<AmpMimeType> mimeTypesToBeInserted = new ArrayList<>();
		
		Set<String> dbMimeTypesNames = getDbAmpMimeTypes().stream()
				.map(m -> m.getName()).collect(Collectors.toSet());
		
		allMimeTypes.stream().filter(m -> updatedMimeTypes.contains(m.getName()))
				.filter(m -> !dbMimeTypesNames.contains(m.getName()))
				.forEach(m -> {
					AmpMimeType mimeType = new AmpMimeType(m.getName(), m.getDescription(), m.getExtensions());
					mimeTypesToBeInserted.add(mimeType);
				});
		
		return mimeTypesToBeInserted;
	}
	
	/**
	 * Validate if is allowed to upload the file
	 * 
	 * @param file to validate
	 * @return MimeTypeValidationResponse containing the status of the validation
	 */
	public MimeTypeValidationResponse validateMimeType(File file) {
		try {
			return validateMimeType(new FileInputStream(file), file.getName());
		} catch (FileNotFoundException e) {
			logger.error("IOException during the mime type validation ", e);
			return new MimeTypeValidationResponse(MimeTypeValidationStatus.INTERNAL_ERROR); 
		}
	}
	
	/**
	 * Validate if the InputStream has a mime type allowed for uploading. 
	 * The file name is used for checking the matching the mime type of the stream.
	 * 
	 * @param is
	 * @param fileName
	 * @return MimeTypeValidationResponse containing the status of the validation
	 */
	public MimeTypeValidationResponse validateMimeType(InputStream is, String fileName) {
		String extension = FilenameUtils.EXTENSION_SEPARATOR + FilenameUtils.getExtension(fileName);
		try {
		    Metadata md = new Metadata();
		    md.set(Metadata.RESOURCE_NAME_KEY, fileName);
		    Detector detector = new DefaultDetector();

		    MediaType mediaType = detector.detect(is, md);
			String mimeTypeName = mediaType.toString();
			
			if (isMimeTypeAllowed(mimeTypeName)) {
				return new MimeTypeValidationResponse(MimeTypeValidationStatus.ALLOWED);
			}
			
			MimeType mimeType = MimeTypes.getDefaultMimeTypes().forName(mimeTypeName);
			
			if (!mimeType.getExtensions().contains(extension)) {
				return new MimeTypeValidationResponse(MimeTypeValidationStatus.CONTENT_EXTENSION_MISMATCH, mimeTypeName, mimeType.getDescription(), extension);
			}
			
			return new MimeTypeValidationResponse(MimeTypeValidationStatus.NOT_ALLOWED, mimeTypeName, mimeType.getDescription());
		} catch (IOException e) {
			logger.error("Error in detecting content type of the stream ", e);
			return new MimeTypeValidationResponse(MimeTypeValidationStatus.INTERNAL_ERROR); 
		} catch (MimeTypeException e) {
			logger.error("Invalid media type name ", e);
			return new MimeTypeValidationResponse(MimeTypeValidationStatus.INTERNAL_ERROR); 
		}
	}
	
	/**
	 * Checks if the content type is allowed (Exists in DB)
	 * 
	 * @param contentTypeName
	 * @return 
	 */
	private boolean isMimeTypeAllowed(String mimeTypeName) {
		List<AmpMimeType> allowedMimeTypes = getAllowedMimeTypes();
		
		Set<String>  allowedMimeTypeNames= allowedMimeTypes.stream()
											.map(m -> m.getName())
											.collect(Collectors.toSet());
		
		return allowedMimeTypeNames.contains(mimeTypeName);
	}
}
