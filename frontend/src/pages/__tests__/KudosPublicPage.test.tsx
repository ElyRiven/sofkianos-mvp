import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import KudosPublicPage from '../KudosPublicPage';
import { kudosService } from '../../services/api/kudosService';

vi.mock('../../services/api/kudosService', () => ({
  kudosService: {
    list: vi.fn(),
    send: vi.fn(),
  },
}));

const renderPage = () =>
  render(
    <MemoryRouter initialEntries={['/kudos-public']}>
      <KudosPublicPage />
    </MemoryRouter>
  );

const mockKudosResponse = {
  content: [
    {
      receptor: 'María García',
      emisor: 'Juan Pérez',
      mensaje: 'Excelente colaboración en el sprint',
      fecha: '2026-02-20T15:30:00',
      categoria: 'Teamwork',
    },
    {
      receptor: 'Carlos López',
      emisor: 'Anónimo',
      mensaje: 'Gran innovación en el sistema',
      fecha: '2026-02-19T10:00:00',
      categoria: 'Innovation',
    },
  ],
  totalElements: 2,
  totalPages: 1,
  number: 0,
  size: 20,
};

describe('KudosPublicPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should render the page title', async () => {
    vi.mocked(kudosService.list).mockResolvedValue(mockKudosResponse);
    renderPage();
    expect(screen.getByText(/kudos/i)).toBeInTheDocument();
  });

  it('should display kudos list after loading', async () => {
    vi.mocked(kudosService.list).mockResolvedValue(mockKudosResponse);
    renderPage();

    await waitFor(() => {
      expect(screen.getByText('María García')).toBeInTheDocument();
      expect(screen.getByText('Juan Pérez')).toBeInTheDocument();
      expect(screen.getByText('Excelente colaboración en el sprint')).toBeInTheDocument();
    });
  });

  it('should show loading state initially', () => {
    vi.mocked(kudosService.list).mockReturnValue(new Promise(() => {}));
    renderPage();
    expect(screen.getByText(/cargando/i)).toBeInTheDocument();
  });

  it('should show error state on API failure', async () => {
    vi.mocked(kudosService.list).mockRejectedValue(new Error('Server Error'));
    renderPage();

    await waitFor(() => {
      expect(screen.getByText(/error/i)).toBeInTheDocument();
    });
  });

  it('should NOT display any email addresses', async () => {
    vi.mocked(kudosService.list).mockResolvedValue(mockKudosResponse);
    renderPage();

    await waitFor(() => {
      const container = screen.getByTestId('kudos-public-list');
      expect(container.textContent).not.toMatch(
        /[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}/
      );
    });
  });

  it('should display kudos ordered by date DESC (newest first)', async () => {
    vi.mocked(kudosService.list).mockResolvedValue(mockKudosResponse);
    renderPage();

    await waitFor(() => {
      const kudoCards = screen.getAllByTestId('kudo-card');
      expect(kudoCards).toHaveLength(2);
      expect(kudoCards[0]).toHaveTextContent('María García');
      expect(kudoCards[1]).toHaveTextContent('Carlos López');
    });
  });

  it('should show pagination when totalPages > 1', async () => {
    vi.mocked(kudosService.list).mockResolvedValue({
      ...mockKudosResponse,
      totalElements: 150,
      totalPages: 8,
    });
    renderPage();

    await waitFor(() => {
      expect(screen.getByTestId('kudos-pagination')).toBeInTheDocument();
    });
  });

  it('should show empty state when no kudos exist', async () => {
    vi.mocked(kudosService.list).mockResolvedValue({
      content: [],
      totalElements: 0,
      totalPages: 0,
      number: 0,
      size: 20,
    });
    renderPage();

    await waitFor(() => {
      expect(screen.getByText(/no hay kudos/i)).toBeInTheDocument();
    });
  });
});
