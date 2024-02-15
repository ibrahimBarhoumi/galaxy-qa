import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DataTestCase } from '../../shared/models/dataTestCase';
import { GenericService } from '../../shared/services/generic.service';

@Injectable()
export class DataTestCaseService extends GenericService<DataTestCase> {
  
  private baseURL ="http://localhost:8082/api";

  constructor(httpClient: HttpClient) {
    super(
      httpClient,
      'api/datatestcase');
}
}
