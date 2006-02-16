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
function open_win()
{
window.open("http://amp.ncb.ernet.in:8080/digijava/aim/addPhysicalProgress.do","my_new_window","toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=no, copyhistory=yes, width=400, height=400")
}

</script>

<digi:errors/>
<digi:form action="/addAmpKnowledge.do" method="post">
	<digi:instance property="aimAmpActivityForm" />
	<table width="700" border="0" height="533">
	<tr><td colspan="2" align="center" width="692" height="24"><h3>Add New Project</h3></td></tr>
	<tr><td width="25%" height="501">
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
		<td width="75%" height="501" valign="top"><br/><table align="center">
		
		<tr><td valign="middle"><b><digi:trn key="aim:addNewPhysicalProgress">Add New Physical Progress :</digi:trn></b></td>

		<td><b><html:button property="button" value="Add" onclick="open_win()" /><b></b></td></tr>
		<tr><td><html:submit value="Next"/></td></tr>
		</table></td></tr>

	</table>
</digi:form>
