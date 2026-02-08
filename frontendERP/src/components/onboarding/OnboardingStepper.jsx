import { Stepper, Step, StepLabel } from "@mui/material";
import { useOnboarding } from "../../context/OnboardingContext";

const steps = [
  { key: "DADOS_PESSOAIS", label: "Dados Pessoais" },
  { key: "ENDERECO", label: "Endereço" },
  { key: "DADOS_BANCARIOS", label: "Dados Bancários" },
  { key: "DOCUMENTOS", label: "Documentos" },
  { key: "DOCUMENTOS_ANEXOS", label: "Documentos Anexos" },
];

export default function OnboardingStepper() {
  const { status } = useOnboarding();

  const activeStep = steps.findIndex(
    (step) => step.key === status?.step
  );

  return (
    <Stepper activeStep={activeStep} alternativeLabel>
      {steps.map((step) => (
        <Step key={step.key}>
          <StepLabel
            sx={{
              "& .MuiStepLabel-label": { fontSize: "18px" },
              "& .MuiStepIcon-root": { fontSize: "40px" },
            }}
          >
            {step.label}
          </StepLabel>
        </Step>
      ))}
    </Stepper>
  );
}
