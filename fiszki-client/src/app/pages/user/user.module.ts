import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { UserRoutingModule } from "./user-routing.module";
import { UserComponent } from "./user.component";
import { NavbarComponent } from './core/navbar/navbar.component';
import { UserHomeComponent } from "./pages/user-home/user-home.component";
import { AuthGuard } from "src/app/shared/services/auth-guard.service";
import { SharedModule } from "src/app/shared/shared.module";
import { UserProfileComponent } from './pages/user-profile-router/pages/user-profile/user-profile.component';
import { UserLevelResolver } from "./services/user-level-resolver";
import { LevelDirective } from "./pages/user-profile-router/pages/user-profile/directives/level-status";
import { UserProfileRouterComponent } from './pages/user-profile-router/user-profile-router.component';
import { UserCollectionsComponent } from './pages/user-profile-router/pages/user-collections/user-collections.component';
import { StoreModule } from "@ngrx/store";
import { carouselFeatureKey, carouselReducer } from "./pages/user-profile-router/pages/user-collections/store/carousel.reducer";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { CollectionComponent } from './pages/user-profile-router/pages/user-collections/components/collection/collection.component';

@NgModule({
    declarations:[
        NavbarComponent,
        UserComponent,
        UserHomeComponent,
        UserProfileComponent,
        LevelDirective,
        UserProfileRouterComponent,
        UserCollectionsComponent,
        CollectionComponent,
    ],
    imports: [
        CommonModule,
        UserRoutingModule,
        SharedModule,
        StoreModule.forFeature(carouselFeatureKey, carouselReducer),
        FontAwesomeModule,
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