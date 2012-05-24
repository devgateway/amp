<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>


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
		var href;
		if (typeof (mylink) == 'string')
			href = mylink;
		else
			href = mylink.href;
		window
				.open(
						href,
						windowname,
						'channelmode=no,directories=no,menubar=no,resizable=yes,status=no,toolbar=no,scrollbars=yes,location=yes');
		return false;
	}
	function checkKeyWord(){
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
<table cellspacing="0" cellpadding="0" border="0" align="center"
	width="1000">
	<tr>
		<td valign="top">
		<fieldset><digi:form action="/search.do" onsubmit="return checkKeyWord()">
			<c:set var="keywordClasses">
			txt_sm_b
			</c:set>
		
			<c:if test="${!empty searchform.keyword}">
				<div class="search_results_header"><digi:trn>Search results for</digi:trn> "<span
					class="green_text"><c:out value="${searchform.keyword}"></c:out></span>"</div>
			<c:set var="keywordClasses">
				help_search txt_sm_b
			</c:set>
			
			</c:if>
			<c:set var="searchTooltip">
				<digi:trn>You can use the * wildcard for matching any character</digi:trn>
			</c:set>
			<div class="${keywordClasses}" style="padding-right: 150px">
			
			
			<table border="0" cellpadding="3" cellspacing="3">
  <tr>
    <td><digi:trn>Keyword</digi:trn>: </td>
    <td><html:text title="${searchTooltip}" 
				property="keyword" styleClass="inputx insidex" size="25" /></td>
    <td><digi:trn>Type</digi:trn>:</td>
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
			</html:select> </td>
    <td><digi:trn>Search Mode</digi:trn>:</td>
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
    <div class="txt_sm_b"><digi:trn>Your search return no results. Please try another keyword.</digi:trn></div>
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
									class="button_green">${fn:length(resultActivities)}</span>
								 ${resultFound} <span class="button_green"><digi:trn>Activities</digi:trn></span>
								<ul>
									<c:forEach items="${resultActivities}" var="activity">
										<li><digi:link module="aim"
											href="/viewActivityPreview.do?pageId=2&activityId=${activity.ampActivityId}">${activity.objectFilteredName}</digi:link>
									</c:forEach>
								</ul>
								</div>
								</c:if>
								<c:if test="${searchform.queryType==-1||searchform.queryType==2}">
							<div class="${search_results_block_class}"><span
								class="button_green">${fn:length(resultTabs)}</span> ${resultFound} <span class="button_green"><digi:trn>Tabs</digi:trn></span>
							<ul>
								<c:forEach items="${resultTabs}" var="tab">
									<li><a
										title="<digi:trn>Click here to view the tab</digi:trn>"
										href="/search/search.do?ampReportId=${tab.ampReportId}">${tab.objectFilteredName}</a>
								</c:forEach>
							</ul>
							</div>
							</c:if>
							<c:if test="${searchform.queryType==-1||searchform.queryType==1}">
							<div class="${search_results_block_class}"><span
								class="button_green">${fn:length(resultReports)}</span> ${resultFound} <span class="button_green"><digi:trn>Reports</digi:trn></span> 
							<ul>
								<c:forEach items="${resultReports}" var="report">
									<li><a
										title="<digi:trn>Click here to view the report</digi:trn>"
										onclick="return popup(this,'');"
										href="/search/search.do?ampReportId=${report.ampReportId}">${report.objectFilteredName}</a>
								</c:forEach>
							</ul>
							</div>
							</c:if>
							<c:if test="${searchform.queryType==-1||searchform.queryType==3}">
							<div class="search_results_block_last"><span
								class="button_green">${fn:length(resultResources)}</span>
							${resultFound} <span class="button_green"><digi:trn>Resources</digi:trn></span>
							<ul>
								<c:forEach items="${resultResources}" var="resource">
									<li><c:choose>
										<c:when test="${empty resource.webLink}">
											<a style="cursor: pointer;text-decoration: underline;"
												onclick="window.location='/contentrepository/downloadFile.do?uuid=${resource.uuid}'"
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
		</table>
		</fieldset>
		</td>
	</tr>
</table>

<!-- MAIN CONTENT PART END -->

