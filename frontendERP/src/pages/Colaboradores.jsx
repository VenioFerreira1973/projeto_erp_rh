import { useEffect, useState } from "react";
import colaboradorService from "../services/colaboradorService.js";
import AppLayout from "../components/AppLayout.jsx";
import { FaEdit } from "react-icons/fa";
import { useAuth } from "../auth/AuthContext.jsx";
import { permissions } from "../auth/permissions.js";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

function Colaboradores() {
  const { user, loading } = useAuth();
  const [colaboradores, setColaboradores] = useState([]);
  const [senhaTemporaria, setSenhaTemporaria] = useState("");

  const [form, setForm] = useState({ nome: "", emailPessoal: "" });
  const [editingId, setEditingId] = useState(null);

  const fetchColaboradores = async () => {
    try {
      const data = await colaboradorService.getAll();
      setColaboradores(data);
    } catch (error) {
      console.error("Erro ao buscar colaboradores", error);
    }
  };

  useEffect(() => {
    if (!loading) fetchColaboradores();
  }, [loading]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm({ ...form, [name]: type === "checkbox" ? checked : value });
  };


  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const payload = {
        nome: form.nome,
        emailPessoal: form.emailPessoal
      };

      if (editingId) {
        await colaboradorService.update(editingId, payload);
        setEditingId(null);
      } else {
        const colaboradorCriado = await colaboradorService.create(payload);

        setSenhaTemporaria(colaboradorCriado.senhaTemporaria);
      }

      setForm({ nome: "", emailPessoal: "" });

      toast.success(
        `Senha enviada para o email: ${form.emailPessoal}`, { autoClose: 3000, }
      );

      fetchColaboradores();
    } catch (error) {
      console.error("Erro ao salvar colaborador", error);
      alert(error.response?.data?.message || "Erro no servidor (500). Verifique se o e-mail já existe.");
    }
  };

  const handleEdit = (colab) => {
    setForm({ nome: colab.nome, emailPessoal: colab.usuario?.emailPessoal || "" });
    setEditingId(colab.id);
  };


  const canRead = permissions.colaborador.read(user);
  const canWrite = permissions.colaborador.write(user);
  const canAdmin = permissions.colaborador.admin(user);

  if (loading) return <div>Carregando...</div>;
  if (!canRead) return <AppLayout><h2>Você não tem permissão para visualizar esta página.</h2></AppLayout>;

  return (
    <>
      {canWrite && (
        <div className="crud-form">
          <h3>{editingId ? "Editar Candidato" : "Cadastrar Candidato"}</h3>
          <form onSubmit={handleSubmit}>
            <div>
              <label>Nome</label>
              <input
                name="nome"
                value={form.nome}
                onChange={handleChange}
                required
                readOnly={!!editingId}
                style={{
                  backgroundColor: editingId ? "#f5f5f5" : "white",
                  cursor: editingId ? "not-allowed" : "text",
                }}
              />
            </div>
            <div>
              <label>Email Pessoal</label>
              <input name="emailPessoal" value={form.emailPessoal} onChange={handleChange} required />
            </div>
            {canAdmin && <button type="submit">{editingId ? "Atualizar" : "Cadastrar"}</button>}
          </form>
        </div>
      )}
      <div className="table-container">
        <table className="crud-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nome</th>
              <th>Matrícula</th>
              <th>Email</th>
              <th>Login</th>
              <th>Data Criação</th>
              <th>Data Alteracao</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {colaboradores.map(c => (
              <tr key={c.id}>
                <td>{c.id}</td>
                <td>{c.nome}</td>
                <td>{c.matricula}</td>
                <td>{c.usuario?.emailPessoal || "N/A"}</td>
                <td>{c.usuarioLogin}</td>
                <td>{c.dataCriacao}</td>
                <td>{c.dataAlteracao}</td>
                <td className="action-buttons">
                  {canWrite && <button className="edit" onClick={() => handleEdit(c)} title="Editar"><FaEdit /></button>}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
}

export default Colaboradores;
