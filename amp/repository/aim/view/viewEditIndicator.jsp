<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/calendar.js"/>"></script>

<digi:instance property="aimNewIndicatorForm" />
<script language="javascript">
function saveIndicator(){
  if(document.getElementById("txtName").value==""){
    alert("Please enter name");
    return false;
  }
  if(document.getElementById("txtCode").value==""){
    alert("Please enter code");
    return false;
  }
  
  /*
  if(document.aimNewIndicatorForm.selActivitySector.value == ""){
		alert("Please add sectors");
		 return false;
	}else{
		var Sector = document.aimNewIndicatorForm.selActivitySector.value;
		document.getElementById("hdnselActivitySectors").value = Sector;
	}
  */
  
  <digi:context name="addInd" property="context/module/moduleinstance/viewEditIndicator.do?action=save" />
  document.forms[0].action="<%=addInd%>";
  document.forms[0].submit();
  window.close();
  window.opener.document.forms[0].submit();
}

function selectProgram(){
  <digi:context name="selPrg" property="context/module/moduleinstance/selectProgramForIndicator.do?action=edit" />
  openURLinWindow("<%= selPrg %>",700, 500);
}

function selectActivity(){

  <digi:context name="selAct" property="context/module/moduleinstance/selectActivityForIndicator.do?action=edit" />
  openURLinWindow("<%= selAct %>",700, 500);
}
  /*
function radiosStatus(type){

  if(type=="0"){
    document.getElementById("radioProgramIndicator").checked=true;
    document.getElementById("radioProjectIndicator").checked=false;
  }else if(type=="1"){
    document.getElementById("radioProgramIndicator").checked=false;
    document.getElementById("radioProjectIndicator").checked=true;
  }else
  
  
 */ 


 /*
function changeIndType(type){
 
   document.getElementById("hdnIndType").value=type;
  if(type=="0"){
    document.getElementById("trType").style.display="";
    document.getElementById("trCategory").style.display="";
    document.getElementById("trDescription").style.display="";

    document.getElementById("trCreationDate").style.display="";

     document.getElementById("Sectors").style.display="";
    document.getElementById("tTypes").style.display="none";
    document.getElementById("radioProjectIndicatore").style.display="";
    //document.getElementById("spnSelectProgram").style.display="";
    document.getElementById("spnSelectProject").style.display="none"
  }else if(type=="1"){
    document.getElementById("trType").style.display="none";
    document.getElementById("trCategory").style.display="none";
    document.getElementById("trCreationDate").style.display="none";

document.getElementById("trType").style.display="none";
    document.getElementById("tTypes").style.display="none";
    document.getElementById("radioProjectIndicatore").style.display="";
    //document.getElementById("spnSelectProgram").style.display="none";
    if(document.getElementById("radioProjectSpecific").checked){
      document.getElementById("spnSelectProject").style.display="";
      document.getElementById("trDescription").style.display="";
    }else{
      document.getElementById("spnSelectProject").style.display="none";
      document.getElementById("trDescription").style.display="none";
    }
  }else 
  
  if(type=="2"){
    
    document.getElementById("trType").style.display="none";
    document.getElementById("trCategory").style.display="none";
    document.getElementById("trCreationDate").style.display="none";
    document.getElementById("trDescription").style.display="";
     document.getElementById("Sectors").style.display="";
    document.getElementById("tTypes").style.display="";
    document.getElementById("radioProjectIndicator").style.display="";
    document.getElementById("spnSelectProject").style.display="";
    
  }
  
}
*/


function validate(field) {

	if (field == 2){  // validate sector
		if (document.aimNewIndicatorForm.selActivitySector.checked != null) {
			if (document.aimNewIndicatorForm.selActivitySector.checked == false) {
				alert("Please choose a sector to remove");
				return false;
			}
		} else {
			var length = document.aimNewIndicatorForm.selActivitySector.length;
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.aimNewIndicatorForm.selActivitySector[i].checked == true) {
					flag = 1;
					break;
				}
			}

			if (flag == 0) {
				alert("Please choose a sector to remove");
				return false;
			}
		}
		return true;
	}
}

