import {
  ChangeDetectorRef,
  Component,
  EventEmitter,
  OnInit,
} from '@angular/core';
import { Campaign } from '../../../../shared/models/campaign';
import { Pageable } from '../../../../shared/models/pageable';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ConfigurationML } from '../../../../shared/models/configurationML';
import { PageOptions } from '../../../../shared/config/page/pageOptions';
import { CampaignService } from '../../../campaign/campaign.service';
import { Observable } from 'rxjs';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { switchMap } from 'rxjs/operators';
import { FilterOptions } from '../../../../shared/config/page/filterOptions';
@Component({
  templateUrl: 'create-campaignml.component.html',
  styleUrls: ['./create-campaignml.component.scss'],
})
export class CreateCampaignMLComponent implements OnInit {
  data: Pageable<Campaign>;

  campaignForm: FormGroup;
  configurationMLS: FormGroup;

  loading = false;
  dataBuffer: Campaign[];

  searchInput = new EventEmitter<string>();

  page = 0;

  listLang = ['fr', 'en'];
  slistLang = [];
  index: number;
  term = {};
  campaign: Campaign;
  configurationML: ConfigurationML;
  languageCode = '';

  public event: EventEmitter<any> = new EventEmitter();

  constructor(
    private campaignService: CampaignService,
    private formBuilder: FormBuilder,
    public bsModalRef: BsModalRef,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    if (this.campaign) {
      this.configurationML = this.campaign.configurationMLS.find(conf => conf.languageCode === this.languageCode);
      this.index = this.campaign.configurationMLS.findIndex(conf => conf.languageCode === this.languageCode);
      }

    this.campaignForm = this.formBuilder.group({
      campaign: [this.campaign || '' , Validators.required],
      configurationMLS: this.formBuilder.array([this.createConfigurationMLS()]),
    });


    this.loadFirst().subscribe((data) => {
      this.setCampaigns(data);
    });

    this.searchInput.pipe(switchMap((term) => this.loadFirst(term))).subscribe(
      (data) => {
        this.setCampaigns(data);
        this.cd.markForCheck();
      },
      (err) => {
        console.log('error', err);
        this.cd.markForCheck();
      }
    );
  }

  createConfigurationMLS(): FormGroup {
    this.configurationMLS = this.formBuilder.group({
      label: [ this.configurationML?.label || '', Validators.required],
      comment: [  this.configurationML?.comment || '', Validators.required],
      languageCode: [ this.configurationML?.languageCode || '' , Validators.required],
    });
    return this.configurationMLS;
  }

  onSubmitCampaign(form: FormGroup): void {
    const campaign = form.value.campaign;
    const configurationML = form.value.configurationMLS[0];
    if (this.campaign) {
      campaign.configurationMLS.splice(this.index, 1);
    }
    campaign.configurationMLS.push(configurationML);
    this.event.emit(this.campaignService
      .update(campaign.id, campaign as Campaign));
  }

  changeCampaign(event: Campaign): void {
    this.slistLang = this.listLang.filter((lang) =>
      event
        ? event.configurationMLS.findIndex((x) => x.languageCode === lang ) ===
          -1
        : true
    );
    this.configurationMLS.reset();
  }

  loadCampaigns({ end }): void {
    if (this.loading || this.data.totalItems <= this.dataBuffer.length) {
      return;
    }

    if (end + 5 > this.dataBuffer.length) {
      this.loadNext();
    }
  }

  loadFirst(term?: string): Observable<Pageable<Campaign>> {
    this.term = term ? { code: { value: term } } : {};
    this.loading = true;
    this.page = 0;
    this.dataBuffer = [];
    return this.campaignService.list(
      new PageOptions(this.page++, 5),
      new FilterOptions(this.term)
    );
  }

  loadNext(): void {
    this.loading = true;
    this.campaignService
      .list(new PageOptions(this.page++, 5), new FilterOptions(this.term))
      .subscribe((data) => this.setCampaigns(data));
  }

  setCampaigns(data: Pageable<Campaign>): void {
    this.data = data;
    this.dataBuffer =
      this.dataBuffer.length > 0
        ? this.dataBuffer.concat(this.data.content)
        : this.data.content;
    this.loading = false;
  }
}
