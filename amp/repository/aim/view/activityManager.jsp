<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.digijava.module.aim.form.ActivityForm"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript">
<!--
	<digi:context name="searchOrg" property="context/module/moduleinstance/activityManager.do"/>
	
	function deleteIndicator()
	{
		var translation = "<digi:trn key="aim:activitydelete">Do you want to delete the Activity</digi:trn>"; 
		return confirm(translation);
	}
	
	function deleteActivities(){
		var chk=document.getElementsByTagName('input');
      	var tIds='';
      	for(var i=0;i<chk.length;i++){
     	 	if(chk[i].type == 'checkbox' && chk[i].checked && chk[i].id != 'chkAll'){
         	 	tIds+=chk[i].value+',';
          	}
      	}
     	if(tIds.length>0){
     		if(deleteActs()){ 
      			tIds=tIds.substring(0,tIds.length-1);
      			<digi:context name="deleteActs" property="context/module/moduleinstance/activityManager.do?action=delete"/>
  				document.aimActivityForm.action = "<%=deleteActs %>&tIds="+tIds+"";
				document.aimActivityForm.target = "_self";
				document.aimActivityForm.submit();	
 			}
 		}else{
 			var translation = "<digi:trn key="aim:activityselectone">Please select at least one activity to be deleted</digi:trn>"; 
     		alert(translation);
     		return false;
 		}
	}	
	
	function unFreezeActivities() {
		var activityIds  = getSelectedIds();
		if(activityIds.length > 0){
     		if(confirm("<digi:trn jsFriendly='true'>Are you sure you want to unfreeze the selected activities?</digi:trn>")){ 
      			activityIds=activityIds.substring(0,activityIds.length-1);
      			<digi:context name="unfreezeAction" property="context/module/moduleinstance/activityManager.do?action=unfreeze"/>
  				document.aimActivityForm.action = "<%=unfreezeAction %>&activityIds="+activityIds+"";
				document.aimActivityForm.target = "_self";
				document.aimActivityForm.submit();				
 			}
 		}else{
 			var translation = "<digi:trn key="aim:activityselectonetounfreeze">Please select at least one activity to unfreeze</digi:trn>";
 			alert(translation);
     		return false;
 		}
		
	}
	
	function getSelectedIds() {
		var chk=document.getElementsByTagName('input');
      	var tIds='';
      	for(var i=0;i<chk.length;i++){
     	 	if(chk[i].type == 'checkbox' && chk[i].checked && chk[i].id != 'chkAll'){
         	 	tIds+=chk[i].value+',';
          	}
      	}
      	
      	return tIds
	}
	
	function deleteActs(){
		var translation = "<digi:trn jsFriendly='true'>Are You Sure You Want To Remove Selected Activities?</digi:trn>"; 
		return confirm(translation);
	}	
	
	function load() {}

	function unload() {}
	
	function searchActivity() {
		if (document.aimActivityForm.tempNumResults.value == 0) {
			  alert ("Invalid value at 'Number of results per page'");
			  document.aimActivityForm.tempNumResults.focus();
			  return false;
		} else {
			
			 <digi:context name="searchOrg" property="context/module/moduleinstance/activityManager.do"/>
		     url = "<%= searchOrg %>?action=search";
		     document.aimActivityForm.action = url;
		     document.aimActivityForm.target="_self";
		     document.aimActivityForm.submit();
			 return true;
		}
	}



	function searchAlpha(val) {
		if (document.aimActivityForm.tempNumResults.value == 0) {
			  alert ("Invalid value at 'Number of results per page'");
			  document.aimActivityForm.tempNumResults.focus();
			  return false;
		} else {
			 <digi:context name="searchOrg" property="context/module/moduleinstance/activityManager.do"/>
			 url = "<%= searchOrg %>?alpha=" + val + "&orgSelReset=false";
		     document.aimActivityForm.action = url;
		     document.aimActivityForm.target="_self";
		     document.aimActivityForm.submit();
			 return true;
		}
	}

	function selectAll(){
		var chkAll = document.getElementById('chkAll');
		var chk = document.getElementsByTagName('input');
		for(var i=0;i<chk.length;i++){
            if(chk[i].type == 'checkbox'){
				if (chkAll.checked) {
					chk[i].checked = true;
				}else{
					chk[i].checked = false;
           		}
            }
		}
    }


	function sortSubmit(val){
		
		var sval = document.aimActivityForm.sort.value;
		var soval = document.aimActivityForm.sortOrder.value;

		if ( val == sval ) {
			if (soval == "asc"){
				document.aimActivityForm.sortOrder.value = "desc";
			}
			if (soval == "desc"){
				document.aimActivityForm.sortOrder.value = "asc";
			}
		}
		else
			document.aimActivityForm.sortOrder.value = "asc";
		
		document.aimActivityForm.sort.value=val;

		<digi:context name="sorting" property="context/module/moduleinstance/activityManager.do" />
		url = "<%= sorting %>?action=sort";
	    document.aimActivityForm.action = url;
	    document.aimActivityForm.target="_self";
	    document.aimActivityForm.submit();
	}
	
	function resetSearch() {
		<digi:context name="searchOrg" property="context/module/moduleinstance/activityManager.do"/>
		url = "<%= searchOrg %>?action=reset";
	     document.aimActivityForm.action = url;
	     document.aimActivityForm.submit();
		 return true;

	}
	  function exportXSL(){
	        <digi:context name="exportUrl" property="context/module/moduleinstance/exportActivityManagerXSL.do"/>;
	        document.aimActivityForm.action="${exportUrl}";
	        document.aimActivityForm.target="_blank";
	        document.aimActivityForm.submit();
	    }
	
	var enterBinder	= new EnterHitBinder('');
	enterBinder.map(["keywordText"], "searchActBtn");	
	
