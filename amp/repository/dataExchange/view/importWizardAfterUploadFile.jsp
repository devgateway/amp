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


<%@page import="org.digijava.module.dataExchange.utils.DataExchangeUtils;"%>

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
	
    <script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/logger-min.js"></script>
      <script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/new/treeview-min.js"></script>
    <script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/tabview-min.js"></script>

    <script type="text/javascript" src="/repository/dataExchange/view/scripts/TaskNodeImport.js"></script>

<style type="text/css">

.ygtvcheck0 { background: url(/TEMPLATE/ampTemplate/images/yui/check0.gif) 0 0 no-repeat; width:16px; cursor:pointer }
.ygtvcheck1 { background: url(/TEMPLATE/ampTemplate/images/yui/check1.gif) 0 0 no-repeat; width:16px; cursor:pointer }
.ygtvcheck2 { background: url(/TEMPLATE/ampTemplate/images/yui/check2.gif) 0 0 no-repeat; width:16px; cursor:pointer }

#expandcontractdiv {border:1px solid #336600; background-color:#FFFFCC; margin:0 0 .5em 0; padding:0.2em;}
#treeDiv1 { background: #fff }
</style> 


	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/tab/assets/tabview.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/border_tabs.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/reportWizard/reportWizard.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/filters.css'/>">

	<br>
	<br>

  <digi:instance property="deImportForm" />
	<script type="text/javascript">
	YAHOOAmp.namespace("YAHOOAmp.amp.dataExchangeImport");
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


    function treeInit() {
      YAHOOAmp.amp.dataExchangeImport.tabView     = new YAHOO.widget.TabView('wizard_container');
      buildRandomTaskNodeTree();
    }
    
    //handler for expanding all nodes
    YAHOOAmp.util.Event.on("expand", "click", function(e) {
      tree.expandAll();
      YAHOOAmp.util.Event.preventDefault(e);
    });
    
    //handler for collapsing all nodes
    YAHOOAmp.util.Event.on("collapse", "click", function(e) {
      tree.collapseAll();
      YAHOOAmp.util.Event.preventDefault(e);
    });

    //handler for checking all nodes
    YAHOOAmp.util.Event.on("check", "click", function(e) {
      checkAll();
      YAHOOAmp.util.Event.preventDefault(e);
    });
    
    //handler for unchecking all nodes
    YAHOOAmp.util.Event.on("uncheck", "click", function(e) {
      uncheckAll();
      YAHOOAmp.util.Event.preventDefault(e);
    });


    YAHOOAmp.util.Event.on("getchecked", "click", function(e) {
      YAHOOAmp.util.Event.preventDefault(e);
    });

    //Function  creates the tree and 
    //builds between 3 and 7 children of the root node:
      function buildRandomTaskNodeTree() {
    
      //instantiate the tree:
          tree = new YAHOOAmp.widget.TreeView("dataImportTree");
          
          
          <bean:define id="tree" name="deImportForm" property="activityTree" type="org.digijava.module.dataExchange.dbentity.AmpDEImportLog" toScope="page"/>
          <%= DataExchangeUtils.renderActivityTree(tree) %>
          
      //The tree is not created in the DOM until this method is called:
          tree.draw();
          //alert(tree.getNodeCount());
          
           //x = tree.getNodesByProperty("checkState", "true");
           //for(i=0; i<x.length;i++){
           //	alert(x.getNodeByIndex(i).getCheckStyle());
           //}
          //alert(tree.getNodeByIndex(2).getCheckStyle());
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

	  function cancelImportManager() {
	      <digi:context name="url" property="/aim/admin.do" />
	      window.location="<%= url %>";
	  }
	  
       function importActivities(){
       
       		var mylist=document.getElementById("id_activities");
			var listitems= mylist.getElementsByTagName("li");
			if(listitems == null || listitems.length < 1)
			{
				alert("Please choose at least one activity to import");
				return;
			}
       		
            var form = document.getElementById('form');
            form.action = "/dataExchange/import.do~saveImport=true";
            form.target="_self"
            form.submit();
      }
           
		YAHOOAmp.util.Event.addListener(window, "load", treeInit) ;
	</script>


<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="65%">
	<tr>
		<td valign="bottom">
				
		</td>
	</tr>
	<tr>
		<td align="left" vAlign="top">
		
		<digi:form action="/import.do" method="post" enctype="multipart/form-data" styleId="form">
		<span id="formChild" style="display:none;">&nbsp;</span>
     
        <span class="subtitle-blue">
          &nbsp;Data Importer
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
	    				<li id="tab_file_selection" class="selected"><a href="#file_selection"><div>1. File Selection and Staging Area</div></a> </li>
		    			<li id="tab_log_after_import" class="disabled"><a href="#log_after_import"><div>2. Log after Import</div></a> </li>
		    			<li id="tab_select_activities" class="enabled"><a href="#select_activities"><div>3. Select Activities</div></a> </li>
		    			<li id="tab_confirm_import" class="disabled"><a href="#confirm_import"><div>4. Confirm Import</div></a> </li>
					</logic:equal>
					
					<logic:equal name="fileUploaded" value="true">
	    				<li id="tab_file_selection" class="disabled"><a href="#file_selection"><div>1. File Selection and Staging Area</div></a> </li>
		    			<li id="tab_log_after_import" class="selected"><a href="#log_after_import"><div>2. Log after Import</div></a> </li>
		    			<li id="tab_select_activities" class="enabled"><a href="#select_activities"><div>3. Select Activities</div></a> </li>
		    			<li id="tab_confirm_import" class="enabled"><a href="#confirm_import"><div>4. Confirm Import</div></a> </li>
					</logic:equal>    		
					
					    		
    		</ul>
			<div class="yui-content" style="background-color: #EEEEEE">
				<div id="tab_file_selection" class="yui-tab-content" style="padding: 0px 0px 1px 0px;" >
                    <c:set var="stepNum" value="0" scope="request" />
                    <jsp:include page="toolbarImport.jsp" />
					<div style="height: 255px;">
    					<table cellpadding="5px" style="vertical-align: middle" width="70%">
    						<tr>
        						<td width="47%" align="center"><br/><br/><br/><br/><br/>
        								<digi:trn key="aim:pleaseChooseTheFile">Please choose the file you want to import
        								</digi:trn><br/><br/><br/>
										<input id="uploadedFile" name="uploadedFile" type="file" class="file">
										<br/><br/>
										<c:set var="translation">
								             <digi:trn key="btn:Import">Import</digi:trn>
								        </c:set>
								         
        						</td>
        						<td align="center">
        							<br/><br/><br/>
        							<digi:trn key="aim:pleaseChooseTheLanguage">Please choose the language(s) that exist in imported file
        								</digi:trn><br/>
        							<logic:iterate name="deImportForm" property="languages" id="lang">
        								<html:multibox property="selectedLanguages">
        									<bean:write name="lang"/>
        								</html:multibox>
        								<bean:write name="lang"/>&nbsp;&nbsp;&nbsp;
        							</logic:iterate>
        							<br/><br/><br/>
        							<digi:trn key="aim:pleaseChooseTheOption">Please choose the option for import the activities
        								</digi:trn><br/>
        							<logic:iterate name="deImportForm" property="options" id="option">
        							<div>
        								<bean:define id="optionValue">
                          					<bean:write name="option"/>
            							</bean:define>
        								<html:radio property="selectedOptions" value="<%=optionValue%>" styleId="<%=optionValue%>" />
        								<bean:write name="option"/> Activity<br/>
        							</div>
        							</logic:iterate>
        						</td>
    						</tr>
    					</table>
					</div>
				</div>
               
				<div id="tab_log_after_import"  class="yui-tab-content" align="center" style="padding: 0px 0px 1px 0px; display: none;">
                    <c:set var="stepNum" value="1" scope="request" />
                    <jsp:include page="toolbarImport.jsp" />
                    <div  style="width:100%;height:250px;overflow:auto;text-align: left">
                    	<logic:notEqual name="DELogGenerated" scope="session" value="false">
							<%= session.getAttribute("DELogGenerated") %>
						</logic:notEqual>
                     
                    </div>
				</div>
				
				<div id="tab_select_activities"  class="yui-tab-content" align="left" style="padding: 0px 0px 1px 0px; display: none;">
                    <c:set var="stepNum" value="2" scope="request" />
                    <jsp:include page="toolbarImport.jsp" />
                    
                    <div id="expandcontractdiv" align="left">
	                    <a id="expand" href="#">Expand all</a>
	                    <a id="collapse" href="#">Collapse all</a>
	                  
	                    <a id="check" href="#">Check all</a>
	                    <a id="uncheck" href="#">Uncheck all</a>
                    </div>
                    
                   <div id="dataImportTree"></div>
				</div>
				<div id="tab_confirm_import"  class="yui-tab-content" align="left" style="padding: 0px 0px 1px 0px; display: none;">
                    <c:set var="stepNum" value="3" scope="request" />
                    <jsp:include page="toolbarImport.jsp" />
                    Step 4 Select additional fields
                    <div align="left">
					<ul id="id_activities" >
					</ul>
					</div>
				</div>
				
			</div>
		</div>
		<%= DataExchangeUtils.renderHiddenElements(tree) %>
		
		

		</digi:form>
	</td>
	</tr>
</table>

	