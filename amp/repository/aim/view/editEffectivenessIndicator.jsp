<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<%@ page import="org.digijava.ampModule.aim.dbentity.AmpAidEffectivenessIndicator" %>

<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/skins/sam/tabview.css"> 
<script type="text/javascript" src='<digi:file src="ampModule/aim/scripts/table_utils.js"/>'>.</script>
<script type="text/javascript" src="/repository/aim/view/multilingual/multilingual_scripts.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script>
<script type="text/javascript">
    var optionsTableCounter = 0;
</script>

<h1 class="admintitle"><digi:trn>Aid Effectiveness Indicator Manager - Edit Indicator</digi:trn></h1>
<digi:errors/>
<digi:form action="/aidEffectivenessIndicatorsManager.do" method="post" styleId="editForm">
    <html:hidden property="actionParam" value="save"/>
    <html:hidden property="ampIndicatorId" />
    <html:hidden property="oldAmpIndicatorName" />

    <table width="100%">
    <tr>
    <td width="75%">
    <table style="font-family: verdana; font-size: 11px; font-weight: bold" cellpadding="4">
        <tr>
            <td>
                <digi:trn key="aim:indicatorType">Indicator Type</digi:trn><font color="red">*</font>:
            </td>
            <td>
                <html:select style="inp-text" property="indicatorType" styleId="searchIndicatorType">
                    <html:option value="-1">
                        -<digi:trn key="aim:selectIndicatorType">Choose One</digi:trn>-
                    </html:option>
                    <html:option value="<%=String.valueOf(AmpAidEffectivenessIndicator.IndicatorType.DROPDOWN_LIST.ordinal())%>">
                        <digi:trn key="aim:dropDownList">Drop down list</digi:trn>
                    </html:option>
                    <html:option value="<%=String.valueOf(AmpAidEffectivenessIndicator.IndicatorType.SELECT_LIST.ordinal())%>">
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
                <digi:trn>Indicator Name</digi:trn><font color="red">*</font>:
            </td>
            <td>
                <jsp:include page="/repository/aim/view/multilingual/multilingualFieldEntry.jsp">
					<jsp:param name="attr_name" value="multilingual_aid_name" />
				</jsp:include>         
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
                <digi:trn>Indicator Tooltip</digi:trn>:
            </td>
            <td>
            	<jsp:include page="/repository/aim/view/multilingual/multilingualFieldEntry.jsp">
							<jsp:param name="attr_name" value="multilingual_aid_tooltip" />
				</jsp:include>         
            </td>

            <td>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <digi:trn>Is Mandatory</digi:trn>:
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
    </td>

    <td valign="top" align="right" width="25%">
        <table align="center" cellpadding="0" cellspacing="0" width="90%" border="0">
            <tr>
                <td>
                    <!-- Other Links -->
                    <table cellpadding="0" cellspacing="0" width="120">
                        <tr>
                            <td bgColor=#c9c9c7 class=box-title>
                                <b style="font-size:12px; padding-left:5px;">
                                    <digi:trn key="aim:otherLinks">
                                        Other links
                                    </digi:trn>
                                </b>
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
                        <tr>
                            <td class="inside">
                                <digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10"/>
                                <c:set var="translation">
                                    <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
                                </c:set>
                                <digi:link href="/admin.do" title="${translation}" >
                                <digi:trn key="aim:AmpAdminHome">
                                Admin Home
                                </digi:trn>
                                </digi:link>
                            </td>
                        </tr>
                        <tr>
                            <td class="inside">
                                <digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10"/>
                                <c:set var="translation">
                                    <digi:trn key="aim:clickToViewIndicators">Click here to view indicators list</digi:trn>
                                </c:set>
                                <digi:link href="/aidEffectivenessIndicatorsManager.do" title="${translation}" >
                                <digi:trn key="aim:AmpIndicatorsList">
                                Aid Effectiveness Indicator Manager
                                </digi:trn>
                                </digi:link>
                            </td>
                        </tr>
                        <!-- end of other links -->
                    </table>
                </td>
            </tr>
        </table>
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
                <th align="center" bgcolor="#c7d4db"><digi:trn>Option Text</digi:trn><font color="red">*</font></th>
                <th align="center" bgcolor="#c7d4db">&nbsp;</th>
            </tr>

            <logic:iterate name="aidEffectivenessIndicatorsForm" property="options" id="option" indexId="idx" type="org.digijava.ampModule.aim.dbentity.AmpAidEffectivenessIndicatorOption">
                <script type="text/javascript">
                    optionsTableCounter++;
                </script>

                <html:hidden property="options[${idx}].ampIndicatorOptionId" />

                <tr>
                    <td width="70%">
                        <html:text property="options[${idx}].ampIndicatorOptionName" styleId="editAmpIndicatorName" size="50" maxlength="250"/>
                        <%-- ${option.ampIndicatorOptionName} --%>
                    </td>
                    <td width="30%" align="center">
                        <a onclick="deleteOptionRow('${option.ampIndicatorOptionId}', '${idx}'); return false;">
                            <img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border="0" title="<digi:trn>Delete</digi:trn>"/>
                        </a>
                    </td>
                </tr>
            </logic:iterate>
        </table>
    </logic:present>

    <div style='height: 30px'>
        <%-- some empty space --%>&nbsp;
    </div>

    <div>
        <html:submit styleClass="dr-menu" onclick="return validateSaveAidIndicator()"><digi:trn key="btn:save">Save</digi:trn></html:submit>
        &nbsp; &nbsp;
        <html:submit styleClass="dr-menu" onclick="cancelSave(); return false;">
            <digi:trn key="btn:cancel">Cancel</digi:trn>
        </html:submit>
    </div>

