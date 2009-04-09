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


<digi:instance property="deExportForm" />
<bean:define name="deExportForm" id="myForm" type="org.digijava.module.dataExchange.form.ExportForm"/>
<digi:form action="/exportWizard.do?method=prepear" method="post">
																
<script type="text/javascript" src="<digi:file src="module/aim/scripts/dhtml-suite-for-applications.js"/>"></script>

                                            
																			<p id="columnTree">
																				<c:if test="${!empty deExportForm.activityTree}">
																						<font size="3">
																						  <ul id="dhtmlgoodies_tree" class="DHTMLSuite_tree">
                                                                                            <li noDrag="true">
																							 <input type=checkbox id="root" name="root" value="root" onclick="checkUncheckAll3();" />
																							 <a id="1" style="font-size: 12px;color:#0e69b3;text-decoration:none">AMP</a>
																							 <ul class="nodragul">
                                                                                                 <bean:define id="tree" name="deExportForm" property="activityTree" type="org.digijava.module.dataExchange.type.AmpColumnEntry" toScope="page"/>
                                                                                                 <%= ExportHelper.renderActivityTree(tree, request) %>
																							 </ul>
																							</li>
                                                                                          </ul>
																						</font>
																				</c:if>
																			</p>



</digi:form>



