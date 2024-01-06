import { createReducer, on } from "@ngrx/store";
import { initialState } from "./carousel.state";
import { setCollection } from "./collections.actions";

export const collectionsFeatureKey = 'collections'

const _collectionsReducer = createReducer(
    initialState,
    on(setCollection, (state, action) => {
        return{
            ...state,
            collections : action.collections
        }
    }),
)

export function collectionsReducer(state : any, action : any){
    return _collectionsReducer(state, action);
}