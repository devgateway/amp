<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<c:set var="src">
    <digi:file src="module/aim/addSectors.jsp"/>
</c:set>
<jsp:include page="${src}" flush="true" />
<script type="text/javascript">
    <!--
    addLoadEvent(initFunctions);

    function initFunctions(){
        initSectorScript();
        checkDonorColumn();
    }
    function addSectors() {
        myAddSectors("");
    }

    function addSector(param){
       <digi:context name="addSec" property="context/widget/sectorTableManager.do~actType=selectSector" />
               document.sectorTableWidgetForm.action = "${addSec}";
               document.sectorTableWidgetForm.target = "_self";
               document.sectorTableWidgetForm.submit();
           }
           function validate(){
               var selSectors=document.sectorTableWidgetForm.selectedSectors
               if(typeof selSectors=='undefined'||selSectors.length==0){
                   alert("Please add at least one sector");
                   return false;
               }
               if(document.sectorTableWidgetForm.name.value==''){
                   alert("Please enter the name");
                   return false;
               }
             
              next();
           }

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
           function next(){
       <digi:context name="addEditSectorTable" property="/widget/sectorTableManager.do~actType=gotoSecondStep" />
               document.sectorTableWidgetForm.action = "${addEditSectorTable}";
               document.sectorTableWidgetForm.target = "_self";
               document.sectorTableWidgetForm.submit();
           }
             function save(){
       <digi:context name="addEditSectorTable" property="/widget/sectorTableManager.do~actType=save" />
               document.sectorTableWidgetForm.action = "${addEditSectorTable}";
               document.sectorTableWidgetForm.target = "_self";
               document.sectorTableWidgetForm.submit();
           }

           function removeSectors() {
               <digi:context name="addEditSectorTable" property="context/widget/sectorTableManager.do~actType=remSectors" />
                       document.sectorTableWidgetForm.action = "${addEditSectorTable}";
                       document.sectorTableWidgetForm.target = "_self";
                       document.sectorTableWidgetForm.submit();

                   }

                   function moveUp(sectorId){
         <digi:context name="addEditSectorTable" property="context/widget/sectorTableManager.do~actType=reorderUp" />
                 document.sectorTableWidgetForm.action = "${addEditSectorTable}&sectorToReorderId="+sectorId;
                 document.sectorTableWidgetForm.target = "_self";
                 document.sectorTableWidgetForm.submit();

             }
             function moveDown(sectorId){
         <digi:context name="addEditSectorTable" property="context/widget/sectorTableManager.do~actType=reorderDown" />
                 document.sectorTableWidgetForm.action = "${addEditSectorTable}&sectorToReorderId="+sectorId;
                 document.sectorTableWidgetForm.target = "_self";
                 document.sectorTableWidgetForm.submit();
             }
             function checkDonorColumn(){
                 var donorCheckbox=document.getElementById("donorCheckbox").checked;
                 var hidden=document.getElementById("donorCheckboxHidden");
                 hidden.value=donorCheckbox;
               
                 var donorColumnYears=document.getElementsByName("donorColumnYear");
                 if(donorColumnYears!=null){
                     for(var i=0;i<donorColumnYears.length;i++){
                         if(donorCheckbox){
                              donorColumnYears[i].disabled=false;
                         }
                         else{
                             donorColumnYears[i].disabled=true;
                         }
                      
                     }
                 }
             }

   
             //-->
</script>

