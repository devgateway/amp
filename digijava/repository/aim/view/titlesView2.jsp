<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<digi:instance property="aimAdvancedReportForm" />

					<logic:iterate name="aimAdvancedReportForm"  property="titles" id="titles" type="org.digijava.module.aim.helper.Column">
					<logic:notEqual name="titles" property="columnName" value="Type Of Assistance">
						<td align="center" height="21" width="42" >
						<div align="center">
						<strong>
						<bean:write name="titles" property="columnName" />
						</strong>
						</div>
						</td>
					</logic:notEqual>					
					</logic:iterate>
						
						<logic:equal name="aimAdvancedReportForm" property="reportType" value="donor">
						<logic:equal name="typeAssist" value="true">
						<td height="21" width="89" align="center">
						<strong>Type of Assistance</strong>
						</td>
						</logic:equal>
						</logic:equal>
								