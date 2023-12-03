<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="Javascript">
	
	function load() {	
		document.getElementById("name").focus();
	}

	function unload() {
		//window.opener.document.getElementById("currUrl").value="";
	}
	
</script>


<digi:instance property="aimViewOrgForm" />
<digi:context name="digiContext" property="context"/>

<table bgColor=#ffffff cellpadding="0" cellspacing="0" class="box-border-nopadding" width="600">
	<tr bgColor=#f4f4f2>
		<td valign="top">
			<table align=left bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="562" border="0">	
				<tr>
					<td bgColor=#ffffff class=box-border width="560">
						<table border="0" cellpadding="1" cellspacing="1" class="box-border" width="100%">
								<tr bgColor=#dddddb>
									<td bgColor=#dddddb height="20" align="center" colspan="5">
										<b><digi:trn key="aim:organizationDetail">Organization Details</digi:trn></b>	
									</td>
								</tr>
								<!-- Page Logic -->
								<tr>
									<td width="100%">	
										<table width="590" border="0"	 bgColor=#f4f4f2 height="363">
											<logic:iterate name="aimViewOrgForm" property="org"  id="org" type="org.digijava.ampModule.aim.dbentity.AmpOrganisation">
																	<tr>
																		<td width="169" align="right" height="30">
																			<digi:trn key="aim:organizationName">Organization Name</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2" >
																	    	<input type="text" id="name" value="<c:out value='${org.name}' />" size="54" readonly="true" >
																	    </td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																			<digi:trn key="aim:orgAcronym">Organization Acronym</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2" >
																	    	<input type="text" value="<c:out value='${org.acronym}' />" size="54" readonly="true" >
																	    </td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																	        <digi:trn key="aim:organizationType">Organization Type</digi:trn>
																		</td>
																		<td width="380" height="30" colspan="2">
																			<input type="text" value="<c:out value='${org.orgTypeId.orgType}' />" size="35" readonly="true">
																        </td>
																	</tr>
																	<logic:notEmpty name="org" property="regionId">
																	<tr>
																		<td width="169" align="right" height="30">
																			<digi:trn key="aim:orgRegion">Region</digi:trn>
																		</td>
																	    <td width="380" height="30">
																	    	<input type="text" value="<c:out value='${org.regionId.name}' />" size="35" readonly="true">
																		</td>
																	</tr>
																	</logic:notEmpty>
																	<tr>
																		<td width="169" align="right" height="30">
                                                                    		<digi:trn key="aim:organizationGroup">Organization Group</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
																	    	<input type="text" value="<c:out value='${org.orgGrpId.orgGrpName}' />" size="35" readonly="true">
																	    </td>
                                                          			</tr>
                                                          			<tr>
																		<td width="169" align="right" height="30">
																	        <digi:trn key="aim:organizationDac">DAC Code</digi:trn>
																		</td>
																		<td width="380" height="30" colspan="2">
																			<input type="text" value="<c:out value='${org.dacOrgCode}' />" size="15" readonly="true" >
																		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																	        <digi:trn key="aim:organizationIsoCode">ISO Code</digi:trn>
																		</td>
																		<td width="380" height="30" colspan="2">
																	          <input type="text" value="<c:out value='${org.orgIsoCode}' />" size="15" readonly="true" >
																		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
                                                                   			<digi:trn key="aim:organizationCode">Organization Code</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                                    		<input type="text" value="<c:out value='${org.orgCode}' />" size="15" readonly="true" >
                                                               			</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
                                                                 			<digi:trn key="aim:fiscalCalendar">Fiscal Calendar</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
																	    	<input type="text" value="<c:out value='${org.ampFiscalCalId.name}' />" size="25" readonly="true">
																		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
                                                                     		<digi:trn key="aim:sectorScheme">Sector Scheme</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
																	    	<input type="text" value="<c:out value='${org.ampSecSchemeId.secSchemeName}' />" size="25" readonly="true">
																		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																			<digi:trn key="aim:orgContactName">Contact Person Name</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
																	    	<input type="text" value="<c:out value='${org.contactPersonName}' />" size="35" readonly="true" >
																	    </td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																			<digi:trn key="aim:orgContactTitle">Contact Person Title</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                              				<input type="text" value="<c:out value='${org.contactPersonTitle}' />" size="20" readonly="true" >
																		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																			<digi:trn key="aim:orgContactPhone">Contact Phone</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
																	    	<input type="text" value="<c:out value='${org.phone}' />" size="35" readonly="true" >
                                                                 		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																			<digi:trn key="aim:orgContactFax">Contact Fax</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                                 			<input type="text" value="<c:out value='${org.fax}' />" size="35" readonly="true" >
																		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																			<digi:trn key="aim:orgContactEmail">Contact Email</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                                 			<input type="text" value="<c:out value='${org.email}' />" size="35" readonly="true" >
																		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																			<digi:trn key="aim:organizationUrl">Organization URL</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                                 			<input type="text" value="<c:out value='${org.orgUrl}' />" size="54" readonly="true" >
																		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																			<digi:trn key="aim:orgAddress">Address</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
																	    	<html:textarea name="org" property="address" cols="40" rows="2" readonly="true" />
																	    </td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																	        <digi:trn key="aim:organizationDescription">Description</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
																        	<html:textarea name="org" property="description" cols="40" rows="3" readonly="true" />
																		</td>
																	</tr>
											</logic:iterate>
																	<tr>
																		<td colspan="3" width="555"  align="center" height="30">
																			<table width="100%" cellspacing="5">
																				<tr>
																					<td width="42%" align="right">
																						&nbsp;
																					</td>
																					<td width="8%" align="left">
																						<input type="button" value="Close" class="dr-menu" onclick="window.close()">
																					</td>
																					<td width="45%" align="left">
																						&nbsp;
																					</td>
																				</tr>
																			</table>
																		</td>
																	</tr>
										</table>
									</td>
								</tr>
							<!-- end page logic -->
						</table>
					</td>
				</tr>									
			</table>
		</td>
	</tr>
</table>
