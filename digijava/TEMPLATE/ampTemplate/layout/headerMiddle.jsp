<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<TABLE cellSpacing=0 cellPadding=0 border=0 vAlign="top" align="left" height="20">
   <TR>
   	<TD>&nbsp;&nbsp;&nbsp;</TD>
      <TD width="1" bgcolor="#999999">
	  </TD>
      <TD valign=center>
			<bean:define id="translation">
				<digi:trn key="aim:enterIntoAIM">Enter in to Aid Information Module</digi:trn>
			</bean:define>
			<digi:link href="/showDesktop.do" module="aim" styleClass="nav" onclick="return quitRnot()"	title="<%=translation%>">
         	::: <digi:trn key="aim:aidInformationModule">AID INFORMATION MODULE</digi:trn>
		 	</digi:link>
	  </TD>
      <TD width="1" bgcolor="#999999">
		</TD>
      <TD valign=center>
			<digi:link href="/viewTeamReports.do" module="aim" styleClass="nav" onclick="return quitRnot()" title="View public team reports">
			:::  <digi:trn key="aim:reports">REPORTS</digi:trn>
		 	</digi:link>
		</TD>
      <TD width="1" bgcolor="#999999">
		</TD>
      <TD valign=center>
					<a class="nav">
			::: <digi:trn key="aim:documentsHeader">DOCUMENTS</digi:trn></a>
		 </TD>
      <TD width="1" bgcolor="#999999">
		</TD>
        <logic:notEmpty name="CL" scope="application">
          <TD vAlign=center>
                <digi:link module="calendar" href="/showCalendarView.do" styleClass="nav" onclick="return quitRnot()"	title="View Planning Calendar">
                  ::: <digi:trn key="aim:calendar">CALENDAR</digi:trn>
                </digi:link>
          </TD>
        </logic:notEmpty>
		<TD width="1" bgcolor="#999999">
		</TD>
		<%--
      <TD vAlign=center>
			<a class="nav">
         	::: <digi:trn key="aim:scenarios">SCENARIOS</digi:trn></a>
		</TD>
      <TD width="1" bgcolor="#999999">
		</TD>
		--%>
	<logic:notEmpty name="ME" scope="application">
      <TD vAlign="center"
				class="headerLink" onMouseOver="this.className='headerLinkOver'" onMouseOut="this.className='headerLink'">
			<digi:link href="/viewPortfolioDashboard.do~actId=-1~indId=-1" module="aim" styleClass="nav"
			onclick="return quitRnot()" title="View M&E Dashboard">
         	::: <digi:trn key="aim:medashboard">M & E DASHBOARD</digi:trn>
			</digi:link>
		</TD>
    </logic:notEmpty>
      <TD width="1" bgcolor="#999999">
		</TD>
	</TR>
</TABLE>
