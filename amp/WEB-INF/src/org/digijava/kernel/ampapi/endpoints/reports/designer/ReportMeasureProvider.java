package org.digijava.kernel.ampapi.endpoints.reports.designer;

import org.dgfoundation.amp.ar.MeasureConstants;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportMeasureType.ALL;
import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportMeasureType.DERIVED;
import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportMeasureType.PLEDGE;

/**
 * Fetch the amp measures from the system and provide the needed list based on the report type
 *
 * @author Viorel Chihai
 */
public class ReportMeasureProvider {

    public static final String MODULE_MEASURE_NAME = "Measures";

    private TranslatorService translatorService;

    public ReportMeasureProvider(final TranslatorService translatorService) {
        this.translatorService = translatorService;
    }

    public List<ReportMeasure> getMeasures(final ReportType type) {
        List<AmpMeasures> ampMeasures = fetchAmpMeasures(type);

        return ampMeasures.stream()
                .filter(this::isAmpMeasureVisible)
                .map(this::convertAmpMeasureToReportMeasure)
                .collect(Collectors.toList());
    }

    private List<AmpMeasures> fetchAmpMeasures(final ReportType type) {
        List<AmpMeasures> ampMeasures = new ArrayList<>();

        if (type.isPledge()) {
            ampMeasures.addAll(fetchAmpMeasuresByTypes(PLEDGE.getValue()));
        } else if (type.isDonor()) {
            ampMeasures.addAll(fetchAmpMeasuresByTypes(ALL.getValue(), DERIVED.getValue()));
        } else {
            ampMeasures.addAll(fetchAmpMeasuresByTypes(ALL.getValue()));
        }

        boolean mtefEnabled = FeaturesUtil.
                getGlobalSettingValueBoolean(GlobalSettingsConstants.MTEF_ANNUAL_DATE_FORMAT);

        if (mtefEnabled) {
            return ampMeasures.stream()
                    .filter(m -> !isMTEFName(m.getMeasureName()))
                    .collect(Collectors.toList());
        }

        return ampMeasures;
    }

    public List<AmpMeasures> fetchAmpMeasuresByTypes(final String... types) {
        return PersistenceManager.getSession()
                .createCriteria(AmpMeasures.class)
                .add(Restrictions.in("type", types))
                .setCacheable(true)
                .addOrder(Order.asc("measureName"))
                .list();
    }

    private boolean isMTEFName(String name) {
        String regex = "^(" + MeasureConstants.MTEF
                + "|" + MeasureConstants.REAL_MTEF
                + "|" + MeasureConstants.MTEF_PROJECTIONS
                + "|" + MeasureConstants.PIPELINE_MTEF_PROJECTIONS
                + "|" + MeasureConstants.PROJECTION_MTEF_PROJECTIONS
                + ").*$";

        return name.matches(regex);
    }

    private boolean isAmpMeasureVisible(AmpMeasures m) {
        return FeaturesUtil.isVisibleFeature(MODULE_MEASURE_NAME, m.getMeasureName());
    }

    private ReportMeasure convertAmpMeasureToReportMeasure(final AmpMeasures ampMeasure) {
        ReportMeasure reportMeasure = new ReportMeasure(ampMeasure.getMeasureId(),
                ampMeasure.getMeasureName(), translatorService.translateText(ampMeasure.getAliasName()),
                translatorService.translateText(ampMeasure.getDescription()),
                ReportMeasureType.fromString(ampMeasure.getType()));

        return reportMeasure;
    }
}
