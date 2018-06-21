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
 * Repository query
 */
var RepositoryUrl = "api/repository";
var repoPathUrl = function() {   
    return  RepositoryUrl;
}

var RepositoryObject = Backbone.Model.extend( {
    url: function( ) {
    	Saiku.logger.log("RepositoryObject");
        var segment = repoPathUrl() + "/resource";
        return segment;
    }
} );

var RepositoryAclObject = Backbone.Model.extend( {
    url: function( ) {
    	Saiku.logger.log("RepositoryAclObject.url");
        var segment = repoPathUrl() + "/resource/acl";
        return segment;
    },
    parse: function(response) {
    	Saiku.logger.log("RepositoryAclObject.parse");
        if (response != "OK") {
            _.extend(this.attributes, response);
        }
    }
} );

var RepositoryZipExport = Backbone.Model.extend( {
    url: function( ) {
    	Saiku.logger.log("RepositoryZipExport.url");
        var segment = repoPathUrl() + "/zip";
        return segment;
    }
} );

var SavedQuery = Backbone.Model.extend({

    parse: function(response) {
    	Saiku.logger.log("SavedQuery.parse");
        //console.log("response: " + response);
        //this.xml = response;
    },
    
    url: function() {
    	Saiku.logger.log("SavedQuery.url");
        var u = repoPathUrl() + "/resource";
        return u;

    },
    
    move_query_to_workspace: function(model, response) {
    	Saiku.logger.log("SavedQuery.move_query_to_workspace");
        var file = response;
        var filename = model.get('file');
        for (var key in Settings) {
            if (key.match("^PARAM")=="PARAM") {
                var variable = key.substring("PARAM".length, key.length);
                var Re = new RegExp("\\$\\{" + variable + "\\}","g");
                var Re2 = new RegExp("\\$\\{" + variable.toLowerCase() + "\\}","g");
                file = file.replace(Re,Settings[key]);
                file = file.replace(Re2,Settings[key]);
                
            }
        }
        var query = new Query({ 
            xml: file,
            formatter: Settings.CELLSET_FORMATTER
        },{
            name: filename
        });
        
        var tab = Saiku.tabs.add(new Workspace({ query: query }));
    },
    move_query_to_workspace_json: function(model, response) {
    	Saiku.logger.log("SavedQuery.move_query_to_workspace_json");
        var json = JSON.stringify(response);
        var filename = model.get('file');
        
        var query = new Query({ 
            json: json,
            formatter: Settings.CELLSET_FORMATTER,
            report_id: model.get('report_id'),
            report_token: model.get('report_token'),
            report_type: model.get('report_type'),
            filters: model.get('filters'),
            raw_settings: model.get('settings'),
            settings_data: model.get('settings_data'),
            hierarchies: model.get('hierarchies'),
            measures: model.get('measures'),
            columns: model.get('columns'),
            page_timestamp: model.get('page_timestamp'),
            original_currency: model.get('original_currency')
        },{
            name: filename
        });
                
        query.set('name', filename);
        var tab = Saiku.tabs.add(new Workspace({ query: query }));
    }
});

/**
 * Repository adapter
 */
var Repository = Backbone.Collection.extend({
    model: SavedQuery,
    
    initialize: function(args, options) {
    	Saiku.logger.log("Repository.initialize");
        if (options && options.dialog) {
            this.dialog = options.dialog;
        }
    },
    
    parse: function(response) {
    	Saiku.logger.log("Repository.parse");
        if (this.dialog) {
            this.dialog.populate(response);
        }
    },
    
    url: function() {
    	Saiku.logger.log("Repository.url");
        var segment = repoPathUrl() + "?type=saiku";
        return segment;
    }
});
