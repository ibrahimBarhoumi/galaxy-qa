import { Component, Input, OnInit } from '@angular/core';
import { ApplicationVersion } from '../../../../shared/models/application-version';
import { Pageable } from '../../../../shared/models/pageable';
import { LazyLoadEvent } from 'primeng/api';
import { PageOptions } from '../../../../shared/config/page/pageOptions';
import { FilterOptions } from '../../../../shared/config/page/filterOptions';
import { VersionService } from '../version.service';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import {
  ApplicationStatus,
  ApplicationStatus2Label,
} from '../../../../shared/enums/applicationStatus.enum';
import { ModalVersionComponent } from '../modal-version/modal-version.component';
import { Observable } from 'rxjs';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { ConfigurationML } from '../../../../shared/models/configurationML';
import { ScenarioStatus, ScenarioStatus2Label } from '../../../../shared/enums/scenarioStatus.enum';
import { ScenarioType, ScenarioType2Label } from '../../../../shared/enums/scenarioType.enum';
import { Scenario } from '../../../../shared/models/scenario';
import { ScenarioVersion } from '../../../../shared/models/scenarioVersion';

@Component({
  templateUrl: 'list-version.component.html',
  styleUrls: ['./list-version.component.scss'],
})
export class ListVersionComponent implements OnInit {
  bsModalRef: BsModalRef;

  versions: ApplicationVersion[];
  data: Pageable<ApplicationVersion>;

  cols: any[];
  dataBuffer: ApplicationVersion[] = [];
  public ScenarioStatus2Label = ScenarioStatus2Label;
  public ScenarioType2Label = ScenarioType2Label;
  _selectedColumns: any[];
   colsSenario: any[];
  versionForm: FormGroup;
  configurationMLS: FormGroup;
  scenarios:Scenario[] = [];
  scenarioVersions:ScenarioVersion[] = [];
  hasScenarios = [];
  public ApplicationStatus2Label = ApplicationStatus2Label;

  constructor(
    private versionService: VersionService,
    private modalService: BsModalService,
    private formBuilder: FormBuilder,
    private translateService: TranslateService
  ) {}

  ngOnInit(): void {
    this.cols = [
      { field: 'code', header: 'VERSION.LIST.COLS.CODE', type: 'text' },
      {
        field: 'status',
        header: 'VERSION.LIST.COLS.STATUS',
        type: 'text',
        style: 'badge',
      },
      {
        field: 'host',
        header: 'VERSION.LIST.COLS.HOST',
        type: 'text',
      },
      {
        field: 'url',
        header: 'VERSION.LIST.COLS.URL',
        type: 'text',
      },
      {
        field: 'port',
        header: 'VERSION.LIST.COLS.PORT',
        type: 'text',
      },
      {
        field: 'username',
        header: 'VERSION.LIST.COLS.USERNAME',
        type: 'text',
      },
      {
        field: 'password',
        header: 'VERSION.LIST.COLS.PASSWORD',
        type: 'text',
      },
      {
        field: 'dbHost',
        header: 'VERSION.LIST.COLS.DBHOST',
        type: 'text',
      },
      {
        field: 'dbPort',
        header: 'VERSION.LIST.COLS.DBPORT',
        type: 'text',
      },
      {
        field: 'dbUsername',
        header: 'VERSION.LIST.COLS.DBUSERNAME',
        type: 'text',
      },
      {
        field: 'dbPassword',
        header: 'VERSION.LIST.COLS.DBPASSWORD',
        type: 'text',
      },
      {
        field: 'label',
        header: 'VERSION.LIST.COLS.LABEL',
        type: 'text',
        hasConfiguration: true,
      },
      {
        field: 'comment',
        header: 'VERSION.LIST.COLS.COMMENT',
        type: 'text',
        hasConfiguration: true,
      },
    ];
    this._selectedColumns = [
      { field: 'code', header: 'VERSION.LIST.COLS.CODE', type: 'text' },
      {
        field: 'status',
        header: 'VERSION.LIST.COLS.STATUS',
        type: 'text',
        style: 'badge',
      },
      {
        field: 'host',
        header: 'VERSION.LIST.COLS.HOST',
        type: 'text',
      },
      {
        field: 'url',
        header: 'VERSION.LIST.COLS.URL',
        type: 'text',
      },
      {
        field: 'label',
        header: 'VERSION.LIST.COLS.LABEL',
        type: 'text',
        hasConfiguration: true,
      },
      {
        field: 'comment',
        header: 'VERSION.LIST.COLS.COMMENT',
        type: 'text',
        hasConfiguration: true,
      },
    ];
    let state = JSON.parse(localStorage.getItem("stateVersionDataTable"));
    if(state){
    delete state?.expandedRowKeys;
    localStorage.setItem("stateVersionDataTable",JSON.stringify(state));
    }
  }

