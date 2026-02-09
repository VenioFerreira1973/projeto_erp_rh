import axios from "axios";

export async function buscarEnderecoPorCep(cep: string) {
  const cepLimpo = cep.replace(/\D/g, "");
  const url = `https://viacep.com.br/ws/${cepLimpo}/json/`;
  const response = await axios.get(url);
  return response.data;
}
