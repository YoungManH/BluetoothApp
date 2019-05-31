package com.example.bluetoothapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private String GOOSE_UUID = "0000fdcd-0000-1000-8000-00805f9b34fb";
    public static String GOOSE_UUID1 = "0000fff9-0000-1000-8000-00805f9b34fb";

    public static String GOOSE_SERVICE_UUID = "22210000-554a-4546-5542-46534450464d";
    public static String BASE_WRITE_CHARACTERIC_UUID = "00000001-0000-1000-8000-00805f9b34fb";
    public static String BASE_NOTIFY_CHARACTERIC_UUID = "00000002-0000-1000-8000-00805f9b34fb";
    public static String GOOSE_WRITE_CHARACTERIC_UUID = "00000003-0000-1000-8000-00805f9b34fb";
    public static String GOOSE_NOTIFY_CHARACTERIC_UUID = "00000004-0000-1000-8000-00805f9b34fb";
    public static String TH_DATA_CHARACTERIC_UUID = "00000100-0000-1000-8000-00805f9b34fb";
    public static String DFU_SERVICE_UUID = "0000fe59-0000-1000-8000-00805f9b34fb";
    public static String DFU_CHARACTERIC_UUID = "8ec90003-f315-4f60-9fb8-838830daea50";
    public static String INFORMATION_SERVICE_UUID = "0000180a-0000-1000-8000-00805f9b34fb";
    public static String INFORMATION_CHARACTERIC_UUID = "00002a26-0000-1000-8000-00805f9b34fb";
    private int x = 0;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager = null;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattService mGooseService, mDfuService, mInforService;
    private BluetoothGattCharacteristic mBaseWriteChar, mBaseNotiChar, mGooseWriteChar, mGooseNotiChar, mDfuChar, mDataNotiChar, mInforChar;

    private ListView mListview = null;
    private List<Card> mCards;
    private DeviceListAdapter mDeviceListAdapter;

    //Update UI
    private Handler mHanderUI = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle dataBundle = msg.getData();
            if (dataBundle == null) {
                return;
            }
//                temp_txt.setText("温度为：" + String.valueOf(dataBundle.getInt("tmp") / 10.0));
//                hum_txt.setText("湿度为：" + String.valueOf(dataBundle.getInt("hum") / 10.0));
//            }
            mCards.get(0).setmHumi(String.valueOf(dataBundle.getInt("hum") / 10.0));
            mCards.get(0).setmTemp(String.valueOf(dataBundle.getInt("tmp") / 10.0));
            mDeviceListAdapter.setmAllDevices(mCards);
            mDeviceListAdapter.notifyDataSetChanged();

        }
    };

    private boolean isBind = false;
    private BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {

            super.onPhyUpdate(gatt, txPhy, rxPhy, status);
        }

        @Override
        public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyRead(gatt, txPhy, rxPhy, status);
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mBluetoothGatt.discoverServices();
                GooseTip.instance.finish();
//                BluetoothLeScanner scanner = bluetoothAdapter.getBluetoothLeScanner();
//                if (scanner != null) {
//                    scanner.stopScan(mLeScanCallback);
//                }


