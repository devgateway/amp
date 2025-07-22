<%-- 
    Document   : newLocationManager
    Created on : May 28, 2008, 2:10:15 PM
    Author     : medea
--%>

<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>


<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript">
    function showRegions(selectId){
        var sel=document.getElementById(selectId);
        var ids=document.getElementById("hidden_ids").value;
        var newIds="";
        if(ids.length>0){ 
            var idsSplited=ids.substring(0,ids.lastIndexOf(",")).split(",");
            for(var i=0;i<selectId-1&&i<idsSplited.length;i++){
                newIds+=idsSplited[i]+",";
            }
        }
        var selectedId=sel.options[sel.selectedIndex].value;
        if(selectedId!='-1'){
            newIds+=selectedId+","; 
        }
        
        
        document.aimNewAddLocationForm.ids.value=newIds;
        document.aimNewAddLocationForm.submit();
    }
    
    function selectedParentId(selectedId,catValId,countryLevel){
        var sel=document.getElementById(selectedId-1);
        var selId=-1;
       
        if(sel!=null){
            selId=sel.options[sel.selectedIndex].value;
            if(selId==-1){
                alert("please select parent location");
                return false;
            }
            
        }
        document.aimNewAddLocationForm.parentLocationId.value=selId;
        document.aimNewAddLocationForm.parentCatValId.value=catValId;
        document.aimNewAddLocationForm.categoryLevelCountry.value=countryLevel
        document.aimNewAddLocationForm.event.value="add";
        document.aimNewAddLocationForm.action="/addNewLocation.do";
        
        document.aimNewAddLocationForm.submit();
        
      
       
        
    }
    function editId(selectedId,countryLevel){
        var sel=document.getElementById(selectedId);
        var selId=-1;
       
        if(sel!=null){
            selId=sel.options[sel.selectedIndex].value;
            if(selId==-1){
                alert("please select location to edit");
                return false;
            }
            
        }
        document.aimNewAddLocationForm.editedId.value=selId;
        document.aimNewAddLocationForm.categoryLevelCountry.value=countryLevel
        document.aimNewAddLocationForm.event.value="edit";
        document.aimNewAddLocationForm.action="/addNewLocation.do";
        
        document.aimNewAddLocationForm.submit();
        
    }
</script>


<digi:errors/>
<digi:context name="digiContext" property="context"/>
<digi:instance property="aimNewAddLocationForm" />

<digi:form action="/newLocationManager.do"  method="post">
    <!--  AMP Admin Logo -->
    <jsp:include page="teamPagesHeader.jsp"  />
    <!-- End of Logo -->
    <html:hidden name="aimNewAddLocationForm" property="ids" styleId="hidden_ids"/>
    <html:hidden name="aimNewAddLocationForm" property="parentLocationId" />
    <html:hidden property="categoryLevelCountry"/>
    <html:hidden property="parentCatValId" />
    <html:hidden property="editedId" />
    <html:hidden property="event" />
    <table cellPadding=5 cellspacing="0" width="912">
        <tr>
            <!-- Start Navigation -->
            <td height=33 width="900"><span class=crumb>
                    <c:set var="clickToViewAdmin">
                        <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
                    </c:set>
                    <digi:link href="/admin.do" styleClass="comment" title="${clickToViewAdmin}">
                        <digi:trn key="aim:AmpAdminHome">
                            Admin Home
                        </digi:trn>
                    </digi:link>&nbsp;&gt;&nbsp;
                    <digi:trn key="aim:regionManager"> Region Manager
                    </digi:trn>
                </span>
            </td>
            <!-- End navigation -->
        </tr>
        <tr>
            <td height=16 valign="center" width=1157 ><span class=subtitle-blue>
                    <digi:trn key="aim:regionManager">Region Manager</digi:trn>
                </span>
            </td>
        </tr>
        <tr>
            <td noWrap width=900 vAlign="top">
                <table width="965" cellspacing="1" cellspacing="1">
                    <tr>
                        <td noWrap width=663 vAlign="top">
                            
                            <table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="626" border="0">
                                <tr>
                                    <td bgColor=#ffffff class=box-border width="624">
                                        <table border="0" cellpadding="1" cellspacing="1" class=box-border width="784">
                                            <tr bgColor=#dddddb>
                                                <!-- header -->
                                                <td bgColor=#ffffff height="20" align="center" colspan="2" width="776"><B>
                                                    
                                                </td>
                                                <!-- end header -->
                                            </tr>
                                            <!-- Page Logic -->

                                        <c:forEach items="${aimNewAddLocationForm.categoryValues}" var="level" varStatus="index">
                                                <div id="${level.value}${index.count}">
                                                    <tr>
                                                        <td>
                                                            <digi:trn key="aim:regionmanager:${level.encodedValue}">${level.value}</digi:trn>
                                                        </td>
                                                        <td>
                                                        
                                                            
                                                            
                                                            
                                                            
                                                            <select onchange="showRegions('${index.count}')" id="${index.count}" >
                                                                <option value=-1>Select  ${level.value}</option>
                                                                
                                                                <c:forEach items="${aimNewAddLocationForm.locations}" var="loc">
                                                                    
                                                                    <c:set var="selectid" value="${index.count}"/>
                                                                    
                                                                    
                                                                   
                                                                    
                                                                    
                                                                    
                                                                    <c:set var="splitedWithoutComma" value="${fn:split(aimNewAddLocationForm.ids,',' )}"/>
                                                                    <c:set var="selected_id" value="${splitedWithoutComma[selectid-1]}"/>
                                                                    
                                                                    
                                                                    <c:if test="${loc.parentCategoryValue.id==level.id}">
                                                                        <c:if test="${selected_id==loc.id}">
                                                                            <option selected value="${loc.id}" >${loc.name}</option>
                                                                        </c:if>
                                                                        <c:if test="${selected_id!=loc.id}">
                                                                            <option value="${loc.id}">${loc.name}</option>
                                                                        </c:if>
                                                                    </c:if>
                                                                </c:forEach>
                                                            </select>
                                                            
                                                        </td>
                                                          <td nowrap> 
                                                            <input type="button" value="<digi:trn key="aim:AddNew${level.encodedValue}">Add New ${level.value}</digi:trn>"  title="${translation}" onclick="return selectedParentId('${index.count}','${level.id}','${level.country}')"/>
                                                            <input type="button" value="<digi:trn key="aim:Edit${level.encodedValue}">Edit ${level.value}</digi:trn>"  title="${translation}" onclick="return editId('${index.count}','${level.country}')"/>
                                                           
                                                        </td>
                                                        
                                                    </tr>
                                                 
                                                     <tr>
                                                        <td  colspan="3"> 
                                                          &nbsp;
                                                        </td>
                                                        
                                                    </tr>
                                                    
                                                </div>
                                                
                                                
                                            </c:forEach>
                                        </table>
                                    </td>
                                </tr>
                                
                                <tr>
                                    <td colspan="4" width="674">
                                        
                                    </td>
                                </tr>
                                
                            </table>
                        </td>
                    </tr>
                    
                </table>
            </td>
        </tr>
    </table>
    
    
    
    
    
</digi:form>
