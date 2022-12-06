import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

public class ExampleExecutorServiceWithCallable {

    public static void main(String[] args) throws Exception {
        record ImportSave(String server, Integer amount) {
        }
        Callable<ImportSave> fetchImportSaveA = () -> {
            Thread.sleep(100);
            return new ImportSave("Server A", 100);
        };
        Callable<ImportSave> fetchImportSaveB = () -> {
            Thread.sleep(100);
            return new ImportSave("Server B", 50);
        };

        // RUNNING THE CALLABLES TASK IN PARALLEL
        List<Callable<ImportSave>> importSaveTasks = List.of(fetchImportSaveA, fetchImportSaveB);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<Future<ImportSave>> futures = new ArrayList<>();
        Instant begin = Instant.now();
        for (Callable<ImportSave> task : importSaveTasks) {
            Future<ImportSave> future = executorService.submit(task);
            futures.add(future);
        }
        List<ImportSave> importSaves = new ArrayList<>();
        for (Future<ImportSave> future : futures) {
            ImportSave importSave = future.get();
            importSaves.add(importSave);
        }
        ImportSave bestImportSave = importSaves.stream().min(Comparator.comparing(ImportSave::amount)).get();
        System.out.println(bestImportSave);
        Instant end = Instant.now();
        Duration duration = Duration.between(begin, end);
        System.out.println("Best quotation [ES   ] = " + bestImportSave + " in " + duration.toMillis() + " ms");

        // RUNNING THE CALLABLES TASK NOT IN PARALLEL
        Instant begin2 = Instant.now();
        List<ImportSave> importSaves2 = new ArrayList<>();
        for (Callable<ImportSave> task : importSaveTasks) {
            ImportSave importSave = task.call();
            importSaves2.add(importSave);
        }
        ImportSave bestImportSave2 = importSaves2.stream().min(Comparator.comparing(ImportSave::amount)).get();
        System.out.println(bestImportSave2);
        Instant end2 = Instant.now();
        Duration duration2 = Duration.between(begin2, end2);
        System.out.println("Best quotation [ES   ] = " + bestImportSave2 + " in " + duration2.toMillis() + " ms");

    }


}
