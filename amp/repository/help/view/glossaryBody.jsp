<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/displaytag" prefix="display" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/yahoo-dom-event.js"></script>

<script type="text/javascript">
  if (YAHOOAmp != null){
    var YAHOO = YAHOOAmp;
  }
  var tree;

  YAHOOAmp.widget.GlossNode = function(oData, oParent, expanded, checked, isMandatory, ident) {
		YAHOOAmp.widget.TaskNode.superclass.constructor.call(this,oData,oParent,expanded);
	    this.setMandatory(isMandatory);
	    this.setUpCheck(checked || oData.checked);
	    this.setId(ident);
	    
	};  
</script>

    <link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/treeview.css" />
    <link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/fonts-min.css" />
    <link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/tabview.css" />
    <link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/styles.css" />

    <script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/logger-min.js"></script>
    <script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/treeview-debug.js"></script>
    <script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/tabview-min.js"></script>

<style type="text/css">

	/** Clearfix */
	div {
      zoom: 1;
    }
    .clearfix {
      overflow: hidden;
      height: 100%;
    }

	/** End Clearfix */

.ygtvcheck0 { background: url(/TEMPLATE/ampTemplate/images/yui/check0.gif) 0 0 no-repeat; width:16px; cursor:pointer }
.ygtvcheck1 { background: url(/TEMPLATE/ampTemplate/images/yui/check1.gif) 0 0 no-repeat; width:16px; cursor:pointer }
.ygtvcheck2 { background: url(/TEMPLATE/ampTemplate/images/yui/check2.gif) 0 0 no-repeat; width:16px; cursor:pointer }

#expandcontractdiv {background-color:#FFFFCC; margin:0 0 .5em 0; padding:0.2em;}
#treeDiv { background: #fff;}
#treeContainer{ float: left; width: 20%;}
#mainAreaContainer { float: right; width: 60%;}
#searchContainer{ float: right; display: inline; float: none; right: 0; top: 0; position: absolute; width: 20%;}

</style> 

<digi:form action="/glossary.do">
<div style="margin: 10px">
	<div class="subtitle-blue">
		<digi:trn>Glossary</digi:trn>	
	</div>
	<div>
		&nbsp;
	</div>
	<div class="glossBody clearfix" style="position: relative;">
		<div id="treeContainer">
			<div id="treeDiv">

			</div>
		</div>
		<div id="mainAreaContainer">
			<div id="nodeContentContainer">
				<div id="nodeTitle" style="font-weight: bolder; font-size: large; margin: 5px;"></div>
				<div id="nodeContentDiv">
					Please select node in glossary tree
				</div>
				<div id="nodeEditorLinkDiv"></div>
				<div id="addEditDeleteLinksDiv" style="padding: 5px"></div>
			</div>
		</div>
		<div id="searchContainer">
			<div>
				<div>
					<input type="text" name="searchGlossary">
				</div>
				<div>	
					<input type="button" value="Search">
					<input type="button" value="Reset">
				</div>
			</div>
		</div>
		
	</div>
</div>
</digi:form>


<script type="text/javascript">

	<digi:context name="topicBodyAction" property="context/ampModule/moduleinstance/topicBody.do" />
	<digi:context name="topicEditAction" property="context/editor/moduleinstance/showEditText.do" />
	<digi:context name="topicDeleteAction" property="context/ampModule/moduleinstance/deleteTopic.do" />
	var getBodyURL = '<%=topicBodyAction%>';
	var editorLink = '<%=topicEditAction%>';
	var deleteLink = '<%=topicDeleteAction%>';

	$(document).ready(function () {

		tree = new YAHOO.widget.TreeView('treeDiv',
		[
			<c:forEach var="node" items="${glossaryForm.tree}">
				${node.yahooJSdefinition}
			</c:forEach>
		]
		);
		
		tree.subscribe('clickEvent',function(event){
			var str='';
			for(var member in event.node.data){ str+=member+', '; };
			//alert(str);
	
			$('div#nodeEditorLinkDiv').html('');
			var resp=$.ajax({
				   type: 'POST',
				   url: getBodyURL,
				   data: ({helpTopicId : event.node.data.ampHelpTopicId}),
				   cache : false,
				   success: function(data,msg){
						$('div#nodeContentDiv').html(data);
						$('div#nodeTitle').html(event.node.label);
						<digi:secure group="Help Administrators">
							var key = event.node.data.ampEditorKey;
							var lang = '${requestScope["org.digijava.kernel.navigation_language"].code}';
							var linkWithParams = editorLink + '?id=' + key + '&lang=' + lang + '&referrer=' + window.location;
							var linkEdit = '<a href="'+linkWithParams+'">Edit text</a>';
							var linkAddChild = '<a href="javascript:addChildToNode('+event.node.data.ampHelpTopicId+')">Add child</a>';
							var linkDeleteNode = '<a href="javascript:deleteNode('+event.node.data.ampHelpTopicId+')">Delete node</a>';
							$('div#nodeEditorLinkDiv').html(linkEdit+'&nbsp;&nbsp;'+linkAddChild+'&nbsp;&nbsp;'+linkDeleteNode);
						</digi:secure>

						
				   },
			   	   error : function(XMLHttpRequest, textStatus, errorThrown){alert('Error, cannot load glossary topic.');} 
			}).responseText;
		});
		tree.render();
	});

	function addChildToNode(parentId){
			alert('add child to '+parentId);
	}

	function deleteNode(topicId){
		var resp=$.ajax({
			   type: 'POST',
			   url: deleteLink,
			   data: ({helpTopicId : topicId}),
			   cache : false,
			   success: function(data,msg){
					var node = tree.getNodeByProperty('ampHelpTopicId',topicId);
					$('div#nodeContentDiv').html('');
					$('div#nodeTitle').html('');
					$('div#nodeEditorLinkDiv').html('');
					tree.removeNode(node,true);
			   },
		   	   error : function(XMLHttpRequest, textStatus, errorThrown){alert('Error, cannot delete glossary topic with id '+topicId);} 
		}).responseText;
		alert(resp);
	}
	
</script>