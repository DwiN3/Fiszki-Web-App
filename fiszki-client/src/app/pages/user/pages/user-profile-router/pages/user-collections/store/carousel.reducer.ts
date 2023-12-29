import { createReducer, on } from "@ngrx/store";
import { decrementPage, incrementPage } from "./carousel.actions";
import { initialState } from "./carousel.state";

export const carouselFeatureKey = 'carousel';

const _carouselReducer = createReducer(
    initialState,
    on(incrementPage, (state) => {
        return{
            ...state,
            currentPage: state.currentPage + 1,
        };
    }),
    on(decrementPage, (state) => {
        return{
            ...state,
            currentPage: state.currentPage - 1,
        }
    })
);

export function carouselReducer(state : any, action : any){
    return _carouselReducer(state, action);
}