<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>


<script language="javascript">
	function resetFields() {
		var keyword = document.getElementsByName("keyword")[0];
		var queryType = document.getElementsByName("queryType")[0];
		keyword.value = "";
		queryType.value = -1;
		document.getElementById("resultTable").innerHTML = "";
		keyword.focus();
	}

	
	function popup(mylink, windowname) {
		if (!window.focus)
			return true;

		if (navigator.appName.indexOf('Microsoft Internet Explorer') > -1) { //Workaround to allow HTTP REFERER to be sent in IE 
			var referLink = document.createElement('a');
			referLink.href = mylink;
			referLink.target = 'blank_';
			document.body.appendChild(referLink);
			referLink.click();
		} else {
			myWindow = window.open(mylink,windowname,'channelmode=no,directories=no,menubar=no,resizable=yes,status=no,toolbar=no,scrollbars=yes,location=yes');
			myWindow.location = mylink;
		}

		return false;
	}
	
	
	function checkKeyWord() {
		var keyword = document.getElementsByName("keyword");
		if (keyword) {
			keyword = keyword[0];
			if (keyword.value.length < 3) {
				alert("<digi:trn keyWords="AMP Search">Please enter a search of more than 2 characters.</digi:trn>");
				return false;
			}

		}
		return true;

	}

	function downloadFile(uuid) {
		if (navigator.appName.indexOf('Microsoft Internet Explorer') > -1) {
			var referLink = document.createElement('a');
			referLink.href = '/contentrepository/downloadFile.do?uuid=' + uuid;
			document.body.appendChild(referLink);
			referLink.click();
		} else {
			window.location = '/contentrepository/downloadFile.do?uuid=' + uuid;
		}
	}
</script>



<!-- MAIN CONTENT PART START -->
<!-- BREADCRUMP START -->
<div class="breadcrump">
<div class="centering">
<div class="breadcrump_cont">
<span class="sec_name"><digi:trn>AMP Search</digi:trn></span><span class="breadcrump_sep">|</span><a class="l_sm"><digi:trn>Tools</digi:trn></a><span class="breadcrump_sep"><b>»</b></span><span class="bread_sel"><digi:trn>AMP Search</digi:trn></span></div>
</div>
</div>
<!-- BREADCRUMP END -->
<div class="content-dir">
<table cellspacing="0" cellpadding="0" border="0" align="center"
	width="1000">
	<tr>
		<td valign="top">
		<fieldset><digi:form action="/search.do" onsubmit="return checkKeyWord()">
			<c:set var="keywordClasses">
			txt_sm_b
			</c:set>
		
			<c:if test="${!empty searchform.keyword}">
				<div class="search_results_header"><digi:trn>Search results for</digi:trn>
					<div class="search_results_header_keyword">
						 "<span class="green_text"><c:out value="${searchform.keyword}"></c:out></span>"
					</div>
				</div>
			<c:set var="keywordClasses">
				help_search txt_sm_b
			</c:set>
			
			</c:if>
			<c:set var="searchTooltip">
				<digi:trn>You can use the * wildcard for matching any character</digi:trn>
			</c:set>
			<div class="${keywordClasses}" style="padding-right: 150px">
			
			
			<table border="0" cellpadding="1" cellspacing="1">
  <tr>
    <td><b><digi:trn>Keyword</digi:trn></b>: </td>
    <td><html:text title="${searchTooltip}" 
				property="keyword" styleClass="inputx insidex" size="25" /></td>
    <td><b><digi:trn>Type</digi:trn></b>:</td>
    <td><html:select property="queryType" styleClass="inputx insidex">
				<html:option value="-1">
					<digi:trn>ALL</digi:trn>
				</html:option>
				<html:option value="0">
					<digi:trn>Activities</digi:trn>
				</html:option>
				<html:option value="1">
					<digi:trn>Reports</digi:trn>
				</html:option>
				<html:option value="2">
					<digi:trn>Tabs</digi:trn>
				</html:option>
				<html:option value="3">
					<digi:trn>Resources</digi:trn>
				</html:option>

				<ampModule:display name="Pledges" parentModule="Project Management">
					<html:option value="7">
						<digi:trn>Pledges</digi:trn>
					</html:option>
				</ampModule:display>
				
                <feature:display name="Responsible Organization" ampModule="Organizations">
                	<field:display name="Search Feature - Responsible Organization" feature="Search Feature">
                   	<html:option value="4"><digi:trn>Responsible Organization</digi:trn></html:option>
                   </field:display>
               </feature:display>
               <feature:display name="Executing Agency" ampModule="Organizations">
               	<field:display name="Search Feature - Executing Agency" feature="Search Feature">
                   	<html:option value="5"><digi:trn>Executing Agency</digi:trn></html:option>
                   </field:display>
               </feature:display>
               <feature:display name="Implementing Agency" ampModule="Organizations">
               	<field:display name="Search Feature - Implementing Agency" feature="Search Feature">
                   	<html:option value="6"><digi:trn>Implementing Agency</digi:trn></html:option>
                   </field:display>
               </feature:display>
               
			</html:select> </td>
    <td><b><digi:trn>Search Mode</digi:trn></b>:</td>
    <td><html:select property="searchMode" styleClass="inputx insidex">
				<html:option value="0">
					<digi:trn>Any keyword</digi:trn>
				</html:option>
				<html:option value="1">
					<digi:trn>All keywords</digi:trn>
				</html:option>
			</html:select></td>
    <td><html:submit styleClass="buttonx_sm">
				<digi:trn>Search</digi:trn>
			</html:submit></td>
    <td> <a style="font-size: 11px;" href="/aim/queryEngine.do"><digi:trn>Advanced Search</digi:trn></a></td>
  </tr>
