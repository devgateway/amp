import React, {Component} from 'react';
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
                <label htmlFor="af">Afghanistan</label>
              </li>
              <li>
                <input type="checkbox" name="al" id="al"/>
                <label htmlFor="al">Albania</label>
              </li>
              <li>
                <input type="checkbox" name="dz" id="dz"/>
                <label htmlFor="dz">Algeria</label>
              </li>
              <li>
                <input type="checkbox" name="ao" id="ao"/>
                <label htmlFor="ao">Angola</label>
              </li>
              <li>
                <input type="checkbox" name="ar" id="ar"/>
                <label htmlFor="ar">Argentina</label>
              </li>
              <li>
                <input type="checkbox" name="am" id="am"/>
                <label htmlFor="am">Armenia</label>
              </li>
              <li>
                <input type="checkbox" name="aw" id="aw"/>
                <label htmlFor="aw">Aruba</label>
              </li>
              <li>
                <input type="checkbox" name="au" id="au"/>
                <label htmlFor="au">Australia</label>
              </li>
              <li>
                <input type="checkbox" name="at" id="at"/>
                <label htmlFor="at">Austria</label>
              </li>
              <li>
                <input type="checkbox" name="az" id="az"/>
                <label htmlFor="az">Azerbaijan</label>
              </li>
              <li>
                <input type="checkbox" name="bs" id="bs"/>
                <label htmlFor="bs">Bahamas</label>
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
