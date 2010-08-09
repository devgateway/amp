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

        $('ul.checkbox_container_donors li:not(.all) input')
		.click(function() {
			if (this.checked) {
                          $('ul.checkbox_container_donors li.all input').attr('checked', false);
			}
		});

	$('ul.checkbox_container_donors li.all input')
		.click(function() {
			if (this.checked) {
                              $('ul.checkbox_container_donors li:not(.all) input').attr('checked', false);
			}
		});
});


function toggleAll(checkboxName, checkboxGroup) {
	if ($("#" + checkboxName + "_toggle").attr("selected") == 'true') {
		$("input[name^='" + checkboxName + "'][type='checkbox'][group='" + checkboxGroup + "']").attr("checked", false);
		$("#" + checkboxName + "_toggle").attr("selected", false);
	}	else {
		$("input[name^='" + checkboxName + "'][type='checkbox'][group='" + checkboxGroup + "']").attr("checked", true);
		$("#" + checkboxName + "_toggle").attr("selected", true);
 	}
	
	$("#" + checkboxName + "_all").attr("checked", false);
}