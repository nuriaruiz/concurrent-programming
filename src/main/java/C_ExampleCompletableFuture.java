import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class C_ExampleCompletableFuture {
    record ImportSave(String server, Integer amount) {}
    public static void main(String[] args) throws InterruptedException {
        run();
    }

    public static void run(){
        Supplier<ImportSave> fetchImportSaveA = () -> {
            return doImportA();
        };
        Supplier<ImportSave> fetchImportSaveB = () -> {
            return doImportB();
        };

        List<Supplier<ImportSave>> importSaveTasks = List.of(fetchImportSaveA, fetchImportSaveB);
        List<CompletableFuture<ImportSave>> futures = new ArrayList<>();
        Instant begin = Instant.now();
        for (Supplier<ImportSave> task : importSaveTasks) {
            CompletableFuture<ImportSave> future = CompletableFuture.supplyAsync(task);
            futures.add(future);
        }
        List<ImportSave> importSaves = new ArrayList<>();
        for (CompletableFuture<ImportSave> future : futures) {
            ImportSave importSave = future.join();
            importSaves.add(importSave);
        }
        ImportSave bestImportSave = importSaves.stream().min(Comparator.comparing(ImportSave::amount)).get();
        Instant end = Instant.now();
        Duration duration = Duration.between(begin, end);
        System.out.println("Best quotation [ES   ] = " + bestImportSave + " in " + duration.toMillis() + " ms");
    }

    private static ImportSave doImportA() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ImportSave("Server A", 100);
    }

    private static ImportSave doImportB() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ImportSave("Server B", 50);
    }


}
