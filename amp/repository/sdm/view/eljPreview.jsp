<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@taglib uri="/src/main/resources/tld/taglib.tld" prefix="elj" %>

<digi:errors />
<digi:instance property="sdmForm"/>
  <bean:define id="parsedText" name="sdmForm" property="content" type="java.lang.String" />
		<%=parsedText%>        
