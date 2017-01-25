package org.digijava.kernel.ampapi.endpoints.mimetype;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
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
}
