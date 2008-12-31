<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fmt" prefix="fmt" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript">
<!--
	function message(val1,val2) {
		var flag = true;
		val2 = val2 - 1;
		<digi:context name="url" property="context/module/moduleinstance/viewComment.do?edit=true" />
		if (val1 == "delete") {
			if(!confirm("Are you sure about deleting this comment?"))
				flag = false;
		}
		if (flag == true) {
			document.getElementById('actionFlag').value = val1;
			document.getElementById('ampCommentId').value = val2;
			document.aimEditActivityForm.action = "<%=url %>";
			document.aimEditActivityForm.target = "_self";
			document.aimEditActivityForm.submit();
		}
	}
	
	function check() {
		var str = document.getElementById('commentText').value;
		str = trim(str);
		document.getElementById('commentText').value = str;
		if (str.length == 0 || str == null) {
			alert("Please enter your comment.");
			document.getElementById('commentText').focus();
			return false;
		}
		else
			document.aimEditActivityForm.submit();
			window.close();

	}
	
	function trim ( inputStringTrim ) {
		fixedTrim = "";
		lastCh = " ";
		for (x=0; x < inputStringTrim.length; x++) {
			ch = inputStringTrim.charAt(x);
			if ((ch != " ") || (lastCh != " ")) { fixedTrim += ch; }
				lastCh = ch;
		}
		if (fixedTrim.charAt(fixedTrim.length - 1) == " ") {
			fixedTrim = fixedTrim.substring(0, fixedTrim.length - 1); }
		return fixedTrim;
	}
	
	function load() {
		var str = document.getElementById('actionFlag').value;
		if (str == "edit")
			document.getElementById('commentText').focus();
	}

	function unload() {
		//window.opener.document.aimEditActivityForm.currUrl1.value="";
	}
-->
</script>

<digi:instance property="aimEditActivityForm" />
<digi:context name="digiContext" property="context"/>

<digi:form action="/viewComment.do" method="post">
<html:hidden property="comments.actionFlag"  styleId="actionFlag"/>
<html:hidden property="comments.ampCommentId"  styleId="ampCommentId"/>
<html:hidden property="editAct" />

		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left vAlign=top>
					<table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<digi:trn key="aim:commentList">List of Comments</digi:trn>
							</td>
						</tr>
						<%--<tr>
							<td align="right">
								<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlParams1}" property="actionFlag" value="create" />
									[<digi:link href="/viewComment.do" name="urlParams1"><digi:trn key="aim:createComment">Add Comment</digi:trn></digi:link>]

							</td>
						</tr>--%>	
						<%-- <%=request.getLocale()%> --%>		
						<logic:notEmpty name="aimEditActivityForm" property="comments.commentsCol">
						<tr>
							<td align=left vAlign=top>						
							<table width="100%" cellPadding=3>
								<c:set value="1" var="sno" />
								<logic:iterate name="aimEditActivityForm" id="comment" property="comments.commentsCol" 
									type="org.digijava.module.aim.dbentity.AmpComments">
										<c:if test="${comment.ampFieldId.ampFieldId == aimEditActivityForm.comments.field.ampFieldId}"> 
											<tr>
												<td bgcolor=#ECF3FD width="5%">
													<b><c:out value="${sno}"/></b>
												</td>
												<td bgcolor=#ECF3FD width="65%"><b>
													<digi:trn key="aim:commentBy">Comment By</digi:trn>:</b>&nbsp;
													<c:out value="${comment.memberId.user.firstNames}" />&nbsp;<c:out value="${comment.memberId.user.lastName}" />
												</td>
												<td bgcolor=#ECF3FD width="30%"><b>
													<%--<bean:write name="comment" property="commentDate" format="dd/mm/yyyy" />--%>
													<fmt:formatDate type="both" value="${comment.commentDate}" dateStyle="short" timeStyle="short"/></b>
												</td>
											</tr>
											<TR bgcolor="#f4f4f2">
												<TD colspan="3" width="90%">
													<TABLE width="90%" cellPadding="5" cellSpacing="1" vAlign="top" align="center" bgcolor="#ffffff">
														<TR>
															<TD width="100%">
																<c:out value="${comment.comment}" />
																<c:if test="${comment.memberId.ampTeamMemId == currentMember.memberId}" >
																<br>
																	<%--<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																	<c:set target="${urlParams}" property="ampCommentId">
																		<bean:write name="comment" property="ampCommentId" />
																	</c:set>
																	<c:set target="${urlParams}" property="actionFlag" value="edit" />--%>
																[<a href="javascript:message('edit','<c:out value="${sno}" />')"><digi:trn key="aim:editComment">Edit</digi:trn></a>]
																	<%--<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
																	<c:set target="${urlParams2}" property="ampCommentId">
																		<bean:write name="comment" property="ampCommentId" />
																	</c:set>
																	<c:set target="${urlParams2}" property="actionFlag" value="delete" />--%>
																[<a href="javascript:message('delete','<c:out value="${sno}" />')"><digi:trn key="aim:deleteComment">Delete</digi:trn></a>]
																</c:if>
															</TD>
														</TR>
													</TABLE>
												</TD>
											</TR>
										</c:if>
										
										<c:set value="${sno + 1}" var="sno"/>
									</logic:iterate>
								</logic:notEmpty>
										<tr>
											<td colspan="3" width="100%" align="left">
												<c:if test="${aimEditActivityForm.comments.actionFlag == 'create'}"><digi:trn key="aim:addCommentText">Add your comment here</digi:trn></c:if>
												<c:if test="${aimEditActivityForm.comments.actionFlag == 'edit'}"><digi:trn key="aim:editCommentText">Edit your comment here</digi:trn></c:if>
												<br>
												<html:textarea name="aimEditActivityForm" property="comments.commentText" styleId="commentText" cols="60" rows="3" />
												<br>
													<input type="button" value="<digi:trn key='btn:save'>Save</digi:trn>"   class="dr-menu" onClick="check()">
													<input type="reset"  value="<digi:trn key='btn:clear'>Clear</digi:trn>"  class="dr-menu">
													<input type="button"  value="<digi:trn key='btn:back'>Back</digi:trn>"  class="dr-menu" onclick="history.back(-1)">
												<!--	<input type="button" value="<digi:trn key='btn:close'>Close</digi:trn>"  class="dr-menu" onclick="window.close()"> -->
											</td>
										</tr>
									</table>
							</td>
						</tr>
						
					</table>				
				</td>
			</tr>
		</table>
</digi:form>
