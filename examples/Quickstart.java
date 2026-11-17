import com.jsonscraper.uptimescout.Client;import java.util.Map;
class Quickstart {public static void main(String[]args)throws Exception{System.out.println(new Client(System.getenv("UPTIMESCOUT_LICENSE_KEY")).request("status",Map.of("service","github")));}}
