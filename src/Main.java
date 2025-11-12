//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
                                            // Lançada se a Thread for interrompida enquanto o main espera com join()
    public static void main(String[] args) throws InterruptedException {

        // 1. Cria o recurso compartilhado
        Buffer buffer = new Buffer();

        // 2. Cria as Threads
        Produtor produtor = new Produtor(buffer);
        Consumidor consumidor = new Consumidor(buffer);

        // 3. INICIA AS THREADS
            // Ao usar o "start" = a JVM criará duas novas Threads, e o código do run() de cada uma será executado simultaneamente
            // JVM = Máquina Virtual Java = responsável pelo Gerenciamento de Threads
        produtor.start();
        consumidor.start();

        // 4. JOIN = ESPERA AS THREADS TERMINAREM antes de fechar o LOG
            // Main só irá para o próximo comando " buffer.fecharLog();" quando as Threads terminarem suas tarefas
        produtor.join();
        consumidor.join();

        buffer.fecharLog();
    }
}