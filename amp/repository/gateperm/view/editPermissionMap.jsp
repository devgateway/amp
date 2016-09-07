<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

</script>
<digi:form action="/managePermMap.do">
	<script type="text/javascript">
function submitForm(mode) {
	permissionMapForm.mode.value=mode;
	permissionMapForm.submit();
}
</script>
<h1 class="admintitle"><digi:trn key="aim:changeexistingperms">Change Existing Permissions</digi:trn></h1>
	<html:hidden property="mode" />
	<digi:errors />
	<div class="breadcrump">
		<div class="centering">
			<div class="breadcrump_cont">
				<span class="sec_name">Global Permission Manager</span>
				<span class="breadcrump_sep">|</span>
				<c:set var="translation"><digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn></c:set> 
				<a href="/aim/admin.do" class="l_sm" title="${translation}"><digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
					<c:set var="translation"><digi:trn key="aim:clickToGlobalPerm">Click here to goto Global Permission Manager</digi:trn></c:set>
				</a>
				<span class="breadcrump_sep"><b>»</b></span>
				<span class="bread_sel"><digi:trn key="aim:globalperms">Global Permission Manager</digi:trn></span>
			</div>
		</div>
	</div>
	
	
	<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
	  <tr>
		    <td class="main_side_1">
				<div class="wht">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
		  				<tr>
		  				<td>

									<b><digi:trn>Permissions</digi:trn></b><br>
									&nbsp;&nbsp; <digi:link href="/managePerm.do?list" title="EDIT PERMISSIONS"><digi:trn>Edit Permissions</digi:trn></digi:link>								
						</td>
						</tr>
		  				
		  				
		  				<tr>
		    				<td valign="top" width="712">
								<table class="inside" width="712" cellpadding="0" cellspacing="0" border="1">
						
									<tr>
	    								<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside" width=50% align="center"><b class="ins_header" style="font-size:11px;"><digi:trn key="aim:permisiblecategory">Permissible Category</digi:trn></b></td>
	    								<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside" width=50% align="center"><b class="ins_header" style="font-size:11px;"><digi:trn>Assign a global permission to the entire class</digi:trn></b>
	    								</td>
	    							</tr>
	    					    				
	    							<tr>
	    								<td class="inside" align="center">
	    									<html:select styleClass="dropdwn_sm" property="permissibleCategory" onchange="submitForm('permissibleCategoryPicked')">
												<html:option value="select"><digi:trn key="aim:comboSelect">--Select--</digi:trn></html:option>
												<html:optionsCollection property="_availablePermissibleCategories" value="simpleName" label="simpleName" />
											</html:select>
	    								</td>
	    								<td class="inside" align="center">
		    								<logic:notEmpty name="permissionMapForm" property="permissibleCategory">
			    								<html:select property="permissionId" styleClass="dropdwn_sm">
													<html:option value="0">--<digi:trn>None</digi:trn>--</html:option>
													<html:optionsCollection property="_availablePermissions" value="id" label="name" />
												</html:select>
											</logic:notEmpty>
	    								</td>
									</tr>
	    						</table>
	    						<br />
	    						<logic:notEmpty name="permissionMapForm" property="permissibleCategory">
									<center><html:button styleClass="buttonx" property="saveGlobal" onclick="submitForm('saveGlobal')"><digi:trn>Assign Global</digi:trn></html:button></center>
								</logic:notEmpty>
							</td>
							<td width="20">&nbsp;</td>
		    				<td valign="top" width="220">
								<b><digi:trn>Key</digi:trn></b>:
								<div class="perm_legend"><hr />
									<digi:context name="exportperm" property="context/module/moduleinstance/exchangePermission.do?export" />
									<digi:context name="importperm" property="context/module/moduleinstance/exchangePermission.do?import" />
									<input type="button" name="export" class="buttonx" value='<digi:trn jsFriendly="true" key="aim:translationmanagerexportbutton">Export</digi:trn>' onclick="javascript:window.location.href='<%=exportperm%>'" /> 
									<input type="button" name="import" class="buttonx" value='<digi:trn jsFriendly="true" key="aim:translationmanagerimportbutton">Import</digi:trn>' onclick="javascript:window.location.href='<%=importperm%>'" />
									<hr />
								</div>
								
																
							</td>
	  						</tr>
		  		
						</table>
					</div>
				</td>
  		</tr>
	</table>


 </digi:form>