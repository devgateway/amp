import React, { Component } from "react";
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
                            src={require('../../../images/flags/argentina.svg')}
                            alt='Argentina'/>
                        <span>Argentina</span>
                    </div>
                    <div className="selected">
                        <img
                            src={require('../../../images/flags/brazil.svg')}
                            alt='Brazil'/>
                        <span>Brazil</span>
                    </div>
                    <div>
                        <img
                            src={require('../../../images/flags/china.svg')}
                            alt='China'/>
                        <span>China</span>
                    </div>
                    <div>
                        <img
                            src={require('../../../images/flags/india.svg')}
                            alt='India'/>
                        <span>India</span>
                    </div>
                    <div>
                        <img
                            src={require('../../../images/flags/indonesia.svg')}
                            alt='Indonesia'/>
                        <span>Indonesia</span>
                    </div>
                    <div>
                        <img
                            src={require('../../../images/flags/japan.svg')}
                            alt='japan'/>
                        <span>Japan</span>
                    </div>
                    <div>
                        <img
                            src={require('../../../images/flags/mozambique.svg')}
                            alt='mozambique'/>
                        <span>Mozambique</span>
                    </div>
                    <div>
                        <img
                            src={require('../../../images/flags/nigeria.svg')}
                            alt='nigeria'/>
                        <span>Nigeria</span>
                    </div>
                    <div>
                        <img
                            src={require('../../../images/flags/senegal.svg')}
                            alt='Senegal'/>
                        <span>Senegal</span>
                    </div>
                    <div>
                        <img
                            src={require('../../../images/flags/ghana.svg')}
                            alt='ghana'/>
                        <span>Ghana</span>
                    </div>

                </InfiniteCarousel>
            </div>
        );
    }
}

export default CountryCarousel;
