/**
 * Tipos globais para a aplicação
 */

export interface ApiResponse<T = any> {
  data?: T;
  message?: string;
  error?: string;
  status?: number;
}

export interface PaginatedResponse<T = any> {
  content: T[];
  totalPages: number;
  totalElements: number;
  currentPage: number;
  pageSize: number;
}

export interface Usuario {
  id?: number;
  login: string;
  emailPessoal: string;
  ativo: string;
  perfis?: string;
}

export interface Colaborador {
  id: number;
  nome: string;
  email: string;
  cpf: string;
  status?: string;
}

export interface AuthResponse {
  token: string;
  usuario: Usuario;
  expiresIn: number;
}
