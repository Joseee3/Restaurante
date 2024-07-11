package com.example.restaurante;

import androidx.appcompat.app.AppCompatActivity;


public class ContactFormActivity extends AppCompatActivity {

//    private static final int CAMERA_REQUEST_CODE = 1001;
//    private Button btnTakePhoto;
//    private Button btnSelectPhoto;
//
//    private ImageView ivPhoto;
//
//    private RecyclerView mRvContacts;
//    private FloatingActionButton mbtnCreateContact;
//
//    private PlatosItemAdapter mAdapter;
//    private List<Platos> mData = new ArrayList<>();
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_contact_form);
//        setUpRecyclerView();
//
//
//        Button btnGuardar = findViewById(R.id.btnGuardar);
//        EditText edtName = findViewById(R.id.edtName);
//        EditText edtLastName = findViewById(R.id.edtLastName);
//        EditText edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
//
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://662f1f5643b6a7dce30e72cc.mockapi.io/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        // Instanciar IContactService
//        IPlatosService service =  retrofit.create(IPlatosService.class);
//        btnGuardar.setOnClickListener(view -> {
//
//            Platos platos = new Platos();
//            platos.name = edtName.getText().toString();
//            platos.categoria = edtLastName.getText().toString();
//            platos.price = edtPhoneNumber.getText().toString();
//
//            service.create(platos).enqueue(new Callback<Platos>() {
//                @Override
//                public void onResponse(Call<Platos> call, Response<Platos> response) {
//                    if (response.isSuccessful()) {
//                        Toast.makeText(ContactFormActivity.this, "Se creo contacto correctamente", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Platos> call, Throwable throwable) {
//
//                }
//            });
//        });
//
//    }
//
//
//    private void setUpRecyclerView() {
//        mRvContacts = findViewById(R.id.rvCartItems);
//        mRvContacts.setLayoutManager(new LinearLayoutManager(this));
//        // configurar RV y agregar elementos en la lista
//        mAdapter = new PlatosItemAdapter(mData);
//        mRvContacts.setAdapter(mAdapter); //Me permite indicar que elementos debe mostrar el RV
//
//        mRvContacts.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//
//            }
//        });
//    }


}