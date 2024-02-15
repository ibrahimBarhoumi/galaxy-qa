import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { FilterOptions } from '../config/page/filterOptions';
import { PageOptions } from '../config/page/pageOptions';
import { Pageable } from '../models/pageable';


export class GenericService<T> {

  protected host = environment.apiHost;


  constructor(
    protected httpClient: HttpClient,
    protected endpoint: string) {
  }

  create(item: T): Observable<T> {
    return this.httpClient
      .post<T>(`${this.host + this.endpoint}`, item)
      .pipe(map(data => data as T));
  }

  update(id: string, item: T): Observable<T> {
    return this.httpClient
      .put<T>(`${this.host + this.endpoint}/${id}`,
        item)
      .pipe(map(data => data as T));
  }

  get(id: string): Observable<T> {
    return this.httpClient
      .get(`${this.host + this.endpoint}/${id}`)
      .pipe(map((data: any) => data as T));
  }

  list(pageOptions?: PageOptions, filterOptions?: FilterOptions): Observable<Pageable<T>> {
    return this.httpClient
      .get(`${this.host + this.endpoint}?${pageOptions?.toQueryString()}&${filterOptions?.toQueryString()}`)
      .pipe(map(data => data as Pageable<T>));
  }

  delete(id: string): Observable<T> {
    return this.httpClient.delete<T>(`${this.host + this.endpoint}/${id}`);
  }

}
