package org.digijava.kernel.ampapi.postgis.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import com.vividsolutions.jts.geom.Point;
/**
 * @author Diego
 * Temporal table to store all point linked to an activity this is also is a sample of how to 
 * add a table to the schema using annotations  	
 */


@Entity
public class Amp_Activity_Points {
	/*
	 * @SequenceGenerator Defines a primary key 
	 * @Id Defines a primary key
	 * @GeneratedValue Defines a primary key  
	*/
	@SequenceGenerator(name = "Amp_Activity_Points_seq", sequenceName = "Amp_Activity_Points_seq")
	@Id
	@GeneratedValue(generator = "Amp_Activity_Points_seq")
	private Long id;
	private Long amp_activity_id;
		
	/*
	 	org.hibernate.spatial.GeometryType add support for geometries postGIS type
	 */
	@Type(type = "org.hibernate.spatial.GeometryType")
	private Point location;

	public Point getLocation() {
		return this.location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public Long getAmp_activity_id() {
		return amp_activity_id;
	}

	public void setAmp_activity_id(Long amp_activity_id) {
		this.amp_activity_id = amp_activity_id;
	}
}
