package org.dgfoundation.amp.newreports;

import java.util.Map;

/**
 * A visitor for traversing an Report Area
 * 
 * @author Viorel Chihai
 * 
 */

public interface ReportVisitor {

    public void visit(ReportArea area);

    public void visit(Map<ReportOutputColumn, ReportCell> contents);

}
