/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

/**
 * Permite cifrar una cadena
 *
 * @author Álex Torres
 */
public class Contrasenya {

    public static String cifrarContrasenya(String contrasenya) throws NoSuchAlgorithmException {
        MessageDigest md;
        md = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_512);
        md.update(contrasenya.getBytes());

        return new String(Base64.encodeBase64(md.digest()));
    }
}
