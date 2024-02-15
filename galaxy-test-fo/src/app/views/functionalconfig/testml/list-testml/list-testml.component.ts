import { Component, OnInit } from '@angular/core';
import { Pageable } from '../../../../shared/models/pageable';
import { LazyLoadEvent } from 'primeng/api';
import { PageOptions } from '../../../../shared/config/page/pageOptions';
import { FilterOptions } from '../../../../shared/config/page/filterOptions';
import { Router } from '@angular/router';
import { ModalTestMLComponent } from '../modal-testml/modal-testml.component';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { Observable } from 'rxjs';
import { TestService } from '../../../test/test.service';
import { Test } from '../../../../shared/models/test';

@Component({
  templateUrl: 'list-testml.component.html',
  styleUrls: ['./list-testml.component.scss'],
})
export class ListTestMLComponent implements OnInit {

  bsModalRef: BsModalRef;

  tests: Test[];
  data: Pageable<Test>;
  language:any;
  cols: any[];
  dataBuffer: Test[] = [];
  listLang = ['fr', 'en'];

  constructor(
    private testService: TestService,
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

  openModalWithComponent(test?: Test, languageCode?: string) {

    this.bsModalRef = this.modalService.show(ModalTestMLComponent, {initialState: {test, languageCode}});
    this.bsModalRef.content.event.subscribe((res: Observable<Test>) => {
    res.subscribe(() => {
        this.bsModalRef.hide();
        this.list();
      });

   });
  }

  list(event?: LazyLoadEvent): void {
    this.data = null;
    const state = JSON.parse(localStorage.getItem('stateTestMLDataTable'));
    const page =
      Math.floor(event?.first / event?.rows) + 1 ||
      Math.floor(state?.first / state?.rows) + 1 ||
      1;
    const size = event?.rows || state?.rows || 10;
    const filters = event?.filters || state?.filters || {};
    this.testService
      .list(new PageOptions(page - 1, size), new FilterOptions(filters))
      .subscribe((data) => {

        this.data = data;

      });
  }

  delete(id: string , languageCode: string , test: Test ) {
    this.testService.updateConfigurationMl(id, languageCode, test).subscribe((data) => {
      this.list();
    });
  }

  clearSelect(){
    this.language = [];
  }
}
