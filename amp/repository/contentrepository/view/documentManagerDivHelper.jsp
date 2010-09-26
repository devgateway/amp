
<div id="viewVersions" style="display:none">
	<div align="center">
		<digi:trn>Please wait a moment... </digi:trn><br />
		<digi:img skipBody="true" src="module/contentrepository/images/ajax-loader-darkblue.gif" border="0" align="absmiddle" />
	</div>
</div>

<div id="tableTemplate" style="display:none">
	<table name="framingTable" border="0" cellPadding=5 cellSpacing=0 width="90%"
			style="position: relative; left: 2px; font-size: x-small" id="team_table">
			<tr>
				<td style="background-image:url(/repository/contentrepository/view/images/left-side.gif); background-repeat: no-repeat; background-position: top right" 
				width="13" height="20"> </td>
				<td bgcolor="#006699" class="textalb" height="20" width="97%" valign="middle" style="font-size: 11px; color:#FFFFFF; padding-bottom: 1px; padding-top: 1px">
					<a style="cursor:pointer" >
						<img
							border="0" align="absmiddle"
							src="/repository/contentrepository/view/images/dhtmlgoodies_minus.gif" name="otherDocumentsImg"/>
						<digi:img skipBody="true" height="16"
							border="0" align="absmiddle"
							src="module/contentrepository/images/folder_folder.gif" />
					</a>
					<button class="dr-menu" type="button" name="otherDocumentsButton" 
						style="font-size: 9px;">
						<digi:trn key="contentrepository:SelectButton">Select</digi:trn>
					</button>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>&nbsp;</span>
				</td>
				<td style="background-image:url(/repository/contentrepository/view/images/right-side.gif); background-repeat: no-repeat; background-position: top left" 
				width="13" height="20"> </td>
			</tr>
			<tr style="display: table-row;  " name="otherDocumentsTr" bgcolor="#f4f4f2">
				<td colspan="3" name="otherDocumentsTd" style="border-color: #006699; border-left-style: solid; border-left-width: thin; 
					border-bottom-style: solid; border-bottom-width: thin; border-right-style: solid; border-right-width: thin; ">
					<digi:trn key="contentrepository:Empty">Empty</digi:trn><a name="otherDocumentsDiv">&nbsp;</a>
				</td>
			</tr>
	</table>
</div>
