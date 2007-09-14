<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<TABLE height="100%" width="100%" border="1">
	<TR height="50%" valign="top">
		<TD valign="top">
		<table height="100%" border="0">
			<TR height="15">
				<TD align="left"><digi:insert attribute="search" /></TD>
			</TR>
			<digi:secure actions="ADMIN, TRANSLATE">
			<TR height="50%">
				<TD height="15" valign="top">
				<digi:insert attribute="create_Edit" /></TD>				
			</TR>
			</digi:secure>
		</table>
		</TD>
	</TR>
</TABLE>