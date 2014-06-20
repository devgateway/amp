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

<%@page import="org.digijava.module.dataExchange.util.ExportHelper"%>


<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event/yahoo-dom-event.js"></script>

<script type="text/javascript">
  if (YAHOOAmp != null){
    var YAHOO = YAHOOAmp;
  }
  var tree;
  
</script>

    <link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/treeview.css" />
    <link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/fonts-min.css" />
    <link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/tabview.css" />
    <link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/styles.css" />

    <script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/logger/logger-min.js"></script>
    <script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/treeview/treeview-min.js"></script>
    <script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script>

    <script type="text/javascript" src="/repository/dataExchange/view/scripts/TaskNode.js"></script>

<style type="text/css">

.ygtvcheck0 { background: url(/TEMPLATE/ampTemplate/images/yui/check0.gif) 0 0 no-repeat; width:16px; cursor:pointer }
.ygtvcheck1 { background: url(/TEMPLATE/ampTemplate/images/yui/check1.gif) 0 0 no-repeat; width:16px; cursor:pointer }
.ygtvcheck2 { background: url(/TEMPLATE/ampTemplate/images/yui/check2.gif) 0 0 no-repeat; width:16px; cursor:pointer }

#expandcontractdiv {background-color:#FFFFCC; margin:0 0 .5em 0; padding:0.2em;}
#treeDiv1 { background: #fff }
</style> 


	
<!--
  <script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/myDragAndDropObjects.js'/>" >.</script>
    <script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/reportManager.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/fundingGroups.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/saving.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/prefilters.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/filters/filters.js'/>" ></script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src='script/tooltip/wz_tooltip.js'/>" > .</script>
-->
	
	<link rel="stylesheet" type="text/css" href="<digi:file src='/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/tabview.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/border_tabs.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/reportWizard/reportWizard.css'/>">
	<link rel="stylesheet" type="text/css" href="/repository/aim/view/css/filters/filters2.css"/>
	
	<br>
	<br>

    <digi:instance property="deExportForm" />


  
	<script type="text/javascript">
    var tabView = null;
	  
	YAHOOAmp.namespace("amp.dataExchange");
	YAHOOAmp.amp.dataExchange.numOfSteps	= 2; // have to be 3 when we include additional fields
		
	YAHOOAmp.amp.dataExchange.tabLabels	= new Array("tab_select_filed", "tab_additional_filed", "tab_filter");
		
        function navigateTab(value){
        	tabView.set("activeIndex", tabView.get("activeIndex")+value);
        }
		
		
		function initializeDragAndDrop() {
			var height			= Math.round(YAHOO.util.Dom.getDocumentHeight() / 2.3);
			
			YAHOOAmp.amp.dataExchange.tabView 		= new YAHOO.widget.TabView('wizard_container');
			YAHOOAmp.amp.dataExchange.tabView.addListener("contentReady", treeInit);
		}


    function treeInit() {
      tabView = new YAHOO.widget.TabView('wizard_container');
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
          tree = new YAHOOAmp.widget.TreeView("dataExportTree");
            
          <bean:define id="tree" name="deExportForm" property="activityTree" type="org.digijava.module.dataExchange.type.AmpColumnEntry" toScope="page"/>
          <%= ExportHelper.renderActivityTree(tree, request) %>

      //The tree is not created in the DOM until this method is called:
          tree.draw();
          cancelFilter();
      }

//    var callback = null;

    function buildRandomTaskBranch(node) {
      if (node.depth < 3) {
//        YAHOOAmp.log("buildRandomTextBranch: " + node.index);
        for ( var i = 0; i < Math.floor(Math.random() * 2) ; i++ ) {
          var tmpNode = new YAHOOAmp.widget.TaskNode(node.label + "-" + i, node, false);
                  //tmpNode.onCheckClick = onCheckClick;
          buildRandomTaskBranch(tmpNode);
        }
      }
    }

