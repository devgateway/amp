<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<html:javascript formName="aimUserRegisterForm"/>
<digi:form action="/registerUser.do" method="post" onsubmit="return validateAimUserRegisterForm(this);">

<table width="100%" valign="top" align="left" cellpadding=0 cellSpacing=0 border=0>
<tr><td width="100%" valign="top" align="left">
<jsp:include page="header.jsp" flush="true" />
</td>
</tr>
<tr><td width="100%" valign="top" align="left">
<table bgColor=#ffffff border=0 cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;
		</td>
		<td align=left class=r-dotted-lg vAlign=top width=520><br>
			<table border=0 cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td width="3%">&nbsp;</td>
					<td align=left class=title noWrap colspan="2">
						<b>
						<digi:trn key="aim:newUserRegistrationSuccess">Congratualtions your AMP registeration was successfull. Please contact your administrator to be assigned to a team</digi:trn>
						</b>
					</td>
				</tr>
			</table>
		</td>
		<td bgColor=#f7f7f4 class=r-dotted-lg vAlign=top>
	      <table align=center border=0 cellPadding=3 cellSpacing=0 width="90%">
      		 <tr>
		          <td class=r-dotted-lg-buttom vAlign=top><br>
						<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
						<c:set var="translation">
							<digi:trn key="aim:loginSuccess:clickToUseAmp">Click here to Use AMP now</digi:trn>
						</c:set>
						<br>
						<digi:link href="/index.do" title="${translation}" >
						<digi:trn key="aim:loginSuccess:useAMPNow">
						Use AMP now
						</digi:trn>
						</digi:link>
						<BR><BR><BR>
      	     	</td>
        		</tr>
        		<tr>
		          <td vAlign=top>&nbsp;</td>
        		</tr>
        		<tr>
	          	<td class=r-dotted-lg-buttom vAlign=top>
						<digi:img src="module/aim/images/i-C2160E.gif" width="13" height="9"/>
						<digi:trn key="aim:loginWarning">
						 You are signing-in to one or more secure applications for
        			    official business. You have been granted the right to access these
          		 	 applications and the information contained in them to facilitate
           			 your official business. Your accounts and passwords are your
						 responsibility. Do not share them with anyone.
						 </digi:trn>
						<BR><BR>
          		</td>
  				</tr>
        		<tr>
          		<td vAlign=top>&nbsp;</td>
  				</tr>
	      </table>
		  <TR>
		</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>




