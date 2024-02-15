import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ListTestMLComponent } from './list-testml/list-testml.component';

const routes: Routes = [
  {
    path: 'list',
    component: ListTestMLComponent,
    data: {
      title: 'List test ml'
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TestMLRoutingModule {}
