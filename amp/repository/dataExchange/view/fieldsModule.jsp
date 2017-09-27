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

<script type="text/javascript">
  var tree;
  
</script>

<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/fonts/fonts-min.css" />


<style type="text/css">

.ygtvcheck0 { background: url(/TEMPLATE/ampTemplate/images/yui/check0.gif) 0 0 no-repeat; width:16px; cursor:pointer }
.ygtvcheck1 { background: url(/TEMPLATE/ampTemplate/images/yui/check1.gif) 0 0 no-repeat; width:16px; cursor:pointer }
.ygtvcheck2 { background: url(/TEMPLATE/ampTemplate/images/yui/check2.gif) 0 0 no-repeat; width:16px; cursor:pointer }

#expandcontractdiv {background-color:#FFFFCC; margin:0 0 .5em 0; padding:0.2em;}
#treeDiv1 { background: #fff }
</style> 

  
<script type="text/javascript">

	function treeInit() {
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
        tree = new YAHOO.widget.TreeView("dataExportTree");
            
        <bean:define id="tree" name="fieldsModuleTree" type="org.digijava.module.dataExchange.type.AmpColumnEntry" toScope="page"/>
        <bean:define id="src" name="sourceId" type="java.lang.Long"/>
        <%= ExportHelper.renderActivityTree(tree,src,request) %>

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

         
		YAHOO.util.Event.addListener(window, "load", treeInit) ;
	</script>

                      
<div class="draglist" style="width:100%; height:510px; border:1px solid; margin-top:15px; overflow:scroll; border:1px solid #CCCCCC;">
   	<div id="dataExportTree"></div>
</div>

<c:if test="${not empty sourceId && sourceId != -1}">
	<bean:define id="srcId" name="sourceId" type="java.lang.Long"></bean:define>
<%= ExportHelper.renderHiddenElements(tree,srcId) %>
</c:if>
<c:if test="${sourceId == -1}">
	<%= ExportHelper.renderHiddenElements(tree) %>
</c:if>	