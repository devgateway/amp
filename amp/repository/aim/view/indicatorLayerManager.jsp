<%@ page pageEncoding="UTF-8" %> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ page import="java.util.List"%>
 
 <digi:instance property="aimIndicatorLayerManagerForm" />
<bean:define id="myForm" name="aimIndicatorLayerManagerForm" toScope="page" type="org.digijava.module.aim.form.AddIndicatorLayerForm" />

	
<c:set var="translation">
				<digi:trn>Are you sure you want to delete the indicator layer? Removing the indicator will also delete the associated values per administrative level</digi:trn>
</c:set>
<script type="text/javascript">
function confirmDelete() {
	var ret		= confirm('${translation}');
	return ret;
}
function addNewIndicatorLayer() {
	openNewWindow(650, 380);
	var myForm= document.getElementById("addNewIndicatorLayerForm");
	myForm.target = popupPointer.name;
	myForm.event.value 				= "new";
	myForm.submit();
	
	
}

function editIndicatorLayer (id) {
	openNewWindow(650, 380);
	var myForm= document.getElementById("addNewIndicatorLayerForm");
	myForm.target = popupPointer.name;
	myForm.event.value 				= "edit";
	myForm.idOfIndicator.value = id;
	myForm.submit();
	return false;
}

</script>

<digi:form action="/indicatorLayerManager.do" method="post">
	<h1 style="text-align: left;" class="admintitle">
		<digi:trn key="aim:indicatorLayerManager">Indicator Layer Manager</digi:trn>
	</h1>

	<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="1000" align=center class="box-border-nopadding">
	<tr>
		<td align=left class=r-dotted-lg valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%" border="0">
				<tr>
					<td height=16 valign="center" width=571>
						<digi:errors />
					</td>
				</tr>
				<tr>
				<tr><td>
				<button type="button" class="buttonx" onclick="javascript:addNewIndicatorLayer();" ><digi:trn>Add Indicator Layer</digi:trn></button> &nbsp; &nbsp;
				</td>
				</tr>
				<td>
				
				<logic:notEmpty name="myForm" property="indicatorLayers">
					<table class="inside" style="font-size:12px;">
					<thead>
						<tr align="center">
							<td bgcolor="#c7d4db" class="inside" align="center" >
								<b><digi:trn>Name</digi:trn></b>
							</td>
							<td bgcolor="#c7d4db" class="inside" align="center">
								<b><digi:trn >Unit</digi:trn></b>
							</td>
							<td bgcolor="#c7d4db" class="inside" align="center">
								<b><digi:trn>Color Ramp</digi:trn></b>
							</td>
							<td bgcolor="#c7d4db" class="inside" align="center">
								<b><digi:trn>Adm Level</digi:trn></b>
							</td>
							<td bgcolor="#c7d4db" class="inside ignore" align="center">
								<b><digi:trn>Actions</digi:trn></b>
							</td>
						</tr>
						</thead>
						<tbody class="yui-dt-data">
						<logic:iterate name="myForm" property="indicatorLayers" id="indLayer" type="org.digijava.module.aim.dbentity.AmpIndicatorLayer">
						<tr align="center">
							<td class="inside">
									<bean:write name="indLayer" property="name" />
							</td>
							<td align="left" class="inside">
									<bean:write name="indLayer" property="unit" /> &nbsp;
								&nbsp;
							</td>
							<td align="left" class="inside">
								<logic:iterate name="indLayer" property="colorRamp" id="color" type="org.digijava.module.aim.dbentity.AmpIndicatorColor">
								<logic:notEmpty name="color">
										<span style="display:inline-block;background-color: ${color.color};width:20px;">&nbsp;</span>
										
								</logic:notEmpty>
								</logic:iterate>
							</td>
							<td align="left" class="inside ignore">
								<bean:write name="indLayer" property="admLevel" />
							</td>
							<td align="left" class="inside ignore">
								<ul>
									 <li>
										
										 <a href="#" onclick="return editIndicatorLayer(${indLayer.id})">
											<digi:trn>
												Edit 
											</digi:trn>
										</a> 
										<%-- 	<button type="button" class="buton" onclick="javascript:editIndicatorLayer(${indLayer.id})" ><digi:trn>Edit</digi:trn></button> &nbsp; &nbsp;
			 --%>
									</li>
									<li>
										<a href="/indicatorLayerManager.do?event=delete&idOfIndicator=${indLayer.id}" onclick="return confirmDelete();">
									
											<digi:trn>
												Delete 
											</digi:trn>
										</a>
									</li> 
								</ul>
							</td>
						</tr>
						</logic:iterate>
						</tbody>
					</table>
				
				</logic:notEmpty>
				</td>
				</tr>
		</table>
	</td>
	   <td style="font-size:12px; padding-top:37px;" noWrap width=250 vAlign="top">
							<table align="center" cellpadding="0" cellspacing="0" width="230"
								border="0">
								<tr>
									<td style="font-size:12px;"><!-- Other Links -->
									<table cellpadding="0" cellspacing="0" width="125">
										<tr>
											<td style="font-size:12px;" bgColor=#c9c9c7 class=box-title>
												<b style="padding-left:5px;"><digi:trn key="aim:otherLinks">Other links</digi:trn></b>
											</td>
											<td style="font-size:12px;" background="module/aim/images/corner-r.gif" height="17" width=17></td>
										</tr>
									</table>
									</td>
								</tr>
								<tr>
									<td style="font-size:12px;" bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellspacing="1" width="230" class="inside">
										<tr>
											<td style="font-size:12px;" class="inside"><digi:img src="module/aim/images/arrow-014E86.gif"
												width="15" height="10" /> <c:set var="trnViewAdmin">
												<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
											</c:set> <digi:link href="/admin.do" title="${trnViewAdmin}">
												<digi:trn key="aim:AmpAdminHome">
												Admin Home
												</digi:trn>
											</digi:link></td>
										</tr>
											<tr>
												<td style="font-size:12px;" class="inside">
													<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10" /> 
													<a href="/aim/dynLocationManager.do">
													<digi:trn>
                                                  		Region Manager
                                                  	</digi:trn>
                                                  </a>
												</td>
											</tr>
										<!-- end of other links -->
									</table>
									</td>
								</tr>
							</table>
							</td>
		
	</tr>
</table>
</digi:form>
<div style="display: none;">
	<form id="addNewIndicatorLayerForm"  method="post" action="/aim/indicatorLayerManager.do">
		<input type="hidden" name="event" />
		<input type="hidden" name="idOfIndicator">
	</form> 
</div>