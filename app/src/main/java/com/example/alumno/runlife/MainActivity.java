package com.example.alumno.runlife;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alumno.runlife.fragmentsEntrenamientos.EntrenamientoDatos;
import com.example.alumno.runlife.fragmentsEntrenamientos.FragmentCarreras;
import com.example.alumno.runlife.fragmentsEntrenamientos.FragmentEntrenamiento;
import com.example.alumno.runlife.fragmentsEntrenamientos.FragmentHistorial;
import com.example.alumno.runlife.fragmentsEntrenamientos.FragmentPortada;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {
    //TAG DEPURACION
    public static final String TAGDEVELOP = "TAGDEVELOP";
    public static final String TAGDEBUG = "TAGDEBUG";

    public static final int RESPUESTAPERMISOS = 1;

    //Cuenta Google del Usuario
    public static GoogleSignInAccount cuentaGoogleUsuario;

    //Datos menu
    private ImageView imageViewPerfilUsuario;
    private TextView textViewNombrePerfilUsuario;
    private TextView textViewEmailPerfilUsuario;

    public static FragmentManager fragmentManager;
    private GoogleApiClient googleApiClient;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Crear primer fragmento y añadirlo
        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayoutMain, new FragmentPortada());
        fragmentTransaction.commit();

        // LOGIN GOOGLE
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        View cabeceraView = navigationView.getHeaderView(0);
        imageViewPerfilUsuario = cabeceraView.findViewById(R.id.imageViewPerfilUsuario);
        textViewNombrePerfilUsuario = cabeceraView.findViewById(R.id.textViewNombrePerfilUsuario);
        textViewEmailPerfilUsuario = cabeceraView.findViewById(R.id.textViewEmailPerfilUsuario);


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            resultadoLogin(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    resultadoLogin(googleSignInResult);
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void resultadoLogin(GoogleSignInResult result) {
        if (result.isSuccess()) {
            cuentaGoogleUsuario = result.getSignInAccount();
            textViewNombrePerfilUsuario.setText(cuentaGoogleUsuario.getDisplayName());
            textViewEmailPerfilUsuario.setText(cuentaGoogleUsuario.getEmail());
        } else {
            volverPantallaLogin();
        }
    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        URL url;
        if ((requestCode == RESPUESTAPERMISOS) && (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)) == PackageManager.PERMISSION_GRANTED) {
            try {
            url = new URL("https://lh5.googleusercontent.com/-8zHRzn1ab2Q/AAAAAAAAAAI/AAAAAAAAMcs/Hvw1DuEyoe4/photo.jpg");
            ImageView imageViewTest = (ImageView) findViewById(R.id.imageView2);
                imageViewTest.setImageBitmap(BitmapFactory.decodeStream(url.openConnection().getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/


    private void volverPantallaLogin() {
        Intent anIntent = new Intent(this, LoginActivity.class);
        startActivity(anIntent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuBar_acercaDe) {
            Intent intent = new Intent(this, AcercaDeActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.menu_portada) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayoutMain, new FragmentPortada());
            fragmentTransaction.commit();
        } else if (id == R.id.menu_entrenamientoLibre) {
            Bundle bundle = new Bundle();
            bundle.putInt(EntrenamientoDatos.ENTRENAMIENTO_TIPO, EntrenamientoDatos.ENTRENAMIENTO_TIPO_LIBRE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FragmentEntrenamiento fragmentEntrenamiento = new FragmentEntrenamiento();
            fragmentEntrenamiento.setArguments(bundle);
            fragmentTransaction.replace(R.id.frameLayoutMain, fragmentEntrenamiento);
            fragmentTransaction.commit();
        } else if (id == R.id.menu_entrenamientoDistancia) {
            Bundle bundle = new Bundle();
            bundle.putInt(EntrenamientoDatos.ENTRENAMIENTO_TIPO, EntrenamientoDatos.ENTRENAMIENTO_TIPO_DISTANCIA);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FragmentEntrenamiento fragmentEntrenamiento = new FragmentEntrenamiento();
            fragmentEntrenamiento.setArguments(bundle);
            fragmentTransaction.replace(R.id.frameLayoutMain, fragmentEntrenamiento);
            fragmentTransaction.commit();
        } else if (id == R.id.menu_historial) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayoutMain, new FragmentHistorial());
            fragmentTransaction.commit();
        } else if (id == R.id.menu_carreras) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayoutMain, new FragmentCarreras());
            fragmentTransaction.commit();
        } else if (id == R.id.menu_ayuda) {
            Intent intent = new Intent(this, TutorialActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_salir) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void logout() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    volverPantallaLogin();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_string) + " " + getResources().getText(R.string.salir_string), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
