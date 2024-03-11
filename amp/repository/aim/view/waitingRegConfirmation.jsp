<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org" prefix="digi" %>
<%@ taglib uri="http://digijava.org/GlobalSettings" prefix="gs" %>

<html:javascript formName="aimUserRegisterForm"/>
<digi:form action="/registerUser.do" method="post" onsubmit="return validateAimUserRegisterForm(this);">

<table width="100%" valign="top" align="left" cellpadding="0" cellspacing="0" border="0">
<tr><td width="100%" valign="top" align="left">
<table bgColor=#ffffff border="0" cellpadding="0" cellspacing="0" width=772>
	<tr>
		<td width=14>&nbsp;
		</td>
		<td align=left valign="top" width=520><br>
			<table border="0" cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<td width="3%">&nbsp;</td>
					<td align=left class=title noWrap colspan="2">
						<b>
                            <gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.USER_REGISTRATION_BY_MAIL%>" compareWith="true" >
                                <digi:trn>Registration is Successful!</digi:trn> <br/>
                                    <digi:trn>A confirmation email has been sent to the address you specified</digi:trn> ${aimUserRegisterForm.email}. <br/> <digi:trn key="aim:newUserConfirmRegSuccessPart2">
                                    You need to read and respond to this email before you can use your account. If you don't do this, the new account will be deleted automatically after a few days.</digi:trn>
                            </gs:test>
                             <gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.USER_REGISTRATION_BY_MAIL%>" compareWith="false" >
                                <digi:trn>
                                  You have been successfully registered!
                                </digi:trn><br/>
                                <digi:trn>
                                 Please contact the AMP system administrator to assign you to a relevant workspace
                                </digi:trn>
                            </gs:test>
					
						</b>
					</td>
				</tr>
			</table>
		</td>
		<td bgColor=#f7f7f4 valign="top">

		</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>


