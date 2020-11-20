import React, { Component } from 'react';
import EllipsisText from "react-ellipsis-text";

export default class CustomLegend extends Component {
	constructor(props) {
		super(props);
		this.formatter2 = new Intl.NumberFormat('en-US', {
			style: 'currency',
			currency: 'USD',
			// These options are needed to round to whole numbers if that's what you want.
			//minimumFractionDigits: 0,
			//maximumFractionDigits: 0,
		});
	}

	render() {
		const {data, colorMap, shouldSplitBig} = this.props;
		const formatter = this.formatter2;
		return (
			<div className="custom-legend">
				<div className="custom-legend-inner">
					<ul className={data.length > 3 && shouldSplitBig ? 'two-rows' : ''}>
						{data.map(d => {
							return (
								<li key={colorMap.get(d.code)}>
									<div className="row">
										<div className="col-md-1 col-xs-1">
						<span
							className="symbol"
							style={{
								border: `2px solid ${colorMap.get(d.code)}`,
								backgroundColor: `${colorMap.get(d.code)}`
							}}/>
										</div>
										<div className="col-md-9 col-xs-9 label">
											<EllipsisText
												text={d.simpleLabel}
												length={80}
												tail=""/>
											{d.percentage &&
											<span
												className="label percentage">
										  {`${d.percentage}%`}
											</span>
											}
											{d.amount &&
											<span
												className="label amount">
										  {formatter ? formatter.format(d.amount) : d.amount}
											</span>
											}
										</div>
									</div>
								</li>
							);
						})}
					</ul>
				</div>
			</div>
		);
	}
}