</table>
		
        </div>
		</digi:form>
		<table width="100%">
			<tbody>
				<tr>
					<td width="50%" valign="top"><c:choose>
						<c:when test="${empty resultList}">
							<c:if test="${param.reset != 'true'}">
    							<div class="txt_sm_b"><digi:trn>Your search returned no results. Please try another keyword.</digi:trn></div>
							</c:if>
						</c:when>
						<c:otherwise>
							<c:set var="search_results_block_class">
								<c:choose>
									<c:when test="${searchform.queryType==-1}">
										search_results_block
									</c:when>
									<c:otherwise>
										search_results_block_last
									</c:otherwise>
								</c:choose>
							</c:set>
							
							<c:set var="resultFound">
								<digi:trn>Results found in</digi:trn>
							</c:set>
							
							<div class="search_results">
							<c:if test="${searchform.queryType==-1||searchform.queryType==0}">
								<div class="${search_results_block_class}"><span
									class="button_green default_cursor">${fn:length(resultActivities)}</span>
								 ${resultFound}
									<div class="button_green_group">
									<span class="button_green default_cursor"><digi:trn>Activities</digi:trn></span>
									</div>
									<ul class="search-results">
										<c:forEach items="${resultActivities}" var="activity">
											<li>
											<c:set var="star" scope="page" value=""/> 
											<digi:link ampModule="aim"
												href="/viewActivityPreview.do?activityId=${activity.ampActivityId}">
											<c:choose>
											<c:when test="${activity.draft == true}">
            									<font color="RED">
            										<c:if test="${activity.status.contains('started') || activity.status.equals('startedapproved') }">
													<c:set var="star" scope="page" value="*"/> 
													</c:if>
											</c:when>
											<c:otherwise>
												<c:if test="${activity.status.equals('started')}">
													<c:set var="star" scope="page" value="*"/> 
													<font color="GREEN">
												</c:if>
												<c:if test="${activity.status.equals('edited')}">
													<font color="GREEN">
												</c:if>
											</c:otherwise>
											</c:choose>
												${star} ${activity.objectFilteredName}</font>
											</digi:link>
											</li>
											
										</c:forEach>
									</ul>
								</div>
							</c:if>
							
							<c:if test="${searchform.queryType==-1||searchform.queryType==2}">
								<div class="${search_results_block_class}"><span
									class="button_green default_cursor">${fn:length(resultTabs)}</span> ${resultFound}
									<div class="button_green_group">
										<span class="button_green default_cursor"><digi:trn>Tabs</digi:trn></span>
									</div>
								<ul class="search-results">
									<c:forEach items="${resultTabs}" var="tab">
										<li><a
											title="<digi:trn>Click here to view the tab</digi:trn>"
											href="/search/search.do?ampReportId=${tab.ampReportId}">${tab.objectFilteredName}</a>
									</c:forEach>
								</ul>
								</div>
							</c:if>
							
							<ampModule:display name="Pledges" parentModule="Project Management">
								<c:if test="${searchform.queryType==-1||searchform.queryType==7}">
									<div class="${search_results_block_class}">
										<span class="button_green default_cursor">${fn:length(resultPledges)}</span> 
										${resultFound}
										<div class="button_green_group">
											<span class="button_green default_cursor"><digi:trn>Pledges</digi:trn></span>
										</div>
									<ul class="search-results">
										<c:forEach items="${resultPledges}" var="pledge">
											<li><a
												title="<digi:trn>Click here to view the pledge]</digi:trn>"
												href="/viewPledge.do?id=${pledge.ampId}">${pledge.objectFilteredName}</a>
										</c:forEach>
									</ul>
									</div>
								</c:if>
							</ampModule:display>
							
							<c:if test="${searchform.queryType==-1||searchform.queryType==1}">
								<div class="${search_results_block_class}"><span
									class="button_green default_cursor">${fn:length(resultReports)}</span> ${resultFound}
									<div class="button_green_group">
										<span class="button_green default_cursor"><digi:trn>Reports</digi:trn></span>
									</div>
									<ul class="search-results">
										<c:forEach items="${resultReports}" var="report">
											<li><a title="<digi:trn>Click here to view the report</digi:trn>"
                                                    onclick="return popup(this,'');"
                                                    href="${fn:getReportUrl(report)}">
                                                    ${report.objectFilteredName}
												</a>
											</li>
										</c:forEach>
									</ul>
								</div>
							</c:if>
							
							<c:if test="${searchform.queryType==-1||searchform.queryType==3}">
								<div class="search_results_block_last"><span
									class="button_green default_cursor">${fn:length(resultResources)}</span>
								${resultFound}
									<div class="button_green_group">
										<span class="button_green default_cursor"><digi:trn>Resources</digi:trn></span>
									</div>
									<ul class="search-results">
										<c:forEach items="${resultResources}" var="resource">
											<li><c:choose>
												<c:when test="${empty resource.webLink}">
													<a style="cursor: pointer;text-decoration: underline;"
														onclick="downloadFile('${resource.uuid}');"
														title="<digi:trn>Click here to download file</digi:trn>">
													<c:out value="${resource.name}"></c:out> </a>
												</c:when>
												<c:otherwise>
													<a href="${resource.webLink}"
														title="Click here to follow link" target="_blank">
													<c:out value="${resource.webLink}"></c:out> </a>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</ul>
								</div>
							</c:if>
							
							</div>
						</c:otherwise>
					</c:choose></td>
				</tr>
			</tbody>
			<feature:display name="Responsible Organization" ampModule="Organizations">
			  <c:set var="resultActivitiesWithOrgs" scope="request" value="${requestScope.resultActivitiesWithRespOrgs}"/>
			  <c:set var="relatedOrgIndex" scope="request" value="1"/>
			  <c:set var="relatedOrgType" scope="request">
			  	  <digi:trn>Responsible Organization</digi:trn>
			  </c:set>	
			  <jsp:include page="relatedOrgs.jsp"/>
			</feature:display>
			      
			<feature:display name="Executing Agency" ampModule="Organizations">
			    <c:set var="resultActivitiesWithOrgs" scope="request" value="${requestScope.resultActivitiesWithExeOrgs}"/>
			    <c:set var="relatedOrgIndex" scope="request" value="2"/>
			    <c:set var="relatedOrgType" scope="request">
			    	<digi:trn>Executing Agency</digi:trn>
			    </c:set>
			    <jsp:include page="relatedOrgs.jsp"/>
			</feature:display>
			          
			<feature:display name="Implementing Agency" ampModule="Organizations">
			    <c:set var="resultActivitiesWithOrgs" scope="request" value="${requestScope.resultActivitiesWithImpOrgs}"/>
			    <c:set var="relatedOrgIndex" scope="request" value="3"/>
			    <c:set var="relatedOrgType" scope="request">
			    	<digi:trn>Implementing Agency</digi:trn>
			    </c:set>
			    <jsp:include page="relatedOrgs.jsp"/>
			</feature:display>
		</table>
		</fieldset>
		</td>
	</tr>
</table>
</div>
<!-- MAIN CONTENT PART END -->

