
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/admin/scripts/parser.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/admin/scripts/tree.js"/>"></script>
<link rel="stylesheet" href="<digi:file src="module/admin/css/tree.css"/>">
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/admin/scripts/propertyView.js"/>"></script>
<link rel="stylesheet" href="<digi:file src="module/admin/css/propertyView.css"/>">
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/admin/scripts/statusWnd.js"/>"></script>


<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
	<tr class="yellow">
		<td><digi:img src="module/admin/images/yellowLeftTile.gif" border="0" width="20"/></td>
		<td width="100%">
			<font class="sectionTitle">
				<digi:trn key="admin:configuration">Configuration</digi:trn>
			</font>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="yellow" valign="top" align="center" height="100%">
		
		<digi:errors/>
		<digi:form action="/showConfiguration.do" method="post">
			
			<html:hidden name="configurationForm" property="xmlString"/>
			<br>
			<table cellspacing="0" cellpadding="0" style="border: 1px solid black;" width="98%">
				<tr>
					<td colspan="3" height="1" class="configCell">
						<table border="0" cellpadding="0" cellspacing="0">
							<tr class="configContainerHeader">
								<td>
									<img src="<digi:file src="module/admin/images/spacer.gif"/>" width="5" height="20">
								</td>							
								<td>
									<img src="<digi:file src="module/admin/images/icons/bookIcon.gif"/>">
								</td>
								<td width="100%" align="left">
									&nbsp;Teaser/layout configuration
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="400" width="50%">
						<div id="treeContainer" class="container" style="width:100%; height:100%;">Processing XML data. Please wait ...</div>
					</td>
					<td width="5" class="configVerticalRow">
						<img src="<digi:file src="module/admin/images/spacer.gif"/>" width="5">
					</td>
					<td height="400" width="50%">
						<div id="propertyViewContainer" class="container" style="width:100%; height:100%;">
							<table class='propertyViewTable' border='1' width='100%' height='100%' cellpadding='0' cellspacing='0'>
								<tr>
									<td width='100%' colspan='2' class='digiBgr' height='100%'>
										&nbsp;
									</td>
								</tr>
							</table>
						</div>
					</td>			
				</tr>
				<tr class="configContainerHeader">
					<td height="1" class="configCell" width="270" colspan="2">
						<table border="0" cellpadding="0" cellspacing="0" width="100%">
							<tr class="configContainerHeader">
								<td align="center" width="20">
									<img src="<digi:file src="module/admin/images/configToolStart.gif"/>">
								</td>
								<td class="configToolsSeparator" width="2">
									<img src="<digi:file src="module/admin/images/spacer.gif"/>" width="2">
								</td>
								<td>
									<img src="<digi:file src="module/admin/images/spacer.gif"/>" width="5" height="20">
								</td>
								<td align="center">
									<digi:link href="/showConfiguration.do">
										<img src="<digi:file src="module/admin/images/icons/configReload.gif"/>" border="0">
									</digi:link>
								</td>
								<td>
									<img src="<digi:file src="module/admin/images/spacer.gif"/>" width="5">
								</td>								
								<td class="configToolsSeparator" width="2">
									<img src="<digi:file src="module/admin/images/spacer.gif"/>" width="2">
								</td>
								<td width="99%">
									<img src="<digi:file src="module/admin/images/spacer.gif"/>">
								</td>								
							</tr>
						</table>
					</td>
					<td height="1" class="configCell" width="280">
						<table border="0" cellpadding="0" cellspacing="0" width="100%">
							<tr class="configContainerHeader">
								<td align="center" width="20">
									<img src="<digi:file src="module/admin/images/configToolStart.gif"/>">
								</td>
								<td class="configToolsSeparator" width="2">
									<img src="<digi:file src="module/admin/images/spacer.gif"/>" width="2">
								</td>								
								<td>
									<img src="<digi:file src="module/admin/images/spacer.gif"/>" width="5" height="20">
								</td>
								<td width="99%">
									<img src="<digi:file src="module/admin/images/spacer.gif"/>">
								</td>								
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="3" style="background-color:#D6B85D" class="configCell" height="5">
						<img src="<digi:file src="module/admin/images/spacer.gif"/>" height="5" width="5">
					</td>
				</tr>
			</table>	
		
		</digi:form>
		</td>
	</tr>
