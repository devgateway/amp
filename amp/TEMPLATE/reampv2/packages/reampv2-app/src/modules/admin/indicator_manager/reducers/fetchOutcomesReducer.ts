import { Dispatch } from 'redux';

export const FETCH_OUTCOMES_REQUEST = 'FETCH_OUTCOMES_REQUEST';
export const FETCH_OUTCOMES_SUCCESS = 'FETCH_OUTCOMES_SUCCESS';
export const FETCH_OUTCOMES_FAILURE = 'FETCH_OUTCOMES_FAILURE';

export interface Outcome {
  id: number;
  name: string;
  outputs: { id: number; name: string }[];
}

interface OutcomesState {
  outcomes: Outcome[];
  loading: boolean;
  error: string | null;
}

const initialState: OutcomesState = {
  outcomes: [],
  loading: false,
  error: null,
};

export const fetchOutcomesReducer = (state = initialState, action: any): OutcomesState => {
  switch (action.type) {
    case FETCH_OUTCOMES_REQUEST:
      return { ...state, loading: true, error: null };
    case FETCH_OUTCOMES_SUCCESS:
      return { ...state, loading: false, outcomes: action.payload, error: null };
    case FETCH_OUTCOMES_FAILURE:
      return { ...state, loading: false, error: action.payload };
    default:
      return state;
  }
};

export const getOutcomes = () => async (dispatch: Dispatch) => {
  dispatch({ type: FETCH_OUTCOMES_REQUEST });
  try {
    const res = await fetch('/rest/amp-outcome-output/outcomes');
    const data = await res.json();
    dispatch({ type: FETCH_OUTCOMES_SUCCESS, payload: data });
  } catch (error: any) {
    dispatch({ type: FETCH_OUTCOMES_FAILURE, payload: error.message || 'Failed to fetch outcomes' });
  }
};
