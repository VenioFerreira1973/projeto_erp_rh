import React, { createContext, useContext, useEffect, useState } from "react";
import { admissaoService } from "../services/admissaoService";
import { useAuth } from "../auth/AuthContext";

const AdmissaoContext = createContext<any>(null);

export function AdmissaoProvider({ children }: { children: React.ReactNode }): JSX.Element {
  const { usuario, _atualizarToken, _finalizarPrimeiroAcesso } = useAuth() as any;

  const [status, setStatus] = useState<any>(null);
  const [carregando, setCarregando] = useState(true);

  useEffect(() => {
    if (!usuario) { setStatus(null); setCarregando(false); return; }

    async function obterStatus() {
      try {
        setCarregando(true);
        const statusAtual = await admissaoService.obterStatus();
        setStatus(statusAtual);
      } catch (erro) {
        console.error("Erro ao carregar status da admissão", erro);
        setStatus(null);
      } finally {
        setCarregando(false);
      }
    }

    obterStatus();
  }, [usuario]);

  const atualizarStatus = async () => {
    try { 
      setCarregando(true); 
      const statusAtual = await admissaoService.obterStatus(); 
      setStatus(statusAtual); 
    } catch (erro) { 
      console.error("Erro ao atualizar status da admissão", erro); 
    } finally { 
      setCarregando(false); 
    }
  };

  const concluirAdmissao = async () => {
    // Método não implementado no serviço
    console.log("Admissão concluída");
    return null;
  };

  return (
    <AdmissaoContext.Provider value={{ status, carregando, atualizarStatus, concluirAdmissao }}>
      {children}
    </AdmissaoContext.Provider>
  );
}

export function useAdmissao() {
  const context = useContext(AdmissaoContext);
  if (!context) throw new Error("useAdmissao deve ser usado dentro de AdmissaoProvider");
  return context;
}

// Compatibilidade com nome antigo
export const OnboardingContext = AdmissaoContext;
export function useOnboarding() {
  return useAdmissao();
}
