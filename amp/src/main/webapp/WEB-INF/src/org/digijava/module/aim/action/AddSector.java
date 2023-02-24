
package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.dimension.SectorDimension;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.form.AddSectorForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.caching.AmpCaching;


public class AddSector extends Action {

    private static Logger logger = Logger.getLogger(GetSectors.class);
    
    public static final String DEFAULT_VALUE_SECTOR = "101";
    public static final String DEFAULT_VALUE_SUB_SECTOR = "1001";
    public static final String DEFAULT_VALUE_SUB_SUB_SECTOR = "10001";

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
            String str = (String) session.getAttribute("ampAdmin");
            if (str.equals("no")) {
                return mapping.findForward("index");
            }
        }

        AddSectorForm addSectorForm = (AddSectorForm) form;

        String event = request.getParameter("event");
        String parent = request.getParameter("parent");
        String schemeId = request.getParameter("ampSecSchemeId");
        logger.debug(request.getParameter("ampSecSchemeId"));
        if(schemeId==null || schemeId.equals("") || schemeId.length()<=0){
            schemeId = (String)session.getAttribute("Id");          
        }
        else{
            session.setAttribute("Id",schemeId);
            //logger.debug("setting the session::::::::::::::::::::");
        }
        if(parent!=null)
        {
             addSectorForm.setLevelType(parent);
        }
