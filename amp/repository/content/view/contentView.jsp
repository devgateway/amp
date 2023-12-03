<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<digi:instance property="contentForm" />

<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/dscript120_ar_style.js"/>"></script>

<script language="javascript" type="text/javascript">
    function downloadFile(index, pageCode) {
//        if (index != '0') {
            window.location='/content/downloadFile.do?index='+index+'&pageCode=' + pageCode;
//            window.location='/aim/downloadFileFromHome.do?index='+index;
//        }
    }

	var labelText = [];
	
    function attachFuncToThumbnail(index, pageCode) {
		var idThumbnail = "displayThumbnail_" + index;
		
        var lastTimeStamp = new Date().getTime();
        var url = '/content/displayThumbnail.do?index='+index+'&pageCode='+pageCode+'&labelText=true&timestamp='+lastTimeStamp;
        $.get(url, function(data) {
        	returnData = data.split('*');
			hasRelDoc = returnData[0];
			labelText[index] = returnData[1];

            if (hasRelDoc != 'true')
			{
                $("#"+idThumbnail).click(function() {
                    var msg="<digi:trn jsFriendly='true'>No related documents to download!</digi:trn>";
                    alert(msg);
                });
            }
            else
			{
				
                $("#"+idThumbnail).click(function() {
                    downloadFile(index, pageCode);
                });
            }
            
            if(labelText[index] != null && labelText[index] != "null")
			{
				if (labelText[index].length > 0){
					$("#"+idThumbnail).mouseover(function() {
						 stm(['<digi:trn jsFriendly="true">Description</digi:trn>',labelText[index]],Style[1]);
					});
					$("#"+idThumbnail).mouseout(function() {
						 htm();
					});
				}
            }
        });
        $("#"+idThumbnail).attr("style", "  ");	
    }

</script>

<c:set var="contentLayout" value="${contentForm.contentLayout}"/>
<c:set var="htmlblock_1" value="${contentForm.htmlblock_1}"/>
<c:set var="htmlblock_2" value="${contentForm.htmlblock_2}"/>
<c:set var="pageCode" value="${contentForm.pageCode}"/>
<c:set var="thumbnailCount" value="${fn:length(contentForm.contentThumbnails)}"/>


<c:import url="/repository/content/view/layout_${contentLayout}.jsp">
	<c:param name="htmlblock_1" value="${htmlblock_1}"/>
	<c:param name="htmlblock_2" value="${htmlblock_2}"/>
	<c:param name="pageCode" value="${pageCode}"/>
	<c:param name="thumbnailCount" value="${thumbnailCount}"/>
</c:import>



