import axios from "axios";

export async function buscarBancoPeloCodigo(cod: string) {
  //const codLimpo = cod.replace(/\D/g, "");
  const url = `https://brasilapi.com.br/api/banks/v1/${cod}`;
  const response = await axios.get(url);
  return response.data;
}
