<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@page import="org.digijava.module.dataExchange.util.ExportHelper"%>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>



<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/yahoo-dom-event.js"></script>

<script type="text/javascript">
  var tree;
  
</script>

    <link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/treeview.css" />
    <link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/fonts-min.css" />
    <link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/tabview.css" />

    <script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/logger-min.js"></script>
    <script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/treeview-min.js"></script>
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

	<br>
	<br>
<digi:instance property="deImportForm" />
  
	<script type="text/javascript">
	YAHOO.namespace("YAHOO.amp.dataExchangeImport");
	YAHOO.amp.dataExchangeImport.numOfSteps	= 4;
		
	YAHOO.amp.dataExchangeImport.tabLabels	= new Array("tab_file_selection", "tab_log_after_import", "tab_select_activities", "tab_confirm_import");
		
        function navigateTab(value){
        	YAHOO.amp.dataExchangeImport.tabView.set("activeIndex", YAHOO.amp.dataExchangeImport.tabView.get("activeIndex")+value);
        }
		
		
		function initializeDragAndDrop() {
			var height			= Math.round(YAHOO.util.Dom.getDocumentHeight() / 2.3);
			
			YAHOO.amp.dataExchangeImport.tabView 		= new YAHOO.widget.TabView('wizard_container');
			YAHOO.amp.dataExchangeImport.tabView.addListener("contentReady", treeInit);
		}

/*
    function treeInit() {
      YAHOO.amp.dataExchangeImport.tabView     = new YAHOO.widget.TabView('wizard_container');
     
    }
  */  
	  function cancelImportManager() {
	      <digi:context name="url" property="/aim/admin.do" />
	      window.location="<%= url %>";
	  }

       function importActivities(){
            var form = document.getElementById('form');
            form.action = "/dataExchange/import.do~loadFile=true";
            form.target="_self"
            form.submit();
      }


       function treeInit() {
         tabView = new YAHOO.widget.TabView('wizard_container');
         buildRandomTaskNodeTree();
       }
       
       //handler for expanding all nodes
       YAHOO.util.Event.on("expand", "click", function(e) {
         tree.expandAll();
         YAHOO.util.Event.preventDefault(e);
       });
       
       //handler for collapsing all nodes
       YAHOO.util.Event.on("collapse", "click", function(e) {
         tree.collapseAll();
         YAHOO.util.Event.preventDefault(e);
       });

       //handler for checking all nodes
       YAHOO.util.Event.on("check", "click", function(e) {
         checkAll();
         YAHOO.util.Event.preventDefault(e);
       });
       
       //handler for unchecking all nodes
       YAHOO.util.Event.on("uncheck", "click", function(e) {
         uncheckAll();
         YAHOO.util.Event.preventDefault(e);
       });


       YAHOO.util.Event.on("getchecked", "click", function(e) {
         YAHOO.util.Event.preventDefault(e);
       });

       //Function  creates the tree and 
       //builds between 3 and 7 children of the root node:
         function buildRandomTaskNodeTree() {
       
         //instantiate the tree:
             tree = new YAHOO.widget.TreeView("dataImportActivityTree");
               
             <bean:define id="tree" name="deImportForm" property="activityStructure" type="org.digijava.module.dataExchange.type.AmpColumnEntry" toScope="page"/>
             <%= ExportHelper.renderActivityTree(tree, request) %>

         //The tree is not created in the DOM until this method is called:
             tree.draw();
             
         }

