import { Component, OnInit } from '@angular/core';
import { Pageable } from '../../../../shared/models/pageable';
import { LazyLoadEvent } from 'primeng/api';
import { PageOptions } from '../../../../shared/config/page/pageOptions';
import { FilterOptions } from '../../../../shared/config/page/filterOptions';
import { Router } from '@angular/router';
import { ModalAppVersionMLComponent } from '../modal-appversionml/modal-appversionml.component';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { Observable } from 'rxjs';
import { VersionService } from '../../../application/version/version.service';
import { ApplicationVersion } from '../../../../shared/models/application-version';

@Component({
  templateUrl: 'list-appversionml.component.html',
  styleUrls: ['./list-appversionml.component.scss'],
})
export class ListAppVersionMLComponent implements OnInit {

  bsModalRef: BsModalRef;

  applicationVersions: ApplicationVersion[];
  data: Pageable<ApplicationVersion>;
  language:any;
  cols: any[];
  dataBuffer: ApplicationVersion[] = [];
  listLang = ['fr', 'en'];

  constructor(
    private versionService: VersionService,
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

  openModalWithComponent(applicationVersion?: ApplicationVersion, languageCode?: string) {

    this.bsModalRef = this.modalService.show(ModalAppVersionMLComponent, {initialState: {applicationVersion, languageCode}});
    this.bsModalRef.content.event.subscribe((res: Observable<ApplicationVersion>) => {
    res.subscribe(() => {
        this.bsModalRef.hide();
        this.list();
      });

   });
  }

  list(event?: LazyLoadEvent): void {
    this.data = null;
    const state = JSON.parse(localStorage.getItem('stateAppVerMLDataTable'));
    const page =
      Math.floor(event?.first / event?.rows) + 1 ||
      Math.floor(state?.first / state?.rows) + 1 ||
      1;
    const size = event?.rows || state?.rows || 10;
    const filters = event?.filters || state?.filters || {};
    this.versionService
      .list(new PageOptions(page - 1, size), new FilterOptions(filters))
      .subscribe((data) => {

        this.data = data;

      });
  }

  delete(id: string , languageCode: string , applicationVersion: ApplicationVersion ) {
    this.versionService.updateConfigurationMl(id, languageCode, applicationVersion).subscribe((data) => {
      this.list();
    });
  }

  clearSelect(){
    this.language = [];
  }
}
