<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language=javascript>
<!--
function showUserProfile(id){
	<digi:context name="information" property="context/aim/default/userProfile.do" />
	openURLinWindow("<%= information %>~edit=true~id="+id,480, 350);
}

-->
</script>
<table cellpadding="0" cellspacing="0" width="100%" style="background-image:url(module/aim/images/bg-header-1.gif);vertical-align:top;height:37px;" border=0>
  <tr>
    <td valign="top">
      <%--<html:errors/>--%>
      <table cellpadding="0" cellSpacing="0" style="background-image:url(module/aim/images/my-desktop.gif);height:33;width:757;vertical-align:top;" class=r-dotted>
        <tr>
          <td width="10">
          &nbsp;&nbsp;&nbsp;
          </td>
          <logic:present name="currentMember" scope="session">
            <td align="left">
              <c:set var="translation">
                <digi:trn key="aim:clickToViewMemberDetails">Click here to view Member Details</digi:trn>
              </c:set>
              <div title="${translation}"'>
                <bean:define id="teamMember" name="currentMember" scope="session" type="org.digijava.module.aim.helper.TeamMember" />
                <a href="javascript:showUserProfile(${teamMember.memberId})" class="header">
                <digi:trn key="aim:pageshead:title:${teamMember.teamName}">${teamMember.teamName}</digi:trn> :
                <digi:trn key="aim:pagesheadtitle:${teamMember.memberName}">${teamMember.memberName}</digi:trn>
                </a>
              </div>
            </td>
          </logic:present>
          <logic:notPresent name="currentMember" scope="session">
            <td align="left">
              <digi:link href="/reportsPublicView.do" styleClass="header">
                <digi:trn key="aim:publicPortfolio">Public Portfolio</digi:trn>
              </digi:link>
            </td>
            <td align="right">&nbsp;
            </td>
          </logic:notPresent>
        </tr>
      </table>
    </td>
  </tr>
</table>
