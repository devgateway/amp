/**
 * 
 */
package org.digijava.module.contentrepository.helper;

import org.digijava.module.aim.dbentity.AbstractAuditLogger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * @author Alex Gartner
 * This class should be extended by all other classes that are referencing documents from
 * Content Repository through their UUID
 * If a class Inheriting ObjectReferringDocument needs to track previous object id will have to
 * define its in own mapping file the said database column
 */
public abstract class ObjectReferringDocument extends AbstractAuditLogger {
    private String uuid;
    public ObjectReferringDocument() {
        uuid    = null;
    }
    public ObjectReferringDocument(String uuid) {
        this.uuid   = uuid;
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
    protected void detach() {
        ;
    }
    
    
    
}
