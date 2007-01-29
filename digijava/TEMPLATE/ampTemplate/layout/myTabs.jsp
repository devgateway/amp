<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<link rel="stylesheet" href="<digi:file src="module/aim/scripts/ajaxtabs/ajaxtabs.css"/>" />

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/relatedLinks.js"/>"></script>
<digi:context name="digiContext" property="context" />

<link rel="stylesheet"
	href="<digi:file src="module/aim/css/newamp.css"/>" />
<link rel="stylesheet"
	href="<digi:file src="module/aim/css/mktree.css"/>" />

<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<!-- script for tree-like view (drilldown reports) -->
<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/mktree.js"/>"></script>

<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/arFunctions.js"/>"></script>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/ajaxtabs/ajaxtabs.js"/>"></script>


<p><b>Documentation:</b> <a href="http://www.dynamicdrive.com/dynamicindex17/ajaxtabscontent/">http://www.dynamicdrive.com/dynamicindex17/ajaxtabscontent/</a></p>
<br />

<ul id="maintab" class="shadetabs">
<li class="selected"><a href="#default" rel="ajaxcontentarea">Intro</a></li>
<li><a href="/aim/viewNewAdvancedReport.do~viewFormat=tree~ampReportId=10~hideBars=true" rel="ajaxcontentarea">Report 1</a></li>
<li><a href="external2.htm" rel="ajaxcontentarea">Dog</a></li>
<li><a href="external3.htm" rel="ajaxcontentarea">Cat</a></li>
<li><a href="external4.htm" rel="ajaxcontentarea">Sea Otter</a></li>
</ul>


<div id="ajaxcontentarea" class="contentstyle">
<p>This is some default tab content, embedded directly inside this space and not via Ajax. It can be shown when no tabs are automatically selected, or associated with a certain tab, in this case, the first tab.</p>
<p><b><a href="javascript: expandtab('maintab', 2)">Select 3rd tab of "maintab"</a></b></p>
</div>

<script type="text/javascript">
//Start Ajax tabs script for UL with id="maintab" Separate multiple ids each with a comma.
startajaxtabs("maintab")
</script>