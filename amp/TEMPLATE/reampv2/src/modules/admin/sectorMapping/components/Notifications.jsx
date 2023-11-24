import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import {SectorMappingContext} from './Startup';
import './css/style.css';

class Notifications extends Component {
  render() {
    const { messages } = this.props;
    if (messages && messages.length > 0) {
      return (
        <div>
          <ul className="notifications">
            {messages.map(message => {
              const className = message.isError ? 'error-color' : 'success-color';
              return <li key={Math.random()} className={className}>{message.text}</li>;
            })}
          </ul>
        </div>
      );
    }
    return null;
  }
}

Notifications.contextType = SectorMappingContext;

Notifications.propTypes = {
  messages: PropTypes.array.isRequired
};

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(Notifications);
