package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.dbentity.Country;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpWoreda;
import org.digijava.module.aim.dbentity.AmpZone;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;

public class LocationSelected extends Action {

	private static Logger logger = Logger.getLogger(LocationSelected.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response) throws Exception {

		EditActivityForm eaForm = (EditActivityForm) form;
		
		Location location[] = null;
		
		boolean flag = false;

		String cntryName = "";
		Long cntryId = null;
		String regnName = "";
		Long regnId = null;
		String zoneName = "";
		Long zoneId = null;
		
		if (eaForm.getImpLevelValue().intValue() == 1) {
			location = new Location[1];
			Collection col =FeaturesUtil.getGlobalSettings();
            Iterator itr = col.iterator();
            Country cntry =null;
            String Iso=null;
        //    while(itr.hasNext())
          //  {
            	AmpGlobalSettings ampgs = (AmpGlobalSettings) itr.next();
            	logger.info(" this is the default country..."+ampgs.getGlobalSettingsValue());
             	Iso = ampgs.getGlobalSettingsValue();
             	cntry = DbUtil.getDgCountry(Iso);
            	logger.info(" this is the ISO "+ Iso+" this is the country got ..... "+ cntry);
            //}
			//Country cntry = DbUtil.getDgCountry(Constants.COUNTRY_ISO);
			location[0] = new Location();
			location[0].setLocId(new Long(System.currentTimeMillis()));
			location[0].setCountryId(cntry.getCountryId());
			location[0].setCountry(cntry.getCountryName());
			location[0].setNewCountryId(cntry.getIso());
			//determine whether activity already has 1 or more locations
			if(eaForm.getSelectedLocs()!=null && eaForm.getSelectedLocs().size()>0) {
				//location[0].setPercent(null);
				Iterator iter=eaForm.getSelectedLocs().iterator();
				while (iter.hasNext()) {
					Location loc=(Location)iter.next();
					//loc.setPercent(null);
				}					
			}else {
				location[0].setPercent("100");
			}		
			
		}
		
		if (eaForm.getImpLevelValue().intValue() > 1) {
			if (eaForm.getImpCountry() != null) {
				Country cntry =null;
	            String Iso=null;
	            Collection col =FeaturesUtil.getGlobalSettings();
	            Iterator itr = col.iterator();
	            //at merging pls keep this while comment :)
	            //while(itr.hasNext())
	            {
	            	AmpGlobalSettings ampgs = (AmpGlobalSettings) itr.next();
	            	logger.info(" this is the default country..."+ampgs.getGlobalSettingsValue());
	             	Iso = ampgs.getGlobalSettingsValue();
	            	cntry = DbUtil.getDgCountry(Iso);
	            	logger.info(" this is the ISO "+ Iso+" this is the country got ..... "+ cntry);
	            }
				//Country cntry = DbUtil.getDgCountry(Constants.COUNTRY_ISO);
				cntryId = cntry.getCountryId();
				cntryName = cntry.getCountryName();
				logger.info(" this is the country ...." +cntry.getCountryName()+ " ID "+cntry.getCountryId() );
				flag = false;
			} else
				flag = true;
		}

		if (eaForm.getImpLevelValue().intValue() == 2) {
			if (eaForm.getImpMultiRegion() != null && eaForm.getImpMultiRegion().length > 0) {
				
				Long rgn[] = eaForm.getImpMultiRegion();
				location = new Location[rgn.length];
				AmpRegion reg;
				Long id = new Long(System.currentTimeMillis());
				for (int i = 0; i < rgn.length; i++) {
					reg = LocationUtil.getAmpRegion(rgn[i]);
					if (reg != null) {
						location[i] = new Location();
						location[i].setLocId(id);
						id = new Long(id.longValue() + 1);
						location[i].setCountryId(cntryId);// from implev=1
						location[i].setCountry(cntryName);// from implev=1
						location[i].setRegionId(reg.getAmpRegionId());
						location[i].setRegion(reg.getName());
						//determine whether activity already has 1 or more locations
						if(eaForm.getSelectedLocs()!=null && eaForm.getSelectedLocs().size()>0) {
							//location[0].setPercent(null);
							Iterator iter=eaForm.getSelectedLocs().iterator();
							while (iter.hasNext()) {
								Location loc=(Location)iter.next();
								//loc.setPercent(null);
							}					
						}else {
							location[0].setPercent("100");
						}		
						if (eaForm.getFundingRegions() == null) {
							eaForm.setFundingRegions(new ArrayList());
						}
						if (eaForm.getFundingRegions().contains(reg) == false) {
							eaForm.getFundingRegions().add(reg);
						}
					}
				}
				flag = false;
			} else
				flag = true;
		}

		if (eaForm.getImpLevelValue().intValue() > 2) {
			if (eaForm.getImpRegion() != null
					&& (!eaForm.getImpRegion().equals(new Long(-1)))) {
				AmpRegion reg = LocationUtil.getAmpRegion(eaForm.getImpRegion());
				regnId = reg.getAmpRegionId();
				regnName = reg.getName();
				if (eaForm.getFundingRegions() == null) {
					eaForm.setFundingRegions(new ArrayList());
				}
				if (eaForm.getFundingRegions().contains(reg) == false) {
					eaForm.getFundingRegions().add(reg);
				}				
				flag = false;
			} else
				flag = true;
		}

		if (eaForm.getImpLevelValue().intValue() == 3) {
			if (eaForm.getImpMultiZone() != null && eaForm.getImpMultiZone().length > 0) {
				logger.info("imp ID is 3 and length="
						+ eaForm.getImpMultiZone().length);

				Long zone[] = eaForm.getImpMultiZone();
				location = new Location[zone.length];

				AmpZone zne;
				Long id = new Long(System.currentTimeMillis());
				for (int i = 0; i < zone.length; i++) {
					zne = LocationUtil.getAmpZone(zone[i]);
					if (zne != null) {
						logger.info("zone=" + zone[i] + ":ZONE-ID::"+ zne.getAmpZoneId() + "::ZONE-NAME::"+ zne.getName());
						location[i] = new Location();
						location[i].setLocId(id);
						id = new Long(id.longValue() + 1);
						location[i].setCountryId(cntryId);// from implev=1
						location[i].setCountry(cntryName);// from implev=1
						location[i].setRegionId(regnId);// from implev=2
						location[i].setRegion(regnName);// from implev=2
						location[i].setZoneId(zne.getAmpZoneId());
						location[i].setZone(zne.getName());
						//determine whether activity already has 1 or more locations
						if(eaForm.getSelectedLocs()!=null && eaForm.getSelectedLocs().size()>0) {
							//location[0].setPercent(null);
							Iterator iter=eaForm.getSelectedLocs().iterator();
							while (iter.hasNext()) {
								Location loc=(Location)iter.next();
								//loc.setPercent(null);
							}					
						}else {
							location[0].setPercent("100");
						}		
					}
				}
				flag = false;
			} else
				flag = true;
		}

		if (eaForm.getImpLevelValue().intValue() > 3) {
			if (eaForm.getImpZone() != null
					&& (!eaForm.getImpZone().equals(new Long(-1)))) {
				AmpZone zon = LocationUtil.getAmpZone(eaForm.getImpZone());
				zoneId = zon.getAmpZoneId();
				zoneName = zon.getName();
				flag = false;
			} else
				flag = true;
		}
		
		if (eaForm.getImpLevelValue().intValue() == 4) {
			if (eaForm.getImpMultiWoreda() != null && eaForm.getImpMultiWoreda().length > 0) {

				Long woreda[] = eaForm.getImpMultiWoreda();
				location = new Location[woreda.length];

				AmpWoreda wrda;
				Long id = new Long(System.currentTimeMillis());
				for (int i = 0; i < woreda.length; i++) {
					wrda = LocationUtil.getAmpWoreda(woreda[i]);
					if (wrda != null) {
						location[i] = new Location();
						location[i].setLocId(id);
						id = new Long(id.longValue() + 1);
						location[i].setCountryId(cntryId);// from implev=1
						location[i].setCountry(cntryName);// from implev=1
						location[i].setRegionId(regnId);// from implev=2
						location[i].setRegion(regnName);// from implev=2
						location[i].setZoneId(zoneId);// from implev=3
						location[i].setZone(zoneName);// from implev=3
						location[i].setWoredaId(wrda.getAmpWoredaId());
						location[i].setWoreda(wrda.getName());
						//determine whether activity already has 1 or more locations
						if(eaForm.getSelectedLocs()!=null && eaForm.getSelectedLocs().size()>0) {
							//location[0].setPercent(null);
							Iterator iter=eaForm.getSelectedLocs().iterator();
							while (iter.hasNext()) {
								Location loc=(Location)iter.next();
								//loc.setPercent(null);
							}					
						}else {
							location[0].setPercent("100");
						}		
					}
				}
				flag = false;
			} else
				flag = true;
		}		
		
		if (flag) {
			logger.info("returning from here");
			return mapping.findForward("forward");
		}
		
		if (eaForm.getSelectedLocs() != null) {
			for (int i = 0;i < location.length;i ++) {
				boolean present = false;
				Iterator itr = eaForm.getSelectedLocs().iterator();
				while (itr.hasNext()) {
					Location tempLoc = (Location) itr.next();
					if (tempLoc.equals(location[i])) {
						present = true;
						break;
					}
				}
				if (!present) eaForm.getSelectedLocs().add(location[i]);				
			}
		} else {
			eaForm.setSelectedLocs(new ArrayList<Location>());
			for (int i = 0;i < location.length;i ++) {
				eaForm.getSelectedLocs().add(location[i]);
			}
		}
		return mapping.findForward("forward");

	}	
}