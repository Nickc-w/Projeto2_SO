import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;



public class Buffer {

    // ---------------- DECLARAÇÃO DO RECURSO COMPARTILHADO ----------------------------------------------
    List<Integer> buffer = new ArrayList<>();

    // DECLARAÇÃO DO LOG
    private final PrintWriter logWriter;

    // ----------------- DECLARAÇÃO DO MUTEX (para a Exclusão Mútua) -------------------------------------
    // Protege o recurso compartilhado (o buffer) de acessos simultâneos
    // Com ele, a thread A pode chamar lock(), para obter acesso exclusivo ao recurso
    // E depois unlock para liberar o acesso
    private final ReentrantLock cadeado = new ReentrantLock();

    // ---------------- DECLARAÇÃO DOS SEMÁFOROS (para Sincronização e Condição) --------------------------
    // SEMÁFOROS (para Sincronização e Condição)
    // Acquire = confere se tem espaços vazios (> 0), bloqueia se não tiver (=0)
    // 1. quantos espaços estão disponíveis
    private final Semaphore espacosDisponiveis = new Semaphore(7);
    // 2. quantos espaços estão ocupados.
    private final Semaphore espacosOcupados = new Semaphore(0);

    // --------------------------- CONSTRUTOR ------------------------------------
    public Buffer() {
        try {
            // Inicializa o logWriter, lançando exceção se falhar
            FileWriter fileWriter = new FileWriter("log_producao_consumo.txt", true); // Cria/abre o arquivo. Se o arquivo ja existir, novos dados serao adicionados ao final
            this.logWriter = new PrintWriter(fileWriter, true); // Adiciona métodos fáceis para formatar e gravar texto.
            // Garantindo que o texto seja gravado no disco imediatamente após cada chamada a println()
            // objeto logWriter criado
        } catch (IOException e) { // se falhar
            // Encerra o programa se não puder criar o arquivo de log
            throw new RuntimeException("Erro ao criar arquivo de log.", e);
        }
    }

    public void fecharLog() {
        logWriter.close();

    }



    // Enquanto a thread está bloqueada esperando, ela pode ser interrompida pelo sistema (se precisar)
    // Bloqueado = É um ponto de parada seguro para ser interrompido.
    // Se isso acontecer, o metodo acquire() lança uma exceção chamada InterruptedException. Avisando ao Java que o metodo pode ser interrompido.
    // ( O Java obriga você a lidar com essa possibilidade. )
    // Você trata a exceção no RUN() de Produtor e Consumidor que vão utilizar as funções
    public void produzir(int item) throws InterruptedException {

        espacosDisponiveis.acquire();
        cadeado.lock();

        // Try-finally = seu uso com o ReentrantLock é obrigatório para evitar um problema catastrófico: o Deadlock.
        try {
            buffer.add(item);

            // Gravação do Log de Produção
            String logProducao = String.format(
                    "Produtor - Inserido item %d; Buffer: %d/%d",
                    item,
                    buffer.size(),
                    7
            );
            logWriter.println(logProducao);

        } finally {
            // O código dentro do finally sempre será executado
            // Se a thread saísse do try devido a uma exceção sem liberar o bloqueio, o Mutex ficaria eternamente bloqueado
            cadeado.unlock();
        }

        espacosOcupados.release();
    }


    public int consumir() throws InterruptedException {

        int itemConsumido = 0;
        espacosOcupados.acquire();
        cadeado.lock();

        try {
            // Seguindo a regra FIFO, o primeiro a entrar vair ser o primeiro a sair
            itemConsumido = buffer.remove(0);

            // Gravação do Log de Consumo
            String logConsumo = String.format(
                    "Consumidor - Consumido item %d; Buffer: %d/%d",
                    itemConsumido,
                    buffer.size(),
                    7
            );
            logWriter.println(logConsumo);
        } finally {
            cadeado.unlock();
        }

        espacosDisponiveis.release();


        return itemConsumido;
    }


}
