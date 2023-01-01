import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class WhatsAppBot {

    private static String myPhoneNumber = System.getenv("MY_PHONE_NUMBER");
    private static String botPhoneNumber = System.getenv("WHATS_APP_PHONE_NUMBER");
    private static String botToken = System.getenv("WHATS_APP_TOKEN");
    private static String botSid = System.getenv("WHATS_APP_SID");

    private WhatsAppBot() {
    }

    public static void sendMessage(String message) {
        Message.creator(new PhoneNumber("whatsapp:" + myPhoneNumber),
                new PhoneNumber("whatsapp:" + botPhoneNumber), message).create();
    }

    public static void init() {
        Twilio.init(botSid, botToken);
    }
}
