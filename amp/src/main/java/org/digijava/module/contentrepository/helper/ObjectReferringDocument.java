/**
 * 
 */
package org.digijava.module.contentrepository.helper;

import org.digijava.kernel.validators.activity.PrivateResourceValidator;
import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.io.Serializable;

/**
 * @author Alex Gartner
 * This class should be extended by all other classes that are referencing documents from
 * Content Repository through their UUID  
 */
public abstract class ObjectReferringDocument implements Serializable {
    
    @Interchangeable(fieldTitle = ActivityFieldsConstants.UUID, importable = true,
            interValidators = {
                    @InterchangeableValidator(RequiredValidator.class),
                    @InterchangeableValidator(PrivateResourceValidator.class)
            })
    private String uuid;

    public ObjectReferringDocument() {
        uuid = null;
    }

    public ObjectReferringDocument(String uuid) {
        this.uuid = uuid;
    }

    public void remove(Session session) throws HibernateException {
        this.detach();
        session.delete(this);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    /**
     * This method needs to be implemented by those classes that have relations to other entities.
     * In this case this function should delete all relations between 'this' and the other entities.
     *
     */
    protected void detach() { }
    
}
