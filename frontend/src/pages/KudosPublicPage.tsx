import React from 'react';
import { useKudosPublic } from '../hooks/data/useKudosPublic';

/**
 * Formats a date string to a localized display string.
 *
 * @param {string} fecha - ISO date string
 * @returns {string} Formatted date
 */
function formatDate(fecha: string): string {
  try {
    return new Date(fecha).toLocaleDateString('es-CO', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  } catch {
    return fecha;
  }
}

/**
 * Public page that displays the list of kudos visible to all company users.
 * No authentication required.
 *
 * @component KudosPublicPage
 * @returns {JSX.Element} Rendered kudos public page
 *
 * @example
 * ```tsx
 * <Route path="/kudos-public" element={<KudosPublicPage />} />
 * ```
 */
const KudosPublicPage: React.FC = () => {
  const { data, isLoading, error, goToPage } = useKudosPublic();

  return (
    <div className="min-h-screen bg-zinc-950 text-zinc-100">
      <div className="fixed inset-0 -z-10 bg-[radial-gradient(circle_at_top,_var(--tw-gradient-stops))] from-brand/10 via-zinc-950 to-zinc-950" />

      <div className="mx-auto w-full max-w-4xl px-4 py-28 sm:px-6">
        <div className="mb-12 text-center">
          <h1 className="text-3xl font-bold tracking-tight text-white sm:text-5xl">
            Kudos{' '}
            <span className="text-[#FF5F00] drop-shadow-[0_0_15px_rgba(255,95,0,0.5)]">
              Públicos
            </span>
          </h1>
          <p className="mx-auto mt-6 max-w-lg text-lg text-zinc-400">
            Reconocimientos de nuestra comunidad SofkianOS
          </p>
        </div>

        {isLoading && (
          <p className="text-center text-zinc-400">Cargando...</p>
        )}

        {error && !isLoading && (
          <p className="text-center text-red-400">
            Error al cargar los kudos. Por favor, intenta de nuevo.
          </p>
        )}

        {!isLoading && !error && data && data.content.length === 0 && (
          <p className="text-center text-zinc-400">No hay kudos registrados aún</p>
        )}

        {!isLoading && !error && data && data.content.length > 0 && (
          <>
            <div
              data-testid="kudos-public-list"
              className="grid gap-6 sm:grid-cols-2"
            >
              {data.content.map((kudo, index) => (
                <div
                  key={index}
                  data-testid="kudo-card"
                  className="rounded-2xl border border-zinc-800 bg-zinc-900 p-6 shadow-lg"
                >
                  <div className="mb-3 flex items-center justify-between">
                    <span className="rounded-full bg-[#FF5F00]/10 px-3 py-1 text-xs font-semibold text-[#FF5F00]">
                      {kudo.categoria}
                    </span>
                    <span className="text-xs text-zinc-500">{formatDate(kudo.fecha)}</span>
                  </div>
                  <p className="mb-4 text-zinc-100">{kudo.mensaje}</p>
                  <div className="flex items-center justify-between text-sm text-zinc-400">
                    <span>
                      Para: <span className="font-semibold text-zinc-200">{kudo.receptor}</span>
                    </span>
                    <span>
                      De: <span className="font-semibold text-zinc-200">{kudo.emisor}</span>
                    </span>
                  </div>
                </div>
              ))}
            </div>

            {data.totalPages > 1 && (
              <nav
                data-testid="kudos-pagination"
                className="mt-10 flex items-center justify-center gap-2"
                aria-label="Paginación de kudos"
              >
                <button
                  onClick={() => goToPage(data.number - 1)}
                  disabled={data.number === 0}
                  className="rounded-lg border border-zinc-700 bg-zinc-800 px-4 py-2 text-sm text-zinc-300 hover:bg-zinc-700 disabled:opacity-40"
                >
                  Anterior
                </button>
                <span className="text-sm text-zinc-400">
                  Página {data.number + 1} de {data.totalPages}
                </span>
                <button
                  onClick={() => goToPage(data.number + 1)}
                  disabled={data.number + 1 >= data.totalPages}
                  className="rounded-lg border border-zinc-700 bg-zinc-800 px-4 py-2 text-sm text-zinc-300 hover:bg-zinc-700 disabled:opacity-40"
                >
                  Siguiente
                </button>
              </nav>
            )}
          </>
        )}
      </div>
    </div>
  );
};

export default KudosPublicPage;
