package com.vf.dev.msuniversidadusuarios.utils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GeneradorDeClaves {


    private static enum TipoCaracterEnum {
        Minuscula, Mayuscula, Simbolo, Numero
    }

    private static int porcentajeMayusculas;
    private static int porcentajeSimbolos;
    private static int porcentajeNumeros;
    private static int longitudPassword;
    private static Random semilla;


    private static String caracteres = "abcdefghijkmnpqrstuvwxyz";
    private static String numeros = "0123456789";
    private static String simbolos = "!+-=&()";


    private static StringBuilder password;

    public GeneradorDeClaves() {
        Date d = new Date();
        semilla = new Random(d.getTime());
    }



    /**
     *
     * @param _longitudCaracteres Numero de caracteres que tendra la clave
     * @param _porcentajeMayusculas Numero del 0 al 100 para representar el porcentaje de Mayusculas
     * @param _porcentajeSimbolos Numero del 0 al 100 para representar el porcentaje de Simbolos
     * @param _porcentajeNumeros Numero del 0 al 100 para representar el porcentaje de Numeros
     * @return Devuelve una clave aleatoria con las caraceristicas especificadas
     */
    public static String generar(int _longitudCaracteres, int _porcentajeMayusculas, int _porcentajeSimbolos,
                                 int _porcentajeNumeros) throws Exception {
        longitudPassword = _longitudCaracteres;
        porcentajeMayusculas = _porcentajeMayusculas;
        porcentajeSimbolos = _porcentajeSimbolos;
        porcentajeNumeros = _porcentajeNumeros;

        if (porcentajeMayusculas + porcentajeSimbolos + porcentajeNumeros > 100) {
            throw new Exception("Erro en porcentájes");
        }

        Date d = new Date();

        semilla = new Random(d.getTime());

        GeneraPassword();
        return password.toString();
    }


    public static void SetCaracteresEspeciales(int numeroCaracteresMayuscula, int numeroCaracteresNumericos,
                                               int numeroCaracteresSimbolos) throws Exception {

        if (numeroCaracteresMayuscula + numeroCaracteresNumericos + numeroCaracteresSimbolos > longitudPassword) {
            throw new Exception("El número de caracteres especiales no puede superar la longitud del password");
        }

        porcentajeMayusculas = numeroCaracteresMayuscula * 100 / longitudPassword;
        porcentajeNumeros = numeroCaracteresNumericos * 100 / longitudPassword;
        porcentajeSimbolos = numeroCaracteresSimbolos * 100 / longitudPassword;
    }


    private static void GeneraPassword() {


        password = new StringBuilder(longitudPassword);
        for (int i = 0; i < longitudPassword; i++) {
            password.append(GetCaracterAleatorio(TipoCaracterEnum.Minuscula));
        }


        int numMayusculas = (int) (longitudPassword * (porcentajeMayusculas / 100d));
        int numSimbolos = (int) (longitudPassword * (porcentajeSimbolos / 100d));
        int numNumeros = (int) (longitudPassword * (porcentajeNumeros / 100d));


        Integer[] caracteresEspeciales = GetPosicionesCaracteresEspeciales(numMayusculas + numSimbolos + numNumeros);
        int posicionInicial = 0;
        int posicionFinal = 0;


        posicionFinal += numMayusculas;
        ReemplazaCaracteresEspeciales(caracteresEspeciales, posicionInicial, posicionFinal, TipoCaracterEnum.Mayuscula);


        posicionInicial = posicionFinal;
        posicionFinal += numSimbolos;
        ReemplazaCaracteresEspeciales(caracteresEspeciales, posicionInicial, posicionFinal, TipoCaracterEnum.Simbolo);


        posicionInicial = posicionFinal;
        posicionFinal += numNumeros;
        ReemplazaCaracteresEspeciales(caracteresEspeciales, posicionInicial, posicionFinal, TipoCaracterEnum.Numero);
    }




    private static void ReemplazaCaracteresEspeciales(Integer[] posiciones, int posicionInicial, int posicionFinal,
                                                      TipoCaracterEnum tipoCaracter) {
        for (Integer i = posicionInicial; i < posicionFinal; i++) {
            int j = posiciones[i];
            password.setCharAt(j, GetCaracterAleatorio(tipoCaracter));
        }
    }










    private static Integer[] GetPosicionesCaracteresEspeciales(int numeroPosiciones) {

        List<Integer> lista = new ArrayList<>();
        while (lista.size() < numeroPosiciones) {
            Integer posicion = semilla.nextInt(longitudPassword);
            if (!lista.contains(posicion)) {
                lista.add(posicion);
            }
        }
        Integer[] arr = new Integer[lista.size()];
        arr = lista.toArray(arr);
        return arr;
    }




    private static char GetCaracterAleatorio(TipoCaracterEnum tipoCaracter) {
        String juegoCaracteres;
        switch (tipoCaracter) {
            case Mayuscula:
                juegoCaracteres = caracteres.toUpperCase();
                break;
            case Minuscula:
                juegoCaracteres = caracteres.toLowerCase();
                break;
            case Numero:
                juegoCaracteres = numeros;
                break;
            default:
                juegoCaracteres = simbolos;
                break;
        }


        int longitudJuegoCaracteres = juegoCaracteres.length();


        int numeroAleatorio = semilla.nextInt(longitudJuegoCaracteres);


        char c = juegoCaracteres.charAt(numeroAleatorio);
        return c;
    }
}

