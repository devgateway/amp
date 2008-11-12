<%@ page session="true" contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<jp:mondrianQuery id="query01" jdbcDriver="com.mysql.jdbc.Driver" jdbcUrl="jdbc:mysql://localhost/amp_generic" catalogUri="/WEB-INF/queries/AMP.xml"
   jdbcUser="amp" jdbcPassword="" connectionPooling="true">
SELECT {[Measures].[Actual Commitments]} ON COLUMNS,
{[Donor]} ON ROWS 
FROM [Donor Funding] 
</jp:mondrianQuery>




<c:set var="title01" scope="session">Test Query uses Mondrian OLAP</c:set>
