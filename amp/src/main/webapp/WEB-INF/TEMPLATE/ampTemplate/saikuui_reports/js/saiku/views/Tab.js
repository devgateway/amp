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
 * Class which handles individual tabs
 */
var Tab = Backbone.View.extend({
    tagName: 'li',
    
    events: {
        'click a': 'select',
        'mousedown a': 'remove',
        'click .close_tab': 'remove'
    },
    
    template: function() {        	
        // Create tab
        return _.template("<a class='saikutab' href='#<%= id %>'><%= caption %></a>" +
                "<span class=''>&nbsp</span>")
            ({
                id: this.id,
                caption: this.caption
            });
    },
    
    /**
     * Assign a unique ID and assign a Backbone view 
     * to handle the tab's contents
     * @param args
     */
    initialize: function(args) {
    	Saiku.logger.log("Tab.initialize");
        _.extend(this, Backbone.Events);
        _.extend(this, args);
        this.content.tab = this;
        this.caption = this.content.caption();
        this.id = _.uniqueId('tab_');
        this.close = args.close;
    },
    
    /**
     * Render the tab and its contents
     * @returns tab
     */
    render: function() {
    	Saiku.logger.log("Tab.render");
        var self = this;
        // Render the content
        this.content.render();

        // Generate the element
        $(this.el).html(this.template());
        if(this.close==false){
            $(this.el).find('.close_tab').hide();
            $(this.el).css('padding-right','10px')
        }
        return this;
    },

    set_caption: function(caption) {
    	Saiku.logger.log("Tab.set_caption");
        $(this.el).find('.saikutab').html(caption);
    },
    
    /**
     * Destroy any data associated with this tab and ensure proper
     * garbage collection to avoid memory leaks
     */
    destroy: function() {
    	Saiku.logger.log("Tab.destroy");
        // Delete data
        if (this.content && this.content.query) {
            this.content.query.destroy();
        }
    },
    
    /**
     * Select a tab
     * @param el
     */
    select: function() {
    	Saiku.logger.log("Tab.select");
        var self = this;
        // Deselect all tabs
        this.parent.select(this);
        
        // Select the selected tab
        $(this.el).addClass('selected');
        
        // Trigger select event
        this.trigger('tab:select');
        return false;
    },
    
    /**
     * Remove a tab
     * @returns {Boolean}
     */
    remove: function(event) {
    	Saiku.logger.log("Tab.remove");
        if (!event || event.which === 2 || $(event.target).hasClass('close_tab')) {
            // Remote the tab object from the container
            this.parent.remove(this);

            try {
                // Remove the tab element
                $(this.el).remove();
                
                // Remove the tab                
                this.destroy();
            } catch (e) {
                Log.log(JSON.stringify({
                    Message: "Tab could not be removed",
                    Tab: JSON.stringify(this)
                }));
            }
        }
   
        return false;
    }
});

/**
 * Class which controls tab pager
 */
var TabPager = Backbone.View.extend({
    className: 'pager_contents',
    events: {
        'click a': 'select'
    },
    
    initialize: function(args) {
    	Saiku.logger.log("TabPager.initialize");
        this.tabset = args.tabset;
        $(this.el).hide().appendTo('body');
        
        // Hide when focus is lost
        $(window).click(function(event) {
            if (! $(event.target).hasClass('pager_contents')) {
                $('.pager_contents').hide();
            }
        });
    },
    
    render: function() {
    	Saiku.logger.log("TabPager.render");
        var pager = "";
        for (var i = 0, len = this.tabset._tabs.length; i < len; i++) {
            pager += "<a href='#" + i + "'>" + 
                this.tabset._tabs[i].caption + "</a><br />";
        }
        $(this.el).html(pager);
        $(this.el).find(".i18n").i18n(Saiku.i18n.po_file);
    },
    
    select: function(event) {
    	Saiku.logger.log("TabPager.select");
        var index = $(event.target).attr('href').replace('#', '');
        this.tabset._tabs[index].select();
        $(this.el).hide();
        event.preventDefault();
        return false;
    }
});

/**
 * Class which controls the tab collection
 */
