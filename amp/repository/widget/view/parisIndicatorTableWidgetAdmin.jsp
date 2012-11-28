<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<script type="text/javascript">
    function validateDelete(){
        var msg='<digi:trn jsFriendly="true">Do you want to delete paris indicator table</digi:trn> ?';
        return confirm(msg);
    }

</script>
<h1 class="admintitle"><digi:trn>Paris Indicator Table Manager</digi:trn></h1>
<digi:instance property="piTableWidgetForm" />
<digi:form action="/piTableWidgetManager.do~actType=viewAll">

<digi:errors/>

    <table width="100%" border="0" cellpadding="0">

        <tr>
            <td style="padding-bottom:15px;">
                <a href="/widget/piTableWidgetManager.do~actType=create"><digi:trn>Create New Paris Indicator Table Widget</digi:trn></a>
            </td>
        </tr>
        <tr>
            <td style="background-color:#f2f2f2; padding:15px;">

                <table border="0" width="60%" align="center" style="font-size:12px;">
                    <tr bgColor="#c7d4db">

                        <td nowrap="nowrap" width="80%" style="padding:5px;">
                            <strong><digi:trn>Name</digi:trn></strong>
                        </td>
                        <td nowrap="nowrap" style="padding:5px;">
                            <strong><digi:trn>Operations</digi:trn></strong>
                        </td>
                    </tr>
                    <c:forEach var="piTable" items="${piTableWidgetForm.piTableWidgets}" varStatus="stat">
                        <tr bgcolor="#FFFFFF">
                            <td nowrap="nowrap">
                                <c:out value="${piTable.name}"></c:out>
                            </td>

                            <td nowrap="nowrap">
                                <a href="/widget/piTableWidgetManager.do~actType=edit~piTableWidgetId=${piTable.id}">
                                    <digi:trn>Edit</digi:trn>
                                </a>

                                |&nbsp;
                                <a href="/widget/piTableWidgetManager.do~actType=delete~piTableWidgetId=${piTable.id}">
                                    <img border="0" src='<digi:file src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif"/>'>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>

              </table>


            </td>
        </tr>
    </table>

</digi:form>

