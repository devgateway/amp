<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>

<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<c:choose>
	<c:when test="${param.queryEngine!='true' }">
		<c:set var="divAlign"></c:set>
	</c:when>
	<c:otherwise>
		<c:set var="divAlign">right: 1px;</c:set>
	</c:otherwise>
</c:choose>


<div id="show_legend_pop_box" style="font-size: 11px;font-family: Arial,sans-serif; ${divAlign}">
	<b class="red">*<digi:trn key="aim:redtext">red</digi:trn></b> - <digi:trn key="aim:newDraftText">New Draft. These are new activities that have been saved as drafts.</digi:trn>
	<hr/>
	<module:display name="Activity Approval Process" parentModule="PROJECT MANAGEMENT">
		<b class="green">*<digi:trn key="aim:greentext">green text</digi:trn></b> - <digi:trn key="aim:newUnvalidatedText">New Unvalidated. These are new activities that have never been approved by the workspace manager.</digi:trn>
	</module:display>
	<hr/>
	<b class="blue"><digi:trn key="aim:bluetext">blue text</digi:trn></b> - <digi:trn key="aim:validatedText">Validated activities</digi:trn>
	<hr/>
	<b class="red"><digi:trn key="aim:redtext">red</digi:trn></b> - <digi:trn key="aim:existingDraftsText">Existing Drafts. These are activities that have been saved once and edited and saved as drafts.</digi:trn>
	<hr/>
	<b class="green"><digi:trn key="aim:greentext">green text</digi:trn></b> - <digi:trn key="aim:existingUnvalidatedText">Existing Un-validated. These are activities that have been approved at least once by the workspace manager. These are edited activities that are not approved.</digi:trn>
</div>

	<c:set var="translation">
		<digi:trn key="aim:clickToViewLegend">Click here to view Legend</digi:trn>
	</c:set>
	&nbsp;
	<a id="show_legend_pop" style="cursor: pointer;text-decoration: underline;" onclick="javascript:showlegend()">
		<digi:trn>Show Legend</digi:trn> <img src="/TEMPLATE/ampTemplate/img_2/ico_arr_dwn.gif"/>
	</a>

