<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>

<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<div style='position:relative;display:none;' id='legendPopupTitle'><digi:trn>Legend</digi:trn></div>

	<div style="font-size: 11px;font-family: Arial,sans-serif" id='show_legend_pop_box'> 
		<b class="red">*<digi:trn>star</digi:trn></b> - <digi:trn>indicates public version of the document</digi:trn> 
		<hr/>
		<b class="green">*<digi:trn>star</digi:trn></b> - <digi:trn>indicates shared version of the document</digi:trn>
		<hr/>
		<b class="red"><digi:trn>red text</digi:trn></b> - <digi:trn>Document Needs Aproval To Become Team Doc.</digi:trn>
		<hr/>
		<b class="green"><digi:trn>green text</digi:trn></b> - <digi:trn>Some Version of this Document needs Approval</digi:trn>	
</div>
<c:set var="translation">
		<digi:trn>Click here to view Legend</digi:trn>
	</c:set>
	

<div align="left" width="2" style="display: inline; cursor: default;text-decoration: underline;"
	onMouseOver="stm([document.getElementById('legendPopupTitle').innerHTML,document.getElementById('show_legend_pop_box').innerHTML],Style[16])" 
	onMouseOut="htm()">
	<c:set var="translation">
		<digi:trn>Click here to view Legend</digi:trn>
	</c:set>
	<span style="font-style: italic;color: #093678">
		<digi:trn>Show Legend</digi:trn>
		<img src="/TEMPLATE/ampTemplate/img_2/ico_arr_dwn.gif" />
	</span>
</div>
	