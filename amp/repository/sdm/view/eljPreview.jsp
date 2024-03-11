<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@taglib uri="/taglib/editlivejava" prefix="elj" %>

<digi:errors />
<digi:instance property="sdmForm"/>
  <bean:define id="parsedText" name="sdmForm" property="content" type="java.lang.String" />
		<%=parsedText%>        
