import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorServiceTask {
    private static final int THREAD_COUNT = 10; // Количество потоков
    private static final int ITERATION_COUNT = 100_000; // Количество итераций
    private static final int WAIT_MILLIS = 10;

    // Эти счетчики потокобезопасны и могут использоваться в многопоточной среде.
    private static final AtomicInteger counter1 = new AtomicInteger(0);
    private static final AtomicInteger counter2 = new AtomicInteger(0);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        Long startTime = System.currentTimeMillis();

        // Запуск задач на увеличение счетчиков заданное число раз (ITERATION_COUNT).
        for (int i = 0; i < ITERATION_COUNT; i++) {
            executorService.submit(ExecutorServiceTask::increaseCounter);
        }

        // Инициирование процесса остановки ExecutorService после выполнения всех задач.
        executorService.shutdown();

        // Цикл ожидания, пока все задачи не будут завершены.
        while (!executorService.isTerminated()) {
            try {
                System.out.println("Waiting completion: " + WAIT_MILLIS);
                Thread.sleep(WAIT_MILLIS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Long endTime = System.currentTimeMillis();
        System.out.println("Duration milliseconds: " + (endTime - startTime));

        // Вывод финальных значений счетчиков.
        System.out.println("Counter 1: " + counter1.get());
        System.out.println("Counter 2: " + counter2.get());
    }

    /**
     *  Синхронизированный метод для увеличения счетчиков.
     */
    private static synchronized void increaseCounter() {
        counter1.incrementAndGet();
        counter2.incrementAndGet();
    }
}
