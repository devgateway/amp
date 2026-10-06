/**
 * 
 */
package org.digijava.module.admin.helper;

import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.LoggerIdentifiable;

/**
 * @author acartaleanu
 *
 */
public class AmpPledgeFake implements LoggerIdentifiable{

    /**
     * 
     */
    
    private String name;
    
    private Long ampId;
    private String additionalInfo;
    
    public AmpPledgeFake(String name, Long ampId) {
        super();
        this.name = name;
        this.ampId = ampId;
    }
    

    public AmpPledgeFake(String name, Long ampId, String additionalInfo) {
        super();
        this.name = name;
        this.ampId = ampId;
        this.additionalInfo = additionalInfo;
    }

    public String getAdditionalInfo() {
        return this.additionalInfo;
    }
    

    public String getName() {
        return name;
    }
    public Long getAmpId() {
        return ampId;
    }


    @Override
    public Object getIdentifier() {
        return this.getAmpId();
    }

    @Override
    public Object getObjectType() {
        return this.getClass().getName();
    }

    @Override
    public String getObjectName() {

        return "("+this.getAmpId()+") "+this.getName();
    }
         @Override
        public String getObjectFilteredName() {
        return DbUtil.filter(getObjectName());
    }

        
}
