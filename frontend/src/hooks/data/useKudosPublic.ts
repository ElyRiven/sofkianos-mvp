import { useState, useEffect, useCallback } from 'react';
import { kudosService } from '../../services/api/kudosService';
import type { KudoListParams, PagedKudoResponse } from '../../types/models/kudoPublic';

/**
 * Hook que gestiona el estado de la lista pública de kudos.
 * Maneja la carga, error, paginación y refetch.
 *
 * @function useKudosPublic
 * @param {KudoListParams} [initialParams] - Parámetros iniciales de búsqueda.
 * @returns {{
 *   data: PagedKudoResponse | null,
 *   isLoading: boolean,
 *   error: Error | null,
 *   params: KudoListParams,
 *   goToPage: (page: number) => void,
 *   setParams: (params: Partial<KudoListParams>) => void,
 *   refetch: () => void
 * }} Estado y acciones del listado de kudos.
 *
 * @example
 * ```tsx
 * const { data, isLoading, error, goToPage } = useKudosPublic();
 * ```
 */
export function useKudosPublic(initialParams: KudoListParams = {}) {
  const [data, setData] = useState<PagedKudoResponse | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);
  const [params, setParamsState] = useState<KudoListParams>({
    page: 0,
    size: 20,
    sortDirection: 'DESC',
    ...initialParams,
  });
  const [fetchTrigger, setFetchTrigger] = useState(0);

  useEffect(() => {
    const controller = new AbortController();

    const fetchData = async () => {
      setIsLoading(true);
      setError(null);
      try {
        const result = await kudosService.list(params);
        if (!controller.signal.aborted) {
          setData(result);
        }
      } catch (err) {
        if (!controller.signal.aborted) {
          setError(err instanceof Error ? err : new Error(String(err)));
        }
      } finally {
        if (!controller.signal.aborted) {
          setIsLoading(false);
        }
      }
    };

    fetchData();

    return () => {
      controller.abort();
    };
  }, [params, fetchTrigger]);

  const goToPage = useCallback((page: number) => {
    setParamsState((prev) => ({ ...prev, page }));
  }, []);

  const setParams = useCallback((newParams: Partial<KudoListParams>) => {
    setParamsState((prev) => ({ ...prev, ...newParams, page: 0 }));
  }, []);

  const refetch = useCallback(() => {
    setFetchTrigger((n) => n + 1);
  }, []);

  return { data, isLoading, error, params, goToPage, setParams, refetch };
}
