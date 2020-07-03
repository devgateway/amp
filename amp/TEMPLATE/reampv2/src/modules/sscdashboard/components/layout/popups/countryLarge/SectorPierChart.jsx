import React, { Component } from "react";
import { ResponsivePie } from '@nivo/pie'
import Tooltip from '../../../utils/GenericTooltip';
import { SECTORS_OTHERS_ID_CHART } from '../../../../utils/constants';
// make sure parent container have a defined height when using
// responsive component, otherwise height will be 0 and
// no chart will be rendered.
const SectorPierChart = ({data /* see data tab */}) => (
    <ResponsivePie
        data={data}
        margin={{top: 5, right: 5, bottom: 5, left: 5}}
        innerRadius={0.8}
        colors={{scheme: 'set1'}}
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
            e.id !== SECTORS_OTHERS_ID_CHART ? null : (
                <Tooltip
                    color={e.color}
                    titleLabel={e.label}
                    values={e.otherValues}
                />)
        }
        theme={{
            tooltip: {
                container: {
                    padding: '0',
                },
            }
        }}
        legends={[
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
        ]}
    />
);
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
export default SectorPierChart;

