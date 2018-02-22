package vo;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RedeNeuralVO.class)
public abstract class RedeNeuralVO_ {

	public static volatile SingularAttribute<RedeNeuralVO, Integer> id;
	public static volatile SingularAttribute<RedeNeuralVO, String> topologiaJson;
	public static volatile SingularAttribute<RedeNeuralVO, Long> epocasParaTreinamento;
	public static volatile SingularAttribute<RedeNeuralVO, String> pesosJson;

}

