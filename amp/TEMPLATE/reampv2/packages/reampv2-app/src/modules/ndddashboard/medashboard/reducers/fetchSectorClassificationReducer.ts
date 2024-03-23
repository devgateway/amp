import { SectorClassifcation } from '../types';
import { errorHelper } from "../../../admin/indicator_manager/utils/errorHelper";
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { REST_SECTOR_CLASSIFICATION } from '../../utils/constants';
import {SectorObjectType} from "../../../admin/indicator_manager/types";
import {extractSectors} from "../utils/data";

const REDUCER_NAME = 'sectorClassification';

type SectorClassificationInitialStateType = {
    data: SectorClassifcation[];
    loading: boolean;
    error: any;
    sectors: SectorObjectType[];
    selectedSector: SectorObjectType | null;
}

const initialState: SectorClassificationInitialStateType = {
    data: [],
    loading: false,
    error: null,
    sectors: [],
    selectedSector: null
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
        setSelectedSectorState: (state, action) => {
            state.selectedSector = action.payload;
        },
        clearSelectedSectorState: (state) => {
            state.selectedSector = null;
        }
    },
    extraReducers: (builder) => {
        builder.addCase(fetchSectorClassification.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(fetchSectorClassification.fulfilled, (state, action) => {
            state.data = [];
            state.loading = false;
            state.data = action.payload;
            state.sectors = extractSectors(action.payload);
        });
        builder.addCase(fetchSectorClassification.rejected, (state, action) => {
            state.loading = false;
            state.error = errorHelper(action.payload);
            state.data = [];
            state.sectors = [];

        });
    }
});

export const {
    resetState,
    setSelectedSectorState,
    clearSelectedSectorState
} = fetchSectorClassificationSlice.actions;

export default fetchSectorClassificationSlice.reducer;
