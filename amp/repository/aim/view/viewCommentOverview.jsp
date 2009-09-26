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
	function load() {
		document.getElementById("viewFlag").value="overview";
	}

	function unload() {
		window.opener.document.aimChannelOverviewForm.currUrl1.value="";
	}
-->
</script>

<digi:instance property="aimEditActivityForm" />
<digi:context name="digiContext" property="context"/>
<input type="hidden" name="viewFlag" id="viewFlag">


		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left vAlign=top>
					<table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<digi:trn key="aim:commentList">List of Comments</digi:trn>
							</td>
						</tr>
						<logic:empty name="aimEditActivityForm" property="comments.commentsCol">
							<tr>
								<td align=left vAlign=top><b><font color="#FF0000">
									<digi:trn key="aim:noCommentPresent">No comments found for this activity.</digi:trn></font></b>
								</td>
							</tr>
						</logic:empty>
						<logic:notEmpty name="aimEditActivityForm" property="comments.commentsCol">
						<tr>
							<td align="left" vAlign="top">						
							<table width="100%" cellPadding="3">
								<c:set value="1" var="sno" />
								<logic:iterate name="aimEditActivityForm" id="comment" property="comments.commentsCol" 
									type="org.digijava.module.aim.dbentity.AmpComments">
									
									
											<tr>
												<td bgcolor="#ECF3FD" width="5%">
													<b><c:out value="${sno}"/></b>
												</td>
												<td bgcolor="#ECF3FD" width="65%"><b>
													<digi:trn key="aim:commentBy">Comment By</digi:trn>:</b>&nbsp;
													<c:out value="${comment.memberId.user.firstNames}" />&nbsp;<c:out value="${comment.memberId.user.lastName}" />
												</td>
												<td bgcolor="#ECF3FD" width="30%"><b>
													<fmt:formatDate type="both" value="${comment.commentDate}" dateStyle="short" timeStyle="short"/></b>
												</td>
											</tr>
											<TR bgcolor="#f4f4f2">
												<TD colspan="3" width="90%">
													<TABLE width="90%" cellPadding="5" cellSpacing="1" vAlign="top" align="center" bgcolor="#ffffff">
														<TR>
															<TD width="100%">
																<c:out value="${comment.comment}" />
															</TD>
														</TR>
													</TABLE>
												</TD>
											</TR>
											<c:set value="${sno + 1}" var="sno"/>
										
									</logic:iterate>
										<tr>
											<td colspan="3" width="100%" align="center" height="20">
												<input type="button" value="Close"  class="dr-menu" onclick="window.close()">
											</td>
										</tr>
									</table>
							</td>
						</tr>
							</logic:notEmpty>
						
					</table>				
				</td>
			</tr>
		</table>
