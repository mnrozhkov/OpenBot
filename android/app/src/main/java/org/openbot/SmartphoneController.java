package org.openbot;

import android.app.Instrumentation;
import android.content.Context;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;

import androidx.annotation.NonNull;

import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static android.view.MotionEvent.obtain;
import static java.lang.Float.parseFloat;

public class SmartphoneController {

    private static final String TAG = "SmartphoneController";

    private static final NearbyConnection connection = new NearbyConnection();

    public void connect(Context context) {
        connection.connect(context, payloadCallback);
    }
    public void disconnect() {
        connection.disconnect();
    }

    // Callbacks for receiving payloads
    private final PayloadCallback payloadCallback =
            new PayloadCallback() {
                @Override
                public void onPayloadReceived(String endpointId, Payload payload) {
                    String command = new String(payload.asBytes(), StandardCharsets.UTF_8);
                    try {
                        JSONObject jsonCommand = new JSONObject(command);

                        String buttonValue = null;
                        if (jsonCommand.has("buttonValue")) {
                            buttonValue = jsonCommand.getString("buttonValue");
                        }

                        ControllerEventProcessor.ControllerEvent event = new ControllerEventProcessor.ControllerEvent();

                        if (buttonValue != null) {
                            switch (buttonValue) {
                                case "LOGS":
                                    event.type = ControllerEventProcessor.ControllerEventsTypes.LOGS;
                                    break;
                                case "NOISE":
                                    event.type = ControllerEventProcessor.ControllerEventsTypes.NOISE;
                                    break;
                                case "DRIVE_BY_NETWORK":
                                    event.type = ControllerEventProcessor.ControllerEventsTypes.DRIVE_MODE;
                                    break;
                                case "INDICATOR_RIGHT":
                                    event.type = ControllerEventProcessor.ControllerEventsTypes.INDICATOR_RIGHT;
                                    break;
                                case "INDICATOR_LEFT":
                                    event.type = ControllerEventProcessor.ControllerEventsTypes.INDICATOR_LEFT;
                                    break;
                                case "INDICATOR_STOP":
                                    event.type = ControllerEventProcessor.ControllerEventsTypes.INDICATOR_STOP;
                                    break;
                                default:
                                    Log.e(TAG, "Woooo, what are you doing? Will take away your keys!");
                                    return;
                            }
                        }

                        // {r:0.22, l:0.3}
                        String rightValue = null;
                        String leftValue = null;
                        if (jsonCommand.has("r") && jsonCommand.has("l")) {
                            leftValue = jsonCommand.getString("l");
                            rightValue = jsonCommand.getString("r");
                        }

                        if (rightValue != null && leftValue != null) {
                            event.type = ControllerEventProcessor.ControllerEventsTypes.DRIVE_LEFT_RIGHT_VALUES;
                            ControllerEventProcessor.DriveValue leftRightValues = new ControllerEventProcessor.DriveValue(new Float(leftValue), new Float(rightValue));
                            event.payload = leftRightValues;
                        }
                        ControllerEventProcessor.emitEvent(event);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onPayloadTransferUpdate(@NonNull String endpointId, @NonNull PayloadTransferUpdate update) {
                }
            };

    public boolean isConnected() {
        return connection.isConnected();
    }
}