//      logger.debug(addSectorForm.getLevelType());
//      logger.debug("Add===================================================Sector:;:::::::"+session.getAttribute("Id"));
//      logger.debug("outside event if    value of schemeId============="+schemeId);
        
        if(event!=null)
        {
            if(event.equals("addSector"))
            {
                if(addSectorForm.getLevelType().equals("scheme"))
                {
                    AmpSectorScheme ampSch = SectorUtil.getAmpSectorScheme(new Long(schemeId));
                    String parentName = ampSch.getSecSchemeName();
                    String parentCode = ampSch.getSecSchemeCode();
                    logger.debug("level 1inside event if    value of schemeId============="+schemeId);
                    if(!checkSectorNameAndCode(addSectorForm))
                    {
                        Collection schemeGot = SectorUtil.getEditScheme(new Integer((String)session.getAttribute("Id")));
                        addSectorForm.setFormFirstLevelSectors(SectorUtil.getSectorLevel1(new Integer((String)session.getAttribute("Id"))));
                        Iterator itr = schemeGot.iterator();
                        while (itr.hasNext()) {
                            AmpSectorScheme ampScheme = (AmpSectorScheme) itr.next();
                            addSectorForm.setSecSchemeId(ampScheme.getAmpSecSchemeId());
                            addSectorForm.setSecSchemeName(ampScheme.getSecSchemeName());
                            addSectorForm.setSecSchemeCode(ampScheme.getSecSchemeCode());
                            addSectorForm.setParentId(ampScheme.getAmpSecSchemeId());
                        }
                        return mapping.findForward("levelFirstSectorAdded");
                    }
                    int sectorStatus = existSectorNameOrCode(addSectorForm.getSectorName(), addSectorForm.getSectorCodeOfficial(), SectorUtil.getSectorLevel1(new Integer((String)session.getAttribute("Id"))));
                    if(sectorStatus == 1 || parentName.equalsIgnoreCase(addSectorForm.getSectorName())) {
                        //error the sector name exists
                        Collection schemeGot = SectorUtil.getEditScheme(new Integer((String)session.getAttribute("Id")));
                        addSectorForm.setFormFirstLevelSectors(SectorUtil.getSectorLevel1(new Integer((String)session.getAttribute("Id"))));
                        Iterator itr = schemeGot.iterator();
                        while (itr.hasNext()) {
                            AmpSectorScheme ampScheme = (AmpSectorScheme) itr.next();
                            addSectorForm.setSecSchemeId(ampScheme.getAmpSecSchemeId());
                            addSectorForm.setSecSchemeName(ampScheme.getSecSchemeName());
                            addSectorForm.setSecSchemeCode(ampScheme.getSecSchemeCode());
                            addSectorForm.setParentId(ampScheme.getAmpSecSchemeId());
                        }
                        ActionMessages errors = new ActionMessages();
                        errors.add("title", new ActionMessage("error.aim.addSector.wrongTitle"));
                        if (errors.size() > 0){ 
                            saveErrors(request, errors);
                            return mapping.findForward("forwardSector");
                        }
                        return mapping.findForward("levelFirstSectorAdded");
                    }
                    if(sectorStatus == 2 || parentCode.equalsIgnoreCase(addSectorForm.getSectorCodeOfficial())){
                        //error the sector code exists
                        Collection schemeGot = SectorUtil.getEditScheme(new Integer((String)session.getAttribute("Id")));
                        addSectorForm.setFormFirstLevelSectors(SectorUtil.getSectorLevel1(new Integer((String)session.getAttribute("Id"))));
                        Iterator itr = schemeGot.iterator();
                        while (itr.hasNext()) {
                            AmpSectorScheme ampScheme = (AmpSectorScheme) itr.next();
                            addSectorForm.setSecSchemeId(ampScheme.getAmpSecSchemeId());
                            addSectorForm.setSecSchemeName(ampScheme.getSecSchemeName());
                            addSectorForm.setSecSchemeCode(ampScheme.getSecSchemeCode());
                            addSectorForm.setParentId(ampScheme.getAmpSecSchemeId());
                        }
                        ActionMessages errors = new ActionMessages();
                        errors.add("title", new ActionMessage("error.aim.addSector.wrongCode"));
                        if (errors.size() > 0){
                            saveErrors(request, errors);
                            return mapping.findForward("forwardSector");
                        }
                        return mapping.findForward("levelFirstSectorAdded");
                    }
                    addSectorForm.setParentId(new Long(schemeId));
                    AmpSector newSector = new AmpSector();
                    newSector.setAmpSectorId(null);
                    newSector.setParentSectorId(null);
                    newSector.setAmpOrgId(null);
                    newSector.setAmpSecSchemeId(SectorUtil.getAmpSectorScheme(addSectorForm.getParentId()));
                    newSector.setSectorCode(AddSector.DEFAULT_VALUE_SECTOR);
                    newSector.setSectorCodeOfficial(addSectorForm.getSectorCodeOfficial());
                    newSector.setName(addSectorForm.getSectorName());
                    newSector.setDescription(addSectorForm.getDescription());
                    newSector.setType(null);
                    newSector.setAmpSectorId(null);
                    if (addSectorForm.getDescription() == null || addSectorForm.getDescription().trim().equals("")) {
                        newSector.setDescription(new String(" "));
                    } else {
                        newSector.setDescription(addSectorForm.getDescription());
                    }
                    newSector.setLanguage(null);
                    newSector.setVersion(null);
                    ARUtil.clearDimension(SectorDimension.class);
                    DbUtil.add(newSector);
                    Collection schemeGot = SectorUtil.getEditScheme(new Integer((String)session.getAttribute("Id")));
                    addSectorForm.setFormFirstLevelSectors(SectorUtil.getSectorLevel1(new Integer((String)session.getAttribute("Id"))));
                        Iterator itr = schemeGot.iterator();
                        while (itr.hasNext()) {
                            AmpSectorScheme ampScheme = (AmpSectorScheme) itr.next();
                            addSectorForm.setSecSchemeId(ampScheme.getAmpSecSchemeId());
                            addSectorForm.setSecSchemeName(ampScheme.getSecSchemeName());
                            addSectorForm.setSecSchemeCode(ampScheme.getSecSchemeCode());
                            addSectorForm.setParentId(ampScheme.getAmpSecSchemeId());
                        }
                    
                    logger.debug("level one sector added");
                    session.setAttribute("Id",null);
                    return mapping.findForward("levelFirstSectorAdded");
                }
                if(addSectorForm.getLevelType().equals("sector"))
                {
                    logger.debug("level 2 inside event if    value of schemeId============="+schemeId);
                    Long id = new Long(schemeId);
                    Long parentId = id;//new Long(id);

                    AmpSector ampSec = SectorUtil.getAmpSector(id);
                    String parentName = ampSec.getName();
                    String parentCode = ampSec.getSectorCodeOfficial();
                    
                    if(!checkSectorNameAndCode(addSectorForm))
                    {
                        addSectorForm.setSubSectors(SectorUtil.getAllChildSectors(parentId));
                        Collection _subSectors = addSectorForm.getSubSectors();
                        Iterator itr = _subSectors.iterator();
                        while (itr.hasNext()) {
                            AmpSector ampScheme = (AmpSector) itr.next();
                            addSectorForm.setAmpSectorId(ampScheme.getAmpSectorId());
                            addSectorForm.setParentId(ampScheme.getAmpSectorId());
                            addSectorForm.setParentSectorId(ampScheme.getParentSectorId().getAmpSectorId());
                        }
                        return mapping.findForward("levelSecondSectorAdded");
                    }
                    
                    int sectorStatus = existSectorNameOrCode(addSectorForm.getSectorName(), addSectorForm.getSectorCodeOfficial(),SectorUtil.getAllChildSectors(id));
                    if(sectorStatus == 1 || parentName.equalsIgnoreCase(addSectorForm.getSectorName())) {
                        //error the sub-sector name exists
                        addSectorForm.setSubSectors(SectorUtil.getAllChildSectors(parentId));
                        Collection _subSectors = addSectorForm.getSubSectors();
                        Iterator itr = _subSectors.iterator();
                        while (itr.hasNext()) {
                            AmpSector ampScheme = (AmpSector) itr.next();
                            addSectorForm.setAmpSectorId(ampScheme.getAmpSectorId());
                            addSectorForm.setParentId(ampScheme.getAmpSectorId());
                            addSectorForm.setParentSectorId(ampScheme.getParentSectorId().getAmpSectorId());
                        }
                        ActionMessages errors = new ActionMessages();
                        errors.add("title", new ActionMessage("error.aim.addSubSector.wrongTitle"));
                        if (errors.size() > 0){
                            saveErrors(request, errors);
                            return mapping.findForward("forwardSector");
                        }
                        
                        return mapping.findForward("levelSecondSectorAdded");
                    }
                    
                    if(sectorStatus == 2 || parentCode.equalsIgnoreCase(addSectorForm.getSectorCodeOfficial())) {
                        //error the sub-sector code exists
                        addSectorForm.setSubSectors(SectorUtil.getAllChildSectors(parentId));
                        Collection _subSectors = addSectorForm.getSubSectors();
                        Iterator itr = _subSectors.iterator();
                        while (itr.hasNext()) {
                            AmpSector ampScheme = (AmpSector) itr.next();
                            addSectorForm.setAmpSectorId(ampScheme.getAmpSectorId());
                            addSectorForm.setParentId(ampScheme.getAmpSectorId());
                            addSectorForm.setParentSectorId(ampScheme.getParentSectorId().getAmpSectorId());
                        }
                        ActionMessages errors = new ActionMessages();
                        errors.add("title", new ActionMessage("error.aim.addSubSector.wrongCode"));
                        if (errors.size() > 0) {
                            saveErrors(request, errors);
                            return mapping.findForward("forwardSector");
                        }
                        
                        return mapping.findForward("levelSecondSectorAdded");
                    }
                    
                    addSectorForm.setParentId(new Long((String)session.getAttribute("Id")));
                    AmpSectorScheme user = SectorUtil.getParentSchemeId(id);
                    AmpSector newSector = new AmpSector();
                    newSector.setAmpSectorId(null);
                    newSector.setParentSectorId(SectorUtil.getAmpSector(id));
                    newSector.setAmpOrgId(null);
                    newSector.setAmpSecSchemeId(user);
                    newSector.setSectorCode(AddSector.DEFAULT_VALUE_SUB_SECTOR);
                    newSector.setSectorCodeOfficial(addSectorForm.getSectorCodeOfficial());
                    newSector.setName(addSectorForm.getSectorName());
                    newSector.setDescription(addSectorForm.getDescription());
                    newSector.setType(null);
                    if (addSectorForm.getDescription() == null
                            || addSectorForm.getDescription().trim().equals("")) {

                        newSector.setDescription(new String(" "));
                    } else {
                        newSector.setDescription(addSectorForm.getDescription());
                    }
                    newSector.setLanguage(null);
                    newSector.setVersion(null);
                    ARUtil.clearDimension(SectorDimension.class);
                    DbUtil.add(newSector);
                    //jdeanquin was here
                    //after the second level sector is added we clean the sector cache in order to the new sector gets displayed
                    //perhaps this should be done in sector util but DbUtil is called directly from here
                    AmpCaching.getInstance().sectorsCache=null;
                    
                    //Long parentId = id;//new Long(id);
                    addSectorForm.setSubSectors(SectorUtil.getAllChildSectors(parentId));
                    Collection _subSectors = addSectorForm.getSubSectors();
                    Iterator itr = _subSectors.iterator();
                    while (itr.hasNext()) {
                        AmpSector ampScheme = (AmpSector) itr.next();
                        addSectorForm.setAmpSectorId(ampScheme.getAmpSectorId());
                        addSectorForm.setParentId(ampScheme.getAmpSectorId());
                        addSectorForm.setParentSectorId(ampScheme.getParentSectorId().getAmpSectorId());
                    }
                    
                    AmpSector editSector= new AmpSector();
                    editSector = SectorUtil.getAmpSector(parentId);
                    addSectorForm.setSectorCode(editSector.getSectorCode());
                    addSectorForm.setSectorCodeOfficial(editSector.getSectorCodeOfficial());
                    addSectorForm.setSectorName(editSector.getName());
                    addSectorForm.setSectorId(editSector.getAmpSectorId());
                    addSectorForm.setDescription(editSector.getDescription());
                    logger.debug("level 2  sector added");
                    session.setAttribute("Id",null);
                    return mapping.findForward("levelSecondSectorAdded");
                }
                if(addSectorForm.getLevelType().equals("sector3"))
                {
                    logger.debug("level 3 inside event if    value of schemeId============="+schemeId);
                    Long id = new Long(schemeId);
                    Long parentId = id;//new Long(id);
                    addSectorForm.setParentId(new Long((String)session.getAttribute("Id")));
                    if(!checkSectorNameAndCode(addSectorForm))
                    {
                        addSectorForm.setSubSectors(SectorUtil.getAllChildSectors(parentId));
                        Collection _subSectors = addSectorForm.getSubSectors();
                        Iterator itr = _subSectors.iterator();
                        while (itr.hasNext()) {
                            AmpSector ampScheme = (AmpSector) itr.next();
                            addSectorForm.setAmpSectorId(ampScheme.getAmpSectorId());
                            addSectorForm.setParentId(ampScheme.getAmpSectorId());
                            addSectorForm.setParentSectorId(ampScheme.getParentSectorId().getAmpSectorId());
                        }
                        return mapping.findForward("levelThirdSectorAdded");
                    }
                    
                    int sectorStatus = existSectorNameOrCode(addSectorForm.getSectorName(), addSectorForm.getSectorCodeOfficial(),SectorUtil.getAllChildSectors(id));
                    if(sectorStatus == 1) {
                        //error the sub-sector name exists
                        addSectorForm.setSubSectors(SectorUtil.getAllChildSectors(parentId));
                        Collection _subSectors = addSectorForm.getSubSectors();
                        Iterator itr = _subSectors.iterator();
                        while (itr.hasNext()) {
                            AmpSector ampScheme = (AmpSector) itr.next();
                            addSectorForm.setAmpSectorId(ampScheme.getAmpSectorId());
                            addSectorForm.setParentId(ampScheme.getAmpSectorId());
                            addSectorForm.setParentSectorId(ampScheme.getParentSectorId().getAmpSectorId());
                        }
                        ActionMessages errors = new ActionMessages();
                        errors.add("title", new ActionMessage("error.aim.addSubSubSector.wrongTitle"));
                        if (errors.size() > 0){
                            saveErrors(request, errors);
                            return mapping.findForward("forwardSector");
                        }
                        
                        return mapping.findForward("levelThirdSectorAdded");
                    }
                    
                    if(sectorStatus == 2) {
                        //error the sub-sector code exists
                        addSectorForm.setSubSectors(SectorUtil.getAllChildSectors(parentId));
                        Collection _subSectors = addSectorForm.getSubSectors();
                        Iterator itr = _subSectors.iterator();
                        while (itr.hasNext()) {
                            AmpSector ampScheme = (AmpSector) itr.next();
                            addSectorForm.setAmpSectorId(ampScheme.getAmpSectorId());
                            addSectorForm.setParentId(ampScheme.getAmpSectorId());
                            addSectorForm.setParentSectorId(ampScheme.getParentSectorId().getAmpSectorId());
                        }
                        ActionMessages errors = new ActionMessages();
                        errors.add("title", new ActionMessage("error.aim.addSubSubSector.wrongCode"));
                        if (errors.size() > 0) {
                            saveErrors(request, errors);
                            return mapping.findForward("forwardSector");
                        }
                        
                        return mapping.findForward("levelThirdSectorAdded");
                    }
                    
                    AmpSectorScheme user = SectorUtil.getParentSchemeId(id);
                    AmpSector newSector = new AmpSector();
                    newSector.setAmpSectorId(null);
                    newSector.setParentSectorId(SectorUtil.getAmpSector(id));
                    newSector.setAmpOrgId(null);
                    newSector.setAmpSecSchemeId(user);
                    newSector.setSectorCode(AddSector.DEFAULT_VALUE_SUB_SUB_SECTOR);
                    newSector.setSectorCodeOfficial(addSectorForm.getSectorCodeOfficial());
                    newSector.setName(addSectorForm.getSectorName());
                    newSector.setDescription(addSectorForm.getDescription());
                    newSector.setType(null);
                    if (addSectorForm.getDescription() == null
                            || addSectorForm.getDescription().trim().equals("")) {

                        newSector.setDescription(new String(" "));
                    } else {
                        newSector.setDescription(addSectorForm.getDescription());
                    }
                    newSector.setLanguage(null);
                    newSector.setVersion(null);
                    
                    ARUtil.clearDimension(SectorDimension.class);
                    DbUtil.add(newSector);
                    //jdeanquin was here
                    //in case of a level3 sector we do clean the cache
                    AmpCaching.getInstance().sectorsCache=null;
                    //Long parentId = id;//new Long(id);
                    addSectorForm.setSubSectors(SectorUtil.getAllChildSectors(parentId));
                    Collection _subSectors = addSectorForm.getSubSectors();
                    Iterator itr = _subSectors.iterator();
                    while (itr.hasNext()) {
                        AmpSector ampScheme = (AmpSector) itr.next();
                        addSectorForm.setAmpSectorId(ampScheme.getAmpSectorId());
                        addSectorForm.setParentId(ampScheme.getAmpSectorId());
                        addSectorForm.setParentSectorId(ampScheme.getParentSectorId().getAmpSectorId());
                    }
                    
                    AmpSector editSector= new AmpSector();
                    editSector = SectorUtil.getAmpSector(parentId);
                    addSectorForm.setSectorCode(editSector.getSectorCode());
                    addSectorForm.setSectorCodeOfficial(editSector.getSectorCodeOfficial());
                    addSectorForm.setSectorName(editSector.getName());
                    addSectorForm.setSectorId(editSector.getAmpSectorId());
                    addSectorForm.setDescription(editSector.getDescription());
                    
                    
                    
                    logger.debug("level Third Sector added");
                    session.setAttribute("Id",null);
                    return mapping.findForward("levelThirdSectorAdded");
                }
            }
        }
            
        
        
        if(parent.equalsIgnoreCase("scheme"))       
        return mapping.findForward("forwardScheme");
        else
            if(parent.equalsIgnoreCase("sector")|| parent.equalsIgnoreCase("sector3"))
                return mapping.findForward("forwardSector");
            else return null;
    }

    private boolean checkSectorNameAndCode(AddSectorForm addSectorForm){
        if(addSectorForm.getSectorCodeOfficial() == null || "".equals(addSectorForm.getSectorCodeOfficial()) ||
                addSectorForm.getSectorName() == null || "".equals(addSectorForm.getSectorName()))
            return false;
        return true;
    }
    
    private boolean checkSectorName(AddSectorForm addSectorForm){
        if( addSectorForm.getSectorName() == null || "".equals(addSectorForm.getSectorName()))
            return false;
        return true;
    }
    
    private int existSectorNameOrCode(String title, String code, Collection<AmpSector> sectors){
        
        if(title == null) title = "";
        if(code == null)  code  = "";
        for (Iterator it = sectors.iterator(); it.hasNext();) {
            AmpSector sector = (AmpSector) it.next();
            if(title.equals(sector.getName())) return 1;  //the sector title exist
            if(code.equals(sector.getSectorCodeOfficial())) return 2; //the sector code exist
        }
        
        return 0; //the sector do not exist
    }
    
}
