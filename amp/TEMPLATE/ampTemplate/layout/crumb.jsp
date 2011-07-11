<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<jsp:useBean id="Crumbs" class="java.util.ArrayList" scope="session" />
<div class="bd">
	<ul class="first-of-type">
      	<c:forEach items="${Crumbs}" var="crumb">
			<li class="yuimenuitem">
		
				<logic:notEmpty name="crumb" property="link">
					<html:link styleClass="pageNav" page="${crumb.link}" onclick="location.href=this.href; document.title=this.innerHTML;">
						<c:out value='${crumb.title}'/>
					</html:link>
					&nbsp;&nbsp;&nbsp;&nbsp;			
				</logic:notEmpty>
			
			</li>
		</c:forEach>
	</ul>
</div>