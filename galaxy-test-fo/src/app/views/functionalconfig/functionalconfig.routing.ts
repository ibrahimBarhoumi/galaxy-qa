import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  {
    path: 'appversionml',
    loadChildren: () =>
      import('./appversionml/appversionml.module').then(
        (m) => m.AppVersionMLModule
      ),
  },
  {
  path: 'campaignesml',
  loadChildren: () =>
    import('./campaignml/campaignml.module').then(
      (m) => m.CampaignMLModule
    ),
  },
  {
    path: 'applicationsml',
    loadChildren: () =>
      import('./applicationml/applicationml.module').then(
        (m) => m.ApplicationMLModule
      ),
  },
  {
    path: 'testml',
    loadChildren: () =>
      import('./testml/testml.module').then(
        (m) => m.TestMLModule
      )
  },
  {
    path: 'scenarioml',
    loadChildren: () =>
      import('.//scenarioml/scenarioml.module').then(
        (m) => m.ScenarioMLModule
      ),
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ApplicationRoutingModule {}
