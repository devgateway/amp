package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.SectorUtil;


public class AmpClassificationConfiguration implements Serializable, Identifiable {
	
	public static final String PRIMARY_CLASSIFICATION_CONFIGURATION_NAME	= "Primary";
	public static final String SECONDARY_CLASSIFICATION_CONFIGURATION_NAME	= "Secondary";
    public static final String TERTIARY_CLASSIFICATION_CONFIGURATION_NAME	= "Tertiary";
    public static final String TAG_CLASSIFICATION_CONFIGURATION_NAME	= "Tag";
    
    @SuppressWarnings("serial")
	public static final Map<String, String> NAME_TO_COLUMN_MAP = new HashMap<String, String>() {{
    	put(PRIMARY_CLASSIFICATION_CONFIGURATION_NAME, ColumnConstants.PRIMARY_SECTOR);
    	put(SECONDARY_CLASSIFICATION_CONFIGURATION_NAME, ColumnConstants.SECONDARY_SECTOR);
    	put(TERTIARY_CLASSIFICATION_CONFIGURATION_NAME, ColumnConstants.TERTIARY_SECTOR);
    }};
	
	@Interchangeable(fieldTitle="ID", id = true)
	private Long id;
	@Interchangeable(fieldTitle="ID", value = true)
	private String name;
	@Interchangeable(fieldTitle="Description")
	private String description;
	@Interchangeable(fieldTitle="Classification", pickIdOnly=true)
	private AmpSectorScheme classification;
    private boolean multisector ;
    private boolean primary;

    public AmpSectorScheme getClassification() {
        return classification;
    }

    public void setClassification(AmpSectorScheme classification) {
        this.classification = classification;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isMultisector() {
        return multisector;
    }

    public void setMultisector(boolean multisector) {
        this.multisector = multisector;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPrimary() {
        return primary;
    }
    /**
     *
     * There must be only one primary configuration in database
     */
    
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }
        
    public boolean isUsed() {
        boolean used = true;
        try {
            used = SectorUtil.isClassificationUsed(id);

        } catch (DgException ex) {
            ex.printStackTrace();

        }
        return used;
    }

	@Override
	public Object getIdentifier() {
		return this.id;
//		return null;
	}

}
