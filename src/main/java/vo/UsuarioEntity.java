package vo;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "usuario", schema = "claudiodb")
public class UsuarioEntity  implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "login", nullable = false)
    private String login;

    @Basic
    @Column(name = "senha", nullable = false)
    private String senha;

    @Basic
    @Column(name = "data_hora_login")
    private Timestamp dataHoraLogin;

    @Basic
    @Column(name = "data_hora_cadastro", nullable = false)
    private Timestamp dataHoraCadastro;

    public static UsuarioEntity criaUsuario(String login, String senha) {
        return new UsuarioEntity(login, senha);
    }

    protected UsuarioEntity() {
    }

    public UsuarioEntity(String login, String senha) {
        Objects.requireNonNull(login, "Login null");
        Objects.requireNonNull(senha, "Senha null");
        this.login = login;
        this.senha = senha;
        this.dataHoraCadastro = new Timestamp(System.currentTimeMillis());
        this.dataHoraLogin = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "UsuarioEntity{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", senha='" + senha + '\'' +
                ", dataHoraLogin=" + dataHoraLogin +
                ", dataHoraCadastro=" + dataHoraCadastro +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioEntity that = (UsuarioEntity) o;
        return id == that.id &&
                Objects.equals(login, that.login) &&
                Objects.equals(senha, that.senha) &&
                Objects.equals(dataHoraLogin, that.dataHoraLogin) &&
                Objects.equals(dataHoraCadastro, that.dataHoraCadastro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, senha, dataHoraLogin, dataHoraCadastro);
    }
}
