<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>



<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<jsp:include page="addSectors.jsp"  />

<script language="JavaScript" type="text/javascript">
<!--
function goNextStep(){
alert("goNextStep");
  if(validateForm()){
    <digi:context name="nextStepUrl" property="context/module/moduleinstance/addActivity.do?edit=true" />
    document.aimEditActivityForm.action = "<%= nextStepUrl %>";
    document.aimEditActivityForm.submit();
  }
}

function validate(field) {
  if (field == 1) { // validate location
	  if (document.aimEditActivityForm.selLocs.checked != null) {
	    if (document.aimEditActivityForm.selLocs.checked == false) {
	      alert("Please choose a location to remove");
	      return false;
	    }
	  } else {
	    var length = document.aimEditActivityForm.selLocs.length;
	    var flag = 0;
	    for (i = 0;i < length;i ++) {
	      if (document.aimEditActivityForm.selLocs[i].checked == true) {
	        flag = 1;
	        break;
	      }
	    }
	
	    if (flag == 0) {
	      alert("Please choose a location to remove");
	      return false;
	    }
	  }
	  return true;
  } else { // validate sector
	if  (document.aimEditActivityForm.selActivitySectors!=null){//checks if there is an sector in the list
		if (document.aimEditActivityForm.selActivitySectors.length==null ) {//check if there is only 1 sector
	  		if (document.aimEditActivityForm.selActivitySectors.checked == false) {//check if it is selected 
	    		alert("Please choose a sector to remove");
	    		return false;
	  		}
	  		return true;
		} else {
		  for (i = 0;i < document.aimEditActivityForm.selActivitySectors.length;i ++) {
		    if (document.aimEditActivityForm.selActivitySectors[i].checked == true) {
		      return true;
		    }
		  }
		  alert("Please choose a sector to remove");
		  return false;
		}
	}
	alert("Please choose a sector to remove");
	return false;
}
}

function selectLocation() {
  openNewWindow(600, 500);
  <digi:context name="selectLoc" property="context/module/moduleinstance/selectLocation.do?edit=false" />
  document.aimEditActivityForm.action = "<%= selectLoc %>";
  document.aimEditActivityForm.target = popupPointer.name;
  document.aimEditActivityForm.submit();
}


 function addSectors(editAct,configId) {
/*  openNewWindow(600, 450);
  document.aimEditActivityForm.action = "/selectSectors.do?edit=true&configId="+configId;
  document.aimEditActivityForm.target = popupPointer.name;
  document.aimEditActivityForm.submit();
*/ 
     //alert("configId="+configId);
	 myAddSectors("edit=true&configId="+configId);	  
}

function resetAll(){
  <digi:context name="resetAll" property="context/module/moduleinstance/resetAll.do?edit=true" />
  document.aimEditActivityForm.action = "<%= resetAll %>";
  document.aimEditActivityForm.target = "_self";
  document.aimEditActivityForm.submit();
  return true;
}

function removeSelLocations(){
  var flag = validate(1);
  if (flag == false) return false;
  <digi:context name="remLocs" property="context/module/moduleinstance/removeSelLocations.do?edit=true" />
  document.aimEditActivityForm.action = "<%= remLocs %>";
  document.aimEditActivityForm.target = "_self"
  document.aimEditActivityForm.submit();
  return true;
}


function removeAllLocations(){
  try{
  
  	//var checkedItems = document.getElementsById("selLocs");
  	var checkedItems = document.aimEditActivityForm.selLocs;
  	if(checkedItems.length > 0){
	  	for(a=0;a<checkedItems.length;a++){
	  	checkedItems[a].checked = true;
	  }
	  removeSelLocations();
  	}else if(checkedItems!=null){
	  		checkedItems.checked = true;
	  		removeSelLocations();
  	}else{
  	  	return false;
	}
  	return true;
  }
  catch(err){
  	return false;
  
  }
  
  <digi:context name="remLocs" property="context/module/moduleinstance/removeSelLocations.do?edit=true" />
  document.aimEditActivityForm.action = "<%= remLocs %>";
  document.aimEditActivityForm.target = "_self"
  document.aimEditActivityForm.submit();
  return true;
}

function locationLevelChanged(){
	 <digi:context name="locationLevelChanged" property="context/module/moduleinstance/locationLevelChanged.do?edit=true" />
	 document.aimEditActivityForm.action = "<%= locationLevelChanged %>";
	 document.aimEditActivityForm.target = "_self"
	 document.aimEditActivityForm.submit();
	 return true;

}



