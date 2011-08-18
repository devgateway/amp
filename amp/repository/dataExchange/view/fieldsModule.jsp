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
	
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/tab/assets/tabview.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/border_tabs.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/reportWizard/reportWizard.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/filters.css'/>">

	<br>

  
	<script type="text/javascript">



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
            
          <bean:define id="tree" name="fieldsModuleTree" type="org.digijava.module.dataExchange.type.AmpColumnEntry" toScope="page"/>
          <%= ExportHelper.renderActivityTree(tree, request) %>

      //The tree is not created in the DOM until this method is called:
          tree.draw();
          
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

         
		YAHOOAmp.util.Event.addListener(window, "load", treeInit) ;
	</script>



                      <div style="text-align: left; width: 87.5%;margin-left: auto;margin-right: auto; ">
                        <div id="expandcontractdiv" style="margin-bottom: 0px;" >
                          <a id="expand" href="#">Expand all</a>
                          <a id="collapse" href="#">Collapse all</a>
                        
                          <a id="check" href="#">Check all</a>
                          <a id="uncheck" href="#">Uncheck all</a>
                        </div>
                      </div>
        <div class="draglist" style="height:80%; border-width: 0px;margin-left: auto;margin-right: auto;">
        	<div id="dataExportTree"></div>
                  
        
        </div>


<%= ExportHelper.renderHiddenElements(tree) %>


	