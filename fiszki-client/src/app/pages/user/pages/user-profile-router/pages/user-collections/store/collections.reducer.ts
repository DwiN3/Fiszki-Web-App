import { createReducer, on } from "@ngrx/store";
import { initialState } from './collections.state'
import { addCollection, setCollection } from "./collections.actions";

export const collectionsFeatureKey = 'collections'

const _collectionsReducer = createReducer(
    initialState,
    on(setCollection, (state, action) => {
        return{
            ...state,
            collections : action.collections
        }
    }),
    on(addCollection, (state, action) => {
        return{
            ...state,
            collections : state.collections.concat(action.collection)
        }
    })
)

export function collectionsReducer(state : any, action : any){
    return _collectionsReducer(state, action);
}