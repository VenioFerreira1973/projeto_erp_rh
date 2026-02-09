/**
 * Pages index - Central export point
 * Organiza páginas por feature logicamente
 */

// Auth Pages
export { default as LoginPage } from './Login';
export { default as EsqueciSenhaPage } from './EsqueciSenha';
export { default as ResetarSenhaPage } from './ResetarSenha';
export { default as PrimeiroAcessoPage } from './PrimeiroAcesso';

// Home Pages
export { default as HomePage } from './Home';
export { default as GenericHomePage } from './GenericHome';
export { default as EmployeeHomePage } from './EmployeeHome';
export { default as CandidateHomeEmAnalisePage } from './CandidateHomeEmAnalise';

// Admissão Pages
export { default as AdmissaoDadosBancariosPage } from './AdmissaoDadosBancarios';
export { default as OnboardingDadosBancariosPage } from './AdmissaoDadosBancarios';
export { default as AdmissaoDadosPessoaisPage } from './AdmissaoDadosPessoais';
export { default as OnboardingDadosPessoaisPage } from './AdmissaoDadosPessoais';
export { default as AdmissaoDocumentosPage } from './AdmissaoDocumentos';
export { default as OnboardingDocumentosPage } from './AdmissaoDocumentos';
export { default as AdmissaoDocumentosAnexosPage } from './AdmissaoDocumentosAnexos';
export { default as OnboardingDocumentosAnexosPage } from './AdmissaoDocumentosAnexos';
export { default as AdmissaoEnderecoPage } from './AdmissaoEndereco';
export { default as OnboardingEnderecoPage } from './AdmissaoEndereco';

// RH Pages
export { default as RHAdmissaoDashboardPage } from './RHAdmissaoDashboard';
export { default as RHOnboardingDashboardPage } from './RHAdmissaoDashboard';
export { default as RHAdmissaoDetalhePage } from './RHAdmissaoDetalhe';
export { default as RHOnboardingDetalhePage } from './RHAdmissaoDetalhe';
export { default as RHAdmissaoPage } from './RHAdmissaoPage';
export { default as RHOnboardingPage } from './RHAdmissaoPage';

// Colaboradores Pages
export { default as ColaboradoresPage } from './Colaboradores';
export { default as ColaboradorContratosPage } from './ColaboradorContratos';

// Legacy exports (para compatibilidade)
export { default as Login } from './Login';
export { default as EsqueciSenha } from './EsqueciSenha';
export { default as ResetarSenha } from './ResetarSenha';
export { default as Home } from './Home';
