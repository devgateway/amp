<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/calendar.js"/>"></script>

<digi:instance property="aimNewIndicatorForm" />
<script language="javascript">
	
<%--	function validete (){
	
	if(document.getElementById("radioProgramIndicator").checked){
	   alert("ok");
	}else{
	      alert("no");  }  
	
	if(document.getElementById("radioProjectSpecific").checked){
	   alert("ok1");
	 }else{
	 alert("no1");
	 }
	 
	}
--%>

function removeActivity(id) {
	<c:set var="translation">
		<digi:trn key="admin:deleteThisActivity">Do you want to delete this Activity?</digi:trn>
	</c:set>
	var temp = confirm("${translation}");
	if(temp == false)
	{
			return false;
	}
	else
	 {
		<digi:context name="update" property="context/ampModule/moduleinstance/selectActivityForIndicator.do?action=remove" />
		document.aimNewIndicatorForm.action = "<%=update%>&forward=edit&aId="+id;
	    document.aimNewIndicatorForm.target = "_self"
	    document.aimNewIndicatorForm.submit();
	    return true;		  
    }
}


function saveIndicator(){

 if(document.getElementById("txtName").value==""){
    <c:set var="translation">
		<digi:trn key="admin:enterName">Please enter name</digi:trn>
	</c:set>
	alert("${translation}");
    return false;
  }

  if(document.getElementById("txtCode").value==""){
    <c:set var="translation">
		<digi:trn key="admin:enterCode">Please enter code</digi:trn>
	</c:set>
	alert("${translation}");
    return false;
  }
 
 <%-- 
  if(!document.getElementById("radioProgramIndicator").checked){
		 var indStatus = "prgUnchecked"
		 document.getElementById("programStatus").value = indStatus;
	}	
	
	if(!document.getElementById("radioProjectSpecific").checked){
		var indStatus = "prjUnchecked"		
		document.getElementById("projectStatus").value = indStatus;		
	}
	--%>   
		
	  
		var length = document.aimNewIndicatorForm.selActivitySector.length;		
		var Sector;
		
		if(!length){
			<c:set var="translation">
				<digi:trn key="admin:addSector">Please add Sectors</digi:trn>
			</c:set>
			alert("${translation}");
			return false;
		}else{
			for(i = 0; i<length; i++){
				Sector = document.aimNewIndicatorForm.selActivitySector[i].value;
				document.getElementById("hdnselActivitySectors").value = Sector;
			}
		} 

  
  <digi:context name="addInd" property="context/ampModule/moduleinstance/viewEditIndicator.do?action=save" />
  
  //document.forms[0].action="<%=addInd%>";
  //document.forms[0].submit();
  //window.close();
  //window.opener.document.forms[0].submit();
  
  document.aimNewIndicatorForm.action = "<%=addInd%>";
  document.aimNewIndicatorForm.target="_self";
  document.aimNewIndicatorForm.submit(); 
 
}

function selectProgram(){
  <digi:context name="selPrg" property="context/ampModule/moduleinstance/selectProgramForIndicator.do?action=edit" />
  openURLinWindow("<%= selPrg %>",700, 500);
}

function selectActivity(){

  <digi:context name="selAct" property="context/ampModule/moduleinstance/selectActivityForIndicator.do?action=edit" />
  openURLinWindow("<%= selAct %>",700, 500);
}
 <%-- 
function radiosStatus(type){
	
	  if(type=="global"){
	    document.getElementById("radioProgramIndicator").checked=false;
	    document.getElementById("radioProjectSpecific").checked=true;
	  }else
	  if(type == "prg/prj "){
	    document.getElementById("radioProgramIndicator").checked=true;
	    document.getElementById("radioProjectSpecific").checked=true;
	  }else 
	  if(type == "programInd"){
	    document.getElementById("radioProgramIndicator").checked=true;
	    document.getElementById("radioProjectSpecific").checked=false;
	  }else
	  if(type == "projectInd"){
	    document.getElementById("radioProgramIndicator").checked=false;
	    document.getElementById("radioProjectSpecific").checked=true;
	  }
 }
 --%>
 


function validate(field) {
	<c:set var="translation">
		<digi:trn key="admin:chooseSectorToRemove">Please choose a sector to remove</digi:trn>
	</c:set>
	if (field == 2){  // validate sector
		if (document.aimNewIndicatorForm.selActivitySector.checked != null) {
			if (document.aimNewIndicatorForm.selActivitySector.checked == false) {
				alert("${translation}");
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
				alert("${translation}");
				return false;
			}
		}
		return true;
	}
}

function removeSelSectors() {
		  var flag = validate(2);
		  if (flag == false) return false;
          <digi:context name="remSec" property="context/ampModule/moduleinstance/removeIndicatorEditSectors.do?edit=true" />
          document.aimNewIndicatorForm.action = "<%= remSec %>";
          document.aimNewIndicatorForm.target = "_self"
          document.aimNewIndicatorForm.submit();
          return true;
}

function addSectors() {
		openNewWindow(600, 450);
		<digi:context name="addSector" property="context/ampModule/moduleinstance/editSectorForind.do?edit=true" />
	  	document.aimNewIndicatorForm.action = "<%= addSector %>";
		document.aimNewIndicatorForm.target = popupPointer.name;
		document.aimNewIndicatorForm.submit();
}
 
function closeWindow() {
		window.close();
	}

</script>
<digi:form action="/viewEditIndicator.do" method="post"> 
<script language="javascript">
<c:if test="${aimNewIndicatorForm.action=='added'}">
    window.opener.location.reload(true);
    window.opener.focus();
    window.close();
