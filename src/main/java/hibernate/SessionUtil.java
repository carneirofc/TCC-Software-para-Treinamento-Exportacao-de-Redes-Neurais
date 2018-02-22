package hibernate;

import main.Recursos;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import vo.UsuarioEntity;

import java.io.Serializable;
import java.util.Objects;

public class SessionUtil {
    private static final SessionFactory ourSessionFactory;

    static {
        try {
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(Recursos.HIBERNATE_CFG), "Não foi possível carregar o arquivo hibernate.cfg.xml").toExternalForm())
                    .build();
            ourSessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return ourSessionFactory;
    }

    public static void main(final String[] args) {
        try (Session session = getSessionFactory().openSession()) {
            UsuarioEntity usuarioEntity1 = UsuarioEntity.criaUsuario("admin", "admin");
            UsuarioEntity usuarioEntity2 = UsuarioEntity.criaUsuario("guest", "guest");

            session.beginTransaction();
            session.save(usuarioEntity1);
            session.save(usuarioEntity2);
            session.getTransaction().commit();

            session.close();
        }
    }
}
