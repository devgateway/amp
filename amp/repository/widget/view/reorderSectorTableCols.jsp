
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script type="text/javascript">
    <!--


    function cancel(){
       <digi:context name="addEditSectorTable" property="context/widget/sectorTableManager.do~actType=viewAll" />
               document.sectorTableWidgetForm.action = "${addEditSectorTable}";
               document.sectorTableWidgetForm.submit();
           }

           /*
            * we need this save method because
            *  someone may press cancel
            *  on the sector selection popup
            */
           function save(){
       <digi:context name="addEditSectorTable" property="/widget/sectorTableManager.do~actType=save" />
               document.sectorTableWidgetForm.action = "${addEditSectorTable}";
               document.sectorTableWidgetForm.target = "_self";
               document.sectorTableWidgetForm.submit();
           }
           function back(){
            <digi:context name="addEditSectorTable" property="/widget/sectorTableManager.do~actType=gotoFirstStep" />
                    document.sectorTableWidgetForm.action = "${addEditSectorTable}";
                    document.sectorTableWidgetForm.target = "_self";
                    document.sectorTableWidgetForm.submit();
                }



                function moveUp(id,type){
                    if(type!=null){
             <digi:context name="addEditSectorTable" property="context/widget/sectorTableManager.do~actType=reorderColumnUp" />
                         document.sectorTableWidgetForm.action = "${addEditSectorTable}&sectorTableYearId="+id+"&type="+type;
                     }
                     else{
                <digi:context name="addEditSectorTable" property="context/widget/sectorTableManager.do~actType=reorderUp" />
                            document.sectorTableWidgetForm.action = "${addEditSectorTable}&sectorToReorderId="+id;
                        }
                        document.sectorTableWidgetForm.target = "_self";
                        document.sectorTableWidgetForm.submit();

                    }
                    function moveDown(id,type){
                        if(type!=null){
                   <digi:context name="addEditSectorTable" property="context/widget/sectorTableManager.do~actType=reorderColumnDown" />
                               document.sectorTableWidgetForm.action = "${addEditSectorTable}&sectorTableYearId="+id+"&type="+type;;
                           }
                           else{
                 <digi:context name="addEditSectorTable" property="context/widget/sectorTableManager.do~actType=reorderDown" />
                             document.sectorTableWidgetForm.action = "${addEditSectorTable}&sectorToReorderId="+id;
                         }
                         document.sectorTableWidgetForm.target = "_self";
                         document.sectorTableWidgetForm.submit();
                     }
                     //-->
</script>

<digi:instance property="sectorTableWidgetForm" />
<digi:form action="/sectorTableManager.do~actType=save">

    <html:hidden name="sectorTableWidgetForm" property="sectorTableId"/>
    <table width="100%" border="0" cellpadding="15">
        <tr>
            <td>
                <span class="crumb">
                    <c:set var="translation">
                        <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
                    </c:set>
                    <html:link  href="/aim/admin.do" styleClass="comment" title="${translation}" >
                        <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
                    </html:link>&nbsp;&gt;&nbsp;
                    <html:link  href="/widget/sectorTableManager.do~actType=viewAll" styleClass="comment">
                        <digi:trn>Sector Table Widget Manager</digi:trn>
                    </html:link>
                    &nbsp;&gt;&nbsp;
                    <digi:trn>Reorder Sector Table Widget Columns/Rows</digi:trn>
                </span>
            </td>
        </tr>
        <tr>
            <td>
                <span class="subtitle-blue">
                    <digi:trn>Reorder Sector Table Widget Columns/Rows</digi:trn>
                </span>
            </td>
        </tr>
        <tr>
            <td>

                <table  width="35%" >
                    <tr>
                        <td >
                            <digi:errors/>
                        </td>
                    </tr>

                    <tr>
                        <td nowrap="nowrap" align="left"  style="font-weight:bold;font-size:14px">

                            ${sectorTableWidgetForm.name}

                        </td>
                    </tr>
                    <tr>
                        <td td nowrap="nowrap" align="left" >
                            <table border="0" style="font-family:verdana;font-size:11px;border:1px solid silver;width:100%">
                                <tr bgColor="#d7eafd">
                                    <td><digi:trn>Sector Name</digi:trn></td>
                                    <td width="10%" colspan="2"><digi:trn>Order</digi:trn></td>
                                </tr>
                                <c:forEach var="sector" items="${sectorTableWidgetForm.sectors}" varStatus="status">
                                    <tr>
                                        <td>
                                            <c:out value="${sector.sector.name}"/>
                                        </td>
                                        <td >
                                            <c:if test="${status.first != true}">
                                                <a href="javascript:moveUp(${sector.sector.ampSectorId})"><img border="0" src='<digi:file src="/TEMPLATE/ampTemplate/images/up.gif"/>'></a>
                                            </c:if>

                                        </td>
                                        <td>
                                            <c:if test="${status.last != true}">
                                                <a href="javascript:moveDown(${sector.sector.ampSectorId})"><img border="0" src='<digi:file src="/TEMPLATE/ampTemplate/images/down.gif"/>'></a>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td nowrap="nowrap" align="left" >&nbsp;</td>
                    </tr>

                    <tr>
                        <td nowrap="nowrap" align="left" >

                            <table style="font-family:verdana;font-size:11px;border:1px solid silver;width:100%" cellpadding="3" cellspacing="3">
                                <tr  style="background-color:#d7eafd;font-weight:bold">
                                    <td align="center"><digi:trn>Year</digi:trn></td>
                                    <td  align="center"><digi:trn>Type</digi:trn></td>
                                    <td width="10%" colspan="2" align="center"><digi:trn>Order</digi:trn></td>
                                </tr>
                                <c:forEach var="sectorTableYear" items="${sectorTableWidgetForm.sectorTableYears}" varStatus="status">
                                    <tr>
                                        <td>

                                            <c:out value="${sectorTableYear.year}"/>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${sectorTableYear.type==1}">
                                                    <digi:trn>Total Year Column</digi:trn>
                                                </c:when>
                                                <c:otherwise>
                                                    <digi:trn>Percent Year Column</digi:trn>
                                                </c:otherwise>
                                            </c:choose>

                                        </td>
                                        <td >
                                            <c:if test="${status.first != true}">
                                                <a href="javascript:moveUp(${sectorTableYear.year},${sectorTableYear.type})"><img border="0" src='<digi:file src="/TEMPLATE/ampTemplate/images/up.gif"/>'></a>
                                            </c:if>

                                        </td>
                                        <td>
                                            <c:if test="${status.last != true}">
                                                <a href="javascript:moveDown(${sectorTableYear.year},${sectorTableYear.type})"><img border="0" src='<digi:file src="/TEMPLATE/ampTemplate/images/down.gif"/>'></a>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>

                        </td>

                    </tr>
                    <tr>
                        <td align="center">
                            <input type="button" onclick="back()" value="<digi:trn>Back</digi:trn>"/>
                            <input type="button" onclick="save()" value="<digi:trn>Save</digi:trn>"/>
                            <input type="button" value="Cancel" onclick="cancel()">
                        </td>

                    </tr>
                </table>

            </td>
        </tr>
    </table>
</digi:form>
