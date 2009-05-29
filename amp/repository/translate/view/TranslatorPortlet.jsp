<%@ page import="java.util.Locale"%>
<%@ page import="org.digijava.kernel.user.User"%>
<%@ page import="org.digijava.kernel.util.DgUtil"%>
<%@ page import="org.digijava.kernel.translator.form.TranslatorForm"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/digijava.tld" prefix="dgcs"%>


<script>
<dgcs:instance id="tForm" property="TranslatorForm"/>

function submitForm(){
	
	var key='<%=request.getParameter("key")%>';
	
	document.forms[0].key.value = key;

	document.forms[0].action = "translate/TranslatorNavigation.do";
	document.forms[0].method = "GET";


	document.forms[0].submit();

}

</script>

<table>

<%

	User user = (User)DgUtil.getCurrentUser(request);
%>


  <small><a href='<%=request.getContextPath()%><%=request.getParameter("back_url")%>'>Go back to where you were</a></small>


<p>

<font color=red>Please note:</font>It will take a moment for your
changes to propagate through the system.  Sometimes the delay may cause
this page to appear as it did before you hit the Submit button.
When this occurs, please wait a moment and then hit Refresh/Reload
on your browser to verify the change.

<p>


<%
String strA = request.getParameter("key");
//org.digijava.kernel.translator.form.TranslatorForm tf = new org.digijava.kernel.translator.form.TranslatorForm();
//session.setAttribute("sitedgmarketdefaultTranslatorForm",tf);
%>
<dgcs:form action="/Translator.do">
<table border="0" cellpadding="5" cellspacing="2" align="center">
<tr>
  <td bgcolor="#cecece" align="center" width="50%"><b>Target language:</b></td>
  <td bgcolor="#cecece" align="center"><b>Translation key:</b></td>
</tr>
<tr>
  <td>
  

<input type="hidden" name="back_url" value='<%=request.getParameter("back_url")%>'>


<%String strLinker="(Choose a different language)";%>

<html:hidden name="tForm" property="destLocale" value="<%=DgUtil.determineLanguage(request).getCode()%>"/>
			
			<%String val = "ln:" + DgUtil.determineLanguage(request).getCode();%>
			<dgcs:trn key="<%=val%>"> </dgcs:trn>&nbsp;<a href="javascript:submitForm();"><%=strLinker%></a>
			
			<input type="hidden" name="lang" value="<%=DgUtil.determineLanguage(request).getCode()%>"/>
			
			<!--<input type="hidden" name="locale" value="<bean:write name="tForm" property="destLocale"/>"/>
			<input type="hidden" name="language" value="<bean:write name="tForm" property="destLocale"/>"/>
			-->
	

  </td>

  <td><%=strA%></td>
</tr>

<html:hidden name="tForm" property="type"/>
<tr>
  <td bgcolor="#cecece" colspan="2"><b>Message to translate from English:</b></td>
</tr>
<%Locale locEn = new Locale("en","");

	    session.setAttribute("lang",locEn);%>
<tr>
  <td colspan="2"><dgcs:get key='<%=request.getParameter("key")%>' locale="en" type="<%=request.getParameter("type")%>"> </dgcs:get></td>
</tr>

<digi:errors/>
<tr>
  <td colspan="2">&nbsp;</td>
</tr>
<tr>
  <td bgcolor="#cecece" colspan="2"><b>Type in your translation:</b></td>
</tr>


	<%
		
		
		String sLocale;

		if(user.getUserLangPreferences() != null){
		
			if(user.getUserLangPreferences().getNavigationLanguage() != null){
			 	sLocale = user.getUserLangPreferences().getNavigationLanguage().getCode().substring(0,2);
			}else{
				sLocale="en";
			}
							
		}else{
			 sLocale="en";
		}

	    Locale loc = new Locale(sLocale.trim(),"");

	    session.setAttribute("lang",loc);



	%>
<tr>
   <td colspan="2"><textarea name="text" cols="80" rows="3"><dgcs:get key='<%=request.getParameter("key")%>' locale="<%=DgUtil.determineLanguage(request).getCode()%>" type="<%=request.getParameter("type")%>"> </dgcs:get></textarea></td>
</tr>
<tr>
  <td align="center" colspan="2"><html:submit value=" Submit "/></td>
</tr>
</table>
	<html:hidden property="key" value='<%=strA%>'/>
	<html:hidden property="mode" value="update"/>
	<html:hidden property="url" value=""/>

</dgcs:form>
</table>
