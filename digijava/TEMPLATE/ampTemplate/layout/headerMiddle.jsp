<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<TABLE cellSpacing=0 cellPadding=0 border=0 vAlign="top" align="left" height="28">
   <TR>	
   	<TD>&nbsp;&nbsp;&nbsp;</TD>
      <TD width="1" bgcolor="#999999">
			<%--
			<digi:img src="images/nav-div.gif" width="2"/>
			--%>
	  </TD>
      <TD valign=center>
			<bean:define id="translation">
				<digi:trn key="aim:enterIntoAIM">Enter in to Aid Information Module</digi:trn>
			</bean:define>
			<digi:link href="/viewMyDesktop.do" styleClass="nav" 
			title="<%=translation%>">
         	::: <digi:trn key="aim:aidInformationModule">AID INFORMATION MODULE</digi:trn>
		 	</digi:link>
	  </TD>
      <TD width="1" bgcolor="#999999">
			<%--
			<digi:img  src="images/nav-div.gif" width="2"/>
			--%>
		</TD>
      <TD valign=center>
			<digi:link href="/viewTeamReports.do" styleClass="nav" title="View public team reports">
			:::  <digi:trn key="aim:reports">REPORTS</digi:trn>
		 	</digi:link>			
		</TD>	  
      <TD width="1" bgcolor="#999999">
			<%--
			<digi:img  src="images/nav-div.gif" width="2"/>
			--%>
		</TD>
      <TD valign=center styleClass="nav">
          <a class="nav" >
			::: <digi:trn key="aim:documentsHeader">DOCUMENTS</digi:trn></a>
		 </TD>
      <TD width="1" bgcolor="#999999">
			<%--
			<digi:img  src="images/nav-div.gif" width="2"/>
			--%>
		</TD>
      <TD vAlign=center >
             <a class="nav">
         	::: <digi:trn key="aim:calendar">CALENDAR</digi:trn></a>
		</TD>
      <TD width="1" bgcolor="#999999">
			<%--
			<digi:img src="images/nav-div.gif" width="2" />
			--%>
		</TD>
      <TD vAlign=center >
			<a class="nav">
         	::: <digi:trn key="aim:scenarios">SCENARIOS</digi:trn></a>
		</TD>
      <TD width="1" bgcolor="#999999">
			<%--
			<digi:img  src="images/nav-div.gif" width="2"/>
			--%>
		</TD>
	</TR>
</TABLE>
