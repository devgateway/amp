<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="quartzJobClassManagerForm" />

<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<script type="text/javascript">
function showValidationmsg(){
    <logic:equal name="quartzJobClassManagerForm" property="errorCode" value="1">
        <c:set var="invalidClassName">
            <digi:trn>The Job class name is invalid</digi:trn>
        </c:set>
        alert("${invalidClassName}");
    </logic:equal>
}

function setAction(action){
  var act=document.getElementById("hdnAction");
  if(act!=null){
    act.value=action;
  }else{
    return false;
  }
  return true;
}

function isEditable() {
  var hdnId=document.getElementById("hdnId");
  return hdnId != null && hdnId.value !== '' && hdnId.value !== '0';
}

function saveJc(){
  var txt=null;

  txt=document.getElementById("txtClassFullname");
  if(txt==null || txt.value==""){
	alert('<digi:trn jsFriendly="true" key="aim:enterclassfullname">Please enter Class Fullname!</digi:trn>');
    txt.focus();
    return false;
  }

  txt=document.getElementById("txtName");
  if(txt==null || txt.value==""){
	alert('<digi:trn jsFriendly="true" key="aim:enterclassname">Please enter Name!</digi:trn>');
    txt.focus();
    return false;
  }

  if((isEditable() && setAction("updateJc")) || setAction("saveJc")){
    document.quartzJobClassManagerForm.submit();
  }
}

function cancel()
{
	window.location.replace("/aim/quartzJobClassManager.do~action=all");
}

$(document).ready(function() {showValidationmsg();});
</script>

<digi:form action="/quartzJobClassManager.do" method="post">
  <html:hidden name="quartzJobClassManagerForm" property="id" styleId="hdnId" />
  <html:hidden name="quartzJobClassManagerForm" property="action" styleId="hdnAction" />
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
                <digi:link ampModule="aim" href="/admin.do" styleClass="comment" title="${translation}" >
                  <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
                </digi:link>&nbsp;&gt;&nbsp;
                <digi:link href="/msgSettings.do~actionType=getSettings" styleClass="comment" title="${translation}" >
                  <digi:trn key="message:messageSettings">Message Settings</digi:trn>
                </digi:link>&nbsp;&gt;&nbsp;
                <digi:link href="/quartzJobClassManager.do" styleClass="comment" title="${translation}" >
                  <digi:trn key="aim:jc:jobManager">Job Class Manager</digi:trn>
                </digi:link>
                &nbsp;&gt;&nbsp;
                <digi:trn key="aim:jc:addJob">Add new job class</digi:trn>
              </span>
            </td>
            <!-- End navigation -->
          </tr>
          <tr>
            <td style="height:53px;width400px">
              <span class="subtitle-blue">
                <digi:trn key="aim:jc:addJob">Add New Job Class</digi:trn>
              </span>
            </td>
          </tr>
          <tr>
            <td>
              <table style="width:400px;">
                <tr>
                  <td width="169">
                    <span style="color:red;">*</span>
                    <digi:trn key="aim:job:name">Name</digi:trn>
                  </td>
                  <td width="219">
                    <html:text name="quartzJobClassManagerForm" property="name" styleId="txtName" style="font-family:Verdana;font-size:10px;width:250px;" />
                  </td>
                </tr>
                <tr>
                  <td>
                    <span style="color:red;">*</span>
                    <digi:trn key="aim:job:classFullname">Class fullname:</digi:trn>
                  </td>
                  <td>
                    <html:text name="quartzJobClassManagerForm" property="classFullname" styleId="txtClassFullname" style="font-family:Verdana;font-size:10px;width:250px;" />
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td colspan="6">&nbsp;</td>
          </tr>
          <tr>
            <td colspan="6">&nbsp;</td>
          </tr>
          <tr>
            <td colspan="6">
            	<table>
            		<tr>
            			<td>
                            <c:set var="trn">
                                <digi:trn key="aim:job:btnSave">Save</digi:trn>
                            </c:set>
                            <input type="button" value="${trn}" onclick="saveJc()"/>
            			</td>
            			<td>
            				<c:set var="btnCancel">
								<digi:trn key="btn:cancel">Cancel</digi:trn>
							</c:set>
            				<input type="button" value="${btnCancel}" onclick="cancel()" />
            			</td>
            		</tr>
            	</table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</digi:form>
