export class PageOptions {


  constructor(private page: number, private size: number) {
    this.page = page;
    this.size = size;
  }

  toQueryMap(): Map<string, string> {
    const queryMap = new Map<string, string>();
    queryMap.set('page', `${this.page}`);
    queryMap.set('size', `${this.size}`);

    return queryMap;
  }

  toQueryString(): string {
    let queryString = '';
    this.toQueryMap().forEach((value: string, key: string) => {
      queryString = queryString.concat(`${key}=${value}&`);
    });

    return queryString.substring(0, queryString.length - 1);
  }
}
