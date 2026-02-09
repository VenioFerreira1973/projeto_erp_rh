export function formatarDataBR(dataIso: string | undefined): string {
  if (!dataIso) return "";

  const [ano, mes, dia] = dataIso.split("-");
  return `${dia}/${mes}/${ano}`;
}
