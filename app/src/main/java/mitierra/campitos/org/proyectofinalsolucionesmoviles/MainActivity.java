package mitierra.campitos.org.proyectofinalsolucionesmoviles;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends ActionBarActivity {
private String url="http://10.0.2.2:9000/usuario";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button boton= (Button) findViewById(R.id.button);
        Button botonGuardar=(Button)findViewById(R.id.botonGuardar);
        Button botonLeerTodos=(Button)findViewById(R.id.botonTodos);
        Button botonActualizar=(Button)findViewById(R.id.botonActualizar);
      final  TextView texto= (TextView) findViewById(R.id.texto);
      final TextView textoGuardar= (TextView)findViewById(R.id.textoGuardar);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TareaAsincronicaUsuario tarea=new TareaAsincronicaUsuario();
                tarea.execute(null,null,null);



            }
        });
        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               TareaAsincronicaGuardarUsuario tareaAsincronicaGuardarUsuario=new TareaAsincronicaGuardarUsuario();
                tareaAsincronicaGuardarUsuario.execute(null,null,null);
            }
        });
        botonLeerTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TareaAsincronciaObtenerTodosLosUsuarios tareaAsincronciaObtenerTodosLosUsuarios =new TareaAsincronciaObtenerTodosLosUsuarios();
                tareaAsincronciaObtenerTodosLosUsuarios.execute(null,null,null);
            }
        });
        botonActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TareaAsincronicaActualizar tareaAsincronicaActualizar=new TareaAsincronicaActualizar();
                tareaAsincronicaActualizar.execute(null,null,null);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