//       var callback = null;

       function buildRandomTaskBranch(node) {
         if (node.depth < 3) {
//           YAHOO.log("buildRandomTextBranch: " + node.index);
           for ( var i = 0; i < Math.floor(Math.random() * 2) ; i++ ) {
             var tmpNode = new YAHOO.widget.TaskNode(node.label + "-" + i, node, false);
                     //tmpNode.onCheckClick = onCheckClick;
             buildRandomTaskBranch(tmpNode);
           }
         }
       }

         function checkAll() {
             var topNodes = tree.getRoot().children;
             for(var i=0; i<topNodes.length; ++i) {
                 topNodes[i].checkAll();
             }
         }

         function uncheckAll() {
             var topNodes = tree.getRoot().children;
             for(var i=0; i<topNodes.length; ++i) {
                 topNodes[i].unCheckAll();
             }
         }

         // Gets the labels of all of the fully checked nodes
         // Could be updated to only return checked leaf nodes by evaluating
         // the children collection first.
          function getCheckedNodes(nodes) {
              nodes = nodes || tree.getRoot().children;
              checkedNodes = [];
              for(var i=0, l=nodes.length; i<l; i=i+1) {
                  var n = nodes[i];
                  //if (n.checkState > 0) { // if we were interested in the nodes that have some but not all children checked
                  if (n.checkState === 2) {
                      checkedNodes.push(n); // just using label for simplicity
                      alert(n.aid+' '+n.label);
                      if (n.hasChildren()) {
                          checkedNodes = checkedNodes.concat(getCheckedNodes(n.children));
                       }
                  }
              }

              return checkedNodes;
          }  

       
           
		YAHOO.util.Event.addListener(window, "load", treeInit) ;
</script>

<script type="text/javascript">
	var W3CDOM = (document.createElement && document.getElementsByTagName);

	function initFileUploads() {
		if (!W3CDOM) return;
		var fakeFileUpload = document.createElement('div');
		fakeFileUpload.className = 'fakefile';
		fakeFileUpload.appendChild(document.createElement('input'));

		var fakeFileUpload2 = document.createElement('div');
		fakeFileUpload2.className = 'fakefile2';


		var button = document.createElement('input');
		button.type = 'button';

		button.value = '<digi:trn>Browse...</digi:trn>';
		fakeFileUpload2.appendChild(button);

		fakeFileUpload.appendChild(fakeFileUpload2);
		var x = document.getElementsByTagName('input');
		for (var i=0;i<x.length;i++) {
			if (x[i].type != 'file') continue;
			if (x[i].parentNode.className != 'fileinputs') continue;
			x[i].className = 'file hidden';
			var clone = fakeFileUpload.cloneNode(true);
			x[i].parentNode.appendChild(clone);
			x[i].relatedElement = clone.getElementsByTagName('input')[0];

 			x[i].onchange = x[i].onmouseout = function () {
				this.relatedElement.value = this.value;
			}
		}
	}

</script>


