package org.digijava.kernel.ampapi.helpers.geojson.validation;

import org.digijava.kernel.ampapi.helpers.geojson.PositionValidator;

import java.util.List;

public class LonLatValidator implements PositionValidator
{
    public boolean isValid(List<Double> position)
    {
        if (position==null) return false;
        if (position.size()!=2) return false;
        double lon = position.get(0);
        if ( lon>180 || lon<-180 ) return false;
        double lat = position.get(1);
        if ( lat>90 || lat<-90 ) return false;
        return true;
    }

    public boolean isValidBB(List<Double> bbox)
    {
        // bbox can be null
        if ( bbox==null ) return true;

        if (bbox.size()!=4) return false;
        
        double minLon = bbox.get(0);
        if ( minLon>180 || minLon<-180 ) return false;
        double minLat = bbox.get(1);
        if ( minLat>90 || minLat<-90 ) return false;
        double maxLon = bbox.get(2);
        if ( maxLon>180 || maxLon<-180 ) return false;
        double maxLat = bbox.get(3);
        if ( maxLat>90 || maxLat<-90 ) return false;
        
        if (minLon>maxLon) return false;
        if (minLat>maxLat) return false;
        
        return true;
    }

    public boolean isEquivalent(List<Double> p1, List<Double> p2)
    {
        for ( int i = 0 ; i<p1.size() ; i++ )
        {
            if ( (double)p1.get(i) != (double)p2.get(i) ) return false;
        }
        return true;
    }
}
