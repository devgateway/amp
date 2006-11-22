<%@ page pageEncoding="UTF-8" import="org.digijava.module.aim.dbentity.AmpOrganisation, java.util.* "%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript">

	function checkNumeric(objName,comma,period,hyphen)
	{
		var numberfield = objName;
		if (chkNumeric(objName,comma,period,hyphen) == false)
		{
			numberfield.select();
			numberfield.focus();
			return false;
		}
		else
		{
			return true;
		}
	}

	function chkNumeric(objName,comma,period,hyphen)
	{
// only allow 0-9 be entered, plus any values passed
// (can be in any order, and don't have to be comma, period, or hyphen)
// if all numbers allow commas, periods, hyphens or whatever,
// just hard code it here and take out the passed parameters
		var checkOK = "0123456789" + comma + period + hyphen;
		var checkStr = objName;
		var allValid = true;
		var decPoints = 0;
		var allNum = "";
		
		for (i = 0;  i < checkStr.value.length;  i++)
		{
			ch = checkStr.value.charAt(i);
			for (j = 0;  j < checkOK.length;  j++)
			if (ch == checkOK.charAt(j))
			break;
			if (j == checkOK.length)
			{
				allValid = false;
				break;
			}
			if (ch != ",")
			allNum += ch;
		}
		if (!allValid)
		{	
			alertsay = "Please enter only numbers in the \"Number of results per page\"."
			alert(alertsay);
			return (false);
		}
	}


	
	function validate() {

		if (document.aimEditActivityForm.selOrganisations.checked != null) { // only one
			if (document.aimEditActivityForm.selOrganisations.checked == false) {
				alert("Please choose an organization to add");
				return false;
			}
		}
		else { // many
			var length = document.aimEditActivityForm.selOrganisations.length;	  
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.aimEditActivityForm.selOrganisations[i].checked == true) {
					flag = 1;
					break;
				}
			}

			if (flag == 0) {
				alert("Please choose an organization to add");
				return false;					  
			}
		}
		return true;
	}

	function selectOrganization() {
		var flag = validate();
		if (flag == false)
			return false;

		<digi:context name="selOrg" property="context/module/moduleinstance/organisationSelected.do?edit=true"/>
	   document.aimEditActivityForm.action = "<%= selOrg %>";
		document.aimEditActivityForm.target = window.opener.name;
	   document.aimEditActivityForm.submit();
		window.close();
		return true;
	}	

	function searchOrganization() {
		if(checkNumeric(document.aimEditActivityForm.tempNumResults	,'','','')==true) 
		{	
			if (document.aimEditActivityForm.tempNumResults.value == 0) {
				  alert ("Invalid value at 'Number of results per page'");
				  document.aimEditActivityForm.tempNumResults.focus();
				  return false;
			} else {
				 <digi:context name="searchOrg" property="context/module/moduleinstance/searchOrganisation.do?edit=true"/>
			    document.aimEditActivityForm.action = "<%= searchOrg %>";
			    document.aimEditActivityForm.submit();
				  return true;
			}
		}
		else return false;
	}
	
	function searchAlpha(val) {
		if (document.aimEditActivityForm.tempNumResults.value == 0) {
			  alert ("Invalid value at 'Number of results per page'");
			  document.aimEditActivityForm.tempNumResults.focus();
			  return false;
		} else {
			 <digi:context name="searchOrg" property="context/module/moduleinstance/searchOrganisation.do"/>
			 url = "<%= searchOrg %>?alpha=" + val + "&orgSelReset=false&edit=true";
		     document.aimEditActivityForm.action = url;
		     document.aimEditActivityForm.submit();
			  return true;
		}
	}	
	
	function searchAlphaAll(val) {
		if (document.aimEditActivityForm.tempNumResults.value == 0) {
			  alert ("Invalid value at 'Number of results per page'");
			  document.aimEditActivityForm.tempNumResults.focus();
			  return false;
		} else {
			 <digi:context name="searchOrg" property="context/module/moduleinstance/searchOrganisation.do"/>
			// url = "<%= searchOrg %>?alpha=viewAll&orgSelReset=false&edit=true";
		    // document.aimEditActivityForm.action = url;
		    document.aimEditActivityForm.action = "<%= searchOrg %>";
		      var aux= document.aimEditActivityForm.tempNumResults.value;
		      document.aimEditActivityForm.tempNumResults.value=1000000;
		     document.aimEditActivityForm.submit();
		      document.aimEditActivityForm.tempNumResults.value=aux;
			  return true;
		}
	}	

	function load() {
		document.aimEditActivityForm.tempNumResults.value=10;
		document.aimEditActivityForm.keyword.focus();			  
	}

	function unload() {
	}

	function closeWindow() {
		window.close();
	}

</script>

<digi:instance property="aimEditActivityForm" />
<digi:form action="/organisationSelected.do" method="post">

<html:hidden property="item" />

