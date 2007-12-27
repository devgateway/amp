<%@ page pageEncoding="UTF-8" import="org.digijava.module.aim.dbentity.AmpOrganisation, java.util.* "%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript">

	function selectOrganization() {
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
		document.forms[0].reset();
	}

	function searchOrganization() {
      var e=document.getElementById("hdnSvAvtion");
      if(e!=null){
        e.value="search";
      }
      <digi:context name="searchOrg" property="context/module/moduleinstance/selectOrganisationForAhsurvey.do?edit=true"/>
      document.forms[0].action="<%=searchOrg%>";
      document.forms[0].submit();
	}

	function unload() {
	}

	function closeWindow() {
		window.close();
	}

</script>

<digi:instance property="aimEditActivityForm" />
<digi:form action="/selectOrganisationForAhsurvey.do" method="post">
<html:hidden property="svAction" styleId="hdnSvAvtion"/>
  <table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border=0>
    <tr>
      <td vAlign="top">
        <table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
          <tr>
            <td align=left vAlign=top>
              <table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
                <tr bgcolor="#006699">
                  <td vAlign="center" width="100%" align ="center" class="textalb" height="20">
                    <digi:trn key="aim:searchOrganization">Search Organizations</digi:trn>
                  </td>
                </tr>
                <tr>
                  <td align="center" bgcolor=#ECF3FD>
                    <table cellSpacing=2 cellPadding=2>
                      <tr>
                        <td>
                          <digi:trn key="aim:enterKeyword">Enter a keyword</digi:trn>
                        </td>
                        <td>
                          <html:text property="keyword" styleClass="inp-text" />
                        </td>
                      </tr>
                      <tr>
                        <td align="center" colspan="2">
                          <c:set var="trn">
                            <digi:trn key="btn:search">Search</digi:trn>
                          </c:set>
                          <input type="button" id="btnSearch" value="${trn}" class="dr-menu" onclick="searchOrganization()">

                          <c:set var="trn">
                            <digi:trn key="btn:clear">Clear</digi:trn>
                          </c:set>
                          <input type="button" id="btnClear" value="${trn}" class="dr-menu" onclick="resetForm()">

                          <c:set var="trn">
                            <digi:trn key="btn:close">Close</digi:trn>
                          </c:set>
                          <input type="button" id="btnClose" value="${trn}" class="dr-menu" onclick="closeWindow()">
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
                    <digi:trn key="aim:organizationList">List of Organizations</digi:trn>
                  </td>
                </tr>
                <c:if test="${!empty aimEditActivityForm.pagedCol}">
                  <tr>
                    <td align=left vAlign=top>
                      <table width="100%" cellPadding=3>
                        <c:forEach var="org" items="${aimEditActivityForm.pagedCol}">
                          <tr>
                            <td bgcolor=#ECF3FD width="10%">
                              &nbsp;&nbsp;
                              <html:radio property="surveyOrgId" value="${org.ampOrgId}"/>
                            </td>
                            <td bgcolor=#ECF3FD width="90%">
                              ${org.name}
                            </td>
                          </tr>
                        </c:forEach>
                        <tr>
                          <td align="center" colspan="2">
                            <table cellPadding=5>
                              <tr>
                                <td>
                                  <c:set var="trn">
                                    <digi:trn key="btn:add">Add</digi:trn>
                                  </c:set>
                                  <input type="button" id="btnAdd" value="${trn}" class="dr-menu" onclick="return selectOrganization()">
                                </td>
                                <td>
                                  <c:set var="trn">
                                    <digi:trn key="btn:close">Close</digi:trn>
                                  </c:set>
                                  <input type="button" id="btnClose" value="${trn}" class="dr-menu" onclick="closeWindow()">
                                </td>
                              </tr>
                            </table>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </c:if>
              </table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</digi:form>
