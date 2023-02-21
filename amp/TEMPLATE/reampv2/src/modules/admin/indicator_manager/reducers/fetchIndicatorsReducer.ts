import { IndicatorObjectType } from './../types';
import { createAsyncThunk, createSlice, } from "@reduxjs/toolkit";

type IndicatorInitialStateType = {
    indicators: IndicatorObjectType[];
    loading: boolean;
    error: any;
}

const initialState: IndicatorInitialStateType = {
    indicators: [],
    loading: false,
    error: null,
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
    name: "indicatorsData",
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
        }
    },
    extraReducers: (builder) => {
        builder.addCase(getIndicators.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(getIndicators.fulfilled, (state, action) => {
            state.loading = false;
            state.indicators = action.payload;
        });
        builder.addCase(getIndicators.rejected, (state, action) => {
            state.loading = false;
            state.error = action.error;
    
        });
    }
});

export const { addIndicator } = fetchIndicatorSlice.actions;

export default fetchIndicatorSlice.reducer;
