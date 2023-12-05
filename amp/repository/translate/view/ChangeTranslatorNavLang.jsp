<%/**
 * <PRE>
 * <IMG SRC = imagesS\logo_us.gif></IMG>
 * </PRE>
 * File Name : ChangeTranslatorNavLang.jsp
 *
 * Purpose : This is where a user can change his/her language
 *
 * @version:    1.0
 *
 * @author
 * shamanth@mphasis.com    May 21, 2003
 *
 *
 * Copyright (c) 2001-2002 Joint Property of WorldBank and MphasiS-BFL
 *
 * MphasiS Limited
 * 139/1 Hosur Road, Koramangala
 * Bangalore 560 095
 *
 *
 *
 *
 * All Rights Reserved.
 *
 * <P> This software is the confidential and proprietary information of
 * WorldBank and MphasiS-BFL. You shall not disclose such confidential
 * Information and shall use it only in accordance with the terms of
 * the license agreement you entered into with WorldBank and MphasiS-BFL.
 *
 * REVISION HISTORY:
 * <PRE>
 * <U>Updated by:</U>
 * <U>Description:</U>
 * </PRE>
 */
%>


<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/dgcs.tld" prefix="dgcs" %>

<link href="css/global.css" rel="stylesheet" type="text/css">


<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr><td colspan="2"><IMG height="3" alt="" src="/images/trans.gif" width="1" border="0"></td></tr>
	<tr>
		<td><IMG height="1" alt="" src="/images/trans.gif" width="5" border="0"></td>
		<td align="left"><IMG height="1" alt="" src="/images/trans.gif" width="1" border="0"><br>
			<font size="+1" face="arial,sans-serif" color="#222222" class="tblackb"><dgcs:trn key="choose_lang:title"> </dgcs:trn></font><br><IMG height="3" alt="" src="/images/trans.gif" width="1" border="0"></td>
		<td>
	</tr>
</table>
<br>

<table width="600" cellspacing="0" cellpadding="5"><html:form action="TranslatorLocaleUpdate.do" method="GET">
<tr>
	<td><IMG height="1" alt="" src="/images/trans.gif" width="5" border="0"></td>
 	<td align="left" class="title" nowrap><dgcs:trn key="choose_lang:choose"> </dgcs:trn></td>
	<td align="left" class="title" nowrap width="300"><dgcs:trn key="choose_lang:select_content_lang"> </dgcs:trn></td>
</tr>
<tr>
	<td><IMG height="1" alt="" src="/images/trans.gif" width="5" border="0"></td>
	<td align="left" valign="top">
		<ul>


		   <logic:iterate id="x" name="TranslatorNavForm" property="locales">
				<html:radio idName="x" property="localesSelected" value="label"/><bean:write name="x" property="value"/><br>

			</logic:iterate>

			<%if((request.getParameter("back_url")).indexOf("Translator.do") != -1){%>
					<input type="hidden" name="back_url" value='<%=request.getParameter("back_url")%>?key=<%=request.getParameter("key")%>&back_url=<%=request.getParameter("url")%>'>
			<%}else if(((request.getParameter("back_url")).indexOf("DoAidaSearch.do") != -1) || (((request.getParameter("back_url")).indexOf("DoTextSearch.do")) != -1)){%>
					<input type="hidden" name="back_url" value='AidaSearch.do'>
			<%}else if(((request.getParameter("back_url")).indexOf("SearchRouter.do") != -1)){%>
					<input type="hidden" name="back_url" value='AidaSearch.do'>

			<%}else if((request.getParameter("back_url")).indexOf("DoHistoricalSearch.do") != -1){%>
					<input type="hidden" name="back_url" value='AidaHistoricalSearch.do'>

			<%}else if((request.getParameter("back_url")).indexOf("DoTextSearch.do") != -1){%>
					<input type="hidden" name="back_url" value='AidaSearch.do'>
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

			<!--&nbsp;<br>
			<span style="color:red">Attention</span>
			Lorem ipsum dolor sit amet, consetetur sadipscing elitr,  sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.</a>
			-->

        	</ul>
	</td>
	<td align="left" valign="top">
		<ul>

				<input type='checkbox' name='checkbox_locale' value='zhs' checked> Chinese</br>

				<input type='checkbox' name='checkbox_locale' value='us' checked> English</br>

				<input type='checkbox' name='checkbox_locale' value='e' checked> Espa�ol</br>

				<input type='checkbox' name='checkbox_locale' value='f' checked> Fran�ais</br>

				<input type='checkbox' name='checkbox_locale' value='el' checked> Greek</br>

				<input type='checkbox' name='checkbox_locale' value='hi' checked> Hindi</br>

				<input type='checkbox' name='checkbox_locale' value='ja' checked> Japanese</br>

				<input type='checkbox' name='checkbox_locale' value='ko' checked> Korean</br>

				<input type='checkbox' name='checkbox_locale' value='pt' checked> Portugu�s</br>

				<input type='checkbox' name='checkbox_locale' value='ru' checked> Russian</br>

				<input type='checkbox' name='checkbox_locale' value='tr' checked> Turkish</br>

        	</ul>
	</td>
</tr>
<!--if 267174 ne 0>
<tr>
	<td><IMG height="1" alt="" src="/images/trans.gif" width="5" border="0"></td>
	<td colspan="2" align="left" class="text">
		<input type="checkbox" name="save_in_profile" value="yes">Save this setting in My Gateway.
	</td>
</tr>
</if-->
<tr><td colspan="3"><IMG height="5" alt="" src="/images/trans.gif" width="1" border="0"></td></tr>
<tr>
	<td><IMG height="1" alt="" src="/images/trans.gif" width="5" border="0"></td>
	<td colspan="2" align="left">
		<p><font size=-1>
        	<b><dgcs:trn key="choose_lang:note1"> </dgcs:trn></b>&nbsp;<dgcs:trn key="choose_lang:note2"> </dgcs:trn><p>
        	</font>
	</td>
</tr>
<tr>
	<td><IMG height="1" alt="" src="/images/trans.gif" width="5" border="0"></td>
	<td colspan="2" align="left">
		<input type="hidden" name="return_url" value="/node/100647/?">
		<input type="submit" VALUE='<bean:message key="choose_lang:continue"/>'>

	</td>
</tr></html:form>
</table>