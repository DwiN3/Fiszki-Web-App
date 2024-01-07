import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from "@angular/router";
import { map, Observable } from "rxjs";
import { UserCollectionService } from "src/app/pages/user/services/user-collection.service";

@Injectable({providedIn: 'root'})
export class CollectionExistsGuard implements CanActivate
{

    constructor(private collectionService : UserCollectionService){}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> 
    {
        const collectionName = route.params['collectionName'];

        return this.collectionService.GetFlashCardsByCollection(collectionName)
            .pipe(
                map(collection => {
                    if(collection)
                    {
                        console.log(collection)
                        return true;
                    }    
                    else
                        return false;
                })
            )
    }
    
}