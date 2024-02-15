import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ListVersionComponent } from './list-version/list-version.component';

const routes: Routes = [
  {
    path: 'list',
    component: ListVersionComponent,
    data: {
      title: 'version'
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class VersionRoutingModule {}
