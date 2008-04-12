package org.dgfundation.amp.support.dbentity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AMP_MODULES")
public class AmpModules {
	@Id
	@Column(name = "ID_MODULE")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long idmodule;

	@Column(name = "NAME_ES")
	private String name_es;

	@Column(name = "NAME_EN")
	private String name_en;

	@Column(name = "NAME_FR")
	private String name_fr;

	public long getIdmodule() {
		return idmodule;
	}

	public void setIdmodule(long idmodule) {
		this.idmodule = idmodule;
	}

	public String getName_en() {
		return name_en;
	}

	public void setName_en(String name_en) {
		this.name_en = name_en;
	}

	public String getName_es() {
		return name_es;
	}

	public void setName_es(String name_es) {
		this.name_es = name_es;
	}

	public String getName_fr() {
		return name_fr;
	}

	public void setName_fr(String name_fr) {
		this.name_fr = name_fr;
	}

}
