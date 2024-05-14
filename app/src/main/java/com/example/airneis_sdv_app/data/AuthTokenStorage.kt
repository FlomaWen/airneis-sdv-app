import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import javax.net.ssl.HttpsURLConnection

fun provideEncryptedSharedPreferences(context: Context): SharedPreferences {
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    return EncryptedSharedPreferences.create(
        "secure_prefs_file",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}

fun saveTokens(context: Context, accessToken: String, refreshToken: String) {
    val prefs = provideEncryptedSharedPreferences(context)
    prefs.edit().putString("accessToken", accessToken).putString("refreshToken", refreshToken).apply()
}

fun isUserLoggedIn(context: Context): Boolean {
    val prefs = provideEncryptedSharedPreferences(context)
    val accessToken = prefs.getString("accessToken", null)
    return !accessToken.isNullOrEmpty()
}

