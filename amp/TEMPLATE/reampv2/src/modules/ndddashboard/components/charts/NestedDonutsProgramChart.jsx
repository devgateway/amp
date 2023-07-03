import React, { useCallback, useState } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { CSSTransitionGroup } from 'react-transition-group';

// Dont use react-plotly directly: https://github.com/plotly/react-plotly.js/issues/135#issuecomment-501398125
import Plotly from 'plotly.js';
import createPlotlyComponent from 'react-plotly.js/factory';
import {
  DIRECT_PROGRAM, INDIRECT_PROGRAMS, PROGRAMLVL1, AMOUNT, CODE, DIRECT, INDIRECT,
  TRANSITIONS, PROGRAMLVL2, TRN_PREFIX, CURRENCY_CODE
} from '../../utils/constants';
import {
  addAlpha, formatNumberWithSettings, getCustomColor
} from '../../utils/Utils';
import ToolTip from '../tooltips/ToolTip';
// eslint-disable-next-line no-unused-vars
import styles from '../styles.css';

const Plot = createPlotlyComponent(Plotly);

const NestedDonutsProgramChart = (props) => {
  const {
    data,
    handleOuterChartClick,
    selectedDirectProgram,
    translations,
    settings,
    globalSettings,
    selectedPrograms
  } = props;

  const [state, setState] = useState({
    showLegend: false, legendTop: 0, legendLeft: 0, tooltipData: null
  });

  // eslint-disable-next-line class-methods-use-this
  const isSubProgram = (i, selectedDirectProgram) => {
    if (selectedDirectProgram) {
      return i[DIRECT_PROGRAM][PROGRAMLVL1][CODE] === selectedDirectProgram[CODE];
    }
    return false;
  }


  /**
* If at least one section of the pie data is too small to be drawn then we make it bigger at the expense
* of the biggest section.
*/
  // eslint-disable-next-line class-methods-use-this
  const normalizePieData = (data) => {
    const MINIMUM_PERCENTAGE = 2;
    if (data.find(i => i.percentageInTotal < MINIMUM_PERCENTAGE)) {
      const biggest = data.sort((i, j) => j.percentageInTotal - i.percentageInTotal)[0];
      let minusPercentage = 0;
      data.filter(i => i.percentageInTotal < MINIMUM_PERCENTAGE).forEach(cat => {
        const localMinusPercentage = MINIMUM_PERCENTAGE - cat.percentageInTotal;
        minusPercentage += localMinusPercentage;
        cat.normalizedPercentage = MINIMUM_PERCENTAGE;
      });
      biggest.normalizedPercentage = biggest.percentageInTotal - minusPercentage;
    }
    return data;
  }

  const memoizedNormalizePieData = useCallback(normalizePieData, []);

  /**
 * Create an array with each unique Direct Program Level 1 adding the amounts when it has multiple children.
 * @returns {[]}
 */
  // eslint-disable-next-line react/sort-comp
  const extractOuterData = (calculateLvl2) => {
    const ret = [];

    if (data && data.length > 0) {
      const totalAmount = data.reduce((acc, cur) => (acc + cur[DIRECT_PROGRAM][AMOUNT]), 0);
      data.forEach(i => {
        const isSubProgramElement = (calculateLvl2 && isSubProgram(i, selectedDirectProgram));
        const directProgram = isSubProgramElement ? i[DIRECT_PROGRAM][PROGRAMLVL2] : i[DIRECT_PROGRAM][PROGRAMLVL1];
        if (directProgram && ret.filter(d => d[CODE] === directProgram[CODE]).length === 0) {
          const item = {
            [CODE]: directProgram[CODE],
            filterColumnName: directProgram.filterColumnName,
            objectId: directProgram.objectId,
            name: directProgram.name,
            [AMOUNT]: data.filter(d2 => ((calculateLvl2 && isSubProgram(i, selectedDirectProgram))
              ? d2[DIRECT_PROGRAM][PROGRAMLVL2] && d2[DIRECT_PROGRAM][PROGRAMLVL2][CODE] === directProgram[CODE]
              : d2[DIRECT_PROGRAM][PROGRAMLVL1] && d2[DIRECT_PROGRAM][PROGRAMLVL1][CODE] === directProgram[CODE]))
              .reduce((accumulator, currentValue) => (
                accumulator + currentValue[DIRECT_PROGRAM][AMOUNT]
              ), 0),
            neverFade: isSubProgram(i, selectedDirectProgram),
            parent: i[DIRECT_PROGRAM][PROGRAMLVL1].objectId
          };
          item.percentageInTotal = (item[AMOUNT] / totalAmount) * 100;
          item.normalizedPercentage = item.percentageInTotal;
          ret.push(item);
        }
      });
    }
    const normalized = memoizedNormalizePieData(ret);

    // If we are showing the lvl 2 we need to sort the results to match the place of the parent area.
    if (calculateLvl2) {
      const sorted = [];
      const parents = extractOuterData(false);
      parents.forEach(p => {
        normalized.filter(c => c.parent === p.objectId).forEach(i => sorted.push(i));
      });
      return sorted;
    }
    return normalized;
  }

  const memoizedExtractOuterData = useCallback(extractOuterData, []);

  const extractInnerData = (outerData) => {
    const ret = [];

    if (data && data.length > 0) {
      outerData.forEach(i => {
        const innerSubGroup = [];
        const subProgramsData = data.filter(j => j[DIRECT_PROGRAM][PROGRAMLVL1][CODE] === i[CODE]);
        subProgramsData.forEach(j => {
          j[INDIRECT_PROGRAMS].forEach(k => {
            if (innerSubGroup.filter(l => l[CODE] === k[PROGRAMLVL1][CODE]).length === 0) {
              innerSubGroup.push({
                [CODE]: k[PROGRAMLVL1][CODE],
                originalAmount: k[AMOUNT],
                name: k[PROGRAMLVL1].name,
                directProgramCode: j[DIRECT_PROGRAM][PROGRAMLVL1][CODE]
              });
            } else {
              const program = innerSubGroup.find(l => l[CODE] === k[PROGRAMLVL1][CODE]);
              program.originalAmount += k[AMOUNT];
            }
          });
        });
        const totalForSubGroup = innerSubGroup.reduce((acc, cur) => (acc + cur.originalAmount), 0);
        innerSubGroup.forEach(j => {
          j.percentageInSubGroup = ((j.originalAmount * 100) / totalForSubGroup);
        });
        ret.push(innerSubGroup);
      });
    }
    return ret;
  }

  // eslint-disable-next-line class-methods-use-this
  const innerDataToChartValues = (data, outerData) => {
    const ret = [];
    if (data) {
      data.forEach(i => {
        i.forEach(j => {
          // Notice we use now normalizedPercentage instead of percentageInTotal to 1) match the outer rings and
          // 2) show categories that are too small.
          const parentPercentage = outerData.find(k => k[CODE] === j.directProgramCode).normalizedPercentage;
          // Dont merge inner categories from different outer categories.
          ret.push({
            [CODE]: j[CODE],
            innerCode: j[CODE] + j.directProgramCode,
            name: j.name,
            percentage: (j.percentageInSubGroup / 100) * parentPercentage,
            amount: j.originalAmount
          });
        });
      });
    }
    return ret;
  }

  const calculateOpacity = (colors, data) => {
    let newColors = [];
    if (selectedDirectProgram) {
      colors.forEach((c, i) => {
        const index = data.findIndex(j => j[CODE] === selectedDirectProgram[CODE]);
        if (i !== index && data[i].neverFade !== true) {
          c = addAlpha(c, 0.15);
          newColors.push(c);
        } else {
          newColors.push(c);
        }
      });
    } else {
      newColors = colors;
    }
    return newColors;
  }

  const memoizedCalculateOpacity = useCallback(calculateOpacity, []);

  const onHover = (data) => {
    if (selectedDirectProgram === null
      || (selectedDirectProgram !== null && data.points[0].data.extraData[data.points[0].i].neverFade)) {
      // Disable tooltip when outer ring is selected
      setState(prevState => ({
        ...prevState,
        showLegend: true,
        legendTop: data.event.layerY,
        legendLeft: data.event.layerX + 15, /* extra pixels to avoid CSS jitter on Chrome. */
        tooltipData: data
      }));
    }
  }

  const onUnHover = () => {
    setState(prevState => ({ ...prevState, showLegend: false, tooltipData: null }));
  }

  const onClick = (event, outerData) => {
    setState(prevState => ({ ...prevState, showLegend: false }));
    handleOuterChartClick(event, outerData);
  }

  const createTooltip = () => {
    const { tooltipData } = state;
    if (tooltipData) {
      const program = tooltipData.points[0].data.extraData[tooltipData.points[0].i];
      const val = formatNumberWithSettings(settings[CURRENCY_CODE], translations, globalSettings, program.amount, true);
      const totalAmount = tooltipData.points[0].data.extraData.reduce((i, j) => (i + j.amount), 0);
      return (
        <ToolTip
          color={tooltipData.points[0].color}
          currencyCode={settings[CURRENCY_CODE]}
          formattedValue={val}
          titleLabel={`${program.name}`}
          total={totalAmount}
          value={program.amount}
          minWidth={400}
          globalSettings={globalSettings}
        />
      );
    }
    return null;
  }

  const outerData = memoizedExtractOuterData(false);

  const outerDataLvl2 = selectedDirectProgram ? memoizedExtractOuterData(true) : memoizedExtractOuterData(false);
  const innerData = extractInnerData(outerData);
  const innerDataForChart = innerDataToChartValues(innerData, outerData);
  const innerColors = memoizedCalculateOpacity(innerDataForChart.map(i => getCustomColor(i, selectedPrograms[1])),
    innerDataForChart);
  const outerColors = memoizedCalculateOpacity(outerDataLvl2
    .map(o => getCustomColor(o, o.neverFade ? `${selectedPrograms[0]}_${selectedDirectProgram.code}`
      : selectedPrograms[0])),
    outerDataLvl2);
  const transition = {
    duration: 2000,
    easing: 'cubic-in-out',
  };


  const annotations = outerData.length === 0 ? [
    {
      text: translations[`${TRN_PREFIX}no-data`],
      xref: 'paper',
      yref: 'paper',
      showarrow: false,
      font: {
        size: 20
      }
    }
  ] : [];

  const transitionRef = React.useRef(null);
  const plotRef = React.useRef(null);

  return (
    <CSSTransitionGroup
      /* key={selectedDirectProgram} */
      noderef={transitionRef}
      transitionName="solar-chart"
      transitionAppear
      transitionLeave
      transitionEnter
      transitionAppearTimeout={TRANSITIONS}
      transitionEnterTimeout={TRANSITIONS}
      transitionLeaveTimeout={TRANSITIONS}>
      <Plot
        key="solarChart"
        ref={plotRef}
        data={
          [{
            values: innerDataForChart.map(i => i.percentage),
            labels: innerDataForChart.map(i => i.innerCode),
            text: innerDataForChart.map(i => i[CODE]),
            neverFade: innerDataForChart.map(i => i.neverFade),
            extraData: innerDataForChart,
            domain: {
              x: [0.15, 0.85],
              y: [0.15, 0.85]
            },
            textposition: 'inside',
            direction: 'clockwise',
            name: INDIRECT,
            hoverinfo: 'skip',
            hole: 0.5,
            type: 'pie',
            sort: false,
            textinfo: 'text',
            marker: {
              colors: innerColors,
              line: {
                color: 'white',
                width: 3
              }
            },
            textfont: {
              color: 'white'
            }
          }, {
            values: outerDataLvl2.map(i => i.normalizedPercentage),
            labels: outerDataLvl2.map(i => i[CODE]),
            text: outerDataLvl2.map(i => i[AMOUNT]),
            neverFade: outerDataLvl2.map(i => i.neverFade),
            extraData: outerDataLvl2,
            name: DIRECT,
            hoverinfo: 'skip',
            textposition: 'inside',
            hole: innerDataForChart.length > 0 ? 0.7 : 0.36,
            type: 'pie',
            sort: false,
            direction: 'clockwise',
            textinfo: 'label',
            marker: {
              colors: outerColors,
              line: {
                color: 'white',
                width: 3
              }
            },
            textfont: {
              color: 'white'
            }
          }]
        }
        layout={{
          autosize: false,
          paper_bgcolor: 'rgba(0,0,0,0)',
          width: 400,
          height: 400,
          title: '',
          showlegend: false,
          transition,
          margin: {
            l: 55,
            r: 0,
            b: 10,
            t: 20,
            pad: 4
          },
          annotations
        }}
        config={{ displaylogo: false }}
        onClick={event => onClick(event, outerData)}
        onHover={event => onHover(event)}
        onUnhover={() => onUnHover()}
      />
      <div
        style={{
          display: (!state.showLegend ? 'none' : 'block'),
          top: state.legendTop,
          left: state.legendLeft
        }}
        className="pie-legend-wrapper">
        {createTooltip()}
      </div>

    </CSSTransitionGroup>
  );

};

NestedDonutsProgramChart.propTypes = {
  data: PropTypes.array.isRequired,
  handleOuterChartClick: PropTypes.func.isRequired,
  selectedDirectProgram: PropTypes.object,
  translations: PropTypes.object.isRequired,
  settings: PropTypes.object.isRequired,
  globalSettings: PropTypes.object.isRequired,
  selectedPrograms: PropTypes.array.isRequired
};

NestedDonutsProgramChart.defaultProps = {
  selectedDirectProgram: undefined
};

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(NestedDonutsProgramChart);
