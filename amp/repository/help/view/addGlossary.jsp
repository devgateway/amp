<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/resources/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/displaytag-11.tld" prefix="display" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>


<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/yahoo-dom-event.js"></script>
 <script type="text/javascript">
     function validateItemTitle() {
         var title=document.glossaryForm.nodeName.value;
         title=title.replace(/^\s+|\s+$/g, '');
         var valid=false;
         if(title!=''){
             valid=true;
         }
         else{
             alert("<digi:trn>Please enter title</digi:trn>");
         }
         return valid;
     }
     function cancelTopicCreation(){
     <digi:context name="cancel" property="context/module/moduleinstance/glossary.do" />
             document.glossaryForm.action = "${cancel}";
             document.glossaryForm.target = "_self";
             document.glossaryForm.submit();
             
         }
  </script>
<digi:form action="/addGlossary.do">
<html:hidden name="glossaryForm" property="parentNodeId"/>
<div>
	<table width="100%">
		<tr>
			<td class="subtitle-blue">
				<digi:trn>Add new glossary item</digi:trn>
			</td>
		</tr>		
		<tr>
			<td>
				&nbsp;
			</td>
		</tr>		
		<tr>
			<td>
				
				<table cellpadding="5px">
					<tr>
						<td nowrap="nowrap" align="right" width="40%">
							<strong><digi:trn>Parent name:</digi:trn></strong>
						</td>
						<td>
							<c:if test="${empty glossaryForm.nodeParentName}">
								<digi:trn>No Parent. This will be top level item</digi:trn>
							</c:if>
							<c:if test="${not empty glossaryForm.nodeParentName}">
								${glossaryForm.nodeParentName}
							</c:if>
						</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="right" width="30%">
							<strong><digi:trn>Title</digi:trn>:</strong>
						</td>
						<td>
							<html:text styleId="txtGlossaryTitle" name="glossaryForm" property="nodeName"/>
						</td>
					</tr>
					<tr>
						<td align="right" width="30%">
							<input type="button" value="Cancel" onclick="cancelTopicCreation()">
						</td>
						<td>
                            <html:submit onclick="return validateItemTitle()">Ok</html:submit>
						</td>
					</tr>
				</table>
				
				
				
			</td>
		</tr>		
	</table>
</div>

</digi:form>
<script type="text/javascript">
	function cancelAdd(){
		var form = window.forms['glossaryForm'];
		form.action='/help/glossary.do';
		form.submit();
	}
	
	$(document).ready(function () {
		$('#txtGlossaryTitle').focus();
	});
</script>