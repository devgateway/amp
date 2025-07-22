<%-- renders the "add document" part --%>
<%-- the HTML is ready to be included in the page per se, so no css/javascript includes here! --%>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm"%>
<div class="container-fluid" id="pledge_form_documents_change">
<%--<div class="row">
		<div class="col-xs-5 text-right"><label class="h5" for="document_title"><digi:trn>Document Title</digi:trn></label></div>
		<div class="col-xs-7">
			<input type="text" name="document_title" class="form-control input-sm validate-mandatory" /> 
		</div>
	</div> --%>	
	<div class="row">
		<div class="col-xs-8 col-xs-offset-2">
			<jsp:include page="/repository/bootstrap/upload/fileupload.jsp">
				<jsp:param name="button_caption" value="Select pledge document" />
				<jsp:param name="url" value="/selectPledgeProgram.do?extra_action=file_upload" />
			</jsp:include>
		</div>
	</div>
	<div class="col-xs-4 col-xs-offset-2 text-right"><button type="button" class="btn btn-success btn-sm" id='pledge_form_documents_change_submit' onclick="documentsController.submitClicked(this);" ><digi:trn>Submit</digi:trn></button></div>
	<div class="col-xs-4 col-xs-offset-0 text-left">
		<button type="button" class="btn btn-success btn-sm" id='pledge_form_documents_change_cancel'
				onclick="documentsController.onDeleteAllNonSubmitted('#pledge_form_documents_change');
				documentsController.cancelClicked(this);" ><digi:trn>Cancel</digi:trn>
		</button>
	</div>
</div>