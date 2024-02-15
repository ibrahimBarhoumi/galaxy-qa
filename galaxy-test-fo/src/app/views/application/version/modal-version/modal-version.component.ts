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
import { VersionService } from '../version.service';
import { ApplicationVersion } from '../../../../shared/models/application-version';
import { ApplicationStatus } from '../../../../shared/enums/applicationStatus.enum';
import { TranslateService } from '@ngx-translate/core';
@Component({
  templateUrl: 'modal-version.component.html',
  styleUrls: ['./modal-version.component.scss'],
})
export class ModalVersionComponent implements OnInit {
  data: Pageable<Application>;

  versionForm: FormGroup;
  configurationMLS: FormGroup;

  loading = false;
  dataBuffer: Application[];

  searchInput = new EventEmitter<string>();

  page = 0;

  listLang = ['fr', 'en'];
  slistLang = [];
  term = {};
  applicationVersion: ApplicationVersion;
  application: Application;
  configurationML: ConfigurationML;

  public event: EventEmitter<any> = new EventEmitter();

  constructor(
    private applicationService: ApplicationService,
    private versionService: VersionService,
    public bsModalRef: BsModalRef,
    private cd: ChangeDetectorRef,
    private formBuilder: FormBuilder,
    private translateService: TranslateService,
  ) {}

  ngOnInit(): void {




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



  onSubmitApplication(form: FormGroup): void {
    form.value.status = form.value.status ? ApplicationStatus.ENDED : ApplicationStatus.OPERATING;
    const version = form.value;
    const configurationML = form.value.configurationMLS[0];
    if (this.applicationVersion?.id) {
      version.configurationMLS = this.applicationVersion.configurationMLS;
     const index = version.configurationMLS.findIndex(conf => conf.languageCode === this.translateService.currentLang);
     if (index === -1) {
      version.configurationMLS.push(configurationML);
     } else {
      version.configurationMLS[index] = configurationML; }
      this.event.emit(this.versionService
        .update(this.applicationVersion.id, version as ApplicationVersion));
    } else {
      this.event.emit(this.versionService
        .create(version as ApplicationVersion));
    }
  }

  changeApplication(event: Application): void {

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
