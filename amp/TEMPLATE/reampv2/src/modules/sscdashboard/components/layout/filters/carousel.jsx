import React, {Component} from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import InfiniteCarousel from 'react-leaf-carousel';
import './filters.css';

class CountryCarousel extends Component {
    render() {
        return (
          <div className="col-md-10">
          <InfiniteCarousel
    breakpoints={[
      {
        breakpoint: 500,
        settings: {
          slidesToShow: 9,
          slidesToScroll: 2,
        },
      },
      {
        breakpoint: 768,
        settings: {
          slidesToShow: 3,
          slidesToScroll: 3,
        },
      },
    ]}
    dots={false}
    showSides={true}
    sidesOpacity={1}
    sideSize={.1}
    slidesToScroll={4}
    slidesToShow={9}
    scrollOnDevice={true}
  >
  <div>
    <img
      src={require('./flags/argentina.svg')}/>
    <span>Argentina</span>
  </div>
    <div className="selected">
      <img
        src={require('./flags/brazil.svg')}/>
      <span>Brazil</span>
    </div>
    <div>
      <img
        src={require('./flags/china.svg')}/>
      <span>China</span>
    </div>
    <div>
      <img
        src={require('./flags/india.svg')}/>
      <span>India</span>
    </div>
    <div>
      <img
        src={require('./flags/indonesia.svg')}/>
      <span>Indonesia</span>
    </div>
    <div>
      <img
        src={require('./flags/japan.svg')}/>
      <span>Japan</span>
    </div>
    <div>
      <img
        src={require('./flags/mozambique.svg')}/>
      <span>Mozambique</span>
    </div>
    <div>
      <img
        src={require('./flags/nigeria.svg')}/>
      <span>Nigeria</span>
    </div>
    <div>
      <img
        src={require('./flags/senegal.svg')}/>
      <span>Senegal</span>
    </div>
    <div>
      <img
        src={require('./flags/ghana.svg')}/>
      <span>Ghana</span>
    </div>

  </InfiniteCarousel>
          </div>
        );
    }
}

export default CountryCarousel;
