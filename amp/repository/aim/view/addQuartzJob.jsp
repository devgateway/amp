<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<digi:instance property="quartzJobManagerForm" />
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>
<jsp:include page="scripts/newCalendar.jsp" flush="true" />

<script type="text/javascript">
function setAction(action){
var type;
	
  var act=document.getElementById("hdnAction");
  if(act!=null){
    act.value=action;
  }else{
    return false;
  }
  return true;
}
function cancel(){
  setAction("all");
  document.quartzJobManagerForm.submit();
}

function saveJob(){
  var txt=null;
  txt=document.getElementById("cmbJc");
  if(txt==null){
    txt=document.getElementById("txtClassFullname");
    if(txt==null || txt.value==""){

    	<c:set var="enterClassName">
			<digi:trn key="aim:job:EnterClassName">Please enter Class Fullname</digi:trn>
		</c:set>   	
	
      alert("${enterClassName}");
      txt.focus();
      return false;
    }
  }

  txt=document.getElementById("txtName");
  if(txt==null || txt.value==""){
	  	<c:set var="name">
			<digi:trn key="aim:job:name">Please enter Name</digi:trn>
		</c:set>   	

		
    alert("${name}");
    txt.focus();
    return false;
  }

  txt=document.getElementById("txtStartDateTime");
  if(txt==null || txt.value==""){
		<c:set var="datetime">
			<digi:trn key="aim:job:datetime">Please enter Start Date/Time!</digi:trn>
		</c:set> 
    alert("${datetime}");
    txt.focus();
    return false;
  }

  var rda=document.getElementsByName("triggerType");
  if(rda!=null){
    var flag=-1;
    for(var i=0;i<rda.length;i++){
      if(rda[i].checked){
        flag=i;
        break;
      }
    }
    if(flag==-1){
    	<c:set var="jobType">
			<digi:trn key="aim:job:jobType">Please select Job typee</digi:trn>
		</c:set> 
      alert("${jobType}");
      return false;
    }

     //
	if (type==1){
		if (document.getElementById("exeTimeM").value=="00"){
			<c:set var="invalidRepeatTime">
				<digi:trn key="aim:job:invalidRepeatTime">Repeat time can't be 0</digi:trn>
			</c:set> 
			alert("${invalidRepeatTime}")
			document.getElementById("exeTimeM").focus();
			 return false;
		}
	}
	if (type==2){
		if (document.getElementById("exeTimeH").value=="00"){
			<c:set var="invalidRepeatTime">
				<digi:trn key="aim:job:invalidRepeatTime">Repeat time can't be 0</digi:trn>
			</c:set> 
			alert("${invalidRepeatTime}")
			document.getElementById("exeTimeH").focus();
			 return false;
		}
	}
    
  }
  

  if(setAction("saveJob")){
    document.quartzJobManagerForm.submit();
  }
}

function typeChanged(value){
	 var mdays=document.getElementById("monthDays");
	 var cmb=document.getElementById("cmbWeekDays");
		 document.getElementById("txtRepeatH").style.display="none";
		document.getElementById("txtRepeatM").style.display="none";
		document.getElementById("selectTime").style.display="none";
		

		type=value;
	  switch(value){
  		case 1:
  			document.getElementById("exeTimeH").disabled=true;
			document.getElementById("exeTimeM").disabled=false;

			
			document.getElementById("txtRepeatM").style.display="block";
			
			break
		case 2:
  			document.getElementById("exeTimeH").disabled=false;
			document.getElementById("exeTimeM").disabled=true;
			document.getElementById("txtRepeatH").style.display="block";
			
			break
      case 3:
  			document.getElementById("exeTimeH").disabled=false;
			document.getElementById("exeTimeM").disabled=false;
			document.getElementById("selectTime").style.display="block";
			break
	  case 4:
            cmb.disabled=false;
		    mdays.disabled=true; 
			document.getElementById("exeTimeH").disabled=false;
			document.getElementById("exeTimeM").disabled=false;	
			document.getElementById("selectTime").style.display="block"; 
			
			break;
	case 5:
	 		 document.getElementById("exeTimeH").disabled=false;
			document.getElementById("exeTimeM").disabled=false;
			document.getElementById("selectTime").style.display="block";
			
             cmb.disabled=true;
             mdays.disabled=false; 
             break;
      default:
          cmb.disabled=true;
      		mdays.disabled=true; 
  		    break;
  }
}

		function shoValidationmsg(){
		<logic:equal name="quartzJobManagerForm" property="invalidTrigger" value="true">
			<c:set var="invalidTrigger">
				<digi:trn key="aim:job:invalidTrigger">The trigeer will never be fired</digi:trn>
			</c:set> 
			
			alert("${invalidTrigger}");
		</logic:equal>		

		<logic:equal name="quartzJobManagerForm" property="invalidEndDate" value="true">
		<c:set var="invalidEndDate">
			<digi:trn key="aim:job:invalidEndDate">End Date should be after Start Date</digi:trn>
		</c:set> 

			alert("${invalidEndDate}");

		</logic:equal>	
			return;
		}
	</script>
