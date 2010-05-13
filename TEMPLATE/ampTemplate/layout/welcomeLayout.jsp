<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:context name="displayThumbnail" property="/aim/displayThumbnail.do" />

<script language="javascript" type="text/javascript">
    function downloadFile(placeholder) {
        if (placeholder != '0') {
            window.location='/aim/downloadFileFromHome.do?placeholder='+placeholder;
        }
    }
  
    function attachFuncToThumbnail(placeholder) {
        var id="displayThumbnail"+placeholder;
        var lastTimeStamp = new Date().getTime();
        var url='/aim/displayThumbnail.do?placeholder='+placeholder+'&relDocs=relDocs'+'&timestamp='+lastTimeStamp;
        $.get(url, function(data) {
            if(data!='true'){
                $("#"+id).click(function() {
                    var msg="<digi:trn>No related documents to download!</digi:trn>"
                    alert(msg);
                });
            }
            else{
                $("#"+id).click(function() {
                    downloadFile(placeholder);
                });

            }
        });
    }

</script>
 
<table width="100%" >
	<tr>
		<td  width="5%" />
		<td  width="60%" >
			<digi:edit key="um:welcomeAmp"></digi:edit>
		</td>
		<td  width="5%" />
		<td width="70%" bgcolor="#dbe5f1">
			<table width="100%" height="400" cellpadding="3" cellspacing="0" >
              <c:forEach var='placeholder' begin='1' end='2'>
			    <tr height="47%">
			        <td valign="middle" align="center" >
						<a style="cursor: pointer">
                            <img id="displayThumbnail${placeholder}" src="${displayThumbnail}?placeholder=${placeholder}" align="middle" border="0" height="150" width="230" onload="attachFuncToThumbnail(${placeholder})">
						</a>
                    </td>
				</tr>
              </c:forEach>
				<tr height="6%">
			        <td valign="middle" align="center" >
						<digi:trn key="clickImageToDownload">
					 	    Click the images to download related documents    				
					  	</digi:trn>
                    </td>
				</tr>
			</table>		
		</td>
	</tr>
</table>
