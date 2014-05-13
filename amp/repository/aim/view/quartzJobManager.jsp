<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<div style="margin:0 auto;width:1000px;">
<digi:instance property="quartzJobManagerForm" />
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
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
    	if ( confirm("<digi:trn>Are you sure you want to delete this Job</digi:trn>?") ) {
	        if(setNameAndAction(name,"deleteJob")){
	            quartzJobManagerForm.submit();
	        }
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
   <h1 class="admintitle">
                                <digi:trn key="aim:jobManager">Job Manager</digi:trn>
                            </h1>
<digi:form action="/quartzJobManager.do" method="post">
    <html:hidden name="quartzJobManagerForm" property="name" styleId="hdnName" />
    <html:hidden name="quartzJobManagerForm" property="action" styleId="hdnAction" />
    <table>
        <tr>
            <td>&nbsp;
                
            </td>
            <td align=center>
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
                        <td colspan="6">
                            <digi:errors />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <table style="border:1px solid #cccccc;" id="jobmanagertable">
                                <tr>
                                    <td style="background-color:#c7d4db;text-align:center;padding: 5px 5px 5px 5px;width:200px;border-right:1px solid #fff;">
                                        <b><digi:trn key="aim:job:clmName">Name</digi:trn></b>
                                    </td>
                                    <td style="background-color:#c7d4db;text-align:center;padding: 5px 5px 5px 5px;width:80px;border-right:1px solid #fff;">
                                        <b><digi:trn key="aim:job:clmStartDate">Start date</digi:trn></b>
                                    </td>
                                    <td style="background-color:#c7d4db;text-align:center;padding: 5px 5px 5px 5px;width:80px;border-right:1px solid #fff;">
                                        <b><digi:trn key="aim:job:clmEndDate">End date</digi:trn></b>
                                    </td>
                                    <td style="background-color:#c7d4db;text-align:center;padding: 5px 5px 5px 5px;width:80px;border-right:1px solid #fff;">
                                        <b><digi:trn key="aim:job:clmPrevFireDate">Previus fire date</digi:trn></b>
                                    </td>
                                    <td style="background-color:#c7d4db;text-align:center;padding: 5px 5px 5px 5px;width:80px;border-right:1px solid #fff;">
                                        <b><digi:trn key="aim:job:clmNextFiredate">Next fire date</digi:trn></b>
                                    </td>
                                    <td style="background-color:#c7d4db;text-align:center;padding: 5px 5px 5px 5px;width:80px;;">
                                        <b><digi:trn key="aim:job:clmFinalFireDate">Final fire date</digi:trn></b>
                                    </td>
                                    <td style="background-color:#c7d4db;text-align:center;padding: 5px 5px 5px 5px;width:50px;border-right:1px solid #fff;">
                                        <b><digi:trn key="aim:job:clmStatus">Status</digi:trn></b>
                                    </td>
                                    <td style="background-color:#c7d4db;text-align:center;padding: 5px 5px 5px 5px;width:100px;">
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
                                <c:forEach var="job" items="${quartzJobManagerForm.jobs}" varStatus="loop">
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
                                        	<c:if test="${loop.index % 2 eq 0}">
                                        		<c:set var="bgcolor_even_odd">
                                            		background:#ffffff
                                            	</c:set>
                                            </c:if>
                                        	<c:if test="${loop.index % 2 ne 0}">
                                        		<c:set var="bgcolor_even_odd">
                                            		background:#F2F2F2
                                            	</c:set>
                                            </c:if>
	                                        <tr>
		                                        <td style="font-size:11px;${bgcolor_even_odd};border-right:1px solid #cccccc;text-align:left;">
		                                            &nbsp;${job.name}
		                                        </td>
		                                        <td style=";font-size:11px;${bgcolor_even_odd};border-right:1px solid #cccccc;text-align:center;">
		                                            &nbsp;${job.startDateTime}
		                                        </td>
		                                        <td style=";font-size:11px;${bgcolor_even_odd};border-right:1px solid #cccccc;text-align:center;">
		                                            &nbsp;${job.endDateTime}
		                                        </td>
		                                        <td style=";font-size:11px;${bgcolor_even_odd};border-right:1px solid #cccccc;text-align:center;">
		                                            &nbsp;${job.prevFireDateTime}
		                                        </td>
		                                        <td style=";font-size:11px;${bgcolor_even_odd};border-right:1px solid #cccccc;text-align:center;">
		                                            &nbsp;${job.nextFireDateTime}
		                                        </td>
		                                        <td style=";font-size:11px;${bgcolor_even_odd};border-right:1px solid #cccccc;text-align:center;">
		                                            &nbsp;${job.finalFireDateTime}
		                                        </td>
		                                        <td style=";font-size:11px;${bgcolor_even_odd};border-right:1px solid #cccccc;text-align:center;">
		                                            &nbsp;
		                                            <c:if test="${job.paused}">
		                                                <b><digi:trn key="aim:job:stPaused">Paused</digi:trn></b>
		                                            </c:if>
		                                            <c:if test="${!job.paused}">
		                                                <b><digi:trn key="aim:job:stWorking">Working</digi:trn></b>
		                                            </c:if>
		                                        </td>
		                                        <td style="${bgcolor_even_odd}; white-space:nowrap;">
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
                        <td>&nbsp;
                            
                        </td>
                        <td valign="top">
						
						
						<table width="100" cellspacing="0" cellpadding="0">
														<tbody><tr>
															<td bgcolor="#c9c9c7" class="box-title">
															<b style="font-size:12px; color:#000000; padding-left:5px;">
															<digi:trn>Other links</digi:trn>
															</b></td>
															<td width="17" height="17" background="module/aim/images/corner-r.gif">&nbsp;</td>
														</tr>
													</tbody></table>
						
						
						
						<table width="150" cellspacing="0" cellpadding="0" class="inside">
														<tbody><tr>
															<td class="inside"><img width="15" height="10" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-014E86.gif"> <digi:link module="aim"  href="/admin.do" > <digi:trn key="aim:AmpAdminHome"> Admin Home </digi:trn></digi:link></td>
														</tr>
														<tr>
														<td class="inside">
														<img width="15" height="10" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-014E86.gif">
														<c:set var="trn"><digi:trn key="aim:viewSettings">Click here to view Job Class Manager</digi:trn></c:set>
            <digi:link module="aim" href="/quartzJobClassManager.do" title="${trn}"><digi:trn key="aim:jobClassManager">Job Class Manager</digi:trn></digi:link>
														
														</td>
														</tr>								
														<!-- end of other links -->
													</tbody></table>
						
						

                            <!-- Other Links -->
                           
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
                <table style="width:75%;" align=center>
                    <tr>
                        <td style="height:70px;" align=center>
                            <a href="javaScript:addJob();"><digi:trn key="aim:job:lnkAddNewJob">Add new job</digi:trn></a>
                            &nbsp;
                            <a href="javaScript:pauseAllJobs();"><digi:trn key="aim:job:lnkPauseAllJobs">Pause all jobs</digi:trn></a>
                            &nbsp;
                            <a href="javaScript:resumeAllJobs();"><digi:trn key="aim:job:lnkResumeAllJobs">Resume all jobs</digi:trn></a>
                        </td>
                    </tr>
                    <tr>
                        <td style="" align=center>
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
</div>