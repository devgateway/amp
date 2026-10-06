package org.digijava.module.aim.helper;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.Quarter;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * @author Aldo Picca
 */
public class SummaryChangeHtmlRenderer {

    private static final Logger LOGGER = Logger.getLogger(SummaryChangeHtmlRenderer.class);
    public static final long SITE_ID = 3L;
    private final AmpActivityVersion activity;
    private final String locale;
    private final Collection<SummaryChange> changesList;
    private AmpFiscalCalendar fiscalCalendar;
    private AmountsUnits amountsUnits;

    public SummaryChangeHtmlRenderer(AmpActivityVersion activity, Collection<SummaryChange> changesList, String
            locale) {
        this.activity = activity;
        this.changesList = changesList;
        if (locale == null) {
            locale = TLSUtils.getEffectiveLangCode();
        }
        this.locale = locale;
        this.fiscalCalendar = setFiscalCalendar();
        this.amountsUnits = AmountsUnits.getDefaultValue();
    }

    private AmpFiscalCalendar setFiscalCalendar() {
        AmpApplicationSettings ampAppSettings = DbUtil.getTeamAppSettings(activity.getTeam().getAmpTeamId());
        return ampAppSettings.getFiscalCalendar();
    }

    public String render() {
        StringBuilder res = new StringBuilder("<table "
                + "style='font-family:Arial, Helvetica, sans-serif;font-size:12px;border: 1px solid black;"
                + "border-collapse:collapse;color:black;' "
                + "cellspacing='2' cellpadding='2' border='1' "
                + " width='100%'>");
        renderHeaders(res);
        renderBody(res);
        res.append("</table>");
        return res.toString();
    }

    public String renderWithLegend() {
        StringBuilder res = new StringBuilder();
        res.append(addAmountsInThousandsLegend());
        res.append(render().toString());
        return res.toString();
    }

    private String render(boolean showAmountsInThousandsLegend) {
        if (showAmountsInThousandsLegend) {
            return renderWithLegend();
        } else {
            return render();
        }
    }

    protected StringBuilder renderHeaders(StringBuilder res) {
        res.append("<thead>");

        res.append(renderHeaderRow("Activity Title", activity.getName()));
        res.append(renderHeaderRow("Activity updated on", FormatHelper.formatDate(activity.getUpdatedDate())));
        res.append(renderHeaderRow("Activity last updated by", getModifiedByInfo(activity)));
        res.append(renderHeaderRow("Changes to financial information", null));
        res.append("</thead>");
        return res;
    }

    private String getModifiedByInfo(AmpActivityVersion activity) {
        StringBuilder res = new StringBuilder();
        ActivityHistory auditHistory = null;

        if (activity.getModifiedBy() == null || (activity.getUpdatedDate() == null
                && activity.getModifiedDate() == null)) {
            auditHistory = ActivityUtil.getModifiedByInfoFromAuditLogger(activity.getAmpActivityId());
            res.append(ActivityUtil.getModifiedByUserName(activity, auditHistory));
        } else {
            res.append(activity.getModifiedBy().getUser().getFirstNames()
                    + " " + activity.getModifiedBy().getUser().getLastName()
                    + " - " + activity.getModifiedBy().getUser().getEmail());
        }
        return res.toString();
    }

    private String renderHeaderRow(String title, String value) {
        StringBuilder res = new StringBuilder();
        res.append("<tr>");
        res.append(String.format("<td width='50%%' style='padding: 0px 0px 0px 5px;'><span style='font-weight: bold;"
                + "'>%s</span></td>", translateText(title)));
        res.append(String.format("<td width='50%%' style='padding: 0px 0px 0px 5px;'>%s</td>", (value != null ? value
                : "")));
        res.append("</tr>");
        return res.toString();
    }

    private String addAmountsInThousandsLegend() {
        StringBuilder res = new StringBuilder();
        String legend = getAmountsInThousandsLegend();
        if (legend != null) {
            res.append("<table "
                    + "style='font-family:Arial, Helvetica, sans-serif;font-size:12px;"
                    + "border-collapse:collapse' "
                    + "cellspacing='2' cellpadding='2' border='0' "
                    + " width='100%'>");

            res.append("<tr>");
            res.append(String.format("<td width='100%%' style='padding: 0px 0px 0px 5px;'>"
                    + "<span style='color:red'>%s</span></td>", translateText(legend)));
            res.append("</tr>");
            res.append("</table>");
        }
        return res.toString();
    }

