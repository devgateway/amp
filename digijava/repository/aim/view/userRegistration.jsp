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
						<digi:errors />
					</td>
				</tr>					
				<tr>
					<td width="3%">&nbsp;</td>				
					<td align=left class=title noWrap colspan="2">
						<digi:trn key="um:accountInfoAboutYou">Account information / about you
						</digi:trn>
					</td>
				</tr>		
				<tr>
					<td width="3%">&nbsp;</td>				
					<td align=left class=title noWrap colspan="2">
						<digi:trn key="um:allMarkedRequiredField">All fields marked with an <FONT color=red><B><BIG>*</BIG>
						</B></FONT> are required.
						</digi:trn>
						<digi:trn key="um:userValidEmail"> Please use a valid e-mail address.</digi:trn>
					</td>
				</tr>					
				<tr>
					<td width="3%">&nbsp;</td>				
					<td align=right class=f-names noWrap width="40%">
						<FONT color=red>*</FONT>
						<digi:trn key="um:firstName">First Name
						</digi:trn>
					</td>
					<td align="left">
						<html:text property="firstNames" size="20" styleClass="inp-text"/>
					</td>
				</tr>				
				<tr>
					<td width="3%">&nbsp;</td>				
					<td align=right class=f-names noWrap width="40%">
						<FONT color=red>*</FONT>
						<digi:trn key="um:lastName">Last Name
						</digi:trn>
					</td>
					<td align="left">
						<html:text property="lastName" size="20" styleClass="inp-text"/>
					</td>
				</tr>
				<tr>
					<td width="3%">&nbsp;</td>				
					<td align=right class=f-names noWrap>
						<FONT color=red>*</FONT>
						<digi:trn key="um:emailAddress">E-mail Address
						</digi:trn>
					</td>
					<td align="left">
						<html:text property="email" size="20" styleClass="inp-text"/>
					</td>
				</tr>
				<tr>
					<td width="3%">&nbsp;</td>				
					<td align=right class=f-names noWrap>
						<FONT color=red>*</FONT>
						<digi:trn key="um:repEmailAddress">Repeat Email Address
						</digi:trn>
					</td>
					<td align="left">
						<html:text property="emailConfirmation" size="20" styleClass="inp-text"/>
					</td>
				</tr>
				<tr>
					<td width="3%">&nbsp;</td>				
					<td align=right class=f-names noWrap>
						<FONT color=red>*</FONT>
						<digi:trn key="um:password">Password
						</digi:trn>
					</td>
					<td align="left">
						<html:password property="password" size="20" />
					</td>
				</tr>	
				<tr>
					<td width="3%">&nbsp;</td>				
					<td align=right class=f-names noWrap>
						<FONT color=red>*</FONT>
						<digi:trn key="um:repPassword">Repeat Password
						</digi:trn>
					</td>
					<td align="left">
						<html:password property="passwordConfirmation" size="20" />
					</td>
				</tr>
				
				<tr>
					<td width="3%">&nbsp;</td>				
					<td align=right class=f-names noWrap>
						<FONT color=red>*</FONT>
						<digi:trn key="um:countryOfResidence">Country of Residence
						</digi:trn>
					</td>
					<td align="left">
              <html:select  property="selectedCountryResidence" styleClass="inp-text">
                <bean:define id="countries" name="aimUserRegisterForm" property="countryResidence" 
					 type="java.util.Collection" />
                <html:options  collection="countries" property="iso" labelProperty="name" />
              </html:select>
					</td>
				</tr>

				<tr>
					<td width="3%">&nbsp;</td>				
					<td align=right class=f-names noWrap>
						<digi:trn key="um:mailingAddress">Mailing Address
						</digi:trn>
					</td>
					<td align="left">
						<html:text property="mailingAddress" size="20" styleClass="inp-text"/>
					</td>
				</tr>		
				<tr>
					<td width="3%">&nbsp;</td>				
					<td align=right class=f-names noWrap>
						<digi:trn key="um:organizationName">Organization Name
						</digi:trn>
					</td>
					<td align="left">
						<html:text property="organizationName" size="20" styleClass="inp-text"/>
					</td>
				</tr>					
				
				<!-- <tr>
					<td width="3%">&nbsp;</td>				
					<td align=right class=f-names noWrap>
						<digi:trn key="um:website">Website
						</digi:trn>
					</td>
					<td align="left">
						<html:text property="webSite" size="20" styleClass="inp-text"/>
					</td>
				</tr> -->
				
				<tr>
					<td width="3%">&nbsp;</td>				
					<td align=right class=f-names noWrap>
						<digi:trn key="um:yourLangSettings">Your language settings
						</digi:trn>
					</td>
					<td align="left">
              <html:select  property="selectedLanguage" styleClass="inp-text">
                <bean:define id="languages" name="aimUserRegisterForm" property="navigationLanguages" 
					 type="java.util.Collection" />
                <html:options  collection="languages" property="code" labelProperty="name" />
              </html:select>
					</td>
				</tr>					
				<tr>
					<td>&nbsp;</td>				
					<td>&nbsp;</td>
					<td align="left">
						<html:submit value="Submit" styleClass="dr-menu" />
					</td>
				</tr>
				<tr>
					<td colspan=3>&nbsp;</td>
				</tr>				
			</table>
		</td>
		<td bgColor=#f7f7f4 class=r-dotted-lg vAlign=top>
	      <table align=center border=0 cellPadding=3 cellSpacing=0 width="90%">
      		 <tr>
		          <td class=r-dotted-lg-buttom vAlign=top><br>
						<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
						<bean:define id="translation">
							<digi:trn key="aim:clickToUseAmpEthiopia">Click here to Use AMP Ethiopia now</digi:trn>
						</bean:define>
						<digi:link href="/index.do" title="<%=translation%>" >	
						<digi:trn key="aim:useAMPEthiopiaNow">
						Use AMP Ethiopia now
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
						 </digi:trn		>
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

