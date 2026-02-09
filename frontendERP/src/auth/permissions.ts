const hasAnyPermission = (user: any, perms: string[]) => {
  console.log("Checando permissões:", perms, "no usuário:", user?.permissoes);
  return perms.some((perm) =>
    user?.permissoes?.some((p: any) => p.descricao === perm)
  );
};

export const permissions = {
  colaborador: {
    read: (user: any) =>
      hasAnyPermission(user, ["COLABORADOR_READ", "COLABORADOR_ADMIN"]),
    write: (user: any) =>
      hasAnyPermission(user, ["COLABORADOR_WRITE", "COLABORADOR_ADMIN"]),
    admin: (user: any) => hasAnyPermission(user, ["COLABORADOR_ADMIN"]),

  },

  estrutura: {
    read: (user: any) => hasAnyPermission(user, ["ESTRUTURA_READ"]),
    manage: (user: any) => hasAnyPermission(user, ["ESTRUTURA_MANAGE"]),
  },

  folha: {
    read: (user: any) => hasAnyPermission(user, ["FOLHA_READ"]),
    processar: (user: any) => hasAnyPermission(user, ["FOLHA_PROCESSAR"]),
    admin: (user: any) => hasAnyPermission(user, ["FOLHA_ADMIN"]),
  },

  seguranca: {
    usuarioAdmin: (user: any) => hasAnyPermission(user, ["USUARIO_ADMIN"]),
    perfilAdmin: (user: any) => hasAnyPermission(user, ["PERFIL_ADMIN"]),
  },

  admissao: {
    read: (user: any) =>
      hasAnyPermission(user, ["ONBOARDING_READ", "ONBOARDING_WRITE"]),
    write: (user: any) =>
      hasAnyPermission(user, ["ONBOARDING_WRITE"]),
  },

  onboarding: {
    read: (user: any) =>
      hasAnyPermission(user, ["ONBOARDING_READ", "ONBOARDING_WRITE"]),
    write: (user: any) =>
      hasAnyPermission(user, ["ONBOARDING_WRITE"]),
  },
};
