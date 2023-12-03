<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule"%>

<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/common.js"/>"></script>

<script language="javascript">

function validate(field) {
//    alert(field);
	<c:set var="translation">
		<digi:trn key="admin:chooseSectorToRemove">Please choose a sector to remove</digi:trn>
	</c:set>
	if (field == 2) { // validate sector
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


function addNewIndicator(){
		
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
  
  <field:display name="Admin Indicator Type" feature="Admin">
  
  if(document.aimNewIndicatorForm.type.value!="A"&&document.aimNewIndicatorForm.type.value!="D"){
      <c:set var="translation">
               <digi:trn key="admin:selectIndicatorType">Please Select Indicator Type</digi:trn>
       </c:set>
	alert("${translation}");
   return false;
 }
  
  </field:display>
  
<%-- 
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
      	var checkbox = document.aimNewIndicatorForm.IndicatorType.checked
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
		
	<digi:context name="addInd" property="context/ampModule/moduleinstance/addNewIndicator.do?action=add" />
	document.aimNewIndicatorForm.action = "<%= addInd %>";
	document.aimNewIndicatorForm.target = "_self";
	document.aimNewIndicatorForm.submit();
	

  
  //document.forms[0].action="<%=addInd%>";
  //document.forms[0].submit();
 // window.opener.location.reload(true);
 // window.opener.focus();
  //window.close();
  //window.opener.viewall();
}

function addSectors() {
		openNewWindow(600, 450);
		<digi:context name="addSector" property="context/ampModule/moduleinstance/selectSectorForind.do?edit=true" />
	  	document.aimNewIndicatorForm.action = "<%= addSector %>";
		document.aimNewIndicatorForm.target = popupPointer.name;
		document.aimNewIndicatorForm.submit();
}

function removeSelSectors() {
		  var flag = validate(2);
		  if (flag == false) return false;
		  
          <digi:context name="remSec" property="context/ampModule/moduleinstance/removeIndicatorSelSectors.do?edit=true" />
          document.aimNewIndicatorForm.action = "<%= remSec %>";
          document.aimNewIndicatorForm.target = "_self"
          document.aimNewIndicatorForm.submit();
          return true;
}

function resetAddNewIndicator()
{
	return true;
	/*
	$('#txtName').val("");
	$('#txtDescription').val("");
	$('#txtCode').val("");
	$('#selectSorting').val("A");*/
}

function selectProgram(){

  <digi:context name="selPrg" property="context/ampModule/moduleinstance/selectProgramForIndicator.do" />
  openURLinWindow("<%= selPrg %>",700, 500);
}

function selectActivity(){
  <digi:context name="selAct" property="context/ampModule/moduleinstance/selectActivityForIndicator.do" />
  openURLinWindow("<%= selAct %>",700, 500);
  
}

function closeWindow() {
		window.close();
	}
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
		document.aimNewIndicatorForm.action = "<%=update%>&aId="+id;
	    document.aimNewIndicatorForm.target = "_self"
	    document.aimNewIndicatorForm.submit();
	    return true;		  
    }
}
	
	
</script>
<digi:instance property="aimNewIndicatorForm" />
<digi:form action="/addNewIndicator.do" method="post">
    
<script language="javascript">
<c:if test="${aimNewIndicatorForm.action=='added'}">
    window.opener.location.href = window.opener.location.href;
    window.opener.focus();
    window.close();
</c:if>
  
