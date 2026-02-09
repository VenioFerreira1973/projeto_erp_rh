import api from "../config/api";

export interface Colaborador {
  id?: number;
  nome: string;
  emailPessoal: string;
  matricula?: string;
  usuarioLogin?: string;
  dataCriacao?: string;
  dataAlteracao?: string;
  senhaTemporaria?: string;
}


const colaboradorService = {

  getAll: async (): Promise<Colaborador[]> => {
    const response = await api.get("/colaboradores");
    return response.data;
  },

  create: async (colaborador: Colaborador): Promise<Colaborador> => {
    const response = await api.post("/colaboradores", colaborador);
    return response.data;
  },

  update: async (id: number, colaborador: Colaborador): Promise<void> => {
    await api.put(`/colaboradores/${id}`, colaborador);
  },

  // caso queira adicionar delete no futuro
  delete: async (id: number): Promise<void> => {
    await api.delete(`/colaboradores/${id}`);
  },
};

export default colaboradorService;