function validateForm(){
  var draftStatus=document.getElementById("draftFlag");
  if(draftStatus!=null && draftStatus.value!="true"){
  
    var npoSize = document.aimEditActivityForm.sizeNPOPrograms.value;
    var ppSize = document.aimEditActivityForm.sizePPrograms.value;
    var spSize = document.aimEditActivityForm.sizeSPrograms.value;
    if ( /*<feature:display name="Sectors" module="Project ID and Planning"> !validateSectorPercentage()</feature:display> */
     <field:display name="Regional Percentage" feature="Location">
   	  !validateLocationPercentage()
     </field:display>
   	<field:display name="National Planning Objectives" feature="NPD Programs"> 
    || !validateProgramsPercentage(npoSize,"nationalPlanObjectivePrograms")
    </field:display>
    <field:display name="Primary Program" feature="NPD Programs">
    || !validateProgramsPercentage(ppSize,"primaryPrograms")
    </field:display>
    <field:display name="Secondary Program" feature="NPD Programs">
    || !validateProgramsPercentage(spSize,"secondaryPrograms")
    </field:display>  
    ){
        //alert("false");
      return false;
    }
  }
  document.aimEditActivityForm.step.value="2";
  return true;
}

function popupwin(){
  var wndWidth = window.screen.availWidth/2.5;
  var wndHeight = window.screen.availHeight/2.5;
  var t = ((screen.width)-wndWidth)/2;
  var l = ((screen.height)-wndHeight)/2;
  winpopup=window.open('',"popup","height=" + wndHeight + ",width=" + wndWidth + ",top=" + l + ",left=" + t +",menubar=no,scrollbars=yes,status=no,toolbar=no");
  winpopup.document.write('<html>\n<head>\n');
  winpopup.document.write('<title>About : Sector</title>\n');
  winpopup.document.write('</head>\n');
  winpopup.document.write('<body bgcolor="#f4f4f2">\n');
  winpopup.document.write('<font face="verdana" size=1>\n');
  winpopup.document.write('The OECD/DAC Creditor Reporting System(CRS) codes are used by all 23 OECD/DAC members when they report on their aid activities to the DAC Secretariat. The complete list of CRS codes and definitions and principles can be found in Annex 3.<ul><li>In the CRS,data on the sector of destination are recorded using 5-digit purpose codes. The first three digits of the code refer to the main sector or category (i.e.112 for Basic education, or 210 for Transpost and storage). The last two digits of the CRS purpose code allow providing more detailed classification(i.e. 11240 for Early childhood education, or 21020 for Rail transport).</li><li>For the purpose of AMP, if the 5-digits codificaton is too detailed and not relevant, only 3-digits codes may be used.</li><li>One and only one purpose code should be applied to each project. In case of multi-sector projects, use the CRS codes 400xx.</li><li>Non-sector activities (i.e. general budget support, debt, emergency aid, NGOs) are covered by the CRS, under codes 500xx to 900xx.</li></ul>\n');
  winpopup.document.write('</font>\n');
  winpopup.document.write('</body>\n</html>\n');
  winpopup.document.close();
}

function validateProgramsPercentage(cnt,prefix){
  <c:set var="errMsgAddPercentage">
  <digi:trn key="aim:addProgramPercentageErrorMessage">
  Please add Program percentage
  </digi:trn>
  </c:set>
  <c:set var="errMsgSumPercentage">
  <digi:trn key="aim:addProgramSumPercentageErrorMessage">
  Sum of programs percentages should be 100
  </digi:trn>
  </c:set>
  <c:set var="errMsgZeroPercentage">
  <digi:trn key="aim:addzeroProgPercentageErrorMessage">
  A programs percentage cannot be equal to 0
  </digi:trn>
  </c:set>
  <c:set var="errMsgFM">
  <digi:trn key="aim:FMProgPercentageErrorMessage">
  Couldn't validate program percentages: Either  National Planning Objectives or Primary Program or Seconday Program is disabled in Feature Manager But has values for current activity
  </digi:trn>
  </c:set>
  var str = null;
  var val = null;
  var i = 0;
  var flag = false;
  var sum = 0;
  while (i < cnt) {
    str   = prefix+"[" + i + "].programPercentage";
    if((document.aimEditActivityForm.elements)[str]==null){
        alert("${errMsgFM}");
        return false;
    }
    val   = (document.aimEditActivityForm.elements)[str].value;
    if (val == "" || val == null) {
      alert("${errMsgAddPercentage}");
      flag = true;
      break;
    }
    if (val == "0"){
    alert("${errMsgZeroPercentage}");
    flag = true;
      break;
    }

    sum = sum + parseFloat(val);
    i = i + 1;
  }
  if (flag == true) {
    (document.aimEditActivityForm.elements)[str].focus();
    return false;
  }
  else if (cnt>0&&sum != 100) {
    alert("${errMsgSumPercentage}");
    (document.aimEditActivityForm.elements)[str].focus();
    return false;
  }
  return true;
}

