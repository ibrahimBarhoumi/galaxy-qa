import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { PageOptions } from '../../shared/config/page/pageOptions';
import { LaunchTest } from '../../shared/models/launchTest';
import { Pageable } from '../../shared/models/pageable';
import { Test } from '../../shared/models/test';
import { TestVersion } from '../../shared/models/testVersion';
import { GenericService } from '../../shared/services/generic.service';

@Injectable()
export class TestService extends GenericService<Test> {

  constructor(httpClient: HttpClient) {
    super(
      httpClient,
      'api/test');
}
updateConfigurationMl(id: string, languageCode: string, item: Test): Observable<Test> {
  return this.httpClient
    .put<Test>(`${this.host + this.endpoint}/${id}/${languageCode}`,
      item)
    .pipe(map(data => data as Test));
}
getTestsByApplicationId( id:string, pageOptions?: PageOptions){
  return this.httpClient.get(`${this.host + this.endpoint}/application/${id}?${pageOptions?.toQueryString()}`).pipe(map(data => data as Pageable<Test>));
}
getVersionsByTestId( id:string){
  return this.httpClient.get(`${this.host + this.endpoint}/${id}/versions`).pipe(map(data => data as TestVersion[]));
}
LaunchTest(launchTest: LaunchTest) {
  return this.httpClient
    .post(`${this.host + this.endpoint}/launch`, launchTest)
    .pipe(map(data => data as string));
}
}
