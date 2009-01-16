<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<digi:instance property="quartzJobManagerForm" />

<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<script type="text/javascript">
function setAction(action){
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
      alert("Please enter Class Fullname!");
      txt.focus();
      return false;
    }
  }

  txt=document.getElementById("txtName");
  if(txt==null || txt.value==""){
    alert("Please enter Name!");
    txt.focus();
    return false;
  }

  txt=document.getElementById("txtStartDateTime");
  if(txt==null || txt.value==""){
    alert("Please enter Start Date/Time!");
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
      alert("Please select Job type");
      return false;
    }
  }

              txt=document.getElementById("txtTime");

              var regEx=/^\d{2}:\d{2}$/;
              if(txt==null || txt.value==""||txt.value=='0'){
                  alert("Please enter time");
                  txt.focus();
                  return false;
              }
              else{
                  if(flag>2){
                      if (txt.value.search(regEx)==-1){
                          //if match failed
                          alert("the time should be in the hh:mm format");
                          txt.focus();
                          return false;
                      }
                  }
                  else{
                      if(isNaN(txt.value)){
                          alert("Please enter valid time interval");
                          txt.focus();
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
  switch(value){
      case 4:
           cmb.disabled=false;mdays.disabled=true; break;
      case 5:
          cmb.disabled=true;mdays.disabled=false; break;
      default:
          cmb.disabled=true;mdays.disabled=true; break;
  }
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
              <table style="width:400px;">
                <tr>
                  <td>
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
                  <td>
                    <span style="color:red;">*</span>
                    <digi:trn key="aim:job:name">name</digi:trn>
                  </td>
                  <td>
                    <html:text name="quartzJobManagerForm" property="name" styleId="txtName" style="font-family:Verdana;font-size:10px;width:250px;" />
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td colspan="6">
            &nbsp;
            </td>
          </tr>
          <tr>
            <td colspan="3">
              <table style="border:dashed 1px;width:400px;">
                <tr>
                  <td>
                    <span style="color:red;">*</span>
                    <digi:trn key="aim:job:startDateTime">Start date/time</digi:trn>
                  </td>
                  <td>
                    <html:text name="quartzJobManagerForm" property="startDateTime" styleId="txtStartDateTime" style="font-family:Verdana;font-size:10px;width:250px;" />
                  </td>
                </tr>
                <tr>
                  <td>
                    <digi:trn key="aim:job:endDateTime">End date/time</digi:trn>
                  </td>
                  <td>
                    <html:text name="quartzJobManagerForm" property="endDateTime" style="font-family:Verdana;font-size:10px;wodth:250px;width:250px;" />
                  </td>
                </tr>
                <tr>
                  <td colspan="2">
                    <b><digi:trn key="aim:job:startEndDateTimeFormatNote">(All date/time format should be like dd/MM/yyyy HH:mm:ss)</digi:trn></b>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td colspan="6">
            &nbsp;
            </td>
          </tr>
          <tr>
            <td colspan="2">
            <span style="color:red;">*</span>
            <digi:trn key="aim:job:jobType">Job Type</digi:trn>
            <table style="border:dashed 1px;width:400px;">
              <tr>
                <td colspan="2">
                  <html:radio name="quartzJobManagerForm" property="triggerType" value="0" onclick="typeChanged(0);" />Secondly
                  <html:radio name="quartzJobManagerForm" property="triggerType" value="1" onclick="typeChanged(1);" />Minutely
                  <html:radio name="quartzJobManagerForm" property="triggerType" value="2" onclick="typeChanged(2);" />Hourly
                  <html:radio name="quartzJobManagerForm" property="triggerType" value="3" onclick="typeChanged(3);" />Daily
                  <html:radio name="quartzJobManagerForm" property="triggerType" value="4" onclick="typeChanged(4);" />Weekly
                  <html:radio name="quartzJobManagerForm" property="triggerType" value="5" onclick="typeChanged(5);" />Monthly
                </td>
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
                    </html:select>
                  </td>
              </tr>
              <tr>
                 <td colspan="2">
                   <digi:trn key="aim:job:jobDayOfMonth">Select Day of month</digi:trn>
                   <html:select name="quartzJobManagerForm" property="selectedMonthDay" styleId="monthDays" style="font-family:Verdana;font-size:10px;" disabled="true">
                          <c:forEach var="i" begin="1" end="31">
                              <html:option value="${i}">${i}</html:option>
                          </c:forEach>
                      </html:select>
                  </td>
              </tr>
              <tr>
                  <td colspan="2">
                      <digi:trn key="aim:job:time">Time</digi:trn>
                      <html:text name="quartzJobManagerForm" property="exeTime" styleId="txtTime" style="font-family:Verdana;font-size:10px;" />
                  </td>
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
    }

</script>
