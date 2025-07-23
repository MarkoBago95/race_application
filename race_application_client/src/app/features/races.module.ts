import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RaceListComponent } from './races/race-list/race-list.component';
import { ApplicationFormComponent } from './applications/application-form/application-form.component';
import { ApplicationListComponent } from './applications/application-list/application-list.component';
import { RaceFormComponent } from './races/race-form/race-form.component';


@NgModule({
  declarations: [
    RaceListComponent,
    RaceFormComponent,
    ApplicationFormComponent,
    ApplicationListComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule
  ],
  exports: [
    RaceListComponent,
    RaceFormComponent,
    ApplicationFormComponent,
    ApplicationListComponent
  ]
})
export class RaceModule {}
