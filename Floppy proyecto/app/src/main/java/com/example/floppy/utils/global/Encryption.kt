package com.example.floppy.utils.global

import android.util.Base64
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class Encryption {
    companion object{
        fun String.encrypt(): String {
            val secretKey = generateKey("Hola")
            val cipher = Cipher.getInstance("AES")

            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val dataEncryption = cipher.doFinal(this.toByteArray())
            return Base64.encodeToString(dataEncryption, Base64.DEFAULT)
        }
        fun generateKey(pass : String) : SecretKeySpec {
            val sha = MessageDigest.getInstance("SHA-256")
            var key = pass.toByteArray(charset("UTF-8"))
            key = sha.digest(key)
            return SecretKeySpec(key,"AES")

        }
        fun String.decrypt() : String {
            val secretKey = generateKey("Hola")
            val cipher = Cipher.getInstance("AES")

            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            val dataDecrypt = Base64.decode(this,Base64.DEFAULT)
            val dataDecryptByte = cipher.doFinal(dataDecrypt)
            return String(dataDecryptByte)
        }
    }

}