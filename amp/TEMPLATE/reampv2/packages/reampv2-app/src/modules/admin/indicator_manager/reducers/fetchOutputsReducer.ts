import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';

export interface Output {
  id: number;
  name: string;
  description?: string;
}

interface OutputsState {
  outputs: Output[];
  loading: boolean;
  error: string | null;
}

const initialState: OutputsState = {
  outputs: [],
  loading: false,
  error: null,
};

export const FETCH_OUTPUTS_REQUEST = 'FETCH_OUTPUTS_REQUEST';
export const FETCH_OUTPUTS_SUCCESS = 'FETCH_OUTPUTS_SUCCESS';
export const FETCH_OUTPUTS_FAILURE = 'FETCH_OUTPUTS_FAILURE';

export const fetchOutputsReducer = (state = initialState, action: any): OutputsState => {
  switch (action.type) {
    case FETCH_OUTPUTS_REQUEST:
      return { ...state, loading: true, error: null };
    case FETCH_OUTPUTS_SUCCESS:
      return { ...state, loading: false, outputs: action.payload, error: null };
    case FETCH_OUTPUTS_FAILURE:
      return { ...state, loading: false, error: action.payload };
    default:
      return state;
  }
};

export const getOutputs = () => async (dispatch: any) => {
  dispatch({ type: FETCH_OUTPUTS_REQUEST });
  try {
    const res = await fetch('/rest/amp-outcome-output/outputs');
    const data = await res.json();
    dispatch({ type: FETCH_OUTPUTS_SUCCESS, payload: data });
  } catch (error: any) {
    dispatch({ type: FETCH_OUTPUTS_FAILURE, payload: error.message || 'Failed to fetch outputs' });
  }
};

export default fetchOutputsReducer;
