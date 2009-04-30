<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>



<digi:instance property="aimThemeForm" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<digi:errors/>

<digi:form action="/addTheme.do" method="post" type="aimThemeForm" name="aimThemeFormPopin">
<digi:context name="digiContext" property="context" />




<html:hidden property="event"/>
<html:hidden property="themeId"/>
<html:hidden property="parentId"/>
<html:hidden property="prgLevel"/>


 				<table width="100%" align="center" cellPadding=3 cellSpacing=0>
			  <tr bgColor=#dddddb>
				<td height="10" colspan="3" align="center" valign="middle" bgColor=#dddddb>
						            <strong>
<logic:empty name="aimThemeForm" property="parentId"> 						  
          <digi:trn key="aim:addEditProgram">
										Add  Program                                    </digi:trn>
</logic:empty>
<logic:notEmpty name="aimThemeForm" property="parentId"> 
								 <digi:trn key="aim:addEditSubProgram">
										Add/Edit Sub Program                                    </digi:trn>
								</logic:notEmpty>
						            </strong>				  </td>
			  </tr>
				<tr bgColor=#ffffff><td height="10" colspan="3"></td></tr>
				<logic:notEmpty name="aimThemeForm" property="parentId"> 
										<tr bgColor=#ffffff>
								  <td height="10" align="right" valign="middle">
									<strong>
									<digi:trn key="aim:parentProgramName">
									Parent Program Name									</digi:trn>
									</strong></td>
									<td align="left">&nbsp;</td>
								    <td height="10" align="left"><strong>
							        <bean:write name="aimThemeForm" property="parentProgram"/>
								      </strong></td>
				  </tr>
				               </logic:notEmpty> 
                  <tr bgColor=#ffffff>
						<td width="30%" height="10" align="right" valign="middle"><digi:trn key="aim:programName">
										Program Name</digi:trn><font color="red">*</font>						</td>
                        <td width="2" align="left">&nbsp;</td>
                  <td width="71%" height="10" align="left">
<html:text property="programName" size="20"/>						</td>
				<tr bgColor=#ffffff>
				<td width="30%" height="20" align="right" valign="middle">
				  <digi:trn key="aim:programDescription">Description</digi:trn>				</td>
				
                <td width="2" align="left">&nbsp;</td>
                <td align="left">
						<html:textarea property="programDescription" cols="35" rows="2" styleClass="inp-text"/>				</td>
				</tr>
					
<logic:empty name="aimThemeForm" property="parentId"> 
<tr bgColor=#ffffff>
					<td width="30%" height="20" align="right" valign="middle">
<digi:trn key="aim:Internal">
							Internal Financing						</digi:trn>			    </td>
                    <td width="2" align="left">&nbsp;</td>
                    <td align="left">
						<html:text property="programInernalFinancing" size="20"/>					</td>
				</tr>
				<tr bgColor=#ffffff>
					<td width="30%" height="20" align="right" valign="middle">
<digi:trn key="aim:External">
							External Financing						</digi:trn>			    </td>
                    <td width="2" align="left">&nbsp;</td>
                  <td align="left">
								<html:text property="programExternalFinancing" size="20"/>					</td>
				</tr>
				<tr bgColor=#ffffff>
					<td width="30%" height="20" align="right" valign="middle">
<digi:trn key="aim:TotasFinance">
							Total Financing Required						</digi:trn>			    </td>
                    <td width="2" align="left">&nbsp;</td>
                  <td align="left">
							<html:text property="programTotalFinancing" size="20"/>					</td>
				</tr>
