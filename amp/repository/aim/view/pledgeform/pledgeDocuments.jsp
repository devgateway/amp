<%-- renders a table of the list of locations for the currently-in-form-pledge --%>
<%-- the HTML is ready to be included in the page per se, so no css/javascript includes here! --%>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm"%>

<digi:instance property="pledgeForm" />
<div id='pledge_form_documents_data'>
	<c:if test="${empty pledgeForm.selectedDocs}">
		<div class="text-center"><h3><digi:trn>No Documents</digi:trn></h3></div>
	</c:if>
	<c:if test="${not empty pledgeForm.selectedDocs}">
	<table class="table table-striped table-bordered table-hover table-condensed">
		<thead>
			<tr>
				<th class="col-xs-8 text-right"><digi:trn>Document Title</digi:trn></th>
				<th class="col-xs-3 text-center"><digi:trn>Document Name</digi:trn> (<digi:trn>Document Size, MB</digi:trn>)</th>
				<th class="col-xs-1 text-center"><%--<digi:trn>Delete</digi:trn> --%></th>
			</tr>
		</thead>
		<tbody>	
		<c:forEach var="selectedDocs" items="${pledgeForm.selectedDocs}" varStatus="index">
			<c:set var="indexLoc" value="${indexLoc+1}"/>
			<tr id="pledge_form_row_for_document_${selectedDocs.uniqueId}">
				<td class="text-right">
					<html:text name="selectedDocs" size="50" indexed="true" property="title" styleClass="form-control input-sm validate-mandatory" />
				</td>
				<td>
					<a target="_blank" href="${selectedDocs.generalLink}">
						<c:out value="${selectedDocs.fileName}" /><%--
						<img src="/repository/contentrepository/view/images/check_out.gif" border="0"> --%>
					</a>
					 (<c:out value="${selectedDocs.formattedSize}" /> MB)
				</td>
				<td class="text-center"><button type="button" onclick="documentsController.onDelete(${selectedDocs.uniqueId});" class="btn btn-danger btn-xs"><digi:trn>Delete</digi:trn></button></td>
			</tr>
		</c:forEach>
		</tbody> 
	</table>
	</c:if>
</div>
