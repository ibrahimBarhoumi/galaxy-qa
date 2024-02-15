import { Component, OnInit } from '@angular/core';
import { Campaign } from '../../../shared/models/campaign';
import { CampaignService } from '../campaign.service';
import {
  CampaignStatus,
  CampaignStatus2Label,
} from '../../../shared/enums/campaignStatus.enum';
import {
  CampaignType,
  CampaignType2Label
} from '../../../shared/enums/campaignType.enum';
import{ScenarioStatus2Label} from '../../../shared/enums/scenarioStatus.enum';
import{ScenarioType2Label} from '../../../shared/enums/scenarioType.enum';
import{Test_type_2Label} from '../../../shared/enums/testType.enum';
import{Test_status_2Label} from '../../../shared/enums/testStatus.enum';
import {  Pageable } from '../../../shared/models/pageable';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ConfigurationML } from '../../../shared/models/configurationML';
import { LazyLoadEvent } from 'primeng/api';
import { PageOptions } from '../../../shared/config/page/pageOptions';
import { FilterOptions } from '../../../shared/config/page/filterOptions';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { ScenarioTest } from '../../../shared/models/scenarioTest';
import { ScenarioTestService } from '../../scenario/scenarioTest.service';

@Component({
  templateUrl: 'list-campaign.component.html',
  styleUrls: ['./list-campaign.component.scss'],
})
export class ListCampaignComponent implements OnInit {

  campaigns: Campaign[];
  data: Pageable<Campaign>;

  public CampaignStatus2Label = CampaignStatus2Label;

  public ScenarioStatus2Label = ScenarioStatus2Label;

  public ScenarioType2Label = ScenarioType2Label;

  public Test_type_2Label = Test_type_2Label;

  public Test_status_2Label = Test_status_2Label;

  public campaignStatusList = Object.values(CampaignStatus);

  public CampaignType2Label = CampaignType2Label;

  public campaignTypeList = Object.values(CampaignType);

  campaignForm: FormGroup;
  configurationMLS: FormGroup;
  chevron:boolean = false;
  cols: any[];
  colsSenario: any[];
  test: ScenarioTest []

  constructor(
    private campaignService: CampaignService,
    private router: Router,
    private formBuilder: FormBuilder,
    private cookiesService: CookieService,
    private scenarioTestService: ScenarioTestService,
  ) {}

  ngOnInit(): void {

    this.cols = [
      { field: 'code', header: 'CAMPAIGN.LIST.COLS.CODE' , type: 'text', hasConfiguration: false},
      { field: 'campaignType', header: 'CAMPAIGN.LIST.COLS.TYPE'  , type: 'text', hasConfiguration: false, style: 'badge'},
      { field: 'campaignStatus', header: 'CAMPAIGN.LIST.COLS.STATUS' , type: 'text', hasConfiguration: false, style: 'badge'},
      { field: 'label', header: 'CAMPAIGN.LIST.COLS.LABEL' , type: 'text', hasConfiguration: true},
      { field: 'comment', header: 'CAMPAIGN.LIST.COLS.COMMENT'  , type: 'text', hasConfiguration: true},
  ];
  this.colsSenario = [
    { field: 'code', header: 'CAMPAIGN.LIST.COLS.CODE' , type: 'text', hasConfiguration: false},
    { field: 'scenarioType', header: 'CAMPAIGN.LIST.COLS.TYPE' , type: 'text', hasConfiguration: false,style: 'badge'},
    { field: 'scenarioStatus', header: 'CAMPAIGN.LIST.COLS.STATUS' , type: 'text', hasConfiguration: false, style: 'badge'},
    { field: 'creationDate', header: 'CAMPAIGN.LIST.COLS.CODE' , type: 'text', hasConfiguration: false},
  ]

    this.campaignForm = this.formBuilder.group({
      id: '',
      code: ['', Validators.required],
      campaignType: ['', Validators.required],
      campaignStatus: ['', Validators.required],
      configurationMLS: this.formBuilder.array([this.createConfigurationMLS()]),
    });
  }
  onEdit(index: number, campaign: Campaign): void {
    this.router.navigate(['/campaign/edit/' + campaign.id]);
  }

  list(event?: LazyLoadEvent): void {
    this.data = null;
    const state = JSON.parse(localStorage.getItem('stateCampaignDataTable'));
    const page = Math.floor(event?.first / event?.rows) + 1 || Math.floor(state?.first / state?.rows) + 1 || 1;
    const size = event?.rows || state?.rows ||  10;
    const filters = event?.filters || state?.filters || {};
    this.campaignService.list(new PageOptions( page - 1, size ), new FilterOptions( filters )).subscribe((data) => {
      this.data = data ;
    });
  }

  createConfigurationMLS(): FormGroup {
    this.configurationMLS = this.formBuilder.group({
      label: ['', Validators.required],
      comment: ['', Validators.required],
      languageCode: 'fr',
    });
    return this.configurationMLS;
  }

  delete(id: string) {
    this.campaignService.delete(id).subscribe((data) => {
      this.list();
    });
  }

  selectConfiguration(configurationMLS: ConfigurationML[]) {
    return configurationMLS.find(x => x.languageCode === this.cookiesService.get("lang")) || {};
  }

  onCreateNew() {
    this.router.navigate(['/campaign/add']);
}

  onDuplicate(index: number, campaign: Campaign) {
    this.router.navigate(['/campaign/duplicate/' + campaign.id]);
  }
  clickChevron(){
    this.chevron = !this.chevron;
  }

  getScenarioTestByScenarioId(id:string){
    return this.scenarioTestService.getScenarioTestByScenarioId(id).subscribe(data =>{
      this.test = data.content
    });
  }
}
