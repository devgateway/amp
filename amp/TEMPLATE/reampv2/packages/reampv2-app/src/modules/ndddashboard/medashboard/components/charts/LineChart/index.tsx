import { LineSvgProps, ResponsiveLine } from '@nivo/line'
import React, {useEffect, useState} from 'react';
import ChartUtils from '../../../utils/chart';
import {LineChartData, YearValues} from "../../../types";

const STEP_SIZE = 100;

//@ts-ignore
export interface LineChartProps extends LineSvgProps {
    intervals?: number[];
    height?: number;
    width?: number;
    data: YearValues;
}

const LineChart: React.FC<LineChartProps> = (props) => {
    const { intervals, height, width, data } = props;


    let displayDataSet: LineChartData [] = [];
    let minMax: Record<any, number> = {
        min: 0,
        max: 0
    };

    if (data) {
        displayDataSet = ChartUtils.generateLineChartValues(data);
        const actualValue = ChartUtils.getMaxActualValue(data.actualValues);
         const { min, max } = ChartUtils.getMaxAndMinValueForAxis({
            actualValue,
            baseValue: data.baseValue,
            targetValue: data.targetValue
        });
         //round off the max value to the nearest 10
        minMax = {
            min: Math.floor(min / 10) * 10,
            max: Math.ceil(max / 10) * 10 + (min > 1000 ? 400 : 100)
        };
    }

    const generateSteps = (max:number) => {
        let steps = 50;

        if (max < 100) {
            steps = 10;
        } else if (max < 400) {
            steps = 40;
        }
        else if (max < 500) {
            steps = 50
        } else if (max < 1000) {
            steps = 100;
        }else if (max < 10_000) {
            steps = 1000;
        }else {
            steps = 10_000;
        }

        return steps;
    }

    const tickValues = intervals || ChartUtils.generateTickValues(minMax.min, minMax.max, generateSteps(minMax.max));

    return (
        <div style={{ height: height || 360, width: width || 630 }}>
            <ResponsiveLine
                {...props}
                data={displayDataSet}
                margin={{ top: 10, right: 130, bottom: 50, left: 25 }}
                xScale={{ type: 'point' }}
                yScale={{
                    type: "linear",
                    min: 0,
                    max: minMax.max ? minMax.max  : 150,
                    clamp: true,
                    stacked: false,
                    reverse: false
                }}
                enablePoints={false}
                useMesh={true}
                lineWidth={4}
                colors={item => item.color}
                axisTop={null}
                axisLeft={{
                    tickValues: tickValues,
                    tickSize: 0,
                }}
                enableGridY
                axisBottom={{
                    tickSize: 0,
                    tickRotation: displayDataSet[0].data.length > 15 ? -65 : 0,
                }}

            />
        </div>
    )
}

export default React.memo(LineChart);
