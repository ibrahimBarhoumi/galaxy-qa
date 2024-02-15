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
import { Test } from '../../../../shared/models/test';
import { TestService } from '../../../test/test.service';
@Component({
  templateUrl: 'modal-testml.component.html',
  styleUrls: ['./modal-testml.component.scss'],
})
export class ModalTestMLComponent implements OnInit {
  data: Pageable<Test>;

  testForm: FormGroup;
  configurationMLS: FormGroup;

  loading = false;
  dataBuffer: Test[];

  searchInput = new EventEmitter<string>();

  page = 0;

  listLang = ['fr', 'en'];
  slistLang = [];
  index: number;
  term = {};
  test: Test;
  configurationML: ConfigurationML;
  languageCode = '';

  public event: EventEmitter<any> = new EventEmitter();

  constructor(
    private testService: TestService,
    private formBuilder: FormBuilder,
    public bsModalRef: BsModalRef,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    if (this.test) {
      this.configurationML = this.test.configurationMLS.find(conf => conf.languageCode === this.languageCode);
      this.index = this.test.configurationMLS.findIndex(conf => conf.languageCode === this.languageCode);
      }

    this.testForm = this.formBuilder.group({
      test: [this.test || '' , Validators.required],
      configurationMLS: this.formBuilder.array([this.createConfigurationMLS()]),
    });


    this.loadFirst().subscribe((data) => {
      this.setTests(data);
    });

    this.searchInput.pipe(switchMap((term) => this.loadFirst(term))).subscribe(
      (data) => {
        this.setTests(data);
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

  onSubmitTest(form: FormGroup): void {
    const test = form.value.test;
    const configurationML = form.value.configurationMLS[0];
    if (this.test) {
      test.configurationMLS.splice(this.index, 1);
    }
    test.configurationMLS.push(configurationML);
    this.event.emit(this.testService
      .update(test.id, test as Test));
  }

  changeTest(event: Test): void {
    this.slistLang = this.listLang.filter((lang) =>
      event
        ? event.configurationMLS.findIndex((x) => x.languageCode === lang ) ===
          -1
        : true
    );
    this.configurationMLS.reset();
  }

  loadTests({ end }): void {
    if (this.loading || this.data.totalItems <= this.dataBuffer.length) {
      return;
    }

    if (end + 5 > this.dataBuffer.length) {
      this.loadNext();
    }
  }

  loadFirst(term?: string): Observable<Pageable<Test>> {
    this.term = term ? { code: { value: term } } : {};
    this.loading = true;
    this.page = 0;
    this.dataBuffer = [];
    return this.testService.list(
      new PageOptions(this.page++, 5),
      new FilterOptions(this.term)
    );
  }

  loadNext(): void {
    this.loading = true;
    this.testService
      .list(new PageOptions(this.page++, 5), new FilterOptions(this.term))
      .subscribe((data) => this.setTests(data));
  }

  setTests(data: Pageable<Test>): void {
    this.data = data;
    this.dataBuffer =
      this.dataBuffer.length > 0
        ? this.dataBuffer.concat(this.data.content)
        : this.data.content;
    this.loading = false;
  }
}