//      function onCheckClick(node) {
//          YAHOOAmp.log(node.label + " check was clicked, new state: " + node.checkState, "info", "example");
//      }

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


       function exportActivity(){
                    //        getCheckedNodes();
                    var selTeamId = document.getElementById('teamId');

                    if (selTeamId.value != "-1"){
                      var form = document.getElementById('form');
            form.action = "/dataExchange/exportWizard.do?method=export";
            form.target="_self"
            form.submit();
            window.setTimeout("enableLogButton()",5000,"JavaScript");
                  } else {
                      alert('please select one team');
                  }
              }


      function exportLog(){
          var form = document.getElementById('form');
          form.action = "/dataExchange/exportWizard.do?method=log";
          form.target="_self"
          form.submit();
          disableLogButton();
      }
      
       
      function cancelBack(){
        window.location = "/aim/admin.do";
      }
       
      function cancelFilter(){
          document.getElementById('donorTypeId').selectedIndex=-1;
          document.getElementById('donorGroupId').selectedIndex=-1;
          document.getElementById('donorAgencyId').selectedIndex=-1;
          document.getElementById('primarySectorsId').selectedIndex=-1;
          document.getElementById('secondarySectorsId').selectedIndex=-1;
          document.getElementById('teamId').selectedIndex=0;
          disableExportButton();
          disableLogButton();
      }

      function changeTeam(){
    	  var selTeamId = document.getElementById('teamId');

          if (selTeamId.value != "-1"){
        	  enableExportButton();
          } else {
        	  disableExportButton();
        	  disableLogButton();
          }             
      }

      function enableToolbarButton(btn) {
          if ( btn.disabled ) {
            var imgEl   = btn.getElementsByTagName("img")[0];
            var imgSrc    = imgEl.src.replace("_dis.png", ".png");
            
            imgEl.src   = imgSrc;
            btn.disabled  = false;
            ( new YAHOOAmp.util.Element(btn) ).replaceClass('toolbar-dis', 'toolbar');
          }
        }

        function disableToolbarButton(btn) {
          if ( !btn.disabled  ) {
              
            var imgEl   = btn.getElementsByTagName("img")[0];
            var imgSrc    = imgEl.src.replace(".png", "_dis.png");
            imgEl.src   = imgSrc;
            
            btn.disabled  = true;
            ( new YAHOOAmp.util.Element(btn) ).replaceClass('toolbar', 'toolbar-dis');
          }
        }

        function enableExportButton(){
            var exportButton = document.getElementsByName('expodtButton');
            for(var i = 0; i < exportButton.length; i++){
              enableToolbarButton(exportButton[i]);
            }    
        }    

        function disableExportButton(){
            var exportButton = document.getElementsByName('expodtButton');
            for(var i = 0; i < exportButton.length; i++){
              disableToolbarButton(exportButton[i]);
            }    
        } 


        function enableLogButton(){
            var exportButton = document.getElementsByName('logButton');
            for(var i = 0; i < exportButton.length; i++){
              enableToolbarButton(exportButton[i]);
            }    
        }      
        function disableLogButton(){
            var exportButton = document.getElementsByName('logButton');
            for(var i = 0; i < exportButton.length; i++){
              disableToolbarButton(exportButton[i]);
            }    
        }          
		YAHOOAmp.util.Event.addListener(window, "load", treeInit) ;
	</script>


<table bgColor=#ffffff cellpadding="1"0 cellspacing="0" width="100%">
	<tr>
		<td valign="bottom">
				
		</td>
	</tr>
	<tr>
		<td align="left" vAlign="top">
		<digi:form action="/exportWizard.do?method=export" method="post" styleId="form">
		<span id="formChild" style="display:none;">&nbsp;</span>

        <h1 class="admintitle" style="text-align:left;">
          &nbsp;<digi:trn>Data Exporter</digi:trn>
        </h1>		
		
		<div style="color: red; text-align: center; visibility: hidden" id="savingReportDiv">

		</div>
		<br />
		<div id="wizard_container" class="yui-navset">
    		<ul class="yui-nav">
    			<li id="tab_select_filed" class="selected"><a href="#type_step_div"><div><digi:trn>Field Selection</digi:trn></div></a> </li>
<%--
     			<li id="tab_additional_filed" class="enabled"><a href="#columns_step_div"><div>Additional Fields</div></a> </li>
--%>
    			<li id="hierachies_tab_label" class="enabled"><a href="#hierarchies_step_div"><div><digi:trn>Team Selection and Filters</digi:trn></div></a> </li>
    		</ul>
			<div class="yui-content" style="background-color: #EEEEEE">
				<div id="tab_select_filed" class="yui-tab-content" style="padding: 0px 0px 1px 0px;" >
                    <c:set var="stepNum" value="0" scope="request" />
                    <jsp:include page="toolbar.jsp" />
                    <div>
                    	<span class="list_header">
                    		<digi:trn key="rep:wizard:supportedSchema">Supported IATI Schema</digi:trn>
                    	</span>
                    	<div>
           	        		<html:select name="deExportForm" property="iatiVersion" styleClass="inp-text"  
   								style="width: 300px;" onchange="">
                              <c:forEach var="fVar" items="${deExportForm.iatiVersionList}" varStatus="lStatus">
                                <option value="${fVar.value}">${fVar.value}</option>
                              </c:forEach>
                            </html:select>
                    	
                    	</div>
                    </div>
					<div style="height: 355px; padding-bottom: 40px;">
    					<table cellpadding="5px" style="vertical-align: middle" width="100%">
    						<tr>
        						<td width="47%" align="center">
        							<span class="list_header">
        								<digi:trn key="rep:wizard:availableColumns">Available Fields</digi:trn>
        							</span>
                      <div style="text-align: left; width: 87.5% ">
                        <div id="expandcontractdiv" style="margin-bottom: 0px;" >
                          <a id="expand" href="#"><digi:trn>Expand all</digi:trn></a>
                          <a id="collapse" href="#"><digi:trn>Collapse all</digi:trn></a>
                        
                          <a id="check" href="#"><digi:trn>Check all</digi:trn></a>
                          <a id="uncheck" href="#"><digi:trn>Uncheck all</digi:trn></a>
                        </div>
                      </div>
                      <div id="source_col_div" class="draglist" style="border-width: 0px;">
        <%--  								<jsp:include page="setColumns.jsp" />    --%>
                  <div id="dataExportTree"></div>
                  
        
        							</div>
        						</td>
    						</tr>
    					</table>
					</div>
				</div>
