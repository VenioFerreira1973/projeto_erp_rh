import { useEffect, useState } from "react";
import AppLayout from "../components/AppLayout.jsx";
import { FaEdit, FaTrash, FaUserCheck } from "react-icons/fa";
import { useAuth } from "../auth/AuthContext.jsx";
import { permissions } from "../auth/permissions.js";

import colaboradorContratoService, { ColaboradorContrato } from "../services/colaboradorContratoService.js";

function ColaboradorContrato() {
  const { user, loading } = useAuth();
  const [colaboradores, setColaboradores] = useState([]);
  const [cargos, setCargos] = useState([]);
  const [departamentos, setDepartamentos] = useState([]);
  const [empresas, setEmpresas] = useState([]);
  const [gestores, setGestores] = useState([]);


  const [form, setForm] = useState < ColaboradorContrato > ({
    nome: "",
    matricula: "",
    empresaId: "",
    tipoVinculo: "CLT",
    regimeTrabalho: "INTEGRAL",
    statusColaborador: "ATIVO",
    primeiroEmprego: false,
    dataAdmissao: "",
    dataInicioVinculo: "",
    salario: { valor: "", dataInicio: "", motivo: "" },
    cargoId: "",
    departamentoId: "",
    gestorId: "",
  });

  const [editingId, setEditingId] = useState < number | null > (null);

  const fetchColaboradores = async () => {
    try {
      const data = await colaboradorContratoService.getAll();
      setColaboradores(data);
      setGestores(data); 
    } catch (error) {
      console.error("Erro ao buscar colaboradores", error);
    }
  };

  const fetchRelacionamentos = async () => {
    try {
      const { cargos, departamentos, empresas } = await colaboradorContratoService.getRelacionamentos();
      setCargos(cargos);
      setDepartamentos(departamentos);
      setEmpresas(empresas);
    } catch (error) {
      console.error("Erro ao buscar relacionamentos", error);
    }
  };

  useEffect(() => {
    fetchColaboradores();
    fetchRelacionamentos();
  }, []);

  // handleChange sem tipagem TypeScript
  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm({
      ...form,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  // handleSubmit sem tipagem TypeScript
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await colaboradorContratoService.update(editingId, form);
        setEditingId(null);
      } else {
        await colaboradorContratoService.create(form);
      }

      setForm({
        nome: "",
        matricula: "",
        empresaId: "",
        tipoVinculo: "CLT",
        regimeTrabalho: "INTEGRAL",
        statusColaborador: "ATIVO",
        primeiroEmprego: false,
        dataAdmissao: "",
        dataInicioVinculo: "",
        salario: { valor: "", dataInicio: "", motivo: "" },
        cargoId: "",
        departamentoId: "",
        gestorId: "",
      });

      fetchColaboradores();
    } catch (error) {
      console.error("Erro ao salvar colaborador", error);
    }
  };

  const handleEdit = (colab) => {
    setForm({
      ...colab,
      salario: colab.salario || { valor: "", dataInicio: "", motivo: "" },
    });
    setEditingId(colab.id || null);
  };

  const handleDelete = async (id) => {
    if (window.confirm("Deseja realmente inativar este colaborador?")) {
      try {
        await colaboradorContratoService.inativar(id);
        fetchColaboradores();
      } catch (error) {
        console.error("Erro ao inativar colaborador", error);
      }
    }
  };

  const handleActive = async (id) => {
    if (window.confirm("Deseja realmente ativar este colaborador?")) {
      try {
        await colaboradorContratoService.ativar(id);
        fetchColaboradores();
      } catch (error) {
        console.error("Erro ao ativar colaborador", error);
      }
    }
  };

  if (loading) return <div>Carregando...</div>;

  const canRead = permissions.colaborador.read(user);
  const canWrite = permissions.colaborador.write(user);
  const canAdmin = permissions.colaborador.admin(user);

  if (!canRead) return <AppLayout><h2>Você não tem permissão para visualizar esta página.</h2></AppLayout>;

  return (
    <AppLayout>
      {canWrite && (
        <div className="crud-form">
          <h3>{editingId ? "Editar Contrato" : "Cadastrar Contrato"}</h3>
          <form onSubmit={handleSubmit}>
            <div>
              <label>Nome</label>
              <input name="nome" value={form.nome} onChange={handleChange} required />
            </div>

            <div>
              <label>Matrícula</label>
              <input name="matricula" value={form.matricula} onChange={handleChange} required />
            </div>

            <div>
              <label>Tipo de Vínculo</label>
              <select name="tipoVinculo" value={form.tipoVinculo} onChange={handleChange} required>
                <option value="CLT">CLT</option>
                <option value="ESTAGIARIO">Estagiário</option>
                <option value="APRENDIZ">Aprendiz</option>
                <option value="AUTONOMO">Autônomo</option>
                <option value="COOPERADO">Cooperado</option>
                <option value="TEMPORARIO">Temporário</option>
              </select>
            </div>

            <div>
              <label>Regime de Trabalho</label>
              <select name="regimeTrabalho" value={form.regimeTrabalho} onChange={handleChange} required>
                <option value="PRESENCIAL">Presencial</option>
                <option value="HIBRIDO">Híbrido</option>
                <option value="REMOTO_INTEGRAL">Remoto Integral</option>
                <option value="REMOTO_PARCIAL">Remoto Parcial</option>
              </select>
            </div>

            <div>
              <label>Salário</label>
              <input type="number" value={form.salario.valor} onChange={(e) =>
                setForm({
                  ...form,
                  salario: { ...form.salario, valor: e.target.value },
                })
              } required />
            </div>

            <div className="checkbox-group">
              <input type="checkbox" name="primeiroEmprego" checked={form.primeiroEmprego} onChange={handleChange} />
              <label htmlFor="primeiroEmprego">Primeiro Emprego</label>
            </div>

            <div>
              <select name="empresaId" value={form.empresaId} onChange={handleChange} required>
                <option value="">Selecione a empresa</option>
                {empresas.map(e => <option key={e.id} value={e.id}>{e.nome}</option>)}
              </select>
            </div>

            {/* Campos de datas */}
            <div className="date-group-container">
              <div className="date-field">
                <label>Data início salário</label>
                <input type="date" value={form.salario.dataInicio} onChange={(e) =>
                  setForm({ ...form, salario: { ...form.salario, dataInicio: e.target.value } })}
                  required
                />
              </div>
              <div className="date-field">
                <label>Data Início Vínculo</label>
                <input type="date" name="dataInicioVinculo" value={form.dataInicioVinculo} onChange={handleChange} required />
              </div>
              <div className="date-field">
                <label>Data Admissão</label>
                <input type="date" name="dataAdmissao" value={form.dataAdmissao} onChange={handleChange} required />
              </div>
            </div>

            {/* Seleção de cargo, departamento e gestor */}
            <div>
              <select name="cargoId" value={form.cargoId} onChange={handleChange} required>
                <option value="">Selecione o cargo</option>
                {cargos.map(c => <option key={c.id} value={c.id}>{c.nome}</option>)}
              </select>
            </div>

            <div>
              <select name="departamentoId" value={form.departamentoId} onChange={handleChange} required>
                <option value="">Selecione o departamento</option>
                {departamentos.map(d => <option key={d.id} value={d.id}>{d.descricao}</option>)}
              </select>
            </div>

            <div>
              <select name="gestorId" value={form.gestorId} onChange={handleChange}>
                <option value="">Selecione o gestor</option>
                {gestores.map(g => <option key={g.id} value={g.id}>{g.nome}</option>)}
              </select>
            </div>

            {canAdmin && (
              <button type="submit">{editingId ? "Atualizar" : "Cadastrar"}</button>
            )}
          </form>
        </div>
      )}

      {/* Tabela de colaboradores contrato */}
      <div className="table-container">
        <table className="crud-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nome</th>
              <th>Matrícula</th>
              <th>Status</th>
              <th>Primeiro Emprego</th>
              <th>Data Admissão</th>
              <th>Data Início Vínculo</th>
              <th>Tipo de Vínculo</th>
              <th>Regime</th>
              <th>Salário</th>
              <th>Cargo</th>
              <th>Departamento</th>
              <th>Gestor</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {colaboradores.map(f => (
              <tr key={f.id}>
                <td>{f.id}</td>
                <td>{f.nome}</td>
                <td>{f.matricula}</td>
                <td>{f.statusColaborador}</td>
                <td>{f.primeiroEmprego ? "Sim" : "Não"}</td>
                <td>{f.dataAdmissao}</td>
                <td>{f.dataInicioVinculo}</td>
                <td>{f.tipoVinculo}</td>
                <td>{f.regimeTrabalho}</td>
                <td>{f.salario?.valor}</td>
                <td>{f.cargoId}</td>
                <td>{f.departamentoId}</td>
                <td>{f.gestorId || "-"}</td>
                <td className="action-buttons">
                  {canWrite && <button className="edit" onClick={() => handleEdit(f)} title="Editar"><FaEdit /></button>}
                  {canAdmin && (
                    f.statusColaborador === "ATIVO" ? (
                      <button className="delete" onClick={() => handleDelete(f.id)} title="Inativar">
                        <FaTrash />
                      </button>
                    ) : (
                      <button className="activate" onClick={() => handleActive(f.id)} title="Ativar">
                        <FaUserCheck />
                      </button>
                    )
                  )}

                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </AppLayout>
  );
}

export default ColaboradorContrato;
