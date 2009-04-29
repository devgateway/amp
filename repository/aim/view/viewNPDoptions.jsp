<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@taglib uri="/taglib/struts-html" prefix="html"%>
<%@taglib uri="/taglib/digijava" prefix="digi"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>

<digi:instance property="aimNPDForm"/>
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<table width="100%" cellSpacing="5" cellPadding="5" vAlign="top" border="0">
	<tr>
		<td valign="top">
			<table bgcolor="#f4f4f2" cellPadding="5" cellSpacing="5" width="100%" class="box-border-nopadding">
				<tr>
					<td align="left" vAlign="top">
						<table bgcolor="#f4f4f2" cellPadding="0" cellSpacing="0" width="100%" class="box-border-nopadding">
							<tr bgcolor="#006699">
								<td valign="center" width="100%" align ="center" class="textalb" height="20">
									<digi:trn key="aim:indGrid:indicatorsHeader">Indicators (Any and all)</digi:trn>
								</td>
							</tr>
							<tr>
								<td bgcolor="#ecf3fd">
									<c:forEach items="${aimNPDForm.indicators}" var="item">
										<html:multibox name="aimNPDForm" property="selIndicators" >
											${item.value}
										</html:multibox>
											${item.label}
											<br/>
									</c:forEach>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

<table width="100%" cellSpacing="5" cellPadding="5" vAlign="top" border="0">
	<tr>
		<td valign="top">
			<table bgcolor="#f4f4f2" cellPadding="5" cellSpacing="5" width="100%" class="box-border-nopadding">
				<tr>
					<td align="left" vAlign="top">
						<table bgcolor="#f4f4f2" cellPadding="0" cellSpacing="0" width="100%" class="box-border-nopadding">
							<tr bgcolor="#006699">
								<td valign="center" width="100%" align ="center" class="textalb" height="20">
									<digi:trn key="aim:indGrid:timeHeader">Time (Limit 5)</digi:trn>
								</td>
							</tr>
							<tr>
								<td bgcolor="#ecf3fd">
									<c:forEach var="year" items="${aimNPDForm.years}" end="4" varStatus="status">
										<c:forEach var="year" items="${aimNPDForm.years}" begin="${status.index}" step="5">
											<html:multibox name="aimNPDForm" property="selYears" onclick="return checkYearsRules()">
												${year.value}
											</html:multibox>
												${year.label}
												&nbsp;
												&nbsp;
												&nbsp;
										</c:forEach>
										<br/>
									</c:forEach>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<table width="100%" cellSpacing="5" cellPadding="5" vAlign="top" border="0">
	<tr>
		<td align="right">		
			<html:button styleClass="dr-dialogmenu" property="submitButton" onclick="doChanges();">
				<digi:trn key="aim:ok">OK</digi:trn>
			</html:button>
		</td>
		<td align="left">
			<html:button styleClass="dr-dialogmenu" property="submitButton" onclick="closeWindow();">
				<digi:trn key="aim:close">Close</digi:trn>
			</html:button>
		</td>
	</tr>
</table>