<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/feature" prefix="feature" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

 

<script language="JavaScript">



	function toggleFeature(id) {

		<digi:context name="urlVal" property="context/module/moduleinstance/featureManager.do" />

		document.aimFeatureManagerForm.action = "<%= urlVal %>?toggle=true&fId="+id;

		document.aimFeatureManagerForm.submit();		

	}



</script>



<digi:instance property="aimFeatureManagerForm" />



<!--  AMP Admin Logo -->

<jsp:include page="teamPagesHeader.jsp"  />

<!-- End of Logo -->

		<h1 class="admintitle"><digi:trn key="aim:featureManager">Feature Manager</digi:trn></h1>

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>

	<tr>

		<td class=r-dotted-lg width=14>&nbsp;</td>

		<td align=left class=r-dotted-lg valign="top" width=750>

	

			<table cellPadding=5 cellspacing="0" width="100%" border="0">

				<tr>

					<!-- Start Navigation -->

					<td height=33><span class=crumb>

						<c:set var="translation">

							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>

						</c:set>

						<digi:link href="/admin.do" styleClass="comment" title="${translation}" >

						<digi:trn key="aim:AmpAdminHome">

							Admin Home

						</digi:trn>

						</digi:link>&nbsp;&gt;&nbsp;

						<digi:trn key="aim:featureManager">

							Feature Manager

						</digi:trn>

					</td>

					<!-- End navigation -->

				</tr>

				<tr>

					<td height=16 valign="center" width=571>

				

					</td>

				</tr>

				<tr>

					<td height=16 valign="center" width=571>

						<digi:errors />
					</td>

				</tr>
				<tr>
					<td noWrap width="100%" vAlign="top">
						<jsp:include page="manageTemplates.jsp" />
					</td>
				</tr>			
				<tr>
					<td noWrap width="100%" vAlign="top">
						<% 
						if(session.getAttribute("newEditTemplate")==null)
						{%>
						<jsp:include page="newFeatureTemplate.jsp"/>
						<%}else
						if(session.getAttribute("newEditTemplate").toString().compareTo("edit")==0){ %>
						<jsp:include page="editFeatureTemplate.jsp"/>
						<%}
						 else { %>
						<jsp:include page="newFeatureTemplate.jsp"/>
						<%} %>
					</td>
				</tr>	
</table>




