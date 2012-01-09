<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script type="text/javascript">
    <!--


    function validate(){
        if(document.piTableWidgetForm.name.value==''){
        	
            alert("Please enter the name");
            return false;
        }
        var radioGrp=document.piTableWidgetForm.donorGroupYearColumn;
        var selected=false;
        for (var i=0;i<radioGrp.length;i++){
            if (radioGrp[i].checked){
                selected=true;
                break;
            }
        }
        if(!selected){
            alert("Please select year");
            return false;
        }
           return save();
    }

    function cancel(){
       <digi:context name="addEditPiTable" property="context/widget/piTableWidgetManager.do~actType=viewAll" />
               document.piTableWidgetForm.action = "${addEditPiTable}";
               document.piTableWidgetForm.submit();
           }

           function save(){
       <digi:context name="addEditPiTable" property="context/widget/piTableWidgetManager.do~actType=save" />
               document.piTableWidgetForm.action = "${addEditPiTable}";
               document.piTableWidgetForm.submit();
           }


           //-->
</script>
<digi:ref href="css/gis.css" type="text/css" rel="stylesheet" />
<digi:instance property="piTableWidgetForm" />
<digi:form action="/piTableWidgetManager.do~actType=save">

    <html:hidden name="piTableWidgetForm" property="piTableWidgetId"/>
    <table width="60%" border="0" cellpadding="15">
        <tr>
            <td>
                <span class="crumb">
                    <c:set var="translation">
                        <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
                    </c:set>
                    <html:link  href="/aim/admin.do" styleClass="comment" title="${translation}" >
                        <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
                    </html:link>&nbsp;&gt;&nbsp;
                    <html:link  href="/widget/piTableWidgetManager.do~actType=viewAll" styleClass="comment">
                        <digi:trn>Paris Indicator Table Widget Manager</digi:trn>
                    </html:link>
                    &nbsp;&gt;&nbsp;

                    <digi:trn>Create/Edit Paris Indicator Table Widget</digi:trn>
                </span>
            </td>
        </tr>
        <tr>
            <td>
                <span class="subtitle-blue">
                    <c:if test="${empty piTableWidgetForm.piTableWidgetId}">
                        <digi:trn>Create Paris Indicator Table Widget</digi:trn>
                    </c:if>
                    <c:if test="${not empty piTableWidgetForm.piTableWidgetId}">
                        <digi:trn>Edit Paris Indicator Table Widget</digi:trn>
                    </c:if>
                </span>
            </td>
        </tr>
        <tr>
            <td>

                <table>
                    <tr>
                        <td colspan="2">
                            <digi:errors/>
                        </td>
                    </tr>

                    <tr>
                        <td nowrap="nowrap" align="left" colspan="2">
                            <font color="red">*</font>
                            <strong>
                                <digi:trn>Name</digi:trn>:&nbsp;
                            </strong>
                            <html:text name="piTableWidgetForm" property="name"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td nowrap="nowrap" align="left" colspan="2">
                            <table class="tableElement">
                                <thead>
                                    <tr class="tableHeader">
                                        <td>
                                            &nbsp;
                                        </td>
                                        <td>
                                            <strong><digi:trn>Name</digi:trn></strong>
                                        </td>
                                        <td>
                                            <digi:trn>Base Value</digi:trn>
                                        </td>
                                        <td>
                                            <digi:trn>Target Value</digi:trn>
                                        </td>
                                    </tr>

                                </thead>
                                <tbody>

                                    <c:forEach var="parisIndicators" items="${piTableWidgetForm.parisIndicators}">
                                        <tr>
                                            <td>
                                                ${parisIndicators.parisIndicator.indicatorCode}
                                            </td>
                                            <td>
                                               <digi:trn>${parisIndicators.parisIndicator.name}</digi:trn>
                                            </td>
                                            <td>
                                                <html:text name="parisIndicators" property="baseValue" indexed="true"/>
                                            </td>
                                            <td>
                                                <html:text name="parisIndicators" property="targetValue" indexed="true"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </td>
                    </tr>


                    <tr>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <fieldset style="width:50%">
                                <legend align="left"> <font color="red">*</font>&nbsp;<digi:trn>Donor Group Year</digi:trn></legend>
                                <c:forEach var="year" items="${piTableWidgetForm.years}" varStatus="status">
                                    <html:radio  name="piTableWidgetForm" property="donorGroupYearColumn" value="${year}"/>${year}
                                    <c:if test="${status.count%4==0}">
                                        <br/>
                                    </c:if>
                                </c:forEach>
                            </fieldset>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td colspan="2">
                   
                            <digi:trn>Places</digi:trn>:
                            <html:select name="piTableWidgetForm" property="selPlaces" style="width: 300px" styleId="tablePlaces">
                            	<option selected="selected"><digi:trn>None</digi:trn></option>
                                <html:optionsCollection name="piTableWidgetForm" property="places" value="id" label="name"/>
                            </html:select>
                        </td>
                    </tr>

                    <tr>
                        <td align="right">
                            <input type="button" onclick="validate()" value="<digi:trn>Save</digi:trn>"/>
                        </td>
                        <c:set var="trnCancel"><digi:trn key="aim:btn:cancel">Cancel</digi:trn></c:set> 
                        <td>
                            <input type="button" value="${trnCancel}" onclick="cancel()">
                        </td>
                    </tr>
                </table>

            </td>
        </tr>
    </table>
</digi:form>
<script type="text/javascript">
    	$(document).ready(function() {
        $('table.tableElement thead tr td').css("font-weight","bold");
        $('table.tableElement tbody tr:odd').addClass('tableOdd');
        $('table.tableElement tbody tr:even').addClass('tableEven');});
</script>

