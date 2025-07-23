// src/app/app-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RaceListComponent } from './features/races/race-list/race-list.component';
import { ApplicationListComponent } from './features/applications/application-list/application-list.component';
import { AuthGuard } from './core/authgard.service';
import { LoginComponent } from './login/login.component';
import { DashboardComponent } from './features/dashbord/dashbord.component';

const routes: Routes = [
  { 
    path: 'login', 
    component: LoginComponent 
  },
  { 
    path: 'dashboard', 
    component: DashboardComponent,
    canActivate: [AuthGuard]
  },
  { 
    path: 'races', 
    component: RaceListComponent,
    canActivate: [AuthGuard]
  },
  { 
    path: 'applications', 
    component: ApplicationListComponent,
    canActivate: [AuthGuard]
  },
  { 
    path: '', 
    redirectTo: '/dashboard',  
    pathMatch: 'full' 
  },
  { 
    path: '**', 
    redirectTo: '/dashboard'   
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}