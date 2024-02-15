import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ListAppVersionMLComponent } from './list-appversionml/list-appversionml.component';

const routes: Routes = [
  {
    path: 'list',
    component: ListAppVersionMLComponent,
    data: {
      title: 'List App version ml'
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AppVersionMLRoutingModule {}
