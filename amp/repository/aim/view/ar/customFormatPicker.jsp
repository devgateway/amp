<%@page import="org.dgfoundation.amp.ar.ReportContextData" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<%
    pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
%>

<jsp:include page="/aim/reportsFilterPicker.do">
    <jsp:param name="init" value=""/>
</jsp:include>

<digi:form action="/reportsFilterPicker.do" name="aimReportsFilterPickerForm3" type="aimReportsFilterPickerForm"
           onsubmit="return validateFormat()">
    <input type="hidden" name="initialCal" id="initialCal" value="${aimReportsFilterPickerForm.calendar}"/>
    <center>
        <b style="font-size: 11px;">
            <digi:trn>Number Format</digi:trn>
        </b>
    </center>
    <hr>
    <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0" style="font-size:11px;">
        <tr>
            <td width="40%" height="18" class="setting-label">
                <digi:trn key="aim:formatPicket:decimalSymbol">Decimal Separator</digi:trn>
            </td>
            <td width="60%" colspan="2" height="18" class="setting-option">
                <html:select styleClass="dropdwn_sm"
                             onchange="initFormatPopup();" property="customDecimalSymbol"
                             styleId="customDecimalSymbol">
                    <c:forEach var="customDecSym"
                               items="${aimReportsFilterPickerForm.alldecimalSymbols}">
                        <html:option value="${customDecSym}">
                            <c:out value="${customDecSym}"/>
                        </html:option>
                    </c:forEach>
                    <html:option value="CUSTOM">
                        <digi:trn>Custom</digi:trn>
                    </html:option>
                </html:select>
                <html:text styleClass="inputx" disabled="true" size="5" maxlength="1" property="customDecimalSymbolTxt"
                           onchange="initFormatPopup()"/>
            </td>
        </tr>
        <tr>
            <td width="40%" height="18" class="setting-label">
                <digi:trn key="aim:formatPicket:maxFracDigits">Maximum Fraction Digits</digi:trn>
            </td>
            <td width="60%" colspan="2" class="setting-option">
                <html:select styleClass="dropdwn_sm" property="customDecimalPlaces" styleId="customDecimalPlaces"
                             onchange="initFormatPopup();">
                    <html:option value="-1">
                        <digi:trn key="aim:formatPicket:NoLimit">No Limit</digi:trn>
                    </html:option>
                    <html:option value="0">0</html:option>
                    <html:option value="1">1</html:option>
                    <html:option value="2">2</html:option>
                    <html:option value="3">3</html:option>
                    <html:option value="4">4</html:option>
                    <html:option value="5">5</html:option>
                    <html:option value="-2">
                        <digi:trn key="aim:formatPicket:custom">Custom</digi:trn>
                    </html:option>
                </html:select>
                <html:text styleClass="inputx" disabled="true" size="5" maxlength="2" property="customDecimalPlacesTxt"
                           onchange="initFormatPopup()"/>
            </td>
        </tr>
        <tr>
            <td width="40%" height="18" class="setting-label">
                <digi:trn key="aim:formatPicket:UseGrouping">Use Grouping Separator</digi:trn>
            </td>
            <td width="40%" height="18" class="setting-option"><html:checkbox property="customUseGrouping"
                                                                              styleId="customUseGrouping"
                                                                              onchange="initFormatPopup();"/></td>
            <td width="20%" height="18" align="left" class="setting-option">&nbsp;</td>
        </tr>
        <tr>
            <td width="40%" height="18" class="setting-label">
                <digi:trn key="aim:formatPicket:GroupingSeparator">Grouping Separator</digi:trn>
            </td>
            <td width="60%" colspan="2" height="18" class="setting-option">
                <html:select styleClass="dropdwn_sm" property="customGroupCharacter" styleId="customGroupCharacter"
                             onchange="initFormatPopup();">
                    <c:forEach var="customGroupChar" items="${aimReportsFilterPickerForm.allgroupingseparators}">
                        <html:option value="${customGroupChar}"><c:out value="${customGroupChar}"/></html:option>
                    </c:forEach>
                    <html:option value="CUSTOM"><digi:trn>Custom</digi:trn></html:option>
                </html:select>
                <html:text styleClass="inputx" disabled="true" size="5" maxlength="1" value=""
                           property="customGroupCharacterTxt" onchange="initFormatPopup()"/>
            </td>
        </tr>
        <tr>
            <td width="40%" height="18" class="setting-label">
                <digi:trn key="aim:formatPicket:GroupSize">Group Size</digi:trn>&nbsp;
            </td>
            <td width="60%" height="18" colspan="2" class="setting-option">
                <html:text styleClass="inputx" disabled="true" property="customGroupSize" size="2" maxlength="1"
                           onchange="initFormatPopup();"/></td>
        </tr>
        <tr>
            <td width="40%" height="18" class="setting-label">
                <digi:trn>Amounts units</digi:trn>
            </td>
            <td width="60%" height="18" colspan="2" class="setting-option">
                    <%-- <html:checkbox property="amountinthousands" styleId="customAmountinThousands"  onchange ="initFormatPopup();" />  --%>
                <html:select property="amountinthousands" styleClass="dropdwn_sm" styleId="customAmountinThousands"
                             onchange="initFormatPopup();">
                    <html:option value="0"><digi:trn>Amounts in Units</digi:trn></html:option>
                    <html:option value="1"><digi:trn>Amounts in Thousands (000)</digi:trn></html:option>
                    <html:option value="2"><digi:trn>Amounts in Millions (000 000)</digi:trn></html:option>
                </html:select>
            </td>
        </tr>
        <tr>
            <td width="40%" height="18" class="setting-label">
                <digi:trn key="aim:formatPicket:Example">Example</digi:trn>
            </td>
            <td width="60%" height="18" colspan="2" class="setting-option bold">
                <div id="number" style="margin-left: 3px">
                    <aim:formatNumber value="123456789.928"/>
                </div>
            </td>
        </tr>
    </table>

    <br>
    <center>
        <b style="font-size: 11px;">
            <digi:trn>Other Settings</digi:trn>
        </b>
    </center>
    <hr>
    <table width="400" align="center" cellpadding="2" cellspacing="2" style="font-size:11px;">
        <tr>
            <td width="40%" class="setting-label">
                <digi:trn>Currency</digi:trn>&nbsp;
            </td>
            <td>
                <html:select property="currency" style="width: 200px" styleClass="dropdwn_sm">
                    <html:optionsCollection property="currencies" value="ampCurrencyId" label="currencyName"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td width="40%" class="setting-label">
                <digi:trn>Calendar</digi:trn>&nbsp;
            </td>
            <td>
                <html:select property="calendar" style="width: 200px" styleClass="dropdwn_sm">
                    <html:optionsCollection property="calendars" value="ampFiscalCalId" label="name"/>
                </html:select>
            </td>
        </tr>

    </table>
    <br>
    <div>
        <div style="margin-right: auto; margin-left: auto; text-align: center;">
                <%-- <html:hidden property="ampReportId" />  --%>
            <input type="hidden" name="ampReportId" value="${reportCD.ampReportId}"/>
            <input type="hidden" name="reportContextId" value="${reportCD.contextId}"/>

            <html:hidden property="defaultCurrency"/>
            <html:hidden property="defaultCalendar"/>

            <html:submit styleClass="buttonx" property="applyFormat" styleId="applyFormatBtn">
                <digi:trn key="rep:filer:ApplyFormat">Apply Format</digi:trn>
            </html:submit>&nbsp;
            <c:set var="maxFractionDigits"><%= org.digijava.module.aim.helper.FormatHelper.getDefaultFormat().getMaximumFractionDigits() %>
            </c:set>

            <input type="hidden" name="apply" value="true">
            <html:hidden property="resetFormat" value="false"/>
            <html:button styleClass="buttonx" onclick="ResetCustom(${maxFractionDigits});" property="applyFormat">
                <digi:trn key="rep:filer:ResetFormat">Reset</digi:trn>
            </html:button>
        </div>
    </div>
</digi:form>