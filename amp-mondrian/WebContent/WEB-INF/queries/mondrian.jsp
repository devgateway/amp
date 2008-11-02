<%@ page session="true" contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<jp:mondrianQuery id="query01" jdbcDriver="com.mysql.jdbc.Driver" jdbcUrl="jdbc:mysql://localhost/amp_ethiopia" catalogUri="/WEB-INF/queries/AMP.xml"
   jdbcUser="root" jdbcPassword="" connectionPooling="true">
SELECT {[Measures].[Actual Commitments], [Measures].[Actual Disbursements],[Measures].[Planned Commitments], [Measures].[Planned Disbursements]} ON COLUMNS,
{[Time].[All Periods]} ON ROWS 
FROM [Donor Funding] 
</jp:mondrianQuery>





<c:set var="title01" scope="session">Test Query uses Mondrian OLAP</c:set>
