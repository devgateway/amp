package org.digijava.module.aim.dbentity ;

import java.io.Serializable;


public class AmpReportHierarchy implements Serializable
{
	private AmpColumns column;
	private String levelId;

	public AmpColumns getColumn() {
		return column;
	}
	public void setColumn(AmpColumns column) {
		this.column = column;
	}
	public String getLevelId() {
		return levelId;
	}
	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}
}