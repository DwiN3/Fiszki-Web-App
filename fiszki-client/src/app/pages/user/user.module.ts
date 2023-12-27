import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { UserRoutingModule } from "./user-routing.module";
import { UserComponent } from "./user.component";
import { NavbarComponent } from './core/navbar/navbar.component';
import { UserHomeComponent } from "./pages/user-home/user-home.component";
import { AuthGuard } from "src/app/shared/services/auth-guard.service";
import { SharedModule } from "src/app/shared/shared.module";
import { UserProfileComponent } from './pages/user-profile/user-profile.component';
import { UserInfoComponent } from './pages/user-profile/components/user-info/user-info.component';
import { UserLevelComponent } from './pages/user-profile/components/user-level/user-level.component';

@NgModule({
    declarations:[
        NavbarComponent,
        UserComponent,
        UserHomeComponent,
        UserProfileComponent,
        UserInfoComponent,
        UserLevelComponent,
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