package org.digijava.kernel.ampapi.endpoints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.ampapi.endpoints.dto.Programs;
import org.digijava.kernel.ampapi.endpoints.dto.Sectors;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
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
    @Path("/boundaries")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getBoundaries() {
        // This should never change should they return from database?
        return new ArrayList<String>(Arrays.asList("Country", "Region", "Zone",
                "District"));
    }

    
    /**
     * Returns the sector schema lists
     * 
     * @return
     */
    @GET
    @Path("/sectors/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FiltersHelper> getSectorsSchemas(
    		) {
    	List<FiltersHelper>schemalist=new ArrayList<FiltersHelper>();
    	try {
    		List<AmpClassificationConfiguration>schems=SectorUtil.getAllClassificationConfigs();
    		for (AmpClassificationConfiguration ampClassificationConfiguration : schems) {
    			schemalist.add(new FiltersHelper(ampClassificationConfiguration.getId().toString(),ampClassificationConfiguration.getName()));
			}
		} catch (DgException e) {
			// TODO till we find out the exception strategy
			e.printStackTrace();
		}
        return schemalist;
    }
    /**
     * Return the sector filtered by the given sectorName
     * 
     * @return
     */
    @GET
    @Path("/sectors/{sectorConfigName}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sectors> getSectors(
    		@PathParam("sectorConfigName") String sectorConfigName) {
        // DozerBeanMapperSingletonWrapper.getInstance().
        List<Sectors> ampSectorsList = new ArrayList<Sectors>();
        
        //Primary
        List<AmpSector> s = SectorUtil
                .getAmpSectorsAndSubSectorsHierarchy(sectorConfigName);
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
    /**
     * Get JsonEnable object for programs
     * @param t AmpThem to get the programFrom
     * @return
     */
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
    /**
     * Get Sectors from AmpSector
     * @param as
     * @return
     */
    
    private Sectors getSectors(AmpSector as) {
        Sectors s = new Sectors();
        s.setId(as.getAmpSectorId());
        s.setCode(as.getSectorCodeOfficial());
        s.setName(as.getName());
        s.setChildren(new ArrayList<Sectors>());
        for (AmpSector ampSectorChild : as.getSectors()) {
            s.getChildren().add(getSectors(ampSectorChild));
        }

        return s;
    }
    public class FiltersHelper{
    	private String id;
    	private String name;
    	public FiltersHelper(){
    		
    	}
    	public FiltersHelper(String id,String name){
        	this.id=id;
        	this.name=name;    		
    	}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
    	
    	
    	
    }
    
    
}
