package kg.bakai.repotestapp.presentation.login

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kg.bakai.repotestapp.presentation.destinations.SearchScreenDestination
import kg.bakai.repotestapp.util.GITHUB_CLIENT_ID
import kg.bakai.repotestapp.util.GITHUB_CLIENT_SECRET
import net.openid.appauth.*

@RootNavGraph(start = true)
@Destination
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
) {
    val context = LocalContext.current
    val service = AuthorizationService(context)
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == ComponentActivity.RESULT_OK) {
            val ex = AuthorizationException.fromIntent(it.data!!)
            val result = AuthorizationResponse.fromIntent(it.data!!)

            if (ex != null){
                Log.e("Github Auth", "launcher: $ex")
            } else {
                val secret = ClientSecretBasic(GITHUB_CLIENT_SECRET)
                val tokenRequest = result?.createTokenExchangeRequest()

                service.performTokenRequest(tokenRequest!!, secret) {res, exception ->
                    if (exception != null){
                        Log.e("Github Auth", "launcher: ${exception.error}" )
                    } else {
                        val token = res?.accessToken
                        Log.i("Token", token!!)
                        navigator.navigate(SearchScreenDestination(token = token))
                    }
                }
            }
        }
    }
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = { githubAuth(
            service, launcher
        ) }) {
            Text(text = "Login with Github")
        }
    }
}

private fun githubAuth(
    service: AuthorizationService,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>

) {
    val redirectUri = Uri.parse("bakai://login")

    val authorizeUri = Uri.parse("https://github.com/login/oauth/authorize")
    val tokenUri = Uri.parse("https://github.com/login/oauth/access_token")

    val config = AuthorizationServiceConfiguration(authorizeUri, tokenUri)

    val request = AuthorizationRequest
        .Builder(config, GITHUB_CLIENT_ID, ResponseTypeValues.CODE, redirectUri)
        .setScopes("user repo")
        .build()

    val intent = service.getAuthorizationRequestIntent(request)
    launcher.launch(intent)
}
