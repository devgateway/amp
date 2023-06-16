/** eslint-disable */
import React, { useLayoutEffect, useRef, useState } from "react";
import { ResponsiveRadialBar } from "@nivo/radial-bar";
import { MarginProps } from "../../../types";

export interface CenterTextProps {
  innerValue: string | number;
  center: any;
  suffix?: string;
}

export interface GaugeProps {
  height?: number;
  width?: number;
  innerValue: string | number;
  suffix?: string;
  margin?: MarginProps;
}

const colorsData = [
  { x: "red", color: "#ee6055", min: 1, max: 25 },
  { x: "dark orange", color: "#ff8c00", min: 26, max: 40 },
  { x: "light orange", color: "#ffd580", min: 41, max: 50 },
  { x: "yellow", color: "#ffff00", min: 51, max: 70 },
  { x: "green", color: "#aaf683", min: 71, max: 100 }
];

//@ts-ignore
const CenteredMetric: React.FC<CenterTextProps> = (props) => {
  const { center, innerValue, suffix } = props;
  const ref = useRef(null);

  return (
    <text
      x={center[0]}
      y={center[1]}
      textAnchor="middle"
      style={{ fontSize: "42pt" }}
      ref={ref}
    >
      {innerValue}{suffix || '%'}
    </text>
  );
};

const Gauge: React.FC<GaugeProps> = (props) => {
  const { height, innerValue, suffix, width, margin } = props;
  const [dataValues, setDataValues] = useState<Array<any>>([])

  useLayoutEffect(() => {
    const initialDataWithColours = Array.from({ length: 100 }, (_, i) => {
      if (i > Number(innerValue)) return { x: `grey${i}`, y: 0.9, color: "#cdcdcd" };
      let current = i + 1;

      const colorData = colorsData.find((data) => current >= data.min && current <= data.max);

      if (!colorData) return { x: `grey${i}`, y: 0.9, color: "#cdcdcd" };
      return { x: colorData.x + i, y: 0.9, color: colorData.color };
    })

    initialDataWithColours.forEach((item, i) => {
      setDataValues((prev) => [...prev, item])

      if (i < initialDataWithColours.length - 1) {
        setDataValues((prev) => [...prev, { x: `white${i}`, y: 0.1, color: "#ffffff" }])
      }
    })
  }, [innerValue]);

  const data = [
    {
      id: "Gauge",
      data: dataValues
    }
  ];

  return (
    <div style={{ height: height || 350, width: width || 350 }}>
      <ResponsiveRadialBar
        data={data}
        startAngle={-90}
        endAngle={90}
        colors={(item) => item.data.color}
        maxValue={100}
        layers={["bars", ({ center  }) => CenteredMetric({ center, innerValue, suffix })]}
        innerRadius={0.8}
        animate={true}
        margin={margin || {
          top: 40, right: 70, bottom: 0,
        }}
        theme={{
          tooltip: {
            container: {
              display: 'none'
            }
          }
        }}
      />
    </div>
  );
}

export default Gauge;
