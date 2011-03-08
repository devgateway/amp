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
		<c:set var="translation">
			<digi:trn key="aim:chooseOrganizationToAdd">Please choose an organization to add</digi:trn>
		</c:set>
		if(document.aimEditActivityForm.selectedOrganisationFromPages.value != "-1") return true;

		if (document.aimEditActivityForm.selOrganisations.checked != null) { // only one
			if (document.aimEditActivityForm.selOrganisations.checked == false) {
				alert("${translation}");
				return false;
			}
		}
		else { // many
			var length = document.aimEditActivityForm.surveyOrgId.length;
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.aimEditActivityForm.surveyOrgId[i].checked == true) {
					flag = 1;
					break;
				}
			}

			if (flag == 0) {
				alert("${translation}");
				return false;
			}
		}
		return true;
	}

    function selectOrganization() {
      if (!validate()){
        return;
      }

      var e=document.getElementById("hdnSvAvtion");
      if(e!=null){
        e.value="sel";
      }

      <digi:context name="selOrg" property="context/module/moduleinstance/selectOrganisationForAhsurvey.do?edit=true"/>
      document.forms[0].action = "<%= selOrg %>";
      document.forms[0].target = window.opener.name;
      document.forms[0].submit();
      window.close();
      return true;
    }

	function resetForm() {
		document.aimEditActivityForm.ampOrgTypeId.value=-1;
		document.aimEditActivityForm.keyword.value="";
		document.aimEditActivityForm.tempNumResults.value=10;
	}

	function selectOrganizationPages(page) {
	   <digi:context name="searchOrg" property="context/module/moduleinstance/selectOrganisationForAhsurvey.do?edit=true&orgSelReset=false&page=" />
	   var val = "<%=searchOrg%>";
	   val = val + page;
	   document.aimEditActivityForm.action = val;
	   document.aimEditActivityForm.selectedOrganisationFromPages.value=page;
	   document.aimEditActivityForm.submit();
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
				 <digi:context name="searchOrg" property="context/module/moduleinstance/selectOrganisationForAhsurvey.do?edit=true"/>
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
			 <digi:context name="searchOrg" property="context/module/moduleinstance/selectOrganisationForAhsurvey.do"/>
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
			 <digi:context name="searchOrg" property="context/module/moduleinstance/selectOrganisationForAhsurvey.do"/>
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
<digi:form action="/selectOrganisationForAhsurvey.do" method="post">
<html:hidden property="step"/>
<html:hidden property="item" />
<html:hidden property="selectedOrganisationFromPages" />
<html:hidden property="svAction" styleId="hdnSvAvtion" />
<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border="0">
	<tr><td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left valign="top">
					<table bgcolor=#f4f4f2 cellpadding="0" cellspacing="0" width="100%" class=box-border-nopadding>
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
											<c:set var="translation">
												<digi:trn key="aim:addActivityAllOrganizationTypes">All</digi:trn>
											</c:set>
											<html:select property="ampOrgTypeId" styleClass="inp-text">
												<html:option value="-1">${translation}</html:option>
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
											<html:button  styleClass="dr-menu" property="submitButton" onclick="return searchOrganization()">
												<digi:trn key="btn:search">Search</digi:trn>
											</html:button>
											&nbsp;
											<html:button  styleClass="dr-menu" property="resetButton" onclick="resetForm()">
												<digi:trn key="btn:clear">Clear</digi:trn>
											</html:button>
											&nbsp;
											<html:button  styleClass="dr-menu" property="submitButton" onclick="closeWindow()">
												<digi:trn key="btn:close">Close</digi:trn>
											</html:button>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td align=left valign="top">
					<table bgcolor=#f4f4f2 cellpadding="0" cellspacing="0" width="100%" class=box-border-nopadding>
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<digi:trn key="aim:organizationList">
								List of Organizations</digi:trn>
							</td>
						</tr>
						<logic:notEmpty name="aimEditActivityForm" property="pagedCol">
						<tr>
							<td align=left valign="top">
                              <table width="100%" cellPadding=3>

                                <c:forEach var="org" items="${aimEditActivityForm.pagedCol}">
                                  <tr>
                                    <td bgcolor=#ECF3FD width="10%">
                                    &nbsp;&nbsp;
                                    <html:radio property="surveyOrgId" value="${org.ampOrgId}"/>
                                    </td>
                                    <td bgcolor=#ECF3FD width="90%">
                                    ${org.acronym} &nbsp;&nbsp; ( ${org.name} )
                                    </td>
                                  </tr>
                                </c:forEach>
                                <tr>
										<td align="center" colspan="2">
											<table cellPadding=5>
												<tr>
													<td>
														<html:button  styleClass="dr-menu" property="submitButton"  onclick="return selectOrganization()">
															<digi:trn key="btn:add">Add</digi:trn>
														</html:button>
													</td>
													<td>
														<html:button  styleClass="dr-menu" property="submitButton" onclick="closeWindow()">
															<digi:trn key="btn:close">Close</digi:trn>
														</html:button>

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
									<digi:trn key="aim:pages">Pages</digi:trn>
										<c:if test="${aimEditActivityForm.currentPage > 1}">
											<c:set var="translation">
												<digi:trn key="aim:firstpage">First Page</digi:trn>
											</c:set>
                                            <a href="javascript:selectOrganizationPages(1);" title="${translation}">&lt;&lt;</a>
											<c:set var="translation">
												<digi:trn key="aim:previouspage">Previous Page</digi:trn>
											</c:set>
                                            <a href="javascript:selectOrganizationPages(${aimEditActivityForm.currentPage - 1});" title="${translation}">&lt;</a>

										</c:if>
										<c:set var="length" value="${aimEditActivityForm.pagesToShow}"></c:set>
										<c:set var="start" value="${aimEditActivityForm.startPage}"/>
                                        <c:forEach var="pg" items="${aimEditActivityForm.pages}">
                                          <c:if test="${pg!=aimEditActivityForm.currentPage}">
                                            <c:set var="translation">
                                              <digi:trn key="aim:clickToViewNextPage">Click here to goto Next Page</digi:trn>
                                            </c:set>
                                            <a href="javascript:selectOrganizationPages(${pg});">${pg}</a>
                                          </c:if>
                                          <c:if test="${pg==aimEditActivityForm.currentPage}">
                                            <c:set var="translation">
                                              <digi:trn key="aim:clickToViewNextPage">Click here to goto Next Page</digi:trn>
                                            </c:set>
                                            <font color="#FF0000">${pg}</font>
                                          </c:if>
                                          |&nbsp;
                                        </c:forEach>

									<c:if test="${aimEditActivityForm.currentPage != aimEditActivityForm.pagesSize}">
                                    <c:set var="translation">
										<digi:trn key="aim:nextpage">Next Page</digi:trn>
									</c:set>
                                          <a href="javascript:selectOrganizationPages(${aimEditActivityForm.currentPage + 1});" title="${translation}">&gt;</a>

						     			<c:set var="translation">
										<digi:trn key="aim:lastpage">Last Page</digi:trn>
										</c:set>
                                        <a href="javascript:selectOrganizationPages(${aimEditActivityForm.pagesSize});" title="${translation}">&gt;&gt;</a>
                                        &nbsp;&nbsp;
										</c:if>
										${aimEditActivityForm.currentPage}&nbsp;<digi:trn key="aim:of">of</digi:trn>&nbsp;${aimEditActivityForm.pagesSize}
									</td>
									</tr>
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
										<c:set var="translation">
											<digi:trn key="aim:clickToViewAllSearchPages">Click here to view all search pages</digi:trn>
										</c:set>
										<a href="javascript:searchAlphaAll('viewAll')" title="${translation}">
											viewAll</a>&nbsp;|&nbsp;
										<logic:iterate name="aimEditActivityForm" property="alphaPages" id="alphaPages" type="java.lang.String">
											<c:if test="${alphaPages != null}">
												<c:if test="${aimEditActivityForm.currentAlpha == alphaPages}">
													<font color="#FF0000"><%=alphaPages %></font>
												</c:if>
												<c:if test="${aimEditActivityForm.currentAlpha != alphaPages}">
												<c:set var="translation">
													<digi:trn key="aim:clickToViewNextPage">Click here to go to next page</digi:trn>
												</c:set>
													<a href="javascript:searchAlpha('<%=alphaPages%>')" title="${translation}" >
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



