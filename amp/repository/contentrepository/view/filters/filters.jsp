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

<bean:define id="myFilterDivId" toScope="page" scope="request" name="filterDivId"/>

<% pageContext.setAttribute("myFilterValues", new FilterValues(request) ); %>

<div id="${myFilterDivId}" style="display:none;">
	<form>
		<table>
			<tr>
				<td><digi:trn>Document Type</digi:trn></td>
				<td>
					<category:showoptions name="filterDocTypeIds" keyName="<%= CategoryConstants.DOCUMENT_TYPE_KEY %>" styleClass="inp-text" />
				</td>
			</tr>
			<tr>
				<td><digi:trn>File Type</digi:trn></td>
				<td>
					<select name="filterFileTypes" class="inp-text" style="width: 200px;" >
						<option value="-1"><digi:trn>Please select from below</digi:trn></option>
						<c:forEach var="kvItem" items="${myFilterValues.possibleFileTypes}">
							<option value="${kvItem.key}">${kvItem.value}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td><digi:trn>Creator</digi:trn></td>
				<td>
					<select name="filterOwners" class="inp-text" style="width: 200px;">
						<option value="-1"><digi:trn>Please select from below</digi:trn></option>
						<c:forEach var="item" items="${myFilterValues.possibleOwners}">
							<option value="${item}">${item}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td><digi:trn>Creator Team</digi:trn></td>
				<td>
					<select name="filterTeamIds" class="inp-text" style="width: 200px;">
						<option value="-1"><digi:trn>Please select from below</digi:trn></option>
						<c:forEach var="kvItem" items="${myFilterValues.possibleTeams}">
							<option value="${kvItem.key}">${kvItem.value}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="text-align: center;">
					<button class="buton" type="button">
						<digi:trn>Apply</digi:trn>
					</button>
					&nbsp;&nbsp;&nbsp;
					<button class="buton" type="button">
						<digi:trn>Close</digi:trn>
					</button>
				</td>
			</tr>
		</table>
	</form>
</div>