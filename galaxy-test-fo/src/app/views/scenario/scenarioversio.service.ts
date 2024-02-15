import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {GenericService} from '../../shared/services/generic.service';
import {Scenario} from '../../shared/models/scenario';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ScenarioTest } from '../../shared/models/scenarioTest';
import { PageOptions } from '../../shared/config/page/pageOptions';
import { ApplicationVersion } from '../../shared/models/application-version';
import { Pageable } from '../../shared/models/pageable';
import { ScenarioVersion } from '../../shared/models/scenarioVersion';
import { Application } from '../../shared/models/application';

@Injectable()
export class ScenarioVersionService extends GenericService<ScenarioVersion> {

  constructor(httpClient: HttpClient) {
    super(
      httpClient,
      'api/scenarioVersion');

  }
  getApplicationByScenarioId( id:string){
    return this.httpClient.get(`${this.host + this.endpoint}/application/${id}`).pipe(map(data => data as Application));
  }
  getVersionsByScenarioId( id:string){
    return this.httpClient.get(`${this.host + this.endpoint}/version/${id}`).pipe(map(data => data as Pageable<ApplicationVersion>));
  }
}