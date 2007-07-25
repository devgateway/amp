<%@ page pageEncoding="UTF-8" import="org.digijava.module.aim.dbentity.AmpOrganisation, java.util.* "%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript">

	<!--
	function validate() {
		if (document.calendarEditActivityForm.selOrganisations.checked != null) { // only one
			if (document.calendarEditActivityForm.selOrganisations.checked == false) {
				alert("Please choose an organization to add");
				return false;
			}
		}
		else { // many
           var length=document.getElementById('tempNumResults').value;
           var flag = 0;
           for (i = 0;i < length;i++) {
             if(document.getElementById('chkBox' + i)!=null){
               if (document.getElementById('chkBox' + i).checked) {
                 flag = 1;
                 break;
               }
             }
           }

			if (flag == 0) {
              return false;
			}
		}
		return true;
	}

	function selectOrganization() {
      var length=document.getElementById('tempNumResults').value;
      var flag = 0;
      for (i = 0;i < length;i++) {
        if(document.getElementById('chkBox' + i)!=null){
          if (document.getElementById('chkBox' + i).checked) {
            flag = 1;
            break;
          }
        }
      }

        if (flag == 0) {
          alert("Please choose an organization to add");
          return false;
          }

         for(var i=1; i<length;i++){
          if(document.getElementById('chkBox' + i)!=null){
            if(document.getElementById('chkBox' + i).checked){
              window.opener.addOrganisation(document.getElementById('chkBox' + i).value,document.getElementById('orgName' + i).innerHTML);
            }
          }
        }
       window.close();
	}

	function searchOrganization() {
		if (document.calendarEditActivityForm.tempNumResults.value == 0) {
			  alert ("Invalid value at 'Number of results per page'");
			  document.calendarEditActivityForm.tempNumResults.focus();
			  return false;
		} else {
			 <digi:context name="searchOrg" property="context/module/moduleinstance/searchOrganisation.do?edit=true"/>
		    document.calendarEditActivityForm.action = "<%= searchOrg %>";
		    document.calendarEditActivityForm.submit();
			  return true;
		}
	}

	function searchAlpha(val) {
		if (document.calendarEditActivityForm.tempNumResults.value == 0) {
			  alert ("Invalid value at 'Number of results per page'");
			  document.calendarEditActivityForm.tempNumResults.focus();
			  return false;
		} else {
			 <digi:context name="searchOrg" property="context/module/moduleinstance/searchOrganisation.do"/>
			 url = "<%= searchOrg %>?alpha=" + val + "&orgSelReset=false&edit=true";
		     document.calendarEditActivityForm.action = url;
		     document.calendarEditActivityForm.submit();
			  return true;
		}
	}

	function load() {
		//document.calendarEditActivityForm.keyword.focus();
        return;
    }

	function unload() {
      return;
    }

	function closeWindow() {
		window.close();
	}
	-->

</script>

<digi:instance property="calendarEditActivityForm" />
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
								<digi:trn key="calendar:searchOrganization">
								Search Organizations</digi:trn>
							</td></tr>
						<tr>
							<td align="center" bgcolor=#ECF3FD>
								<table cellSpacing=2 cellPadding=2>
									<tr>
										<td>
											<digi:trn key="calendar:selectOrganizationType">
											Select Organization type</digi:trn>
										</td>
										<td>
											<html:select property="ampOrgTypeId" styleClass="inp-text">
												<html:option value="-1">All</html:option>
												<logic:notEmpty name="calendarEditActivityForm" property="orgTypes">
													<html:optionsCollection name="calendarEditActivityForm" property="orgTypes"
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
											<html:text property="tempNumResults" size="2" styleClass="inp-text" styleId="tempNumResults" />
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
						<logic:notEmpty name="calendarEditActivityForm" property="pagedCol">
						<tr>
							<td align=left vAlign=top>
							<table width="100%" cellPadding=3>
                                  <c:forEach items="${calendarEditActivityForm.pagedCol}" var="pagedCol" varStatus="avoe" >
									   <tr>
											<td bgcolor=#ECF3FD width="10%">
												&nbsp;&nbsp;
                                                <input type="checkbox" name="selOrganisations" value="${pagedCol.ampOrgId}" id="chkBox${avoe.count}" />
                                                &nbsp;
											</td>
											<td bgcolor=#ECF3FD width="90%" id="orgName${avoe.count}">
                                                ${pagedCol.acronym}
											</td>
										</tr>
                                    </c:forEach>
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
						<logic:notEmpty name="calendarEditActivityForm" property="pages">
							<tr>
								<td align="center">
									<table width="90%">
									<tr><td>
									<digi:trn key="aim:pages">
									Pages</digi:trn>
									<logic:iterate name="calendarEditActivityForm" property="pages" id="pages" type="java.lang.Integer">
										<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
										<c:set target="${urlParams1}" property="page">
											<%=pages%>
										</c:set>
										<c:set target="${urlParams1}" property="orgSelReset" value="false"/>
										<c:set target="${urlParams1}" property="edit" value="true"/>

										<c:if test="${calendarEditActivityForm.currentPage == pages}">
											<font color="#FF0000"><%=pages%></font>
										</c:if>
										<c:if test="${calendarEditActivityForm.currentPage != pages}">
											<c:set var="translation">
												<digi:trn key="aim:clickToViewNextPage">Click here to goto Next Page</digi:trn>
											</c:set>
											<digi:link href="/selectOrganization.do" name="urlParams1" title="${translation}" >
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
							<logic:notEmpty name="calendarEditActivityForm" property="alphaPages">
							<tr>
								<td align="center">
									<table width="90%">
									<tr><td>
										<c:set var="translation">
											<digi:trn key="aim:clickToViewAllSearchPages">Click here to view all search pages</digi:trn>
										</c:set>
										<a href="javascript:searchAlpha('viewAll')" title="${translation}">
											viewAll</a>&nbsp;|&nbsp;
										<logic:iterate name="calendarEditActivityForm" property="alphaPages" id="alphaPages" type="java.lang.String">
											<c:if test="${alphaPages != null}">
												<c:if test="${calendarEditActivityForm.currentAlpha == alphaPages}">
													<font color="#FF0000"><%=alphaPages %></font>
												</c:if>
												<c:if test="${calendarEditActivityForm.currentAlpha != alphaPages}">
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



