import {ActualValue, LineChartData, YearValues} from "../types";
import {printChart} from "../../../sscdashboard/utils/PrintUtils";
import {
    BASE_VALUE,
    BASE_VALUE_COLOR, CURRENT_VALUE, CURRENT_VALUE_COLOR,
    DEFAULT_REPORTING_PERIOD,
    TARGET_VALUE,
    TARGET_VALUE_COLOR
} from "../../utils/constants";

interface GaugeUtils {
    baseValue: number,
    actualValue: number,
    targetValue: number
}

class ChartUtils {
    public static generateTickValues = (min: number, max: number,  step: number) => {
        const tickValues: number[] = [];
        for (let i = min; i <= max; i += step) {
            tickValues.push(i);
        }
        return tickValues;
    }

    public static generateGaugeValue = (data : GaugeUtils) => {
        const { baseValue, actualValue, targetValue } = data;
        if (!targetValue || !baseValue) {
            return 0;
        }

        const actual = actualValue ? actualValue : baseValue;

        // formula:  [(Current value - Base value) / (Target value - Base value)]*100
        const progress = (actual - baseValue) / (targetValue - baseValue);
        return Math.round(progress * 100);
    }

    public static getActualValueForCurrentYear = (actualValues: ActualValue []) => {
        if (!actualValues || actualValues.length === 0) return 0;

        const currentYear = new Date().getFullYear();
        const actualValue = actualValues.find((actual) => actual.year === currentYear);

        if (!actualValue) {
            // if current year is not found, get the most recent year
            const sortedActualValues = actualValues.sort((a: any, b: any) => b.year - a.year);
            return sortedActualValues[0].value;
        }

        return actualValue ? actualValue.value : 0;
    }

    public static computeAggregateValues = (data: YearValues []) => {
        return data.reduce((acc, curr) => {
            acc.actualValue += ChartUtils.getActualValueForCurrentYear(curr.actualValues);
            acc.targetValue += curr.targetValue;
            acc.baseValue += curr.baseValue;
            return acc;
        }, {actualValue: 0, targetValue: 0, baseValue: 0});
    }

    public static downloadChartImage = (title: string, containerId: string) => {
        printChart(title, containerId, [], 'png', false, 'print-simple-dummy-container', false);
    }

    public static generateLineChartValues = (data: YearValues): LineChartData [] => {
        const { actualValues, targetValue, baseValue } = data;
        const reportlength = actualValues.length >= 5 ? actualValues.length: DEFAULT_REPORTING_PERIOD;

        const baseValueArrayWithYear = new Array(reportlength).fill(baseValue).map((value, index) => {
            return {
                x: (new Date().getFullYear() - index).toString(),
                y: value as number
            };
        });

        const targetValueArrayWithYear = new Array(reportlength).fill(targetValue).map((value, index) => {
            return {
                x: (new Date().getFullYear() - index).toString(),
                y: value as number
            };
        });

        const actualValueArrayWithYear = new Array(reportlength).fill(0).map((value, index) => {
            const actualValue = actualValues.find((actual) => actual.year === (new Date().getFullYear() - index));
            const findBaseValue = baseValueArrayWithYear.find((base) => base.x === (new Date().getFullYear() - index).toString());
            return {
                x: (new Date().getFullYear() - index).toString(),
                y: actualValue ? actualValue.value : (findBaseValue ? findBaseValue.y : 0)
            };
        });

        return [
            {
                id: BASE_VALUE,
                color: BASE_VALUE_COLOR,
                data: baseValueArrayWithYear
            },
            {
                id: CURRENT_VALUE,
                color: CURRENT_VALUE_COLOR,
                data: actualValueArrayWithYear
            },
            {
                id: TARGET_VALUE,
                color: TARGET_VALUE_COLOR,
                data: targetValueArrayWithYear
            }
        ];
    }
}

export default ChartUtils;
