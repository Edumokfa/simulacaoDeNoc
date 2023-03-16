package simulacaodenoc;

import java.util.Scanner;

public class SimulacaoDeNoCMesh3D {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("Altura: ");
        int altura = scan.nextInt();

        System.out.println("Largura: ");
        int largura = scan.nextInt();

        if (altura > 9 || largura > 9) {
            System.out.println("Matriz Inválida");
            return; //continue;
        }

        Nodo[][] matriz = new Nodo[altura][largura];
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                Nodo nodo = new Nodo(y, x);
                matriz[y][x] = nodo;
            }
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

            boolean isMovimentoBaixo;
            int diferencaY = sourceY - targetY;
            if (diferencaY < 0) {
                isMovimentoBaixo = Math.abs(sourceY - targetY) < (altura / 2);
            } else {
                isMovimentoBaixo = Math.abs(sourceY - targetY) > (altura / 2);
            }

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
                            } else {
                                sourceY = altura - 1;
                            }
                        }
                    } else {
                        if (isMovimentoBaixo) {
                            sourceY = 0;
                        } else {
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

            if (sourceX != targetX) {
                while (true) {
                    anteriorX = sourceX;
                    if (sourceX < largura - 1) {
                        if (isMovimentoBaixo) {
                            sourceX++;
                        } else {
                            if (sourceX != 0) {
                                sourceX--;
                            } else {
                                sourceX = largura - 1;
                            }
                        }
                    } else {
                        if (isMovimentoBaixo) {
                            sourceX = 0;
                        } else {
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

    public static class Nodo {

        private int posicaoY;
        private int posicaoX;

        public Nodo(int posicaoY, int posicaoX) {
            this.posicaoY = posicaoY;
            this.posicaoX = posicaoX;
        }

        public int getPosicaoX() {
            return posicaoX;
        }

        public void setPosicaoX(int posicaoX) {
            this.posicaoX = posicaoX;
        }

        public int getPosicaoY() {
            return posicaoY;
        }

        public void setPosicaoY(int posicaoY) {
            this.posicaoY = posicaoY;
        }
    }
}
