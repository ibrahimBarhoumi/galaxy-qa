import { Component, OnInit } from '@angular/core';
import { Campaign } from '../../../../shared/models/campaign';
import { Pageable } from '../../../../shared/models/pageable';
import { LazyLoadEvent } from 'primeng/api';
import { PageOptions } from '../../../../shared/config/page/pageOptions';
import { FilterOptions } from '../../../../shared/config/page/filterOptions';
import { Router } from '@angular/router';
import { CampaignService } from '../../../campaign/campaign.service';
import { CreateCampaignMLComponent } from '../create-campaignml/create-campaignml.component';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { Observable } from 'rxjs';

@Component({
  templateUrl: 'list-campaignml.component.html',
  styleUrls: ['./list-campaignml.component.scss'],
})
export class ListCampaignMLComponent implements OnInit {

  bsModalRef: BsModalRef;

  campaigns: Campaign[];
  data: Pageable<Campaign>;
  language:any;
  cols: any[];
  dataBuffer: Campaign[] = [];
  listLang = ['fr', 'en'];

  constructor(
    private campaignService: CampaignService,
    private router: Router,
    private modalService: BsModalService
  ) {}

  ngOnInit(): void {
    this.cols = [
      { field: 'code', header: 'CAMPAIGNML.LIST.COLS.CODE', type: 'text' },
      {
        field: 'label',
        header: 'CAMPAIGNML.LIST.COLS.LABEL',
        type: 'text',
        hasConfiguration: true,
      },
      {
        field: 'comment',
        header: 'CAMPAIGNML.LIST.COLS.COMMENT',
        type: 'text',
        hasConfiguration: true,
      },
      {
        field: 'languageCode',
        header: 'CAMPAIGNML.LIST.COLS.LANGUAGE',
        type: 'text',
        hasConfiguration: true,
        style: 'flag',
      },
    ];

  }

  openModalWithComponent(campaign?: Campaign, languageCode?: string) {

    this.bsModalRef = this.modalService.show(CreateCampaignMLComponent, {initialState: {campaign, languageCode}});
    this.bsModalRef.content.event.subscribe((res: Observable<Campaign>) => {
    res.subscribe(() => {
        this.bsModalRef.hide();
        this.list();
      });

   });
  }

  list(event?: LazyLoadEvent): void {
    this.data = null;
    const state = JSON.parse(localStorage.getItem('stateCampaignmlDataTable'));
    const page =
      Math.floor(event?.first / event?.rows) + 1 ||
      Math.floor(state?.first / state?.rows) + 1 ||
      1;
    const size = event?.rows || state?.rows || 10;
    const filters = event?.filters || state?.filters || {};
    this.campaignService
      .list(new PageOptions(page - 1, size), new FilterOptions(filters))
      .subscribe((data) => {

        this.data = data;

      });
  }

  delete(id: string , languageCode: string , campaign: Campaign ) {
    this.campaignService.updateConfigurationMl(id, languageCode, campaign).subscribe((data) => {
      this.list();
    });
  }

  onCreateNew() {
    this.router.navigate(['/campaign/add']);
  }
  clearSelect(){
    this.language = [];
  }
}
