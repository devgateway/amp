<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script type="text/javascript">
    <!--
    function addSectors() {
        openNewWindow(600, 450);
          <digi:context name="selSec" property="context/aim/selectSectors.do" />
                  document.gisIndicatorSectorRegionForm.action = "${selSec}";
                  document.gisIndicatorSectorRegionForm.target = popupPointer.name;
                  document.gisIndicatorSectorRegionForm.submit();
              }

              function addSector(param)
              {
                <digi:context name="addSec" property="context/gis/indSectRegManager.do~actType=selectSector" />
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
                       
                        return true;
                    }
                    
                    function cancel(){
        <digi:context name="addEditIndVal" property="/gis/indSectRegManager.do~actType=cancel" />
                document.gisIndicatorSectorRegionForm.action = "${addEditIndVal}";
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
                    <html:link  href="/gis/indSectRegManager.do~actType=viewAll" styleClass="comment">
                        <digi:trn key="gis:Navigation:indicatorSectorRegionManager">Indicator Sector Region Manager</digi:trn>
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
                                <html:option value="-1">Slect Region</html:option>
                                <html:optionsCollection name="gisIndicatorSectorRegionForm" property="regions" label="name" value="ampRegionId"/>
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
                            <html:submit onclick="return validate()">
                                <digi:trn key="gis:createIndicatorSector:btnSave">Save</digi:trn>
                            </html:submit>
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
