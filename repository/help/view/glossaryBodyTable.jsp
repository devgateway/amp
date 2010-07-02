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
	<div style="padding: 2; text-align: center">
						
						
							<div id="treeDiv">
					
							</div>
							
</div>
</div>							
</div>
</div>							
							<a href="/help/showAddGlossary.do"><digi:trn>Add root topic</digi:trn> </a>
						</td>
						<td valign="top" width="100%">
						
						
	<div id="content" class="yui-skin-sam" style="width: 100%;">
	<div id="demo" class="yui-navset" style="font-family: Arial, Helvetica, sans-serif;">
		<ul class="yui-nav">
			<li class="selected">
				<a title='<digi:trn key="aim:helpSearch">Select items in the tree</digi:trn>'>
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
									Please select node in glossary tree
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
				<a title='<digi:trn key="aim:helpSearch">Search Glossary</digi:trn>'>
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
									<input id="btnSearchGlossary" type="button" value="Search">
									<input id="btnResetSearch" type="button" value="Reset">
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

	<digi:context name="topicBodyAction" property="context/module/moduleinstance/topicBody.do" />
	<digi:context name="topicEditAction" property="context/editor/moduleinstance/showEditText.do" />
	<digi:context name="topicDeleteAction" property="context/module/moduleinstance/deleteTopic.do" />
	<digi:context name="topicSearchAction" property="context/module/moduleinstance/searchGlossary.do" />
	<digi:context name="topicShowAddAction" property="context/module/moduleinstance/showAddGlossary.do" />
	
	var getBodyURL = '<%=topicBodyAction%>';
	var editorLink = '<%=topicEditAction%>';
	var deleteLink = '<%=topicDeleteAction%>';
	var addLink = '<%=topicShowAddAction%>';
	var searchLink = '<%=topicSearchAction%>';

	$(document).ready(function () {

		tree = new YAHOO.widget.TreeView('treeDiv',
		[
			<c:forEach var="node" items="${glossaryForm.tree}">
				${node.yahooJSdefinition},
			</c:forEach>
		]
		);
		
		tree.subscribe('clickEvent',function(event){
			showGlossary(event.node.data.ampHelpTopicId, event.node.label, event.node.data.ampEditorKey);
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
						var key = editorKey;//event.node.data.ampEditorKey;
						var lang = '${requestScope["org.digijava.kernel.navigation_language"].code}';
						var linkWithParams = editorLink + '?id=' + key + '&lang=' + lang + '&referrer=' + window.location;
						var linkEdit = '<a href="'+linkWithParams+'">Edit text</a>';
						var linkAddChild = '<a href="/help/showAddGlossary.do?nodeId='+glossId+'">Add child</a>';
						var linkDeleteNode = '<a href="javascript:deleteNode('+glossId+')">Delete node</a>';
						$('div#nodeEditorLinkDiv').html(linkEdit+'&nbsp;&nbsp;'+linkAddChild+'&nbsp;&nbsp;'+linkDeleteNode);
					</digi:secure>
			   },
		   	   error : function(XMLHttpRequest, textStatus, errorThrown){
			   	   	alert('Error, cannot load glossary topic.');
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
		   	   error : function(XMLHttpRequest, textStatus, errorThrown){alert('Error, cannot search glossary ');} 
		}).responseText;
	}

	function doSearchReset(){
		$('div#searchResults').html('');
	}
	
	function deleteNode(topicId){
		if (!confirm("Delete this glossary item?")) return;
		var resp=$.ajax({
			   type: 'POST',
			   url: deleteLink,
			   data: ({helpTopicId : topicId}),
			   cache : false,
			   success: function(data,msg){
					var node = tree.getNodeByProperty('ampHelpTopicId',topicId);
					$('div#nodeContentDiv').html('');
					$('div#nodeTitle').html('&nbsp;&nbsp;&nbsp;&nbsp;');
					$('div#nodeEditorLinkDiv').html('');
					tree.removeNode(node,true);
			   },
		   	   error : function(XMLHttpRequest, textStatus, errorThrown){alert('Error, cannot delete glossary topic with id '+topicId);} 
		}).responseText;
	}
	
</script>