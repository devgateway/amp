<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>

<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>

<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>

<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>

<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>



<script language="JavaScript">

	
	function editTemplate(id) {
    	// if(!onDelete) return false;
        <digi:context name="url" property="context/module/moduleinstance/visibilityManager.do?edit=true" />
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
	
	<logic:iterate name="aimVisibilityManagerForm" property="modules" id="module"
		type="org.digijava.module.aim.dbentity.AmpModulesVisibility">
		<tr bgcolor="#ffffff">
		<c:set target="${urlParams10}" property="action" value="editModule"/>
		<c:set target="${urlParams10}" property="moduleId" value="<%=module.getId() %>"/>
			<c:set var="translation">
				<digi:trn key="aim:clickToEditModule">Click here to Edit Module</digi:trn>
			</c:set>	
			<td width="70%"> <digi:link href="/visibilityManager.do" name="urlParams10" 
				title="${translation}" ><bean:write name="module" property="name"/></digi:link> &nbsp;&nbsp;&nbsp;
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




