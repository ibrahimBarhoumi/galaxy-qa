import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ListApplicationMLComponent } from './list-applicationml/list-applicationml.component';

const routes: Routes = [
  {
    path: 'list',
    component: ListApplicationMLComponent,
    data: {
      title: 'List appliction ml'
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ApplicationMLRoutingModule {}