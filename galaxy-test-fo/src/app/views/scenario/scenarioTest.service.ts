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

@Injectable()
export class ScenarioTestService extends GenericService<ScenarioTest> {

  constructor(httpClient: HttpClient) {
    super(
      httpClient,
      'api/scenarioTest');

  }
  getScenarioTestByScenarioId( id:string, pageOptions?: PageOptions){
    return this.httpClient.get(`${this.host + this.endpoint}/${id}?${pageOptions?.toQueryString()}`).pipe(map(data => data as Pageable<ScenarioTest>));
  }
}