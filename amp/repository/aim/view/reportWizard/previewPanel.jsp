<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<div id="fakePreviewSectionDiv" style="display: none; height: 50px;">&nbsp;</div>
<div id="previewSectionDiv" style="display: none; width: 930px; height: 220px;  background-color: white;">
	<div id="previewHeaderSectionDiv" style="width: 100%"><span onclick="togglePreview()"><digi:trn key="aim:preview">Preview</digi:trn> <img id="previewHeaderSectionImg" src="../../../../TEMPLATE/ampTemplate/images/preview_open.gif" /></span></div>
	<div id="previewBodySectionDiv" class="content-direction" style="width: 100%; margin-bottom: 5px; margin-top: 5px; height: 180px; overflow:auto; overflow-x: scroll; white-space: nowrap;"></div>
</div>