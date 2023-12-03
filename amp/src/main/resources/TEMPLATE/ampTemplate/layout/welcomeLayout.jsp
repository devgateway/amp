<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/dscript120_ar_style.js"/>"></script>
<digi:context name="displayThumbnail" property="/aim/displayThumbnail.do" />

<script language="javascript" type="text/javascript">
    function downloadFile(placeholder) {
        if (placeholder != '0') {
            window.location='/aim/downloadFileFromHome.do?placeholder='+placeholder;
        }
    }

    var title1 = "";
    var title2 = "";
    
    function attachFuncToThumbnail(placeholder) {
		var id="displayThumbnail"+placeholder;
        var lastTimeStamp = new Date().getTime();
        var url='/aim/displayThumbnail.do?placeholder='+placeholder+'&relDocs=relDocs'+'&timestamp='+lastTimeStamp;
        $.get(url, function(data) {
        	temp = data.split('*');
            if(temp[0]!='true'){
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
            
            if(temp[1]!=null && temp[1]!="null"){
                //document.getElementById("displayThumbnail"+placeholder).title = temp[1];
				if (placeholder==1) {
					title1 = temp[1];
					if (title1!=null && title1.length!=0){
						$("#"+id).mouseover(function() {
		            	     stm(['<digi:trn jsFriendly="true">Description</digi:trn>',title1],Style[1]);
		                });
		            	$("#"+id).mouseout(function() {
		                	 htm();
		                });
					}
				} else {
					title2 = temp[1];
					if (title2!=null && title2.length!=0){
						$("#"+id).mouseover(function() {
		            	     stm(['<digi:trn jsFriendly="true">Description</digi:trn>',title2],Style[1]);
		                });
		            	$("#"+id).mouseout(function() {
		                	 htm();
		                });
					}
				}
            }
        });
    }

</script>
 
<DIV id="TipLayer"
  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
<table width="100%" >
	<tr>
		<td  width="5%" />
		<td  width="60%">
			<digi:edit key="um:welcomeAmp" maxLength="1500"></digi:edit>
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
				
			</table>		
		</td>
	</tr>
</table>
