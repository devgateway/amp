package org.digijava.module.aim.dbentity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.*;

@Entity
@Table(name = "AMP_THEME_MAPPING")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
/**
 * @author Viorel Chihai
 */
public class AmpThemeMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_theme_mapping_seq_gen")
    @SequenceGenerator(name = "amp_theme_mapping_seq_gen", sequenceName = "AMP_THEME_MAPPING_SEQ", allocationSize = 1)
    @Column(name = "id")
    @JsonIgnore
    private Long id;
     @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "src_theme_id", nullable = false, unique = true, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_amp_theme_mapping_src_theme"))
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ampThemeId",
            resolver = EntityResolver.class, scope = AmpTheme.class)
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("src-program")
    private AmpTheme srcTheme;
     @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dst_theme_id", nullable = false, unique = true, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_amp_theme_mapping_dst_theme"))
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ampThemeId",
            resolver = EntityResolver.class, scope = AmpTheme.class)
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("dst-program")
    private AmpTheme dstTheme;

    @JsonProperty("level")
    private Integer level;
    public AmpThemeMapping() {
    }

    public AmpThemeMapping(AmpTheme srcTheme, AmpTheme dstTheme, Integer level) {
        this.srcTheme = srcTheme;
        this.dstTheme = dstTheme;
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpTheme getSrcTheme() {
        return srcTheme;
    }

    public void setSrcTheme(AmpTheme srcTheme) {
        this.srcTheme = srcTheme;
    }

    public AmpTheme getDstTheme() {
        return dstTheme;
    }

    public void setDstTheme(AmpTheme dstTheme) {
        this.dstTheme = dstTheme;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
