<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>



<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/common.js"/>"></script>

<script language="JavaScript">

<!--





		function editQuestion(qid2)

		{

			

			openNewWindow(500, 300);

			<digi:context name="editQuestion" property="context/ampModule/moduleinstance/parisIndicatorManager.do?create=edit" />

			document.aimParisIndicatorManagerForm.currUrl.value = "<%= editQuestion %>";

			document.aimParisIndicatorManagerForm.action = "<%= editQuestion %>&qid2="+qid2;

			document.aimParisIndicatorManagerForm.target = popupPointer.name;

			document.aimParisIndicatorManagerForm.submit();

		}

		function deleteQuestion(qid2)

		{

			if(confirm("Do you want to DELETE the Question ?"))

			{

				<digi:context name="deleteQuestion" property="context/ampModule/moduleinstance/parisIndicatorManager.do?create=delete" />

				document.aimParisIndicatorManagerForm.currUrl.value = "<%= deleteQuestion %>";

				document.aimParisIndicatorManagerForm.action = "<%= deleteQuestion %>&qid2="+qid2;

				document.aimParisIndicatorManagerForm.target = "_self";

				document.aimParisIndicatorManagerForm.submit();

			}

		}

		function addQuestion()

		{

			openNewWindow(500, 300);

			<digi:context name="addQuestion" property="context/ampModule/moduleinstance/parisIndicatorManager.do?create=new" />

			document.aimParisIndicatorManagerForm.currUrl.value = "<%= addQuestion %>";

			document.aimParisIndicatorManagerForm.action = "<%= addQuestion %>";

			document.aimParisIndicatorManagerForm.target = popupPointer.name;

			document.aimParisIndicatorManagerForm.submit();

		}

		function saveIndicator()

		{

			

			<digi:context name="editPIInd" property="context/ampModule/moduleinstance/parisIndicatorAdd.do?create=indi" />

			document.aimParisIndicatorManagerForm.currUrl.value = "<%= editPIInd%>";

			document.aimParisIndicatorManagerForm.action = "<%= editPIInd %>";

			document.aimParisIndicatorManagerForm.target = "_self";

			document.aimParisIndicatorManagerForm.submit();

			

		}



-->

</script>



<digi:errors/>



<digi:instance property="aimParisIndicatorManagerForm" />

<digi:form action="/parisIndicatorManager.do" method="post">

<input type="hidden" name="currUrl">

<input type="hidden" name="create">

<input type="hidden" name="indicator">



<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left">

<tr><td vAlign="top" align="left">

<jsp:include page="teamPagesHeader.jsp"  />

</td></tr>