//                mBluetoothAdapter.cancelDiscovery();
                Log.i("BluetoothGattCallback", "onConnected");

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                gatt.close();
                Log.i("BluetoothGattCallback", "DisConnected: " + status);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.i("BluetoothGattCallback", "onServicesDiscovered: " + status);

            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                mGooseService = mBluetoothGatt.getService(UUID.fromString(GOOSE_SERVICE_UUID));
                mDfuService = mBluetoothGatt.getService(UUID.fromString(DFU_SERVICE_UUID));
                mInforService = mBluetoothGatt.getService(UUID.fromString(INFORMATION_SERVICE_UUID));

                if (mDfuService != null) {
                    mDfuChar = mDfuService.getCharacteristic(UUID.fromString(DFU_CHARACTERIC_UUID));
//                    mDfuChar.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                }

                if (mInforService != null) {
                    mInforChar = mInforService.getCharacteristic(UUID.fromString(INFORMATION_CHARACTERIC_UUID));
                }

                if (mGooseService != null) {

                    mBaseWriteChar = mGooseService.getCharacteristic(UUID.fromString(BASE_WRITE_CHARACTERIC_UUID));
                    mBaseNotiChar = mGooseService.getCharacteristic(UUID.fromString(BASE_NOTIFY_CHARACTERIC_UUID));
                    mGooseWriteChar = mGooseService.getCharacteristic(UUID.fromString(GOOSE_WRITE_CHARACTERIC_UUID));
                    mGooseNotiChar = mGooseService.getCharacteristic(UUID.fromString(GOOSE_NOTIFY_CHARACTERIC_UUID));
                    mDataNotiChar = mGooseService.getCharacteristic(UUID.fromString(TH_DATA_CHARACTERIC_UUID));

                    mBaseWriteChar.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                    mGooseWriteChar.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);

                    BluetoothGattDescriptor descriptor = mBaseNotiChar.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    mBluetoothGatt.writeDescriptor(descriptor);
                    mBluetoothGatt.setCharacteristicNotification(mBaseNotiChar, true);
                }
            } else {
                Log.i("BluetoothGattCallback", "SvcDiscoveredFailed: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.i("BluetoothGattCallback", "onCharacteristicRead: " + status);

        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.i("BluetoothGattCallback", "onCharacteristicWrite: " + status);

        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            String value = null;
            value = PublicMethod.bytesToHexString(characteristic.getValue());
            Log.i("BluetoothGattCallback", "onCharacteristicChanged: value:" + value);
            if (!isBind) {
                byte[] bytes = PublicMethod.hexStringToBytes("1101000102030405060708090A0B0C0D0E0F");
                mGooseWriteChar.setValue(bytes);
                mBluetoothGatt.writeCharacteristic(mGooseWriteChar);
            }
            String hexTemp1 = value.substring(4, 4 + 2); //goose
            String hexTemp2 = value.substring(4 + 3, 4 + 4);
            String hexHumi1 = value.substring(4 + 4, 4 + 6);
            String hexHumi2 = value.substring(4 + 6, 4 + 8);

            final int tempNum = PublicMethod.hexStringToInt(hexTemp2 + hexTemp1);
            final int humiNum = PublicMethod.hexStringToInt(hexHumi2 + hexHumi1);

            Message msg = new Message();
            Bundle dataBundle = new Bundle();
            dataBundle.putInt("tmp", tempNum);
            dataBundle.putInt("hum", humiNum);
            msg.setData(dataBundle);
            mHanderUI.sendMessage(msg);

            Log.e("BluetoothGattCallback", "tmp:" + tempNum + ", hum:" + humiNum);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            Log.i("BluetoothGattCallback", "onDescriptorRead: " + status);

        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.i("BluetoothGattCallback", "onDescriptorWrite,status: " + status + "uuid:" + descriptor.getUuid() + ",x:" + x);
            if (status == 0 && x == 0) {
                BluetoothGattDescriptor descriptor1 = mGooseNotiChar.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
                descriptor1.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mBluetoothGatt.writeDescriptor(descriptor1);
                mBluetoothGatt.setCharacteristicNotification(mGooseNotiChar, true);
                x = x + 1;
            } else if (status == 0 && x == 1) {
                for (int i = 0; i < mDfuChar.getDescriptors().size(); i++) {
                    BluetoothGattDescriptor descriptor2 = mDfuChar.getDescriptors().get(i);
                    descriptor2.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                    descriptor2.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    mBluetoothGatt.writeDescriptor(descriptor2);
                }
                mBluetoothGatt.setCharacteristicNotification(mDfuChar, true);
                x = x + 1;
            } else if (status == 0 && x == 2) {
                for (int i = 0; i < mDataNotiChar.getDescriptors().size(); i++) {
                    BluetoothGattDescriptor descriptor2 = mDataNotiChar.getDescriptors().get(i);
                    descriptor2.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    mBluetoothGatt.writeDescriptor(descriptor2);
                    mBluetoothGatt.setCharacteristicNotification(mDataNotiChar, true);
                }
                x = x + 1;
            }

        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
            Log.i("BluetoothGattCallback", "onReliableWriteCompleted: " + status);

        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            Log.i("BluetoothGattCallback", "onReadRemoteRssi: " + status);

        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            Log.i("BluetoothGattCallback", "onMtuChanged: " + status);

        }
    };

    BroadcastReceiverTest broadcastReceiverTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

