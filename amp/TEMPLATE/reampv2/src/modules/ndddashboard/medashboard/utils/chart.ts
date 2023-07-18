import { ActualValue, YearValues } from "../types";
import {printChart} from "../../../sscdashboard/utils/PrintUtils";

interface GaugeUtils {
    baseValue: number,
    actualValue: number,
    targetValue: number
}

class ChartUtils {
    public static generateTickValues = (min: number, max: number,  step: number) => {
        const tickValues = [];
        for (let i = min; i <= max; i += step) {
            tickValues.push(i);
        }
        return tickValues;
    }

    public static generateGaugeValue = (data : GaugeUtils) => {
        const { baseValue, actualValue, targetValue } = data;
        if (!targetValue || !baseValue || !actualValue) {
            return 0;
        }

        const actual = actualValue ? actualValue : 0;
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
        const aggregateValues = data.reduce((acc, curr) => {
            acc.actualValue += ChartUtils.getActualValueForCurrentYear(curr.actualValues);
            acc.targetValue += curr.targetValue;
            acc.baseValue += curr.baseValue;
            return acc;
        }, { actualValue: 0, targetValue: 0, baseValue: 0 });

        return aggregateValues;
    }

    public static downloadChartImage = (title: string, containerId: string) => {
        printChart(title, containerId, [], 'png', false, 'print-simple-dummy-container', false);
    }
}

export default ChartUtils;
