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

<%@page import="org.digijava.module.aim.fmtool.util.FeatureManagerTreeHelper"%>
<%@page import="org.digijava.module.aim.fmtool.util.FMToolConstants"%>


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
      <script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/treeview-min.js"></script>
      <script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/tabview-min.js"></script>

      <script type="text/javascript" src="/repository/dataExchange/view/scripts/TaskNode.js"></script>

  <style type="text/css">

  .ygtvcheck0 { background: url(/TEMPLATE/ampTemplate/images/yui/check0.gif) 0 0 no-repeat; width:16px; cursor:pointer }
  .ygtvcheck1 { background: url(/TEMPLATE/ampTemplate/images/yui/check1.gif) 0 0 no-repeat; width:16px; cursor:pointer }
  .ygtvcheck2 { background: url(/TEMPLATE/ampTemplate/images/yui/check2.gif) 0 0 no-repeat; width:16px; cursor:pointer }
    .icon-module { display:block; padding-left: 17px; background: transparent url(module/aim/images/folder.gif) 0 0px no-repeat; }
    .icon-feature { display:block; padding-left: 17px; background: transparent url(module/aim/images/gfolder.gif) 0 0px no-repeat; }
    .icon-field { display:block; padding-left: 17px; background: transparent url(module/aim/images/sheet.gif) 0 0px no-repeat; }
    .icon-module-cerr { display:block; padding-left: 16px; background: #FFE4C4 url(module/aim/images/folder.gif) 0 0px no-repeat; }
    .icon-feature-cerr { display:block; padding-left: 16px; background: #FFE4C4 url(module/aim/images/gfolder.gif) 0 0px no-repeat; }
    .icon-field-cerr { display:block; padding-left: 16px; background: #FFE4C4 url(module/aim/images/sheet.gif) 0 0px no-repeat; }
    .icon-module-err { display:block; padding-left: 16px; background: #FF7575 url(module/aim/images/folder.gif) 0 0px no-repeat; }
    .icon-feature-err { display:block; padding-left: 16px; background: #FF7575 url(module/aim/images/gfolder.gif) 0 0px no-repeat; }
    .icon-field-err { display:block; padding-left: 16px; background: #FF7575 url(module/aim/images/sheet.gif) 0 0px no-repeat; }
  }
  #expandcontractdiv {background-color:#FFFFCC; margin:0 0 .5em 0; padding:0.2em;}
  #treeDiv1 { background: #fff }

  </style> 

  	
  	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/tab/assets/tabview.css'/>">
  	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/border_tabs.css'/>">
  	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/reportWizard/reportWizard.css'/>">
  	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/filters.css'/>">

  	<br>
  	<br>

      <digi:instance property="fmCheckForm" />


  	<script type="text/javascript">
      var tabView = null;
      var currentFMEType = null;
      var currentFMEName = null;
      
  	YAHOOAmp.namespace("YAHOOAmp.amp.dataExchange");
  	YAHOOAmp.amp.dataExchange.numOfSteps	= 2; // have to be 3 when we include additional fields
  		
  	YAHOOAmp.amp.dataExchange.tabLabels	= new Array("tab_tree", "tab_database");
  		
       function navigateTab(value){
       		tabView.set("activeIndex", tabView.get("activeIndex")+value);
       }
  		

      function init() {
        tabView = new YAHOOAmp.widget.TabView('wizard_container');
        buildRandomTextNodeTree();
      }
      
	function removeFME(value, type){
        var form = document.getElementById('form');
        form.action = "/aim/fmCheck.do?method=removeFME";
        document.getElementById('fixValueId').value = value;
        document.getElementById('fixValueTypeId').value = type;
        form.submit();
	}

	function clearFMESources(value){
		console.log(value);
		var uri = "fmeType=";
		if (value == null){
			uri = uri + "allFME&fmeName=allFME";
		} else {
			console.log("type: "+currentFMEType);
			console.log("name: "+currentFMEName);
			if (currentFMEType == null || currentFMEName == null){
				return;
			}
			uri = uri + encodeURIComponent(currentFMEType) + "&fmeName=" + encodeURIComponent(currentFMEName);
		}
		
        var form = document.getElementById('form');
        form.action = "/aim/fmCheck.do?method=clearSource&"+uri;
        form.submit();
	}

      function buildRandomTextNodeTree(showAll) {
          if (showAll == null)
        	  showAll = true;
  		//instantiate the tree:
          tree = new YAHOOAmp.widget.TreeView("fmTree");
		<bean:define id="tree" name="fmCheckForm" property="fmeTree" type="org.digijava.module.aim.fmtool.types.FMCheckTreeEntry" toScope="page"/>
          	<%= FeatureManagerTreeHelper.renderTree(tree) %>
          
          
         // Expand and collapse happen prior to the actual expand/collapse,
         // and can be used to cancel the operation
         tree.subscribe("expand", function(node) {
//        	 YAHOOAmp.log(node.index + " was expanded", "info", "example");
                // return false; // return false to cancel the expand
             });

         tree.subscribe("collapse", function(node) {
//        	 YAHOOAmp.log(node.index + " was collapsed", "info", "example");
             });

         // Trees with TextNodes will fire an event for when the label is clicked:
         tree.subscribe("labelClick", function(node) {
             currentFMEType = node.labelElId;
             currentFMEName = node.label;
             
             getSource(node.labelElId, node.label);
//        	 YAHOOAmp.log(node.index + " label was clicked", "info", "example");
             });

  		//The tree is not created in the DOM until this method is called:
          tree.draw();
      }

      function addLodingImageToLogTab(logDiv){
          var v_img = document.createElement("img");
          v_img.setAttribute("src", "/TEMPLATE/ampTemplate/images/amploading.gif");
          v_img.setAttribute("alt", "");
          v_img.setAttribute("border", "0");

          logDiv.appendChild(v_img);
          logDiv.appendChild(document.createElement("br"));
          logDiv.appendChild(document.createTextNode("loading data ..."));
          
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
      
      function getSource(type, name) {

          var v_table = document.getElementById('sourceTableId');
          v_table.setAttribute("height","100%");
          var v_tbody = v_table.childNodes[1];
          v_tbody.innerHTML = '';
          row = document.createElement("tr");
          logDiv = createCall("td","");
          logDiv.setAttribute("width","100%");
          logDiv.setAttribute("align","center");
          row.appendChild(logDiv);
          v_tbody.appendChild(row);

          addLodingImageToLogTab(logDiv);
      	  /*
          	v_table.removeAttribute("height");
          	var v_tbody = v_table.childNodes[1];
              row = document.createElement("tr");
              row.setAttribute("bgcolor", "#D7EAFD");
              row.appendChild(createCall("th",messages.title+" sources", "center", "100%"));
              v_tbody.appendChild(row);
			*/

    	  
          // Define the callbacks for the asyncRequest
          var callbacks = {

              success : function (o) {
                  var messages = [];
                  try {
                      messages = YAHOOAmp.lang.JSON.parse(o.responseText);
                  }
                  catch (x) {
//                      alert("JSON Parse failed!");
                      return;
                  }
                  	var v_table = document.getElementById('sourceTableId');
                  	v_table.removeAttribute("height");
                  	var v_tbody = v_table.childNodes[1];
                  	v_tbody.innerHTML = '';
                      row = document.createElement("tr");
                      row.setAttribute("bgcolor", "#D7EAFD");
                      row.appendChild(createCall("th",messages.title+" sources", "center", "100%"));
                      v_tbody.appendChild(row);

                  	for (var i = 0, len = messages.items.length; i < len; ++i) {
                          var m = messages.items[i];

                          row = document.createElement("tr");
                          row.setAttribute("bgcolor", "#F7F7F7");
                          row.appendChild(createCall("td",m.source));
                          v_tbody.appendChild(row);
                      }
                      return;

              },

              failure : function (o) {
                  if (!YAHOOAmp.util.Connect.isCallInProgress(o)) {
                      alert("Async call failed!");
                  }
              },

              timeout : 30000
          }

          // Make the call to the server for JSON data
          var uri = "/aim/fmCheck.do~method=sourceAjax&fmeType=" + encodeURIComponent(type)+"&fmeName="+encodeURIComponent(name);
          YAHOOAmp.util.Connect.asyncRequest('GET', uri, callbacks);
      }
                
  		YAHOOAmp.util.Event.addListener(window, "load", init) ;
  	</script>

						<span class="crumb" style="padding-left: 10px;">
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:AmpAdminHome">
							Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
                        <c:set var="trnFeatureManager">
                          <digi:trn key="aim:clickToAccessFeatureManager">Click here to access Feature Manager</digi:trn>
                        </c:set>
                        <digi:link href="/visibilityManager.do" styleClass="comment" title="${trnFeatureManager}" >
                          <digi:trn key="aim:theFeatureManager">Feature Manager</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:FMCheckTool">
							Check Tool
						</digi:trn>
						</span>

		<digi:form action="/fmCheck.do" method="post" styleId="form">
			<html:hidden styleId="fixValueId"  name="fmCheckForm" property="fixValue"/>
			<html:hidden styleId="fixValueTypeId"  name="fmCheckForm" property="fixValueType"/>
		<span id="formChild" style="display:none;">&nbsp;</span>

		<br />
		<div id="wizard_container" class="yui-navset">
    		<ul class="yui-nav">
    			<li id="tab_tree" class="selected"><a href="#tab_tree"><div><digi:trn>Tree</digi:trn></div></a></li>
     			<li id="tab_database" class="enabled"><a href="#tab_database"><div>Database</div></a> </li>
    		</ul>
			<div class="yui-content" style="background-color: #EEEEEE">
				<div id="tab_tree" class="yui-tab-content" style="padding: 0px 0px 1px 20px;" >
                    <c:set var="stepNum" value="0" scope="request" />
                      <input type="button" class="dr-menu" onclick="clearFMESources();" value='Clear all Sources'/>
                      <input type="checkbox" id="jspShowAllId" onclick="buildRandomTextNodeTree(!this.checked);">show only Features without Sources
                      
                      
                   	<table width='100%'>
               			<tr>
           					<td width='50%'>
                    			<div style="text-align: center; padding: 10px 10px 10px 0px;">
                    				<div class="draglist" style="border-width: 0px; height: 310; ">
										<div id="fmTree"></div>
				  					</div>
								</div>
							</td>
                    		<td width='50%'>
                    			<div style="text-align: center; padding: 10px 10px 10px 10px;">
			                      	<input type="button" class="dr-menu" onclick="clearFMESources('current');" value='Clear all Sources for current Feature'/>
                    				<div class="draglist" style="border-width: 0px; height: 310; ">
			        					<table id="sourceTableId" border="0" style="border-collapse: separate;" width="100%" height="100%" border="0" bgcolor="#eeeeee" border-spacing="0" >
			        						<tbody>
			        							<tr>
			        								<td width="100%" align="center">
			        								</td>
			        							</tr>
			        						</tbody>
			        					</table>                    			
					 				</div>
								</div>                    			
			        		</td>
                    	</tr>
                    </table>
                    <table>
						<tr>
							<td colspan="2">
								<strong>Icons Reference</strong>
							</td>
						</tr>
						<tr>
							<td nowrap="nowrap" bgcolor="#e9e9e9">
								<img vspace="2" border="0" align="absmiddle" src="module/aim/images/folder.gif"/> Module
							</td>
						</tr>
						<tr>
							<td nowrap="nowrap" bgcolor="#e9e9e9">
								<img vspace="2" border="0" align="absmiddle" src="module/aim/images/gfolder.gif"/> Feature
							</td>
						</tr>
						<tr>
							<td nowrap="nowrap" bgcolor="#e9e9e9">
								<img vspace="2" border="0" align="absmiddle" src="module/aim/images/sheet.gif"/> Field
							</td>
						</tr>
					</table>        
			    </div>
				<div id="tab_database"  class="yui-tab-content"  style="padding: 0px 0px 1px 0px; display: none;">
                    <c:set var="stepNum" value="1" scope="request" />
                    <div style="text-align: center; padding: 10px 10px 10px 50px;">
                      <input type="button" class="dr-menu" onclick="removeFME('fixAll');" value='Fix All'/>
                      <html:checkbox styleId="jspFixAllId" property="jspFixAll" name="fmCheckForm">Include features with children elements in fix.</html:checkbox>
                      <div class="draglist" style="text-align: center; border-width: 0px; height: 310; ">
                        <table id="dbTableId" border="0" style="border-collapse: separate;" width="100%"  border="0" bgcolor="#eeeeee" border-spacing="0" >
                          <tbody>
                          	<tr>
                          		<th>Feature Name</th>
                          		<th>Problem</th>
                          		<th>Fix</th>
                          	</tr>
                            <c:forEach var="fVar" items="${fmCheckForm.duplicatesList}" varStatus="lStatus">
	                            <tr id="dup_${fVar.type}_${fVar.name}">
	                              <td  width="50%" align="center">
	                                ${fVar.name}
	                              </td>
	                              <td width="25%" align="center">
	                                Duplicates ${fVar.count}
	                              </td>
	                              <td width="25%" align="center">
	                                <input type="button" class="dr-menu" onclick="removeFME('${fVar.name}','${fVar.type}');" value='Fix ${fVar.type}'/>
	                              </td>
	                            </tr>
                            </c:forEach>
                            <c:forEach var="fVar" items="${fmCheckForm.problemList}" varStatus="lStatus">
	                            <tr id="prob_${fVar.type}_${fVar.name}">
	                              <td  width="50%" align="center">
	                                ${fVar.name}
	                              </td>
	                              <td width="25%" align="center">
	                                ${fVar.problemName}
	                              </td>
	                              <td width="25%" align="center">
	                                <input type="button" class="dr-menu" onclick="removeFME('${fVar.name}','${fVar.type}');" value='Fix ${fVar.type}'/>
	                              </td>
	                            </tr>
                            </c:forEach>
                          </tbody>
                        </table>
					  </div>
					</div>
				</div>
			</div>
		</div>


		</digi:form>
