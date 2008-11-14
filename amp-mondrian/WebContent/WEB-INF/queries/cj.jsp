<%@ page session="true" contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<jp:mondrianQuery id="query01" jdbcDriver="com.mysql.jdbc.Driver" jdbcUrl="jdbc:mysql://localhost/amp_burundi" catalogUri="/WEB-INF/queries/AMP.xml"
   jdbcUser="amp" jdbcPassword="" connectionPooling="true">
SELECT NON EMPTY CROSSJOIN({[Donor Dates]},{[Measures].[Actual Commitments]}) ON COLUMNS,
NON EMPTY CROSSJOIN({[Financing Instrument]},CROSSJOIN({[Terms of Assistance]},CROSSJOIN ({[Donor]},CROSSJOIN({[Primary Sector]},{[Activity]})))) ON ROWS 
FROM [Donor Funding Weighted] 
</jp:mondrianQuery>




<c:set var="title01" scope="session">Test Query uses Mondrian OLAP</c:set>
