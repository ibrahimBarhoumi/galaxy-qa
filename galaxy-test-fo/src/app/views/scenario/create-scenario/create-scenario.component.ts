import {
  CdkDragDrop,
  moveItemInArray,
  transferArrayItem,
} from '@angular/cdk/drag-drop';
import { ChangeDetectorRef } from '@angular/core';
import { Component, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { Observable } from 'rxjs';
import { elementAt, switchMap } from 'rxjs/operators';
import { FilterOptions } from '../../../shared/config/page/filterOptions';
import { PageOptions } from '../../../shared/config/page/pageOptions';
import { ScenarioStatus } from '../../../shared/enums/scenarioStatus.enum';
import { ScenarioType } from '../../../shared/enums/scenarioType.enum';
import { Application } from '../../../shared/models/application';
import { ApplicationVersion } from '../../../shared/models/application-version';
import { Campaign } from '../../../shared/models/campaign';
import { ConfigurationML } from '../../../shared/models/configurationML';
import { Pageable } from '../../../shared/models/pageable';
import { Scenario } from '../../../shared/models/scenario';
import { ScenarioTest } from '../../../shared/models/scenarioTest';
import { ScenarioVersion } from '../../../shared/models/scenarioVersion';
import { Test } from '../../../shared/models/test';
import { ApplicationService } from '../../application/application.service';
import { VersionService } from '../../application/version/version.service';
import { CampaignService } from '../../campaign/campaign.service';
import { TestService } from '../../test/test.service';
import { ScenarioService } from '../scenario.service';
import { ScenarioTestService } from '../scenarioTest.service';
import { ScenarioVersionService } from '../scenarioversio.service';
import { TestVersionService } from '../sceraniro-test.service';

@Component({
  selector: 'app-create-scenario',
  templateUrl: './create-scenario.component.html',
  styleUrls: ['./create-scenario.component.scss'],
})
export class CreateScenarioComponent implements OnInit {
  scenarioForm: FormGroup;
  isSimpleScenario = false;
  isActivated = false;
  configurationMLS: FormGroup;
  configurationML: ConfigurationML;
  dataBuffer: Application[];
  data: Pageable<Application>;
  loading = false;
  page = 0;
  term = {};
  searchInput = new EventEmitter<string>();
  selectedApplication: Application;
  selectedVersion: ApplicationVersion;
  versions: Pageable<ScenarioVersion>;
  availableTestList: Test[] = [];
  selectedTestList: Test[] = [];
  scenarioTestList: ScenarioTest[] = [];
  scenariTest: ScenarioTest;
  beforeTest = false;
  test = {} as Test;
  element: Test;
  scenariTest2: ScenarioTest;
  showButto = false;
  response: Pageable<Test>;
  versionIdList: string[];
  selectedTest: Test;
  scenarioId: string;
  scenario: Scenario;
  application: Application;
  display = true;
  isFormInEditMode;
  exist = false;
  campaignList: Campaign[];
  sameCode = false;
  deleted = false;
  isFormInDuplicateMode = false;
  constructor(
    private formBuilder: FormBuilder,
    private cookiesService: CookieService,
    private applicationService: ApplicationService,
    private cd: ChangeDetectorRef,
    private versionService: VersionService,
    private testService: TestService,
    private scenarioService: ScenarioService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private scenarioVersion: ScenarioVersionService,
    private testVersionService: TestVersionService,
    private scenarioTestService: ScenarioTestService,
    private campaignService: CampaignService
  ) {}

  ngOnInit(): void {
    this.scenarioId = this.activatedRoute.snapshot.params.id;
    this.scenarioForm = this.formBuilder.group({
      id: '',
      code: ['', Validators.required],
      application: [this.application?.code || '', Validators.required],
      scenarioVersions: ['', Validators.required],
      scenarioType: [
        this.isSimpleScenario ? ScenarioType.TEST : ScenarioType.TNR,
        Validators.required,
      ],
      scenarioStatus: [
        this.isActivated ? ScenarioStatus.BLK : ScenarioStatus.ACT,
        Validators.required,
      ],
      scenarioTests: [[]],
      filtersTests: [[]],
      configurationMLS: this.formBuilder.array([this.createConfigurationMLS()]),
    });
    this.loadFirst().subscribe((data) => {
      this.setApplication(data);
    });
    this.searchInput.pipe(switchMap((term) => this.loadFirst(term))).subscribe(
      (data) => {
        this.setApplication(data);
        this.cd.markForCheck();
      },
      (err) => {
        console.log('error', err);
        this.cd.markForCheck();
      }
    );
    this.isFormInEditMode =
      !!this.scenarioId && this.activatedRoute.snapshot.url[0].path === 'edit';
    this.isFormInDuplicateMode =
      !!this.scenarioId &&
      this.activatedRoute.snapshot.url[0].path === 'duplicate';
    if (this.scenarioId) {
      this.getScenario(this.scenarioId);
      this.getApplication(this.scenarioId);
      this.getTest(this.scenarioId);
      console.log(this.isFormInEditMode);
    }
  }

  createConfigurationMLS(): FormGroup {
    this.configurationMLS = this.formBuilder.group({
      label: ['' || this.configurationML?.label, Validators.required],
      comment: ['' || this.configurationML?.comment, Validators.required],
      languageCode: this.cookiesService.get('lang'),
    });
    return this.configurationMLS;
  }
  toggleChange() {
    this.campaignService
      .getCampaignByScenarioId(this.scenarioId)
      .subscribe((data) => {
        this.campaignList = data;
        if (this.campaignList.length === 0) {
          this.exist = false;
        } else {
          this.exist = true;
          if (this.isSimpleScenario) {
            this.isSimpleScenario = false;
          } else {
            this.isSimpleScenario = true;
          }
        }
      });
  }

  loadApplication({ end }): void {
    if (this.loading || this.data.totalItems <= this.dataBuffer.length) {
      return;
    }

    if (end + 5 > this.dataBuffer.length) {
      this.loadNext();
    }
  }

  loadNext(): void {
    this.loading = true;
    this.applicationService
      .list(new PageOptions(this.page++, 5), new FilterOptions(this.term))
      .subscribe((data) => this.setApplication(data));
  }

  setApplication(data: Pageable<Application>): void {
    this.data = data;
    this.dataBuffer =
      this.dataBuffer.length > 0
        ? this.dataBuffer.concat(this.data.content)
        : this.data.content;
    this.loading = false;
  }

  loadFirst(term?: string): Observable<Pageable<Application>> {
    this.term = term ? { code: { value: term } } : {};
    this.loading = true;
    this.page = 0;
    this.dataBuffer = [];
    return this.applicationService.list(
      new PageOptions(this.page++, 5),
      new FilterOptions(this.term)
    );
  }

  changeApplication(event: Application) {
    this.selectedApplication = event;
    this.scenarioForm.controls.scenarioVersions.setValue(null);
    this.versionService
      .getVersionsByApplicationId(this.selectedApplication.id)
      .subscribe((data) => {
        const { content, ...pageable } = data;
        this.versions = pageable;
        this.versions.content = [];
        data.content.forEach((element, index) => {
          this.versions.content[index] = { applicationVersion: element };
        });
      });
    /* this.testService.getTestsByApplicationId(this.selectedApplication.id).subscribe(data => {
      this.availableTestList = data.content;
    });*/
  }
  changeVersion(event: ScenarioVersion[]) {
    this.versionIdList = [];
    event.forEach((element) => {
      this.versionIdList.push(element.applicationVersion.id);
    });
    this.testVersionService
      .getListTestByVersion(this.versionIdList)
      .subscribe((data) => {
        this.availableTestList = data.content;
      });
    this.selectedTestList = [];
    this.scenarioTestList = [];
  }

  drop(event: CdkDragDrop<string[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );
      this.scenarioTestList = [];
      this.createScenarioTestList();
      this.displayBeforeTest();
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );
      this.createScenarioTestList();
      this.displayBeforeTest();
    }
  }
  deleteDuplicate(): void {
    this.scenarioTestList.filter((value, index) => {
      // tslint:disable-next-line: no-unused-expression
      this.scenarioTestList.indexOf(value) === index;
    });
  }
  createScenarioTestList(): void {
    for (let i = this.scenarioTestList.length - 1; i >= 0; i--) {
      for (let j = 0; j < this.availableTestList.length; j++) {
        if (
          this.scenarioTestList[i] &&
          this.scenarioTestList[i].test === this.availableTestList[j]
        ) {
          this.scenarioTestList.splice(i, 1);
        }
      }
    }
    this.selectedTestList.find((element, index) => {
      this.scenariTest = {
        test: element,
        beforeTest: null,
        order: index + 1,
        scenario: null,
      };
      this.scenarioTestList.splice(index, 0, this.scenariTest);
      if (this.scenarioTestList.length > this.selectedTestList.length) {
        this.scenarioTestList.pop();
      }
    });
  }

  addBeforeTest(test: Test) {
    this.test = test;
    let x = this.selectedTestList.indexOf(test);
    this.scenariTest = this.scenarioTestList[x];
    this.scenarioTestList.splice(x, 1);
    if (x === this.scenarioTestList.length) {
      this.scenariTest2 = this.scenariTest;
      this.scenarioTestList.push(this.scenariTest2);
    } else if (x !== this.scenarioTestList.length) {
      this.test = this.selectedTestList[x + 1];
      this.scenariTest2 = {
        test: this.scenariTest.test,
        beforeTest: this.test,
        order: this.scenariTest.order,
        scenario: this.scenariTest.scenario,
      };
      console.log(this.scenarioTestList.join());
      console.log(this.scenarioTestList.splice(x, 0, this.scenariTest2));
    }
  }

  onSubmit(form: FormGroup): void {
    form.controls['scenarioType'].setValue(
      this.isSimpleScenario ? ScenarioType.TEST : ScenarioType.TNR
    );
    form.controls['scenarioStatus'].setValue(
      this.isActivated ? ScenarioStatus.BLK : ScenarioStatus.ACT
    );
    form.controls['scenarioTests'].setValue(this.scenarioTestList);
    if (this.isFormInEditMode) {
      this.scenario.scenarioStatus = form.controls['scenarioStatus'].value;
      this.scenario.scenarioType = form.controls['scenarioType'].value;
      this.scenario.code = form.controls['code'].value;
      this.scenario.scenarioTests = form.controls['scenarioTests'].value;
      this.scenario.scenarioVersions = form.controls['scenarioVersions'].value;
      const index = this.scenario.configurationMLS.findIndex(
        (conf) => conf.languageCode === this.cookiesService.get('lang')
      );
      if (index !== -1) {
        this.scenario.configurationMLS.splice(
          index,
          1,
          form.controls['configurationMLS'].value[0]
        );
      } else {
        this.scenario.configurationMLS.push(
          form.controls['configurationMLS'].value[0]
        );
      }
      this.scenarioService.update(this.scenarioId, this.scenario).subscribe(
        (data) => {
          setTimeout(() => {
            this.router.navigate(['/scenarios/list']);
          }, 700);
        },
        (err) => {
          if (err.error.message === 'SCENARIO_TEST_CODE_EXISTS') {
            this.sameCode = true;
            this.deleted = false;
            form.controls['code'].setValue('');
          }
          if (err.error.message === 'SCENARIO_TEST_CODE_DELETED') {
            this.deleted = true;
            this.sameCode = false;
            form.controls['code'].setValue('');
          }
        }
      );
    } else {
      console.log(form.value);
      this.scenarioService.create(form.value as Scenario).subscribe(
        (data) => {
          setTimeout(() => {
            this.router.navigate(['/scenarios/list']);
          }, 700);
        },
        (err) => {
          if (err.error.message === 'SCENARIO_TEST_CODE_EXISTS') {
            this.sameCode = true;
            this.deleted = false;
            form.controls['code'].setValue('');
          }
          if (err.error.message === 'SCENARIO_TEST_CODE_DELETED') {
            this.deleted = true;
            this.sameCode = false;
            form.controls['code'].setValue('');
          }
        }
      );
    }
  }

  searchScenarios(): void {
    const page = 0;
    const size = 10;
    const filters = {
      code: { value: !!this.getCodeQuery() ? this.getCodeQuery() : '' },
      application: { value: this.selectedApplication.id },
    };
    this.testService
      .list(new PageOptions(page, size), new FilterOptions(filters))
      .subscribe((data) => {
        this.response = data;
        this.availableTestList = this.response.content;
      });
  }

  getCodeQuery(): string {
    const query = this.scenarioForm.value.filtersTests;
    return !!query ? query : '';
  }

  onCancel(): void {
    this.router.navigate(['/scenarios/list']);
  }
  private getApplication(id: string) {
    this.scenarioVersion.getApplicationByScenarioId(id).subscribe((data) => {
      this.application = data;
      console.log(this.application);
      this.scenarioForm.controls['application'].setValue(this.application);
      this.scenarioVersion
        .getVersionsByScenarioId(this.scenarioId)
        // tslint:disable-next-line: no-shadowed-variable
        .subscribe((data) => {
          const { content, ...pageable } = data;
          this.versions = pageable;
          this.versions.content = [];
          data.content.forEach((element, index) => {
            this.versions.content[index] = { applicationVersion: element };
          });
          this.scenarioForm.controls['scenarioVersions'].setValue(
            this.versions.content
          );
          this.versionIdList = [];
          this.versions.content.forEach((element) => {
            this.versionIdList.push(element.applicationVersion.id);
            this.testVersionService
              .getListTestByVersion(this.versionIdList)
              // tslint:disable-next-line: no-shadowed-variable
              .subscribe((data) => {
                console.log(data);
                this.availableTestList = data.content;
                this.availableTestList = this.availableTestList.filter((sce) =>
                  this.selectedTestList.every(
                    (sce2) => !sce2.id.includes(sce.id)
                  )
                );

                console.log(this.selectedTestList);
              });
          });
        });
      this.versionService
        .getVersionsByApplicationId(this.application.id)
        // tslint:disable-next-line: no-shadowed-variable
        .subscribe((data) => {
          console.log(data);
          const { content, ...pageable } = data;
          this.versions = pageable;
          this.versions.content = [];
          data.content.forEach((element, index) => {
            this.versions.content[index] = { applicationVersion: element };
          });
        });
    });
  }

  getScenario(id: string) {
    this.scenarioService.get(id).subscribe((data) => {
      this.scenario = data;
      console.log(this.scenario);
      this.isActivated = this.scenario.scenarioStatus === ScenarioStatus.BLK;
      this.isSimpleScenario = this.scenario.scenarioType === ScenarioType.TEST;
      this.initScenarioForm();
    });
  }
  getTest(id: string) {
    this.scenarioTestService
      .getScenarioTestByScenarioId(id)
      .subscribe((data) => {
        this.scenarioTestList = data.content;
        data.content.forEach((element) => {
          this.selectedTestList.push(element.test);
        });
      });
  }

  private initScenarioForm() {
    if (this.isFormInDuplicateMode === true) {
      this.scenarioForm.controls['code'].setValue('');
      this.scenarioForm.controls['id'].setValue('');
      console.log(this.scenario.id);
    } else {
      this.scenarioForm.controls['code'].setValue(this.scenario.code);
    }
    this.scenarioForm.controls['id'].setValue(this.scenario.id);
    this.scenarioForm
      .get('configurationMLS')
      ['controls'][0].controls['label'].setValue(
        (this.configurationML = this.scenario.configurationMLS.find(
          (conf) => conf.languageCode === this.cookiesService.get('lang')
        ))
      );
    this.scenarioForm
      .get('configurationMLS')
      ['controls'][0].controls['comment'].setValue(
        this.configurationML?.comment
      );
    this.scenarioForm
      .get('configurationMLS')
      ['controls'][0].controls['label'].setValue(this.configurationML?.label);

    this.scenarioForm
      .get('configurationMLS')
      ['controls'][0].controls['comment'].setValue(
        this.configurationML?.comment
      );
  }
  displayBeforeTest() {
    if (this.scenarioTestList.length > 1) {
      this.display = false;
    } else {
      this.display = true;
    }
  }
}
