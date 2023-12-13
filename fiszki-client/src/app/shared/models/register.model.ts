import { BaseUserModel } from "./base-user.model";

export class RegisterModel extends BaseUserModel
{
    firstname : string;
    lastname : string;

    constructor(email: string, password: string, firstname : string, lastname : string)
    {
        super(email, password);
        this.firstname = firstname;
        this.lastname = lastname;
    }
}