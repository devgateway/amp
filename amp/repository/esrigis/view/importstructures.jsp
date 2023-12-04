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
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<h1 class="admintitle">Structures Importer</h1>
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%">
			<tr>
				<!-- Start Navigation -->
				<td height=33><span class=crumb>
					<c:set var="translation">
						<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
					</c:set>
					<digi:link href="/admin.do" styleClass="comment" title="${translation}" contextPath="/aim">
					<digi:trn key="aim:AmpAdminHome">
					Admin Home
					</digi:trn>
					</digi:link>&nbsp;&gt;&nbsp;
					<digi:trn>Structures Importer</digi:trn>
				</td>
				<!-- End navigation -->
			</tr>
			<tr>
				<td>
					<h2>
					<digi:trn>File Format</digi:trn>
					</h2>
				</td>
			</tr>
			<tr>
				<td>
					<table style="border: 1px solid gray;">
						<tr>
							<td><b>AMPID</b></td>
							<td><b>TITLE</b></td>
							<td><b>LATITUD</b></td>
							<td><b>LONGITUD</b></td>
							<td><b>TYPE</b></td>
							<td><b>DESCRIPTION</b></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					"AMP856325","TEST TITLE","15.249505","-88.368521","School","DESCRIPTION TEST"
				</td>
			</tr>
			<tr>
				<td>
				<digi:instance property="structuresimporterform" />
				<digi:form action="/StructuresImporter.do" enctype="multipart/form-data" method="POST">
	            <digi:errors/>
	            <p/>
				<html:file property="uploadedFile"/>
				(<digi:trn>text/csv file only </digi:trn>)
				<p/>
					<html:submit property="importPerform"><digi:trn>Import</digi:trn></html:submit>
				</digi:form>
				</td>
			</tr>
		</table>
		<c:if test="${!empty structuresimporterform.errors}" >
			<c:set var="display">
				block
			</c:set>
		</c:if>
		<c:if test="${empty structuresimporterform.errors}" >
			<c:set var="display">
				none
			</c:set>
		</c:if>
		<div id="erros" style="font-style: italic;color: red;display: ${display};">
			<p style="font-size: 12px;font-weight: bold;"> 
			<digi:trn>We could not import the following structures</digi:trn>
			</p>
			<br>
			<c:forEach items="${structuresimporterform.errors}" var="item">
				<c:out value="${item}"/><br>
			</c:forEach>
		</div>
		</td>
	</tr>
</table>
