
public class Consumidor extends Thread {
    private final Buffer buffer;

    public Consumidor(Buffer buffer){
        this.buffer = buffer;
    } // Construtor

    // Está sobrescrevendo o metodo run() herdado da classe Thread. Ao fazer isso:
    // Avisa ao JAVA que é um trabalho independente que deve ser executado em paralelo com o resto do programa = Thread
    @Override
    public void run(){

        // Consumo de até 12 itens
        for (int i=0;i < 12;i++){
            try{
                buffer.consumir(); // Consumindo o item
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

        }


    }
}
