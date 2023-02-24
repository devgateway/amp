<%@ page language="java" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="userContactForm" />



<!-- Top Nav start-->
<table height="100%" width="100%" border="0" cellspacing="0" cellpadding="0">
<tr> 
<td valign="top" align="center">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr> 
		<td width="10"><img src="images/trans.gif" width="10" height="1" alt=""></td>
		<td>
     			<table border="0" cellspacing="0" cellpadding="0">
				<tr><td valign="top"><img src="images/trans.gif" width="1" height="5" alt=""></td></tr>
				<tr><td><img src="images/h_logo.gif" width="195" alt="Development Gateway Version 4.0" border="0"></td></tr>
				<tr><td valign="top"><img src="images/trans.gif" width="1" height="5" alt=""></td></tr>
				<tr><td align="center" valign="bottom" class="comment"><small><digi:trn key="um:wordsOfKnowledge">where worlds of knowledge meet</digi:trn></small></td></tr>
			</table>
		</td>
		<td width="10"><img src="images/trans.gif" width="10" height="1" alt=""></td>
		<td align="right" valign="top" height="45" width="100%">
				<a href="javascript:window.close()" class="navtop4"><digi:trn key="um:closeThisWindow">Close this window</digi:trn></a>
        			<table border="0" width="100%"  height="45" cellspacing="0" cellpadding="0" background="images/devgate.gif">
	 	 			<tr><td>&nbsp;</td></tr>
				</table>
		</td>
		<td width="10"><img src="images/trans.gif" width="10" height="1" alt=""></td>
	</tr>
	<tr><td colspan="5" valign="top"><img src="images/trans.gif" width="1" height="10" alt=""></td></tr>
	<tr><td colspan="5" height="1" bgcolor="#000000"><img src="images/trans.gif" width="1" height="1" alt=""></td></tr>
	<tr><td colspan="5" valign="top"><img src="images/trans.gif" width="1" height="10" alt=""></td></tr>
	</table>
	

<table>
<digi:errors/>
 <tr><td colspan=2 align=left class="Title"><big>Tell a friend about the Development Gateway!</big></td></tr>
 <tr><td>
  <digi:form action="/userTell.do" method="post">
   <table>
   <tr >
      <td width="5%"  align="right"><digi:trn key="um:from">From:</digi:trn></td>
      <td width="80%" align="left"><c:out value="${userContactForm.senderName}" /></td>
   </tr>
   <tr >
      <td width="5%"  align="right" nowrap><digi:trn key="um:colleaguesName">Colleague's name</digi:trn>:</td>
      <td width="80%" align="left"><html:text property="recipientName" size="30"/></td>
   </tr> 
   <tr>
      <td width="5%"   align="right" nowrap><digi:trn key="um:colleaguesEmail">Colleague's email</digi:trn>:</td>
      <td width="80%"  align="left"><html:text property="recipientEmail" size="30" /></td>
  </tr>
  <tr>
      <td></td>
      <td>
          <html:select property="selectedLanguage">
             <bean:define id="languages" name="userContactForm" property="contentLanguages" type="java.util.Collection" />
             <html:options collection="languages" property="code" labelProperty="name" />
          </html:select>
	</td>		
  </tr>
  </table>
  <table>
  <tr>
      <td width="5%"  align="right"><html:submit property="submit" value=" SEND " /></td>
  </tr>
  </table> 	    
 </digi:form>
 <td></tr>
</table>


</td>
</tr>
<tr><td height="1" bgcolor="#000000"><img src="images/trans.gif" width="1" height="1" alt=""></td></tr>
<tr><td height="20" align="center" valign="center" bgcolor="#DDDDDD"><a href="javascript:window.close()" class="navtop4"><digi:trn key="um:closeThisWindow">Close this window</digi:trn></a></td></tr>
</table>