import { SectorObjectType } from '../../../admin/indicator_manager/types';
import { errorHelper } from "../../../admin/indicator_manager/utils/errorHelper";
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { REST_SECTORS } from '../../utils/constants';

const REDUCER_NAME = 'sectors';

type SectorInitialStateType = {
    sectors: SectorObjectType[];
    loading: boolean;
    error: any;
}

const initialState: SectorInitialStateType = {
    sectors: [],
    loading: false,
    error: null,
}

export const fetchSectors = createAsyncThunk(
    `${REDUCER_NAME}/fetch`,
    async (_, { rejectWithValue }) => {
        const response = await fetch(REST_SECTORS)
        const data: SectorObjectType [] = await response.json();

        console.log('fetching sectors data', data)

        if (response.status !== 200) {
            return rejectWithValue(data);
        }

        return data;
    }
);

const fetchSectorSlice = createSlice({
    name: 'fetchSectorsReducer',
    initialState,
    reducers: {
        resetState : () => initialState,
    },
    extraReducers: (builder) => {
        builder.addCase(fetchSectors.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(fetchSectors.fulfilled, (state, action) => {
            state.sectors = [];
            state.loading = false;
            state.sectors = action.payload;
        });
        builder.addCase(fetchSectors.rejected, (state, action) => {
            state.loading = false;
            state.error = errorHelper(action.payload);

        });
    }
});

export const {
    resetState
} = fetchSectorSlice.actions;

export default fetchSectorSlice.reducer;
