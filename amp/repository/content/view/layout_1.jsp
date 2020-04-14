<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<digi:context name="displayThumbnail" property="/content/displayThumbnail.do" />
<digi:secure authenticated="false">
  <c:set var="thumbnailCount">
	  ${param.thumbnailCount}
  </c:set>
</digi:secure>
<digi:secure authenticated="true">
  <c:set var="thumbnailCount"> ${fn:length(sessionScope.contentThumbnails)} </c:set>
</digi:secure>
<!-- BREADCRUMP START -->
	<!--<div class="breadcrump">
		<div class="centering">
			<div class="breadcrump_cont">
			</div>
		</div>
	</div>-->
<!-- BREADCRUMP END -->
<DIV id="TipLayer"
  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
<div id="amphome">
	<div id="amphometext">
<digi:edit key="${param.htmlblock_2}" displayText="Edit HTML"></digi:edit>
     
      
      </div>
      <div id="amphomesidebar" >
      <digi:edit key="${param.htmlblock_1}" displayText="Edit HTML"></digi:edit>
      <c:if test="${thumbnailCount > 0}">
         <br/>
          <table width="100%" cellpadding="0" cellspacing="0"  class="layoutTable" style="vertical-align:bottom;padding-top:10px;clear:both;">
           <tr>
              <c:forEach var='index' begin='0' end='${thumbnailCount-1}'>
              	<c:choose>
                    <c:when test="${index%2 == 0}">	                    
			              	</tr>
			              	<tr>
			              	<td>
			               		<a style="cursor: pointer">
			                  		<digi:secure authenticated="false"><img id="displayThumbnail_${index}" src="${displayThumbnail}?index=${index}&pageCode=${param.pageCode}" align="middle" width="110" style="border:1px solid #cecece" onload="attachFuncToThumbnail(${index}, '${param.pageCode}')"> </digi:secure>
			                  		<digi:secure authenticated="true"> <img id="displayThumbnail_${index}" src="${displayThumbnail}?index=${index}&pageCode=${param.pageCode}&isAdmin=true" align="middle" width="110" style="border:1px solid #cecece" onload="attachFuncToThumbnail(${index}, '${param.pageCode}')"> </digi:secure>
			                 	</a> 
			                </td>
                    </c:when>
                    <c:otherwise>
			              	<td>
			               		<a style="cursor: pointer">
			                  		<digi:secure authenticated="false"><img id="displayThumbnail_${index}" src="${displayThumbnail}?index=${index}&pageCode=${param.pageCode}" align="middle" width="110" style="border:1px solid #cecece" onload="attachFuncToThumbnail(${index}, '${param.pageCode}')"> </digi:secure>
			                  		<digi:secure authenticated="true"> <img id="displayThumbnail_${index}" src="${displayThumbnail}?index=${index}&pageCode=${param.pageCode}&isAdmin=true" align="middle" width="110" style="border:1px solid #cecece" onload="attachFuncToThumbnail(${index}, '${param.pageCode}')"> </digi:secure>
			                 	</a> 
			                </td>
                    </c:otherwise>
                </c:choose>
              </c:forEach>
   		 </tr>
          </table>
      <br />
      </c:if>
    </div> 
</div>

<jsp:include page="/TEMPLATE/ampTemplate/layout/broswerDetection.jsp"/>