function validateLocationPercentage(){
  <c:set var="errMsgAddPercentage">
  <digi:trn key="aim:addLocationPercentageErrorMessage">
  Please add location percentage
  </digi:trn>
  </c:set>
  <c:set var="errMsgSumPercentage">
  <digi:trn key="aim:addLocationSumPercentageErrorMessage">
  Sum of locations percentages should be 100
  </digi:trn>
  </c:set>
  <c:set var="errMsgZeroPercentage">
  <digi:trn key="aim:addzeroLocationPercentageErrorMessage">
  A locations percentage cannot be equal to 0
  </digi:trn>
  </c:set>
  var str = null;
  var val = null;
  var i = 0;
  var flag = false;
  var sum = 0;
  var cnt = document.aimEditActivityForm.sizeLocs.value;
  var checkoutSum=false;
  while (i < cnt) {
    str   = "selectedLocs[" + i + "].percent";    
    val   = (document.aimEditActivityForm.elements)[str].value;    
   <field:display name="Validate Mandatory Regional Percentage" feature="Location">
	  checkoutSum=true;
	    if (val == ""){
	   			alert("${errMsgAddPercentage}");
	   			return false;
	    }
	 </field:display>
    
    if(val=="") {
    	val=0;
    }
    if ( val.indexOf(",") > 0 ) {
    	val = val.replace(",",".");
    }
    sum = sum + parseFloat(val.replace(/,/gi,"."));
    //alert(sum);
    i = i + 1;
  }


	if (checkoutSum){
	 	if (sum!=100){
	 		 alert("${errMsgSumPercentage}");
	   		 (document.aimEditActivityForm.elements)[str].focus();
	 		 return false;
	 	}
	}else {
		if (sum!=100 && sum!=0) {
		    alert("${errMsgSumPercentage}");
		    (document.aimEditActivityForm.elements)[str].focus();
		    return false;
	    }
    }
    return true;
}




function fnChk(frmContrl, f){
  <c:set var="errMsgAddSectorNumericValue">
  <digi:trn key="aim:addSecorNumericValueErrorMessage">
  Please enter numeric value only
  </digi:trn>
  </c:set>
  
  frmContrl.value = frmContrl.value.replace(/,/gi,".");
  var myValue	= frmContrl.value + "";
  if ( frmContrl && frmContrl.value && frmContrl.value.indexOf(",")>0 ) {
	  myValue		= myValue.value.replace(",", ".");
  }
  
  if (isNaN(myValue)) {
    alert("${errMsgAddSectorNumericValue}");
    frmContrl.value = "";
    //frmContrl.focus();
    return false;
  }  
  if (myValue > 100) {    
      if (f == "sector") {
	     <c:set var="errMsgAddSumExceed">
		  <digi:trn key="aim:addSecorSumExceedErrorMessage">
		  Sector percentage can not exceed 100
		  </digi:trn>
		  </c:set>
		  alert("${errMsgAddSumExceed}");
	  } else if (f == "program") {
	     <c:set var="errMsgAddSumExceed">
		  <digi:trn key="aim:addProgramSumExceedErrorMessage">
		  Program percentage can not exceed 100
		  </digi:trn>
		  </c:set>  
		  alert("${errMsgAddSumExceed}");
	  } else if (f == "region") {
	      <c:set var="errMsgAddSumExceed">
		  <digi:trn key="aim:addRegionSumExceedErrorMessage">
		  Region percentage can not exceed 100
		  </digi:trn>
		  </c:set> 
		  alert("${errMsgAddSumExceed}");
	  }
    frmContrl.value = "";
    return false;
  }
  return true;
}