//        broadcastReceiverTest = new BroadcastReceiverTest();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        registerReceiver(broadcastReceiverTest, intentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.title_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_ble:
                BluetoothLeScanner scanner = bluetoothAdapter.getBluetoothLeScanner();
                if (scanner != null) {
                    scanner.startScan(mLeScanCallback);

                    Intent intent = new Intent(MainActivity.this, GooseTip.class);
                    startActivity(intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        mListview = (ListView) findViewById(R.id.device_list);
        mDeviceListAdapter = new DeviceListAdapter(this);
        mCards = new ArrayList<>();
        mCards.add(new Card("青萍温湿度计", "--", "--", "--"));
        mDeviceListAdapter.setmAllDevices(mCards);
        mListview.setAdapter(mDeviceListAdapter);
        checkPermission();
        light_test(5);
        light_test(12);
        light_test(105);
        light_test(205);

    }

    public class QPoint {
        public int x;
        public int y;

        public QPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int x() {
            return this.x;
        }

        public int y() {
            return this.y;
        }
    }

    public void light_test(int outLight) {
        double light = 5;


        // 一次函数解析式斜率/截距
        double a, b;

        // x为光线传感器值,y为背光
        QPoint p1, p2;
        if (outLight >= 2 && outLight <= 10) {
            p1 = new QPoint(2, 6);
            p2 = new QPoint(10, 60);
        } else if (outLight > 10 && outLight <= 80) {
            p1 = new QPoint(10, 60);
            p2 = new QPoint(100, 120);
        } else if (outLight > 80 && outLight <= 300) {
            p1 = new QPoint(101, 120);
            p2 = new QPoint(300, 180);
        } else {
            p1 = new QPoint(300, 180);
            p2 = new QPoint(400, 204);
        }
        // 计算函数斜率，截距
        a = (p1.y() - p2.y()) * 1.0 / (p1.x() - p2.x());
        b = p1.y() - a * 1.0 * p1.x();
        Log.e("light_test_", "--------------------------------------------------------------------");
        Log.e("light_test_outlight:",String.valueOf(outLight));
        Log.e("light_test_a:",String.valueOf(a));
        Log.e("light_test_b:",String.valueOf(b));
        Log.e("light_test_", "--------------------------------------------------------------------");


        light = a * outLight + b;
    }

    private ScanCallback mLeScanCallback = new ScanCallback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            String UUID = null;
            super.onScanResult(callbackType, result);
            if (result.getDevice().getType() == 2) {
                if (Objects.requireNonNull(result.getScanRecord()).getServiceData() != null) {
                    Set<ParcelUuid> set = Objects.requireNonNull(result.getScanRecord()).getServiceData().keySet();
                    if (set.size() > 0)
                        for (Object uuid : set.toArray()) {
                            UUID = uuid.toString();
                        }
                }
                if (UUID != null && (UUID.equalsIgnoreCase(GOOSE_UUID) || UUID.equalsIgnoreCase(GOOSE_UUID1))) {
                    if (UUID.equalsIgnoreCase(GOOSE_UUID)) {
                        UUID = "cdfd";
                    } else if (UUID.equalsIgnoreCase(GOOSE_UUID1)) {
                        UUID = "f9ff";
                    }
                    String scanResult = PublicMethod.bytesToHexString(result.getScanRecord().getBytes());
                    if (scanResult != null && scanResult.contains(UUID)) {
                        String FrameControl = scanResult.substring(scanResult.indexOf(UUID) + 4, scanResult.indexOf(UUID) + 6);
                        String frameStr = PublicMethod.getBit(PublicMethod.hexStringToBytes(FrameControl)[0]);
                        String mac = scanResult.substring(scanResult.indexOf(UUID) + 8, scanResult.indexOf(UUID) + 20);
                        String productId = scanResult.substring(scanResult.indexOf(UUID) + 6, scanResult.indexOf(UUID) + 8);
                        String address = scanResult.substring(scanResult.indexOf(UUID) + +8, scanResult.indexOf(UUID) + 20);
                        String aes = frameStr.substring(7, 8);
                        String bind = frameStr.substring(6, 7);
                        if (bind.equalsIgnoreCase("1")) {
                            Log.i("scanBLE:", "\n" + result.getDevice().getName() + "-->" + result.getDevice().getAddress()
                                    + "\nUUID:" + UUID + "\n" + "mac:" + mac + ", productid:" + productId + ",add:" + address + ",aes:" + aes
                                    + "bind:" + bind + "\n");
                            mBluetoothGatt = result.getDevice().connectGatt(getBaseContext(), false, mBluetoothGattCallback);
                        }
                    }
                }

            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }


}
