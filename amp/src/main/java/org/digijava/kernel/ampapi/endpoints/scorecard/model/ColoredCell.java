package org.digijava.kernel.ampapi.endpoints.scorecard.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Class that holds the information of the the level of completion of projects
 * updates for a given donor on a given quarter. Donors need to update their
 * projects every quarter. How good they do that task is defined by a color.
 *  - Green cell: If a donor has updated more projects that the number defined by doing:
 *  Number of Updated Projects by donor > = Threshold  X total live projects of a donor on a given quarter, then 
 *  the  cell for that quarter will be green. 
 * - Gray cell: It the admin has been identified as a donor with no project updates (as
 * specified here), the donor will have a grey cell filled in for that quarter
 * - Red cell: If a donor has not updated projects or has updated projects but didn’t reach the threshold and 
 * has not been identified as a donor with no project updates, the cell in the timeframe is filled in with
 * red. 
 * - Yellow cell: if a donor updates projects during the grace period, the update occurred is counted double. 
 *  It is counted on the quarter to which the grace period belongs; and it is also counted on the current quarter. 
 *  If the updates  performed on the grace period make the number of updates to reach or surpass the threshold for that quarter,
 *   then the previous quarter is marked with yellow. 
 * 
 * @author Emanuel Perez
 * 
 */
public class ColoredCell {

    public static enum Colors {
        GREEN, RED, GRAY, YELLOW
    };

    private Colors color;
    private Long donorId;
    private Quarter quarter;
    // updated activities on the current quarter
    private Set<String> updatedActivites = new HashSet<String>();

    // updated activities on the grace period of the previous quarter
    private Set<String> updatedActivitiesOnGracePeriod = new HashSet<String>();
    private Integer totalActivities = 0;

    public ColoredCell(Colors color) {
        this.color = color;
    }

    /**
     * Creates a new ColoredCell. 
     * New ColoredCells are given Colors.Red as default color
     * 
     */
    public ColoredCell() {
        this.color = Colors.RED;
    }

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    public Long getDonorId() {
        return donorId;
    }

    public void setDonorId(Long donorId) {
        this.donorId = donorId;
    }

    public Quarter getQuarter() {
        return quarter;
    }

    public void setQuarter(Quarter quarter) {
        this.quarter = quarter;
    }

    public Set<String> getUpdatedActivites() {
        return updatedActivites;
    }

    public void setUpdatedActivites(Set<String> updatedActivites) {
        this.updatedActivites = updatedActivites;
    }

    public Set<String> getUpdatedActivitiesOnGracePeriod() {
        return updatedActivitiesOnGracePeriod;
    }

    public void setUpdatedActivitiesOnGracePeriod(Set<String> updatedActivitiesOnGracePeriod) {
        this.updatedActivitiesOnGracePeriod = updatedActivitiesOnGracePeriod;
    }

    public Integer getTotalActivities() {
        return totalActivities;
    }

    public void setTotalActivities(Integer totalActivities) {
        this.totalActivities = totalActivities;
    }

}
