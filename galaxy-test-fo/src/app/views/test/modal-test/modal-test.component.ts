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
@Component({
  templateUrl: 'modal-test.component.html',
  styleUrls: ['./modal-test.component.scss'],
})
export class ModalTestComponent implements OnInit {
  applications: Pageable<Application>;
  versions: Pageable<TestVersion>;

  testForm: FormGroup;
  configurationMLS: FormGroup;

  loadingApplications = false;
  dataBufferApplications: Application[];

  loadingVersions = false;
  dataBufferVersions: TestVersion[];

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

  public event: EventEmitter<any> = new EventEmitter();

  constructor(
    private applicationService: ApplicationService,
    private versionService: VersionService,
    private testService: TestService,
    public bsModalRef: BsModalRef,
    private cd: ChangeDetectorRef,
    private formBuilder: FormBuilder,
    private translateService: TranslateService,
  ) {}

  ngOnInit(): void {

    this.loadFirstApplicationPage().subscribe((data) => {
      this.setApplications(data);
    });

    if(this.testForm.value.application){

      this.loadFirstVersionPage(null, this.testForm.value.application).subscribe((data) => {
        this.setVersions(data);
      });

      this.testService.getVersionsByTestId(this.test.id).subscribe(data=>{

        this.testForm.controls.testVersions.setValue(data);
      }
        )
    }

    this.searchApplicationInput.pipe(switchMap((term) => this.loadFirstApplicationPage(term))).subscribe(
      (data) => {
        this.setApplications(data);
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



  onSubmitTest(form: FormGroup): void {
    const test = form.value;
    const configurationML = form.value.configurationMLS[0];
    if (this.copy?.id) {
      test.configurationMLS = this.test.configurationMLS;
     const index = test.configurationMLS.findIndex(conf => conf.languageCode === this.translateService.currentLang);
     if (index === -1) {
      test.configurationMLS.push(configurationML);
     } else {
      test.configurationMLS[index] = configurationML; }
      this.event.emit(this.testService
        .update(this.copy.id, test as Test));
    } else {
      this.event.emit(this.testService
        .create(test as Test));
    }
  }


  loadApplications({ end }): void {
    if (this.loadingApplications || this.applications.totalItems <= this.dataBufferApplications.length) {
      return;
    }

    if (end + 5 > this.dataBufferApplications.length) {
      this.loadNextApplicationPage();
    }
  }

  loadFirstApplicationPage(term?: string): Observable<Pageable<Application>> {
    this.term = term ? { code: { value: term } } : {};
    this.loadingApplications = true;
    this.page = 0;
    this.dataBufferApplications = [];
    return this.applicationService.list(
      new PageOptions(this.page++, 5),
      new FilterOptions(this.term)
    );
  }

  loadNextApplicationPage(): void {
    this.loadingApplications = true;
    this.applicationService
      .list(new PageOptions(this.page++, 5), new FilterOptions(this.term))
      .subscribe((data) => this.setApplications(data));
  }

  setApplications(data: Pageable<Application>): void {
    this.applications = data;
    this.dataBufferApplications =
      this.dataBufferApplications.length > 0
        ? this.dataBufferApplications.concat(this.applications.content)
        : this.applications.content;
    this.loadingApplications = false;
  }

  changeApplication(application:Application){
    this.testForm.controls.testVersions.setValue(null);
    this.loadFirstVersionPage(null, application).subscribe((data) => {
      this.setVersions(data);
    });
  }

  loadVersions({ end }): void {
    if (this.loadingVersions || this.versions.totalItems <= this.dataBufferVersions.length) {
      return;
    }

    if (end + 5 > this.dataBufferVersions.length) {
      this.loadNextVersionPage();
    }
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
}
