export const TRN_PREFIX = 'amp.ndd.dashboard:';
export const DIRECT_INDIRECT_REPORT = '/rest/ndd/direct-indirect-report';
export const DIRECT_PROGRAM = 'directProgram';
export const INDIRECT_PROGRAMS = 'indirectPrograms';
export const PROGRAMLVL1 = 'programLvl1';
export const PROGRAMLVL2 = 'programLvl2';
export const PROGRAMLVL3 = 'programLvl3';
export const AMOUNT = 'amount';
export const CODE = 'code';
export const DIRECT = 'DIRECT';
export const INDIRECT = 'INDIRECT';
export const TRANSITIONS = 100;
export const SETTINGS_EP = '/rest/settings-definitions/dashboards';
export const FUNDING_TYPE = 'funding-type';

const PROGRAMLVL1_COLOR = ['#1f5177',
  '#a42727',
  '#b6861e',
  '#598118',
  '#551881'];

const INDIRECT_PROGRAM_COLOR = ['#91cb34',
  '#9053bc',
  '#2480c6',
  '#ec4444',
  '#ffbd2f'];

export const AVAILABLE_COLORS = new Map([
  [PROGRAMLVL1, PROGRAMLVL1_COLOR],
  [INDIRECT_PROGRAMS, INDIRECT_PROGRAM_COLOR]
]);

export const CHART_COLOR_MAP = new Map([
  [PROGRAMLVL1, new Map()],
  [INDIRECT_PROGRAMS, new Map()]
]);
