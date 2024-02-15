import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {GenericService} from '../../shared/services/generic.service';
import {Scenario} from '../../shared/models/scenario';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ScenarioTest } from '../../shared/models/scenarioTest';
import { PageOptions } from '../../shared/config/page/pageOptions';
import { ApplicationVersion } from '../../shared/models/application-version';
import { Pageable } from '../../shared/models/pageable';
import { DataTestCaseVersion } from '../../shared/models/dataTestCaseVersion';

@Injectable()
export class DataTestCaseVersionService extends GenericService<DataTestCaseVersionService> {

  constructor(httpClient: HttpClient) {
    super(
      httpClient,
      'api/datatestcaseversion');

  }
  getVersionByDataTestCaseId( id:string, pageOptions?: PageOptions){
    return this.httpClient.get(`${this.host + this.endpoint}/${id}?${pageOptions?.toQueryString()}`).pipe(map(data => data as Pageable<ApplicationVersion>));
  }

  upload(file,fileName: string, fileType: string,organization: string,businessKey :string ): Observable<any> {
  let params = new HttpParams()
    .set("fileName", fileName)
    .set("fileType", fileType)
    .set("organization", organization)
    .set("businessKey", businessKey);
    const data: FormData = new FormData();
    data.append('file', file);
    const HttpUploadOptions = {
      params:params
    }
  return this.httpClient.post(`${this.host + this.endpoint}/file/upload`,file,HttpUploadOptions);
}
  getVersionByTestId(testId:string){
    return this.httpClient.get(`${this.host + this.endpoint}/version/${testId}`).pipe(map(data => data as ApplicationVersion[]));
  }
  getDataTestCaseByTestIdAndVersionId(testId:string,versionId:string){
    return this.httpClient.get(`${this.host + this.endpoint}/test/${testId}/${versionId}`).pipe(map(data => data as DataTestCaseVersion[]));
  }

}
