<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="globalsettings" %>

<script type="text/javascript">
function checkEmptyEmail(){
	var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
	var address = document.aimUserEmailForm.email.value;
	if (address==null || address==''){
		<c:set var="translation">
		<digi:trn>Email can not be empty.</digi:trn>
		</c:set>
		alert("${translation}");
    	return false;
	}
	if(reg.test(address) == false) {
		<c:set var="translation">
		<digi:trn key="error.registration.noemail">Please enter a valid email address.</digi:trn>
		</c:set>
		alert("${translation}");
    	return false;
	}
	document.aimUserEmailForm.submit();
}


</script>

<digi:form name="aimUserEmailForm" type="org.digijava.module.um.form.UserEmailForm" action="/resetUserPassword.do" >
<div class="reg_form_container">
<div class="home_sec_title"><digi:trn key="aim:changePassword">
						Reset your e-mail address
						</digi:trn></div>
<table width="100%" valign="top" class="left-align" cellpadding="0" cellspacing="0" border="0">

<tr><td width="100%" valign="top" class="left-align">
<table bgColor=#ffffff border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td width="5%">&nbsp;
		</td>
		<td class="left-align" valign="top" width="60%"><br>
			<table border="0" cellPadding=5 cellspacing="0" width="60%">
				<tr>
					<td width="3">&nbsp;</td>
					<td colspan="2">
						<digi:errors/>
					</td>
				</tr>
				<tr>
					<td width="3">&nbsp;</td>
					<td class="right-align" class=f-names noWrap valign=top>
<!--						<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>-->
						<digi:trn key="aim:email">Email</digi:trn>&nbsp;
					</td>
					<td class="left-align">
						<html:text property="email" styleClass="email-input" size="20" />
						<br /><font color="red" style="font-size:11px;">
						<digi:trn key="aim:userIdExample1">
						e.g. yourname@emailaddress.com
						</digi:trn>
						</font>
					</td>
				</tr>
				<tr>
					<td width="3">&nbsp;</td>
					<td colspan="2" align="center">
						<html:button onclick="checkEmptyEmail();" property="btnSubmit" styleClass="buttonx" ><digi:trn key="btn:submit">Submit</digi:trn> </html:button>
					</td>
				</tr>
			</table>
		</td>
		<td width="5%">&nbsp;
		</td>
		<td bgcolor="#dbe5f1" valign="top" width="30%">
	      <table align="center" border="0" cellPadding=3 cellspacing="0" width="90%">
        		<tr>
		          <td valign="top">&nbsp;</td>
        		</tr>
        		<tr>
	          	<td valign="top">
<span class="formnote">
						 <digi:trn key="aim:loginWarning">
						 You are signing-in to one or more secure applications for
	        		     official business. You have been granted the right to access these
    	      		 	 applications and the information contained in them to facilitate
        	   			 your official business. Your accounts and passwords are your
						 responsibility. Do not share them with anyone.
						 </digi:trn>
</span>						<BR><BR>
          		</td>
  				</tr>
        		<tr>
          		<td valign="top">&nbsp;</td>
  				</tr>
	      </table>
		  <TR>
		</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>



</div>