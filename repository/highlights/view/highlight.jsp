<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:errors/>
<digi:instance property="highlightItemForm" />

<!-- view Hightlight -->
<c:if test="${ !empty highlightItemForm.previewItem}">
<c:set var="previewItem" value="${highlightItemForm.previewItem}" />
<div align="center">
<table width="100%">
<tr><td>
<!-- Layout1 -->
<c:if test="${highlightItemForm.layout1}">
  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY><TR>
        <TD colSpan=3>
           <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
              <TBODY><TR>
                 <TD bgColor=#00499b>
                   <DIV class=mheading><FONT color=#ffffff><c:out value="${previewItem.description}"/></FONT></DIV></TD>
			   </TR></TBODY>
		    </TABLE>
		  </TD></TR>
          <TR><TD colSpan=3></TD></TR>
          <TR>
              <TD rowSpan=4>&nbsp;</TD></TR>
          <TR><TD>
              <c:if test="${previewItem.title}">
              <b><h3><c:out value="${previewItem.title}" escapeXml="false" /></h3></b></c:if>
             <BR><SPAN class=date><FONT color=#666666 size=2>
			 <c:out value="${previewItem.creationDate}"/></FONT></SPAN>
		   </TD></TR>
           <TR><TD><IMG height=5 src="" width=1></TD></TR>
           <TR><TD colSpan=3></TD></TR><TR>
           <TD></TD>
           <TD>
             <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
                 <TBODY><TR>
                    <TD class=text>
                      <TABLE width="100%" cellSpacing=0 cellPadding=0 width=77 align=left border=0>
                         <TBODY><TR>
                            <TD align="center" valign="center">
                            <digi:context name="showImage" property="context/module/moduleinstance/showImage.do" />
                            <c:if test="${previewItem.haveImageSizes}">
                              <img src="<%= showImage%>?activeHighlightId=<c:out value='${previewItem.id}' />"  height="<c:out value='${previewItem.imageHeight}' />" width="<c:out value='${previewItem.imageWidth}' />" />
                            </c:if>
                           <c:if test="${! previewItem.haveImageSizes}">
                              <img src="<%= showImage%>?activeHighlightId=<c:out value='${previewItem.id}' />" />
                           </c:if><BR>
                           </TD>
                          </TR></TBODY>
					  </TABLE>
                     </TD></TR>
                     <TR><TD class=text>  
					 <c:if test="${!empty previewItem.topic}">
					  <c:out value="${previewItem.topic}" escapeXml="false"/>
 					 </c:if><BR><BR>
                        <FONT color=#0000cc><B>
                        Related links:</B><BR></FONT>
					</TD></TR></TBODY>
					</TABLE></TD></TR>
	           <c:if test="${ !empty previewItem.links}">
    	          <TR>
        	        <TD></TD>
            		<TD>
	                <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
    	             <TBODY>
        	         <c:forEach var="lid" items="${previewItem.links}">
            	        <TR>
                	      <TD class=news align=left><digi:img src="module/highlights/images/arrow.gif" />
													<c:if test="${ !empty lid.name}">
													<a href='<c:out value="${lid.url}"/>' class="text">
													<c:out value="${lid.name}" escapeXml="false" /></a><BR></c:if>
						 </TD>							
	                    </TR>
                   </c:forEach></TBODY>
	             </TABLE></TD>
	           </TR>
    	     </c:if>  
       </TBODY></TABLE>
