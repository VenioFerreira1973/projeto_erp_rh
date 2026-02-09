import api from "../config/api";

export interface Usuario {
  id?: number;
  login: string;
  emailPessoal: string;
  ativo: string;
  perfis?: string;
}

const usuarioService = {

    get: async (_id: number): Promise<Usuario> => {
      const response = await api.get("/usuarios");
      return response.data;
    },
}
export default usuarioService;