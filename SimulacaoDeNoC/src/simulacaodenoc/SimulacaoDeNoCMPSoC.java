package simulacaodenoc;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SimulacaoDeNoCMPSoC {

    static class Objeto {

        public String nome = "";
        public int valor = 0;
        public Integer largura;
        public Integer altura;

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

    public static Objeto[][] preencheValoreMatriz(Objeto[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                matriz[i][j] = new Objeto();
            }
        }
        System.out.println("Informando as tarefas (Informe o nome 'parar' para parar)");
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("Informe o nome da tarefa");
            String valorLido = scan.next();

            if (valorLido.equals("parar")) {
                break;
            }

            System.out.println("Informe a posição X");
            int altura = scan.nextInt();

            System.out.println("Informe a posição Y");
            int largura = scan.nextInt();

            matriz[altura][largura] = new Objeto(valorLido, altura, largura);

        }
//        "A", 0, 0
//        "B", 1, 3
//        "C", 2, 2
//        "D", 3, 1

//        "X", 0, 0
//        "T", 2, 3
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
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("C:\\Users\\edumo\\Desktop\\SimulacaoDeNoC\\json.json"));
            JSONArray objeto = (JSONArray) jsonObject.get("aplicacoes");
            for (Object aplicacao : objeto.toArray()) {
                Scanner scan = new Scanner(System.in);

                System.out.println("Largura: ");
                int largura = scan.nextInt();

                System.out.println("Altura: ");
                int altura = scan.nextInt();

                if (altura > 9 || largura > 9) {
                    System.out.println("Matriz Inválida");
                    return;
                }

                JSONObject aplicacaoAux = (JSONObject) aplicacao;
                JSONArray listTarefas = (JSONArray) aplicacaoAux.get("grafo_tarefas");
                List<Tarefa> tarefas = new ArrayList<>();
                Objeto[][] matriz = new Objeto[largura][altura];
                matriz = preencheValoreMatriz(matriz);
                for (Object tarefaJson : listTarefas.toArray()) {
                    JSONObject tarefaAux = (JSONObject) tarefaJson;
                    String origem = tarefaAux.get("tarefa_origem").toString();
                    String destino = tarefaAux.get("tarefa_destino").toString();
                    Long pacotes = (Long) tarefaAux.get("quantidade_pacotes");

                    tarefas.add(new Tarefa(origem, destino, pacotes.intValue()));
                }

                for (Tarefa tarefa : tarefas) {
                    Objeto posicaoOrigem = buscaPosicaoTarefa(matriz, tarefa.origem);
                    int sourceY = posicaoOrigem.altura;
                    int sourceX = posicaoOrigem.largura;

                    Objeto posicaoDestino = buscaPosicaoTarefa(matriz, tarefa.destino);
                    Integer targetY = posicaoDestino.altura;
                    Integer targetX = posicaoDestino.largura;

                    boolean isMovimentoBaixo = sourceY < targetY;

                    Objeto primeiroItem = matriz[sourceX][sourceY];
                    primeiroItem.valor += tarefa.pacotes;
                    if (sourceY != targetY) {
                        while (true) {
                            if (sourceY < altura - 1) {
                                if (isMovimentoBaixo) {
                                    sourceY++;
                                } else {
                                    if (sourceY != 0) {
                                        sourceY--;
                                    }
                                }
                            } else {
                                if (!isMovimentoBaixo) {
                                    sourceY--;
                                }
                            }
                            Objeto objetoAtual = matriz[sourceX][sourceY];
                            objetoAtual.valor += tarefa.pacotes;
                            if (sourceY == targetY) {
                                break;
                            }
                        }
                    }

                    boolean isMovimentoDireita = sourceX < targetX;
                    if (sourceX != targetX) {
                        while (true) {
                            if (sourceX < largura - 1) {
                                if (isMovimentoDireita) {
                                    sourceX++;
                                } else {
                                    if (sourceX != 0) {
                                        sourceX--;
                                    }
                                }
                            } else {
                                if (!isMovimentoDireita) {
                                    sourceX--;
                                }
                            }
                            Objeto objetoAtual = matriz[sourceX][sourceY];
                            objetoAtual.valor += tarefa.pacotes;
                            if (sourceX == targetX) {
                                break;
                            }
                        }
                    }
                }

                //linhas
                for (int i = 0; i < matriz.length; i++) {
                    //Colunas
                    for (int j = 0; j < matriz[0].length; j++) {
                        Objeto objetoAtual = matriz[i][j];
                        System.out.print(objetoAtual.valor);
                    }
                    System.out.println("");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
