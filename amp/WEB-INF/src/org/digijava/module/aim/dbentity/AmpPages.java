/**
 * author Priyajith 18-oct-04
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

@Deprecated
public class AmpPages implements Serializable {

    private Long ampPageId;
    private String pageName;
    private String pageCode;
    private Long ampTeamId;
    private Set filters;

    public Long getAmpPageId() {
        return ampPageId;
    }

    public String getPageName() {
        return pageName;
    }

    public Set getFilters() {
        return filters;
    }

    public void setAmpPageId(Long ampPageId) {
        this.ampPageId = ampPageId;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public void setFilters(Set filters) {
        this.filters = filters;
    }

    /**
     * @return
     */
    public String getPageCode() {
        return pageCode;
    }

    /**
     * @param string
     */
    public void setPageCode(String string) {
        pageCode = string;
    }

	/**
	 * @return Returns the ampTeamId.
	 */
	public Long getAmpTeamId() {
		return ampTeamId;
	}

	/**
	 * @param ampTeamId The ampTeamId to set.
	 */
	public void setAmpTeamId(Long ampTeamId) {
		this.ampTeamId = ampTeamId;
	}

}
