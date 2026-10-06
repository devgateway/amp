package org.digijava.module.aim.dbentity;


/**
 * Indicator -sector connection.
 * from devinfo we have indicators which have different values for each sector.
 * @author Irakli Kobiashvili
 *
 */
public class IndicatorSector extends IndicatorConnection {

    private static final long serialVersionUID = 1L;
    private AmpSector sector;
    private AmpLocation location;
    
    public AmpLocation getLocation() {
        return location;
    }
    public void setLocation(AmpLocation location) {
        this.location = location;
    }
    public AmpSector getSector() {
        return sector;
    }
    public void setSector(AmpSector sector) {
        this.sector = sector;
    }
    public String getGeneratedName(){
        StringBuffer buf= new StringBuffer();
        buf.append(this.getIndicator().getName());
        buf.append(" (");
        if (this.sector!=null){
            buf.append(this.sector.getName());
        }else{
            buf.append(" NO SECTOR");
        }
        buf.append(") ");
        if (this.location!=null && this.location.getLocation().getName()!=null){
            buf.append(this.location.getLocation().getName());
        }else{
            buf.append("NO LOCATION");
        }
        return buf.toString();
    }
    
    @Override
    public String toString() {
        return getGeneratedName();
    }
}
