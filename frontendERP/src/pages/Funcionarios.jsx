import { useEffect, useState } from "react";
import api from "../api";
import AppLayout from "../components/AppLayout";
import { FaEdit, FaTrash } from "react-icons/fa";
import { useAuth } from "../auth/AuthContext.jsx";
import { permissions } from "../auth/permissions";

function Funcionarios() {
  const { user, loading } = useAuth()
  const [funcionarios, setFuncionarios] = useState([]);
  const [cargos, setCargos] = useState([]);
  const [departamentos, setDepartamentos] = useState([]);

  const [form, setForm] = useState({
    nome: "",
    salario: "",
    dataAdmissao: "",
    status: "",
    cargoId: "",
    departamentoId: "",
    gerente: false
  });

  const [editingId, setEditingId] = useState(null);

  const fetchFuncionarios = async () => {
    try {
      const response = await api.get("/funcionarios");
      setFuncionarios(response.data);
    } catch (error) {
      console.error("Erro ao buscar funcionários", error);
    }
  };

  const fetchRelacionamentos = async () => {
    try {
      const [cargosRes, departamentosRes] = await Promise.all([
        api.get("/cargos"),
        api.get("/departamentos")
      ]);
      setCargos(cargosRes.data);
      setDepartamentos(departamentosRes.data);
    } catch (error) {
      console.error("Erro ao buscar relacionamentos", error);
    }
  };

  useEffect(() => {
    fetchFuncionarios();
    fetchRelacionamentos();
  }, []);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm({
      ...form,
      [name]: type === "checkbox" ? checked : value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await api.put(`/funcionarios/${editingId}`, form);
        setEditingId(null);
      } else {
        await api.post("/funcionarios", form);
      }

      setForm({
        nome: "",
        salario: "",
        dataAdmissao: "",
        status: "",
        cargoId: "",
        departamentoId: "",
        gerente: false
      });

      fetchFuncionarios();
    } catch (error) {
      console.error("Erro ao salvar funcionário", error);
    }
  };

  const handleEdit = (func) => {
    setForm({
      nome: func.nome,
      salario: func.salario,
      dataAdmissao: func.dataAdmissao,
      status: func.status,
      cargoId: func.cargoDTO.id,
      departamentoId: func.departamentoDTO.id,
      gerente: func.gerente
    });
    setEditingId(func.id);
  };

  const handleDelete = async (id) => {
    if (window.confirm("Deseja realmente excluir este funcionário?")) {
      try {
        await api.delete(`/funcionarios/${id}`);
        fetchFuncionarios();
      } catch (error) {
        console.error("Erro ao excluir funcionário", error);
      }
    }
  };

  if (loading) {
    return <div>Carregando...</div>;
  }

  console.log("USUÁRIO LOGADO:", user);

  const canRead = permissions.funcionario.read(user);
  const canWrite = permissions.funcionario.write(user);
  const canAdmin = permissions.funcionario.admin(user);

  console.log("Permissões usuário:", user?.permissoes);
  console.log({ canRead, canWrite, canAdmin });

  if (!canRead) {
    return (
      <AppLayout>
        <h2>Você não tem permissão para visualizar esta página.</h2>
      </AppLayout>
    );
  }

  return (
    <AppLayout>
      {canWrite && (
        <div className="crud-form">
          <h3>{editingId ? "Editar Funcionário" : "Cadastrar Funcionário"}</h3>
          <form onSubmit={handleSubmit}>
            <input name="nome" placeholder="Nome" value={form.nome} onChange={handleChange} required />
            <input name="salario" type="number" placeholder="Salário" value={form.salario} onChange={handleChange} required />
            <input name="dataAdmissao" type="date" value={form.dataAdmissao} onChange={handleChange} required />

            <select name="status" value={form.status} onChange={handleChange} required>
              <option value="">Selecione o status</option>
              <option value="ATIVO">ATIVO</option>
              <option value="INATIVO">INATIVO</option>
            </select>

            <select name="cargoId" value={form.cargoId} onChange={handleChange} required>
              <option value="">Selecione o cargo</option>
              {cargos.map(c => (
                <option key={c.id} value={c.id}>{c.nome}</option>
              ))}
            </select>

            <select name="departamentoId" value={form.departamentoId} onChange={handleChange} required>
              <option value="">Selecione o departamento</option>
              {departamentos.map(d => (
                <option key={d.id} value={d.id}>{d.descricao}</option>
              ))}
            </select>

            <div className="checkbox-gerente">
              <label htmlFor="gerente">Gerente</label>
              <input
                type="checkbox"
                name="gerente"
                checked={form.gerente}
                onChange={handleChange}
                id="gerente"
              />
            </div>
            {canAdmin && (
              <button type="submit">
                {editingId ? "Atualizar" : "Cadastrar"}
              </button>
            )}
          </form>
        </div>
      )}

      <table className="crud-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Salário</th>
            <th>Data Admissão</th>
            <th>Status</th>
            <th>Usuário</th>
            <th>Cargo</th>
            <th>Departamento</th>
            <th>Gerente</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          {funcionarios.map(f => (
            <tr key={f.id}>
              <td>{f.id}</td>
              <td>{f.nome}</td>
              <td>{f.salario}</td>
              <td>{f.dataAdmissao}</td>
              <td>{f.status}</td>
              <td>{f.usuarioLogin}</td>
              <td>{f.cargoDTO?.nome || ""}</td>
              <td>{f.departamentoDTO?.descricao || ""}</td>
              <td>{f.gerente ? "Sim" : "Não"}</td>
              <td className="action-buttons">
                {canWrite && (
                  <button
                    className="edit"
                    onClick={() => handleEdit(f)}
                    title="Editar"
                  >
                    <FaEdit />
                  </button>
                )}
                {canAdmin && (
                  <button
                    className="delete"
                    onClick={() => handleDelete(f.id)}
                    title="Excluir"
                  >
                    <FaTrash />
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </AppLayout>
  );
}


export default Funcionarios;
