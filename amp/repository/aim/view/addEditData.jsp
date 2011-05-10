<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/calendar.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addFunding.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>

<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
<jsp:include page="scripts/newCalendar.jsp" flush="true" />


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
          document.forms[0].action = "<%=addEditIndicator%>";
          document.forms[0].submit();
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
<table  width=572 cellPadding=4 cellspacing="1" valign="top" align=left bgcolor="#ffffff" border="0">
  <tr>
    <td bgColor=#d7eafd class=box-title height="10" align="center" colspan="7">
    <digi:trn key="aim:addIndicator:add">Add/Edit data</digi:trn>: ${aimThemeForm.indicatorName}
    </td>
  </tr>
  <tr bgcolor="#003366" class="textalb">
    <td align="center" valign="middle" width="75">
      <b><font color="white"><digi:trn key="aim:addeditdata:actualbasetarget">Actual/Base/<br>Target</digi:trn></font></b>
    </td>
    <td align="center" valign="middle" width="120">
      <b><font color="white"><digi:trn key="aim:addeditdata:value">Value</digi:trn></font></b>
    </td>
    <td align="center" valign="middle" width="120">
      <b><font color="white"><digi:trn key="aim:addData:creationdate">Date</digi:trn></font></b>
    </td>
    <td align="center" valign="middle" width="120" colspan="3">
      <b><font color="white"><digi:trn key="aim:addeditdata:addlocation">Add Location</digi:trn></font></b>
    </td>
  </tr>
  <c:if test="${!empty aimThemeForm.prgIndValues}">
    <c:forEach var="ind" varStatus="index" items="${aimThemeForm.prgIndValues}">
        <tr>
          <td bgColor=#d7eafd  height="10" align="center" width="10%">
            <html:select name="ind" property="valueType" styleClass="inp-text">
              <html:option value="1"><digi:trn key="aim:addeditdata:actual">Actual</digi:trn></html:option>
              <html:option value="2"><digi:trn key="aim:addeditdata:base">Base</digi:trn></html:option>
              <html:option value="0"><digi:trn key="aim:addeditdata:target">Target</digi:trn></html:option>
            </html:select>
          </td>

          <td bgColor=#d7eafd height="10" align="center" width="10%">
            <html:text name="ind" property="valAmount" styleId="txtName" styleClass="amt" onblur="chkNumeric(this)"/>
          </td>

          <td bgColor=#d7eafd  height="10" align="center" nowrap="nowrap">
            <html:text name="ind" property="creationDate" styleId="txtDate${index.count-1}" readonly="true" style="width:80px;"/>
			<a id="date${index.count-1}" href='javascript:pickDateById("date${index.count-1}","txtDate${index.count-1}")'>
				<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0"> 
			</a> 

          </td>

          <td bgColor=#d7eafd width="100%">
            <c:if test="${ind.location!=null&&ind.location.location!=null}">
                ${ind.location.location.name}
            </c:if>
            <c:if test="${ind.location==null}">
              <span>[<span style="color:Red"><digi:trn key="aim:addeditdata:national">National</digi:trn></span>]</span>
            </c:if>
          </td>

          <td bgColor=#d7eafd  height="10" nowrap="nowrap">
            [<a href="javascript:selectLocation('${index.count-1}')">
            	<digi:trn key="aim:addeditdata:addlocation">Add location</digi:trn>
              <!-- <img src="../ampTemplate/images/closed.gif" border="0" alt="Select location" /> -->
            </a>]
          </td>

          <td bgColor=#d7eafd>
            <a href="javascript:deleteData('${index.count-1}')">
              <img src="../ampTemplate/images/trash_16.gif" border="0" alt="Delete indicator value" />
            </a>
          </td>
        </tr>        
    </c:forEach>   
  </c:if>

  <c:if test="${empty aimThemeForm.programIndicators}">
    <tr align="center" bgcolor="#ffffff"><td><b>
      <digi:trn key="aim:noIndicatorsPresent">No data present</digi:trn></b></td>
    </tr>
    <tr bgColor="#d7eafd">
      <td>
      &nbsp;
      </td>
    </tr>
  </c:if>
  <tr>
    <td height="25" align="center" colspan="6">
      <c:set var="trnadd"><digi:trn key="aim:btn:adddata">Add data</digi:trn></c:set>
      <input id="addDataBtn" style="font-family:verdana;font-size:11px;" type="button" name="addValBtn" value="${trnadd}">&nbsp;&nbsp;
    </td>
  </tr>  
  <tr>
    <td bgColor=#dddddb height="25" align="center" colspan="6">
      <c:set var="trn"><digi:trn key="aim:btn:save">Save</digi:trn></c:set>
      <c:set var="trncancel"><digi:trn key="aim:btn:cancel">Cancel</digi:trn></c:set>
      <c:set var="trnclose"><digi:trn key="aim:btn:close">Close</digi:trn></c:set>
      
      <input class="dr-menu" type="button" name="addBtn" value="${trn}" onclick="return saveIndicator('${aimThemeForm.themeId}')">&nbsp;&nbsp;
      <input class="dr-menu" type="reset" value="${trncancel}">
      <input class="dr-menu" type="button" name="close" value="${trnclose}" onclick="window.close();">
    </td>
  </tr>
   <tr><td width="100%" colspan="6"><br>
   		<font color="red"> *<digi:trn key="aim:addEditData:enterBaseAndTargetValues">Please ensure that you enter at least 1 base and 1 target value</digi:trn></font> 
   </td></tr>
</table>

</digi:form>
