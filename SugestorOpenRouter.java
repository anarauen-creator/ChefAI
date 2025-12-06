import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;

public class SugestorOpenRouter extends SugestorBase {

    // Endereço padrão do OpenRouter
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
    
    // MODELO ESCOLHIDO (Você pode trocar o texto entre aspas por outro modelo gratuito do OpenRouter)
    // Exemplos: "google/gemini-2.0-flash-exp:free", "meta-llama/llama-3.2-3b-instruct:free"
    private static final String MODELO = "kwaipilot/kat-coder-pro:free"; 

    public SugestorOpenRouter(String apiKey) {
        super(apiKey.trim());
    }

    @Override
    public List<Receita> sugerirReceitas(Usuario usuario) {
        List<Receita> receitasSugeridas = new ArrayList<>();
        
        try {
            // 1. Preparar o Prompt
            String promptSistema = "Você é um chef de cozinha experiente. " +
                "O usuário tem estes ingredientes: " + usuario.getResumoUsuario() + ". " +
                "Sugira 3 receitas rápidas (max 30 min) e fáceis. " +
                "IMPORTANTE: Sua resposta deve ser APENAS um JSON puro, sem explicações, sem markdown (```json), " +
                "neste formato estrito: " +
                "[{ \"nome\": \"Nome da Receita\", \"tempo\": 20, \"preparo\": \"Passo a passo aqui...\", \"ingredientes\": [\"item1\", \"item2\"] }]";

            // 2. Montar o JSON da Requisição (Padrão OpenAI/OpenRouter)
            JSONObject bodyJson = new JSONObject();
            bodyJson.put("model", MODELO);
            
            JSONArray messages = new JSONArray();
            messages.put(new JSONObject().put("role", "user").put("content", promptSistema));
            
            bodyJson.put("messages", messages);
            // Temperatura 0.7 para ser criativo mas não alucinar muito
            bodyJson.put("temperature", 0.7); 

            // 3. Configurar e Enviar a Requisição
            HttpClient client = HttpClient.newHttpClient();
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + this.apiKey) // Cabeçalho de autenticação
                // Cabeçalhos opcionais exigidos pelo OpenRouter para boas práticas
                .header("HTTP-Referer", "http://localhost:8080") 
                .header("X-Title", "ChefAI Student Project")
                .POST(HttpRequest.BodyPublishers.ofString(bodyJson.toString()))
                .build();

            System.out.println("Consultando o OpenRouter (" + MODELO + ")...");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 4. Processar a Resposta
            if (response.statusCode() == 200) {
                JSONObject jsonResposta = new JSONObject(response.body());
                
                // O caminho para achar o texto no OpenRouter é: choices[0] -> message -> content
                String textoResposta = jsonResposta.getJSONArray("choices")
                                        .getJSONObject(0)
                                        .getJSONObject("message")
                                        .getString("content");
                
                // Limpeza de segurança (caso o modelo coloque blocos de código)
                textoResposta = textoResposta.replace("```json", "").replace("```", "").trim();
                
                // Converter para Objetos Java
                JSONArray arrayReceitas = new JSONArray(textoResposta);
                
                for (int i = 0; i < arrayReceitas.length(); i++) {
                    JSONObject r = arrayReceitas.getJSONObject(i);
                    Receita novaReceita = new Receita(
                        r.getString("nome"),
                        r.getInt("tempo"),
                        r.getString("preparo")
                    );
                    
                    JSONArray ings = r.getJSONArray("ingredientes");
                    for(int j=0; j<ings.length(); j++) {
                        novaReceita.adicionarIngrediente(new Ingrediente(ings.getString(j), "a gosto"));
                    }
                    receitasSugeridas.add(novaReceita);
                }
            } else {
                System.out.println("❌ Erro no OpenRouter (" + response.statusCode() + "):");
                System.out.println(response.body());
            }

        } catch (Exception e) {
            System.out.println("❌ Erro ao processar: " + e.getMessage());
            // Imprime o erro completo no terminal para ajudar a debugar
            e.printStackTrace(); 
        }

        return receitasSugeridas;
    }
}