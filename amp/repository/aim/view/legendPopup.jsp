<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>

<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<div style='position:relative;display:none;' id='legendPopup'> 
	<ul>
		<li>
			<font color="GREEN">* "green Text"</font> - New Unvalidated. These are new activites that have never been approved by the workspace manager
		</li>
		<li>
			<font color="GREEN">"green text"</font> - Existing Un-validated. These are activites that have been approved at least once by the workspace manager. These are edited actvities that are not approved.
		</li>
		<li>
			<font color="RED">* "red"</font> - New Draft. These are new actvities that have been saved as drafts.
		</li>
		<li>
			<font color="RED">"red"</font> - Existing Drafts. These are actvities that have been saved once and edited and saved as drafts.
		</li>
	</ul>
</div>
<div align="left" width="2" style="display: inline"
	onMouseOver="stm(['legendPopup',document.getElementById('legendPopup').innerHTML],Style[0])" 
	onMouseOut="htm()">
	<jsp:useBean id="urlParams22" type="java.util.Map" class="java.util.HashMap"/>
	<c:set var="translation">
		<digi:trn key="aim:clickToViewLegend">Click here to view Legend</digi:trn>
	</c:set>
	<digi:link href="/getWorkspace.do" name="urlParams22" >
		<digi:trn key="aim:showLegend">Show Legend</digi:trn>
	</digi:link>
</div>