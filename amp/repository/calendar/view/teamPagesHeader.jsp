<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/resources/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language=javascript>
function showUserProfile(id){
	<digi:context name="information" property="context/module/moduleinstance/userProfile.do" />
	openURLinWindow("<%= information %>~edit=true~id="+id,480, 350);
}
</script>
<table cellpadding="0" cellspacing="0" width="100%" style="background-image:url(module/aim/images/bg-header-1.gif);vertical-align:top;height:37px;" border="0">
  <tr>
    <td valign="top">
      <table cellpadding="0" cellSpacing="0" width="757" style="background-image:url(module/aim/images/my-desktop.gif);vertical-align:top;height:33px;" class=r-dotted>
        <tr>
          <td width="10">
          &nbsp;&nbsp;&nbsp;
          </td>
          <td align="left">
            <logic:notEmpty name="currentMember" scope="session">
              <bean:define id="teamMember" name="currentMember" scope="session" type="org.digijava.module.aim.helper.TeamMember" />
              <div title='<digi:trn key="aim:clickToViewMemberDetails">Click here to view Member Details</digi:trn>'>
                <a href="javascript:showUserProfile(${teamMember.memberId})" class="header">
                ${teamMember.teamName} :
                ${teamMember.memberName}
</a>
              </div>
            </logic:notEmpty>
          </td>
          <td align="right">
            <div title='<digi:trn key="aim:clickToLogoutTheSystem">Click here to logout from the system</digi:trn>'>
              <a href="${request.contextPath}/j_spring_logout" class="up-menu" onclick="return quitRnot()">
                <digi:trn key="aim:logout">Logout</digi:trn>
              </a>
            </div>&nbsp;&nbsp;&nbsp;&nbsp;
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