    private String getAmountsInThousandsLegend() {
        int amountsUnitCode = Integer.valueOf(FeaturesUtil.getGlobalSettingValue(
                GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS));

        if (amountsUnitCode == AmpARFilter.AMOUNT_OPTION_IN_THOUSANDS) {
            return SettingsConstants.ID_NAME_MAP.get(SettingsConstants.AMOUNT_UNITS_1000);
        } else if (amountsUnitCode == AmpARFilter.AMOUNT_OPTION_IN_MILLIONS) {
            return SettingsConstants.ID_NAME_MAP.get(SettingsConstants.AMOUNT_UNITS_1000000);
        }
        return null;
    }

    private String renderChangeRow(String quarter, Collection<SummaryChange> changes) {
        StringBuilder res = new StringBuilder();
        res.append("<tr>");
        res.append(String.format("<td width='50%%' valign='top' style='padding: 0px 0px 0px 5px;'>%s</td>", quarter));

        res.append("<td width='50%%' valign='top' style='padding: 0px 0px 0px 5px;'>");
        for (SummaryChange summaryChange : changes) {

            res.append(String.format("<span style='font-weight: bold;'>%s</span>",
                    translateText(summaryChange.getAdjustmentType().getValue()) + " "
                            + translateText(ArConstants.TRANSACTION_ID_TO_TYPE_NAME.get(summaryChange
                                    .getTransactionType()))));
            res.append(String.format("<br><font color='" + getFontColor(summaryChange.getChangeType())
                    + "'>%s</font>", translateText(summaryChange.getChangeType())));
            res.append("<ol>");

            if (summaryChange.getChangeType() == SummaryChangesService.EDITED) {
                res.append(String.format("<li style='list-style-type: disc;'>%s: %s %s</li>", translateText("Previous"
                                + " amount"),
                        FormatHelper.formatNumber(formatCurrency(summaryChange.getPreviousValue())), summaryChange
                                .getPreviousCurrency()
                                .getCurrencyCode()));
                res.append(String.format("<li style='list-style-type: disc;'>%s: %s %s</li>", translateText("Current "
                                + "amount"),
                        FormatHelper.formatNumber(formatCurrency(summaryChange.getCurrentValue())), summaryChange
                                .getCurrentCurrency()
                                .getCurrencyCode()));
            } else {
                if (summaryChange.getChangeType() == SummaryChangesService.NEW) {
                    res.append(String.format("<li style='list-style-type: disc;'>%s %s</li>", FormatHelper
                                    .formatNumber(formatCurrency(summaryChange.getCurrentValue())),
                            summaryChange.getCurrentCurrency().getCurrencyCode()));
                } else {
                    res.append(String.format("<li style='list-style-type: disc;'>%s %s</li>", FormatHelper.formatNumber(
                            formatCurrency(summaryChange.getPreviousValue())), summaryChange.getPreviousCurrency()
                            .getCurrencyCode()));
                }
            }
            res.append("</ol>");

        }

        res.append("</td>");
        res.append("</tr>");
        return res.toString();
    }

    private double formatCurrency(Double amount) {
        return amount / amountsUnits.divider;
    }

    protected StringBuilder renderBody(StringBuilder res) {
        res.append("<tbody>");
        LinkedHashMap<String, Collection<SummaryChange>> quarterList = buildQuarterGroup();
        for (String quarter : quarterList.keySet()) {
            res.append(renderChangeRow(quarter, quarterList.get(quarter)));
        }
        res.append("</tbody>");
        return res;
    }

    protected String getFontColor(String changeType) {
        switch (changeType) {
            case SummaryChangesService.NEW:
                return "green";
            case SummaryChangesService.EDITED:
                return "orange";
            case SummaryChangesService.DELETED:
                return "red";
            default:
                return null;
        }
    }

    private static String buildQuarterLabel(SummaryChange summaryChange, Quarter quarter) {
        String label = "Q " + quarter.getQuarterNumber() + " " + quarter.getYearCode();
        if (summaryChange.getTransactionType() == Constants.MTEFPROJECTION) {
            label = "FY " + quarter.getYearCode();
        }
        return label;
    }

    protected LinkedHashMap<String, Collection<SummaryChange>> buildQuarterGroup() {
        LinkedHashMap<String, Collection<SummaryChange>> activitiesChanges = new LinkedHashMap<>();
        for (SummaryChange change : changesList) {
            Quarter quarter = new Quarter(this.fiscalCalendar, change.getTransactionDate());
            activitiesChanges.computeIfAbsent(buildQuarterLabel(change, quarter), z ->
                    new LinkedHashSet<SummaryChange>()).add(change);
        }
        return activitiesChanges;
    }

    protected String translateText(String text) {
        return TranslatorWorker.translateText(text, this.locale, SITE_ID);
    }
}
