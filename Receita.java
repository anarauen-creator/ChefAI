import java.util.List;
import java.util.ArrayList;

public class Receita {
    private String nome;
    private int tempoPreparoMinutos;
    private String modoPreparo;
    private List<Ingrediente> ingredientes;

    public Receita(String nome, int tempo, String preparo) {
        this.nome = nome;
        this.tempoPreparoMinutos = tempo;
        this.modoPreparo = preparo;
        this.ingredientes = new ArrayList<>();
    }

    public void adicionarIngrediente(Ingrediente ing) {
        this.ingredientes.add(ing);
    }

    public void exibirDetalhes() {
        System.out.println("\n========================================");
        System.out.println("RECEITA: " + nome);
        System.out.println("TEMPO: " + tempoPreparoMinutos + " min");
        System.out.println("----------------------------------------");
        System.out.println("INGREDIENTES:");
        for (Ingrediente ing : ingredientes) {
            System.out.println(" - " + ing.toString());
        }
        System.out.println("----------------------------------------");
        System.out.println("MODO DE PREPARO:");
        System.out.println(modoPreparo);
        System.out.println("========================================\n");
    }
}