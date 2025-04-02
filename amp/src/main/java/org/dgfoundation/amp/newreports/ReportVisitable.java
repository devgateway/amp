package org.dgfoundation.amp.newreports;

/**
 * Defines an interface for classes that may be interpreted by a Report Visitor.
 * 
 * @author Viorel Chihai
 *
 */
public interface ReportVisitable {

    void accept(ReportVisitor visitor);
            
}
