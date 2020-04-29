import React, {Component} from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import './filters.css';

class FilterCountries extends Component {
    render() {
        return (
        <div className="horizontal-filter dropdown">

          <button className="btn btn-primary" type="button" data-toggle="collapse" data-target="#countries">
          Pays <span className="select-count">(0/15)</span>
          </button>
          <div className="filter-list collapse" id="countries">
            <div className="well">

            <div className="well-inner">
            <ul>
              <li>
                <input type="checkbox" name="af" id="af"/>
                <label for="af">Afghanistan</label>
              </li>
              <li>
                <input type="checkbox" name="al" id="al"/>
                <label for="al">Albania</label>
              </li>
              <li>
                <input type="checkbox" name="dz" id="dz"/>
                <label for="dz">Algeria</label>
              </li>
              <li>
                <input type="checkbox" name="ao" id="ao"/>
                <label for="ao">Angola</label>
              </li>
              <li>
                <input type="checkbox" name="ar" id="ar"/>
                <label for="ar">Argentina</label>
              </li>
              <li>
                <input type="checkbox" name="am" id="am"/>
                <label for="am">Armenia</label>
              </li>
              <li>
                <input type="checkbox" name="aw" id="aw"/>
                <label for="aw">Aruba</label>
              </li>
              <li>
                <input type="checkbox" name="au" id="au"/>
                <label for="au">Australia</label>
              </li>
              <li>
                <input type="checkbox" name="at" id="at"/>
                <label for="at">Austria</label>
              </li>
              <li>
                <input type="checkbox" name="az" id="az"/>
                <label for="az">Azerbaijan</label>
              </li>
              <li>
                <input type="checkbox" name="bs" id="bs"/>
                <label for="bs">Bahamas</label>
              </li>

            </ul>
            </div>

            </div>
          </div>

        </div>
        );
    }
}

export default FilterCountries;
