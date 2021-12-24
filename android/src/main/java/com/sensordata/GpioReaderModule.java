package com.sensordata;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.ReadableArray;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.android.wlg.WlgHelper;
import com.android.wlg.WlgHelper.UpdateReturnCallback;
import android.content.Intent;
public class GpioReaderModule extends ReactContextBaseJavaModule {

    private WlgHelper mWlgHelper = new WlgHelper();
    private float vref = 3.3f;
    private boolean isDebug = false;
    private ShellUtils shellUtils = null;
    private boolean isFirst = true;

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ReactApplicationContext reactContext;


    GpioReaderModule(ReactApplicationContext context) {
        super(context);
        this.reactContext = context;
        shellUtils = new ShellUtils();
    }

    @Override
    public String getName() {
        return "ModuleGPIO";
    }

    private String getGpioState(String gpioName) {
        String value = null;
        File file = new File("/sys/class/gpio/" + gpioName + "/value");

        try {
            InputStream instream = new FileInputStream(file);
            if (instream != null) {
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                value = buffreader.readLine();
                instream.close();
            }
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return value;
    }


    @ReactMethod
    private void gpioReadStateOne(String gpioName, Promise promise) {
        String value = null;
        String errorMsg = null;

        if (!isDebug) {
            try {
                value = getGpioState(gpioName);
                promise.resolve(value);
            } catch (Exception ex) {
                errorMsg = "Error gpioReadStateOne";
                ex.printStackTrace();
                promise.reject("Error gpioReadStateOne", errorMsg);

            }
        }
        promise.resolve(value);
    }


    /**
     * Retorna un array con los valores de cada uno de los gpio
     */
    @ReactMethod
    private void gpioReadStateAll(Promise promise) {
        try {
            Future<WritableMap> future = gpioTimedRead();
            try {
                WritableMap values = future.get(1500, TimeUnit.MILLISECONDS);
                promise.resolve(values);
            } catch (TimeoutException e) {
                future.cancel(true);

                WritableMap values = new WritableNativeMap();
                values.putString("Di1", "0");
                values.putString("Di2", "0");
                values.putString("Di3", "0");
                values.putString("Di4", "0");
                values.putString("Ai1", "0");
                values.putString("Ai2", "0");
                values.putString("Ai3", "0");
                values.putString("Ai4", "0");
                values.putString("Engine", "-1");
                values.putString("Pls1", "0");
                values.putString("Pls2", "0");
                promise.resolve(values);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            promise.reject("ERROR gpioReadStateAll", ex);
        }
    }


    /**
     * Retorna un array con los valores de cada uno de los gpio sin resetear los counters
     */
    @ReactMethod
    private void gpioReadStateAllNoReset(Promise promise) {
        try {
            Future<WritableMap> future = gpioTimedReadNoReset();
            try {
                WritableMap values = future.get(1500, TimeUnit.MILLISECONDS);
                promise.resolve(values);
            } catch (TimeoutException e) {
                future.cancel(true);

                WritableMap values = new WritableNativeMap();
                values.putString("Di1", "0");
                values.putString("Di2", "0");
                values.putString("Di3", "0");
                values.putString("Di4", "0");
                values.putString("Ai1", "0");
                values.putString("Ai2", "0");
                values.putString("Ai3", "0");
                values.putString("Ai4", "0");
                values.putString("Engine", "-1");
                values.putString("Pls1", "0");
                values.putString("Pls2", "0");
                promise.resolve(values);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            promise.reject("ERROR gpioReadStateAllNoReset", ex);
        }
    }


    /**
     * Retorna un array con los valores de cada uno de los gpio
     * gpioList - array con los nombres de los gpio a consultar los valores
     * ["gpio22","gpio23","gpio33","gpio66","gpio24", ...]
     */
    @ReactMethod
    private void gpioReadStateAllOld(ReadableArray gpioList, Promise promise) {
        try {
            WritableMap values = new WritableNativeMap();

            if (!isDebug && gpioList != null) {
                for (int i = 0; i < 5; i++) {
                    values.putString(gpioList.getString(i), getGpioState(gpioList.getString(i)));
                }
            }
            promise.resolve(values);
        } catch (Exception ex) {
            ex.printStackTrace();
            promise.reject("ERROR gpioReadStateAll", ex);
        }
    }


    private Future<WritableMap> gpioTimedRead() {
        return executor.submit(() -> {
            WritableMap values = new WritableNativeMap();

            // Lectura de las señales digitales
            values.putString("Di1", String.valueOf(mWlgHelper.getGpioValue(1)));
            values.putString("Di2", String.valueOf(mWlgHelper.getGpioValue(2)));
            values.putString("Di3", String.valueOf(mWlgHelper.getGpioValue(3)));
            values.putString("Di4", String.valueOf(mWlgHelper.getGpioValue(4)));

            // Lectura de las señales analogicas
            values.putString("Ai1", String.valueOf(mWlgHelper.getAdcValue(0, vref)));
            values.putString("Ai2", String.valueOf(mWlgHelper.getAdcValue(1, vref)));
            values.putString("Ai3", String.valueOf(mWlgHelper.getAdcValue(2, vref)));
            values.putString("Ai4", String.valueOf(mWlgHelper.getAdcValue(3, vref)));

            // Lectura de motor
            values.putString("Engine", String.valueOf(getGpioState("gpio24")));

            // Por primera vez lectura
            if (isFirst) {
                values.putString("Pls1", "0");
                values.putString("Pls2", "0");
                mWlgHelper.setPulseStartAndReset(0);
                mWlgHelper.setPulseStartAndReset(1);
                isFirst = false;
            } else {
                // Lectura y resteo del caudalimetro 1
                values.putString("Pls1", String.valueOf(mWlgHelper.setPulseCountStop(0)));
                mWlgHelper.setPulseStartAndReset(0);

                // Lectura y reseteo del caudalimetro 2
                values.putString("Pls2", String.valueOf(mWlgHelper.setPulseCountStop(1)));
                mWlgHelper.setPulseStartAndReset(1);
            }

            return values;
        });
    }

    private Future<WritableMap> gpioTimedReadNoReset() {
        return executor.submit(() -> {

            WritableMap values = new WritableNativeMap();

            // Lectura de las señales digitales
            values.putString("Di1", String.valueOf(mWlgHelper.getGpioValue(1)));
            values.putString("Di2", String.valueOf(mWlgHelper.getGpioValue(2)));
            values.putString("Di3", String.valueOf(mWlgHelper.getGpioValue(3)));
            values.putString("Di4", String.valueOf(mWlgHelper.getGpioValue(4)));

            // Lectura de las señales analogicas
            values.putString("Ai1", String.valueOf(mWlgHelper.getAdcValue(0, vref)));
            values.putString("Ai2", String.valueOf(mWlgHelper.getAdcValue(1, vref)));
            values.putString("Ai3", String.valueOf(mWlgHelper.getAdcValue(2, vref)));
            values.putString("Ai4", String.valueOf(mWlgHelper.getAdcValue(3, vref)));

            // Lectura de motor
            values.putString("Engine", String.valueOf(getGpioState("gpio24")));

            // Por primera vez lectura
            if (isFirst) {
                values.putString("Pls1", "0");
                values.putString("Pls2", "0");
                mWlgHelper.setPulseStartAndReset(0);
                mWlgHelper.setPulseStartAndReset(1);
                isFirst = false;
            } else {
                // Lectura del caudalimetro 1
                values.putString("Pls1", String.valueOf(mWlgHelper.getPulseCountNum(0)));

                // Lectura del caudalimetro 2
                values.putString("Pls2", String.valueOf(mWlgHelper.getPulseCountNum(1)));
            }
            return values;
        });
    }


    @ReactMethod
    public void gpioSetDirectionOne(String name, String dir, Promise promise) {
        if (!isDebug) {
            String cmd = "echo " + dir + " > /sys/class/gpio/" + name + "/direction\n";
            ShellUtils.CommandResult commandResult = shellUtils.execCommand(cmd, false);

            if (commandResult.result == 0) {
                promise.resolve(true);
            } 
            
            promise.reject("ERROR set state failed!", name);
        }

        promise.reject("Está en modo DEBUG", name);
    }


    @ReactMethod
    public void gpioSetStateOne(String name, int state, Promise promise) {
        if (!isDebug) {
            String cmd = "echo " + state + " > /sys/class/gpio/" + name + "/value\n";
            ShellUtils.CommandResult commandResult = shellUtils.execCommand(cmd, false);

            if (commandResult.result == 0) {
                promise.resolve(true);
            }
            promise.reject("ERROR set state failed!", name);
        }
        promise.reject("Está en modo DEBUG", name);
    }


    @ReactMethod
    public void setIsDebug(boolean debug) {
        isDebug = debug;
    }

    @ReactMethod
    private void gpioGetVersion(Promise promise) {
        String version = mWlgHelper.getVersion()
        WritableMap value = new WritableNativeMap();
        value.putString("version", version);
        
        promise.resolve(value);
    }

    @ReactMethod
    private void setKioscoMode(boolean activated, Promise promise) {
        String actionOn = "com.android.kiosk.ZYZ_CONTROL_KIOSK_ON";
        String actionOff = "com.android.kiosk.ZYZ_CONTROL_KIOSK_OFF";
        Intent intent = new Intent();

        if(activated) {
            intent.setAction(actionOn);
            this.reactContext.sendBroadcast(intent);
            promise.resolve(true);
        }
        
        intent.setAction(actionOff);
        this.reactContext.sendBroadcast(intent);
        promise.resolve(false);
    }

    /**
     * This function is an operation interface function. If you need to operate, you must initialize the interface first.
     * For details on how to use it, see the context in the demo.
     * Parameter analysis：
     * serial_baudrate：This parameter represents the serial port baud rate of the connection interface.
     * At present, the baud rates we support are 2400, 4800, 9600, 19200, 38400, 57600, 115200, 230400, 460800, 921600, and 100,000.
     * 0：Indicates that the setting returns successfully.
     * -1： Indicates that the interface is in the occupied state at this time. The reason may be that the function has already been called, or there may be a configuration interface function that is being called at this time.
     * -2：The serial_baudrate parameter is incorrectly filled, please refer to the serial port baud rate supported in the parameter analysis.
     * -3：Failed to open the serial port, please check whether there are other processes or threads occupying the serial port of the interface. If not, please call again.
     * -4：Failed to start the listening thread, please check whether the interface is occupied (uninitialize can be called to release).
     */
    @ReactMethod
    public void initialize(int serial_baudrate, Promise promise) {
        int ret = mWlgHelper.initialize(serial_baudrate);
        // Init failed
        if (ret < 0) {
            promise.reject("ERROR", "Init failed! - Code: " + ret);
        }
        
        promise.resolve(true);
    }

    /**
     * This function is an operation interface function, and the function is to close the interface, that is, release
     * the interface resource occupation. For specific use, please refer to the calling method in the demo.
     * Return analysis：
     * 0：Normal return.
     * -1：Close failed, please try again.
     */
    @ReactMethod
    public void uninitialize(Promise promise) {
        int ret = mWlgHelper.uninitialize();
        // Un-Init failed
        if (ret < 0) {
            promise.reject("ERROR", "Init failed! - Code: " + ret);
        }

        promise.resolve(true);
    }
}
