<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<script type="text/javascript">
function fnAddDetails()
{
	<digi:context name="addUrl" property="context/module/moduleinstance/addAmpProjectDetails.do" />
    document.aimAmpActivityForm.action = "<%=addUrl%>";
    document.aimAmpActivityForm.submit();
}
function fnAddOthers()
{
	<digi:context name="addUrl" property="context/module/moduleinstance/addAmpProjectOthers.do" />
    document.aimAmpActivityForm.action = "<%=addUrl%>";
    document.aimAmpActivityForm.submit();
}
function fnAddOrg()
{
	<digi:context name="addUrl" property="context/module/moduleinstance/addAmpOrganisations.do" />
    document.aimAmpActivityForm.action = "<%=addUrl%>";
    document.aimAmpActivityForm.submit();
}

function fnAddProgress()
{
	<digi:context name="addUrl" property="context/module/moduleinstance/addAmpPhysicalProgress.do" />
    document.aimAmpActivityForm.action = "<%=addUrl%>";
    document.aimAmpActivityForm.submit();
}
function fnAddKnowledge()
{
	<digi:context name="addUrl" property="context/module/moduleinstance/addAmpKnowledge.do" />
    document.aimAmpActivityForm.action = "<%=addUrl%>";
    document.aimAmpActivityForm.submit();
}
</script>
<digi:errors/>
<digi:instance property="aimAmpActivityForm" />
<digi:form action="/addAmpProjectOthers.do" method="post">
<html:javascript formName="highlightItemForm"/>
	<table width="700" border="0" height="533">
	<tr><td colspan="2" align="center" width="692" height="24"><h3><digi:trn key="aim:addNewProject">Add New Project</digi:trn></h3></td></tr>
		<td width="25%" height="501">
          <table border="0" width="100%" height="284" cellspacing="6">
            <tr>
              <td width="100%" height="38" bgcolor="#00FFFF"><a  name="aimAmpActivityForm" href="javascript:fnAddDetails()"><b><digi:trn key="aim:identification">Identification</digi:trn></b></a</td>
            </tr>
            <tr>
              <td width="100%" height="30" bgcolor="#00FFFF"><b><digi:trn key="aim:planning">Planning</digi:trn></b></td>
            </tr>
            <tr>
              <td width="100%" height="35" bgcolor="#00FFFF"><a  name="aimAmpActivityForm" href="javascript:fnAddOthers()"><b><digi:trn key="aim:projectDetails">Project Details</digi:trn></b></a></td>
            </tr>
            <tr>
              <td width="100%" height="37" bgcolor="#00FFFF"><a  name="aimAmpActivityForm" href="javascript:fnAddOrg()"><b><digi:trn key="aim:fundingOrganisation">Funding Organisation</digi:trn></b></a></td>
            </tr>
            <tr>
              <td width="100%" height="33" bgcolor="#00FFFF"><a  name="aimAmpActivityForm" href="javascript:fnAddProgress()"><b><digi:trn key="aim:physicalProgress">Physical Progress</digi:trn></b></a></td>
            </tr>
            <tr>
              <td width="100%" height="38" bgcolor="#00FFFF"><a  name="aimAmpActivityForm" href="javascript:fnAddKnowledge()"><b><digi:trn key="aim:relevantDocuments">Relevant Documents</digi:trn></b></a></td>
            </tr>
          </table>
    </td>
		<td width="75%" height="501">

		<table width=80% align=center>
			<tr><td width=30%><digi:trn key="aim:projectTitle">Project Title :</digi:trn></td>
			<td width=70%><html:text property="name" /></td></tr>
			<tr><td width=30%><digi:trn key="aim:projectModality">Project Modality :</digi:trn></td>
			<td width=70%><html:select property="ampModalityId" >
			<html:optionsCollection name="aimAmpActivityForm" property="modality" value="ampModalityId" label="name"/> 
			</html:select>
			</td></tr>
			<tr><td width=30%><digi:trn key="aim:projectStatus">Project Status :</digi:trn></td>
			<td width=70%><html:select property="ampStatusId" >
			<html:optionsCollection name="aimAmpActivityForm" property="status" value="ampStatusId" label="name"  /> 
			</html:select>
			</td></tr>
			
			<tr><td colspan=2 ><digi:trn key="aim:description">Description : </digi:trn></td></tr><tr>
<td colspan=2> <html:textarea property="description"/></td></tr>
<tr><td colspan=2>&nbsp;</td></tr>
<tr><td colspan=2><digi:trn key="aim:objective">Objective : </digi:trn></td></tr><tr>
<td colspan=2> <html:textarea property="objective"/></td></tr>

<tr>
	<td align="center" height="21"> <html:reset> Reset </html:reset> </td>
	<td align="center" height="21"> <html:submit> Next </html:submit> </td> 
</tr>

			</table>
		</digi:form></td></tr>

	</table>








