import com.jsonscraper.uptimescout.Client;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** One request per invocation; --help needs no key and makes no request. */
public class Quickstart {
    public static void main(String[] args) {
        if (args.length == 0 || java.util.Arrays.asList(args).contains("--help")) {
            System.out.println("Usage: java -cp build Quickstart --method status --region GB --lang en\nMethods: services, status, statuses, summary, history, stats, incidents\nOptions: --service github --services github,cloudflare --hours 24 --limit 50 --offset 0 --q github");
            return;
        }
        try {
            var options = new HashMap<String, String>();
            for (int i = 0; i < args.length; i += 2) {
                if (!args[i].startsWith("--") || i + 1 >= args.length) throw new IllegalArgumentException("Expected --option value");
                options.put(args[i].substring(2), args[i + 1]);
            }
            String method = options.remove("method");
            if (!Set.of("services","status","statuses","summary","history","stats","incidents").contains(method)) throw new IllegalArgumentException("Choose a valid --method");
            var parameters = new HashMap<String, String>();
            parameters.put("region", "GB"); parameters.put("lang", "en");
            switch (method) {
                case "status" -> parameters.put("service", "github");
                case "services" -> { parameters.put("q", "github"); parameters.put("limit", "10"); }
                case "statuses", "summary" -> parameters.put("services", "github,cloudflare");
                case "history", "stats" -> { parameters.put("service", "github"); parameters.put("hours", "24"); }
                case "incidents" -> parameters.put("limit", "10");
            }
            if (method.equals("history")) { parameters.put("limit", "50"); parameters.put("offset", "0"); }
            parameters.putAll(options);
            String base = System.getenv().getOrDefault("UPTIMESCOUT_BASE_URL", "https://api.uptimescout.com");
            String json = new Client(System.getenv("UPTIMESCOUT_LICENSE_KEY"), base).request(method, parameters);
            System.out.println(json);
        } catch (Exception error) {
            System.err.println("UptimeScout request failed; check key, method, parameters, plan and network.");
            System.exit(1);
        }
    }
}
