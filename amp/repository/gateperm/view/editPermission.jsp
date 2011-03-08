<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>


<digi:form action="/managePerm.do">
<script type="text/javascript">
function submitForm(mode) {
	permissionForm.mode.value=mode;
	permissionForm.submit();
}
</script>
<digi:errors/>

<div class="breadcrump">
	<div class="centering">
		<div class="breadcrump_cont">
			<span class="sec_name">Advanced Permission Manager</span>
			<span class="breadcrump_sep">|</span>
			<c:set var="translation"><digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn></c:set> 
			<a href="/aim/admin.do" class="l_sm" title="${translation}"><digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
				<c:set var="translation"><digi:trn key="aim:clickToGlobalPerm">Click here to goto Global Permission Manager</digi:trn></c:set>
			</a>
			<span class="breadcrump_sep"><b>»</b></span>
			<span class="breadcrump_sep"><a href="/gateperm/managePermMap.do" class="l_sm"><digi:trn key="aim:globalperms">Global Permission Manager</digi:trn></a></span>
			<span class="breadcrump_sep"><b>»</b></span>
			<span class="bread_sel">List Permissions</span>
		</div>
	</div>
</div>

<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
	<tr>
	    <td class="main_side_1">
			<div class="wht">
				<table class="inside" width=100% cellpadding="0" cellspacing="0" border=1>
					<tr>
					    <td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside align="center" width=20%><b class="ins_header" style="font-size:11px;">Name</b></td>
					    <td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside align="center" width=20%><b class="ins_header" style="font-size:11px;">Description</b></td>
					    <td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside align="center" width=20%><b class="ins_header" style="font-size:11px;">Permission Type</b></td>
					   	<logic:equal name="permissionForm" property="type" value="Composite">
					    	<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside align="center" width=20%><b class="ins_header" style="font-size:11px;">Permissions</b></td>
					    </logic:equal>
					    <logic:equal name="permissionForm" property="type" value="Gate">
					    	<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside align="center" width=20%><b class="ins_header" style="font-size:11px;">Actions</b></td>
						    <td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside align="center" width=20%><b class="ins_header" style="font-size:11px;">Gate Init</b></td>
					    </logic:equal>
				    </tr>
				    <tr>
				        <td class="inside" align="center" valign="top"><html:text property="name" styleClass="inputx"/></td>
					    <td class=inside align="center"  valign="top"><html:textarea property="description" styleClass="inputx"/></td>
					    <td class=inside align="center"  valign="top">
					    	<logic:equal name="permissionForm" property="id" value="0">
								<html:select property="type" onchange="submitForm('type')" styleClass="dropdwn_sm" >
									<html:option value="Gate">Gate</html:option>
									<html:option value="Composite">Composite</html:option>
								</html:select>
							</logic:equal>
							<logic:notEqual name="permissionForm" property="id" value="0">
								<html:select property="type" disabled="true" styleClass="dropdwn_sm">
									<html:option value="Gate">Gate</html:option>
									<html:option value="Composite">Composite</html:option>
								</html:select>
							</logic:notEqual>
						</td>
						<logic:equal name="permissionForm" property="type" value="Composite">	
							<td class="inside" align="center"  valign="top">
											<html:select property="permissions" multiple="true" styleClass="dropdwn_sm">
											<html:optionsCollection property="_availablePermissions" value="id" label="name"/>
											</html:select>
							</td>
						</logic:equal>						
						<logic:equal name="permissionForm" property="type" value="Gate">
							<td class="inside" align="center"  valign="top">
								<html:select property="actions" multiple="true" styleClass="dropdwn_sm">
									<html:optionsCollection property="_availableActions" value="wrappedInstance" label="wrappedInstance"/>
								</html:select>
							</td>
							<td class="inside" align="center"  valign="top">
								<html:select property="gateTypeName" onchange="submitForm('gate')" styleClass="dropdwn_sm">
									<html:option value="unselected">--Select--</html:option>
									<html:optionsCollection property="_availableGateTypes" value="name" label="simpleName"/>
								</html:select>
								
									<logic:notEmpty name="permissionForm" property="gateParameters"><br/><br/>
										<table width="100%" border="0" cellpadding="0" cellspacing="0">
											<logic:iterate id="gateParameter" name="permissionForm" property="gateParameters">
												<tr>
													<td class="inside1" align="left"><bean:write name="gateParameter" property="category"/>:</td>
													<td class="inside1" align="right">
														<html:textarea indexed="true" name="gateParameter" property="value.value" styleClass="inputx"/>
														<i><bean:write name="gateParameter" property="value.category"/></i>
													</td>
												</tr>
											</logic:iterate>
										</table>
									</logic:notEmpty>
							</td>
						</logic:equal>						
				    </tr>
				</table>
				<br/>
				<center>
				<html:button property="save" onclick="submitForm('save')" styleClass="buttonx">Save</html:button>
				<html:cancel property="list" styleClass="buttonx">Cancel</html:cancel></center>
			</div>
		</td>
 	</tr>
 </table>
    
</digi:form>