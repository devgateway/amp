/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.module.dataExchange.dbentity;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.digijava.kernel.user.User;

/**
 * 
 * @author dan
 *
 */

public class DEMappingFields
    implements Serializable {

    private static Logger logger = Logger.getLogger(DEMappingFields.class);

    private Long id;
    private String importedFieldValue;
    private String importedFieldCode;
    private Long ampFieldId;
    private String fieldType;
    private String status;
    
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getImportedFieldValue() {
		return importedFieldValue;
	}
	public void setImportedFieldValue(String importedFieldValue) {
		this.importedFieldValue = importedFieldValue;
	}
	public String getImportedFieldCode() {
		return importedFieldCode;
	}
	public void setImportedFieldCode(String importedFieldCode) {
		this.importedFieldCode = importedFieldCode;
	}
	public Long getAmpFieldId() {
		return ampFieldId;
	}
	public void setAmpFieldId(Long ampFieldId) {
		this.ampFieldId = ampFieldId;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
    
}