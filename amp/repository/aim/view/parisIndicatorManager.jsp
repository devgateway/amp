<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>



<script language="JavaScript">

	<!--

		function AddIndicator()

		{

			openNewWindow(500, 300);

			<digi:context name="addIndicator" property="context/module/moduleinstance/parisIndicatorManager.do?parisIndicator=add" />

			document.aimParisIndicatorManagerForm.currUrl.value = "<%= addIndicator %>";

			document.aimParisIndicatorManagerForm.action = "<%= addIndicator %>";

			document.aimParisIndicatorManagerForm.target = popupPointer.name;

			document.aimParisIndicatorManagerForm.submit();

		}
		function deleteIndicator()
		{
			<c:set var="translation">
				<digi:trn key="aim:deleteIndicator">Do you want to delete the Indicator?</digi:trn>
			</c:set> 
			return confirm("${translation}"); 
		} 

	-->

</script>



<digi:instance property="aimParisIndicatorManagerForm" />

<digi:form action="/parisIndicatorManager.do" method="post">

<input type="hidden" name="currUrl">

<jsp:include page="teamPagesHeader.jsp"  />









<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=800 border="0">

	<tr>

		<td class=r-dotted-lg width=14>&nbsp;</td>

		<td align=left class=r-dotted-lg valign="top" width=788>

				<table cellPadding=5 cellspacing="0" width="100%" >

					<tr>

						<td height=33><span class=crumb>

							<c:set var="translation">

								<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>

							</c:set>

							<digi:link href="/admin.do" styleClass="comment" title="${translation}" >

							<digi:trn key="aim:AmpAdminHome">

								Admin Home

							</digi:trn>

							</digi:link>&nbsp;&gt;&nbsp;

							<digi:trn key="aim:parisIndicatorManager">

								Paris Indicator Manager

							</digi:trn>

						</td>

					</tr>

					

					<tr>

						<td height=16 valign="center" align="center"><span class=subtitle-blue>Paris Indicators Manager</span>

						</td>

					</tr>

					<tr>

					<table border="0">

					<tr>

						<td>

						<table border="1" cellspacing="1" cellpadding="0" bordercolor=#d7eafd>

								<tr bgColor=#d7eafd>

									<td class=f-names align="center" valign="center">

										Indicator Code:

									</td>

									<td class=f-names align="center" valign="center">

											Indicator Text

									</td>

									<!--<td class=f-names align="center" valign="center">

											Indicator Id

									</td>-->



									<td>

									&nbsp

									</td>



						

								</tr>									

								</tr>

								<logic:iterate name="aimParisIndicatorManagerForm" property="parisIndicatorName" id="parisIndicatorName"

								type="org.digijava.module.aim.dbentity.AmpAhsurveyIndicator	">

								<tr>



									<td class=f-names noWrap align="center" valign="center">

															<%--<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>--%>

															<c:set var="translation">

																<digi:trn key="aim:clickToViewParisIndicators">Click here to view Paris Indicator </digi:trn>

															</c:set>

															

															<%--<digi:link href="/parisIndicatorManager.do" title="${translation}" >

																<bean:write name="parisIndicatorName" property="ampIndicatorId"/>&nbsp;&nbsp;--%>

																<bean:write name="parisIndicatorName" property="indicatorCode"/>

									</td>

									<td class=f-names  valign="center">

																<bean:write name="parisIndicatorName" property="name"/>

																	

															<%--</digi:link>--%>

									</td>

									<%--<td class=f-names align="center" valign="center">

														<bean:write name="parisIndicatorName" property="ampIndicatorId"/>						

															<%--</digi:link>

									</td>--%>

									<td align="center" valign="center" noWrap>

											

											

											<c:set var="translation">

												<digi:trn key="aim:clickToEditReport">Click here to Edit Report</digi:trn>

											</c:set>

											<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>

												<c:set target="${urlParams}" property="pid">

													<bean:write name="parisIndicatorName" property="ampIndicatorId" />

												</c:set>

												<c:set target="${urlParams}" property="event" value="edit" />

											[ <digi:link href="/parisIndicatorManager.do" name="urlParams" title="${translation}" >

												<digi:trn key="aim:reportManagerEdit">Edit</digi:trn>

											</digi:link> ]

												<c:set target="${urlParams}" property="event" value="delete" />
											[ <digi:link href="/parisIndicatorManager.do" name="urlParams" title="${translation}" onclick="return deleteIndicator()">
												<digi:trn key="aim:reportManagerDelete">Delete</digi:trn>
											</digi:link> ]


				<%--							<bean:write name="parisIndicatorName" property="ampIndicatorId"/>--%>

										</td>

								

						

										

									

								</tr>

								</logic:iterate>

							

						</table>

						</td>

						<td>

								<table align="center" cellpadding="0" cellspacing="0" width="90%" border="0">	

							<tr>

								<td>

									<!-- Other Links -->

									<table cellpadding="0" cellspacing="0" width="120">

										<tr>

											<td bgColor=#c9c9c7 class=box-title>

												<digi:trn key="aim:otherLinks">

												Other links

												</digi:trn>

											</td>

											<td background="module/aim/images/corner-r.gif" height="17" width="17">

												&nbsp;

											</td>

										</tr>

									</table>

								</td>

							</tr>

							<tr align=top valign="top">

								<td bgColor=#ffffff class=box-border align=top valign="top" height="100%">

									<table cellPadding=5 cellspacing="1" width="100%" align=top valign="top">

										

										<tr align=top valign="top">

											<td align=top valign="top">

												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>

												<c:set var="translation">

													<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>

												</c:set>

												<digi:link href="/admin.do" title="${translation}" >

												<digi:trn key="aim:AmpAdminHome">

												Admin Home

												</digi:trn>

												</digi:link>

											</td>

										</tr>

										<tr align=top valign="top">

											<td align=top valign="top">

												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>

												<c:set var="translation">

													<digi:trn key="aim:AddIndicator"> Add Indicator</digi:trn>

												</c:set>

												<a href="javascript:AddIndicator()"/>

												<digi:trn key="aim:AddIndicator">

												Add Indicator

												</digi:trn>

											<%--</digi:link>--%>

											</td>

										</tr>

										<!-- end of other links -->

									</table>

						<td>

						</tr>

					<tr bgColor=#dddddb>														

						<%--<td bgColor=#dddddb height="20" align="center" colspan="5"><B>

							<input styleClass="dr-menu" type="button" name="addBtn" value="Add a New Indicator" onclick="AddIndicator()">

						</td>--%>

					</tr>

				</table>

			</td>

					

	</tr>



	

</table>



</digi:form>



