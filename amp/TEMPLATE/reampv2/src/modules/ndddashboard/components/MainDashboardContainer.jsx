import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import NestedDonutsProgramChart from './NestedDonutsProgramChart';
import callReport from '../actions/callReports';
import { Col } from 'react-bootstrap';
import CustomLegend from "../../../utils/components/CustomLegend";
import './legends/legends.css';
import { DIRECT_PROGRAM_COLOR, NDD_COLOR_MAP } from "../utils/constants";
import { Loading } from "../../../utils/components/Loading";
import { getCustomColor } from "../utils/Utils";

class MainDashboardContainer extends Component {
	componentDidMount() {
		this.props.callReport();
	}

	getDirectProgramLegend() {
		const {ndd} = this.props;
		const directLegend = new Map();
		const inDirectLegend = new Map();
		ndd.forEach(dp => {
				let dirProg = directLegend.get(dp.directProgram.programLvl1.code);
				if (!dirProg) {
					dirProg = {}
					dirProg.amount = 0;
					dirProg.code = dp.directProgram.programLvl1.code;
					dirProg.simpleLabel = dp.directProgram.programLvl1.name;
				}
				dirProg.amount += dp.directProgram.amount;
				getCustomColor(dirProg, 'DIRECT_PROGRAM');
				directLegend.set(dirProg.code, dirProg);
			}
		);
		return [...directLegend.values()];
	}

	render() {
		const {error, ndd, nddLoadingPending, nddLoaded} = this.props;
		if (error) {
			// TODO proper error handling
			return (<div>ERROR</div>);
		} else {
			if (nddLoaded && !nddLoadingPending) {
				const directProgramLegend = this.getDirectProgramLegend();
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
							<div><CustomLegend data={directProgramLegend}
											   colorMap={NDD_COLOR_MAP.get('DIRECT_PROGRAM')}/></div>
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
