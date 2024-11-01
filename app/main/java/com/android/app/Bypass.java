package com.android.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Bypass extends Application implements InvocationHandler {
    private static final int GET_SIGNATURES = 64;
    private String appPkgName = "";
    private Object base;
    private byte[][] sign;

    private static String a(byte[] bArr) {
        return new String(bArr);
    }

    @Override // android.content.ContextWrapper
    protected void attachBaseContext(Context context) {
        try {
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(Base64.decode("AQAAArEwggKtMIICFqADAgECAgRMwTx6MA0GCSqGSIb3DQEBBQUAMIGaMQswCQYDVQQGEwJqcDEOMAwGA1UECBMFVG9reW8xFTATBgNVBAcTDFNoaW5hZ2F3YS1rdTEgMB4GA1UEChMXTkFNQ08gQkFOREFJIEdhbWVzIEluYy4xIDAeBgNVBAsTF05BTUNPIEJBTkRBSSBHYW1lcyBJbmMuMSAwHgYDVQQDExdOQU1DTyBCQU5EQUkgR2FtZXMgSW5jLjAeFw0xMDEwMjIwNzI1NDZaFw0zODAzMDkwNzI1NDZaMIGaMQswCQYDVQQGEwJqcDEOMAwGA1UECBMFVG9reW8xFTATBgNVBAcTDFNoaW5hZ2F3YS1rdTEgMB4GA1UEChMXTkFNQ08gQkFOREFJIEdhbWVzIEluYy4xIDAeBgNVBAsTF05BTUNPIEJBTkRBSSBHYW1lcyBJbmMuMSAwHgYDVQQDExdOQU1DTyBCQU5EQUkgR2FtZXMgSW5jLjCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEA23Ciox9qE3cwZAkhZhNgk74tXHMx91pyMplboV/0l/RsHLGvh8+NBiNnPZlA731M0R7kHASonFk0IRXFfA6OSUxXPPi7c2qQbEWxYysskPsrO1A+CcbTBjKZqAox6e+4Vt1g9dbv9KrKc+TkyN3qlXm0FXFW0ERROfZoJMn49rMCAwEAATANBgkqhkiG9w0BAQUFAAOBgQCb2h0a8gXSfLNU9ekxCS6LsT8Pd45rbH5ZArXjFUfMRmSMXqkdnGFLFoA/WVHuSwdFZB+lqK3NYvbOYjwacAi8iuaf4t7QAmXVZyjxUzNwGEO91tiO7b3jooIjdBtl5RuYdhJhrm+X5elwTuSib0s3qI6R9vwy6o7ruZYcHAkKgg==", 0)));
            byte[][] bArr = new byte[][]{new byte[dataInputStream.read() & 255]};
            for (int i = 0; i < bArr.length; i++) {
                bArr[i] = new byte[dataInputStream.readInt()];
                dataInputStream.readFully(bArr[i]);
            }
            Class<?> cls = Class.forName(a(new byte[]{97, 110, 100, 114, 111, 105, 100, 46, 97, 112, 112, 46, 65, 99, 116, 105, 118, 105, 116, 121, 84, 104, 114, 101, 97, 100}));
            Object invoke = cls.getDeclaredMethod(a(new byte[]{99, 117, 114, 114, 101, 110, 116, 65, 99, 116, 105, 118, 105, 116, 121, 84, 104, 114, 101, 97, 100}), new Class[0]).invoke(null, new Object[0]);
            Field declaredField = cls.getDeclaredField(a(new byte[]{115, 80, 97, 99, 107, 97, 103, 101, 77, 97, 110, 97, 103, 101, 114}));
            declaredField.setAccessible(true);
            Object obj = declaredField.get(invoke);
            Class<?> cls2 = Class.forName(a(new byte[]{97, 110, 100, 114, 111, 105, 100, 46, 99, 111, 110, 116, 101, 110, 116, 46, 112, 109, 46, 73, 80, 97, 99, 107, 97, 103, 101, 77, 97, 110, 97, 103, 101, 114}));
            this.base = obj;
            this.sign = bArr;
            this.appPkgName = context.getPackageName();
            Object newProxyInstance = Proxy.newProxyInstance(cls2.getClassLoader(), new Class[]{cls2}, this);
            declaredField.set(invoke, newProxyInstance);
            PackageManager packageManager = context.getPackageManager();
            Field declaredField2 = packageManager.getClass().getDeclaredField(a(new byte[]{109, 80, 77}));
            declaredField2.setAccessible(true);
            declaredField2.set(packageManager, newProxyInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.attachBaseContext(context);
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
        if (a(new byte[]{103, 101, 116, 80, 97, 99, 107, 97, 103, 101, 73, 110, 102, 111}).equals(method.getName())) {
            String str = (String) objArr[0];
            if ((Long.valueOf(Long.parseLong(String.valueOf(objArr[1]))).longValue() & 64) != 0 && this.appPkgName.equals(str)) {
                PackageInfo packageInfo = (PackageInfo) method.invoke(this.base, objArr);
                packageInfo.signatures = new Signature[this.sign.length];
                for (int i = 0; i < packageInfo.signatures.length; i++) {
                    packageInfo.signatures[i] = new Signature(this.sign[i]);
                }
                return packageInfo;
            }
        }
        return method.invoke(this.base, objArr);
    }
}

