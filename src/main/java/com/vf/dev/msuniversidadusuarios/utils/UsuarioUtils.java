package com.vf.dev.msuniversidadusuarios.utils;

import com.vf.dev.msuniversidadusuarios.model.entity.UsuarioEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class UsuarioUtils {

    public static String generarFolio(UsuarioEntity pUsuarioEntity) {
        SimpleDateFormat mSimpleDateFormatAnio = new SimpleDateFormat("yyyy");
        SimpleDateFormat mSimpleDateFormatMes = new SimpleDateFormat("MM");


        return null;
    }

    public static String generateMatricula() {
        return null;
    }

    public static Date generateRandomDate() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, getRamndomNumberRange(1980, 2000));
        mCalendar.set(Calendar.MONTH, getRamndomNumberRange(1, 12));
        mCalendar.set(Calendar.DAY_OF_MONTH, getRamndomNumberRange(1, 28));
        return mCalendar.getTime();
    }


    public static int getRamndomNumberRange(int min, int max) {
        Random mRandom = new Random();
        return mRandom.nextInt((max-1 - min) + 1) + min;
    }

    public static int getEdad(Date pFechaNacimiento) {
        Calendar mCalendar = Calendar.getInstance();
        Calendar mCalendarDate = Calendar.getInstance();
        mCalendarDate.setTime(pFechaNacimiento);
        return mCalendar.get(Calendar.YEAR) - mCalendarDate.get(Calendar.YEAR);
    }

    public static String getCurp(String nombre, String aPaterno, String aMaterno, Date pFechaNacimiento, String claveEstado, String aleatorio, int count) {
        String mStrCurp = "";
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("YYYYMMDD");
        mStrCurp = mStrCurp.concat(aPaterno.trim().substring(0, 1));
        mStrCurp = mStrCurp.concat(aMaterno.substring(0,1));
        mStrCurp = mStrCurp.concat(nombre.substring(0,1));
        mStrCurp = mStrCurp.concat(mSimpleDateFormat.format(pFechaNacimiento));
        mStrCurp = mStrCurp.concat(claveEstado);
        mStrCurp = mStrCurp.concat(aleatorio);
        mStrCurp = mStrCurp.concat(String.valueOf(count));
       mStrCurp = mStrCurp.toUpperCase().replace(" ","");
        return mStrCurp.trim();
    }


}
