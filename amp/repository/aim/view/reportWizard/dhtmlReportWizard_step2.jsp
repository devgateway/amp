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

						<div id="columns_step_div" class="main_side_cont yui-hidden" style="${topBottomPadding}">
							<c:set var="stepNum" value="1"  scope="request" />
							<c:if test="${!myForm.onePager}">
								<jsp:include page="toolbar.jsp" />
							</c:if>
							<br />
							<table cellpadding="0" cellspacing="0" style="width: 735px;" border=0>
								<tr>
									<td width="48%" align="center">
										<fieldset class="main_side_cont">
											<legend><span class="legend_label"><digi:trn key="rep:wizard:availableColumns">Available Columns</digi:trn></span></legend>
											<div id="source_col_div" class="draglist">
												<jsp:include page="setColumns.jsp" />
											</div>
										</fieldset>
									</td>
									<td valign="middle" align="center">
										<button type="button" onClick="ColumnsDragAndDropObject.selectObjs('source_col_div', 'dest_col_ul')" style="border: none;">
											<img src="/TEMPLATE/ampTemplate/img_2/ico_arr_right.gif"/>
										</button>
										<br/> <br />
										<button type="button" onClick="ColumnsDragAndDropObject.deselectObjs('dest_col_ul')" style="border: none;">
											<img src="/TEMPLATE/ampTemplate/img_2/ico_arr_left.gif"/>
										</button>
									</td>
									<td width="48%" align="center">
										<fieldset class="main_side_cont">
											<legend><span class="legend_label"><digi:trn key="rep:wizard:selectedColumns">Selected Columns</digi:trn></span></legend>
											<ul id="dest_col_ul" class="draglist" style="line-height: 20px;">
												
											</ul>
										</fieldset>
									</td>
								</tr>
								<tr>
									<td align="center" valign="top">
										<span id="columnsMust" style="visibility: hidden">
											<font color="red">* 
												<digi:trn key="rep:wizard:hint:mustselectcolumn">
													Must select at least one column
												</digi:trn>
											</font>
										</span>
									</td>
									<td>&nbsp;</td>
									<td align="center" valign="top">
										<span id="columnsLimit" style="visibility: hidden">
											<font color="red">* 
												<digi:trn key="rep:wizard:hint:limit3columns">
													You cannot select more than 3 columns in a desktop tab
												</digi:trn>
											</font>
										</span>
									</td>
								</tr>
							</table>							
						</div>