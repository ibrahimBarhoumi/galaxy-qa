import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ListCampaignComponent } from './list-campaign/list-campaign.component';
import {CreateCampaignComponent} from './create-campaign/create-campaign.component';

const routes: Routes = [
  {
    path: 'list',
    component: ListCampaignComponent,
    data: {
      title: 'List campaigns'
    }
  },
  {
    path: 'add',
    component: CreateCampaignComponent,
    data: {
      title: 'Add campaign'
    }
  },
  {
    path: 'edit/:id',
    component: CreateCampaignComponent,
    data: {
      title: 'Edit campaign'
    }
  },
  {
    path: 'duplicate/:id',
    component: CreateCampaignComponent,
    data: {
      title: 'Duplicate campaign'
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CampaignRoutingModule {}
