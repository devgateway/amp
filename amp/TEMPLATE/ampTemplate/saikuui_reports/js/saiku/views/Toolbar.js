/*  
 *   Copyright 2012 OSBI Ltd
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
 
/**
 * The global toolbar
 */
var Toolbar = Backbone.View.extend({
    tagName: "div",
    
    events: {
        'click a' : 'call'
    },
    
    template: function() {
        return _.template( $("#template-toolbar").html() )(this);
    },
    
    initialize: function() {
    	Saiku.logger.log("Toolbar.initialize");
        //this.render();
    },
    
    render: function() {
    	Saiku.logger.log("Toolbar.render");
        $(this.el).attr('id', 'toolbar')
            .html(this.template());
        
        // Trigger render event on toolbar so plugins can register buttons
        Saiku.events.trigger('toolbar:render', { toolbar: this });
        
        return this;
    },
    
    call: function(e) {
    	Saiku.logger.log("Toolbar.call");
        var target = $(e.target);
        var callback = target.attr('href').replace('#', '');
        if(this[callback]) {
            this[callback](e);
        }
        e.preventDefault();
    },
    
    /**
     * Add a new tab to the interface
     */
    new_query: function() {
    	Saiku.logger.log("Toolbar.new_query");
        Saiku.tabs.add(new Workspace());
        return false;
    },
    
    /**
     * Open a query from the repository into a new tab
     */
    open_query: function() {
    	Saiku.logger.log("Toolbar.open_query");
        var dialog = _.detect(Saiku.tabs._tabs, function(tab) {
            return tab.content instanceof OpenQuery;
        });
        
        if (dialog) {
            dialog.select();
        } else {
            Saiku.tabs.add(new OpenQuery());
        }
        
        return false;
    }
});
