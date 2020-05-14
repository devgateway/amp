import React, {Component} from "react";
import './filters.css';

class CountrySearch extends Component {
    render() {
        return (
        <div className="col-md-2 country-search-wrapper dropdown">

          <button className="btn btn-primary" type="button" data-toggle="collapse" data-target="#countrysearch">
          Search by Country <span className="select-count">(0/15)</span>
          </button>
          <div className="country-search-content collapse" id="countrysearch">
            <div className="well">
            <div className="well-inner">

            Country list goes here

            </div>
            </div>
          </div>

        </div>
        );
    }
}

export default CountrySearch;
