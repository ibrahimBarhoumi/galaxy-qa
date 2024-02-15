import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EnvironmentComponent } from './environment.component';
import { EnvironmentRoutingModule } from './environment-routing.module';



@NgModule({
  declarations: [EnvironmentComponent],
  imports: [
    EnvironmentRoutingModule
  ]
})
export class EnvironmentModule { }
