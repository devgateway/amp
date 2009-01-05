package org.dgfundation.amp.support.dbentity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AMP_VERSION")
public class AmpVersion {
	@Id
	@Column(name = "ID_VERSION")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long idversion;
	@Column(name = "NAME")
	private String name;

	public long getIdversion() {
		return idversion;
	}

	public void setIdversion(long idversion) {
		this.idversion = idversion;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
