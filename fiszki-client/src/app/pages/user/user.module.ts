import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { UserRoutingModule } from "./user-routing.module";
import { UserComponent } from "./user.component";
import { NavbarComponent } from './core/navbar/navbar.component';
import { UserHomeComponent } from "./pages/user-home/user-home.component";
import { AuthGuard } from "src/app/shared/services/auth-guard.service";
import { SharedModule } from "src/app/shared/shared.module";
import { UserProfileComponent } from './pages/user-profile/user-profile.component';
import { UserLevelResolver } from "./services/user-level-resolver";
import { LevelDirective } from "./pages/user-profile/directives/level-status";

@NgModule({
    declarations:[
        NavbarComponent,
        UserComponent,
        UserHomeComponent,
        UserProfileComponent,
        LevelDirective,
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
        UserLevelResolver,
    ]
})

export class UserModule {}