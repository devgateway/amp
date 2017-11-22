<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>



<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript">
	var idOfFolderTrees = ['dhtmlgoodies_tree'];
</script>
<link rel="stylesheet" href="<digi:file src="module/aim/css/css_dhtmlsuite/folder-tree-static.css" />" />
<link rel="stylesheet" href="<digi:file src="module/aim/css/css_dhtmlsuite/context-menu.css" />" />

<digi:instance property="aimReportWizardForm" />
<bean:define name="aimReportWizardForm" id="myForm" type="org.digijava.module.aim.form.reportwizard.ReportWizardForm"/>
	<p id="columnTree">
	
	<c:if test="${empty aimReportWizardForm.ampTreeColumns}">  <!-- this element needs to exist at all times, else report wizard will crash later in
		== var menuItems = DHTMLSuite_tree.getElementsByTagName('LI'); == 
		dhtmlSuite-dragDropTree.js is library code and we do not want to make local changes to library code
	-->
		<ul id="dhtmlgoodies_tree" class="DHTMLSuite_tree"></ul>
	</c:if>
	
	<c:if test="${!empty aimReportWizardForm.ampTreeColumns}">
		<bean:define name="aimReportWizardForm" property="ampTreeColumns" id="ampTreeColumns" type="java.util.Map"  toScope="page"/>
		<logic:iterate name="ampTreeColumns" id="ampTreeColumn" type="java.util.Map.Entry" >
			<bean:define id="columnCollection" name="ampTreeColumn" property="value" type="java.util.ArrayList" scope="page"/>
			<logic:iterate name="columnCollection" id="ampColumnFromTree" type="org.digijava.module.aim.dbentity.AmpColumns">
				<script type="text/javascript" >
					insertColInfo('${ampColumnFromTree.columnId}', '${ampColumnFromTree.columnName}');
				</script>
			</logic:iterate>
		</logic:iterate>
		<!-- Because the donor report and the contribution report are now different (the donor has also the indicator columnns)
		we have to create different c:if for each report
		-->
		<font size="3">
		<ul id="dhtmlgoodies_tree" class="DHTMLSuite_tree">
			<bean:define name="aimReportWizardForm" property="ampTreeColumns" id="ampTreeColumns" type="java.util.Map"  toScope="page"/>
			<li noDrag="true">
				<a id="1" style="font-size: 12px;color:#0e69b3;text-decoration:none"><digi:trn key="aim:report:AMP" >AMP</digi:trn></a>
				<ul class="nodragul wizard-column-padding">
					<logic:iterate name="ampTreeColumns" id="ampTreeColumn" type="java.util.Map.Entry" >
						<bean:define id="themeColumn" name="ampTreeColumn" property="key" type="java.lang.String" scope="page"/>
						<bean:define id="columnCollection" name="ampTreeColumn" property="value" type="java.util.ArrayList" scope="page"/>
						<div id="limodule:<bean:write name="themeColumn"/>" noDrag="true">
							<li id="limodule:<bean:write name="themeColumn"/>" noDrag="true">
								<a id="module:<bean:write name="themeColumn"/>" style="font-size: 12px;text-decoration:none">
									<digi:trn key="aim:report:${themeColumn}"><bean:write name="themeColumn"/></digi:trn>
								</a>
								<ul name="dhtmltreeArray" class="wizard-columns-list">
									<logic:iterate name="columnCollection" id="ampColumnFromTree" type="org.digijava.module.aim.dbentity.AmpColumns">
										<li class="" draggable="true">
											<input type="checkbox" style='line-height:15px; margin-top:6px;' id="fieldVis:<bean:write name="ampColumnFromTree" property="columnId"/>" name="selectedColumns" value="<bean:write name="ampColumnFromTree" property="columnId"/>"/>
											<a id="field:<bean:write name="ampColumnFromTree" property="columnId"/>" style="font-size: 11px;text-decoration:none"></a>
											<span style="font-size: 11px; text-decoration:none">
												<digi:colNameTrn><bean:write name="ampColumnFromTree" property="columnName"/></digi:colNameTrn>
												<logic:notEmpty name="ampColumnFromTree" property="description">
													<img src= "../ampTemplate/images/help.gif" border="0" title="<digi:trn key="aim:report:tip:${ampColumnFromTree.columnName}:${ampColumnFromTree.description}">${ampColumnFromTree.description}</digi:trn>">
												</logic:notEmpty>
											</span>
										</li>
									</logic:iterate>
								</ul>
							</li>
						</div>
					</logic:iterate>
				</ul>
			</li>
		</ul>
		</font>
	</c:if>
</p>