<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@page import="org.digijava.module.dataExchange.util.ExportHelper"%>

<!-- Dependency source file --> 

<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/yahoo-dom-event.js"></script>
<!-- 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/animation-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/dragdrop-min.js"></script>
-->

<script type="text/javascript">
  if (YAHOOAmp != null){
    var YAHOO = YAHOOAmp;
  }
</script>

<digi:instance property="deExportForm" />


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<title>YUI Library Examples: TreeView Control: Custom TreeView with Check Boxes</title>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">

<!-- 
    <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.6.0/build/treeview/assets/skins/sam/treeview.css" />
 -->
    <link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/treeview.css" />
    <link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/fonts-min.css" />
    <link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/tabview.css" />


<!-- 

    <script type="text/javascript" src="http://yui.yahooapis.com/2.5.2/build/event/event-min.js"></script>
    <script type="text/javascript" src="http://yui.yahooapis.com/2.5.2/build/dom/dom-min.js"></script>
    <script type="text/javascript" src="http://yui.yahooapis.com/2.5.2/build/element/element-beta-min.js"></script>

 -->

    <script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/logger-min.js"></script>
    <script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/treeview-debug.js"></script>
    <script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/tabview-min.js"></script>


<!--begin custom header content for this example-->
<!--Additional custom style rules for this example:-->
<style type="text/css">

.ygtvcheck0 { background: url(/TEMPLATE/ampTemplate/images/yui/check0.gif) 0 0 no-repeat; width:16px; cursor:pointer }
.ygtvcheck1 { background: url(/TEMPLATE/ampTemplate/images/yui/check1.gif) 0 0 no-repeat; width:16px; cursor:pointer }
.ygtvcheck2 { background: url(/TEMPLATE/ampTemplate/images/yui/check2.gif) 0 0 no-repeat; width:16px; cursor:pointer }

#expandcontractdiv {border:1px solid #336600; background-color:#FFFFCC; margin:0 0 .5em 0; padding:0.2em;}
#treeDiv1 { background: #fff }
</style>

<!--end custom header content for this example-->



</head>
<body id="yahoo-com" class=" yui-skin-sam">
	
		
	<!--BEGIN SOURCE CODE FOR EXAMPLE =============================== -->
	
	<!-- Some style for the expand/contract section-->
<style>

</style>

<digi:form action="/export.do?method=export" method="post" styleId="form">

<html:hidden name="deExportForm" property="activityTags[0].select" value="true" />

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="85%">
  <tr>
    <td>Data Export testing</td>
  </tr>
  <tr>
    <td>
      <div id="dataExportTab" class="yui-navset">
          <ul class="yui-nav">
              <li id="0" class="selected"><a href="#tab1"><em>Select Fields</em></a></li>
              <li id="1"><a href="#tab2"><em>Additional Fields</em></a></li>
              <li id="2"><a href="#tab3"><em>Export</em></a></li>
      
          </ul>            
          <div class="yui-content">
              <div id="tab1">
                <c:set var="stepNum" value="0" scope="request" />
                <jsp:include page="toolbar.jsp" />
                <p>
                  <div id="expandcontractdiv">
                    <a id="expand" href="#">Expand all</a>
                    <a id="collapse" href="#">Collapse all</a>
                  
                    <a id="check" href="#">Check all</a>
                    <a id="uncheck" href="#">Uncheck all</a>
                  </div>
                  <div id="dataExportTree"></div>        
                </p>
              </div>
              <div id="tab2">
                <c:set var="stepNum" value="1" scope="request" />
                <jsp:include page="toolbar.jsp" />
                <p>here will be additional fields list, possible with drag and drop</p>
              </div>
              <div id="tab3">
                <c:set var="stepNum" value="2" scope="request" />
                <jsp:include page="toolbar.jsp" />
                <p>Final step Export</p>
                
                  <html:select name="deExportForm" property="selectedTeamId" styleId="teamId">
                    <option value="-1">Please select team</option>
                    <c:forEach var="vTeam" items="${deExportForm.teamList}" varStatus="lStatus">
                      <option value="${vTeam.ampTeamId}">${vTeam.name}</option>
                    </c:forEach>
                  </html:select>
                
                
                <button type="button" class="toolbar" onclick="exportActivity();">
                  <img height="16" src="/TEMPLATE/ampTemplate/images/green_check.png" class="toolbar" />
                  Export
                </button>
                
              </div>
          </div>
      </div>
    </td>
  </tr>
</table>

<!-- markup for expand/contract links -->


<script type="text/javascript" src="/repository/dataExchange/view/scripts/TaskNode.js"></script>

<script type="text/javascript">

function exportActivity(){
//	getCheckedNodes();
    var selTeamId = document.getElementById('teamId');

    if (selTeamId.value != "-1"){
      var form = document.getElementById('form');
	  form.action = "/dataExchange/export.do?method=export";
      form.target="_self"
      form.submit();
    } else {
        alert('please select one team');
    }
}

