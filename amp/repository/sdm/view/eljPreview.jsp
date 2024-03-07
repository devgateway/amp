<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi" %>
<%@taglib uri="/src/main/webapp/WEB-INF/taglib.tld" prefix="elj" %>

<digi:errors />
<digi:instance property="sdmForm"/>
  <bean:define id="parsedText" name="sdmForm" property="content" type="java.lang.String" />
		<%=parsedText%>        
