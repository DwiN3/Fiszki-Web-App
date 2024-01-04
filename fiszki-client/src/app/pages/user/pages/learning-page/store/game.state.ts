export interface GameSettingsState
{
    learningMode : string;
    polishFirst : boolean;
    category : string;
    collection : string;
    limit : number;
}

export const initialState = 
{
    learningMode : 'learning',
    polishFirst : true,
    category : '',
    collection : '',
    limit : 5,
}