</table>

			<script language="JavaScript">
			prepareView();
			
//			window.setTimeout ("parseXml()", 100);
			
			var progressBarWndPointer = null;
			
			function parseXml () {
				xmlSrc = document.getElementsByName("xmlString")[0].value;
				xmlDoc = new XMLDocument (xmlSrc);
				
				//Prepare parser rules
				xmlDoc.docRules.addRule(new XMLParserRuleItem(1, "site-layout"));
				xmlDoc.docRules.addRule(new XMLParserRuleItem(1, "module-layout"));
				xmlDoc.docRules.addRule(new XMLParserRuleItem(2, "layout"));
				xmlDoc.docRules.addRule(new XMLParserRuleItem(3, "module"));
				xmlDoc.docRules.addRule(new XMLParserRuleItem(4, "put"));
				xmlDoc.docRules.addRule(new XMLParserRuleItem(5, "put-item"));
				xmlDoc.docRules.addRule(new XMLParserRuleItem(6, "page"));
				xmlDoc.docRules.addRule(new XMLParserRuleItem(7, "teaser"));
				xmlDoc.docRules.addRule(new XMLParserRuleItem(7, "teaser-file"));

				openStatusWnd();
				setText ("Parsing XML ...");
				//parse document

				window.setTimeout ("xmlDoc.parse();", 50);
				cont = document.getElementById('treeContainer');
				
				window.setTimeout ("setText ('Building tree ...');", 50);				
				//setText ('Building tree ...');
				
				window.setTimeout ("buldTree(xmlDoc, cont)", 100);
//				window.setTimeout ("setSelectedById (1)", 100);
				window.setTimeout ("closeStatusWnd()", 200);
			}
			
			function prepareView() {
				setCollapseIconSrc ("<digi:file src="module/admin/images/tree/collapse.gif"/>");
				setExpandIconSrc ("<digi:file src="module/admin/images/tree/expand.gif"/>");
				setSpacerSrc ("<digi:file src="module/admin/images/tree/spacer.gif"/>");
				setRootIconSrc ("<digi:file src="module/admin/images/tree/icons/rootIcon.gif"/>");
				setCommentIconSrc ("<digi:file src="module/admin/images/tree/icons/comment.gif"/>");
				setDefaultIconSrc ("<digi:file src="module/admin/images/tree/icons/default.gif"/>");
				
				addType(new Type(1, "<digi:file src="module/admin/images/tree/icons/group.gif"/>"));
				
				var layoutType = new Type(2, "<digi:file src="module/admin/images/tree/icons/layout.gif"/>");
				layoutType.addNameExtra("name");
				addType(layoutType);
				
				var moduleType = new Type(3, "<digi:file src="module/admin/images/tree/icons/module.gif"/>");
				moduleType.addNameExtra("name");
				addType(moduleType);
				
				addType(new Type(4, "<digi:file src="module/admin/images/tree/icons/put.gif"/>"));

				var putItemType = new Type(5, "<digi:file src="module/admin/images/tree/icons/putItem.gif"/>");
				putItemType.addNameExtra("name");
				addType(putItemType);
				
				addType(new Type(6, "<digi:file src="module/admin/images/tree/icons/page.gif"/>"));
				addType(new Type(7, "<digi:file src="module/admin/images/tree/icons/teaser.gif"/>"));
				
				srcXMLString = document.getElementsByName("xmlString")[0].value;
				propertyViewContainer = document.getElementById ("propertyViewContainer");
			}
			

			function notifyParseProgressChange (val){
				setProgressValue (val);
			}
			
			function notifyTreeBuildProgressChange (val) {
				setProgressValue (val);
			}
			document.onload = window.setTimeout ("parseXml()", 100);

			</script>