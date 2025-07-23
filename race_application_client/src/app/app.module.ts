import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthInterceptor } from './core/auth.intercepter';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RaceModule } from './features/races.module';
import { ApplicationService } from './core/application.service';
import { LoginComponent } from './login/login.component';
import { DashboardComponent } from './features/dashbord/dashbord.component';

@NgModule({
  declarations: [
    AppComponent,LoginComponent,DashboardComponent ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    CommonModule,
    HttpClientModule,
    RaceModule],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }, ApplicationService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
