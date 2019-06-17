package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.util.Output;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * Persister class for Structures
 * @author fferreyra
 */
@TranslatableClass (displayName = "Structure")
public class AmpStructure implements Serializable, Comparable<Object>, Versionable, Cloneable {

    private static final long serialVersionUID = 1L;

    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long ampStructureId;
    
    @TranslatableField
    @Interchangeable(fieldTitle = "Title", importable = true, fmPath = "/Activity Form/Structures/Structure Title",
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    private String title;
    
    @TranslatableField
    @Interchangeable(fieldTitle = "Description", importable = true, 
            fmPath = "/Activity Form/Structures/Structure Description")
    private String description;
    
    @Interchangeable(fieldTitle = "Latitude", importable = true, 
            fmPath = "/Activity Form/Structures/Structure Latitude")
    private String latitude;
    
    @Interchangeable(fieldTitle = "Longitude", importable = true, 
            fmPath = "/Activity Form/Structures/Structure Longitude")
    private String longitude;
    
    @Interchangeable(fieldTitle = "Shape", importable = true, fmPath = "/Activity Form/Structures/Structure Shape")
    private String shape;
    
    private java.sql.Timestamp creationdate;
    
    @Interchangeable(fieldTitle = "Type", pickIdOnly = true, importable = true, 
            fmPath = "/Activity Form/Structures/Structure Type")
    private AmpStructureType type;

    @InterchangeableBackReference
    private AmpActivityVersion activity;
    
    private Set<AmpStructureImg> images;
    
    @Interchangeable(fieldTitle = "Coordinates", importable = true, fmPath = "/Activity Form/Structures/Map")
    private List<AmpStructureCoordinate> coordinates = new ArrayList<>();
    
    private String coords;

    @Interchangeable(fieldTitle = "Structure Color", fmPath = "/Activity Form/Structures/Map",
            discriminatorOption = CategoryConstants.GIS_STRUCTURES_COLOR_CODING_KEY, importable = true,
            pickIdOnly = true)
    private AmpCategoryValue structureColor;
    private Long structureColorId;
    private Integer tempId; // client side id used for identifying structures

    public AmpActivityVersion getActivity() {
        return activity;
    }

    public void setActivity(AmpActivityVersion activity) {
        this.activity = activity;
    }

    public Long getAmpStructureId() {
        return ampStructureId;
    }

    public void setAmpStructureId(Long ampStructureId) {
        this.ampStructureId = ampStructureId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AmpStructureType getType() {
        return type;
    }

    public void setType(AmpStructureType type) {
        this.type = type;
    }

    public int compareTo(Object obj) {
        
        if (!(obj instanceof AmpStructure)) 
            throw new ClassCastException();
        
        AmpStructure ampStr = (AmpStructure) obj;
        if (this.title != null) {
            if (ampStr.title != null) {
                return (this.title.trim().toLowerCase().
                        compareTo(ampStr.title.trim().toLowerCase()));
            } else {
                return (this.title.trim().toLowerCase().
                        compareTo(""));
            }
        } else {
            if (ampStr.title != null) {
                return ("".compareTo(ampStr.title.trim().toLowerCase()));
            } else {
                return 0;
            }           
        }
    }
    
    public java.sql.Timestamp getCreationdate() {
        return creationdate;
    }
    public void setCreationdate(java.sql.Timestamp creationdate) {
        this.creationdate = creationdate;
    }
    
    @Override
    public boolean equalsForVersioning(Object obj) {
        AmpStructure aux = (AmpStructure) obj;
        String original = this.title != null ? this.title : "";
        String copy = aux.title != null ? aux.title : "";
        if (original.equals(copy)) {
            return true;
        }
        return false;
    }

    @Override
    public Output getOutput() {
        Output out = new Output();
        out.setOutputs(new ArrayList<Output>());
        out.getOutputs().add(
                new Output(null, new String[] { "Title" }, new Object[] { this.title != null ? this.title
                        : "Empty Title" }));
        if (this.description != null && !this.description.trim().equals("")) {
            out.getOutputs()
                    .add(new Output(null, new String[] { "Description" }, new Object[] { this.description }));
        }
        if (this.creationdate != null) {
            out.getOutputs().add(
                    new Output(null, new String[] { "Creation Date" }, new Object[] { this.creationdate }));
        }
        return out;
    }
    @Override
    public Object getValue() {
        String value = " " + this.creationdate + this.description /*+ this.activity*/;
        return value;
    }
    
    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
        AmpStructure aux = (AmpStructure) clone();
        aux.setActivity(newActivity);
        aux.setAmpStructureId(null);
        
        if (aux.getImages() != null && aux.getImages().size() > 0) {
            Set<AmpStructureImg> auxSetImages = new HashSet<AmpStructureImg>();
            for (AmpStructureImg img : aux.getImages()) {
                AmpStructureImg auxImg = (AmpStructureImg) img.clone();
                auxImg.setId(null);
                auxImg.setStructure(aux);
                auxSetImages.add(auxImg);
            }
            aux.setImages(auxSetImages);
        } else {
            aux.setImages(null);
        }

        if (aux.getCoordinates() != null && aux.getCoordinates().size() > 0) {
            List<AmpStructureCoordinate> coords = new ArrayList<AmpStructureCoordinate>();
            for (AmpStructureCoordinate coord : aux.getCoordinates()) {
                AmpStructureCoordinate auxCoord = (AmpStructureCoordinate) coord.clone();
                auxCoord.setAmpStructureCoordinateId(null);
                auxCoord.setStructure(aux);
                coords.add(auxCoord);
            }
            aux.setCoordinates(coords);
        } else {
            aux.setCoordinates(null);
        }

        return aux;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getShape() {
        return shape;
    }

    public Set<AmpStructureImg> getImages() {
        return images;
    }

    public void setImages(Set<AmpStructureImg> images) {
        this.images = images;
    }

    public List<AmpStructureCoordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<AmpStructureCoordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public String getCoords() {
        return coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }

    public AmpCategoryValue getStructureColor() {
        return structureColor;
    }
    public void setStructureColor(AmpCategoryValue structureColor) {
        this.structureColor = structureColor;
    }

    public Integer getTempId() {
        return tempId;
    }
    public void setTempId(Integer tempId) {
        this.tempId = tempId;
    }

    public Long getStructureColorId() {
        return structureColorId;
    }
    public void setStructureColorId(Long structureColorId) {
        this.structureColorId = structureColorId;
    }

    @Override
    public String toString() {
        return String.format("AmpStructure[id=%s], title = %s, description = %s", this.ampStructureId, this.title, this.description);
    }
    
    public static String hqlStringForTitle(String idSource) {
        return InternationalizedModelDescription.getForProperty(AmpStructure.class, "title").getSQLFunctionCall(idSource + ".ampStructureId");
    }

    public static String sqlStringForTitle(String idSource) {
        return InternationalizedModelDescription.getForProperty(AmpStructure.class, "title").getSQLFunctionCall(idSource);
    }

    public static String hqlStringForDescription(String idSource) {
        return InternationalizedModelDescription.getForProperty(AmpStructure.class, "description").getSQLFunctionCall(idSource + ".ampStructureId");
    }

    public static String sqlStringForDescription(String idSource) {
        return InternationalizedModelDescription.getForProperty(AmpStructure.class, "description").getSQLFunctionCall(idSource);
    }
    
}
