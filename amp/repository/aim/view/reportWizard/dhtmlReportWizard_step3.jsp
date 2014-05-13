<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
   
<%@page import="org.dgfoundation.amp.ar.ArConstants"%>

						<div id="hierarchies_step_div" class="main_side_cont yui-hidden" style="${topBottomPadding} ${hierarchiesVisibility}">
							<c:set var="stepNum" value="2" scope="request" />
							<c:if test="${!myForm.onePager}">
								<jsp:include page="toolbar.jsp" />
							</c:if>
							<br />
							<table align="center" cellpadding="0" cellspacing="0" style="width: 735px;" border=0>
								<tr>
								<td width="340px" align="center">
									<fieldset class="main_side_cont">
										<legend><span class="legend_label"><digi:trn key="rep:wizard:availableHierarchies">Available Hierarchies</digi:trn></span></legend>
										<ul id="source_hierarchies_ul" class="draglist">
										</ul>
									</fieldset>	
								</td>
								<td valign="middle" align="center">
									<button style="border: none;" type="button" onClick="MyDragAndDropObject.selectObjs('source_hierarchies_ul', 'dest_hierarchies_ul')">
										<img src="/TEMPLATE/ampTemplate/img_2/ico_arr_right.gif"/>
									</button>
									<br/> <br />
									<button style="border: none;" type="button" onClick="MyDragAndDropObject.deselectObjs('dest_hierarchies_ul', 'source_hierarchies_ul')">
										<img src="/TEMPLATE/ampTemplate/img_2/ico_arr_left.gif"/>
									</button>
								</td>
								<td width="340px" align="center">
									<fieldset class="main_side_cont">
										<legend><span class="legend_label"><digi:trn key="rep:wizard:selectedHierarchies">Selected Hierarchies</digi:trn></span></legend>
										<ol id="dest_hierarchies_ul" class="draglist">
										</ol>
									</fieldset>
								</td>					
								</tr>
								<tr>
								<td colspan="3">
									<span id="hierarchiesMust" style="visibility: hidden;">
									<font color="red">*
										<c:choose> 
											<c:when test="${myForm.budgetExporter }">
												<digi:trn>You cannot select more than 5 hierarchies</digi:trn>
											</c:when>
											<c:otherwise>
												<digi:trn>You cannot select more than 3 hierarchies</digi:trn>
											</c:otherwise>
										</c:choose>
									</font>
									</span>
									<span id="hierarchiesSummaryMust" style="visibility: hidden;">
									<font color="red">*
											<digi:trn>Only in summary reports can all columns be hierarchies</digi:trn>
									</font>
									</span>
									<br>
									<span id="incompatiblehierarchies" style="visibility: hidden;">
									<font color="red">* 
										<digi:trn key="rep:wizard:hint:notmorehierarchies">
											You cannot Select Primary and Secondary Sectors as hierarchies
										</digi:trn>
									</font>
									</span>
								</td>
							</tr>
						
							</table>
						</div>