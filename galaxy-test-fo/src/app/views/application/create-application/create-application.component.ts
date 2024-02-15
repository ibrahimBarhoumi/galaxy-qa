import {
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  OnInit,
} from '@angular/core';

import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ConfigurationML } from '../../../shared/models/configurationML';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { TranslateService } from '@ngx-translate/core';
import { ApplicationStatus } from '../../../shared/enums/applicationStatus.enum';
import { ApplicationService } from '../application.service';
import { Application } from '../../../shared/models/application';
import { CookieService } from 'ngx-cookie-service';
import { PageOptions } from '../../../shared/config/page/pageOptions';
import { FilterOptions } from '../../../shared/config/page/filterOptions';
import { Pageable } from '../../../shared/models/pageable';
import { ApplicationVersion } from '../../../shared/models/application-version';
import { switchMap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { VersionService } from '../version/version.service';
@Component({
  templateUrl: 'create-application.component.html',
  styleUrls: ['./create-application.component.scss'],
})
export class CreateApplicationComponent implements OnInit {

  applicationForm: FormGroup;
  configurationMLS: FormGroup;
  configurationML: ConfigurationML;
  configurationMlList: ConfigurationML [];
  indexConfig: number;
  isOperating = false;
  loading = false;
  listAppPhase = ['DEV', 'PROD', 'PREPROD'];
  exist: boolean;
  deleted: boolean;
  language: string;
  duplicateMode: boolean;
  application: Application;
  page = 0;
  term = {};
  dataBuffer: ApplicationVersion[];
  data: Pageable<ApplicationVersion>;
  searchInput = new EventEmitter<string>();
  public event: EventEmitter<any> = new EventEmitter();
  constructor(
    private translateService: TranslateService,
    private applicationService: ApplicationService,
    private formBuilder: FormBuilder,
    public bsModalRef: BsModalRef,
    private cookiesService: CookieService,
    private cd: ChangeDetectorRef,
    private versionService: VersionService
  ) {}

  ngOnInit(): void {
    if (!this.duplicateMode) {
      this.applicationForm = this.formBuilder.group({
        id: [this.application?.id || ''],
        code: [this.application?.code || '', Validators.required],
        applicationStatus: [
           this.isOperating ? ApplicationStatus.OPERATING : ApplicationStatus.ENDED,
          Validators.required,
        ],
        applicationPhase: [this.application?.applicationPhase , Validators.required],
        configurationMLS: this.formBuilder.array([this.createConfigurationMLS()]),
        
      });
    } else if (this.duplicateMode) {
      this.applicationForm = this.formBuilder.group({
        id: [''],
        code: [ '', Validators.required],
        applicationStatus: [
           this.isOperating ? ApplicationStatus.OPERATING : ApplicationStatus.ENDED,
          Validators.required,
        ],
        applicationPhase: [this.application.applicationPhase , Validators.required],
        configurationMLS: this.formBuilder.array([this.createConfigurationMLS()]),
       
      });
    }


    if (this.application) {
      this.configurationMlList = this.application.configurationMLS;
      if (this.application.applicationStatus == ApplicationStatus.OPERATING) {
        this.isOperating = false;
      } else {
        this.isOperating = true;
      }
      this.configurationML = this.application.configurationMLS.find(x => x.languageCode === this.cookiesService.get('lang')) || {};
      this.applicationForm
      .get('configurationMLS')
      ['controls'][0].controls['label'].setValue(
        this.configurationML.label
      );
    this.applicationForm.get('configurationMLS')['controls'][0].controls['comment'].setValue(
        this.configurationML.comment
      );

      this.indexConfig = this.application.configurationMLS.findIndex(x => x.languageCode === this.cookiesService.get('lang'));
    }
    this.loadFirst().subscribe((data) => {
      this.setVersions(data);
    });
    this.searchInput.pipe(switchMap((term) => this.loadFirst(term))).subscribe(
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

  createConfigurationMLS(): FormGroup {
    this.configurationMLS = this.formBuilder.group({
      label: [ this.configurationML?.label || '', Validators.required],
      comment: [  this.configurationML?.comment || '', Validators.required],
       languageCode: this.translateService.currentLang,
    });
    return this.configurationMLS;
  }

  onSubmitApplication(form: FormGroup): void {

    form.controls['applicationStatus'].setValue(
      this.isOperating ?  ApplicationStatus.ENDED :  ApplicationStatus.OPERATING
    );
    if (this.duplicateMode) {
      this.application = form.value;
      this.application.configurationMLS = this.configurationMlList;
      this.event.emit(this.applicationService.create(this.application as Application));
    } else if (!this.application?.id && !this.duplicateMode) {

   this.event.emit(this.applicationService.create(form.value as Application));
    } else {

      this.configurationML = form.value.configurationMLS[0];
      if (this.indexConfig != -1) {
        this.configurationMlList.splice(this.indexConfig, 1, this.configurationML);
      } else {
        this.configurationMlList.push(this.configurationML);
      }
      this.application = form.value;
      this.application.configurationMLS = this.configurationMlList;
      this.event.emit(this.applicationService.update(this.application.id, this.application as Application));
    }
  }
  loadVersions({ end }): void {
    if (this.loading || this.data.totalItems <= this.dataBuffer.length) {
      return;
    }

    if (end + 5 > this.dataBuffer.length) {
      this.loadNext();
    }
  }

  loadNext(): void {
    this.loading = true;
    this.versionService
      .list(new PageOptions(this.page++, 5), new FilterOptions(this.term))
      .subscribe((data) => this.setVersions(data));
  }

  setVersions(data: Pageable<ApplicationVersion>): void {
    this.data = data;
    this.dataBuffer =
      this.dataBuffer.length > 0
        ? this.dataBuffer.concat(this.data.content)
        : this.data.content;
    this.loading = false;
  }

  loadFirst(term?: string): Observable<Pageable<ApplicationVersion>> {
    this.term = term ? { code: { value: term } } : {};
    this.loading = true;
    this.page = 0;
    this.dataBuffer = [];
    return this.versionService.list(
      new PageOptions(this.page++, 5),
      new FilterOptions(this.term)
    );
  }
  changeVersion($event){}
}
