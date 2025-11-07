//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {

        // 1. Cria o recurso compartilhado
        Buffer buffer = new Buffer();

        // 2. Cria os objetos Thread
        Produtor produtor = new Produtor(buffer);
        Consumidor consumidor = new Consumidor(buffer);

        // 3. INICIA AS THREAD
        // Ao usar start = a JVM criará duas novas Threads, e o código do run() de cada uma será executado simultaneamente
        // JVM = Máquina Virtual Java = responsável pelo Gerenciamento de Threads
        produtor.start();
        consumidor.start();

        // 4. ESPERA AS THREADS TERMINAREM
        // Servem para fechar o log DEPOIS da execeução das Threads (sem isso, o arquivo log fechará antes)
        produtor.join();
        consumidor.join();

        buffer.fecharLog();
    }
}