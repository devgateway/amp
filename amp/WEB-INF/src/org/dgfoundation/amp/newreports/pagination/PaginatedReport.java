package org.dgfoundation.amp.newreports.pagination;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.newreports.ReportArea;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * this class indexes a ReportArea so that it can answer pagination requests extremely fastly
 * @author Dolghier Constantin
 *
 */
public class PaginatedReport {
    private final static Logger logger = Logger.getLogger(PaginatedReport.class);
    
    final ReportArea rootElement;
    final Map<ReportArea, ReportArea> parents;
    final List<ReportArea> indexedLeaves;
    
    public PaginatedReport(ReportArea rootElement) {
        long startTime = System.currentTimeMillis();
        this.rootElement = rootElement;
        this.parents = Collections.unmodifiableMap(mapParents(this.rootElement, new IdentityHashMap<>()));
        this.indexedLeaves = Collections.unmodifiableList(indexLeaves(this.rootElement, new ArrayList<>()));
        long deltaTime = System.currentTimeMillis() - startTime;
        logger.info(String.format("indexed a report with %d parents and %d leaves in %d millies\n", parents.size(), indexedLeaves.size(), deltaTime));
    }
    
    public ReportArea getPage(int start, int pageSize) {
        long startTime = System.currentTimeMillis();
        int firstRow = clamp(start, 0, indexedLeaves.size() - 1);
        int lastRow = clamp(firstRow + pageSize - 1, 0, indexedLeaves.size() - 1);
        Set<ReportArea> areasToInclude = computeAreasToInclude(firstRow, lastRow);
        // at this point, if an element is present in areasToInclude, then all of its parents are also included. If an element is not included, then none of its children is
        ReportArea res = scanAreas(rootElement, areasToInclude);
        long deltaTime = System.currentTimeMillis() - startTime;
        logger.info("getting page took " + deltaTime + " millies");
        return res;
    }
    
    /**
     * recursively scans the ReportArea tree and selects the trail cells of all the relevant nodes + the relevant children
     * @param elem
     * @param areasToInclude
     * @return
     */
    protected PaginatedReportArea scanAreas(ReportArea elem, Set<ReportArea> areasToInclude) {
        if (!areasToInclude.contains(elem))
            throw new IllegalStateException("pagination error: asked to scan an area which should not be scanned");
        
        List<PaginatedReportArea> scannedChildren = elem.getChildren() == null ? null : elem.getChildren().stream().filter(areasToInclude::contains).map(z -> scanAreas(z, areasToInclude)).collect(toList());
        return duplicate(elem, scannedChildren);
    }
    
    protected PaginatedReportArea duplicate(ReportArea elem, List<PaginatedReportArea> children) {
        return new PaginatedReportArea(elem, children);
    }
    
    /**
     * walks the tree up for all the leaves which form a page
     * @param firstRow
     * @param lastRow
     * @return
     */
    protected Set<ReportArea> computeAreasToInclude(int firstRow, int lastRow) {
        Set<ReportArea> areasToInclude = Collections.newSetFromMap(new IdentityHashMap<ReportArea, Boolean>());
        areasToInclude.add(rootElement); // the root is always included in the output
        for(int i = Math.max(firstRow, 0); i <= Math.min(lastRow, indexedLeaves.size() - 1); i++)
            addAreas(indexedLeaves.get(i), areasToInclude);
        return areasToInclude;
    }
    
    /**
     * walks a tree up and adds all the found nodes in a given set
     * @param elem
     * @param areas
     */
    protected void addAreas(ReportArea elem, Set<ReportArea> areas) {
        while (elem != null) {
            areas.add(elem);
            elem = parents.get(elem);
        }
    }
    
    /**
     * @param recordsPerPage - maximum number of records per page, or -1 if unlimited
     * @return page count for the given list of leaf areas and number of records per page
     */
    public int getPageCount(int recordsPerPage) {
        if (indexedLeaves.isEmpty())
            return 0;
        if (recordsPerPage == -1)
            return 1;
        return indexedLeaves.size() / recordsPerPage + signum(indexedLeaves.size() % recordsPerPage);
    }
    
    public int getRecordsCount() {
        return indexedLeaves.size();
    }
    
    @Override
    public String toString() {
        return String.format("parents: %d, leaves: %d", parents.size(), indexedLeaves.size());
    }
    /**
     * an inefficient way of using this class; only use it if you paginate a given report once
     * @param contents
     * @param start
     * @param pageSize
     * @return
     */
    public static ReportArea getPage(ReportArea contents, int start, int pageSize) {
        return new PaginatedReport(contents).getPage(start, pageSize);
    }
    
    protected static int clamp(int value, int min, int max) {
        if (value < min) value = min;
        if (value > max) value = max;
        return value;
    }
    
    protected static int signum(int val) {
        if (val == 0) return 0;
        if (val < 0) return -1;
        return 1;
    }
    
    /**
     * collects all the parents in a ReportArea tree
     * @param elem
     * @param res
     * @return
     */
    protected static IdentityHashMap<ReportArea, ReportArea> mapParents(ReportArea elem, IdentityHashMap<ReportArea, ReportArea> res) {
        if (elem == null) return res;
        if (elem.getChildren() != null) {
            for(ReportArea child:elem.getChildren()) {
                res.put(child, elem);
                mapParents(child, res);
            }
        }
        return res;
    }
    
    /**
     * collects all the leaves in a ReportArea tree
     * @param elem
     * @param res
     * @return
     */
    protected static List<ReportArea> indexLeaves(ReportArea elem, List<ReportArea> res) {
        if (elem == null) return res;
        if (elem.getChildren() == null)
            res.add(elem);
        else {
            for(ReportArea child:elem.getChildren())
                indexLeaves(child, res);
        }
        return res;
    }
        
}
