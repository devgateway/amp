
<div class="dash_graph_opt">
	<c:if test="${graph.barGraphEnabled==true}">
		<img style="padding-left: 5px"
			onclick="changeChart(event, 'bar', '${graph.containerId}', true)"
			src="/TEMPLATE/ampTemplate/img_2/barchart.gif"
			title="<digi:trn>Bar Chart</digi:trn>" />
	</c:if>
	<c:if test="${graph.barProfileGraphEnabled==true}">
		<img style="padding-left: 5px"
			onclick="changeChart(event, 'bar_profile', '${graph.containerId}', true)"
			src="/TEMPLATE/ampTemplate/img_2/barchart.gif"
			title="<digi:trn>Bar Chart</digi:trn>" />
	</c:if>
	<c:if test="${graph.barGrowthGraphEnabled==true}">
		<img style="padding-left: 5px"
			onclick="changeChart(event, 'bar_growth', '${graph.containerId}', true)"
			src="/TEMPLATE/ampTemplate/img_2/barchart.gif"
			title="<digi:trn>Bar Chart</digi:trn>" />
	</c:if>
	<c:if test="${graph.pieGraphEnabled==true}">
		<img style="padding-left: 5px"
			src="/TEMPLATE/ampTemplate/img_2/donutchart.png"
			onclick="changeChart(event, 'donut', '${graph.containerId}', true)"
			title="<digi:trn>Donut Chart</digi:trn>" />
	</c:if>
	<c:if test="${graph.lineGraphEnabled==true}">
		<img style="padding-left: 5px"
			src="/TEMPLATE/ampTemplate/img_2/linechart.gif"
			onclick="changeChart(event, 'line', '${graph.containerId}', true)"
			title="<digi:trn>Line Chart</digi:trn>" />
	</c:if>
	<c:if test="${graph.dataListEnabled==true}">
		<img style="padding-left: 5px"
			src="/TEMPLATE/ampTemplate/img_2/datasheet.gif"
			onclick="changeChart(event, 'dataview', '${graph.containerId}', true)"
			title="<digi:trn>Data View</digi:trn>" />
	</c:if>
</div>