<digi:instance property="sectorTableWidgetForm" />
<digi:form action="/sectorTableManager.do~actType=save">

    <html:hidden name="sectorTableWidgetForm" property="sectorTableId"/>
    <html:hidden name="sectorTableWidgetForm" property="donorColumn" styleId="donorCheckboxHidden"/>
    <table width="1000" border="0" cellpadding="0" align=center style="font-size:12px;">
        <!--<tr>
            <td bgcolor=#f2f2f2 height=40 style="padding-left:10px;">
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

                    <digi:trn><b>Create/Edit Sector Table Widget</b></digi:trn>
                </span>
            </td>
        </tr>-->
        <tr>
            <td style="padding-top:10px; padding-bottom:10px;">
                
                    <c:if test="${empty sectorTableWidgetForm.sectorTableId}">
                        <digi:trn>Create Sector Table Widget</digi:trn>
                    </c:if>
                    <c:if test="${not empty sectorTableWidgetForm.sectorTableId}">
                        <digi:trn>Edit Sector Table Widget</digi:trn>
                    </c:if>
                
            </td>
        </tr>
        <tr>
            <td align=center style="padding-bottom:10px; padding-top:10px; border:1px solid #CCCCCC;" bgcolor=#f2f2f2>

                <table>
                    <tr>
                        <td colspan="2">
                            <digi:errors/>
                        </td>
                    </tr>

                    <tr>
                        <td nowrap="nowrap" align="left" colspan="2">
                            <font color="red">*</font>
                            <strong>
                                <digi:trn>Name</digi:trn>:&nbsp;
                            </strong>
                            <html:text name="sectorTableWidgetForm" styleClass="inputx" property="name"/>
                        </td>
                    </tr>

                    <tr>
                        <td nowrap="nowrap" align="left" colspan="2">
                            <fieldset>
                                <legend align="left"><font color="red">*</font><digi:trn>Sector</digi:trn></legend>
                                <table border="0" style="font-family:verdana;font-size:11px;border:1px solid silver;width:100%">
                                    <tr bgColor="#d7eafd">
                                        <td><digi:trn>Sector Name</digi:trn></td>
                                    </tr>
                                    <c:forEach var="sector" items="${sectorTableWidgetForm.sectors}" varStatus="status">
                                        <tr>
                                            <td>
                                                <html:multibox property="selectedSectors">
                                                    <c:out value="${sector.sector.ampSectorId}"/>
                                                </html:multibox>
                                                <c:out value="${sector.sector.name}"/>
                                            </td>
                                          
                                        </tr>
                                    </c:forEach>
                                </table>
                                <br/>
                                <input type="button" onclick="addSectors()" value="<digi:trn>Add Sector</digi:trn>" />
                                <c:if test="${!empty sectorTableWidgetForm.sectors}">
                                    <input type="button" onclick="removeSectors()" value="<digi:trn>Remove Sector(s)</digi:trn>" />
                                </c:if>
                            </fieldset>
                        </td>

                    </tr>

                    <tr>
                        <td nowrap="nowrap" align="left">
                            <fieldset>
                                <legend align="left"><digi:trn>Total Years Column</digi:trn></legend>
                                <c:forEach var="year" items="${sectorTableWidgetForm.years}" varStatus="status">
                                    <html:multibox property="selectedTotalYears">
                                        <c:out value="${year}"/>
                                    </html:multibox>
                                    <c:out value="${year}"/>
                                    <c:if test="${status.count%4==0}">
                                        <br/>
                                    </c:if>
                                </c:forEach>
                            </fieldset>
                        </td>
                        <td nowrap="nowrap" align="left">
                            <fieldset>
                                <legend align="left"><digi:trn>Percent Years Column</digi:trn></legend>
                                <c:forEach var="year" items="${sectorTableWidgetForm.years}"  varStatus="status">
                                    <html:multibox property="selectedPercentYears">
                                        <c:out value="${year}"/>
                                    </html:multibox>
                                    <c:out value="${year}"/>
                                    <c:if test="${status.count%4==0}">
                                        <br/>
                                    </c:if>
                                </c:forEach>

                            </fieldset>
                        </td>


                    </tr>
                    <tr>
                        <td colspan="2" style="padding-top:10px; padding-bottom:10px;">
                            <digi:trn>Places</digi:trn>:
                            <html:select name="sectorTableWidgetForm" property="selPlaces" style="width: 300px">
                                <html:optionsCollection name="sectorTableWidgetForm" property="places" value="id" label="name"/>
                            </html:select>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <html:checkbox name="sectorTableWidgetForm" property="donorColumn" onclick="checkDonorColumn()" styleId="donorCheckbox">
                                  <digi:trn>Add Donor column</digi:trn>:
                            </html:checkbox>
                            <br/>
                              <fieldset style="width:50%">
                                <legend align="left"><digi:trn>Donor Year</digi:trn></legend>
                                <c:forEach var="year" items="${sectorTableWidgetForm.years}" varStatus="status">
                                 <html:radio  name="sectorTableWidgetForm" property="donorColumnYear" value="${year}"/>${year}
                                 <c:if test="${status.count%4==0}">
                                        <br/>
                                  </c:if>
                                </c:forEach>
                            </fieldset>
                        </td>
                    </tr>

                    <tr>
                        <td align="right" style="padding-top:10px;">
                            <input type="button" onclick="validate()" value="<digi:trn>Next</digi:trn>"/>
                        </td>
                        <c:set var="trnCancel"><digi:trn key="aim:btn:cancel">Cancel</digi:trn></c:set> 
                        <td style="padding-top:10px;">
                            <input type="button" value="${trnCancel}" onclick="cancel()">
                        </td>
                    </tr>
                </table>

            </td>
        </tr>
    </table>
</digi:form>
