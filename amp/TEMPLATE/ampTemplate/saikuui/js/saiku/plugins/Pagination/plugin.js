Saiku.events.bind('session:new', function(session) {
	if(!Settings.PAGINATION) {
		$(".pagination_sprite").hide();
		$(".pagination_info").hide();
	}

    function new_workspace(args) {
        args.workspace.bind('query:result', function(){
	        	if(Settings.PAGINATION) {
	        		$(".pagination_sprite").show();
	        		$(".pagination_info").show();
		        	var current_page = this.query.get('page')+1;
		        	var total_pages = this.query.get('max_page_no') + 1;
		        	$(this.el).find(".pagination_info").val(current_page + "/" + total_pages);
	        	}
	        	else
        		{
	        		$(".pagination_sprite").hide();
	        		$(".pagination_info").hide();
        		}
        	});
    }

    // Attach stats to existing tabs
    for(var i = 0; i < Saiku.tabs._tabs.length; i++) {
        var tab = Saiku.tabs._tabs[i];
        new_workspace({
            workspace: tab.content
        });
    };

    // Attach stats to future tabs
    Saiku.session.bind("workspace:new", new_workspace);
});
