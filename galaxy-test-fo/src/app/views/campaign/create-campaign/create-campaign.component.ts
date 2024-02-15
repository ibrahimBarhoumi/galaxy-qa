import { Component, DoCheck, EventEmitter, OnInit } from '@angular/core';
import {
  CdkDragDrop,
  moveItemInArray,
  transferArrayItem,
} from '@angular/cdk/drag-drop';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Campaign } from '../../../shared/models/campaign';
import { CampaignService } from '../campaign.service';
import { CampaignStatus } from '../../../shared/enums/campaignStatus.enum';
import { CampaignType } from '../../../shared/enums/campaignType.enum';
import { LoadingScreenService } from '../../../shared/services/loading-screen.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ScenarioService } from '../../scenario/scenario.service';
import { Scenario } from '../../../shared/models/scenario';
import { Pageable } from '../../../shared/models/pageable';
import { PageOptions } from '../../../shared/config/page/pageOptions';
import { FilterOptions } from '../../../shared/config/page/filterOptions';
import { CookieService } from 'ngx-cookie-service';
import { ConfigurationML } from '../../../shared/models/configurationML';

@Component({
  selector: 'app-create-campaign',
  templateUrl: './create-campaign.component.html',
  styleUrls: ['./create-campaign.component.scss'],
})
export class CreateCampaignComponent implements OnInit, DoCheck  {
  campaignForm: FormGroup;
  configurationMLS: FormGroup;
  isActivated = false;
  isSimpleTest = false;
  response: Pageable<Scenario>;
  selectedScenario: any[] = [];
  scenarioList: any[] = [];
  allScenarios: any[] = [];
  scenarioSearchInput: string = '';
  lang = this.cookiesService.get('lang');
  stateChanged: EventEmitter<string> = new EventEmitter();
  isFormInEditMode = false;
  isFormInDuplicateMode = false;
  private campaignId: string;
  campaign: Campaign;
  configurationML: ConfigurationML;
  deleted = false;
  exist = false;
  disablePopover = true;
  constructor(
    private formBuilder: FormBuilder,
    private campaignService: CampaignService,
    private scenarioService: ScenarioService,
    private loadingScreenService: LoadingScreenService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private cookiesService: CookieService
  ) {}

  ngOnInit(): void {
    this.campaignId = this.activatedRoute.snapshot.params.id;
    this.campaignForm = this.formBuilder.group({
      id: '',
      code: ['', Validators.required],
      campaignType: [
        this.isSimpleTest ? CampaignType.TEST : CampaignType.TNR,
        Validators.required,
      ],
      campaignStatus: [
        this.isActivated ? CampaignStatus.ACTIVATED : CampaignStatus.BLOCKED,
        Validators.required,
      ],
      configurationMLS: this.formBuilder.array([this.createConfigurationMLS()]),
      filterScenario: ['', null],
      scenarioList: [[], null],
      isFormEditMode: false,
    });
    this.isFormInEditMode =
      !!this.campaignId && this.activatedRoute.snapshot.url[0].path === 'edit';
    this.isFormInDuplicateMode =
      !!this.campaignId &&
      this.activatedRoute.snapshot.url[0].path === 'duplicate';
    if (this.campaignId) {
      this.getCampaign(this.campaignId);
    }
    this.searchScenarios();
  }

  createConfigurationMLS(): FormGroup {
    this.configurationMLS = this.formBuilder.group({
      label: ['' || this.configurationML?.label, Validators.required],
      comment: ['' || this.configurationML?.comment, Validators.required],
      languageCode: this.cookiesService.get('lang'),
    });
    return this.configurationMLS;
  }

