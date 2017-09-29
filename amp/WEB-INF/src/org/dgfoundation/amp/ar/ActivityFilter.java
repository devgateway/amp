package org.dgfoundation.amp.ar;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import org.dgfoundation.amp.nireports.output.NiReportFilterResult;
import org.dgfoundation.amp.reports.converters.AmpARFilterConverter;
import org.digijava.module.aim.startup.AMPStartupListener;
import org.digijava.module.aim.util.LuceneUtil;

/**
 * This class is used to filter activities according to filters.
 *
 * Filtering can happen in two contexts:
 * <ul>
 * <li>global - useful for computed workspaces, does not depend on TLSUtils
 * <li>current workspace - useful for search, depends on TLSUtils, see more in
 * {@link org.dgfoundation.amp.newreports.ReportEnvironment ReportEnvironment}
 * </ul>
 *
 * @author Octavian Ciubotaru
 */
public class ActivityFilter {

    private static final Logger logger = Logger.getLogger(ActivityFilter.class);

    private static final ActivityFilter INSTANCE = new ActivityFilter();

    public static ActivityFilter getInstance() {
        return INSTANCE;
    }

    /**
     * Returns activities that match the filter.
     * Order of returned activities may be important.
     */
    public Set<Long> filter(AmpARFilter filter) {
        Set<Long> activityIds = niReportsFilter(filter);

        if (StringUtils.isNotBlank(filter.getIndexText())) {
            Set<Long> luceneIds = luceneSearch(filter);
            luceneIds.retainAll(activityIds); // lucene ids order is important
            activityIds = luceneIds;
        }

        return activityIds;
    }

    /**
     * Filter activities according to AmpARFilter.
     */
    private Set<Long> niReportsFilter(AmpARFilter arFilter) {
        AmpARFilterConverter ampARFilterConverter = new AmpARFilterConverter(arFilter);
        AmpReportFilters filters = ampARFilterConverter.buildFilters();

        ReportSpecificationImpl spec = new ReportSpecificationImpl("filter", ArConstants.DONOR_TYPE);
        spec.setFilters(filters);

        NiReportsEngine engine = new NiReportsEngine(AmpReportsSchema.getInstance(), spec);
        NiReportFilterResult result = engine.executeFilter();

        logger.info("NiFilter activities: " + result.getActivityIds().size());

        return result.getActivityIds();
    }

    /**
     * Returns activities that match the full text search. Order of activities is important.
     */
    private Set<Long> luceneSearch(AmpARFilter filter) {
        String index = AMPStartupListener.SERVLET_CONTEXT_ROOT_REAL_PATH + LuceneUtil.ACTIVITY_INDEX_DIRECTORY;
        Document[] docs = LuceneUtil.search(index, "all", filter.getIndexText(), filter.getSearchMode());

        Set<Long> ids = new LinkedHashSet<>();
        for (Document doc : docs) {
            Long id = Long.parseLong(doc.get("id"));
            ids.add(id);
        }

        logger.info("Lucene activities: " + ids.size());

        return ids;
    }
}
