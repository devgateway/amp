package org.dgfoundation.amp.nireports.testcases.generic;

import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class SecondarySectorCells extends HardcodedCells<PercentageTextCell>{

    public SecondarySectorCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
        super(activityNames, entityNames, lc);
    }

    @Override
    protected List<PercentageTextCell> populateCells() {
        return  Arrays.asList(
            cell("activity 1 with agreement", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("Activity 2 with multiple agreements", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("Activity Linked With Pledge", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("Activity with both MTEFs and Act.Comms", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("activity with capital spending", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("activity with components", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("activity with contracting agency", "5 REGIONAL DEVELOPMENT", 6492, 1.000000),
            cell("activity with directed MTEFs", "1-DEMOCRATIC COUNTRY", 6475, 0.500000),
            cell("activity with directed MTEFs", "4 HUMAN RESOURCES", 6487, 0.500000),
            cell("activity with funded components", "5 REGIONAL DEVELOPMENT", 6492, 1.000000),
            cell("activity with incomplete agreement", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("activity with many MTEFs", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("activity with pipeline MTEFs and act. disb", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("Activity with planned disbursements", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("activity with primary_program", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("Activity with primary_tertiary_program", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("activity with tertiary_program", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("Activity with Zones", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("Activity With Zones and Percentages", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("activity-weird-funding", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("activity-with-unfunded-components", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("activity_with_disaster_response", "3 NATIONAL COMPETITIVENESS", 6481, 1.000000),
            cell("arrears test", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("crazy funding 1", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("date-filters-activity", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("department/division", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("Eth Water", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("execution rate activity", "3 NATIONAL COMPETITIVENESS", 6481, 1.000000),
            cell("expenditure class", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("mtef activity 1", "02 TRANSDNISTRIAN CONFLICT", 6480, 1.000000),
            cell("mtef activity 2", "02 TRANSDNISTRIAN CONFLICT", 6480, 1.000000),
            cell("PID: original", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("PID: original > actual", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("PID: original, actual", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("PID: original, proposed", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("PID: original, proposed, actual", "02 TRANSDNISTRIAN CONFLICT", 6480, 1.000000),
            cell("pledged 2", "4 HUMAN RESOURCES", 6487, 1.000000),
            cell("pledged education activity 1", "4 HUMAN RESOURCES", 6487, 1.000000),
            cell("Project with documents", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("Proposed Project Cost 1 - USD", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("Proposed Project Cost 2 - EUR", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("ptc activity 1", "02 TRANSDNISTRIAN CONFLICT", 6480, 1.000000),
            cell("ptc activity 2", "02 TRANSDNISTRIAN CONFLICT", 6480, 1.000000),
            cell("Pure MTEF Project", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("Real SSC Activity 1", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("Real SSC Activity 2", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("second with disaster response", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("SSC Project 1", "02 TRANSDNISTRIAN CONFLICT", 6480, 1.000000),
            cell("SSC Project 2", "3 NATIONAL COMPETITIVENESS", 6481, 1.000000),
            cell("SubNational no percentages", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("TAC_activity_1", "3 NATIONAL COMPETITIVENESS", 6481, 1.000000),
            cell("TAC_activity_2", "5 REGIONAL DEVELOPMENT", 6492, 1.000000),
            cell("Test MTEF directed", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("third activity with agreements", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("Unvalidated activity", "1-DEMOCRATIC COUNTRY", 6475, 1.000000),
            cell("with annual ppc and actual comm", "3 NATIONAL COMPETITIVENESS", 6481, 1.000000),
            cell("with weird currencies", "1-DEMOCRATIC COUNTRY", 6475, 1.000000));
    }

}
