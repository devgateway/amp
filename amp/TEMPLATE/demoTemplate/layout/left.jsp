<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<!-- <b>T/SITEDOMAIN LEFT</b>&nbsp; -->

<table border="0" width="100%">
<tr><td>
   <digi:insert attribute="umUser" />
</td></tr>
<tr><td>
   <digi:insert attribute="showVerticalNavigation" />
</td></tr>
<tr><td>
   <digi:insert attribute="newsAll" />
</td></tr>
<tr><td>
   <digi:insert attribute="calendarItem" />
</td></tr>
</table>
