<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="javascript">

function validate(field) {
    alert(field);
	if (field == 2) { // validate sector
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


function addNewIndicator(){
	
 if(document.getElementById("txtName").value==""){
    alert("Please enter name");
    return false;
  }

  if(document.getElementById("txtCode").value==""){
    alert("Please enter code");
    return false;
  }

 
if(!document.aimNewIndicatorForm.IndType.checked && !document.aimNewIndicatorForm.IndicatorType.checked){
	   alert("Please select indicator type(s).!");
	   return false;
	   
 }
  if(!document.aimNewIndicatorForm.IndicatorType.checked)
      {  var valind = 1;
         document.getElementById("IndicatorTypChecke").value = valind;
      }
      
      if(!document.aimNewIndicatorForm.IndType.checked)
      {
         var valind = 1;
         document.getElementById("IndTypChecke").value = valind;
      }
 
 /*
	var length = document.aimNewIndicatorForm.selActivitySector.length;
	var Sector;
	
	if(!length){
		alert("Please add Sectors");
		 return false;
	}else{
		for(i = 0; i<length; i++){
			Sector = document.aimNewIndicatorForm.selActivitySector[i].value;
			document.getElementById("hdnselActivitySectors").value = Sector;
		}
	}
*/
  <digi:context name="addInd" property="context/module/moduleinstance/addNewIndicator.do?action=add" />
  document.forms[0].action="<%=addInd%>";
  document.forms[0].submit();
  window.opener.location.reload(true);
  window.opener.focus();
  window.close();
}

function addSectors() {

 		openNewWindow(600, 450);
		<digi:context name="addSector" property="context/module/moduleinstance/selectSectorForind.do?edit=true" />
	  	document.aimNewIndicatorForm.action = "<%= addSector %>";
		document.aimNewIndicatorForm.target = popupPointer.name;
		document.aimNewIndicatorForm.submit();
}

function removeSelSectors() {
		  var flag = validate(2);
		  if (flag == false) return false;
		  
          <digi:context name="remSec" property="context/module/moduleinstance/removeIndicatorSelSectors.do?edit=true" />
          document.aimNewIndicatorForm.action = "<%= remSec %>";
          document.aimNewIndicatorForm.target = "_self"
          document.aimNewIndicatorForm.submit();
          return true;
}

function selectProgram(){

  <digi:context name="selPrg" property="context/module/moduleinstance/selectProgramForIndicator.do" />
  openURLinWindow("<%= selPrg %>",700, 500);
}

function selectActivity(){
  <digi:context name="selAct" property="context/module/moduleinstance/selectActivityForIndicator.do" />
  openURLinWindow("<%= selAct %>",700, 500);
  
}

function closeWindow() {
		window.close();
	}
</script>
<digi:instance property="aimNewIndicatorForm" />
<digi:form action="/addNewIndicator.do" method="post">
  <html:hidden property="type" value="3"/>
  <html:hidden property="trType" value="3"/>
  <html:hidden property="category" value="-1"/>
  <html:hidden property="indType"  styleId="IndTypChecke"/>
  <html:hidden property="indicatorType"  styleId="IndicatorTypChecke"/>
  <html:hidden property="selActivitySector" styleId="hdnselActivitySectors" />

  <table width="100%" align="center" class=box-border-nopadding>
    <tr bgcolor="#006699" class=r-dotted-lg>
      <td vAlign="center" width="100%" align ="center" class="textalb" height="20">
      <b><digi:trn key="aim:addnewindicator">Add New Indicator</digi:trn></b>
      </td>
    </tr>
    <tr align="center" bgcolor="#ECF3FD">
      <td>
        <table border="0">
          <tr id="trName">
            <td>
            Indicator name:<span style="color:Red;">*</span>
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
            Indicator code:<span style="color:Red;">*</span>
            </td>
            <td>
               <html:text property="code" styleId="txtCode" style="font-family:verdana;font-size:11px;width:100px;"/>
            </td>
          </tr>
          <tr id="trType">
          </tr>
          <tr id="trCategory">
          </tr>
          <tr id="trSector">
          <td>Sectors<span style="color:Red;">*</td>
            <td >
              <jsp:include page="addIndicatorSector.jsp"/>
             </td>
          </tr>   
	      <tr id="trCreationDate">
            <td>
            Creation date:
            </td>
            <td>
              <html:text property="date" disabled="true" styleId="txtCreationDate" style="font-family:verdana;font-size:11px;width:80px;"/>
            </td>
          </tr>
          <tr>
            <td colspan="10" nowrap="nowrap">
              <input type="checkbox" name="IndicatorType" id="radioProgramIndicator" value="0" /> &nbsp;Program indicator&nbsp;
              <!-- 
               <span id="spnSelectProgram" style="">
                 [<a href="javascript:selectProgram();">Select program</a>]
                <c:if test="${!empty aimNewIndicatorForm.selectedPrograms}">
                  <c:forEach var="prog" items="${aimNewIndicatorForm.selectedPrograms}">
                    [${prog.label}]
                    <html:hidden property="selectedProgramId" value="${prog.value}"/>
                  </c:forEach>
                </c:if>
                <c:if test="${empty aimNewIndicatorForm.selectedPrograms}">
                  [<span style="color:Red;">Program is not selected</span>]<span style="color:Red;">*</span>
                </c:if>
              </span>
               -->
              <br>
              <input type="checkbox" name="IndType" id="radioProjectIndicator" value="2" /> &nbsp;Project indicator&nbsp;
              <span id="spnSelectProject" style="">
                     [<a href="javascript:selectActivity();">Select project</a>]
                     <c:if test="${!empty aimNewIndicatorForm.selectedActivities}">
                       <c:forEach var="act" items="${aimNewIndicatorForm.selectedActivities}">
                         [${act.label}]
                       </c:forEach>
                   </c:if>
                   <c:if test="${empty aimNewIndicatorForm.selectedActivities}">
                       [<span style="color:Red;">Activity is not selected</span>]
                  </c:if>
             </span>
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr  align="center" bgcolor="#ECF3FD">
      <td>
      <html:button  styleClass="dr-menu" property="submitButton"  onclick="addNewIndicator()">
		<digi:trn key="btn:add">Add</digi:trn> 													
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
