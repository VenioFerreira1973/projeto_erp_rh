const hasAnyPermission = (user, perms) => {
  console.log("Checando permissões:", perms, "no usuário:", user?.permissoes);
  return perms.some((perm) =>
    user?.permissoes?.some((p) => p.descricao === perm)
  );
};

export const permissions = {
  funcionario: {
    read: (user) =>
      hasAnyPermission(user, ["FUNCIONARIO_READ", "FUNCIONARIO_ADMIN"]),
    write: (user) =>
      hasAnyPermission(user, ["FUNCIONARIO_WRITE", "FUNCIONARIO_ADMIN"]),
    admin: (user) => hasAnyPermission(user, ["FUNCIONARIO_ADMIN"]),
  },

  estrutura: {
    read: (user) => hasAnyPermission(user, ["ESTRUTURA_READ"]),
    manage: (user) => hasAnyPermission(user, ["ESTRUTURA_MANAGE"]),
  },

  folha: {
    read: (user) => hasAnyPermission(user, ["FOLHA_READ"]),
    processar: (user) => hasAnyPermission(user, ["FOLHA_PROCESSAR"]),
    admin: (user) => hasAnyPermission(user, ["FOLHA_ADMIN"]),
  },

  seguranca: {
    usuarioAdmin: (user) => hasAnyPermission(user, ["USUARIO_ADMIN"]),
    perfilAdmin: (user) => hasAnyPermission(user, ["PERFIL_ADMIN"]),
  },
};
