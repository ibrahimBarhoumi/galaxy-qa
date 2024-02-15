import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ListScenarioMLComponent } from './list-scenarionml/list-scenarioml.component';


const routes: Routes = [
  {
    path: 'list',
    component: ListScenarioMLComponent,
    data: {
      title: 'List scenario Ml'
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ScenarioMLRoutingModule {}
