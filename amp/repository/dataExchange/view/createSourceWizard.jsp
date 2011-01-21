<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>



<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/yahoo-dom-event.js"></script>

<script type="text/javascript">
  if (YAHOOAmp != null){
    var YAHOO = YAHOOAmp;
  }
  var tree;
  
</script>

    <link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/treeview.css" />
    <link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/fonts-min.css" />
    <link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/tabview.css" />

    <script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/yahoo-min.js"></script>
    <script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/event-min.js"></script>
    <script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/new/treeview-min.js"></script>
    <script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/tabview-min.js"></script>

    <script type="text/javascript" src="/repository/dataExchange/view/scripts/TaskNode.js"></script>

<style type="text/css">

.ygtvcheck0 { background: url(/TEMPLATE/ampTemplate/images/yui/check0.gif) 0 0 no-repeat; width:16px; cursor:pointer }
.ygtvcheck1 { background: url(/TEMPLATE/ampTemplate/images/yui/check1.gif) 0 0 no-repeat; width:16px; cursor:pointer }
.ygtvcheck2 { background: url(/TEMPLATE/ampTemplate/images/yui/check2.gif) 0 0 no-repeat; width:16px; cursor:pointer }

#expandcontractdiv {border:1px solid #336600; background-color:#FFFFCC; margin:0 0 .5em 0; padding:0.2em;}
#treeDiv1 { background: #fff }

div.fileinputs {
	position: relative;
	height: 30px;
	width: 300px;
}
input.file {
	width: 300px;
	margin: 0;
}
input.file.hidden {
	position: relative;
	text-align: right;
	-moz-opacity:0 ;
	filter:alpha(opacity: 0);
	width: 300px;
	opacity: 0;
	z-index: 2;
}

div.fakefile {
	position: absolute;
	top: 0px;
	left: 0px;
	width: 300px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}
div.fakefile input {
	margin-bottom: 5px;
	margin-left: 0;
	width: 217px;
}
div.fakefile2 {
	position: absolute;
	top: 0px;
	left: 217px;
	width: 300px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}
div.fakefile2 input{
	width: 83px;
}
</style> 


	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/tab/assets/tabview.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/border_tabs.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/reportWizard/reportWizard.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/filters.css'/>">
  
	<script type="text/javascript">
	YAHOOAmp.namespace("YAHOOAmp.amp");
	YAHOOAmp.namespace("YAHOOAmp.amp.dataExchangeImport");
	YAHOOAmp.amp.dataExchangeImport				= new Object();
	YAHOOAmp.amp.dataExchangeImport.numOfSteps	= 4;
		
	YAHOOAmp.amp.dataExchangeImport.tabLabels	= new Array("tab_file_selection", "tab_log_after_import", "tab_select_activities", "tab_confirm_import");
		
        function navigateTab(value){
        	YAHOOAmp.amp.dataExchangeImport.tabView.set("activeIndex", YAHOO.amp.dataExchangeImport.tabView.get("activeIndex")+value);
        }
		
		
		function initializeDragAndDrop() {
			var height			= Math.round(YAHOO.util.Dom.getDocumentHeight() / 2.3);
			
			YAHOOAmp.amp.dataExchangeImport.tabView 		= new YAHOO.widget.TabView('wizard_container');
			YAHOOAmp.amp.dataExchangeImport.tabView.addListener("contentReady", treeInit);
		}


    function tabsInit() {
      YAHOOAmp.amp.dataExchangeImport.tabView     = new YAHOO.widget.TabView('wizard_container');
     
    }
    
	  function cancelImportManager() {
	      <digi:context name="url" property="/aim/admin.do" />
	      window.location="<%= url %>";
	  }

       function importActivities(){
            var form = document.getElementById('form');
            form.action = "/dataExchange/createSource.do?saveImport=true";
            form.target="_self"
            form.submit();
      }
           
		YAHOOAmp.util.Event.addListener(window, "load", tabsInit) ;
</script>

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="90%" class="box-border-nopadding">
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
		
		<div style="">
			<c:set var="translation">
				<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
			</c:set>
			<digi:link href="/admin.do" styleClass="comment" title="${translation}" contextPath="/aim">
			<digi:trn key="aim:AmpAdminHome">
			Admin Home
			</digi:trn>
			</digi:link>&nbsp;&gt;&nbsp;
			<digi:link href="/manageSource.do?htmlView=true" styleClass="comment">
			<digi:trn>
			Source Manager
			</digi:trn>
			</digi:link>&nbsp;&gt;&nbsp;
			
			<digi:trn>
				Source Wizard
			</digi:trn>
		</div>

