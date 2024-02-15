import { Component, OnInit } from '@angular/core';
import { Application } from '../../../../shared/models/application';
import { Pageable } from '../../../../shared/models/pageable';
import { LazyLoadEvent } from 'primeng/api';
import { PageOptions } from '../../../../shared/config/page/pageOptions';
import { FilterOptions } from '../../../../shared/config/page/filterOptions';
import { Router } from '@angular/router';
import { ApplicationService } from '../../../application/application.service';
import { CreateApplicationMLComponent } from '../create-applicationml/create-applicationml.component';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { Observable } from 'rxjs';

@Component({
  templateUrl: 'list-applicationml.component.html',
  styleUrls: ['./list-applicationml.component.scss'],
})
export class ListApplicationMLComponent implements OnInit {

  bsModalRef: BsModalRef;

  applications: Application[];
  data: Pageable<Application>;
  language:any;
  cols: any[];
  dataBuffer: Application[] = [];
  listLang = ['fr', 'en'];

  constructor(
    private applicationService: ApplicationService,
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

  openModalWithComponent(application?: Application, languageCode?: string) {

    this.bsModalRef = this.modalService.show(CreateApplicationMLComponent, {initialState: {application, languageCode}});
    this.bsModalRef.content.event.subscribe((res: Observable<Application>) => {
    res.subscribe(() => {
        this.bsModalRef.hide();
        this.list();
      });

   });
  }

  list(event?: LazyLoadEvent): void {
    this.data = null;
    const state = JSON.parse(localStorage.getItem('stateApplicationmlDataTable'));
    const page =
      Math.floor(event?.first / event?.rows) + 1 ||
      Math.floor(state?.first / state?.rows) + 1 ||
      1;
    const size = event?.rows || state?.rows || 10;
    const filters = event?.filters || state?.filters || {};
    this.applicationService
      .list(new PageOptions(page - 1, size), new FilterOptions(filters))
      .subscribe((data) => {

        this.data = data;

      });
  }

  delete(id: string , languageCode: string , application: Application ) {
    this.applicationService.updateConfigurationMl(id, languageCode, application).subscribe((data) => {
      this.list();
    });
  }

  onCreateNew() {
    this.router.navigate(['/application/add']);
  }
  clearSelect(){
    this.language = [];
  }
}
