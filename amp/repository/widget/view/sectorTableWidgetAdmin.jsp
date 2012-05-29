<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<script type="text/javascript">
    function validateDelete(){
        var msg='<digi:trn jsFriendly="true">Do you want to delete sector table</digi:trn> ?';
        return confirm(msg);
    }

</script>
<h1 class="admintitle">
                <span class="subtitle-blue"><digi:trn>Sector Table Manager</digi:trn></span>
</h1>
<digi:instance property="sectorTableWidgetForm" />
<digi:form action="/sectorTableManager.do~actType=viewAll">



    <table width="1000" border="0" cellpadding="0" align=center style="font-size:12px;">
       <!-- <tr>
            <td height=40 bgcolor=#f2f2f2 style="padding-top:10px; padding-bottom:10px; padding-left:10px;">
                <span class="crumb">
                    <c:set var="translation">
                        <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
                    </c:set>
                    <html:link  href="/aim/admin.do" styleClass="comment" title="${translation}" >
                        <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
                    </html:link>&nbsp;&gt;&nbsp;
                    <digi:trn><b>Sector Table Manager</b></digi:trn>
                </span>
            </td>
			</tr>
			-->
            <tr>
                <td>
                    <digi:errors/>
                </td>
            </tr>
    
        <tr>
            <td style="padding-top:10px; padding-bottom:10px;">
                <a href="/widget/sectorTableManager.do~actType=create"><digi:trn><b>Create New Sector Table Widget</b></digi:trn></a>
            </td>
        </tr>
        <tr>
            <td style="padding-top:10px; padding-bottom:10px; border:1px solid #CCCCCC;" bgcolor=#f2f2f2>

                <table border="0" width="60%" align="center" style="font-size:12px;" class="inside">
                    <tr bgColor="#c7d4db">

                        <td nowrap="nowrap" width="80%" class="inside" height=25>
                            <strong><digi:trn key="widget:orgProfileManager:type">Name</digi:trn></strong>
                        </td>
                        <td nowrap="nowrap" class="inside" align=center>
                            <strong><digi:trn key="widget:orgProfileManager:operations">Operations</digi:trn></strong>
                        </td>
                    </tr>
                    <c:forEach var="sectorTable" items="${sectorTableWidgetForm.sectorTables}" varStatus="stat">
                        <tr>
                            <td nowrap="nowrap" class="inside">
                               <c:out value="${sectorTable.name}"/> 
                            </td>

                            <td nowrap="nowrap" class="inside" align=center>
                                <a href="/widget/sectorTableManager.do~actType=edit~sectorTableId=${sectorTable.id}">
                                    <digi:trn>Edit</digi:trn>
                                </a>

                                |&nbsp;
                                <a href="/widget/sectorTableManager.do~actType=delete~sectorTableId=${sectorTable.id}">
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

