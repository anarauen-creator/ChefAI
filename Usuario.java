import java.util.List;
import java.util.ArrayList;

public class Usuario {
    private List<Ingrediente> despensa;
    private List<String> restricoes;

    public Usuario() {
        this.despensa = new ArrayList<>();
        this.restricoes = new ArrayList<>();
    }

    public void adicionarIngrediente(String nome, String qtd) {
        Ingrediente novo = new Ingrediente(nome, qtd);
        despensa.add(novo);
    }

    public void adicionarRestricao(String restricao) {
        restricoes.add(restricao);
    }

    public List<Ingrediente> getDespensa() {
        return despensa;
    }

    public List<String> getRestricoes() {
        return restricoes;
    }
    
    // Formata os dados para enviar à IA
    public String getResumoUsuario() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ingredientes disponíveis: ");
        for(Ingrediente i : despensa) {
            sb.append(i.getNome()).append(", ");
        }
        if(!restricoes.isEmpty()) {
            sb.append(". Restrições alimentares: ").append(String.join(", ", restricoes));
        }
        return sb.toString();
    }
}