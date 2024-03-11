<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>

<script type="text/javascript">
function validateSave () {
	if ($('[name="templateName"]').val().trim()=='') {
		var nameError = "<b><font color='red'><digi:trn>The template must have a name</digi:trn></font></b>";
		$('#errorMessage').html (nameError);
		$('#errorMessage').show();
		return false;
		
	}
	if ($('[name="fmTemplate"]').val()==-1) {
		var sourceError = "<b><font color='red'><digi:trn>You must select a Source template</digi:trn></font></b>";
		$('#errorMessage').html (sourceError);
		$('#errorMessage').show();
		return false;
	}
	$('#errorMessage').hide();
	return true;
	
}
</script>


<digi:instance property="aimVisibilityManagerForm" />
<span id="errorMessage" style="display:none;"></span>
<table width="100%" cellspacing="1" cellpadding="1" valign="top" align=left class="inside">	
	<tr><td bgColor=#c7d4db  height="25" align="center" colspan="3" class="inside">
	<!-- Table title -->
	<digi:trn key="aim:ampNewTemplate">
		<b>AMP New Template</b> 
	</digi:trn>
	<!-- end table title -->										
	</td></tr>
	<digi:form action="/visibilityManager.do" method="post" onsubmit="return validateSave()">

        <tr>
            <td class="inside">
                <digi:trn key="aim:newFeatureTemplateNameBbl">Template Name:</digi:trn>
            </td>
            <td class="inside">
                <input type="text" name="templateName" size="30"/>
            </td>
        </tr>
        <tr>
            <td class="inside">
                <digi:trn>Source template:</digi:trn>
            </td>
            <td class="inside">
                <html:select name="aimVisibilityManagerForm" property="fmTemplate" styleClass="inp-text">
                    <html:option value="-1"><digi:trn>Select one</digi:trn></html:option>
                    <logic:notEmpty name="aimVisibilityManagerForm" property="fmTemplateList" >
                        <html:optionsCollection name="aimVisibilityManagerForm" property="fmTemplateList"
                                                value="id" label="name" styleClass="COLOR: #cc0000;" />
                    </logic:notEmpty>
                </html:select>
            </td>
        </tr>

        <tr><td colspan="2" class="inside" align=center>
            <c:set var="translation">
                <digi:trn key="aim:newFeatureTemplateSaveNewTemplate">Save New Template</digi:trn>
            </c:set>
            <html:submit styleClass="buttonx" value="${translation}" property="newTemplate"/>
        </td></tr>
	</digi:form>
	
</table>
