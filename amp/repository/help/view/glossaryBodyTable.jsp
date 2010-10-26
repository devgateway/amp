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
    <script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/new/treeview-min.js"></script>
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

	.ygtvlabel{
		background-color:  #EDF5FF;
	}

	.searchResults{
		margin-top: 10px;
	}
</style> 

<digi:form action="/glossary.do">
<div style="margin: 10px">

	<table border="0" width="100%">
		<tr>
			<td class="subtitle-blue">
				<digi:trn>Glossary</digi:trn>	
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<table width="100%" border="0">
					<tr>
						<td width="20%" valign="top">
						
						
	<div id="content" class="yui-skin-sam" style="width: 100%;">
	<div id="demo" class="yui-navset" style="font-family: Arial, Helvetica, sans-serif;">
		<ul class="yui-nav">
			<li class="selected">
				<a title='<digi:trn>AMP field definition glossary</digi:trn>'>
					<div>
						<digi:trn>Glossary</digi:trn>
					</div>
				</a>
			</li>
		</ul>
	<div class="yui-content" style="height: auto; font-size: 11px; font-family: Verdana, Arial, Helvetica, sans-serif;">
	<div style="padding: 2;">
						
						
							<div id="treeDiv">
					
							</div>
							
</div>
</div>							
</div>
</div>							
					<digi:secure group="Help Administrators">
							<a href="/help/showAddGlossary.do"><digi:trn>Add top level topic</digi:trn> </a>
					</digi:secure>
						</td>
						<td valign="top" width="100%">
						
						
	<div id="content" class="yui-skin-sam" style="width: 100%;">
	<div id="demo" class="yui-navset" style="font-family: Arial, Helvetica, sans-serif;">
		<ul class="yui-nav">
			<li class="selected">
				<a title='<digi:trn>Select items in the tree</digi:trn>'>
					<div id="nodeTitle">
						&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
				</a>
			</li>
		</ul>
	<div class="yui-content" style="height: auto; font-size: 11px; font-family: Verdana, Arial, Helvetica, sans-serif;">
	<div style="padding: 2;">

						
							<div id="nodeContentContainer">
								<div id="nodeTitle1" style="font-weight: bolder; font-size: large; margin: 5px;"></div>
								<div id="nodeContentDiv">
									<digi:trn>Please select node in glossary tree</digi:trn>
								</div>
								<div id="nodeEditorLinkDiv"></div>
								<div id="addEditDeleteLinksDiv" style="padding: 5px"></div>
							</div>
							
	  		</div>
		</div>
	  </div>
	</div>
							
							
						</td>
						<td align="right" valign="top">
						
							<table border="0">
								<tr>
									<td align="left">
									
	<div id="content" class="yui-skin-sam" style="width: 100%;">
	<div id="demo" class="yui-navset" style="font-family: Arial, Helvetica, sans-serif;">
		<ul class="yui-nav">
			<li class="selected">
				<a title='<digi:trn>Search Glossary</digi:trn>'>
					<div>
						<digi:trn>Search</digi:trn>
					</div>
				</a>
			</li>
		</ul>
	<div class="yui-content" style="height: auto; font-size: 11px; font-family: Verdana, Arial, Helvetica, sans-serif;">
	<div style="padding: 2;">

							<div id="searchContainer">
								<div>
									<input id="edtSearchField" type="text" name="searchGlossary">
								</div>
								<div>	
									<c:set var="lblSearchButton"><digi:trn>Search</digi:trn></c:set>
									<c:set var="lblResetButton"><digi:trn>Reset</digi:trn></c:set>
									<input id="btnSearchGlossary" type="button" value="${lblSearchButton}">
									<input id="btnResetSearch" type="button" value="${lblResetButton}">
								</div>
								<div id="searchResults" class="searchResults">
								</div>
							</div>

									
</div>
</div>						
</div>
</div>						
									
									</td>
								</tr>
							</table>
						
						
						
							
							
							
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

</div>
</digi:form>