var tree;
(function() {

//	var nodes = [];
//	var nodeIndex;

  
	
	function treeInit() {
//		YAHOOAmp.log("Initializing TaskNode TreeView instance.")
		buildRandomTaskNodeTree();
	}
	
	//handler for expanding all nodes
	YAHOOAmp.util.Event.on("expand", "click", function(e) {
//		YAHOOAmp.log("Expanding all TreeView  nodes.", "info", "example");
		tree.expandAll();
		YAHOOAmp.util.Event.preventDefault(e);
	});
	
	//handler for collapsing all nodes
	YAHOOAmp.util.Event.on("collapse", "click", function(e) {
//		YAHOOAmp.log("Collapsing all TreeView  nodes.", "info", "example");
		tree.collapseAll();
		YAHOOAmp.util.Event.preventDefault(e);
	});

	//handler for checking all nodes
	YAHOOAmp.util.Event.on("check", "click", function(e) {
//		YAHOOAmp.log("Checking all TreeView  nodes.", "info", "example");
		checkAll();
		YAHOOAmp.util.Event.preventDefault(e);
	});
	
	//handler for unchecking all nodes
	YAHOOAmp.util.Event.on("uncheck", "click", function(e) {
//		YAHOOAmp.log("Unchecking all TreeView  nodes.", "info", "example");
		uncheckAll();
		YAHOOAmp.util.Event.preventDefault(e);
	});


	YAHOOAmp.util.Event.on("getchecked", "click", function(e) {
//		YAHOOAmp.log("Checked nodes: " + YAHOOAmp.lang.dump(getCheckedNodes()), "info", "example");
            
		YAHOOAmp.util.Event.preventDefault(e);
	});

	//Function  creates the tree and 
	//builds between 3 and 7 children of the root node:
    function buildRandomTaskNodeTree() {
	
		//instantiate the tree:
        tree = new YAHOOAmp.widget.TreeView("dataExportTree");
          
        <bean:define id="tree" name="deExportForm" property="activityTree" type="org.digijava.module.dataExchange.type.AmpColumnEntry" toScope="page"/>
        <%= ExportHelper.renderActivityTree(tree) %>

//        for (var i = 0; i < Math.floor((Math.random()*4) + 3); i++) {
//            var tmpNode = new YAHOOAmp.widget.TaskNode("label-" + i, tree.getRoot(), false);
//            // tmpNode.collapse();
//            // tmpNode.expand();
//            buildRandomTaskBranch(tmpNode);
//        }

       // Expand and collapse happen prior to the actual expand/collapse,
       // and can be used to cancel the operation
//       tree.subscribe("expand", function(node) {
//              YAHOOAmp.log(node.index + " was expanded", "info", "example");
//              // return false; // return false to cancel the expand
//           });

//       tree.subscribe("collapse", function(node) {
//              YAHOOAmp.log(node.index + " was collapsed", "info", "example");
//           });

       // Trees with TextNodes will fire an event for when the label is clicked:
//       tree.subscribe("labelClick", function(node) {
//              YAHOOAmp.log(node.index + " label was clicked", "info", "example");
//           });

       // Trees with TaskNodes will fire an event for when a check box is clicked
//       tree.subscribe("checkClick", function(node) {
//              YAHOOAmp.log(node.index + " check was clicked", "info", "example");
//           });

//       tree.subscribe("clickEvent", function(node) {
//              YAHOOAmp.log(node.index + " clickEvent", "info", "example");
//           });

		//The tree is not created in the DOM until this method is called:
        tree.draw();
    }

//	var callback = null;

	function buildRandomTaskBranch(node) {
		if (node.depth < 3) {
//			YAHOOAmp.log("buildRandomTextBranch: " + node.index);
			for ( var i = 0; i < Math.floor(Math.random() * 2) ; i++ ) {
				var tmpNode = new YAHOOAmp.widget.TaskNode(node.label + "-" + i, node, false);
                //tmpNode.onCheckClick = onCheckClick;
				buildRandomTaskBranch(tmpNode);
			}
		}
	}

//    function onCheckClick(node) {
//        YAHOOAmp.log(node.label + " check was clicked, new state: " + node.checkState, "info", "example");
//    }

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






    
	YAHOOAmp.util.Event.onDOMReady(treeInit);
})();

  var tabView = new YAHOOAmp.widget.TabView('dataExportTab');

  tabView.on('activeTabChange', function(ev) { 
//    alert(tabView.get("activeIndex"));
/*
	  if (ev.newValue == editorTab) { 
          var myConfig = { 
              height: '100px', 
              width: '600px', 
              animate: true, 
              dompath: true 
          }; 
          if (!myEditor) { 
              YAHOO.log('Create the Editor..', 'info', 'example'); 
              myEditor = new YAHOO.widget.Editor('editor', myConfig); 
              myEditor.render(); 
          } 
       }
*/       
  }); 
  
  function navigateTab(value){
    tabView.set("activeIndex", tabView.get("activeIndex")+value);
    
  }

  function prevDisable(){
    
  }

  function prevEnable(){
    
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
</script>
<%= ExportHelper.renderHiddenElements(tree) %>
  
</digi:form>

</body>

</html>
