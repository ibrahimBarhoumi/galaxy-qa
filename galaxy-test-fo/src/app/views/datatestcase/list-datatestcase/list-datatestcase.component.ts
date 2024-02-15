import { Component, Input, OnInit } from '@angular/core';
import { DataTestCase } from '../../../shared/models/dataTestCase';
import { Pageable } from '../../../shared/models/pageable';
import { LazyLoadEvent } from 'primeng/api';
import { PageOptions } from '../../../shared/config/page/pageOptions';
import { FilterOptions } from '../../../shared/config/page/filterOptions';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { Observable } from 'rxjs';
import { FormArray, FormBuilder, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { ConfigurationML } from '../../../shared/models/configurationML';
import { DataTestCaseService } from '../datatestcase.service';
import { Test } from '../../../shared/models/test';
import { ApplicationVersion } from '../../../shared/models/application-version';
import { TestService } from '../../test/test.service';

import { ModalDataTestCaseComponent } from '../modal-datatestcase/modal-datatestcase.component';
import { DataTestCaseVersionService } from '../datatestcaseVersion.service';
import { FileInfo } from '../../../shared/models/FileInfo';

@Component({
  templateUrl: 'list-datatestcase.component.html',
  styleUrls: ['./list-datatestcase.component.scss'],
})
export class ListDatatestCaseComponent implements OnInit {
  datatestcases: DataTestCase[];

  data: Pageable<DataTestCase>;

  tests: Pageable<Test>;

  datatestcaseForm: FormGroup;
  bsModalRef: BsModalRef;
  cols: any[];

  loading = false;
  testsBuffer: Test[];
  term = {};

  page = 0;
  configurationMLS: FormGroup;
  hasScenarios = [];
  fileInfo: FileInfo;
  version: ApplicationVersion[];
  constructor(
    private applicationService: TestService,
    private datatestcaseService: DataTestCaseService,
    private formBuilder: FormBuilder,
    private modalService: BsModalService,
    private dataTestCaseVersionService: DataTestCaseVersionService,
  ) {

}

  ngOnInit(): void {
    this.cols = [
      { field: 'code', header: 'DATATESTCASE.COLS.CODE', type: 'text' },
      { field: 'order', header: 'DATATESTCASE.COLS.ORDER', type: 'text' },
      {
        field: 'test',
        header: 'DATATESTCASE.COLS.TEST',
        type: 'text',
        isParent: true,
      }
    ];

  }

  list(event?: LazyLoadEvent): void {
    this.data = null;
    const state = JSON.parse(localStorage.getItem('stateDataTestCaseDataTable'));
    const page =
      Math.floor(event?.first / event?.rows) + 1 ||
      Math.floor(state?.first / state?.rows) + 1 ||
      1;
    const size = event?.rows || state?.rows || 10;
    const filters = event?.filters || state?.filters || {};
    this.datatestcaseService
      .list(new PageOptions(page - 1, size), new FilterOptions(filters))
      .subscribe((data) => {
        this.data = data;
      });
  }

  /*edit(datatestcase?: DataTestCase) {
    const copy = { ...datatestcase };
    this.openModalWithComponent(datatestcase, copy);
  }

  duplicate(datatestcase?: DataTestCase) {
    const copy = { ...datatestcase };
    copy.id = null;
    this.openModalWithComponent(datatestcase, copy);
  }*/
  openModalWithComponent(dataTestCase?: DataTestCase, copy?: DataTestCase) {


    /*if (datatestcase) {
      this.dataTestCaseVersionService
        .getVersionByDataTestCaseId(datatestcase.id)
        .subscribe((data) => (datatestcase.v = data));
    }*/

    this.datatestcaseForm = this.formBuilder.group({
      code: [copy?.id ? dataTestCase.code : '', Validators.required],
      order: [dataTestCase?.order, Validators.required],
      test: [dataTestCase?.test || '', Validators.required],
      applicationVersionList: dataTestCase?.applicationVersionList || '',
      fileInfo:this.fileInfo || '',
      values: this.formBuilder.array([]),
    });
    this.bsModalRef = this.modalService.show(ModalDataTestCaseComponent, {
      initialState: { dataTestCaseForm: this.datatestcaseForm, dataTestCase, copy },
    });
    this.bsModalRef.content.event.subscribe((res: Observable<DataTestCase>) => {
      res.subscribe(
        () => {
          this.bsModalRef.hide();
          this.list();
        },
        (data) => {
          if (data.error.message === 'TEST_CODE_EXISTS') {
            this.datatestcaseForm.controls['code'].setErrors({
              error: 'code exist',
            });
          } else if (data.error.message === 'TEST_CODE_DELETED') {
            this.datatestcaseForm.controls['code'].setErrors({
              error: 'code exist in deleted datatestcase',
            });
          }
        }
      );
    });
  }


  loadTests({ end }): void {
    if (
      this.loading ||
      this.tests.totalItems <= this.testsBuffer.length
    ) {
      return;
    }

    if (end + 5 > this.testsBuffer.length) {
      this.loadNext();
    }
  }



  loadNext(): void {
    this.loading = true;
    this.applicationService
      .list(new PageOptions(this.page++, 5), new FilterOptions(this.term))
      .subscribe((data) => this.setTests(data));
  }

  setTests(data: Pageable<Test>): void {
    this.tests = data;
    this.testsBuffer =
      this.testsBuffer.length > 0
        ? this.testsBuffer.concat(this.tests.content)
        : this.tests.content;
    this.loading = false;
  }

  delete(id: string) {
    this.datatestcaseService.delete(id).subscribe((data) => {
      this.list();
    });
  }

  changeTest(event: Test): void {
    console.log(event);
  }
  deleteDataTestCase(id: string) {
    this.datatestcaseService.delete(id).subscribe(
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

  getScenarioTestByScenarioId(id: string) {
    return this.dataTestCaseVersionService
      .getVersionByDataTestCaseId(id)
      .subscribe((data) => {
        this.version = data.content;
        console.log(this.version)
      });
  }



}
