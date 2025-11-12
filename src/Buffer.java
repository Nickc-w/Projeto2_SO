
// --------------------------------------------IMPORTAÇÕES -------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;



public class Buffer {

    // -------------------------------- DECLARAÇÃO DO RECURSO COMPARTILHADO --------------------------------------------
    List<Integer> buffer = new ArrayList<>(); // Lista

    // DECLARAÇÃO DO LOG
    private final PrintWriter logWriter; // Para escrever no arquivo (println)

    // ------------------------------- DECLARAÇÃO DO MUTEX (para a Exclusão Mútua) -------------------------------------

    // Protege o recurso compartilhado (o buffer) de acessos simultâneos
    // Com ele, a thread A pode chamar lock(), para obter acesso exclusivo ao recurso
    // E depois unlock para liberar o acesso
    private final ReentrantLock cadeado = new ReentrantLock();

    // --------------------------- DECLARAÇÃO DOS SEMÁFOROS (para Sincronização e Condição) ----------------------------

    // 1. quantos espaços estão disponíveis
    private final Semaphore espacosDisponiveis = new Semaphore(7);
        // Acquire = confere se tem espaços vazios (> 0). Se tiver, libera seu uso e decrescenta -1 do contador de "espaçosDisponiveis", senão bloqueia (= 0).
        // Release = acrescenta ao contador de "espaçosDisponiveis" + 1. Sinaliza ao Produtor que tem um espaço vazio

    // 2. quantos espaços estão ocupados.
    private final Semaphore espacosOcupados = new Semaphore(0);
        // Acquire = confere se tem espaços ocupados (> 0). Se tiver, libera seu uso e decrescenta -1 do contador de "espacosOcupados", senão bloqueia (=0)
        // Release = acrescenta ao contador de "espaçosOcupados" + 1. Sinaliza ao Consumidor que um item foi inserido.


    // -------------------------------------------- CONSTRUTOR/LOG -----------------------------------------------------
    public Buffer() {
        try {
            // Abre o arquivo, lança uma exceção se falhar = IOException
            FileWriter fileWriter = new FileWriter("log_producao_consumo.txt", false); // Sobescreve quando rodar o código várias vezes = false
            this.logWriter = new PrintWriter(fileWriter, true); // Para escrever dentro do arquivo usando "println"
        } catch (IOException e) { // se falhar, o erro é pego e tratado
            throw new RuntimeException("Erro ao criar arquivo de log.", e);
            // Encerra o programa se não puder criar o arquivo de log
        }
    }

    // Fechar o log
    public void fecharLog() {
        logWriter.close();
    }




    // ---------------------------------------- MÉTODOS PRODUZIR E CONSUMIR --------------------------------------------

    // Enquanto a thread está bloqueada esperando, ela pode ser interrompida pelo sistema (se precisar)
    // Se isso acontecer, o metodo acquire() lança uma exceção chamada InterruptedException.
    // ( O Java obriga você a lidar com essa possibilidade. )
    // Você trata a exceção no RUN() de "Produtor" e "Consumidor" com "catch"
    public void produzir(int item) throws InterruptedException {

        // Tem espaços disponiveis?
            // Se sim, pode usar! : -1 ao contador do semáforo espacosDisponiveis
            // Senão, bloqueia, vai ter que esperar ! (Buffer está cheio, = 0)
        espacosDisponiveis.acquire();
        // Acesso exclusivo á Thread que for usar o recurso
        cadeado.lock(); // Começo da Região crítica

        // Try-finally = é obrigatório com o ReentrantLock para evitar o: o Deadlock = usando o unlock em finally
        try {
            // Produzindo um item
            buffer.add(item); // buffer

            // Gravação do Log de Produção
                // O metodo availablePermits() retorna o número de permissões restantes!
            String logProducao = String.format(
                    "Produtor - Inserido um item no buffer - espaços disponíveis: %d ",espacosDisponiveis.availablePermits()
            );
            logWriter.println(logProducao);

        } finally {
            // O código dentro do finally sempre será executado
            // Se a thread saísse do try devido a uma exceção sem liberar o bloqueio, o Mutex ficaria eternamente bloqueado = deadlock
            cadeado.unlock();  // Fim da Região crítica
        }

        // Avisa ao consumidor que tem um item disponível. + 1 ao contador do semáforo espacosOcupados
        espacosOcupados.release();
    }



    public void consumir() throws InterruptedException {


        // Tem item?
            // Se sim, pode consumir! : -1 ao contador do semáforo espacosOcupados
            // Senão, bloqueia, vai ter que esperar ! (Buffer está vazio, = 0)
        espacosOcupados.acquire();
        cadeado.lock();

        try {
            // Seguindo a regra FIFO, o primeiro a entrar vai ser o primeiro a sair
            buffer.remove(0); // buffer



            int espacosDisponiveisAtual = 7 - buffer.size();

            // Gravação do Log de Consumo
            String logConsumo = String.format(
                    "Consumidor - Consumido um item no buffer -  espaços disponíveis: %d",espacosDisponiveisAtual
            );
            logWriter.println(logConsumo);
        } finally {
            cadeado.unlock();
        }

        //  Avisao ao produtor que tem um espaço livre. +1 ao contador do semáforo espacosDisponiveis
        espacosDisponiveis.release();

    }


}
