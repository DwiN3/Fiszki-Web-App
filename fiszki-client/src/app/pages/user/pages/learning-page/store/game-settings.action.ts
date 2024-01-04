import { createAction, props } from "@ngrx/store";

export const setLearningMode = createAction(
    'setLearningMode',
    props<{mode : string}>()
)

export const setLanguage = createAction(
    'setLanguage'
)

export const setCategory = createAction(
    'setCategory',
    props<{category : string}>()
)

export const setCollection = createAction(
    'setCollection',
    props<{collectionName : string}>()
)

export const setLimit = createAction(
    'setLimit',
    props<{limit : number}>()
)