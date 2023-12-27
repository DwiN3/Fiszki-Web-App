import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { UserRoutingModule } from "./user-routing.module";
import { UserComponent } from "./user.component";
import { NavbarComponent } from './core/navbar/navbar.component';
import { UserSettingsComponent } from "./pages/user-settings/user-settings.component";
import { UserHomeComponent } from "./pages/user-home/user-home.component";
import { AuthGuard } from "src/app/shared/services/auth-guard.service";
import { SharedModule } from "src/app/shared/shared.module";

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
        SharedModule
    ],
    exports: [
        
    ],
    providers: [
        AuthGuard,
        UserComponent,
    ]
})

export class UserModule {}