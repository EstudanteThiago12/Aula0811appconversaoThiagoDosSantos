package com.example.aula_08_11_app_conversao_thiagodossantos;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aula_08_11_app_conversao_thiagodossantos.Api.ApiCallback;
import com.example.aula_08_11_app_conversao_thiagodossantos.Api.AwesomeClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.util.Currency;

public class MainActivity extends AppCompatActivity {

    String opcaoSelecionada = "";
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        RadioGroup rg = findViewById(R.id.Opcoes);

        rg.setOnCheckedChangeListener((RadioGroup, i) -> {
            RadioButton rb = findViewById(i);
            switch (rb.getText().toString()) {
                case "Real -> Dollar":
                    opcaoSelecionada = "BRL-USD";
                    break;
                default:
                    break;

            }
        });

        Button bt = findViewById(R.id.Converter);
        bt.setOnClickListener(view -> converter());

    }

    private void converter() {
        if (opcaoSelecionada.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Selecione uma opção!", Toast.LENGTH_LONG).show();
            return;
        }

        EditText valorEt = findViewById(R.id.CampoConversao);
        String valor$ = valorEt.getText().toString();
        if (valor$.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Digite um valor!", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            AwesomeClient awesomeClient = new AwesomeClient();

            awesomeClient.buscarDados(opcaoSelecionada, new ApiCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    CurrencyRate currencyRate = null;
                    JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
                    if(jsonObject.has("BRLUSD")){
                        currencyRate = gson.fromJson(jsonObject.get("BRLUSD"), CurrencyRate.class);
                    }else{
                        Toast.makeText(getApplicationContext(),"Nao foi possivel encontrar o valor da Moeda", Toast.LENGTH_SHORT).show();
                    }

                    if (currencyRate != null){
                        Double valor = Double.valueOf(valor$);

                        TextView resultado = findViewById(R.id.Resultado);
                        Double valorMoeda = Double.valueOf(currencyRate.getHigh());
                        resultado.setText(String.valueOf(valor * valorMoeda));
                    }

                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getApplicationContext(),error, Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e){
            Log.e("Erro", "Ocorreu um erro", e);
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}


