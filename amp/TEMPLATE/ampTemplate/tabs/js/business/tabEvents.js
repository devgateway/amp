/*https://gist.github.com/jonnyreeves/2474026*/
/*https://github.com/icereval/backbone-documentmodel*/
define([ 'marionette', 'collections/tabContents', 'models/tabContent' ], function (Marionette, TabContents, TabContent) {

    "use strict";

    function TabEvents() {
        // Constructor
        function TabEvents() {
        }
    }

    // Some private method.
    function putAnimation() {
        return '<span><img src="/images/ajax-loader.gif"/></span>';
    }

    function retrieveTabContent(selectedTabIndex) {
        //var content = app.tabs.children.toArray()[selectedTabIndex].model.attributes.content;
        var content = "";
        var tabContents = new TabContents();
        console.log(tabContents);
        var firstContent = tabContents.first();
        content = firstContent.get('name');
        firstContent.get('metadata').get('filter').get('sector').each(function (item) {
            content += ' ' + item.attributes.value;
        }, this);
        return content;
    }

    // "Class" methods definition here.
    TabEvents.prototype = {
        constructor: TabEvents,
        onCreateTab: function (event, ui) {
            console.log('create tab');
            this.onActivateTab(event, ui);
        },
        onActivateTab: function (event, ui) {
            console.log('activate tab');
            console.log(event);
            console.log(ui);

            //TODO: move this logic elsewhere.
            var panel = null;
            var selectedTabIndex = 0;
            if (ui.panel != undefined) {
                panel = ui.panel;
                selectedTabIndex = ui.tab.index();
            } else {
                panel = ui.newPanel;
                selectedTabIndex = ui.newTab.index();
            }
            // Put loading animation.
            $(panel).html(putAnimation());
            // Simulate time consuming content.
            setTimeout(function () {
                console.log(app.tabs);
                var content = retrieveTabContent(selectedTabIndex);
                $(panel).html('<p>' + content + '</p>');
            }, 1000);
        }
    };

    return TabEvents;
});