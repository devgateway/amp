<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<script language="javascript" type="text/javascript">
	function toggleDiv(id,state){
		if (state==true){
			document.getElementById('uncollapse'+id).style.display='block';
			document.getElementById('collapse'+id).style.display='none';
		}
		if (state==false){
			document.getElementById('collapse'+id).style.display='block';
			document.getElementById('uncollapse'+id).style.display='none';
		}
	}
</script>

<digi:instance property="helpForm" />

<TABLE align=center border=0 cellPadding=2 cellSpacing=3 width="100%" height="100%" bgcolor="#f4f4f2">
	<TR>
		<TD class=r-dotted-lg-buttom vAlign=top>
			<TABLE border=0 cellPadding=0 cellSpacing=0 width="100%" >
        		<TR><TD>
              		<TABLE border=0 cellPadding=0 cellSpacing=0 >
              			<TR bgColor=#f4f4f2>
                 			<TD bgColor=#c9c9c7 class=box-title>Help Topics</TD>
                    		<TD background="module/aim/images/corner-r.gif"	height=17 width=17></TD>
						</TR>
					</TABLE>
				</TD></TR>
				<TR><TD bgColor=#ffffff class=box-border align=left>
				<TABLE height="90%">
					<tr>
						<td>
							<c:if test="${ not empty helpForm.topicTree}">
								<c:if test="${not empty helpForm.parentId}">
									<c:if test="${helpForm.parentId!=''}">
										<c:set var="curTopicParentId">${helpForm.parentId}</c:set>						
									</c:if>
								</c:if>			
			
								<table>
									<c:forEach var="parent" items="${helpForm.topicTree}">
										<tr>
											<td nowrap>
												<c:if test="${curTopicParentId!=parent.helpTopicId}">						
													<div id="collapse${parent.helpTopicId}" style="display:block;">
														<table>
															<tr>
																<td valign="top" ><digi:img src="images/arrow_right.gif" onclick="javascript:toggleDiv(${parent.helpTopicId},true);"/></td>
																<td nowrap>
																	<a href="/help/helpActions.do?actionType=viewSelectedHelpTopic&topicKey=${parent.topicKey}">
																		<digi:trn key="${parent.titleTrnKey}"></digi:trn>
																	</a>
																</td>
															</tr>
														</table>
													</div>
							
													<div id="uncollapse${parent.helpTopicId}" style="display: none;">
														<table>
															<tr>
																<td valign="top"><digi:img src="images/arrow_down.gif" onclick="javascript:toggleDiv(${parent.helpTopicId},false);"/></td>
																<td nowrap><a href="/help/helpActions.do?actionType=viewSelectedHelpTopic&topicKey=${parent.topicKey}">
																		<digi:trn key="${parent.titleTrnKey}"></digi:trn>
																	</a>
																	<c:if test="${not empty parent.children}">
																		<table>
																			<c:forEach var="child" items="${parent.children}">
																				<tr>
																					<td nowrap>
																						<a href="/help/helpActions.do?actionType=viewSelectedHelpTopic&topicKey=${child.topicKey}">
																							<digi:trn key="${child.titleTrnKey}"></digi:trn>
																						</a>
																					</td>
																				</tr>
																			</c:forEach>
																		</table>
																	</c:if>
																</td>
															</tr>
														</table>
													</div>
													</c:if>
													<c:if test="${curTopicParentId==parent.helpTopicId}">						
													<div id="collapse${parent.helpTopicId}" style="display:none;">
														<table>
															<tr>
																<td valign="top"><digi:img src="images/arrow_right.gif" onclick="javascript:toggleDiv(${parent.helpTopicId},true);"/></td>
																<td nowrap><a href="/help/helpActions.do?actionType=viewSelectedHelpTopic&topicKey=${parent.topicKey}">
																		<digi:trn key="${parent.titleTrnKey}"></digi:trn>
																	</a>
																</td>
															</tr>
														</table>
													</div>
													
													<div id="uncollapse${parent.helpTopicId}" style="display: block;">
														<table>
															<tr>
																<td valign="top"><digi:img src="images/arrow_down.gif" onclick="javascript:toggleDiv(${parent.helpTopicId},false);"/></td>
																<td nowrap><a href="/help/helpActions.do?actionType=viewSelectedHelpTopic&topicKey=${parent.topicKey}">
																		<digi:trn key="${parent.titleTrnKey}"></digi:trn>
																	</a>
																	<c:if test="${not empty parent.children}">
																		<table>
																			<c:forEach var="child" items="${parent.children}">
																				<tr>
																					<td nowrap>
																						<a href="/help/helpActions.do?actionType=viewSelectedHelpTopic&topicKey=${child.topicKey}">
																							<digi:trn key="${child.titleTrnKey}"></digi:trn>
																						</a>
																					</td>
																				</tr>
																			</c:forEach>
																		</table>
																	</c:if>
																</td>
															</tr>
														</table>
													</div>
													</c:if>
												</td>
											</tr>
										</c:forEach>
									</table>
								</c:if>		
							</td>
						</tr>
					</table>	
				</TD></TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>


