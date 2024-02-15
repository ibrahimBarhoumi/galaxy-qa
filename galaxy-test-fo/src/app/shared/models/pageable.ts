export interface Pageable<T> {
  totalPages?: number;
  totalItems?: number;
  currentPage?: number;
  first?: boolean;
  last?: boolean;
  itemsPerPage?: number;
  pageSize?: number;
  content?: T[];
}
