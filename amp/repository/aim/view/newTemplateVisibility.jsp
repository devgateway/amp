<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">

</script>


<digi:instance property="aimVisibilityManagerForm" />
<table width="100%" cellspacing="1" cellpadding="1" valign="top" align=left class="inside">	
	<tr><td bgColor=#c7d4db  height="25" align="center" colspan="3" class="inside">
	<!-- Table title -->
	<digi:trn key="aim:ampNewTemplate">
		<b>AMP New Template</b> 
	</digi:trn>
	<!-- end table title -->										
	</td></tr>
	<digi:form action="/visibilityManager.do" method="post" >

	<tr><td colspan="2" class="inside" align=center>
	<digi:trn key="aim:newFeatureTemplateNameBbl">Template Name:</digi:trn> <input type="text" name="templateName" size="30"/>
	</td></tr>
	
	<tr><td colspan="2" class="inside" align=center>
		<c:set var="translation">
			<digi:trn key="aim:newFeatureTemplateSaveNewTemplate">Save New Template</digi:trn>
		</c:set>
		<html:submit styleClass="buttonx" value="${translation}" property="newTemplate"/>
	</td></tr>
	</digi:form>
	
</table>
