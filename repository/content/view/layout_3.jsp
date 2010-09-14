<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/content" prefix="content" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<digi:context name="displayThumbnail" property="/content/displayThumbnail.do" />
<digi:secure authenticated="false">
  <c:set var="thumbnailCount">
    <content:getThumbnailCount pageCode="${param.pageCode}"/>
  </c:set>
</digi:secure>
<digi:secure authenticated="true">
  <c:set var="thumbnailCount"> ${fn:length(sessionScope.contentThumbnails)} </c:set>
</digi:secure>
<DIV id="TipLayer"
  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
<div align="center">
  <table width="1000" class="layoutTable" border="0">
    <tr>
      <td valign="top" rowspan="2"><digi:edit key="${param.htmlblock_2}" maxLength="1500" displayText="Edit HTML"></digi:edit>
      </td>
    </tr>
  </table>
</div>
