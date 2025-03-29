import React from "react";
import Select from "react-select";

class SelectHandler extends React.Component {
    render() {
        const { options, placeholder, selected, disabled, onChange, translations, trnPrefix } = this.props;

        // Ensure options are formatted correctly for react-select

        const sortedOptions = options ? options.sort((a, b) => a.value.localeCompare(b.value)) : [];

        return (
            <Select
                id="custom-select"
                options={sortedOptions}
                isSearchable
                isClearable
                getOptionLabel={(option)=>option.value}
                getOptionValue={(option)=>option.id}
                placeholder={placeholder || "Select an option..."}
                value={selected && selected.length > 0 ? sortedOptions.find((opt) => opt.id === selected[0].id) : null}
                onChange={onChange}
                isDisabled={disabled}
                className={selected ? "" : "is-invalid"}
                noOptionsMessage={() => translations?.[`${trnPrefix}no-matches-found`] || "No matches found"}
            />
        );
    }
}

export default SelectHandler;
