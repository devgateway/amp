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

        <tr>
            <td colspan="4" align="center">
                <html:submit styleClass="dr-menu"><digi:trn key="btn:save">Save</digi:trn></html:submit>
                &nbsp; &nbsp;
                <html:submit styleClass="dr-menu" onclick="cancelSave(); return false;">
                    <digi:trn key="btn:cancel">Cancel</digi:trn>
                </html:submit>
            </td>
        </tr>

    </table>
</digi:form>

<script type="text/javascript">
    function cancelSave() {
        var editForm = document.getElementById("editForm");
        editForm.elements["actionParam"].value = "search";
        editForm.reset();
        editForm.submit();
    }
</script>