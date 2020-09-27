import React, {Component} from "react";
import '../popups.css';
import {SSCTranslationContext} from '../../../StartUp';
import {
    COLOR_MAP, OTHERS_CODE, SECTORS_LIMIT_CHART,
    SECTORS_OTHERS_ID_CHART
} from '../../../../utils/constants';
import {toCamelCase} from '../../../../utils/Utils';
import {ResponsivePie} from '@nivo/pie';
import Tooltip from '../../../utils/GenericTooltip';
import CustomLegend from '../../../utils/CustomLegend';

class CountryPopupChart extends Component {
    constructor(props) {
        super(props);
    }

    getColor(item) {
        return (COLOR_MAP.get(item.code));
    }

    render() {

        const {chartData, columnCount} = this.props;
        const {translations} = this.context;


        const nonGrouped = chartData.slice(0, SECTORS_LIMIT_CHART).sort((b1, b2) => {
            return b1.percentage < b2.percentage
        });
        const percentageAcum = nonGrouped.reduce((tot, s) =>
            Math.round(((tot + s.percentage) + Number.EPSILON) * 100) / 100
            , 0);

        console.log("percentage acum:" + percentageAcum);
        const others = chartData.slice(SECTORS_LIMIT_CHART, chartData.length);
        if (others.length > 0) {
            const other = {};
            const othersLabel = toCamelCase(translations['amp.ssc.dashboard:sectors-others']);

            other.id = SECTORS_OTHERS_ID_CHART;
            other.value = 0;
            other.percentage = 0;
            other.code = OTHERS_CODE;
            other.otherValues = others;
            others.forEach(o => {
                other.value += o.value;
                other.percentage += o.percentage;
            });

            other.label = `${othersLabel} ${other.percentage}%`;
            other.simpleLabel = othersLabel;
            nonGrouped.push(other);
        }
        const chartComponents = this.getChart(nonGrouped, columnCount);
        return (
            <div className={"row"}>
                <div className="chart-container single-50 float-left">
                    {chartComponents.chart}
                </div>
                {chartComponents.legend && <div className="chart-legend single-50 float-right">
                    {chartComponents.legend}
                </div>}
            </div>
        );
    }

    getChart(data, columnCount) {
        const chartComponents = {};
        chartComponents.chart =
            (<ResponsivePie
                    data={data}
                    margin={{top: 5, right: 5, bottom: 5, left: 5}}
                    innerRadius={0.8}
                    colors={this.getColor.bind(this)}
                    borderWidth={1}
                    sortByValue={false}
                    borderColor={{from: 'color', modifiers: [['darker', 0.2]]}}
                    enableRadialLabels={false}
                    radialLabelsSkipAngle={12}
                    radialLabelsTextXOffset={6}
                    radialLabelsTextColor="#333333"
                    radialLabelsLinkOffset={0}
                    radialLabelsLinkDiagonalLength={16}
                    radialLabelsLinkHorizontalLength={24}
                    radialLabelsLinkStrokeWidth={1}
                    radialLabelsLinkColor={{from: 'color'}}
                    enableSlicesLabels={false}
                    slicesLabelsSkipAngle={10}
                    slicesLabelsTextColor="#333333"
                    animate={true}
                    motionStiffness={90}
                    motionDamping={15}
                    tooltip={(e) =>
                        <Tooltip
                            color={e.color}
                            titleLabel={e.label}
                            values={e.otherValues}
                        />

                    }
                    theme={{
                        tooltip: {
                            container: {
                                padding: '0',
                            },
                        }
                    }}
                    {...(columnCount === 1 ? {'legends': legends} : {})}

                />

            );
        chartComponents.legend = columnCount > 1 ? <CustomLegend data={data}/> : null;
        return chartComponents;
    }
}

const CustomSymbolShape = ({x, y, size, fill}) => {
    return (
        <circle className="donut-segment"
                cx={x + size / 2}
                cy={y + size / 2}
                r={size / 3}
                fill="transparent"
                stroke={fill}
                strokeWidth="2">
        </circle>
    );
};
const legends = [
    {

        anchor: 'right',
        direction: 'column',

        translateY: 0,
        translateX: 200,
        itemWidth: 450,
        itemHeight: 18,
        itemTextColor: '#999',
        symbolSize: 15,
        symbolShape: CustomSymbolShape,
    }
];


CountryPopupChart.contextType = SSCTranslationContext;
export default CountryPopupChart;
