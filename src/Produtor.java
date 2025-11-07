public class Produtor extends Thread {
    private final Buffer buffer;

    public Produtor(Buffer buffer){
        this.buffer = buffer;
    } // Construtor


    @Override
    public void run(){

        // Produção de até 15 itens
        for (int i=0;i < 15;i++){
            try{
                buffer.produzir(i); // Produz um item

                // Pegando e tratando o  InterruptedException se o processo for interrmpido
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Sinaliza a interrupção para outras partes do sistema.
                return; // sai do metodo run() . Encerra o trabalho da Thread
            }

        }


    }
}
