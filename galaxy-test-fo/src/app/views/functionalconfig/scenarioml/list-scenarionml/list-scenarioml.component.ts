import { Component, OnInit } from '@angular/core';

import { Pageable } from '../../../../shared/models/pageable';
import { LazyLoadEvent } from 'primeng/api';
import { PageOptions } from '../../../../shared/config/page/pageOptions';
import { FilterOptions } from '../../../../shared/config/page/filterOptions';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { Observable } from 'rxjs';
import { CreateScenarioMLComponent } from '../create-scenarioml/create-scenarioml.component';
import { Scenario } from '../../../../shared/models/scenario';
import { ScenarioService } from '../../../scenario/scenario.service';

@Component({
  templateUrl: 'list-scenarioml.component.html',
  styleUrls: ['./list-scenarioml.component.scss'],
})
export class ListScenarioMLComponent implements OnInit {

  bsModalRef: BsModalRef;

  scenarios: Scenario[];
  data: Pageable<Scenario>;
  language:any;
  cols: any[];
  dataBuffer: Scenario[] = [];
  listLang = ['fr', 'en'];

  constructor(
    private scenarioService: ScenarioService,
    private modalService: BsModalService
  ) {}

  ngOnInit(): void {
    this.cols = [
      { field: 'code', header: 'SCENARIO.LIST.COLS.CODE', type: 'text' },
      {
        field: 'label',
        header: 'SCENARIO.LIST.COLS.LABEL',
        type: 'text',
        hasConfiguration: true,
      },
      {
        field: 'comment',
        header: 'SCENARIO.LIST.COLS.COMMENT',
        type: 'text',
        hasConfiguration: true,
      },
      {
        field: 'languageCode',
        header: 'SCENARIO.LIST.COLS.LANGUAGE',
        type: 'text',
        hasConfiguration: true,
        style: 'flag',
      },
    ];

  }

  openModalWithComponent(scenario?: Scenario, languageCode?: string) {

    this.bsModalRef = this.modalService.show(CreateScenarioMLComponent, {initialState: {scenario, languageCode}});
    this.bsModalRef.content.event.subscribe((res: Observable<Scenario>) => {
    res.subscribe(() => {
        this.bsModalRef.hide();
        this.list();
      });

   });
  }

  list(event?: LazyLoadEvent): void {
    this.data = null;
    const state = JSON.parse(localStorage.getItem('stateScenariomlDataTable'));
    const page =
      Math.floor(event?.first / event?.rows) + 1 ||
      Math.floor(state?.first / state?.rows) + 1 ||
      1;
    const size = event?.rows || state?.rows || 10;
    const filters = event?.filters || state?.filters || {};
    this.scenarioService
      .list(new PageOptions(page - 1, size), new FilterOptions(filters))
      .subscribe((data) => {

        this.data = data;

      });
  }

  delete(id: string , languageCode: string , scenario: Scenario ) {
    this.scenarioService.updateConfigurationMl(id, languageCode, scenario).subscribe((data) => {
      this.list();
    });
  }

 
  clearSelect(){
    this.language = [];
  }
}
