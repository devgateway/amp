<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@taglib uri="/taglib/editlivejava" prefix="elj" %>

<digi:errors />
<digi:instance property="sdmForm"/>
  <bean:define id="parsedText" name="sdmForm" property="content" type="java.lang.String" />
		<%=parsedText%>        
