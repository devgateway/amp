package org.digijava.module.aim.dbentity;

/**
 * author Priyajith 18-oct-04
 */

import java.io.Serializable;

public class AmpTeamPageFilters implements Serializable {

    private Long ampTeamPageFilterId;
    private AmpTeam team;
	private AmpPages page;
	private AmpFilters filter;


    /**
     * @return Returns the ampTeamPageFilterId.
     */
    public Long getAmpTeamPageFilterId() {
        return ampTeamPageFilterId;
    }
    /**
     * @param ampTeamPageFilterId The ampTeamPageFilterId to set.
     */
    public void setAmpTeamPageFilterId(Long ampTeamPageFilterId) {
        this.ampTeamPageFilterId = ampTeamPageFilterId;
    }
    /**
     * @return Returns the filter.
     */
    public AmpFilters getFilter() {
        return filter;
    }
    /**
     * @param filter The filter to set.
     */
    public void setFilter(AmpFilters filter) {
        this.filter = filter;
    }
    /**
     * @return Returns the page.
     */
    public AmpPages getPage() {
        return page;
    }
    /**
     * @param page The page to set.
     */
    public void setPage(AmpPages page) {
        this.page = page;
    }
    /**
     * @return Returns the team.
     */
    public AmpTeam getTeam() {
        return team;
    }
    /**
     * @param team The team to set.
     */
    public void setTeam(AmpTeam team) {
        this.team = team;
    }
}
