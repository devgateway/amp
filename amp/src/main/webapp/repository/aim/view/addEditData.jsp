<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<style>
body {font-family:Arial, Helvetica, sans-serif; font-size:12px;}
.buttonx {background-color:#5E8AD1; border-top: 1px solid #99BAF1; border-left:1px solid #99BAF1; border-right:1px solid #225099; border-bottom:1px solid #225099; font-size:11px; color:#FFFFFF; font-weight:bold; padding-left:5px; padding-right:5px; padding-top:3px; padding-bottom:3px;}
hr {border: 0; color: #E5E5E5; background-color: #E5E5E5; height: 1px; width: 100%; text-align: left;}
</style>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/calendar.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addFunding.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/yahoo/yahoo-min.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event/yahoo-dom-event.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/container/container-min.js"/>"></script>

<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/container/assets/container.css"> 

<jsp:include page="scripts/newCalendar.jsp"  />

<script language="JavaScript">

function chkNumeric(frmContrl){
  var regEx=/^[0-9]*\.?[0-9]*$/;
  var errorMsg="<digi:trn>Please enter numeric value only</digi:trn>";
  if(!frmContrl.value.match(regEx)){
      alert(errorMsg);
      frmContrl.value = "";
      frmContrl.focus();
      return false;
  }
}
$(document).ready(function(){
      $("#addDataBtn").click(function () {
          $(this).attr('disabled', 'disabled');
           <digi:context name="addEditIndicator" property="context/module/moduleinstance/addEditData.do?event=addIndValue" />
           aimThemeForm.action = "<%=addEditIndicator%>";
       	   aimThemeForm.submit();
      });
  });


function deleteData(ind){
  var flag = confirm("Delete this data?");
  if(flag == true){
  <digi:context name="addEditIndicator" property="context/module/moduleinstance/addEditData.do?event=delIndValue" />
  document.forms[0].action = "<%=addEditIndicator%>&index="+ind;
  document.forms[0].submit();
  }
}

function saveIndicator(id){
	if (!validation()){
        return false;
	}
  <digi:context name="addEditIndicator" property="context/module/moduleinstance/addEditData.do?event=save" /> 
  aimThemeForm.action = "<%=addEditIndicator%>";
  aimThemeForm.target=window.opener.name;
  aimThemeForm.submit();
  //window.opener.location.reload();
  window.close()
}

function selectLocation(index){
    <digi:context name="justSubmit" property="context/module/moduleinstance/addEditData.do?action=justSubmit" />
  	document.aimThemeForm.action = "<%=justSubmit%>&index="+index;
  	document.aimThemeForm.submit();
  <digi:context name="selLoc" property="context/module/moduleinstance/selectLocationForIndicatorValue.do?action=justSubmit"/>  
  openURLinWindow("<%=selLoc%>&index="+index,700,500);



}

function validation(){
	var values=document.getElementsByTagName("select");
    var msg='<digi:trn jsFriendly="true">Please ensure that you enter at least 1 base and 1 target value</digi:trn>';
    var msgDate='<digi:trn jsFriendly="true">Please specify date</digi:trn>';
    var dates=document.getElementsByName("creationDate");
    for (var j=0;j<dates.length;j++){
        var date=dates[j];
        if(date.value==''){
            date.focus();
            alert(msgDate);
            return false;
        }

    }
	var baseValue=0;
	var actualValue=0;
	var targetValue=0;
	if(values!=null){
		for (var i=0;i<values.length;i++){
			
			if (values[i].selectedIndex==1){
				baseValue++;
			}else if(values[i].selectedIndex==2){
				targetValue++;
			}
		}	
	}
	//for every actual value we should have base and target values
	if(baseValue==0||targetValue==0){
        alert(msg);
		return false;	
	}
	return true;
}
</script>

<digi:instance property="aimThemeForm" />
<digi:form action="/addEditData.do" method="post">
<digi:context name="digiContext" property="context"/>
<input type="hidden" name="event">
<table  width=572 cellPadding=4 cellspacing="1" valign="top" align=left bgcolor="#ffffff" border="0" style="font-size:12px;">
  <tr>
    <td bgColor=#c7d4db class=box-title height="25" align="center" colspan="7">
    <digi:trn key="aim:addIndicator:add"><b><digi:trn>Add/Edit data</digi:trn></b></digi:trn><b>: <c:out value="${aimThemeForm.indicatorName}"></c:out> </b>
    </td>
  </tr>
  <tr bgcolor="#F2F2F2" class="textalb">
    <td align="center" valign="middle" width="75">
      <b><digi:trn key="aim:addeditdata:actualbasetarget">Actual/Base/<br>Target</digi:trn></b>
    </td>
    <td align="center" valign="middle" width="120">
      <b><digi:trn key="aim:addeditdata:value">Value</digi:trn></b>
    </td>
    <td align="center" valign="middle" width="120">
      <b><digi:trn key="aim:addData:creationdate">Date</digi:trn></b>
    </td>
    <td align="center" valign="middle" width="120" colspan="3">
      <b><digi:trn key="aim:addeditdata:addlocation">Add Location</digi:trn></b>
    </td>
  </tr>
  <c:if test="${!empty aimThemeForm.prgIndValues}">
    <c:forEach var="ind" varStatus="index" items="${aimThemeForm.prgIndValues}">
        <tr>
          <td bgColor=#ffffff  height="10" align="center" width="10%">
            <html:select name="ind" property="valueType" styleClass="inp-text">
              <html:option value="1"><digi:trn key="aim:addeditdata:actual">Actual</digi:trn></html:option>
              <html:option value="2"><digi:trn key="aim:addeditdata:base">Base</digi:trn></html:option>
              <html:option value="0"><digi:trn key="aim:addeditdata:target">Target</digi:trn></html:option>
            </html:select>
          </td>

          <td bgColor=#ffffff height="10" align="center" width="10%">
            <html:text name="ind" property="valAmount" styleId="txtName" styleClass="amt" onblur="chkNumeric(this)"/>
          </td>

          <td bgColor=#ffffff  height="10" align="center" nowrap="nowrap">
            <html:text name="ind" property="creationDate" styleId="txtDate${index.count-1}" readonly="true" style="width:80px;"/>
			<a id="date${index.count-1}" href='javascript:pickDateById2("date${index.count-1}","txtDate${index.count-1}",false)'>
				<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0"> 
			</a> 

          </td>

          <td bgColor=#ffffff width="100%">
            <c:if test="${ind.location!=null&&ind.location.location!=null}">
                <c:out value="${ind.location.location.name}"></c:out>
            </c:if>
            <c:if test="${ind.location==null}">
              <span>[<span style="color:Red"><digi:trn key="aim:addeditdata:national">National</digi:trn></span>]</span>
            </c:if>
          </td>

          <td bgColor=#ffffff  height="10" nowrap="nowrap">
            [<a href="javascript:selectLocation('${index.count-1}')">
            	<digi:trn key="aim:addeditdata:addlocation">Add location</digi:trn>
              <!-- <img src="../ampTemplate/images/closed.gif" border="0" alt="Select location" /> -->
            </a>]
          </td>

          <td bgColor=#ffffff>
            <a href="javascript:deleteData('${index.count-1}')">
              <img src="../ampTemplate/images/trash_16.gif" border="0" alt="Delete indicator value" />
            </a>
          </td>
        </tr>        
    </c:forEach>   
  </c:if>

  <c:if test="${empty aimThemeForm.programIndicators}">
    <tr align="center" bgcolor="#ffffff"><td colspan=6><b>
      <digi:trn key="aim:noIndicatorsPresent">No data present</digi:trn></b></td>
  </c:if>
  <tr>
    <td height="25" align="center" colspan="6">
      <c:set var="trnadd"><digi:trn key="aim:btn:adddata">Add data</digi:trn></c:set>
      <input id="addDataBtn" style="font-family:verdana;font-size:11px;" type="button" class="buttonx" name="addValBtn" value="${trnadd}">&nbsp;&nbsp;
    </td>
  </tr>  
  <tr>
    <td height="25" align="center" colspan="6" style="padding-top:15px;">
      <c:set var="trn"><digi:trn key="aim:btn:save">Save</digi:trn></c:set>
      <c:set var="trnReset"><digi:trn>Reset</digi:trn></c:set>
      <c:set var="trnclose"><digi:trn key="aim:btn:close">Close</digi:trn></c:set>
      
      <input class="buttonx" type="button" name="addBtn" value="${trn}" onclick="return saveIndicator('${aimThemeForm.themeId}')">&nbsp;&nbsp;
      <input class="buttonx" type="reset" value="${trnReset}">
      <input class="buttonx" type="button" name="close" value="${trnclose}" onclick="window.close();">
    </td>
  </tr>
   <tr><td width="100%" colspan="6" align=center style="font-size:10px;"><br>
   		<font color="red"> *<digi:trn key="aim:addEditData:enterBaseAndTargetValues">Please ensure that you enter at least 1 base and 1 target value</digi:trn></font> 
   </td></tr>
</table>

</digi:form>
