<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi" %>
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