</c:if>
  
</script>
  <html:hidden property="prjStatus" styleId="projectStatus" />
  <html:hidden property="prgStatus" styleId="programStatus" /> 
  <html:hidden name="aimNewIndicatorForm" property="themeId" styleId="hdnThemeId" />
  <html:hidden property="selActivitySector" styleId="hdnselActivitySectors" />

  <table bgcolor="#F8F8F8" cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
    <tr bgcolor="#4A687A" >
      <td colspan="1" align="center" style="color: #FFF;font-family:verdana;font-size:12px;">
      <b><digi:trn key="aim:vieweditindicator">View/Edit Indicator</digi:trn></b>
      </td>
    </tr>
    <tr>
          <td>
            <digi:errors/>
        </td>
    </tr>
    <tr align="center" bgcolor="#F8F8F8">
      <td>
          <table border="0" class="inside">
          <tr id="trName">
            <td>
            <b style="color: red;">*</b> <digi:trn key="aim:indicatorname">Indicator name:</digi:trn>
            </td>
            <td>
              <html:text property="name" styleId="txtName" style="font-family:verdana;font-size:11px;width:200px;"/>
            </td>
          </tr>
          <tr id="trDescription">
            <td valign="top">
            <digi:trn>Indicator Description</digi:trn>:
            </td>
            <td>
              <html:textarea property="description" styleId="txtDescription" style="font-family:verdana;font-size:11px;width:200px;"></html:textarea>
            </td>
          </tr>
          <tr>
            <td>
            <b style="color: red;">*</b> <digi:trn key="admin:indicatorcode">Indicator code:</digi:trn>
            </td>
            <td>
               <html:text property="code" styleId="txtCode" style="font-family:verdana;font-size:11px;width:100px;"/>
            </td>
          </tr>
          <tr>
          	<td><digi:trn key="admin:indicatorType">Indicator Type</digi:trn>:</td>
          	<td><html:select name="aimNewIndicatorForm" property="type">          		
          		<html:option value="A"><digi:trn key="admin:indicatorType:ascending">ascending</digi:trn></html:option>
          		<html:option value="D"><digi:trn key="admin:indicatorType:descending">descending</digi:trn></html:option>
          	</html:select>
          	</td>
          </tr>
          <tr id="trType">
          </tr>
          <tr id="trCategory">
          </tr>
          <tr>
            <td><b style="color: red;">*</b> <digi:trn key="admin:sectors">Sectors</digi:trn></td>
             <td>
              <jsp:include page="addIndicatorSector.jsp"/>
            </td>
          </tr>  
          <tr id="trCreationDate">
            <td>
            <digi:trn key="admin:creationdate">
            Creation date:
            </digi:trn>
            </td>
            <td>
               <html:text property="date" styleId="date" disabled="true" readonly="true" style="font-family:verdana;font-size:11px;width:80px;"/>
            </a>
            </td>
          </tr>
          <!-- 
          <tr>
            <td colspan="10" nowrap="nowrap">
              <input type="checkbox" name="indTypeRadio" id="radioProgramIndicator"  checked="checked"/> 
              &nbsp;Program indicator&nbsp;
             <br />
              <input type="checkbox" name="tradio" id="radioProjectSpecific"  checked="checked" />
                   &nbsp;Project specific&nbsp;
                      [<a href="javascript:selectActivity();">Select project</a>]
                      <br>
                 </td>
                </tr>
                <tr> 
                 <td colspan="10" nowrap="nowrap" align="center" bgcolor="#f4f4f2">
                   <table width="98%" cellPadding=2 cellspacing="0" valign="top" align="center" class="box-border-nopadding">
						<c:if test="${!empty aimNewIndicatorForm.selectedActivities}">
	            		   <c:forEach var="act" items="${aimNewIndicatorForm.selectedActivities}">
			                   <tr onmouseover="style.backgroundColor='#dddddd';" onmouseout="style.backgroundColor='white'">
				                   <td align="left" >&nbsp;
				                     ${act.label}
				                  </td>
				                  <td align="right">
				                  <a href="javascript:removeActivity(${act.value})">
																	 	<digi:img src="../ampTemplate/images/deleteIcon.gif" 
																		border="0" alt="Remove this child workspace"/></a>&nbsp;
				                  </td>
			                  </tr>
	                      </c:forEach>									
			          </c:if>
			          <c:if test="${empty aimNewIndicatorForm.selectedActivities}">
                         [<span style="color:Red;">Activity is not selected</span>]
                      </c:if>
				 </table>									
            </td>
          </tr>
           -->
        </table>
      </td>
    </tr>
    <tr align="center" bgcolor="#F8F8F8">
      <td>
      <c:forEach var="act" items="${aimNewIndicatorForm.selectedActivities}">
      <html:hidden property="selectedActivityId"  value="${act.value}"/>
      </c:forEach>
      <html:button  styleClass="buttonx" property="submitButton"  onclick="saveIndicator();">
			<digi:trn key="btn:Save">Save</digi:trn> 
	     </html:button>
         <html:reset  styleClass="buttonx" property="submitButton">
		   <digi:trn key="btn:clear">Clear</digi:trn> 
	     </html:reset>											
 	     <html:button  styleClass="buttonx" property="submitButton"  onclick="closeWindow()">
			<digi:trn key="btn:close">Close</digi:trn> 
	     </html:button>
      </td>
    </tr>
  </table>
</digi:form>

<script language="javascript">
//radiosStatus(document.getElementById("Intype").value);
</script>
