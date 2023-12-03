
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fmt" prefix="fmt" %>

<script language="JavaScript1.2" type="text/javascript" src='<digi:file src="ampModule/common/js/bbcode.js"/>'></script>
<script language=JavaScript type=text/javascript>function fnOnPreview() {
      <digi:context name="previewEvent" property="context/ampModule/moduleinstance/previewCalendarItem.do" />
      document.calendarItemForm.action = "<%= previewEvent%>?CreateOrEdit=editCalendarItem";
      document.calendarItemForm.submit();
  }
</script>
<digi:errors/>
<digi:form action="/editCalendarItem.do" method="post"><link href="images/style.css" rel="stylesheet" type="text/css">
	<table border="0" cellspacing="1" width="100%">
		<tr>
			<td nowrap width="100%">
				<table border="0" cellpadding="0" cellspacing="0" width="80%" height="44">
					<tr>
						<td width="25%" height="44">
							<div align="left">
								<table border="1" cellpadding="0" cellspacing="0" width="95%" height="40" bordercolor="steelblue">
									<tr>
										<td nowrap height="40" bgcolor="#006699">
											<p align="center">
											<digi:link href="/viewMonthEvents.do"><b><font color="#FFFFFF">
											<digi:trn key="calendar:monthView">Month View</digi:trn>&nbsp;</font></b></digi:link>
										</td>
									</tr>
								</table>
							</div>
						</td>
						<td width="25%" height="44">
							<table border="1" cellpadding="0" cellspacing="0" width="95%" height="40" bordercolor="steelblue">
								<tr>
									<td nowrap height="40" bgcolor="#006699">
										<p align="center">
										<digi:link href="/viewYearEvents.do"><b><font color="#FFFFFF">
										<digi:trn key="calendar:yearView">Year View</digi:trn></font></b></digi:link>
									</td>
								</tr>
							</table>
						</td>
						<td width="25%" height="44">
							<table border="1" cellpadding="0" cellspacing="0" width="95%" height="40" bordercolor="steelblue">
								<tr>
									<td nowrap height="40" bgcolor="#006699">
										<p align="center">
										<digi:link href="/viewListEvents.do"><b><font color="#FFFFFF">
										<digi:trn key="calendar:listView">List View</digi:trn></font></b></digi:link>
									</td>
								</tr>
							</table>
						</td>
						<td width="25%" height="44">
							<div align="left">
								<table border="1" cellpadding="0" cellspacing="0" width="95%" height="43" bordercolor="steelblue">
									<tr>
										<td nowrap height="40" bgcolor="steelblue">
											<p align="center">
											<digi:link href="/showCreateCalendarItem.do"><font color="#FFFFFF"><b>
											<digi:trn key="calendar:addEvent">Add Event</digi:trn></b></font></digi:link>
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td align="center">	<!-- Preview -->
				<logic:present name="calendarItemForm" property="previewItem">
				<TABLE border="5" width="100%" style="border-collapse: collapse" bordercolor="steelblue">
					<TR>
						<TD>
							<logic:equal name="calendarItemForm" property="preview" value="true">
							<bean:define id="previewItem" name="calendarItemForm" property="previewItem" type="org.digijava.ampModule.calendar.form.CalendarItemForm.EventInfo"/>
							<div align="center">
								<table border="0" cellspacing="0" cellpadding="5" width="90%">
									<tr>
										<td align="left" width="887"><strong>
											<digi:trn key="calendar:reviewEvent">Please review and confirm the following event:</digi:trn></strong>
										</td>
									</tr>
									<tr>
										<td class="tlt"><strong><font size="5">
											<logic:present name="previewItem" property="title">
											<bean:write name="previewItem" property="title" filter="false"/>
											</logic:present></font></strong>
										</td>
									</tr>
									<tr>
										<td>
											<HR>
										</td>
									</tr>
									<tr>
										<td class="tlt"><b>
											<digi:trn key="calendar:startDate">Start Date:</digi:trn>
											<c:out value="${previewItem.startDate}"/>
											<c:if test="${calendarItemForm.start.allDayEvent && !calendarItemForm.start.TBD}"><digi:trn key="calendar:allDayEvent">All Day Event</digi:trn></c:if>
											<html:hidden name="calendarItemForm" property="allStartDayEvent"/></b>
										</td>
									</tr>
									<tr>
										<td class="tlt"><b>
											<digi:trn key="calendar:endDate">End Date:</digi:trn>
											<c:out value="${previewItem.endDate}"/>
											<c:if test="${calendarItemForm.end.allDayEvent && !calendarItemForm.end.TBD}"><digi:trn key="calendar:allDayEvent">All Day Event</digi:trn></c:if>
											<html:hidden name="calendarItemForm" property="allEndDayEvent"/></b>
										</td>
									</tr>
									<tr>
										<td align="left">
											<logic:present name="previewItem" property="description">
											<bean:write name="previewItem" property="description" filter="false" />
											</logic:present>
										</td>
									</tr>
									<tr>
										<td class="tlt"><b>
											<digi:trn key="calendar:location">Location:</digi:trn></b>
											<bean:write name="previewItem" property="location"/>
										</td>
									</tr>
									<tr>
										<td class="tlt"><b>
											<digi:trn key="calendar:country">Country:</digi:trn>
											<logic:present name="previewItem" property="country">
											<bean:define id="countryKey" name="previewItem" property="countryKey" type="java.lang.String"/>
											<digi:trn key="<%=countryKey%>"><%=countryKey%></digi:trn></logic:present>
										</td>
									</tr>
									<tr>
										<td class="tlt"><b>

											<digi:trn key="calendar:source">Source:</digi:trn></b>&nbsp;<a href='<bean:write name="previewItem" property="sourceUrl" />'>
											   <bean:write name="previewItem" property="sourceName" />
											</a>
										</td>
									</tr>
									<tr>
										<td width="781" align="left">
											<html:submit value="Update" onclick="this.disabled = true; form.submit()"/>
											<input type="button" value="Edit" onclick="history.back();return false;">
											<html:hidden name="calendarItemForm" property="activeCalendarItem"/>
											<html:hidden name="calendarItemForm" property="returnUrl"/>
											<html:hidden name="calendarItemForm" property="title"/>
											<html:hidden name="calendarItemForm" property="startDay"/>
											<html:hidden name="calendarItemForm" property="startMonth"/>
											<html:hidden name="calendarItemForm" property="startYear"/>
											<html:hidden name="calendarItemForm" property="startHour"/>
											<html:hidden name="calendarItemForm" property="startMin"/>
											<html:hidden name="calendarItemForm" property="allStartDayEvent"/>
											<html:hidden name="calendarItemForm" property="endDay"/>
											<html:hidden name="calendarItemForm" property="endMonth"/>
											<html:hidden name="calendarItemForm" property="endYear"/>
											<html:hidden name="calendarItemForm" property="endHour"/>
											<html:hidden name="calendarItemForm" property="endMin"/>
											<html:hidden name="calendarItemForm" property="allEndDayEvent"/>
											<html:hidden name="calendarItemForm" property="location"/>
											<html:hidden name="calendarItemForm" property="country"/>
											<html:hidden name="calendarItemForm" property="description"/>
											<html:hidden name="calendarItemForm" property="sourceName"/>
											<html:hidden name="calendarItemForm" property="sourceUrl"/>
											<html:hidden name="calendarItemForm" property="enableSmiles"/>
											<html:hidden name="calendarItemForm" property="enableHTML"/>
										</td>
									</tr>
									<tr>
										<td>
											<HR>
										</td>
									</tr></logic:equal>
								</table>
							</div></logic:present>
				</TABLE>	<!-- end preview -->
				<logic:notEqual name="calendarItemForm" property="preview" value="true">
				<TABLE border="5" width="100%" style="border-collapse: collapse" bordercolor="steelblue">
					<TR>
						<TD>
							<TABLE border="0" align="center" cellPadding="2" cellSpacing="1" class="forumline" width="60%">
								<TBODY>
									<TR valign="middle" bgcolor="#507EA9">
										<TD height="25" colspan="4" width="510">	&nbsp;
											<digi:img src="ampModule/calendar/images/1blt.gif"/>
											<digi:img src="ampModule/calendar/images/spacer.gif" width="1" height="1"/><b><font color="#FFFFFF">&nbsp;<font color="#E8F1F8">
											<digi:trn key="calendar:addEditEvent">Add/Edit Event</digi:trn></font></font></b>
										</TD>
									</TR>
									<tr>
										<TD width="110" height="19">
											<div align="left">
												<SPAN class="gen"><FONT color="red">*</FONT><B>
												<digi:trn key="calendar:title">Title</digi:trn>:</B></SPAN>
											</div>
										</TD>
										<TD width="597" noWrap align="left" height="19">
											<html:text name="calendarItemForm" property="title" size="40"/>
										</TD>
						</TD>
					</TR>
					<tr>
						<TD width="110" height="19">	&nbsp;
						</TD>
						<TD width="597" noWrap align="left" height="19">	&nbsp;
						</TD>
					</TR>
					<tr>
						<TD>
							<span class="gen"><b>
							<digi:trn key="calendar:startDate">Start Date:</digi:trn></b></span>
						</TD>
						<TD noWrap align="left">
							<html:select property="startDay">
							<bean:define id="did" name="calendarItemForm" property="days" type="java.util.Collection"/>
							<html:option value="">TBD</html:option>							
							<html:options collection="did" property="calendarId" labelProperty="calendarText"/></html:select>
							<html:select property="startMonth">
							<bean:define id="mid" name="calendarItemForm" property="months" type="java.util.Collection"/>
							<html:options collection="mid" property="calendarId" labelProperty="calendarText"/></html:select>
							<html:select property="startYear">
							<bean:define id="yid" name="calendarItemForm" property="years" type="java.util.Collection"/>
							<html:options collection="yid" property="calendarId" labelProperty="calendarText"/></html:select>
							<html:radio name="calendarItemForm" property="allStartDayEvent" value="false"/>
							<digi:trn key="calendar:time">Time</digi:trn>
							<html:select property="startHour">
							<bean:define id="hid" name="calendarItemForm" property="hours" type="java.util.Collection"/>
							<html:options collection="hid" property="calendarId" labelProperty="calendarText"/></html:select>
							<html:select property="startMin">
							<bean:define id="minid" name="calendarItemForm" property="mins" type="java.util.Collection"/>
							<html:options collection="minid" property="calendarId" labelProperty="calendarText"/></html:select>
							<html:radio name="calendarItemForm" property="allStartDayEvent" value="true"/>
							<digi:trn key="calendar:allDayEvent">All Day Event</digi:trn>
						</TD>
					</TR>
					<tr>
						<TD>
							<span class="gen"><b>
							<digi:trn key="calendar:endDate">End Date:</digi:trn></b></span>
						</TD>
						<TD noWrap align="left">
							<html:select property="endDay">
							<bean:define id="did" name="calendarItemForm" property="days" type="java.util.Collection"/>
							<html:option value="">TBD</html:option>							
							<html:options collection="did" property="calendarId" labelProperty="calendarText"/></html:select>
							<html:select property="endMonth">
							<bean:define id="mid" name="calendarItemForm" property="months" type="java.util.Collection"/>
							<html:options collection="mid" property="calendarId" labelProperty="calendarText"/></html:select>
							<html:select property="endYear">
							<bean:define id="yid" name="calendarItemForm" property="years" type="java.util.Collection"/>
							<html:options collection="yid" property="calendarId" labelProperty="calendarText"/></html:select>
							<html:radio name="calendarItemForm" property="allEndDayEvent" value="false"/>
							<digi:trn key="calendar:time">Time</digi:trn>
							<html:select property="endHour">
							<bean:define id="hid" name="calendarItemForm" property="hours" type="java.util.Collection"/>
							<html:options collection="hid" property="calendarId" labelProperty="calendarText"/></html:select>
							<html:select property="endMin">
							<bean:define id="minid" name="calendarItemForm" property="mins" type="java.util.Collection"/>
							<html:options collection="minid" property="calendarId" labelProperty="calendarText"/></html:select>
							<html:radio name="calendarItemForm" property="allEndDayEvent" value="true"/>
							<digi:trn key="calendar:allDayEvent">All Day Event</digi:trn>
						</TD>
					</TR>
					<tr>
						<TD width="110" height="19">
							<span class="gen"><FONT color="red">*</FONT><b>
							<digi:trn key="calendar:location">Location:</digi:trn></b></span>
						</TD>
						<TD width="597" noWrap align="left" height="19">
							<html:text name="calendarItemForm" property="location"/>
							<span class="gen"><b>
							<digi:trn key="calendar:country">Country:</digi:trn></b></span>
							<html:select property="country">
							<bean:define id="countries" name="calendarItemForm" property="countryResidence" type="java.util.Collection"/>
							<html:options collection="countries" property="iso" labelProperty="name"/></html:select>
						</td>
					</tr>
					<tr>
						<TD width="110" height="19">	&nbsp;
						</TD>
						<TD width="597" noWrap align="left" height="19">	&nbsp;
						</td>
					</tr>
					<TR>
						<TD vAlign="top" class="row1" width="110" height="140">
							<TABLE cellSpacing="0" cellPadding="1" border="0">
								<TBODY>
									<TR>
										<TD align="">
											<SPAN class="gen"><B>
											<digi:trn key="calendar:description">Description:</digi:trn></B></SPAN>
										</TD>
									</TR>
									<TR>
										<TD align="middle" vAlign="center" nowrap><BR>
											<TABLE cellSpacing="0" cellPadding="5" width="100" border="0">
												<TBODY>
													<TR align="middle">
														<TD class="gensmall" colSpan="4"><B>
															<digi:trn key="calendar:clickableSmiles">clickable smiles</digi:trn></B>
														</TD>
													</TR>
													<TR vAlign="center" align="middle">
														<TD><A href="javascript:emoticon(':D')">
															<digi:img title="Very Happy" alt="Very Happy" src="ampModule/common/images/smiles/icon_biggrin.gif" border="0" width="15" height="15"/></A>
														</TD>
														<TD><A href="javascript:emoticon(':)')">
															<digi:img title="Smile" alt="Smile" src="ampModule/common/images/smiles/icon_smile.gif" border="0" width="15" height="15"/></A>
														</TD>
														<TD><A href="javascript:emoticon(':(')">
															<digi:img title="Sad" alt="Sad" src="ampModule/common/images/smiles/icon_sad.gif" border="0" width="15" height="15"/></A>
														</TD>
														<TD><A href="javascript:emoticon(':o')">
															<digi:img title="Surprised" alt="Surprised" src="ampModule/common/images/smiles/icon_surprised.gif" border="0" width="15" height="15"/></A>
														</TD>
													</TR>
													<TR vAlign="center" align="middle">
														<TD><A href="javascript:emoticon(':shock:')">
															<digi:img title="Shocked" alt="Shocked" src="ampModule/common/images/smiles/icon_eek.gif" border="0" width="15" height="15"/></A>
														</TD>
														<TD><A href="javascript:emoticon(':?')">
															<digi:img title="Confused" alt="Confused" src="ampModule/common/images/smiles/icon_confused.gif" border="0" width="15" height="15"/></A>
														</TD>
														<TD><A href="javascript:emoticon('8)')">
															<digi:img title="Cool" alt="Cool" src="ampModule/common/images/smiles/icon_cool.gif" border="0" width="15" height="15"/></A>
														</TD>
														<TD><A href="javascript:emoticon(':lol:')">
															<digi:img title="Laughing" alt="Laughing" src="ampModule/common/images/smiles/icon_lol.gif" border="0" width="15" height="15"/></A>
														</TD>
													</TR>
													<TR vAlign="center" align="middle">
														<TD><A href="javascript:emoticon(':x')">
															<digi:img title="Mad" alt="Mad" src="ampModule/common/images/smiles/icon_mad.gif" border="0" width="15" height="15"/></A>
														</TD>
														<TD><A href="javascript:emoticon(':P')">
															<digi:img title="Razz" alt="Razz" src="ampModule/common/images/smiles/icon_razz.gif" border="0" width="15" height="15"/></A>
														</TD>
														<TD><A href="javascript:emoticon(':oops:')">
															<digi:img title="Embarassed" alt="Embarassed" src="ampModule/common/images/smiles/icon_redface.gif" border="0" width="15" height="15"/></A>
														</TD>
														<TD><A href="javascript:emoticon(':cry:')">
															<digi:img title="Crying or Very sad" alt="Crying or Very sad" src="ampModule/common/images/smiles/icon_cry.gif" border="0" width="15" height="15"/></A>
														</TD>
													</TR>
													<TR vAlign="center" align="middle">
														<TD><A href="javascript:emoticon(':evil:')">
															<digi:img title="Evil or Very Mad" alt="Evil or Very Mad" src="ampModule/common/images/smiles/icon_evil.gif" border="0" width="15" height="15"/></A>
														</TD>
														<TD><A href="javascript:emoticon(':twisted:')">
															<digi:img title="Twisted Evil" alt="Twisted Evil" src="ampModule/common/images/smiles/icon_twisted.gif" border="0" width="15" height="15"/></A>
														</TD>
														<TD><A href="javascript:emoticon(':roll:')">
															<digi:img title="Rolling Eyes" alt="Rolling Eyes" src="ampModule/common/images/smiles/icon_rolleyes.gif" border="0" width="15" height="15"/></A>
														</TD>
														<TD><A href="javascript:emoticon(':wink:')">
															<digi:img title="Wink" alt="Wink" src="ampModule/common/images/smiles/icon_wink.gif" border="0" width="15" height="15"/></A>
														</TD>
													</TR>
													<TR vAlign="center" align="middle">
														<TD><A href="javascript:emoticon(':!:')">
															<digi:img title="Exclamation" alt="Exclamation" src="ampModule/common/images/smiles/icon_exclaim.gif" border="0" width="15" height="15"/></A>
														</TD>
														<TD><A href="javascript:emoticon(':?:')">
															<digi:img title="Question" alt="Question" src="ampModule/common/images/smiles/icon_question.gif" border="0" width="15" height="15"/></A>
														</TD>
														<TD><A href="javascript:emoticon(':idea:')">
															<digi:img title="Idea" alt="Idea" src="ampModule/common/images/smiles/icon_idea.gif" border="0" width="15" height="15"/></A>
														</TD>
														<TD><A href="javascript:emoticon(':arrow:')">
															<digi:img title="Arrow" alt="Arrow" src="ampModule/common/images/smiles/icon_arrow.gif" border="0" width="15" height="15"/></A>
														</TD>
													</TR>
												</TBODY>
											</TABLE>
										</TD>
									</TR>
								</TBODY>
							</TABLE>
						</TD>
						<TD vAlign="top" align="left" width="597" height="140">
							<SPAN class="gen">
							<TABLE width="473">
								<TR>
									<TD vAlign="top" align="center" class="row2" width="467">
										<SPAN class="gen">
										<TABLE cellSpacing="0" cellPadding="2" width="429">
											<TBODY>
												<TR vAlign="center" align="middle">
													<TD height="27" width="44">
														<SPAN class="genmed">
														<INPUT class="button" style="FONT-WEIGHT: bold; WIDTH: 30px" accessKey="b" onclick="bbstyle(0)" type="button" value=" B " name="addbbcode0"></SPAN>
													</TD>
													<TD height="27" width="44">
														<SPAN class="genmed">
														<INPUT class="button" style="WIDTH: 30px; FONT-STYLE: italic" accessKey="i" onclick="bbstyle(2)" type="button" value=" i " name="addbbcode2"></SPAN>
													</TD>
													<TD height="27" width="44">
														<SPAN class="genmed">
														<INPUT class="button" style="WIDTH: 30px; TEXT-DECORATION: underline" accessKey="u" onclick="bbstyle(4)" type="button" value=" u " name="addbbcode4"></SPAN>
													</TD>
													<TD height="27" width="73">
														<SPAN class="genmed">
														<INPUT class="button" style="WIDTH: 50px" accessKey="q" onclick="bbstyle(6)" type="button" value="Quote" name="addbbcode6"></SPAN>
													</TD>
													<TD height="27" width="58">
														<SPAN class="genmed">
														<INPUT class="button" style="WIDTH: 40px" accessKey="c" onclick="bbstyle(8)" type="button" value="Code" name="addbbcode8"></SPAN>
													</TD>
													<TD height="27" width="58">
														<SPAN class="genmed">
														<INPUT class="button" style="WIDTH: 40px" accessKey="l" onclick="bbstyle(10)" type="button" value="List" name="addbbcode10"></SPAN>
													</TD>
													<TD height="27" width="57">
														<SPAN class="genmed">
														<INPUT class="button" style="WIDTH: 40px" accessKey="o" onclick="bbstyle(12)" type="button" value="List=" name="addbbcode12"></SPAN>
													</TD>
													<TD height="27" width="57">
														<SPAN class="genmed">
														<INPUT class="button" style="WIDTH: 40px" accessKey="p" onclick="bbstyle(14)" type="button" value="Img" name="addbbcode14"></SPAN>
													</TD>
													<TD height="27" width="59">
														<SPAN class="genmed">
														<INPUT class="button" style="WIDTH: 40px; TEXT-DECORATION: underline" accessKey="w" onclick="bbstyle(16)" type="button" value="URL" name="addbbcode16"></SPAN>
													</TD>
												</TR>
												<TR>
													<TD colSpan="9" height="24" width="526">
														<TABLE cellSpacing="0" cellPadding="0" border="0">
															<TBODY>
																<TR>
																	<TD nowrap>
																		<SPAN class="genmed">&nbsp;
																		<digi:trn key="calendar:fontColor">Font color:</digi:trn>
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
																		<digi:trn key="calendar:fontSize">Font size:</digi:trn>
																		<SELECT onMouseOver="helpline('f')" onChange="bbfontstyle('[size=' + this.form.addbbcode20.options[this.form.addbbcode20.selectedIndex].value + ']', '[/size]')" name="addbbcode20">
																			<OPTION class="genmed" value="7">Tiny</OPTION>
																			<OPTION class="genmed" value="9">Small</OPTION>
																			<OPTION class="genmed" value="12" selected>Normal</OPTION>
																			<OPTION class="genmed" value="18">Large</OPTION>
																			<OPTION class="genmed" value="24">Huge</OPTION>
																		</SELECT></SPAN>
																	</TD>
																	<TD noWrap align="center">
																		<SPAN class="gensmall"><A class="genmed" onMouseOver="helpline('a')" href="javascript:bbstyle(-1)">
																		<digi:trn key="calendar:closeTags">Close Tags</digi:trn></A></SPAN>
																	</TD>
																</TR>
															</TBODY>
														</TABLE>
													</TD>
												</TR>
												<TR>
													<TD colSpan="9" height="52" width="526">
														<SPAN class="gensmall">
														<html:textarea name="calendarItemForm" property="description" rows="15" cols="70"/></SPAN>
													</TD>
												</TR>
											</TBODY>
										</TABLE></SPAN>
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
					<TR>
						<TD vAlign="top" width="110" height="19">	&nbsp;
						</TD>
						<TD width="597" noWrap align="left" height="19">	&nbsp;
						</TD>
					</TR>
					<TR>
						<TD>
							<div align="left">
								<span class="gen"><b>
								<digi:trn key="calendar:source">Source</digi:trn></b></span>
							</div>
						</TD>
						<TD noWrap vAlign="top">
							<div align="left">
								<table width="226">
									<tr>
										<td noWrap vAlign="top" width="44">
											<div align="right">
												<span class="gen"><b>
												<digi:trn key="calendar:name">Name</digi:trn></b></span>
											</div>
										</td>
										<td noWrap vAlign="top" width="172">
											<html:text name="calendarItemForm" property="sourceName" size="50"/>
										</td>
									</tr>
								</table>
							</div>
						</TD>
					</TR>
					<TR>
						<TD vAlign="top">
							<div align="left">	&nbsp;
							</div>
						</TD>
						<TD noWrap vAlign="top">
							<div align="left">
								<table width="196">
									<tr>
										<td noWrap vAlign="top" width="44">
											<div align="right">
												<span class="gen"><b>
												<digi:trn key="calendar:URL">URL</digi:trn></b></span>
											</div>
										</td>
										<td noWrap vAlign="top" width="142">
											<html:text name="calendarItemForm" property="sourceUrl" size="50"/>
										</td>
									</tr>
								</table>
							</div>
						</TD>
					</TR>
					<TR>
						<TD width="110" height="25">
							<span class="gen"><b>
							<digi:trn key="calendar:options">Options</digi:trn></b></span>
						</TD>
						<TD vAlign="top" width="597" height="25">
							<table border="0" width="60%">
								<tr>
									<td>
										<html:checkbox name="calendarItemForm" property="enableSmiles">
										<digi:trn key="calendar:enableEmotions">Enable emotions</digi:trn></html:checkbox>
										<html:checkbox name="calendarItemForm" property="enableHTML">
										<digi:trn key="calendar:HTML">HTML</digi:trn></html:checkbox>
									</td>
								</tr>
							</table>
						</TD>
					</TR>
				</TABLE>
				<TABLE border="0" align="center">
					<tr>
						<td align="center" valign="bottom">
							<html:hidden name="calendarItemForm" property="activeCalendarItem"/>
							<html:hidden name="calendarItemForm" property="returnUrl"/>
							<html:submit value="Preview" onclick="javascript:fnOnPreview()"/>
						</td>
					</tr>
				</TABLE>
			</TD>
		</TR>
	</TABLE></logic:notEqual>
</TD>
</TR>
</TABLE>
</digi:form>
<script>setForm(document.calendarItemForm)
</script>