import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { SharedModule } from "src/app/shared/shared.module";
import { UserRoutingModule } from "./user-page-routing.module";

@NgModule({
    declarations:[],
    imports: [
        CommonModule,
        UserRoutingModule,
    ],
    providers: []
})

export class UserPageModule {}