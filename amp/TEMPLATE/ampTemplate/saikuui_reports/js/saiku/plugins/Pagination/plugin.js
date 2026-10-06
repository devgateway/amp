Saiku.events.bind('session:new', function(session) {
	if(!Settings.PAGINATION) {
		$(".pagination_sprite").hide();
		$(".pagination_info").hide();
	}

    function new_workspace(args) {
        args.workspace.bind('query:result', function(){
        	Saiku.logger.log("Pagination.new_workspace");
	        if(Settings.PAGINATION) {
	        	$(".pagination_sprite").show();
	        	$(".pagination_info").show();
	        	$(this.el).find(".pagination_info").val(this.query.get('page') + "/" + this.query.get('max_page_no'));
	        }
	        else {
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
