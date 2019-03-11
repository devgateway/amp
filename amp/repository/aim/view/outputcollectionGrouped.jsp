<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<digi:instance property="aimCompareActivityVersionsForm" />
				<logic:iterate id="groupItem" property="outputCollectionGroupedAsSet" name="aimCompareActivityVersionsForm" type="java.util.Map.Entry">
					
					<td rowspan="${groupItem.value.size()}" align="left" valign="center" width="8%" class="inside" style="padding-left: 5px; font-size: 12px; border-left-width: 1px;">
							<digi:trn><bean:write property="key" name="groupItem"/></digi:trn>
					</td>
						<logic:iterate id="diffItem" name="groupItem" property="value" indexId="iterIdx">
								
								<logic:greaterThan name="iterIdx" value="0">
									<tr>
								</logic:greaterThan>
									<td width="50%" align="left" valign="top" style="padding-left: 5px; border-right-width: 0px;" class="inside">
										<div id="left${diffItem.index}">
											<logic:empty name="diffItem" property="stringOutput[1]">&nbsp;</logic:empty>
											<bean:write name="diffItem" property="stringOutput[1]" filter="false"/>
										</div>
									</td>
									<logic:equal value="true" name="aimCompareActivityVersionsForm" property="showMergeColumn">
										<td align="center" valign="middle" class="inside">
                                            <c:if test="${!diffItem.blockSingleChangeOutput}">
                                                <button type="button" onClick="javascript:left(${diffItem.index});" style="border: none; background-color: transparent">
                                                    <img src="/TEMPLATE/ampTemplate/img_2/ico_arr_right.gif"/>
                                                </button>
                                            </c:if>
										</td>
										<td align="left" valign="top" style="padding-left: 5px;" class="inside">
											<div id="merge${diffItem.index}">&nbsp;</div>
										</td>
										<td align="center" valign="middle" class="inside" style="border-right-width: 0px;">
                                            <c:if test="${!diffItem.blockSingleChangeOutput}">
                                                <button type="button" onClick="javascript:right(${diffItem.index});" style="border: none; background-color: transparent">
                                                    <img src="/TEMPLATE/ampTemplate/img_2/ico_arr_left.gif"/>
                                                </button>
                                            </c:if>
										</td>
                                        <c:if test="${!diffItem.blockSingleChangeOutput}">
										    <input type="hidden" id='mergedValues[${diffItem.index}]' value="" name="mergedValues[${index}]"/>
                                        </c:if>
									</logic:equal>
									<td width="50%" align="left" valign="top" style="padding-left: 5px; border-left-width: 0px;" class="inside">
										<div id="right${diffItem.index}">
											<logic:empty name="diffItem" property="stringOutput[0]">&nbsp;</logic:empty>
											<bean:write name="diffItem" property="stringOutput[0]" filter="false"/>
										</div>
									</td>
								<logic:greaterThan name="iterIdx" value="0">
								</tr>
								</logic:greaterThan>
						</logic:iterate>

					</td></tr>
				</logic:iterate>