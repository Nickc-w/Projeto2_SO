public class Produtor extends Thread {
    private Buffer buffer;

    public Produtor(Buffer buffer){
        this.buffer = buffer;
    }


    @Override
    public void run(){

        for (int i=0;i < 15;i++){
            try{
                buffer.produzir(i);

                // Pegando e tratando o  InterruptedException se o processo for interrmpido
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Sinaliza a interrupção para outras partes do sistema.
                return; // sai do metodo run() . Encerra o trabalho da Thread
            }

        }


    }
}
