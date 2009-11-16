<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<link rel="stylesheet" type="text/css" href="/repository/xmlpatcher/css/fonts-min.css" />
<link rel="stylesheet" type="text/css" href="/repository/xmlpatcher/css/datatable.css" />
<link rel="stylesheet" type="text/css" href="/repository/xmlpatcher/css/paginator.css" />

<!-- Individual YUI JS files --> 
<script type="text/javascript" src="/repository/xmlpatcher/js/yahoo-dom-event.js"></script> 
<script type="text/javascript" src="/repository/xmlpatcher/js/connection-min.js"></script> 
<script type="text/javascript" src="/repository/xmlpatcher/js/element-min.js"></script> 
<script type="text/javascript" src="/repository/xmlpatcher/js/datasource-min.js"></script> 
<script type="text/javascript" src="/repository/xmlpatcher/js/datatable-min.js"></script> 
<script type="text/javascript" src="/repository/xmlpatcher/js/json-min.js"></script> 
<script type="text/javascript" src="/repository/xmlpatcher/js/yahoo-min.js"></script> 
<script type="text/javascript" src="/repository/xmlpatcher/js/event-min.js"></script> 
<script type="text/javascript" src="/repository/xmlpatcher/js/paginator-min.js"></script> 

<script language="JavaScript">

YAHOO.util.Event.addListener(window, "load", function() {
	
    YAHOO.example.XHR_JSON = new function() {
    	
       	         
        this.formatActions = function(elCell, oRecord, oColumn, sData) {
            elCell.innerHTML = "<a href=/um/viewEditUser.do~id=" +sData+">" +"<img vspace='2' border='0' src='/repository/message/view/images/edit.gif'/>" + "</a>";
            elCell.innerHTML +="&nbsp;&nbsp;<a onclick='return banUser();' title='Ban User' href=/um/viewEditUser.do~id=" +sData+"~ban=true>" +"<img vspace='2' border='0' src='/TEMPLATE/ampTemplate/images/deleteIcon.gif'/>" + "</a>";
        };
 
        this.myDataSource = new YAHOO.util.DataSource("/um/userSearch.do?");
        this.myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
        //this.myDataSource.connXhrMode = "queueRequests";
        this.myDataSource.responseSchema = {
            resultsList: "users",
            fields: ["ID","name","email","workspaces","actions"],
            metaFields: {
            	totalRecords: "totalRecords" // Access to value in the server response
        	}    
        };
        
        
        var myColumnDefs = [
            //{key:"ID", label:"ID"},
            {key:"name", label:"NAME", sortable:true},
            {key:"email", label:"EMAIL", sortable:true},
            {key:"workspaces", label:"WORKSPACES"},
            {key:"actions", label:"ACTION"}
            //{key:"actions", label:"ACTION", formatter:this.formatActions}
        ];
  
        var div = document.getElementById('errors');

        var handleSuccess = function(o){
        	if(o.responseText != undefined){
        		o.argument.oArgs.liner_element.innerHTML=o.responseText;
        	}
        }

        var handleFailure = function(o){
        	if(o.responseText !== undefined){
        		div.innerHTML = "<li>Transaction id: " + o.tId + "</li>";
        		div.innerHTML += "<li>HTTP status: " + o.status + "</li>";
        		div.innerHTML += "<li>Status code message: " + o.statusText + "</li>";
        	}
        }
        // Create the Paginator 
        var myPaginator = new YAHOO.widget.Paginator({ 
        	rowsPerPage:10,
        	containers : ["dt-pag-nav"], 
        	template : "{FirstPageLink} {PreviousPageLink} {PageLinks} {NextPageLink} {LastPageLink}   {CurrentPageReport}  {RowsPerPageDropdown}", 
        	pageReportTemplate : "Showing items {startIndex} - {endIndex} of {totalRecords}", 
        	rowsPerPageOptions : [10,25,50,100] 
        });   
        var myConfigs = {
            initialRequest: "sort=name&dir=asc&startIndex=0&results=10", // Initial request for first page of data
            dynamicData: true, // Enables dynamic server-driven data
            sortedBy : {key:"name", dir:YAHOO.widget.DataTable.CLASS_ASC}, // Sets UI initial sort arrow
            //paginator: new YAHOO.widget.Paginator({ rowsPerPage:10 }) // Enables pagination
            paginator:myPaginator
        };
    	 
        this.myDataTable = new YAHOO.widget.DataTable("dynamicdata", myColumnDefs, this.myDataSource, myConfigs);
        this.myDataTable.subscribe("rowMouseoverEvent", this.myDataTable.onEventHighlightRow); 
        this.myDataTable.subscribe("rowMouseoutEvent", this.myDataTable.onEventUnhighlightRow);
    
       // Update totalRecords on the fly with value from server
       this.myDataTable.handleDataReturnPayload = function(oRequest, oResponse, oPayload) {
           oPayload.totalRecords = oResponse.meta.totalRecords;
           return oPayload;
       }
       
    };
    
});
</script>


