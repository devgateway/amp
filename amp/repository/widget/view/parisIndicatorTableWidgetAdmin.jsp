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

<digi:instance property="piTableWidgetForm" />
<digi:form action="/piTableWidgetManager.do~actType=viewAll">



    <table width="60%" border="0" cellpadding="15">
        <!--<tr>
            <td>
                <span class="crumb">
                    <c:set var="translation">
                        <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
                    </c:set>
                    <html:link  href="/aim/admin.do" styleClass="comment" title="${translation}" >
                        <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
                    </html:link>&nbsp;&gt;&nbsp;
                    <digi:trn>Paris Indicator Table Manager</digi:trn>
                </span>
            </td>
			</tr>-->
            <tr>
                <td>
                    <digi:errors/>
                </td>
            </tr>
        <tr>
            <td>
                <span class="subtitle-blue"><digi:trn>Paris Indicator Table Manager</digi:trn></span>
            </td>
        </tr>
        <tr>
            <td>
                <a href="/widget/piTableWidgetManager.do~actType=create"><digi:trn>Create New Paris Indicator Table Widget</digi:trn></a>
            </td>
        </tr>
        <tr>
            <td>

                <table border="0" width="60%" align="center" style="font-family:verdana;font-size:11px;">
                    <tr bgColor="#d7eafd">

                        <td nowrap="nowrap" width="80%">
                            <strong><digi:trn>Name</digi:trn></strong>
                        </td>
                        <td nowrap="nowrap">
                            <strong><digi:trn>Operations</digi:trn></strong>
                        </td>
                    </tr>
                    <c:forEach var="piTable" items="${piTableWidgetForm.piTableWidgets}" varStatus="stat">
                        <tr>
                            <td nowrap="nowrap">
                                ${piTable.name}
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

