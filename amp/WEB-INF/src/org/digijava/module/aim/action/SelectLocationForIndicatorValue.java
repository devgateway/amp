	package org.digijava.module.aim.action;
	
	import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.dbentity.Country;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpWoreda;
import org.digijava.module.aim.dbentity.AmpZone;
import org.digijava.module.aim.form.ThemeForm;
import org.digijava.module.aim.helper.AmpPrgIndicatorValue;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
	
	public class SelectLocationForIndicatorValue   extends Action {
		
	    public ActionForward execute(ActionMapping mapping, ActionForm form,
	                                 HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
	        ThemeForm themeForm = (ThemeForm) form;
	        String strIndex = request.getParameter("index");
	        if(strIndex == null && themeForm.getParentIndex() == null) {
	            return mapping.findForward("forward");
	        } else if(strIndex != null) {
	            themeForm.setParentIndex(Long.valueOf(strIndex));
	        }
	        
	        
	        String iso= FeaturesUtil.getDefaultCountryIso();
	        Long countryId;
	        Country ampGS=null;
	        String CountryName = null; 
	        if(iso!=null){
	        	themeForm.setDefaultCountryIsSet(true);                 
	            Collection<Country> b = FeaturesUtil.getDefaultCountry(iso);
	            Iterator<Country> itr2 = b.iterator();
	            while (itr2.hasNext()) {
	            ampGS = (Country) itr2.next();
	            CountryName = ampGS.getCountryName();            
	          }
	
	          if (themeForm.getFill() == null || themeForm.getFill().trim().length() == 0) {
	        	  themeForm.setCountry(CountryName);
	        	  themeForm.setImpCountry(iso);
	        	  themeForm.setRegions(LocationUtil.getAllRegionsUnderCountry(iso));           
	          }
	          else {
	            if (themeForm.getFill().equals("zone")) {
	              if (themeForm.getImpRegion() != null) {
	            	  themeForm.setZones(LocationUtil.getAllZonesUnderRegion(themeForm.getImpRegion()));               
	            	  themeForm.setRegions(LocationUtil.getAllRegionsUnderCountry(iso));
	            	  themeForm.setImpZone(null);
	//            	  themeForm.setImpMultiZone(null);
	//            	  themeForm.setImpMultiWoreda(null);
	            	  themeForm.setImpWoreda(null);                
	              }
	            }
	            else if (themeForm.getFill().equals("woreda")) {
	              if (themeForm.getImpZone() != null) {
	            	  themeForm.setWoredas(LocationUtil.getAllWoredasUnderZone(themeForm.getImpZone()));
	            	  themeForm.setZones(LocationUtil.getAllZonesUnderRegion(themeForm.getImpRegion()));                
	            	  themeForm.setRegions(LocationUtil.getAllRegionsUnderCountry(iso));
	            	  themeForm.setImpWoreda(null);
	              }
	            }
	          }
	        }
	        else{
	        	themeForm.setDefaultCountryIsSet(false);
	      }        
	
	        
	
	        if(themeForm.getAction() != null && themeForm.getAction().equalsIgnoreCase("add")) {
	        	
	        	AmpPrgIndicatorValue indValue = (AmpPrgIndicatorValue) themeForm.getPrgIndValues().get(themeForm.getParentIndex().intValue());
	        	
	        	AmpLocation location=new AmpLocation();
//	        	if(indValue.getLocation()!=null){
//	        		location=indValue.getLocation();
//	        	}else {
//	        		location=new AmpLocation();
//	        	}
	            //National
		   		if(themeForm.getLocationLevelIndex().intValue()>=1){
		   			location.setDgCountry(ampGS);
		   			location.setCountry(CountryName);
		   		}
		   		//Regional
		   		if (themeForm.getLocationLevelIndex().intValue()>=2){
		   			AmpRegion region=LocationUtil.getAmpRegion(themeForm.getImpRegion());
		   			location.setAmpRegion(region);
		   			location.setRegion(region.getName());	   			
		   		}
		   		//District
		   		if (themeForm.getLocationLevelIndex().intValue()>=3){
		   			AmpZone zone=LocationUtil.getAmpZone(themeForm.getImpZone());
		   			location.setAmpZone(zone);
		   			location.setZone(zone.getName());
		   			AmpWoreda woreda=LocationUtil.getAmpWoreda(themeForm.getImpWoreda());
		   			location.setAmpWoreda(woreda);
		   			if(woreda!=null && woreda.getName()!=null){
		   				location.setWoreda(woreda.getName());
		   			}
		   			
		   		}
	
	            
	            indValue.setLocation(location);
	            themeForm.setLocationLevelIndex(-1);
	            themeForm.setParentIndex(null);
	            themeForm.setAction(null);
	            themeForm.setSelectedLocationId(null);
	            themeForm.setLocation(null);
	            themeForm.setLocationLevelIndex(null);
	            themeForm.setImpCountry(null);
	            themeForm.setImpRegion(null);
	            themeForm.setImpZone(null);
	            themeForm.setImpWoreda(null);
	            themeForm.setAction(null);
	            return mapping.findForward("backToAddDataPage");
	        }
	
	//        Collection<AmpRegion> locations = LocationUtil.getAmpLocations();
	//        if(locations != null) {
	//            List<AmpRegion> locationsList = new ArrayList<AmpRegion>(locations);
	//            if(locationsList != null && themeForm.getKeyword() != null) {
	//                for(Iterator iter = locationsList.iterator(); iter.hasNext(); ) {
	//                    AmpRegion location = (AmpRegion) iter.next();
	//                    if(location.getName().indexOf(themeForm.getKeyword()) == -1) {
	//                        iter.remove();
	//                    }
	//                }
	//            }
	//            Collections.sort(locationsList, new LocationUtil.HelperAmpRegionNameComparator());
	//            themeForm.setLocationsCol(locationsList);
	//        }
	        return mapping.findForward("forward");
	    }
	
	    public SelectLocationForIndicatorValue() {
	    }
	}
