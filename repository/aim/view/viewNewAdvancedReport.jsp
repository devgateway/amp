<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div id="mySorter" style="display: none">
	<jsp:include page="/repository/aim/view/ar/levelSorterPicker.jsp" />
        <!--
		<a href='#' onclick='hideSorter();return false'>
			<b>
				<digi:trn key="rep:pop:Close">Close</digi:trn>
			</b>
		</a>
		 -->
	</div>

<div id="myFilter" style="display: none;" >
		<jsp:include page="/aim/reportsFilterPicker.do" />
        <!--
		<a href='#' onclick='hideFilter();return false'>
			<b>
				<digi:trn key="rep:pop:Close">Close</digi:trn>
			</b>
		</a>
		-->
</div>

<jsp:include page="/repository/aim/view/ar/reportsScripts.jsp"/>



<c:set var="rowIdx" value="<%=new Integer(0)%>" scope="request"/>


<div align="center" >
<table  cellSpacing="0" cellPadding="0" width="100%" align="center">

	<logic:notEqual name="widget" scope="request" value="true">
	<logic:notEqual name="viewFormat" scope="request" value="print">
		<!-- attach filters -->
		<tr>
			<td><jsp:include page="/repository/aim/view/ar/toolBar.jsp" /></td>
		</tr>
		<tr>
		<!-- filters -->
		</tr>
	</logic:notEqual>
	</logic:notEqual>
	<tr>
		<td><bean:define id="reportMeta" name="reportMeta"
			type="org.digijava.module.aim.dbentity.AmpReports" scope="session"
			toScope="page" />
		</td>
	</tr>

	<logic:equal name="viewFormat" scope="request" value="print">
		<script language="JavaScript">
	function load()
	{
		window.print();
	}

	function unload() {}
</script>
	</logic:equal>

<logic:notEqual name="widget" scope="request" value="true">

	<tr>
		<td style="padding-left: 5px;padding-left: 5px;" align="left"><font size="+1"><digi:trn key="rep:pop:ReportName">Report Name:</digi:trn><b><bean:write
			scope="session" name="reportMeta" property="name" /></b></font></td>
	</tr>
	<tr>
		<td style="padding-left: 5px;padding-left: 5px;"><digi:trn key="rep:pop:Description">Description:</digi:trn><i><bean:write scope="session" name="reportMeta"
			property="reportDescription" /></i></td>
	</tr>

</logic:notEqual>


	<tr>
		<td style="padding-left: 5px;padding-left: 5px;">
		<div id="menucontainer">
			<logic:notEmpty name="reportMeta" property="hierarchies">
				<a style="cursor:pointer"
					onClick="showSorter(); ">
				<u><digi:trn key="rep:pop:ChangeSorting">Change Sorting</digi:trn></u> </a>&nbsp;
			</logic:notEmpty>
			<a style="cursor:pointer"
				onClick="showFilter(); ">
			<u><digi:trn key="rep:pop:ChangeFilters">Change Filters</digi:trn></u> </a>
		</div>
		</td>
	</tr>
	<tr>
			<td align="right" style="padding-left: 5px;padding-left: 5px;">
							<span  style="font-style:italic;color: red;font-family: Arial">
								<c:set var="AllAmount">
							<%=org.digijava.module.aim.dbentity.AmpReports.getNote(session)%>
							</c:set>
							<digi:trn key="rep:pop:AllAmount"><%=org.digijava.module.aim.dbentity.AmpReports.getNote(session)%></digi:trn>
							<logic:present name="<%=org.dgfoundation.amp.ar.ArConstants.SELECTED_CURRENCY%>">
								<bean:define id="selCurrency" name="<%=org.dgfoundation.amp.ar.ArConstants.SELECTED_CURRENCY %>" />
								<digi:trn key="<%="aim:currency:" + ((String)selCurrency).toLowerCase().replaceAll(" ", "") %>">
									<%=selCurrency %>
								</digi:trn>
							</logic:present>
					</span>
				&nbsp;
			</td>
		</tr>
