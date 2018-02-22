package vo;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UsuarioVO.class)
public abstract class UsuarioVO_ {

	public static volatile SingularAttribute<UsuarioVO, String> senha;
	public static volatile SingularAttribute<UsuarioVO, Date> dataHoraCadastro;
	public static volatile SingularAttribute<UsuarioVO, Integer> id;
	public static volatile SingularAttribute<UsuarioVO, String> login;
	public static volatile SingularAttribute<UsuarioVO, Date> dataHoraLogin;

}

