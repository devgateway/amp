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