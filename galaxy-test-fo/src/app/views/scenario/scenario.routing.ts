import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CreateScenarioComponent } from './create-scenario/create-scenario.component';
import { ListScenarioComponent } from './list-scenario/scenario-list.component';

const routes: Routes = [
  {
    path: 'list',
    component: ListScenarioComponent,
    data: {
      title: 'List scenario',
    },
  },
  {
    path: 'add',
    component: CreateScenarioComponent,
    data: {
      title: 'Add scenario',
    },
  },
  {
    path: 'duplicate/:id',
    component: CreateScenarioComponent,
    data: {
      title: 'Duplicate scenario',
    },
  },
  {
    path: 'edit/:id',
    component: CreateScenarioComponent,
    data: {
      title: 'Edit Scenario',
    },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ScenarioRoutingModule {}
