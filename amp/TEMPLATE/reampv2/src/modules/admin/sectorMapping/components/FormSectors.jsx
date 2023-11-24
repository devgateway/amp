import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { SectorMappingContext } from './Startup';
import './css/style.css';
//import * as Utils from '../utils/Utils';
import {
    DST_SCHEME_SECTOR,
    SRC_SCHEME_SECTOR,
    SECTOR_MAPPING,
    SRC_SECTOR,
    DST_SECTOR,
    TYPE_SRC,
    TYPE_DST,
    ALL_SCHEMES
} from '../constants/Constants';

import {sendNDDError, sendNDDPending, sendNDDSaving} from "../../ndd/reducers/saveNDDReducer";
import {updateActivitiesError, updateActivitiesPending} from "../../ndd/reducers/updateActivitiesReducer";
import saveNDD from "../../ndd/actions/saveNDD";
import updateActivities from "../../ndd/actions/updateActivities";
import SectorsHeader from "./SectorsHeader";
import Notifications from "./Notifications";

class FormSectors extends Component {

    constructor(props) {
        super(props);
        this.state = {
            data: [],
            validationErrors: undefined,
            src: undefined,
            dst: undefined,
            schemes: undefined,
            updatedActivities: false,
            blockUI: false,
            unsavedChanges: false,
            saved: false,
            level: 0
        };
        this.addRow = this.addRow.bind(this);
        this.saveAll = this.saveAll.bind(this);
        this.onRowChange = this.onRowChange.bind(this);
        this.remove = this.remove.bind(this);
        this.clearMessages = this.clearMessages.bind(this);
        // this.onChangeMainProgram = this.onChangeMainProgram.bind(this);
        this.onChangeMainScheme = this.onChangeMainScheme.bind(this);
        this.clearAll = this.clearAll.bind(this);
    }

    componentDidMount() {
        const { mappings, schemes, translations, trnPrefix, settings, isIndirect } = this.context;
        document.title = translations[`${trnPrefix}page-title`];

        // Load Source Scheme Sector selected
        this.setState(() => {
            console.log('mappings: ', mappings);
            if (mappings[SRC_SCHEME_SECTOR]) {
                const src = {id: mappings[SRC_SCHEME_SECTOR].id, value: mappings[SRC_SCHEME_SECTOR].value};
                return {src};
            }
            return { src: undefined };
        });

        // Load Destination Scheme Sector selected
        this.setState(() => {
            if (mappings[DST_SCHEME_SECTOR]) {
                const dst = {id: mappings[DST_SCHEME_SECTOR].id, value: mappings[DST_SCHEME_SECTOR].value};
                return {dst};
            }
            return { dst: undefined };
        });

        this.setState(() => ({ schemes }));

        // Load Sector Mappings saved
        this.setState(previousState => {
            const data = [...previousState.data];
            if (mappings[SECTOR_MAPPING]) {
                mappings[SECTOR_MAPPING].forEach(mapping => {
                    const pair = {id: `${mapping[SRC_SECTOR]}${mapping[DST_SECTOR]}` };
                    pair[SRC_SECTOR] =  {
                        id: mapping[SRC_SECTOR],
                        value: this.getSectorNameFromScheme(mappings, mapping[SRC_SECTOR], TYPE_SRC)
                    };

                    pair[DST_SECTOR] = {
                        id: mapping[DST_SECTOR],
                        value: this.getSectorNameFromScheme(mappings, mapping[DST_SECTOR], TYPE_DST)
                    };

                    data.push(pair);
                });
            }
            return { data };
        });
    }

    getSectorNameFromScheme(mappings, sectorId, type) {
        const key = type === TYPE_SRC ? SRC_SCHEME_SECTOR : DST_SCHEME_SECTOR;

        if (mappings) {
            const schemeId = mappings[key].id;
            const scheme = mappings[ALL_SCHEMES].find(s => s.id === schemeId);
            if (scheme) {
                const sector = scheme.children.find(s => s.id === sectorId);
                if (sector) {
                    return sector.value;
                }
            }
        }
        return '';
    }

    componentDidUpdate(prevProps) {
        if (prevProps !== this.props) {
            // eslint-disable-next-line react/no-did-update-set-state
            this.setState(previousState => {
                if (!previousState.saved) {
                    return { saved: true };
                } else {
                    return null;
                }
            });
        }
    }

