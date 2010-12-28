<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<DIV id="TipLayer"
  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
<div align="center">
  <table width="1000" class="layoutTable" border="0">
    <tr>
      <td valign="top" rowspan="2"><digi:edit key="${param.htmlblock_2}" displayText="Edit HTML"></digi:edit>
      </td>
    </tr>
    <tr>
    	<td>
    		<div class="welcome_note">
    			<digi:trn>
    				Please note that this version of the AMP platform has been designed for the following browsers: Internet Explorer 7 (or above) and Mozilla Firefox 3.5 (or above).
    			</digi:trn>
    		</div>
    	</td>
    </tr>
  </table>
</div>
