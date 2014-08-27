/*https://gist.github.com/jonnyreeves/2474026*/
/*https://github.com/icereval/backbone-documentmodel*/
define([ 'marionette', 'collections/contents', 'models/content', 'views/dynamicContentView' ],
    function (Marionette, Contents, Content, DynamicContentView) {

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
            var content = "";
            // Get collection with data we will use to render the tab content.
            var tabContents = new Contents();
            var firstContent = tabContents.first();

            /*
             1) Defino class Filter(model)
             2) defino class Filters(collection)
             3) defino class FilterItemView(itemView)
             4) defino class FiltersCollectionView(collectionView)
             5) defino region donde insertar la FiltersCollectionView.
             6) defino los templates para las 2 vistas (inline y simples por ahora).
             7) tomo los datos de firstContent.metadata.filters y los convierto en una colección de objetos Filter.
             8) instancio todos los objetos y los meto en la region.
             */

            // Create a region where the dynamic content will be rendered inside the tab.
            var regionsName = '#main-dynamic-content-region_' + selectedTabIndex;
            app.TabsApp.addRegions({'filtersRegion': regionsName});

            var Item = Marionette.ItemView.extend({
                tagName: 'li',
                template: '<p>nada</p>'
            });

            // Create LayoutView object setting template.
            var dynamicLayoutView = new DynamicContentView();
            app.TabsApp.filtersRegion.show(dynamicLayoutView);
            dynamicLayoutView.filters.show(new Item());

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
                //$(panel).html(putAnimation());
                // Simulate time consuming content.
                setTimeout(function () {
                    console.log(app.tabs);
                    var content = retrieveTabContent(selectedTabIndex);
                    //$(panel).html('<p>' + content + '</p>');
                }, 1000);
            }
        };

        return TabEvents;
    });