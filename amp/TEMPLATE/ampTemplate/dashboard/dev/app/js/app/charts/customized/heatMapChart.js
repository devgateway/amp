nv.models.heatmap = function() {
    "use strict";

    //============================================================
    // Public Variables with Default Settings
    //------------------------------------------------------------

    var margin = {top: 0, right: 0, bottom: 0, left: 0}
        , width = 500
        , height = 500
        , getX = function(d) { return d.x }
        , getY = function(d) { return d.y }
        , id = Math.floor(Math.random() * 10000) //Create semi-unique ID in case user doesn't select one
        , duration = 250
        , dispatch = d3.dispatch('chartClick', 'elementClick', 'elementDblClick', 'elementMouseover', 'elementMouseout', 'renderEnd')
        ;


    //============================================================
    // chart function
    //------------------------------------------------------------

    var renderWatch = nv.utils.renderWatch(dispatch);

    function chart(selection) {
        renderWatch.reset();
        renderWatch.renderEnd('heatmap immediate');
        return chart;
    }

    //============================================================
    // Expose Public Variables
    //------------------------------------------------------------

    chart.dispatch = dispatch;
    chart.options = nv.utils.optionsFunc.bind(chart);

    chart._options = Object.create({}, {
        // simple options, just get/set the necessary values
        width:      {get: function(){return width;}, set: function(_){width=_;}},
        height:     {get: function(){return height;}, set: function(_){height=_;}},
        x:          {get: function(){return getX;}, set: function(_){getX=_;}},
        id:         {get: function(){return id;}, set: function(_){id=_;}},

        // options that require extra logic in the setter
        margin: {get: function(){return margin;}, set: function(_){
            margin.top    = typeof _.top    != 'undefined' ? _.top    : margin.top;
            margin.right  = typeof _.right  != 'undefined' ? _.right  : margin.right;
            margin.bottom = typeof _.bottom != 'undefined' ? _.bottom : margin.bottom;
            margin.left   = typeof _.left   != 'undefined' ? _.left   : margin.left;
        }},
        y: {get: function(){return getY;}, set: function(_){
            getY=d3.functor(_);
        }}
    });

    nv.utils.initOptions(chart);
    return chart;
};

