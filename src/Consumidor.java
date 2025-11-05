public class Consumidor extends Thread {
    private Buffer buffer;

    public Consumidor(Buffer buffer){
        this.buffer = buffer;
    }

    // está sobrescrevendo o metodo run() herdado da classe Thread. Ao fazer isso:
    // Avisa ao JAVA  que essa lógica é trabalho independente que deve ser executado em paralelo com o resto do programa.
    @Override
    public void run(){

        for (int i=0;i < 12;i++){
            try{
                buffer.consumir();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

        }


    }
}
