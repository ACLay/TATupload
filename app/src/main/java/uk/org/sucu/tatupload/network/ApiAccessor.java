package uk.org.sucu.tatupload.network;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.script.Script;
import com.google.api.services.script.model.ExecutionRequest;
import com.google.api.services.script.model.Operation;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import uk.org.sucu.tatupload.message.Text;
import uk.org.sucu.tatupload.parse.Parser;

public class ApiAccessor {

    private static final String scriptId = "Mc2ZRG3fFz5aQEmI6jh-2XVB4gM7Prbig";

    public static void createSheet(String sheetName) throws IOException, GoogleAuthException {

        Script mService = getAccessor();

        Object[] params = {sheetName};
        List<Object> paramList = Arrays.asList(params);

        // Create an execution request object.
        ExecutionRequest request = new ExecutionRequest()
                .setFunction("create")
                .setParameters(paramList);

        // Make the request.
        Operation op = mService.scripts().run(scriptId, request).execute();

        // Print results of request.
        if (op.getError() != null) {
            throw new IOException(getScriptError(op));
        }
        /*if (op.getResponse() != null && op.getResponse().get("result") != null) {
            // The result provided by the API needs to be cast into
            // the correct type, based upon what types the Apps Script
            // function returns.
        }*/

    }

    public static void uploadText(Text message) throws IOException, GoogleAuthException {

        String body = message.getBody();
        String number = message.getNumber();
        String time = Parser.timeStampToString(message.getTimestamp());

        String question = Parser.concatenateArrayList(Parser.getQuestion(body), "");
        String location = Parser.concatenateArrayList(Parser.getLocation(body), "");
        String toastie = Parser.concatenateArrayList(Parser.getFlavours(body), ", ");

        uploadText(number, question, location, toastie, body, time);
    }


    public static void uploadText(String number, String question, String location, String toastie, String body, String time) throws IOException, GoogleAuthException {

        Script mService = getAccessor();

        Object[] params = {number, question, location, toastie, body, time};
        List<Object> paramList = Arrays.asList(params);

        // Create an execution request object.
        ExecutionRequest request = new ExecutionRequest()
                .setFunction("upload")
                .setParameters(paramList);

        // Make the request.
        Operation op = mService.scripts().run(scriptId, request).execute();

        // Print results of request.
        if (op.getError() != null) {
            throw new IOException(getScriptError(op));
        }
        /*if (op.getResponse() != null && op.getResponse().get("result") != null) {
            // The result provided by the API needs to be cast into
            // the correct type, based upon what types the Apps Script
            // function returns.
        }*/
    }

    private static Script getAccessor(){
        GoogleAccountCredential credential = AuthManager.mCredential;
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        return new com.google.api.services.script.Script.Builder(
                transport, jsonFactory, NetManager.setHttpTimeout(credential))
                .setApplicationName("TATupload 2.0")
                .build();
    }

    /**
     * Interpret an error response returned by the API and return a String
     * summary.
     *
     * @param op the Operation returning an error response
     * @return summary of error response, or null if Operation returned no
     *     error
     */
    private static String getScriptError(Operation op) {
        if (op.getError() == null) {
            return null;
        }

        // Extract the first (and only) set of error details and cast as a Map.
        // The values of this map are the script's 'errorMessage' and
        // 'errorType', and an array of stack trace elements (which also need to
        // be cast as Maps).
        Map<String, Object> detail = op.getError().getDetails().get(0);
        List<Map<String, Object>> stacktrace =
                (List<Map<String, Object>>)detail.get("scriptStackTraceElements");

        java.lang.StringBuilder sb =
                new StringBuilder("\nScript error message: ");
        sb.append(detail.get("errorMessage"));

        if (stacktrace != null) {
            // There may not be a stacktrace if the script didn't start
            // executing.
            sb.append("\nScript error stacktrace:");
            for (Map<String, Object> elem : stacktrace) {
                sb.append("\n  ");
                sb.append(elem.get("function"));
                sb.append(":");
                sb.append(elem.get("lineNumber"));
            }
        }
        sb.append("\n");
        return sb.toString();
    }
}
