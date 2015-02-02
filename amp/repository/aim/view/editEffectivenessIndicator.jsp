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

<script type="text/javascript" src='<digi:file src="module/aim/scripts/table_utils.js"/>'>.</script>

<h1 class="admintitle"><digi:trn>Aid Effectiveness Indicator Manager - Edit Indicator</digi:trn></h1>

<digi:form action="/aidEffectivenessIndicatorsManager.do" method="post" styleId="editForm">
    <html:hidden property="actionParam" value="save"/>
    <html:hidden property="ampIndicatorId" />
    <table style="font-family: verdana; font-size: 11px; font-weight: bold" cellpadding="4">
        <tr>
            <td>
                <digi:trn key="aim:indicatorType">Indicator Type</digi:trn>:
            </td>
            <td>
                <html:select style="inp-text" property="indicatorType" styleId="searchIndicatorType">
                    <html:option value="-1">
                        -<digi:trn key="aim:selectIndicatorType">Choose One</digi:trn>-
                    </html:option>
                    <html:option value="0">
                        <digi:trn key="aim:dropDownList">Drop down list</digi:trn>
                    </html:option>
                    <html:option value="1">
                        <digi:trn key="aim:radioOption">Radio option</digi:trn>
                    </html:option>
                </html:select>
            </td>
        </tr>

        <tr>
            <td colspan="4">
                &nbsp;
            </td>
        </tr>

        <tr>
            <td>
                <digi:trn>Indicator Name</digi:trn>:</b>
            </td>
            <td>
                <html:text property="ampIndicatorName" styleId="editAmpIndicatorName" />
            </td>


            <td>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <digi:trn>Display on Form</digi:trn>:</b>
            </td>
            <td>
                <html:checkbox property="active" styleId="editActive" />
            </td>
        </tr>

        <tr>
            <td>
                <digi:trn>Indicator Title</digi:trn>:</b>
            </td>
            <td>
                <html:text property="tooltipText" styleId="editAmpIndicatorTitle" />
            </td>

            <td>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <digi:trn>Is Mandatory</digi:trn>:</b>
            </td>
            <td>
                <html:checkbox property="mandatory" styleId="editMandatory" />
            </td>
        </tr>

        <tr>
            <td colspan="4">
                &nbsp;
            </td>
        </tr>

    </table>

    <div valign="30px">
        <c:set var="addOptionText"><digi:trn>Add Option</digi:trn></c:set>
        <input type="button" class="dr-menu" value="${addOptionText}" onclick="javascript: addOptionRow(); "/>
    </div>

    <logic:present name="aidEffectivenessIndicatorsForm" property="options">
        <table style="font-family: verdana; font-size: 11px; font-weight: bold" cellpadding="4" id="optionsTableId" width="50%">
            <tr>
                <th align="center" bgcolor="#c7d4db"><digi:trn>Option Text</digi:trn></th>
                <th align="center" bgcolor="#c7d4db"><digi:trn>Is Default</digi:trn></th>
            </tr>

            <logic:iterate name="aidEffectivenessIndicatorsForm" property="options" id="option" type="org.digijava.module.aim.dbentity.AmpAidEffectivenessIndicatorOption">
                <tr>
                    <td width="50%">${option.ampIndicatorOptionName}</td>

                    <td width="50%">
                        <c:choose>
                            <c:when test="${option.defaultOption}">
                                <digi:trn key="aim:yes">Yes</digi:trn>
                            </c:when>
                            <c:otherwise>
                                <digi:trn key="aim:no">No</digi:trn>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </logic:iterate>
        </table>
    </logic:present>

    <div style='height: 30px'>
        <%-- some empty space --%>&nbsp;
    </div>

    <div>
        <html:submit styleClass="dr-menu"><digi:trn key="btn:save">Save</digi:trn></html:submit>
        &nbsp; &nbsp;
        <html:submit styleClass="dr-menu" onclick="cancelSave(); return false;">
            <digi:trn key="btn:cancel">Cancel</digi:trn>
        </html:submit>
    </div>

</digi:form>

<script type="text/javascript">
    function cancelSave() {
        var editForm = document.getElementById("editForm");
        editForm.elements["actionParam"].value = "search";
        editForm.reset();
        editForm.submit();
    }

    function addOptionRow() {
        var table = document.getElementById("optionsTableId");

        // Create an empty <tr> element and add it to the 1st position of the table:
        var row = table.insertRow();

        // Insert new cells (<td> elements) at the 1st and 2nd position of the "new" <tr> element:
        var cell1 = row.insertCell(0);
        var cell2 = row.insertCell(1);

        // Add some text to the new cells:
        cell1.innerHTML = "NEW CELL1";
        cell2.innerHTML = "NEW CELL2";
    }

    setStripsTable("optionsTableId", "tableEven", "tableOdd");
    setHoveredTable("optionsTableId", true);

</script>