<tr><td vAlign="top" align="left">

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772 vAlign="top" align="left">

	<tr>

		<td class=r-dotted-lg width=14>&nbsp;</td>

		<td align=left class=r-dotted-lg valign="top" width=750>

			<table cellPadding=5 cellspacing="0" width="100%">

				<tr>

					<td height=33><span class=crumb>

						<digi:link href="/admin.do" styleClass="comment">

						<digi:trn key="aim:AmpAdminHome">

						Admin Home

						</digi:trn>

						</digi:link>&nbsp;&gt;&nbsp;

						<c:set var="translation">

							<digi:trn key="aim:clickToViewPiIndicator">Click here to goto Paris Indicator</digi:trn>

						</c:set>

						<digi:link href="/parisIndicatorManager.do" styleClass="comment" title="${translation}" >

							<digi:trn key="aim:parisIndicatorManager">

							Paris Indicator Manager

							</digi:trn>

						</digi:link>&nbsp;&gt;&nbsp;

						<digi:trn key="aim:parisIndicatorEdit">

							Paris Indicator Edit

						</digi:trn>

					</td>

				</tr>

				<tr>

					<td height=16 valign="center" width=571><span class=subtitle-blue>Paris Indicator Manager</span>

					</td>

				</tr>

				<tr bgColor=#f4f4f2>

					<td bgColor=#f4f4f2>

						<digi:errors />

					</td>

				</tr>	

				<tr>

					<td noWrap width="100%" vAlign="top">

					<table width="100%" cellspacing="1" cellspacing="1">

					<tr><td noWrap width=600 vAlign="top">

						<table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="100%">

							<tr bgColor=#f4f4f2>

								<td vAlign="top" width="100%" valign="center">

									&nbsp; 

								</td>

							</tr>

							<tr bgColor=#f4f4f2>

								<td valign="top">

									<table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="90%" border="0">	

										<tr bgColor=#f4f4f2>

											<td bgColor=#f4f4f2>

												<table border="0" cellpadding="0" cellspacing="0" width="100%">

													<tr bgColor=#f4f4f2>

														<td bgColor=#c9c9c7 class=box-title height="20" align="center">

															Edit Indicator

														</td>

													</tr>

												</table>

											</td>

										</tr>

										<tr>

											<td bgColor=#ffffff class=box-border>

												<table border="0" cellPadding=3 cellspacing="1" width="100%" bgcolor="#dddddd">

													<tr>

														<td align="right" bgcolor="#f4f4f2">

															Indicator Code: 			

														</td>

														<td align="left" bgcolor="#f4f4f2">

															<html:text property="addNewIndicatorCode" /> 

														</td>

													</tr>

													<tr>

														<td align="right" bgcolor="#f4f4f2">

															Indicator Text:		

														</td>

														<td align="left" bgcolor="#f4f4f2">

															<html:textarea property="addNewIndicatorText" cols="45" rows="5"/>

														</td>

													</tr>	

													<tr>

														<td colspan="2" align="center">

															<html:submit  styleClass="dr-menu" property="submitButton"  onclick="saveIndicator()">
																<digi:trn key="btn:saveIndicator">Save Indicator</digi:trn> 
															</html:submit>
														</td>

													</tr>	

													<tr>

														<td align="right" width="150" bgcolor="#f4f4f2">

																Add A Question

														</td>

														<td align="left" bgcolor="#f4f4f2">

																<html:button  styleClass="dr-menu" property="submitButton"  onclick="addQuestion()">
																	<digi:trn key="btn:add">Add</digi:trn> 
																</html:button>
														</td>																

													</tr>

														

													

												

													<tr>

														<td colspan="2" align="center" bgcolor="#f4f4f2">

															<table width="98%" cellPadding=2 cellspacing="0" valign="top" align="center" 

															class="box-border-nopadding">

																<logic:iterate name="aimParisIndicatorManagerForm" property="formQuestion" id="quest" type="org.digijava.ampModule.aim.dbentity.AmpAhsurveyQuestion">

																		<tr>

																		<td align="left" noWrap>

																			<digi:img src="ampModule/aim/images/arrow-th-BABAB9.gif" width="16"/>

																			<%--<bean:write name="quest" property="questionNumber"/>--%>

																			<c:set var="translation">

																				<digi:trn key="aim:clickToEditQuestion">Click here to edit Question </digi:trn>

																			</c:set>

																			

																				<bean:write name="quest" property="questionText"/>



																		</td>

																		<td noWrap>

																			[ <a href="javascript:editQuestion('<bean:write name="quest" property="ampQuestionId" />')"> Edit </a> ]&nbsp;&nbsp;

																			[ <a href="javascript:deleteQuestion('<bean:write name="quest" property="ampQuestionId" />')"> Delete </a> ]

																		</td>

																	</tr>

																</logic:iterate>

															</table>										

														</td>

													</tr>													

													

												</table>

											</td>

										</tr>



									</table>

								</td>

							</tr>

							<tr><td bgColor=#f4f4f2>&nbsp;

								

							</td></tr>

						</table>

					</td>

					<td noWrap width="100%" vAlign="top">

						<table align="center" cellpadding="0" cellspacing="0" width="90%" border="0">	

							<tr>

								<td>

									<table cellpadding="0" cellspacing="0" width="10"0>

										<tr>

											<td bgColor=#c9c9c7 class=box-title>

												<digi:trn key="aim:otherLinks">

												Other links

												</digi:trn>

											</td>

											<td background="ampModule/aim/images/corner-r.gif" height="17" width=17>&nbsp;

												

											</td>

										</tr>

									</table>

								</td>

							</tr>

							<tr>

								<td bgColor=#ffffff class=box-border>

									<table cellPadding=5 cellspacing="1" width="100%">

										

										<%--<tr>

											<td>

												<digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10"/>

												<digi:link href="/updateRole.do">

												<digi:trn key="aim:addRole">

												Add Roles

												</digi:trn>

												</digi:link>

											</td>

										</tr>--%>

										<tr>

											<td class="inside">

												<digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10"/>

												<digi:link href="/admin.do">

												<digi:trn key="aim:AmpAdminHome">

												Admin Home

												</digi:trn>

												</digi:link>

											</td>

										</tr>

									</table>

								</td>

							</tr>

						</table>

					</td></tr>

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




