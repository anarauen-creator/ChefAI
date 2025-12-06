import java.util.Scanner;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;

public class ChefAI {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Usuario usuario = new Usuario();

        System.out.println("Bem-vindo ao ChefAI - Seu Assistente Culinário Inteligente!");
        System.out.println("Vamos ver o que tem na sua geladeira.");

        // Loop de cadastro
        while (true) {
            System.out.print("Digite um ingrediente (ou 'fim' para encerrar): ");
            String nome = scanner.nextLine();
            if (nome.equalsIgnoreCase("fim")) break;

            System.out.print("Quantidade aproximada (ex: 2 unidades, 500g): ");
            String qtd = scanner.nextLine();

            usuario.adicionarIngrediente(nome, qtd);
        }

        // Restrições
        System.out.print("Você tem alguma restrição alimentar? (Digite ou tecle Enter para pular): ");
        String restricao = scanner.nextLine();
        if (!restricao.isEmpty()) {
            usuario.adicionarRestricao(restricao);
        }

        // Ler API Key do arquivo
        String apiKey = lerApiKey();
        
        if (apiKey == null) {
            System.out.println("ERRO CRÍTICO: Arquivo config.txt não encontrado ou vazio.");
            return;
        }

        // Executar Sugestão
        SugestorBase sugestor = new SugestorOpenRouter(apiKey); // <--- NOVO NOME DA CLASSE
        List<Receita> sugestoes = sugestor.sugerirReceitas(usuario);

        // Exibir Resultados
        if (sugestoes.isEmpty()) {
            System.out.println("Não foi possível gerar sugestões no momento.");
        } else {
            System.out.println("\n--- SUGESTÕES DO CHEF ---");
            for (Receita r : sugestoes) {
                r.exibirDetalhes();
            }
        }
        
        scanner.close();
    }

    private static String lerApiKey() {
        try {
            File arquivo = new File("config.txt");
            Scanner leitorArquivo = new Scanner(arquivo);
            if (leitorArquivo.hasNextLine()) {
                String chave = leitorArquivo.nextLine().trim();
                leitorArquivo.close();
                return chave;
            }
            leitorArquivo.close();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de configuração não encontrado na pasta do projeto.");
        }
        return null;
    }
}