import React, { Component, Suspense } from 'react';
import InfiniteCarousel from 'react-leaf-carousel';
import EllipsisText from 'react-ellipsis-text';
import PropTypes from 'prop-types';
import * as Utils from '../../../utils/Utils';
import './filters.css';
import {
  FLAGS_TO_SHOW_DEFAULT,
  FLAGS_TO_SHOW_SMALL,
  FLAGS_TO_SHOW_SMALL_WIDTH_THRESHOLD, NEGATIVE_ONE, ONE, ZERO
} from '../../../utils/constants';
import CountryFlag from '../../utils/CountryFlag';

export default class CountryCarousel extends Component {
  constructor(props) {
    super(props);
    this.state = { slidesToShow: FLAGS_TO_SHOW_DEFAULT };
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
      return slidesToShow === previousState.slidesToShow ? null : { slidesToShow };
    });
  }

  onFlagClick = param => () => {
    const ipSelectedFilter = parseInt(param, 10);
    const { selectedOptions, onChange } = this.props;
    if (selectedOptions.length < 6 || selectedOptions.includes(ipSelectedFilter)) {
      onChange(Utils.calculateUpdatedValuesForDropDowns(ipSelectedFilter, selectedOptions));
    }
  };

  getFlags() {
    const { options, selectedOptions } = this.props;
    return options.sort((a, b) => (a.name > b.name ? ONE : NEGATIVE_ONE)).map((c) => (
      <div
        key={`flag-${c.id}`}
        onClick={this.onFlagClick(c.id)}
        className={`flag ${selectedOptions.includes(c.id) ? 'selected' : ''}`}>
        <Suspense fallback={(<div>loading</div>)}>
          <CountryFlag countryName={c.name} />
        </Suspense>
        <EllipsisText
          text={c.name}
          length={9}
          tail="" />
      </div>
    ));
  }

  // eslint-disable-next-line class-methods-use-this
  calculateCarouselValues() {
    let slidesToShow = FLAGS_TO_SHOW_DEFAULT;
    if (window.innerWidth <= FLAGS_TO_SHOW_SMALL_WIDTH_THRESHOLD) {
      slidesToShow = FLAGS_TO_SHOW_SMALL;
    }
    return slidesToShow;
  }

  render() {
    const { options } = this.props;
    const { slidesToShow } = this.state;
    return (
      options.length > ZERO
        ? (
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
            sideSize={0.1}
            slidesToScroll={4}
            slidesToShow={slidesToShow}
            scrollOnDevice
          >
            {this.getFlags()}
          </InfiniteCarousel>
        )
        : null
    );
  }
}
CountryCarousel.propTypes = {
  options: PropTypes.array.isRequired,
  onChange: PropTypes.func.isRequired,
  selectedOptions: PropTypes.array
};

CountryCarousel.defaultProps = {
  selectedOptions: []
};
