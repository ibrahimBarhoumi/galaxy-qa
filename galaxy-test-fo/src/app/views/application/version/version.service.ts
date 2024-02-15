import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { PageOptions } from '../../../shared/config/page/pageOptions';
import { ApplicationVersion } from '../../../shared/models/application-version';
import { Pageable } from '../../../shared/models/pageable';
import { ScenarioVersion } from '../../../shared/models/scenarioVersion';
import { GenericService } from '../../../shared/services/generic.service';

@Injectable()
export class VersionService extends GenericService<ApplicationVersion> {
  scenarioVersionEndpoint = "api/scenarioVersion"
  constructor(httpClient: HttpClient) {
    super(
      httpClient,
      'api/applicationVersion');
}
updateConfigurationMl(id: string, languageCode: string, item: ApplicationVersion): Observable<ApplicationVersion> {
  return this.httpClient
    .put<ApplicationVersion>(`${this.host + this.endpoint}/${id}/${languageCode}`,
      item)
    .pipe(map(data => data as ApplicationVersion));
}
getVersionsByApplicationId( id:string, pageOptions?: PageOptions){
  return this.httpClient.get(`${this.host + this.endpoint}/application/${id}?${pageOptions?.toQueryString()}`).pipe(map(data => data as Pageable<ApplicationVersion>));
}
getScenarioByVersionId( id:string, pageOptions?: PageOptions){
  return this.httpClient.get(`${this.host + this.scenarioVersionEndpoint}/${id}?${pageOptions?.toQueryString()}`).pipe(map(data => data as Pageable<ScenarioVersion>));
}
}