var TabSet = Backbone.View.extend({
    className: 'tabs',
    queryCount: 0,
    
    events: { 
        'click a.pager': 'togglePager' ,
        'click a.new' : 'new_tab'
    },
    
    _tabs: [],
    
    /**
     * Render the tab containers
     * @returns tab_container
     */
    render: function() {
    	Saiku.logger.log("TabSet.render");
        $(this.el).html('<ul><li class="newtab"><a class="new">+&nbsp;&nbsp;</a></li></ul>')
            .appendTo($('#header'));
        this.content = $('<div id="tab_panel">').appendTo($('body'));
        this.pager = new TabPager({ tabset: this });
        return this;
    },
    
    /**
     * Add a tab to the collection
     * @param tab
     */
    add: function(content, close) {
    	Saiku.logger.log("TabSet.add");
        // Add it to the set
        this.queryCount++;

        var tab = new Tab({ content: content, close: close});
        this._tabs.push(tab);
        tab.parent = this;
        
        // Render it in the background, then select it
        tab.render().select();
        $(tab.el).insertBefore($(this.el).find('ul li.newtab'));
        
        // Trigger add event on session
        Saiku.session.trigger('tab:add', { tab: tab });
        this.pager.render();
        
        return tab;
    },

    find: function(id) {
    	Saiku.logger.log("TabSet.find");
        for (var i = 0, len = this._tabs.length; i < len; i++) {
            if (this._tabs[i].id == id) {
                return this._tabs[i];
            }
        }
        return null;
    },
    
    /**
     * Select a tab, and move its contents to the tab panel
     * @param tab
     */
    select: function(tab) {
    	Saiku.logger.log("TabSet.select");
        // Clear selections
        $(this.el).find('li').removeClass('selected');
        
        // Replace the contents of the tab panel with the new content

            this.content.children().detach();
            this.content.append($(tab.content.el));

    },
    
    /**
     * Remove a tab from the collection
     * @param tab
     */
    remove: function(tab) {
    	Saiku.logger.log("TabSet.remove");
        // Add another tab if the last one has been deleted
        if (this._tabs.length == 1) {
            //this.add(new Workspace());

        }
        
        for (var i = 0, len = this._tabs.length; i < len; i++) {
            if (this._tabs[i] == tab) {
                // Remove the element
                this._tabs.splice(i, 1);

                Saiku.session.trigger('tab:remove', { tab: tab });
                this.pager.render();                
                // Select the previous, or first tab
                var next = this._tabs[i] ? i : (this._tabs.length - 1);
                this._tabs[next].select();
            }
        }
        
        return true;
    },

    close_others: function(tab) {
    	Saiku.logger.log("TabSet.close_others");
        var index = _.indexOf(this._tabs, tab);
        this._tabs[index].select();
        
        // Remove tabs placed before and after selected tab
        var i = 0;
        while(1 < this._tabs.length){
            if (this._tabs[i] != tab)
                this._tabs[i].remove();
            else
                i++;
        }
    },
    
    close_all: function() {
    	Saiku.logger.log("TabSet.close_all");
        for (var i = 0, len = this._tabs.length; i < len; i++) {
            var otherTab = this._tabs[i];
            otherTab.remove();
        }
    },
    
    togglePager: function() {
    	Saiku.logger.log("TabSet.togglePager");
        $(this.pager.el).toggle();
        return false;
    },

    new_tab: function() {
    	Saiku.logger.log("TabSet.newTab");
        this.add(new Workspace());
        var next = this._tabs.length - 1;
        this._tabs[next].select();
        return false;
    },
    
    duplicate: function(tab) {
    	Saiku.logger.log("TabSet.duplicate");
        // Block UI to prevent other events
        Saiku.ui.block("Duplicating tab...");
        
        // Check for empty query
        if(tab.content.query){
            // For versions using Query2Resource
            this.add(new Workspace({
                query : new Query({
                    json : JSON.stringify(tab.content.query.model)
                }, Settings.PARAMS),
                viewState : tab.content.viewState
            }));
            
        } else {
            this.add(new Workspace());
        }
        
        // Unblock UI and restore functionality
        Saiku.ui.unblock();
        return false;
    }
});
