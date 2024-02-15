import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {GenericService} from '../../shared/services/generic.service';
import {Scenario} from '../../shared/models/scenario';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { TestVersion } from '../../shared/models/testVersion';
import { Pageable } from '../../shared/models/pageable';
import { Test } from '../../shared/models/test';

@Injectable()
export class TestVersionService extends GenericService<TestVersion> {

  constructor(httpClient: HttpClient) {
    super(
      httpClient,
      'api/testVersion');
  }

  getListTestByVersion(versionId: string []) {
    return this.httpClient
      .post(`${this.host + this.endpoint}/version`, versionId)
      .pipe(map(data => data as Pageable<Test>));
  }
}
