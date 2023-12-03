<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<digi:instance property="quartzJobManagerForm" />
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>
<jsp:include page="scripts/newCalendar.jsp"  />

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
			<digi:trn>Please enter Class Fullname</digi:trn>
		</c:set>   	
	
      alert("${enterClassName}");
      txt.focus();
      return false;
    }
  }

  txt=document.getElementById("txtName");
  if(txt==null || txt.value==""){
	  	<c:set var="name">
			<digi:trn>Please enter Name</digi:trn>
		</c:set>   	

		
    alert("${name}");
    txt.focus();
    return false;
  }

  txt=document.getElementById("txtStartDateTime");
  if(txt==null || txt.value==""){
		<c:set var="datetime">
			<digi:trn>Please enter Start Date/Time!</digi:trn>
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
			<digi:trn>Please select Job typee</digi:trn>
		</c:set> 
      alert("${jobType}");
      return false;
    }

     //
	if (type==1){
		if (document.getElementById("exeTimeM").value=="00"){
			<c:set var="invalidRepeatTime">
				<digi:trn>Repeat time can't be 0</digi:trn>
			</c:set> 
			alert("${invalidRepeatTime}")
			document.getElementById("exeTimeM").focus();
			 return false;
		}
	}
	if (type==2){
		if (document.getElementById("exeTimeH").value=="00"){
			<c:set var="invalidRepeatTime">
				<digi:trn>Repeat time can't be 0</digi:trn>
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

		function showValidationmsg(){
		<logic:equal name="quartzJobManagerForm" property="invalidTrigger" value="true">
			<c:set var="invalidTrigger">
				<digi:trn>The trigger will never be fired</digi:trn>
			</c:set> 
			
			alert("${invalidTrigger}");
		</logic:equal>		

		<logic:equal name="quartzJobManagerForm" property="invalidClass" value="true">
			<c:set var="invalidClass">
				<digi:trn>Cannot find job class. Please check class configuration in the Job Class Manager</digi:trn>
			</c:set> 
			
			alert("${invalidClass}");
	   </logic:equal>		
		
		
		<logic:equal name="quartzJobManagerForm" property="invalidEndDate" value="true">
		<c:set var="invalidEndDate">
			<digi:trn>End Date should be after Start Date</digi:trn>
		</c:set> 

			alert("${invalidEndDate}");

		</logic:equal>	
			return;
		}
	</script>
<digi:form action="/quartzJobManager.do" method="post">

  <html:hidden name="quartzJobManagerForm" property="action" styleId="hdnAction" />
  <html:hidden name="quartzJobManagerForm" property="editAction" />
 <h1 class="admintitle" style="text-align:left;">
								<c:choose>
									<c:when test="${quartzJobManagerForm.editAction=='false'}">
										<digi:trn>Add new job</digi:trn>
									</c:when>
									<c:otherwise>
										<digi:trn>Edit job</digi:trn>
									</c:otherwise>
								</c:choose>
						</h1>
  <table cellpadding="0" cellspacing="0" width=750 align=center>
    <tr>
     <td align=center bgcolor=#F2F2F2>
        <table style="font-size:12px;">
          <tr>
            <!-- Start Navigation -->
            <td>
              <span class="crumb">
                <c:set var="translation">
                  <digi:trn>Click here to goto Admin Home</digi:trn>
                </c:set>
                <digi:link ampModule="aim" href="/admin.do" styleClass="comment" title="${translation}" >
                  <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
                </digi:link>&nbsp;&gt;&nbsp;
                <digi:link href="/quartzJobManager.do?action=all" styleClass="comment" title="${translation}" >
                  <digi:trn>Job Manager</digi:trn>
                </digi:link> &nbsp;&gt;&nbsp;<c:choose>
									<c:when test="${quartzJobManagerForm.editAction=='false'}">
										<digi:trn>Add new job</digi:trn>
									</c:when>
									<c:otherwise>
										<digi:trn>Edit job</digi:trn>
									</c:otherwise>
								</c:choose>
						</span>
            </td>
            <!-- End navigation -->
          </tr>
          <tr>
          	<td>
          		<digi:errors/>
          	</td>
          </tr>         
          <tr>
            <td align=center>
              <table cellpadding="2" style="width:400px;">
                <tr>
                  <td align="right">
                    <c:if test="${empty quartzJobManagerForm.jcCol}">
                      <span style="color:red;">*</span>
                      <digi:trn>Class fullname:</digi:trn>
                    </c:if>
                    <c:if test="${!empty quartzJobManagerForm.jcCol}">
                      <digi:trn>Class:</digi:trn>
                    </c:if>
                  </td>
                <td>
                      <c:if test="${!empty quartzJobManagerForm.jcCol}">
                              <html:select name="quartzJobManagerForm" property="classFullname"  styleId="cmbJc" style="font-family:Verdana;font-size:10px;width:250px;">
                                  <c:forEach var="jc" items="${quartzJobManagerForm.jcCol}">
                                      <html:option value="${jc.classFullname}"><digi:trn>${jc.name}</digi:trn></html:option>
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
                    <digi:trn>Name</digi:trn>
                  </td>
                  <td>
                  	<c:set var="readOnly">
                  		<c:if test="${quartzJobManagerForm.editAction }">true</c:if>
                  		<c:if test="${!quartzJobManagerForm.editAction }">false</c:if>
                  	</c:set>
                    <html:text name="quartzJobManagerForm" readonly="${readOnly}" property="name" styleId="txtName" style="font-family:Verdana;font-size:10px;width:250px;" />
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
            <td colspan="3" align=center>
              <table cellpadding="2" cellspacing="1" style="border:dashed 1px;width:400px;">
                <tr>
                  <td width="96" align="right">
                    <span style="color:red;">*</span>
                  <digi:trn>Start date/time</digi:trn>                  </td>
                  <td width="288">
                
                
                  <html:text name="quartzJobManagerForm" readonly="true" property="startDateTime" styleId="txtStartDateTime" style="width:100px" styleClass="inp-text"/>
               	  
                  <a id="clear1" href='javascript:clearDate("txtStartDateTime")'>
		<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
	</a>
	<a id="date1" href='javascript:pickDateWithClear("date1",document.getElementById("txtStartDateTime"),"clear1")'>
		<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">	</a>      
               	 
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
                  <digi:trn>End date/time</digi:trn>                  </td>
                  <td>
                 
     
<html:text name="quartzJobManagerForm" readonly="true" property="endDateTime" styleId="txtEndDateTime" style="width:100px"  styleClass="inp-text"/>
                   <a id="clear2" href='javascript:clearDate("txtEndDateTime")'>
		<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
	</a>
	<a id="date2" href='javascript:pickDateWithClear("date2",document.getElementById("txtEndDateTime"),"clear2")'>
		<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">	</a>    
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
            <td colspan="2" align=center>
            <span style="color:red;">*</span>
            <b><digi:trn>Job Type</digi:trn></b>
            <table width="439" cellpadding="2" cellspacing="1" style="border:dashed 1px;width:400px;">
              <tr>
                <td colspan="2">
               	  <html:radio name="quartzJobManagerForm" property="triggerType" value="1" onclick="typeChanged(1);" /><digi:trn>Minutely</digi:trn>
                  <html:radio name="quartzJobManagerForm" property="triggerType" value="2" onclick="typeChanged(2);" /><digi:trn>Hourly</digi:trn>
                  <html:radio name="quartzJobManagerForm" property="triggerType" value="3" onclick="typeChanged(3);" /><digi:trn>Daily</digi:trn>
                  <html:radio name="quartzJobManagerForm" property="triggerType" value="4" onclick="typeChanged(4);" /><digi:trn>Weekly</digi:trn>
                  <html:radio name="quartzJobManagerForm" property="triggerType" value="5" onclick="typeChanged(5);" /><digi:trn>Monthly</digi:trn>                </td>
              </tr>
              <tr>
                  <td colspan="2">
                  <digi:trn>Select Day of week</digi:trn>
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
              		    <span id="selectTime" style="color: red;display: none"> <digi:trn>Select trigger time</digi:trn></span>                       
        
              </td>
              </tr>
              <tr>
                  <td width="60">
                       <digi:trn>Hour</digi:trn>          
                  <td>
                  
                  <span id="txtRepeatH" style="color: red;display: none"> <digi:trn>Select repeat interval</digi:trn></span>                       
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
                    <digi:trn>Minute</digi:trn>           
                <td>  
                
                
                 <span id="txtRepeatM" style="color: red;display: none"> <digi:trn>Select repeat interval</digi:trn></span>
			      
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
            <td align=center style="padding:15px;">
              <c:set var="trn">
                <digi:trn>Save</digi:trn>
              </c:set>
              <input type="button" value="${trn}" onclick="saveJob()" class="buttonx"/>
              <c:set var="trnCan">
                <digi:trn>Cancel</digi:trn>
              </c:set>
              <input type="button" value="${trnCan}" onclick="cancel()" class="buttonx"/>
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
        showValidationmsg();
       }
</script>


