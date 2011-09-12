<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script type="text/javascript">
<!--
	function cancelEdit(myForm){
		<digi:context name="justSubmit" property="context/module/moduleinstance/indicatorchartwidgets.do" />
		myForm.action="<%=justSubmit%>";  
		myForm.submit();
	}
    function unselectAll(){
        var selectPlace=document.getElementsByName("selPlaces")[0];
        var opts=selectPlace.options;
        for(var i=0;i<opts.length;i++){
            var opt=opts[i];
            if(opt.selected){
                opt.selected=false;
            }
        }

    }
    function validateSave(){
        var valid=true;
        if (document.gisIndicatorChartForm.widgetName.value==''){
            alert('<digi:trn jsFriendly="true">Please enter name</digi:trn>');
            valid= false;
        }
        else{
            if(document.gisIndicatorChartForm.selIndicators.value=='-1'){
                alert('<digi:trn jsFriendly="true">Please select indicator</digi:trn>');
                valid= false;
            }
        }
        return valid;

    }
//-->
</script>
<digi:instance property="gisIndicatorChartForm" />
<digi:form action="/indicatorchartwidgets.do~actType=save">

<table width="1000" border="0" cellpadding="0" align=center>
	<tr>
		<td height=40 bgcolor="#f2f2f2">
			<span class="crumb">
              <c:set var="translation">
                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
              </c:set>
              <html:link  href="/aim/admin.do" styleClass="comment" title="${translation}" >
                <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
              </html:link>&nbsp;&gt;&nbsp;
                <digi:trn key="admin:Navigation:indicatorchartWidgets">Indicator chart widgets</digi:trn>
                &nbsp;&gt;&nbsp;
                <digi:trn key="admin:Navigation:indicatorchartWidgetsCreateEdit"><b>Input widget fields</b></digi:trn>
			</span>
		</td>
	</tr>
	<tr>
		<td style="padding-top:10px; padding-bottom:10px;">
			<span class="subtitle-blue"><digi:trn key="gis:indicatorChartWidgetCreateEdit:pageHeader">Edit widget data</digi:trn></span>
		</td>
	</tr>
	<tr>
		<td align=center bgcolor=#F2F2F2 style="border:1px solid #CCCCCC; padding-top:10px; padding-bottom:10px;">
			<html:hidden name="gisIndicatorChartForm" property="widgetId"/>
			<table style="font-size:12px;">
				<tr>
					<td nowrap="nowrap" align="right">
						<font color="red">*</font>
						<strong>
							<digi:trn key="gis:editIndicChartWidget:widgetName">WidgetName</digi:trn>
						</strong>
					</td>
					<td>
						<html:text name="gisIndicatorChartForm" property="widgetName" style="width : 300px"/>
					</td>
					<td>
					</td>
				</tr>
				<tr>
					<td nowrap="nowrap" align="right">
						<font color="red">*</font>
						<strong>
							<digi:trn key="gis:editIndicChartWidget:selIndicator">Indicator</digi:trn>
						</strong>
					</td>
					<td>
						<html:select name="gisIndicatorChartForm" property="selIndicators" style="width : 300px">
							<html:option value="-1">&nbsp;</html:option>
							<html:optionsCollection name="gisIndicatorChartForm" property="indicators" label="generatedName" value="id"/>
						</html:select>
					</td>
					<td>&nbsp;
						
					</td>
				</tr>
				<tr>
					<td nowrap="nowrap" align="right">
						<strong>
							<digi:trn key="gis:editIndicChartWidget:place">Place</digi:trn>
						</strong>
					</td>
					<td>
						<html:select name="gisIndicatorChartForm" property="selPlaces" multiple="true" style="width: 300px">
							<html:optionsCollection name="gisIndicatorChartForm" property="places" value="id" label="name"/>
						</html:select>
					</td>
					<td valign="top">
						<input type="button" value="<digi:trn>Unselect All</digi:trn>" class="buttonx" onclick="unselectAll()">
					</td>
				</tr>
				<tr>
					<td colspan="3">
						<hr>
					</td>
				</tr>
				<tr>
					<td align="center" colspan=3>
                        <html:submit onclick="return validateSave()" styleClass="buttonx">
							<digi:trn key="gis:editIndicatorChartWidget:btnSave">Save</digi:trn>
						</html:submit>
						<input type="button" value="<digi:trn>Cancel</digi:trn>" onclick="cancelEdit(this.form)" class="buttonx">
					</td>
				</tr>
			</table>

		</td>
	</tr>
</table>
</digi:form>