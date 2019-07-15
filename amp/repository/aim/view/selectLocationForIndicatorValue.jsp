<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

<style>
body {font-family:Arial, Helvetica, sans-serif; font-size:12px;}
.buttonx {background-color:#5E8AD1; border-top: 1px solid #99BAF1; border-left:1px solid #99BAF1; border-right:1px solid #225099; border-bottom:1px solid #225099; font-size:11px; color:#FFFFFF; font-weight:bold; padding-left:5px; padding-right:5px; padding-top:3px; padding-bottom:3px;}
hr {border: 0; color: #E5E5E5; background-color: #E5E5E5; height: 1px; width: 100%; text-align: left;}
</style>
<script language="javascript">

function closeWindow() {
	window.close();
}

function selectLocation(){
  var locationLevelIndex=document.aimThemeForm.locationLevelIndex.value;
  var msg="<digi:trn jsFriendly='true'>Please, select value</digi:trn>"
  if(locationLevelIndex==2){
      var regionLevelValue=document.aimThemeForm.impRegion;
      if(regionLevelValue.value==-1){
          regionLevelValue.focus();
          alert(msg);
          return;
      }
  }
  else{
      if(locationLevelIndex==3){
      var zoneLevelValue=document.aimThemeForm.impZone;
      if(zoneLevelValue.value==-1){
          zoneLevelValue.focus();
          alert(msg);
          return;
      }
  }
  else{
       if(locationLevelIndex==4){
      var woredaLevelValue=document.aimThemeForm.impWoreda;
      if(woredaLevelValue.value==-1){
          woredaLevelValue.focus();
          alert(msg);
          return;
      }

  }
   else{
          if(locationLevelIndex==-1){
               alert(msg);
              return;
          }
      }
  }
  }
  document.getElementById("indAction").value="add";
  document.aimThemeForm.target=window.opener.name;
  document.aimThemeForm.submit();
  window.close();
}

function countryChanged() {
		  document.aimThemeForm.fill.value = "region";
		  <digi:context name="selectLoc" property="context/module/moduleinstance/selectLocationForIndicatorValue.do?edit=true" />
		  document.aimThemeForm.target = "_self";
		  document.aimThemeForm.submit();
	}

	function regionChanged() {
	     document.aimThemeForm.fill.value = "zone";
		  <digi:context name="selectLoc" property="context/module/moduleinstance/selectLocationForIndicatorValue.do?edit=true" />
		  document.aimThemeForm.target = "_self";
		  document.aimThemeForm.submit();
	}

	function zoneChanged() {
		  document.aimThemeForm.fill.value = "woreda";
		  <digi:context name="selectLoc" property="context/module/moduleinstance/selectLocationForIndicatorValue.do?edit=true" />
		  document.aimThemeForm.target = "_self";
		  document.aimThemeForm.submit();
	}

	function woredaChanged() {
		  document.aimThemeForm.fill.value = "woredaSelected";
		  <digi:context name="selectLoc" property="context/module/moduleinstance/selectLocationForIndicatorValue.do?edit=true" />
		  document.aimThemeForm.target = "_self";
		  document.aimThemeForm.submit();
	}
	
	function levelChanged() {
		 
		  <digi:context name="selectLoc" property="context/module/moduleinstance/selectLocationForIndicatorValue.do?edit=true" />
	 	  
		  document.aimThemeForm.target = "_self";
		  document.aimThemeForm.submit();
	}
</script>
<digi:instance property="aimThemeForm" />
<digi:form action="/selectLocationForIndicatorValue.do" method="post">
  <html:hidden property="action" styleId="indAction" value=""/>
  <html:hidden property="fill" />
  <table width="100%" vAlign="top" border="0">
	<tr><td vAlign="top" width="100%">
		<table cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr width="100%">
				<td align=left valign="top" width="100%">
					<table cellpadding="0" cellspacing="0" width="100%" class=box-border-nopadding style="font-size:12px;">
						<tr bgcolor="#c7d4db">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="30">
								<b><digi:trn key="aim:selectLocation">Select Location</digi:trn></b>
							</td>
						</tr>
						<tr>
							<td>
								<table width="100%" cellpadding="2" cellspacing="2" style="font-size:12px;">
									<tr>
										<td align="right" width="50%"><digi:trn key="aim:pleaseSelectLevel">Select level</digi:trn></td>
										<td align="left" width="50%">
											<html:select name="aimThemeForm" property="locationLevelIndex" onchange="levelChanged();">
												<html:option value="-1"><digi:trn key="aim:selectFromBelow">Select From Below</digi:trn></html:option>
												<html:option value="1"><digi:trn key="aim:national">National</digi:trn></html:option>
												<html:option value="2"><digi:trn key="aim:regional">Regional</digi:trn></html:option>
                                                <html:option value="3"><digi:trn key="aim:district">Zone</digi:trn></html:option>
												<html:option value="4"><digi:trn key="aim:district">District</digi:trn></html:option>
											</html:select>
										</td>
									</tr>
									<c:if test="${aimThemeForm.locationLevelIndex>=1}">
										<tr>
											<td align="right"><digi:trn key="aim:country">Administrative Level 0</digi:trn> </td>
											<td align="left"><b><c:out value="${aimThemeForm.country}"></c:out></b></td>
										</tr>
									</c:if>
									<c:if test="${aimThemeForm.locationLevelIndex>=2  &&  !empty aimThemeForm.regions}">
										<tr>
											<td align="right" width="50%"><digi:trn key="aim:selectRegion">Select Region</digi:trn></td>
											<td align="left" width="50%">
												<html:select name="aimThemeForm" property="impRegion" onchange="regionChanged()">
													<html:option value="-1">Select Region</html:option>
													<logic:notEmpty name="aimThemeForm" property="regions">
														<html:optionsCollection name="aimThemeForm" property="regions" value="id" label="name" />
													</logic:notEmpty>
												</html:select>
											</td>
										</tr>
									</c:if>
									<c:if test="${aimThemeForm.locationLevelIndex>=3 &&  !empty aimThemeForm.zones }">
										<tr>
										<td align="right" width="50%"><digi:trn key="aim:selectZone">Select Zone</digi:trn></td>
										<td align="left" width="50%">
											<html:select property="impZone" onchange="zoneChanged()" styleClass="inp-text" >
												<html:option value="-1">Select Zone</html:option>
												<logic:notEmpty name="aimThemeForm" property="zones">
													<html:optionsCollection name="aimThemeForm" property="zones"value="id" label="name" />
												</logic:notEmpty>
											</html:select>
										</td>
									</tr>
									<c:if test="${aimThemeForm.locationLevelIndex>=4 &&  !empty aimThemeForm.woredas }">
									<tr>
										<td align="right" width="50%"><digi:trn key="aim:selectWoreda">Select Woreda</digi:trn></td>
										<td align="left" width="50%">
											<html:select property="impWoreda" onchange="woredaChanged()" styleClass="inp-text" >
												<html:option value="-1">Select Woreda</html:option>
												<logic:notEmpty name="aimThemeForm" property="woredas">
													<html:optionsCollection name="aimThemeForm" property="woredas"value="id" label="name" />
												</logic:notEmpty>
											</html:select>
										</td>
									</tr>
									</c:if>
								</c:if>
									
							  </table>
							</td>
						</tr>

						<tr>
							<td align="center">
								<table cellPadding=3 cellSpacing=3>
									<tr>
										<td>
											<input type="button" value="<digi:trn key='btn:add'>Add</digi:trn>" class="buttonx"
											onclick="selectLocation()" >
										</td>
										<td>
											<input type="reset" value="<digi:trn key='btn:clear'>Clear</digi:trn>" class="buttonx">
										</td>
										<td>
											<input type="button" value="<digi:trn key='btn:close'>Close</digi:trn>" class="buttonx" onclick="closeWindow()">
										</td>
									</tr>
								</table>
							</td>
						</tr>

					</table>
				</td>
			</tr>
		</table>
	</td></tr>
</table>

</digi:form>



















