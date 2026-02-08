import { createContext, useContext, useEffect, useState } from "react";
import { onboardingService } from "../services/onboardingService";
import { useAuth } from "../auth/AuthContext";

const OnboardingContext = createContext(null);

export function OnboardingProvider({ children }) {
  const { user, atualizarToken, finalizarPrimeiroAcesso } = useAuth();

  const [status, setStatus] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!user) {
      setStatus(null);
      setLoading(false);
      return;
    }

    async function fetchStatus() {
      try {
        setLoading(true);
        const st = await onboardingService.getStatus();
        setStatus(st);
      } catch (err) {
        console.error("Erro ao carregar status do onboarding", err);
        setStatus(null);
      } finally {
        setLoading(false);
      }
    }

    fetchStatus();
  }, [user]);

  const refreshStatus = async () => {
    try {
      setLoading(true);
      const st = await onboardingService.getStatus();
      setStatus(st);
    } catch (err) {
      console.error("Erro ao atualizar status do onboarding", err);
    } finally {
      setLoading(false);
    }
  };

  const concluirOnboarding = async () => {
    const result = await onboardingService.concluirOnboarding();

    if (result && result.token) {
      atualizarToken(result.token);
      finalizarPrimeiroAcesso();
    }
    
    return result;
  };

  return (
    <OnboardingContext.Provider
      value={{
        status,
        loading,
        refreshStatus,
        concluirOnboarding,
      }}
    >
      {children}
    </OnboardingContext.Provider>
  );
}

/**
 * Hook para consumir o contexto
 */
export function useOnboarding() {
  const context = useContext(OnboardingContext);

  if (!context) {
    throw new Error("useOnboarding deve ser usado dentro de OnboardingProvider");
  }

  return context;
}
