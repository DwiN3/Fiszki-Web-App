import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { UserRoutingModule } from "./user-routing.module";
import { UserComponent } from "./user.component";
import { NavbarComponent } from './core/navbar/navbar.component';
import { UserSettingsComponent } from "./pages/user-settings/user-settings.component";
import { UserHomeComponent } from "./pages/user-home/user-home.component";

@NgModule({
    declarations:[
        NavbarComponent,
        UserComponent,
        UserHomeComponent,
        UserSettingsComponent,
    ],
    imports: [
        CommonModule,
        UserRoutingModule,
    ],
    exports: [
        
    ],
    providers: [
        UserComponent,
    ]
})

export class UserModule {}