import {ActualValue, DefaultTranslations, LineChartData, SectorReport, YearValues} from "../types";
import {printChart} from "../../../sscdashboard/utils/PrintUtils";
import {
    BASE_VALUE,
    BASE_VALUE_COLOR, CURRENT_VALUE, CURRENT_VALUE_COLOR,
    DEFAULT_REPORTING_PERIOD,
    TARGET_VALUE,
    TARGET_VALUE_COLOR,
    SECTOR_COLOR
} from "../../utils/constants";
import React from "react";
import {DataType} from "../components/charts/BarChart";
import _ from 'lodash';
import {DateUtil} from "../../../admin/indicator_manager/utils/dateFn";
import dayjs from "dayjs";

interface GaugeUtils {
    baseValue: number,
    actualValue: number,
    targetValue: number
}

interface ValuesDataset {
    data: any,
    translations: DefaultTranslations
}

interface SectorDataset {
    data: SectorReport,
    translations: DefaultTranslations
}

class ChartUtils {
    public static generateTickValues = (min: number, max: number,  step: number) => {
        const tickValues: number[] = [];
        for (let i = min; i <= max; i += step) {
            tickValues.push(i);
        }
        return tickValues;
    }

    public static getMaxAndMinValueForAxis = (data: GaugeUtils) => {
        const { baseValue, actualValue, targetValue } = data;
        const values = [baseValue, actualValue, targetValue];
        return {
            min: Math.min(...values),
            max: Math.max(...values)
        }
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
            if (!curr.actualValues || curr.actualValues.length === 0) {
                acc.actualValue += curr.baseValue;
            }
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
        const {actualValues, targetValue, baseValue} = data;
        const reportlength = actualValues.length >= 5 ? actualValues.length : DEFAULT_REPORTING_PERIOD;

        const baseValueArrayWithYear = new Array(reportlength).fill(baseValue).map((value, index) => {
            return {
                x: (new Date().getFullYear() - index).toString(),
                y: value as number
            };
        }).sort((a, b) => parseInt(a.x) - parseInt(b.x));

        const targetValueArrayWithYear = new Array(reportlength).fill(targetValue).map((value, index) => {
            return {
                x: (new Date().getFullYear() - index).toString(),
                y: value as number
            };
        }).sort((a, b) => parseInt(a.x) - parseInt(b.x));

        const actualValueArrayWithYear = new Array(reportlength).fill(0).map((value, index) => {
            const actualValue = actualValues.find((actual) => actual.year === (new Date().getFullYear() - index));
            const findBaseValue = baseValueArrayWithYear.find((base) => base.x === (new Date().getFullYear() - index).toString());
            return {
                x: (new Date().getFullYear() - index).toString(),
                y: actualValue ? actualValue.value : (findBaseValue ? findBaseValue.y : 0)
            };
        }).sort((a, b) => parseInt(a.x) - parseInt(b.x));

        return [
            {
                id: BASE_VALUE,
                color: BASE_VALUE_COLOR,
                data: baseValueArrayWithYear
            },
            {
                id: CURRENT_VALUE,
                color: CURRENT_VALUE_COLOR,
                data: data.actualValues.length > 0 ? actualValueArrayWithYear : baseValueArrayWithYear
            },
            {
                id: TARGET_VALUE,
                color: TARGET_VALUE_COLOR,
                data: targetValueArrayWithYear
            }
        ];
    }

    public static generateValuesDataset = (props: ValuesDataset) => {
        const {data, translations } = props;

        const finalDataSet: DataType [] = [];

        if (data) {
            let aggregateValue = {
                baseValue: 0,
                targetValue: 0,
                actualValue: 0
            };

            if (Array.isArray(data)) {
                aggregateValue = ChartUtils.computeAggregateValues(data);
            } else {
                aggregateValue = ChartUtils.computeAggregateValues([data]);
            }

            const year = new Date().getFullYear();
            if (aggregateValue.baseValue) {
                const baseData = {
                    id: translations['amp.ndd.dashboard:me-baseline'],
                    value: aggregateValue.baseValue,
                    label: `${translations['amp.ndd.dashboard:me-baseline']} ${year}`,
                    color: BASE_VALUE_COLOR
                }

                finalDataSet.push(baseData);
            }

            if (aggregateValue.actualValue) {
                const actualData = {
                    id: translations['amp.ndd.dashboard:me-current'],
                    value: aggregateValue.actualValue,
                    label: `${translations['amp.ndd.dashboard:me-current']} ${year}`,
                    color: CURRENT_VALUE_COLOR
                };

                finalDataSet.push(actualData);
            }

            if (aggregateValue.targetValue) {
                const targetData = {
                    id: translations['amp.ndd.dashboard:me-target'],
                    value: aggregateValue.targetValue,
                    label: `${translations['amp.ndd.dashboard:me-target']} ${year}`,
                    color: TARGET_VALUE_COLOR
                };

                finalDataSet.push(targetData);
            }

        }

        return finalDataSet;
    }

    public static generateSectorsReport = (props: SectorDataset) => {
        const {data, translations} = props;

        const processedReport: DataType [] = [];

        if (data) {
            data.values.map((sector, index) => {
                const sectorData = {
                    id: sector.name,
                    value: sector.amount,
                    label: sector.name,
                    color: SECTOR_COLOR[index]
                };

                processedReport.push(sectorData);
            })
        }

        return processedReport;

    };

    public static formatNumber = (value: number) => {
        return value.toLocaleString('en-US', {maximumFractionDigits: 4});
    }

    public static getYearOptions = (startDate: string, endDate: string, dateFormat = 'dd/MM/yyyy', translations: DefaultTranslations) => {
        const options: { label: string, value: number}[] = [];

        if (startDate && endDate) {
            const startYear = dayjs(startDate).year()
            const endYear = new Date(endDate).getFullYear();

            const yearDiff = endYear - startYear;
            const yearRemainder = yearDiff % 5;
            const yearOptions = yearDiff - yearRemainder;

            for (let i = 5; i <= yearOptions; i += 5) {
                options.push({
                    label: `${i} ${translations['amp.ndd.dashboard:years']}`,
                    value: i
                });
            }
        }

        return options;
    };
}

export default ChartUtils;
