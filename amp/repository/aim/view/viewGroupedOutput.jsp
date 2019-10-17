<%--
  Created by IntelliJ IDEA.
  User: Wessi
  Date: 08-Mar-19
  Time: 10:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<bean:define id="beanGroupId" name="beanGroupItem" scope="request" toScope="page"/>

<c:if test="${not empty beanGroupId}">
    <logic:iterate id="groupId" name="beanGroupId" type="java.util.Map.Entry">

        <tr><td rowspan="${groupId.value.size()}" align="left" valign="center" width="8%" class="inside" style="padding-left: 5px; font-size: 12px; border-left-width: 1px;">
        <digi:trn><bean:write property="key" name="groupId"/></digi:trn>

        <logic:iterate id="changeItemId" name="groupId" property="value" indexId="iterIdx">

            <logic:greaterThan name="iterIdx" value="0">
                <tr>
            </logic:greaterThan>

            <td width="50%" align="left" valign="top" style="padding-left: 5px; border-right-width: 0px;" class="inside">
                <div id="left${changeItemId.index}">
                    <logic:empty name="changeItemId" property="stringOutput[1]">&nbsp;</logic:empty>
                    <bean:write name="changeItemId" property="stringOutput[1]" filter="false"/>
                </div>
            </td>

            <logic:equal value="true" name="aimCompareActivityVersionsForm" property="showMergeColumn">
                <td align="center" valign="middle" class="inside">
                    <c:if test="${!changeItemId.blockSingleChangeOutput}">
                        <button type="button" onClick="javascript:left(${changeItemId.index});" style="border: none; background-color: transparent">
                            <img src="/TEMPLATE/ampTemplate/img_2/ico_arr_right.gif"/>
                        </button>
                    </c:if>
                </td>
                <td align="left" valign="top" style="padding-left: 5px;" class="inside">
                    <div id="merge${changeItemId.index}">&nbsp;</div>
                </td>
                <td align="center" valign="middle" class="inside" style="border-right-width: 0px;">
                    <c:if test="${!changeItemId.blockSingleChangeOutput}">
                        <button type="button" onClick="javascript:right(${changeItemId.index});" style="border: none; background-color: transparent">
                            <img src="/TEMPLATE/ampTemplate/img_2/ico_arr_left.gif"/>
                        </button>
                    </c:if>
                </td>
                <c:if test="${!changeItemId.blockSingleChangeOutput}">
                    <input type="hidden" id='mergedValues[${changeItemId.index}]' value="" name="mergedValues[${index}]"/>
                </c:if>
            </logic:equal>

            <td width="50%" align="left" valign="top" style="padding-left: 5px; border-left-width: 0px;" class="inside">
                <div id="right${changeItemId.index}">
                    <logic:empty name="changeItemId" property="stringOutput[0]">&nbsp;</logic:empty>
                    <bean:write name="changeItemId" property="stringOutput[0]" filter="false"/>
                </div>
            </td>

            <logic:greaterThan name="iterIdx" value="0">
                </tr>
            </logic:greaterThan>

        </logic:iterate>

        </td></tr>

    </logic:iterate>
</c:if>