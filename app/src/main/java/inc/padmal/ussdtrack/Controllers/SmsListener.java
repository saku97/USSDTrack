package inc.padmal.ussdtrack.Controllers;

/**
 * Created by knight on 10/21/18.
 */

public interface SmsListener {
    void messageReceived(String messageText);
}
