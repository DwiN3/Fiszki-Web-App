import { createAction, props } from "@ngrx/store";
import { FlashcardCollectionModel } from "../models/flashcard-collection.model";

export const addCollection = createAction(
    'addCollection',
    props<{collection : FlashcardCollectionModel}>()
)

export const deleteCollection = createAction(
    'deleteCollection',
    props<{id : number}>()
)

export const setCollection = createAction(
    'setCollection',
    props<{collections : FlashcardCollectionModel[]}>()
)