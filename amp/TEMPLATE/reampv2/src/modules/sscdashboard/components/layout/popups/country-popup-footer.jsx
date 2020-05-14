import React, {Component} from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import './popups.css';

class CountryPopupFooter extends Component {
    render() {
        return (
            <div className="popup-footer">
              <div className="col-md-1">
                <div className="label">Projets</div>
              </div>
              <div className="col-md-9 project-list">
                <ul>
                  <li>Lorem ipsum dolor sit amet</li>
                  <li>A scelerisque purus semper</li>
                  <li>Ultrices neque ornare aenean</li>
                  <li>Ornare quam viverra</li>
                  <li>Ut tellus elementum sagittis</li>
                  <li>Cursus vitae congue mauris</li>
                  <li>Venenatis a condimentum </li>
                  <li>Pulvinar etiam</li>
                  <li>Lorem ipsum dolor sit amet</li>
                  <li>A scelerisque purus semper</li>
                  <li>Ultrices neque ornare aenean</li>
                  <li>Ornare quam viverra</li>
                  <li>Ut tellus elementum sagittis</li>
                  <li>Cursus vitae congue mauris</li>
                  <li>Venenatis a condimentum </li>
                  <li>Pulvinar etiam</li>
                </ul>
              </div>
              <div className="col-md-2">
                <a className="more" href="#">Load more</a>
              </div>
            </div>
        );
    }
}

export default CountryPopupFooter;
