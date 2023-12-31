import { createAction, props } from "@ngrx/store";

export const incrementPage = createAction(
    'incrementPage',    
)

export const decrementPage = createAction(
    'decrementPage',    
)

export const resetPage = createAction(
    'resetPage',
)

export const setElementsToDisplay = createAction(
    'setElementsToDisplay',
    props<{value : number, pageQuantity : number}>()
)

