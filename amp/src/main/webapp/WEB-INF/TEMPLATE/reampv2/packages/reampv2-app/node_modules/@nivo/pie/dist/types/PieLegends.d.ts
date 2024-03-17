/// <reference types="react" />
import { CompletePieSvgProps, ComputedDatum, DatumId } from './types';
interface PieLegendsProps<RawDatum> {
    width: number;
    height: number;
    legends: CompletePieSvgProps<RawDatum>['legends'];
    data: Omit<ComputedDatum<RawDatum>, 'arc'>[];
    toggleSerie: (id: DatumId) => void;
}
declare const PieLegends: <RawDatum>({ width, height, legends, data, toggleSerie, }: PieLegendsProps<RawDatum>) => JSX.Element;
export default PieLegends;
//# sourceMappingURL=PieLegends.d.ts.map