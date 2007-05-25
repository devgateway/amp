
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script language="JavaScript1.2" type="text/javascript" src='<digi:file src="module/common/js/bbcode.js"/>'></script>
<script language=JavaScript type=text/javascript>function fnOnPreview() {
      <digi:context name="previewNewsItem" property="context/module/moduleinstance/previewNewsItem.do" />
      document.newsItemForm.action = "<%= previewNewsItem%>?CreateOrEdit=editNewsItem";
      document.newsItemForm.submit();
  }
</script>
	<digi:errors/>
	<digi:form action="/editNewsItem.do" method="post"><link href="images/style.css" rel="stylesheet" type="text/css">
<!-- Preview -->
	<c:if test="${!empty newsItemForm.previewItem}">
	<c:if test="${newsItemForm.preview}">
	<c:set var="previewItem" value="${newsItemForm.previewItem}" /> 
	<div align="center">
		<table border="0" cellspacing="1" cellpadding="5" width="95%">
			<tr>
				<td colspan="5" align="left"><b>
					<digi:trn key="news:reviewNews">Please review the new item before publishing it:</digi:trn></b>
				</td>
			</tr>
			<tr bgcolor="#F0F3F7">
				<td valign="top" class="tlt" width="78"><b><digi:trn key="news:title1">Title</digi:trn></b>:
				</td>
				<td class="tlt" width="780"><strong><font size="5">
					<c:if test="${!empty previewItem.title}">
					<c:out value="${previewItem.title}" escapeXml="false" />	
					</c:if>
					</font></strong>
				</td>
			</tr>
			<tr bgcolor="#F0F3F7">
				<td valign="top" class="tlt" width="78"><b>
					<digi:trn key="news:description">Description:</digi:trn></b>
				</td>
				<td width="780">
					<c:if test="${!empty previewItem.description}">
					<c:out value="${previewItem.description}" escapeXml="false" />	
					</c:if>				
				</td>
			</tr>
			<tr bgcolor="#F0F3F7">
				<td valign="top" class="tlt" width="95"><b>
					<digi:trn key="news:source">Source:</digi:trn></b>
				</td>
				<td width="780">
					<c:out value="${previewItem.sourceName}" /><br><a href='<c:out value="${previewItem.sourceUrl}" />'>
					<c:out value="${newsItemForm.sourceUrl}"/></a>
				</td>
			</tr>
			<tr bgcolor="#F0F3F7">
				<td valign="top" class="tlt" width="95"><b>
					<digi:trn key="news:language">Language:</digi:trn></b>
				</td>
				<td width="780">
					<c:if test="${!empty previewItem.language}">
					<bean:define id="languageKey" name="previewItem" property="languageKey" type="java.lang.String"/>
					<digi:trn key="<%=languageKey%>"><%=languageKey%></digi:trn>
					</c:if>
				</td>
			</tr>
			<tr bgcolor="#F0F3F7">
				<td valign="top" class="tlt" width="95"><b>
					<digi:trn key="news:country">Country:</digi:trn></b>
				</td>
				<td width="781">
					<c:if test="${!empty previewItem.country}">
					<bean:define id="countryKey" name="previewItem" property="countryKey" type="java.lang.String"/>
					<digi:trn key="<%=countryKey%>"><bean:write name="previewItem" property="countryName" /></digi:trn>
					</c:if>
				</td>
			</tr>
			<tr bgcolor="#F0F3F7">
				<td valign="top" class="tlt" width="95"><b>
					<digi:trn key="news:releaseDate">Release date:</digi:trn></b>
				</td>
				<td width="780">
					<c:out value="${previewItem.releaseDate}"/>
				</td>
			</tr>
			<tr bgcolor="#F0F3F7">
				<td valign="top" class="tlt" width="95"><b>
					<digi:trn key="news:archiveDate">Archive date:</digi:trn></b>
				</td>
				<td width="780">
					<c:out value="${previewItem.archiveDate}"/>
				</td>
			</tr>
			<tr bgcolor="#F0F3F7">
				<td valign="top" class="tlt" width="95"><b>
					<digi:trn key="news:author">Author:</digi:trn></b>
				</td>
				<td width="780">
					<digi:context name="digiContext" property="context"/><a href='<%= digiContext %>/um/user/showUserProfile.do?activeUserId=<bean:write name="previewItem" property="authorUserId" />' onclick="window.open(this.href, 'users', 'HEIGHT=500,resizable=yes,scrollbars=yes,WIDTH=500');return false;" target="user" paramId="id" paramName="index">
					<c:out value="${previewItem.authorFirstNames}"/>
					<c:out value="${previewItem.authorLastName}"/></a>
				</td>
			</tr>
			<tr>
				<td valign="top" width="95">	&nbsp;
				</td>
				<td width="780">
					<html:submit value="Update" onclick="this.disabled = true; form.submit()" />
					<input type="button" value="Edit" onclick="history.back();return false;">
					<html:hidden name="newsItemForm" property="activeNewsItem"/>
					<html:hidden name="newsItemForm" property="returnUrl"/>
					<html:hidden name="newsItemForm" property="sourceName"/>
					<html:hidden name="newsItemForm" property="sourceUrl"/>
					<html:hidden name="newsItemForm" property="title"/>
					<html:hidden name="newsItemForm" property="description"/>
					<html:hidden name="newsItemForm" property="selectedLanguage"/>
					<html:hidden name="newsItemForm" property="country"/>
					<html:hidden name="newsItemForm" property="releaseMonth"/>
					<html:hidden name="newsItemForm" property="releaseDay"/>
					<html:hidden name="newsItemForm" property="releaseYear"/>
					<html:hidden name="newsItemForm" property="archiveMonth"/>
					<html:hidden name="newsItemForm" property="archiveDay"/>
					<html:hidden name="newsItemForm" property="archiveYear"/>
					<html:hidden name="newsItemForm" property="enableHTML"/>
					<html:hidden name="newsItemForm" property="enableSmiles"/>
					<html:hidden name="newsItemForm" property="neverArchive"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<HR>
				</td>
			</tr>
		</table>
	</div></c:if>
	</c:if>
	<!-- end preview -->
	<c:if test="${!newsItemForm.preview}" >
    <div align="center">
	<table width="80%">
  	 <digi:secure authenticated="true">
		<tr>
	  	  <td width="50%" align>
			<digi:link href="/viewAllNews.do?status=mall">
			<digi:trn key="news:viewMyNews">View my news</digi:trn></digi:link>
		   </td>
		   <digi:secure actions="ADMIN">
			<td width="50%" align="right">
			 <digi:link href="/showNewsItems.do?status=pe">
			 <digi:trn key="news:administer">Administer</digi:trn></digi:link>
			</td></digi:secure>
		</tr>
		<tr>
			<td width="50%">
		  	 <digi:link href="/viewAllNews.do?status=mpe">
		     <digi:trn key="news:viewMyPeNews">View my pending news</digi:trn></digi:link>
			</td>
			<digi:secure actions="ADMIN">
			<td width="50%" align="right">
	 		 <digi:link href="/showNewsItemSettings.do">
			 <digi:trn key="news:settings">Settings</digi:trn></digi:link>
  		    </td></digi:secure>
		</tr>
		<tr>
			<td width="50%">
			 <digi:link href="/showCreateNewsItem.do">
			 <digi:trn key="news:addNewItem">Add New Item</digi:trn></digi:link>
			</td>
			<td width="50%">	&nbsp;
			</td>
		</tr></digi:secure>
   </table>
   <table width="80%">
      <tr><td><HR></td></tr>
   </table>
   </div>
	<TABLE border="0" align="center" cellPadding="2" cellSpacing="1" class="forumline" width="80%">
		<TBODY>
			<TR valign="middle" bgcolor="#507EA9">
				<TD height="25" colspan="4" width="510">	&nbsp;
					<digi:img src="module/news/images/1blt.gif"/>
					<digi:img src="module/news/images/spacer.gif" width="1" height="1"/><b><font color="#FFFFFF">&nbsp;<font color="#E8F1F8">
					<digi:trn key="news:addEditNewsItem">Add/Edit News Item</digi:trn></font></font></b>
				</TD>
			</TR>
			<tr>
				<TD noWrap width="4" rowspan="8" background="<digi:file src=" images images/ " />" class="row1">	&nbsp;
				</TD>
				<TD noWrap width="110" class="row1">
					<div align="left">
						<SPAN class="gen"><FONT color="red">*</FONT><B>
						<digi:trn key="news:title">title:</digi:trn></B></SPAN>
					</div>
				</TD>
				<TD width="377" class="row2" noWrap align="left">
					<html:text name="newsItemForm" property="title" size="70"/>
				</TD>
				</TD>
				<TD width="4" rowspan="8" background="<digi:file src=" images images/ " />" class="row1">	&nbsp;
				</TD>
			</TR>
			<TR>
				<TD noWrap vAlign="top" class="row1" width="110">
					<TABLE cellSpacing="0" cellPadding="1" border="0">
						<TBODY>
							<TR>
								<TD noWrap align="">
									<SPAN class="gen"><B>
									<digi:trn key="news:description">description:</digi:trn></B></SPAN>
								</TD>
							</TR>
							<TR>
								<TD noWrap align="middle" vAlign="center" nowrap><BR>
									<TABLE cellSpacing="0" cellPadding="5" width="100" border="0">
										<TBODY>
											<TR align="middle">
												<TD class="gensmall" colSpan="4"><B>
													<digi:trn key="news:clickableSmiles">clickable smiles</digi:trn></B>
												</TD>
											</TR>
											<TR vAlign="center" align="middle">
												<TD><A href="javascript:emoticon(':D')">
													<digi:img title="Very Happy" alt="Very Happy" src="module/common/images/smiles/icon_biggrin.gif" border="0" width="15" height="15"/></A>
												</TD>
												<TD><A href="javascript:emoticon(':)')">
													<digi:img title="Smile" alt="Smile" src="module/common/images/smiles/icon_smile.gif" border="0" width="15" height="15"/></A>
												</TD>
												<TD><A href="javascript:emoticon(':(')">
													<digi:img title="Sad" alt="Sad" src="module/common/images/smiles/icon_sad.gif" border="0" width="15" height="15"/></A>
												</TD>
												<TD><A href="javascript:emoticon(':o')">
													<digi:img title="Surprised" alt="Surprised" src="module/common/images/smiles/icon_surprised.gif" border="0" width="15" height="15"/></A>
												</TD>
											</TR>
											<TR vAlign="center" align="middle">
												<TD><A href="javascript:emoticon(':shock:')">
													<digi:img title="Shocked" alt="Shocked" src="module/common/images/smiles/icon_eek.gif" border="0" width="15" height="15"/></A>
												</TD>
												<TD><A href="javascript:emoticon(':?')">
													<digi:img title="Confused" alt="Confused" src="module/common/images/smiles/icon_confused.gif" border="0" width="15" height="15"/></A>
												</TD>
												<TD><A href="javascript:emoticon('8)')">
													<digi:img title="Cool" alt="Cool" src="module/common/images/smiles/icon_cool.gif" border="0" width="15" height="15"/></A>
												</TD>
												<TD><A href="javascript:emoticon(':lol:')">
													<digi:img title="Laughing" alt="Laughing" src="module/common/images/smiles/icon_lol.gif" border="0" width="15" height="15"/></A>
												</TD>
											</TR>
											<TR vAlign="center" align="middle">
												<TD><A href="javascript:emoticon(':x')">
													<digi:img title="Mad" alt="Mad" src="module/common/images/smiles/icon_mad.gif" border="0" width="15" height="15"/></A>
												</TD>
												<TD><A href="javascript:emoticon(':P')">
													<digi:img title="Razz" alt="Razz" src="module/common/images/smiles/icon_razz.gif" border="0" width="15" height="15"/></A>
												</TD>
												<TD><A href="javascript:emoticon(':oops:')">
													<digi:img title="Embarassed" alt="Embarassed" src="module/common/images/smiles/icon_redface.gif" border="0" width="15" height="15"/></A>
												</TD>
												<TD><A href="javascript:emoticon(':cry:')">
													<digi:img title="Crying or Very sad" alt="Crying or Very sad" src="module/common/images/smiles/icon_cry.gif" border="0" width="15" height="15"/></A>
												</TD>
											</TR>
											<TR vAlign="center" align="middle">
												<TD><A href="javascript:emoticon(':evil:')">
													<digi:img title="Evil or Very Mad" alt="Evil or Very Mad" src="module/common/images/smiles/icon_evil.gif" border="0" width="15" height="15"/></A>
												</TD>
												<TD><A href="javascript:emoticon(':twisted:')">
													<digi:img title="Twisted Evil" alt="Twisted Evil" src="module/common/images/smiles/icon_twisted.gif" border="0" width="15" height="15"/></A>
												</TD>
												<TD><A href="javascript:emoticon(':roll:')">
													<digi:img title="Rolling Eyes" alt="Rolling Eyes" src="module/common/images/smiles/icon_rolleyes.gif" border="0" width="15" height="15"/></A>
												</TD>
												<TD><A href="javascript:emoticon(':wink:')">
													<digi:img title="Wink" alt="Wink" src="module/common/images/smiles/icon_wink.gif" border="0" width="15" height="15"/></A>
												</TD>
											</TR>
											<TR vAlign="center" align="middle">
												<TD><A href="javascript:emoticon(':!:')">
													<digi:img title="Exclamation" alt="Exclamation" src="module/common/images/smiles/icon_exclaim.gif" border="0" width="15" height="15"/></A>
												</TD>
												<TD><A href="javascript:emoticon(':?:')">
													<digi:img title="Question" alt="Question" src="module/common/images/smiles/icon_question.gif" border="0" width="15" height="15"/></A>
												</TD>
												<TD><A href="javascript:emoticon(':idea:')">
													<digi:img title="Idea" alt="Idea" src="module/common/images/smiles/icon_idea.gif" border="0" width="15" height="15"/></A>
												</TD>
												<TD><A href="javascript:emoticon(':arrow:')">
													<digi:img title="Arrow" alt="Arrow" src="module/common/images/smiles/icon_arrow.gif" border="0" width="15" height="15"/></A>
												</TD>
											</TR>
										</TBODY>
									</TABLE>
								</TD>
							</TR>
						</TBODY>
					</TABLE>
				</TD>
				<TD noWrap vAlign="top" class="row2" width="377">
					<SPAN class="gen">
					<TABLE cellSpacing="0" cellPadding="2" width="348" border="0" height="107">
						<TBODY>
							<TR vAlign="center" align="middle">
								<TD height="27" width="36">
									<SPAN class="genmed">
									<INPUT class="button" style="FONT-WEIGHT: bold; WIDTH: 30px" accessKey="b" onclick="bbstyle(0)" type="button" value=" B " name="addbbcode0"></SPAN>
								</TD>
								<TD height="27" width="36">
									<SPAN class="genmed">
									<INPUT class="button" style="WIDTH: 30px; FONT-STYLE: italic" accessKey="i" onclick="bbstyle(2)" type="button" value=" i " name="addbbcode2"></SPAN>
								</TD>
								<TD height="27" width="36">
									<SPAN class="genmed">
									<INPUT class="button" style="WIDTH: 30px; TEXT-DECORATION: underline" accessKey="u" onclick="bbstyle(4)" type="button" value=" u " name="addbbcode4"></SPAN>
								</TD>
								<TD height="27" width="61">
									<SPAN class="genmed">
									<INPUT class="button" style="WIDTH: 50px" accessKey="q" onclick="bbstyle(6)" type="button" value="Quote" name="addbbcode6"></SPAN>
								</TD>
								<TD height="27" width="49">
									<SPAN class="genmed">
									<INPUT class="button" style="WIDTH: 40px" accessKey="c" onclick="bbstyle(8)" type="button" value="Code" name="addbbcode8"></SPAN>
								</TD>
								<TD height="27" width="49">
									<SPAN class="genmed">
									<INPUT class="button" style="WIDTH: 40px" accessKey="l" onclick="bbstyle(10)" type="button" value="List" name="addbbcode10"></SPAN>
								</TD>
								<TD height="27" width="49">
									<SPAN class="genmed">
									<INPUT class="button" style="WIDTH: 40px" accessKey="o" onclick="bbstyle(12)" type="button" value="List=" name="addbbcode12"></SPAN>
								</TD>
								<TD height="27" width="49">
									<SPAN class="genmed">
									<INPUT class="button" style="WIDTH: 40px" accessKey="p" onclick="bbstyle(14)" type="button" value="Img" name="addbbcode14"></SPAN>
								</TD>
								<TD height="27" width="40">
									<SPAN class="genmed">
									<INPUT class="button" style="WIDTH: 40px; TEXT-DECORATION: underline" accessKey="w" onclick="bbstyle(16)" type="button" value="URL" name="addbbcode16"></SPAN>
								</TD>
							</TR>
							<TR>
								<TD colSpan="9" height="24" width="437">
									<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
										<TBODY>
											<TR>
												<TD noWrap>
													<SPAN class="genmed">&nbsp;
													<digi:trn key="news:fontColor">Font color:</digi:trn>
													<SELECT onMouseOver="helpline('s')" onChange="bbfontstyle('[color=' + this.form.addbbcode18.options[this.form.addbbcode18.selectedIndex].value + ']', '[/color]')" name="addbbcode18">
														<OPTION value="#444444" selected class="genmed" style="COLOR: black; BACKGROUND-COLOR: #fafafa">Font Color</OPTION>
														<OPTION class="genmed" style="COLOR: darkred; BACKGROUND-COLOR: #fafafa" value="darkred">Dark Red</OPTION>
														<OPTION class="genmed" style="COLOR: red; BACKGROUND-COLOR: #fafafa" value="red">Red</OPTION>
														<OPTION class="genmed" style="COLOR: orange; BACKGROUND-COLOR: #fafafa" value="orange">Orange</OPTION>
														<OPTION class="genmed" style="COLOR: brown; BACKGROUND-COLOR: #fafafa" value="brown">Brown</OPTION>
														<OPTION class="genmed" style="COLOR: yellow; BACKGROUND-COLOR: #fafafa" value="yellow">Yellow</OPTION>
														<OPTION class="genmed" style="COLOR: green; BACKGROUND-COLOR: #fafafa" value="green">Green</OPTION>
														<OPTION class="genmed" style="COLOR: olive; BACKGROUND-COLOR: #fafafa" value="olive">Olive</OPTION>
														<OPTION class="genmed" style="COLOR: cyan; BACKGROUND-COLOR: #fafafa" value="cyan">Cyan</OPTION>
														<OPTION class="genmed" style="COLOR: blue; BACKGROUND-COLOR: #fafafa" value="blue">Blue</OPTION>
														<OPTION class="genmed" style="COLOR: darkblue; BACKGROUND-COLOR: #fafafa" value="darkblue">Dark Blue</OPTION>
														<OPTION class="genmed" style="COLOR: indigo; BACKGROUND-COLOR: #fafafa" value="indigo">Indigo</OPTION>
														<OPTION class="genmed" style="COLOR: violet; BACKGROUND-COLOR: #fafafa" value="violet">Violet</OPTION>
														<OPTION class="genmed" style="COLOR: white; BACKGROUND-COLOR: #fafafa" value="white">White</OPTION>
														<OPTION class="genmed" style="COLOR: black; BACKGROUND-COLOR: #fafafa" value="black">Black</OPTION>
													</SELECT>	&nbsp;
													<digi:trn key="news:fontSize">Font size:</digi:trn>
													<SELECT onMouseOver="helpline('f')" onChange="bbfontstyle('[size=' + this.form.addbbcode20.options[this.form.addbbcode20.selectedIndex].value + ']', '[/size]')" name="addbbcode20">
														<OPTION class="genmed" value="7">Tiny</OPTION>
														<OPTION class="genmed" value="9">Small</OPTION>
														<OPTION class="genmed" value="12" selected>Normal</OPTION>
														<OPTION class="genmed" value="18">Large</OPTION>
														<OPTION class="genmed" value="24">Huge</OPTION>
													</SELECT></SPAN>
												</TD>
												<TD noWrap align="right">
													<SPAN class="gensmall"><A class="genmed" onMouseOver="helpline('a')" href="javascript:bbstyle(-1)">
													<digi:trn key="news:closeTags">Close Tags</digi:trn></A></SPAN>
												</TD>
											</TR>
										</TBODY>
									</TABLE>
								</TD>
							</TR>
							<TR>
								<TD colSpan="9" height="19" width="437">
									<SPAN class="gensmall">&nbsp;</SPAN>
								</TD>
							</TR>
							<TR>
								<TD colSpan="9" height="21" width="437">
									<SPAN class="gen">
									<html:textarea name="newsItemForm" property="description" rows="15" cols="70"/>
									<TABLE cellSpacing="0" cellPadding="1" border="0">
										<TBODY>
										    <c:if test="${!empty newsItemForm.shortVersionDelimiter}">
											<TR>
												<TD colspan=3>
												<small>
												   <digi:trn key="news:userCharacter">Use character</digi:trn> 
												   '<digi:trn key="news:delimiter">{delimiter}</digi:trn>' 
												   <digi:trn key="news:forShortVersion">for a short version as a delimiter</digi:trn>
												   .( '\<digi:trn key="news:delimiter">{delimiter}</digi:trn>' 
												   <digi:trn key="news:ifCharNeedsToBeDisplayed">if the character itself needs to be displayed</digi:trn> ).
												</small> 												
												</TD>
											</TR>
											</c:if>
											<TR>
												<TD height="21">
													<html:checkbox name="newsItemForm" property="enableHTML">
													<digi:trn key="news:enableHTML">Enable HTML</digi:trn></html:checkbox>
												</TD>
												<td>	&nbsp;&nbsp;&nbsp;
												</td>
												<TD>
													<html:checkbox name="newsItemForm" property="enableSmiles">
													<digi:trn key="news:enableSmiles">Enable Smiles</digi:trn></html:checkbox>
												</TD>
											</TR>
										</TBODY>
									</TABLE>
									<SPAN class="gen"></SPAN>
								</TD>
							</TR>
						</TBODY>
					</TABLE></SPAN>
				</TD>
			</TR>
			<TR>
				<TD noWrap vAlign="top" nowrap class="row1" width="110">
					<span class="gen"><b>
					<digi:trn key="news:language">Language:</digi:trn></b></span>
				</TD>
				<TD width="377" class="row2" align="left">
					<html:select property="selectedLanguage">
					<bean:define id="lid" name="newsItemForm" property="languages" type="java.util.Collection"/>
					<html:options collection="lid" property="code" labelProperty="name"/></html:select>
				</TD>
			</TR>
			<TR>
				<TD noWrap vAlign="top" nowrap class="row1" width="110">
					<span class="gen"><b>
					<digi:trn key="news:country">Country:</digi:trn></b></span>
				</TD>
				<TD width="377" class="row2" noWrap align="left">
					<html:select property="country">
					<bean:define id="countries" name="newsItemForm" property="countryResidence" type="java.util.Collection"/>
					<html:options collection="countries" property="iso" labelProperty="name"/></html:select>
				</TD>
			</TR>
			<TR>
				<TD noWrap vAlign="top" class="row1" width="110">
					<div align="left">
						<span class="gen"><b>
						<digi:trn key="news:sourceName">Source Name:</digi:trn></b></span>
					</div>
				</TD>
				<TD width="377" class="row2" noWrap align="left">
					<html:text name="newsItemForm" property="sourceName" size="70"/>
				</TD>
			</TD>
			</TR>
			<TR>
				<TD noWrap vAlign="top" class="row1" width="110">
					<div align="left">
						<span class="gen"><b>
						<digi:trn key="news:sourceUrl">Source URL:</digi:trn></b></span>
					</div>
				</TD>
				<TD noWrap width="377" class="row2" noWrap align="left">
					<html:text name="newsItemForm" property="sourceUrl" size="70"/><br>
					<span class="genmed"><small>&nbsp;
					<digi:trn key="news:urlDescription">The 
          URL should be protocol specified (http://, ftp://, https//,...)</digi:trn></small></span>
				</TD>
			</TR>
			<TR>
				<TD noWrap vAlign="top" class="row1" width="110">
					<div align="left">
						<span class="gen"><b>
						<digi:trn key="news:releaseDate">Release Date:</digi:trn></b></span>
					</div>
				</TD>
				<TD vAlign="top" class="row2" width="377">
					<html:select property="releaseMonth">
					<bean:define id="mid" name="newsItemForm" property="months" type="java.util.Collection"/>
					<html:options collection="mid" property="calendarId" labelProperty="calendarText"/></html:select>
					<html:select property="releaseDay">
					<bean:define id="did" name="newsItemForm" property="days" type="java.util.Collection"/>
					<html:options collection="did" property="calendarId" labelProperty="calendarText"/></html:select>
					<html:text name="newsItemForm" property="releaseYear" size="4"/>
				</TD>
			</TR>
			<TR>
				<TD noWrap vAlign="top" class="row1" width="110">
					<div align="left">
						<span class="gen"><b>
						<digi:trn key="news:archiveDate">Archive Date:</digi:trn></b></span>
					</div>
				</TD>
				<TD vAlign="top" class="row2" width="377">
					<html:select property="archiveMonth">
					<bean:define id="mid" name="newsItemForm" property="months" type="java.util.Collection"/>
					<html:options collection="mid" property="calendarId" labelProperty="calendarText"/></html:select>
					<html:select property="archiveDay">
					<bean:define id="did" name="newsItemForm" property="days" type="java.util.Collection"/>
					<html:options collection="did" property="calendarId" labelProperty="calendarText"/></html:select>
					<html:text name="newsItemForm" property="archiveYear" size="4"/><br>
					<html:checkbox name="newsItemForm" property="neverArchive">
					<span class="genmed">
					<digi:trn key="news:neverArchive"><b>never</b> 
          (show permanently)</digi:trn></span></html:checkbox>
				</TD>
			</TR>
			<TR>
				<TD vAlign="top" class="row1" width="4">	&nbsp;
				</TD>
				<TD class="row2" width="110">
					<SPAN class="gen"></SPAN>
				</TD>
			</TR>
			<TR>
				<TD height="28" colSpan="4" align="center" class="catBottom" width="510">
					<html:hidden property="activeNewsItem"/>
					<html:hidden property="returnUrl"/>
					<html:submit value="Preview" onclick="javascript:fnOnPreview()"/>
				</TD>
			</TR>
	</TABLE></c:if></digi:form>
<script>setForm(document.newsItemForm);
</script>