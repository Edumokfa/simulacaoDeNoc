package simulacaodenoc;

import java.util.Scanner;

public class SimulacaoDeNoCAnel {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Quantos nodos a rede terá: ");
        Integer nodos = scan.nextInt();
        
        while (true) {
            System.out.println("Quem é o Source? ");
            Integer source = scan.nextInt();
            System.out.println("Quem é o Target? ");
            Integer target = scan.nextInt();
            Integer diferenca = source - target;
            boolean isMovimentoDireita;
            if (diferenca < 0) {
                isMovimentoDireita = Math.abs(source - target) < (nodos / 2);
            } else {
                isMovimentoDireita = Math.abs(source - target) > (nodos / 2);
            }
            int anterior;
            System.out.println("Proc[" + source + "] criou a mensagem");
            if (source.equals(target)) {
                exibeMsgSucesso(source);
                continue;
            }
            while (true) {
                anterior = source;
                if (source < nodos - 1) {
                    if (isMovimentoDireita) {
                        source++;
                    } else {
                        if (source != 0) {
                            source--;
                        } else {
                            source = nodos - 1;
                        }
                    }
                } else {
                    if (isMovimentoDireita) {
                        source = 0;
                    } else {
                        source--;
                    }
                }
                System.out.println("Proc[" + anterior + "] Enviou a mensagem para Proc[" + source + "]");
                System.out.println("Proc[" + source + "] Recebeu a mensagem de Proc[" + anterior + "]");
                if (source.equals(target)) {
                    exibeMsgSucesso(source);
                    break;
                } else {
                    System.out.println("Proc[" + source + "] NÃO é o destino");
                }
            }
        }
    }

    private static void exibeMsgSucesso(Integer source) {
        System.out.println("Proc[" + source + "] é o destino");
        System.out.println("Proc[" + source + "] consumiu a mensagem");
    }
}
