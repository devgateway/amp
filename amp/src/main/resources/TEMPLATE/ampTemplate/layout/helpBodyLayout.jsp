<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<digi:context name="url" property="context/module/moduleinstance/"/>
<TABLE width="100%" height="100%" cellpadding="4" cellspacing="2" border="0" class="help_body_table">
<TR height="100%" valign="top">
    <TD align="left" width="20%" valign="top">
        <c:choose>
            <c:when test="${url=='/help'}">
                 <digi:insert attribute="helpTopics" />
            </c:when>
            <c:otherwise>
                  <digi:insert attribute="adminHelpTopics" />
            </c:otherwise>
        </c:choose>
    </TD>
    <TD align="left" width="60%" valign="top">
        <digi:insert attribute="helpBody" />
    </TD>
    <TD align="left" width="20%" valign="top">
    &nbsp;
        <digi:insert attribute="rightColumn" />
            
    </TD>
	</TR>
</TABLE>
