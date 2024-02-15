import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Campaign } from '../../shared/models/campaign';
import { GenericService } from '../../shared/services/generic.service';

@Injectable()
export class CampaignService extends GenericService<Campaign> {

  constructor(httpClient: HttpClient) {
    super(
      httpClient,
      'api/campaigntest');
}
updateConfigurationMl(id: string, languageCode: string, item: Campaign): Observable<Campaign> {
  return this.httpClient
    .put<Campaign>(`${this.host + this.endpoint}/${id}/${languageCode}`,
      item)
    .pipe(map(data => data as Campaign));
}
getCampaignByScenarioId(id: string) {
  return this.httpClient
    .get<Campaign>(`${this.host + this.endpoint}/scenario/${id}`)
    .pipe(map(data => data as Campaign []));
}
}
