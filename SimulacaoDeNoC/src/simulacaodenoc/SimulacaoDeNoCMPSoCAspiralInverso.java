package simulacaodenoc;

import com.sun.tools.javac.util.StringUtils;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SimulacaoDeNoCMPSoCAspiralInverso {

    private static String diretorio = "C:\\Users\\edumo\\Documents\\GitHub\\simulacaoDeNoc\\SimulacaoDeNoC\\";
    static boolean movDireita = true, movEsquerda = false, movBaixo = false, movCima = false;
    static int altura = 0, largura = 0;

    static class Objeto {

        public String nome = "";
        public int valor = 0;
        public Integer largura;
        public Integer altura;
        public int quantidadeUsos = 0;
        public List<Tarefa> listaTarefas = new ArrayList<>();

        public Objeto() {
        }

        public Objeto(String nome, Integer largura, Integer altura) {
            this.nome = nome;
            this.altura = altura;
            this.largura = largura;
        }
    }

    static class Tarefa {

        public String nomeTarefa = "";

        public Tarefa(String nomeTarefa) {
            this.nomeTarefa = nomeTarefa;
        }
    }

    static Objeto[][] preencheValoreMatriz(Objeto[][] matriz, String jsonAplicacao, int quantTarefas) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(diretorio + jsonAplicacao + ".json"));
            JSONArray tarefas = (JSONArray) jsonObject.get("grafo_tarefas");
            for (Object tarefa : tarefas.toArray()) {
                JSONObject jsonTarefa = (JSONObject) tarefa;
                //testando apenas os números (AJUSTAR)
                String origem = (String) jsonTarefa.get("tarefa_origem");
                String destino = (String) jsonTarefa.get("tarefa_destino");

                preencheTarefa(matriz, quantTarefas, origem);
                preencheTarefa(matriz, quantTarefas, destino);

                for (int i = 0; i < matriz.length; i++) {
                    for (int j = 0; j < matriz[0].length; j++) {
                        Objeto objetoAtual = matriz[i][j];
                        System.out.print(String.format("%5s", String.join("|", objetoAtual.listaTarefas.stream().map(t -> t.nomeTarefa).collect(Collectors.toList()))));
                    }
                    System.out.println("");
                }
                System.out.println("\n");
            }
        } catch (IOException | NumberFormatException | ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Não foi possível ler o arquivo");
        }
        return matriz;
    }

    protected static void preencheTarefa(Objeto[][] matriz, int quantTarefas, String tarefa) {

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

    public static void main(String[] args) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(diretorio + "Test1.json"));
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

            for (Object aplicacao : objeto.toArray()) {
                JSONObject aplicacaoAux = (JSONObject) aplicacao;

                Integer quantidadeExecucoes = Integer.valueOf((String) aplicacaoAux.get("QTD"));
                while (quantidadeExecucoes > 0) {
                    matriz = preencheValoreMatriz(matriz, (String) aplicacaoAux.get("APP"), tarefas);
                    quantidadeExecucoes--;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
