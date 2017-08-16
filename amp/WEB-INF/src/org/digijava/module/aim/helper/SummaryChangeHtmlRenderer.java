package org.digijava.module.aim.helper;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.Quarter;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.util.DbUtil;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * @author Aldo Picca
 */
public class SummaryChangeHtmlRenderer {

    private static final Logger LOGGER = Logger.getLogger(SummaryChangeHtmlRenderer.class);
    private final AmpActivityVersion activity;
    private final String locale;
    private final Collection<SummaryChange> changesList;
    private AmpFiscalCalendar fiscalCalendar;

    public SummaryChangeHtmlRenderer(AmpActivityVersion activity, Collection<SummaryChange> changesList, String
            locale) {
        this.activity = activity;
        this.changesList = changesList;
        if (locale == null) {
            locale = TLSUtils.getEffectiveLangCode();
        }
        this.locale = locale;
        this.fiscalCalendar = setFiscalCalendar();
    }

    private AmpFiscalCalendar setFiscalCalendar() {
        AmpApplicationSettings ampAppSettings = DbUtil.getTeamAppSettings(activity.getTeam().getAmpTeamId());
        return ampAppSettings.getFiscalCalendar();
    }

    public String render() {
        StringBuilder res = new StringBuilder("<table "
                + "style='font-family:Arial, Helvetica, sans-serif;font-size:12px;border: 1px solid black;"
                + "border-collapse:collapse' "
                + "cellspacing='2' cellpadding='2' border='1' "
                + " width='100%'>");
        res.append("\n");
        renderHeaders(res);
        res.append("\n");
        renderBody(res);
        res.append("</table>");
        res.append("\n");
        return res.toString();
    }

    protected StringBuilder renderHeaders(StringBuilder res) {
        res.append("<thead>");

        res.append(renderHeaderRow("Activity Title", activity.getName()));
        res.append(renderHeaderRow("Activity updated on", FormatHelper.formatDate(activity.getUpdatedDate())));
        res.append(renderHeaderRow("Activity last updated by", activity.getModifiedBy().getUser().getFirstNames()
                + " " + activity.getModifiedBy().getUser().getLastName()
                + " - " + activity.getModifiedBy().getUser().getEmail()));

        res.append(renderHeaderRow("Changes to financial information", null));
        res.append("</thead>");
        return res;
    }

    private String renderHeaderRow(String title, String value) {
        StringBuilder res = new StringBuilder();
        res.append("<tr>");
        res.append(String.format("<td width='50%%' style='padding-left: 5px;'><span style='font-weight: bold;"
                + "'>%s</span></td>", translateText(title)));
        res.append(String.format("<td width='50%%' style='padding-left: 5px;'>%s</td>", (value != null ? value : "")));
        res.append("</tr>\n");
        return res.toString();
    }

    private String renderChangeRow(String quarter, Collection<SummaryChange> changes) {
        StringBuilder res = new StringBuilder();
        res.append("<tr>");
        res.append(String.format("<td width='50%%' valign='top' style='padding-left: 5px;'>%s</td>", quarter));

        res.append("<td width='50%%' valign='top' style='padding-left: 5px;'>");
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
                        FormatHelper.formatNumber(summaryChange.getPreviousValue()), summaryChange.getPreviousCurrency()
                                .getCurrencyCode()));
                res.append(String.format("<li style='list-style-type: disc;'>%s: %s %s</li>", translateText("Current "
                                + "amount"),
                        FormatHelper.formatNumber(summaryChange.getCurrentValue()), summaryChange.getCurrentCurrency()
                                .getCurrencyCode()));
            } else {
                if (summaryChange.getChangeType() == SummaryChangesService.NEW) {
                    res.append(String.format("<li style='list-style-type: disc;'>%s %s</li>", FormatHelper
                                    .formatNumber(summaryChange.getCurrentValue()),
                            summaryChange.getCurrentCurrency().getCurrencyCode()));
                } else {
                    res.append(String.format("<li style='list-style-type: disc;'>%s %s</li>", FormatHelper.formatNumber(
                            summaryChange.getPreviousValue()), summaryChange.getPreviousCurrency().getCurrencyCode()));
                }
            }
            res.append("</ol>");

        }

        res.append("</td>\n");
        res.append("</tr>\n");
        return res.toString();
    }

    protected StringBuilder renderBody(StringBuilder res) {
        res.append("<tbody>\n");
        LinkedHashMap<String, Collection<SummaryChange>> quarterList = buildQuarterGroup();
        for (String quarter : quarterList.keySet()) {
            res.append(renderChangeRow(quarter, quarterList.get(quarter)));
        }
        res.append("</tbody>\n");
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
        }
        return null;
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
        return TranslatorWorker.translateText(text, this.locale, 3L);
    }
}
