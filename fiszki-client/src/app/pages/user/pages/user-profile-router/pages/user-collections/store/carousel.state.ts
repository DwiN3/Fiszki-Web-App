export interface CarouselState 
{
    currentPage : number,
    collectionQuantity : number,
    elementsToDisplay : number,
    pageQuantity : number,
}

export const initialState = 
{
    currentPage: 0,
    collectionQuantity: 8,
    elementsToDisplay: 0,
    pageQuantity : 0,
}