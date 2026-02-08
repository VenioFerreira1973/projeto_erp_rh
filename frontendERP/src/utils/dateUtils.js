export function formatarDataBR(dataIso) {
  if (!dataIso) return "";

  const [ano, mes, dia] = dataIso.split("-");
  return `${dia}/${mes}/${ano}`;
}