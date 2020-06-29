import React, { Component } from "react";
import InfiniteCarousel from 'react-leaf-carousel';
import { Img } from 'react-image'
import EllipsisText from 'react-ellipsis-text';
import PropTypes from 'prop-types';
import * as Utils from '../../../utils/Utils';
import './filters.css';
import { FLAGS_TO_SHOW_DEFAULT, FLAGS_TO_SHOW_SMALL } from '../../../utils/constants';


export default class CountryCarousel extends Component {
    constructor(props) {
        super();
        this.state = {slidesToShow: this.calculateCarouselValues()};
    }

    componentDidMount() {

        window.addEventListener('resize', this.onResize.bind(this));

    }

    componentWillUnmount() {
        window.removeEventListener('resize', this.onResize.bind(this));
    }

    onResize() {
        this.setState(previousState => {
            const slidesToShow = this.calculateCarouselValues();
            return slidesToShow === previousState.slidesToShow ? null : {slidesToShow: slidesToShow};
        })
    }

    calculateCarouselValues() {
        let slidesToShow = FLAGS_TO_SHOW_DEFAULT;
        if (window.innerWidth <= 1766) {
            slidesToShow = FLAGS_TO_SHOW_SMALL;
        }
        return slidesToShow;
    }

    onFlagClick = param => e => {
        const ipSelectedFilter = parseInt(param);
        const {selectedOptions} = this.props;
        this.props.onChange(Utils.calculateUpdatedValuesForDropDowns(ipSelectedFilter, selectedOptions));
    };

    getFlags() {
        const {options, selectedOptions} = this.props;
        return options.sort((a, b) => a.name > b.name ? 1 : -1).map((c) => {
                return (<div key={`flag-${c.id}`} onClick={this.onFlagClick(c.id)}
                             className={`flag ${selectedOptions.includes(c.id) ? 'selected' : ''}`}>
                    <Img
                        src={Utils.getCountryFlag(c.name)}/>
                    <EllipsisText
                        text={c.name}
                        length={9} tail={''}/>
                </div>);
            }
        );
    }

    render() {
        const {options} = this.props;
        return (
            options.length > 0 ?
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
                        }
                    ]}
                    dots={false}
                    showSides
                    sidesOpacity={1}
                    sideSize={.1}
                    slidesToScroll={4}
                    slidesToShow={this.state.slidesToShow}
                    scrollOnDevice
                >{this.getFlags()}
                </InfiniteCarousel>
                : null
        );
    }
}
