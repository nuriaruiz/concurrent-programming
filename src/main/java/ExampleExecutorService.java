import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.*;

public class ExampleExecutorService {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://www.google.com")).build();
        Future<String> future = executorService.submit(() -> httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body());
        try {
            System.out.println(future.get()); // at some point in the future
        } catch (ExecutionException ex) { return; }
    }


}