<script type="text/javascript">

	<digi:context name="topicBodyAction" 		property="context/module/moduleinstance/topicBody.do" />
	<digi:context name="topicEditAction" 		property="context/editor/moduleinstance/showEditText.do" />
	<digi:context name="topicDeleteAction" 		property="context/module/moduleinstance/deleteTopic.do" />
	<digi:context name="topicSearchAction" 		property="context/module/moduleinstance/searchGlossary.do" />
	<digi:context name="topicShowAddAction" 	property="context/module/moduleinstance/showAddGlossary.do" />
	<digi:context name="topicTitleSaveAction" 	property="context/module/moduleinstance/saveTopicTitle.do" />
	
	var getBodyURL 		= '<%=topicBodyAction%>';
	var editorLink 		= '<%=topicEditAction%>';
	var deleteLink 		= '<%=topicDeleteAction%>';
	var addLink 		= '<%=topicShowAddAction%>';
	var searchLink 		= '<%=topicSearchAction%>';
	var titleSaveLink 	= '<%=topicTitleSaveAction%>';
	var curTitle = '';
	var curTopicId;
	var curEditorKey = '';
	var curNode = '';
	var titleEditMode = false;

	$(document).ready(function () {

		tree = new YAHOO.widget.TreeView('treeDiv',
		[
			<c:forEach var="node" items="${glossaryForm.tree}">
				${node.yahooJSdefinition},
			</c:forEach>
		]
		);
		
		tree.subscribe('clickEvent',function(event){
			curTopicId 		= event.node.data.ampHelpTopicId;
			curTitle		= event.node.label;
			curEditorKey	= event.node.data.ampEditorKey;
			curNode			= event.node;
			showGlossary(curTopicId, curTitle, curEditorKey);
//			showGlossary(event.node.data.ampHelpTopicId, event.node.label, event.node.data.ampEditorKey);
		});

		tree.render();

		$('input#btnSearchGlossary').click(function(e){
			doSearch($('input#edtSearchField').val());
		});

		$('input#btnResetSearch').click(function(e){
			doSearchReset();
		});
		
	});

	function showGlossary(glossId,glossName,editorKey){
		$('div#nodeEditorLinkDiv').html('');
		var resp=$.ajax({
			   type: 'POST',
			   url: getBodyURL,
			   data: ({helpTopicId : glossId}),
			   cache : false,
			   success: function(data,msg){
					$('div#nodeContentDiv').html(data);
					$('div#nodeTitle').html(glossName);
					<digi:secure group="Help Administrators">
						var lblEdit 		= '<digi:trn jsFriendly="true">Edit</digi:trn>';
						var lblEditTitle 	= '<digi:trn jsFriendly="true">Edit title</digi:trn>';
						var lblEditText 	= '<digi:trn jsFriendly="true">Edit text</digi:trn>';
						var lblAddChild 	= '<digi:trn jsFriendly="true">Add child</digi:trn>';
						var lblDeleteNode 	= '<digi:trn jsFriendly="true">Delete topic</digi:trn>';
						var key = editorKey;//event.node.data.ampEditorKey;
						var lang = '${requestScope["org.digijava.kernel.navigation_language"].code}';
						var linkWithParams = editorLink + '?id=' + key + '&lang=' + lang + '&referrer=' + window.location;
						var linkEdit = '<a href="'+linkWithParams+'" title="'+lblEditText+'">['+lblEditText+']</a>';
						var linkAddChild = '<a href="/help/showAddGlossary.do?nodeId='+glossId+'" title="'+lblAddChild+'">['+lblAddChild+']</a>';
						var linkDeleteNode = '<a href="javascript:deleteNode('+glossId+')" title="'+lblDeleteNode+'">['+lblDeleteNode+']</a>';
						$('div#nodeEditorLinkDiv').html(linkEdit+'&nbsp;&nbsp;'+linkAddChild+'&nbsp;&nbsp;'+linkDeleteNode);

						//var titleWithEditLink = '&nbsp;<a href="javascript:startTitleEdit()">'+glossName+' [Edit]</a>';
						var titleWithEditLink = glossName+'  [Edit]';
						$('div#nodeTitle').click(function(e){
							var event = e || window.event;
							doTitleEdit(event);
						});
						//alert(titleWithEditLink);
						$('div#nodeTitle').html(titleWithEditLink);
					</digi:secure>
			   },
		   	   error : function(XMLHttpRequest, textStatus, errorThrown){
			   	   	alert('<digi:trn jsFriendly="true">Error, cannot load glossary topic.</digi:trn>');
			   } 
		}).responseText;
	}
	
	function doSearch(term){
		var resp=$.ajax({
			   type: 'POST',
			   url: searchLink,
			   data: ({searchTerm : term}),
			   cache : false,
			   success: function(data,msg){
					$('div#searchResults').html(data);			   	
			   },
		   	   error : function(XMLHttpRequest, textStatus, errorThrown){alert('<digi:trn jsFriendly="true">Error, cannot search glossary </digi:trn>');} 
		}).responseText;
	}

	function doSearchReset(){
		$('div#searchResults').html('');
	}
	
	function deleteNode(topicId){
		if (!confirm('<digi:trn jsFriendly="true">Delete this glossary item?</digi:trn>')) return;
		var lastTimeStamp = new Date().getTime();
		var resp=$.ajax({
			   type: 'POST',
			   url: deleteLink+'~timestamp='+lastTimeStamp,
			   data: ({helpTopicId : topicId}),
			   cache : false,
			   success: function(data,msg){
					var node = tree.getNodeByProperty('ampHelpTopicId',topicId);
					$('div#nodeContentDiv').html('');
					$('div#nodeTitle').html('&nbsp;&nbsp;&nbsp;&nbsp;');
					$('div#nodeEditorLinkDiv').html('');
					if (!tree.removeNode(node,true)){
						alert('<digi:trn jsFriendly="true">problem with</digi:trn> '+ problem);
					}
					tree.render();
			   },
		   	   error : function(XMLHttpRequest, textStatus, errorThrown){alert('<digi:trn jsFriendly="true">Error, cannot delete glossary topic with id </digi:trn>'+topicId);} 
		}).responseText;
	}

	function doTitleEdit(e){
		var source = e.target || e.srcElement;
		var sName = source.tagName.toLowerCase();
		if(sName=='div'){
			showTitleEdit();
		}else if (sName=='input'){
			if (source.id == 'btnNewTitleSave'){
				saveTitleEdit()
			}else if (source.id == 'btnNewTitleCancel'){
				cancelTitleEdit();
			}			
		}
	}

	function showTitleEdit(){
		var translationWorning = '<digi:trn jsFriendly="true">You may need to re-translate new title</digi:trn>';
		var nodeHtml = '<input id="glossaryItemNewTitle" type="text" name="itemNewTitle" value="'+curTitle+'">';
		nodeHtml += '<input type="button" value="Save" id="btnNewTitleSave">';
		nodeHtml += '<input type="button" value="Cancel" id="btnNewTitleCancel">';
		nodeHtml += '<span style="color : red;">'+translationWorning+'</span>';
		$('div#nodeTitle').html(nodeHtml);
		$('#glossaryItemNewTitle').focus();
	}
	
	function saveTitleEdit(){
		var newTitle = $('#glossaryItemNewTitle').val();
		if (newTitle.trim()=='') return;
		var lastTimeStamp = new Date().getTime();
		var actionError = '<digi:trn jsFriendly="true">Error, cannot save topic title</digi:trn>';
		var resp=$.ajax({
			   type: 'POST',
			   url: titleSaveLink+'~timestamp='+lastTimeStamp,
			   data: ({nodeName : newTitle, nodeId : curTopicId}),
			   cache : false,
			   success: function(data,msg){
			   		if (msg.toLowerCase()=='success'){
						curNode.label = newTitle;
						//curNode.data.label = newTitle;
						curTitle = newTitle;
						tree.render(true);
			   		}else{
				   		alert(actionError);
			   		}
					showGlossary(curTopicId, curTitle, curEditorKey);
			   },
		   	   error : function(XMLHttpRequest, textStatus, errorThrown){alert(actionError);} 
		}).responseText;
	}
	
	function cancelTitleEdit(){
		showGlossary(curTopicId, curTitle, curEditorKey);
	}
</script>