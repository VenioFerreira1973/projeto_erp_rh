# 📋 Patch de Migração para TypeScript (Opção 2 - Revisão Simulada)

**Data:** 8 de Fevereiro de 2026  
**Projeto:** frontendERP  
**Objetivo:** Migrar 100% do código para TypeScript puro  
**Status:** Simulação/Proposta (aguardando aprovação)

---

## 🎯 Impacto na Execução

### Resposta Direta: **NÃO há mudança no comando de execução**

- **Antes:** `npm run dev` → Vite compila + hot-reload JSX
- **Depois:** `npm run dev` → Vite compila + hot-reload **TSX** (equivalente)

**Por quê?** Vite é agnóstico de extension e já suporta TypeScript nativamente. O Vite detectará automaticamente `.ts` e `.tsx` e compilará sem problemas adicionais.

### Novos comportamentos:
1. **Tipagem em tempo de desenvolvimento:** VS Code mostrará erros de tipo em tempo real (underlines vermelhos).
2. **Build mais seguro:** `npm run build` executará `tsc --noEmit` **antes** da build (recomendado via script adicionado).
3. **Nenhuma mudança em APIs:** Backend, API calls e runtime permanecem idênticos.

---

## 📦 Mudanças de Dependências

### Adicionar ao `package.json` (devDependencies):

```json
{
  "typescript": "^5.6.0",
  "@types/react": "^19.2.5",
  "@types/react-dom": "^19.2.3",
  "@types/node": "^22.5.0"
}
```

**Nota:** Essas dependências já existem parcialmente (veja `package.json` linha 29, 30). Precisaremos garantir que `typescript` seja instalado.

### Instalar:
```bash
npm install --save-dev typescript
```

---

## 🗂️ Renomeações de Arquivos Propostas

**Total: 55 arquivos**

### Categoria 1: Componentes (`.jsx` → `.tsx`) - **22 arquivos**

```
src/context/OnboardingContext.jsx           → OnboardingContext.tsx
src/routes/RootRedirect.jsx                 → RootRedirect.tsx
src/routes/RHOnboardingRoute.jsx            → RHOnboardingRoute.tsx
src/routes/PrivateRoute.jsx                 → PrivateRoute.tsx
src/routes/PermissionRoute.jsx              → PermissionRoute.tsx
src/main.jsx                                → main.tsx
src/pages/ColaboradorContratos.jsx          → ColaboradorContratos.tsx
src/pages/CandidateHomeEmAnalise.jsx        → CandidateHomeEmAnalise.tsx
src/pages/Colaboradores.jsx                 → Colaboradores.tsx
src/pages/OnboardingEndereco.jsx            → OnboardingEndereco.tsx
src/pages/OnboardingDocumentosAnexos.jsx    → OnboardingDocumentosAnexos.tsx
src/pages/OnboardingDocumentos.jsx          → OnboardingDocumentos.tsx
src/pages/ResetarSenha.jsx                  → ResetarSenha.tsx
src/pages/PrimeiroAcesso.jsx                → PrimeiroAcesso.tsx
src/pages/RHOnboardingDashboard.jsx         → RHOnboardingDashboard.tsx
src/pages/OnboardingDadosPessoais.jsx       → OnboardingDadosPessoais.tsx
src/pages/OnboardingDadosBancarios.jsx      → OnboardingDadosBancarios.tsx
src/pages/RHOnboardingPage.jsx              → RHOnboardingPage.tsx
src/pages/RHOnboardingDetalhe.jsx           → RHOnboardingDetalhe.tsx
src/pages/Login.jsx                         → Login.tsx
src/pages/Home.jsx                          → Home.tsx
src/pages/GenericHome.jsx                   → GenericHome.tsx
```

**Subcategoria: Pages/Onboarding - 1 arquivo**
```
src/pages/onboarding/OnboardingRouter.jsx   → OnboardingRouter.tsx
```

