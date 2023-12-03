<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script langauage="JavaScript">
    function onDelete() {
        var flag = confirm("Delete this Scheme?");
        return flag;
    }

    function saveClicked(){
            if(document.getElementById("configNameId").value==''){
                alert("<digi:trn>Please enter name</digi:trn>")
                return false;
            }
	   var id = document.getElementById("originalSectorId").value;
	   var newId = document.getElementById("sctClassId").value;
	   // Check if this is edit.
	   var sectorId = document.getElementById("id").value;
	   if(sectorId != "") {
		   if(id != newId) {
				var flag = confirm("<digi:trn jsFriendly='true'>Sectors for activities will have to be changed manually. Proceed anyway?</digi:trn>");
				if (!flag) {
					return false;
				}
		   }
	   }
       document.aimSectorClassConfigForm.event.value = "save";
       document.getElementById("originalSectorId").value = newId;
       return true;
    }
    
    var enterBinder	= new EnterHitBinder('addSectConfBtn');
    </script>
<div class="admin-content">
<digi:instance property="aimSectorClassConfigForm" />
<digi:context name="digiContext" property="context" />

<digi:form action="/updateSectorClassConfig.do" method="post">
    <!--  AMP Admin Logo -->
    <jsp:include page="teamPagesHeader.jsp"  />
    <!-- End of Logo -->
    <html:hidden name="aimSectorClassConfigForm" property="id" styleId="id"/>
    <html:hidden name="aimSectorClassConfigForm" property="sectorClassId" styleId="originalSectorId"/>
    <h1 class="admintitle"><digi:trn key="aim:scheme:sector">Sector Manager</digi:trn></h1>
    
    <table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
        <tr>
            <td class=r-dotted-lg width=14>&nbsp;</td>
            <td align=left class=r-dotted-lg valign="top" width=750>
                <table cellPadding=5 cellspacing="0" width="100%" border="0">
                    <tr>
                        <!-- Start Navigation -->
                        <td height=33><span class=crumb>
                            <c:set var="translation">
                                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
                            </c:set>
                            <digi:link href="/admin.do" styleClass="comment" title="${translation}" >
                                <digi:trn key="aim:AmpAdminHome">
                                    Admin Home
                                </digi:trn>
                            </digi:link>&nbsp;&gt;&nbsp;
                            <digi:link href="/getSectorClassConfig.do" styleClass="comment" title="${translation}" >
                                <digi:trn key="aim:aim:SectorClassificationsConfiguration:Configurations">
                                    Configurations
                                </digi:trn>
                            </digi:link>
                        </td>
                        <!-- End navigation -->
                    </tr>
                    <tr>
                        <td height=16 valign="center" width=571>
                            <digi:errors />
                        </td>
                    </tr>	
            
                    <tr>
                        <td noWrap width="100%" vAlign="top">
                            <table width="100%" cellspacing="1" cellspacing="1" border="0">
                                <tr><td noWrap width=600 vAlign="top">
                                        <table bgColor=#d7eafd cellpadding="1" cellspacing="1" width="100%" valign="top">
                                            <tr bgColor=#ffffff>
                                                <td vAlign="top" width="100%">
                                                    
                                                    <table width="100%" cellspacing="1" cellpadding="1" valign="top" align=left>	
                                                        
                                                        <tr>
                                                            <td>	
                                                                <table width="100%">
                                                                    <tr>
                                                                        <td>
                                                                            <digi:trn key="aim:SectorClassificationsConfiguration:ConfigurationName">Configuration Name</digi:trn><font size="2" color="#FF0000">*</font> :
                                                                        </td>
                                                                        <td>             
                                                                            <c:if test="${aimSectorClassConfigForm.id>0}">
                                                                 
                                                                                <html:textarea  name ="aimSectorClassConfigForm" property="configName" rows="1" cols= "35" readonly="true" styleId="configNameId"/> 
                                                                            
                                                                            </c:if>
                                                                             <c:if test="${empty aimSectorClassConfigForm.id||aimSectorClassConfigForm.id==0}">
                                                                                 <html:textarea  name ="aimSectorClassConfigForm" property="configName" rows="1" cols= "35" styleId="configNameId"/> 
                                                                            </c:if>
                                                                            
                                                                        </td>
                                                                    </tr>
                                                                     <tr>
                                                                        <td>
                                                                            <digi:trn key="aim:SectorClassificationsConfiguration:SelectSectorClassifications">Select Sector Classifications</digi:trn> :
                                                                        </td>
                                                                        <td>
                                                                           <html:select property="sectorClassId"  styleClass="inp-text" styleId="sctClassId">
									<html:optionsCollection name="aimSectorClassConfigForm" property="classifications" 
									value="ampSecSchemeId" label="secSchemeName" />												
												
                                                                            </html:select> 
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>
                                                                            <digi:trn key="aim:SectorClassificationsConfiguration:MultiSectorSelecting">Multi-Sector Selecting</digi:trn>:
                                                                        </td>
                                                                        <td>
                                                                            <html:select property="multiSectorSelecting">
                                                                                <html:option value="1"><digi:trn key="aim:SectorClassificationsConfiguration:on">on</digi:trn></html:option>
                                                                                <html:option value="2"><digi:trn key="aim:SectorClassificationsConfiguration:off">off</digi:trn></html:option>
                                                                            </html:select>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>
                                                                            <digi:trn>Description</digi:trn>:
                                                                        </td>
                                                                        <td>
                                                                            <html:textarea  name ="aimSectorClassConfigForm" property="configDescription" rows="1" cols= "35" styleId="configNameId"/>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;
                                                                            
                                                                        </td>
                                                                        <td >&nbsp;&nbsp;
                                                                        <html:submit  styleClass="dr-menu" property="event" onclick="return saveClicked()" styleId="addSectConfBtn">
                                                                            <digi:trn key="aim:SectorClassificationsConfiguration:Save">Save</digi:trn>
                                                                        </html:submit>
                                                                        <td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td colspan="2"><digi:trn>All fields marked with</digi:trn> <font size="2" color="#FF0000">*</font> <digi:trn>are required</digi:trn></td>
                                                                    </tr>
                                                                </table>
                                                            </td>
                                                            
                                                        </tr>	
                                                        
                                                        
                                                        <tr><td>
                                                                <table width="100%" cellspacing="1" cellpadding=4 valign="top" align=left bgcolor="#d7eafd">
                                                                    
                                                                    <!-- end page logic -->													
                                                                </table>
                                                        </td></tr>
                                                    </table>
                                                    
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    
                                    <td noWrap width="100%" vAlign="top">
                                        <table align="center" cellpadding="0" cellspacing="0" width="90%" border="0">	
                                            <tr>
                                                <td>
                                                    <!-- Other Links -->
                                                    <table cellpadding="0" cellspacing="0" width="120">
                                                        <tr>
                                                            <td bgColor=#c9c9c7 class=box-title>
                                                                <digi:trn key="aim:otherLinks">
                                                                    Other links
                                                                </digi:trn>
                                                            </td>
                                                            <td class="header-corner" height="17" width=17>&nbsp;
                                                                
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td bgColor=#ffffff class=box-border>
                                                    <table cellPadding=5 cellspacing="1" width="100%" class="inside">
                                                        <tr>
                                                            <td class="inside">
                                                                <digi:img src="ampModule/aim/images/arrow-014E86.gif" styleClass="list-item-image" width="15" height="10"/>
                                                                <c:set var="translation">
                                                                    <digi:trn key="aim:clickToAddScheme">Click here to Add a Scheme</digi:trn>
                                                                </c:set>
                                                                <digi:link href="/updateSectorSchemes.do?dest=admin&event=addscheme" title="${translation}" >
                                                                    <digi:trn key="aim:addScheme">
                                                                        Add Scheme
                                                                    </digi:trn>
                                                                </digi:link>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="inside">
                                                                <digi:img src="ampModule/aim/images/arrow-014E86.gif" styleClass="list-item-image" width="15" height="10"/>
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
                                                        <!-- end of other links -->
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                </td></tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    </digi:form>
 </div>