</c:if>
<!-- Layout2 -->
<c:if test="${highlightItemForm.layout2}">
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
   <TBODY><TR>
        <TD colSpan=3>
            <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
               <TBODY><TR>
                    <TD bgColor=#00499b>
                      <DIV class=mheading><FONT color=#ffffff>
					  <c:out value="${previewItem.description}"/></FONT></DIV>
				    </TD></TR></TBODY>
		  	 </TABLE></TD></TR>
             <TR>
               <TD colSpan=3></TD></TR>
             <TR>
               <TD rowSpan=4>&nbsp;</TD></TR>
             <TR>
               <TD>
				<c:if test="${!empty previewItem.title}">
				 <b><h3><c:out value="${previewItem.title}" escapeXml="false" /></h3></b></c:if>
                 <BR>  
                <SPAN class=date><FONT color=#666666 size=2>
				<c:out value="${previewItem.creationDate}"/></FONT></SPAN>
			</TD></TR>
             <TR>
                <TD><IMG height=5 src="" width=1></TD></TR>
              <TR>
                <TD colSpan=3></TD></TR>
              <TR>
                <TD></TD>
                <TD>
                  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
                    <TBODY><TR>
                      <TD class=text>
                        <TABLE cellSpacing=0 cellPadding=0 width=77 align=left border=0>
                          <TBODY><TR>
                            <TD><TH>
                            <digi:context name="showImage" property="context/module/moduleinstance/showImage.do" />
                            <c:if test="${previewItem.haveImageSizes}">
                              <img src="<%= showImage%>?activeHighlightId=<c:out value='${previewItem.id}' />"  height="<c:out value='${previewItem.imageHeight}' />" width="<c:out value='${previewItem.imageWidth}' />" />
                            </c:if>
                           <c:if test="${! previewItem.haveImageSizes}">
                              <img src="<%= showImage%>?activeHighlightId=<c:out value='${previewItem.id}' />" />
                           </c:if><BR>
                            </TH></TD>
                          </TR></TBODY></TABLE>
							<c:if test="${!empty previewItem.topic}">
									  <c:out value="${previewItem.topic}" escapeXml="false"/>
							</c:if>
                        <BR><BR>
                        </TD></TR>
                        <TR><TD>
                        <FONT color=#0000cc><B>
                        Related links:</B><BR></FONT>
					</TD>
				  </TR></TBODY>
				  </TABLE></TD>
                 </TR>
	           <c:if test="${ !empty previewItem.links}">
    	          <TR>
        	        <TD></TD>
            	    <TD>
                	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
                 	<TBODY>
	                 <c:forEach var="lid" items="${previewItem.links}">
    	                <TR>
        	              <TD class=news align=left><digi:img src="module/highlights/images/arrow.gif" />
													<c:if test="${ !empty lid.name}">
													<a href='<c:out value="${lid.url}"/>' class="text">
													<c:out value="${lid.name}" escapeXml="false" /></a><BR></c:if>
						      	                  </TD></TR>
                  </c:forEach>  
                </TBODY>
             </TABLE></TD>
           </TR>
         </c:if>  
       </TBODY></TABLE>
</c:if>
<!-- Layout3 -->
<c:if test="${highlightItemForm.layout3}">
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
    <TBODY><TR>
      <TD colSpan=3>
        <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
          <TBODY><TR>
            <TD bgColor=#00499b>
              <DIV class=mheading><FONT color=#ffffff>
			  <c:out value="${previewItem.description}"/></FONT></DIV>
 		   </TD></TR></TBODY></TABLE></TD></TR>
           <TR>
              <TD colSpan=3></TD></TR>
           <TR>
             <TD rowSpan=4>&nbsp;</TD></TR>
           <TR>
            <TD>
									<c:if test="${previewItem.title}">
									<b><h3><c:out value="${previewItem.title}" escapeXml="false" /></h3></b></c:if>
                <BR><SPAN class=date><FONT color=#666666 
                  size=2><c:out value="${previewItem.creationDate}"/></FONT></SPAN></TD></TR>
              <TR>
                <TD><IMG height=5 src="" width=1></TD></TR>
              <TR>
                <TD colSpan=3></TD></TR>
              <TR>
                <TD></TD>
                <TD>
                 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
                  <TBODY><TR><TD class=text> 
													<c:if test="${!empty previewItem.topic}">
													  <c:out value="${previewItem.topic}" escapeXml="false"/>
													</c:if>
                        <BR><BR>
                        <FONT color=#0000cc><B>
                        Related links:</B><BR></FONT>
						</TD></TR></TBODY>
					</TABLE></TD>
               </TR>
	           <c:if test="${ !empty previewItem.links}">
    	          <TR>
        	        <TD></TD>
            	    <TD>
                	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
	                 <TBODY>
    	             <c:forEach var="lid" items="${previewItem.links}">
        	            <TR>
            	          <TD class=news align=left><digi:img src="module/highlights/images/arrow.gif" />
													<c:if test="${ !empty lid.name}">
													<a href='<c:out value="${lid.url}"/>' class="text"><B>
													<c:out value="${lid.name}" escapeXml="false" /></B></a><BR></c:if>
            	          </TD>
                	    </TR>
	                  </c:forEach>  
    	            </TBODY>
        	     </TABLE></TD>
	           </TR>
	         </c:if>  
    </TBODY></TABLE>
</c:if>
</td></tr>
<tr bgcolor="lightgrey">
<td noWrap align="center" valign="center">
     <a href="javascript:window.close()" class="navtop4"><digi:trn key="highlights:closeThisWindow">Close this window</digi:trn></a>
</td></tr>
</table>
</div>
</c:if>