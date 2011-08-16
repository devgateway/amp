/**
 * 
 */
package org.digijava.module.admin.helper;

import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpTeam;

/**
 * @author dan
 *
 */
public class AmpActivityAdmin {

	/**
	 * 
	 */
	
	private String name;
	private AmpTeam team;
	private String ampId;
	private Long ampActivityId;
	private AmpActivityGroup ampActGroup;
	
	public AmpActivityAdmin(String name, AmpTeam team, String ampId, Long ampActivityId, AmpActivityGroup ampActGroup) {
		super();
		this.name = name;
		this.team = team;
		this.ampId = ampId;
		this.ampActivityId = ampActivityId;
		this.ampActGroup = ampActGroup;
	}

	public AmpActivityGroup getAmpActGroup() {
		return ampActGroup;
	}

	public void setAmpActGroup(AmpActivityGroup ampActGroup) {
		this.ampActGroup = ampActGroup;
	}

	public AmpActivityAdmin(String name, AmpTeam team, String ampId, Long ampActivityId) {
		super();
		this.name = name;
		this.team = team;
		this.ampId = ampId;
		this.ampActivityId = ampActivityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AmpTeam getTeam() {
		return team;
	}

	public void setTeam(AmpTeam team) {
		this.team = team;
	}

	public String getAmpId() {
		return ampId;
	}

	public void setAmpId(String ampId) {
		this.ampId = ampId;
	}

	public Long getAmpActivityId() {
		return ampActivityId;
	}

	public void setAmpActivityId(Long ampActivityId) {
		this.ampActivityId = ampActivityId;
	}

	public AmpActivityAdmin() {
		// TODO Auto-generated constructor stub
	}

}
