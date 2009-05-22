package org.digijava.module.widget.helper;

import java.util.List;

import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpWidgetOrgProfile;

public class WidgetUpdatePlaceHelper {
	
	
	public List<AmpDaWidgetPlace> getOldplace() {
		return oldplace;
	}
	public void setOldplace(List<AmpDaWidgetPlace> oldplace) {
		this.oldplace = oldplace;
	}
	public AmpWidgetOrgProfile getOrgNewProfWidget() {
		return orgNewProfWidget;
	}
	public void setOrgNewProfWidget(AmpWidgetOrgProfile orgNewProfWidget) {
		this.orgNewProfWidget = orgNewProfWidget;
	}
	private  List<AmpDaWidgetPlace> oldplace;
	private AmpWidgetOrgProfile orgNewProfWidget;

}
