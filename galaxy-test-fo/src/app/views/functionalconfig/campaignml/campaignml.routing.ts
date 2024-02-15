import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ListCampaignMLComponent } from './list-campaignml/list-campaignml.component';

const routes: Routes = [
  {
    path: 'list',
    component: ListCampaignMLComponent,
    data: {
      title: 'List campaign ml'
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CampaignMLRoutingModule {}
