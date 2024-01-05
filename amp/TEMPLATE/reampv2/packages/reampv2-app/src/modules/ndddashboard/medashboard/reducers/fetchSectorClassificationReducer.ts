import { SectorClassifcation } from '../types';
import { errorHelper } from "../../../admin/indicator_manager/utils/errorHelper";
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { REST_SECTOR_CLASSIFICATION } from '../../utils/constants';

const REDUCER_NAME = 'sectorClassification';

type SectorClassificationInitialStateType = {
    data: SectorClassifcation[];
    loading: boolean;
    error: any;
}

const initialState: SectorClassificationInitialStateType = {
    data: [],
    loading: false,
    error: null,
}

export const fetchSectorClassification = createAsyncThunk(
    `${REDUCER_NAME}/fetch`,
    async (_, { rejectWithValue }) => {
        const response = await fetch(REST_SECTOR_CLASSIFICATION)
        const data: SectorClassifcation [] = await response.json();

        if (response.status !== 200) {
            return rejectWithValue(data);
        }

        return data;
    }
);

const fetchSectorClassificationSlice = createSlice({
    name: 'fetchSectorClassificationReducer',
    initialState,
    reducers: {
        resetState : () => initialState,
    },
    extraReducers: (builder) => {
        builder.addCase(fetchSectorClassificationReducer.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(fetchSectorClassificationReducer.fulfilled, (state, action) => {
            state.data = [];
            state.loading = false;
            state.data = action.payload;
        });
        builder.addCase(fetchSectorClassificationReducer.rejected, (state, action) => {
            state.loading = false;
            state.error = errorHelper(action.payload);

        });
    }
});

export const {
    resetState
} = fetchSectorClassificationSlice.actions;

export default fetchSectorClassificationSlice.reducer;
