<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<style>

.tableEven {
	background-color:#dbe5f1;
	font-size:8pt;
	padding:2px;
}

.tableOdd {
	background-color:#FFFFFF;
	font-size:8pt;!important
	padding:2px;
}
 
.Hovered {
	background-color:#a5bcf2;
}

.tableHeaderCls {
      color: #FFFFFF;
      background-color:#222E5D;

}
</style>

<script language="javascript">
function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	rows = tableElement.getElementsByTagName('tr');
	for(var i = 0, n = rows.length; i < n; ++i) {
		if(i%2 == 0)
			rows[i].className = classEven;
		else
			rows[i].className = classOdd;
	}
	rows = null;
}
function setHoveredTable(tableId) {

	var tableElement = document.getElementById(tableId);
	if(tableElement){
    var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        rows      = tableElement.getElementsByTagName('tr');

		for(var i = 0, n = rows.length; i < n; ++i) {
			rows[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			rows[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		rows = null;
	}
	


}
</script>
<digi:instance property="orgProfOrgProfileFilterForm"/>

<digi:form action="/showOrgProfile.do">


    <table>
        <tr>
            <td><b><digi:trn key="orgProfile:filer:Organization">Organization</digi:trn></b></td>
            <td>
                <html:select property="org" styleClass="inp-text">
                    <html:optionsCollection property="organizations"
                value="ampOrgId" label="name" />
                    
                </html:select>
            </td>
       
            <td ><b><digi:trn key="orgProfile:filer:fiscalCalendar">Fiscal Year</digi:trn></b></td>
            
       
            
            <td align="center">
                <html:select property="year" styleClass="inp-text">
                    <html:optionsCollection property="years" label="wrappedInstance" value="wrappedInstance" />
                </html:select>
            </td>
            
    
     
            <td><b><digi:trn key="orgProfile:filer:Currency">Currency</digi:trn></b></td>
       
            <td>
                <html:select property="currency" styleClass="inp-text">
                    <html:optionsCollection property="currencies"
                value="ampCurrencyId" label="currencyName" />
                    
                </html:select>
            </td>
     
            <td>
                 <html:select property="transactionType" styleClass="inp-text">
                     <html:option value="0">Comm</html:option>
                     <html:option value="1">Disb</html:option>
                </html:select>
         
            </td>
            <td align="center">
                
                <html:submit styleClass="buton" property="apply"><digi:trn key="orgProfile:filer:Apply">Apply</digi:trn></html:submit>&nbsp;
                
              
            </td>
            
        </tr>
    </table>
</digi:form>

