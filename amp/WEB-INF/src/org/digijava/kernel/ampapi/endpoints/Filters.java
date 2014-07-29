package org.digijava.kernel.ampapi.endpoints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.ampapi.endpoints.dto.Programs;
import org.digijava.kernel.ampapi.endpoints.dto.Sectors;
import org.digijava.kernel.ampapi.endpoints.util.FiltersParams;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;

/**
 * Class that holds method related to filtres for gis querys (available options,
 * available filters)
 * 
 * @author jdeanquin@developmentgateway.org
 * 
 */
@Path("filters")
public class Filters {
    AmpARFilter filters;

    public Filters() {
        filters = new AmpARFilter();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String ping() {
        return "Hello filters";
    }

    /**
     * Return activity status options
     * 
     * @return
     */
    @GET
    @Path("/activityStatus")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<String> getActivityStatus() {
        return AmpARFilter.activityStatus;
    }

    /**
     * Return the adminlevels for filtering
     * 
     * @return
     */
    @GET
    @Path("/adminLevels")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getAdminLevels() {
        // This should never change should they return from database?
        return new ArrayList<String>(Arrays.asList("Country", "Region", "Zone",
                "District"));
    }

    /**
     * Return the sector filtered by the given sectorName
     * 
     * @return
     */
    @GET
    @Path("/sectors")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sectors> getSectors(
           final FiltersParams filter) {
        // DozerBeanMapperSingletonWrapper.getInstance().
        List<Sectors> ampSectorsList = new ArrayList<Sectors>();

        List<AmpSector> s = SectorUtil
                .getAmpSectorsAndSubSectorsHierarchy("Primary");
        for (AmpSector ampSector : s) {
            ampSectorsList.add(getSectors(ampSector));
        }
        return ampSectorsList;
    }
    /**
     * Return the programs filtered by the given sectorName
     * 
     * @return
     */
    @GET
    @Path("/programs")
    @Produces(MediaType.APPLICATION_JSON)
    public Programs getPrograms(){
        Programs program=new Programs();
        try {
            AmpActivityProgramSettings npd=ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.NATIONAL_PLAN_OBJECTIVE);
            
            return getPrograms(npd.getDefaultHierarchy());
            
        } catch (DgException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    private Programs getPrograms(AmpTheme t){
        Programs p=new Programs();
        p.setId(t.getAmpThemeId());
        p.setDescription(t.getName());
        p.setPrograms(new ArrayList<Programs>());
        
        for(AmpTheme tt:t.getSiblings()){
            p.getPrograms().add(getPrograms(tt));
        }
        return p;
    }
    
    private Sectors getSectors(AmpSector as) {
        Sectors s = new Sectors();
        s.setId(as.getAmpSectorId());
        s.setCode(as.getSectorCodeOfficial());
        s.setName(as.getName());
        s.setSectors(new ArrayList<Sectors>());
        for (AmpSector ampSectorChild : as.getSectors()) {
            s.getSectors().add(getSectors(ampSectorChild));
        }

        return s;
    }
    
}
