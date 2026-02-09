export interface Validacao {
  admissaoValidacaoStep: string;
  statusValidacao: 'APROVADO' | 'REPROVADO' | string;
  observacao?: string;
}

export interface DadosPessoais {
  nomeCompleto?: string;
  cpf?: string;
  dataNascimento?: string;
}

export interface Dependente {
  id?: number;
  nome?: string;
  dataNascimento?: string;
  parentesco?: string;
}

export interface Endereco {
  logradouro?: string;
  numero?: string;
  bairro?: string;
  cidade?: string;
  uf?: string;
}

export interface DadosBancarios {
  bancoCodigo?: string | number;
  bancoNome?: string;
  agencia?: string;
  conta?: string;
  digitoConta?: string;
  tipoConta?: string;
}

export interface Documento {
  id?: number;
  tipoDocumento?: string;
  statusDocumento?: string;
}

export type DadosAdmissao = {
  dadosPessoais?: DadosPessoais;
  dependentes?: Dependente[];
  endereco?: Endereco;
  dadosBancarios?: DadosBancarios;
  documentos?: Documento[];
};
