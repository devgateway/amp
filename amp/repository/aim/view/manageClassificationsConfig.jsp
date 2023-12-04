<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<script langauage="JavaScript">
    <c:set var="translation">
    <digi:trn>Delete this Classification?</digi:trn>
    </c:set>
    function onDelete(url) {
        var flag = validate();
            if (flag)
            	window.location = url; 
        }
    
    function validate(){
    	return(confirm('${translation}'));
    }

    
</script>
<div class="admin-content">
<digi:instance property="aimSectorClassConfigForm" />
<digi:context name="digiContext" property="context" />
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->
<html:hidden property="event" value="view"/>
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000>
    <tr>
        <td class=r-dotted-lg width=14>&nbsp;</td>
        <td align=left class=r-dotted-lg valign="top" width=750>
            <table cellPadding=5 cellspacing="0" width="100%" border="0">
               <!-- <tr> -->
                    <!-- Start Navigation -->
                   <!-- <td height=33><span class=crumb>
                        <c:set var="clickToViewAdmin">
                            <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
                        </c:set>
                        <digi:link href="/admin.do" styleClass="comment" title="${clickToViewAdmin}" >
                            <digi:trn key="aim:AmpAdminHome">
                                Admin Home
                            </digi:trn>
                        </digi:link>&nbsp;&gt;&nbsp;
                        <digi:trn key="aim:sectorManager">
                            Sector Manager
                        </digi:trn>
                    </td> -->
                    <!-- End navigation -->
                <!-- </tr> -->
                <tr>
                    <td height=16 valign="center" width=571><h1 class="admintitle"><digi:trn key="aim:ClassificationConfigurations">Classification Configurations</digi:trn></h1>
                    </td>
                </tr>
                <tr>
                    <td height=16 valign="center" width=571>
                        <digi:errors />
                    </td>
                </tr>
                <tr>
                    <td noWrap width="100%" vAlign="top">
                        <table>
                            <tr>
                            	<td noWrap width=750 vAlign="top">
                            		<table width="100%" valign="top">
                                		<tr bgColor=#ffffff>
                                    		<td vAlign="top" width="100%">
                                        		<table width="100%" cellspacing="1" cellpadding="1" valign="top" align=left class="inside">
                                                	<thead>
                                                	<tr>
                                                		<td colspan="3" bgColor=#c7d4db class=box-title height="25" align="center" >
                                                            <!-- Table title -->
                                                    		<b style="font-size:12px;"><digi:trn key="aim:ManageClassificationsConfigurations">	</digi:trn></b>
                                                            <!-- end table title -->
                                                    	</td>
                                                	</tr>
                                                	</thead>
                                                	<logic:notEmpty name="aimSectorClassConfigForm" property="classifications">
                                                 		<logic:iterate name="aimSectorClassConfigForm" property="classificationConfigs" id="classConfig"
															type="org.digijava.module.aim.dbentity.AmpClassificationConfiguration">
                                                      		<tr>
                                                         		<td width="80%" bgcolor="#ffffff" class="inside">
                                                             		<c:out value="${classConfig.name}"/>
                                                              	</td>
                                              					<td bgcolor="#ffffff" width="10%" align="center" class="inside ignore">
                                                             		<c:set var="trnEditScheme">
                                                                 		<digi:trn key="aim:clickToEdit">Click here to Edit</digi:trn>
                                                                   	</c:set>
                                                                     [ <digi:link href="/updateSectorClassConfig.do?event=edit&id=${classConfig.id}"  title="${trnEditScheme}">
                                                                     	<digi:trn key="aim:edit">edit</digi:trn>
                                                                     </digi:link>]													  
                                                                  </td>
                                                                 <td bgcolor="#ffffff" width="10%" align="center" class="inside ignore">
                                                                   	<c:if test="${!classConfig.used}">
                                                             		<c:set var="trnEditScheme">
                                                                 		<digi:trn key="aim:clickToDeleteClassification">Click here to Delete a Classification</digi:trn>
                                                                   	</c:set>
                                                                     [ <digi:link onclick="onDelete('/updateSectorClassConfig.do?event=delete&id=${classConfig.id}'); return false;" href="/getSectorClassConfig.do"  title="${trnEditScheme}">
                                                                     	 <digi:trn key="aim:delete">Delete</digi:trn>
                                                                     </digi:link>]	
                                                                     </c:if>												  
                                                                  </td>
                                                             	</tr>
                                                     	</logic:iterate>
                                                    </logic:notEmpty>
                                                     <!-- end page logic -->
                                                           
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
															<b style="font-size:12px; padding-left:5px;">
																<digi:trn key="aim:otherLinks">
																	Other links
																</digi:trn>
															</b>
														</td>
														<td class="header-corner" height="17" width=17></td>
													</tr>
												</table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td bgColor=#ffffff class=box-border>
                                                <table cellPadding=5 cellspacing="1" width="100%" class="inside">
                                                  <tr>
                                                  	<td class="inside">
                                                    	<digi:img src="module/aim/images/arrow-014E86.gif" styleClass="list-item-image" width="15" height="10"/>
                                                        <c:set var="trnAddScheme">
                                                         	<digi:trn key="aim:clickToAddConfiguration">Click here to Add a Configuration</digi:trn>
                                                         </c:set>
                                                      	<digi:link href="/updateSectorClassConfig.do?event=add" title="${trnAddScheme}" >
                                                          <digi:trn key="aim:AddConfiguration">Add Configuration</digi:trn>
                                                           </digi:link>
                                                          </td>
                                                        </tr>
                                              		 <tr>
                                                        <td class="inside">
                                                            <digi:img src="module/aim/images/arrow-014E86.gif" styleClass="list-item-image" width="15" height="10"/>
                                                            <c:set var="trnViewAdmin">
                                                                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
                                                            </c:set>
                                                            <digi:link href="/admin.do" title="${trnViewAdmin}" >
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
</div>

