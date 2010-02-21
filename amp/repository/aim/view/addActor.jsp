<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>


<digi:instance  property="aimEditActivityForm" />

<digi:form name="addActorForm" type="aimEditActivityForm" action="/addActor.do" method="post" onsubmit="return validate()">


<script language="JavaScript">
<!--

	function validate() {
	var meas = document.getElementsByName("issues.actor")[0];
	if(isEmpty(meas.value) == true) {
			alert("Please enter the actor");
			meas.focus();
			return false;
		}
		return true;

	}

	function addActor() {
		var flag = validate();
		if (flag == true) {
			document.addActorForm.submit();
			return flag;
		} else {
			return false;
		}
	}

	function load() {}

	function unload() {}

	function clearField(){
		document.getElementsByName("issues.actor")[0].value="";
		return true;
	}

-->
</script>

<html:hidden property="issues.issueId" />
<html:hidden property="issues.measureId" />
<html:hidden property="issues.actorId" />
<html:hidden property="funding.event"/>
<input type="hidden" name="edit" value="true">
<html:hidden property="editAct"/>

<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border=0>
	<tr><td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left vAlign=top>
					<table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
					
						<tr>
							<td align="center" bgcolor=#ECF3FD>
								<table cellSpacing=2 cellPadding=2>
									<tr>
										<td valign="top">
											<digi:trn key="aim:actor">Actor</digi:trn>
										</td>
										<td valign="top">
											 <a title="<digi:trn key="aim:actorsForTheMeasures">Actors</digi:trn>">
												<html:textarea property="issues.actor" styleClass="inp-text" rows="3" cols="60" />
											 </a>
										</td>
									</tr>
									<tr>
										<td align="center" colspan=2>
											<table cellPadding=5>
												<tr>
													<td>
														<c:if test="${aimEditActivityForm.issues.actorId == -1}">
															<input type="submit" value="<digi:trn key='btn:addActor'>Add</digi:trn>" class="dr-menu">
														</c:if>
														
														<c:if test="${aimEditActivityForm.issues.actorId != -1}">
															<input type="submit" value="<digi:trn key='btn:updateActor'>Update</digi:trn>" class="dr-menu">
														</c:if>
													</td>
													<td>
														<input type="button" value="<digi:trn key='btn:clear'>Clear</digi:trn>" class="dr-menu" onclick="javascript:return clearField()">
													</td>
													
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</td></tr>
</table>
</digi:form>
