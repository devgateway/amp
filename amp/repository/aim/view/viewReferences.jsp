
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<DIV id="TipLayer"
	style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>

<script type="text/javascript">

function fnEditProject(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
   document.location.href="<%=addUrl%>?pageId=1&action=edit&step=1&surveyFlag=true&activityId=" + id;

}

function previewLogframe(id)
{

    <digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
	var url ="<%=addUrl%>~pageId=1~step=1~action=edit~surveyFlag=true~logframepr=true~activityId=" + id + "~actId=" + id;
	openURLinWindow(url,650,500);

}

function projectFiche(id)
{
	<digi:context name="ficheUrl" property="context/module/moduleinstance/projectFicheExport.do" />
	var url ="<%=ficheUrl%>~ampActivityId=" + id;
	openURLinWindow(url,650,500);
}

function preview(id)
{

  <digi:context name="addUrl" property="context/module/moduleinstance/viewActivityPreview.do" />
  var urlToGo = "<%=addUrl%>~pageId=2~activityId=" + id;
    document.location.href = urlToGo;

}


function login()
{
	<digi:context name="addUrl" property="context/module/moduleinstance/login.do" />
    document.aimReferenceForm.action = "<%=addUrl%>";
    document.aimReferenceForm.submit();
}
</script>


<digi:form action="/viewReferences.do" 	name="aimReferenceForm" type="org.digijava.module.aim.form.ReferenceForm" method="post">


<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
	<TR>
		<TD vAlign="top" align="center">
			<!-- contents -->
			<TABLE width="99%" cellSpacing=0 cellPadding=0 vAlign="top" border="0" align="center" bgcolor="#f4f4f4" class="box-border-nopadding">
				<TR>
					<TD bgcolor="#f4f4f4">
						<TABLE width="100%" cellSpacing=3 cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4" border="0">
							<TR bgColor=#f4f4f2>
      	      					<TD align=left>
									<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left" vAlign="top">
										<TR>
											<TD align="left">
								<SPAN class=crumb>
									<jsp:useBean id="urlReference" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlReference}" property="ampActivityId">
										<bean:write name="aimReferenceForm" property="ampActivityId"/>
									</c:set>
									<c:set target="${urlReference}" property="tabIndex" value="1"/>
									<c:set var="translation">
										<digi:trn key="aim:clickToViewReference">Click here to view Reference</digi:trn>
									</c:set>
									<digi:link href="/viewReferences.do" name="urlReference" styleClass="comment" title="${translation}" >
										<digi:trn key="aim:references">References</digi:trn>
									</digi:link>
									&nbsp;&gt;&nbsp;Overview
								</SPAN>
							</TD>
											<TD align="right">
												&nbsp;
											</TD>
											<TD align="right">
											<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
												<feature:display name="Preview Activity" module="Previews">
													<field:display feature="Preview Activity" name="Preview Button">
														<input type="button" value="<digi:trn key='btn:preview'>Preview</digi:trn>" class="dr-menu"
															onclick="preview(<c:out value="${aimReferenceForm.ampActivityId}"/>)">
													</field:display>
												</feature:display>
											</module:display>
											<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
												<feature:display name="Edit Activity" module="Previews">
													<field:display feature="Edit Activity" name="Edit Activity Button">
														<input type="button" value="<digi:trn key='btn:edit'>Edit</digi:trn>" class="dr-menu"
															onclick="fnEditProject(<c:out value="${aimReferenceForm.ampActivityId}"/>)">
													&nbsp;
													</field:display>
												</feature:display>
											</module:display>
											<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
												<feature:display name="Logframe" module="Previews">
													<field:display name="Logframe Preview Button" feature="Logframe" >
														<input type="button" value="Preview Logframe" class="dr-menu"	onclick="previewLogframe(<c:out value="${aimReferenceForm.ampActivityId}"/>)">
													</field:display>
												</feature:display>
											</module:display>
											<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
												<feature:display name="Project Fiche" module="Previews">
													<field:display name="Project Fiche Button" feature="Project Fiche" >
														<input type='button' value='<digi:trn key="aim:projectFiche">Project Fiche</digi:trn>' class='dr-menu'
															onclick='projectFiche(<c:out value="${aimReferenceForm.ampActivityId}"/>)'>
													</field:display>
												</feature:display>
											</module:display>

											</TD>


										</TR>
									</TABLE>
								</TD>
							</TR>
							<TR bgColor=#f4f4f2>
								<TD vAlign="top" align="center" width="100%">
								<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top"
									align="center" bgColor=#f4f4f2>
									<TR>
										<TD width="100%" bgcolor="#F4F4F2" height="17">
										<TABLE border="0" cellpadding="0" cellspacing="0"
											bgcolor="#F4F4F2" height="17">
											<TR bgcolor="#F4F4F2" height="17">
												<TD bgcolor="#C9C9C7" class="box-title">&nbsp;&nbsp;<digi:trn
													key="aim:references">References</digi:trn></TD>
												<TD><IMG src="../ampTemplate/images/corner-r.gif" width="17"
													height="17"></TD>
											</TR>
										</TABLE>
										</TD>
									</TR>
										<tr bgcolor="#ffffff">
												<td width="750" bgcolor="#F4F4F2" align="center" class="box-border-nopadding">
													<table border="0" width="100%">
													<tr valign="top" bgcolor="#DDDDDB" >
															<td >
																&nbsp;
																</td>
																<td>&nbsp;&nbsp;</td>
																<td>
																&nbsp;
																</td>
															</tr>
														<c:forEach items="${aimReferenceForm.referenceDocs}" var="refDoc" varStatus="loopstatus">
															<tr valign="top">
															<c:if test="${!empty refDoc.comment}">
																<td width="50%">
																	${refDoc.categoryValue}
																</td>
																<td>&nbsp;&nbsp;</td>
																<td width="50%">
																	${refDoc.comment}
																</td>
																</c:if>
															</tr>
														</c:forEach>
													</table>
												</td>
											</tr>
										</TD>
								</TR>
							</TABLE>
				<!-- end --></TD>
						</TR>
					<TR>
				<TD>&nbsp;</TD>
			</TR>
		</TABLE>
		</TD>
		</TR></TABLE></TD></TR></TABLE>
</digi:form>