<digi:form action="/quartzJobManager.do" method="post">

  <html:hidden name="quartzJobManagerForm" property="action" styleId="hdnAction" />
  <html:hidden name="quartzJobManagerForm" property="editAction" />
  <table>
    <tr>
      <td>
      &nbsp;&nbsp;&nbsp;
      </td>
      <td>
        <table>
          <tr>
            <!-- Start Navigation -->
            <td>
              <span class="crumb">
                <c:set var="translation">
                  <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
                </c:set>
                <digi:link module="aim" href="/admin.do" styleClass="comment" title="${translation}" >
                  <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
                </digi:link>&nbsp;&gt;&nbsp;
                <digi:link href="/quartzJobManager.do?action=all" styleClass="comment" title="${translation}" >
                  <digi:trn key="aim:jobManager">Job Manager</digi:trn>
                </digi:link>
                &nbsp;&gt;&nbsp;
                <digi:trn key="aim:addJob">Add new job</digi:trn>
              </span>
            </td>
            <!-- End navigation -->
          </tr>
          <tr>
            <td style="height:53px;">
              <span class="subtitle-blue">
                <digi:trn key="aim:addJob">Add New Job</digi:trn>
              </span>
            </td>
          </tr>
          <tr>
            <td>
              <table cellpadding="2" style="width:400px;">
                <tr>
                  <td align="right">
                    <c:if test="${empty quartzJobManagerForm.jcCol}">
                      <span style="color:red;">*</span>
                      <digi:trn key="aim:job:classFullname">Class fullname:</digi:trn>
                    </c:if>
                    <c:if test="${!empty quartzJobManagerForm.jcCol}">
                      <digi:trn key="aim:job:class">Class:</digi:trn>
                    </c:if>
                  </td>
                <td>
                      <c:if test="${!empty quartzJobManagerForm.jcCol}">
                              <html:select name="quartzJobManagerForm" property="classFullname"  styleId="cmbJc" style="font-family:Verdana;font-size:10px;width:250px;">
                                  <c:forEach var="jc" items="${quartzJobManagerForm.jcCol}">
                                      <html:option value="${jc.classFullname}"><digi:trn key="aim:job:${jc.name}">${jc.name}</digi:trn></html:option>
                                  </c:forEach>
                              </html:select>
                    </c:if>
                          <c:if test="${empty quartzJobManagerForm.jcCol}">
                              <html:text name="quartzJobManagerForm" property="classFullname" styleId="txtClassFullname" style="font-family:Verdana;font-size:10px;width:250px;" />
                          </c:if>
                  </td>
                </tr>
                <tr>
                  <td align="right">
                    <span style="color:red;">*</span>
                    <digi:trn key="aim:job:name">Name</digi:trn>
                  </td>
                  <td>
                    <html:text name="quartzJobManagerForm" property="name" styleId="txtName" style="font-family:Verdana;font-size:10px;width:250px;" />
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td colspan="6">&nbsp;
            
            </td>
          </tr>
          <tr>
            <td colspan="3">
              <table cellpadding="2" cellspacing="1" style="border:dashed 1px;width:400px;">
                <tr>
                  <td width="96" align="right">
                    <span style="color:red;">*</span>
                  <digi:trn key="aim:job:startDateTime">Start date/time</digi:trn>                  </td>
                  <td width="288">
                
                
                  <html:text name="quartzJobManagerForm" property="startDateTime" styleId="txtStartDateTime" style="width:100px" readonly="readonly" styleClass="inp-text"/>
               	  
                  <a id="clear1" href='javascript:clearDate(document.getElementById("txtStartDateTime"), "clear1")'>
		<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
	</a>
	<a id="date1" href='javascript:pickDateWithClear("date1",document.getElementById("txtStartDateTime"),"clear1")'>
		<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>	</a>      
               	 
               	                               </td>
                </tr>
                <tr>
                  <td align="right">&nbsp;</td>
                  <td> <html:select  name="quartzJobManagerForm" property="startH"   styleClass="inp-text">
				   <html:option value="00">00</html:option>
               	   <html:option value="01">01</html:option>
				   <html:option value="02">02</html:option>
				   <html:option value="03">03</html:option>
				   <html:option value="04">04</html:option>
				   <html:option value="05">05</html:option>
				   <html:option value="06">06</html:option>
				   <html:option value="07">07</html:option>
				   <html:option value="08">08</html:option>
				   <html:option value="09">09</html:option>
				   <html:option value="10">10</html:option>
				   <html:option value="11">11</html:option>
				   <html:option value="12">12</html:option>
                   <html:option value="13">13</html:option>
                   <html:option value="14">14</html:option>
                   <html:option value="15">15</html:option>
                   <html:option value="16">16</html:option>
                   <html:option value="17" >17</html:option>
                   <html:option value="18">18</html:option>
                   <html:option value="19">19</html:option>
                   <html:option value="20">20</html:option>
                   <html:option value="21">21</html:option>
                   <html:option value="22">22</html:option>
                   <html:option value="23">23</html:option>
				  </html:select>       
                             
                  <html:select name="quartzJobManagerForm"  property="startM"  styleClass="inp-text">
					<html:option value="00">00</html:option>
				  	<html:option value="15">15</html:option>
				  	<html:option value="30">30</html:option>
				  	<html:option value="45">45</html:option>
				  </html:select></td>
                </tr>
                <tr>
                  <td align="right">
                  <digi:trn key="aim:job:endDateTime">End date/time</digi:trn>                  </td>
                  <td>
                 
     
