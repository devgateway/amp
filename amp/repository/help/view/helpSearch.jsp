<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<digi:instance property="helpForm" />
<digi:form action="/helpActions.do?actionType=searchHelpTopic">
<TABLE align=center border=0 cellPadding=2 cellSpacing=3 width="100%" bgcolor="#f4f4f2">
	<TR>
		<TD class=r-dotted-lg-buttom vAlign=top>
			<TABLE border=0 cellPadding=0 cellSpacing=0 width="100%" >
        		<TR><TD>
              		<TABLE border=0 cellPadding=0 cellSpacing=0 >
              			<TR bgColor=#f4f4f2>
                 			<TD bgColor=#c9c9c7 class=box-title>
                 				<digi:trn key="help:search">Search</digi:trn>
                 			</TD>
                    		<TD background="module/aim/images/corner-r.gif"	height=17 width=17></TD>
						</TR>
					</TABLE>
				</TD></TR>
				<TR><TD bgColor=#ffffff class=box-border align=left>
					<TABLE>
						<TR>
							<TD>
								<html:text property="keywords" />
							</TD>
							<TD>
								<c:set var="searchtpc">
									<digi:trn key="help:SearchText">Search Topic</digi:trn>
								</c:set>
								<input type="submit" class="dr-menu"  value="${searchtpc}"/>
							</TD>
						</TR>
					</TABLE>
				</TD></TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</digi:form>