<%@ page session="true" contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<jp:mondrianQuery id="query01" jdbcDriver="com.mysql.jdbc.Driver" jdbcUrl="jdbc:mysql://localhost/amp_generic" catalogUri="/WEB-INF/queries/AMP.xml"
   jdbcUser="amp" jdbcPassword="" connectionPooling="true">
select NON EMPTY {[Measures].[Actual Commitments], [Measures].[Weighted Actual Commitments], [Measures].[Sector Percentage]} ON COLUMNS,
  NON EMPTY Crossjoin(Hierarchize(Union(Union({[Primary Sector].[All Primary Sectors]}, [Primary Sector].[All Primary Sectors].Children), [Primary Sector].[All Primary Sectors].[DAC-CRS Sector Scheme].Children)), {[Activity].[All Activities].[Activity 6], [Activity].[All Activities].[Activity 7], [Activity].[All Activities].[Activity 8]}) ON ROWS
from [Donor Funding Weighted]
</jp:mondrianQuery>




<c:set var="title01" scope="session">Donor Funding Amounts Weighted to Sector Percentages</c:set>