<html:text name="quartzJobManagerForm" property="endDateTime" styleId="txtEndDateTime" style="width:100px"  readonly="readonly"  styleClass="inp-text"/>
                   <a id="clear1" href='javascript:clearDate(document.getElementById("txtEndDateTime"), "clear1")'>
		<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
	</a>
	<a id="date1" href='javascript:pickDateWithClear("date1",document.getElementById("txtEndDateTime"),"clear1")'>
		<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>	</a>    
                                            </td>
                </tr>
                <tr>
                  <td align="right">&nbsp;</td>
                  <td>  <html:select name="quartzJobManagerForm" property="endH"  styleClass="inp-text">
					
					
				   <html:option value="00">00</html:option>
               	   <html:option value="01">01</html:option>
				   <html:option value="02">02</html:option>
				   <html:option value="03">03</html:option>
				   <html:option value="04">04</html:option>
				   <html:option value="05">05</html:option>
				   <html:option value="06">06</html:option>
				   <html:option value="07">07</html:option>
				   <html:option value="08">08</html:option>
				   <html:option value="09">09</html:option>
				   <html:option value="10">10</html:option>
				   <html:option value="11">11</html:option>
				   <html:option value="12">12</html:option>
                   <html:option value="13">13</html:option>
                   <html:option value="14">14</html:option>
                   <html:option value="15">15</html:option>
                   <html:option value="16">16</html:option>
                   <html:option value="17">17</html:option>
                   <html:option value="18">18</html:option>
                   <html:option value="19">19</html:option>
                   <html:option value="20">20</html:option>
                   <html:option value="21">21</html:option>
                   <html:option value="22">22</html:option>
                   <html:option value="23">23</html:option>
				  </html:select>  
                                  
                  <html:select name="quartzJobManagerForm" property="endM"  styleClass="inp-text">
					<html:option value="00">00</html:option>
				  	<html:option value="15">15</html:option>
				  	<html:option value="30">30</html:option>
				  	<html:option value="45">45</html:option>
				  </html:select>   </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td colspan="6">&nbsp;
            
            </td>
          </tr>
          <tr>
            <td colspan="2">
            <span style="color:red;">*</span>
            <digi:trn key="aim:job:jobType">Job Type</digi:trn>
            <table width="439" cellpadding="2" cellspacing="1" style="border:dashed 1px;width:400px;">
              <tr>
                <td colspan="2">
               	  <html:radio name="quartzJobManagerForm" property="triggerType" value="1" onclick="typeChanged(1);" /><digi:trn key="qMinutely">Minutely</digi:trn>
                  <html:radio name="quartzJobManagerForm" property="triggerType" value="2" onclick="typeChanged(2);" /><digi:trn key="qHourly">Hourly</digi:trn>
                  <html:radio name="quartzJobManagerForm" property="triggerType" value="3" onclick="typeChanged(3);" /><digi:trn key="qDaily">Daily</digi:trn>
                  <html:radio name="quartzJobManagerForm" property="triggerType" value="4" onclick="typeChanged(4);" /><digi:trn key="qWeekly">Weekly</digi:trn>
                  <html:radio name="quartzJobManagerForm" property="triggerType" value="5" onclick="typeChanged(5);" /><digi:trn key="qMonthly">Monthly</digi:trn>                </td>
              </tr>
              <tr>
                  <td colspan="2">
                  <digi:trn key="aim:job:jobDayOfWeek">Select Day of week</digi:trn>
                  	<html:select name="quartzJobManagerForm" property="selectedDay"  styleId="cmbWeekDays" style="font-family:Verdana;font-size:10px;" disabled="true">
	                    <html:option value="1">1</html:option>
	                    <html:option value="2">2</html:option>
	                    <html:option value="3">3</html:option>
	                    <html:option value="4">4</html:option>
	                    <html:option value="5">5</html:option>
	                    <html:option value="6">6</html:option>
	                    <html:option value="7">7</html:option>
                    </html:select>                  </td>
              </tr>
              <tr>
                 <td colspan="2">
                   <digi:trn key="aim:job:jobDayOfMonth">Select Day of month</digi:trn>
                   <html:select name="quartzJobManagerForm" property="selectedMonthDay" styleId="monthDays" style="font-family:Verdana;font-size:10px;" disabled="true">
                          <c:forEach var="i" begin="1" end="31">
                              <html:option value="${i}">${i}</html:option>
                          </c:forEach>
                    </html:select>                  </td>
              </tr>
              <tr>
              <td colspan="2">
              		    <span id="selectTime" style="color: red;display: none"> <digi:trn key="aim:job:selectTime">Select trigger time</digi:trn></span>                       
        
              </td>
              </tr>
              <tr>
                  <td width="60">
                
              
                 
                   <digi:trn key="aim:job:hour">Hour</digi:trn>  
                                                          
                  <td>
                  
                  <span id="txtRepeatH" style="color: red;display: none"> <digi:trn key="aim:job:selectRepeat">Select repeat interval</digi:trn></span>                       
    	<html:select name="quartzJobManagerForm" property="exeTimeH"  styleId="exeTimeH" styleClass="inp-text">
				   <html:option value="00">00</html:option>
               	   <html:option value="01">01</html:option>
				   <html:option value="02">02</html:option>
				   <html:option value="03">03</html:option>
				   <html:option value="04">04</html:option>
				   <html:option value="05">05</html:option>
				   <html:option value="06">06</html:option>
				   <html:option value="07">07</html:option>
				   <html:option value="08">08</html:option>
				   <html:option value="09">09</html:option>
				   <html:option value="10">10</html:option>
				   <html:option value="11">11</html:option>
				   <html:option value="12">12</html:option>
                   <html:option value="13">13</html:option>
                   <html:option value="14">14</html:option>
                   <html:option value="15">15</html:option>
                   <html:option value="16">16</html:option>
                   <html:option value="17">17</html:option>
                   <html:option value="18">18</html:option>
                   <html:option value="19">19</html:option>
                   <html:option value="20">20</html:option>
                   <html:option value="21">21</html:option>
                   <html:option value="22">22</html:option>
                   <html:option value="23">23</html:option>
				  </html:select>  
	          </tr>
              <tr>
                <td width="60">            
                    <digi:trn key="aim:job:Minute">Minute</digi:trn>           
                <td>  
                
                
                 <span id="txtRepeatM" style="color: red;display: none"> <digi:trn key="aim:job:selectRepeat">Select repeat interval</digi:trn></span>
			      
                <html:select name="quartzJobManagerForm" property="exeTimeM"  styleId="exeTimeM" styleClass="inp-text">
					<html:option value="00">00</html:option>
				 	<html:option value="05">05</html:option>
					<html:option value="10">10</html:option>
					<html:option value="15">15</html:option>
				  	<html:option value="20">20</html:option>
				  	<html:option value="25">25</html:option>
				  	<html:option value="30">30</html:option>
				  	<html:option value="35">35</html:option>
				  	<html:option value="40">40</html:option>
				  	<html:option value="45">45</html:option>
				  	<html:option value="50">50</html:option>
				  	<html:option value="55">55</html:option>
			    </html:select>           
			  	   </tr>
            </table>
            </td>
          </tr>
          <tr>
            <td>
              <c:set var="trn">
                <digi:trn key="aim:job:btnSave">Save</digi:trn>
              </c:set>
              <input type="button" value="${trn}" onclick="saveJob()"/>
              <c:set var="trnCan">
                <digi:trn key="aim:job:btnCancel">Cancel</digi:trn>
              </c:set>
              <input type="button" value="${trnCan}" onclick="cancel()"/>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</digi:form>
<script type="text/javascript">
    window.onload = onLoad;
    function onLoad(){
        
        var val=1;
        var elements= document.getElementsByName("triggerType");
        for(var i=0;i<elements.length;i++){
            if(elements[i].checked){
                val=elements[i].value;
                break;
            }
        }
        if(val==5){
             var mdays=document.getElementById("monthDays");
             mdays.disabled=false;
        }
        if(val==4){
            var cmb=document.getElementById("cmbWeekDays");
            cmb.disabled=false;
        }
        
        typeChanged(parseInt(val));
        shoValidationmsg();
       }
</script>


