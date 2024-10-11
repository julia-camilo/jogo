package entidade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MovimentoCirculo extends JPanel implements KeyListener {

    private int circuloX = 100; // Posição inicial do círculo no eixo X
    private int circuloY = 100; // Posição inicial do círculo no eixo Y
    private int raioCirculo = 25; // Raio do círculo
    private int velocidade = 10; // Velocidade do movimento do círculo
    private boolean circuloVivo = true; // Estado do círculo

    private int quadradoX = 500; // Posição inicial do quadrado no eixo X
    private int quadradoY = 500; // Posição inicial do quadrado no eixo Y
    private int tamanhoQuadrado = 30; // Tamanho do quadrado
    private int velocidadeQuadrado = 2; // Velocidade inicial do quadrado

    private int trianguloX = 100; // Posição inicial do triângulo no eixo X
    private int trianguloY = 500; // Posição inicial do triângulo no eixo Y
    private int tamanhoTriangulo = 30; // Tamanho do triângulo
    private int velocidadeTriangulo = 2; // Velocidade inicial do triângulo

    private String dificuldade = "normal"; // Nível de dificuldade
    private Color fundoCor; // Cor do fundo

    public MovimentoCirculo(String dificuldade) {
        this.dificuldade = dificuldade;
        ajustarDificuldade();
        addKeyListener(this);
        setFocusable(true);
        iniciarTimer(); // Inicia o timer para aumentar a velocidade do quadrado e do triângulo
        iniciarTimerCorFundo(); // Inicia o timer para mudar a cor do fundo
    }

    private void ajustarDificuldade() {
        switch (dificuldade) {
            case "facil":
                velocidadeQuadrado = 1;
                velocidadeTriangulo = 1;
                break;
            case "dificil":
                velocidadeQuadrado = 4;
                velocidadeTriangulo = 4;
                break;
            case "normal":
            default:
                velocidadeQuadrado = 2;
                velocidadeTriangulo = 2;
                break;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Define a cor de fundo
        g.setColor(fundoCor);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Desenha o círculo se estiver vivo
        if (circuloVivo) {
            g.setColor(Color.magenta);
            g.fillOval(circuloX - raioCirculo, circuloY - raioCirculo, raioCirculo * 2, raioCirculo * 2);
        } else {
            g.setColor(Color.red);
            g.drawString("Você foi pego! Reinicie o programa", 250, 250); // Mensagem quando o círculo morre
        }

        // Desenha o quadrado
        g.setColor(Color.orange);
        g.fillRect(quadradoX, quadradoY, tamanhoQuadrado, tamanhoQuadrado);

        // Desenha o triângulo
        g.setColor(Color.blue);
        int[] xPoints = {trianguloX, trianguloX + tamanhoTriangulo, trianguloX - tamanhoTriangulo};
        int[] yPoints = {trianguloY, trianguloY + tamanhoTriangulo, trianguloY + tamanhoTriangulo};
        g.fillPolygon(xPoints, yPoints, 3); // Desenha o triângulo
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int codigoTecla = e.getKeyCode();

        switch (codigoTecla) {
            case KeyEvent.VK_UP:
                if (circuloVivo) circuloY -= velocidade;
                break;
            case KeyEvent.VK_DOWN:
                if (circuloVivo) circuloY += velocidade;
                break;
            case KeyEvent.VK_LEFT:
                if (circuloVivo) circuloX -= velocidade;
                break;
            case KeyEvent.VK_RIGHT:
                if (circuloVivo) circuloX += velocidade;
                break;
            case KeyEvent.VK_SHIFT:
                velocidade += 5;
                break;
        }

        // Impede que o círculo saia da tela
        if (circuloX - raioCirculo < 0) {
            circuloX = raioCirculo;
        } else if (circuloX + raioCirculo > getWidth()) {
            circuloX = getWidth() - raioCirculo;
        }
        if (circuloY - raioCirculo < 0) {
            circuloY = raioCirculo;
        } else if (circuloY + raioCirculo > getHeight()) {
            circuloY = getHeight() - raioCirculo;
        }

        // Movimento do quadrado e do triângulo em direção ao círculo
        moveQuadrado();
        moveTriangulo();

        // Verifica se o quadrado ou o triângulo encostaram no círculo
        verificaColisao();

        repaint();
    }

    private void moveQuadrado() {
        if (quadradoX < circuloX) {
            quadradoX += velocidadeQuadrado; // Move o quadrado para a direita
        } else if (quadradoX > circuloX) {
            quadradoX -= velocidadeQuadrado; // Move o quadrado para a esquerda
        }

        if (quadradoY < circuloY) {
            quadradoY += velocidadeQuadrado; // Move o quadrado para baixo
        } else if (quadradoY > circuloY) {
            quadradoY -= velocidadeQuadrado; // Move o quadrado para cima
        }
    }

    private void moveTriangulo() {
        if (trianguloX < circuloX) {
            trianguloX += velocidadeTriangulo; // Move o triângulo para a direita
        } else if (trianguloX > circuloX) {
            trianguloX -= velocidadeTriangulo; // Move o triângulo para a esquerda
        }

        if (trianguloY < circuloY) {
            trianguloY += velocidadeTriangulo; // Move o triângulo para baixo
        } else if (trianguloY > circuloY) {
            trianguloY -= velocidadeTriangulo; // Move o triângulo para cima
        }
    }

    private void verificaColisao() {
        // Verifica se o quadrado ou o triângulo colidiram com o círculo
        if (circuloVivo && ((quadradoX < circuloX + raioCirculo && quadradoX + tamanhoQuadrado > circuloX - raioCirculo &&
                quadradoY < circuloY + raioCirculo && quadradoY + tamanhoQuadrado > circuloY - raioCirculo) ||
                (trianguloX < circuloX + raioCirculo && trianguloX + tamanhoTriangulo > circuloX - raioCirculo &&
                        trianguloY < circuloY + raioCirculo && trianguloY + tamanhoTriangulo > circuloY - raioCirculo))) {
            circuloVivo = false; // O círculo "morre"
        }
    }

    private void iniciarTimer() {
        // Timer que aumenta a velocidade do quadrado e do triângulo a cada 15 segundos
        Timer timer = new Timer(15000, e -> {
            velocidadeQuadrado += 1; // Aumenta a velocidade do quadrado
            velocidadeTriangulo += 1; // Aumenta a velocidade do triângulo
        });
        timer.start(); // Inicia o timer
    }

    private void iniciarTimerCorFundo() {
        // Array de cores escuras e opacas
        Color[] cores = {
                new Color(30, 30, 30), // Cinza escuro
                new Color(50, 50, 50), // Cinza médio
                new Color(70, 70, 70), // Cinza claro
                new Color(20, 40, 20), // Verde escuro
                new Color(40, 20, 20), // Vermelho escuro
                new Color(20, 20, 40)  // Azul escuro
        };

        // Timer que muda a cor do fundo a cada 2 segundos
        Timer timer = new Timer(2000, e -> {
            fundoCor = cores[(int)(Math.random() * cores.length)];
            repaint();
        });
        timer.start(); // Inicia o timer de mudança de cor
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        String dificuldade = (String) JOptionPane.showInputDialog(null, "Escolha o nível de dificuldade:",
                "Nível de Dificuldade", JOptionPane.QUESTION_MESSAGE, null,
                new Object[]{"facil", "normal", "dificil"}, "normal");

        JFrame quadro = new JFrame("Mova o círculo");
        quadro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        quadro.add(new MovimentoCirculo(dificuldade));
        quadro.setSize(650, 650);
        quadro.setVisible(true);
    }
}