<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border=0>
	<tr><td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left vAlign=top>
					<table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<digi:trn key="aim:searchOrganization">
								Search Organizations</digi:trn>
							</td></tr>
						<tr>
							<td align="center" bgcolor=#ECF3FD>
								<table cellSpacing=2 cellPadding=2>
									<tr>
										<td>
											<digi:trn key="aim:selectOrganizationType">
											Select Organization type</digi:trn>
										</td>
										<td>
											<html:select property="ampOrgTypeId" styleClass="inp-text">
												<html:option value="-1">All</html:option>
												<logic:notEmpty name="aimEditActivityForm" property="orgTypes">
													<html:optionsCollection name="aimEditActivityForm" property="orgTypes" 
														value="ampOrgTypeId" label="orgType" />
												</logic:notEmpty>
											</html:select>
										</td>
									</tr>								
									<tr>
										<td>
											<digi:trn key="aim:enterKeyword">
											Enter a keyword</digi:trn>
										</td>
										<td>
											<html:text property="keyword" styleClass="inp-text" />
										</td>
									</tr>
									<tr>
										<td>
											<digi:trn key="aim:numResultsPerPage">
											Number of results per page</digi:trn>
										</td>
										<td>
											<html:text property="tempNumResults" size="2" styleClass="inp-text" />
										</td>
									</tr>									
									<tr>
										<td align="center" colspan=2>
											<input type="button" value="Search" class="dr-menu" onclick="return searchOrganization()">&nbsp;
											<input type="reset" value="Clear" class="dr-menu">&nbsp;
											<input type="button" value="Close" class="dr-menu" onclick="closeWindow()">
										</td>
									</tr>
								</table>
							</td>
						</tr>					
					</table>
				</td>
			</tr>
			<tr>
				<td align=left vAlign=top>
					<table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<digi:trn key="aim:organizationList">
								List of Organizations</digi:trn>
							</td>
						</tr>					
						<logic:notEmpty name="aimEditActivityForm" property="pagedCol">
						<tr>
							<td align=left vAlign=top>						
							<table width="100%" cellPadding=3>
								<logic:iterate name="aimEditActivityForm" id="organisations" property="pagedCol" 
									type="org.digijava.module.aim.dbentity.AmpOrganisation">
										<tr>
											<td bgcolor=#ECF3FD width="10%">
												&nbsp;&nbsp;
												<html:multibox property="selOrganisations">
													<bean:write name="organisations" property="ampOrgId" />
												</html:multibox>&nbsp;
											</td>
											<td bgcolor=#ECF3FD width="90%">
												<bean:write name="organisations" property="acronym" />
											</td>
										</tr>
									</logic:iterate>
									<tr>
										<td align="center" colspan="2">
											<table cellPadding=5>
												<tr>
													<td>
														<input type="button" value="Add" class="dr-menu" onclick="return selectOrganization()">
													</td>
													<td>
														<input type="button" value="Close" class="dr-menu" onclick="closeWindow()">
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<logic:notEmpty name="aimEditActivityForm" property="pages">
							<tr>
								<td align="center">
									<table width="90%">
									<tr><td>
									<digi:trn key="aim:pages">
									Pages</digi:trn>
									<logic:iterate name="aimEditActivityForm" property="pages" id="pages" type="java.lang.Integer">
										<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
										<c:set target="${urlParams1}" property="page">
											<%=pages%>
										</c:set>
										<c:set target="${urlParams1}" property="orgSelReset" value="false"/>
										<c:set target="${urlParams1}" property="edit" value="true"/>
										
										<c:if test="${aimEditActivityForm.currentPage == pages}">
											<font color="#FF0000"><%=pages%></font>
										</c:if>
										<c:if test="${aimEditActivityForm.currentPage != pages}">
											<bean:define id="translation">
												<digi:trn key="aim:clickToViewNextPage">Click here to goto Next Page</digi:trn>
											</bean:define>
											<digi:link href="/selectOrganization.do" name="urlParams1" title="<%=translation%>" >
												<%=pages%>
											</digi:link>					
										</c:if>										
										|&nbsp; 
									</logic:iterate>
									</td></tr>
									</table>
								</td>
							</tr>
						</logic:notEmpty>						
						</logic:notEmpty>
							<logic:notEmpty name="aimEditActivityForm" property="alphaPages">
							<tr>
								<td align="center">
									<table width="90%">
									<tr><td>
										<bean:define id="translation">
											<digi:trn key="aim:clickToViewAllSearchPages">Click here to view all search pages</digi:trn>
										</bean:define>
										<a href="javascript:searchAlphaAll('viewAll')" title="<%=translation%>">
											viewAll</a>&nbsp;|&nbsp;
										<logic:iterate name="aimEditActivityForm" property="alphaPages" id="alphaPages" type="java.lang.String">
											<c:if test="${alphaPages != null}">
												<c:if test="${aimEditActivityForm.currentAlpha == alphaPages}">
													<font color="#FF0000"><%=alphaPages %></font>
												</c:if>
												<c:if test="${aimEditActivityForm.currentAlpha != alphaPages}">
												<bean:define id="translation">
													<digi:trn key="aim:clickToViewNextPage">Click here to go to next page</digi:trn>
												</bean:define>
													<a href="javascript:searchAlpha('<%=alphaPages%>')" title="<%=translation%>" >
														<%=alphaPages %></a>
												</c:if>
											|&nbsp;
											</c:if>
										</logic:iterate>
									</td></tr>
									</table>
								</td>
							</tr>
						</logic:notEmpty>
					</table>				
				</td>
			</tr>
		</table>
	</td></tr>
</table>
</digi:form>
