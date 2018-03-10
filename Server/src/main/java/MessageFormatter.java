import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class MessageFormatter {

    private final String receivedMessage;
    private final String clientName;

    public MessageFormatter(String receivedMessage, String clientName) {
        this.receivedMessage = receivedMessage;
        this.clientName = clientName;
    }

    public String getMessage() {
        Instant now = Instant.now();
        ZoneId zoneId = ZoneId.of("Europe/Warsaw");
        ZonedDateTime dateAndTimeInKrakow = ZonedDateTime.ofInstant(now, zoneId);

        return String.format("[%d:%d:%d]\t%s>%s", dateAndTimeInKrakow.getHour(),
                dateAndTimeInKrakow.getMinute(), dateAndTimeInKrakow.getSecond(), clientName,
                receivedMessage);
    }
}
