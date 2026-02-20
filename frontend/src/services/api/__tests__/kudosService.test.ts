import { describe, it, expect, vi, beforeEach } from 'vitest';
import { kudosService } from '../kudosService';
import { apiClient } from '../client';

vi.mock('../client', () => ({
  apiClient: {
    get: vi.fn(),
    post: vi.fn(),
  },
}));

describe('kudosService.list', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should call GET /v1/kudos with default pagination', async () => {
    const mockResponse = {
      status: 200,
      data: {
        content: [],
        totalElements: 0,
        totalPages: 0,
        number: 0,
        size: 20,
      },
    };
    vi.mocked(apiClient.get).mockResolvedValue(mockResponse);

    const result = await kudosService.list();

    expect(apiClient.get).toHaveBeenCalledWith('/v1/kudos', {
      params: { page: 0, size: 20, sortDirection: 'DESC' },
    });
    expect(result.content).toEqual([]);
    expect(result.totalElements).toBe(0);
  });

  it('should pass page and size parameters', async () => {
    vi.mocked(apiClient.get).mockResolvedValue({
      status: 200,
      data: { content: [], totalElements: 100, totalPages: 10, number: 2, size: 10 },
    });

    await kudosService.list({ page: 2, size: 10 });

    expect(apiClient.get).toHaveBeenCalledWith('/v1/kudos', {
      params: expect.objectContaining({ page: 2, size: 10 }),
    });
  });

  it('should return kudos with safe fields only (no emails, no IDs)', async () => {
    const mockKudo = {
      receptor: 'María García',
      emisor: 'Juan Pérez',
      mensaje: 'Excelente trabajo',
      fecha: '2026-02-20T10:30:00',
      categoria: 'Teamwork',
    };
    vi.mocked(apiClient.get).mockResolvedValue({
      status: 200,
      data: { content: [mockKudo], totalElements: 1, totalPages: 1, number: 0, size: 20 },
    });

    const result = await kudosService.list();

    const kudo = result.content[0];
    expect(kudo).toHaveProperty('receptor');
    expect(kudo).toHaveProperty('emisor');
    expect(kudo).toHaveProperty('mensaje');
    expect(kudo).toHaveProperty('fecha');
    expect(kudo).toHaveProperty('categoria');
    expect(kudo).not.toHaveProperty('id');
    expect(kudo).not.toHaveProperty('email');
    expect(kudo).not.toHaveProperty('fromUser');
    expect(kudo).not.toHaveProperty('toUser');
  });

  it('should handle API errors gracefully', async () => {
    vi.mocked(apiClient.get).mockRejectedValue(new Error('Network Error'));
    await expect(kudosService.list()).rejects.toThrow('Network Error');
  });
});
