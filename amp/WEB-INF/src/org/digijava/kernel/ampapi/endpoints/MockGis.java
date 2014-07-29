package org.digijava.kernel.ampapi.endpoints;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.node.POJONode;
import org.digijava.kernel.ampapi.endpoints.util.FiltersParams;
import org.digijava.kernel.ampapi.helpers.geojson.FeatureCollectionGeoJSON;
import org.digijava.kernel.ampapi.helpers.geojson.FeatureGeoJSON;
import org.digijava.kernel.ampapi.helpers.geojson.PointGeoJSON;
import org.digijava.kernel.ampapi.postgis.TestPostGis;
import org.digijava.kernel.ampapi.postgis.entity.Amp_Activity_Points;

import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/***
 * 
 * @author
 * 
 */

@Path("mock")
public class MockGis {

	@GET
	@Path("/getactivities")
	@Produces(MediaType.APPLICATION_JSON)
	public final List<Point> getActivties() {
		ArrayList<Point> points = new ArrayList<>();
		Point test = new Point("2201", "2.92023", "33.35336");
		points.add(test);
		return points;
	}
    @GET
    @Path("/testfilterparam")
    @Produces(MediaType.APPLICATION_JSON)
    public final FiltersParams getFiltersParams() {
//        FiltersParams p=new FiltersParams();
//        p.setFilterName("theFilter");
//        p.setFilterValue(new ArrayList<String>());
//        p.getFilterValue().add("hola");
//        p.getFilterValue().add("chau");
//        return p;
        return null;
    }
	
	/***
	 * 
	 * @return
	 * @throws ParseException
	 */

	@GET
	@Path("/getfrompostgis")
	@Produces(MediaType.APPLICATION_JSON)
	public final FeatureCollectionGeoJSON getFromPostGis() throws ParseException {
		TestPostGis gis = new TestPostGis();
		List all = gis.getAll();
		FeatureCollectionGeoJSON result = new FeatureCollectionGeoJSON ();
		WKTReader wKTReader = new WKTReader();
		for (Iterator iterator = all.iterator(); iterator.hasNext();) {
			Amp_Activity_Points point = (Amp_Activity_Points) iterator.next();
			com.vividsolutions.jts.geom.Point finalpoint = (com.vividsolutions.jts.geom.Point) wKTReader.read(point.getLocation().toString());
			
			result.features.add(getPoint(finalpoint.getY(), finalpoint.getX(), point.getAmp_activity_id().toString()));

		}
		
		return result;
	}
	
    public FeatureGeoJSON getPoint(Double lat, Double lon, String activityid) {
        FeatureGeoJSON fgj=new FeatureGeoJSON();
        PointGeoJSON pg = new PointGeoJSON();
        pg.coordinates.add(lat);
        pg.coordinates.add(lon);
        List <Long>ids=new ArrayList<>();
        ids.add(123L);
        ids.add(122L);
        ids.add(121L);
        ids.add(121L);
        POJONode o=new POJONode(ids);
        //pg.properties.put("activityid", new TextNode(activityid));
        pg.properties.put("activityid", o);
        
        
        
        fgj.geometry=pg;
        return fgj;
    }	

	/***
	 * 
	 * 
	 * 
	 */
	@SuppressWarnings("unused")
	private class Point {
		private String activityid;
		private String lat;
		private String lon;

		public Point(String activityid, String lat, String lon) {
			super();
			this.activityid = activityid;
			this.lat = lat.toString();
			this.lon = lon.toString();
		}

		public Point(String activityid, Double lat, Double lon) {
			super();
			this.activityid = activityid;
			this.lat = lat.toString();
			this.lon = lon.toString();
		}

		public String getActivityid() {
			return activityid;
		}

		public void setActivityid(String activityid) {
			this.activityid = activityid;
		}

		public String getLat() {
			return lat;
		}

		public void setLat(String lat) {
			this.lat = lat;
		}

		public String getLon() {
			return lon;
		}

		public void setLon(String lon) {
			this.lon = lon;
		}
	}
	
	
}