**Subcategoria: Components - 9 arquivos**
```
src/components/Sidebar.jsx                  → Sidebar.tsx
src/components/AppLayout.jsx                → AppLayout.tsx
src/components/onboarding/PerfilResumo.jsx  → PerfilResumo.tsx
src/components/onboarding/OnboardingStepper.jsx → OnboardingStepper.tsx
src/components/onboarding/OnboardingStatus.jsx  → OnboardingStatus.tsx
src/components/homologation/RHValidacaoCard.jsx → RHValidacaoCard.tsx
src/components/onboarding/EnderecoResumo.jsx    → EnderecoResumo.tsx
src/components/onboarding/DocumentosResumo.jsx  → DocumentosResumo.tsx
src/components/onboarding/DocumentosAnexosResumo.jsx → DocumentosAnexosResumo.tsx
src/components/onboarding/DependentesResumo.jsx → DependentesResumo.tsx
src/components/onboarding/DependentesForm.jsx   → DependentesForm.tsx
src/components/onboarding/DadosBancariosResumo.jsx → DadosBancariosResumo.tsx
src/components/homologacao/RHValidacaoCard.tsx  [NOVO: renomear homologation → homologacao]
```

**Subcategoria: Auth - 1 arquivo**
```
src/auth/AuthContext.jsx                    → AuthContext.tsx
```

### Categoria 2: Utilitários e Hooks (`.js` → `.ts`) - **5 arquivos**

```
src/theme.js                                → theme.ts
src/utils/dateUtils.js                      → dateUtils.ts
src/api.js                                  → api.ts
src/hooks/useOnboardingStep.js              → useOnboardingStep.ts
src/auth/permissions.js                     → permissions.ts
```

### Categoria 3: Serviços (`.js`/`.ts` → `.ts`) - **10 arquivos**

Já estão em `.ts` mas confirmamos:
```
src/services/usuarioService.ts              ✓ OK
src/services/rhOnboardingService.ts         ✓ OK
src/services/resetarSenhaService.ts         ✓ OK
src/services/primeiroAcessoService.ts       ✓ OK
src/services/onboardingService.ts           ✓ OK
src/services/onboardingCorrigirService.ts   ✓ OK
src/services/esqueciSenhaService.ts         ✓ OK
src/services/colaboradorService.ts          ✓ OK
src/services/colaboradorContratoService.ts  ✓ OK
```

Convertem de `.js` → `.ts`:
```
src/services/viacodBancoService.js          → viacodBancoService.ts
src/services/viacepService.js               → viacepService.ts
```

---

## 📝 Novos Arquivos a Criar

### 1. `tsconfig.json` (raiz do projeto)

```json
{
  "compilerOptions": {
    "target": "ES2020",
    "useDefineForClassFields": true,
    "lib": ["ES2020", "DOM", "DOM.Iterable"],
    "module": "ESNext",
    "skipLibCheck": true,
    "declaration": true,
    "declarationMap": true,
    "sourceMap": true,
    "outDir": "./dist",
    "rootDir": "./src",

    /* Bundler mode */
    "moduleResolution": "bundler",
    "allowImportingTsExtensions": true,
    "resolveJsonModule": true,
    "isolatedModules": true,
    "noEmit": true,
    "jsx": "react-jsx",

    /* Linting */
    "strict": true,
    "noUnusedLocals": true,
    "noUnusedParameters": true,
    "noFallthroughCasesInSwitch": true,
    "esModuleInterop": true,
    "allowSyntheticDefaultImports": true
  },
  "include": ["src"],
  "exclude": ["dist", "node_modules"]
}
```

### 2. `eslint.config.js` (ATUALIZADO para suportar TS)

