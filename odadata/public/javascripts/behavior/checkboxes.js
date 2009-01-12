$(document).ready(function() {
	$('ul.checkbox_container li:not(.all) input')
		.click(function() {
			if (this.checked) {
				$(this).parent().siblings('li.all').children('input').attr('checked', false);
			}
		});
		
	$('ul.checkbox_container li.all input')
		.click(function() {
			if (this.checked) {
				$(this).parent().siblings('li:not(.all)').children('input').attr('checked', false);
			}
		});
});


function toggleAll(checkboxName, selectAll, deselectAll)
{
	if($("#" + checkboxName + "_toggle").attr("selected") == 'true'){
		$("input[name^='" + checkboxName + "'][type='checkbox']").attr("checked", false);
		$("#" + checkboxName + "_toggle").attr("selected", false);
		$("#" + checkboxName + "_toggle").html(selectAll)
	}
	else
	{
		$("input[name^='" + checkboxName + "'][type='checkbox']").attr("checked", true);
		$("#" + checkboxName + "_toggle").attr("selected", true);
		$("#" + checkboxName + "_toggle").html(deselectAll)
	}
	$("#" + checkboxName + "_all").attr("checked", false);
}