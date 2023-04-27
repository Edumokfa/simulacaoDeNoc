package simulacaodenoc;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author edumo
 */
public class Objeto {
    
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