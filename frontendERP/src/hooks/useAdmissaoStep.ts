import { useEffect, useState } from "react";
import { useAdmissao } from "../context/AdmissaoContext";
import { toast } from "react-toastify";

export function useAdmissaoStep({
  carregar,
  salvar,
}: {
  carregar: () => Promise<any>;
  salvar: (payload: any) => Promise<any>;
}) {
  const { atualizarStatus } = useAdmissao();
  const [dados, setDados] = useState<any>(null);
  const [carregando, setCarregando] = useState(true);
  const [salvando, setSalvando] = useState(false);

  useEffect(() => {
    async function obterDados() {
      try {
        const resposta = await carregar();
        setDados(resposta || {});
      } catch (_erro) {
        void _erro;
        toast.error("Erro ao carregar dados", { autoClose: 3000, });
      } finally {
        setCarregando(false);
      }
    }

    obterDados();
  }, [carregar]);

  const handleSalvar = async (payload: any) => {
    setSalvando(true);
    try {
      await salvar(payload);
      await atualizarStatus(); 
    } catch (_erro) {
      void _erro;
      toast.error("Erro ao salvar", { autoClose: 3000, });
      throw _erro;
    } finally {
      setSalvando(false);
    }
  };

  return {
    dados,
    setDados,
    carregando,
    salvando,
    handleSalvar,
  };
}

// Compatibilidade com nome antigo
export function useOnboardingStep({
  load,
  save,
}: {
  load: () => Promise<any>;
  save: (payload: any) => Promise<any>;
}) {
  return useAdmissaoStep({
    carregar: load,
    salvar: save,
  });
}