</script>
 <!-- <html:hidden property="type" value="3"/> --> 
  <html:hidden property="trType" value="3"/>
  <html:hidden property="category" value="-1"/> 
  <html:hidden property="selActivitySector" styleId="hdnselActivitySectors" />

  <table  bgcolor="#F8F8F8" cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
    <tr  bgcolor="#4A687A">
      <td vAlign="center" width="100%" align ="center"  style="color: #FFF;font-family:verdana;font-size:12px;">
      <b><digi:trn key="aim:addnewindicator">Add New Indicator</digi:trn></b>
      </td>
    </tr>
    <tr>
         <td>
            <digi:errors/>
        </td>
    </tr>
    <tr align="center"  bgcolor="#F8F8F8">
       
      <td>
          <table border="0" class="inside">
        <field:display name="Admin Indicator name" feature="Admin"></field:display>
          <tr id="trName" class="addNewIndicatorLabel">
            <td>
            <digi:trn key="aim:indicatorname">Indicator name:</digi:trn>
            <span style="color:Red;">*</span>
            </td>
            <td>
              <html:text property="name" styleId="txtName" style="font-family:verdana;font-size:11px;width:200px;"/>
            </td>
          </tr>
          
          <field:display name="Admin Description" feature="Admin">
          <tr id="trDescription">
            <td valign="top" class="addNewIndicatorLabel">
            <digi:trn>Indicator Description</digi:trn>
            </td>
            <td>
              <html:textarea property="description" styleId="txtDescription" style="font-family:verdana;font-size:11px;width:200px;"></html:textarea>
            </td>
          </tr>
          </field:display>
          <field:display name="Admin Indicator code" feature="Admin"></field:display>
          <tr>
            <td class="addNewIndicatorLabel">
            	<digi:trn key="admin:indicatorcode">
            	Indicator code:
            	</digi:trn>
            	<span style="color:Red;">*</span>
            </td>
            <td>
               <html:text property="code" styleId="txtCode" style="font-family:verdana;font-size:11px;width:100px;"/>
            </td>
          </tr>
          
          <field:display name="Admin Indicator Type" feature="Admin">
          <tr>
          	<td class="addNewIndicatorLabel"><digi:trn key="admin:indicatorType">Indicator Type</digi:trn>: <span style="color:Red;">*</span></td>
          	<td><html:select name="aimNewIndicatorForm" property="type" styleId="selectSorting">          		
          		<html:option value="A"><digi:trn key="admin:indicatorType:ascending">ascending</digi:trn></html:option>
          		<html:option value="D"><digi:trn key="admin:indicatorType:descending">descending</digi:trn></html:option>
          	</html:select>
          	</td>
          </tr>
          </field:display>
          <tr id="trType">
          </tr>
          <tr id="trCategory">
          </tr>
          <field:display name="Sectors" feature="Admin"></field:display> 
			<tr>
				<td class="addNewIndicatorLabel">
					<digi:trn key="admin:sectors">
						Sectors
					</digi:trn>
					<span style="color:Red;">*</span>
				</td>
				<td>
					<jsp:include page="addIndicatorSector.jsp"/>
				</td>
			</tr>
			<tr id="trSector">
			</tr>
          <field:display name="Creation date" feature="Admin">
	      <tr id="trCreationDate">
            <td  class="addNewIndicatorLabel">
            <digi:trn key="admin:creationdate">
            Creation date:
            </digi:trn>
            </td>
            <td>
              <html:text property="date" disabled="true" styleId="txtCreationDate" style="font-family:verdana;font-size:11px;width:80px;"/>
            </td>
          </tr> 
          </field:display>
          <!--
          <tr>
            <td colspan="10" nowrap="nowrap">
              <input type="checkbox" name="IndicatorType" id="radioProgramIndicator" value="0" /> &nbsp;<digi:trn key="admin:programind">Program indicator</digi:trn>&nbsp;
              
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
              <br>
              <input type="checkbox" name="IndType" id="radioProjectIndicator" value="2" /> 
              &nbsp;<digi:trn key="admin:projectind">Project indicator</digi:trn>&nbsp;
                     [<a href="javascript:selectActivity();"><digi:trn key="admin:selectproject">Select project</digi:trn></a>]
                     
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
                       [<span style="color:Red;"><digi:trn key="admin:activitynoselected">Activity is not selected</digi:trn></span>]
                  </c:if>                   
            </table>
            </td>
          </tr>
           -->
        </table>
      </td>
    </tr>
    <tr  align="center" bgcolor="#F8F8F8">
      <td>
      <field:display name="Add New Indicator" feature="Admin">
      <html:button  styleClass="buttonx" property="submitButton"  onclick="addNewIndicator()">
		<digi:trn key="btn:add">Add</digi:trn> 													
 	 </html:button>
 	 </field:display>
 	
<%-- <html:reset  styleClass="buttonx" property="submitButton">
		<digi:trn key="btn:clear">Clear</digi:trn> 
	</html:reset>
--%>	 
 	
	<input type="submit" name="clearButton" value='<digi:trn jsFriendly="true" key="btn:clear">Clear</digi:trn>' class="buttonx" onclick="resetAddNewIndicator();"/>
 
 	 <html:button  styleClass="buttonx" property="submitButton"  onclick="closeWindow()">
			<digi:trn key="btn:close">Close</digi:trn> 
	 </html:button>
      </td>
    </tr>
    <tr>
      <td bgcolor="#F8F8F8">
       
      </td>
    </tr>
  </table>
</digi:form>
