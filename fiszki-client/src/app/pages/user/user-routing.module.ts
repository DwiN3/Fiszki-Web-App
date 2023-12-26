import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserHomeComponent } from './pages/user-home/user-home.component';
import { UserSettingsComponent } from './pages/user-settings/user-settings.component';
import { UserComponent } from './user.component';

const routes : Routes = 
[
    { 
        path: 'user', 
        component: UserComponent, 
        children: [
            {
                path: '',
                component: UserHomeComponent,
            },
            {
                path: 'settings',
                component: UserSettingsComponent,
            }
        ] 
    },
    
]

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})

export class UserRoutingModule{}