<c:set var="translationBan">
	<digi:trn key="um:confirmBanMsg">Do you really want to ban the user ?</digi:trn>
</c:set>

<c:set var="translationUnban">
	<digi:trn key="um:confirmUnbanMsg">Do you really want to remove the ban ?</digi:trn>
</c:set>

<script language="JavaScript">


function banUser(txt) {
  var ban=confirm("${translationBan}");
  return ban;
  }
  
 function unbanUser(txt) {
  var ban=confirm("${translationUnban}");
  return ban;
  }
  
  
  function searchAlpha(val) {
		     document.umViewAllUsersForm.action = "/um/viewAllUsers.do?currentAlpha="+val;
		     document.umViewAllUsersForm.submit();
			 return true;		
	}

</script>

<digi:instance property="umViewAllUsersForm" />
<digi:context name="digiContext" property="context" />

<digi:form action="/viewAllUsers.do" method="post">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=850>
    <tr>
      		<td align=left vAlign=top>
		        <table cellPadding=5 cellSpacing=0 width="100%">
		          	<tr>
		            <!-- Start Navigation -->
			            <td height=33>
			              <span class=crumb>
			                <c:set var="translation">
			                  <digi:trn key="um:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
			                </c:set>
			                <digi:link module="aim" href="/admin.do" styleClass="comment" title="${translation}" >
			                  <digi:trn key="um:AmpAdminHome">
			                  Admin Home
			                  </digi:trn>
			                </digi:link>&nbsp;&gt;&nbsp;
			
			                <digi:trn key="um:users">
			                Users
			                </digi:trn>
			              </span>
			            </td>
		            <!-- End navigation -->
		          	</tr>
		         	<tr>
			            <td>
			              <digi:errors/> 
			              <span class=subtitle-blue>
			                <digi:trn key="um:viewAllUsers:ListOfUsers">
			                List of users
			                </digi:trn>
			              </span>
			            </td>
		          	</tr>
		        	<tr style="width:50%;">
			          	<c:choose>
			          		<c:when test="${umViewAllUsersForm.showBanned}">
			          			<td width="350">
					              <digi:trn key="um:viewAllUsers:filter">
					              	Filter by:
					              </digi:trn>
					              <html:select property="type" style="font-family:verdana;font-size:11px;" disabled="true" onchange="document.umViewAllUsersForm.submit()">
					                <c:set var="translation">
					                  <digi:trn key="um:viewAllUsers:all">
					                  -All-
					                  </digi:trn>
					                </c:set>
					                <html:option value="-1">${translation}</html:option>
					              </html:select>
				              </td>
			          		</c:when>
			          		<c:otherwise>
					            <td width="350px;" >
						        	<digi:trn key="um:viewAllUsers:filter">
						              	Filter by:
						            </digi:trn>
						            <html:select property="type" style="font-family:verdana;font-size:11px; margin-right:70px;" onchange="document.umViewAllUsersForm.submit()">
						                <c:set var="translation">
						                 	<digi:trn key="um:viewAllUsers:all">
						                 	 -All-
						                  	</digi:trn>
						                </c:set>
						                <html:option value="-1">${translation}</html:option>
										<c:set var="translation">
						                  <digi:trn key="um:viewAllUsers:regisetred">
						                  Registered
						                  </digi:trn>
						                </c:set>
						                <html:option value="0">${translation}</html:option>
										<c:set var="translation">
						                  <digi:trn key="um:viewAllUsers:workspaceMembers">
						                  Workspace members
						                  </digi:trn>
						                </c:set>
						                <html:option value="1">${translation}</html:option>	                
						            </html:select>
					           	</td>  
				            </c:otherwise>
			            </c:choose>
			            <!--  <td width="300px" style="background-color:#99FF66">  -->
			            <c:if test="${not empty umViewAllUsersForm.currentAlpha}">
					    	<c:if test="${umViewAllUsersForm.currentAlpha!='viewAll'}">
						    	<c:if test="${umViewAllUsersForm.currentAlpha!=''}">														    	
							    	<c:set var="trnViewAllLink">
										<digi:trn key="aim:clickToViewAllSearchPages">Click here to view all search pages</digi:trn>
									</c:set>
									<a href="javascript:searchAlpha('viewAll')" title="${trnViewAllLink}">
										<digi:trn key="aim:viewAllLink"><!-- viewAll  --></digi:trn></a>
								</c:if>
							</c:if>
					    </c:if>
						<td width="300px;" >
			            	<digi:trn>Go to:</digi:trn>
							<html:select property="currentAlpha" style="font-family:verdana;font-size:11px; margin-right:100px;" onchange="document.umViewAllUsersForm.submit()">
								<html:option value="viewAll">-All-</html:option>
								<c:if test="${not empty umViewAllUsersForm.alphaPages}">
								<logic:iterate name="umViewAllUsersForm" property="alphaPages" id="alphaPages" type="java.lang.String">
									<c:if test="${alphaPages != null}">
										<html:option value="<%=alphaPages %>"><%=alphaPages %></html:option>
									</c:if>
								</logic:iterate>
								</c:if>
							</html:select>
						</td>
						<td width="850px;" >
				   		    <digi:trn key="um:viewAllUsers:keyword">
					        Keyword:
					        </digi:trn>
					        <html:text property="keyword" style="font-family:verdana;font-size:11px; width:130px; text-align:left;"/>
							<c:set var="translation">
								<digi:trn key="um:viewAllUsers:showButton">
									 Show
								</digi:trn>
							</c:set>
							<input type="submit" value="${translation}"  class="dr-menu" style="font-family:verdana;font-size:11px;" />
					    </td>
			            <td>
			
						</td>
		        	</tr>
		        	<tr>
		            <td noWrap width=817 vAlign="top" colspan="7">
		            	<table width="100%" cellspacing=1 cellSpacing=1>
							<tr>
								<td vAlign="top"> 
									<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%"  >
										<tr >
											<td vAlign="top" width="100%">
											</td>
										</tr>
										<tr >
											<td valign="top">
												<table align=center bgColor=#ffffff cellPadding=0 cellSpacing=0  border=1 >
													<tr>
														<td bgColor=#ffffff >
															<table border=0 cellPadding=0 cellSpacing=0  width="100%">
																<tr bgColor=#ffffff>
																	<!-- header -->
																	<td height="" align="center" colspan="5"><B>
																	<!-- <digi:trn key="um:users"></digi:trn> -->
		                                                              </b>
																	</td>
																	<!-- end header -->
																</tr>		
		 														<tr>
																	<td width="100%">
																		<div class='yui-skin-sam'>
																			<div id="dt-pag-nav"></div>
																			<div id="dynamicdata"></div>
																			<div id="errors"></div>
																		</div>
																	</td>
																</tr>
																<tr>
															
																</tr>
																<logic:notEmpty name="umViewAllUsersForm" property="alphaPages">
																	<tr>
																		<td align="center" colspan="4">
																	
																		</td>
																	</tr>
																</logic:notEmpty>									
																<logic:notEmpty name="umViewAllUsersForm" property="alphaPages">
																	<tr>
																		<td bgColor=#ffffff>
																			<c:if test="${not empty umViewAllUsersForm.currentAlpha}">
																				<c:if test="${umViewAllUsersForm.currentAlpha!='viewAll'}">
																			   		<c:if test="${umViewAllUsersForm.currentAlpha!=''}">														    	
																			    		<digi:trn key="um:UserMan:alphaFilterNote">
																							Go to -All- to see all existing Users.
																						</digi:trn>
																					</c:if>
																				</c:if>
																			</c:if>										
																		</td>
																	</tr>
																</logic:notEmpty>	
							                         		</table>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
									        <td>
												<table>
									             	<tr>
									                 	<td colspan="2">
									                 		<strong><digi:trn key="aim:IconReference">Icons Reference</digi:trn></strong>
									       				</td>
									       			</tr>
									     			<tr>
									           			<td nowrap="nowrap"><img src= "/repository/message/view/images/edit.gif" vspace="2" border="0" align="absmiddle" />
									               			<digi:trn key="aim:ClickEditUser">Click on this icon to edit the user&nbsp;</digi:trn>
									               			<br />
									       				</td>
									       			</tr>
									        		<tr>
														<c:choose>
		                                                  <c:when test="${umViewAllUsersForm.showBanned}">
															<td nowrap="nowrap"><img src= "/TEMPLATE/ampTemplate/images/green_check_16.png" vspace="2" border="0" align="absmiddle" />
									               				<digi:trn key="aim:ClickUnbanUser">Click on this icon to unban the user&nbsp;</digi:trn>
									                   			<br />
															</td>
		                                                  </c:when>
		                                                  <c:otherwise>
																<td nowrap="nowrap"><img src= "/TEMPLATE/ampTemplate/images/deleteIcon.gif" vspace="2" border="0" align="absmiddle" />
									               					<digi:trn key="aim:ClickBanUser">Click on this icon to ban the user&nbsp;</digi:trn>
									                   			<br />
																</td>
		                                                    </c:otherwise>
		                                                </c:choose>	
									           		</tr>
									       		</table>
									     	</td>
									    </tr> 
									</table>
							    </td>
								<td noWrap width="180" vAlign="top">
									<table align=center cellPadding=0 cellSpacing=0 width="100%" border=0>
										<tr>
											<td bgColor=#c9c9c7 class=box-title height="20">
												<digi:trn key="aim:Links">
												Links
												</digi:trn>
											</td>
										</tr>
										<tr>
											<td bgColor=#ffffff class=box-border>
												<table cellPadding=5 cellSpacing=1 width="100%">
													<tr>
														<td>
															<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
														</td>
														<td>
															<digi:link module="aim"  href="/../um/addUser.do">
										 						<digi:trn key="aim:addNewUser">
																	Add new user
																</digi:trn>
															</digi:link>
														</td>
													</tr>																								
													<tr>
														<td>
															<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>
														<td>
															<digi:link module="aim"  href="/admin.do">
															<digi:trn key="aim:AmpAdminHome">
															Admin Home
															</digi:trn>
															</digi:link>
														</td>
													</tr>
													<tr>
														<td>
															<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>
														<td>
															<digi:link  module="aim" href="/workspaceManager.do~page=1">
															<digi:trn key="aim:WorkspaceManager">
															Workspace Manager
															</digi:trn>
															</digi:link>
														</td>
													</tr>
													<c:choose>
														<c:when test="${umViewAllUsersForm.showBanned}">
															<tr>
																<td>
																	<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>
																<td>
																	<digi:link  module="aim" href="/../um/viewAllUsers.do~showBanned=false">
																	<digi:trn key="aim:ViewActiveUsers">
																	View Active Users
																	</digi:trn>
																	</digi:link>
																</td>
															</tr>
														</c:when>
														<c:otherwise>
															<tr>
																<td>
																	<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>
																<td>
																	<digi:link  module="aim" href="/../um/viewAllUsers.do~showBanned=true">
																	<digi:trn key="aim:ViewBannedUsers">
																	View Banned Users
																	</digi:trn>
																	</digi:link>
																</td>
															</tr>
														</c:otherwise>
													</c:choose>
													<!-- end of other links -->
												</table>
											</td>
										</tr>
									</table>
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



