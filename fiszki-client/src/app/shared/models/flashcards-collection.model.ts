export class FlashcardCollectionModel
{
    flashcards : number;
    nameCollection : string;

    constructor(nameCollection : string,  flashcards : number)
    {
        this.nameCollection = nameCollection;
        this.flashcards = flashcards;
    }
}