```javascript
import js from '@eslint/js'
import globals from 'globals'
import reactHooks from 'eslint-plugin-react-hooks'
import reactRefresh from 'eslint-plugin-react-refresh'
import tseslint from 'typescript-eslint'

export default tseslint.config(
  {
    ignores: ['dist', 'node_modules'],
  },
  {
    extends: [js.configs.recommended, ...tseslint.configs.recommended],
    files: ['**/*.{ts,tsx}'],
    languageOptions: {
      ecmaVersion: 2020,
      globals: globals.browser,
      parserOptions: {
        ecmaVersion: 'latest',
        ecmaFeatures: { jsx: true },
        sourceType: 'module',
      },
    },
    plugins: {
      'react-hooks': reactHooks,
      'react-refresh': reactRefresh,
    },
    rules: {
      ...reactHooks.configs.flat.recommended.rules,
      'react-refresh/only-export-components': [
        'warn',
        { allowConstantExport: true },
      ],
      '@typescript-eslint/no-unused-vars': [
        'error',
        { varsIgnorePattern: '^[A-Z_]|^_' },
      ],
    },
  }
)
```

**Dependência adicional de desenvolvimento:**
```bash
npm install --save-dev typescript-eslint
```

---

## 🔧 Mudanças em `package.json`

### Scripts a Adicionar/Atualizar:

```json
{
  "scripts": {
    "dev": "vite",
    "build": "tsc --noEmit && vite build",
    "lint": "eslint .",
    "lint:fix": "eslint . --fix",
    "type-check": "tsc --noEmit",
    "preview": "vite preview"
  }
}
```

**Mudança importante:** 
- `"build"` agora executa `tsc --noEmit` antes do Vite (verifica tipos antes de compilar).

### Dependências de Desenvolvimento a Adicionar:

```json
{
  "devDependencies": {
    "typescript": "^5.6.0",
    "typescript-eslint": "^8.0.0",
    "@types/react": "^19.2.5",
    "@types/react-dom": "^19.2.3",
    "@types/node": "^22.5.0"
  }
}
```

---

## 🔀 Mudanças de Imports em Arquivos Críticos

### Arquivos que precisarão de ajustes manuais (relativamente poucos):

#### 1. `src/main.tsx` (era `src/main.jsx`)
**Mudança esperada:** Nenhuma — imports de arquivo já função sem `.tsx`

```typescript
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import './App.css';
import App from './App'  // ← '.jsx' removido (TS assume extensão)
import theme from './theme'  // ← tema agora é .ts
```

#### 2. `src/App.tsx` (era `src/App.jsx`)
**Mudança esperada:** Imports já ok; adicionar tipos opcionalmente

```typescript
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
// ... resto dos imports, removar .jsx conforme necessário
```

#### 3. `src/services/index.ts` (NOVO arquivo para barrels)
Criar `src/services/index.ts`:
```typescript
export * from './colaboradorService'
export * from './colaboradorContratoService'
export * from './esqueciSenhaService'
export * from './onboardingService'
export * from './onboardingCorrigirService'
export * from './primeiroAcessoService'
export * from './resetarSenhaService'
export * from './rhOnboardingService'
export * from './usuarioService'
export * from './viacepService'
export * from './viacodBancoService'
```

**Benefício:** Importações mais limpas como `import { colaboradorService } from '@/services'`.

---

## 🚨 Possíveis Erros de Tipo Pós-Migração

Os seguintes erros de tipo **podem** aparecer ao rodar `tsc --noEmit`:

1. **Imports implícitos de dependencies sem tipos:**
   - `react-toastify`, `react-modal` podem gerar erros se não tiverem tipos definidos.
   - **Solução:** Instalar `@types/react-modal` ou ignorar com `// @ts-ignore`.

2. **Services com tipagem fraca (`any`):**
   - Serviços atuais provavelmente têm respostas `any`. Ideal tipar posteriormente:
     ```typescript
     interface ColaboradorResponse {
       id: string;
       nome: string;
       // ...
     }
     const response = await colaboradorService.getColaboradores(): Promise<ColaboradorResponse[]>
     ```

3. **Context API sem GenericTypes:**
   - `OnboardingContext.tsx` pode precisar de tipos para valores passados:
     ```typescript
     interface OnboardingContextType {
       activeStep: number;
       // ...
     }
     const OnboardingContext = createContext<OnboardingContextType | undefined>(undefined);
     ```