</logic:empty>
				<tr bgColor=#ffffff>
					<td width="30%" height="20" align="right" valign="middle"><digi:trn key="aim:programLeadAgency">
								Lead Agency
						</digi:trn>					</td>
				    <td width="2" align="left">&nbsp;</td>
			      <td align="left"><html:textarea property="programLeadAgency"
						cols="35" rows="2" styleClass="inp-text" /></td>
				</tr>
				<tr bgColor=#ffffff>
				<td width="30%" height="20" align="right" valign="middle"><digi:trn key="aim:programCode">
						Program Code</digi:trn>		<font color="red"> *</font>	</td>
                <td width="2" align="left">&nbsp;</td>
                <td align="left">
						<html:text property="programCode" size="20" styleClass="inp-text"/>				</td>
				</tr>
				<tr bgColor=#ffffff>
				<td width="30%" height="20" align="right" valign="middle"><digi:trn key="aim:programType">
						Program Type</digi:trn>
						<font color="red">*</font>				</td>
                <td width="2" align="left">&nbsp;</td>
                <td align="left">
					<c:set var="translation">
						<digi:trn key="aim:program:programTypeFirstLine">Please select from below</digi:trn>
					</c:set>
					<category:showoptions firstLine="${translation}" name="aimThemeForm" property="programTypeCategValId" 
						keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.PROGRAM_TYPE_KEY %>" styleClass="inp-text" />						</td>
				</tr>
				<tr bgColor="#ffffff">
					<td width="30%" height="20" align="right" valign="middle"><digi:trn key="aim:programTargetGroups">Target Groups
						</digi:trn>					</td>
                    <td width="2" align="left">&nbsp;</td>
                  <td align="left">
						<html:textarea property="programTargetGroups" cols="35" rows="2" styleClass="inp-text"/>					</td>
				</tr>
				<tr bgColor=#ffffff>
					<td width="30%" height="20" align="right" valign="middle"><digi:trn key="aim:programBackground">Background
						</digi:trn>					</td>
                    <td width="2" align="left">&nbsp;</td>
                  <td align="left">
						<html:textarea property="programBackground" cols="35" rows="2" styleClass="inp-text"/>					</td>
				</tr>
				<tr bgColor=#ffffff>
					<td width="30%" height="20" align="right" valign="middle"><digi:trn key="aim:programObjectives">Objectives
						</digi:trn>					</td>
                    <td width="2" align="left">&nbsp;</td>
                  <td align="left">
						<html:textarea property="programObjectives" cols="35" rows="2" styleClass="inp-text"/>					</td>
				</tr>
				<tr bgColor=#ffffff>
					<td width="30%" height="20" align="right" valign="middle"><digi:trn
						key="aim:programOutputs">Outputs
				  </digi:trn></td>
				    <td width="2" align="left">&nbsp;</td>
			      <td align="left"><html:textarea property="programOutputs" cols="35"
						rows="2" styleClass="inp-text" /></td>
				</tr>
				<tr bgColor=#ffffff>
					<td width="30%" height="20" align="right" valign="middle"><digi:trn
						key="aim:programBeneficiaries">Beneficiaries
				  </digi:trn></td>
				    <td width="2" align="left">&nbsp;</td>
			      <td align="left"><html:textarea property="programBeneficiaries"
						cols="35" rows="2" styleClass="inp-text" /></td>
				</tr>
				<tr bgColor=#ffffff>
					<td width="30%" height="20" align="right" valign="middle"><digi:trn
						key="aim:programEnvironmentConsiderations">Environment Considerations
				  </digi:trn></td>
				    <td width="2" align="left">&nbsp;</td>
			      <td align="left"><html:textarea
						property="programEnvironmentConsiderations" cols="35" rows="2"
						styleClass="inp-text" /></td>
				</tr>
				<tr bgcolor=#ffffff><td height="5"></td></tr>	
				<tr bgColor=#ffffff><td height="30" colspan="3"></td></tr>
				<tr bgColor=#dddddb>
				<td bgColor=#dddddb height="25" align="center" colspan="3">
						<input styleClass="dr-menu" type="button" name="addBtn" value="<digi:trn key="aim:btnsave">Save</digi:trn>" onclick="return saveProgram('<bean:write name="aimThemeForm" property="themeId"/>','<bean:write name="aimThemeForm" property="rootId"/>','<bean:write name="aimThemeForm" property="name"/>')">&nbsp;&nbsp;
						<input styleClass="dr-menu" type="reset" value="<digi:trn key="aim:btncancelCancel">Cancel</digi:trn>">&nbsp;&nbsp;
						<input styleClass="dr-menu" type="button" name="close" value="<digi:trn key="aim:btnclose">Close</digi:trn>" onclick="closeWindow()">				</td>
				</tr>	
  </table>

</digi:form>