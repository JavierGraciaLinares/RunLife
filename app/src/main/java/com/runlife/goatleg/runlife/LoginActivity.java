package com.runlife.goatleg.runlife;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.runlife.goatleg.runlife.herramientas.Animaciones;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int LOGINGOOGLE = 666;

    private LinearLayout linearLayoutLoginDown, linearLayoutLoginUp;

    private GoogleApiClient googleApiClient;

    private SignInButton botonLoginGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Animacion Wellcome
        linearLayoutLoginDown = (LinearLayout) findViewById(R.id.linearLayoutLoginDown);
        linearLayoutLoginUp = (LinearLayout) findViewById(R.id.linearLayoutLoginUp);
        Animaciones.descenderElemento(this, linearLayoutLoginUp);
        Animaciones.ascenderElemento(this, linearLayoutLoginDown);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        botonLoginGoogle = (SignInButton) findViewById(R.id.botonLoginGoogle);
        botonLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent anIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(anIntent, LOGINGOOGLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGINGOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            resultadoLoginGoogle(result);
        }
    }

    private void resultadoLoginGoogle(GoogleSignInResult result) {
        if (result.isSuccess()) {
            Intent anIntent = new Intent(this, MainActivity.class);
            anIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //Flag activity para no volver a esta ventana pulsando volver
            startActivity(anIntent);
        } else {
            Toast.makeText(this, getResources().getText(R.string.error_string), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, getResources().getText(R.string.error_string), Toast.LENGTH_LONG).show();
    }
}