nv.models.heatMapChart = function() {
	"use strict";

	//============================================================
    // Public Variables with Default Settings
    //------------------------------------------------------------	

    var heatmap = nv.models.heatmap();
    //var legend = nv.models.legend().margin({top: 0, right: 0, bottom: 0, left: 0});

    var margin = {top: 30, right: 20, bottom: 20, left: 20}
    //var legendMargin = {top: 30, right: 20, bottom: 20, left: 20}
    var width = null;
	var height = null;
    var showLegend = false;
    var color = nv.utils.defaultColor();
    var tooltips = true;
    var tooltip = function(key, y, e, graph) {
    	return '<h3 style="background-color: '
        	+ e.color + '">' + key + '</h3>'
            + '<p>' +  y + '</p>';
        }
    var	state = nv.utils.state();
    var defaultState = null;
    var noData = "No Data Available.";
    var duration = 250;
    var dispatch = d3.dispatch('tooltipShow', 'tooltipHide', 'stateChange', 'changeState','renderEnd');

    //============================================================
    // Private Variables
    //------------------------------------------------------------
    var renderWatch = nv.utils.renderWatch(dispatch);

    var stateGetter = function(data) {
    	return function(){
    		return {
    			active: data.map(function(d) { return !d.disabled })
            };
        }
    };

    var stateSetter = function(data) {
        return function(state) {
            if (state.active !== undefined) {
                data.forEach(function (series, i) {
                    series.disabled = !state.active[i];
                });
            }
        }
    };
  
    var shortenText = function(text) {
    	var length = 17;
    	if (text.length > length) {
    		text = text.substring(0, length) + '...';
    	}
    	return text;
    };

    //============================================================
    // Chart function
    //------------------------------------------------------------

    function chart(selection) {
    	var _self = this;
    	var _ = require('underscore'); // This doesnt works on top of the file :(((
    	//console.log('heatMapChart.chart');
        renderWatch.reset();
        renderWatch.models(heatmap);

        selection.each(function(data) {//TODO: selection.each????
        	// Get currency for later.
        	var currencySettings = _.find(app.settings.models, function(item) {return item.get('id') === '1'});
        	var selectedCurrency = _.find(currencySettings.get('options'), function(item) {return item.selected === true}).value;
        	
        	var container = d3.select(this);
            nv.utils.initSVG(container);

            var availableWidth = (width || parseInt(container.style('width'), 10) || 960)
                    - margin.left - margin.right;
            var availableHeight = (height || parseInt(container.style('height'), 10) || 400)
                    - margin.top - margin.bottom;

            //chart.update = function() { container.transition().call(chart); }; //comented to avoid adding the chart again.
            chart.container = this;

            state.setter(stateSetter(data), chart.update)
                .getter(stateGetter(data))
                .update();

            //set state.disabled
            state.disabled = data.map(function(d) { return !!d.disabled });

            if (!defaultState) {
                var key;
                defaultState = {};
                for (key in state) {
                    if (state[key] instanceof Array)
                        defaultState[key] = state[key].slice(0);
                    else
                        defaultState[key] = state[key];
                }
            }

            //TODO: move these definitions to top.
            var innerMargin = {
            		top : 120,
        			right : 0,
        			bottom : 100,
        			left : 150
            };
        	var cubeSize = 30;
        	var width = 1024 - innerMargin.left - innerMargin.right;
        	var topSectionHeight = 180;
        	var legendSectionHeight = 20;
        	var height = topSectionHeight + (cubeSize * data[0].values.y.length) + legendSectionHeight;
        	var legendElementHeight = 22;
        	var noColor = '#FFFFFF';
        	var categories = [{min: -1, max: 0, color: noColor},
        	                  {min: 0, max: 1, color: "#D05151"},
        	                  {min: 1, max: 5, color: "#E68787"}, 
        	                  {min: 5, max: 10, color: "#E4883E"}, 
        	                  {min: 10, max: 15, color: "#F6B277"}, 
        	                  {min: 15, max: 20, color: "#ADCD95"}, 
        	                  {min: 20, max: 101, color: "#7BA05F"}];
        	
        	$(container[0]).attr('height', height).attr('class', 'dash-chart nvd3-svg heatmap-chart');
        	
        	var svg = container
        		.append("g")
        		.attr("transform", "translate(" + innerMargin.left + "," + innerMargin.top + ")");
        	
        	// Add SVG filter for cell highlight.
        	svg.append("defs").append("filter").attr("id", "filterSaturate").append("feColorMatrix").attr("in", "SourceGraphic").attr("type", "saturate").attr("values", "5");
        	svg.append("defs").append("filter").attr("id", "filterLuminanceToAlpha").append("feColorMatrix").attr("in", "SourceGraphic").attr("type", "luminanceToAlpha");
        	svg.append("defs").append("filter").attr("id", "filterBlur").append("feGaussianBlur").attr("in", "SourceGraphic").attr("stdDeviation", "2");
        	svg.append("defs").append("filter").attr("id", "filterDarken").append("feColorMatrix").attr("type", "matrix").attr("values", "0.5 0 0 0 0 0 0.5 0 0 0 0 0 0.5 0 0 0 0 0 1 0");

        	// Rows container.
        	var yAxisLabelsContainer = svg        	
				.append("g")
				.attr("class", "heatmap-yAxis-container");
        	
        	// Rows.
        	var yLabels = yAxisLabelsContainer
        		.selectAll(".yLabel")
        		.data(data[0].values.y)
        		.enter()
        		.append("text")
        		.text(function(d) {
        			return shortenText(d);
        		})
        		.attr("x", 0)
        		.attr("y", function(d, i) {
        			return i * cubeSize;
        		})
        		.style("text-anchor", "end")
        		.attr("transform", "translate(-6," + cubeSize / 1.5 + ")")
        		.attr("class", function(d, i) {
        							return "yLabel mono axis nv-series";
        		})
        		.attr('data-title', function(d) {
        			return d;
        		});
        	
        		// Format "Others" special row if needed.
        		if (data[0].values.yCount < data[0].values.yTotalCount) {
        			var self = this;
        			var data2 = data;
        			var textElement = $(container[0]).find('.yLabel').last();
        			$(textElement).attr("class", function(d, i) {
        				return $(textElement).attr('class') + ' legend-others';
        			})/*.on('click', function(event) {
        				data2[0].values.model.set('yLimit', data2[0].values.model.get('yLimit') + 5);
        				//chart.dispatch.elementClick;
        				//chart.update();
        				dispatch.elementClick();
        			})*/;
        		}
        	
        		// Add Totals special row.
        		yAxisLabelsContainer.append("text")
        			.text("TOTALS")
        			.attr("x", 0)
        			.attr("y", (data[0].values.y.length * cubeSize))
        			.attr("class", "yLabel mono axis nv-series heatmap-totals")
        			.style("text-anchor", "end")
        			.style("font-weight", "bold")
            		.attr("transform", "translate(-6," + cubeSize / 1.5 + ")")
            		.attr('data-i18n', 'amp.dashboard:chart-heatmap-totals');

        		// Columns container.
        		var xAxisLabelsContainer = svg
        			.append("g")
        			.attr("transform", "translate(18.5, -5)")
        			.attr("class", "heatmap-xAxis-container");

        		// Columns
        		var yAxisLabels = xAxisLabelsContainer
        			.selectAll(".xLabel")
        			.data(data[0].values.x)
        			.enter()
        			.append("text")
        			.text(function(d) {
        				return shortenText(d);
        			})
        			.attr("x", function(d, i) {
        				return i * cubeSize;
        			})
        			.attr("y", 0)
        			/*.style("text-anchor", "middle")*/
        			.attr("transform", function(d, i) {
        				return "rotate(270, " + (cubeSize * i) + ", 0)";
        			})
        			.attr("class", function(d, i) {
        							return "xLabel mono axis nv-series";
        			})
        			.attr('data-title', function(d) {
        				return d;
        			});
        		
        		// Add Totals special column.
        		xAxisLabelsContainer.append("text")
        			.text("TOTALS")
        			.attr("x", cubeSize * data[0].values.x.length)
        			.attr("y", 0)
        			.attr("class", "xLabel mono axis nv-series heatmap-totals")
        			.style("font-weight", "bold")
            		.attr("transform", function(d, i) {
        				return "rotate(270, " + (cubeSize * data[0].values.x.length) + ", 0)";
        			})
        			.attr('data-i18n', 'amp.dashboard:chart-heatmap-totals');

        		// Cubes
        		var cubesContainer = svg
    				.append("g")
    				.attr("class", "heatmap-cubes-container");
        		for (var i = 0; i < data[0].values.length; i++) {
        			createCube(cubesContainer, $.extend(data[0].values[i], {tooltip: selectedCurrency + ' ' + data[0].values[i].amount}), cubeSize, noColor, categories);        			    				
        		}        
        		// Add total's row in the end.        		
        		for (var i = 0; i < data[0].values.x.length; i++) {
        			createCube(cubesContainer, {x: i + 1, y: data[0].values.y.length + 1, value: data[0].values.xPTotals[i], tooltip: selectedCurrency + ' ' + data[0].values.xTotals[i]}, cubeSize, noColor, categories);
        		}
        		// Add total's column on the right side.
        		for (var j = 0; j < data[0].values.y.length; j++) {
        			createCube(cubesContainer, {x: data[0].values.x.length + 1 , y: j + 1, value: data[0].values.yPTotals[j], tooltip: selectedCurrency + ' ' + data[0].values.yTotals[j]}, cubeSize, noColor, categories);
        		}
        		
        		// Add percentage legends.
        		createLegends(svg, data, cubeSize, categories, legendElementHeight);
        		
        		app.translator.translateDOM(svg[0]);
        });
        
        renderWatch.renderEnd('heatmap immediate');
        return chart;
    }
    
    function createLegends(svg, data, cubeSize, categories, legendElementHeight) {
    	var legendsContainer = svg
			.append("g")
			.attr("transform", "translate(0, " + (((data[0].values.y.length + 1) * cubeSize) + 10) + ")")
			.attr("class", "heatmap-legends-container");
    	var legendsPool = [app.translator.translateSync("amp.dashboard:chart-heatmap-legend1"),
    	                   app.translator.translateSync("amp.dashboard:chart-heatmap-legend2"),
    	                   app.translator.translateSync("amp.dashboard:chart-heatmap-legend3"),
    	                   app.translator.translateSync("amp.dashboard:chart-heatmap-legend4"),
    	                   app.translator.translateSync("amp.dashboard:chart-heatmap-legend5"),
    	                   app.translator.translateSync("amp.dashboard:chart-heatmap-legend6")];
    	var maxLegendTextWidth = 0;
    	for (var i = 0; i < legendsPool.length; i++) {
    		var auxWidth = calculateTextWidth(legendsPool[i]);
    		if (auxWidth > maxLegendTextWidth) {
    			maxLegendTextWidth = auxWidth;
    		}
    		$("#tempSpan").remove();
    	}
    	maxLegendTextWidth += 20;
    	
    	for (var i = 0; i < legendsPool.length; i++) {
    		var legends = legendsContainer
				.append("rect")
				.attr("x", (i * maxLegendTextWidth))
				.attr("width", maxLegendTextWidth)
				.attr("height", legendElementHeight)
				.attr("class", "bordered")
				.style("fill", categories[i + 1].color);
		
			var text = legendsContainer.append("text"); 
			text.attr('font-family', 'Arial')
				.attr('font-size', '11px')
				.attr("y", 15)
				.attr("x", ((i * maxLegendTextWidth) + ((maxLegendTextWidth - calculateTextWidth(legendsPool[i])) / 2)))
				.html(legendsPool[i]);
    	}
    	
    	legendsContainer.append("text")
			.text("LEGENDS")
			.attr("x", -6)
			.attr("y", 15)
			.attr("class", "yLabel mono axis nv-series heatmap-totals")
			.style("text-anchor", "end")
			.style("font-weight", "bold")
			.attr('data-i18n', 'amp.dashboard:chart-heatmap-legends');
    }
    
    function createCube(cubesContainer, data, cubeSize, noColor, categories) {
    	var selfData = data;
    	var cube = cubesContainer
			.append("rect")
			.attr("x", ((data.x - 1) * cubeSize))
			.attr("y", ((data.y - 1) * cubeSize))
			.attr("rx", 4)
			.attr("ry", 4)
			.attr("class", "bordered")
			.attr("width", cubeSize)
			.attr("height", cubeSize)
			.style("fill", noColor)
			.attr("data-x", data.x - 1)
			.attr("data-y", data.y - 1);
		
		cube.transition()
			.duration(1000)
			.style("fill", calculateColorFromCategories(data.value, categories, noColor));
		
		var text = cubesContainer.append("text"); 
		text.attr('font-family', 'Arial')
			.attr('font-size', '11px')
			.attr("x", ((data.y - 1) * cubeSize))
			.attr("y", ((data.y - 1) * cubeSize) + 19)
			.attr("class", "heatmap-cell")
			.attr("x", function() {
				var d = data;
				var auxVal = d.value;
				if (auxVal > 0 && auxVal < 1) {
					return ((d.x - 1) * cubeSize) + 4;
				} else if (auxVal < 10) {
					return ((d.x - 1) * cubeSize) + 8;
				} else if (auxVal == 100) {
					return ((d.x - 1) * cubeSize) + 1;	
				} else {
					return ((d.x - 1) * cubeSize) + 5;
				}					
			}).html(function() {
				var d = data;
				var auxVal = d.value;
				if (auxVal > -1) {
					if (auxVal > 0 && auxVal < 1) {
						return '<1%';
					} else {
						return auxVal + '%';
					}
				} else {
					return '';
				}
			})
			.on("click", function(obj) {				
				// Remove all filters applied.
				$($(cubesContainer[0]).find("rect")).removeAttr("filter");
				// Apply "darken" to the row and column.		
				$($(cubesContainer[0]).find("[data-x='"+ (selfData.x - 1) + "']")).attr("filter", "url(#filterDarken)");
				$($(cubesContainer[0]).find("[data-y='"+ (selfData.y - 1) + "']")).attr("filter", "url(#filterDarken)");
				// Highlight this cell.
				$($(cubesContainer[0]).find("[data-y='"+ (selfData.y - 1) + "']" + "[data-x='"+ (selfData.x - 1) + "']")).removeAttr("filter");
			}).on("mouseover", function(obj) {				
				//console.log(selfData);
			});
		if (data.tooltip) {
			text.attr('data-title', data.tooltip)
				.attr("class", "nv-series heatmap-cell");
		}
    }
    
    function calculateColor(value, colors) {
    	var color = "";
    	var cutPoint = 100 / colors.length;
    	for (var i = 0; i < colors.length ; i++) {
    		if ((value >= (i * cutPoint)) && (value < ((i + 1) * cutPoint))) {
    			color = colors[i];
    			break;
    		}
    	}    	
    	return color;
    }
    
    function calculateColorFromCategories(value, categories, noColor) {
    	var color = noColor;
    	for (var i = 0; i < categories.length; i++) {
    		if ((value >= categories[i].min) && (value < categories[i].max)) {
    			color = categories[i].color;
    			break;
    		}
    	}
    	return color;
    }
    
    function calculateTextWidth(text) {
    	$("body").append("<span id='tempSpan' class='invisible'>" + text + "</span>");
    	var auxWidth = $("#tempSpan").width();
    	$("#tempSpan").remove();
    	return auxWidth;
    }

    //============================================================
    // Event Handling/Dispatching (out of chart's scope)
    //------------------------------------------------------------

    /*dispatch.on('elementMouseover.tooltip', function(e) {
        e.pos = [e.pos[0] +  margin.left, e.pos[1] + margin.top];
        dispatch.tooltipShow(e);
    });

    dispatch.on('tooltipShow', function(e) {
        if (tooltips) showTooltip(e);
    });

    dispatch.on('tooltipHide', function() {
        if (tooltips) nv.tooltip.cleanup();
    });*/

    //============================================================
    // Expose Public Variables
    //------------------------------------------------------------

    // expose chart's sub-components
    chart.legend = {};/*legend;*/
    chart.dispatch = dispatch;
    chart.heatmap = heatmap;
    chart.options = nv.utils.optionsFunc.bind(chart);

    // use Object get/set functionality to map between vars and chart functions
    chart._options = Object.create({}, {    	
        // simple options, just get/set the necessary values
        noData:         {get: function(){return noData;},         set: function(_){noData=_;}},
        defaultState:   {get: function(){return defaultState;},   set: function(_){defaultState=_;}},
        // options that require extra logic in the setter
        duration: {get: function(){return duration;}, set: function(_){
            duration = _;
            renderWatch.reset(duration);
        }},
        color: {},
        margin: {get: function(){return margin;}, set: function(_){
            margin.top    = _.top    !== undefined ? _.top    : margin.top;
            margin.right  = _.right  !== undefined ? _.right  : margin.right;
            margin.bottom = _.bottom !== undefined ? _.bottom : margin.bottom;
            margin.left   = _.left   !== undefined ? _.left   : margin.left;
        }},
        tooltipContent: {}
    });
    
    chart.height = function(_) {
        if (!arguments.length) return height;
        height = _;
        return chart;
    };
    
    nv.utils.inheritOptions(chart, heatmap);
    nv.utils.initOptions(chart);
    return chart;
};