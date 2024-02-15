import {
  ChangeDetectorRef,
  Component,
  EventEmitter,
  OnInit,
} from '@angular/core';
import { Pageable } from '../../../../shared/models/pageable';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ConfigurationML } from '../../../../shared/models/configurationML';
import { PageOptions } from '../../../../shared/config/page/pageOptions';
import { Observable } from 'rxjs';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { switchMap } from 'rxjs/operators';
import { FilterOptions } from '../../../../shared/config/page/filterOptions';
import { ApplicationVersion } from '../../../../shared/models/application-version';
import { VersionService } from '../../../application/version/version.service';
@Component({
  templateUrl: 'modal-appversionml.component.html',
  styleUrls: ['./modal-appversionml.component.scss'],
})
export class ModalAppVersionMLComponent implements OnInit {
  data: Pageable<ApplicationVersion>;

  appVersionForm: FormGroup;
  configurationMLS: FormGroup;

  loading = false;
  dataBuffer: ApplicationVersion[];

  searchInput = new EventEmitter<string>();

  page = 0;

  listLang = ['fr', 'en'];
  slistLang = [];
  index: number;
  term = {};
  applicationVersion: ApplicationVersion;
  configurationML: ConfigurationML;
  languageCode = '';

  public event: EventEmitter<any> = new EventEmitter();

  constructor(
    private versionService: VersionService,
    private formBuilder: FormBuilder,
    public bsModalRef: BsModalRef,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    if (this.applicationVersion) {
      this.configurationML = this.applicationVersion.configurationMLS.find(conf => conf.languageCode === this.languageCode);
      this.index = this.applicationVersion.configurationMLS.findIndex(conf => conf.languageCode === this.languageCode);
      }

    this.appVersionForm = this.formBuilder.group({
      applicationVersion: [this.applicationVersion || '' , Validators.required],
      configurationMLS: this.formBuilder.array([this.createConfigurationMLS()]),
    });


    this.loadFirst().subscribe((data) => {
      this.setAppVersions(data);
    });

    this.searchInput.pipe(switchMap((term) => this.loadFirst(term))).subscribe(
      (data) => {
        this.setAppVersions(data);
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

  onSubmitAppVersion(form: FormGroup): void {
    const applicationVersion = form.value.applicationVersion;
    const configurationML = form.value.configurationMLS[0];
    if (this.applicationVersion) {
      applicationVersion.configurationMLS.splice(this.index, 1);
    }
    applicationVersion.configurationMLS.push(configurationML);
    this.event.emit(this.versionService
      .update(applicationVersion.id, applicationVersion as ApplicationVersion));
  }

  changeAppVersion(event: ApplicationVersion): void {
    this.slistLang = this.listLang.filter((lang) =>
      event
        ? event.configurationMLS.findIndex((x) => x.languageCode === lang ) ===
          -1
        : true
    );
    this.configurationMLS.reset();
  }

  loadAppVersions({ end }): void {
    if (this.loading || this.data.totalItems <= this.dataBuffer.length) {
      return;
    }

    if (end + 5 > this.dataBuffer.length) {
      this.loadNext();
    }
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

  loadNext(): void {
    this.loading = true;
    this.versionService
      .list(new PageOptions(this.page++, 5), new FilterOptions(this.term))
      .subscribe((data) => this.setAppVersions(data));
  }

  setAppVersions(data: Pageable<ApplicationVersion>): void {
    this.data = data;
    this.dataBuffer =
      this.dataBuffer.length > 0
        ? this.dataBuffer.concat(this.data.content)
        : this.data.content;
    this.loading = false;
  }
}
