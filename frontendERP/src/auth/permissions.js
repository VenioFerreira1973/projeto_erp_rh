const hasAnyPermission = (user, perms) => {
  console.log("Checando permissões:", perms, "no usuário:", user?.permissoes);
  return perms.some((perm) =>
    user?.permissoes?.some((p) => p.descricao === perm)
  );
};

export const permissions = {
  colaborador: {
    read: (user) =>
      hasAnyPermission(user, ["COLABORADOR_READ", "COLABORADOR_ADMIN"]),
    write: (user) =>
      hasAnyPermission(user, ["COLABORADOR_WRITE", "COLABORADOR_ADMIN"]),
    admin: (user) => hasAnyPermission(user, ["COLABORADOR_ADMIN"]),

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

  onboarding: {
    read: (user) =>
      hasAnyPermission(user, ["ONBOARDING_READ", "ONBOARDING_WRITE"]),
    write: (user) =>
      hasAnyPermission(user, ["ONBOARDING_WRITE"]),
  },
};
