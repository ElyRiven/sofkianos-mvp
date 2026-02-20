/**
 * Represents a single Kudo item for public display.
 * Contains only safe, non-sensitive fields.
 */
export interface KudoPublicItem {
  receptor: string;
  emisor: string;
  mensaje: string;
  fecha: string;
  categoria: string;
}

/**
 * Paginated response for Kudo list queries.
 */
export interface PagedKudoResponse {
  content: KudoPublicItem[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

/**
 * Parameters for listing kudos with optional filtering and pagination.
 */
export interface KudoListParams {
  page?: number;
  size?: number;
  sortDirection?: 'ASC' | 'DESC';
  category?: string;
  searchText?: string;
}
