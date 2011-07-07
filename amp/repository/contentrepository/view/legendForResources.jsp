<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>

<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<div style='position:relative;display:none;' id='legendPopupTitle'><digi:trn>Legend</digi:trn></div> 
<div style='position:relative;display:none;' id='legendPopup'> 
	<ul>
		<li>
			<font color="RED">* <digi:trn>star</digi:trn></font> - <digi:trn>indicates public version of the document</digi:trn>
		</li>		
		<li>
			<font color="#00CC00">* <digi:trn>star</digi:trn></font> - <digi:trn>indicates shared version of the document</digi:trn>
		</li>
		<li>
			<font color="RED"><digi:trn>red text</digi:trn></font> - <digi:trn>Document Needs Aproval To Become Team Doc.</digi:trn>
		</li>
		<li>
			<font color="#00CC00"><digi:trn>green text</digi:trn></font> - <digi:trn>Some Version of this Document needs Approval</digi:trn>
		</li>		
	</ul>
</div>
<div align="left" width="2" style="display: inline; cursor: default"
	onMouseOver="stm([document.getElementById('legendPopupTitle').innerHTML,document.getElementById('legendPopup').innerHTML],Style[13])" 
	onMouseOut="htm()" onclick="stm([document.getElementById('legendPopupTitle').innerHTML,document.getElementById('legendPopup').innerHTML],Style[13])">
	<c:set var="translation">
		<digi:trn>Click here to view Legend</digi:trn>
	</c:set>
	<span style="font-style: italic;color: #093678">
		<digi:trn>Show Legend</digi:trn>
	</span>
</div>