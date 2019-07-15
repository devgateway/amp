<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<% // ----------- AFTER USER LOGIN %>
<logic:present name="org.digijava.kernel.user">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="10" height="25"><digi:img src="images/ui/teaserTitleLeft.gif"/></td>
					<td width="100%" height="25" class="teaserTitleBody">&nbsp;Logged in</td>
					<td width="5" height="25"><digi:img src="images/ui/teaserTitleRight.gif"/></td>
				</tr>
				<tr>
				<td colspan="2" class="bodyField" width="100%">
				<!-- Body -->
	
					<table border="0" width="100%">
						<tr>
							<td nowrap >
								<digi:link href="/showUserAccount.do" styleClass="title_topic"><bean:write name="org.digijava.kernel.user" property="name" /></digi:link>
							</td>
						</tr>
						<tr>
							<td align="right">
								<digi:link site="dglogin" contextPath="/module/moduleinstance" href="/logoutAction.do" styleClass="text" >Log&nbsp;out</digi:link>
							</td>
						</tr>
					</table>
					
				<!-- Body -->
					</td>
					<td class="bodyRightTile" width="10"><digi:img src="images/ui/spacer.gif"/></td>
					</tr>
					
					<tr>
					<td width="10" height="11"><digi:img src="images/ui/teaserBottomLeft.gif"/></td>
					<td width="100%" height="11" class="teaserBottomBody"><digi:img src="images/ui/spacer.gif"/></td>
					<td width="5" height="11"><digi:img src="images/ui/teaserBottomRight.gif"/></td>
					</tr>	
				</table>


</logic:present>
<% // ---------------------------------  %>

<% // ----------- BEFOR USER LOGIN %>
<logic:notPresent name="org.digijava.kernel.user">


		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="10" height="25"><digi:img src="images/ui/teaserTitleLeft.gif"/></td>
				<td width="100%" height="25" class="teaserTitleBody">&nbsp;Demosite login</td>
				<td width="5" height="25"><digi:img src="images/ui/teaserTitleRight.gif"/></td>
			</tr>
			<tr>
			<td colspan="2" class="bodyField" width="100%">
			<!-- Body -->
		
		<digi:javascript formName="logonForm" />
		<div align="center">
		<digi:form site="dglogin" contextPath="/module/moduleinstance" action="/logonAction.do" onsubmit="return validateLogonForm(this);">
		<table border="0" class="border" cellpadding="0" cellspacing="0">
		<tr><td>
		<digi:errors />

		</td></tr>
		<tr><td>
		<TABLE border="0" width="100%" cellpadding="0" cellspacing="3">
		<TR>
		<TD colspan=2></TD>
		</TR>
		<TR>
		<TD align="right">email:</TH>
		<TD align="left"><html:text styleClass="demo email-input" property="username"/></TD>
		</TR>
		<TR>
		<TD align="right">Password:</TH>
		<TD align="left"><html:password styleClass="demo" property="password"/></TD>
		</TR>
		<TR><TD colspan="2"><digi:link href="/showEmailForm.do">Forgot your password ?</digi:link></TD></TR>
		<TR><TD colspan="2" align="center">e.g.: admin/admin  read/read  translator/translator</TD></TR>
	<tr>
		<td colspan="2" class="contentSplitter" height="4" width="100%">
			<digi:img src="images/ui/spacer.gif" height="4"/>
		</td>
	</tr>
		<TR>
		<TD align="left" ><html:submit value="Enter"/></TD>
		<TD align="left" ><html:checkbox property="saveLogin">Save Login?</html:checkbox></TD>
		</TR>

	<tr>
		<td colspan="2" class="contentSplitter" height="4" width="100%">
			<digi:img src="images/ui/spacer.gif" height="4"/>
		</td>
	</tr>
		
		<TR><TD colspan="2"><digi:link href="/showUserRegister.do" styleClass="title">Register Here</digi:link></TD></TR>
		</TABLE>
		</digi:form>
		</td></tr>
		</table>
		</div>

		<!-- Body -->
			</td>
			<td class="bodyRightTile" width="10"><digi:img src="images/ui/spacer.gif"/></td>
			</tr>
			
			<tr>
			<td width="10" height="11"><digi:img src="images/ui/teaserBottomLeft.gif"/></td>
			<td width="100%" height="11" class="teaserBottomBody"><digi:img src="images/ui/spacer.gif"/></td>
			<td width="5" height="11"><digi:img src="images/ui/teaserBottomRight.gif"/></td>
			</tr>	
		</table>
		
</logic:notPresent>
<% // ---------------------------------  %>

