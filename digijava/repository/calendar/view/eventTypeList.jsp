<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fmt" prefix="fmt" %>

<script language="javaScript" type="">
function setActionMethod(methodName) {
    document.getElementsByName('method')[0].value=methodName;
    return true;
}
function setDeleteId(id) {
    document.getElementsByName('deleteId')[0].value=id;

    return setActionMethod('delete');
}

</script>
<digi:form action="/eventTypes.do" method="post">
    <input type="hidden" name="deleteId" value="-1" />
    <ul>
        <digi:errors />
    </ul>
    <table>
        <tr>
            <td>
                <digi:trn key="calendar:eventTypes:page_header">Event Types</digi:trn>
                <input type="hidden" name="method" value="NONE" />
            </td>
        </tr>
        <tr>
            <td>
                <table width="100%">
                    <tr>
                        <th><digi:trn key="calendar:typeName">Name</digi:trn></th>
                        <th><digi:trn key="calendar:typeColor">Color</digi:trn></th>
                        <th>&nbsp;</th>
                    </tr>
                    <c:forEach items="${calendarEventTypeForm.eventTypes}" var="eventType">
                        <tr>
                            <td>
                                <html:text name="eventType" property="name" indexed="true"/>
                            </td>
                            <td>
                                <html:text name="eventType" property="color" indexed="true"/>
                            </td>
                            <td>
                                <html:submit value="Delete" onclick="setDeleteId('${eventType.id}');" />
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <fieldset>
                    <legend><digi:trn key="calendar:addNewType">Add a new type</digi:trn></legend>
                <table width="100%">
                    <tr>
                        <td><digi:trn key="calendar:typeName">Name</digi:trn></td>
                        <td><html:text property="addName" /></td>
                    </tr>
                    <tr>
                        <td><digi:trn key="calendar:typeColor">Color</digi:trn></td>
                        <td><html:text property="addColor" /></td>
                    </tr>
                    <tr>
                        <td colspan="2" align="right"><html:submit value="Add" onclick="setActionMethod('addType')"/></td></td>
                    </tr>
                </table>
                </fieldset>
        </tr>
     </table>

</digi:form>

