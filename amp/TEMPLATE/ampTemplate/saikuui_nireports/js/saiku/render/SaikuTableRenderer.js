
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
            // in case we have some left over scrollers
            if (self._options.hasOwnProperty('batch')) {
                $(self._options.htmlObject).parent().parent().unbind('scroll');
            }

            _.defer(function(that) {
                if (self._options.hasOwnProperty('batch') && !self._options.hasOwnProperty('batchSize')) {
                    self._options['batchSize'] = 1000;
                }

                // Here is where we get the HTML with the report.
                var html =  self.internalRender(self._data, self._options);
                $(self._options.htmlObject).html(html);
                $(".tooltipped").tipsy();
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
	
	allData.workspace.bind('saikuTableRender:tableRenderedInDOM', function(
			args) {
		ampTableRenderer.postProcessTooltips();
	});
	
	return ampTableRenderer.render(allData, options);
}