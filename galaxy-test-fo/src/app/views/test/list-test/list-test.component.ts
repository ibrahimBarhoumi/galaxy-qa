import { Component, Input, OnInit } from '@angular/core';
import { Test } from '../../../shared/models/test';
import { Pageable } from '../../../shared/models/pageable';
import { LazyLoadEvent } from 'primeng/api';
import { PageOptions } from '../../../shared/config/page/pageOptions';
import { FilterOptions } from '../../../shared/config/page/filterOptions';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import {
  TestType,
  Test_type_2Label,
} from '../../../shared/enums/testType.enum';
import {
  TestStatus,
  Test_status_2Label,
} from '../../../shared/enums/testStatus.enum';
import { Observable } from 'rxjs';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { ConfigurationML } from '../../../shared/models/configurationML';
import { TestService } from '../test.service';
import { Application } from '../../../shared/models/application';
import { ApplicationVersion } from '../../../shared/models/application-version';
import { ApplicationService } from '../../application/application.service';

import { ModalTestComponent } from '../modal-test/modal-test.component';
import { ModalPlaytestComponent } from '../modal-playtest/modal-playtest.component';

@Component({
  templateUrl: 'list-test.component.html',
  styleUrls: ['./list-test.component.scss'],
})
export class ListTestComponent implements OnInit {
  tests: Test[];

  data: Pageable<Test>;

  applications: Pageable<Application>;

  testForm: FormGroup;
  executionForm: FormGroup;
  bsModalRef: BsModalRef;
  cols: any[];

  loading = false;
  applicationsBuffer: Application[];
  term = {};

  page = 0;

  public Test_type_2Label = Test_type_2Label;
  public Test_status_2Label = Test_status_2Label;

  configurationMLS: FormGroup;
  hasScenarios = [];

  constructor(
    private applicationService: ApplicationService,
    private testService: TestService,
    private translateService: TranslateService,
    private formBuilder: FormBuilder,
    private modalService: BsModalService
  ) {}

  ngOnInit(): void {
    this.cols = [
      { field: 'code', header: 'TEST.LIST.COLS.CODE', type: 'text' },
      {
        field: 'status',
        header: 'TEST.LIST.COLS.STATUS',
        type: 'text',
        style: 'badge',
      },
      {
        field: 'type',
        header: 'TEST.LIST.COLS.TYPE',
        type: 'text',
        style: 'badge',
      },

      {
        field: 'application',
        header: 'TEST.LIST.COLS.APP',
        type: 'select',
        isParent: true,
      },
      {
        field: 'label',
        header: 'TEST.LIST.COLS.LABEL',
        type: 'text',
        hasConfiguration: true,
      },
      {
        field: 'comment',
        header: 'TEST.LIST.COLS.COMMENT',
        type: 'text',
        hasConfiguration: true,
      },
    ];
    this.loadFirst().subscribe((data) => {
      this.setApplications(data);
    });
  }

  list(event?: LazyLoadEvent): void {
    this.data = null;
    const state = JSON.parse(localStorage.getItem('stateTestDataTable'));
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

  edit(test?: Test) {
    const copy = { ...test };
    this.openModalWithComponent(test, copy);
  }

  duplicate(test?: Test) {
    const copy = { ...test };
    copy.id = null;
    this.openModalWithComponent(test, copy);
  }
  openModalWithComponent(test?: Test, copy?: Test) {
    let configurationML;

    if (test) {
      configurationML = test.configurationMLS.find(
        (conf) => conf.languageCode === this.translateService.currentLang
      );
      this.testService
        .getVersionsByTestId(test.id)
        .subscribe((data) => (test.testVersions = data));
    }

    this.testForm = this.formBuilder.group({
      code: [copy?.id ? test.code : '', Validators.required],
      status: [test?.status, Validators.required],
      type: [test?.type || '', Validators.required],
      application: [test?.application || '', Validators.required],
      testVersions: test?.testVersions || '',
      configurationMLS: this.formBuilder.array([
        this.createConfigurationMLS(configurationML),
      ]),
    });
    this.bsModalRef = this.modalService.show(ModalTestComponent, {
      initialState: { testForm: this.testForm, test, copy },
    });
    this.bsModalRef.content.event.subscribe((res: Observable<Test>) => {
      res.subscribe(
        () => {
          this.bsModalRef.hide();
          this.list();
        },
        (data) => {
          if (data.error.message === 'TEST_CODE_EXISTS') {
            this.testForm.controls['code'].setErrors({
              error: 'code exist',
            });
          } else if (data.error.message === 'TEST_CODE_DELETED') {
            this.testForm.controls['code'].setErrors({
              error: 'code exist in deleted test',
            });
          } else {
            this.testForm.controls['testVersions'].setErrors({
              error: data.error.message,
            });
          }
        }
      );
    });
  }

  openModalForExecution(test?: Test) {
    this.executionForm = this.formBuilder.group({
      code: [test.code || '', Validators.required],
      application: [],
      testVersions:  [],

    });
    this.bsModalRef = this.modalService.show(ModalPlaytestComponent, {
      initialState: { executionForm: this.executionForm, test},
    });
    this.bsModalRef.content.event.subscribe((res : any) => {
      res.subscribe(
        () => {
          console.log("hi")
          this.bsModalRef.hide();
          this.list()
        } 
      );
    });
  }

  createConfigurationMLS(configurationML?: ConfigurationML): FormGroup {
    this.configurationMLS = this.formBuilder.group({
      label: [configurationML?.label || '', Validators.required],
      comment: [configurationML?.comment || '', Validators.required],
      languageCode: this.translateService.currentLang,
    });
    return this.configurationMLS;
  }

  selectConfiguration(configurationMLS: ConfigurationML[]) {
    return (
      configurationMLS?.find(
        (x) => x.languageCode === this.translateService.currentLang
      ) || {}
    );
  }

  loadApplications({ end }): void {
    if (
      this.loading ||
      this.applications.totalItems <= this.applicationsBuffer.length
    ) {
      return;
    }

    if (end + 5 > this.applicationsBuffer.length) {
      this.loadNext();
    }
  }

  loadFirst(term?: string): Observable<Pageable<Application>> {
    this.term = term ? { code: { value: term } } : {};
    this.loading = true;
    this.page = 0;
    this.applicationsBuffer = [];
    return this.applicationService.list(
      new PageOptions(this.page++, 5),
      new FilterOptions(this.term)
    );
  }

  loadNext(): void {
    this.loading = true;
    this.applicationService
      .list(new PageOptions(this.page++, 5), new FilterOptions(this.term))
      .subscribe((data) => this.setApplications(data));
  }

  setApplications(data: Pageable<Application>): void {
    this.applications = data;
    this.applicationsBuffer =
      this.applicationsBuffer.length > 0
        ? this.applicationsBuffer.concat(this.applications.content)
        : this.applications.content;
    this.loading = false;
  }

  delete(id: string) {
    this.testService.delete(id).subscribe((data) => {
      this.list();
    });
  }

  changeApplication(event: Application): void {
    console.log(event);
  }
  deleteTest(id: string) {
    this.testService.delete(id).subscribe(
      (data) => {
        this.list();
      },
      (err) => {
        if (err.error.message === 'TEST_OWNED_BY_SCENARIO') {
          this.hasScenarios[id] = true;
        }
      }
    );
  }
}
