package simulacaodenoc;

import java.io.FileReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SimulacaoDeNoCMPSoCAspiralInverso {

    private static String diretorio = "C:\\Users\\edumo\\OneDrive\\Documentos\\GitHub\\simulacaoDeNoc\\SimulacaoDeNoC\\";

    static class Objeto {

        public String nome = "";
        public int valor = 0;
        public Integer largura;
        public Integer altura;
        public int quantidadeUsos = 0;

        public Objeto() {
        }

        public Objeto(String nome, Integer largura, Integer altura) {
            this.nome = nome;
            this.altura = altura;
            this.largura = largura;
        }
    }

    static class Tarefa {

        public String origem = "";
        public String destino = "";
        public int pacotes = 0;

        public Tarefa(String origem, String destino, int pacotes) {
            this.origem = origem;
            this.destino = destino;
            this.pacotes = pacotes;
        }
    }

    static Objeto[][] preencheValoreMatriz(Objeto[][] matriz, String jsonAplicacao) {
        for (Objeto[] matriz1 : matriz) {
            for (int j = 0; j < matriz[0].length; j++) {
                matriz1[j] = new Objeto();
            }
        }
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(diretorio + jsonAplicacao + "json"));
            JSONArray tarefas = (JSONArray) jsonObject.get("grafo_tarefas");
            for (Object tarefa : tarefas.toArray()) {
                JSONObject jsonTarefa = (JSONObject) tarefa;
                Integer origem = (Integer) jsonTarefa.get("tarefa_origem");
                Integer destino = (Integer) jsonTarefa.get("tarefa_destino");
                Integer pacotes = (Integer) jsonTarefa.get("quantidade_pacotes");

            }
        } catch (Exception e) {

        }
        return matriz;
    }

    public static Objeto buscaPosicaoTarefa(Objeto[][] matriz, String nome) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                Objeto objetoAtual = matriz[i][j];
                if (objetoAtual != null && nome.equals(objetoAtual.nome)) {
                    return objetoAtual;
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(diretorio + "Test1.json"));
            Integer largura = Integer.valueOf((String) jsonObject.get("MPSOC_SIZE_X"));
            Integer altura = Integer.valueOf((String) jsonObject.get("MPSOC_SIZE_Y"));
            JSONArray objeto = (JSONArray) jsonObject.get("TEST");
            Objeto[][] matriz = new Objeto[largura][altura];
            for (Object aplicacao : objeto.toArray()) {
                JSONObject aplicacaoAux = (JSONObject) aplicacao;

                Integer quantidadeExecucoes = Integer.valueOf((String) aplicacaoAux.get("QTD"));
                while (quantidadeExecucoes > 0) {
                    matriz = preencheValoreMatriz(matriz, (String) aplicacaoAux.get("APP"));
                    quantidadeExecucoes --;
                }
            }

            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[0].length; j++) {
                    Objeto objetoAtual = matriz[i][j];
                    System.out.print(objetoAtual.valor);
                }
                System.out.println("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
