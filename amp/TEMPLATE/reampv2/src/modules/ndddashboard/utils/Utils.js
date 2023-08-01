/* eslint-disable  no-bitwise */
// eslint-disable-next-line no-unused-vars
import React from 'react';
import Gradient from 'javascript-color-gradient';
import {format} from 'd3-format';

import {AVAILABLE_COLORS, CHART_COLOR_MAP, MAX_GRADIENTS, SELECTED_COLORS} from './constants';
import {ALL_PROGRAMS, DST_PROGRAM, PROGRAM_MAPPING, SRC_PROGRAM} from '../../admin/ndd/constants/Constants';

export function hashCode(str) { // java String#hashCode
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    hash = str.charCodeAt(i) + ((hash << 5) - hash);
  }
  return hash;
}

export function removeFilter(filters, selectedDirectProgram) {
  if (filters && filters.filters && filters.filters[selectedDirectProgram.filterColumnName]) {
    filters.filters[selectedDirectProgram.filterColumnName]
      .splice(filters.filters[selectedDirectProgram.filterColumnName]
        .findIndex(i => i === selectedDirectProgram.objectId), 1);
    if (filters.filters[selectedDirectProgram.filterColumnName].length === 0) {
      filters.filters[selectedDirectProgram.filterColumnName] = null;
    }
  }
  return filters;
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
    let CHART_COLORS = SELECTED_COLORS.get(program);
    if (!CHART_COLORS) {
      if (AVAILABLE_COLORS.length > 0) {
        CHART_COLORS = AVAILABLE_COLORS.shift();
        SELECTED_COLORS.set(program, CHART_COLORS);
      } else {
        CHART_COLORS = ['#00ff00', '#aa00bb']; // TODO: This shouldn't be needed but leaving just in case
      }
    }
    color = CHART_COLORS.shift();
    colorMap.set(item.code, color);
  }
  return color;
}

export function getGradient(colorFrom, colorTwo, maxGradient = MAX_GRADIENTS) {
  const colorGradient = new Gradient('', maxGradient);

  colorGradient.setGradient(colorFrom, colorTwo);
  return colorGradient.getArray();
}

export function extractPrograms(mapping, noIndirectMapping) {
  const ret = { direct: undefined, indirect1: undefined, indirect2: undefined };
  if (mapping && mapping[PROGRAM_MAPPING] && mapping[PROGRAM_MAPPING].length > 0) {
    ret.direct = mapping[ALL_PROGRAMS].find(i => i.id === mapping[SRC_PROGRAM].id);
    ret.indirect1 = mapping[ALL_PROGRAMS].find(i => i.id === mapping[DST_PROGRAM].id);
  }
  if (noIndirectMapping && noIndirectMapping[PROGRAM_MAPPING] && noIndirectMapping[PROGRAM_MAPPING].length > 0) {
    ret.indirect2 = noIndirectMapping[ALL_PROGRAMS].find(i => i.id === noIndirectMapping[DST_PROGRAM].id);
  }
  return ret;
}

export function formatKMB(translations, precision, decimalSeparator, summary, lang) {
  const formatSI = format(`.${precision || 3}s`);
  decimalSeparator = decimalSeparator || '.';
  if (summary) {
    return (value) => formatSI(value)
      .replace('k', getSuffixForLang('k', lang))
      .replace('G', getSuffixForLang('G', lang))
      .replace('.', decimalSeparator);
  }
  return (value) => formatSI(value)
    .replace('k', ` ${translations['amp.dashboard:chart-thousand']}`)
    .replace('M', ` ${translations['amp.dashboard:chart-million']}`)
    .replace('G', ` ${translations['amp.dashboard:chart-billion']}`) // now just need to convert G Gigia -> B Billion
    .replace('T', ` ${translations['amp.dashboard:chart-trillion']}`)
    .replace('P', ` ${translations['amp.dashboard:chart-peta']}`)
    .replace('E', ` ${translations['amp.dashboard:chart-exa']}`)
    .replace('.', decimalSeparator);
}

function getSuffixForLang(prefix, lang) {
  // eslint-disable-next-line default-case
  switch (lang) {
    case 'en':
      return prefix;
    case 'fr':
      // eslint-disable-next-line default-case
      switch (prefix) {
        case 'k':
          return 'm';
        case 'G':
          return 'MM';
      }
      break;
    case 'sp':
      // eslint-disable-next-line default-case
      switch (prefix) {
        case 'G':
          return 'B';
      }
      break;
  }
  return prefix;
}

export function formatNumber(currency, translations, value, precision, decimalSeparator, groupSeparator, numberDivider,
  numberDividerDescriptionKey) {
  const formatString = `${decimalSeparator}.${precision}f`;
  const dividedValue = (numberDivider && numberDividerDescriptionKey) ? value / numberDivider : value;
  // eslint-disable-next-line max-len
  const txtVal = <b>{format(formatString)(dividedValue).replace(/[,]+/g, groupSeparator).replace(/[.]+/g, decimalSeparator)}</b>;
  return (
    <>
      {txtVal}
      {' '}
      {currency}
      {numberDivider && numberDividerDescriptionKey
        ? ` (${translations[`amp.ndd.dashboard:${numberDividerDescriptionKey}`]})`
        : null}
    </>
  );
}

export function formatNumberWithSettings(currency, translations, settings, value, useUnits) {
  if (useUnits) {
    return formatNumber(currency, translations, value, settings.precision, settings.decimalSeparator,
      settings.groupSeparator, settings.numberDivider, settings.numberDividerDescriptionKey);
  }
  return formatNumber(currency, translations, value, settings.precision, settings.decimalSeparator,
    settings.groupSeparator);
}

// TODO: Unify with formatNumber();
export function formatOnlyNumber(settings, value) {
  const formatString = `${settings.decimalSeparator}.${settings.precision}f`;
  const dividedValue = (settings.numberDivider && settings.numberDividerDescriptionKey)
    ? value / settings.numberDivider
    : value;
  return format(formatString)(dividedValue)
    .replace(/[,]+/g, settings.groupSeparator)
    .replace('.', settings.decimalSeparator);
}

export function getAmountsInWord(translations, settings) {
  if (settings.numberDivider > 1) {
    return translations[`amp.ndd.dashboard:${settings.numberDividerDescriptionKey}-amounts`];
  }
  return '';
}
