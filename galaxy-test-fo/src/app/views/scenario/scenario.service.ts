import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {GenericService} from '../../shared/services/generic.service';
import {Scenario} from '../../shared/models/scenario';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable()
export class ScenarioService extends GenericService<Scenario> {

  constructor(httpClient: HttpClient) {
    super(
      httpClient,
      'api/scenario');
  }
  updateConfigurationMl(id: string, languageCode: string, item: Scenario): Observable<Scenario> {
    return this.httpClient
      .put<Scenario>(`${this.host + this.endpoint}/${id}/${languageCode}`,
        item)
      .pipe(map(data => data as Scenario));
  }
}
