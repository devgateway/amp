/**
 * 
 */
package org.digijava.module.gateperm.core;

import org.digijava.module.aim.util.Identifiable;

/**
 * @author mihai
 * 
 * Exporting permissions is no cake walk, since the permissions are attached to
 * object that allow permissions to be attached to (called Permissibles), those
 * objects need to be "globally" identified by an identifier that is unique in
 * the AMP Network. To be more precise, an identifier for an exportable
 * Permissible needs to remain unchanged from one AMP installation to the other.
 * Example: if we export permissions of the field "Project Title" we want to be
 * able to identify that field uniquely on both AMP Czech and AMP Czech Staging.
 * <br/>
 * Thus, we cannot use the internal Id of the field, since that id is not unique
 * and may vary from db to db. A Permissible object that can be exported is
 * identified by more than just a local id, and should implement the interface
 * ClusterIdentifiable, uniquely identifying that object over a cluster of AMP
 * installations. ClusterIdentifiable is a descendant of Identifiable, since it
 * has to be locally identifiable, too, in order for the internal permission
 * mechanism to work as expected.
 * @see Identifiable
 * http://docs.google.com/Doc?id=djf3gch_56gs9dqtjc
 */
public interface ClusterIdentifiable extends Identifiable {
    /**
     * @return a harmonized identifier, uniquely identifying the resource across
     *         a cluster of servers
     */
    public String getClusterIdentifier();
}
