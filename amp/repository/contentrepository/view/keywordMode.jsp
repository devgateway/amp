<%@page import="org.digijava.module.contentrepository.helper.FilterValues"%>
<%@page import="org.digijava.module.categorymanager.util.CategoryConstants"%>
<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>

<bean:define id="myKeywordModeDivId" toScope="page" scope="request" name="keywordModeDivId"/>

<div id="${myKeywordModeDivId}" style="display:none;">
	<form>
		<table border="0" cellspacing="1" cellpadding="1">
			<tr>
				<td><div class="t_sm"><b><digi:trn>Keyword Mode</digi:trn>:</b></div></td>
				<td>
					<select name="keywordMode" class="dropdwn_sm" style="width: 250px;" >
                        <option value="0"><digi:trn>Any keyword</digi:trn></option>
                        <option value="1"><digi:trn>All keywords</digi:trn></option>
                    </select>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="text-align: center; padding-top: 30px;">
					<button class="buttonx" type="button">
						<digi:trn>Apply</digi:trn>
					</button>
					 &nbsp;&nbsp;&nbsp;
					<button class="buttonx" type="button">
						<digi:trn>Close</digi:trn>
					</button>
				</td>
			</tr>
		</table>
	</form>
</div>