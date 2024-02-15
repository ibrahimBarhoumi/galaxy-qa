import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CreateDatatestCaseComponent } from './create-datatestcase/create-datatestcase.component';
import { ListDatatestCaseComponent } from './list-datatestcase/list-datatestcase.component';

const routes: Routes = [
  {
    path: 'list',
    component: ListDatatestCaseComponent,
    data: {
      title: 'List Data Test Case',
    },
  },
  {
    path: 'add',
    component: CreateDatatestCaseComponent,
    data: {
      title: 'Add Data Test Case',
    },
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DataTestCaseRoutingModule {}
