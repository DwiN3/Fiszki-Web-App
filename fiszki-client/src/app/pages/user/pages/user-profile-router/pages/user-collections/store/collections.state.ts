import { FlashcardCollectionModel } from "../models/flashcard-collection.model";

export interface CollectionsState
{
    collections : FlashcardCollectionModel[];
}

export const initialState = 
{
    collections : [],
}