function addProgram(programType) {
		
		openNewRsWindow(750, 550);
		<digi:context name="taddProgram" property="context/module/moduleinstance/addProgram.do?edit=true"/>

                var url="<%= taddProgram %>&programType="+programType;
         //       alert(programType + " "+url);
	  	document.aimEditActivityForm.action =url ;

		document.aimEditActivityForm.target = popupPointer.name;

		document.aimEditActivityForm.submit();

}



function remProgram(programType) {
	if(programType==1){
		var val=document.getElementsByName('programs.selectedNPOPrograms');
	} else if (programType==2) {
		var val=document.getElementsByName('programs.selectedPPrograms');
	}else if (programType==3){
		var val=document.getElementsByName('programs.selectedSPrograms');
	}		
		if(val!=null ){
			var isProgramSelected = false;
			for(var i=0;i<val.length;i++){
				if(val[i]!=null && val[i].checked){
					isProgramSelected = true;
				}
			}			
			if(!isProgramSelected)
			{
				alert('Please select a program to remove');
				return false;
			}
			else
			{
				<digi:context name="tremProgram" property="context/module/moduleinstance/remProgram.do?edit=true" />
	            var url="<%=tremProgram %>&programType="+programType;
		  		document.aimEditActivityForm.action = url;
	            document.aimEditActivityForm.target = "_self"
				document.aimEditActivityForm.submit();
	            return true;
			}
		}		
}
-->
</script>

<digi:instance property="aimEditActivityForm" />

