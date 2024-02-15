export class FilterOptions {

  constructor(private filters: any) {
    this.filters = filters;
  }

  toQueryMap(): Map<string, string> {
    const queryMap = new Map<string, string>();
    const filters = this.filters;
    Object.keys(filters).forEach(function(key) {
      queryMap.set(key, filters[key].value);
    });
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
