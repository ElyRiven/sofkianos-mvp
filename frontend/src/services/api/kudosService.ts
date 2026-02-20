import { apiClient } from "./client";
import type { KudoFormData } from "../../schemas/kudoFormSchema";
import type { KudoListParams, PagedKudoResponse } from "../../types/models/kudoPublic";

/**
 * Servicio que encapsula todas las operaciones relacionadas con Kudos.
 *
 * Proporciona métodos para interactuar con los endpoints de kudos del backend.
 * Usa el cliente HTTP centralizado {@link apiClient} para todas las peticiones.
 *
 * Métodos disponibles:
 * - **send**: Envía un kudo al backend (POST /v1/kudos)
 * - **list**: Obtiene la lista paginada de kudos públicos (GET /v1/kudos)
 *
 * Características:
 * - Validación de status code esperado (202 Accepted)
 * - Manejo de errores mediante excepciones
 * - Tipado fuerte con TypeScript
 * - Integración con esquemas de validación
 *
 * @namespace kudosService
 */
export const kudosService = {
  /**
   * Envía un kudo al backend para ser procesado.
   *
   * Realiza una petición POST al endpoint /v1/kudos con los datos del kudo.
   * El backend responde con 202 Accepted indicando que el kudo fue recibido
   * y será procesado asincrónicamente por el consumer worker.
   *
   * Flujo:
   * 1. Envía POST a /v1/kudos con payload
   * 2. Backend valida datos y publica evento en RabbitMQ
   * 3. Responde 202 Accepted si todo es correcto
   * 4. Consumer worker procesa el kudo asincrónicamente
   *
   * Códigos de respuesta:
   * - **202 Accepted**: Kudo recibido correctamente
   * - **400 Bad Request**: Datos de validación incorrectos (manejado por interceptor)
   * - **500 Internal Server Error**: Error del servidor (manejado por interceptor)
   *
   * @async
   * @function send
   * @memberof kudosService
   * @param {KudoFormData} payload - Datos del kudo a enviar.
   * @param {string} payload.from - Email del remitente.
   * @param {string} payload.to - Email del destinatario.
   * @param {string} payload.category - Categoría del kudo.
   * @param {string} payload.message - Mensaje del kudo (10-500 caracteres).
   * @returns {Promise<void>} Promesa que se resuelve cuando el kudo es aceptado.
   * @throws {Error} Si el status code no es 202.
   * @throws {AxiosError} Si hay error de red o el servidor responde con error.
   *
   * @example
   * await kudosService.send({
   *   from: 'sender@sofkianos.com',
   *   to: 'recipient@sofkianos.com',
   *   category: 'Colaboración',
   *   message: 'Excelente trabajo en el proyecto X'
   * });
   */
  send: async (payload: KudoFormData): Promise<void> => {
    const response = await apiClient.post("/v1/kudos", payload);
    if (response.status !== 202) {
      throw new Error(`Unexpected status: ${response.status}`);
    }
  },

  /**
   * Obtiene la lista paginada de kudos públicos desde el API.
   *
   * @async
   * @function list
   * @memberof kudosService
   * @param {KudoListParams} [params] - Parámetros de paginación y filtrado.
   * @param {number} [params.page=0] - Página actual (base 0).
   * @param {number} [params.size=20] - Tamaño de página (máximo 50).
   * @param {string} [params.sortDirection='DESC'] - Dirección de ordenamiento.
   * @param {string} [params.category] - Filtrar por categoría.
   * @param {string} [params.searchText] - Texto de búsqueda libre.
   * @returns {Promise<PagedKudoResponse>} Promesa con la lista paginada de kudos.
   *
   * @example
   * const { content, totalElements } = await kudosService.list({ page: 0, size: 20 });
   */
  list: async (params: KudoListParams = {}): Promise<PagedKudoResponse> => {
    const { page = 0, size = 20, sortDirection = 'DESC', ...rest } = params;
    const response = await apiClient.get("/v1/kudos", {
      params: { page, size, sortDirection, ...rest },
    });
    return response.data as PagedKudoResponse;
  },
};

export default kudosService;
