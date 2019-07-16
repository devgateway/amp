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
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.form.ThemeForm;
import org.digijava.module.aim.helper.AmpPrgIndicatorValue;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;

/**
 *
 *Todo refactoring whole code using Dynamic location manager
 * but not today. This is a quick bug fix version.
 */
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
                  themeForm.setRegions(DynLocationManagerUtil.getLocationsOfTypeRegionOfDefCountry());
              }
              else {
                if (themeForm.getFill().equals("zone")) {
                  if (themeForm.getImpRegion() != null) {
                      AmpCategoryValueLocations selectedRegion=DynLocationManagerUtil.getLocation(themeForm.getImpRegion(), true);
                      if (selectedRegion != null) {
                          themeForm.setZones(selectedRegion.getChildLocations());
                      } else {
                          themeForm.setZones(null);
                      }
                      themeForm.setRegions(DynLocationManagerUtil.getLocationsOfTypeRegionOfDefCountry());
                      themeForm.setImpZone(null);
    //                themeForm.setImpMultiZone(null);
    //                themeForm.setImpMultiWoreda(null);
                      themeForm.setImpWoreda(null); 
                  }
                }
                else if (themeForm.getFill().equals("woreda")) {
                  if (themeForm.getImpZone() != null) {
                      AmpCategoryValueLocations selectedRegion=DynLocationManagerUtil.getLocation(themeForm.getImpRegion(), true);
                     AmpCategoryValueLocations selectedZone=DynLocationManagerUtil.getLocation(themeForm.getImpZone(), true);
                      if (selectedZone != null) {
                          themeForm.setWoredas(selectedZone.getChildLocations());
                      } else {
                          themeForm.setWoredas(null);
                      }
                      if (selectedRegion != null) {
                          themeForm.setZones(selectedRegion.getChildLocations());
                      } else {
                          themeForm.setZones(null);
                      }
                      themeForm.setRegions(DynLocationManagerUtil.getLocationsOfTypeRegionOfDefCountry());
                      themeForm.setImpWoreda(null);
                  }
                }
                else if (themeForm.getFill().equals("woredaSelected")) {
                  if (themeForm.getImpWoreda() != null) {
                     AmpCategoryValueLocations selectedRegion=DynLocationManagerUtil.getLocation(themeForm.getImpRegion(), true);
                     AmpCategoryValueLocations selectedZone=DynLocationManagerUtil.getLocation(themeForm.getImpZone(), true);
                       if (selectedZone != null) {
                          themeForm.setWoredas(selectedZone.getChildLocations());
                      } else {
                          themeForm.setWoredas(null);
                      }
                      if (selectedRegion != null) {
                          themeForm.setZones(selectedRegion.getChildLocations());
                      } else {
                          themeForm.setZones(null);
                      }
                      themeForm.setRegions(DynLocationManagerUtil.getLocationsOfTypeRegionOfDefCountry());
                      //themeForm.setImpWoreda(null);
                  }
                }
              }
            }
            else{
                themeForm.setDefaultCountryIsSet(false);
          }        
    
            
    
            if(themeForm.getAction() != null && themeForm.getAction().equalsIgnoreCase("add")) {
                
                AmpPrgIndicatorValue indValue = (AmpPrgIndicatorValue) themeForm.getPrgIndValues().get(themeForm.getParentIndex().intValue());
                
                
                Long id=null;

              
//              if(indValue.getLocation()!=null){
//                  location=indValue.getLocation();
//              }else {
//                  location=new AmpLocation();
//              }
                //National
                if(themeForm.getLocationLevelIndex().intValue()==2){
                    //Regional
                    id=themeForm.getImpRegion();
                }
                else{
                      if (themeForm.getLocationLevelIndex().intValue()==3){
                          //Zone
                          id=themeForm.getImpZone();

                      }
                      if (themeForm.getLocationLevelIndex().intValue()==4){
                          //District
                          id=themeForm.getImpWoreda();

                      }

                }
                
                AmpLocation ampLoc = DynLocationManagerUtil.getOrCreateAmpLocationByCVLId(id);
                
                indValue.setLocation(ampLoc);
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
    
            return mapping.findForward("forward");
        }
    
        public SelectLocationForIndicatorValue() {
        }
    }
