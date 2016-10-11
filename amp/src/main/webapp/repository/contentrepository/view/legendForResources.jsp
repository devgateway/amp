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
<%--
<c:choose>
	<c:when test="${param.queryEngine!='true' }">
		<c:set var="divAlign"></c:set>
	</c:when>
	<c:otherwise>
		<c:set var="divAlign">right: 1px;</c:set>
	</c:otherwise>
</c:choose>

 --%>

<bean:define id="myLegendDivId" toScope="page" scope="request" name="legendDivId"/>

<div id="show_legend_pop_box_${myLegendDivId}" style="font-size: 11px;font-family: Arial,sans-serif;">
	<b class="red">*<digi:trn>star</digi:trn></b> - <digi:trn>indicates public version of the document</digi:trn> 
		<hr/>
		<b class="green">*<digi:trn>star</digi:trn></b> - <digi:trn>indicates shared version of the document</digi:trn>
		<hr/>
		<b class="red"><digi:trn>red text</digi:trn></b> - <digi:trn>Document Needs Approval To Become Team Doc.</digi:trn>
		<hr/>
		<b class="green"><digi:trn>green text</digi:trn></b> - <digi:trn>Some Version of this Document needs Approval</digi:trn>
</div>

	<c:set var="translation">
		<digi:trn>Click here to view Legend</digi:trn>
	</c:set>
	&nbsp;
	<a id="show_legend_pop" style="cursor: pointer;text-decoration: underline;" onclick="javascript:showlegend('${myLegendDivId}')">
		<digi:trn>Show Legend</digi:trn> <img src="/TEMPLATE/ampTemplate/img_2/ico_arr_dwn.gif"/>
	</a>

