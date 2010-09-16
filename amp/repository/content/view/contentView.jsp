<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/content" prefix="content" %>

<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>

<script language="javascript" type="text/javascript">
    function downloadFile(index, pageCode) {
        if (index != '0') {
            window.location='/content/downloadFile.do?index='+index+'&pageCode=' + pageCode;
//            window.location='/aim/downloadFileFromHome.do?index='+index;
        }
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
                    var msg="<digi:trn>No related documents to download!</digi:trn>"
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
						 stm(['<digi:trn>Description</digi:trn>',labelText[index]],Style[1]);
					});
					$("#"+idThumbnail).mouseout(function() {
						 htm();
					});
				}
            }
        });
    }

</script>

<c:if test="${empty param.c}">
	<c:set var="pageCode"><content:home/></c:set>
</c:if>
<c:if test="${!empty param.c}">
	<c:set var="pageCode">${param.c}</c:set>
</c:if>
<c:set var="layout">
	<content:get pageCode="${pageCode}" attribute="Layout"/>
</c:set>
<c:set var="htmlblock_1">
	<content:get pageCode="${pageCode}" attribute="Htmlblock_1"/>
</c:set>
<c:set var="htmlblock_2">
	<content:get pageCode="${pageCode}" attribute="Htmlblock_2"/>
</c:set>

<c:import url="/repository/content/view/layout_${layout}.jsp">
	<c:param name="pageCode" value="${pageCode}"/>
	<c:param name="htmlblock_1" value="${htmlblock_1}"/>
	<c:param name="htmlblock_2" value="${htmlblock_2}"/>
</c:import>



