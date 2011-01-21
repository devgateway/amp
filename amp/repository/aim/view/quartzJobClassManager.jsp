<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<digi:instance property="quartzJobClassManagerForm" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<script type="text/javascript">
function setIdAndAction(id, action){
  var hid=document.getElementById("hdnId");
  if(hid!=null){
    hid.value=id;
  }else{
    return false;
  }

  var act=document.getElementById("hdnAction");
  if(act!=null){
    act.value=action;
  }else{
    return false;
  }
  return true;
}

function deleteJc(id){
  if(setIdAndAction(id,"deleteJc")){
      document.quartzJobClassManagerForm.submit();
  }
}
function editJc(id){
  if(setIdAndAction(id,"editJc")){
      document.quartzJobClassManagerForm.submit();
  }
}

function addJc(){
  if(setIdAndAction(null,"addJc")){
      quartzJobClassManagerForm.submit();
  }
}
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
            <td colspan="6">
              <span class="crumb">
                <c:set var="translation">
                  <digi:trn>Click here to goto Admin Home</digi:trn>
                </c:set>
                <digi:link module="aim" href="/admin.do" styleClass="comment" title="${translation}" >
                  <digi:trn>Admin Home</digi:trn>
                </digi:link>&nbsp;&gt;&nbsp;
                <c:set var="trn">
                  <digi:trn>Click here to goto Job Manager</digi:trn>
                </c:set>
                <digi:link module="aim" href="/quartzJobManager.do~action=all" styleClass="comment" title="${trn}" >
                  <digi:trn>Job Manager</digi:trn>
                </digi:link>&nbsp;&gt;&nbsp;
                <digi:trn>Job Class Manager</digi:trn>
              </span>
            </td>
            <!-- End navigation -->
          </tr>
          <tr>
            <td style="height:53px;" colspan="6">
              <span class="subtitle-blue">
                <digi:trn>Job Class Manager</digi:trn>
              </span>
            </td>
          </tr>
          <tr>
            <td colspan="3">
              <digi:errors />
            </td>
          </tr>
          <tr>
            <td colspan="3">
            &nbsp;
            </td>
          </tr>
          <tr>
            <td style="background-color:#CCCCCC;padding: 5px 5px 5px 5px;width:250px;">
              <digi:trn key="aim:jc:clmName">Name</digi:trn>
            </td>
            <td style="background-color:#CCCCCC;padding: 5px 5px 5px 5px;width:350px;">
              <digi:trn key="aim:jc:clmClassFullName">Class full name</digi:trn>
            </td>
            <td style="background-color:#CCCCCC;padding: 5px 5px 5px 5px;width:90px;">
              <digi:trn key="aim:jc:clmCommands">Commands</digi:trn>
            </td>
          </tr>
          <c:if test="${empty quartzJobClassManagerForm.clsCol}">
            <tr>
              <td colspan="3" style="text-align:center;">
                <br/>
                <b> <digi:trn key="aim:jc:noJcs">No job classes at this time</digi:trn></b>
              </td>
            </tr>
          </c:if>
          <c:forEach var="jc" items="${quartzJobClassManagerForm.clsCol}">
            <tr>
              <td>
              	<digi:trn key="job:${jc.name}">${jc.name}</digi:trn>
              </td>
              <td>
              ${jc.classFullname}
              </td>
              <td>
                [<a href="javaScript:editJc('${jc.id}');"><digi:trn key="aim:jc:lnkResume">Edit</digi:trn></a>]
                &nbsp;
                [<a href="javaScript:deleteJc('${jc.id}');"><digi:trn key="aim:jc:lnkDelete">Delete</digi:trn></a>]
              </td>
            </tr>
          </c:forEach>
        </table>
        <table style="text-align:right;width:100%;">
          <tr>
            <td style="height:50px;">
              <a href="javaScript:addJc();"><digi:trn key="aim:jc:lnkAddNewJob">Add new job class</digi:trn></a>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</digi:form>
