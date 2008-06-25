<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<digi:instance property="quartzJobManagerForm" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<script type="text/javascript">
function setNameAndAction(name, action){
  var nm=document.getElementById("hdnName");
  if(nm!=null){
    nm.value=name;
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

function deleteJob(name){
  if(setNameAndAction(name,"deleteJob")){
      quartzJobManagerForm.submit();
  }
}
function pauseJob(name){
  if(setNameAndAction(name,"pauseJob")){
      quartzJobManagerForm.submit();
  }
}
function resumeJob(name){
  if(setNameAndAction(name,"resumeJob")){
      quartzJobManagerForm.submit();
  }
}

function pauseAllJobs(){
  if(setNameAndAction(name,"pauseAll")){
      quartzJobManagerForm.submit();
  }
}
function resumeAllJobs(){
  if(setNameAndAction(name,"resumeAll")){
      quartzJobManagerForm.submit();
  }
}

function editJob(name){
  if(setNameAndAction(name,"editJob")){
      quartzJobManagerForm.submit();
  }
}

function addJob(){
  if(setNameAndAction(null,"addJob")){
      quartzJobManagerForm.submit();
  }
}
</script>
<digi:form action="/quartzJobManager.do" method="post">
  <html:hidden name="quartzJobManagerForm" property="name" styleId="hdnName" />
  <html:hidden name="quartzJobManagerForm" property="action" styleId="hdnAction" />
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
                <digi:link href="/admin.do" styleClass="comment" title="${translation}" >
                  <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
                </digi:link>&nbsp;&gt;&nbsp;
                <digi:trn key="aim:jobManager">Job Manager</digi:trn>
              </span>
            </td>
            <!-- End navigation -->
          </tr>
          <tr>
            <td style="height:53px;">
              <span class="subtitle-blue">
                <digi:trn key="aim:jobManager">Job Manager</digi:trn>
              </span>
            </td>
          </tr>
          <tr>
            <td colspan="6">
              <digi:errors />
            </td>
          </tr>
          <tr>
            <td colspan="6">
            &nbsp;
            </td>
          </tr>
          <tr style="">
            <td style="background-color:#CCCCCC;padding: 5px 15px 5px 15px;">
              <digi:trn key="aim:job:clmName">Name</digi:trn>
            </td>
            <td style="background-color:#CCCCCC;padding: 5px 15px 5px 15px;">
              <digi:trn key="aim:job:clmStartDate">Start date</digi:trn>
            </td>
            <td style="background-color:#CCCCCC;padding: 5px 15px 5px 15px;">
              <digi:trn key="aim:job:clmEndDate">End date</digi:trn>
            </td>
            <td style="background-color:#CCCCCC;padding: 5px 15px 5px 15px;">
              <digi:trn key="aim:job:clmNextFiredate">Next fire date</digi:trn>
            </td>
            <td style="background-color:#CCCCCC;padding: 5px 15px 5px 15px;">
              <digi:trn key="aim:job:clmPrevFireDate">Previus fire date</digi:trn>
            </td>
            <td style="background-color:#CCCCCC;padding: 5px 15px 5px 15px;">
              <digi:trn key="aim:job:clmFinalFireDate">Final fire date</digi:trn>
            </td>
            <td style="background-color:#CCCCCC;padding: 5px 15px 5px 15px;">
              <digi:trn key="aim:job:clmStatus">Status</digi:trn>
            </td>
            <td style="background-color:#CCCCCC;padding: 5px 15px 5px 15px;">
              <digi:trn key="aim:job:clmCommands">Commands</digi:trn>
            </td>
          </tr>
          <c:if test="${empty quartzJobManagerForm.jobs}">
            <tr>
              <td colspan="8" style="text-align:center;">
                <br/>
                <b> <digi:trn key="aim:job:noJobs">No jobs at this time</digi:trn></b>
              </td>
            </tr>
          </c:if>
          <c:forEach var="job" items="${quartzJobManagerForm.jobs}">
            <tr>
              <td>
              ${job.name}
              </td>
              <td>
              ${job.startDateTime}
              </td>
              <td>
              ${job.endDateTime}
              </td>
              <td>
              ${job.nextFireDateTime}
              </td>
              <td>
              ${job.prevFireDateTime}
              </td>
              <td>
              ${job.finalFireDateTime}
              </td>
              <td>
                <c:if test="${job.paused}">
                  <b><digi:trn key="aim:job:stPaused">Paused</digi:trn></b>
                </c:if>
                <c:if test="${!job.paused}">
                  <b><digi:trn key="aim:job:stWorking">Working</digi:trn></b>
                </c:if>
              </td>
              <td>
                <c:if test="${job.paused}">
                  [<digi:trn key="aim:job:lnkPause">Pause</digi:trn>]
                  &nbsp;
                  [<a href="javaScript:resumeJob('${job.name}');"><digi:trn key="aim:job:lnkResume">Resume</digi:trn></a>]
                </c:if>
                <c:if test="${!job.paused}">
                  [<a href="javaScript:pauseJob('${job.name}');"><digi:trn key="aim:job:lnkPause">Pause</digi:trn></a>]
                  &nbsp;
                  [<digi:trn key="aim:job:lnkResume">Resume</digi:trn>]
                </c:if>
                &nbsp;
                [<a href="javaScript:deleteJob('${job.name}');"><digi:trn key="aim:job:lnkDelete">Delete</digi:trn></a>]
              </td>
            </tr>
          </c:forEach>
        </table>
        <table style="text-align:right;width:100%;">
          <tr>
            <td style="height:50px;">
              <a href="javaScript:addJob();"><digi:trn key="aim:job:lnkAddNewJob">Add new job</digi:trn></a>
              &nbsp;
              <a href="javaScript:pauseAllJobs();"><digi:trn key="aim:job:lnkPauseAllJobs">Pause all jobs</digi:trn></a>
              &nbsp;
              <a href="javaScript:resumeAllJobs();"><digi:trn key="aim:job:lnkResumeAllJobs">Resume all jobs</digi:trn></a>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</digi:form>
