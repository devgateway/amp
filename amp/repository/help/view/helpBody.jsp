<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<digi:instance property="helpForm" />

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td colspan="2">
		<table>
			<c:if test="${helpForm.blankPage==false}"> 
				<c:if test="${not empty helpForm.helpErrors}">
					<c:forEach var="error" items="${helpForm.helpErrors}">
						<tr>
							<td>
								<font color="red">${error}</font>
							</td>
						</tr>
					</c:forEach>
				</c:if>
			</c:if>			
		</table>
		</td>
	</tr>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td height=16 align="center" vAlign=center>
						<span class=subtitle-blue>
							<digi:trn key="help:selectedTpc">Selected Topic</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td>
						<table class="box-border-nopadding" width="100%" cellspacing="0" cellpadding="0" bgcolor="#ffffff">
							<tr bgcolor="#f4f4f2">
								<td>&nbsp;</td>
							</tr>
							<tr bgcolor="#f4f4f2">
								<td>
									<table width="99%" cellspacing="0" cellpadding="0" bgcolor="#f4f4f2" align="center">
										<tr>
											<td class="box-border" bgcolor="#ffffff" >
												<table class="box-border" width="100%" cellspacing="3" cellpadding="3">
													<tr bgcolor="#dddddb" >
														<td align="center">
															<c:if test="${empty helpForm.topicKey}">
																<b><digi:edit key="help:topic:default">no topic selected</digi:edit></b>
															</c:if>
															<c:if test="${not empty helpForm.topicKey}">
																<c:if test="${helpForm.topicKey!=''}">
																	<b>
																		<digi:trn key="${helpForm.titleTrnKey}" linkAlwaysVisible="true"></digi:trn>
																	</b>
																</c:if>
															</c:if>
														</td>
													</tr>
													<tr bgcolor="#ffffff">
														<td >
															<c:if test="${not empty helpForm.topicKey}">
																<c:if test="${helpForm.topicKey!=''}">
																	<digi:edit key="${helpForm.bodyEditKey}">no text preview</digi:edit>
																</c:if>
															</c:if>
														</td>
													</tr>
												</table>												
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr bgcolor="#f4f4f2">
								<td>&nbsp;</td>
							</tr>
						</table>
					</td>
				</tr>											
			</table>
		</td>			
	</tr>
</table>






												