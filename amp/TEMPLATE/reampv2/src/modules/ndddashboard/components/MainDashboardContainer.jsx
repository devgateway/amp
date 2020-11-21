import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import NestedDonutsProgramChart from './NestedDonutsProgramChart';
import callReport from '../actions/callReports';
import { Col } from 'react-bootstrap';
import CustomLegend from "../../../utils/components/CustomLegend";
import './legends/legends.css';
import {
	DIRECT_PROGRAM_COLOR,
	CHART_COLOR_MAP,
	DIRECT_PROGRAM,
	INDIRECT_PROGRAMS,
	PROGRAMLVL1
} from "../utils/constants";
import { Loading } from "../../../utils/components/Loading";
import { ColorLuminance, getCustomColor } from "../utils/Utils";

class MainDashboardContainer extends Component {
	componentDidMount() {
		this.props.callReport();
	}

	getProgramLegend() {
		const {ndd} = this.props;
		const legends = [];
		const directLegend = new Map();
		const indirectLegend = new Map();
		let directTotal = 0;
		let indirectTotal = 0;
		ndd.forEach(dp => {
				directTotal += this.generateLegend(dp.directProgram, directLegend, PROGRAMLVL1, directTotal);
				dp.indirectPrograms.forEach(idp => {
					indirectTotal += this.generateLegend(idp, indirectLegend, INDIRECT_PROGRAMS, indirectTotal);
				})

			}
		);

		legends.push({total: directTotal, legends: [...directLegend.values()]});
		legends.push({total: indirectTotal, legends: [...indirectLegend.values()]});
		return legends;
	}

	generateLegend(program, legend, programColor, total) {
		let prog = legend.get(program.programLvl1.code);
		if (!prog) {
			prog = {}
			prog.amount = 0;
			prog.code = program.programLvl1.code;
			prog.simpleLabel = program.programLvl1.name;
		}
		prog.amount += program.amount;
		total += program.amount;
		getCustomColor(prog, programColor);
		legend.set(prog.code, prog);
		return total;
	}

	render() {
		const formatter = new Intl.NumberFormat('en-US', {
			style: 'currency',
			currency: 'USD',
			// These options are needed to round to whole numbers if that's what you want.
			//minimumFractionDigits: 0,
			//maximumFractionDigits: 0,
		});
		const {error, ndd, nddLoadingPending, nddLoaded} = this.props;
		if (error) {
			// TODO proper error handling
			return (<div>ERROR</div>);
		} else {
			if (nddLoaded && !nddLoadingPending) {
				const programLegend = this.getProgramLegend();
				return (
					<div>
						<Col md={6}>
							<div>
								<div className="solar-container">
									<div><NestedDonutsProgramChart data={ndd}/></div>
								</div>
								<div className="year-chart-container">amounts by year</div>
							</div>
						</Col>
						<Col md={6}>
							<div className="legends-container">
								<div className='legend-title'>PNSD <span
									className="amount">{formatter.format(programLegend[0].total)}</span></div>
								<CustomLegend formatter={formatter} data={programLegend[0].legends}
											  colorMap={CHART_COLOR_MAP.get(PROGRAMLVL1)}/>
								<div className='legend-title'>New Deal <span
									className="amount">{formatter.format(programLegend[1].total)}</span></div>
								<CustomLegend formatter={formatter} data={programLegend[1].legends}
											  colorMap={CHART_COLOR_MAP.get(INDIRECT_PROGRAMS)}/>
							</div>
						</Col>
					</div>
				);
			}
			// TODO: proper loading component.
			return (<Loading/>);
		}
	}
}

const mapStateToProps = state => ({
	ndd: state.reportsReducer.ndd,
	error: state.reportsReducer.error,
	nddLoaded: state.reportsReducer.nddLoaded,
	nddLoadingPending: state.reportsReducer.nddLoadingPending,
});
const mapDispatchToProps = dispatch => bindActionCreators({
	callReport
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(MainDashboardContainer);

MainDashboardContainer.propTypes = {
	callReport: PropTypes.func.isRequired,
	error: PropTypes.object
};
MainDashboardContainer.defaultProps = {
	error: undefined
};