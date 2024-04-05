    package org.digijava.module.aim.action;

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

    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import java.util.Collection;
    import java.util.Iterator;

    /**
 *
 *Todo refactoring whole code using Dynamic location manager
 * but not today. This is a quick bug fix version.
 */
    public class SelectLocationForIndicatorValue   extends Action {
    
        public static final int LEVEL_INDEX_3 = 3;
        public static final int LEVEL_INDEX_4 = 4;
    
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
            String countryName = null; 
            if(iso!=null){
                themeForm.setDefaultCountryIsSet(true);                 
                Collection<Country> b = FeaturesUtil.getDefaultCountry(iso);
                Iterator<Country> itr2 = b.iterator();
                while (itr2.hasNext()) {
                ampGS = (Country) itr2.next();
                countryName = ampGS.getCountryName();            
              }
    
              if (themeForm.getFill() == null || themeForm.getFill().trim().length() == 0) {
                  themeForm.setAdmLevel0Location(countryName);
                  themeForm.setImpAdmLevel0(iso);
                  themeForm.setAdmLevel1Locations(DynLocationManagerUtil.getLocationsOfTypeAdmLevel1OfDefCountry());
              }
              else {
                if (themeForm.getFill().equals("admLevel2Location")) {
                  if (themeForm.getImpAdmLevel1() != null) {
                      AmpCategoryValueLocations selectedAdmLevel1Locations =
                              DynLocationManagerUtil.getLocation(themeForm.getImpAdmLevel1(), true);
                      if (selectedAdmLevel1Locations != null) {
                          themeForm.setAdmLevel2Locations(selectedAdmLevel1Locations.getChildLocations());
                      } else {
                          themeForm.setAdmLevel2Locations(null);
                      }
                      themeForm.setAdmLevel1Locations(DynLocationManagerUtil.getLocationsOfTypeAdmLevel1OfDefCountry());
                      themeForm.setImpAdmLevel2(null);
                      themeForm.setImpAdmLevel3(null);
                  }
                } else if (themeForm.getFill().equals("admLevel3Location")) {
                    if (themeForm.getImpAdmLevel2() != null) {
                        AmpCategoryValueLocations selectedAdmLevel1Location =
                                DynLocationManagerUtil.getLocation(themeForm.getImpAdmLevel1(), true);
                        AmpCategoryValueLocations selectedAdmLevel2Location =
                                DynLocationManagerUtil.getLocation(themeForm.getImpAdmLevel2(), true);
                        if (selectedAdmLevel2Location != null) {
                            themeForm.setAdmLevel3Locations(selectedAdmLevel2Location.getChildLocations());
                        } else {
                            themeForm.setAdmLevel3Locations(null);
                        }
                        if (selectedAdmLevel1Location != null) {
                            themeForm.setAdmLevel2Locations(selectedAdmLevel1Location.getChildLocations());
                        } else {
                            themeForm.setAdmLevel2Locations(null);
                        }
                        themeForm.setAdmLevel1Locations(
                                DynLocationManagerUtil.getLocationsOfTypeAdmLevel1OfDefCountry());
                        themeForm.setImpAdmLevel3(null);
                    }
                } else if (themeForm.getFill().equals("admLevel3LocationSelected")) {
                    if (themeForm.getImpAdmLevel3() != null) {
                        AmpCategoryValueLocations selectedAdmLevel1Location =
                                DynLocationManagerUtil.getLocation(themeForm.getImpAdmLevel1(), true);
                        AmpCategoryValueLocations selectedAdmLevel2Location =
                                DynLocationManagerUtil.getLocation(themeForm.getImpAdmLevel2(), true);
                        if (selectedAdmLevel2Location != null) {
                            themeForm.setAdmLevel3Locations(selectedAdmLevel2Location.getChildLocations());
                        } else {
                            themeForm.setAdmLevel3Locations(null);
                        }
                        if (selectedAdmLevel1Location != null) {
                            themeForm.setAdmLevel2Locations(selectedAdmLevel1Location.getChildLocations());
                        } else {
                            themeForm.setAdmLevel2Locations(null);
                        }
                        themeForm.setAdmLevel1Locations(
                                DynLocationManagerUtil.getLocationsOfTypeAdmLevel1OfDefCountry());
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

              
                //National
                if (themeForm.getLocationLevelIndex().intValue() == 2) {
                    //Regional
                    id = themeForm.getImpAdmLevel1();
                } else {
                    if (themeForm.getLocationLevelIndex().intValue() == LEVEL_INDEX_3) {
                        //Zone
                        id = themeForm.getImpAdmLevel2();
                    }
                    if (themeForm.getLocationLevelIndex().intValue() == LEVEL_INDEX_4) {
                        //District
                        id = themeForm.getImpAdmLevel3();
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
                themeForm.setImpAdmLevel0(null);
                themeForm.setImpAdmLevel1(null);
                themeForm.setImpAdmLevel2(null);
                themeForm.setImpAdmLevel3(null);
                themeForm.setAction(null);
                return mapping.findForward("backToAddDataPage");
            }
    
            return mapping.findForward("forward");
        }
    
        public SelectLocationForIndicatorValue() {
        }
    }
