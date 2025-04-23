package org.digijava.module.aim.util.caching;

import org.digijava.module.aim.dbentity.AmpSector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * session-local cache
 * @author simple
 *
 */
public class SectorsCache
{
    //fields initialized by constructor
    private ArrayList<AmpSector> liveSectors;
    private Map<Long, ArrayList<AmpSector>> liveSectorsByParent;
    
    // fields initialized by users
    public Map<String, List<AmpSector>> sectorsHierarchy;
    
    public SectorsCache(List<AmpSector> liveSectors)
    {
        this.liveSectors = new ArrayList<AmpSector>(liveSectors);
        this.liveSectorsByParent = new HashMap<Long, ArrayList<AmpSector>>();
        for(AmpSector sector:this.liveSectors)
        {
            Long parentId = sector.getParentSectorId() == null ? 0 : sector.getParentSectorId().getAmpSectorId();
            if (parentId == null)
                parentId = 0L;
            if (!liveSectorsByParent.containsKey(parentId))
                liveSectorsByParent.put(parentId, new ArrayList<AmpSector>());
            liveSectorsByParent.get(parentId).add(sector);
        }
        
        this.sectorsHierarchy = new HashMap<String, List<AmpSector>>();
    }
    
    public List<AmpSector> getChildSectors(long parentSectorId)
    {
        List<AmpSector> result = new ArrayList<AmpSector>();
        if (liveSectorsByParent.containsKey(parentSectorId))
            result.addAll(liveSectorsByParent.get(parentSectorId));
        return result;
    }
    
    public List<AmpSector> getAllSectors()
    {
        return new ArrayList<AmpSector>(this.liveSectors);
    }
}
