package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.SectorUtil;
import javax.persistence.*;

@Entity
@Table(name = "AMP_CLASSIFICATION_CONFIG")
public class AmpClassificationConfiguration implements Serializable, Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_classification_config_seq_generator")
    @SequenceGenerator(name = "amp_classification_config_seq_generator", sequenceName = "AMP_CLASSIFICATION_CONFIG_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "multisector")
    private boolean multisector;

    @Column(name = "is_primary_sector")
    private boolean primary;

    @ManyToOne
    @JoinColumn(name = "classification_id")
    private AmpSectorScheme classification;

    @Transient
    public static final String PRIMARY_CLASSIFICATION_CONFIGURATION_NAME    = "Primary";
    @Transient
    public static final String SECONDARY_CLASSIFICATION_CONFIGURATION_NAME  = "Secondary";
    @Transient
    public static final String TERTIARY_CLASSIFICATION_CONFIGURATION_NAME   = "Tertiary";
    @Transient
    public static final String QUATERNARY_CLASSIFICATION_CONFIGURATION_NAME = "Quaternary";
    @Transient
    public static final String QUINARY_CLASSIFICATION_CONFIGURATION_NAME    = "Quinary";
    @Transient
    public static final String TAG_CLASSIFICATION_CONFIGURATION_NAME    = "Tag";
    
    @SuppressWarnings("serial")
    public static final Map<String, String> NAME_TO_COLUMN_MAP = new HashMap<String, String>() {{
        put(PRIMARY_CLASSIFICATION_CONFIGURATION_NAME, ColumnConstants.PRIMARY_SECTOR);
        put(SECONDARY_CLASSIFICATION_CONFIGURATION_NAME, ColumnConstants.SECONDARY_SECTOR);
        put(TERTIARY_CLASSIFICATION_CONFIGURATION_NAME, ColumnConstants.TERTIARY_SECTOR);
        put(QUATERNARY_CLASSIFICATION_CONFIGURATION_NAME, ColumnConstants.QUATERNARY_SECTOR);
        put(QUINARY_CLASSIFICATION_CONFIGURATION_NAME, ColumnConstants.QUINARY_SECTOR);
    }};

    private static final int LEVEL_1 = 1;
    private static final int LEVEL_2 = 2;
    private static final int LEVEL_3 = 3;

    private static final Map<Integer, String> PRIMARY_SECTOR_COLUMNS_BY_LEVEL =
            new ImmutableMap.Builder<Integer, String>()
                    .put(LEVEL_1, ColumnConstants.PRIMARY_SECTOR)
                    .put(LEVEL_2, ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR)
                    .put(LEVEL_3, ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR)
                    .build();

    private static final Map<Integer, String> SECONDARY_SECTOR_COLUMNS_BY_LEVEL =
            new ImmutableMap.Builder<Integer, String>()
                    .put(LEVEL_1, ColumnConstants.SECONDARY_SECTOR)
                    .put(LEVEL_2, ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR)
                    .put(LEVEL_3, ColumnConstants.SECONDARY_SECTOR_SUB_SUB_SECTOR)
                    .build();

    private static final Map<Integer, String> TERTIARY_SECTOR_COLUMNS_BY_LEVEL =
            new ImmutableMap.Builder<Integer, String>()
                    .put(LEVEL_1, ColumnConstants.TERTIARY_SECTOR)
                    .put(LEVEL_2, ColumnConstants.TERTIARY_SECTOR_SUB_SECTOR)
                    .put(LEVEL_3, ColumnConstants.TERTIARY_SECTOR_SUB_SUB_SECTOR)
                    .build();

    private static final Map<Integer, String> QUATERNARY_SECTOR_COLUMNS_BY_LEVEL =
            new ImmutableMap.Builder<Integer, String>()
                    .put(LEVEL_1, ColumnConstants.QUATERNARY_SECTOR)
                    .put(LEVEL_2, ColumnConstants.QUATERNARY_SECTOR_SUB_SECTOR)
                    .put(LEVEL_3, ColumnConstants.QUATERNARY_SECTOR_SUB_SUB_SECTOR)
                    .build();

    private static final Map<Integer, String> QUINARY_SECTOR_COLUMNS_BY_LEVEL =
            new ImmutableMap.Builder<Integer, String>()
                    .put(LEVEL_1, ColumnConstants.QUINARY_SECTOR)
                    .put(LEVEL_2, ColumnConstants.QUINARY_SECTOR_SUB_SECTOR)
                    .put(LEVEL_3, ColumnConstants.QUINARY_SECTOR_SUB_SUB_SECTOR)
                    .build();

    public static final Map<String, Map<Integer, String>> NAME_TO_COLUMN_AND_LEVEL =
            new ImmutableMap.Builder<String, Map<Integer, String>>()
                    .put(PRIMARY_CLASSIFICATION_CONFIGURATION_NAME, PRIMARY_SECTOR_COLUMNS_BY_LEVEL)
                    .put(SECONDARY_CLASSIFICATION_CONFIGURATION_NAME, SECONDARY_SECTOR_COLUMNS_BY_LEVEL)
                    .put(TERTIARY_CLASSIFICATION_CONFIGURATION_NAME, TERTIARY_SECTOR_COLUMNS_BY_LEVEL)
                    .put(QUATERNARY_CLASSIFICATION_CONFIGURATION_NAME, QUATERNARY_SECTOR_COLUMNS_BY_LEVEL)
                    .put(QUINARY_CLASSIFICATION_CONFIGURATION_NAME, QUINARY_SECTOR_COLUMNS_BY_LEVEL)
                    .build();


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
//      return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
