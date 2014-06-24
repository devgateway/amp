package org.digijava.kernel.ampapi.endpoints;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
			this.lat = lat;
			this.lon = lon;
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
