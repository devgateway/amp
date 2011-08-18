<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<jsp:include page="/repository/aim/view/addSectors.jsp"  />
<script type="text/javascript">
    <!--
    window.onload=initSectorScript();
    function addSectors() {
       /*openNewWindow(600, 450);
       <digi:context name="selSec" property="context/aim/selectSectors.do" />
       document.gisIndicatorSectorRegionForm.action = "${selSec}";
       document.gisIndicatorSectorRegionForm.target = popupPointer.name;
       document.gisIndicatorSectorRegionForm.submit();
       */
       myAddSectors("");
    }
              
    function addSector(param){
       <digi:context name="addSec" property="context/widget/indSectRegManager.do~actType=selectSector" />
       document.gisIndicatorSectorRegionForm.action = "${addSec}";
       document.gisIndicatorSectorRegionForm.target = "_self";
       document.gisIndicatorSectorRegionForm.submit();
    }
    function validate(){
       if(document.gisIndicatorSectorRegionForm.selIndicator.value=='-1'){
           alert("please select indicator");
           return false;
       }
       if(document.gisIndicatorSectorRegionForm.sectorHidden.value==''){
           alert("please select sector");
           return false;
       }
       if(document.gisIndicatorSectorRegionForm.selRegionId.value=='-1'){
           alert("please select region");
           return false;
       }
       
       save();
    }
                    
    function cancel(){
       <digi:context name="addEditIndVal" property="/widget/indSectRegManager.do~actType=cancel" />
       document.gisIndicatorSectorRegionForm.action = "${addEditIndVal}";
       document.gisIndicatorSectorRegionForm.submit();
    }
              
    /*
    * we need this save method because 
    *  someone may press cancel 
    *  on the sector selection popup
    */
    function save(){
       <digi:context name="addEditIndVal" property="/widget/indSectRegManager.do~actType=save" />
       document.gisIndicatorSectorRegionForm.action = "${addEditIndVal}";
       document.gisIndicatorSectorRegionForm.target = "_self";
       document.gisIndicatorSectorRegionForm.submit();
    }
    //-->
</script>

<digi:instance property="gisIndicatorSectorRegionForm" />
<digi:form action="/indSectRegManager.do~actType=save">
    <input type="hidden" value="${gisIndicatorSectorRegionForm.sector}" name="sectorHidden"/>
    <html:hidden name="gisIndicatorSectorRegionForm" property="indSectId"/>
    <table width="60%" border="0" cellpadding="15">
        <tr>
            <td>
                <span class="crumb">
                    <c:set var="translation">
                        <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
                    </c:set>
                    <html:link  href="/aim/admin.do" styleClass="comment" title="${translation}" >
                        <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
                    </html:link>&nbsp;&gt;&nbsp;
                    <html:link  href="/widget/indSectRegManager.do~actType=viewAll" styleClass="comment">
                        <digi:trn key="gis:Navigation:ResultsDashboardDataManager">Results Dashboard Data Manager</digi:trn>
                    </html:link>
                    &nbsp;&gt;&nbsp;
                    
                    <digi:trn key="gis:Navigation:createIndicatorSector">Create/Edit Indicator Sector</digi:trn>
                </span>
            </td>
        </tr>
        <tr>
            <td>
                <span class="subtitle-blue">
                    <c:if test="${empty gisIndicatorSectorRegionForm.indSectId}">
                        <digi:trn key="gis:createIndicatorSector">Create Indicator Sector</digi:trn>
                    </c:if>
                    <c:if test="${not empty gisIndicatorSectorRegionForm.indSectId}">
                        <digi:trn key="gis:editIndicatorSector">Edit Indicator Sector</digi:trn>
                    </c:if>
                </span>
            </td>
        </tr>
        <tr>
            <td>
                
                <table>
                    <tr>
                     <td colspan="3">
                            <digi:errors/>
                    </td>
                    </tr>
                    
                    <tr>
                        <td nowrap="nowrap" align="left">
                            <font color="red">*</font>
                            <strong>
                                <digi:trn key="gis:createIndicatorSector:selIndicator">Indicator</digi:trn>:&nbsp;
                            </strong>
                        </td>
                        <td  colspan="2">
                            <html:select name="gisIndicatorSectorRegionForm" property="selIndicator">
                                <html:option value="-1">Select Indicator</html:option>
                                <html:optionsCollection name="gisIndicatorSectorRegionForm" property="indicators" label="name" value="indicatorId"/>
                            </html:select>
                        </td>
                        
                    </tr>
                    <tr>
                        <td colspan="3">&nbsp;</td>
                    </tr>
                    <tr>
                        <td nowrap="nowrap" align="left">
                            <font color="red">*</font>
                            <strong>
                                <digi:trn key="gis:createIndicatorSector:selSector">Sector</digi:trn>:&nbsp;
                            </strong>
                        </td>
                        
                        
                        <c:if test="${empty gisIndicatorSectorRegionForm.sector}">
                        
                            <td colspan="2" align="left">
                                <input type="button" onclick="addSectors()" value="<digi:trn key='gis:createIndicatorSector:addSector'>Add Sector</digi:trn>" >
                            </td>
                        </c:if>
                        <c:if test="${not empty gisIndicatorSectorRegionForm.sector}">
                            <td>
                                ${gisIndicatorSectorRegionForm.sector.name}
                            </td>
                            <td>
                                <input type="button" onclick="addSectors()" value="<digi:trn key='gis:createIndicatorSector:changeSector'>Change Sector</digi:trn>" >
                            </td>
                        </c:if>
                        
                   
                     
                     
                    </tr>
                    <tr>
                        <td colspan="3">&nbsp;</td>
                    </tr>
                    <tr>
                        <td nowrap="nowrap" align="left">
                            <font color="red">*</font>
                            <strong>
                                <digi:trn key="gis:createIndicatorSector:selRegion">Region</digi:trn>:&nbsp;
                            </strong>
                        </td>
                        <td  colspan="2">
                            <html:select name="gisIndicatorSectorRegionForm" property="selRegionId">
                                <html:option value="-1">Select Region</html:option>
                                 <html:option value="-2">All</html:option>
                                 <html:option value="-3">National</html:option>
                                <html:optionsCollection name="gisIndicatorSectorRegionForm" property="regions" label="name" value="id"/>
                            </html:select>
                        </td>
                        
                    </tr>
                    
                    <tr>
                        <td colspan="3">
                            <hr>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">
                             <input type="button" onclick="validate()" value="<digi:trn>Save</digi:trn>"/>
                        </td>
                        <td>
                            <input type="button" value="Cancel" onclick="cancel()">
                        </td>
                    </tr>
                </table>
                
            </td>
        </tr>
    </table>
</digi:form>
