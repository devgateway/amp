import React, {Component} from 'react';
import './filters.css';

class FilterSectorsHorizontal extends Component {
    render() {
        return (
        <div className="horizontal-filter dropdown">

          <button className="btn btn-primary" type="button" data-toggle="collapse" data-target="#sectors">
          Secteur <span className="select-count">(0/12)</span>
          </button>
          <div className="filter-list collapse" id="sectors">
            <div className="well">

            <div className="well-inner">
            <ul>
              <li>
                <input type="checkbox" name="s1" id="s1"/>
                <label htmlFor="s1">Sector 1</label>
              </li>
              <li>
                <input type="checkbox" name="s2" id="s2"/>
                <label htmlFor="s2">Sector 2</label>
              </li>
              <li>
                <input type="checkbox" name="s3" id="s3"/>
                <label htmlFor="s3">Sector 3</label>
              </li>
              <li>
                <input type="checkbox" name="s4" id="s4"/>
                <label htmlFor="s4">Sector 4</label>
              </li>
              <li>
                <input type="checkbox" name="s5" id="s5"/>
                <label htmlFor="s5">Sector 5</label>
              </li>
              <li>
                <input type="checkbox" name="s6" id="s6"/>
                <label htmlFor="s6">Sector 6</label>
              </li>
              <li>
                <input type="checkbox" name="s7" id="s7"/>
                <label htmlFor="s7">Sector 7</label>
              </li>
              <li>
                <input type="checkbox" name="s8" id="s8"/>
                <label htmlFor="s8">Sector 8</label>
              </li>
              <li>
                <input type="checkbox" name="s9" id="s9"/>
                <label htmlFor="s9">Sector 9</label>
              </li>
              <li>
                <input type="checkbox" name="s10" id="s10"/>
                <label htmlFor="s10">Sector 10</label>
              </li>
              <li>
                <input type="checkbox" name="s11" id="s11"/>
                <label htmlFor="s11">Sector 11</label>
              </li>
              <li>
                <input type="checkbox" name="s12" id="s12"/>
                <label htmlFor="s12">Sector 12</label>
              </li>
            </ul>
            </div>

            </div>
          </div>

        </div>
        );
    }
}

export default FilterSectorsHorizontal;
