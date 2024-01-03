import { createReducer, on } from "@ngrx/store";
import { Action } from "rxjs/internal/scheduler/Action";
import { decrementPage, incrementPage, resetPage, setCollectionQuantity, setElementsToDisplay } from "./carousel.actions";
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
    }),
    on(resetPage, (state) => {
        return{
            ...state,
            currentPage: 0,
        }
    }),
    on(setCollectionQuantity, (state, action) => {
        return{
            ...state,
            collectionQuantity : action.quantity
        }
    }),
    on(setElementsToDisplay, (state, action) => {
        return{
            ...state,
            elementsToDisplay: action.value,
            pageQuantity : action.pageQuantity,
        }
    })
);

export function carouselReducer(state : any, action : any){
    return _carouselReducer(state, action);
}