/**************************************************************************************************************************************
CLASE PARA RECIBIR UN MENSAJE SIMPLE GET
 *************************************************************************************************************************************/
    class TareaAsincronicaUsuario extends AsyncTask<String, Integer, Integer> {


        private String mensaje;

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        @Override


        protected Integer doInBackground(String... params) {
            try{
                setMensaje(leerMensaje("algo"));
                System.out.println("<<<<<<<<<<<<<<<<<"+mensaje);


            }catch(Exception e){
                mensaje=e.getMessage();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer i){

            try {

                System.out.println("<<<<<<<<<<<<<<<<<<<<<<<Holaaa");
                TextView texto= (TextView) findViewById(R.id.texto);
                texto.setText(getMensaje());

            }catch (Exception e){
                mensaje=e.getMessage();
            }
        }
/*
Metodo para recibir un mensaje simple, en texto/html desde el controlor con dicho header
 */
        public String leerMensaje(String miurl)throws Exception{
            String leido="nada se leyo  :(";

// Cremos una instancia de restemplate
            RestTemplate restTemplate = new RestTemplate();

// Agregamos un convertidor de mensaje
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

// Hacemos el http simple
            leido = restTemplate.getForObject("http://10.0.2.2:9000/mensaje", String.class, "no sirve");



            return leido;

        }

    }
/*****************************************************************************************
CLASE PARA HACER POST
 *******************************************************************************************/
    class TareaAsincronicaGuardarUsuario extends AsyncTask<String, Integer, Integer>{
String mensaje="nada";
        @Override
        protected Integer doInBackground(String... params) {
            try {
              mensaje=  guardarUsuario();
                System.out.println("<<<<<<<<<<<<<<<<<<<<<<SE GUARDO BIEN");
            } catch (Exception e) {
              System.out.println("Hubo problemas "+e.getMessage());
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer i){

            try {

                System.out.println("<<<<<<<<<<<<<<<<<<<<<<<Holaaa");
                TextView texto= (TextView) findViewById(R.id.textoGuardar);
                texto.setText(mensaje);


            }catch (Exception e){
               System.out.println(">>>>>>>>>>>>>>>>Algo malo sucedio "+e.getMessage());
            }
        }

        /*
Metodo para enviar un json a un controller que acxepte dicho json
 */
        public String guardarUsuario()throws Exception{
// Creamos un objeto simple
      EditText editNombre=      (EditText)findViewById(R.id.editNombre);
            String nombre=editNombre.getText().toString();
      EditText editEdad= (EditText)findViewById(R.id.editEdad);
            int edad=Integer.parseInt(editEdad.getText().toString());
      EditText editSueldo=(EditText)findViewById(R.id.editSueldo);
            float sueldo=Float.parseFloat(editSueldo.getText().toString());

            Usuario u=new Usuario();
            u.setEdad(edad);
            u.setNombre(nombre);
            u.setSueldo(sueldo);

// Ajustamos el content type acorde, en ese caso json
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application","json"));
            HttpEntity<Usuario> requestEntity = new HttpEntity<Usuario>(u, requestHeaders);

// Creamos una nueva instancia de RestTemplate
            RestTemplate restTemplate = new RestTemplate();

// Agregamos los conertidores de jackson
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

// Hacemos el metodo post y obtenemos nuestra respuesta
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            String respuesta = responseEntity.getBody();


            return respuesta;
        }
    }
/***************************************************************************************************************************************
CLASE PARA OBTENER TODOS LOS USUARIOS CON GET
 ****************************************************************************************************************************************/

    class TareaAsincronciaObtenerTodosLosUsuarios extends AsyncTask<String, Integer, Integer>{
ArrayList<Usuario> usuarios=new ArrayList<Usuario>();
    @Override
    protected Integer doInBackground(String... params) {

        try{
           usuarios=leerTodos();
        }catch(Exception e){
        System.out.println("<<<<<<<<<<<<<<<<< ALGO MALO PASOOO"+e.getMessage());
        }
        return 0;
    }
    @Override
    public void onPostExecute(Integer i){
        try{
            System.out.println("ahhhh<<<<<<<<<<<<<<<"+usuarios.size());
         TextView textView= (TextView) findViewById(R.id.textoTodos);
            textView.setText("SE encontraron:"+usuarios.size()+" registros");
        }catch(Exception e){
            System.out.println("ERROORRRRRRRRRR"+  e.getMessage());
        }
    }

    public ArrayList<Usuario> leerTodos()throws Exception{
        String leido="nada se leyo  :(";

        HttpHeaders requestHeaders=new HttpHeaders();
        requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));

        HttpEntity<?> requestEntity=new HttpEntity<Object>(requestHeaders);



        RestTemplate restTemplate=new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        ResponseEntity<String> responseEntity=restTemplate.exchange(url, HttpMethod.GET,requestEntity, String.class);
        leido=responseEntity.getBody();

        ObjectMapper mapper=new ObjectMapper();
        ArrayList<Usuario> usuarios=mapper.readValue(leido, new TypeReference<ArrayList<Usuario>>(){});

        return usuarios;

    }
}

    /******************************************************************************************************************************************
    CLASE PARA HACER UN UPDATE
     **************************************************************************************************************************************/
    class TareaAsincronicaActualizar extends AsyncTask<String, Integer, Integer>{

        @Override
        protected Integer doInBackground(String... params){
            try{
                actualizarUsuario();
            }catch(Exception e){

            }
            return 0;
        }

        @Override
        public void onPostExecute(Integer i){
            try{
                System.out.println("ahhhhctualizacionnn<<<<<<<<<<<<<<<");
                TextView textView= (TextView) findViewById(R.id.textoTodos);
                textView.setText("SE actualiso este usuario con  exito");
            }catch(Exception e){
                System.out.println("ERROORRRRRRRRRR"+  e.getMessage());
            }
        }

        public String actualizarUsuario()throws Exception{
// Creamos un objeto simple
            EditText editNombre=      (EditText)findViewById(R.id.editNombre);
            String nombre=editNombre.getText().toString();
            EditText editEdad= (EditText)findViewById(R.id.editEdad);
            int edad=Integer.parseInt(editEdad.getText().toString());
            EditText editSueldo=(EditText)findViewById(R.id.editSueldo);
            float sueldo=Float.parseFloat(editSueldo.getText().toString());

            Usuario u=new Usuario();
            u.setEdad(edad);
            u.setNombre(nombre);
            u.setSueldo(sueldo);
            u.setIdUsuario(3);

// Ajustamos el content type acorde, en ese caso json
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application","json"));
            HttpEntity<Usuario> requestEntity = new HttpEntity<Usuario>(u, requestHeaders);

// Creamos una nueva instancia de RestTemplate
            RestTemplate restTemplate = new RestTemplate();

// Agregamos los conertidores de jackson
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

// Hacemos el metodo post y obtenemos nuestra respuesta
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
            String respuesta = responseEntity.getBody();


            return respuesta;
        }
    }
}
