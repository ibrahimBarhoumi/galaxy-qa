import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { LazyLoadEvent } from 'primeng/api';
import { FilterOptions } from '../../../shared/config/page/filterOptions';
import { PageOptions } from '../../../shared/config/page/pageOptions';
import { ScenarioStatus2Label } from '../../../shared/enums/scenarioStatus.enum';
import { ScenarioType2Label } from '../../../shared/enums/scenarioType.enum';
import { Test_status_2Label } from '../../../shared/enums/testStatus.enum';
import { Test_type_2Label } from '../../../shared/enums/testType.enum';
import { ConfigurationML } from '../../../shared/models/configurationML';
import { Pageable } from '../../../shared/models/pageable';
import { Scenario } from '../../../shared/models/scenario';
import { ScenarioTest } from '../../../shared/models/scenarioTest';
import { ScenarioService } from '../scenario.service';
import { ScenarioTestService } from '../scenarioTest.service';

@Component({
  selector: 'app-scenario',
  templateUrl: './scenario-list.component.html',
  styleUrls: ['./scenario-list.component.scss'],
})
export class ListScenarioComponent implements OnInit {
  scenario: Scenario[];
  data: Pageable<Scenario>;
  cols: any[];
  public ScenarioStatus2Label = ScenarioStatus2Label;
  public ScenarioType2Label = ScenarioType2Label;
  public TestType2Label = Test_type_2Label;
  test: ScenarioTest[];
  hasVersions = [];
  hasCompagne = [];
  public TestStatus2Label = Test_status_2Label;
  constructor(
    private scenarioService: ScenarioService,
    private cookiesService: CookieService,
    private router: Router,
    private scenarioTestService: ScenarioTestService
  ) {}

  ngOnInit(): void {
    this.cols = [
      {
        field: 'code',
        header: 'SCENARIO.LIST.COLS.CODE',
        type: 'text',
        hasConfiguration: false,
      },
      {
        field: 'scenarioType',
        header: 'SCENARIO.LIST.COLS.TYPE',
        type: 'text',
        hasConfiguration: false,
        style: 'badge',
      },
      {
        field: 'scenarioStatus',
        header: 'SCENARIO.LIST.COLS.STATUS',
        type: 'text',
        hasConfiguration: false,
        style: 'badge',
      },
      {
        field: 'comment',
        header: 'SCENARIO.LIST.COLS.COMMENT',
        type: 'text',
        hasConfiguration: true,
      },
      {
        field: 'label',
        header: 'SCENARIO.LIST.COLS.LABEL',
        type: 'text',
        hasConfiguration: true,
      },
    ];
  }
  list(event?: LazyLoadEvent): void {
    this.data = null;
    const state = JSON.parse(localStorage.getItem('stateApplicationDataTable'));
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
  selectConfiguration(configurationMLS: ConfigurationML[]) {
    return (
      configurationMLS.find(
        (x) => x.languageCode === this.cookiesService.get('lang')
      ) || {}
    );
  }

  delete(id: string) {
    this.scenarioService.delete(id).subscribe(
      () => {
        this.list();
      },
      (err) => {
        if (err.error.message === 'CAMPAIGN_HAS_SCENARIO') {
          this.hasCompagne[id] = true;
        }
      }
    );
  }

  onCreateNew() {
    this.router.navigate(['/scenarios/add']);
  }

  onDuplicate(index: number, scenario: Scenario) {
    this.router.navigate(['/scenarios/duplicate/' + scenario.id]);
  }

  getScenarioTestByScenarioId(id: string) {
    return this.scenarioTestService
      .getScenarioTestByScenarioId(id)
      .subscribe((data5) => {
        this.test = data5.content;
      });
  }
  onEdit(scenario: Scenario): void {
    this.router.navigate(['/scenarios/edit/' + scenario.id]);
  }
}
