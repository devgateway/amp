<%@ page session="true" contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<jp:mondrianQuery id="query01" jdbcDriver="com.mysql.jdbc.Driver" jdbcUrl="jdbc:mysql://localhost/amp_generic" catalogUri="/WEB-INF/queries/AMP.xml"
   jdbcUser="amp" jdbcPassword="" connectionPooling="true">
select NON EMPTY Crossjoin({[Donor Dates].[All Periods]}, {[Measures].[Actual Commitments], [Measures].[Sector Percentage], [Measures].[Actual Disbursements], [Measures].[Actual Expenditures], [Measures].[Planned Commitments], [Measures].[Planned Disbursements], [Measures].[Planned Expenditures]}) ON COLUMNS,
  NON EMPTY Crossjoin(Hierarchize({([Financing Instrument].[All Financing Instruments], [Terms of Assistance].[All Terms of Assistances], [Donor].[All Donors], [Primary Sector].[All Primary Sectors])}), {[Activity].[All Activities]}) ON ROWS
from [Donor Funding Weighted]
</jp:mondrianQuery>




<c:set var="title01" scope="session">Test Query uses Mondrian OLAP</c:set>
