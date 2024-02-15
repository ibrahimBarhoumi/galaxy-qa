import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ListTestComponent } from './list-test/list-test.component';

const routes: Routes = [
  {
    path: 'list',
    component: ListTestComponent,
    data: {
      title: 'test'
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TestRoutingModule {}
