<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/calendar.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addFunding.js"/>"></script>

<script language="JavaScript">
    var openImg;
    var closeImg;
    if(document.images){

      openImg = new Image(9,9)

      closeImg = new Image(6,6)

      openImg.src = "../ampTemplate/images/arrow_down.gif"

      closeImg.src = "../ampTemplate/images/arrow_right.gif"

    }
	<!--
	function validate()
	{
		var err1="<digi:trn key='admin:enterindicatorname'>Please enter Indicator name</digi:trn>";
		var err2="<digi:trn key='admin:enterindicatorcode'>Please enter Indicator code</digi:trn>";
		var err3="<digi:trn key='admin:enterindicatortype'>Please enter Indicator type</digi:trn>";
		
		if (trim(document.aimThemeForm.name.value).length == 0)
		{
			alert(err1);
			document.aimThemeForm.name.focus();
			return false;
		}
		if (trim(document.aimThemeForm.code.value).length == 0)
		{
			alert(err2);
			document.aimThemeForm.code.focus();
			return false;
		}

		if (trim(document.aimThemeForm.type.value).length == 0)
		{
			alert(err3);
			document.aimThemeForm.type.focus();
			return false;
		}

		return true;

	}

	function getDateValues(){
		var dates = [];
		var c=0;
		var elem=document.getElementsByName('prgIndValues['+c+'].creationDate');
		while(elem != null && elem.length > 0){
			dates[dates.length]=elem[0].value;
			c++;
			elem=document.getElementsByName('prgIndValues['+c+'].creationDate');
		}
		return dates;

	}

	function getTypeValues(){
		var types = [];
		var c=0;
		var elem=document.getElementsByName('prgIndValues['+c+'].valueType');
		while(elem != null && elem.length > 0){
			types[types.length]=elem[0].value;
			c++;
			elem=document.getElementsByName('prgIndValues['+c+'].valueType');
		}
		return types;
	}


function addIndicator(id){
  <digi:context name="addIndicator" property="context/module/moduleinstance/assignNewIndicator.do" />
  openURLinWindow("<%= addIndicator %>?parentid=" + id + "&type=program",800, 500);
}


function editIndicator(id,parentid,type){
  <digi:context name="viewEditIndicator" property="context/module/moduleinstance/viewEditIndicator.do" />
  openURLinWindow("<%=viewEditIndicator%>?id=" + id + "&parentid="+parentid+"&type=program&event=edit",500, 300);
}

function addData(id){
  <digi:context name="addEditIndicator" property="context/module/moduleinstance/addEditData.do" />
  openURLinWindow("<%= addEditIndicator %>?parent=" + id,575, 300);
}

function checkValues(){
  var values=getTypeValues();
  var err1="<digi:trn key='admin:onlyonetargetvalue'>Please specify only one target value</digi:trn>";
  var err2="<digi:trn key='admin:datesallindicators'>Please specify dates for all indicators</digi:trn>";
	
  if (values.length !=null){
    var targets=0;
    for (var i=0; i< values.length; i++){
      if (values[i] == '0'){
        targets++;
      }
    }
    if (targets > 1 || targets < 1 ) {
   	  alert(err1);
      return false;
    }
  }

  var dates=getDateValues();
  if (dates.length !=null){
    for (var i=0; i< dates.length; i++){
      if (dates[i] == '' || dates[i] ==  null ){
    	alert(err2);
        return false;
      }
    }
  }
  return true;
}

function checkBaseValues(){
  var values=getTypeValues();
  var err1 = "<digi:trn key='admin:onlyonebasevalues'>Please specify only one Base value</digi:trn>";
  if (values.length !=null){
    var targets=0;
    for (var i=0; i< values.length; i++){
      if (values[i] == '2'){
        targets++;
      }
    }
    if (targets > 1 || targets < 1 ) {
      alert(err1);
      return false;
    }
  }


  return true;
}


function saveProgram(id,indId)
{
  if(checkBaseValues() == false) return false;
  if (checkValues() == false) return false;

  var temp = validate();
  if (temp == true)
  {

    <digi:context name="addThmInd" property="context/module/moduleinstance/addThemeIndicator.do?event=save"/>
    document.aimThemeForm.action = "<%=addThmInd%>&themeId=" + id + "&indicatorId=" + indId;
    document.aimThemeForm.target = "_self";
    document.aimThemeForm.submit();
  }
  return true;
}



function addIndVal(id)
{
  <digi:context name="addIndVal" property="context/module/moduleinstance/addThemeIndicator.do?event=indValue"/>
  document.aimThemeForm.action = "<%=addIndVal%>&themeId=" +id;
  document.aimThemeForm.target = "_self";
  document.aimThemeForm.submit();
  return true;
}


function load(){}

function unload(){}

