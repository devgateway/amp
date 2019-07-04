<%@ page language="java" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:errors/>
<html:javascript formName="userRegisterForm" />

      <TABLE width="500px" align="center">
      <digi:form action="/userRegisterGateway.do" method="post" onsubmit="return validateUserRegisterForm(this);" >
        <TR>
          <TD class=PageTitle colSpan=2>Register MyGateway</TD></TR>
        <TR>
          <TD class=text align=left colSpan=2>To become a member of the
            Development Gateway, please complete the form below.</TD></TR>
        <TR>
          <TD>&nbsp;</TD></TR>
        <TR>
          <TD class=title align=left colSpan=2>Account information / about
          you</TD></TR>
        <TR>
          <TD class=text align=left colSpan=2><digi:trn>All fields marked with an</digi:trn><FONT color=red><B><BIG> * </BIG></B></FONT><digi:trn>are required.</digi:trn>
           <digi:trn key="um:userValidEmail"> Please use a valid e-mail address.</digi:trn></TD></TR>
        <TR>
          <TD colSpan=2 td>
            <TABLE cellspacing="1" cellPadding=2 width="95%" border="0">
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<FONT
                  color=red>*</FONT>First Name</TD>
            <TD class=text noWrap align=left><html:text  property="firstNames" size="50" maxlength="50" /></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<FONT
                  color=red>*</FONT>Last name</TD>
            <TD class=text noWrap align=left><html:text  property="lastName" size="50" maxlength="50" /></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<FONT
                  color=red>*</FONT>E-mail Address</TD>
            <TD class=text noWrap align=left><INPUT type=hidden
                  value=642226 name=user_id> <html:text  property="email" styleClass="email-input" size="50" maxlength="50" />
            </TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<FONT
                  color=red>*</FONT>Repeat Email Address</TD>
            <TD class=text noWrap align=left><html:text  property="emailConfirmation" styleClass="email-input" size="50" maxlength="50" /></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<FONT
                  color=red>*</FONT>Password</TD>
            <TD class=text noWrap align=left><html:password  property="password" size="30" maxlength="30" /></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<FONT
                  color=red>*</FONT>Repeat Password</TD>
            <TD class=text noWrap align=left><html:password  property="passwordConfirmation" size="30" maxlength="30" /></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left><FONT color=red>*</FONT>Country of Residence</TD>
            <TD class=text noWrap align=left>
              <html:select  property="selectedCountryResidence" >
                <bean:define id="countries" name="userRegisterForm" property="countryResidence" type="java.util.Collection" />
                <html:options  collection="countries" property="iso" labelProperty="name" />
              </html:select>
            </TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;Mailing Address</TD>
            <TD class=text noWrap align=left><html:text  property="mailingAddress" size="50" maxlength="50" /></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;Organization Name</TD>
            <TD class=text noWrap align=left><html:text  property="organizationName" size="50" maxlength="50" /></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;Organization Type</TD>
            <TD class=text noWrap align=left>
              <html:select property="selectedOrganizationType">
                <bean:define id="types" name="userRegisterForm" property="organizationType" type="java.util.Collection" />
                <html:options collection="types" property="id" labelProperty="type" />
              </html:select>
            </TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;Website</TD>
            <TD class=text noWrap align=left><html:text  property="webSite" size="60" maxlength="50" /></TD>
          </TR>
          <tr><td><img src="/images/trans.gif" height=15 width=1 border="0"></td></tr>
          <TR >
            <TD class=text noWrap align=left colspan=2>
          	<table cellpadding="0" cellspacing="0" border="0">
             <tr><td nowrap class="text">How did you hear about the Development Gateway?</td></tr>
             <tr><td>
              <html:select property="howDidyouSelect">
                <bean:define id="howDidYou" name="userRegisterForm" property="howDidyouhear" type="java.util.Collection" />
                <html:options collection="howDidYou" property="id" labelProperty="referral" />
              </html:select>
              </td>
             </tr>
             </table>
            </TD>
          </TR>
          <tr><td><img src="/images/trans.gif" height=15 width=1 border="0"></td></tr>
          <TR >
            <TD class=text noWrap align=left colspan=2>
             <table border="0" cellspacing="1" cellpadding="0" width="100%">
          <TR bgcolor="#D5D5D5">
            <TD align=left noWrap class="title">Topics</TD>
            <TD align="center" noWrap class="title">Join</TD>
          </TR>
          <logic:present name="userRegisterForm" property="topicitems">
           <logic:iterate  name="userRegisterForm" id="item" property="topicitems">
            <tr bgColor="#f0f0f0">
	       <td align="left" class="text"><a href='<bean:write name="item" property="siteUrl"/>' class="text"><bean:write name="item" property="siteDescription"/></a></td>
              <td align="center"><html:multibox property="topicselectedItems" ><bean:write name="item" property="site.id"/> </html:multibox></td>
            </tr>
           </logic:iterate>
          </logic:present> 
              </table>
            </TD>
          </TR>
          <TR >
            <TD align=left noWrap class="text">Send me the Development Gateway monthly e-mail newsletter?</TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR bgColor="#FFFFFF">
            <TD align=left noWrap class=text><html:radio  property ="newsLetterRadio" value="true"/>Yes
              <html:radio  property ="newsLetterRadio" value="false"/>No</TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <tr><td><img src="/images/trans.gif" height=15 width=1 border="0"></td></tr>
          <TR >
            <TD align=left noWrap class=title>YOUR USER PROFILE</TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR >
            <TD align=left class="text" colspan=2><p>In addition to having your name
                associated with your content contributions, you can create a
                member profile and appear in the topic directory so that other
                members can see your organization, country of residence and
                favorite topics. <br>
                Please note that your e-mail address will not be displayed. </p></TD>
          </TR>
          <TR bgColor="#FFFFFF">
            <TD align=left noWrap class=text><FONT color=red>*</FONT>Display my member profile <html:radio  property ="membersProfile" value="true"/>Yes
              <html:radio  property ="membersProfile" value="false"/>No</TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <tr><td><img src="/images/trans.gif" height=15 width=1 border="0"></td></tr>
          <TR>
            <TD align=left noWrap class="title">Your language settings</TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR bgColor="#FFFFFF">
            <TD align=left noWrap class=text>I want to receive the alert messages in</TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR bgColor="#FFFFFF">
            <TD align=left noWrap class=text>
              <html:select  property="selectedLanguage">
                <bean:define id="languages" name="userRegisterForm" property="navigationLanguages" type="java.util.Collection" />
                <html:options  collection="languages" property="code" labelProperty="name" />
              </html:select>
             </TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR>
            <TD align=left noWrap class="text">I want to view content in following languages</TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR>
            <TD align=left noWrap class=text>
          	<table cellpadding="0" cellspacing="0" border="0">
		  	 <logic:iterate name="userRegisterForm" id="item" property="contentLanguages">
             <tr><td>
             <html:multibox property="contentSelectedLanguages"><bean:write name="item" property="code"/></html:multibox>
             <bean:write name="item" property="name"/>
              </td></tr>
             </logic:iterate>
            </table>
            </TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
        </TABLE >
            </td>

<TR>
<TD colspan=2 align="center">
<html:submit property="submit" value="SUBMIT" />
</TD>
</TR>
</digi:form>
</TABLE>