import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Application } from '../../shared/models/application';
import { GenericService } from '../../shared/services/generic.service';

@Injectable()
export class ApplicationService extends GenericService<Application> {

  constructor(httpClient: HttpClient) {
    super(
      httpClient,
      'api/application');
}
updateConfigurationMl(id: string, languageCode: string, item: Application): Observable<Application> {
  return this.httpClient
    .put<Application>(`${this.host + this.endpoint}/${id}/${languageCode}`,
      item)
    .pipe(map(data => data as Application));
}
}