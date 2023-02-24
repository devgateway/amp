<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ page import = "org.digijava.kernel.translator.ValueBean"%>

<logic:notEmpty name="languages" scope="request">
<bean:define id="lang" name="languages" type="java.util.Set" scope="request"/>
</logic:notEmpty>

<%
String search = request.getParameter("search");
%>
<html>
<head>
<script language="JavaScript">
function myfunc(myString)
{
	window.document.form1.rownum.value=myString;
	window.document.form1.submit();
}
function myfunction(myString)
{
	window.document.form1.SaveAll.value=myString;
	window.document.form1.submit();
}
function check(myString)
{
	window.document.form1.expired.value=myString;
	window.document.form1.submit();
}
function addkey(myString)
{
	window.document.form1.action="AddKey.do";

}
</script>
</head>



  <body bgcolor="white">
<form name="form1" method="POST" action="<%=request.getContextPath()%>/translate/complete/trans.do">

      <table width="100%" border="0" cellpadding="0" cellspacing="0" class="headerNav">
        <tr>
          <td class="headerNav" width="100%" bgcolor="#D8D8D8" align="left">
          <center><b>Add New Key</b></center><br><br><br>
          </td>
          <input type="hidden" name="method" value="addMessage">
          </tr>
	      <tr><td class="headerNav" width="50%" bgcolor="#D8D8D8" align="left">
	           Key:&nbsp;<input type="text" name="key_list" size="10">
			   </td>
	           </tr>
	           <tr>
         <td class="headerNav" width="50%" bgcolor="#D8D8D8" align="left">Message:&nbsp;<input type="text" name="message_text" size="10">
		 &nbsp;Locale &nbsp;:&nbsp;<select name="locale" style="FONT-SIZE: 7pt; FONT-FAMILY: Verdana, Tahoma, 'MS Sans Serif', Arial" onChange="document.form1.mykey.value='';document.form1.new_text.value='';document.form1.choice.value='';document.form1.src_locale.value=this.options[this.selectedIndex].value;document.form1.multi.value='0';document.form1.buttontype.value='0';document.form1.submit()">
		    <logic:iterate id="lan" name="lang" type="org.digijava.kernel.entity.Locale">
			<option value="<bean:write name='lan' property='code'/>"><bean:write name="lan" property="name"/></option>
			</logic:iterate>
	  </select>



			      </td></tr>
			   <tr>
			     <td class="headerNav" width="100%" bgcolor="#D8D8D8" align="center">
        	          	<input type="submit" name="add_button" value="Add New Key">
				</td>
			   </tr>

</table>
    </form>
  </body>
</html>
