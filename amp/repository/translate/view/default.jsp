

<%@ page import="java.util.Locale"%>
<%@ page import="org.digijava.kernel.user.User"%>
<%@ page import="org.digijava.kernel.util.DgUtil"%>
<%@ page import="org.digijava.kernel.translator.form.TranslatorForm"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="dgcs"%>


<script>
<dgcs:instance id="tForm" property="TranslatorForm"/>

function submitForm(){
	
	var key='<%=request.getParameter("key")%>';
	var url= document.forms[0].back_url.value;
	var back_url = '<%=request.getRequestURL()%>';	
	document.forms[0].back_url.value = back_url;
	document.forms[0].url.value = url;
	document.forms[0].key.value = key;

	document.forms[0].action = "TranslatorNavigation.do";
	document.forms[0].method = "GET";


	document.forms[0].submit();

}

</script>

<table>

<%

	User user = (User)DgUtil.getCurrentUser(request);
					
	session.setAttribute("user_lang",user.getUserLangPreferences().getNavigationLanguage().getCode().substring(0,2));
%>

<%if((request.getParameter("back_url")).startsWith("TranslatorNavigation.do")){%>
	<small><a href='<%=request.getParameter("back_url")%>?back_url=Translator.do?key=<%=request.getParameter("key")%>'>Go back to where you were</a></small>

<%}else if(((request.getParameter("back_url")).indexOf("DoAidaSearch.do") != -1) || (((request.getParameter("back_url")).indexOf("DoTextSearch.do")) != -1)){%>
	<input type="hidden" name="back_url" value='AidaSearch.do'>
	<small><a href='AidaSearch.do'>Go back to where you were</a></small>
<%}else if(((request.getParameter("back_url")).indexOf("SearchRouter.do") != -1) || (((request.getParameter("back_url")).indexOf("DoTextSearch.do")) != -1)){%>
	<input type="hidden" name="back_url" value='AidaSearch.do'>
	<small><a href='AidaSearch.do'>Go back to where you were</a></small>
<%}else if((request.getParameter("back_url")).indexOf("DoHistoricalSearch.do") != -1){%>
	<input type="hidden" name="back_url" value='AidaHistoricalSearch.do'>
	<small><a href='AidaHistoricalSearch.do'>Go back to where you were</a></small>
<%}else if((request.getParameter("back_url")).indexOf("AiDAArchiveActivityShow.do") != -1){%>
	<input type="hidden" name="back_url" value='<%=request.getParameter("back_url")%>&sco_id=<%=request.getParameter("sco_id")%>&res_name=<%=request.getParameter("res_name")%>'>
	<small><a href='<%=request.getParameter("back_url")%>&sco_id=<%=request.getParameter("sco_id")%>&res_name=<%=request.getParameter("res_name")%>'>Go back to where you were</a></small>
<%}else if((request.getParameter("back_url")).indexOf("AiDAActivityShow.do") != -1){%>
	<input type="hidden" name="back_url" value='<%=request.getParameter("back_url")%>&sco_id=<%=request.getParameter("sco_id")%>&res_name=<%=request.getParameter("res_name")%>'>
	<small><a href='<%=request.getParameter("back_url")%>&sco_id=<%=request.getParameter("sco_id")%>&res_name=<%=request.getParameter("res_name")%>'>Go back to where you were</a></small>
<%}else if((request.getParameter("back_url")).indexOf("CountryIndex.do") != -1){%>
	<%if(request.getParameter("view") != null){%>
		<input type="hidden" name="back_url" value='<%=request.getParameter("back_url")%>&cnt=<%=request.getParameter("cnt")%>&view=<%=request.getParameter("view")%>'>
		<small><a href='<%=request.getParameter("back_url")%>&cnt=<%=request.getParameter("cnt")%>&view=<%=request.getParameter("view")%>'>Go back to where you were</a></small>
	<%}else{%>
		<input type="hidden" name="back_url" value='<%=request.getParameter("back_url")%>&cnt=<%=request.getParameter("cnt")%>'>
		<small><a href='<%=request.getParameter("back_url")%>&cnt=<%=request.getParameter("cnt")%>'>Go back to where you were</a></small>
	<%}%>
<%}else{%>
	<small><a href='<%=request.getParameter("back_url")%>'>Go back to where you were</a></small>
<%}%>

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