<%--   temporoary removed.      
				<div id="tab_additional_filed"  class="yui-tab-content" align="center" style="padding: 0px 0px 1px 0px; display: none;">
                    <c:set var="stepNum" value="1" scope="request" />
                    <jsp:include page="toolbar.jsp" />
                    Select additional fields
				</div>
--%>
				<div id="tab_filter"  class="yui-tab-content"  style="padding: 0px 0px 1px 0px; ">
                    <c:set var="stepNum" value="2" scope="request" />
                    <jsp:include page="toolbar.jsp" />
                    <table cellpadding="15px" width="100%" align="center" border="0" >
                      <tr>
                        <td width="46%" style="vertical-align: top;">
	                       <span class="list_header"><digi:trn>Donors</digi:trn></span>
                           <br/>
                             <span><digi:trn>Donor Type</digi:trn></span>
                             <br/>
                             <html:select name="deExportForm" property="donorTypeSelected" styleClass="inp-text" styleId="donorTypeId" style="width: 300px;" multiple="true" size="3">
                               <c:forEach var="fVar" items="${deExportForm.donorTypeList}" varStatus="lStatus">
                                 <option value="${fVar.ampOrgTypeId}">${fVar.orgType}</option>
                               </c:forEach>
                             </html:select>
                             <br/>
                             <span ><digi:trn>Donor Group</digi:trn></span>
                             <br/>
                             <html:select name="deExportForm" property="donorGroupSelected" styleClass="inp-text"  styleId="donorGroupId" style="width: 300px;" multiple="true"  size="3">
                               <c:forEach var="fVar" items="${deExportForm.donorGroupList}" varStatus="lStatus">
                                 <option value="${fVar.ampOrgGrpId}">${fVar.orgGrpName}</option>
                               </c:forEach>
                             </html:select>
                             <br/>
                             <span ><digi:trn>Donor Agency</digi:trn></span>
                             <br/>
                             <html:select name="deExportForm" property="donorAgencySelected" styleClass="inp-text"  styleId="donorAgencyId" style="width: 300px;" multiple="true"  size="3">
                               <c:forEach var="fVar" items="${deExportForm.donorAgencyList}" varStatus="lStatus">
                                 <option value="${fVar.ampOrgId}">${fVar.name}</option>
                               </c:forEach>
                             </html:select>
                           <br/>
                        </td>
                        <td width="46%" style="vertical-align: top;">
                         <span class="list_header"><digi:trn>Sectors</digi:trn></span>
                         <br/>
                             <span><digi:trn>Primary Sector</digi:trn></span>
                             <br/>
                             <html:select name="deExportForm" property="primarySectorsSelected" styleClass="inp-text"  styleId="primarySectorsId" style="width: 300px;" multiple="true"  size="3">
                               <c:forEach var="fVar" items="${deExportForm.primarySectorsList}" varStatus="lStatus">
                                 <option value="${fVar.ampSectorId}">${fVar.name}</option>
                               </c:forEach>
                             </html:select>
                             <br/>
                             <span ><digi:trn>Secondary Sector</digi:trn></span>
                             <br/>
                             <html:select name="deExportForm" property="secondarySectorsSelected" styleClass="inp-text"  styleId="secondarySectorsId" style="width: 300px;" multiple="true"  size="3">
                               <c:forEach var="fVar" items="${deExportForm.secondarySectorsList}" varStatus="lStatus">
                                 <option value="${fVar.ampSectorId}">${fVar.name}</option>
                               </c:forEach>
                             </html:select>
                             <br/>
                             <span class="list_header"><digi:trn>Select Team</digi:trn></span>
                             <br/>
                             <html:select name="deExportForm" property="selectedTeamId" styleClass="inp-text"  styleId="teamId" style="width: 300px;" onchange="changeTeam()">
                               <option value="-1"><digi:trn>Please select team</digi:trn></option>
                               <c:forEach var="fVar" items="${deExportForm.teamList}" varStatus="lStatus">
                                 <option value="${fVar.ampTeamId}">${fVar.name}</option>
                               </c:forEach>
                             </html:select>
<%--                          
                         <span class="list_header">Language</span>
                         <div id="reportGroupDiv" style="padding: 15px 15px 15px 15px; border: 1px solid gray; background-color: white; position: relative;">
                           <html:radio name="deExportForm" property="language" value="en" />English<br/>
                           <html:radio name="deExportForm" property="language" value="es" />Spanish<br/>
                           <html:radio name="deExportForm" property="language" value="fr" />French<br/>
                         </div>
--%>
                        </td>
                      </tr>
                    </table>
                    
                    <input type="button" class="dr-menu" onclick="cancelFilter();" value="<digi:trn>Reset</digi:trn>" name="reset" style="font-size: larger;"/>
                  
                    <br/>
                    <br/>
                    <br/>
                  
                    
				</div>
        
			</div>
		</div>

<%= ExportHelper.renderHiddenElements(tree) %>

		</digi:form>
	</td>
	</tr>
</table>

	