import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

// Import Containers
import { LayoutComponent } from './shared/layout/layout.component';

import { LoginComponent } from './views/login/login.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'page',
    pathMatch: 'full',
  },
  {
    path: 'login',
    component: LoginComponent,
    data: {
      title: 'Login Page',
    },
  },
  {
    path: '',
    component: LayoutComponent,
    data: {
      title: 'Home',
    },
    children: [
      {
        path: 'campaign',
        loadChildren: () =>
          import('./views/campaign/campaign.module').then(
            (m) => m.CampaignModule
          ),
      },
      {
        path: 'application',
        loadChildren: () =>
          import('./views/application/application.module').then(
            (m) => m.ApplicationModule
          ),
      },
      {
        path: 'scenarios',
        loadChildren: () =>
          import('./views/scenario/scenario.module').then(
            (m) => m.ScenarioModule
          ),
      },
      {
        path: 'environment',
        loadChildren: () =>
          import('./views/environment/environment.module').then(
            (m) => m.EnvironmentModule
          ),
      },
      {
        path: 'functionalconfigs',
        loadChildren: () =>
          import('./views/functionalconfig/functionalconfig.module').then(
            (m) => m.FunctionalConfigModule
          ),
      },
      {
        path: 'tests',
        loadChildren: () =>
          import('./views/test/test.module').then(
            (m) => m.TestModule
          ),
      },
      {
        path: 'datatestcase',
        loadChildren: () =>
          import('./views/datatestcase/datatestcase.module').then(
            (m) => m.DataTestCaseModule
          ),
      },
    ],
  },
  { path: '**', component: LayoutComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
  exports: [RouterModule],
})
export class AppRoutingModule {}