<digi:form action="/addActivity.do" method="post">


  <html:hidden property="step"/>
  <html:hidden property="reset" />
  <html:hidden property="location.country" />
  <html:hidden property="editAct" />

  <input type="hidden" name="edit" value="true">
  <c:set var="stepNm">
  ${aimEditActivityForm.stepNumberOnPage}
  </c:set>
      
  <c:if test="${empty aimEditActivityForm.location.selectedLocs}">
    <input type="hidden" name="sizeLocs" value="0">
  </c:if>
  <c:if test="${!empty aimEditActivityForm.location.selectedLocs}">
    <input type="hidden" name="sizeLocs" value="${fn:length(aimEditActivityForm.location.selectedLocs)}">
  </c:if>


  <c:if test="${empty aimEditActivityForm.programs.nationalPlanObjectivePrograms}">
    <input type="hidden" name="sizeNPOPrograms" value="0">
  </c:if>
  <c:if test="${!empty aimEditActivityForm.programs.nationalPlanObjectivePrograms}">
    <input type="hidden" name="sizeNPOPrograms" value="${fn:length(aimEditActivityForm.programs.nationalPlanObjectivePrograms)}">
  </c:if>

   <c:if test="${empty aimEditActivityForm.programs.primaryPrograms}">
    <input type="hidden" name="sizePPrograms" value="0">
  </c:if>
  <c:if test="${!empty aimEditActivityForm.programs.primaryPrograms}">
    <input type="hidden" name="sizePPrograms" value="${fn:length(aimEditActivityForm.programs.primaryPrograms)}">
  </c:if>

   <c:if test="${empty aimEditActivityForm.programs.secondaryPrograms}">
    <input type="hidden" name="sizeSPrograms" value="0">
  </c:if>
  <c:if test="${!empty aimEditActivityForm.programs.secondaryPrograms}">
    <input type="hidden" name="sizeSPrograms" value="${fn:length(aimEditActivityForm.programs.secondaryPrograms)}">
  </c:if>


  <c:if test="${empty aimEditActivityForm.sectors.classificationConfigs}">
    <input type="hidden" name="sizeSectorConfigs" value="0">
  </c:if>
  <c:if test="${!empty aimEditActivityForm.sectors.activitySectors}">
     <input type="hidden" name="sizeSectorConfigs" value="${fn:length(aimEditActivityForm.sectors.classificationConfigs)}">
  </c:if>



  <table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left">
    <tr>
      <td width="100%" vAlign="top" align="left">
        <!--  AMP Admin Logo -->
        <jsp:include page="teamPagesHeader.jsp"  />
        <!-- End of Logo -->
      </td>
    </tr>
    <tr>
      <td width="100%" vAlign="top" align="left" class=r-dotted-lg>
        <table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" vAlign="top" align="center" border=0>
          <tr>
            <td class=r-dotted-lg width="10">
            &nbsp;
            </td>
            <td align=left vAlign=top>
              <table width="98%" cellSpacing="3" cellPadding="1" vAlign="top" align="left">
                <tr>
                  <td>
                    <table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
                      <tr>
                        <td>
                          <span class=crumb>
                              <c:set property="translation" var="ttt">
                                <digi:trn key="aim:clickToViewMyDesktop">
                                Click here to view MyDesktop
                                </digi:trn>
                              </c:set>
                              <c:set var="message">
								<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
								</c:set>
								<c:set var="quote">'</c:set>
								<c:set var="escapedQuote">\'</c:set>
								<c:set var="msg">
									${fn:replace(message,quote,escapedQuote)}
								</c:set>
                              <digi:link href="/viewMyDesktop.do" styleClass="comment" onclick="return quitRnot1('${msg}')" title="${trans}">
                                <digi:trn key="aim:portfolio">
                                Portfolio
                                </digi:trn>
                              </digi:link>&nbsp;&gt;&nbsp;
                           <c:forEach var="step" items="${aimEditActivityForm.steps}" end="${stepNm-1}" varStatus="index">
                               
                               <c:set property="translation" var="trans">
                                   <digi:trn key="aim:clickToViewAddActivityStep${step.stepActualNumber}">
                                       Click here to goto Add Activity Step ${step.stepActualNumber}
                                   </digi:trn>
                               </c:set>
                               
                               
                               
                               <c:if test="${!index.last}">
                                   
                                   <c:if test="${index.first}">
                                       <digi:link href="/addActivity.do?step=${step.stepNumber}&edit=true" styleClass="comment" title="${trans}">
                                           
                                           
                                           <c:if test="${aimEditActivityForm.editAct == true}">
                                               <digi:trn key="aim:editActivityStep1">
                                                   Edit Activity - Step 1
                                               </digi:trn>
                                           </c:if>
                                           <c:if test="${aimEditActivityForm.editAct == false}">
                                               <digi:trn key="aim:addActivityStep1">
                                                   Add Activity - Step 1
                                               </digi:trn>
                                           </c:if>
                                           
                                       </digi:link>
                                        &nbsp;&gt;&nbsp;
                                   </c:if>
                                   <c:if test="${!index.first}">
                                       <digi:link href="/addActivity.do?step=${step.stepNumber}&edit=true" styleClass="comment" title="${trans}">
                                           <digi:trn key="aim:addActivityStep${step.stepActualNumber}">Step ${step.stepActualNumber}</digi:trn>
                                       </digi:link>
                                        &nbsp;&gt;&nbsp;
                                   </c:if>
                               </c:if>
                               
                               
                               
                               <c:if test="${index.last}">
                                   
                                   <c:if test="${index.first}">
                                       
                                       
                                       
                                       <c:if test="${aimEditActivityForm.editAct == true}">
                                           <digi:trn key="aim:editActivityStep1">
                                               Edit Activity - Step 1
                                           </digi:trn>
                                       </c:if>
                                       <c:if test="${aimEditActivityForm.editAct == false}">
                                           <digi:trn key="aim:addActivityStep1">
                                               Add Activity - Step 1
                                           </digi:trn>
                                       </c:if>
                                   </c:if>
                                   
                                   
                                   <c:if test="${!index.first}">
                                       <digi:trn key="aim:addActivityStep${step.stepActualNumber}"> Step ${step.stepActualNumber}</digi:trn>
                                   </c:if>
                                   
                                   
                                   
                               </c:if>
                               
                               
                               
                               
                               
                               
                               
                           </c:forEach>
                            
                           
								
                          </span>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td>
                    <table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
                      <tr>
                        <td height=16 vAlign=center width="100%">
                          <span class=subtitle-blue>
                            <c:if test="${aimEditActivityForm.editAct == false}">
                              <digi:trn key="aim:addNewActivity">
                              Add New Activity
                              </digi:trn>
                            </c:if>
                            <c:if test="${aimEditActivityForm.editAct == true}">
                              <digi:trn key="aim:editActivity">
                              Edit Activity
                              </digi:trn>:
                              ${aimEditActivityForm.identification.title}
                            </c:if>
                          </span>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td>
                  &nbsp;
                  <digi:trn key="um:allMarkedRequiredField">
                  All fields marked with an
                  <FONT color="red">
                    <B>
                      <BIG>
                      *
                      </BIG>
                    </B>
                  </FONT>
                  are required.
                  </digi:trn>
                  </td>
                </tr>
                <tr>
                  <td>
                    <digi:errors />
                  </td>
                </tr>
                <tr>
                  <td>
                    <table width="100%" cellSpacing="5" cellPadding="3" vAlign="top">
                      <tr>
                        <td width="75%" vAlign="top">
                          <table cellPadding=0 cellSpacing=0 width="100%">
                          	<tr>
                          		<td>
                          			<table width="100%">
                          				<tr>
                          				    <td width="100%">
				                                <table cellPadding=0 cellSpacing=0 width="100%" border=0>
				                                  <tr>
				                                    <td width="13" height="20" background="module/aim/images/left-side.gif">
				                                    &nbsp
				                                    </td>
				                                    <td vAlign="center" align="center" class="textalb" height="20" bgcolor="#006699">
                                                                   <digi:trn>Step</digi:trn> ${stepNm} <digi:trn>of  </digi:trn>
                                                                         ${fn:length(aimEditActivityForm.steps)}:
                                                                                                 <digi:trn key="aim:activity:LocationSectors">
                                                                                                     Location | Sectors
                                                                                                 </digi:trn>
				                                   		
				                                    </td>
				                                    <td width="13" height="20" background="module/aim/images/right-side.gif">
				                                    &nbsp
				                                    </td>
				                                  </tr>
			                                	</table>
			                              </td>
			                            </tr>
			                            <feature:display name="Location" module="Project ID and Planning">
			                              <tr>
			                                <td width="100%" bgcolor="#f4f4f2">
			                                  <jsp:include page="addActivityStep2Location.jsp"/>
			                                  <!-- Add Location -->
			                                </td>
			                              </tr>
			                            </feature:display>
			                            <tr>
			                              <td>
			                              &nbsp;
			                              </td>
			                            </tr>
			                            <feature:display name="Sectors" module="Project ID and Planning">
			                            	<jsp:include page="addActivityStep2Sector.jsp"/>
										</feature:display>
			                            <tr>
			                              <td>
			                              &nbsp;
			                              </td>
			                            </tr>                            
			                            <tr>
			                              <td>
			                              &nbsp;
			                              </td>
			                            </tr>
			                            <feature:display name="Program" module="Program">
			                              <jsp:include page="addActivityStep2Program.jsp"/>
			                            </feature:display>
			                            <tr>
			                              <td>
			                              &nbsp;
			                              </td>
			                            </tr>
			                            <feature:display name="Cross Cutting Issues" module="Cross Cutting Issues">
			                            	<tr>
			                            		<td>
 													<jsp:include page="addActivityStep2CrossCuttingIssues.jsp"/>
			                            		</td>
			                            	</tr>
										</feature:display>

										<feature:display name="Step2" module="Custom Fields">
				                            <tr>
				                              <td>
				                              &nbsp;
				                              </td>
				                            </tr>			                            
			                            	<tr>
			                            		<td>
													<table border="0">
														<bean:define id="stepNumber" toScope="request" value="2"/>
	 													<jsp:include page="addActivityCustomFields.jsp"/>
													</table>
			                            		</td>
			                            	</tr>
										</feature:display>

			                            <!--
			                            <tr>
			                              <td bgColor=#f4f4f2 align="center">
			                                <table cellPadding=3>
			                                  <tr>
			                                    <td>
			                                      <html:submit styleClass="dr-menu" property="submitButton" onclick="return gotoStep(1)">
			                                        &lt;&lt; <digi:trn key="btn:back">Back</digi:trn>
			                                      </html:submit>
			                                    </td>
			                                    <td>
			                                      <html:button styleClass="dr-menu" property="submitButton"
			                                      onclick="goNextStep()">
			                                      <digi:trn key="btn:next">Next</digi:trn> &gt;&gt;
			                                      </html:button>
			                                    </td>
			                                    <td>
			                                      <html:reset styleClass="dr-menu"
			                                      property="submitButton" onclick="return resetAll()">
			                                      <digi:trn key="btn:reset">Reset</digi:trn>
			                                      </html:reset>
			                                    </td>
			                                  </tr>
			                                </table>
			                              </td>
			                            </tr>
			                            -->
                          			</table>
                          		</td>

                          	</tr>


                          </table>
                          <!-- end contents -->
                        </td>
                    	
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </td>
             <td width="25%" vAlign="top" align="right">
	           <!-- edit activity form menu -->
	           <jsp:include page="editActivityMenu.jsp"  />
	           <!-- end of activity form menu -->
	       	 </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td>
      &nbsp;
      </td>
    </tr>
  </table>

</digi:form>
