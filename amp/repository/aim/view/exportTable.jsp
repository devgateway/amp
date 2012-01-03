<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>


<script type="text/javascript">
    function exportXSL(){
        var heads=$(".report>table>thead>tr");
        var rows=$(".report>table>tbody.yui-dt-data>tr");
        var output='<table>'+getOutput(heads)+getOutput(rows)+'</table>';
        document.exportTableForm.data.value=output;
        document.exportTableForm.submit();
    }
    function getOutput(rows){
        var  output='';
        var regexp = new RegExp(/\<[^\<]+\>/g);
        var regexpWhiteSpaces = new RegExp(/&nbsp;/g);
        var regexpListItem = new RegExp(/<li>/gi);
        var arrayRows=new Array();
        var m=0;
        for(var i=0;i<rows.length;i++){
            var cells=rows[i].cells;
            var arrayCells=new Array()
            var k=0
            for(var j=0;j<cells.length;j++){
                var ignore=false;
                var classNames = cells[j].className.split(' ');
                for (var l = 0; l < classNames.length; l++) {
                    if (classNames[l] == 'ignore') {
                        ignore=true;
                        break;
                    }
                }
                if(!ignore){
                    arrayCells[k++]='<cell>'+cells[j].innerHTML.replace(regexpListItem, '\u2022').replace(regexp, "").replace(regexpWhiteSpaces," ").trim()+'</cell>';
                }    
            }
            arrayRows[m++]='<row>'+arrayCells.join('')+'</row>';
        }
        output=arrayRows.join('');
        return output;
       

    }
    function printPage(){
        if(typeof myPrint=='undefined'){
            window.print(); return false;
            
        }
        myPrint();
    }
</script>


<c:set var="translationxls">
	<digi:trn>Export to Excel</digi:trn>
</c:set>
<c:set var="translationPrinter">
	<digi:trn>Printer Friendly</digi:trn>
</c:set>
<form action="/aim/exportAdminTable.do" method="post" name="exportTableForm" >
    <input type="hidden" name="data" class="reportData"/>
    <div class="toolbar" align="center" style="background:#f2f2f2;">
        <table border="0" align="center">
            <tr>
                <td noWrap align=left valign="middle" style="cursor:pointer;" height="30px">
                    <a target="_blank" onclick="exportXSL(); return false;" title="${translationxls}">
                        <digi:img width="17"  hspace="2" vspace="2" src="/TEMPLATE/ampTemplate/imagesSource/common/excel.gif" border="0" alt="${translationxls}" />
                    </a>
                </td>
		<td noWrap align=left valign="middle">
                    <digi:link styleId="printWin" href="#" onclick="window.print(); return false;" title="${translationPrinter}">
                        <digi:img width="17"  hspace="2" vspace="2" src="/TEMPLATE/ampTemplate/imagesSource/common/printer.gif" border="0" alt="${translationPrinter}"/>
                    </digi:link>
                </td>
            </tr>
        </table>
    </div>
</form>
