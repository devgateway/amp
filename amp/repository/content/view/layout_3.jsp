<%@ taglib uri="http://digijava.org" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!-- BREADCRUMP START -->
	<div class="breadcrump">
		<div class="centering">
			<div class="breadcrump_cont">
			</div>
		</div>
	</div>
<!-- BREADCRUMP END -->
<DIV id="TipLayer"
  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
<div align="center">
  <table width="1000" class="layoutTable" border="0">
    <tr>
      <td valign="top" rowspan="2"><digi:edit key="${param.htmlblock_2}" displayText="Edit HTML"></digi:edit>
      </td>
      <td valign="top" rowspan="2"><digi:edit key="${param.htmlblock_1}" displayText="Edit HTML"></digi:edit>
      </td>
    </tr>
  </table>
</div>
