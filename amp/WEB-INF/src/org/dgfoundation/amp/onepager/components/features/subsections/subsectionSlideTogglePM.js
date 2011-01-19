$(document).ready(function(){
	$("#tabs").tabs();
	$("#filter_tabs").tabs();
	$("#perm_tabs").tabs();
	$("a.sliderPM").click(function(){
		$(this).siblings("div:first").slideToggle();
		return false;
		});

	$(".tab_opt_cont a").click(function(){
		//$(this).siblings("div:first").slideToggle();
		var selector = $(this).attr("href");
		var dlgopts = {
			height: 200,
			width: 400,
			modal: true
			};
		switch (selector)
        {
			case '#dialog1':
				dlgopts.width = 400;
				dlgopts.height = 180;

			break;
			case '#dialog2':
				dlgopts.width = 900;
				dlgopts.height = 500;
			break;
			case '#dialog3':
				dlgopts.width = 400;
				dlgopts.height = 110;
				
			break;
			case '#dialog4':
				dlgopts.width = 500;
				dlgopts.height = 420;
			break;


		};	
     	$(selector).dialog(dlgopts);

		return false;
		});
	
})