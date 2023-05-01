package simulacaodenoc;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SimulacaoDeNoCMPSoCAspiralInverso {

    private static final String diretorio = "C:\\Users\\edumo\\OneDrive\\Documentos\\GitHub\\simulacaoDeNoc\\SimulacaoDeNoC\\";
    static boolean movDireita = true, movEsquerda = false, movBaixo = false, movCima = false;
    static int altura = 0, largura = 1;

    public static class TarefaReferencia {

        public int altura;
        public int largura;

        public TarefaReferencia(int altura, int largura) {
            this.altura = altura;
            this.largura = largura;
        }
    }

    private static Objeto[][] preencheValoreMatriz(Objeto[][] matriz, String jsonAplicacao, int quantTarefas) {
        try {
            Map<String, TarefaReferencia> tarefasReferencia = new HashMap<>();

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(diretorio + jsonAplicacao + ".json"));
            JSONArray tarefas = (JSONArray) jsonObject.get("grafo_tarefas");
            List<String> tarefasDistintas = new ArrayList<>();
            for (Object tarefa : tarefas.toArray()) {
                JSONObject jsonTarefa = (JSONObject) tarefa;
                String tarefaOrigem = (String) jsonTarefa.get("tarefa_origem");
                String tarefaDestino = (String) jsonTarefa.get("tarefa_destino");
                if (!tarefasDistintas.contains(tarefaOrigem)) {
                    tarefasDistintas.add(tarefaOrigem);
                }
                if (!tarefasDistintas.contains(tarefaDestino)) {
                    tarefasDistintas.add(tarefaDestino);
                }
            }

            for (String tarefa : tarefasDistintas) {
                preencheTarefa(matriz, quantTarefas, tarefa);
                tarefasReferencia.put(tarefa, new TarefaReferencia(altura, largura));
                for (int i = 0; i < matriz.length; i++) {
                    for (int j = 0; j < matriz[0].length; j++) {
                        Objeto objetoAtual = matriz[i][j];
                        System.out.print(String.format("%15s", String.join("|", objetoAtual.listaTarefas.stream().map(t -> t.nomeTarefa).collect(Collectors.toList()))));
                    }
                    System.out.println("");
                }
                System.out.println("\n");
            }
            
            for (Object tarefa : tarefas.toArray()) {
                JSONObject jsonTarefa = (JSONObject) tarefa;
                String tarefaOrigem = (String) jsonTarefa.get("tarefa_origem");
                String tarefaDestino = (String) jsonTarefa.get("tarefa_destino");
                TarefaReferencia ref = tarefasReferencia.get(tarefaDestino);
                matriz[ref.altura][ref.largura].valor += (Long) jsonTarefa.get("quantidade_pacotes");
            }
            
            for (int i = 0; i < matriz.length; i++) {
                    for (int j = 0; j < matriz[0].length; j++) {
                        Objeto objetoAtual = matriz[i][j];
                        System.out.print(String.format("%15s", String.join("|", objetoAtual.valor + "")));
                    }
                    System.out.println("");
                }
        } catch (IOException | NumberFormatException | ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Não foi possível ler o arquivo");
        }
        return matriz;
    }

    private static void preencheTarefa(Objeto[][] matriz, int quantTarefas, String tarefa) {
        Objeto objetoMatriz = matriz[altura][largura];
        if (objetoMatriz.quantidadeUsos >= quantTarefas) {
            //movimenta na matriz
            if (largura < matriz[0].length - 1 && movDireita) {
                largura++;
                //Se for o último ou o próximo já estiver preenchido, muda direção
                if (largura == matriz[0].length - 1 || !matriz[altura][largura + 1].listaTarefas.isEmpty()) {
                    movDireita = false;
                    movBaixo = true;
                }
            } else if (altura < matriz.length - 1 && movBaixo) {
                altura++;
                //Se for o último ou o próximo já estiver preenchido, muda direção
                if (altura == matriz[0].length - 1 || matriz[altura + 1][largura].quantidadeUsos > 0) {
                    movBaixo = false;
                    movEsquerda = true;
                }
            } else if (largura != 0 && movEsquerda) {
                largura--;
                //Se for o último ou o próximo já estiver preenchido, muda direção
                if (largura == 0 || matriz[altura][largura - 1].quantidadeUsos > 0) {
                    movEsquerda = false;
                    movCima = true;
                }
            } else if (movCima) {
                altura--;
                //Se for o último ou o próximo já estiver preenchido, muda direção
                if (altura == 0 || matriz[altura - 1][largura].quantidadeUsos > 0) {
                    movCima = false;
                    movDireita = true;
                }
            }
            objetoMatriz = matriz[altura][largura];
        }

        objetoMatriz.quantidadeUsos++;
        objetoMatriz.listaTarefas.add(new Tarefa(tarefa));
    }

    public static Objeto[][] preencherTarefas(String nomeApp) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(diretorio + nomeApp + ".json"));
            Integer largura = Integer.valueOf((String) jsonObject.get("MPSOC_SIZE_X"));
            Integer altura = Integer.valueOf((String) jsonObject.get("MPSOC_SIZE_Y"));
            JSONArray objeto = (JSONArray) jsonObject.get("TEST");
            Integer tarefas = Integer.valueOf((String) jsonObject.get("TASKS_PER_PROCESSOR"));
            Objeto[][] matriz = new Objeto[largura][altura];
            for (Objeto[] matriz1 : matriz) {
                for (int j = 0; j < matriz[0].length; j++) {
                    matriz1[j] = new Objeto();
                }
            }

            Objeto objetoInicial = matriz[0][0];
            objetoInicial.altura = 0;
            objetoInicial.largura = 0;
            objetoInicial.listaTarefas.add(new Tarefa("GMP"));
            objetoInicial.quantidadeUsos++;
            for (Object aplicacao : objeto.toArray()) {
                JSONObject aplicacaoAux = (JSONObject) aplicacao;

                Integer quantidadeExecucoes = Integer.valueOf((String) aplicacaoAux.get("QTD"));
                while (quantidadeExecucoes > 0) {
                    matriz = preencheValoreMatriz(matriz, (String) aplicacaoAux.get("APP"), tarefas);
                    quantidadeExecucoes--;
                }
            }
            return matriz;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        preencherTarefas("Test1");

    }
}
