package ann.funcao_ativacao;

import javafx.util.StringConverter;
import main.config.ConfigGeral;

public class FuncaoTipoStringConverter extends StringConverter<FuncaoTipo> {
    @Override
    public String toString(FuncaoTipo object) {
        return object.getNomeFuncao() + " : " + object.getStringFuncao(ConfigGeral.getConfigGeralAtual().getTermoLinear());
    }

    @Override
    public FuncaoTipo fromString(String string) {
        return null;
    }
}
