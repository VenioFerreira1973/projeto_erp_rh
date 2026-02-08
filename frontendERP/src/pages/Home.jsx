import AppLayout from "../components/AppLayout";
import { useAuth } from "../auth/AuthContext";
import { useOnboarding } from "../context/OnboardingContext";

import CandidateHomeEmAnalise from "./CandidateHomeEmAnalise";
import EmployeeHome from "./EmployeeHome";
import GenericHome from "./GenericHome";

function Home() {
  const { perfil } = useAuth(); // CANDIDATO | COLABORADOR | RH
  const { status, loading } = useOnboarding();

  return (
    <div>
      {loading && perfil === "CANDIDATO" && <p>Carregando...</p>}

      {!loading && perfil === "CANDIDATO" && (
        <CandidateHomeEmAnalise status={status} />
      )}

      {perfil === "COLABORADOR" && <EmployeeHome />}

      {perfil !== "CANDIDATO" && perfil !== "COLABORADOR" && (
        <GenericHome />
      )}
    </div>
  );
}

export default Home;
