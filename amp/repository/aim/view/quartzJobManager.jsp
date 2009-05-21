<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<digi:instance property="quartzJobManagerForm" />
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>

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
   function runJobNow(name){
        if(setNameAndAction(name,"runJobNow")){
            quartzJobManagerForm.submit();
        }
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

    function addActionToURL(actionName){
        var fullURL=document.URL;
        var urlPath=location.pathname;
        var cutPos = fullURL.indexOf(urlPath);
        var partialURL=fullURL.substr(0,cutPos);
        return partialURL+"/"+actionName;
    }

    function getServerTime(){
        var url=addActionToURL('aim/quartzJobManager.do?action=serverTime');
        var async=new Asynchronous();
        async.complete=displayServerTime;
        async.call(url);
        window.setTimeout("getServerTime()",10000,"JavaScript");
    }

    function displayServerTime(status, statusText, responseText, responseXML){
        var dv=document.getElementById("divServerTime");
        if(dv!=null){
            dv.innerHTML=responseText;
        }
    }
</script>
<digi:form action="/quartzJobManager.do" method="post">
    <html:hidden name="quartzJobManagerForm" property="name" styleId="hdnName" />
    <html:hidden name="quartzJobManagerForm" property="action" styleId="hdnAction" />
    <table>
        <tr>
            <td>
                &nbsp;
            </td>
            <td>
                <table>
                    <tr>
                        <!-- Start Navigation -->
                        <td colspan="6">
                            <span class="crumb">
                                <c:set var="translation">
                                    <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
                                </c:set>
                                <digi:link module="aim" href="/admin.do" styleClass="comment" title="${translation}" >
                                    <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
                                </digi:link>&nbsp;&gt;&nbsp;
                                <digi:trn key="aim:jobManager">Job Manager</digi:trn>
                            </span>
                        </td>
                        <!-- End navigation -->
                    </tr>
                    <tr>
                        <td colspan="6" style="height:53px;width:350px;">
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
                        <td>
                            <table>
                                <tr>
                                    <td style="white-space:nowrap;background-color:#CCCCCC;padding: 5px 5px 5px 5px;width:250px;border-left:solid 1px #000000;">
                                        <b><digi:trn key="aim:job:clmName">Name</digi:trn></b>
                                    </td>
                                    <td style="white-space:nowrap;background-color:#CCCCCC;padding: 5px 5px 5px 5px;width:120px;border-left:solid 1px #000000;">
                                        <b><digi:trn key="aim:job:clmStartDate">Start date</digi:trn></b>
                                    </td>
                                    <td style="white-space:nowrap;background-color:#CCCCCC;padding: 5px 5px 5px 5px;width:120px;border-left:solid 1px #000000;">
                                        <b><digi:trn key="aim:job:clmEndDate">End date</digi:trn></b>
                                    </td>
                                    <td style="white-space:nowrap;background-color:#CCCCCC;padding: 5px 5px 5px 5px;width:120px;border-left:solid 1px #000000;">
                                        <b><digi:trn key="aim:job:clmPrevFireDate">Previus fire date</digi:trn></b>
                                    </td>
                                    <td style="white-space:nowrap;background-color:#CCCCCC;padding: 5px 5px 5px 5px;width:120px;border-left:solid 1px #000000;">
                                        <b><digi:trn key="aim:job:clmNextFiredate">Next fire date</digi:trn></b>
                                    </td>
                                    <td style="white-space:nowrap;background-color:#CCCCCC;padding: 5px 5px 5px 5px;width:120px;border-left:solid 1px #000000;">
                                        <b><digi:trn key="aim:job:clmFinalFireDate">Final fire date</digi:trn></b>
                                    </td>
                                    <td style="white-space:nowrap;background-color:#CCCCCC;padding: 5px 5px 5px 5px;width:60px;border-left:solid 1px #000000;">
                                        <b><digi:trn key="aim:job:clmStatus">Status</digi:trn></b>
                                    </td>
                                    <td style="white-space:nowrap;background-color:#CCCCCC;padding: 5px 5px 5px 5px;width:160px;border-left:solid 1px #000000;border-right:solid 1px #000000;">
                                        <b><digi:trn key="aim:job:clmCommands">Commands</digi:trn></b>
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
                                    <c:choose>
                                        <c:when test="${job.manualJob&&job.startDateTime==job.finalFireDateTime}">
                                            <c:set var="manualJobName">
                                            ${job.name}
                                            </c:set>
                                            <c:set var="manualJobStartDate">
                                            ${job.startDateTime}
                                            </c:set>
                                        </c:when>
                                        <c:otherwise>
                                             <tr>
                                        <td style="border-left:solid 1px #000000;white-space:nowrap;">
                                            &nbsp;${job.name}
                                        </td>
                                        <td style="border-left:solid 1px #000000;white-space:nowrap;">
                                            &nbsp;${job.startDateTime}
                                        </td>
                                        <td style="border-left:solid 1px #000000;white-space:nowrap;">
                                            &nbsp;${job.endDateTime}
                                        </td>
                                        <td style="border-left:solid 1px #000000;white-space:nowrap;">
                                            &nbsp;${job.prevFireDateTime}
                                        </td>
                                        <td style="border-left:solid 1px #000000;white-space:nowrap;">
                                            &nbsp;${job.nextFireDateTime}
                                        </td>
                                        <td style="border-left:solid 1px #000000;white-space:nowrap;">
                                            &nbsp;${job.finalFireDateTime}
                                        </td>
                                        <td style="border-left:solid 1px #000000;white-space:nowrap;">
                                            &nbsp;
                                            <c:if test="${job.paused}">
                                                <b><digi:trn key="aim:job:stPaused">Paused</digi:trn></b>
                                            </c:if>
                                            <c:if test="${!job.paused}">
                                                <b><digi:trn key="aim:job:stWorking">Working</digi:trn></b>
                                            </c:if>
                                        </td>
                                        <td style="border-left:solid 1px #000000;border-right:solid 1px #000000;white-space:nowrap;">
                                            &nbsp;
                                            <c:if test="${job.paused}">
                                                [<digi:trn key="aim:job:lnkPause">Pause</digi:trn>]
                                                [<a href="javaScript:resumeJob('${job.name}');"><digi:trn key="aim:job:lnkResume">Resume</digi:trn></a>]
                                                [<digi:trn key="aim:job:lnkEditjob">Edit job</digi:trn>]
                                            </c:if>
                                            <c:if test="${!job.paused}">
                                                [<a href="javaScript:pauseJob('${job.name}');"><digi:trn key="aim:job:lnkPause">Pause</digi:trn></a>]
                                                [<digi:trn key="aim:job:lnkResume">Resume</digi:trn>]
                                                [<a href="javaScript:editJob('${job.name}');"><digi:trn key="aim:job:lnkEditjob">Edit job</digi:trn></a>]
                                            </c:if>
                                              [<a href="javaScript:runJobNow('${job.name}');"><digi:trn key="aim:job:lnkRunNow">Run Now</digi:trn></a>]
                                             [<a href="javaScript:deleteJob('${job.name}');"><digi:trn key="aim:job:lnkDelete">Delete</digi:trn></a>]
                                        </td>
                                    </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </table>
                        </td>
                        <td>
                            &nbsp;
                        </td>
                        <td>
                            <!-- Other Links -->
                            <table cellPadding=0 cellSpacing=0 width=100>
                                <tr>
                                    <td bgColor=#c9c9c7 class=box-title>
                                        <digi:trn key="aim:otherLinks">
                                            Other links
                                        </digi:trn>
                                    </td>
                                    <td style="background-image: url(module/aim/images/corner-r.gif);background-repeat:no-repeat;height:17px">
                                        &nbsp;&nbsp;
                                    </td>
                                </tr>
                                <tr>
                                    <td bgColor=#ffffff class=box-border colspan="2">
                                        <table cellPadding=5 cellSpacing=1 width="100%">
                                            <tr>
                                                <td>
                                                    <digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
                                                </td>
                                                <td>
                                                     <digi:link module="aim"  href="/admin.do" >
                                                        <digi:trn key="aim:AmpAdminHome">
                                                            Admin Home
                                                        </digi:trn>
                                                    </digi:link>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                <digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
                                                </td>
                                                <td nowrap>
                                                    <c:set var="trn">
                                                        <digi:trn key="aim:viewSettings">Click here to view Job Class Manager</digi:trn>
                                                    </c:set>
                                                    <digi:link module="aim" href="/quartzJobClassManager.do" title="${trn}">
                                                        <digi:trn key="aim:jobClassManager">Job Class Manager</digi:trn>
                                                    </digi:link>

                                                </td>
                                            </tr>

                                            <!-- end of other links -->
                                        </table>



                                    </td>
                                </tr>
                            </table>
                 </td>
                 </tr>
                 </table>
                 <c:if test="${not empty manualJobName}">
                     <table>
                         <tr style="color:red">
                             <td>
                                 &nbsp;${manualJobName} job was run manually at ${manualJobStartDate}
                             </td>
                         </tr>
                     </table>
                 </c:if>
                <table style="text-align:right;width:100%;">
                    <tr>
                        <td style="height:70px;white-space:nowrap;">
                            <a href="javaScript:addJob();"><digi:trn key="aim:job:lnkAddNewJob">Add new job</digi:trn></a>
                            &nbsp;
                            <a href="javaScript:pauseAllJobs();"><digi:trn key="aim:job:lnkPauseAllJobs">Pause all jobs</digi:trn></a>
                            &nbsp;
                            <a href="javaScript:resumeAllJobs();"><digi:trn key="aim:job:lnkResumeAllJobs">Resume all jobs</digi:trn></a>
                        </td>
                    </tr>
                    <tr>
                        <td style="white-space:nowrap;">
                            <digi:trn key="aim:job:serverTime">Server date and time:</digi:trn>
                            <span id="divServerTime"></span>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</digi:form>
<script type="text/javascript">
    getServerTime();
</script>
