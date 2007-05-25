<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:instance property="forumPageForm"/>

<link rel="stylesheet" href="<digi:file src="module/forum/css/forum.css"/>">
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/forum/scripts/forum.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/common/js/bbcode_fixed.js"/>"></script>

<script language="JavaScript">

	function saveThread(){
      <digi:context name="saveThread" property="context/module/moduleinstance/saveThread.do" />
      document.forumPageForm.action = "<%= saveThread %>";
      document.forumPageForm.submit();
	}

	function showPostPreview() {
      <digi:context name="showPostPreview" property="context/module/moduleinstance/showPostPreview.do" />
      document.forumPageForm.action = "<%= showPostPreview %>?postId=<bean:write name="forumPageForm" property="postId"/>";
      document.forumPageForm.submit();
	}

	function initForumEditor(){
		setForm (document.getElementsByName("forumPageForm")[0]);
		setEditField (document.getElementsByName("postContent")[0]);
	}
</script>

<digi:form action="/saveThread.do">
	<html:hidden name="forumPageForm" property="subsectionId"/>

<digi:errors property="forumGlobalError"/>

	<table class="forumListContainer" width="100%" cellpadding="3" cellspacing="0" border="1">
		<tr>
			<th colspan="2" align="center" class="groupHeader" width="70%" nowrap>
				<digi:trn key="forum:newTopic">New topic</digi:trn>
			</th>
		</tr>
		<tr>
			<td class="light" width="20%">
				<digi:trn key="forum:topicTitle">Topic title</digi:trn>
			</td>
			<td class="dark" width="80%">
				<html:text name="forumPageForm" property="threadTitle" styleClass="forumControls" size="70"/>
			</td>
		</tr>
		<html:hidden name="forumPageForm" property="postTitle"/>
		<html:hidden name="forumPageForm" property="unregisteredFullName"/>
		<html:hidden name="forumPageForm" property="unregisteredEmail"/>
		
				<%--<tr>
					<td class="light" width="20%">Post title
					</td>
					<td class="dark" width="80%">
						<html:text name="forumPageForm" property="postTitle" styleClass="forumControls" size="70"/>
					</td>
				</tr>--%>
				<tr>
					<td class="light" valign="top" width="20%">
						<TABLE cellSpacing="0" cellPadding="5" width="100" border="0">
							<TBODY>
								<TR align="middle">
									<TD class="gensmall" colSpan="4"><B>
										<digi:trn key="forum:clickableSmiles">clickable smiles</digi:trn></B>
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
					</td>
					<td class="dark" width="80%">
						<table width="1" border="0" cellpadding="0" cellspacing="3">
							<td>
								<INPUT class="forumButtons" style="FONT-WEIGHT: bold; WIDTH: 30px" accessKey="b" onClick="bbstyle(0)" type="button" value=" B " name="addbbcode0">
							</td>
							<td>
								<INPUT class="forumButtons" style="WIDTH: 30px; FONT-STYLE: italic" accessKey="i" onClick="bbstyle(2)" type="button" value=" i " name="addbbcode2">
							</td>
							<td>
								<INPUT class="forumButtons" style="WIDTH: 30px; TEXT-DECORATION: underline" accessKey="u" onClick="bbstyle(4)" type="button" value=" u " name="addbbcode4">
							</td>
							<td>
								<INPUT class="forumButtons" style="WIDTH: 50px" accessKey="q" onClick="bbstyle(6)" type="button" value="Quote" name="addbbcode6">
							</td>
							<td>
								<INPUT class="forumButtons" style="WIDTH: 40px" accessKey="c" onClick="bbstyle(8)" type="button" value="Code" name="addbbcode8">
							</td>
							<td>
								<INPUT class="forumButtons" style="WIDTH: 40px" accessKey="l" onClick="bbstyle(10)" type="button" value="List" name="addbbcode10">
							</td>
							<td>
								<INPUT class="forumButtons" style="WIDTH: 40px" accessKey="o" onClick="bbstyle(12)" type="button" value="List=" name="addbbcode12">
							</td>
							<td>
								<INPUT class="forumButtons" style="WIDTH: 40px" accessKey="p" onClick="bbstyle(14)" type="button" value="Img" name="addbbcode14">
							</td>
							<td>
								<INPUT class="forumButtons" style="WIDTH: 40px; TEXT-DECORATION: underline" accessKey="w" onClick="bbstyle(16)" type="button" value="URL" name="addbbcode16">
							</td>
				</tr>
			</table>
			<table width="1" border="0" cellpadding="0" cellspacing="3">
				<tr>
					<td><font class="smallInfoText"><digi:trn key="forum:color">Color</digi:trn>:</font>
					</td>
					<td>
						<SELECT onMouseOver="helpline('s')" onChange="bbfontstyle('[color=' + this.options[this.selectedIndex].value + ']', '[/color]')" name="addbbcode18">
							<OPTION value="#444444" selected style="COLOR: black; BACKGROUND-COLOR: #fafafa">Font Color</OPTION>
							<OPTION style="COLOR: darkred; BACKGROUND-COLOR: #fafafa" value="darkred">Dark Red</OPTION>
							<OPTION class="genmed" style="COLOR: red; BACKGROUND-COLOR: #fafafa" value="red">Red</OPTION>
							<OPTION class="genmed" style="COLOR: orange; BACKGROUND-COLOR: #fafafa" value="orange">Orange</OPTION>
							<OPTION class="genmed" style="COLOR: brown; BACKGROUND-COLOR: #fafafa" value="brown">Brown</OPTION>
							<OPTION class="genmed" style="COLOR: yellow; BACKGROUND-COLOR: #fafafa" value="yellow">Yellow</OPTION>
							<OPTION class="genmed" style="COLOR: green; BACKGROUND-COLOR: #fafafa" value="green">Green</OPTION>
							<OPTION class="genmed" style="COLOR: olive; BACKGROUND-COLOR: #fafafa" alue="olive">Olive</OPTION>
							<OPTION class="genmed" style="COLOR: cyan; BACKGROUND-COLOR: #fafafa" value="cyan">Cyan</OPTION>
							<OPTION class="genmed" style="COLOR: blue; BACKGROUND-COLOR: #fafafa" value="blue">Blue</OPTION>
							<OPTION class="genmed" style="COLOR: darkblue; BACKGROUND-COLOR: #fafafa" value="darkblue">Dark Blue</OPTION>
							<OPTION class="genmed" style="COLOR: indigo; BACKGROUND-COLOR: #fafafa" value="indigo">Indigo</OPTION>
							<OPTION class="genmed" style="COLOR: violet; BACKGROUND-COLOR: #fafafa" value="violet">Violet</OPTION>
							<OPTION class="genmed" style="COLOR: white; BACKGROUND-COLOR: #fafafa" value="white">White</OPTION>
							<OPTION class="genmed" style="COLOR: black; BACKGROUND-COLOR: #fafafa" value="black">Black</OPTION>
						</SELECT>
					</td>
					<td><font class="smallInfoText"><digi:trn key="forum:size">Size</digi:trn>:</font>
					</td>
					<td>
						<SELECT onMouseOver="helpline('f')" onChange="bbfontstyle('[size=' + this.form.addbbcode20.options[this.form.addbbcode20.selectedIndex].value + ']', '[/size]')" name="addbbcode20">
							<OPTION class="genmed" value="7">Tiny</OPTION>
							<OPTION class="genmed" value="9">Small</OPTION>
							<OPTION class="genmed" value="12" selected>Normal</OPTION>
							<OPTION class="genmed" value="18">Large</OPTION>
							<OPTION class="genmed" value="24">Huge</OPTION>
						</SELECT>
					</td>
				</tr>
			</table>
			<html:textarea rows="14" cols="70" styleClass="forumControls" name="forumPageForm" property="postContent"></html:textarea><br>
			<table width="100%" border="0" cellpadding="0" cellspacing="3">
				<tr>
					<td>
						<html:checkbox name="forumPageForm" property="enableEmotions"/>
						<digi:trn key="forum:enableEmotions">Enable emotions</digi:trn>
					</td>
				</tr>
				<tr>
					<td>
						<html:checkbox name="forumPageForm" property="allowHtml"/>
						<digi:trn key="forum:allowHTML">Allow HTML</digi:trn>
					</td>
				</tr>
				<tr>
					<td>
						<html:checkbox name="forumPageForm" property="notifyOnReply"/>
						<digi:trn key="forum:enableNotificationsOnReply">Enable notifications on reply</digi:trn>
					</td>
				</tr>
			</table>
		</td>
		</tr>
			<tr>
				<td colspan="2" class="sectionTitle" align="center">
						<input class="forumButtons"
							   type="button"
							   value="Preview"
							   onClick="showPostPreview()">
							   &nbsp;
						<input class="forumButtons"
							   type="button"
							   value="Create"
							   onClick="saveThread()">
				</td>
			</tr>
	</table>



<script language="JavaScript">
	initForumEditor();
</script>

</digi:form>