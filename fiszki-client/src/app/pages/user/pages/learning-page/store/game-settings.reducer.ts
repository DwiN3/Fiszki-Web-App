import { createReducer, on } from "@ngrx/store";
import { setCategory, setCollection, setLanguage, setLearningMode, setLimit } from "./game-settings.action";
import { initialState } from "./game.state";

export const gameSettingFeautureKey = 'gameSettings';

const _gameSettingsReducer = createReducer(
    initialState,
    on(setLearningMode, (state, action) => {
        return{
            ...state,
            learningMode : action.mode 
        }
    }),
    on(setLanguage, (state) => {
        return{
            ...state,
            polishFirst : !state.polishFirst,
        };
    }),
    on(setCollection, (state, action) => {
        return{
            ...state,
           collection : action.collectionName,
           category : ''
        };
    }),
    on(setCategory, (state, action) => {
        return{
            ...state,
           collection : '',
           category : action.category
        };
    }),
    on(setLimit, (state, action) => {
        return{
            ...state,
            limit : action.limit
        }
    })
);

export function gameSettingsReducer(state : any, action : any){
    return _gameSettingsReducer(state, action);
}