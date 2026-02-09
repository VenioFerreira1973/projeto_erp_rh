import api from "../config/api";

export interface Salario {
  valor: string;
  dataInicio: string;
  motivo?: string;
}

export interface ColaboradorContrato {
  id?: number;
  nome: string;
  matricula: string;
  empresaId: string;
  tipoVinculo: string;
  regimeTrabalho: string;
  statusColaborador?: string;
  primeiroEmprego?: boolean;
  dataAdmissao: string;
  dataInicioVinculo: string;
  salario?: Salario;
  cargoId: string;
  departamentoId: string;
  gestorId?: string;
}

const colaboradorContratoService = {
  getAll: async (): Promise<ColaboradorContrato[]> => {
    const response = await api.get("/colaboradores-contrato");
    return response.data;
  },

  create: async (colaborador: ColaboradorContrato): Promise<void> => {
    await api.post("/colaboradores-contrato", colaborador);
  },

  update: async (id: number, colaborador: ColaboradorContrato): Promise<void> => {
    await api.put(`/colaboradores-contrato/${id}`, colaborador);
  },

  inativar: async (id: number): Promise<void> => {
    await api.patch(`/colaboradores-contrato/${id}/inativar`);
  },

  ativar: async (id: number): Promise<void> => {
    await api.patch(`/colaboradores-contrato/${id}/ativar`);
  },

  getRelacionamentos: async (): Promise<{
    cargos: any[];
    departamentos: any[];
    empresas: any[];
  }> => {
    const [cargosRes, departamentosRes, empresasRes] = await Promise.all([
      api.get("/cargos"),
      api.get("/departamentos"),
      api.get("/empresas"),
    ]);

    return {
      cargos: cargosRes.data,
      departamentos: departamentosRes.data,
      empresas: empresasRes.data,
    };
  },
};

export default colaboradorContratoService;