<br />
<table cellPadding=0 cellSpacing=0 style="width: 84%; background-color: #ffffff">
	<tr>
		<td valign="bottom">
				<digi:errors/>
		</td>
	</tr>
	<tr>
		<td align="left" vAlign="top">
		<digi:instance property="createSourceForm" />
		<digi:form action="/createSource.do" method="post" styleId="form">
		<span id="formChild" style="display:none;">&nbsp;</span>
     
        <span class="subtitle-blue">
          &nbsp;<digi:trn>Data Importer</digi:trn>
        </span>		
		
		<div style="color: red; text-align: center; visibility: hidden" id="savingReportDiv">

		</div>
		<br />
		<bean:define id="fileUploaded" value="false"/>

		<logic:equal name="DEfileUploaded" scope="session" value="true">
			<bean:define id="fileUploaded" value="true"/>
		</logic:equal>
		
		<div id="wizard_container" class="yui-navset">
    		<ul class="yui-nav">
	    				<li id="tab_file_selection" class="selected"><a href="#file_selection"><div>1. <digi:trn>General Details</digi:trn></div></a> </li>
		    			<li id="tab_log_after_import"><a href="#log_after_import"><div>2. <digi:trn> Field Selection</digi:trn></div></a> </li>
		    			<li id="tab_select_activities"><a href="#select_activities"><div>3. <digi:trn>Filter and Identifier</digi:trn></div></a> </li>
		    			<li id="tab_confirm_import" class="disabled"><a href="#confirm_import"><div>4. <digi:trn>Type Specific Settings</digi:trn></div></a> </li>
					
					    		
    		</ul>
			<div class="yui-content" style="background-color: #EEEEEE">
				<div id="tab_file_selection" class="yui-tab-content" style="padding: 0px 0px 1px 0px;" >
                    <c:set var="stepNum" value="0" scope="request" />
                    <jsp:include page="newToolbarImport.jsp" />
					<div style="height: 255px;">
    					<table cellpadding="5px" width="80%">
    						<tr>
        						<td width="47%" align="left" valign="top"><br/><br />
        							Name: <html:text property="name" size="20"/>
        							<br /><br /> 
        							
        							Please choose the type of source: </br> 
  								    <logic:iterate name="createSourceForm" property="sourceValues" id="srcVal">
  								    			<br />
        										<html:radio property="source" value="${srcVal.key}">
		        									<digi:trn>${srcVal.value}</digi:trn>
		        								</html:radio>
        							</logic:iterate> 
        							
        							<br /><br /> 
        							<digi:trn>Please choose the workspace that will be used</digi:trn> <br />
        							<html:select property="teamId" style="width: 260px;">
        								<html:optionsCollection property="teamValues" label="name" value="ampTeamId"/>
        							</html:select>  																		
        						</td>
        						<td align="left">
        							<br/><br/><br/>
        							<digi:trn key="aim:pleaseChooseTheLanguage">Please choose the language(s) that exist in imported file
        								</digi:trn><br/>
        							<table width="70%">
        							<logic:iterate name="createSourceForm" property="languages" id="lang">
        								<tr><td>
        								<html:multibox property="selectedLanguages" >
        									<bean:write name="lang"/>
        								</html:multibox>
        								<bean:write name="lang"/>&nbsp;&nbsp;&nbsp;
        								</td></tr>
        							</logic:iterate>
        							</table>
        							<br/><br/>
        							<digi:trn>Please choose import strategy</digi:trn>
        							<br/>
        							<table  width="70%">
        							<logic:iterate name="createSourceForm" property="importStrategyValues" id="impVal">
        								<tr>
        									<td>
		        								<html:radio property="importStrategy" value="${impVal.key}">
		        									<digi:trn>${impVal.value}</digi:trn>
		        								</html:radio>
        									</td>
        								</tr>
        							</logic:iterate>
        							</table>
        						</td>
    						</tr>
    					</table>
					</div>
				</div>
               
				<div id="tab_log_after_import"  class="yui-tab-content" align="center" style="padding: 0px 0px 1px 0px; display: none;">
                    <c:set var="stepNum" value="1" scope="request" />
                    <jsp:include page="newToolbarImport.jsp" />
	                    <div  style="width: 100%;height:250px;overflow:auto;margin-left: auto;margin-right: auto;" id="wrapperDivForTree">
	                    	<bean:define id="fieldsModuleTree" name="createSourceForm" property="activityTree" toScope="request" />
	                     	<jsp:include page="fieldsModule.jsp"></jsp:include>
	                    </div>
				</div>
				
				<div id="tab_select_activities"  class="yui-tab-content" align="center" style="padding: 0px 0px 1px 0px; display: none;">
                    <c:set var="stepNum" value="2" scope="request" />
                    <jsp:include page="newToolbarImport.jsp" />
                    <br />
                    <table>
                    	<tr>
                    		<td>
                    			<digi:trn>Type unique identifier (title,id,ampid,ptip) separatd by '|' </digi:trn>: <br/>
                    			<html:text property="uniqueIdentifier"></html:text>
                    			<br />
                    			<br />
                    			<logic:notEmpty name="createSourceForm" property="approvalStatusValues" >
                    			<digi:trn>Select the approval status that the new activities will have</digi:trn>: <br />
                    				<div style="margin-left: auto; margin-right: auto; width: 60%;">
                    				<logic:iterate id="appStatus" name="createSourceForm" property="approvalStatusValues">
		                    			<html:radio property="approvalStatus" value="${appStatus.key}">${appStatus.value}</html:radio> <br />
                    				</logic:iterate>
                    				</div>
                    			</logic:notEmpty>
                    		</td>
                    		<td>
                    			
                    		</td>
                    	</tr>
                    </table>
				</div>
				<div id="tab_confirm_import"  class="yui-tab-content" align="center" style="padding: 0px 0px 1px 0px; display: none;">
                    <c:set var="stepNum" value="3" scope="request" />
                    <jsp:include page="newToolbarImport.jsp" />
                    Step 4 Select additional fields
				</div>
				
			</div>
		</div>

		</digi:form>
	</td>
	</tr>
</table>


		</td>
	</tr>
</table>
	