-->
</script>

<h1 class="admintitle"><digi:trn key="aim:activityManager">
							Activity Manager
						</digi:trn></h1>
<digi:instance property="aimActivityForm" />
<digi:form action="/activityManager.do" method="post">
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->
<html:hidden property="sort" />
<html:hidden property="sortOrder" />
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000 align="center">
	<tr>
		<td align=left class=r-dotted-lg valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%" border="0">
				<!--<tr>-->
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
						<digi:trn key="aim:activityManager">
							Activity Manager
						</digi:trn>
                        <div class="adminicon"><img src="/TEMPLATE/ampTemplate/img_2/adminicons/activitymanager.jpg"/></div>
					</td>-->
					<!-- End navigation -->
				<!--</tr>-->
				<!--<tr>
					<td height=16 valign="center" width=571>
						<span class=subtitle-blue>
						<digi:trn key="aim:activityManager">
							Activity Manager
						</digi:trn>
						</span>
					</td>
				</tr>-->
			<tr>
				<td align="left">
				
            <table border="0" align="center" bgcolor="#f2f2f2" width=100%>
                <tr>


                    <td noWrap align=center valign="middle">
                      	<jsp:include
									page="/repository/aim/view/adminXSLExportToolbar.jsp" />
        </td>
    </tr>
