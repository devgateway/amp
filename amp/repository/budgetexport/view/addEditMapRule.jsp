<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="beMapRuleForm"/>
<digi:form action="/addEditDeleteMapRule.do?action=save" method="post">
	<html:hidden name="beMapRuleForm" property="id"/>
	<html:hidden name="beMapRuleForm" property="curProjectId"/>

	<span><a href="/aim/admin.do" class="comment" title="<digi:trn>Click here to goto Admin Home</digi:trn>"><digi:trn>Admin Home</digi:trn></a>&nbsp;&gt;&nbsp;<a href="/budgetexport/showProjectList.do" class="comment" title="<digi:trn>Click here to goto Project List</digi:trn>"><digi:trn>Budget Export Projects</digi:trn></a>&nbsp;&gt;&nbsp;<digi:trn>Add/Edit Mapping Rule</digi:trn></span>
	

	

		<digi:errors/>
			
			

<!-- MAIN CONTENT PART START -->
<table width="1000" border="0" cellspacing="0" cellpadding="0" align=center>
  <tr>
    <td class="main_side_1">
	<div class="wht">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td valign=top width=712>
	<table class="inside" width="100%" cellpadding="0" cellspacing="0" border=1>
<tr>
<td colspan=7 align=center background="/TEMPLATE/ampTemplate/images/ins_header.gif" class=inside><b class="ins_header"><digi:trn>Budget export manager</digi:trn></b></td>
</tr>
<tr>
    <td background="/TEMPLATE/ampTemplate/images/ins_bg.gif" class=inside><b class="ins_header" style="font-size:11px;"><digi:trn>Name</digi:trn></b></td>
    <td background="/TEMPLATE/ampTemplate/images/ins_bg.gif" class=inside><b class="ins_header" style="font-size:11px;"><digi:trn>AMP Column</digi:trn></b></td>
    <td background="/TEMPLATE/ampTemplate/images/ins_bg.gif" class=inside><b class="ins_header" style="font-size:11px;"><digi:trn>Has Header</digi:trn></b></td>
	<td background="/TEMPLATE/ampTemplate/images/ins_bg.gif" class=inside><b class="ins_header" style="font-size:11px;"><digi:trn>Allow "None" Mapping</digi:trn></b></td>
	<td background="/TEMPLATE/ampTemplate/images/ins_bg.gif" class=inside><b class="ins_header" style="font-size:11px;"><digi:trn>Allow "All" Mapping</digi:trn></b></td>
	<td background="/TEMPLATE/ampTemplate/images/ins_bg.gif" class=inside><b class="ins_header" style="font-size:11px;"><digi:trn>CSV Column Delimiter</digi:trn></b></td>
	<td background="/TEMPLATE/ampTemplate/images/ins_bg.gif" class=inside><b class="ins_header" style="font-size:11px;"><digi:trn>Service</digi:trn></b></td>
    </tr>
<tr>
    <td class=inside><html:text name="beMapRuleForm" property="name" styleClass="inputx" style="width:95%;"/></td>
    <td class=inside>
    	<bean:define id="availCols" name="beMapRuleForm" property="availColumns"/>
				<html:select name="beMapRuleForm" property="ampColumnId" styleClass="dropdwn_sm" style="width:95%;">
					<html:options collection="availCols" property="columnId" labelProperty="columnName"/>
				</html:select>
		</td>
    <td class=inside align=center><html:checkbox name="beMapRuleForm" property="header"/></td>
    <td class=inside align=center><html:checkbox name="beMapRuleForm" property="allowNone"/></td>
    <td class=inside align=center><html:checkbox name="beMapRuleForm" property="allowAll"/></td>
    <td class=inside>
    	<html:select name="beMapRuleForm" property="csvColDelimiter" styleClass="dropdwn_sm" style="width:95%;">
				<html:option value="0">Coma Separated</html:option>
				<html:option value="1">Tab Separated</html:option>
			</html:select>
	</td>
	<td class=inside>
  	<html:select name="beMapRuleForm" property="dataRetrieverClass" styleClass="dropdwn_sm" style="width:95%;">
			<html:optionsCollection name="beMapRuleForm" property="availRetrieverClasses" value="key" label="value" />
		</html:select>
	</td>
</tr>
	</table>
<br />
			<center><html:submit value="Save" styleClass="buttonx"/></center>
	</td>
   
  </tr>
</table>

	</div>
	
	</td>
  </tr>
</table>

<!-- MAIN CONTENT PART END -->

</digi:form>