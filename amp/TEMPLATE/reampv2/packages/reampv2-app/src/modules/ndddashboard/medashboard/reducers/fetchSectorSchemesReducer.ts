import { SectorScheme } from "../types";
import { errorHelper } from "../../../admin/indicator_manager/utils/errorHelper";
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { REST_SECTOR_SCHEMES } from '../../utils/constants';

const REDUCER_NAME = 'sectorSchemes';

type SectorSchemesInitialStateType = {
    data: SectorScheme[];
    loading: boolean;
    error: any;
}

const initialState: SectorSchemesInitialStateType = {
    data: [],
    loading: false,
    error: null,
}

export const fetchSectorSchemesReducer = createAsyncThunk(
    `${REDUCER_NAME}/fetch`,
    async (_, { rejectWithValue }) => {
        const response = await fetch(REST_SECTOR_SCHEMES)
        const data: SectorScheme [] = await response.json();

        console.log('fetching sector schemes data', data)

        if (response.status !== 200) {
            return rejectWithValue(data);
        }

        return data;
    }
);

const fetchSectorSchemesSlice = createSlice({
    name: 'fetchSectorSchemesReducer',
    initialState,
    reducers: {
        resetState : () => initialState,
    },
    extraReducers: (builder) => {
        builder.addCase(fetchSectorSchemesReducer.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(fetchSectorSchemesReducer.fulfilled, (state, action) => {
            state.data = [];
            state.loading = false;
            state.data = action.payload;
        });
        builder.addCase(fetchSectorSchemesReducer.rejected, (state, action) => {
            state.loading = false;
            state.error = errorHelper(action.payload);

        });
    }
});

export const {
    resetState
} = fetchSectorSchemesSlice.actions;

export default fetchSectorSchemesSlice.reducer;
