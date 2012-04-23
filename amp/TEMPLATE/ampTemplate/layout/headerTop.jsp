<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<logic:present name="currentMember" scope="session">
	<script language=javascript>
	function showUserProfile(id){
		<digi:context name="information" property="context/aim/default/userProfile.do" />
		//openURLinWindow("<%= information %>~edit=true~id="+id,480, 350);
		var param = "~edit=true~id="+id;
		previewWorkspaceframe('/aim/default/userProfile.do',param);
	}
	</script>
</logic:present>

 <LINK REL="SHORTCUT ICON" HREF="/favicon.ico">

<!-- Prevent Skype Highlighter -->
<META name="SKYPE_TOOLBAR" content="SKYPE_TOOLBAR_PARSER_COMPATIBLE" />

<digi:context name="displayFlag" property="context/aim/default/displayFlag.do" />
<c:set var="message">
<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
</c:set>
<c:set var="quote">'</c:set>
<c:set var="escapedQuote">\'</c:set>
<c:set var="msg">
${fn:replace(message,quote,escapedQuote)}
</c:set>
<style>
a.header_text:link, a.header_text, A.header_text:active, A.header_text:visited{
	position:relative;
	padding: 5 10 5 10px;
	font-family: Verdana,Tahoma,Arial,sans-serif;
	font-size: 7pt;		  
	font-weight:normal;
	text-align: center ;
	text-transform:uppercase;
	text-decoration:none;
	color: #ffffff;
}

a.header_text:hover {
	color: #ffffff;
	background-color:#222e5d;
	position:relative;
	padding: 5 10 5 10px;
	font-family: Verdana,Tahoma,Arial,sans-serif;
	font-size: 7pt;		  
	font-weight:normal;
	text-align: center ;
	text-transform:uppercase;
	text-decoration:none; 
}
a.header_title,a.header_title:link,a.header_title:hover,A.header_title:active, A.header_title:visited {
	font-family: Verdana,Tahoma,Arial,sans-serif;
	color:#D6D6D6;
	font-size:19px;
	letter-spacing:2px;
	text-decoration:none;
	text-transform:uppercase;
	font-size:18px;!important
	letter-spacing:2px;!important
}
.logLabel{
	color: white;
}	

</style>
<table cellspacing="0" cellPadding="0" border="0" width="100%" vAlign="top" bgcolor="#27415f">
	<tbody>
   	<tr>
        <td valign="center" height="34">&nbsp;
            <logic:notEmpty name="defFlagExist" scope="application">
                <logic:equal name="defFlagExist" scope="application" value="true">
                <img src="<%=displayFlag%>" border="0" width="30" vspace="2" hspace="2" align="absmiddle">
                </logic:equal>
            </logic:notEmpty>
            <digi:link href="/" module="aim" styleClass="header_title" onclick="return quitRnot1('${msg}')" title="Aid Management Platform">
                    <digi:trn key="aim:aidManagementPlatform">Aid Management Platform (AMP)</digi:trn>
            </digi:link>
        </td>
		<td valign="middle" align="right" width="400px">
 	 		<digi:insert attribute="logWidget"/>
 	 	<%-- 
          <logic:present name="currentMember" scope="session">
              <c:set var="translation">
                <digi:trn key="aim:workspacename">Workspace Name</digi:trn>
              </c:set>
              <span title="${translation}"'>
                <bean:define id="teamMember" name="currentMember" scope="session" type="org.digijava.module.aim.helper.TeamMember" />
				<!--<a href="javascript:showUserProfile(${teamMember.memberId})"class="header_text">-->
                	<strong style="color:#FFFFFF">${teamMember.teamName}</strong>
				<!--</a>-->
              </span>
              <c:set var="translation">
                <digi:trn key="aim:clickToViewMemberDetails">Click here to view Member Details</digi:trn>
              </c:set>
              <span title="${translation}"'>
                <a href="javascript:showUserProfile(${teamMember.memberId})"class="header_text">
                	${teamMember.memberName}
                </a>
              </span>
          </logic:present>
           --%>        
		</td>
   	</tr>
   </tbody>

</table>
