package ann.geral;

import javafx.util.StringConverter;

public class FuncaoDecaimentoConverter extends StringConverter<FuncaoDecaimento> {
    @Override
    public String toString(FuncaoDecaimento object) {
        return object.nome;
    }

    @Override
    public FuncaoDecaimento fromString(String string) {
        return null;
    }
}
