<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<div class="budgetcontainer">
	<span><a href="/aim/admin.do" class="comment" title="<digi:trn>Click here to goto Admin Home</digi:trn>"><digi:trn>Admin Home</digi:trn></a>&nbsp;&gt;&nbsp;<a href="/budgetexport/showProjectList.do" class="comment" title="<digi:trn>Click here to goto Project List</digi:trn>"><digi:trn>Budget Export Projects</digi:trn></a>&nbsp;&gt;&nbsp;<digi:trn>Add/Edit Budget Export Project</digi:trn></span>

<digi:instance property="beProjectForm"/>
	
<digi:form action="/addEditDeleteProject.do?action=save" method="post">
	<html:hidden name="beProjectForm" property="id"/>
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
<td colspan=6 align=center background="/TEMPLATE/ampTemplate/images/ins_header.gif" class=inside><b class="ins_header"><digi:trn>Add/Edit Budget Export Project</digi:trn></b></td>
</tr>
<tr>
    <td background="/TEMPLATE/ampTemplate/images/ins_bg.gif" class=inside width=20%><b class="ins_header" style="font-size:11px;"><digi:trn>Name</digi:trn></b></td>
    <td background="/TEMPLATE/ampTemplate/images/ins_bg.gif" class=inside width=20%><b class="ins_header" style="font-size:11px;"><digi:trn>Description</digi:trn></b></td>
    <td background="/TEMPLATE/ampTemplate/images/ins_bg.gif" class=inside width=10%><b class="ins_header" style="font-size:11px;"><digi:trn>Data Source</digi:trn></b></td>
    <td background="/TEMPLATE/ampTemplate/images/ins_bg.gif" class=inside width=20%><b class="ins_header" style="font-size:11px;"><digi:trn>Mapping Service URL</digi:trn></b></td>
    <td background="/TEMPLATE/ampTemplate/images/ins_bg.gif" class=inside width=20%><b class="ins_header" style="font-size:11px;"><digi:trn>Service Action URL</digi:trn></b></td>
    <td background="/TEMPLATE/ampTemplate/images/ins_bg.gif" class=inside width=10%><b class="ins_header" style="font-size:11px;"><digi:trn>Report</digi:trn></b></td>
    </tr>
<tr>
    <td class=inside>
    	<html:text name="beProjectForm" styleClass="inputx" style="width:95%;" property="name"/>
    </td>
    <td class=inside>
    	<html:text name="beProjectForm" styleClass="inputx" style="width:95%;" property="description"/>
    </td>
    <td class=inside>
    	<html:select name="beProjectForm" styleClass="inputx" style="width:95%;" property="dataSource">
    		<html:option value="0"><digi:trn>CSV File</digi:trn></html:option>
    		<html:option value="1"><digi:trn>Service</digi:trn></html:option>
    	</html:select>
    		
    </td>
    <td class=inside>
    	<html:text name="beProjectForm" styleClass="inputx" style="width:95%;" property="mappingImportServiceURL"/>
    </td>
    <td class=inside>
    	<html:text name="beProjectForm" styleClass="inputx" style="width:95%;" property="serviceActionURL"/>
    </td>
		<td class=inside>
			<bean:define id="availReports" name="beProjectForm" property="availReports"/>
			<html:select name="beProjectForm" styleClass="inputx" style="width:95%;" property="selReport">
				<html:options collection="availReports" property="id" labelProperty="name"/>
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