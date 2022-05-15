//package com.android.foodorderapp.extras;
//
//public class Broadcast extends AppCompatActivity {
//
//            private Broadcast broadcast;
//
//            @Override
//            protected void onCreate(Bundle savedInstanceState) {
//                super.onCreate(savedInstanceState);
//                setContentView(R.layout.activity_main);
//                broadcast = new Broadcast();
//                IntentFilter filter = new IntentFilter("android.intent.action.AIRPLANE_MODE");
//                registerReceiver(broadcast, filter);
//            }
//
//            @Override
//            protected void onStop() {
//                super.onStop();
//                unregisterReceiver(broadcast);
//            }
//        }