function closeWindow(){
	<digi:context name="closeInd" property="context/module/moduleinstance/closeThemeIndicator.do"/>
	document.aimThemeForm.action = "<%=closeInd%>";
	document.aimThemeForm.submit();
	window.close();
	return true;
}

  function closeWindow(indiType)
  {
    <digi:context name="closeInd" property="context/module/moduleinstance/closeThemeIndicator.do"/>
    document.aimThemeForm.action = "<%=closeInd%>?type="+indiType;
    document.aimThemeForm.target = window.opener.name;
    document.aimThemeForm.submit();
    window.close();
  }

  function trim(s) {
    return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
  }




  
  function showhide(what,what2){

    if (what.style.display=='none'){

      what.style.display='';

      what2.src=openImg.src

    }
    else{

      what.style.display='none'

      what2.src=closeImg.src

    }

  }
  
   // validate indicator
   
  function validate(field) {
	  var err1 = "<digi:trn key='admin:chooseindicatorremove'>Please choose a indicator to remove</digi:trn>";
	  var msg = "<digi:trn key='admin:confirmremoveindicator'>Are you sure you want to remove the selected indicator(s)?</digi:trn>";
	  if (field == 2) {
	  
	  	if (document.aimThemeForm.indicatorsId.checked != null) {
	  		if (document.aimThemeForm.indicatorsId.checked == false) {
	  			alert(err1);
	    return false;
	  	}
	  	
		} else {
	  		var length = document.aimThemeForm.indicatorsId.length;
	  		var flag = 0;
	  
	  	for (i = 0;i < length;i ++) {
	    	if (document.aimThemeForm.indicatorsId[i].checked == true) {
	      flag = 1;
	     break;
	    }
	  }
	  	if (flag == 0) {
	  		alert(err2);
		    return false;
		  }
		}
		
		var validate = window.confirm(msg); 
		if(validate){
			 return true;
			 }else{
			 return false;
		 }
	  }
}

  	
  	function removeIndicators() {
		var flag = validate(2);
		if (flag == false) return false;
	    <digi:context name="remInd" property="context/module/moduleinstance/addThemeIndicator.do?event=Delete" />
		    document.aimThemeForm.action = "<%= remInd %>";
		    document.aimThemeForm.target = "_self";
		    document.aimThemeForm.submit();
	    return true;
	}
  
  
  	-->
 	
</script>

<digi:instance property="aimThemeForm" />
<digi:form action="/addThemeIndicator.do" method="post">
<digi:context name="digiContext" property="context"/>
	<input type="hidden" name="event">


