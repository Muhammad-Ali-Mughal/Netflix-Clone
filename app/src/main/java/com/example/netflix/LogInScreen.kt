package com.example.netflix

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class LogInManager(private val context: Context) {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN: Int = 1

    private val storageRef = FirebaseStorage.getInstance().reference

    init {
        FirebaseApp.initializeApp(context)
        val defaultWebClientId = context.getString(R.string.default_web_client_id)
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(defaultWebClientId)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)
        auth = FirebaseAuth.getInstance()
    }

    fun signIn(activity: ComponentActivity) {
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun performGoogleSignIn(activity: Activity, onSuccess: () -> Unit) {
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
        // onSuccess() - This will be triggered in onActivityResult
    }

    fun firebaseAuthWithGoogle(
        account: GoogleSignInAccount,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
        onNavigateToSignUp: () -> Unit,
        onNavigateToHome: () -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                    onNavigateToHome()
                } else {
                    onFailure(task.exception?.message ?: "Login Failed")
                    onNavigateToSignUp()
                }
            }
    }

    fun login(email: String, password: String, onNavigateToHome: () -> Unit, callback: (String) -> Unit) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onNavigateToHome()
                    } else {
                        callback.invoke(task.exception?.message ?: "Log-In Failed")
                    }
                }
        } else {
            callback.invoke("Email or password is empty")
        }
    }

    fun uploadProfilePicture(
        imageUri: Uri,
        callback: (String) -> Unit,
    ) {
        val profilePicRef = storageRef.child("profile_pictures/${UUID.randomUUID()}")

        profilePicRef.putFile(imageUri)
            .addOnSuccessListener {
                profilePicRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        callback(uri.toString())
                        Toast.makeText(context, "Profile picture uploaded successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { exception ->
                        Log.e("LogInManager", "Error getting download URL", exception)
                    }
            }
            .addOnFailureListener { exception ->
                Log.e("LogInManager", "Error uploading profile picture", exception)
            }
    }
}

@Composable
fun LogInScreen(
    onNavigateToSignUp: () -> Unit,
    onNavigateToHome: () -> Unit,
    message: String?,
    loginManager: LogInManager
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val activity = LocalContext.current as Activity

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Log In",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text = "Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        loginManager.login(email, password, onNavigateToHome) { message ->
                            "Logging In..."
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Log In")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Create new account",
                    modifier = Modifier
                        .clickable { onNavigateToSignUp() }
                        .padding(16.dp),
                    fontSize = 16.sp,
                    color = Color.Red
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        loginManager.performGoogleSignIn(activity = activity) {
                            onNavigateToHome()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color.Black),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.icons8_google),
                            contentDescription = "Google Logo",
                            modifier = Modifier.size(24.dp),
                            alignment = Alignment.CenterStart
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sign in with Google",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                message?.let {
                    Text(
                        text = it,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}