  onSubmit(form: FormGroup): void {
    this.loadingScreenService.startLoading();
    form.controls['campaignType'].setValue(
      this.isSimpleTest ? CampaignType.TEST : CampaignType.TNR
    );
    form.controls['campaignStatus'].setValue(
      this.isActivated ? CampaignStatus.ACTIVATED : CampaignStatus.BLOCKED
    );
    form.controls['scenarioList'].setValue(this.selectedScenario);
    if (this.isFormInDuplicateMode) {
      this.campaignService.create(form.value as Campaign).subscribe(
        (data) => {
          setTimeout(() => {
            this.loadingScreenService.stopLoading();
            this.router.navigate(['/campaign/list']);
          }, 700);
        },
        (err) => {
          if (err.error.message === 'CAMPAIGN_TEST_CODE_EXISTS') {
            this.exist = true;
            this.deleted = false;
            form.controls['code'].setValue('');
          }
          if (err.error.message === 'CAMPAIGN_TEST_CODE_DELETED') {
            this.deleted = true;
            this.exist = false;
            form.controls['code'].setValue('');
          }
        },
        () => this.loadingScreenService.stopLoading()
      );
    }
    if (this.isFormInEditMode) {
      this.campaign.campaignStatus = form.controls['campaignStatus'].value;
      this.campaign.campaignType = form.controls['campaignType'].value;
      this.campaign.code = form.controls['code'].value;
      this.campaign.scenarioList = form.controls['scenarioList'].value;
      const index = this.campaign.configurationMLS.findIndex(
        (conf) => conf.languageCode === this.cookiesService.get('lang')
      );
      if (index !== -1) {
        this.campaign.configurationMLS.splice(
          index,
          1,
          form.controls['configurationMLS'].value[0]
        );
      } else {
        this.campaign.configurationMLS.push(
          form.controls['configurationMLS'].value[0]
        );
      }
      this.campaignService.update(this.campaignId, this.campaign).subscribe(
        (data) => {
          setTimeout(() => {
            this.loadingScreenService.stopLoading();
            this.router.navigate(['/campaign/list']);
          }, 700);
        },
        (err) => {
          if (err.error.message === 'CAMPAIGN_TEST_CODE_EXISTS') {
            this.exist = true;
            this.deleted = false;
            form.controls['code'].setValue('');
          }
          if (err.error.message === 'CAMPAIGN_TEST_CODE_DELETED') {
            this.deleted = true;
            this.exist = false;
            form.controls['code'].setValue('');
          }
        },
        () => this.loadingScreenService.stopLoading()
      );
    } else {
      this.campaignService.create(form.value as Campaign).subscribe(
        (data) => {
          setTimeout(() => {
            this.loadingScreenService.stopLoading();
            this.router.navigate(['/campaign/list']);
          }, 700);
        },
        (err) => {
          if (err.error.message === 'CAMPAIGN_TEST_CODE_EXISTS') {
            this.exist = true;
            this.deleted = false;
            form.controls['code'].setValue('');
          }
          if (err.error.message === 'CAMPAIGN_TEST_CODE_DELETED') {
            this.deleted = true;
            this.exist = false;
            form.controls['code'].setValue('');
          }
        },
        () => this.loadingScreenService.stopLoading()
      );
    }
  }

  onCancel(): void {
    this.router.navigate(['/campaign/list']);
  }

  drop(event: CdkDragDrop<string[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );
    }
  }

  searchScenarios(): void {
    const page = 0;
    const size = 10;
    const filters = {
      code: { value: !!this.getCodeQuery() ? this.getCodeQuery() : '' },
    };
    this.scenarioService
      .list(new PageOptions(page, size), new FilterOptions(filters))
      .subscribe((data) => {
        this.response = data;
        this.scenarioList = this.response.content;
        this.scenarioList = this.scenarioList.filter((sce) =>
          this.selectedScenario.every((sce2) => !sce2.id.includes(sce.id))
        );
        this.allScenarios = this.scenarioList;
        this.toggleChange();
      });
  }

  getCodeQuery(): string {
    const query = this.campaignForm.value.filterScenario;
    return !!query ? query : '';
  }

  getCampaign(id: string): void {
    this.campaignService.get(id).subscribe({
      next: (data) => {
        this.campaign = data;
        this.isActivated =
          this.campaign.campaignStatus === CampaignStatus.ACTIVATED;
        this.isSimpleTest = this.campaign.campaignType === CampaignType.TEST;
        this.initCampaignForm();
      },
    });
  }

  private initCampaignForm() {
    if (!!this.campaign) {
      if (!this.isFormInDuplicateMode) {
        this.campaignForm.controls['code'].setValue(this.campaign.code);
      }
      this.campaignForm
        .get('configurationMLS')
        ['controls'][0].controls['label'].setValue(
          (this.configurationML = this.campaign.configurationMLS.find(
            (conf) => conf.languageCode === this.cookiesService.get('lang')
          ))
        );

      this.campaignForm
        .get('configurationMLS')
        ['controls'][0].controls['comment'].setValue(
          this.configurationML?.comment
        );
      this.campaignForm
        .get('configurationMLS')
        ['controls'][0].controls['label'].setValue(this.configurationML?.label);

      this.campaignForm
        .get('configurationMLS')
        ['controls'][0].controls['comment'].setValue(
          this.configurationML?.comment
        );
      this.selectedScenario = this.campaign.scenarioList;
    }
  }

  toggleChange() {
    if (this.isSimpleTest) {
      this.scenarioList = this.allScenarios.filter(
        (sc) => sc.scenarioType === 'TEST'
      );
    }
    if (!this.isSimpleTest) {
      this.scenarioList = this.allScenarios.filter(
        (sc) => sc.scenarioType === 'TNR'
      );
    }
  }

  // tslint:disable-next-line: use-lifecycle-interface
  ngDoCheck() {
    const newLang = this.cookiesService.get('lang');
    if (newLang !== this.lang) {
      this.campaignForm
        .get('configurationMLS')
        ['controls'][0].controls['label'].setValue(
          (this.configurationML = this.campaign.configurationMLS.find(
            (conf) => conf.languageCode === this.cookiesService.get('lang')
          ))
        );
      this.campaignForm
        .get('configurationMLS')
        ['controls'][0].controls['label'].setValue(this.configurationML?.label);

      this.campaignForm
        .get('configurationMLS')
        ['controls'][0].controls['comment'].setValue(
          this.configurationML?.comment
        );
      this.lang = this.cookiesService.get('lang');
    }
  }
  getAllScenarios() {
    const page = 0;
    const size = 10;
    this.scenarioService.list(new PageOptions(page, size)).subscribe((data) => {
      this.response = data;
      this.allScenarios = this.response.content;
    });
  }
}