<jsp:include page="teamPagesHeader.jsp"  />
<body >
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772 border="0">
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750 border="0">
			<table cellPadding=5 cellSpacing=0 width="100%" border="0">
				<tr><%-- Start Navigation --%>

					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:AmpAdminHome">
							Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:link href="/themeManager.do" styleClass="comment" title="${translation}">
						<digi:trn key="aim:multilevelprogramManager">
							Multi-Level Program Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:manageindicators">
							Manage Indicators
							</digi:trn>
					</td>
				</tr><%-- End navigation --%>
				<tr>
					<td height=16 vAlign=center width=571>
						<span class="subtitle-blue">
							<digi:trn key="aim:manageindicators">
										Manage Indicators
							</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td height="16" vAlign="center" width="571">
						<digi:errors />
					</td>
				</tr>
	
		<table  width=772 cellPadding=1 cellSpacing=1 valign=top align=left bgcolor="#ffffff" border="0">
				<tr>
					 <td bgColor="#d7eafd" class="box-title" height="10" align="center">
						<digi:trn key="aim:manageindicators">Manage Indicators</digi:trn>: ${aimThemeForm.themeName}
					 </td>
				 </tr>
					 <tr>
						 <td align="center">
						 <c:if test="${aimThemeForm.flag == 'error'}">
									<font color="red">
												<b><digi:trn key="aim:cannotasigne">
													indicator with this name already assign 
												</digi:trn></b>
								  </font>
							</c:if>
						 </td>
					 </tr>
					 
				<logic:notEmpty name="aimThemeForm" property="programIndicators">
				<tr>
					<td>
						<table  bgColor="#d7eafd" cellPadding=5 cellSpacing=1 border="0" align="center" width=772>
						 	<c:forEach var="prgIndicatorItr" varStatus="rIndex" items="${aimThemeForm.programIndicators}">
								<tr>
									<td width="1" bgcolor="white" colspan="7">
									
									</td>
								</tr>
						    	<tr>
									<td width="9" height="15" bgcolor="#ffffff" id="menu1" onClick="showhide(menu1outline${prgIndicatorItr.indicatorThemeId},menu1sign${prgIndicatorItr.indicatorThemeId})" >
												<img id="menu1sign${prgIndicatorItr.indicatorThemeId}" src= "../ampTemplate/images/arrow_right.gif" valign="bottom">
									</td>
									<td align="left" width="60%" bgcolor="#f4f4f2">
										<b>${prgIndicatorItr.indicator.name}</b>
									</td>
					                <td  height="15" width="17%"bgcolor="#f4f4f2" nowrap="nowrap">
										  <jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
												<c:set target="${urlParams}" property="prgIndicatorId">
														${prgIndicatorItr.indicatorThemeId}
												</c:set>
												<c:set target="${urlParams}" property="themeId">
														${aimThemeForm.themeId}
												</c:set>

												<bean:define id="translation">
														<digi:trn key="aim:clickToEditPrgIndicator">Click here to Edit Program Indicator</digi:trn>
												</bean:define>												
									</td>
                  					<td height="10%" width="9%" bgcolor="#f4f4f2" nowrap="nowrap">
                  						<c:set var="trn"><digi:trn key="aim:addIndicator:add">Add/Edit data</digi:trn></c:set>
												<a href="javascript:addData('${prgIndicatorItr.indicatorThemeId}')">${trn}</a>
									</td>									
									<td bgcolor="#f4f4f2" height="2%">
									 <html:multibox property="indicatorsId">
									 ${prgIndicatorItr.indicatorThemeId}
									 </html:multibox>
									</td>
								</tr>
                                <tr>
									<td width="25" height="15" align="Center" colspan="7" id="menu1outline${prgIndicatorItr.indicatorThemeId}" style="display:none; background-color:#f4f4f2;">
										 <table border="0" width="70%" class="box-border">
											<tr bgcolor="white">
													  <td  width="20%"></td>
													  <td bgcolor="white" width="5%"><b></b></td>
											</tr>
											<tr>
    										      <td bgColor=#d7eafd class=box-title height="10" align="center" colspan="7">
    														<b>Data</b>
    												</td>
  											</tr>
											<tr bgcolor="#003366" class="textalb">
                                                 <td align="center" valign="middle" width="75">
                                                  <b><font color="white"><digi:trn key="aim:addtheme:actualbasetarget">Actual/Base/<br>Target</digi:trn></font></b>
                                                 </td>
                                                 <td align="center" valign="middle" width="120">
                                                   <b><font color="white"><digi:trn key="aim:addtheme:totalamount">Total Amount</digi:trn></font></b>
                                                 </td>
                                                 <td align="center" valign="middle" width="120">
                                                   <b><font color="white"><digi:trn key="aim:addtheme:creationdate">Creation Date</digi:trn></font></b>
                                                 </td>
                                                 <td align="center" valign="middle" width="120" colspan="3">
                                                   <b><font color="white"><digi:trn key="aim:addtheme:location">Location</digi:trn></font></b>
                                                 </td>
                                            </tr>
                                        <logic:notEmpty name="prgIndicatorItr" property="programIndicatorValues">
                                           <logic:iterate name="prgIndicatorItr" property="programIndicatorValues" id="prgIndicatorValues" type="org.digijava.module.aim.helper.AmpPrgIndicatorValue">
                                            <tr bgcolor="#ffffff">
                                                <td width="40" bgcolor="#f4f4f2" align="center">																		
                                                    <c:if test="${prgIndicatorValues.valueType=='0'}"><digi:trn key="aim:addeditdata:target">Target</digi:trn></c:if>
                                                    <c:if test="${prgIndicatorValues.valueType=='1'}"><digi:trn key="aim:addeditdata:actual">Actual</digi:trn></c:if>
                                                    <c:if test="${prgIndicatorValues.valueType=='2'}"><digi:trn key="aim:addeditdata:base">Base</digi:trn></c:if>
                                                </td>
                                                <td align="center" width="10%" bgcolor="#f4f4f2"><b>
                                                    <bean:write name="prgIndicatorValues" property="valAmount"/></b>
                                                </td>
                                                <td bgcolor="#f4f4f2" align="center">
                                                    <bean:write name="prgIndicatorValues" property="creationDate"/></b>
                                                </td>
                                                <td bgcolor="#f4f4f2" align="center">
                                                    <c:if test="${not empty prgIndicatorValues.location}">
                                                        <bean:define id="loc" name="prgIndicatorValues" property="location"></bean:define>
                                                        <c:if test="${!empty loc.location.name}">
                                                            [${loc.location.name}]
                                                        </c:if>
                                                    </c:if>
                                                    <c:if test="${empty prgIndicatorValues.location}">
                                                        <span>[<span style="color:Red"><digi:trn key="aim:addeditdata:national">National</digi:trn></span>]</span>
                                                    </c:if>
                                                </td>
                                            </tr>
                                            </logic:iterate>
                                        </logic:notEmpty>
										</table>
	                                </td>
                                </tr>
								</c:forEach>
						 </table>
					</td>
				</tr>
				</logic:notEmpty>
				<logic:empty name="aimThemeForm" property="programIndicators">
						<tr align="center" bgcolor="#ffffff"><td><b>
								<digi:trn key="aim:noIndicatorsPresent">No Indicators present</digi:trn></b></td>
						</tr>
				</logic:empty>
				<tr>
				<td align="center" colspan="6" bgcolor="white">
				<input type="button" class="dr-menu" onclick="return removeIndicators()" value='<digi:trn key="aim:removeselec">Remove Selected</digi:trn>' />
				<input class="dr-menu" type="button" name="addValBtn" value="<digi:trn key="aim:assignnewindicator">Assign New Indicator</digi:trn>" onclick="addIndicator('${aimThemeForm.themeId}');">&nbsp;&nbsp;
				</td>
				</tr>
		</table>
	</td>
</tr>
</table>
</body>
</digi:form> 