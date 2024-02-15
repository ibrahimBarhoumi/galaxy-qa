import {
  ChangeDetectorRef,
  Component,
  EventEmitter,
  OnInit,
} from '@angular/core';
import { Pageable } from '../../../shared/models/pageable';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ConfigurationML } from '../../../shared/models/configurationML';
import { PageOptions } from '../../../shared/config/page/pageOptions';
import { Observable } from 'rxjs';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { switchMap } from 'rxjs/operators';
import { FilterOptions } from '../../../shared/config/page/filterOptions';
import { TestService } from '../../test/test.service';
import { DataTestCase } from '../../../shared/models/dataTestCase';

import { VersionService } from '../../application/version/version.service';
import { TestVersion } from '../../../shared/models/testVersion';
import { Test } from '../../../shared/models/test';
import { ApplicationVersion } from '../../../shared/models/application-version';
import { DataTestCaseService } from '../datatestcase.service';
import { DataTestCaseVersionService } from '../datatestcaseVersion.service';
import { FileInfo } from '../../../shared/models/FileInfo';
@Component({
  templateUrl: 'modal-datatestcase.component.html',
  styleUrls: ['./modal-datatestcase.component.scss'],
})
export class ModalDataTestCaseComponent implements OnInit {
  tests: Pageable<Test>;
  versions: Pageable<TestVersion>;

  dataTestCaseForm: FormGroup;
  configurationMLS: FormGroup;

  loadingTests = false;
  dataBufferTests: Test[];

  loadingVersions = false;
  dataBufferVersions: TestVersion[];

  searchTestInput = new EventEmitter<string>();
  searchVersionInput = new EventEmitter<string>();


  page = 0;

  listLang = ['fr', 'en'];
  slistLang = [];
  term = {};
  dataTestCase: DataTestCase;
  copy: DataTestCase;
  configurationML: ConfigurationML;

  loading: boolean = false;
  file: File = null;
  /*fileName: string;
   fileType: string;
   organization: string;
   businessKey :string ;*/
  fileInfo:FileInfo;
  public event: EventEmitter<any> = new EventEmitter();

  constructor(
    private testService: TestService,
    private versionService: VersionService,
    private dataTestCaseService: DataTestCaseService,
    public bsModalRef: BsModalRef,
    private cd: ChangeDetectorRef,
    private formBuilder: FormBuilder,
    private dataTestCaseVersionService: DataTestCaseVersionService,
  ) {

  }

  ngOnInit(): void {

    this.loadFirstTestPage().subscribe((data) => {
      this.setTests(data);
    });

    if(this.dataTestCaseForm.value.test){

      this.loadFirstVersionPage(null, this.dataTestCaseForm.value.test).subscribe((data) => {
        this.setVersions(data);
      });

      this.testService.getVersionsByTestId(this.dataTestCase.id).subscribe(data=>{

        this.dataTestCaseForm.controls.dataTestCaseVersions.setValue(data);
      }
        )
    }

    this.searchTestInput.pipe(switchMap((term) => this.loadFirstTestPage(term))).subscribe(
      (data) => {
        this.setTests(data);
        this.cd.markForCheck();
      },
      (err) => {
        console.log('error', err);
        this.cd.markForCheck();
      }
    );



    this.searchVersionInput.pipe(switchMap((term) => this.loadFirstVersionPage(term))).subscribe(
      (data) => {
        this.setVersions(data);
        this.cd.markForCheck();
      },
      (err) => {
        console.log('error', err);
        this.cd.markForCheck();
      }
    );
  }



  onSubmitDataTestCase(form: FormGroup): void {
    setTimeout(
    form.value.applicationVersionList.forEach(element => {
      element.fileInfo = this.fileInfo;
    }),5000);
    const dataTestCase = form.value;
      this.event.emit(this.dataTestCaseService
        .create(dataTestCase as DataTestCase));
        console.log(dataTestCase)
  }


  loadTests({ end }): void {
    if (this.loadingTests || this.tests.totalItems <= this.dataBufferTests.length) {
      return;
    }

    if (end + 5 > this.dataBufferTests.length) {
      this.loadNextTestPage();
    }
  }

  loadFirstTestPage(term?: string): Observable<Pageable<Test>> {
    this.term = term ? { code: { value: term } } : {};
    this.loadingTests = true;
    this.page = 0;
    this.dataBufferTests = [];
    return this.testService.list(
      new PageOptions(this.page++, 5),
      new FilterOptions(this.term)
    );
  }

  loadNextTestPage(): void {
    this.loadingTests = true;
    this.testService
      .list(new PageOptions(this.page++, 5), new FilterOptions(this.term))
      .subscribe((data) => this.setTests(data));
  }

  setTests(data: Pageable<Test>): void {
    this.tests = data;
    this.dataBufferTests =
      this.dataBufferTests.length > 0
        ? this.dataBufferTests.concat(this.tests.content)
        : this.tests.content;
    this.loadingTests = false;
  }

  changeTest(test:Test){
    this.testService.getVersionsByTestId(test.id).subscribe((data)=>{
      this.dataBufferVersions=data;
    })
  }

  loadVersions({ end }): void {
    if (this.loadingVersions || this.versions.totalItems <= this.dataBufferVersions.length) {
      return;
    }

    if (end + 5 > this.dataBufferVersions.length) {
      this.loadNextVersionPage();
    }
  }

  loadFirstVersionPage(term?: string,test?:Test): Observable<Pageable<ApplicationVersion>> {
    this.term = term ? { code: { value: term },test:{value:test?.id} } : {test:{value:test?.id}};
    this.term
    this.loadingVersions = true;
    this.page = 0;
    this.dataBufferVersions = [];
    return this.versionService.list(
      new PageOptions(this.page++, 5),
      new FilterOptions(this.term)
    );
  }

  loadNextVersionPage(): void {
    this.loadingVersions = true;
    this.versionService
      .list(new PageOptions(this.page++, 5), new FilterOptions(this.term))
      .subscribe((data) => this.setVersions(data));
  }

  setVersions(data: Pageable<ApplicationVersion>): void {


    const { content, ...pageable } = data;
    this.versions = pageable
    this.versions.content =[]
    data.content.forEach((element,index) => {
      this.versions.content[index]={applicationVersion:element}
    });

    this.dataBufferVersions =
      this.dataBufferVersions.length > 0
        ? this.dataBufferVersions.concat(this.versions.content)
        : this.versions.content;
    this.loadingVersions = false;
  }

  field(): FormGroup {
    return this.formBuilder.group({
      label: '',
      value: '',
    });
  }

  addField() {
    this.formData().push(this.field());
  }

  formData(): FormArray {
    return this.dataTestCaseForm.get('values') as FormArray;
  }
  removeField(i: number) {
      this.formData().removeAt(i);
  }
  onChangeFile(event) {
    this.file = event.target.files[0];
}
onUpload() {
  this.loading = !this.loading;
  console.log(this.file);
  const data: FormData = new FormData();
  data.append('file', this.file);
  this.dataTestCaseVersionService.upload(data,"aaaaa","pdf","Lov","Key").subscribe((data)=>
   { this.fileInfo = data; }
  );
}
}
