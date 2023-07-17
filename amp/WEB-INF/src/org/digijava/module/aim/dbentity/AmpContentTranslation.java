package org.digijava.module.aim.dbentity;

/**
 * Entity that stores the translation for a field of an object in a certain locale
 * @author aartimon@developmentgateway.org
 */
import javax.persistence.*;

@Entity
@Table(name = "AMP_CONTENT_TRANSLATION")
public class AmpContentTranslation {
    @Id
    @GeneratedValue(generator = "ampContentTranslationSeq")
    @SequenceGenerator(name = "ampContentTranslationSeq", sequenceName = "AMP_CONTENT_TRANSLATION_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "object_class", nullable = false)
    private String objectClass;

    @Column(name = "object_id", nullable = false)
    private Long objectId;

    @Column(name = "field_name", nullable = false)
    private String fieldName;

    @Column(name = "locale", nullable = false)
    private String locale;

    @Column(name = "translation", nullable = false)
    private String translation;


    public AmpContentTranslation(){
        super();
    }
    
    public AmpContentTranslation(String objectClass, Long objectId, String fieldName, String locale, String translation) {
        this.objectClass = objectClass;
        this.objectId = objectId;
        this.fieldName = fieldName;
        this.locale = locale;
        this.translation = translation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(String objectClass) {
        this.objectClass = objectClass;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
    
    @Override
    public String toString()
    {
        return String.format("%s[%d, %s].%s = %s; id = %s", compressClassName(objectClass), objectId, locale, fieldName, this.translation, id);
    }
    
    public static String compressClassName(String className)
    {
        if (className == null)
            return className;
        
        int pos = className.lastIndexOf('.');
        if (pos == -1)
            return className;
        
        return className.substring(pos + 1);
    }
}
