var config = require('./webpack.dev.config.js');
var webpack = require('webpack');
config.entry["admin/currency/deflator/script"] = "./modules/admin/currency/deflator/script.es6";
config.entry["admin/dashboard/script"] = "./modules/admin/dashboard/script.es6";
config.entry["gpi-data/script"] = "./modules/gpi-data/script.es6";
config.entry["gpi-reports/script"] = "./modules/gpi-reports/script.es6";
config.entry["admin/resource-manager-admin/script"] = "./modules/admin/resource-manager-admin/script.es6";
config.entry["admin/data-freeze-manager/script"] = "./modules/admin/data-freeze-manager/script.es6";
config.entry["admin/performance-alert-manager/script"] = "./modules/admin/performance-alert-manager/script.es6";
config.entry["ampoffline/download/script"] = "./modules/ampoffline/download/script.es6";
config.entry["activity/preview/script"] = "./modules/activity/preview/script.es6";

config.output.filename = "modules/[name].min.js";
delete config.output.publicPath;
delete config.devtool;
config.plugins.push(new webpack.optimize.CommonsChunkPlugin({
	    	name: 'dependencies-bundle',
	        filename: 'dependencies-bundle.js',
	        minChunks(module, count) {
	            var context = module.context;
	            return context && context.indexOf('node_modules') >= 0 && count > 1;
	        }
	      }));
config.plugins.push(new webpack.DefinePlugin({'process.env': {
		'NODE_ENV': JSON.stringify('production')
	}
}));
config.plugins.push(new webpack.optimize.UglifyJsPlugin({sourceMap: false, compress: false}));
config.plugins.push(new webpack.optimize.DedupePlugin());
module.exports = config;
