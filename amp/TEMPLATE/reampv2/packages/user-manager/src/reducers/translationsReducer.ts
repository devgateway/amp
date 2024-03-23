import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { loadTranslations } from '../utils/loadTranslations';
import initialTranslations from '../config/initialTranslations.json';
import { TranslationsPack } from '../types';
import { errorHelper } from '../utils/errorHelper';

interface TranslationsState {
    translations: TranslationsPack;
    loading: boolean;
    error: any;
}

const initialState: TranslationsState = {
  translations: initialTranslations,
  loading: false,
  error: null,
};

export const fetchTranslations = createAsyncThunk(
  'translations/fetchTranslations',
  async (trnPack:any, { rejectWithValue }) => {
    const response = await loadTranslations(trnPack);

    if (!response) {
      return rejectWithValue(response);
    }

    return response;
  }
);

const translationsSlice = createSlice({
  name: 'translations',
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder.addCase(fetchTranslations.pending, (state) => {
      state.loading = true;
    });
    builder.addCase(fetchTranslations.fulfilled, (state, action) => {
      state.loading = false;
      state.translations = action.payload as TranslationsPack;
    });
    builder.addCase(fetchTranslations.rejected, (state, action) => {
      state.loading = false;
      state.error = errorHelper(action.payload);
    });
  }
});

export default translationsSlice.reducer;
