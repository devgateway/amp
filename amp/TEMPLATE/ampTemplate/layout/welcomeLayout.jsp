<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:context name="displayThumbnail" property="/aim/displayThumbnail.do" />

<script>
function downloadFile(placeholder) {
	if (placeholder != '0') {
		window.location='/aim/downloadFileFromHome.do?placeholder='+placeholder;
	}
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
			    <tr height="47%">
			        <td valign="middle" align="center" >
						<a href="javascript:">
                        <img src="<%=displayThumbnail%>?placeholder=1" align="middle" border="0" height="150" width="230" onclick="downloadFile('1');">
						</a>
                    </td>
				</tr>	
				<tr height="47%">
			        <td valign="middle" align="center" >
						<a href="javascript:">
                        <img src="<%=displayThumbnail%>?placeholder=2" align="middle" border="0" height="150" width="230" onclick="downloadFile('2');">
						</a>
                    </td>
				</tr>
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
