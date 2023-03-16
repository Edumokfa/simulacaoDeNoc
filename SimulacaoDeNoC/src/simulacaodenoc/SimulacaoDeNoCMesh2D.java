package simulacaodenoc;

import java.util.Scanner;

public class SimulacaoDeNoCMesh2D {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("Largura: ");
        int largura = scan.nextInt();

        System.out.println("Altura: ");
        int altura = scan.nextInt();

        if (altura > 9 || largura > 9) {
            System.out.println("Matriz Inválida");
            return; //continue;
        }

        while (true) {
            System.out.println("Quem é o Source Y? ");
            int sourceY = scan.nextInt();
            System.out.println("Quem é o Source X? ");
            int sourceX = scan.nextInt();

            System.out.println("Quem é o Target Y? ");
            Integer targetY = scan.nextInt();
            System.out.println("Quem é o Target X? ");
            Integer targetX = scan.nextInt();

            boolean isMovimentoBaixo = sourceY < targetY;

            int anteriorY = sourceY;
            int anteriorX = sourceX;
            System.out.println("Proc[" + sourceY + "]" + " [" + sourceX + "] criou a mensagem");
            if (sourceY != targetY) {
                while (true) {
                    anteriorY = sourceY;
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
                    System.out.println("ProcY[" + anteriorY + "] ProcX[" + anteriorX + "] Enviou a mensagem para ProcY[" + sourceY + "] ProcX[" + anteriorX + "]");
                    System.out.println("ProcY[" + sourceY + "] ProcX[" + anteriorX + "] Recebeu a mensagem de ProcY[" + anteriorY + "] ProcX[" + anteriorX + "]");
                    System.out.println("ProcY[" + sourceY + "] ProcX[" + anteriorX + "] NÃO é o destino");
                    if (sourceY == targetY) {
                        anteriorY = sourceY;
                        break;
                    }
                }
            }
            
            boolean isMovimentoDireita = sourceX < targetX;
            if (sourceX != targetX) {
                while (true) {
                    anteriorX = sourceX;
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
                    System.out.println("ProcY[" + anteriorY + "] ProcX[" + anteriorX + "] Enviou a mensagem para ProcY[" + sourceY + "] ProcX[" + sourceX + "]");
                    System.out.println("ProcY[" + sourceY + "] ProcX[" + sourceX + "] Recebeu a mensagem de ProcY[" + anteriorY + "] ProcX[" + anteriorX + "]");
                    if (sourceX == targetX) {
                        break;
                    } else{
                        System.out.println("ProcY[" + sourceY + "] ProcX[" + sourceX + "] NÃO é o destino");
                    }
                }
            }
            System.out.println("Proc[" + sourceY + "]" + " [" + sourceX + "] é o destino");
            System.out.println("Proc[" + sourceY + "]" + " [" + sourceX + "] consumiu a mensagem");
        }
    }
}