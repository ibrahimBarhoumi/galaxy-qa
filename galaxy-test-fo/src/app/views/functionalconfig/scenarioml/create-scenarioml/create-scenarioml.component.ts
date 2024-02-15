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
import { Scenario } from '../../../../shared/models/scenario';
import { ScenarioService } from '../../../scenario/scenario.service';
@Component({
  templateUrl: 'create-scenarioml.component.html',
  styleUrls: ['./create-scenarioml.component.scss'],
})
export class CreateScenarioMLComponent implements OnInit {
  data: Pageable<Scenario>;

  scenarioForm: FormGroup;
  configurationMLS: FormGroup;

  loading = false;
  dataBuffer: Scenario[];

  searchInput = new EventEmitter<string>();

  page = 0;

  listLang = ['fr', 'en'];
  slistLang = [];
  index: number;
  term = {};
  scenario: Scenario;
  configurationML: ConfigurationML;
  languageCode = '';

  public event: EventEmitter<any> = new EventEmitter();

  constructor(
    private scenarioService: ScenarioService,
    private formBuilder: FormBuilder,
    public bsModalRef: BsModalRef,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    if (this.scenario) {
      this.configurationML = this.scenario.configurationMLS.find(conf => conf.languageCode === this.languageCode);
      this.index = this.scenario.configurationMLS.findIndex(conf => conf.languageCode === this.languageCode);
      }

    this.scenarioForm = this.formBuilder.group({
      scenario: [this.scenario || '' , Validators.required],
      configurationMLS: this.formBuilder.array([this.createConfigurationMLS()]),
    });


    this.loadFirst().subscribe((data) => {
      this.setScenarios(data);
    });

    this.searchInput.pipe(switchMap((term) => this.loadFirst(term))).subscribe(
      (data) => {
        this.setScenarios(data);
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

  onSubmitScenario(form: FormGroup): void {
    const scenario = form.value.scenario;
    const configurationML = form.value.configurationMLS[0];
    if (this.scenario) {
      scenario.configurationMLS.splice(this.index, 1);
    }
    scenario.configurationMLS.push(configurationML);
    this.event.emit(this.scenarioService
      .update(scenario.id, scenario as Scenario));
  }

  changeScenario(event: Scenario): void {
    this.slistLang = this.listLang.filter((lang) =>
      event
        ? event.configurationMLS.findIndex((x) => x.languageCode === lang ) ===
          -1
        : true
    );
    this.configurationMLS.reset();
  }

  loadScenarios({ end }): void {
    if (this.loading || this.data.totalItems <= this.dataBuffer.length) {
      return;
    }

    if (end + 5 > this.dataBuffer.length) {
      this.loadNext();
    }
  }

  loadFirst(term?: string): Observable<Pageable<Scenario>> {
    this.term = term ? { code: { value: term } } : {};
    this.loading = true;
    this.page = 0;
    this.dataBuffer = [];
    return this.scenarioService.list(
      new PageOptions(this.page++, 5),
      new FilterOptions(this.term)
    );
  }

  loadNext(): void {
    this.loading = true;
    this.scenarioService
      .list(new PageOptions(this.page++, 5), new FilterOptions(this.term))
      .subscribe((data) => this.setScenarios(data));
  }

  setScenarios(data: Pageable<Scenario>): void {
    this.data = data;
    this.dataBuffer =
      this.dataBuffer.length > 0
        ? this.dataBuffer.concat(this.data.content)
        : this.data.content;
    this.loading = false;
  }
}
