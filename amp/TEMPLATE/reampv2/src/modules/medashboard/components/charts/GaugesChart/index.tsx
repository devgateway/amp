import React, { ReactElement, useEffect, useRef, useState } from "react";
import { Pie, CommonPieProps } from "@nivo/pie";
// make sure parent container have a defined height when using responsive component,
// otherwise height will be 0 and no chart will be rendered.
// website examples showcase many properties, you'll often use just a few of them.

interface CenteredMetricProps {
    centerX: number;
    centerY: number;
    value: string | number;
    innerColor?: string;
}

export interface GaugeProps {
    data: Array<{ id: string, value: number }>;
    height: number;
    width: number;
    innerValue: number | string;
    innerColor: string;
    tooltip?: any;
    suffix?: string;
}

interface Layers {
    centerX: number,
    centerY: number
}

interface Data {
    id: string;
    value: number;
}

interface ItemParam {
    data: Data;
    index: number;
    value: number;
    startAngle: number;
    endAngle: number;
    padAngle: number;
    angle: number;
    angleDeg: number;
    color: string;
}

interface PieProps extends CommonPieProps {
    layers: Array<string | ((layers: Layers) => ReactElement<any, any> | null)>;
}

const DefaultPie: React.FC<Partial<PieProps>> = (props) => {
    const { layers } = props;
    return (
        //@ts-ignore
        <Pie
            layers={layers}
            {...props}
        />
    )
}

const colors = new Map(
    [
        ["EP", "#ffffff"],
        ["1", "#FF3833"],
        ["P", "#ffffff"],
        ["P_S", "#FF7E37"],
        ["F", "#ffffff"],
        ["2", "#FFFC61"],
        ["G", "#ffffff"],
        ["G_S", "#CCF000"],
        ["E", "#ffffff"],
        ["E_S", "#75DD00"]
    ]);

const colorsData = [
    { x: "red", color: "#ee6055", min: 1, max: 25 },
    { x: "orange", color: "#ff9b85", min: 26, max: 50 },
    { x: "yellow", color: "#ffd97d", min: 51, max: 75 },
    { x: "green", color: "#aaf683", min: 76, max: 100 }
];



const getColor = (item: Data) => {
    console.log(item);
    const foundColour = colors.get(item.id);

    if (foundColour) {
        return foundColour;
    }
};

const borderColors = new Map(
    [
        ["1", "#FF3833"],
        ["EP_S", "#FF3833"],
        ["P", "#FF7E37"],
        ["P_S", "#FF7E37"],
        ["2", "#FFFC61"],
        ["F_S", "#FFFC61"],
        ["G", "#CCF000"],
        ["G_S", "#CCF000"],
        ["E", "#75DD00"],
        ["E_S", "#75DD00"]
    ]);

const getBorderColor = (item: ItemParam) => {
    const foundColour = borderColors.get(item.data.id);

    if (foundColour) {
        return foundColour;
    }
}

const CenteredMetric: React.FC<CenteredMetricProps> = (props) => {
    const { centerX, centerY, value } = props;
    const ref = useRef(null);
    return (
      <text
        x={centerX - 50}
        y={centerY}
        style={{ fontSize: "40pt" }}
        ref={ref}
      >
        {value}%
      </text>
    );
  };
const Gauge: React.FC<GaugeProps> = (props) => {
    const { data, height, width, innerValue, innerColor, tooltip, suffix } = props;

    const [arrayWithWhite, setArrayWithWhite] = useState<any>({});

    let value = innerValue;
    if (suffix) {
        value += suffix;
    }

    useEffect(() => {
        const arrayWithColor = Array.from({ length : 100 }, (_ , i) => {
            if (i > Number(innerValue)) {
                return {
                    x: 'grey',
                    y: 0.9,
                    color: '#cdcdcd'
                }
            }

            let curr = i + 1;

            const colorData = colorsData.find((data) => curr >= data.min && curr <= data.max);
            if (colorData) {
                return {
                    x: colorData.x,
                    y: 0.9,
                    color: colorData.color
                }
            }
        })


        arrayWithColor.forEach((item, i) => {
            setArrayWithWhite((prevState: any) => ({
                ...prevState,
                item
            }));

            if (i < arrayWithColor.length - 1) {
                const newItem = { x: 'white', y: 0.1, color: '#fffff' };

                setArrayWithWhite((prevState: any) => ({
                    ...prevState,
                    item: newItem
                }))
            }
        });
    }, [])

    console.log(arrayWithWhite);


    const finalData = [
        {
            id: "white",
            value: innerValue,
            data: arrayWithWhite
        }
    ]


    return (<div style={{ height }}>
        <DefaultPie
            layers={['arcs', 'arcLabels', 'arcLinkLabels', 'legends',
                ({ centerX, centerY }: { centerX: number, centerY: number }) =>
                    CenteredMetric({  centerX, centerY, value: innerValue, innerColor })]}
                    // @ts-ignore
            width={width}
            data={data}
            // @ts-ignore
            colors={(item) => getColor(item)}
            height={height}
            startAngle={-90}
            endAngle={90}
            innerRadius={0.8}
            padAngle={2}
            cornerRadius={1}
            labelSkipWidth={18}
            slicesLabelsTextColor="#FFFFFF"
            enableRadialLabels={false}
            enableArcLinkLabels={false}
            enableArcLabels={false}
            slicesLabelsSkipAngle={10}
            animate={true}
            motionStiffness={90}
            motionDamping={15}
            tooltip={tooltip}
            borderWidth={1.25}
            // @ts-ignore
            borderColor={item => getBorderColor(item)}
            fit={true}
        />
    </div>);
}

export default Gauge;
