/**
 * Central export point for all components
 * Permite importações modernas enquanto mantém a estrutura atual
 */

// Common/Generic Components
export { default as AppLayout } from './common/AppLayout';
export { default as Sidebar } from './common/Sidebar';

// Features - Admissão
export { default as AdmissaoStepper } from './admissao/AdmissaoStepper';
export { default as AdmissaoStatus } from './admissao/AdmissaoStatus';
export { default as DadosBancariosResumo } from './admissao/DadosBancariosResumo';
export { default as DependentesForm } from './admissao/DependentesForm';
export { default as DependentesResumo } from './admissao/DependentesResumo';
export { default as DocumentosAnexosResumo } from './admissao/DocumentosAnexosResumo';
export { default as DocumentosResumo } from './admissao/DocumentosResumo';
export { default as EnderecoResumo } from './admissao/EnderecoResumo';
export { default as PerfilResumo } from './admissao/PerfilResumo';

// Features - RH
export { default as RHValidacaoCard } from './homologation/RHValidacaoCard';

// Legacy exports (para compatibilidade com código antigo)
export { default as OnboardingStepper } from './admissao/AdmissaoStepper';
export { default as OnboardingStatus } from './admissao/AdmissaoStatus';
