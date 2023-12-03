<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>



<script language="JavaScript">

	
	function editTemplate(id) {
    	// if(!onDelete) return false;
        <digi:context name="url" property="context/ampModule/moduleinstance/visibilityManager.do?edit=true" />
        document.aimFlagUploaderForm.action = "<%=url%>&templateId="+id;
        document.aimFlagUploaderForm.submit();
	}



</script>





<digi:instance property="aimVisibilityManagerForm" />

<table width="100%" cellspacing="1" cellpadding="1" valign="top" align=left>	
	<tr><td bgColor=#d7eafd class=box-title height="20" align="center" colspan="3">
	<!-- Table title -->
	<digi:trn key="aim:ampFeatureManager">
		Feature Manager
	</digi:trn>
	<!-- end table title -->										
	</td></tr>
	<digi:form action="/visibilityManager.do" method="post" >
	<tr>
		<th><digi:trn key="aim:featureManagerTemplateName">Template name</digi:trn></th>
		
		<th><digi:trn key="aim:featureManagerOptions">Options</digi:trn></th>
	</tr>
	<jsp:useBean id="urlParams10" type="java.util.Map" class="java.util.HashMap"/>
	<jsp:useBean id="urlParams11" type="java.util.Map" class="java.util.HashMap"/>
	
	<logic:iterate name="aimVisibilityManagerForm" property="modules" id="ampModule"
		type="org.digijava.ampModule.aim.dbentity.AmpModulesVisibility">
		<tr bgcolor="#ffffff">
		<c:set target="${urlParams10}" property="action" value="editModule"/>
		<c:set target="${urlParams10}" property="moduleId" value="<%=ampModule.getId() %>"/>
			<c:set var="translation">
				<digi:trn key="aim:clickToEditModule">Click here to Edit Module</digi:trn>
			</c:set>	
			<td width="70%"> <digi:link href="/visibilityManager.do" name="urlParams10" 
				title="${translation}" ><bean:write name="ampModule" property="name"/></digi:link> &nbsp;&nbsp;&nbsp;
			</td>

			<td width="30%" align="center">
			<c:set var="translation">
				<digi:trn key="aim:clickToEditModule">Click here to Edit Module</digi:trn>
			</c:set>
			[ <digi:link href="/visibilityManager.do" name="urlParams10" 
				title="${translation}" >Edit</digi:link> ]&nbsp;&nbsp;&nbsp;
			</td>
		</tr>
	</logic:iterate>
	</digi:form>
	<tr>
		<td><br/>
		</td>
	</tr>
	
</table>