<%if((request.getParameter("back_url")).startsWith("TranslatorNavigation.do")){%>
	<input type="hidden" name="back_url" value='<%=request.getParameter("back_url")%>?back_url=Translator.do?key=<%=request.getParameter("key")%>'>
<%}else if(((request.getParameter("back_url")).indexOf("DoAidaSearch.do") != -1) || (((request.getParameter("back_url")).indexOf("DoTextSearch.do")) != -1)){%>
	<input type="hidden" name="back_url" value='AidaSearch.do'>

<%}else if(((request.getParameter("back_url")).indexOf("SearchRouter.do") != -1) || (((request.getParameter("back_url")).indexOf("DoTextSearch.do")) != -1)){%>
	<input type="hidden" name="back_url" value='AidaSearch.do'>

<%}else if((request.getParameter("back_url")).indexOf("DoHistoricalSearch.do") != -1){%>
	<input type="hidden" name="back_url" value='AidaHistoricalSearch.do'>

<%}else if((request.getParameter("back_url")).indexOf("AiDAArchiveActivityShow.do") != -1){%>
	<input type="hidden" name="back_url" value='<%=request.getParameter("back_url")%>&sco_id=<%=request.getParameter("sco_id")%>&res_name=<%=request.getParameter("res_name")%>'>

<%}else if((request.getParameter("back_url")).indexOf("AiDAActivityShow.do") != -1){%>
	<input type="hidden" name="back_url" value='<%=request.getParameter("back_url")%>&sco_id=<%=request.getParameter("sco_id")%>&res_name=<%=request.getParameter("res_name")%>'>

<%}else if((request.getParameter("back_url")).indexOf("CountryIndex.do") != -1){%>
	<%if(request.getParameter("view") != null){%>
		<input type="hidden" name="back_url" value='<%=request.getParameter("back_url")%>&cnt=<%=request.getParameter("cnt")%>&view=<%=request.getParameter("view")%>'>
	<%}else{%>
		<input type="hidden" name="back_url" value='<%=request.getParameter("back_url")%>&cnt=<%=request.getParameter("cnt")%>'>
	<%}%>
<%}else{%>
	<input type="hidden" name="back_url" value='<%=request.getParameter("back_url")%>'>
<%}%>

<%String strLinker="(Choose a different language)";%>


	
			<bean:write name="tForm" property="destLocale"/>&nbsp;<a href="javascript:submitForm();"><%=strLinker%></a>

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
  <td colspan="2"><bean:write name="tForm" property="messageEn"/></td>
</tr>

<tr>
  <td colspan="2">&nbsp;</td>
</tr>
<tr>
  <td bgcolor="#cecece" colspan="2"><b>Type in your translation:</b></td>
</tr>

	<%
		
		
		String sLocale;

		if(user.getUserLangPreferences().getNavigationLanguage().getCode().substring(0,2) != null){
		
			 sLocale = user.getUserLangPreferences().getNavigationLanguage().getCode().substring(0,2);
							
		}else{
			 sLocale="en";
		}

	    Locale loc = new Locale(sLocale.trim(),"");

	    session.setAttribute("lang",loc);



	%>
<tr>
	<td colspan="2"><textarea name="text" cols="80" rows="3"><bean:write name="tForm" property="messageEn"/></dgcs:trn></textarea></td>
</tr>
<tr>
  <td align="center" colspan="2"><html:submit value=" Submit "/></td>
</tr>
</table>
	<html:hidden property="key" value='<%=strA%>'/>
	<html:hidden property="mode" value="update"/>
	<html:hidden property="url" value=""/>
<%session.removeAttribute("user_lang");%>
</dgcs:form>
</table>