4. **Route params tipagem:**
   - `useParams()` pode estar `any`. Melhorar com tipos:
     ```typescript
     interface RHOnboardingParams {
       id: string;
     }
     const { id } = useParams<RHOnboardingParams>();
     ```

**Nota:** Esses ajustes são **opcionais** para migração inicial; podem ser feitos pós-migração em sprints posteriores.

---

## 📋 Ordem de Aplicação do Patch

### Fase 1: Setup Inicial
1. Instalar dependências TypeScript:
   ```bash
   npm install --save-dev typescript typescript-eslint @types/react @types/react-dom @types/node
   ```

2. Criar `tsconfig.json` (arquivo novo completo acima)

3. Atualizar `eslint.config.js` (ver acima)

4. Atualizar `package.json` (scripts + devDependencies)

### Fase 2: Renomeações de Arquivos
- Renomear todos os 55 arquivos conforme lista acima
- Vite + VSCode resolverão imports automaticamente via linguagem server TS

### Fase 3: Validação
1. Rodar: `npm run type-check` (deve passar sem erros críticos ou com lista de erros conhecidos)
2. Rodar: `npm run dev` (dev server deve iniciar sem problemas)
3. Testar na navegação: clicar nos features principais (login, onboarding, colaboradores)

### Fase 4: Verificação de Runtime
1. Backend API calls funcionam?
2. Autenticação OK?
3. Onboarding fluxo OK?

---

## ✅ Checklist Pré-Migração

- [ ] Criar branch Git: `git checkout -b feat/migrate-to-typescript`
- [ ] Commit estado atual: `git commit -m "Pre-migration snapshot"`
- [ ] Revisar este patch
- [ ] Fazer backup de `.env` e config sensível
- [ ] Verificar se há scripts em `package.json` que depende de extensões (não há, então OK)

---

## 🎯 Próximos Passos Pós-Migração (RECOMENDADO)

1. **Incrementar tipagem (Week 2):**
   - Tipar `services` com interfaces claras para respostas
   - Tipar `Context` APIs

2. **Testes Unitários (Week 3-4):**
   - Configurar Jest ou Vitest para testes de componentes
   - Adicionar testes críticos de services

3. **Refatoração de Arquitetura (Week 4+):**
   - Migrar para **feature folders** (vinda de projetos mais maduros)
   - Organizar rotas em matriz configurável

4. **Performance & Security (Contínuo):**
   - Code splitting por rota com React.lazy()
   - ESLint + Prettier + pre-commit hooks

---

## 📊 Resumo de Impacto

| Aspecto | Antes | Depois | Impacto |
|--------|-------|--------|--------|
| **Linguagem** | JavaScript/JSX + 8 TS | 100% TypeScript | ✅ Tipagem forte |
| **Execução (`npm run dev`)** | Vite compila JSX | Vite compila TSX | ❌ Sem mudança |
| **Build** | Sem verificação de tipo | `tsc --noEmit` + Vite | ⚠️ Build mais seguro |
| **Erros em Desenvolvimento** | Sintaxe (runtime) | Tipo + Sintaxe | ✅ Erros mais cedo |
| **Performance** | Normal | Normal | ❌ Sem mudança |
| **Backend Integration** | OK | OK | ❌ Sem mudança |

---

## 🎁 Bonus: Atualizações de VSCode

Recomenda-se instalar extensões no VSCode:
- **Prettier** (code formatter)
- **ESLint** (linter integrado)
- **TypeScript Vue Plugin** (já integrado em VSCode 1.60+)

Nenhuma configuração adicional necessária — VSCode detectará `tsconfig.json` automaticamente.

---

**FIM DO PATCH PROPOSTO**

Aguardando aprovação para aplicação. Responda com:
- ✅ Aplicar patch completo
- 🔄 Aplicar apenas fase 1 e 2 (setup + renomeações, sem validação)
- 📝 Ajustar algo no patch antes de aplicar