<logic:notEmpty name="reportMeta" property="hierarchies">
		<tr>
			<td style="padding-left: 5px;padding-left: 5px;">
				<logic:notEmpty name="report" property="levelSorters">
				<logic:iterate name="report" property="levelSorters" id="sorter" indexId="levelId">
				<logic:present name="sorter">
					<digi:trn key="rep:pop:Level">Level</digi:trn> <bean:write name="levelId"/> <digi:trn key="rep:pop:sortedBy">sorted by</digi:trn> <bean:write name="sorter"/><br/>
				</logic:present>
				</logic:iterate>
				</logic:notEmpty>
			</td>
		</tr>
	<tr> <td>&nbsp;</td></tr>
	</logic:notEmpty>

	<tr>
	<td width="500" style="padding-left: 5px;padding-left: 5px;">
	<digi:trn key="rep:pop:SelectedFilters">Currently Selected Filters:</digi:trn>
		<logic:present name="<%=org.dgfoundation.amp.ar.ArConstants.REPORTS_FILTER%>" scope="session">
		<bean:define id="listable" name="<%=org.dgfoundation.amp.ar.ArConstants.REPORTS_FILTER%>" toScope="request"/>
		<bean:define id="listableStyle" value="list" toScope="request"/>
		<bean:define id="listableTrnPrefix" value="filterProperty" toScope="request"/>
			<jsp:include page="${listable.jspFile}"/>
		</logic:present>
	</td>
	</tr>
	<logic:notEqual name="report" property="totalUniqueRows" value="0">
		<tr>
			<td  style="padding-left: 5px;padding-left: 5px;">
			
			<!-- begin big report table -->
			<c:set var="pageNumber" value="<%=new Integer(0)%>" scope="request"/>
			<c:set var="paginar" value="<%=new Boolean(true)%>" scope="request"/>
			<c:if test="${not empty param.pageNumber }">
				<c:set var="pageNumber" value="<%=Integer.valueOf(request.getParameter("pageNumber"))%>" scope="request"/>
			</c:if>

	<logic:equal name="viewFormat" value="print">
			<table id='reportTable'  cellSpacing="0" width="780">
				<bean:define id="viewable" name="report"
					type="org.dgfoundation.amp.ar.Viewable" toScope="request" />
				<jsp:include page="/repository/aim/view/ar/viewableItem.jsp" />
			</table>
	</logic:equal>
		<logic:notEqual name="viewFormat" value="print">
			<table id='reportTable'  cellSpacing="0" cellPadding="1" width="100%" class="reportsBorderTable">
				<bean:define id="viewable" name="report"
					type="org.dgfoundation.amp.ar.Viewable" toScope="request" />
				<jsp:include page="/repository/aim/view/ar/viewableItem.jsp" />
			</table>
		</logic:notEqual>
		
			<!-- end of big report table --></td>
		</tr>
		<tr>
			<td>
				<logic:equal name="viewFormat" value="print">
				<u><digi:trn key="rep:print:lastupdate">Last Update :</digi:trn></u>
				&nbsp;
				<c:if test="${reportMeta.updatedDate != null}">
					<bean:write scope="session" name="reportMeta" property="updatedDate"/>
				</c:if>
				&nbsp;
				<u><digi:trn key="rep:print:user">User :</digi:trn></u>
				<c:if test="${reportMeta.user != null}">
					<bean:write scope="session" name="reportMeta" property="user"/>
				</c:if>
				</logic:equal>
			<BR>
			</td>
		</tr>
		
			<tr>
			 <td style="padding-left: 5px;padding-right: 5px">
				<logic:notEqual name="viewFormat" value="print">
				<digi:trn key="aim:pages">Pages :
				
				</digi:trn>&nbsp;
				<c:forEach var="i" begin="1" end="${report.visibleRows}" step="${recordsPerPage}">
					<logic:equal name="viewFormat" value="html">
							<a  style="cursor:pointer" onclick="window.location.href='/aim/viewNewAdvancedReport.do~viewFormat=html~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=false~cached=true~startRow=<c:out value="${i}"/>~endRow=<c:out value="${i+recordsPerPage}"/>';">
					</logic:equal>
					
					<logic:equal name="viewFormat" value="foldable">
						<a  style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="name"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${i}"/>~endRow=<c:out value="${i+recordsPerPage}"/>');">	
					</logic:equal>
					<c:choose>							
						<c:when  test="${i eq report.startRow}">
							<font color="#FF0000"><fmt:formatNumber value="${(i-1)/recordsPerPage + 1}" maxFractionDigits="0"/></font>
						</c:when>
						<c:otherwise>
							<fmt:formatNumber value="${(i-1)/recordsPerPage + 1}" maxFractionDigits="0"/>
						</c:otherwise>								
					</c:choose>
					</a>
				|
				</c:forEach>
				</logic:notEqual>
			</td>
			</tr>
			

	</logic:notEqual>
	<logic:equal name="report" property="totalUniqueRows" value="0">
		<tr>
			<td><b>
			<digi:trn key="rep:pop:filteredreport">The specified filtered report does not hold any data. Either
			pick a different filter criteria or use another report.	</digi:trn>
			</b></td>
		</tr>
	</logic:equal>
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>

</table>
</div>

