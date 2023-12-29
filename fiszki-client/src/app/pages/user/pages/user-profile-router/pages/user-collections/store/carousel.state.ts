export interface CarouselState 
{
    currentPage : number,
    collectionQuantity : number,
}

export const initialState = 
{
    currentPage: 0,
    collectionQuantity: 10,
}