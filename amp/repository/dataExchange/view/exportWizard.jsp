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


<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/yahoo-dom-event.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/json-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/connection-min.js"></script>


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

    <script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/logger-min.js"></script>
    <script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/treeview-debug.js"></script>
    <script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/tabview-min.js"></script>

    <script type="text/javascript" src="/repository/dataExchange/view/scripts/TaskNode.js"></script>

<style type="text/css">

.ygtvcheck0 { background: url(/TEMPLATE/ampTemplate/images/yui/check0.gif) 0 0 no-repeat; width:16px; cursor:pointer }
.ygtvcheck1 { background: url(/TEMPLATE/ampTemplate/images/yui/check1.gif) 0 0 no-repeat; width:16px; cursor:pointer }
.ygtvcheck2 { background: url(/TEMPLATE/ampTemplate/images/yui/check2.gif) 0 0 no-repeat; width:16px; cursor:pointer }

#expandcontractdiv {background-color:#FFFFCC; margin:0 0 .5em 0; padding:0.2em;}
#treeDiv1 { background: #fff }

</style> 

	
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/tab/assets/tabview.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/border_tabs.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/reportWizard/reportWizard.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/filters.css'/>">

	<br>
	<br>

    <digi:instance property="deExportForm" />


	<script type="text/javascript">
    var tabView = null;
    
	YAHOOAmp.namespace("YAHOOAmp.amp.dataExchange");
	YAHOOAmp.amp.dataExchange.numOfSteps	= 2; // have to be 3 when we include additional fields
		
	YAHOOAmp.amp.dataExchange.tabLabels	= new Array("tab_select_field", "tab_filter", "tab_logger");
		
        function navigateTab(value){
        	tabView.set("activeIndex", tabView.get("activeIndex")+value);
        }
		
		
		function initializeDragAndDrop() {
			var height			= Math.round(YAHOO.util.Dom.getDocumentHeight() / 2.3);
			
			tabView 		= new YAHOOAmp.widget.TabView('wizard_container');
			tabView.addListener("contentReady", treeInit);
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

            enableLogTab();
          } else {
              alert('please select one team');
          }
      }

      function exportLog(){
         var form = document.getElementById('form');
         form.action = "/dataExchange/exportWizard.do?method=log";
         form.target="_self"
         form.submit();
      }
       
      function cancelBack(){
        window.location = "/aim/admin.do";
      }

      function enableLogTab(){
          var logDiv = document.getElementById('logDivId');
          logDiv.innerHTML = '';
          enableToolbarButton(document.getElementById('step2_next_button'));
          addLodingImageToLogTab(logDiv);
          enableTab(2);
          tabView.set("activeIndex", 2);            
          setTimeout("checkLog()",3000);
      }

      function disableLogTab(){
          disableToolbarButton(document.getElementById('step2_next_button'));
          disableTab(2);

        var v_tbody = document.getElementById('logTableId');
        v_tbody.innerHTML = '';
        row = document.createElement("tr");
        logDiv = createCall("td","");
        logDiv.setAttribute("id","logDivId");
        logDiv.setAttribute("width","100%");
        logDiv.setAttribute("align","center");
        row.appendChild(logDiv);
        v_tbody.appendChild(row);

        addLodingImageToLogTab(logDiv);
        
      }

      
      function cancelFilter(){
          document.getElementById('donorTypeId').selectedIndex=-1;
          document.getElementById('donorGroupId').selectedIndex=-1;
          document.getElementById('donorAgencyId').selectedIndex=-1;
          document.getElementById('primarySectorsId').selectedIndex=-1;
          document.getElementById('secondarySectorsId').selectedIndex=-1;
          document.getElementById('teamId').selectedIndex=0;
          disableExportButton();
          disableLogTab();
      }

      function changeTeam(){
    	  var selTeamId = document.getElementById('teamId');

          if (selTeamId.value != "-1"){
        	  enableExportButton();
          } else {
        	  disableExportButton();
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

        function enableTab(tabIndex) {
            var tab     = tabView.getTab(tabIndex);
            if ( tab.get("disabled") ) {
              tab.set("disabled", false);
              var labelEl   = document.getElementById( YAHOOAmp.amp.dataExchange.tabLabels[tabIndex] );
              (new YAHOO.util.Element(labelEl)).replaceClass('disabled', 'unsel');
            }
          
          
//          var btnid   = "step" + (tabIndex-1) + "_next_button";
//          var btn     = document.getElementById(btnid);
//          this.enableToolbarButton(btn);
        }
        
        function disableTab(tabIndex) {
            var tab     = tabView.getTab(tabIndex);
            if ( !tab.get("disabled") ) {
              tab.set("disabled", true);
              var labelEl   = document.getElementById( YAHOOAmp.amp.dataExchange.tabLabels[tabIndex] );
              (new YAHOO.util.Element(labelEl)).replaceClass('unsel', 'disabled');
            }
          
//          var btnid   = "step" + (tabIndex-1) + "_next_button";
//          var btn     = document.getElementById(btnid);
//          this.disableToolbarButton(btn);
        }

        
        
        function addLodingImageToLogTab(logDiv){
            var v_img = document.createElement("img");
            v_img.setAttribute("src", "/TEMPLATE/ampTemplate/images/amploading.gif");
            v_img.setAttribute("alt", "");
            v_img.setAttribute("border", "0");

            logDiv.appendChild(v_img);
            logDiv.appendChild(document.createElement("br"));
            logDiv.appendChild(document.createTextNode("Error logs are generated ."));
            
        }

        function createCall(type, text, align, width){
          var v_cell = document.createElement(type);
          if (align == null){
        	  align = "left";
          } 
          v_cell.setAttribute("align", align);
          v_cell.appendChild(document.createTextNode(text));
          if (width != null){
              v_cell.setAttribute("width", width);
          }
          
          return v_cell;
        }
        
        function checkLog(e) {

            var logDiv = document.getElementById('logDivId');

            // Define the callbacks for the asyncRequest
            var callbacks = {

                success : function (o) {
                    var messages = [];
                    try {
                        messages = YAHOOAmp.lang.JSON.parse(o.responseText);
                    }
                    catch (x) {
//                        alert("JSON Parse failed!");
                        return;
                    }
                    if (messages.status == 0){ // nothing to show
//                        logDiv.innerHTML = '';
//                        setTimeout("checkLog()",5000); 
                        return;
                    } else if (messages.status == 1){ // not ready generating
//                        logDiv.appendChild(document.createTextNode("Error logs are generated ...."));
                        logDiv.appendChild(document.createTextNode("."));
                        setTimeout("checkLog()",5000); 
                        return;
                    } else if (messages.status == 2){ // readyadding content to div
                    	var v_tbody = document.getElementById('logTableId');
                    	v_tbody.innerHTML = '';
                        row = document.createElement("tr");
                        row.setAttribute("bgcolor", "#D7EAFD");
                        row.appendChild(createCall("th","Activity Title", "center", "60%"));
                        row.appendChild(createCall("th","Error", "center", "40%"));
                        v_tbody.appendChild(row);

                    	for (var i = 0, len = messages.items.length; i < len; ++i) {
                            var m = messages.items[i];

                            row = document.createElement("tr");
                            row.setAttribute("bgcolor", "#F7F7F7");
                            row.appendChild(createCall("td",m.ActivityName));
                            row.appendChild(createCall("td",m.ErrorID));
                            v_tbody.appendChild(row);
                        }
                        return;
                    } else  if (messages.status == 3){ // no errors
                        logDiv.innerHTML = '';
                        logDiv.appendChild(document.createTextNode("There are no errors while export"));
                        return;
                    } else if (messages.status == 4){ // error
                        logDiv.innerHTML = '';
                        logDiv.appendChild(document.createTextNode("Error throw while log generation"));
                        return;
                    } 
                },

                failure : function (o) {
                    if (!YAHOOAmp.util.Connect.isCallInProgress(o)) {
                        alert("Async call failed!");
                    }
                },

                timeout : 30000
            }

            // Make the call to the server for JSON data
            YAHOOAmp.util.Connect.asyncRequest('GET',"/dataExchange/exportWizard.do~method=logAjax", callbacks);
        }

              
		YAHOOAmp.util.Event.addListener(window, "load", treeInit) ;
	</script>


<table bgColor=#ffffff cellPadding=10 cellSpacing=0 width="900px">
	<tr>
		<td valign="bottom">
				
		</td>
	</tr>
	<tr>
		<td align="left" vAlign="top">
		<digi:form action="/exportWizard.do?method=export" method="post" styleId="form">
		<span id="formChild" style="display:none;">&nbsp;</span>

        <span class="subtitle-blue">
          &nbsp;<digi:trn key="Data Exporter">Data Exporter</digi:trn>
        </span>		
		
		<div style="color: red; text-align: center; visibility: hidden" id="savingReportDiv">

		</div>
		<br />
		<div id="wizard_container" class="yui-navset">
    		<ul class="yui-nav">
    			<li id="tab_select_field" class="selected"><a href="#tab_select_field"><div><digi:trn key="Field Selection">Field Selection</digi:trn></div></a> </li>
<%--
     			<li id="tab_additional_field" class="enabled"><a href="#tab_additional_field"><div>Additional Fields</div></a> </li>
--%>
    			<li id="tab_filter" class="enabled"><a href="#tab_filter"><div><digi:trn key="Team Selection and Filters">Team Selection and Filters</digi:trn></div></a> </li>
          <c:if test="${deExportForm.tabCount==3}">
                <li id="tab_logger" class="enabled"><a href="#tab_logger"><div><digi:trn key="Export Log">Export log</digi:trn></div></a> </li>
          </c:if>
    		</ul>
			<div class="yui-content" style="background-color: #EEEEEE">
				<div id="tab_select_field" class="yui-tab-content" style="padding: 0px 0px 1px 0px;" >
                    <c:set var="stepNum" value="0" scope="request" />
                    <jsp:include page="toolbar.jsp" />
					<div style="height: 355px; padding-bottom: 40px;">
    					<table cellpadding="5px" style="vertical-align: middle" width="100%">
    						<tr>
        						<td width="47%" align="center">
        							<span class="list_header">
        								<digi:trn key="rep:wizard:availableColumns">Available Fields</digi:trn>
        							</span>
                      <div style="text-align: left; width: 87.5% ">
                        <div id="expandcontractdiv" style="margin-bottom: 0px;" >
                          <a id="expand" href="#"><digi:trn key="Expand All">Expand all</digi:trn></a>
                          <a id="collapse" href="#"><digi:trn key="Collapse all">Collapse all</digi:trn></a>
                        
                          <a id="check" href="#"><digi:trn key="Check all">Check all</digi:trn></a>
                          <a id="uncheck" href="#"><digi:trn key="Uncheck all">Uncheck all</digi:trn></a>
                        </div>
                      </div>
                      <div id="source_col_div" class="draglist" style="border-width: 0px;">
                          <div id="dataExportTree"></div>
                  
        
        			  </div>
        						</td>
    						</tr>
    					</table>
					</div>
			    </div>
<%--   temporoary removed.      
				<div id="tab_additional_field"  class="yui-tab-content" align="center" style="padding: 0px 0px 1px 0px; display: none;">
                    <c:set var="stepNum" value="1" scope="request" />
                    <jsp:include page="toolbar.jsp" />
                    Select additional fields
				</div>
--%>
				<div id="tab_filter"  class="yui-tab-content"  style="padding: 0px 0px 1px 0px; display: none;">
                    <c:set var="stepNum" value="2" scope="request" />
                    <jsp:include page="toolbar.jsp" />
                    <table cellpadding="15px" width="100%" align="center" border="0" >
                      <tr>
                        <td width="46%" style="vertical-align: top;">
	                       <span class="list_header"><digi:trn key="Donors">Donors</digi:trn></span>
                           <br/>
                             <span><digi:trn key="Donor Type">Donor Type</digi:trn></span>
                             <br/>
                             <html:select name="deExportForm" property="donorTypeSelected" styleClass="inp-text" styleId="donorTypeId" style="width: 300px;" multiple="true" size="3">
                               <c:forEach var="fVar" items="${deExportForm.donorTypeList}" varStatus="lStatus">
                                 <option value="${fVar.ampOrgTypeId}"><digi:trn key="${fVar.orgType}">${fVar.orgType}</digi:trn></option>
                               </c:forEach>
                             </html:select>
                             <br/>
                             <span><digi:trn key="Donor Group">Donor Group</digi:trn></span>
                             <br/>
                             <html:select name="deExportForm" property="donorGroupSelected" styleClass="inp-text"  styleId="donorGroupId" style="width: 300px;" multiple="true"  size="3">
                               <c:forEach var="fVar" items="${deExportForm.donorGroupList}" varStatus="lStatus">
                                 <option value="${fVar.ampOrgGrpId}"><digi:trn key="${fVar.orgGrpName}">${fVar.orgGrpName}</digi:trn></option>
                               </c:forEach>
                             </html:select>
                             <br/>
                             <span ><digi:trn key="Donor Agency">Donor Agency</digi:trn></span>
                             <br/>
                             <html:select name="deExportForm" property="donorAgencySelected" styleClass="inp-text"  styleId="donorAgencyId" style="width: 300px;" multiple="true"  size="3">
                               <c:forEach var="fVar" items="${deExportForm.donorAgencyList}" varStatus="lStatus">
                                 <option value="${fVar.ampOrgId}"><digi:trn key="${fVar.name}">${fVar.name}</digi:trn></option>
                               </c:forEach>
                             </html:select>
                           <br/>
                        </td>
                        <td width="46%" style="vertical-align: top;">
                         <span class="list_header"><digi:trn key="Sectors">Sectors</digi:trn></span>
                         <br/>
                             <span><digi:trn key="Primary Sector">Primary Sector</digi:trn></span>
                             <br/>
                             <html:select name="deExportForm" property="primarySectorsSelected" styleClass="inp-text"  styleId="primarySectorsId" style="width: 300px;" multiple="true"  size="3">
                               <c:forEach var="fVar" items="${deExportForm.primarySectorsList}" varStatus="lStatus">
                                 <option value="${fVar.ampSectorId}"><digi:trn key="${fVar.name}">${fVar.name}</digi:trn></option>
                               </c:forEach>
                             </html:select>
                             <br/>
                             <span ><digi:trn key="Secondary Sector">Secondary Sector</digi:trn></span>
                             <br/>
                             <html:select name="deExportForm" property="secondarySectorsSelected" styleClass="inp-text"  styleId="secondarySectorsId" style="width: 300px;" multiple="true"  size="3">
                               <c:forEach var="fVar" items="${deExportForm.secondarySectorsList}" varStatus="lStatus">
                                 <option value="${fVar.ampSectorId}"><digi:trn key="${fVar.name}">${fVar.name}</digi:trn></option>
                               </c:forEach>
                             </html:select>
                             <br/>
                             <span class="list_header"><digi:trn key="Select Team">Select Team</digi:trn></span>
                             <br/>
                             <html:select name="deExportForm" property="selectedTeamId" styleClass="inp-text"  styleId="teamId" style="width: 300px;" onchange="changeTeam()">
                               <option value="-1"><digi:trn key="Please select team">Please select team</digi:trn></option>
                               <c:forEach var="fVar" items="${deExportForm.teamList}" varStatus="lStatus">
                                 <option value="${fVar.ampTeamId}"><digi:trn key="${fVar.name}">${fVar.name}</digi:trn></option>
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
                    
                    <input type="button" class="dr-menu" onclick="cancelFilter();" value='<digi:trn key="Reset">Reset</digi:trn>' name="reset" style="font-size: larger;"/>
                  
                    <br/>
                    <br/>
                    <br/>
                  
                    
				</div>
          <c:if test="${deExportForm.tabCount==3}">
                <div id="tab_logger" class="yui-tab-content" align="center" style="padding: 0px 0px 1px 0px; display: none;">
                    <c:set var="stepNum" value="3" scope="request" />
                    <jsp:include page="toolbar.jsp" />
                    <div style="text-align: center; padding: 10px 10px 10px 50px;">
                      <div class="draglist" style="text-align: center; border-width: 0px; height: 310; ">
                        <table  border="0" style="border-collapse: separate;" width="100%" height="100%" border="0" bgcolor="#eeeeee" border-spacing="0" >
                          <tbody id="logTableId">
                            <tr>
                              <td id="logDivId" width="100%" align="center">
                                <img src="/TEMPLATE/ampTemplate/images/amploading.gif" alt="" border="0"/>  
                              </td>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                    </div>                    
                    <div style="text-align: right; padding: 0px 80px 10px 0px;">
                      <input type="button" class="dr-menu" onclick="exportLog();" value='<digi:trn key="Save Log">Save Log</digi:trn>' name="saveLog" style="font-size: larger;"/>
                    </div>
                </div>
          </c:if>
			</div>
		</div>

<%= ExportHelper.renderHiddenElements(tree) %>

		</digi:form>
	</td>
	</tr>
</table>

	