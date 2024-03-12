
var SaikuTableRenderer = _.extend(SaikuRenderer, {
    key: "table"
});

SaikuTableRenderer.prototype._render = function(data, options) {
        var self = this;
        if (data) {
            this._data = data;
        }
        if (options) {
            this._options = _.extend({}, SaikuRendererOptions, options);
        }

        if (typeof this._data == "undefined") {
            return;
        }

        if (this._data != null && this._data.error != null) {
            return;
        }        
        if (this._data == null || (this._data.cellset && this._data.cellset.length === 0)) {
            return;
        }
        if (this._options.htmlObject) {
        	Saiku.currentActiveBatch = 0;
        	Saiku.totalBatches = 0 /*zero-based*/;        	
        	$(self._options.htmlObject).attr('id', 'results_table');       	
        	$('.workspace_results').off('scroll');
            
            Saiku.ui.block('Rendering results...');
            _.defer(function(that) {
                // Generate the html containing the table, in some cases it can have more than 500.000 cells and more than 32MB as plain text.               
                var html =  self.internalRender(self._data, self._options);
                // Assigning directly with vanilla js is usually faster than $.html() and $.append()
                document.getElementById("results_table").innerHTML = html;                
                                
                // Show first batch of results.
                $("#results_table [name='" + Saiku.currentActiveBatch + "']").removeClass('hidden_row').attr('name', ''); //TODO: move these 2 common lines to a function.
                //ampTableRenderer.postProcessTooltips();
               	Saiku.logger.log('SaikuTableRenderer.render - Finished rendering?');
               	
               	// This code runs when the user scrolls down to the end of the report to make visible a new batch of results.
           	    $('.workspace_results').on('scroll', function() {
           	    	if (Saiku.totalBatches > Saiku.currentActiveBatch) {	           	    		
           	            if($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight) {
           	            	Saiku.currentActiveBatch++;
           	            	Saiku.logger.log('SaikuTableRenderer.render - EOF Reached - batch: '+ Saiku.currentActiveBatch);
           	                // By uncommending the Saiku.ui.block call we show very briefly a popup but it makes the whole process a little slower mostly due to an extra I18n.translate call.
           	            	//Saiku.ui.block('Adding more results...', function() {
           		                $("[name='" + Saiku.currentActiveBatch + "']").removeClass('hidden_row').attr('name', '');     	
           		
           		                _.defer(function(){
           		                	Saiku.logger.log('SaikuTableRenderer.render - Finished rendering?');
           		                    //Saiku.ui.unblock();
           		                });                      	           		                
           		            //});                        
           	            }
           	       	}
           	    });                
                Saiku.ui.unblock();                
                data.workspace.trigger('ampTableRender:tableRenderedInDOM', self);
                data.workspace.trigger('saikuTableRender:tableRenderedInDOM', self);
                return html;
            });
        } else {
            var html =  this.internalRender(this._data, self._options);
            data.workspace.trigger('saikuTableRender:tableRenderedInDOM', self);
            return html;
        }             
};

SaikuTableRenderer.prototype.clear = function(data, options) {
    var self = this;
    if (this._options.htmlObject && this._options.hasOwnProperty('batch')) {
        $(self._options.htmlObject).parent().parent().unbind('scroll');
    }

};

SaikuTableRenderer.prototype._processData = function(data, options) {
    this._hasProcessed = true;
};

SaikuTableRenderer.prototype.internalRender = function(allData, options) {	
	var metadata = {
		hierarchies : Saiku.tabs._tabs[0].content.query.attributes.hierarchies,
		columns : Saiku.tabs._tabs[0].content.query.attributes.columns,
		type : 'html'
	};
	var ampTableRenderer = new AMPTableRenderer(metadata);
	
	allData.workspace.bind('ampTableRender:tableRenderedInDOM', function(
			args) {
		ampTableRenderer.postProcessTooltips();
		allData.workspace.unbind('ampTableRender:tableRenderedInDOM');
	});	
	
	return ampTableRenderer.render(allData, options);
}