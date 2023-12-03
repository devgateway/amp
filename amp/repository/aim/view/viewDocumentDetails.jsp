<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">

	<!--

    function confirmDelete() {
      var flag = confirm("Are you sure you want to remove the selected document?");
      return flag;
    }

    function editDocument(uuid,actId) {
      document.aimRelatedLinksForm.activityId.value=actId;
      document.aimRelatedLinksForm.uuid.value=uuid;
      <digi:context name="editDoc" property="context/ampModule/moduleinstance/updateDocumentDetails.do" />
      document.aimRelatedLinksForm.action = "<%= editDoc %>";
      document.aimRelatedLinksForm.target = "_self";
      document.aimRelatedLinksForm.submit();
    }

    function deleteDocument(uuid,actId) {
      if(!confirmDelete()){
        return false;
      }
      document.aimRelatedLinksForm.activityId.value=actId;
      document.aimRelatedLinksForm.uuid.value=uuid;
      <digi:context name="deleteDoc" property="context/ampModule/moduleinstance/deleteDocument.do" />
      document.aimRelatedLinksForm.action = "<%= deleteDoc %>";
      document.aimRelatedLinksForm.target = "_self";
      document.aimRelatedLinksForm.submit();
    }

	-->

</script>


<digi:instance property="aimRelatedLinksForm" />
<digi:context name="digiContext" property="context" />
<digi:form action="/getDocumentDetails.do" method="post">

<html:hidden property="activityId" />
<html:hidden property="uuid" />
<html:hidden property="pageId" />
<html:hidden property="valuesSet" value="false"/>

<table class="inside" width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="inside">
			<digi:trn key="aim:nameOfActivity">Activity</digi:trn>
		</td>
		<td class="inside">
			<c:out value="${aimRelatedLinksForm.document.activityName}" />
		</td>
	</tr>
	<tr>
		<td class="inside">
			<digi:trn key="aim:docTitle">Title</digi:trn>
		</td>
		<td class="inside">
			<c:out value="${aimRelatedLinksForm.document.title}" />
		</td>
	</tr>
	<tr>
		<td class="inside">
			<digi:trn key="aim:docDescription">Description</digi:trn>
		</td>
		<td class="inside">
			<c:out value="${aimRelatedLinksForm.document.docDescription}" />
		</td>
	</tr>
	<tr>
		<td class="inside">
			<digi:trn key="aim:docType">Type</digi:trn>
		</td>
		<td class="inside">
			<c:if test="${aimRelatedLinksForm.document.isFile == true}">
				<digi:trn key="aim:docIsFile">File</digi:trn>
			</c:if>
			<c:if test="${aimRelatedLinksForm.document.isFile == false}">
				<digi:trn key="aim:docIsURL">URL</digi:trn>
			</c:if>
		</td>
	</tr>
	<tr>
		<td class="inside">
			<c:if test="${aimRelatedLinksForm.document.isFile == true}">
				<digi:trn key="aim:fileName">File name</digi:trn>
			</c:if>
			<c:if test="${aimRelatedLinksForm.document.isFile == false}">
				<digi:trn key="aim:url">URL</digi:trn>
			</c:if>
		</td>
		<td class="inside">
			<c:if test="${aimRelatedLinksForm.document.isFile == true}">
				<a href="<%=digiContext%>/contentrepository/downloadFile.do?uuid=<c:out value="${aimRelatedLinksForm.document.uuid}"/>" class="l_sm">
					<c:out value="${aimRelatedLinksForm.document.fileName}" />
					<bean:write name="relatedLink" property="fileName" />
				</a>
			</c:if>
			<c:if test="${aimRelatedLinksForm.document.isFile == false}">
				<a href="<c:out value="${aimRelatedLinksForm.document.url}" />" target="_blank" class="l_sm">
				<c:out value="${aimRelatedLinksForm.document.url}" /></a>
			</c:if>
		</td>
	</tr>
</table>
<br>
<div class="buttons" align="center">
	<c:if test="${aimRelatedLinksForm.pageId == 1}">
							<c:set var="translation">
								<digi:trn key="btn:back">Back</digi:trn>
							</c:set>					
							<input type="button" value="${translation}" class="buttonx_sm btn" onclick="history.back()">
	</c:if>
</div>

</digi:form>
