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
 * Central object for handling global application state
 */
var Saiku = {

    logger : new AMPCustomLogging(),
    /**
     * View which manages toolbar interactions
     */
    toolbar : {},

    /**
     * View which handles tabs
     */
    tabs : new TabSet(),

    splash: null,

    /**
     * Model which handles session and authentication
     */
    session : null,

    /**
     * Global event bus
     */
    events : _.extend({}, Backbone.Events),

    /**
     * Collection of routers for page fragments
     */
    routers : [],

    totalBatches : 0 /* zero-based */,

    currentActiveBatch : 0,

    /**
     * Convenience functions for blocking the UI
     */
    ui : {
        block : function(message, callback) {
            $('.processing_message').html(message);
            $('.processing_message').removeClass("i18n_translated").addClass(
                "i18n");
            Saiku.i18n.translate();
            $('.processing,.processing_container').show();
            if(callback !== undefined) {
                callback();
            }
        },

        unblock : function() {
            $('.processing,.processing_container, .blockOverlay').hide();

            // Fix For Internet Explorer 10 UIBlock issue
            $('.blockUI').fadeOut('slow');
        }
    }
};

/**
 * Setting this option to true will fake PUT and DELETE requests with a HTTP
 * POST, and pass them under the _method parameter. Setting this option will
 * also set an X-HTTP-Method-Override header with the true method. This is
 * required for BI server integration
 */
Backbone.emulateHTTP = false;

// Load plugins.
$(document).ready(function () {
    Saiku.logger.log('Saiku.Document Ready');
    /* This is a change from original Saiku behavior, we need to know if the report is Donor/Pledge before
    loading the Filters widget. */
    var id = hash.match(/\d+$/);
    $.getJSON(Settings.AMP_PATH + '/' + id, loadPlugins);
});

function loadPlugins(data) {
    // Save report's metadata for later usage in QueryRouter.js
    Saiku.originalReportMetadata = data;
    Saiku.logger.log(data.reportMetadata.reportSpec.reportType);
    var plugins = new PluginCollection();
    plugins.fetch({
        success: function () {
            var i = plugins.size();
            var j = 0;
            plugins.each(function (plugin) {
                j = j + 1;

                //Is important to make these calls sync so we continue only after loading all plugins.
                jQuery.ajax({
                    url: plugin.attributes.path,
                    dataType: "script",
                    async: false,
                    cache: true
                });

                if (j === i) {
                    Saiku.logger.log('Saiku.Create Session');
                    Saiku.session = new Session({}, {
                        username: Settings.USERNAME,
                        password: Settings.PASSWORD
                    });

                    Saiku.session.bind("tab:add", function () {
                    });
                }
            });
        }
    });
}