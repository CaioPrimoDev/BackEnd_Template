package br.com.usuario.entity;

public enum PerfilUsuario {
    CLIENTE("ROLE_CLIENTE"),
    GESTOR("ROLE_GESTOR"),
    ADMIN("ROLE_ADMIN");

    private final String roleName;

    PerfilUsuario(String roleName) {
        this.roleName = roleName;
    }

    // Este é o método que será usado pelo Spring Security
    public String getAuthority() {
        return roleName;
    }

    // busca o Enum pela String do banco de dados/JWT
    public static PerfilUsuario fromRoleName(String roleName) {
        for (PerfilUsuario perfil : PerfilUsuario.values()) {
            if (perfil.roleName.equalsIgnoreCase(roleName)) {
                return perfil;
            }
        }
        throw new IllegalArgumentException("Perfil inválido: " + roleName);
    }
}