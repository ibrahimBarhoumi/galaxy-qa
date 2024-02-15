import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CreateApplicationComponent } from './create-application/create-application.component';
import { ListApplicationComponent } from './list-application/list-application.component';

const routes: Routes = [
  {
    path: 'versions',
    loadChildren: () =>
      import('./version/version.module').then(
        (m) => m.VersionModule
      ),
  },
  {
    path: 'list',
    component: ListApplicationComponent,
    data: {
      title: 'List Application'
    }
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ApplicationRoutingModule {}
