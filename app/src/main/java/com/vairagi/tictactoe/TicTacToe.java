package com.vairagi.tictactoe;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import static com.vairagi.tictactoe.R.anim.sendmove;

public class TicTacToe extends AppCompatActivity {
    Toolbar toolbar;
    boolean game = true;
    int player = 1;
    boolean play;
    int p = 0;
    int available[] = {0,0,0,0,0,0,0,0,0};
    char fill[] = {'t','t','t','t','t','t','t','t','t'};
    Animation animation;
    TextView infotext;
    Button restart;

    SendReceive sendReceive;

    TextView tv;
    ListView devicelist;
    Button sendButton,receiveButton;

    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice[] bluetoothArray;

    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED = 4;
    static final int STATE_MESSAGE_RECEIVED = 5;

    private static final String App_Name = "BTChat";
    private static final UUID MY_UUID = UUID.fromString("8ce255c0-223a-11e0-ac64-0803450c9a66");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_connection);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        init1();
        onBluetooth();

        devicelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClientClass clientClass = new ClientClass(bluetoothArray[position]);
                clientClass.start();
                tv.setText("Connecting");
            }
        });
    }

    private void onBluetooth() {
        if(!bluetoothAdapter.isEnabled()){
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init2() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menus);
        if(player == 1){
            toolbar.setTitle("Player 1");
        }
        else if(player == 2){
            toolbar.setTitle("Player 2");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                sendReceive.write(new String("New Game").getBytes());
                return true;
                }
            });
        }
        restart = (Button) findViewById(R.id.restart);
        restart.setVisibility(View.INVISIBLE);
        infotext = (TextView) findViewById(R.id.infotext);
        infotext.setVisibility(View.INVISIBLE);
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.imageanimation);
    }

    private void init1() {
        tv = (TextView) findViewById(R.id.connectinfo);
        tv.setText("Please Connect Bluetooth In Your Mobile Manualy");
        devicelist = (ListView) findViewById(R.id.devicelist);
        sendButton = (Button) findViewById(R.id.send);
        receiveButton = (Button) findViewById(R.id.receive);
    }

    private void newGame() {
        for(int i= 0; i<available.length; i++){
            available[i] = 0;
        }
        for(int i = 0; i<fill.length; i++){
            fill[i] = 't';
        }
        ImageView i1 = (ImageView) findViewById(R.id.img1);
        ImageView i2 = (ImageView) findViewById(R.id.img2);
        ImageView i3 = (ImageView) findViewById(R.id.img3);
        ImageView i4 = (ImageView) findViewById(R.id.img4);
        ImageView i5 = (ImageView) findViewById(R.id.img5);
        ImageView i6 = (ImageView) findViewById(R.id.img6);
        ImageView i7 = (ImageView) findViewById(R.id.img7);
        ImageView i8 = (ImageView) findViewById(R.id.img8);
        ImageView i9 = (ImageView) findViewById(R.id.img9);
        i1.setImageDrawable(null);
        i2.setImageDrawable(null);
        i3.setImageDrawable(null);
        i4.setImageDrawable(null);
        i5.setImageDrawable(null);
        i6.setImageDrawable(null);
        i7.setImageDrawable(null);
        i8.setImageDrawable(null);
        i9.setImageDrawable(null);
        game = true;
        player = p;
        infotext.clearAnimation();
        restart.setVisibility(View.INVISIBLE);
        infotext.setVisibility(View.INVISIBLE);
    }

    public void setImage(View view) {

        ImageView img = (ImageView) view;
        if(available[Integer.parseInt(img.getTag().toString()) - 1] != 0){
            return;
        }
        if(game == false){
            return;
        }
        int tag = Integer.parseInt(img.getTag().toString()) - 1;

        //img.clearAnimation();
        if (player == 1 && play == true) {
            img.setImageResource(R.drawable.ic_zero);
            img.startAnimation(animation);
            //img.animate().rotation(360 * 2).setDuration(1000);
            available[tag] = player;
            fill[tag] = 'z';
            sendReceive.write(String.valueOf(img.getId()).getBytes());
            //player = 2;
            play = false;
        } else if(player == 2 && play == false){
            img.setImageResource(R.drawable.ic_close);
            img.startAnimation(animation);
            //img.animate().rotation(360 * 2).setDuration(1000);
            available[tag] = player;
            fill[tag] = 'x';
            sendReceive.write(String.valueOf(img.getId()).getBytes());
            //player = 1;
            play = true;
        }
        else {
            return;
        }
        check();
    }

    private void winn1(){
        if (player == 1) {
            infotext.setText("You Are Winn");
        }
        else {
            infotext.setText("Player 1 Winn");
        }
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom);
        infotext.startAnimation(anim);
        infotext.setVisibility(View.VISIBLE);
    }
    private void winn2(){
        if(player == 1) {
            infotext.setText("Player 2 Winn");
        }
        else {
            infotext.setText("You Are Winn");
        }
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom);
        infotext.startAnimation(anim);
        infotext.setVisibility(View.VISIBLE);
    }

    private void check() {
        if(fill[0] == fill[1] && fill[1] == fill[2] && fill[0] != 't'){
            if(fill[0] == 'z') {
                winn1();
            }
            else {
                winn2();
            }
            game = false;
            restart.setVisibility(View.VISIBLE);
            return;
        }
        if(fill[3] == fill[4] && fill[4] == fill[5] && fill[3] != 't'){
            if(fill[3] == 'z') {
                winn1();
            }
            else{
                winn2();
            }
            game = false;
            restart.setVisibility(View.VISIBLE);
            return;
        }
        if(fill[6] == fill[7] && fill[7] == fill[8] && fill[6] != 't'){
            if(fill[6] == 'z'){
                winn1();
            }
            else{
                winn2();
            }
            game = false;
            restart.setVisibility(View.VISIBLE);
            return;
        }
        if(fill[0] == fill[3] && fill[3] == fill[6] && fill[0] != 't'){
            if(fill[0] == 'z'){
                winn1();
            }
            else{
                winn2();
            }
            game = false;
            restart.setVisibility(View.VISIBLE);
            return;
        }
        if(fill[1] == fill[4] && fill[4] == fill[7] && fill[1] != 't'){
            if(fill[1] == 'z'){
                winn1();
            }
            else{
                winn2();
            }
            game = false;
            restart.setVisibility(View.VISIBLE);
            return;
        }
        if(fill[2] == fill[5] && fill[5] == fill[8] && fill[2] != 't'){
            if(fill[2] == 'z') {
                winn1();
            }
            else {
                winn2();
            }
            game = false;
            restart.setVisibility(View.VISIBLE);
            return;
        }
        if(fill[0] == fill[4] && fill[4] == fill[8] && fill[0] != 't'){
            if(fill[0] == 'z'){
                winn1();
            }
            else{
                winn2();
            }
            game = false;
            restart.setVisibility(View.VISIBLE);
            return;
        }
        if(fill[2] == fill[4] && fill[4] == fill[6] && fill[2] != 't'){
            if(fill[2] == 'z'){
                winn1();
            }
            else{
                winn2();
            }
            game = false;
            restart.setVisibility(View.VISIBLE);
            return;
        }
        if(available[0] != 0 && available[1] != 0 && available[2] != 0 && available[3] != 0 && available[4] != 0
                && available[5] != 0 && available[6] != 0 && available[7] != 0 && available[8] != 0)
        {
            restart.setVisibility(View.VISIBLE);
            infotext.setText("Game Over");
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom);
            infotext.startAnimation(anim);
            infotext.setVisibility(View.VISIBLE);
            return;
        }
    }

    public void reStart(View view) {
        restart.setVisibility(View.INVISIBLE);
        newGame();
    }

    public void receiveButton(View view) {
        if(bluetoothAdapter.isEnabled()) {
            animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.receivemove);
            sendButton.setVisibility(View.INVISIBLE);
//            receiveButton.startAnimation(animation);
//            receiveButton.setVisibility(View.INVISIBLE);

            player = 2;
            p = 2;
            play = false;
            setDeviceList();
        }
        else {
            onBluetooth();
        }
    }

    public void sendButton(View view) {
        if(bluetoothAdapter.isEnabled()) {
            animation = AnimationUtils.loadAnimation(getApplicationContext(), sendmove);
            receiveButton.setVisibility(View.INVISIBLE);
//            sendButton.startAnimation(animation);
//            sendButton.setVisibility(View.INVISIBLE);

            player = 1;
            p = 1;
            play = true;
            ServerClass serverClass = new ServerClass();
            serverClass.start();
        }
        else {
            onBluetooth();
        }
    }

    private void setDeviceList() {
        Set<BluetoothDevice> bt = bluetoothAdapter.getBondedDevices();
        String[] strings = new String[bt.size()];
        bluetoothArray = new BluetoothDevice[bt.size()];
        int index = 0;
        if(bt.size() > 0){
            for(BluetoothDevice device : bt){
                bluetoothArray[index] = device;
                strings[index] = device.getName();
                index++;
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strings);
            devicelist.setAdapter(arrayAdapter);
        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case STATE_LISTENING:
                    tv.setText("Listening");
                    break;
                case STATE_CONNECTING:
                    tv.setText("Connecting");
                    break;
                case STATE_CONNECTED:
                    tv.setText("Connected");
                    Toast.makeText(getApplicationContext(),"Device Connection Success",Toast.LENGTH_LONG).show();
                    handler.postDelayed(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void run() {
                            setContentView(R.layout.activity_tic_tac_toe);
                            init2();
                        }
                    }, 1500);
                    break;
                case STATE_CONNECTION_FAILED:
                    tv.setText("Connetion Failed");
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuff = (byte[]) msg.obj;
                    String tempMsg = new String(readBuff,0,msg.arg1);
                    if(tempMsg.toString().equals("New Game")){
                        newGameRequest();
                        break;
                    }
                    if(tempMsg.toString().equals("Game Yes")){
                        newGame();
                        Toast.makeText(getApplicationContext(),"New Game",Toast.LENGTH_LONG).show();
                        break;
                    }
                    int id = Integer.parseInt(tempMsg);
                    ImageView img = (ImageView) findViewById(id);
                    if(player == 1){
                        img.setImageResource(R.drawable.ic_close);
                        img.startAnimation(animation);
                        switch (id) {
                            case R.id.img1:
                                available[0] = 2;
                                fill[0] = 'x';
                                play = true;
                                break;
                            case R.id.img2:
                                available[1] = 2;
                                fill[1] = 'x';
                                play = true;
                                break;
                            case R.id.img3:
                                available[2] = 2;
                                fill[2] = 'x';
                                play = true;
                                break;
                            case R.id.img4:
                                available[3] = 2;
                                fill[3] = 'x';
                                play = true;
                                break;
                            case R.id.img5:
                                available[4] = 2;
                                fill[4] = 'x';
                                play = true;
                                break;
                            case R.id.img6:
                                available[5] = 2;
                                fill[5] = 'x';
                                play = true;
                                break;
                            case R.id.img7:
                                available[6] = 2;
                                fill[6] = 'x';
                                play = true;
                                break;
                            case R.id.img8:
                                available[7] = 2;
                                fill[7] = 'x';
                                play = true;
                                break;
                            case R.id.img9:
                                available[8] = 2;
                                fill[8] = 'x';
                                play = true;
                                break;
                        }
                        check();
                    }
                    else if(player == 2){
                        img.setImageResource(R.drawable.ic_zero);
                        img.startAnimation(animation);
                        switch (id) {
                            case R.id.img1:
                                available[0] = 1;
                                fill[0] = 'z';
                                play = false;
                                break;
                            case R.id.img2:
                                available[1] = 1;
                                fill[1] = 'z';
                                play = false;
                                break;
                            case R.id.img3:
                                available[2] = 1;
                                fill[2] = 'z';
                                play = false;
                                break;
                            case R.id.img4:
                                available[3] = 1;
                                fill[3] = 'z';
                                play = false;
                                break;
                            case R.id.img5:
                                available[4] = 1;
                                fill[4] = 'z';
                                play = false;
                                break;
                            case R.id.img6:
                                available[5] = 1;
                                fill[5] = 'z';
                                play = false;
                                break;
                            case R.id.img7:
                                available[6] = 1;
                                fill[6] = 'z';
                                play = false;
                                break;
                            case R.id.img8:
                                available[7] = 1;
                                fill[7] = 'z';
                                play = false;
                                break;
                            case R.id.img9:
                                available[8] = 1;
                                fill[8] = 'z';
                                play = false;
                                break;
                        }
                        check();
                    }
                    else {
                        break;
                    }
                    //Toast.makeText(getApplicationContext(),tempMsg,Toast.LENGTH_LONG).show();
                    break;
            }
            return true;
        }
    });

    private void newGameRequest() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(TicTacToe.this);
        if ((player == 1)) {
            builder.setTitle("Player 2 Want To New Game");
        } else {
            builder.setTitle("Player 1 Want To New Game");
        }
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendReceive.write(new String("Game Yes").getBytes());
                newGame();
                Toast.makeText(getApplicationContext(),"New Game",Toast.LENGTH_LONG).show();
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private class ServerClass extends  Thread{
        private BluetoothServerSocket serverSocket;
        public ServerClass(){
            try {
                serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(App_Name,MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void run(){
            BluetoothSocket socket = null;
            while (socket == null){
                try {
                    Message msg = Message.obtain();
                    msg.what = STATE_CONNECTING;
                    handler.sendMessage(msg);
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    Message msg = Message.obtain();
                    msg.what = STATE_CONNECTION_FAILED;
                    handler.sendMessage(msg);
                }
                if(socket != null){
                    Message msg = Message.obtain();
                    msg.what = STATE_CONNECTED;
                    handler.sendMessage(msg);
                    sendReceive = new SendReceive(socket);
                    sendReceive.start();
                    break;
                }
            }
        }
    }

    private class ClientClass extends Thread{
        private BluetoothDevice device;
        private BluetoothSocket socket;
        public ClientClass(BluetoothDevice device1){
            device = device1;
            try {
                socket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run(){
            try {
                socket.connect();
                Message msg = Message.obtain();
                msg.what = STATE_CONNECTED;
                handler.sendMessage(msg);
                sendReceive = new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
                Message msg = Message.obtain();
                msg.what = STATE_CONNECTION_FAILED;
                handler.sendMessage(msg);
            }
        }
    }

    private class SendReceive extends Thread{
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendReceive(BluetoothSocket socket){
            bluetoothSocket = socket;
            InputStream tempIn = null;
            OutputStream tempOut = null;

            try {
                tempIn = bluetoothSocket.getInputStream();
                tempOut = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputStream = tempIn;
            outputStream = tempOut;
        }

        public void run(){
            byte[] buffer = new byte[1024];
            int bytes;
            while (true){
                try {
                    bytes = inputStream.read(buffer);
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes){
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

