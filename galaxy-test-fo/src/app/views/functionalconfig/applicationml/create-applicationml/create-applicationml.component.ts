import {
  ChangeDetectorRef,
  Component,
  EventEmitter,
  OnInit,
} from '@angular/core';
import { Application } from '../../../../shared/models/application';
import { Pageable } from '../../../../shared/models/pageable';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ConfigurationML } from '../../../../shared/models/configurationML';
import { PageOptions } from '../../../../shared/config/page/pageOptions';
import { ApplicationService } from '../../../application/application.service';
import { Observable } from 'rxjs';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { switchMap } from 'rxjs/operators';
import { FilterOptions } from '../../../../shared/config/page/filterOptions';
@Component({
  templateUrl: 'create-applicationml.component.html',
  styleUrls: ['./create-applicationml.component.scss'],
})
export class CreateApplicationMLComponent implements OnInit {
  data: Pageable<Application>;

  applicationForm: FormGroup;
  configurationMLS: FormGroup;

  loading = false;
  dataBuffer: Application[];

  searchInput = new EventEmitter<string>();

  page = 0;

  listLang = ['fr', 'en'];
  slistLang = [];
  index: number;
  term = {};
  application: Application;
  configurationML: ConfigurationML;
  languageCode = '';

  public event: EventEmitter<any> = new EventEmitter();

  constructor(
    private applicationService: ApplicationService,
    private formBuilder: FormBuilder,
    public bsModalRef: BsModalRef,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    if (this.application) {
      this.configurationML = this.application.configurationMLS.find(conf => conf.languageCode === this.languageCode);
      this.index = this.application.configurationMLS.findIndex(conf => conf.languageCode === this.languageCode);
      }

    this.applicationForm = this.formBuilder.group({
      application: [this.application || '' , Validators.required],
      configurationMLS: this.formBuilder.array([this.createConfigurationMLS()]),
    });


    this.loadFirst().subscribe((data) => {
      this.setApplications(data);
    });

    this.searchInput.pipe(switchMap((term) => this.loadFirst(term))).subscribe(
      (data) => {
        this.setApplications(data);
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

  onSubmitApplication(form: FormGroup): void {
    const application = form.value.application;
    const configurationML = form.value.configurationMLS[0];
    if (this.application) {
      application.configurationMLS.splice(this.index, 1);
    }
    application.configurationMLS.push(configurationML);
    this.event.emit(this.applicationService
      .update(application.id, application as Application));
  }

  changeApplication(event: Application): void {
    this.slistLang = this.listLang.filter((lang) =>
      event
        ? event.configurationMLS.findIndex((x) => x.languageCode === lang ) ===
          -1
        : true
    );
    this.configurationMLS.reset();
  }

  loadApplications({ end }): void {
    if (this.loading || this.data.totalItems <= this.dataBuffer.length) {
      return;
    }

    if (end + 5 > this.dataBuffer.length) {
      this.loadNext();
    }
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

  loadNext(): void {
    this.loading = true;
    this.applicationService
      .list(new PageOptions(this.page++, 5), new FilterOptions(this.term))
      .subscribe((data) => this.setApplications(data));
  }

  setApplications(data: Pageable<Application>): void {
    this.data = data;
    this.dataBuffer =
      this.dataBuffer.length > 0
        ? this.dataBuffer.concat(this.data.content)
        : this.data.content;
    this.loading = false;
  }
}
