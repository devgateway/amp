<%@page import="org.digijava.module.contentrepository.helper.FilterValues"%>
<%@page import="org.digijava.module.categorymanager.util.CategoryConstants"%>
<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<jsp:include page="/repository/aim/view/scripts/newCalendar.jsp"  />
<%-- This file renders the Resource-Filtering popup table. Add one <tr></tr> per filter criterium --%>
<style>
#docTypes {
	width: 250px;
}
</style>

<bean:define id="myFilterDivId" toScope="page" scope="request" name="filterDivId"/>

<% pageContext.setAttribute("myFilterValues", new FilterValues(request) ); %>

<div id="${myFilterDivId}" style="display:none;">
	<form>
		<table border="0" cellspacing="1" cellpadding="1">
			<tr>
				<td><div class="t_sm"><b><digi:trn>Document Type</digi:trn>:</b></div></td>
				<td>
					<category:showoptions name="filterDocTypeIds" keyName="<%= CategoryConstants.DOCUMENT_TYPE_KEY %>" styleClass="dropdwn_sm" styleId="docTypes"/>
				</td>
			</tr>
			<tr>
				<td><div class="t_sm"><b><digi:trn>File Type</digi:trn>:</b></div></td>
				<td>
					<select name="filterFileTypes" class="dropdwn_sm" style="width: 250px;" >
						<option value="-1"><digi:trn>Please select from below</digi:trn></option>
						<c:forEach var="kvItem" items="${myFilterValues.possibleFileTypes}">
							<option value="${kvItem.key}">${kvItem.value}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td><div class="t_sm"><b><digi:trn>Creator</digi:trn>:</b></div></td>
				<td>
					<select name="filterOwners" class="dropdwn_sm" style="width: 250px;">
						<option value="-1"><digi:trn>Please select from below</digi:trn></option>
						<c:forEach var="item" items="${myFilterValues.possibleOwners}">
							<option value="${item}"><c:out value="${item}"></c:out> </option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td><div class="t_sm"><b><digi:trn>Creator Team</digi:trn>:</b></div></td>
				<td>
					<select name="filterTeamIds"  id="filterTeamIds" class="dropdwn_sm" style="width: 250px;">
						<option value="-1"><digi:trn>Please select from below</digi:trn></option>
						<c:forEach var="kvItem" items="${myFilterValues.possibleTeams}">
							<option value="${kvItem.key}"><c:out value="${kvItem.value}"></c:out> </option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<field:display name="Resource Organisations" feature="Resource Columns">			
				<tr>
					<td><div class="t_sm"><b><digi:trn>Organisations</digi:trn>:</b></div></td>
					<td>
						<select name="filterOrganisations" class="dropdwn_sm" style="width: 250px;">
							<option value="-1"><digi:trn>Please select from below</digi:trn></option>
							<c:forEach var="kvItem" items="${myFilterValues.possibleOrganisations}">
								<option value="${kvItem.key}"><c:out value="${kvItem.value}"></c:out> </option>
							</c:forEach>
						</select>
					</td>
				</tr>
			</field:display>			
			<tr>
				<td><div class="t_sm"><b><digi:trn>From date</digi:trn>:</b></div></td>
				<td>
					<input id="filterFromDate" type="text" readonly="readonly" name="filterFromDate" class="dropdwn_sm"/>
					<a id="date1" href='javascript:pickDateById2("filterTeamIds","filterFromDate",true,"tl")'>
			        <img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0"/>	
			        </a>
			        <a id="clear1" href="javascript:clearDate('filterFromDate')">
                    <digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border="0" alt="Delete this date"/>
                   	</a>
				</td>
			</tr>
			<tr>
				<td><div class="t_sm"><b><digi:trn>To date</digi:trn>:</b></div></td>
				<td>
					<input id="filterToDate" type="text" readonly="readonly" name="filterToDate" class="dropdwn_sm"/>
					<a id="date2" href='javascript:pickDateById2("filterTeamIds","filterToDate",true,"tl")'>
			        <img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0"/>	
			        </a>
			        <a id="clear2" href='javascript:clearDate("filterToDate")'>
                    <digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border="0" alt="Delete this date"/>
                   	</a>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="text-align: center;padding-top: 30px;">
					<button class="buttonx" type="button">
						<digi:trn>Apply</digi:trn>
					</button>
					 &nbsp;&nbsp;&nbsp;
					<button class="buttonx" type="button">
						<digi:trn>Reset</digi:trn>
					</button> 
					&nbsp;&nbsp;&nbsp;
					<button class="buttonx" type="button">
						<digi:trn>Close</digi:trn>
					</button>
				</td>
			</tr>
		</table>
	</form>
</div>