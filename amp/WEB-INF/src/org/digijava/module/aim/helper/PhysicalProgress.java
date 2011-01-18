package org.digijava.module.aim.helper ;

import java.io.Serializable;

public class PhysicalProgress implements Comparable, Serializable
{
	private String title ;
	private String reportingDate ;
	private Long pid;
	private String description;
        private boolean newProgress;
  public PhysicalProgress() {}

	public PhysicalProgress(Long id) {
		pid = id;
	}

	/**
	 * @return
	 */
	public String getReportingDate() {
		return reportingDate;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	public Long getPid() {
		return pid;
	}

        public boolean isNewProgress() {
          return newProgress;
        }

  /**
	 * @param string
	 */
	public void setReportingDate(String string) {
		reportingDate = string;
	}

	public void setDescription(String string) {
		description = string;
	}

	/**
	 * @param string
	 */
	public void setTitle(String string) {
		title = string;
	}

	public void setPid(Long string) {
		pid = string;
	}

        public void setNewProgress(boolean newProgress) {
          this.newProgress = newProgress;
        }

  public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof PhysicalProgress)) throw new ClassCastException();

		PhysicalProgress pp = (PhysicalProgress) obj;

		if (this.pid == null) return false;
		if (pp.pid == null) return false;
		return this.pid.equals(pp.pid);


	}

	public int compareTo(Object obj) {
		if (obj == null) throw new NullPointerException();

		if (!(obj instanceof PhysicalProgress)) {
			throw new ClassCastException();
		}

		PhysicalProgress pp = (PhysicalProgress) obj;
		return (this.pid.compareTo(pp.pid));
	}
}
