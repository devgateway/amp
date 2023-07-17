package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

import org.dgfoundation.amp.ar.dimension.ARDimensionable;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.util.Identifiable;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "AMP_COMPONENT_TYPE")
@TranslatableClass(displayName = "Component Type")
public class AmpComponentType implements ARDimensionable, Serializable, Identifiable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "ampComponentTypeSeq")
    @SequenceGenerator(name = "ampComponentTypeSeq", sequenceName = "AMP_COMPONENT_TYPE_seq", allocationSize = 1)
    @Column(name = "type_id")
    private Long type_id;

    @Column(name = "name")
    @TranslatableField

    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "enable")
    private Boolean enable;

    @Column(name = "selectable")
    private Boolean selectable;

    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL)
    private Set<AmpComponent> components;



    @Override
    public Object getIdentifier() {
        return type_id;
    }

    public Set<AmpComponent> getComponents() {
        return components;
    }

    public void setComponents(Set<AmpComponent> components) {
        this.components = components;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getType_id() {
        return type_id;
    }

    public void setType_id(Long type_id) {
        this.type_id = type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnable() {
        return enable;
    }

    
    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
    
    

    /**
     * @return the selectable
     */
    public Boolean getSelectable() {
        return selectable;
    }

    /**
     * @param selectable the selectable to set
     */
    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AmpComponentType))
            return false;
        AmpComponentType target= (AmpComponentType) obj;
        if (target!=null && this.type_id!=null){
            if (target.getType_id().doubleValue()==this.getType_id().doubleValue()){
                return true;
            }
        }
        return false;
    }
    @Override
    public int hashCode() {
        return this.getType_id().hashCode();
    }
    
    public int compareTo(AmpComponentType o) {
        try {
            if (this.name.compareToIgnoreCase(o.getName()) > 0) {
                return 1;
            } else if (this.name.compareToIgnoreCase(o.getName()) == 0) {
                return -0;
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }

    @Override
    public Class getDimensionClass() {
        return null;
    }   
}
