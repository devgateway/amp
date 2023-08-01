import React, { Component } from 'react';
import '../popups.css';
import { getProjects } from '../../../../utils/ProjectUtils';
import { SSCTranslationContext } from '../../../StartUp';
import { DEFAULT_ELLIPSIS, DEFAULT_SCREEN_SIZE } from '../../../../utils/constants';

class CountryPopupFooter extends Component {
  constructor(props) {
    super();
    this.state = {
      ellipsisSize: this.calculateEllipsis(),
      currentWindowWith: window.innerWidth
    };
  }

  componentDidMount() {
    window.addEventListener('resize', this.onResize.bind(this));
  }

  componentWillUnmount() {
    window.removeEventListener('resize', this.onResize.bind(this));
  }

  render() {
    const { projects, activitiesDetails } = this.props;
    const { ellipsisSize } = this.state;
    const { translations } = this.context;
    return (
      <div className="popup-footer">
        <div className="col-md-1">
          <div className="label">{translations['amp.ssc.dashboard:sectors-projects']}</div>
        </div>
        <div className="col-md-11 project-list">
          <ul>
            {getProjects(projects, 'country-popup',
              activitiesDetails, ellipsisSize, translations['amp.ssc.dashboard:NA'])}
          </ul>
        </div>
      </div>
    );
  }

  onResize() {
    if (this.state.currentWindowWith !== window.innerWidth) {
      return { ellipsisSize: this.calculateEllipsis(), currentWindowWith: window.innerWidth };
    }
  }

  calculateEllipsis() {
    return Math.floor((DEFAULT_ELLIPSIS / DEFAULT_SCREEN_SIZE * window.innerWidth));
  }
}

CountryPopupFooter.contextType = SSCTranslationContext;

export default CountryPopupFooter;