function removeSelSectors() {
		  var flag = validate(2);
		  if (flag == false) return false;
          <digi:context name="remSec" property="context/module/moduleinstance/removeIndicatorEditSectors.do?edit=true" />
          document.aimNewIndicatorForm.action = "<%= remSec %>";
          document.aimNewIndicatorForm.target = "_self"
          document.aimNewIndicatorForm.submit();
          return true;
}

function addSectors() {
		openNewWindow(600, 450);
		<digi:context name="addSector" property="context/module/moduleinstance/editSectorForind.do?edit=true" />
	  	document.aimNewIndicatorForm.action = "<%= addSector %>";
		document.aimNewIndicatorForm.target = popupPointer.name;
		document.aimNewIndicatorForm.submit();
}
 
function closeWindow() {
		window.close();
	}

</script>
<digi:form action="/viewEditIndicator.do" method="post">
  <html:hidden property="indType" styleId="hdnIndType" />
  <html:hidden name="aimNewIndicatorForm" property="themeId" styleId="hdnThemeId" />
   <html:hidden property="selActivitySector" styleId="hdnselActivitySectors" />

  <table width="100%" align="center" border="0" class=box-border-nopadding>
    <tr bgcolor="#006699" class=r-dotted-lg >
      <td colspan="1" align="center" class="textalb">
      <b>View/Edit Indicator</b>
      </td>
    </tr>
    <tr align="center" bgcolor="#ECF3FD">
      <td>
        <table>
          <tr id="trName">
            <td>
            Indicator name:
            </td>
            <td>
              <html:text property="name" styleId="txtName" style="font-family:verdana;font-size:11px;width:200px;"/>
            </td>
          </tr>
          <tr id="trDescription">
            <td valign="top">
            Description:
            </td>
            <td>
              <html:textarea property="description" styleId="txtDescription" style="font-family:verdana;font-size:11px;width:200px;"></html:textarea>
            </td>
          </tr>
          <tr>
            <td>
            Indicator code:
            </td>
            <td>
               <html:text property="code" styleId="txtCode" style="font-family:verdana;font-size:11px;width:100px;"/>
            </td>
          </tr>
          <tr id="trType">
          </tr>
          <tr id="trCategory">
          </tr>
          <tr>
            <td>Sectors:</td>
             <td>
              <jsp:include page="addIndicatorSector.jsp"/>
            </td>
          </tr>  
          <tr id="trCreationDate">
            <td>
            Creation date:
            </td>
            <td>
               <html:text property="date" styleId="date" readonly="true" style="font-family:verdana;font-size:11px;width:80px;"/>
                  <a href="javascript:calendar('date')">
              <img src="../ampTemplate/images/show-calendar.gif" border="1" alt="" />
            </a>
            </td>
          </tr>
          <tr>
            <td colspan="10" nowrap="nowrap">
              <input type="checkbox" name="indTypeRadio" id="radioProgramIndicator" checked="checked"/> &nbsp;Program indicator&nbsp;
              <br />
                   <input type="checkbox" name="tradio" id="radioProjectSpecific"  value="" checked="checked" /> &nbsp;Project specific&nbsp;
                   [<a href="javascript:selectActivity();">Select project</a>]<c:if test="${!empty aimNewIndicatorForm.selectedActivities}"><c:forEach var="act" items="${aimNewIndicatorForm.selectedActivities}">[${act.label}]</c:forEach></c:if><c:if test="${empty aimNewIndicatorForm.selectedActivities}">[<span style="color:Red;">Activity is not selected</span>]</c:if>
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr align="center" bgcolor="#ECF3FD">
      <td>
      <c:forEach var="act" items="${aimNewIndicatorForm.selectedActivities}">
      <html:hidden property="selectedActivityId"  value="${act.value}"/>
      </c:forEach>
      <html:button  styleClass="dr-menu" property="submitButton"  onclick="saveIndicator();">
			<digi:trn key="btn:Edit">Edit</digi:trn> 
	     </html:button>
         <html:reset  styleClass="dr-menu" property="submitButton">
		   <digi:trn key="btn:clear">Clear</digi:trn> 
	     </html:reset>											
 	     <html:button  styleClass="dr-menu" property="submitButton"  onclick="closeWindow()">
			<digi:trn key="btn:close">Close</digi:trn> 
	     </html:button>
      </td>
    </tr>
    <tr>
      <td bgcolor="#006699">
       
      </td>
    </tr>
  </table>
</digi:form>
