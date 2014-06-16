/**
 * This view wraps the ESRI base map gallery component
 */
define(
    [
        "underscore",
        "backbone",
        "text!" + APP_ROOT + "/amp/map/templates/basemapGallery.html",
    ], function (_, Backbone, Template) {
        'use strict';

        var view = Backbone.View.extend({

                template: _.template(Template),
                tagName: "div",

                events: {

                },

                initialize: function () {
                    _.bindAll(this, "render");
                },

                render: function () {
                    var self = this;
                    this.$el.append(this.template());
                    require(["esri/dijit/BasemapGallery", "esri/arcgis/utils", "dojo/parser", "dijit/layout/BorderContainer", "dijit/layout/ContentPane", "dijit/TitlePane", "dojo/domReady!"], function (BasemapGallery, arcgisUtils, parser) {

                        var basemapGallery = new BasemapGallery({
                            showArcGISBasemaps: true,
                            map: self.options.map
                        }, "basemapGallery");

                        basemapGallery.startup();

                        self.$el.find(".esriBasemapGalleryThumbnail").click(function () {
                            var selText = $(this).text();
                            $(this).parents(".baseMap").find('.dropdown-toggle').html(selText + ' <span class="caret"></span>');
                        });
                    });


                    return this.$el;
                }




            }
        );


        return view;
    })
;
