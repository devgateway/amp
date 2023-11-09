import { errorHelper } from './../utils/errorHelper';
import { IndicatorObjectType } from './../types';
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";

type IndicatorInitialStateType = {
    indicators: IndicatorObjectType[];
    loading: boolean;
    error: any;
    sizePerPage?: number;
    filterSectorSelected: number;
}

const initialState: IndicatorInitialStateType = {
    indicators: [],
    loading: false,
    error: null,
    sizePerPage: 10,
    filterSectorSelected: 0
}

export const getIndicators = createAsyncThunk(
    "indicators/getIndicators",
    async (_, { rejectWithValue }) => {
        const response = await fetch("/rest/indicatorManager/indicators");
        const data: IndicatorObjectType [] = await response.json();

        if (response.status !== 200) {
            return rejectWithValue(data);
        }

        return data;
    }
);

const fetchIndicatorSlice = createSlice({
    name: "indicatorState",
    initialState,
    reducers: {
        addIndicator: (state, action) => {
            state.indicators = [...state.indicators, action.payload];
        },
        removeIndicator: (state, action) => {
            state.indicators = state.indicators.filter((indicator) => indicator.id !== action.payload);
        },
        updateIndicator: (state, action) => {
            state.indicators = state.indicators.map((indicator) => {
                if (indicator.id === action.payload.id) {
                    return action.payload;
                }
                return indicator;
            });
        },
        setSizePerPage: (state, action) => {
            state.sizePerPage = action.payload;
        },
        resetSizePerPage : (state) => {
            state.sizePerPage = initialState.sizePerPage;
        },
        setFilterSectorSelected: (state, action) => {
            state.filterSectorSelected = parseInt(action.payload);
        }
    },
    extraReducers: (builder) => {
        builder.addCase(getIndicators.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(getIndicators.fulfilled, (state, action) => {
            state.indicators = [];
            state.loading = false;
            state.indicators = action.payload;
        });
        builder.addCase(getIndicators.rejected, (state, action) => {
            state.loading = false;
            state.error = errorHelper(action.payload);

        });
    }
});

export const {
    addIndicator,
    removeIndicator,
    updateIndicator,
    setSizePerPage,
    resetSizePerPage,
    setFilterSectorSelected
} = fetchIndicatorSlice.actions;

export default fetchIndicatorSlice.reducer;
