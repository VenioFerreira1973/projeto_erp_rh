import { useEffect, useState } from "react";
import { useOnboarding } from "../context/OnboardingContext";
import { toast } from "react-toastify";

export function useOnboardingStep({
  load,
  save,
}) {
  const { refreshStatus } = useOnboarding();
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    async function fetchData() {
      try {
        const response = await load();
        setData(response || {});
      } catch (err) {
        toast.error("Erro ao carregar dados", { autoClose: 3000, });
      } finally {
        setLoading(false);
      }
    }

    fetchData();
  }, [load]);

  const handleSave = async (payload) => {
    setSaving(true);
    try {
      await save(payload);
      await refreshStatus(); 
    } catch (err) {
      toast.error("Erro ao salvar", { autoClose: 3000, });
      throw err;
    } finally {
      setSaving(false);
    }
  };

  return {
    data,
    setData,
    loading,
    saving,
    handleSave,
  };
}
