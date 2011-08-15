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
    <digi:trn key="aim:ConfirmDelete">Delete this Scheme ?</digi:trn>
    </c:set>
    function onDelete() {
        var flag = confirm("${translation}");
            return flag;
        }
        </script>
<digi:instance property="aimSectorClassConfigForm" />
<digi:context name="digiContext" property="context" />
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->
<html:hidden property="event" value="view"/>
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
    <tr>
        <td class=r-dotted-lg width=14>&nbsp;</td>
        <td align=left class=r-dotted-lg valign="top" width=750>
            <table cellPadding=5 cellspacing="0" width="100%" border="0">
                <tr>
                    <!-- Start Navigation -->
                    <td height=33><span class=crumb>
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
                    </td>
                    <!-- End navigation -->
                </tr>
                <tr>
                    <td height=16 valign="center" width=571><span class=subtitle-blue><digi:trn key="aim:ClassificationConfigurations">Classification Configurations</digi:trn></span>
                    </td>
                </tr>
                <tr>
                    <td height=16 valign="center" width=571>
                        <digi:errors />
                    </td>
                </tr>
                <tr>
                    <td noWrap width="100%" vAlign="top">
                        <table width="100%" cellspacing="1" cellspacing="1" border="0">
                            <tr>
                            	<td noWrap width=600 vAlign="top">
                            		<table bgColor=#d7eafd cellpadding="1" cellspacing="1" width="100%" valign="top">
                                		<tr bgColor=#ffffff>
                                    		<td vAlign="top" width="100%">
                                        		<table width="100%" cellspacing="1" cellpadding="1" valign="top" align=left>
                                                	<tr>
                                                		<td bgColor=#d7eafd class=box-title height="20" align="center">
                                                            <!-- Table title -->
                                                    		<digi:trn key="aim:ManageClassificationsConfigurations">Manage Classifications Configurations</digi:trn>
                                                            <!-- end table title -->
                                                    	</td>
                                                	</tr>
                                                	<tr>
                                               			<td>
                                                    		<table width="90%" cellspacing="1" cellpadding=4 valign="top" align=left bgcolor="#d7eafd">
                                                        		<logic:notEmpty name="aimSectorClassConfigForm" property="classifications">
                                                            		<logic:iterate name="aimSectorClassConfigForm" property="classificationConfigs" id="classConfig"
																				type="org.digijava.module.aim.dbentity.AmpClassificationConfiguration">
                                                                 		<tr>
                                                                    		<td width="256" bgcolor="#ffffff">
                                                                        		<digi:trn>${classConfig.name}</digi:trn>
                                                                         	</td>
                                                         					<td bgcolor="#ffffff" width="80px" align="right">
                                                                        		<c:set var="trnEditScheme">
                                                                            		<digi:trn key="aim:clickToEdit">Click here to Edit</digi:trn>
                                                                              	</c:set>
                                                                                [ <digi:link href="/updateSectorClassConfig.do?event=edit&id=${classConfig.id}"  title="${trnEditScheme}">
                                                                                	<digi:trn key="aim:edit">edit</digi:trn>
                                                                                </digi:link>]													  
                                                                             </td>
                                                                             <td bgcolor="#ffffff" width="80px" align="left">
                                                                        		<c:set var="trnEditScheme">
                                                                            		<digi:trn key="aim:clickToDeleteClassification">Click here to Delete a Classification</digi:trn>
                                                                              	</c:set>
                                                                                [ <digi:link href="/updateSectorClassConfig.do?event=delete&id=${classConfig.id}"  title="${trnEditScheme}">
                                                                                	 <digi:trn key="aim:delete">Delete</digi:trn>
                                                                                </digi:link>]													  
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
                                        </tr>
                                    </table>
                                </td>
                                
                                <td noWrap width="100%" vAlign="top">
                                    <table align="center" cellpadding="0" cellspacing="0" width="90%" border="0">
                                        <tr>
                                            <td>
                                                <!-- Other Links -->
                                                <table cellpadding="0" cellspacing="0" width="20"0>
                                                    <tr>
                                                        <td bgColor=#c9c9c7 class=box-title>
                                                            <digi:trn key="aim:otherLinks">
                                                                Other links
                                                            </digi:trn>
                                                        </td>
                                                        <td background="module/aim/images/corner-r.gif" height="17" width=17>&nbsp;</td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td bgColor=#ffffff class=box-border>
                                                <table cellPadding=5 cellspacing="1" width="100%">
                                                  <tr>
                                                  	<td>
                                                    	<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
                                                        <c:set var="trnAddScheme">
                                                         	<digi:trn key="aim:clickToAddConfiguration">Click here to Add a Configuration</digi:trn>
                                                         </c:set>
                                                      	<digi:link href="/updateSectorClassConfig.do?event=add" title="${trnAddScheme}" >
                                                          <digi:trn key="aim:AddConfiguration">Add Configuration</digi:trn>
                                                           </digi:link>
                                                          </td>
                                                        </tr>
                                              		 <tr>
                                                        <td>
                                                            <digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
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