</table></td>
			</tr>
			
				<tr>
					<td noWrap width="100%" vAlign="top">
					<table width="100%" cellpadding="1" cellspacing="1" border="0">
						<tr>
							<td noWrap width=600 vAlign="top">
							<table bgColor=#d7eafd cellpadding="1" cellspacing="1" width="100%"
								valign="top">
								<tr bgColor=#ffffff>
									<td vAlign="top" width="100%">

									<table width="100%" cellspacing="1" cellpadding="1" valign="top"
										align=left>
										<tr>
											<td>
											<table style="margin-bottom:15px;5">
												<tr>
													<td width="195"><digi:trn key="aim:keyword">Keyword</digi:trn>&nbsp;
													<html:text property="keyword" styleClass="inp-text" styleId="keywordText"/></td>
													<td width="120"><digi:trn key="aim:results">Results</digi:trn>&nbsp;
													<!--<digi:trn key="aim:resultsPerPage">Results per page</digi:trn>&nbsp;-->
													<!--<html:text property="tempNumResults" size="2" styleClass="inp-text" />-->
													<html:select property="tempNumResults"
														styleClass="inp-text" onchange="return searchActivity()">
														<html:option value="10">10</html:option>
														<html:option value="20">20</html:option>
														<html:option value="50">50</html:option>
														<html:option value="-1" >
															<digi:trn key="aim:resultsAll">All</digi:trn>
														</html:option>
													</html:select></td>
													<td width="200"><digi:trn key="aim:dataFreezeFilter">Data Freeze Filter</digi:trn>&nbsp;													
													<html:select property="dataFreezeFilter" styleClass="inp-text" onchange="return searchActivity()">
	                                                    <html:option value="ALL"><digi:trn key="aim:all">All</digi:trn></html:option>
	                                                    <html:option value="FROZEN"><digi:trn key="aim:frozen">Frozen</digi:trn></html:option>
	                                                    <html:option value="UNFROZEN"><digi:trn key="aim:unfrozen">Unfrozen</digi:trn></html:option>	
                                                    </html:select>
                                                    &nbsp;
													</td>
													<td width="50"><c:set var="trnResetBtn">
														<digi:trn key="aim:btnReset"> Reset </digi:trn>
													</c:set> <input type="button" value="${trnResetBtn}"
														class="dr-menu" onclick="return resetSearch()"></td>
													<td width="260"><c:set var="trnGoBtn">
														<digi:trn key="aim:btnGo"> GO </digi:trn>
													</c:set> <input type="button" value="${trnGoBtn}" class="dr-menu"
														onclick="return searchActivity()" id="searchActBtn"></td>
												</tr>
											</table>
											</td>
										</tr>

										<tr>
											<td bgColor=#c7d4db height="25"
												align="center"><!-- Table title -->
												<b style="font-size:12px; font-family:Arial; color:#000000;">
												<digi:trn key="aim:activityList">Activity List</digi:trn></b> <!-- end table title --></td>
										</tr>
										<tr>
										
											<td>
											<table width="100%" cellspacing="0" cellpadding=0 valign="top"
												align=left bgcolor="#ffffff">
												
												<logic:notEmpty name="aimActivityForm"
													property="activityList">
													<tr>
													<!--  to export table we are adding class "report" to its container -->
														<td class="report">
														<table width="100%" cellspacing="0" cellpadding=0
															bgcolor="#cccccc" style="font-size:12px;" class="inside">
															<thead>
															<tr bgcolor="#ffffff">
																<jsp:useBean id="urlParamsSort" type="java.util.Map"
																	class="java.util.HashMap" />
																<c:set target="${urlParamsSort}" property="action"
																	value="sort" />
																<td width="9" height="15" class="inside ignore">&nbsp;</td>
																<td style="color:#376091;">
																<b>
																<digi:trn key="aim:FreezingColorCol">
	                                                                            	Frozen/Unfrozen
	                                                                            </digi:trn>
	                                                                            </b>
																</td>
																<td class="inside" >
																<a href="javascript:sortSubmit('activityName')">
																<b> <digi:trn key="aim:ActivityNameCol">
	                                                                            	Activity Name
	                                                                            </digi:trn>
																<%-- <c:set target="${urlParamsSort}"
																	property="sortByColumn" value="activityName" /> <digi:link
																	href="/activityManager.do" name="urlParamsSort">
																	<digi:trn key="aim:ActivityNameCol">
	                                                                            	Activity Name
	                                                                            </digi:trn>
																</digi:link>  --%></b>
																<c:if test="${aimActivityForm.sort=='activityName' && aimActivityForm.sortOrder=='asc'}">
																		<img src="/repository/aim/images/up.gif" alt="up" />
																	</c:if>
																<c:if test="${aimActivityForm.sort=='activityName' && aimActivityForm.sortOrder=='desc'}">
																		<img src="/repository/aim/images/down.gif" alt="down" />
																	</c:if>
																</a>
																</td>
																<td width="200" class="inside" >
																<a href="javascript:sortSubmit('activityTeamName')">
																<b> 
																<digi:trn key="aim:ActivityTeamName">
	                                                                            	Team Name
	                                                                            </digi:trn>
																<%-- <c:set
																	target="${urlParamsSort}" property="sortByColumn"
																	value="activityTeamName" /> <digi:link
																	href="/activityManager.do" name="urlParamsSort">
																	<digi:trn key="aim:ActivityTeamName">
	                                                                            	Team Name
	                                                                            </digi:trn>
																</digi:link>  --%></b>
																<c:if test="${aimActivityForm.sort=='activityTeamName' && aimActivityForm.sortOrder=='asc'}">
																		<img src="/repository/aim/images/up.gif" alt="up" />
																	</c:if>
																<c:if test="${aimActivityForm.sort=='activityTeamName' && aimActivityForm.sortOrder=='desc'}">
																		<img src="/repository/aim/images/down.gif" alt="down" />
																	</c:if>
																</a>
																</td>

																<td width="100" class="inside" >
																<a href="javascript:sortSubmit('activityId')">
																<b>
																<digi:trn key="aim:ActivityIdCol">
	                                                                            	Activity Id
	                                                                            </digi:trn> 
																<%-- <c:set
																	target="${urlParamsSort}" property="sortByColumn"
																	value="activityId" /> <digi:link
																	href="/activityManager.do" name="urlParamsSort">
																	<digi:trn key="aim:ActivityIdCol">
	                                                                            	Activity Id
	                                                                            </digi:trn>
																</digi:link> --%> </b>
																<c:if test="${aimActivityForm.sort=='activityId' && aimActivityForm.sortOrder=='asc'}">
																		<img src="/repository/aim/images/up.gif" alt="up" />
																	</c:if>
																<c:if test="${aimActivityForm.sort=='activityId' && aimActivityForm.sortOrder=='desc'}">
																		<img src="/repository/aim/images/down.gif" alt="down" />
																	</c:if>
																</a>
																</td>
																
																<td width="5%" align="left" class="inside ignore"><c:set
																	var="trnSelectAll">
																	<digi:trn>Select All</digi:trn>
																</c:set> <input type="checkbox" id="chkAll"
																	onclick="javascript:selectAll()"
																	title="${trnSelectAll}" /></td>
															</tr>
															</thead>
														<!--  to export table we are adding class "yui-dt-data" to its tbody-->
													<tbody class="yui-dt-data">
															<logic:iterate name="aimActivityForm"
																property="activityList" id="activities"
																type="org.digijava.module.admin.helper.AmpActivityFake">
																<tr bgcolor="#ffffff">
																	<logic:notEmpty name="activities" property="team">
																		<td width="9" height="15" class="inside ignore"><img
																			src="../ampTemplate/images/arrow_right.gif" border="0">
																		</td>
																	</logic:notEmpty>
																	
																	<logic:empty name="activities" property="team">
																		<td width="9" height="15" class="inside ignore"><img
																			src="../ampTemplate/images/start_button.gif" border="0">
																		</td>
																	</logic:empty>
																	<td width="100" class="inside">
																	 <div class="<%= activities.getFrozen() ? "frozen" : "unfrozen" %>" > </div>
																	</td>
																	<td class="inside"><bean:write name="activities" property="name" />
																	</td>
																	<td width="100" class="inside"><logic:notEmpty name="activities"
																		property="team">
																		<bean:write name="activities" property="team.name" />
																	</logic:notEmpty></td>
																	<td width="100" class="inside"><bean:write name="activities"
																		property="ampId" /></td>
																	

																	<td align="left" width="12" class="inside ignore">
																		<c:set var="actId">
																			<bean:write name="activities" property="ampActivityId" />
																		</c:set>
																	 <input type="checkbox" value="${actId}" /></td>
																</tr>
															</logic:iterate>
															</tbody>
														</table>
														</td>
													</tr>
												</logic:notEmpty>
												<logic:empty name="aimActivityForm" property="activityList">
													<tr align="center" bgcolor="#ffffff">
														<td><b> <digi:trn
															key="aim:emptyActivitiesPresent">
															No activities present
														</digi:trn></b></td>
													</tr>
												</logic:empty>
											</table>
											</td>
										</tr>
										<tr>
											<td bgColor=#ffffff height="20" align="left" style="padding-top:15px;"><img
												src="../ampTemplate/images/start_button.gif" border="0">
											- <b><digi:trn key="aim:unassignedactivities">Unassigned Activities</digi:trn></b>
											
											</td>
											
										</tr>
										<tr>
										<td>						
										
										<div class="legend-item">
										  <div class="frozen"> </div>
										  <div style= "float:left;"><digi:trn key="aim:frozen">Frozen</digi:trn></div>
										</div>
										
										<div class="legend-item">
										  <div class="unfrozen"> </div>
										  <div style= "float:left;"><digi:trn key="aim:unfrozen">Unfrozen</digi:trn></div>
										</div>
										
										</td>
										</tr>
										<tr>
										<td> 
									    
										</td>
										</tr>
										
										<tr bgcolor="#ffffff">
											<td>&nbsp;</td>
										</tr>
										<logic:notEmpty name="aimActivityForm" property="activityList">

											<tr bgcolor="#ffffff">
												<td>
												<hr />
												<table width=100%>
													<tr>
														<td style="text-align:center;">
														<%
													ActivityForm aimActivityForm = (ActivityForm) pageContext.getAttribute("aimActivityForm");
													java.util.List pagelist = new java.util.ArrayList();
													for(int i = 0; i < aimActivityForm.getTotalPages(); i++)
														pagelist.add(new Integer(i + 1));
													pageContext.setAttribute("pagelist",pagelist);
													pageContext.setAttribute("maxpages", new Integer(aimActivityForm.getTotalPages()));
													pageContext.setAttribute("actualPage", new Integer(aimActivityForm.getPage()));
												%> <jsp:useBean id="urlParamsPagination"
															type="java.util.Map" class="java.util.HashMap" /> <c:set
															target="${urlParamsPagination}" property="action"
															value="getPage" /> <digi:trn key="aim:pages">Pages:</digi:trn>&nbsp;
														<c:if test="${aimActivityForm.currentPage >0}">
															<jsp:useBean id="urlParamsFirst" type="java.util.Map"
																class="java.util.HashMap" />
															<c:set target="${urlParamsFirst}" property="page"
																value="0" />
															<c:set target="${urlParamsFirst}" property="action"
																value="getPage" />
															<c:set var="translation">
																<digi:trn key="aim:firstpage">First Page</digi:trn>
															</c:set>
															<digi:link href="/activityManager.do"
																style="text-decoration=none" name="urlParamsFirst"
																title="${translation}">
														&lt;&lt;
													</digi:link>

															<jsp:useBean id="urlParamsPrevious" type="java.util.Map"
																class="java.util.HashMap" />
															<c:set target="${urlParamsPrevious}" property="page"
																value="${aimActivityForm.currentPage -1}" />
															<c:set target="${urlParamsPrevious}" property="action"
																value="getPage" />
															<c:set var="translation">
																<digi:trn key="aim:previouspage">Previous Page</digi:trn>
															</c:set>
															<digi:link href="/activityManager.do"
																name="urlParamsPrevious" style="text-decoration=none"
																title="${translation}">
														&lt;
													</digi:link>
														</c:if> <c:set var="length"
															value="${aimActivityForm.pagesToShow}"></c:set> <c:set
															var="start" value="${aimActivityForm.offset}" /> <logic:iterate
															name="pagelist" id="pageidx" type="java.lang.Integer"
															offset="${start}" length="${length}">
															<c:set target="${urlParamsPagination}" property="page"
																value="${pageidx - 1}" />
															<c:if test="${(pageidx - 1) eq actualPage}">
																<bean:write name="pageidx" />
															</c:if>
															<c:if test="${(pageidx - 1) ne actualPage}">
																<digi:link href="/activityManager.do"
																	name="urlParamsPagination">
																	<bean:write name="pageidx" />
																</digi:link>
															</c:if>
															<c:if test="${pageidx < maxpages}"> | </c:if>
														</logic:iterate> <c:if
															test="${aimActivityForm.currentPage+1 != aimActivityForm.totalPages}">
															<jsp:useBean id="urlParamsNext" type="java.util.Map"
																class="java.util.HashMap" />
															<c:set target="${urlParamsNext}" property="page"
																value="${aimActivityForm.currentPage+1}" />
															<c:set target="${urlParamsNext}" property="action"
																value="getPage" />
															<c:set var="translation">
																<digi:trn key="aim:nextpage">Next Page</digi:trn>
															</c:set>
															<digi:link href="/activityManager.do"
																style="text-decoration=none" name="urlParamsNext"
																title="${translation}">
														&gt;
													</digi:link>
															<jsp:useBean id="urlParamsLast" type="java.util.Map"
																class="java.util.HashMap" />
															<c:if
																test="${aimActivityForm.totalPages  > aimActivityForm.pagesToShow}">
																<c:set target="${urlParamsLast}" property="page"
																	value="${aimActivityForm.totalPages-aimOrgManagerForm.pagesToShow}" />
															</c:if>

															<c:set target="${urlParamsLast}" property="page"
																value="${aimActivityForm.totalPages-1}" />
															<c:set target="${urlParamsLast}" property="action"
																value="getPage" />
															<c:set var="translation">
																<digi:trn key="aim:lastpage">Last Page</digi:trn>
															</c:set>
															<digi:link href="/activityManager.do"
																style="text-decoration=none" name="urlParamsLast"
																title="${translation}">
														&gt;&gt; 
													</digi:link>
													</c:if> &nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;  
													<digi:trn>Page</digi:trn>&nbsp;
													<c:out value="${aimActivityForm.currentPage+1}"></c:out>&nbsp;
													<digi:trn>of</digi:trn>&nbsp;
													<c:out value="${aimActivityForm.totalPages}"></c:out>
													</td>
														<td width="20%" align="right"><c:set
															var="trnDeleteSelectedBtn">
															<digi:trn>Delete Selected Activities</digi:trn>
														</c:set> <input type="button" value="${trnDeleteSelectedBtn}"
															class="dr-menu" onclick="return deleteActivities()">
														</td>
														<td width="10%" align="center"><c:set
															var="trnUnfreezeSelectedBtn">
															<digi:trn>Unfreeze</digi:trn>
														</c:set> <input type="button" value="${trnUnfreezeSelectedBtn}"
															class="dr-menu" onclick="return unFreezeActivities()">
														</td>
													</tr>
												</table>
												</td>
											</tr>
										</logic:notEmpty>
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