<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="65%">
	<tr>
		<td valign="bottom">
				<digi:errors/>
		</td>
	</tr>
	<tr>
		<td align="left" vAlign="top">
		
		<digi:form action="/import.do" method="post" enctype="multipart/form-data" styleId="form">
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
					<logic:equal name="fileUploaded" value="false">
	    				<li id="tab_file_selection" class="selected"><a href="#file_selection"><div>1. <digi:trn>File Selection and Staging Area</digi:trn></div></a> </li>
		    			<li id="tab_log_after_import" class="disabled"><a href="#log_after_import"><div>2. <digi:trn> Log after Import</digi:trn></div></a> </li>
		    			<li id="tab_select_activities" class="disabled"><a href="#select_activities"><div>3. <digi:trn>Select Activities</digi:trn></div></a> </li>
		    			<li id="tab_confirm_import" class="disabled"><a href="#confirm_import"><div>4. <digi:trn>Confirm Import</digi:trn></div></a> </li>
					</logic:equal>
					
					<logic:equal name="fileUploaded" value="true">
	    				<li id="tab_file_selection" class="disabled"><a href="#file_selection"><div>1. <digi:trn>File Selection and Staging Area</digi:trn></div></a> </li>
		    			<li id="tab_log_after_import" class="selected"><a href="#log_after_import"><div>2. <digi:trn> Log after Import</digi:trn></div></a> </li>
		    			<li id="tab_select_activities" class="enabled"><a href="#select_activities"><div>3. <digi:trn>Select Activities</digi:trn></div></a> </li>
		    			<li id="tab_confirm_import" class="enabled"><a href="#confirm_import"><div>4. <digi:trn>Confirm Import</digi:trn></div></a> </li>
					</logic:equal>    		
					
					    		
    		</ul>
			<div class="yui-content" style="background-color: #EEEEEE">
				<div id="tab_file_selection" class="yui-tab-content" style="padding: 0px 0px 1px 0px;" >
                    <c:set var="stepNum" value="0" scope="request" />
                    <jsp:include page="toolbarImport.jsp" />
					<div style="height: 500px;">
    					<table cellpadding="5px" width="100%">
    						<tr>
    							<td width="50%" valign="top">
    							<div style="border: 1px grey solid; padding:5px">
								<div >							
        									<digi:trn key="aim:pleaseChooseTheFile">Please choose the file you want to import
	        								</digi:trn><br/>
	        								<div class="fileinputs">  <!-- We must use this trick so we can translate the Browse button. AMP-1786 -->
												<input id="uploadedFile" name="uploadedFile" type="file" class="file">												
											</div>  
	        									<br/>
	        								<digi:trn key="aim:pleaseChooseTheOption">Please choose the option for import the activities
	        								</digi:trn><br/>
        						</div>
        						<div>
        							<logic:iterate name="deImportForm" property="options" id="option">
        								<bean:define id="optionValue">
                          					<bean:write name="option"/>
            							</bean:define>
        								<html:radio property="selectedOptions" value="<%=optionValue%>" styleId="<%=optionValue%>" />
        								<bean:write name="option"/> <digi:trn>Activity</digi:trn><br/>
        							</logic:iterate>
    							</div>
    							</div>
    							</td>
    							
    							<td  width="50%" valign="top" >
    							<div style="border: 1px grey solid; padding:5px">
		    							<digi:trn key="aim:pleaseChooseKeysForImport"> Please choose the primary keys for import </digi:trn>: <br/>
        									<html:checkbox property="primaryKeys" name="deImportForm" value="title">
        										<digi:trn key="aimm:Title">Title</digi:trn>
        									</html:checkbox><br/>
        									<html:checkbox property="primaryKeys" name="deImportForm" value="budgetCode">
        										<digi:trn key="aimm:BudgetCode">Budget Code</digi:trn>
        									</html:checkbox><br/>
        									<html:checkbox property="primaryKeys" name="deImportForm" value="projectId">
        										<digi:trn key="aimm:projectID">Project ID</digi:trn>
        									</html:checkbox><br/><br/><br/><br/>
        									
        						</div>
    							</td>
    						</tr>
    						<tr>
        						<td width="47%" align="left" valign="top" colspan="2"> 
        						
        							<div id="expandcontractdiv" align="left" style="width:85%">
					                    <a id="expand" href="#"><digi:trn>Expand all</digi:trn></a>
					                    <a id="collapse" href="#"><digi:trn>Collapse all</digi:trn></a>
        							
					                    <a id="check" href="#"><digi:trn>Check all</digi:trn></a>
					                    <a id="uncheck" href="#"><digi:trn>Uncheck all</digi:trn></a>
			                    	</div>
        						<div id="source_col_div" class="draglist" style="border-width: 0px; width: 100%; height: 300px;">
			                    	<div id="dataImportActivityTree"></div>
			                    </div>	      																		
        						</td>
    						</tr>
    					</table>
					</div>
				</div>
               
				<div id="tab_log_after_import"  class="yui-hidden" align="center" style="padding: 0px 0px 1px 0px;">
                    <c:set var="stepNum" value="1" scope="request" />
                    <jsp:include page="toolbarImport.jsp" />
                    <div  style="width:100%;height:250px;overflow:auto;text-align: left">
                    	<logic:notEqual name="DELogGenerated" scope="session" value="false">
							<%= session.getAttribute("DELogGenerated") %>
						</logic:notEqual>
                     
                    </div>
				</div>
				
				<div id="tab_select_activities"  class="yui-hidden" align="center" style="padding: 0px 0px 1px 0px;">
                    <c:set var="stepNum" value="2" scope="request" />
                    <jsp:include page="toolbarImport.jsp" />
                   <div id="dataImportTree"></div>
				</div>
				<div id="tab_confirm_import"  class="yui-hidden" align="center" style="padding: 0px 0px 1px 0px; ">
                    <c:set var="stepNum" value="3" scope="request" />
                    <jsp:include page="toolbarImport.jsp" />
                    <digi:trn>Step 4 Select additional fields</digi:trn>
				</div>
				
			</div>
		</div>
<%= ExportHelper.renderHiddenElements(tree) %>
		</digi:form>
	</td>
	</tr>
</table>
<script type="text/javascript">
	initFileUploads();
</script>
	