package org.digijava.module.aim.dbentity;

/**
 * @author Octavian Ciubotaru
 */
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "machine_translation")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MachineTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "machine_translation_seq_gen")
    @SequenceGenerator(name = "machine_translation_seq_gen", sequenceName = "machine_translation_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "src_lang", length = 2, nullable = false)
    private String sourceLanguage;

    @Column(name = "dst_lang", length = 2, nullable = false)
    private String targetLanguage;

    @Column(name = "src_text", columnDefinition = "text", nullable = false, unique = true)
    private String text;

    @Column(name = "dst_text", columnDefinition = "text", nullable = false)
    private String translatedText;



    public MachineTranslation() {
    }

    public MachineTranslation(String sourceLanguage, String targetLanguage, String text, String translatedText) {
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
        this.text = text;
        this.translatedText = translatedText;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }
}
