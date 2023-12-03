<%@page import="org.digijava.kernel.util.SiteUtils"%>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<% if(SiteUtils.isEffectiveLangRTL()) { %>
  <link rel="stylesheet" href="/TEMPLATE/ampTemplate/css_2/amp-rtl.css">
<% } %>
<script language="JavaScript">

	function msg() {
          var msg="<digi:trn key="aim:deleteOrganizationGroup">Are you sure about deleting this Organization Group ?</digi:trn>"
		if (confirm(msg)) {
			document.aimAddOrgGroupForm.action.value = "delete";
			document.aimAddOrgGroupForm.submit();
		}
		else
			return false;
	}

	function move() {
		<digi:context name="selectLoc" property="context/ampModule/moduleinstance/orgGroupManager.do" />
		url = "<%= selectLoc %>";
		document.location.href = url;
	}

	function check() {
		var str = document.aimAddOrgGroupForm.orgGrpName.value;
		var type = trim(document.aimAddOrgGroupForm.orgTypeId.value);
		str = trim(str);

		if (str == null || str.length == 0) {
			alert('<digi:trn jsFriendly="true" key="aim:alert:editOrgGroup">Please enter a name for this Group</digi:trn>');
			document.aimAddOrgGroupForm.orgGrpName.focus();
			return false;
		}
		else if (type == null || type == "-1" || type.length == 0) {
			alert('<digi:trn jsFriendly="true" key="aim:alert:editOrgGroupType">Please select type for this Group</digi:trn>');
			document.aimAddOrgGroupForm.orgTypeId.focus();
			return false;
		}
		else {
			document.aimAddOrgGroupForm.orgGrpName.value = str;
			document.aimAddOrgGroupForm.submit();
		}
	}

	function trim ( inputStringTrim ) {
		fixedTrim = "";
		lastCh = " ";
		for (x=0; x < inputStringTrim.length; x++) {
			ch = inputStringTrim.charAt(x);
			if ((ch != " ") || (lastCh != " ")) { fixedTrim += ch; }
				lastCh = ch;
		}
		if (fixedTrim.charAt(fixedTrim.length - 1) == " ") {
			fixedTrim = fixedTrim.substring(0, fixedTrim.length - 1); }
		return fixedTrim
	}

	var enterBinder	= new EnterHitBinder('saveOrgGrpBtn');
</script>
<div class="admin-content">
<div class="addOrgBox" style="background-color:#F8F8F8;">
	<div class="">
<digi:instance property="aimAddOrgGroupForm" />
<digi:context name="digiContext" property="context"/>

<digi:form action="/editOrgGroup.do" method="post">
<html:hidden property="action" />
<html:hidden property="ampOrgGrpId" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->

				<!---->
					<!-- Start Navigation -->
					<!--<td height=33 colspan=5><span class=crumb>

						<digi:link href="/admin.do" styleClass="comment">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:link href="/orgGroupManager.do" styleClass="comment">
						<digi:trn key="aim:orgGroupManager">
						Organization Group Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<logic:equal name="aimAddOrgGroupForm" property="action" value="create" >
							<digi:trn key="aim:addOrgGroup">Add Organization Group</digi:trn>
						</logic:equal>
						<logic:equal name="aimAddOrgGroupForm" property="action" value="edit" >
							<digi:trn key="aim:editOrgGroup">Edit Organization Group</digi:trn>
						</logic:equal>
                      </span>
					-->
					<!-- End navigation -->
				<!---->
				<!--
					<td height=16 valign="center" width=57 colspan=51><span class=subtitle-blue>
						<digi:trn key="aim:orgGroupManager">
						Organization Group Manager
						</digi:trn>
						</span>
						<br>
						<digi:errors/>
					
				-->
			
																<logic:equal name="aimAddOrgGroupForm" property="action" value="create" >
																	<b><digi:trn key="aim:addOrgGroup">Add Organization Group</digi:trn></b>
																</logic:equal>
																<logic:equal name="aimAddOrgGroupForm" property="action" value="edit" >
																	<b><digi:trn key="aim:editOrgGroup">Edit Organization Group</digi:trn></b>
																</logic:equal>
													
													
													
													<table border="0" cellspacing="3" cellpadding="3" align="center">
  <tr>
    <td align="right"><digi:trn key="aim:orgGroupName">Name</digi:trn><font color="#ff0000">*</font>	</td>
    <td><html:text property="orgGrpName" size="35" /></td><td><digi:errors /></td>
  </tr>
    <tr>
    <td align="right"><digi:trn key="aim:orgGroupCode">Group Code</digi:trn></td>
    <td><html:text property="orgGrpCode" size="15" /></td>
  </tr>
    <tr>
    <td align="right"><digi:trn key="aim:orgGroupType">Type</digi:trn><font color="#ff0000">*</font></td>
    <td>
    	<html:select property="orgTypeId">
	        <c:set var="translation">
				<digi:trn key="aim:btnSelectType">Select Type</digi:trn>
			</c:set>
    		<html:option value="-1">-- ${translation} --</html:option>
    		<logic:notEmpty name="aimAddOrgGroupForm" property="orgTypeColl">
				<html:optionsCollection name="aimAddOrgGroupForm" property="orgTypeColl"
	   									value="ampOrgTypeId" label="orgType" />
	   		</logic:notEmpty>
		</html:select>
	</td>
</tr>
</table>

													
																														
																	          
																
																		
																	  
																		
																	
																	
																		
																	 
																		
																	
																	<%--
																	
																		
																	        <digi:trn key="aim:orgGroupLevel">Level</digi:trn>
																		
																		
																	          <html:select property="levelId">
																	    		<html:option value="-1">-- Select Level --</html:option>
																	    		<logic:notEmpty name="aimAddOrgGroupForm" property="level">
																					<html:optionsCollection name="aimAddOrgGroupForm" property="level"
																		   				value="ampLevelId" label="name" />
																		   		</logic:notEmpty>
																			</html:select>
																		
																	 --%>
																	<div class="buttons">
																			
																						<html:button  styleClass="buttonx_sm" property="submitButton" styleId="saveOrgGrpBtn" onclick="check()">
																							<digi:trn key="btn:save">Save</digi:trn>
																						</html:button>
																					
																						
																						<html:button  styleClass="buttonx_sm" property="submitButton"  onclick="move()">
																							<digi:trn key="btn:cancel">Cancel</digi:trn>
																						</html:button>
                                                                                        	<logic:equal name="aimAddOrgGroupForm" property="flag" value="delete" >
																				<html:button  styleClass="buttonx_sm" property="submitButton"  onclick="msg()">
																					<digi:trn key="btn:deleteThisGroup">Delete this Group</digi:trn>
																				</html:button>
																		
																	</logic:equal>
																					
																		
																	</div>
																
																	<logic:equal name="aimAddOrgGroupForm" property="flag" value="orgReferences" >
																		
																			
																				<b><digi:trn key="aim:cannotDeleteOrgGrpMsgRefOrgs"><font color="#FF0000">
																						Cannot delete the organization group. It is used by one or more organizations.</font>
																					</digi:trn>
																				</b>
																			
																		
																	</logic:equal>
															 
															
														
													<!-- end page logic -->
													
												
											


</digi:form>
</div></div>
</div>