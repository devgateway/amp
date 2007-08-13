package org.digijava.module.autopatcher.dbentity;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

/**
* AmpPatch.java
* TODO description here
* @author mihai
* @package org.digijava.module.autopatcher.dbentity
* @since 12.08.2007
 */
public class AmpPatch implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5759615519655548333L;
	
	private Long id;
	private String keyName;
	private String abstractFileName;
	private Timestamp discovered;
	private Timestamp lastInvoked;
	private String md5;
	
	private Set logs;

	public String getAbstractFileName() {
		return abstractFileName;
	}

	public void setAbstractFileName(String abstractFileName) {
		this.abstractFileName = abstractFileName;
	}

	public Timestamp getDiscovered() {
		return discovered;
	}

	public void setDiscovered(Timestamp discovered) {
		this.discovered = discovered;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public Timestamp getLastInvoked() {
		return lastInvoked;
	}

	public void setLastInvoked(Timestamp lastInvoked) {
		this.lastInvoked = lastInvoked;
	}

	public Set getLogs() {
		return logs;
	}

	public void setLogs(Set logs) {
		this.logs = logs;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String lastMD5) {
		this.md5 = lastMD5;
	}
}
