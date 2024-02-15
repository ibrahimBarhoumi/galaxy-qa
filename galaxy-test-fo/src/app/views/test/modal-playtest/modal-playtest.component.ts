import {
  ChangeDetectorRef,
  Component,
  EventEmitter,
  OnInit,
} from '@angular/core';
import { Application } from '../../../shared/models/application';
import { Pageable } from '../../../shared/models/pageable';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ConfigurationML } from '../../../shared/models/configurationML';
import { PageOptions } from '../../../shared/config/page/pageOptions';
import { ApplicationService } from '../../application/application.service';
import { Observable } from 'rxjs';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { switchMap } from 'rxjs/operators';
import { FilterOptions } from '../../../shared/config/page/filterOptions';
import { TestService } from '../test.service';
import { Test } from '../../../shared/models/test';
import { ApplicationStatus } from '../../../shared/enums/applicationStatus.enum';
import { TranslateService } from '@ngx-translate/core';
import { TestStatus, Test_status_2Label } from '../../../shared/enums/testStatus.enum';
import { TestType, Test_type_2Label } from '../../../shared/enums/testType.enum';
import { ApplicationVersion } from '../../../shared/models/application-version';
import { VersionService } from '../../application/version/version.service';
import { TestVersion } from '../../../shared/models/testVersion';
import { DataTestCaseVersionService } from '../../datatestcase/datatestcaseVersion.service';
import { DataTestCaseVersion } from '../../../shared/models/dataTestCaseVersion';
import { LaunchTest } from '../../../shared/models/launchTest';
import { DataTestCase } from '../../../shared/models/dataTestCase';
@Component({
  selector: 'app-modal-playtest',
  templateUrl: './modal-playtest.component.html',
  styleUrls: ['./modal-playtest.component.scss']
})
export class ModalPlaytestComponent implements OnInit {
  applications: Pageable<Application>;

  executionForm: FormGroup;
  configurationMLS: FormGroup;

  loadingApplications = false;
  dataBufferApplications: DataTestCaseVersion[];

  loadingVersions = false;
  dataBufferVersions: ApplicationVersion[];

  searchApplicationInput = new EventEmitter<string>();
  searchVersionInput = new EventEmitter<string>();

  public testStatusList = Object.values(TestStatus);
  public testTypeList = Object.values(TestType);

  public Test_type_2Label = Test_type_2Label;
  public Test_status_2Label = Test_status_2Label;

  page = 0;

  listLang = ['fr', 'en'];
  slistLang = [];
  term = {};
  test: Test;
  copy: Test;
  configurationML: ConfigurationML;
  launchTest = {} as LaunchTest 
  public event: EventEmitter<any> = new EventEmitter();

  constructor(
    private applicationService: ApplicationService,
    private versionService: VersionService,
    private testService: TestService,
    public bsModalRef: BsModalRef,
    private cd: ChangeDetectorRef,
    private formBuilder: FormBuilder,
    private translateService: TranslateService,
    private dataTestCaseVersionService: DataTestCaseVersionService
  ) {}

  ngOnInit(): void {
    this.setVersions();


  }



  onSubmitTest(form: FormGroup): void {
    console.log(this.launchTest)
    this.event.emit(this.testService.LaunchTest(this.launchTest))
  }
  loadFirstVersionPage(term?: string,application?:Application): Observable<Pageable<ApplicationVersion>> {
    this.term = term ? { code: { value: term },application:{value:application?.id} } : {application:{value:application?.id}};
    this.term
    this.loadingVersions = true;
    this.page = 0;
    this.dataBufferVersions = [];
    return this.versionService.list(
      new PageOptions(this.page++, 5),
      new FilterOptions(this.term)
    );
  }
  setVersions(): void {
    this.dataTestCaseVersionService.getVersionByTestId(this.test.id).subscribe((data)=>{this.dataBufferVersions =data
    console.log(this.dataBufferVersions)})
  }
  versionChange(version:ApplicationVersion){
    this.dataBufferApplications = []
    this.executionForm.controls['application'].setValue('')
    this.dataTestCaseVersionService.getDataTestCaseByTestIdAndVersionId(this.test.id,version.id).subscribe((data)=>{
      this.dataBufferApplications = data;
      console.log(this.dataBufferApplications)
    })
  }
  DataTestCaseChange(dataTestCase: DataTestCaseVersion){
console.log(dataTestCase)  
this.launchTest.test = this.test
this.launchTest.dataTestCaseVersion = dataTestCase
console.log(this.launchTest)
}
}
