/* eslint-disable  no-bitwise */
import Gradient from 'javascript-color-gradient';
import { format } from 'd3-format';

import { CHART_COLOR_MAP, AVAILABLE_COLORS } from './constants';
import {
  ALL_PROGRAMS, DST_PROGRAM, PROGRAM_MAPPING, SRC_PROGRAM
} from '../../admin/ndd/constants/Constants';

export function hashCode(str) { // java String#hashCode
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    hash = str.charCodeAt(i) + ((hash << 5) - hash);
  }
  return hash;
}

export function intToRGB(i) {
  const c = (i & 0x00FFFFFF)
    .toString(16)
    .toUpperCase();

  return '00000'.substring(0, 6 - c.length) + c;
}

export function addAlpha(color, opacity) {
  const _opacity = Math.round(Math.min(Math.max(opacity || 1, 0), 1) * 255);
  return color + _opacity.toString(16)
    .toUpperCase();
}

export function getCustomColor(item, program) {
  let colorMap;
  let color;
  colorMap = CHART_COLOR_MAP.get(program);
  if (!colorMap) {
    colorMap = new Map();
    CHART_COLOR_MAP.set(program, colorMap);
  }
  color = colorMap.get(item.code.trim());
  if (!color) {
    let CHART_COLORS = AVAILABLE_COLORS.get(program);
    if (!CHART_COLORS) {
      CHART_COLORS = ['#00ff00', '#aa00bb']; // TODO: define colors for lvl2.
    }
    color = CHART_COLORS.shift();
    colorMap.set(item.code, color);
  }
  return color;
}

export function getGradient(colorFrom, colorTwo) {
  const colorGradient = new Gradient();

  colorGradient.setGradient(colorFrom, colorTwo);
  return colorGradient.getArray();
}

export function extractPrograms(mapping, noIndirectMapping) {
  const ret = { direct: undefined, indirect1: undefined, indirect2: undefined };
  if (mapping && mapping[PROGRAM_MAPPING] && mapping[PROGRAM_MAPPING].length > 0) {
    ret.direct = mapping[ALL_PROGRAMS].find(i => i.children
      .find(j => j.children && j.children
        .find(k => k.children && k.children
          .find(l => l.id === mapping[PROGRAM_MAPPING][0][SRC_PROGRAM]))));
    ret.indirect1 = mapping[ALL_PROGRAMS].find(i => i.children
      .find(j => j.children && j.children
        .find(k => k.children && k.children
          .find(l => l.id === mapping[PROGRAM_MAPPING][0][DST_PROGRAM]))));
  }
  if (noIndirectMapping && noIndirectMapping[PROGRAM_MAPPING] && noIndirectMapping[PROGRAM_MAPPING].length > 0) {
    ret.indirect2 = noIndirectMapping[ALL_PROGRAMS].find(i => i.children
      .find(j => j.children && j.children
        .find(k => k.children && k.children
          .find(l => l.id === noIndirectMapping[PROGRAM_MAPPING][0][DST_PROGRAM]))));
  }
  return ret;
}

export function formatKMB(translations, precision, decimalSeparator) {
  const formatSI = format(`.${precision || 3}s`);
  decimalSeparator = decimalSeparator || '.';
  return (value) => formatSI(value)
    .replace('k', translations['amp.dashboard:chart-thousand'])
    .replace('M', translations['amp.dashboard:chart-million'])
    .replace('G', translations['amp.dashboard:chart-billion']) // now just need to convert G Gigia -> B Billion
    .replace('T', translations['amp.dashboard:chart-trillion'])
    .replace('P', translations['amp.dashboard:chart-peta'])
    .replace('E', translations['amp.dashboard:chart-exa'])
    .replace('.', decimalSeparator);
}
