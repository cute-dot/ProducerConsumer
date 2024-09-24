import java.util.LinkedList;
import java.util.Queue;

public class Main {

    public static void main(String[] args) {
        ProducerConsumer producerСonsumer = new ProducerConsumer();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {

                producerСonsumer.produce();
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {

                producerСonsumer.consume();
            }
        });

        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}

class ProducerConsumer{
    private Queue<Integer> queue = new LinkedList<>();
    private final int LIMIT = 10;
    private final Object lock = new Object();
    public void produce(){
        int value = 0;
        while(true){
            synchronized (lock){
                while (queue.size() == LIMIT){
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                queue.offer(value++);
                lock.notify();
            }
        }
    }
    public void consume() {
        while (true){
            synchronized (lock){
                while (queue.isEmpty()){
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                int value = queue.poll();
                System.out.println(queue.size());
                lock.notify();
                System.out.println(value);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}