  createConfigurationMLS(configurationML?: ConfigurationML): FormGroup {
    this.configurationMLS = this.formBuilder.group({
      label: [configurationML?.label || '', Validators.required],
      comment: [configurationML?.comment || '', Validators.required],
      languageCode: this.translateService.currentLang,
    });
    return this.configurationMLS;
  }

  list(event?: LazyLoadEvent): void {
    this.data = null;
    const state = JSON.parse(localStorage.getItem('stateVersionDataTable'));
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

  selectConfiguration(configurationMLS: ConfigurationML[]) {
    return (
      configurationMLS.find(
        (x) => x.languageCode === this.translateService.currentLang
      ) || {}
    );
  }

  openModalWithComponent(applicationVersion?: ApplicationVersion) {
    let configurationML;

    if (applicationVersion) {
      configurationML = applicationVersion.configurationMLS.find(
        (conf) => conf.languageCode === this.translateService.currentLang
      );
    }

    this.versionForm = this.formBuilder.group({
      code: [applicationVersion?.id ? applicationVersion.code : '', Validators.required],
      status: [applicationVersion?.status === ApplicationStatus.ENDED ? true : false || ''],
      host: [applicationVersion?.host || '', Validators.required],
      url: [applicationVersion?.url || '', Validators.required],
      port: [applicationVersion?.port || '', Validators.required],
      username: [applicationVersion?.username || '', Validators.required],
      password: [applicationVersion?.password || '', Validators.required],
      dbHost: [applicationVersion?.dbHost || '', Validators.required],
      dbPort: [applicationVersion?.dbPort || '', Validators.required],
      application: [applicationVersion?.application || '', Validators.required],
      dbUsername: [applicationVersion?.dbUsername || '', Validators.required],
      dbPassword: [applicationVersion?.dbPassword || '', Validators.required],
      configurationMLS: this.formBuilder.array([
        this.createConfigurationMLS(configurationML),
      ]),
    });
    this.bsModalRef = this.modalService.show(ModalVersionComponent, {
      initialState: { versionForm: this.versionForm, applicationVersion },
    });
    this.bsModalRef.content.event.subscribe(
      (res: Observable<ApplicationStatus>) => {
        res.subscribe(
          () => {
            this.bsModalRef.hide();
            this.list();
          },
          (data) => {
            if (data.error.message === 'APPLICATIONVERSION_EXIST') {
              this.versionForm.controls['code'].setErrors({
                error: 'code exist',
              });
            }
            if (data.error.message === 'APPLICATIONVERSION_EXIST_DELETED') {
              this.versionForm.controls['code'].setErrors({
                error: 'code exist in deleted application version',
              });
            }
          }
        );
      }
    );
  }


  delete(id: string) {
    this.versionService
    .delete(id).subscribe((data) => {
      this.list();
    },(error)=>{
      if(error.error.message == "VERSION_HAS_SCENARIOS"){
        this.hasScenarios[id] = true;
      }
    });
  }

  @Input() get selectedColumns(): any[] {
    return this._selectedColumns;
  }

  set selectedColumns(val: any[]) {
    this._selectedColumns = this.cols.filter((col) => val.includes(col));
  }

  getScenarios(id:string){
    this.scenarios = [];
    return this.versionService.getScenarioByVersionId(id).subscribe(data=>{
      this.scenarioVersions = data.content;
      this.scenarioVersions.forEach(scenarioVersion=>{this.scenarios.push(scenarioVersion.scenario)})

    });
  }
}
