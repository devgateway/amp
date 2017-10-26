<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:errors/>            
      <TABLE width="650px" align="center">
      <digi:form action="/userUpdateBlank.do" method="post" >
        <TR>
          <TD class=PageTitle colSpan=2><digi:trn key="um:editYourInfo">Edit your information</digi:trn></TD></TR>
        <TR>
          <TD class=text align=left colSpan=2>&nbsp;</TD></TR>
        <TR>
          <TD>&nbsp;</TD></TR>
        <TR>
          <TD class=title align=left colSpan=2><digi:trn key="um:accountInfoAboutYou">Account information / about you</digi:trn></TD></TR>
        <TR>
          <TD class=text align=left colSpan=2><digi:trn>All fields marked with an</digi:trn><FONT color=red><B><BIG> * </BIG></B></FONT><digi:trn>are required.</digi:trn></TD></TR>
        <TR>
          <TD colSpan=2 td>
            <TABLE cellspacing="1" cellPadding=2 width="95%" border="0">
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<FONT
                  color=red>*</FONT><digi:trn key="um:firstName">First Name</digi:trn></TD>
            <TD class=text noWrap align=left><html:text  property="firstNames" size="30" /></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<FONT
                  color=red>*</FONT><digi:trn key="um:lastName">Last name</digi:trn></TD>
            <TD class=text noWrap align=left><html:text  property="lastName" size="30" /></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<FONT
                  color=red>*</FONT><digi:trn key="um:emailAddress">E-mail Address</digi:trn></TD>
            <TD class=text noWrap align=left><html:text  property="email" size="30" />
            </TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<digi:trn key="um:mailingAddress">Mailing Address</digi:trn></TD>
            <TD class=text noWrap align=left><html:text  property="mailingAddress" size="50" /></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<digi:trn key="um:organizationName">Organization Name</digi:trn></TD>
            <TD class=text noWrap align=left><html:text  property="organizationName" size="50" /></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left><FONT color=red>*</FONT><digi:trn key="um:countryOfResidence">Country of Residence</digi:trn></TD>
            <TD class=text noWrap align=left>
              <html:select  property="selectedCountryResidence" >
                <bean:define id="countries" name="userUpdateForm" property="countryResidence" type="java.util.Collection" />
                <html:options  collection="countries" property="iso" labelProperty="name" />
              </html:select>
            </TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<digi:trn key="um:website">Website</digi:trn></TD>
            <TD class=text noWrap align=left><html:text  property="webSite" size="50" /></TD>
          </TR>
          <TR><TD><img src="/images/trans.gif" height=15 width=1 border="0"></TD></TR>
	  <TR class="bgwhite">
		  <TD align=left noWrap><span class="redbold">*</span><digi:trn key="display_my_profile">Display my member profile</digi:trn><html:radio  property ="membersProfile" value="true"/><digi:trn key="yes">Yes</digi:trn><html:radio  property ="membersProfile" value="false"/><digi:trn key="no">No</digi:trn></TD>
		  <TD align=left noWrap>&nbsp;</TD>
	  </TR>
          <TR><TD><img src="/images/trans.gif" height=15 width=1 border="0"></TD></TR>
          <TR>
            <TD align=left noWrap class="title"><digi:trn key="um:yourLangSettings">Your language settings</digi:trn></TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR bgColor="#FFFFFF">
            <TD align=left noWrap class=text><digi:trn key="um:alertLanguage">Alert language</digi:trn></TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR bgColor="#FFFFFF">
            <TD align=left noWrap class=text>
              <html:select  property="selectedLanguage">
                <bean:define id="languages" name="userUpdateForm" property="navigationLanguages" type="java.util.Collection" />
                <html:options  collection="languages" property="code" labelProperty="name" />
              </html:select>
             </TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>

        </TABLE >
            </td>
<TR>
<TD colspan=2 align="center">
<html:submit property="submit" value="UPDATE"/>
</TD>
</TR>
</digi:form>
</TABLE>