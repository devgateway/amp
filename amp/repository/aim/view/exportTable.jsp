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
        var regexpListItem = new RegExp(/<li>/g);
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
</script>



<form action="/aim/exportAdminTable.do" method="post" name="exportTableForm" >
 <input type="hidden" name="data" class="reportData"/>
 	<table cellspacing="0" class="report_indicator" cellpadding="0" border="0" align="center" width="100%" >
 	<tbody><tr>
<td valign="top">
		<div class="tab_opt_box">
			<div class="tab_opt">
			<div class="tab_opt_cont">
			<a href="#" target="_blank" onclick="exportXSL(); return false;" class="l_sm">
			<img border="0" src="/TEMPLATE/ampTemplate/img_2/ico-excel.png"></a>&nbsp;<a href="#" onclick="exportXSL(); return false;" class="l_sm">Export to Excel</a> &nbsp;&nbsp; 
			<a href="#" onclick="window.print(); return false;" class="l_sm"><img border="0" src="/TEMPLATE/ampTemplate/img_2/ico-print.png"></a>&nbsp;<a href="#" onclick="window.print(); return false;" class="l_sm">Print</a>
			</div>
			</div>
		</div>
</td>
</tr>
</tbody>
</table>

</form>

