import React, { Component } from 'react';

export default class SectorsHome extends Component {
  render() {
    return (
      <div className="col-md-10 col-md-offset-2 map-wrapper">
        <div>Caroussel</div>
        <div>
          <div id="accordion" role="tablist" aria-multiselectable="true">
            <div className="panel panel-default">
              <div className="panel-heading" role="tab" id="headingOne">
                <h4 className="panel-title">
                  <a
                    role="button"
data-toggle="collapse"
data-parent="#accordion"
href="#collapseOne"
                    aria-expanded="true"
aria-controls="collapseOne">
                    Collapsible Group Item #1
                  </a>
                </h4>
              </div>
              <div
                id="collapseOne"
                className="panel-collapse collapse in"
                role="tabpanel"
                aria-labelledby="headingOne">
                <div className="panel-body">
                  Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad
                  squid. 3 wolf moon officia aute, non cupidatat skateboard dolor brunch. Food truck
                  quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon tempor, sunt aliqua put a bird on it
                  squid single-origin coffee nulla assumenda shoreditch et. Nihil anim keffiyeh helvetica,
                  craft beer labore wes anderson cred nesciunt sapiente ea proident. Ad vegan excepteur
                  butcher vice lomo. Leggings occaecat craft beer farm-to-table, raw denim aesthetic synth
                  nesciunt you probably haven't heard of them accusamus labore sustainable VHS.
                </div>
              </div>
            </div>
            <div className="panel panel-default">
              <div className="panel-heading" role="tab" id="headingTwo">
                <h4 className="panel-title">
                  <a
                    className="collapsed"
role="button"
data-toggle="collapse"
data-parent="#accordion"
                    href="#collapseTwo"
aria-expanded="false"
aria-controls="collapseTwo">
                    Collapsible Group Item #2
                  </a>
                </h4>
              </div>
              <div
                id="collapseTwo"
                className="panel-collapse collapse"
                role="tabpanel"
                aria-labelledby="headingTwo">
                <div className="panel-body">
                  Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad
                  squid. 3 wolf moon officia aute, non cupidatat skateboard dolor brunch. Food truck
                  quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon tempor, sunt aliqua put a bird on it
                  squid single-origin coffee nulla assumenda shoreditch et. Nihil anim keffiyeh helvetica,
                  craft beer labore wes anderson cred nesciunt sapiente ea proident. Ad vegan excepteur
                  butcher vice lomo. Leggings occaecat craft beer farm-to-table, raw denim aesthetic synth
                  nesciunt you probably haven't heard of them accusamus labore sustainable VHS.
                </div>
              </div>
            </div>
            <div className="panel panel-default">
              <div className="panel-heading" role="tab" id="headingThree">
                <h4 className="panel-title">
                  <a
                    className="collapsed"
role="button"
data-toggle="collapse"
data-parent="#accordion"
                    href="#collapseThree"
aria-expanded="false"
aria-controls="collapseThree">
                    Collapsible Group Item #3
                  </a>
                </h4>
              </div>
              <div
                id="collapseThree"
                className="panel-collapse collapse"
                role="tabpanel"
                aria-labelledby="headingThree">
                <div className="panel-body">
                  Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad
                  squid. 3 wolf moon officia aute, non cupidatat skateboard dolor brunch. Food truck
                  quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon tempor, sunt aliqua put a bird on it
                  squid single-origin coffee nulla assumenda shoreditch et. Nihil anim keffiyeh helvetica,
                  craft beer labore wes anderson cred nesciunt sapiente ea proident. Ad vegan excepteur
                  butcher vice lomo. Leggings occaecat craft beer farm-to-table, raw denim aesthetic synth
                  nesciunt you probably haven't heard of them accusamus labore sustainable VHS.
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}
