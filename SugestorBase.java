import java.util.List;

public abstract class SugestorBase {
    protected String apiKey;

    public SugestorBase(String apiKey) {
        this.apiKey = apiKey;
    }

    // Método abstrato: obriga quem herdar a implementar a lógica
    public abstract List<Receita> sugerirReceitas(Usuario usuario);
}