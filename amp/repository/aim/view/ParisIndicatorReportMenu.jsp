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

			alert("in add new indicator");

			openNewWindow(500, 300);

			<digi:context name="addIndicator" property="context/ampModule/moduleinstance/parisIndicatorManager.do?parisIndicator=add" />

			document.aimParisIndicatorManagerForm.currUrl.value = "<%= addIndicator %>";

			document.aimParisIndicatorManagerForm.action = "<%= addIndicator %>";

			//alert(document.aimParisIndicatorManagerForm.action);

			document.aimParisIndicatorManagerForm.target = popupPointer.name;

			document.aimParisIndicatorManagerForm.submit();

			//return true;

		}

		

	-->

</script>



<digi:instance property="aimParisIndicatorForm" />

<digi:form action="/ParisIndicatorReport1.do" method="post">

<input type="hidden" name="currUrl">

<jsp:include page="teamPagesHeader.jsp"  />



<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>

	<tr>

		<td class=r-dotted-lg width=14>&nbsp;</td>

		<td align=left class=r-dotted-lg valign="top" width=750>

			<table cellPadding=5 cellspacing="0" width="100%">

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

				<logic:iterate name="aimParisIndicatorForm" property="parisIndicatorsList" id="quest"

				type="org.digijava.ampModule.aim.helper.ParisIndicatorHelper">

				<tr>



				<!--	<td class=f-names noWrap>

											<digi:img src="ampModule/aim/images/arrow-th-BABAB9.gif" width="16"/>

											<c:set var="translation">

												<digi:trn key="aim:clickToViewParisIndicators">Click here to view Paris Indicator </digi:trn>

											</c:set>

											<digi:link href="/ParisIndicatorReport1.do" title="${translation}" >

											

												<bean:write name="quest" property="helperParisIndicator"/>

											

											</digi:link>

					</td>-->

					<td class=f-names noWrap>

							

							<bean:write name="quest" property="helperPihIndicId"/> &nbsp;&nbsp;

							<c:set var="translation">

								<digi:trn key="aim:clickToEditReport">Click here to Edit Report</digi:trn>

							</c:set>

							<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>

								<c:set target="${urlParams}" property="pid">

									<bean:write name="quest" property="helperPihIndicId" />

								</c:set>

								<c:set target="${urlParams}" property="report" value="edit" />

							 <digi:link href="/ParisIndicatorReport1.do" name="urlParams" title="${translation}" >

								

								<bean:write name="quest" property="helperParisIndicator"/>

								

							</digi:link> 



							

						</td>

						

					

				</tr>

				</logic:iterate>

			</table>

			<tr bgColor=#dddddb>														

		<td bgColor=#dddddb height="20" align="center" colspan="5"><B>

			<input styleClass="dr-menu" type="button" name="addBtn" value="Add a New Indicator" onclick="AddIndicator()">

		</td>

	</tr>

		</td>

		

	</tr>

	

</table>

</digi:form>