    //TODO: check addRow()
    addRow() {
        this.clearMessages();
        this.setState(previousState => {
            const data = [...previousState.data];
            const pair = {
                [SRC_SECTOR]: {},
                [DST_SECTOR]: {},
                id: Math.random() * -1
            };
            data.push(pair);
            setTimeout(() => (window.scrollTo(0, document.body.scrollHeight)), 500);
            return { data, unsavedChanges: true, adding: true };
        });

    }
    onRowChange() {}

    remove(row) {}

    clearMessages() {
        this.setState({
            validationErrors: undefined, updatedActivities: false, blockUI: false
        });
    }

    saveAll() {}

    onChangeMainScheme(type, scheme) {
        const { translations, trnPrefix } = this.context;
        const { data, src, dst } = this.state;
        this.src_ = src;
        this.dst_ = dst;

        const newScheme = (scheme && scheme.length > 0) ? scheme[0] : {};
        const oldScheme = (this.state[type] ? this.state[type] : {});
        let autoAddRow = false;

        if (oldScheme.id !== newScheme.id) {
            if (oldScheme.id !== undefined) {
                if (data.length === 0 || window.confirm(translations[`${trnPrefix}warning_on_change_main_sector`])) {
                    if (newScheme.id !== undefined) {
                        // Old Scheme Sector -> New Sector.
                        this.setState(previousState => ({ [type]: newScheme }));
                        this[`${type}_`] = newScheme;
                        autoAddRow = true;
                    } else {
                        // Old Scheme Sector -> Nothing.
                        this.setState(previousState => ({ [type]: undefined }));
                        this[`${type}_`] = undefined;
                    }
                    this.clearAll();
                } else {
                    // Revert to previous Sector.
                    this.setState(previousState => previousState);
                    // TODO: set focus in the selector.
                }
            } else {
                // Nothing -> Program.
                this.setState(previousState => ({ [type]: newScheme }));
                this[`${type}_`] = newScheme;
                autoAddRow = true;
            }
        }

        // Note src_ and dst_ because setState() is not immediate.
        if (this.src_ && this.dst_ && autoAddRow) {
            this.addRow();
        }
    }

    clearAll() {
        this.setState({
            data: []
        });
    }

    revertAllChanges() {
        window.location.reload();
    }

    render() {
        const { data, validationErrors, src, dst, updatedActivities, unsavedChanges, saved, level } = this.state;
        const { error, pending, translations, updating, errorUpdating, saving } = this.props;

        const { trnPrefix } = this.context;
        const messages = [];
        if (error) {
            messages.push({ isError: true, text: error.toString() });
        }
        if (validationErrors) {
            messages.push({ isError: true, text: validationErrors });
        }
        if (!saving && !error && !unsavedChanges && saved) {
            messages.push({ isError: false, text: translations[`${trnPrefix}notification-saved-ok`] });
        }

        //TODO: check this flags commented
        // if (updatedActivities) {
        //     messages.push({
        //         isError: false,
        //         text: translations[`${trnPrefix}update-activities-successful`]
        //     });
        // }
        // if (updating) {
        //     messages.push({
        //         isError: false,
        //         text: translations[`${trnPrefix}update-activities-wait`]
        //     });
        // }
        if (errorUpdating) {
            messages.push({
                isError: true,
                text: errorUpdating
            });
        }

        return (
            <div className="form-container">
                <SectorsHeader
                    src={src}
                    dst={dst}
                    key={Math.random()}
                    busy={updating}
                    onChange={this.onChangeMainScheme}
                />

                <Notifications messages={messages} />
            </div>
        );
    }

}

FormSectors.contextType = SectorMappingContext;

FormSectors.propTypes = {
    translations: PropTypes.object.isRequired,
    error: PropTypes.object,
    pending: PropTypes.bool,
    updating: PropTypes.bool,
    errorUpdating: PropTypes.string,
    _saveSectorMapping: PropTypes.func.isRequired, // _saveNDD
    //_updateActivities: PropTypes.func.isRequired,
    saving: PropTypes.bool.isRequired
};

FormSectors.defaultProps = {
    error: undefined,
    pending: false,
    updating: false,
    errorUpdating: null
};

const mapStateToProps = state => ({
    translations: state.translationsReducer.translations,
    // error: sendNDDError(state.saveNDDReducer),
    // saving: sendNDDSaving(state.saveNDDReducer),
    // pending: sendNDDPending(state.saveNDDReducer),
    // updating: updateActivitiesPending(state.updateActivitiesReducer),
    // errorUpdating: updateActivitiesError(state.updateActivitiesReducer)
});
const mapDispatchToProps = dispatch => bindActionCreators({
    // _saveNDD: saveNDD,
    // _updateActivities: updateActivities
}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(FormSectors);