</digi:form>

<script type="text/javascript">
	function validateSaveAidIndicator() {
	    var nameEntered = check_multilingual_value_entered('AmpAidEffectivenessIndicator_ampIndicatorName');
	    if (!nameEntered) {
	        alert('<digi:trn jsFriendly="true">Please enter name for Indicator.</digi:trn>');
	        return false;
	    }
	    
	    var indicatorType = document.aimAddOrgForm.indicatorType.value;
        if (ampOrgTypeId == '-1' || ampOrgTypeId == null) {
            alert('<digi:trn  jsFriendly="true">Please Select Indicator Type.</digi:trn>');
            document.aimAddOrgForm.ampOrgTypeId.focus();
            return false;
        }
	    
	    return true;
    }

    function cancelSave() {
        var editForm = document.getElementById("editForm");
        editForm.elements["actionParam"].value = "list";
        editForm.reset();
        editForm.submit();
    }

    /* The dynamic (javascript) adding does not work because of struts beans populator
    function addOptionRow() {
        optionsTableCounter ++;
        var table = document.getElementById("optionsTableId");

        // Create an empty <tr> element and add it to the 1st position of the table:
        var row = table.insertRow();

        // Insert new cells (<td> elements) at the 1st and 2nd position of the "new" <tr> element:
        var cell1 = row.insertCell(0);

        // Add some text to the new cells:
        cell1.innerHTML = '<input type="text" name="options[' + optionsTableCounter + '].ampIndicatorOptionName" id="ampIndicatorOptionNameId" />';
    }*/

    function addOptionRow() {
        var editForm = document.getElementById("editForm");
        editForm.action = "/aidEffectivenessIndicatorsManager.do?actionParam=addOption";
        editForm.submit();
    }

    function deleteOptionRow(ampIndicatorOptionId, idx) {
        var editForm = document.getElementById("editForm");
        editForm.action = '/aidEffectivenessIndicatorsManager.do?actionParam=deleteOption&ampIndicatorOptionId=' + ampIndicatorOptionId + '&optionIndex=' + idx;
        editForm.submit();
    }

    setStripsTable("optionsTableId", "tableEven", "tableOdd");
    setHoveredTable("optionsTableId", true);

</script>